package kr.co.exsoft.common.controller;

import java.io.File;
import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.HashMap;
import java.util.UUID;
import java.util.List;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springmodules.validation.commons.DefaultBeanValidator;
import org.springframework.web.multipart.MultipartFile;

import kr.co.exsoft.common.service.CommonService;
import kr.co.exsoft.user.service.UserService;
import kr.co.exsoft.common.vo.SessionVO;
import kr.co.exsoft.document.vo.PageVO;
import kr.co.exsoft.eframework.configuration.Constant;
import kr.co.exsoft.eframework.exception.BizException;
import kr.co.exsoft.eframework.library.DownloadView;
import kr.co.exsoft.eframework.library.ExcelFileView;
import kr.co.exsoft.eframework.library.LocaleLibrary;
import kr.co.exsoft.eframework.repository.EXrepClient;
import kr.co.exsoft.eframework.util.CommonUtil;
import kr.co.exsoft.eframework.util.ConfigData;
import kr.co.exsoft.eframework.util.MailSendUtil;
import kr.co.exsoft.eframework.util.StringUtil;
import kr.co.exsoft.eframework.util.UtilFileApp;

import org.springframework.web.servlet.ModelAndView;

import com.exsoft.net.SizedInputStream;

/**
 * 메뉴/코드 관련 컨트롤러
 * @author 패키지 개발팀
 * @since 2014.07.17
 * @version 3.0
 *
 */
@Controller
@RequestMapping("/common")
@SessionAttributes("sessionVO")
public class CommonAuthController {

	@Autowired
	private CommonService commonService;
	
	@Autowired
	private CommonService commonCustomService;						// 사이트 커스터마이징 비지니스 로직예
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private MessageSource messageSource;
	
    @Autowired
    private DefaultBeanValidator beanValidator;
    
	protected static final Log logger = LogFactory.getLog(CommonAuthController.class);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 문서등록 - 첨부파일 업로드 처리
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : fileUpload
	 * @param model
	 * @param request
	 * @param map
	 * @param report
	 * @return Map<String,Object>
	 */
	@RequestMapping(value="/fileUpload.do", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> fileUpload(Model model,HttpServletRequest request,@ModelAttribute SessionVO sessionVO,
			@RequestParam Map<String, String> map,@RequestParam("wFiles") MultipartFile report) {
		
		Locale locale = LocaleLibrary.setLocale(sessionVO.getSessLanguage() != null  ? sessionVO.getSessLanguage() : ConfigData.getString("LANGUAGE"));
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		String orgFileName = "";
		String errUUID = UUID.randomUUID().toString();
		long fileSize = 0L;
		
		EXrepClient eXrepClient = new EXrepClient();
		
		try {
			
			if(!report.isEmpty())	{

				// 업로드 파일명
				orgFileName = report.getOriginalFilename();	
				fileSize = report.getSize();
				
				
				eXrepClient.connect();
				String contentPath = CommonUtil.getContentPathByDate(ConfigData.getString(Constant.EXREP_ROOT_EDMS_NM))+UUID.randomUUID().toString();
				
				if(eXrepClient.putFile(new SizedInputStream(report.getInputStream(), fileSize),ConfigData.getString(Constant.EXREP_VOLUME_NM), contentPath,true)) {										
					// 성공
					resultMap.put("result", "success");
				}else{
					//실패
					resultMap.put("result", "fail");
					resultMap.put("message",messageSource.getMessage("file.upload.fail",new Object[0],locale));
					
					logger.error("["+errUUID+"][fileName] : " + orgFileName);
					logger.error("["+errUUID+"] : "+messageSource.getMessage("file.upload.fail",new Object[0],locale));
					throw new Exception();
				}
				
				resultMap.put("orgFile", orgFileName);
				resultMap.put("contentPath",contentPath);
				resultMap.put("volumeId", CommonUtil.getContentPathByDate(ConfigData.getString(Constant.EXREP_ROOT_EDMS_NM)));
				resultMap.put("fileSize",fileSize);
				
			}else {
				resultMap.put("result", "fail");
				resultMap.put("message",messageSource.getMessage("file.upload.empty.fail",new Object[0],locale));

				logger.error("["+errUUID+"][fileName] : " + orgFileName);
				logger.error("["+errUUID+"] : "+messageSource.getMessage("file.upload.empty.fail",new Object[0],locale));
				throw new Exception();
			}
			
		}catch(Exception e) {
			logger.error("["+errUUID+"][fileName] : " + orgFileName);
			logger.error("["+errUUID+"] : "+e.getMessage());
			
			resultMap.put("result", "fail");
			resultMap.put("message",messageSource.getMessage("file.upload.exception",new Object[0],locale));
		}finally{
			try{
				eXrepClient.disconnect();
			}catch(Exception e){
				logger.error("["+errUUID+"][disconnect err]");
				logger.error("["+errUUID+"] : "+e.getMessage());
			}
		}
		
		return resultMap;
	}
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 첨부파일 삭제 처리
	 * 2. 처리내용 : 문서, 협업 등록 실패 시 exRep ECM에 등록된 파일은 XR_DELETEFILE_QUEUE에 삽입 후
	 *           배치 작업으로 해당 파일을 삭제 한다.
	 * </pre>
	 * @Method Name : fileDelete
	 * @param model
	 * @param request
	 * @param map
	 * @param report
	 * @return Map<String,Object>
	 */
	@RequestMapping(value="/fileDelete.do", method=RequestMethod.POST)
	@ResponseBody 
	public Map<String,Object> fileDelete(@ModelAttribute SessionVO sessionVO,Model model,@RequestParam HashMap<String,Object> map){
		
		Locale locale = LocaleLibrary.setLocale(sessionVO.getSessLanguage() != null  ? sessionVO.getSessLanguage() : ConfigData.getString("LANGUAGE"));
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String fileList = map.get("fileList") != null ? map.get("fileList").toString() : null;
		if(StringUtil.isNull(fileList)){
			resultMap.put("result",Constant.RESULT_TRUE);
			return resultMap;
		}
		
		try{
			
			String volume_id = ConfigData.getString(Constant.EXREP_VOLUME_NM);
			JSONArray jsonArray = JSONArray.fromObject(fileList);
			
			map.put("volume_id",volume_id);
			if(jsonArray.size() > 0 ) {		
				 for(int j=0;j < jsonArray.size();j++)	{					 
					 map.put("contentPath", jsonArray.getJSONObject(j).getString("contentPath").toString());
					 commonService.insertDeleteFileQueue(map);
				 }
			}
			resultMap.put("result",Constant.RESULT_TRUE);
		}catch(BizException e){
			resultMap.put("result",Constant.RESULT_FALSE);
			resultMap.put("message",e.getMessage());				
		}catch(Exception e)	{										
			resultMap.put("result",Constant.RESULT_FALSE);
			resultMap.put("message",messageSource.getMessage("common.system.error",new Object[0],locale));			
		}
		
		return resultMap;
	}

	/**
	 * 
	 * <pre>
	 * 1. 개용 : 문서 첨부파일 다운로드(단일/멀티)
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : download
	 * @param model
	 * @param map
	 * @param sessionVO
	 * @return ModelAndView
	 */
	@RequestMapping("/downLoad.do")
	public ModelAndView download(Model model, @RequestParam HashMap<String,Object> map, @ModelAttribute SessionVO sessionVO) {
		
		List<PageVO> pageList = new ArrayList<PageVO>();
		
		try {
			
			// 1.다운로드 대상 목록 및 PageVO 객체 구하기[APPLIANCE VERSION]
			if(ConfigData.getString("VERSION_INFO") != null && 
					ConfigData.getString("VERSION_INFO").equals(Constant.PRODUCT_EDMS_APPLIANCE)) {
				pageList = commonService.setPageLocalList(map);
			}else {
				pageList = commonService.setPageList(map);
			}

			// 2.첨부파일 조회이력 처리
			if(pageList != null && pageList.size() > 0)	{
				commonService.pageHtWrite(pageList, sessionVO);
			}
			
			// 3.다운로드 VIEW 		
			model.addAttribute("isZip",map.get("isZip") != null ? map.get("isZip").toString() : Constant.F);
			model.addAttribute("pageList",pageList);
			
		}catch(Exception e) {
			logger.error(e.getMessage());
			return new ModelAndView("error/noPage");
		}
		
		return new ModelAndView(new DownloadView());
	}
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 일괄업로드 샘플 다운로드
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : downExcel
	 * @param model
	 * @param map
	 * @param sessionVO
	 * @return ModelAndView
	 */
	@RequestMapping("/downExcel.do")
	public ModelAndView downExcel(Model model, @RequestParam HashMap<String,Object> map,HttpServletRequest request, @ModelAttribute SessionVO sessionVO) {
	
		String type = map.get("type") != null ? map.get("type").toString() : Constant.IS_GROUP;
		String filePath = "";

		try {

			// 사용자/그룹에 따른 업로드 샘플 파일 경로 설정
			if(type.equals(Constant.IS_GROUP) )	{				
				filePath = ConfigData.getString("FILE_DOWNLOAD_PATH") + "groupUpload.xls";							
			}else {			
				filePath = ConfigData.getString("FILE_DOWNLOAD_PATH") + "userUpload.xls";						
			}
			
			// 다운로드 VIEW 파라미터 설정
			model.addAttribute("filePath",filePath);

		}catch(Exception e) {
			logger.error(e.getMessage());
			return new ModelAndView("error/noPage");
		}
		
		return new ModelAndView(new ExcelFileView());
	}

	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : URL 링크복사 다운로드 처리
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : urlDownLoad
	 * @param model
	 * @param map
	 * @param sessionVO
	 * @return ModelAndView
	 */
	@RequestMapping("/urlDownLoad.do")
	public ModelAndView urlDownLoad(Model model, @RequestParam HashMap<String,Object> map, @ModelAttribute SessionVO sessionVO) {
		
		List<PageVO> pageList = new ArrayList<PageVO>();
		PageVO pageVO = new PageVO();
		
		try {
			
			// 1.다운로드 대상 목록 및 PageVO 객체 구하기
			pageVO = commonService.urlPageInfo(map);
			pageList.add(pageVO);
						
			// 2.다운로드 VIEW 		
			model.addAttribute("isZip",Constant.F);
			model.addAttribute("pageList",pageList);
					
		}catch(Exception e) {
			logger.error(e.getMessage());
			model.addAttribute("message",e.getMessage());
			return new ModelAndView("error/404");
		}
		
		return new ModelAndView(new DownloadView());
	}
	
	
	/**
	 * 세션체크
	 * @param model
	 * @param sessionVO
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/sessionCheck.do")	
	@ResponseBody
	public  Map<String, Object> sessonCheck(Model model, @ModelAttribute SessionVO sessionVO, @RequestParam HashMap<String,Object> map,
			HttpServletRequest request) {		
		
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("result", Constant.RESULT_SUCCESS);
		
		return resultMap;
	}
	
	/**
	 * <pre>
	 * 1. 개용 : 내문서 - 작업카트 - URL메일발송
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : sendURLMail
	 * @param sessionVO
	 * @param map
	 * @param request
	 * @return Map<String,Object>
	 */
	@RequestMapping(value="/sendURLMail.do")
	@ResponseBody
	public  Map<String, Object> sendURLMail(@ModelAttribute SessionVO sessionVO, @RequestParam HashMap<String,Object> map,
			HttpServletRequest request) {		
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<String> receiverMailList = new ArrayList<String>();	// 메일수신자 리스트
		List<String> ccMailList = new ArrayList<String>();	// 참조 리스트
		List<String> hccMailList = new ArrayList<String>();	// 숨은참조 리스트
		
		String subject = map.get("subject") != null ? map.get("subject").toString() : "";
		String receiver_address = map.get("receiver_address") != null ? map.get("receiver_address").toString() : "";
		String cc_address = map.get("cc_address") != null ? map.get("cc_address").toString() : "";
		String hcc_address = map.get("hcc_address") != null ? map.get("hcc_address").toString() : "";
		String messageText = map.get("messageText") != null ? map.get("messageText").toString() : "";
		
		try {
			String[] receiverAddress = StringUtil.split2Array(receiver_address, ";", false);
			String[] ccAddress = StringUtil.split2Array(cc_address, ";", false);
			String[] hccAddress = StringUtil.split2Array(hcc_address, ";", false);
			
    		if(receiverAddress.length > 0) {
    			
    			receiverMailList = Arrays.asList(receiverAddress);
    			ccMailList = Arrays.asList(ccAddress);
    			hccMailList = Arrays.asList(hccAddress);
    			
	    		MailSendUtil.sendURLMail(subject,receiverMailList,ccMailList,hccMailList,messageText);		    					    		
	    		resultMap.put("result", Constant.RESULT_SUCCESS);
	    		
    		}else {
    			resultMap.put("result",Constant.RESULT_FAIL);
    			resultMap.put("message","메일수신자 담당없음 :: 메일발송안함");
    		}
		}catch(Exception e) {
			logger.error(e.getMessage());
			resultMap.put("result",Constant.RESULT_FAIL);
			resultMap.put("message","MAIL SEND ERROR");			
		}
		
		
		return resultMap;
	}
	
	@RequestMapping("/deleteRecently.do")
	@ResponseBody
	public Map<String,Object> deleteRecently(@ModelAttribute SessionVO sessionVO,Model model,@RequestParam HashMap<String,Object> map){
		
		@SuppressWarnings("unused")
		Locale locale = LocaleLibrary.setLocale(sessionVO.getSessLanguage() != null  ? sessionVO.getSessLanguage() : ConfigData.getString("LANGUAGE"));
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		try{
			// map param : recently_id
			resultMap = commonService.deleteRecently(map);
		}catch(BizException e){
			resultMap.put("result",Constant.RESULT_FALSE);
			resultMap.put("message",e.getMessage());				
		}catch(Exception e)	{										
			resultMap.put("result",Constant.RESULT_FALSE);
			resultMap.put("message",messageSource.getMessage("common.system.error",new Object[0],locale));			
		}
		
		return resultMap;
	}
	
	@RequestMapping("/configFileInfo.do")
	@ResponseBody
	public Map<String,Object> configFileInfo(@ModelAttribute SessionVO sessionVO,Model model,@RequestParam HashMap<String,Object> map){
		
		@SuppressWarnings("unused")
		Locale locale = LocaleLibrary.setLocale(sessionVO.getSessLanguage() != null  ? sessionVO.getSessLanguage() : ConfigData.getString("LANGUAGE"));
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		try{
			// map param : recently_id
			resultMap = commonService.configFileInfo(map);
		}catch(BizException e){
			resultMap.put("result",Constant.RESULT_FALSE);
			resultMap.put("message",e.getMessage());				
		}catch(Exception e)	{										
			resultMap.put("result",Constant.RESULT_FALSE);
			resultMap.put("message",messageSource.getMessage("common.system.error",new Object[0],locale));			
		}
		
		return resultMap;
	}
}

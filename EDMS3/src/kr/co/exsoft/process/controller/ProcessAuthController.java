package kr.co.exsoft.process.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import kr.co.exsoft.common.vo.SessionVO;
import kr.co.exsoft.document.controller.DocumentAuthController;
import kr.co.exsoft.document.service.TypeService;
import kr.co.exsoft.document.vo.TypeVO;
import kr.co.exsoft.eframework.configuration.Constant;
import kr.co.exsoft.eframework.exception.BizException;
import kr.co.exsoft.eframework.library.ExcelView;
import kr.co.exsoft.eframework.library.LocaleLibrary;
import kr.co.exsoft.eframework.util.CommonUtil;
import kr.co.exsoft.eframework.util.ConfigData;
import kr.co.exsoft.eframework.util.StringUtil;
import kr.co.exsoft.process.service.ProcessService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

/**
 * 협업프로세스 Controller
 * @author 패키지 개발팀
 * @since 2015.03.12
 * @version 3.0
 *
 */
@Controller
@SessionAttributes("sessionVO")
@RequestMapping("/process")
public class ProcessAuthController {

	@Autowired
	private ProcessService processService;
	
	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	private TypeService typeService;
	
	protected static final Log logger = LogFactory.getLog(ProcessAuthController.class);
	
	/**
	 * 	
	 * <pre>
	 * 1. 개용 : 협업 레이아웃
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : processLayout
	 * @param sessionVO
	 * @param model
	 * @param map
	 * @param request
	 * @return String
	 */
	@RequestMapping("/processLayout.do")
	public String processLayout(@ModelAttribute SessionVO sessionVO,Model model,@RequestParam HashMap<String,Object> map,HttpServletRequest request) {
		CommonUtil.setSessionToModel(model, sessionVO);			// call by reference
		
		// 문서유형 : 파라미터 - is_doc
		HashMap<String, Object> param = new HashMap<String, Object>();
		List<TypeVO> typeList = new ArrayList<TypeVO>();
		
		try{
			param.put("is_doc", Constant.T);
			param.put("is_hidden", Constant.T); // 쿼리가 != 비교여서 T를 넘김
			typeList = typeService.typeList(param);
			
		}catch (BizException e){
			logger.info(StringUtil.getErrorTrace(e));
		} catch (Exception e)	{			
			logger.info(StringUtil.getErrorTrace(e));
		}
				
		model.addAttribute("typeList",typeList);
		model.addAttribute("menuType",Constant.TOPMENU_WORKPROCESS);
		model.addAttribute("processType",Constant.PROCESS_WRITE_ING_MENU);
		
		return "process/processList";
	}
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 협업 메뉴별 목록 가져오기
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : processList
	 * @param model
	 * @param sessionVO
	 * @param map
	 * @param request
	 * @return ModelAndView
	 */
	@RequestMapping(value="/processList.do")
	@ResponseBody 
	public ModelAndView processList(Model model, @ModelAttribute SessionVO sessionVO, @RequestParam HashMap<String,Object> map,
			HttpServletRequest request) {
		
		Locale locale = LocaleLibrary.setLocale(sessionVO.getSessLanguage() != null  ? sessionVO.getSessLanguage() : ConfigData.getString("LANGUAGE"));
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		HashMap<String, Object> param = new HashMap<String, Object>();
		
		// 사용자 조건 셋팅
		param.put("user_id", sessionVO.getSessId());
		
		// 입력 파라미터 유효성 체크
		param.put("contextRoot",sessionVO.getSessContextRoot());  // page에서 이미지 처리를 위해
		param.put("sdate",map.get("sdate") != null ? map.get("sdate").toString() : "");
		param.put("edate",map.get("edate") != null ? map.get("edate").toString() : "");
		param.put("type",map.get("type") != null ? map.get("type").toString() : Constant.PROCESS_WRITE_ING_MENU); // 협업 메뉴 type
		
		String strIndex = StringUtil.getMapString(map, "strIndex");
		String strIndexData = "";
		switch (strIndex) {
		case "doc_name" :
			strIndexData = "P.NAME";
			break;
		case "doc_description" :
			strIndexData = "P.CONTENT";
			break;
		case "creator_name" :
			strIndexData = "P.CREATOR_NAME";
			break;
		}
		
		param.put("strIndexColumn", strIndexData);
		param.put("strKeyword1", StringUtil.getMapString(map, "strKeyword1"));		
		param.put("orderCol", StringUtil.getMapString(map, "sidx", "PRECESS_ID"));
		param.put("orderType", StringUtil.getMapString(map, "sord", "ASC"));
		param.put("page_size", StringUtil.getMapString(map, "rows", sessionVO.getSessPage_size()));		
		param.put("nPage",CommonUtil.getPage(map));
		
		//상세검색 입력 파라미터 유효성 체크
		param.put("page_name",map.get("page_name") != null ? map.get("page_name") : ""); 				// 첨부파일명
		
		// Excel Down 처리  : 파리미터=oper		
		String oper = map.get("oper") != null ? map.get("oper").toString() : "";
		String[]  members ;
		String[]  cell_headers ;
		int[] cell_widths;
		
		try{
			
			// 1.1 엑셀다운로드시 한글검색어 인코딩 처리
			if(oper.equals(Constant.EXCEL_FORMAT)) {
				param.put("nPage",1);			
				param.put("page_size",ConfigData.getInt("EXCEL_MAX_LIMIT"));		// 엑셀 저장 row수					
				param.put("strKeyword",map.get("strKeyword") != null ? new String(map.get("strKeyword").toString().getBytes("8859_1"),"utf-8") : "");	
			}

			// 2. 협업 리스트 가져오기 
			resultMap = processService.processList(param);
			
			// Excel Down 처리 :: 목록
			if(oper.equals(Constant.EXCEL_FORMAT)) {
				
				CommonUtil.getExcelList(resultMap,model);
				
				members = new String[]{ "name", "status_nm", "expect_date","complete_date","author_nm","approval_count","receiver_count"};
				cell_headers = new String[]{ "제목", "상태", "완료예정일","최종수정일","작성자","승인","열람"};
				cell_widths = new int[]{ 80,15,20,20,15,15,15};
						
        		model.addAttribute("members",members);
        		model.addAttribute("cell_headers",cell_headers);
        		model.addAttribute("cell_widths",cell_widths);
        		model.addAttribute("fileName","downLoad.xls");

        		return new ModelAndView(new ExcelView());        									
			}
		}catch(BizException e){
			logger.info(StringUtil.getErrorTrace(e));
			resultMap.put("result",Constant.RESULT_FALSE);
			resultMap.put("message",e.getMessage());				
		}catch(Exception e)	{	
			logger.info(StringUtil.getErrorTrace(e));
			resultMap.put("result",Constant.RESULT_FALSE);
			resultMap.put("message",messageSource.getMessage("process.list.error",new Object[0],locale));			
		}
		
		ModelAndView mav = new ModelAndView("jsonView",resultMap);
		return mav;
	}
	
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 좌측 메뉴 상단엣 문서에 대한 count를 가져오기 
	 * 2. 처리내용 : 사용자 ID기준으로 협업 관련 문서 count를 가져오기
	 * </pre>
	 * @Method Name : processCount
	 * @param sessionVO
	 * @param model
	 * @param map
	 * @return Map<String,Object>
	 */
	@RequestMapping("/processCount.do")
	@ResponseBody
	public Map<String,Object> processCount(@ModelAttribute SessionVO sessionVO,Model model,@RequestParam HashMap<String,Object> map){
		
		@SuppressWarnings("unused")
		Locale locale = LocaleLibrary.setLocale(sessionVO.getSessLanguage() != null  ? sessionVO.getSessLanguage() : ConfigData.getString("LANGUAGE"));
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		try{
			map.put("user_id", sessionVO.getSessId());
			resultMap = processService.processCount(map);
		}catch(BizException e){	
			logger.info(StringUtil.getErrorTrace(e));
			resultMap.put("result",Constant.RESULT_FALSE);
			resultMap.put("data",-1000);
		}catch(Exception e)	{	
			logger.info(StringUtil.getErrorTrace(e));
			resultMap.put("result",Constant.RESULT_FALSE);
			resultMap.put("data",-2000);			
		}
		
		return resultMap;
	}
	
	@RequestMapping(value="/processRecentlyList.do")
	@ResponseBody 
	public ModelAndView processRecentlyList(Model model, @ModelAttribute SessionVO sessionVO, @RequestParam HashMap<String,Object> map,
			HttpServletRequest request) {
		
		@SuppressWarnings("unused")
		Locale locale = LocaleLibrary.setLocale(sessionVO.getSessLanguage() != null  ? sessionVO.getSessLanguage() : ConfigData.getString("LANGUAGE"));
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		HashMap<String, Object> param = new HashMap<String, Object>();
		
		// 사용자 조건 셋팅
		param.put("user_id", sessionVO.getSessId());
		
		try{
			
			// 1. 최근 협업 등록 리스트 가져오기 
			resultMap = processService.processRecentlyList(param);
			
		}catch(BizException e){
			logger.info(StringUtil.getErrorTrace(e));
			resultMap.put("result",Constant.RESULT_FALSE);
			resultMap.put("message",e.getMessage());				
		}catch(Exception e)	{	
			logger.info(StringUtil.getErrorTrace(e));
			resultMap.put("result",Constant.RESULT_FALSE);
			resultMap.put("message",messageSource.getMessage("process.recently.list.error",new Object[0],locale));			
		}
		
		ModelAndView mav = new ModelAndView("jsonView",resultMap);
		return mav;
	}
	
	@RequestMapping("/selectProcessRecently.do")
	@ResponseBody
	public Map<String,Object> selectProcessRecently(@ModelAttribute SessionVO sessionVO,Model model,@RequestParam HashMap<String,Object> map){
		
		@SuppressWarnings("unused")
		Locale locale = LocaleLibrary.setLocale(sessionVO.getSessLanguage() != null  ? sessionVO.getSessLanguage() : ConfigData.getString("LANGUAGE"));
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		try{
			resultMap = processService.selectProcessRecently(map);
		}catch(BizException e){
			logger.info(StringUtil.getErrorTrace(e));
			resultMap.put("result",Constant.RESULT_FALSE);
			resultMap.put("message",e.getMessage());				
		}catch(Exception e)	{
			logger.info(StringUtil.getErrorTrace(e));
			resultMap.put("result",Constant.RESULT_FALSE);
			resultMap.put("message",messageSource.getMessage("process.recently.info.error",new Object[0],locale));			
		}
		
		return resultMap;
	}
	
	@RequestMapping("/processControl.do")
	@ResponseBody
	public Map<String,Object> processControl(@ModelAttribute SessionVO sessionVO, Model model, @RequestParam HashMap<String,Object> map, HttpServletRequest request){
		
		@SuppressWarnings("unused")
		Locale locale = LocaleLibrary.setLocale(sessionVO.getSessLanguage() != null  ? sessionVO.getSessLanguage() : ConfigData.getString("LANGUAGE"));
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		Object[] msgObject = null;
		try{
			// 1. action에 따른 service 구분
			String strActionType =  map.get("actionType") != null ? map.get("actionType").toString() : "";
			
			switch (strActionType) {
				case Constant.PROCESS_ACTION_APPROVEREQUEST:resultMap = processService.approveAction(map, sessionVO);msgObject = new String[]{"승인 요청이"};break;
				case Constant.PROCESS_ACTION_APPROVE:resultMap = processService.approveAction(map, sessionVO);msgObject = new String[]{"승인이"};break;
				case Constant.PROCESS_ACTION_APPROVEREJECT:resultMap = processService.approveAction(map, sessionVO);msgObject = new String[]{"반려가"};break;
				case Constant.ACTION_VIEW:resultMap = processService.processDetail(map);msgObject = new String[]{"협업 정보 가져오기에"};break;
				case Constant.ACTION_CREATE:resultMap = processService.processWrite(sessionVO, model, map, request); msgObject = new String[]{"신규 업무 등록이"}; break; //협업 등록

			default:
				break;
			}
			
		resultMap.put("message",messageSource.getMessage("process.control.sucess", msgObject,locale));
		
		}catch(BizException e){
			logger.info(StringUtil.getErrorTrace(e));
			resultMap.put("result",Constant.RESULT_FALSE);
			resultMap.put("message",e.getMessage());				
		}catch(Exception e)	{	
			logger.info(StringUtil.getErrorTrace(e));
			resultMap.put("result",Constant.RESULT_FALSE);
			resultMap.put("message",messageSource.getMessage("process.control.error", new String[]{e.getMessage()},locale));			
		}
		
		return resultMap;
	}
	
}

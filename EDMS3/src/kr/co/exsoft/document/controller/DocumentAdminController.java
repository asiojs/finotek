package kr.co.exsoft.document.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import kr.co.exsoft.common.service.CommonService;
import kr.co.exsoft.common.vo.CodeVO;
import kr.co.exsoft.common.vo.SessionVO;
import kr.co.exsoft.document.service.DocumentService;
import kr.co.exsoft.eframework.configuration.Constant;
import kr.co.exsoft.eframework.exception.BizException;
import kr.co.exsoft.eframework.library.ExcelView;
import kr.co.exsoft.eframework.library.LocaleLibrary;
import kr.co.exsoft.eframework.util.CommonUtil;
import kr.co.exsoft.eframework.util.ConfigData;
import kr.co.exsoft.eframework.util.StringUtil;
import kr.co.exsoft.folder.service.FolderService;

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
import org.springframework.web.servlet.ModelAndView;
import org.springmodules.validation.commons.DefaultBeanValidator;

/**
 * Document Controller
 * @author 패키지 개발팀
 * @since 2014.07.21
 * @version 3.0
 *
 */
@Controller
@SessionAttributes("sessionVO")
@RequestMapping("/admin")
public class DocumentAdminController {

	@Autowired
	private DocumentService documentService;
	
	@Autowired
	private CommonService commonService;
	
	@Autowired
	private MessageSource messageSource;
	
    @Autowired
    private DefaultBeanValidator beanValidator;
    
    @Autowired
    private FolderService folderService;
    
	protected static final Log logger = LogFactory.getLog(DocumentAdminController.class);

	/**
	 * 
	 * <pre>
	 * 1. 개용 : 만기문서관리
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : localExtManager
	 * @param sessionVO
	 * @param model
	 * @param map
	 * @return String
	 */
	@RequestMapping("expiredManager.do")
	public String expiredManager(@ModelAttribute SessionVO sessionVO,Model model,@RequestParam HashMap<String,Object> map) {
		
		@SuppressWarnings("unused")
		Locale locale = LocaleLibrary.setLocale(sessionVO.getSessLanguage() != null  ? sessionVO.getSessLanguage() : ConfigData.getString("LANGUAGE"));
		
		List<CodeVO> preservation = new ArrayList<CodeVO>();
		HashMap<String,Object> param = new HashMap<String,Object>();
		
		Map<String, Object> menuInfo = new HashMap<String, Object>();
		Map<String, Object> partInfo = new HashMap<String, Object>();
		
		try {
			
			// 1. 관리자 ROLE 접근권한 및 페이지 네비게이션 :: 상위메뉴명 / 현재메뉴명
			commonService.setPageToModel(map,menuInfo,partInfo,sessionVO);
						
			// 2. 보존기간 연장 정보 
			param.put("gcode_id", Constant.CODE_PRESERVATION_YEAR);
			preservation = commonService.codeList(param);
			
		}catch(BizException e){	
			logger.error(e.getMessage());
			CommonUtil.setErrorMsg(model, Constant.ERROR_403, e.getMessage(),sessionVO.getSessContextRoot());
			return "error/message";
		}catch(Exception e)	{										
			logger.error(e.getMessage());
			CommonUtil.setErrorMsg(model, Constant.ERROR_505, e.getMessage(),sessionVO.getSessContextRoot());
			return "error/message";
		}
				
		CommonUtil.setSessionToModel(model, sessionVO);
		
		model.addAttribute("part",partInfo.get("part").toString());
		model.addAttribute("menuInfo",menuInfo);
		model.addAttribute("preservation_year",preservation);
		model.addAttribute("topSelect",Constant.TOPMENU_DOCUMENT);
		model.addAttribute("subSelect",Constant.DOCUMENT_EXPIREDMANAGER);
		
		return "docadmin/expiredManager";
	}
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 휴지통 관리
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : wasteManager
	 * @param sessionVO
	 * @param model
	 * @param map
	 * @return String
	 */
	@RequestMapping("wasteManager.do")
	public String wasterManager(@ModelAttribute SessionVO sessionVO,Model model,@RequestParam HashMap<String,Object> map) {
		
		@SuppressWarnings("unused")
		Locale locale = LocaleLibrary.setLocale(sessionVO.getSessLanguage() != null  ? sessionVO.getSessLanguage() : ConfigData.getString("LANGUAGE"));
		
		Map<String, Object> menuInfo = new HashMap<String, Object>();
		Map<String, Object> partInfo = new HashMap<String, Object>();
		
		try {
			
			// 관리자 ROLE 접근권한 및 페이지 네비게이션 :: 상위메뉴명 / 현재메뉴명
			commonService.setPageToModel(map,menuInfo,partInfo,sessionVO);
						
		}catch(BizException e){	
			logger.error(e.getMessage());
			CommonUtil.setErrorMsg(model, Constant.ERROR_403, e.getMessage(),sessionVO.getSessContextRoot());
			return "error/message";
		}catch(Exception e)	{										
			logger.error(e.getMessage());
			CommonUtil.setErrorMsg(model, Constant.ERROR_505, e.getMessage(),sessionVO.getSessContextRoot());
			return "error/message";
		}
		
		CommonUtil.setSessionToModel(model, sessionVO);
		
		model.addAttribute("part",partInfo.get("part").toString());
		model.addAttribute("menuInfo",menuInfo);
		model.addAttribute("topSelect",Constant.TOPMENU_DOCUMENT);
		model.addAttribute("subSelect",Constant.DOCUMENT_WASTEMANAGER);
		
		return "docadmin/wasteManager";
	}
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 소유권 변경 
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : ownerManager
	 * @param sessionVO
	 * @param model
	 * @param map
	 * @return String
	 */
	@RequestMapping("ownerManager.do")
	public String ownerManager(@ModelAttribute SessionVO sessionVO,Model model,@RequestParam HashMap<String,Object> map) {
		
		@SuppressWarnings("unused")
		Locale locale = LocaleLibrary.setLocale(sessionVO.getSessLanguage() != null  ? sessionVO.getSessLanguage() : ConfigData.getString("LANGUAGE"));
		
		Map<String, Object> menuInfo = new HashMap<String, Object>();
		Map<String, Object> partInfo = new HashMap<String, Object>();
		
		try {
			
			// 관리자 ROLE 접근권한 및 페이지 네비게이션 :: 상위메뉴명 / 현재메뉴명
			commonService.setPageToModel(map,menuInfo,partInfo,sessionVO);
			
		}catch(BizException e){	
			logger.error(e.getMessage());
			CommonUtil.setErrorMsg(model, Constant.ERROR_403, e.getMessage(),sessionVO.getSessContextRoot());
			return "error/message";
		}catch(Exception e)	{										
			logger.error(e.getMessage());
			CommonUtil.setErrorMsg(model, Constant.ERROR_505, e.getMessage(),sessionVO.getSessContextRoot());
			return "error/message";
		}

		CommonUtil.setSessionToModel(model, sessionVO);
		
		model.addAttribute("part",partInfo.get("part").toString());
		model.addAttribute("menuInfo",menuInfo);
		model.addAttribute("topSelect",Constant.TOPMENU_DOCUMENT);
		model.addAttribute("subSelect",Constant.DOCUMENT_OWNERMANAGER);
		
		return "docadmin/ownerManager";
	}
	
	
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 중복파일 문서리스트 가져오기.
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : duplicateDocList
	 * @param model
	 * @param sessionVO
	 * @param map
	 * @param request
	 * @return Map<String,Object>
	 */
	@RequestMapping(value="/duplicateDocList.do", method=RequestMethod.POST)
	@ResponseBody 
	public Map<String,Object> duplicateDocList(Model model, @ModelAttribute SessionVO sessionVO, @RequestParam HashMap<String,Object> map,
			HttpServletRequest request) {
		
		Locale locale = LocaleLibrary.setLocale(sessionVO.getSessLanguage() != null  ? sessionVO.getSessLanguage() : ConfigData.getString("LANGUAGE"));
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		HashMap<String, Object> param = new HashMap<String, Object>();
		
		// 입력 파라미터 유효성 체크		
		param.put("contextRoot",sessionVO.getSessContextRoot());
		param.put("page_name",map.get("page_name") != null ? map.get("page_name") : "" );
		param.put("file_size",map.get("file_size") != null ? map.get("file_size") : "" );		
		param.put("orderCol",map.get("sidx") != null ? map.get("sidx") : "doc_name");
		param.put("orderType",map.get("sord") != null ? map.get("sord") : "ASC");
		param.put("page_size",map.get("rows") != null ? map.get("rows") : sessionVO.getSessPage_size());		
		param.put("sdate",map.get("sdate") != null ? map.get("sdate").toString() : "");
		param.put("edate",map.get("edate") != null ? map.get("edate").toString() : "");
		param.put("is_current",Constant.T);
		param.put("nPage",CommonUtil.getPage(map));
		
		try {
			
			// 정렬 컬럼명 변경처리
			if(param.get("orderCol").toString().toLowerCase().equals("type_name")) {
				param.put("orderCol","T."+param.get("orderCol").toString());				
			}else {
				param.put("orderCol","D."+param.get("orderCol").toString());
			}
			
			// 중복파일 목록 가져오기
			if(!param.get("page_name").toString().equals("") && !param.get("page_size").toString().equals(""))	{
				resultMap = documentService.duplicateDocList(param);
			}			
			
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
	 * 1. 개용 : 문서 목록 조회
	 * 2. 처리내용 : 관리자내 만기문서, 소유권변경, 관리자 휴지통 대한 처리
	 * </pre>
	 * @Method Name : docuemntList
	 * @param sessionVO
	 * @param model
	 * @param map
	 * @param request
	 * @return Map<String,Object>
	 * , method=RequestMethod.POST
	 */
	@RequestMapping(value="documentList.do")
	@ResponseBody
	public ModelAndView docuemntList(@ModelAttribute SessionVO sessionVO, Model model, @RequestParam HashMap<String, Object> map,
			HttpServletRequest request) {
				
		Locale locale = LocaleLibrary.setLocale(sessionVO.getSessLanguage() != null  ? sessionVO.getSessLanguage() : ConfigData.getString("LANGUAGE"));

		List<String> authGroupList = new ArrayList<String>();
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		HashMap<String,Object> param = new  HashMap<String, Object>();
		
		// 입력 파라미터 유효성 체크
		param.put("contextRoot",sessionVO.getSessContextRoot());
		param.put("strIndex",map.get("strIndex") != null ? map.get("strIndex") : "" );
		param.put("strKeyword",map.get("strKeyword") != null ? map.get("strKeyword") : "" );
		param.put("ownerKeyword",map.get("ownerKeyword") != null ? map.get("ownerKeyword") : "" );
		param.put("orderCol",map.get("sidx") != null ? map.get("sidx") : "DOC_NAME");
		param.put("orderType",map.get("sord") != null ? map.get("sord") : "ASC");
		param.put("page_size",map.get("rows") != null ? map.get("rows") : sessionVO.getSessPage_size());		
		param.put("is_admin",Constant.T);		
		param.put("sdate",map.get("sdate") != null ? map.get("sdate").toString().replaceAll("-","") : "");
		param.put("edate",map.get("edate") != null ? map.get("edate").toString().replaceAll("-","") : "");
		param.put("search_type", map.get("search_type") != null ? map.get("search_type") : "");				// 소유권변경 검색 대상 설정
		param.put("part",map.get("part") != null ? map.get("part").toString() : Constant.MENU_TEAM);		
		param.put("nPage",CommonUtil.getPage(map));				// page 설정
		
		// Excel Down 처리  : 파리미터=oper			
		String oper = map.get("oper") != null ? map.get("oper").toString() : "";
		String[] members = null;
		String[] cell_headers = null;
		int[] cell_widths = null;
		
		try {
			
			String listType = map.get(Constant.DOCUMENT_LIST_TYPE) != null ? map.get(Constant.DOCUMENT_LIST_TYPE).toString() : null;

			// 검색필터추가 :: 관리자 접근 권한 구분 (ALL/GROUP/TEAM)
			if(!param.get("part").toString().equals(Constant.MENU_ALL)) {				
				authGroupList = commonService.authGroupList(param.get("part").toString(), sessionVO);
				param.put("authGroupList",authGroupList);
			}else {
				param.put("authGroupList","");
			}
						
			// Excel Down 처리  : 파리미터=oper			
			if(oper.equals(Constant.EXCEL_FORMAT)) {
				param.put("nPage",1);			
				param.put("page_size",ConfigData.getInt("EXCEL_MAX_LIMIT"));						
				param.put("ownerKeyword",map.get("ownerKeyword") != null ? new String(map.get("ownerKeyword").toString().getBytes("8859_1"),"utf-8") : "");
				param.put("strKeyword",map.get("strKeyword") != null ? new String(map.get("strKeyword").toString().getBytes("8859_1"),"utf-8") : "");	
			}
			
			if(Constant.DOCUMENT_LIST_TYPE_EXPIRED.equals(listType)){					// 만기문서		
				
				resultMap = documentService.expiredDocumentList(param);				
				
			}else if(Constant.DOCUMENT_LIST_TYPE_TRASHCAN.equals(listType)){		// 관리자 휴지통
				
				// 관련 QUERY 변경
				resultMap = documentService.wasteDocList(param);			
				
			}else if(Constant.DOCUMENT_LIST_TYPE_OWNER.equals(listType)){				// 소유권 변경	
				
				// 처리 프로세스상 user_id로 조건검색을 하지 않은 데이터는 의미가 없으므로, 체크하여 로직에서 튕겨낸다.
				if (StringUtil.getMapString(param, "ownerKeyword") != "") {
					resultMap = documentService.ownerDocList(param);
				}
			}
			
			// Excel Down 처리 :: 목록
			if(oper.equals(Constant.EXCEL_FORMAT)) {
				
				CommonUtil.getExcelList(resultMap,model);
				
				if(Constant.DOCUMENT_LIST_TYPE_EXPIRED.equals(listType)){	
					members = new String[]{ "doc_name", "type_name", "owner_name", "expired_date" };
	        		cell_headers = new String[]{ "문서명", "문서유형", "소유자명", "만기일" };
	        		cell_widths = new int[]{ 50, 10, 20, 20 };
				}else if(Constant.DOCUMENT_LIST_TYPE_TRASHCAN.equals(listType)){
					members = new String[]{ "doc_name", "type_name", "owner_name", "waste_date" };
	        		cell_headers = new String[]{ "문서명", "문서유형", "소유자명", "삭제일" };
	        		cell_widths = new int[]{ 50, 10, 20, 20 };
				}else if(Constant.DOCUMENT_LIST_TYPE_OWNER.equals(listType)){			
					members = new String[]{ "doc_name", "type_name", "owner_name", "create_date" };
	        		cell_headers = new String[]{ "문서명", "문서유형", "소유자명", "등록일" };
	        		cell_widths = new int[]{ 50, 10, 20, 20 };
				}
				
        		model.addAttribute("members",members);
        		model.addAttribute("cell_headers",cell_headers);
        		model.addAttribute("cell_widths",cell_widths);
        		model.addAttribute("fileName","downLoad.xls");
				
				return new ModelAndView(new ExcelView());
			}

		}catch(BizException e){		
			resultMap.put("result",Constant.RESULT_FALSE);
			resultMap.put("message",e.getMessage());				
		}catch(Exception e)	{										
			resultMap.put("result",Constant.RESULT_FALSE);
			resultMap.put("message",messageSource.getMessage("common.system.error",new Object[0],locale));			
		}

		//return resultMap;
		ModelAndView mav = new ModelAndView("jsonView",resultMap);
		return mav;
	}
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 문서완전삭제처리 :: 대상테이블 XR_DOCUMENT
	 * 2. 처리내용 : 만기문서관리/중복파일관리 선택문서 리스트 삭제처리
	 * </pre>
	 * @Method Name : typeControl
	 * @param model
	 * @param sessionVO
	 * @param map
	 * @param request
	 * @return Map<String,Object>
	 */
	@RequestMapping(value="/terminateDoc.do", method=RequestMethod.POST)
	@ResponseBody 
	public Map<String,Object> terminateDoc(Model model, @ModelAttribute SessionVO sessionVO, @RequestParam HashMap<String,Object> map,HttpServletRequest request) {
		
		Locale locale = LocaleLibrary.setLocale(sessionVO.getSessLanguage() != null  ? sessionVO.getSessLanguage() : ConfigData.getString("LANGUAGE"));
		
		List<HashMap<String,Object>> docList = new ArrayList<HashMap<String,Object>>();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		try {
			
			// 완전삭제할 문서 목록 유효성 체크 및 구하기.
			docList = documentService.documentValidList(map);
			
			// 완전삭제처리 
			resultMap = documentService.terminateDocument(docList, map, sessionVO);
			
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
	 * 1. 개용 : 휴지통 문서 상세 조회 :: 대상 테이블 XR_DOCUMENT_DEL
	 * 2. 처리내용 : 만기문서/관리자휴지통 문서상세보기 통합처리
	 * </pre>
	 * @Method Name : wasteDocDetail
	 * @param sessionVO
	 * @param model
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/wasteDocDetail.do", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> wasteDocDetail(@ModelAttribute SessionVO sessionVO, Model model, @RequestParam HashMap<String, Object> map,
			HttpServletRequest request) {
				
		Locale locale = LocaleLibrary.setLocale(sessionVO.getSessLanguage() != null  ? sessionVO.getSessLanguage() : ConfigData.getString("LANGUAGE"));		
		Map<String, Object> resultMap = new HashMap<String, Object>();

		try {
			
			// 관리자 휴지통 문서의 상세 정보를 조회. :: 공통서비스로 변경처리(파라미터 table_nm/doc_id
			resultMap = documentService.docCommonView(map, sessionVO);			
			
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
	 * <pre>
	 * 1. 내용 : 만기 문서의 보존기간을 연장한다
	 * 2. 처리내용 
	 * </pre>
	 * @Method Name : extendDocument
	 * @param sessionVO
	 * @param model
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/extendDocument.do", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> extendDocument(@ModelAttribute SessionVO sessionVO, Model model, @RequestParam HashMap<String, Object> map) {
		Locale locale = LocaleLibrary.setLocale(sessionVO.getSessLanguage() != null  ? sessionVO.getSessLanguage() : ConfigData.getString("LANGUAGE"));
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		try {
			// 보존기간을 변경한다
			resultMap = documentService.preservationYearUpdate(map, sessionVO);
			
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
	 * <pre>
	 * 1. 내용 : 휴지통 문서 영구 삭제
	 * 2. 처리내용 : 
	 * </pre>
	 * 
	 * @Method Name : wasteDocDelete
	 * @param model
	 * @param sessionVO
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/wasteDocDelete.do", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> wasteDocDelete(Model model, @ModelAttribute SessionVO sessionVO, @RequestParam HashMap<String, Object> map,
			HttpServletRequest request) {

		Locale locale = LocaleLibrary.setLocale(sessionVO.getSessLanguage() != null  ? sessionVO.getSessLanguage() : ConfigData.getString("LANGUAGE"));
		
		List<HashMap<String,Object>> docList = new ArrayList<HashMap<String,Object>>();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		try {
			
			// 삭제할 휴지통 문서 목록 유효성 체크 및 구하기.
			docList = documentService.documentValidList(map);
			
			// 관리자 휴지통 문서 삭제 처리			
			resultMap = documentService.adminTrashDeleteDoc(docList, map, sessionVO);			
			
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
	 * 1. 개용 : 관리자 페이지내 문서 상세보기 조회
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : docCommonView
	 * @param sessionVO
	 * @param model
	 * @param map
	 * @param request
	 * @return Map<String,Object>
	 */
	@RequestMapping(value="/docCommonView.do", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> docCommonView(@ModelAttribute SessionVO sessionVO, Model model, @RequestParam HashMap<String, Object> map,
			HttpServletRequest request) {

		Locale locale = LocaleLibrary.setLocale(sessionVO.getSessLanguage() != null  ? sessionVO.getSessLanguage() : ConfigData.getString("LANGUAGE"));
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		try {
			
			// 문서정보 조회 :: 첨부파일 및 권한 목록 포함
			map.put("table_nm",Constant.DOC_TABLE);
			resultMap = documentService.docCommonView(map, sessionVO);

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
	 *  * <pre>
	 * 1. 내용 : 휴지통 비우기
	 * 2. 처리내용 : 
	 * </pre>
	 * 
	 * @Method Name : wasteAllDocDelete
	 * @param sessionVO
	 * @param model
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping(value="wasteAllDocDelete.do", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> wasteAllDocDelete(@ModelAttribute SessionVO sessionVO, Model model, @RequestParam HashMap<String, Object> map,
			HttpServletRequest request) {

		Locale locale = LocaleLibrary.setLocale(sessionVO.getSessLanguage() != null  ? sessionVO.getSessLanguage() : ConfigData.getString("LANGUAGE"));
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<HashMap<String,Object>> docList = new ArrayList<HashMap<String,Object>>();
			
		try {
			
			// 1.휴지통 문서 목록 유효성 체크 및 구하기. 
			docList = documentService.allDocumentValidList(map);	

			// 2.관리자 휴지통 문서 삭제 처리			
			resultMap = documentService.adminTrashDeleteDoc(docList, map, sessionVO);			
			
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

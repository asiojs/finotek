package kr.co.exsoft.document.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import kr.co.exsoft.common.service.CacheService;
import kr.co.exsoft.common.service.CommonService;
import kr.co.exsoft.common.vo.SessionVO;
import kr.co.exsoft.document.service.DocumentService;
import kr.co.exsoft.document.service.PageService;
import kr.co.exsoft.document.service.TypeService;
import kr.co.exsoft.document.vo.PageVO;
import kr.co.exsoft.eframework.configuration.Constant;
import kr.co.exsoft.eframework.exception.BizException;
import kr.co.exsoft.eframework.library.DownloadView;
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
 * 나의문서관련 메뉴 Controller
 *
 * @author 패키지팀
 * @since 2014. 11. 12.
 * @version 1.0
 * 
 */
@Controller
@SessionAttributes("sessionVO")
@RequestMapping("/mypage")
public class MyDocumentAuthController {

	@Autowired
	private DocumentService documentService;
	
	@Autowired
	private PageService pageService;
	
	@Autowired
	private CommonService commonService;
	
	@Autowired
	private TypeService typeService;
	
	@Autowired
	private CacheService cacheService;
	
    @Autowired
	private FolderService folderService;
    
	@Autowired
	private MessageSource messageSource;
	
    @Autowired
    private DefaultBeanValidator beanValidator;
    
	protected static final Log logger = LogFactory.getLog(DocumentAuthController.class);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 나의문서 레이아웃 초기화면 
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : userLayout
	 * @param sessionVO
	 * @param model
	 * @param map
	 * @param request
	 * @return String
	 */
	@RequestMapping("/myLayout.do")
	public String myLayout(@ModelAttribute SessionVO sessionVO,Model model,@RequestParam HashMap<String,Object> map,HttpServletRequest request) {
			
		CommonUtil.setSessionToModel(model, sessionVO);
		
		// 나의문서 메뉴 구분자
		String myMenuType = map.get("myMenuType") != null ? map.get("myMenuType").toString() : "OWNER";
		
		model.addAttribute("contents","/mypage/myPageDocList.do?myMenuType="+myMenuType);		
		model.addAttribute("role_id",sessionVO.getSessRole_id());
		model.addAttribute("role_nm",sessionVO.getSessRole_nm());
		model.addAttribute("user_role",Constant.USER_ROLE);
				
		return "myPageLayout";
	}
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 나의문서 리스트 통합
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : myPageDocList
	 * @param sessionVO
	 * @param model
	 * @param map
	 * @return String
	 */
	@RequestMapping("/myPageDocList.do")
	public String myPageDocList(@ModelAttribute SessionVO sessionVO,Model model,@RequestParam HashMap<String,Object> map) {
		
		@SuppressWarnings("unused")
		Locale locale = LocaleLibrary.setLocale(sessionVO.getSessLanguage() != null ? sessionVO.getSessLanguage() : ConfigData.getString("LANGUAGE"));
		Map<String, Object> resultMap = new HashMap<String, Object>();
				
		String myMenuType = map.get("myMenuType") != null ? map.get("myMenuType").toString() : Constant.DOCUMENT_LIST_TYPE_OWNER;
				
		try {		
					
			// 문서등록 사전정보 가져오기 :: 문서등록 INCLUDE 공통 
			// 문서유형/첨부파일제약조건/보안등급/보존년한/조회등급(직급)			
			// 문서수정 - 버전설정
			resultMap = documentService.documentListForInserting(map, sessionVO);

			// 문서공통 객체 CallBack
			CommonUtil.docSessionToModel(model, resultMap, sessionVO);			
			
			// 각 문서함 선택에 따른 메뉴명
			
			
		}catch (BizException e){
			logger.error(e.getMessage());
		} catch (Exception e)	{			
			logger.error(e.getMessage());
		}
				
		model.addAttribute("menuType",Constant.TOPMENU_MYDOC);
		model.addAttribute("myMenuType",myMenuType);
		model.addAttribute("contextRoot",sessionVO.getSessContextRoot());
		// 작업카트 - 다운로드 (최대 다운로드개수)
		model.addAttribute("tempDocDownloadMax", ConfigData.getInt("TEMP_DOC_DOWNLOAD_MAX"));
		
		return "mypage/myPageDocList";		
	}
	
	/**
	 * <pre>
	 * 1. 개용 : 나의문서 리스트 가져오기.
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : authDocumentList
	 * @param sessionVO
	 * @param model
	 * @param map
	 * @return
	 */
	@RequestMapping("/authDocumentList.do")
	public ModelAndView authDocumentList(@ModelAttribute SessionVO sessionVO, Model model, @RequestParam HashMap<String,Object> map,
			HttpServletRequest request) {
		
		Locale locale = LocaleLibrary.setLocale(sessionVO.getSessLanguage() != null ? sessionVO.getSessLanguage() : ConfigData.getString("LANGUAGE"));
		Map<String, Object> resultMap = new HashMap<String, Object>();
		HashMap<String, Object> param = new HashMap<String, Object>();
		
		// 입력 파라미터 유효성 체크
		param.put("strIndex", StringUtil.getMapString(map, "strIndex"));
		param.put("strKeyword1", StringUtil.getMapString(map, "strKeyword1"));		
		param.put("orderCol", StringUtil.getMapString(map, "sidx", "DOC_NAME"));
		param.put("orderType", StringUtil.getMapString(map, "sord", "ASC"));
		param.put("page_size", StringUtil.getMapString(map, "rows", sessionVO.getSessPage_size()));		
		param.put("nPage",CommonUtil.getPage(map));
				
		//상세검색 입력 파라미터 유효성 체크
		param.put("keyword",map.get("keyword") != null ? map.get("keyword") : ""); 							// 키워드
		param.put("page_name",map.get("page_name") != null ? map.get("page_name") : ""); 				// 첨부파일명
		
		List<String> map_id = new ArrayList<String>();
		String listType = StringUtil.getMapString(map, Constant.DOCUMENT_LIST_TYPE);
		String getMapID = map.get("map_id") != null ? map.get("map_id").toString() : "";
		String oper = StringUtil.getMapString(map, "oper");																// Excel Down 처리 : 파라미터=oper
		
		// 검색기간 세팅
		// 최신문서함일 경우는 검색기간이 없을경우 일주일로 세팅.
		if(Constant.DOCUMENT_LIST_TYPE_RECENTLYDOC.equals(listType)) {
			param.put("sdate", StringUtil.getMapString(map, "sdate") != "" ? StringUtil.getMapString(map, "sdate").replaceAll("-", "") : StringUtil.getDay(7));
			param.put("edate", StringUtil.getMapString(map, "edate") != "" ? StringUtil.getMapString(map, "edate").replaceAll("-", "") : StringUtil.getToday());
		} else {
			param.put("sdate", StringUtil.getMapString(map, "sdate").replaceAll("-", ""));
			param.put("edate", StringUtil.getMapString(map, "edate").replaceAll("-", ""));
		}
		
		try {
			
			// 1. 엑셀다운로드시 파라미터 재정의
			if(oper.equals(Constant.EXCEL_FORMAT)) {
				param.put("oper", oper);
				param.put("nPage", 1);	// 엑셀 저장 row수
				param.put("page_size", ConfigData.getInt("EXCEL_MAX_LIMIT"));
				param.put("strKeyword",map.get("strKeyword") != null ? new String(map.get("strKeyword").toString().getBytes("8859_1"),"utf-8") : "");
			}
			
			// 2. 문서상세검색 CallBack 함수
			CommonUtil.docDetailSearch(sessionVO, request, map, cacheService, folderService, documentService, param);
			
			// 3. ListType에 따른 별도 조건 분리 및 조회결과 가져오기
			param.put("select_action",listType);
			param.put("user_id", sessionVO.getSessId().toString());
			
			// 4. select_action 에 따른 검색조건 추가
			CommonUtil.setSearchColumn(listType, param);
			
			// 5.strIndex에 따른 검색조건 추가 strKeyword1 
			CommonUtil.setColumNm(map, param);

			// 6.리스트타입에 따른 분리 처리
			if(Constant.DOCUMENT_LIST_TYPE_OWNER.equals(listType) 
					|| Constant.DOCUMENT_LIST_TYPE_TRASHCAN.equals(listType)
					|| Constant.DOCUMENT_LIST_TYPE_RECENTLYDOC.equals(listType)){			// 내소유문서 || 개인휴지통 || 최신문서함
				
				// 업무문서함인 경우 프로젝트함을 자동으로 포함한다.(사이트 커스터마이징영역)
				if(getMapID.equals(Constant.MAP_ID_DEPT)){
					map_id.add(Constant.MAP_ID_PROJECT);
				}
				
				map_id.add(getMapID);
				param.put("map_id", map_id);
				
			} else if (Constant.DOCUMENT_LIST_TYPE_FAVORITE.equals(listType)) {				// 즐겨찾기 폴더 || 문서					
				param.put("is_virtual", StringUtil.getMapString(map, "is_virtual"));
				param.put("folder_id", StringUtil.getMapString(map, "folder_id"));				
			} else if (Constant.DOCUMENT_LIST_TYPE_SHARE_FOLDER.equals(listType)) {		// 공유 폴더 || 문서				
				param.put("folder_id", StringUtil.getMapString(map, "folder_id"));
			}
			
			// 6.1 Paging 처리 이미지경로 
			param.put("contextRoot",sessionVO.getSessContextRoot());
			
			// 7. 문서리스트 조회처리(공유폴더인 경우 workDocumentBasicList			
			if (Constant.DOCUMENT_LIST_TYPE_SHARE_FOLDER.equals(listType)) {
				param.put("strLink","javascript:exsoftMypageFunc.event.docFunctions.gridPage");
				resultMap = documentService.workDocumentBasicList(param);
			}else {
				resultMap = documentService.myDocumentBasicList(param);
			}
			
			// 8. Excel Down 처리 :: 목록
			if(oper.equals(Constant.EXCEL_FORMAT)) {
				
				CommonUtil.getExcelList(resultMap, model);
				CommonUtil.setExcelFormat(model, listType);				
				
				return new ModelAndView(new ExcelView());
			}
			
		} catch(BizException e){
			resultMap.put("result",Constant.RESULT_FALSE);
			resultMap.put("message",e.getMessage());				
		}catch(Exception e)	{										
			resultMap.put("result",Constant.RESULT_FALSE);
			resultMap.put("message",messageSource.getMessage("common.system.error",new Object[0],locale));			
		}

		ModelAndView mav = new ModelAndView("jsonView",resultMap);
		return mav;
	}
	
	/**
	 * <pre>
	 * 1. 개용 : 사용자 휴지통 관리 : 휴지통 삭제, 비우기, 복원 등  
	 * 2. 처리내용 :
	 * </pre>
	 * @Method Name : authWasteDocDelete
	 * @param sessionVO
	 * @param model
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/authWasteDocDelete.do", method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> authWasteDocDelete(@ModelAttribute SessionVO sessionVO, Model model, @RequestParam HashMap<String,Object> map,
			HttpServletRequest request) {
		Locale locale = LocaleLibrary.setLocale(sessionVO.getSessLanguage() != null  ? sessionVO.getSessLanguage() : ConfigData.getString("LANGUAGE"));
		
		List<HashMap<String,Object>> docList = new ArrayList<HashMap<String,Object>>();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String type = map.get(Constant.TYPE) != null ? map.get(Constant.TYPE).toString() : "";
		try{
			if(!StringUtil.isEmpty(type) && type.equals(Constant.TRASH_DELETE)){
				//TODO : 휴지통 삭제
				docList = documentService.documentValidList(map);
				resultMap = documentService.trashDeleteDoc(docList, map, sessionVO);
				
			} else if(!StringUtil.isEmpty(type) && type.equals(Constant.TRASH_ALL_DELETE)){
				//TODO : 휴지통 비우기
				docList = documentService.authWasteDocValidList(map, sessionVO);
				resultMap = documentService.trashDeleteDoc(docList, map, sessionVO);
			} else if(!StringUtil.isEmpty(type) && type.equals(Constant.ACTION_RESTORE)) {
				//TODO : 문서복원
				docList = documentService.restoreValidList(map);
				resultMap = documentService.restoreDocument(docList, map, sessionVO);
			}
		} catch(BizException e){
			resultMap.put("result",Constant.RESULT_FALSE);
			resultMap.put("message",e.getMessage());	
		} catch(Exception e){
			resultMap.put("result",Constant.RESULT_FALSE);
			resultMap.put("message",messageSource.getMessage("common.system.error",new Object[0],locale));		
		}
		
		return resultMap;
	}
	
	/**
	 * 만기문서 보조기간을 변경처리한다.
	 * @param sessionVO
	 * @param model
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/expiredDocument.do", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> expiredDocument(@ModelAttribute SessionVO sessionVO, Model model, @RequestParam HashMap<String, Object> map) {
		Locale locale = LocaleLibrary.setLocale(sessionVO.getSessLanguage() != null  ? sessionVO.getSessLanguage() : ConfigData.getString("LANGUAGE"));
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		try{
			// 내 만기 문서 > 보존기간을 변경한다.
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
	 * 1. 개용 : 임시작업함 문서 제외
	 * 2. 처리내용 : 임시작업함에 추가된 문서를 제외한다.
	 * </pre>
	 * @Method Name : tempDocumentDelete
	 * @param sessionVO
	 * @param model
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/tempDocumentDelete.do", method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> tempDocumentDelete(@ModelAttribute SessionVO sessionVO, Model model, @RequestParam HashMap<String,Object> map,
			HttpServletRequest request) {
		
		Locale locale = LocaleLibrary.setLocale(sessionVO.getSessLanguage() != null ? sessionVO.getSessLanguage() : ConfigData.getString("LANGUAGE"));
		List<HashMap<String,Object>> docList = new ArrayList<HashMap<String,Object>>();
		
		Map<String,Object> resultMap = new HashMap<String,Object>();

		try{
			map.put("is_type", Constant.DELETE);
			docList = documentService.tempDocValidList(map, sessionVO);
			resultMap = documentService.tempDocDelete(docList, map, sessionVO);
			
			// 제품 타입
			model.addAttribute("versionInfo",ConfigData.getString("VERSION_INFO"));
			
		} catch(BizException e){
			resultMap.put("result",Constant.RESULT_FALSE);
			resultMap.put("message",e.getMessage());	
		} catch(Exception e){
			resultMap.put("result",Constant.RESULT_FALSE);
			resultMap.put("message",messageSource.getMessage("common.system.error",new Object[0],locale));		
		}
		
		return resultMap;
		
	}
	
	/**
	 * <pre>
	 * 1. 개용 : 임시작업함 > 관련문서 설정
	 * 2. 처리내용 : 관련문서등록/validation check
	 * </pre>
	 * @Method Name tempRefDocument
	 * @param sessionVO
	 * @param model
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/tempRefDocument.do", method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> tempRefDocument(@ModelAttribute SessionVO sessionVO, Model model, @RequestParam HashMap<String,Object> map,
			HttpServletRequest request){
		Locale locale = LocaleLibrary.setLocale(sessionVO.getSessLanguage() != null ? sessionVO.getSessLanguage() : ConfigData.getString("LANGUAGE"));
		Map<String,Object> resultMap = new HashMap<String,Object>();
		List<HashMap<String,Object>> docList = new ArrayList<HashMap<String,Object>>();
		String type = map.get(Constant.TYPE) != null ? map.get(Constant.TYPE).toString() : "";
		
		try {
			if(!StringUtil.isEmpty(type) && type.equals("CHECK")){
				// 등록된 관련문서 리스트에 메인문서가 있는지 확인한다.
				map.put("is_type", "CHECK");
				resultMap = documentService.tempRefDocIsUsing(map);
			} else {
				map.put("is_type", "INSERT");
				// 관련문서 등록
				docList = documentService.tempRefValidList(map);
				resultMap = documentService.tempRefInsert(docList, map);
			}
		} catch(BizException e){
			resultMap.put("result",Constant.RESULT_FALSE);
			resultMap.put("message",e.getMessage());	
		} catch(Exception e){
			resultMap.put("result",Constant.RESULT_FALSE);
			resultMap.put("message",messageSource.getMessage("common.system.error",new Object[0],locale));		
		}
		
		return resultMap;
	}
	
	/**
	 * <pre>
	 * 1. 개용 : 임시작업함 > 다운로드 처리
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name tempDownload
	 * @param model
	 * @param map
	 * @param sessionVO
	 * @return
	 */
	@RequestMapping("/tempDownload.do")
	public ModelAndView tempDownload(Model model, @RequestParam HashMap<String,Object> map, @ModelAttribute SessionVO sessionVO){
		
		@SuppressWarnings("unused")
		Locale locale = LocaleLibrary.setLocale(sessionVO.getSessLanguage() != null ? sessionVO.getSessLanguage() : ConfigData.getString("LANGUAGE"));
		
		HashMap<String, Object> pageInfo = new HashMap<String, Object>();
		List<PageVO> pageList = new ArrayList<PageVO>();
		
		try{			
			
			// 0. doc_id로 pageList를 구성한다.
			pageInfo = documentService.getPageListByTempDocList(map);
			
			// 1.다운로드 대상 목록 및 PageVO 객체 구하기[APPLIANCE VERSION]
			if(ConfigData.getString("VERSION_INFO") != null && 
					ConfigData.getString("VERSION_INFO").equals(Constant.PRODUCT_EDMS_APPLIANCE)) {
				pageList = commonService.setPageLocalList(pageInfo);
			}else {
				pageList = commonService.setPageList(pageInfo);
			}

			// 2.첨부파일 조회이력 처리
			if(pageList != null && pageList.size() > 0)	{
				commonService.pageHtWrite(pageList, sessionVO);
			}
						
			// 3.다운로드 VIEW 		
			model.addAttribute("isZip",map.get("isZip") != null ? map.get("isZip").toString() : Constant.F);
			model.addAttribute("pageList",pageList);
						
			
		} catch(Exception e) {
			logger.error(e.getMessage());
			return new ModelAndView("error/noPage");
		} 		
		return new ModelAndView(new DownloadView());
	}
	
}

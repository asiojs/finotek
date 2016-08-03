package kr.co.exsoft.document.controller;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import kr.co.exsoft.common.service.CacheService;
import kr.co.exsoft.common.service.CommonService;
import kr.co.exsoft.common.vo.CommentVO;
import kr.co.exsoft.common.vo.MenuAuthVO;
import kr.co.exsoft.common.vo.SessionVO;
import kr.co.exsoft.document.service.DocumentService;
import kr.co.exsoft.document.service.PageService;
import kr.co.exsoft.document.service.TypeService;
import kr.co.exsoft.document.vo.AttrVO;
import kr.co.exsoft.document.vo.DocumentVO;
import kr.co.exsoft.eframework.configuration.Constant;
import kr.co.exsoft.eframework.exception.BizException;
import kr.co.exsoft.eframework.library.ExcelView;
import kr.co.exsoft.eframework.library.LocaleLibrary;
import kr.co.exsoft.eframework.util.CommonUtil;
import kr.co.exsoft.eframework.util.ConfigData;
import kr.co.exsoft.eframework.util.StringUtil;
import kr.co.exsoft.folder.service.FolderService;

import org.apache.commons.collections.map.CaseInsensitiveMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
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
 * @since 2014.07.17
 * @version 3.0
 *
 */
@Controller
@SessionAttributes("sessionVO")
@RequestMapping("/document")
public class DocumentAuthController {

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
	private MessageSource messageSource;
	
    @Autowired
    private DefaultBeanValidator beanValidator;
    
    @Autowired
	private FolderService folderService;
    
	protected static final Log logger = LogFactory.getLog(DocumentAuthController.class);
	
	
	
	/**
	 * <pre>
	 * 1. 개용 : 문서 상세 조회
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : documentDetail
	 * @param model
	 * @param sessionVO
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/documentDetail.do", method=RequestMethod.POST)
	@ResponseBody 
	public Map<String,Object> documentDetail(Model model, @ModelAttribute SessionVO sessionVO, @RequestParam HashMap<String,Object> map) {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Locale locale = LocaleLibrary.setLocale(sessionVO.getSessLanguage() != null  ? sessionVO.getSessLanguage() : ConfigData.getString("LANGUAGE"));
		
		try {
			
			// 그룹+프로젝트 그룹 ID
			List<String> groupList = sessionVO.getSessProjectGroup();
			groupList.add(sessionVO.getSessGroup_id());

			map.put("table_nm",Constant.DOC_TABLE);
			map.put("group_id_list",groupList);
			map.put("user_id", sessionVO.getSessId());
			map.put("actionType",Constant.ACTION_READ);
			
			resultMap = documentService.docCommonView(map, sessionVO);
			
		} catch (Exception e) {
			resultMap.put("result",Constant.RESULT_FALSE);
			resultMap.put("message",messageSource.getMessage("common.system.error",new Object[0],locale));
		}
		
		return resultMap;
	}
	
	/**
	 * <pre>
	 * 1. 개용 : 문서 버전 목록조회
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : documentVersionList
	 * @param model
	 * @param sessionVO
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/documentVersionList.do", method=RequestMethod.POST)
	@ResponseBody 
	public Map<String,Object> documentVersionList(Model model, @ModelAttribute SessionVO sessionVO, @RequestParam HashMap<String,Object> map) {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Locale locale = LocaleLibrary.setLocale(sessionVO.getSessLanguage() != null  ? sessionVO.getSessLanguage() : ConfigData.getString("LANGUAGE"));
		
		try {
			
			// map=table_id 파라미터 클라이언트측에서 넘기는 것으로 변경처리
			resultMap = documentService.docAllVersionInfoList(map);
			
		} catch (Exception e) {
			resultMap.put("result",Constant.RESULT_FALSE);
			resultMap.put("message",messageSource.getMessage("common.system.error",new Object[0],locale));
		}
		
		return resultMap;
	}
	
	/**
	 * <pre>
	 * 1. 개용 : 문서 이력 목록조회
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : documentHtList
	 * @param model
	 * @param sessionVO
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/documentHtList.do", method=RequestMethod.POST)
	@ResponseBody 
	public Map<String,Object> documentHtList(Model model, @ModelAttribute SessionVO sessionVO, @RequestParam HashMap<String,Object> map) {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Locale locale = LocaleLibrary.setLocale(sessionVO.getSessLanguage() != null  ? sessionVO.getSessLanguage() : ConfigData.getString("LANGUAGE"));
		
		try {
			
			resultMap = documentService.documentHtList(map);
			
		} catch (Exception e) {
			resultMap.put("result",Constant.RESULT_FALSE);
			resultMap.put("message",messageSource.getMessage("common.system.error",new Object[0],locale));
		}
		
		return resultMap;
	}
	
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 :  문서 이력 목록 조회 :: GRID VERSION
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : docHistoryList
	 * @param model
	 * @param sessionVO
	 * @param map
	 * @return Map<String,Object>
	 */
	@RequestMapping(value="/docHistoryList.do", method=RequestMethod.POST)
	@ResponseBody 
	public Map<String,Object> docHistoryList(Model model, @ModelAttribute SessionVO sessionVO, @RequestParam HashMap<String,Object> map) {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		HashMap<String, Object> param = new HashMap<String, Object>();
		
		Locale locale = LocaleLibrary.setLocale(sessionVO.getSessLanguage() != null  ? sessionVO.getSessLanguage() : ConfigData.getString("LANGUAGE"));
		
		param.put("strIndex",map.get("strIndex") != null ? map.get("strIndex") : "" );
		param.put("orderCol",map.get("sidx") != null ? map.get("sidx") : "action_date");				
		param.put("orderType",map.get("sord") != null ? map.get("sord") : "DESC");
		param.put("page_size",map.get("rows") != null ? map.get("rows") : sessionVO.getSessPage_size());
		param.put("nPage",CommonUtil.getPage(map));
		param.put("doc_id",map.get("doc_id") != null ? map.get("doc_id") : "" );
		param.put("contextRoot",sessionVO.getSessContextRoot());
		
		try {
			
			// 문서보기 이력조회 공통처리
			resultMap = documentService.docHistoryList(param);
			
			
		} catch(BizException e){
			resultMap.put("result",Constant.RESULT_FALSE);
			resultMap.put("message",e.getMessage());				
		}catch (Exception e) {
			resultMap.put("result",Constant.RESULT_FALSE);
			resultMap.put("message",messageSource.getMessage("common.system.error",new Object[0],locale));
		}
		
		return resultMap;
	}
	/**
	 * 
	 * <pre>
	 * 1. 개용 :  문서 상세 이력 엑셀 다운로드 처리 :: GRID VERSION
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : docHistoryListEX
	 * @param model
	 * @param sessionVO
	 * @param map
	 * @return Map<String,Object>
	 */
	@RequestMapping(value="/docHistoryListEX.do")
	@ResponseBody 
	public ModelAndView docHistoryListEX(Model model, @ModelAttribute SessionVO sessionVO, @RequestParam HashMap<String,Object> map) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		HashMap<String, Object> param = new HashMap<String, Object>();
		
		Locale locale = LocaleLibrary.setLocale(sessionVO.getSessLanguage() != null  ? sessionVO.getSessLanguage() : ConfigData.getString("LANGUAGE"));
		
		param.put("strIndex",map.get("strIndex") != null ? map.get("strIndex") : "" );
		param.put("orderCol",map.get("sidx") != null ? map.get("sidx") : "action_date");				
		param.put("orderType",map.get("sord") != null ? map.get("sord") : "DESC");
		param.put("page_size",map.get("rows") != null ? map.get("rows") : sessionVO.getSessPage_size());
		param.put("nPage",CommonUtil.getPage(map));
		param.put("doc_id",map.get("doc_id") != null ? map.get("doc_id") : "" );
		param.put("contextRoot",sessionVO.getSessContextRoot());
		
		String oper = map.get("oper") != null ? map.get("oper").toString() : "";					// Excel Down 처리  : 파리미터=oper		
		String[]  members ;
		String[]  cell_headers ;
		int[] cell_widths;
		try {

			// 1.1 엑셀다운로드시 한글검색어 인코딩 처리
			if(oper.equals(Constant.EXCEL_FORMAT)) {				
				param.put("strKeyword",map.get("strKeyword") != null ? new String(map.get("strKeyword").toString().getBytes("8859_1"),"utf-8") : "");	
			}
			
			// 문서보기 이력조회 공통처리
			resultMap = documentService.docHistoryList(param);
			
			// Excel Down 처리 :: 목록
			if(oper.equals(Constant.EXCEL_FORMAT)) {

				CommonUtil.getExcelList(resultMap,model);
				
				members = new String[]{ "action_date","action_name","actor_nm", "version_no","action_place"};
				cell_headers = new String[]{ "일시","수행작업", "작업자", "버전", "비고"};
				cell_widths = new int[]{ 30,20,15,15,15};

				model.addAttribute("members",members);
				model.addAttribute("cell_headers",cell_headers);
				model.addAttribute("cell_widths",cell_widths);
				model.addAttribute("fileName","downLoad.xls");
				model.addAttribute("groupField","type_name");

				
				return new ModelAndView(new ExcelView());
			}
			
		} catch(BizException e){
			resultMap.put("result",Constant.RESULT_FALSE);
			resultMap.put("message",e.getMessage());				
		}catch (Exception e) {
			resultMap.put("result",Constant.RESULT_FALSE);
			resultMap.put("message",messageSource.getMessage("common.system.error",new Object[0],locale));
		}
		
		ModelAndView mav = new ModelAndView("jsonView",resultMap);
		return mav;
	}
	
	/**
	 * <pre>
	 * 1. 개용 : 문서 버전  삭제
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : deleteVersion
	 * @param model
	 * @param sessionVO
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/deleteVersion.do", method=RequestMethod.POST)
	@ResponseBody 
	public Map<String,Object> deleteVersion(Model model, @ModelAttribute SessionVO sessionVO, @RequestParam HashMap<String,Object> map) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Locale locale = LocaleLibrary.setLocale(sessionVO.getSessLanguage() != null  ? sessionVO.getSessLanguage() : ConfigData.getString("LANGUAGE"));
		List<HashMap<String, Object>> docList = new ArrayList<HashMap<String, Object>>();
		
		try {
			HashMap<String, Object> doc = new HashMap<String, Object>();
			doc.put("doc_id", StringUtil.getMapString(map, "doc_id"));
			doc.put("table_nm", Constant.DOC_TABLE);
			docList.add(doc);
			
			resultMap = documentService.versionDocDelete(docList, map, sessionVO);
			
		} catch (BizException e) {
			resultMap.put("result",Constant.RESULT_FALSE);
			resultMap.put("message",e.getMessage());				
		} catch (Exception e)	{										
			resultMap.put("result",Constant.RESULT_FALSE);
			resultMap.put("message",messageSource.getMessage("common.system.error",new Object[0],locale));			
		}
		
		return resultMap;
	}
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 사용자 레이아웃 초기화면 
	 * 2. 처리내용 : 업무문서함 추후 :: 초기화면 변경예정
	 * </pre>
	 * @Method Name : userLayout
	 * @param sessionVO
	 * @param model
	 * @param map
	 * @param request
	 * @return String
	 */
	@RequestMapping("/userLayout.do")
	public String userLayout(@ModelAttribute SessionVO sessionVO,Model model,@RequestParam HashMap<String,Object> map,HttpServletRequest request) {
				
		// 좌측 메뉴 URL /document/workDocList.do => /user/mainContent.do 변경
		String url = map.get("href") != null ? map.get("href").toString() : sessionVO.getSessContextRoot()+"/user/mainContent.do";
		
		CommonUtil.setSessionToModel(model, sessionVO);
		
		model.addAttribute("contents",url);			
		model.addAttribute("user_name",sessionVO.getSessName());		
		model.addAttribute("role_id",sessionVO.getSessRole_id());
		model.addAttribute("role_nm",sessionVO.getSessRole_nm());
		model.addAttribute("user_role",Constant.USER_ROLE);
		model.addAttribute("language",sessionVO.getSessLanguage());
				
		return "userLayout";
	}
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 업무문서함
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : workDocList
	 * @param sessionVO
	 * @param model
	 * @param map
	 * @return String
	 */
	@RequestMapping("/workDocList.do")
	public String workDocList(@ModelAttribute SessionVO sessionVO,Model model,@RequestParam HashMap<String,Object> map) {
		
		@SuppressWarnings("unused")
		Locale locale = LocaleLibrary.setLocale(sessionVO.getSessLanguage() != null  ? sessionVO.getSessLanguage() : ConfigData.getString("LANGUAGE"));
		HashMap<String, Object> userAuth = new HashMap<String, Object>();
		HashMap<String, Object> param = new HashMap<String, Object>();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		/**************************************************************************************************************************************************************************
		 * ROLE에 따른 폴더/권한/문서 권한 부여처리한다. 상기메뉴에 대한 권한여부를 체크한다. XR_AUTH_MENU / XR_MENU
		 * CREATOR(일반사용자)는 해당 권한없으며, CREATOR(일반사용자)가 아닌 경우라도 XR_AUTH_MENU에 권한이 존재하지 않으면 CREATOR 가 동일하게 처리한다.
		 * 해당 부분을 세션에 둘경우 관리자에 의해 변경이 실시간 적용이 되지 않으므로, 해당 페이지 호출시에 가져오도록 처리한다.
		 * XR_MENU : M005 - 사용자
		 * 폴더관리 : M050 / 권한관리 : M051 / 문서관리 : M052
		 * 권한이 있는 경우 ALL/GROUP/TEAM 
		 * 권한이 없는 경우 EMPTY STRING
		 * 권한이 있는 경우 TREE/LIST 를 가져올 때 해당 권한을 적용하여 가져오도록 처리한다.
		 *******************************************************************************************************************************************************************************/
		
		try {
			

			
			// 1.ROLE에 따른 접근권한 정보를 가져온다.
			param.put("role_id",sessionVO.getSessRole_id());
			
			if(sessionVO.getSessRole_id().equals(Constant.USER_ROLE)) {
				
				userAuth.put("folderAuth","");
				userAuth.put("aclAuth","");
				userAuth.put("docAuth","");
				
			}else {
				
				// 1.1 폴더권한
				param.put("menu_cd",Constant.USER_FOLDER_MENU_CODE);
				userAuth.put("folderAuth",commonService.getMenuAuth(param));
				
				// 1.2 ACL권한
				param.put("menu_cd",Constant.USER_ACL_MENU_CODE);
				userAuth.put("aclAuth",commonService.getMenuAuth(param));
				
				// 1.3 문서권한
				param.put("menu_cd",Constant.USER_DOC_MENU_CODE);
				userAuth.put("docAuth",commonService.getMenuAuth(param));				
			}
			
			// 문서등록 사전정보 가져오기 :: 문서등록 INCLUDE 공통 
			// 문서유형/첨부파일제약조건/보안등급/보존년한/조회등급(직급)			
			// 문서수정 - 버전설정
			resultMap = documentService.documentListForInserting(map, sessionVO);

			@SuppressWarnings("rawtypes")
			Set set = resultMap.keySet(); 
			Object[] argArray = set.toArray(); 

			//[position, FILETOTAL, FILESIZE, sercurity, FILECNT, typeList, EXT, preservation_year]
			for( int i = 0; i < argArray.length; i++ ){				
				
				String key = (String)argArray[i];
				
				// 파일확장자인경우 문자열 가공 처리 후 전달함
				if(key.equals(Constant.FILE_EXT))	{
					CaseInsensitiveMap caseMap = (CaseInsensitiveMap)resultMap.get(key);				
					String value = caseMap.get("fval").toString();			
					caseMap.put("fval",value.substring(0,value.length()-1).replaceAll(";",","));		
					model.addAttribute(key,caseMap);
				}else {
					Object value = resultMap.get(key);
					model.addAttribute(key,value);
				}
			}
			
		} catch (Exception e)	{			
			logger.error(e.getMessage());
		}
		
		// jstl 값 추가
		model.addAttribute("menuType",Constant.TOPMENU_WORKSPACE);		//  1차고도화 TOP 메뉴 선택 
		model.addAttribute("userAuth",userAuth);
		model.addAttribute("action_create",Constant.ACTION_CREATE);
		model.addAttribute("action_update",Constant.ACTION_UPDATE);
		model.addAttribute("action_delete",Constant.ACTION_DELETE);
		model.addAttribute("action_move",Constant.ACTION_MOVE);
		model.addAttribute("action_checkin",Constant.ACTION_CHECKIN);
		model.addAttribute("acl_none",Constant.ACL_NONE);
		model.addAttribute("acl_browse",Constant.ACL_BROWSE);
		
		// 파일첨부 기본값 :: 환경설정에서 사용안함 옵션 적용시 & 제품 타입
		/*model.addAttribute("defaultFileCnt",ConfigData.getInt("DOC.DEFAULT.FILECNT"));
		model.addAttribute("defaultFileSize",ConfigData.getInt("DOC.DEFAULT.FILESIZE"));
		model.addAttribute("defaultFileTotal",ConfigData.getInt("DOC.DEFAULT.TOTAL"));*/ //commonUtil.setSessionToModel()로 이동
		model.addAttribute("defaultRefDocCnt",ConfigData.getInt("DOC.REF.FILECNT"));					
		model.addAttribute("versionInfo",ConfigData.getString("VERSION_INFO"));			 

		CommonUtil.setSessionToModel(model, sessionVO);

		return "workspace/workDocList";
	}
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 개인문서함
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : myDocList
	 * @param sessionVO
	 * @param model
	 * @param map
	 * @return String
	 */
	@RequestMapping("/myDocList.do")
	public String myDocList(@ModelAttribute SessionVO sessionVO,Model model,@RequestParam HashMap<String,Object> map) {
		
		@SuppressWarnings("unused")
		Locale locale = LocaleLibrary.setLocale(sessionVO.getSessLanguage() != null  ? sessionVO.getSessLanguage() : ConfigData.getString("LANGUAGE"));		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		try {		
		
			// 문서등록 사전정보 가져오기 :: 문서등록 INCLUDE 공통 
			// 문서유형/첨부파일제약조건/보안등급/보존년한/조회등급(직급)
			resultMap = documentService.documentListForInserting(map, sessionVO);
			
			@SuppressWarnings("rawtypes")
			Set set = resultMap.keySet();
			Object[] argArray = set.toArray();
			
			for(int i = 0; i < argArray.length; i++) {
				String key = (String)argArray[i];
				
				if(key.equals(Constant.FILE_EXT)) {
					CaseInsensitiveMap caseMap = (CaseInsensitiveMap) resultMap.get(key);
					String value = caseMap.get("fval").toString();
					caseMap.put("fval", value.substring(0, value.length()-1).replaceAll(";", ","));
					model.addAttribute(key,caseMap);
				}else {
					Object value = resultMap.get(key);
					model.addAttribute(key,value);
				}
			}
			
		} catch (Exception e)	{
			logger.error(e.getMessage());
		}
		
		model.addAttribute("menuType",Constant.TOPMENU_MYWORK);		//  1차고도화 TOP 메뉴 선택 
		// 파일첨부 기본값 :: 환경설정에서 사용안함 옵션 적용시
		/*model.addAttribute("defaultFileCnt",ConfigData.getInt("DOC.DEFAULT.FILECNT"));
		model.addAttribute("defaultFileSize",ConfigData.getInt("DOC.DEFAULT.FILESIZE"));
		model.addAttribute("defaultFileTotal",ConfigData.getInt("DOC.DEFAULT.TOTAL"));*/ //commonUtil.setSessionToModel()로 이동
		model.addAttribute("defaultRefDocCnt",ConfigData.getInt("DOC.REF.FILECNT"));
		model.addAttribute("versionInfo",ConfigData.getString("VERSION_INFO"));				
		
		CommonUtil.setSessionToModel(model, sessionVO);
		
		return "mydoc/myDocList";
	}
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 문서등록 개발 페이지 :: 추후 삭제
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : myPage
	 * @param sessionVO
	 * @param model
	 * @param map
	 * @return String
	 */
	@RequestMapping("/docRegist.do")
	public String docRegist(@ModelAttribute SessionVO sessionVO,Model model,@RequestParam HashMap<String,Object> map) {
		
		//Locale locale = LocaleLibrary.setLocale(sessionVO.getSessLanguage() != null  ? sessionVO.getSessLanguage() : ConfigData.getString("LANGUAGE"));		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		try {		
		
			// 문서등록 사전정보 가져오기 :: 문서등록 INCLUDE 공통 
			// 문서유형/첨부파일제약조건/보안등급/보존년한/조회등급(직급)			
			// 문서수정 - 버전설정
			resultMap = documentService.documentListForInserting(map, sessionVO);

			@SuppressWarnings("rawtypes")
			Set set = resultMap.keySet(); 
			Object[] argArray = set.toArray(); 

			//[position, FILETOTAL, FILESIZE, sercurity, FILECNT, typeList, EXT, preservation_year]
			for( int i = 0; i < argArray.length; i++ ){				
				
				String key = (String)argArray[i];
				
				// 파일확장자인경우 문자열 가공 처리 후 전달함
				if(key.equals(Constant.FILE_EXT))	{
					CaseInsensitiveMap caseMap = (CaseInsensitiveMap)resultMap.get(key);				
					String value = caseMap.get("fval").toString();			
					caseMap.put("fval",value.substring(0,value.length()-1).replaceAll(";",","));		
					model.addAttribute(key,caseMap);
				}else {
					Object value = resultMap.get(key);
					model.addAttribute(key,value);
				}
			}
			
			// 파일첨부 기본값 :: 환경설정에서 사용안함 옵션 적용시
			/*model.addAttribute("defaultFileCnt",ConfigData.getInt("DOC.DEFAULT.FILECNT"));
			model.addAttribute("defaultFileSize",ConfigData.getInt("DOC.DEFAULT.FILESIZE"));
			model.addAttribute("defaultFileTotal",ConfigData.getInt("DOC.DEFAULT.TOTAL"));*/  //commonUtil.setSessionToModel()로 이동
			model.addAttribute("defaultRefDocCnt",ConfigData.getInt("DOC.REF.FILECNT"));

		}catch (BizException e){
			logger.error(e.getMessage());
		} catch (Exception e)	{			
			logger.error(e.getMessage());
		}
		
		// call by reference
		CommonUtil.setSessionToModel(model, sessionVO);
		
		return "mypage/docRegist";
		
	}
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 문서 등록시 설정 정보 가져오기
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : docCommonRegist
	 * @param sessionVO
	 * @param model
	 * @param map
	 * @param request
	 * @return Map<String,Object>
	 */
	@RequestMapping(value="/docCommonRegist.do", method=RequestMethod.POST)
	@ResponseBody 
	public Map<String, Object> docCommonRegist(@ModelAttribute SessionVO sessionVO, @ModelAttribute DocumentVO documentVO,Model model, 
			@RequestParam HashMap<String, Object> map,HttpServletRequest request) {
		
		Locale locale = LocaleLibrary.setLocale(sessionVO.getSessLanguage() != null ? sessionVO.getSessLanguage() : ConfigData.getString("LANGUAGE"));		
		Map<String,Object> resultMap = new HashMap<String, Object>();
		
		try{
		
			/**************************************************************************************
			 * 문서등록 화면설정 처리 :: 폴더ID,폴더경로,맵ID
			 * 개인함 / 부서함 / 전사함 / 프로젝트함 공통으로 사용됨
			 **************************************************************************************/
			
			// 1. 폴더등록 유효성 체크 및 기본ACL/기본문서유형/기본권한 및 기본권한 속성ITEM
			resultMap = documentService.documentInfoForInserting(map, sessionVO);
			// 문서공통 객체 CallBack
			CommonUtil.docSessionToModel(model, resultMap, sessionVO);	
			
			// 2. 등록화면 전달된 파라미터 :: 톨더경로
			resultMap.put("folder_path",map.get("folder_path"));
			resultMap.put("result",Constant.RESULT_TRUE);
			
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
	 * 
	 * <pre>
	 * 1. 개용 : 문서수정 화면 유효성 체크및 정보 가져오기
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : docCommonUpdate
	 * @param sessionVO
	 * @param documentVO
	 * @param model
	 * @param map
	 * @param request
	 * @return Map<String,Object>
	 */
	@RequestMapping(value="/docCommonUpdate.do", method=RequestMethod.POST)
	@ResponseBody 
	public Map<String, Object> docCommonUpdate(@ModelAttribute SessionVO sessionVO, @ModelAttribute DocumentVO documentVO,Model model, 
			@RequestParam HashMap<String, Object> map,HttpServletRequest request) {
		
		Locale locale = LocaleLibrary.setLocale(sessionVO.getSessLanguage() != null ? sessionVO.getSessLanguage() : ConfigData.getString("LANGUAGE"));		
		Map<String,Object> resultMap = new HashMap<String, Object>();
		
		try{

			// 1.문서상세정보기 :: 파라미터 doc_id
			map.put("table_nm",Constant.DOC_TABLE);
			map.put("actionType",Constant.ACTION_CHECKOUT);
			resultMap = documentService.docCommonView(map, sessionVO);

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
	 * 
	 * <pre>
	 * 1. 개용 : 문서삭제
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : deleteDocument
	 * @param sessionVO
	 * @param model
	 * @param map
	 * @param request
	 * @return Map<String,Object>
	 *
	 */
	@RequestMapping(value="/deleteDocument.do", method=RequestMethod.POST)
	@ResponseBody 
	public Map<String, Object> deleteDocument(@ModelAttribute SessionVO sessionVO, Model model, @RequestParam HashMap<String, Object> map,
			HttpServletRequest request) {
		
		Locale locale = LocaleLibrary.setLocale(sessionVO.getSessLanguage() != null ? sessionVO.getSessLanguage() : ConfigData.getString("LANGUAGE"));
		List<HashMap<String,Object>> docList = new ArrayList<HashMap<String,Object>>();
		Map<String,Object> resultMap = new HashMap<String, Object>();		
				
		try{
			
			//삭제할 문서 목록 유효성 체크 및 구하기.
			docList = documentService.documentValidList(map);
				
			//문서 삭제 처리
			resultMap = documentService.deleteDocument(docList, map, sessionVO);
			
		} catch(BizException e){
			resultMap.put("result",Constant.RESULT_FALSE);
			resultMap.put("message",e.getMessage());	
		} catch(Exception e){
			resultMap.put("result",Constant.RESULT_FALSE);
			resultMap.put("message",messageSource.getMessage("common.system.error",new Object[0],locale));		
		}
		return null;
	}

	/**
	 * 
	 * <pre>
	 * 1. 개용 : 문서의 등록, 수정, 삭제, 이동 등 문서 변화 관리 
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : documentControl
	 * @param model
	 * @param sessionVO
	 * @param map
	 * @param documentVO
	 * @param bindingResult
	 * @param request
	 * @return Map<String,Object>
	 */
	@RequestMapping(value="/documentControl.do", method=RequestMethod.POST)
	@ResponseBody 
	public Map<String, Object> documentControl(Model model, @ModelAttribute SessionVO sessionVO, @RequestParam HashMap<String,Object> map,
			@ModelAttribute("documentVO") DocumentVO documentVO,BindingResult bindingResult,HttpServletRequest request) {
		
		Locale locale = LocaleLibrary.setLocale(sessionVO.getSessLanguage() != null  ? sessionVO.getSessLanguage() : ConfigData.getString("LANGUAGE"));
		List<HashMap<String,Object>> docList = new ArrayList<HashMap<String,Object>>();
		Map<String, Object> resultMap = new HashMap<String, Object>();		
		String type =  map.get(Constant.TYPE) != null ? map.get(Constant.TYPE).toString() : "";
		
		try{
			
			if(!StringUtil.isEmpty(type) && type.equals(Constant.ACTION_DELETE)) {
				// 문서삭제
				docList = documentService.documentValidList(map);
				resultMap = documentService.deleteDocument(docList, map, sessionVO);
				
			} else if(!StringUtil.isEmpty(type) && type.equals(Constant.ACTION_CHECKIN)) {
				// 체크인
				resultMap = documentService.documentUpdate(map, documentVO, sessionVO);
				
			} else if(!StringUtil.isEmpty(type) && type.equals(Constant.ACTION_MOVE)) {			
				// 문서 이동
				docList = documentService.moveCopyDocValidList(map, sessionVO);
				resultMap = documentService.moveDocListUpdate(docList,map,sessionVO);
				
			} else if(!StringUtil.isEmpty(type) && type.equals(Constant.ACTION_COPY)){
				// 문서 복사
				docList = documentService.moveCopyDocValidList(map, sessionVO);
				resultMap = documentService.copyDocListUpdate(docList, map, sessionVO);
				
			} else if(!StringUtil.isEmpty(type) && type.equals(Constant.ACTION_ADD_TO_FAVORITES)) {
				// 문서 즐겨찾기 추가
				docList =  documentService.favoriteDocValidList(map, sessionVO);
				resultMap = documentService.favoriteInsert(docList,map,sessionVO);
											
			} else if(!StringUtil.isEmpty(type) && type.equals(Constant.ACTION_DELETE_FAVORITES)) {
				//TODO : 문서 즐겨찾기 삭제
				docList = documentService.favoriteDocValidList(map, sessionVO);
				resultMap = documentService.favoriteDelete(docList,map,sessionVO);
				
			} else if(!StringUtil.isEmpty(type) && type.equals(Constant.ACTION_CANCEL_CHECKOUT)) {
				// 체크아웃취소
				//doc_id, is_locked
				docList = documentService.cancelCheckoutValidList(map, sessionVO);
				for(HashMap<String,Object> listMap : docList){
					listMap.put("type", Constant.ACTION_CANCEL_CHECKOUT);
					resultMap = documentService.documentUpdate(listMap, documentVO, sessionVO);
				}				
				
			} else if(!StringUtil.isEmpty(type) && type.equals(Constant.ACTION_ADD_TO_TEMPWORK)){
				// 임시작업함 추가
				map.put("is_type", Constant.INSERT);
				docList = documentService.tempDocValidList(map, sessionVO);
				resultMap = documentService.tempDocInsert(docList, map, sessionVO);							
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
	 * 
	 * <pre>
	 * 1. 개용 : 개인/업무문서함 문서등록/수정 처리
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : docSubmit
	 * @param sessionVO
	 * @param documentVO
	 * @param model
	 * @param map
	 * @param request
	 * @return Map<String,Object>
	 */
	@RequestMapping(value="/docSubmit.do", method=RequestMethod.POST)
	@ResponseBody 
	public Map<String, Object> docSubmit(@ModelAttribute SessionVO sessionVO, Model model,@ModelAttribute DocumentVO documentVO, 
			@RequestParam HashMap<String, Object> map,HttpServletRequest request) {
		
		Locale locale = LocaleLibrary.setLocale(sessionVO.getSessLanguage() != null ? sessionVO.getSessLanguage() : ConfigData.getString("LANGUAGE"));

		List<HashMap<String,Object>> attrList = new ArrayList<HashMap<String,Object>>();
		
		Map<String,Object> resultMap = new HashMap<String, Object>();		
		DocumentVO docVO = new DocumentVO();
		
		boolean isDebug = true;
		
		try {

			// 1. 입력/수정 파라미터 유효성 체크
			docVO = documentService.writeDocValid(map, documentVO, sessionVO);
			
			// 2. 확장속성이 존재하는 경우			
			if(map.get("is_extended") != null && map.get("is_extended").toString().equals(Constant.T))	{	
				attrList = documentService.docExtendedAttrList(request,docVO.getDoc_type()); 				
			}
			
			// 최종 배포시에 삭제처리한다.
			if(isDebug)	{
				
				logger.info("구분="+map.get("isType"));
				logger.info("버전="+map.get("version_type"));
				logger.info("map_id="+documentVO.getMap_id());
				logger.info("문서ID="+documentVO.getDoc_id());
				logger.info("문서제목="+documentVO.getDoc_name());
				logger.info("문서유형="+documentVO.getDoc_type());
				logger.info("page_cnt="+documentVO.getPage_cnt());
				logger.info("문서ROOT_ID="+documentVO.getRoot_id());
				logger.info("문서REF_ID="+documentVO.getRef_id());				
				logger.info("is_current="+documentVO.getIs_current());
				logger.info("is_locked="+documentVO.getIs_locked());
				logger.info("lock_owner="+documentVO.getLock_owner());
				logger.info("lock_date="+documentVO.getLock_date());				
				logger.info("version_no="+documentVO.getVersion_no());
				logger.info("version_note="+documentVO.getVersion_note());
				logger.info("preservation_year="+documentVO.getPreservation_year());
				logger.info("expired_date="+documentVO.getExpired_date());
				logger.info("is_expired="+documentVO.getIs_expired());				
				logger.info("doc_status="+documentVO.getDoc_status());
				logger.info("acl_id="+documentVO.getAcl_id());				
				logger.info("creator_id="+documentVO.getCreator_id());
				logger.info("creator_name="+documentVO.getCreator_name());				
				logger.info("is_inherit_acl="+documentVO.getIs_inherit_acl());
				logger.info("access_grade="+documentVO.getAccess_grade());
				logger.info("folder_id="+documentVO.getFolder_id());
				logger.info("security_level="+documentVO.getSecurity_level());
				logger.info("keyword="+documentVO.getKeyword());
				logger.info("owner_id="+documentVO.getOwner_id());
				logger.info("doc_description="+documentVO.getDoc_description());
				logger.info("is_share="+documentVO.getIs_share());
				logger.info("pre_doc_id="+documentVO.getPre_doc_id());				
				logger.info("refList="+documentVO.getRefList().size());
				logger.info("multiFolders="+documentVO.getMultiFolders().size());
				logger.info("insertFileList="+documentVO.getInsertFileList().size());
				logger.info("delFileList"+documentVO.getDelFileList().size());
				
			}					
			
			// 3. 데이터베이스 처리
			resultMap = documentService.writeDocProc(map,docVO,attrList,sessionVO);			
			
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
	 * 
	 * <pre>
	 * 1. 개용 : 문서ID 기준 관련 문서 가져오기 
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : documentRelationDocByDoc_id
	 * @param model
	 * @param sessionVO
	 * @param map
	 * @return Map<String,Object>
	 */
	@RequestMapping(value="/documentRelationDocByDoc_id.do", method=RequestMethod.POST)
	@ResponseBody 
	public Map<String, Object> documentRelationDocByDoc_id(Model model, @ModelAttribute SessionVO sessionVO, 
			@RequestParam HashMap<String,Object> map) {
		
		Locale locale = LocaleLibrary.setLocale(sessionVO.getSessLanguage() != null  ? sessionVO.getSessLanguage() : ConfigData.getString("LANGUAGE"));
		
		Map<String, Object> resultMap = new HashMap<String, Object>();		
		map.put("user_id", sessionVO.getSessId());
		map.put("manage_group_id", sessionVO.getSessManage_group());
		
		// 그룹+프로젝트 그룹 ID
		String[] group_id_list = sessionVO.getSessProjectGroup().toArray(new String[sessionVO.getSessProjectGroup().size()+1]);
		group_id_list[sessionVO.getSessProjectGroup().size()] = sessionVO.getSessGroup_id();
		map.put("group_id_list",group_id_list);
		
		try {
			
			resultMap = documentService.documentRelationDocByDoc_id(map);
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
	 * 1. 개용 : 문서ID 기준 첨부파일 가져오기
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : documentAttachFileByDoc_id
	 * @param model
	 * @param sessionVO
	 * @param map
	 * @return Map<String,Object>
	 */
	@RequestMapping(value="/documentAttachFileByDoc_id.do", method=RequestMethod.POST)
	@ResponseBody 
	public Map<String, Object> documentAttachFileByDoc_id(Model model, @ModelAttribute SessionVO sessionVO, @RequestParam HashMap<String,Object> map) {
		
		Locale locale = LocaleLibrary.setLocale(sessionVO.getSessLanguage() != null  ? sessionVO.getSessLanguage() : ConfigData.getString("LANGUAGE"));
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		try {
			
			resultMap = pageService.comDocPageList(map);
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
	 * <pre>
	 * 1. 개용 : 사용자 문서 보존기간연장
	 * 2. 처리내용 : 
	 * </pre>
	 * 
	 * @Method Name : authExtendDocument
	 * @param sessionVO
	 * @param model
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/authExtendDocument.do", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> authExtendDocument(@ModelAttribute SessionVO sessionVO, Model model, @RequestParam HashMap<String, Object> map) {
		
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
	 * 
	 * <pre>
	 * 1. 개용 : URL 링크복사 관리 
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : copyUrlLink
	 * @param sessionVO
	 * @param model
	 * @param map
	 * @return Map<String,Object>
	 */
	@RequestMapping(value="/copyUrlLink.do", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> copyUrlLink(@ModelAttribute SessionVO sessionVO, Model model, @RequestParam HashMap<String, Object> map,
			HttpServletRequest req){
		
		Locale locale = LocaleLibrary.setLocale(sessionVO.getSessLanguage() != null  ? sessionVO.getSessLanguage() : ConfigData.getString("LANGUAGE"));
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		String urlInfo = "";
		
		try{
			
			// 1. URL 복사 유효기간 설정
			resultMap = documentService.urlLinkInfo(map);
			
			// 2. 서버URL 
			urlInfo = req.getScheme()+"://" + InetAddress.getLocalHost().getHostAddress() + ((req.getServerPort() == 80) ? "" : ":"+req.getServerPort()) +  req.getContextPath();
			resultMap.put("urlInfo",urlInfo+"/external/urlDownLoad.do?params=");
			
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
	 * 1. 내용 : 소유권 이전
	 * 2. 처리내용 : 
	 * </pre>
	 * 
	 * @Method Name : changeOwner
	 * @param sessionVO
	 * @param model
	 * @param map
	 * @return
	 */
	@RequestMapping(value="changeOwner.do", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> changeOwner(@ModelAttribute SessionVO sessionVO, Model model, @RequestParam HashMap<String, Object> map) {
		Locale locale = LocaleLibrary.setLocale(sessionVO.getSessLanguage() != null  ? sessionVO.getSessLanguage() : ConfigData.getString("LANGUAGE"));
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		try {
			
			// 1. .소유권 변경 실행
			documentService.changeDocOwner(map, sessionVO);
			
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
	 * 업무/개인 문서함 목록 리스트 가져오기
	 * @param model
	 * @param sessionVO
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/workDocumentList.do")
	@ResponseBody 
	public ModelAndView workDocumentList(Model model, @ModelAttribute SessionVO sessionVO, @RequestParam HashMap<String,Object> map, HttpServletRequest request) {
		
		Locale locale = LocaleLibrary.setLocale(sessionVO.getSessLanguage() != null  ? sessionVO.getSessLanguage() : ConfigData.getString("LANGUAGE"));
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		HashMap<String, Object> param = new HashMap<String, Object>();
		List<String> folder_id_list = new ArrayList<String>(); 
		List<AttrVO>  attrList = new ArrayList<AttrVO>();
		
		String folder_id =  map.get("folder_id") != null ? map.get("folder_id").toString() : "";
		String is_extended =  map.get("is_extended") != null ? map.get("is_extended").toString() : "";
		String doc_type = map.get("doc_type") != null ?map.get("doc_type").toString() : "";
		String child_include = map.get("child_include") != null ?map.get("child_include").toString() : "";
		String oper = map.get("oper") != null ? map.get("oper").toString() : "";							// Excel Down 처리  : 파리미터=oper
		
		//입력 파라미터 유효성 체크
		param.put("strIndex",map.get("strIndex") != null ? map.get("strIndex") : "" );
		param.put("strKeyword1",map.get("strKeyword1") != null ? map.get("strKeyword1") : "" );
		param.put("sdate",map.get("sdate") != null ? map.get("sdate") : "" );
		param.put("edate",map.get("edate") != null ? map.get("edate") : "" );
		param.put("orderCol",map.get("sidx") != null ? map.get("sidx") : "create_date");				
		param.put("orderType",map.get("sord") != null ? map.get("sord") : "ASC");
		param.put("page_size",map.get("rows") != null ? map.get("rows") : sessionVO.getSessPage_size());
		
		//상세검색 입력 파라미터 유효성 체크
		param.put("keyword",map.get("keyword") != null ? map.get("keyword") : ""); 				// 키워드
		param.put("page_name",map.get("page_name") != null ? map.get("page_name") : ""); 	// 첨부파일명			
		//param.put("nPage",CommonUtil.getPage(map));														// page 설정(Grid)		
		param.put("nPage",map.get("page") != null ? Integer.parseInt(map.get("page").toString()) : 1 );		//page 설정
		
		param.put("user_id", sessionVO.getSessId());
		param.put("folder_id", folder_id); 																			// 권한 및 role 관련해서 사용
		
		try {
			
			// 1. 엑셀다운로드시 파라미터 재정의
			if(oper.equals(Constant.EXCEL_FORMAT)) {
				param.put("oper", oper);
				param.put("nPage",1);		// 엑셀 저장 row수		
				param.put("page_size",ConfigData.getInt("EXCEL_MAX_LIMIT"));		
				param.put("strKeyword",map.get("strKeyword") != null ? new String(map.get("strKeyword").toString().getBytes("8859_1"),"utf-8") : "");
			}

			// 2. 그룹+프로젝트 그룹 ID
			String[] group_id_list = sessionVO.getSessProjectGroup().toArray(new String[sessionVO.getSessProjectGroup().size()+1]);
			group_id_list[sessionVO.getSessProjectGroup().size()] = sessionVO.getSessGroup_id();
			param.put("group_id_list",group_id_list);
		
			String acl_check = String.valueOf(true);
			String document_menu_part = CommonUtil.getMenuPart(sessionVO, Constant.USER_DOC_MENU_CODE);
			String folder_menu_part = CommonUtil.getMenuPart(sessionVO, Constant.USER_FOLDER_MENU_CODE);
			boolean isFolderMenuPart = false;
			
			if(document_menu_part.equals(Constant.MENU_ALL)){
				// 전사 문서 관리자 일 경우 acl_check 안함
				acl_check = String.valueOf(false);
				isFolderMenuPart = true;
			} else if(document_menu_part.equals(Constant.MENU_GROUP) || document_menu_part.equals(Constant.MENU_TEAM)){
				// 하위부서포함, 소속부서 관리는 관리부서 ID일 경우 acl_check 안함
				if(cacheService.menuAuthByFolderID(folder_id, sessionVO.getSessManage_group())){
					acl_check = String.valueOf(false);
					isFolderMenuPart = true;
				}
			}
			
			// 3. 확장문서 상세 검색 처리
			// 하위폴더 체크여부(child_include), 문서유형 전체 여부(doc_type), 첨부파일명 쿼리
			if(is_extended.equals(Constant.T) && !StringUtil.isEmpty(doc_type)) {
				param.put("tbl_name",  Constant.TABLE_PREFIX + doc_type); // 테문서유형(테이블명)블명
				
				// 문서유형 속성(AttrVO) search where 추가
				attrList = documentService.extendedAttrListByDocType(request, doc_type);
				param.put("attrList", attrList); // 테이블명
			} else if(!StringUtil.isEmpty(doc_type)) {
				switch (doc_type) {
				case Constant.DOC_TABLE_ALL_TYPE:
					break;
				case Constant.DOC_TABLE: param.put("tbl_name", doc_type); // 테이블명
					break;
				default: param.put("tbl_name",  Constant.TABLE_PREFIX + doc_type); // 테이블명
					break;
				}
			}
			
			// 4. 하위폴더 체크여부(child_include)
			if(!StringUtil.isEmpty(child_include) && child_include.equals("on")) {
				// 하위폴더 folder_id_list
				folder_id_list = folderService.childFolderIdsByfolderId(folder_id, isFolderMenuPart ? folder_menu_part : "");
			}
			folder_id_list.add(folder_id);
			
			param.put("folder_id_list", folder_id_list);
			param.put("document_menu_part",document_menu_part);
			param.put("manage_group_id", sessionVO.getSessManage_group());
			
			// 5. 검색조건 추가	
			param.put("dateColumn", "D.CREATE_DATE");
			param.put("acl_check",acl_check);		
			param.put("doc_status", Constant.DOC_STATUS_CREATE);
			param.put("is_expired",Constant.F);
			
			// 6. strIndex에 따른 검색조건 추가 strKeyword1
			CommonUtil.setColumNm(map, param);
			
			// 6.1 Paging 처리 이미지경로 
			param.put("contextRoot",sessionVO.getSessContextRoot());

			// 7. 문서리스트 조회처리
			param.put("strLink","javascript:workDocList.grid.refresh");		// 나의문서 공유폴더 분기처리 파리미터
			resultMap = documentService.workDocumentBasicList(param);
			
			// 8. Excel Down 처리 :: 목록
			if(oper.equals(Constant.EXCEL_FORMAT)) {

				CommonUtil.getExcelList(resultMap,model);
				CommonUtil.setExcelFormat(model,"");			// 개인/업무문서 목록은 listType을 지정하지 않음

				return new ModelAndView(new ExcelView());
			}
			
		}catch(BizException e){
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
	 * 1. 개용 : 내문서 - 작업카트 - URL메일발송할때 id리스트로 첨부파일목록 얻기
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : documentAttachFileByIDList
	 * @param model
	 * @param sessionVO
	 * @param map
	 * @return Map<String,Object>
	 */
	@RequestMapping(value="/documentAttachFileByIDList.do", method=RequestMethod.POST)
	@ResponseBody 
	public Map<String, Object> documentAttachFileByIDList(Model model, @ModelAttribute SessionVO sessionVO, @RequestParam HashMap<String,Object> map) {
		
		Locale locale = LocaleLibrary.setLocale(sessionVO.getSessLanguage() != null  ? sessionVO.getSessLanguage() : ConfigData.getString("LANGUAGE"));
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
				
		try {
			
			resultMap = pageService.docPageListForURLMail(map);
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
	 * <pre>
	 * 1. 개용 : 문서 댓글 목록조회
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : documentHtList
	 * @param model
	 * @param sessionVO
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/documentCommentList.do", method=RequestMethod.POST)
	@ResponseBody 
	public Map<String,Object> documentCommentList(Model model, @ModelAttribute SessionVO sessionVO, @RequestParam HashMap<String,Object> map) {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Locale locale = LocaleLibrary.setLocale(sessionVO.getSessLanguage() != null  ? sessionVO.getSessLanguage() : ConfigData.getString("LANGUAGE"));
		
		try {
			
			resultMap = documentService.documentCommentList(map);
						
		}catch(BizException e){
			resultMap.put("result",Constant.RESULT_FALSE);
			resultMap.put("message",e.getMessage());	
		} catch (Exception e) {
			resultMap.put("result",Constant.RESULT_FALSE);
			resultMap.put("message",messageSource.getMessage("common.system.error",new Object[0],locale));
		}
		
		return resultMap;
	}
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 문서버전 상세
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : noteUserSelect
	 * @param sessionVO
	 * @param model
	 * @param map
	 * @return String
	 */
	@RequestMapping(value="/docVersionDetail.do", method=RequestMethod.POST)
	public String docVersionDetail(@ModelAttribute SessionVO sessionVO,Model model,@RequestParam HashMap<String,Object> map) {
		
		@SuppressWarnings("unused")
		Locale locale = LocaleLibrary.setLocale(sessionVO.getSessLanguage() != null  ? sessionVO.getSessLanguage() : ConfigData.getString("LANGUAGE"));

		model.addAttribute("docId",map.get("docId") != null ? map.get("docId").toString() : null);
		
		CommonUtil.setSessionToModel(model, sessionVO);
		
		return "popup/documentVersionDetail";
	}
	
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 문서의견 수정/삭제/댓글 화면
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : documentCommentUpdate
	 * @param sessionVO
	 * @param commentVO
	 * @param model
	 * @param map
	 * @param request
	 * @return Map<String,Object>
	 */
	@RequestMapping(value="/documentCommentUpdate.do", method=RequestMethod.POST)
	@ResponseBody 
	public Map<String, Object> documentCommentUpdate(@ModelAttribute SessionVO sessionVO, @ModelAttribute CommentVO cVO,Model model, 
			@RequestParam HashMap<String, Object> map,HttpServletRequest request) {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		Locale locale = LocaleLibrary.setLocale(sessionVO.getSessLanguage() != null  ? sessionVO.getSessLanguage() : ConfigData.getString("LANGUAGE"));
		try {
			documentService.docCommentAction(map, sessionVO);
			
		}catch(BizException e){
			resultMap.put("result",Constant.RESULT_FALSE);
			resultMap.put("message",e.getMessage());
		} catch (Exception e) {
			resultMap.put("result",Constant.RESULT_FALSE);
			resultMap.put("message",messageSource.getMessage("common.system.error",new Object[0],locale));
		}

		resultMap.put("result",Constant.RESULT_TRUE);
		return resultMap;


	}
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 문서버전 상세
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : noteUserSelect
	 * @param sessionVO
	 * @param model
	 * @param map
	 * @return String
	 */
	@RequestMapping(value="/reciverUserSelect.do", method=RequestMethod.POST)
	public String reciverUserSelect(@ModelAttribute SessionVO sessionVO,Model model,@RequestParam HashMap<String,Object> map) {
		
		@SuppressWarnings("unused")
		Locale locale = LocaleLibrary.setLocale(sessionVO.getSessLanguage() != null  ? sessionVO.getSessLanguage() : ConfigData.getString("LANGUAGE"));

		model.addAttribute("docId",map.get("docId") != null ? map.get("docId").toString() : null);
		
		CommonUtil.setSessionToModel(model, sessionVO);
		
		return "popup/reciverUserSelect";
	}
	
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 메인 문서목록 리스트 가져오기 
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : mainDocumentList
	 * @param model
	 * @param sessionVO
	 * @param map
	 * @param request
	 * @return ModelAndView
	 */
	@RequestMapping(value="/mainDocumentList.do", method=RequestMethod.POST)
	@ResponseBody 
	public ModelAndView mainDocumentList(Model model, @ModelAttribute SessionVO sessionVO, @RequestParam HashMap<String,Object> map, 
			HttpServletRequest request) {
		
		Locale locale = LocaleLibrary.setLocale(sessionVO.getSessLanguage() != null  ? sessionVO.getSessLanguage() : ConfigData.getString("LANGUAGE"));
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		HashMap<String, Object> param = new HashMap<String, Object>();

		String actionType = StringUtil.getMapString(map,"actionType");
		
		param.put("document_menu_part",CommonUtil.getMenuPart(sessionVO, Constant.USER_DOC_MENU_CODE));
		param.put("manage_group_id", sessionVO.getSessManage_group());
		param.put("user_id", sessionVO.getSessId());
		param.put("actionType",actionType);
		
		try {

			// 1. actionType에 따른 검색조건 분기
			if(actionType.equals(Constant.MAIN_MOSTMYDOC)) {				// 최다조회 내문서
				param.put("userColumn","D.OWNER_ID");
				param.put("isRead",Constant.RESULT_TRUE);
				param.put("acl_check",Constant.RESULT_FALSE);
			}else if(actionType.equals(Constant.MAIN_CHECKOUTDOC)) {	// 내수정중인문서
				param.put("userColumn","D.LOCK_OWNER");
				param.put("is_locked",Constant.RESULT_TRUE);
				param.put("acl_check",Constant.RESULT_FALSE);
			}else if(actionType.equals(Constant.MAIN_NEWDOC)) {				// 새로운문서
				param.put("sdate", StringUtil.getMapString(map, "sdate") != "" ? StringUtil.getMapString(map, "sdate") : StringUtil.getDay(7));
				param.put("edate", StringUtil.getMapString(map, "edate") != "" ? StringUtil.getMapString(map, "edate") : StringUtil.getToday());
				param.put("acl_check",Constant.RESULT_TRUE);
			}else if(actionType.equals(Constant.MAIN_RECENTLYDOC)) {		// 최근조회
				param.put("isRecently",Constant.RESULT_TRUE);
				param.put("acl_check",Constant.RESULT_TRUE);
			}else if(actionType.equals(Constant.MAIN_MOSTVIEWDOC)) {	// 최다조회 문서	
				param.put("isRead",Constant.RESULT_TRUE);
				param.put("acl_check",Constant.RESULT_TRUE);
			}

			// 2. 권한그룹리스트 
			String[] group_id_list = sessionVO.getSessProjectGroup().toArray(new String[sessionVO.getSessProjectGroup().size()+1]);
			group_id_list[sessionVO.getSessProjectGroup().size()] = sessionVO.getSessGroup_id();
			param.put("group_id_list",group_id_list);
			
			// 3. 문서목록 리스트 가져오기
			resultMap = documentService.mainDocumentBasicList(param);			
			
		}catch(BizException e){
			logger.error(e.getMessage());
			resultMap.put("result",Constant.RESULT_FALSE);
			resultMap.put("message",e.getMessage());				
		}catch(Exception e)	{										
			logger.error(e.getMessage());
			resultMap.put("result",Constant.RESULT_FALSE);
			resultMap.put("message",messageSource.getMessage("common.system.error",new Object[0],locale));			
		}
		
		ModelAndView mav = new ModelAndView("jsonView",resultMap);
		return mav;
	}
	
	
	/**
	 * <pre>
	 * 1. 개용 : 조회 건수 update
	 * 2. 처리내용 : 조회 건수 update
	 * </pre>
	 * @Method Name : noteStorageboxUpdate
	 * @param model
	 * @param sessionVO
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/doReadCountUpdate.do", method=RequestMethod.POST)
	@ResponseBody 
	public Map<String,Object> doReadCountUpdate(Model model, @ModelAttribute SessionVO sessionVO, @RequestParam HashMap<String,Object> map) {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Locale locale = LocaleLibrary.setLocale(sessionVO.getSessLanguage() != null  ? sessionVO.getSessLanguage() : ConfigData.getString("LANGUAGE"));
		
		try {
			documentService.docReadCountUpdate(map);
				
		}catch(BizException e){
			resultMap.put("result",Constant.RESULT_FALSE);
			resultMap.put("message",e.getMessage());
		}catch(Exception e)	{										
			resultMap.put("result",Constant.RESULT_FALSE);
			resultMap.put("message",messageSource.getMessage("common.system.error",new Object[0],locale));			
		}
		
		resultMap.put("result",Constant.RESULT_TRUE);
		return resultMap;
	}
	
	
}

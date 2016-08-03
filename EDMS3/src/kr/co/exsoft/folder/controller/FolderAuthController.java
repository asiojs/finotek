package kr.co.exsoft.folder.controller;

import java.util.Map;
import java.util.HashMap;
import java.util.Locale;
import java.util.List;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;



import org.springmodules.validation.commons.DefaultBeanValidator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import kr.co.exsoft.common.service.CommonService;
import kr.co.exsoft.common.vo.HistoryVO;
import kr.co.exsoft.common.vo.RecentlyObjectVO;
import kr.co.exsoft.common.vo.SessionVO;
import kr.co.exsoft.document.service.TypeService;
import kr.co.exsoft.document.vo.TypeVO;
import kr.co.exsoft.eframework.configuration.Constant;
import kr.co.exsoft.eframework.exception.BizException;
import kr.co.exsoft.eframework.library.LocaleLibrary;
import kr.co.exsoft.eframework.util.CommonUtil;
import kr.co.exsoft.eframework.util.ConfigData;
import kr.co.exsoft.eframework.util.StringUtil;
import kr.co.exsoft.folder.service.FolderService;
import kr.co.exsoft.folder.vo.FavoriteFolderVO;
import kr.co.exsoft.folder.vo.FolderVO;
import kr.co.exsoft.permission.service.AclService;
import kr.co.exsoft.permission.vo.AclItemListVO;

/**
 * Folder Controller
 * @author 패키지 개발팀
 * @since 2014.07.17
 * @version 3.0
 *
 */
@Controller
@SessionAttributes("sessionVO")
@RequestMapping("/folder")
public class FolderAuthController {
	
	@Autowired
	private CommonService commonService;
	
	@Autowired
	private FolderService folderService;
	
	@Autowired
	private TypeService typeService;
	
	@Autowired
	private AclService aclService;
	
	@Autowired
	private MessageSource messageSource;
	
    @Autowired
    private DefaultBeanValidator beanValidator;
    
    protected static final Log logger = LogFactory.getLog(FolderAuthController.class);
    
    /**
	 * 
	 * <pre>
	 * 1. 개용 : 폴더 조회
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : folderList
	 * @param sessionVO
	 * @param model
	 * @param map
	 * @param request
	 * @return Map<String,Object>
	 */
	@RequestMapping(value="folderList.do", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> folderList(@ModelAttribute SessionVO sessionVO, Model model, @RequestParam HashMap<String, Object> map,
			HttpServletRequest request) {

		Locale locale = LocaleLibrary.setLocale(sessionVO.getSessLanguage() != null  ? sessionVO.getSessLanguage() : ConfigData.getString("LANGUAGE"));
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		HashMap<String,Object> param = new  HashMap<String, Object>();
		
		try {
			
			String mapId = map.get(Constant.TREE_MAP_ID) != null ? map.get(Constant.TREE_MAP_ID).toString() : "";
			String parentId = map.get(Constant.TREE_PARENT_ID) != null ? map.get(Constant.TREE_PARENT_ID).toString() : null;
			String workType = map.get(Constant.TREE_WORK_TYPE) != null ? map.get(Constant.TREE_WORK_TYPE).toString() : "";
			
			param.put("mapId", mapId);
			param.put("parentId", parentId);

			// 그룹+프로젝트 그룹 ID
			String[] group_id_list = sessionVO.getSessProjectGroup().toArray(new String[sessionVO.getSessProjectGroup().size()+1]);
			group_id_list[sessionVO.getSessProjectGroup().size()] = sessionVO.getSessGroup_id();
			param.put("user_id",sessionVO.getSessId());
			param.put("group_id_list",group_id_list);
			
			/**
			 *  롤+개인문서함일 경우 권한 체크 안한다
			 * check point
			 * 1. 관리자 메뉴인가? 사용자 기능인가?
			 * 2. 개인/프로젝트함은 ACL 체크, 부서/전사함은 메뉴 접근권한 체크
			 * 3. 폴더 팝업에 대해서 2번과 동일하게 적용
			 * 4. 하위부서 포함일 경우 하위 폴더 검색 시 folder_type 조건 없음
			 * 5. rootId가 부서폴더에 속해 있으면 acl 체크 안함
			 * 6. 관리자, 사용자 접근 코드 틀림
			 */
			String acl_check = String.valueOf(true);
			String folder_menu_part = CommonUtil.getMenuPart(sessionVO, Constant.USER_FOLDER_MENU_CODE); // 사용자화면, 관리자화면 전사관리 여부
			String document_menu_part = CommonUtil.getMenuPart(sessionVO, Constant.USER_DOC_MENU_CODE);
			boolean isManagerPartAll = false;
			
			//ALL(전사):GROUP(하포):TEAM(소속부서)
			if(sessionVO.getSessLocation().equals(Constant.LOCATION_ADMIN) && mapId.equals(Constant.MAP_ID_DEPT)){

				acl_check = String.valueOf(false);
				
				// 1. 프로젝트함 무조건 권한 체크
				if(mapId.equals(Constant.MAP_ID_PROJECT))
					acl_check = String.valueOf(true);
				
				// 2. 접근 범위가 소속부서일 경우 where절에 folder_type 조건 추가
				if(folder_menu_part.equals(Constant.MENU_TEAM))
					param.put("folderType", Constant.FOLDER_TYPE_DOCUMENT);
				
			} else {
				
				// 1. 점근 범위(role)가 있는 경우 권한 체크 안함
				if(!StringUtil.isEmpty(folder_menu_part))
					acl_check = String.valueOf(false);
					
				// 2. 개인 문서함은 권한 체크 없음
				if(mapId.equals(Constant.MAP_ID_MYPAGE)) 
					acl_check = String.valueOf(false);
				
				// 3. 프로젝트함 무조건 권한 체크
				if(mapId.equals(Constant.MAP_ID_PROJECT))
					acl_check = String.valueOf(true);
				
				// 4. 부서함이면서 접근범위가 소속부서일 경우
				if(workType.equals(Constant.TREE_WORKTYPE_MYDEPT) && folder_menu_part.equals(Constant.MENU_TEAM))
					param.put("folderType", Constant.FOLDER_TYPE_DOCUMENT);
				
				// 5. 부서함이면서 접근범위가 없을 경우
				if(workType.equals(Constant.TREE_WORKTYPE_MYDEPT) && StringUtil.isEmpty(folder_menu_part))
					param.put("folderType", Constant.FOLDER_TYPE_DOCUMENT);
			}
			
			// 사용자화면/관리자화면에서 부서폴더 전사 권한
			if(folder_menu_part.equals(Constant.MENU_ALL) && workType.equals(Constant.TREE_WORKTYPE_MYDEPT))
				isManagerPartAll = true;
			
			param.put("folder_menu_part",folder_menu_part);
			param.put("document_menu_part",document_menu_part);
			param.put("manage_group_id", sessionVO.getSessManage_group());
			param.put("acl_check",acl_check);
			List<FolderVO> folderList = null;
			
			// 1.루트 / 자식 폴더 조회 구분
			if (StringUtil.isNull(parentId)) {
				
				if(workType.equals(Constant.TREE_WORKTYPE_MYDEPT) && !isManagerPartAll){
					// 1-1. 부서함일 경우 rootId를 그룹폴더로 변경
					if(folder_menu_part.equals(Constant.MENU_GROUP)){
						//하위부서포함 관리자의 경우 관리 부서를 가져 온다.
						param.put("rootId", sessionVO.getSessManage_group().replace(Constant.ID_PREFIX_GROUP, Constant.ID_PREFIX_FOLDER));
					} else {
					param.put("rootId", sessionVO.getSessGroup_id().replace(Constant.ID_PREFIX_GROUP, Constant.ID_PREFIX_FOLDER));
					}
					
				} else if (mapId.equals(Constant.MAP_ID_MYPAGE)) {
					// 1-2. 개임함일 경우 rootId를 사용자 ID로 변경
					param.put("rootId", sessionVO.getSessId());
				}
				
				param.put("folderType", null);
				folderList = folderService.rootFolderList(param);
			} else {
				
				folderList = folderService.childFolderList(param);
			}
			
			resultMap.put("folderList", folderList);
			
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
	 * 1. 개용 : 등록된 문서유형 목록 
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : makeTypeSelectbox
	 * @param sessionVO
	 * @param model
	 * @param map
	 * @param request
	 * @return Map<String,Object>
	 */
	@RequestMapping(value="makeTypeSelectbox.do", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> makeTypeSelectbox(@ModelAttribute SessionVO sessionVO, Model model, @RequestParam HashMap<String, Object> map,
			HttpServletRequest request) {

		Locale locale = LocaleLibrary.setLocale(sessionVO.getSessLanguage() != null  ? sessionVO.getSessLanguage() : ConfigData.getString("LANGUAGE"));
		
		List<TypeVO> typeList = new ArrayList<TypeVO>();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		HashMap<String,Object> param = new  HashMap<String, Object>();	
		
		try {
			
			param.put("is_system", Constant.T);
			
			typeList = typeService.typeList(param);
			
			resultMap.put("typeList", typeList);
			
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
	 * 폴더 공통 업무 처리
	 * @param model
	 * @param sessionVO
	 * @param map
	 * @param folderVO
	 * @param bindingResult
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/folderControl.do", method=RequestMethod.POST)
	@ResponseBody 
	public Map<String, Object> folderControl(Model model, @ModelAttribute SessionVO sessionVO, @RequestParam HashMap<String,Object> map,
			@ModelAttribute("folderVO") FolderVO folderVO,BindingResult bindingResult,HttpServletRequest request) {
		
		Locale locale = LocaleLibrary.setLocale(sessionVO.getSessLanguage() != null  ? sessionVO.getSessLanguage() : ConfigData.getString("LANGUAGE"));
		
		Map<String, Object> resultMap = new HashMap<String, Object>();

		String type =  map.get(Constant.TYPE) != null ? map.get(Constant.TYPE).toString() : "";
		
		try {
			
			//1. validation check
			// 서버 파라미터 유효성 체크 kr.co.exsoft.eframework.validator.FolderValidator.xml 정의되어야 함
			beanValidator.validate(folderVO, bindingResult);
			
			if ((type.equals(Constant.ACTION_CREATE) || type.equals(Constant.ACTION_UPDATE)) && bindingResult.hasErrors()) { 		
				
				 for(ObjectError error : bindingResult.getAllErrors()) {
	 				 
					 if(error instanceof FieldError)	{
						 
						 FieldError fieldError = (FieldError)error;
						 
						 logger.info(error.getCode() + " : " + error.getDefaultMessage());
						 logger.info(error.getCode() + " : " + error.getObjectName() );
		
						 Object[] args = error.getArguments();
						 
						 if(error.getCode().equals("typeMismatch")) {						 							 
							 resultMap.put("message",messageSource.getMessage("typeMismatch."+fieldError.getField(),args,locale));						 						 
						 }else {											 		 				
							 resultMap.put("message",messageSource.getMessage(error.getDefaultMessage(),args,locale));
						 }				 
					 }
					 
					 break;
				 }
				 
				 resultMap.put("result",Constant.RESULT_FALSE);
				 
			}else {
				//2. 작업에 대한 분기
				Object[] msgObject = new String[]{folderVO.getFolder_name_ko()};
				folderVO.setUpdate_action(type);
				if(!StringUtil.isEmpty(type) && type.equals(Constant.ACTION_CREATE)) {
					// 폴더 등록
					resultMap = folderService.folderWrite(folderVO, map, sessionVO);
					resultMap.put("message",messageSource.getMessage("folder.create.msg",msgObject,locale));
				} else if(!StringUtil.isEmpty(type) && type.equals(Constant.ACTION_UPDATE)) {
					// 폴더 수정
					resultMap = folderService.folderUpdate(folderVO, map, sessionVO);
					resultMap.put("message",messageSource.getMessage((String)resultMap.get("message_code"),msgObject,locale));
				} else if(!StringUtil.isEmpty(type) && type.equals(Constant.ACTION_DELETE)) {
					// 폴더 삭제
					resultMap = folderService.folderDelete(map, sessionVO);
					resultMap.put("message",messageSource.getMessage((String)resultMap.get("message_code"),msgObject,locale));
				} else if(!StringUtil.isEmpty(type) && type.equals(Constant.ACTION_MOVE)) {
					// 폴더 이동
					resultMap = folderService.folderUpdate(folderVO, map, sessionVO);
					resultMap.put("message",messageSource.getMessage((String)resultMap.get("message_code"),msgObject,locale));
				}
				
				// 2-1 folder Cache 변경 한다.
				String result =  resultMap.get("result") != null ? resultMap.get("result").toString() : "";
				String folder_id = "";
				if(!StringUtil.isEmpty(result) && result.equals(Constant.RESULT_TRUE)) {
					if(!StringUtil.isEmpty(type) && type.equals(Constant.ACTION_CREATE)) {
						folder_id =  resultMap.get("folder_id") != null ? resultMap.get("folder_id").toString() : "";
						folderService.changeFolderCacheByFolderID(folder_id, folderVO, type);
					} else {
						folder_id = folderVO.getFolder_id();
						folderService.changeFolderCacheByFolderID(folder_id, folderVO, type);
					}
					
					resultMap.put("folder_fullpath_ids", folderService.folderFullpathIdsByfolderID(folder_id));
					resultMap.put("refresh_id", map.get("refresh_id"));
					resultMap.put("target_refresh_id", folderVO.getParent_id());
				}
				
				// XR_HISTORY 등록처리
				if(!StringUtil.isEmpty(type) && result.equals(Constant.RESULT_TRUE)){
					long history_seq = 0;
					history_seq = commonService.commonNextVal(Constant.COUNTER_ID_HISTORY);
					HistoryVO historyVO = CommonUtil.setHistoryVO(history_seq, folder_id, type,  Constant.TARGET_FOLDER, sessionVO);
					commonService.historyWrite(historyVO);
				}
			}
			
			
			
		} catch(BizException e){
			resultMap.put("result",Constant.RESULT_FALSE);
			resultMap.put("message",e.getMessage());	
		} catch(Exception e){
			e.printStackTrace();
			resultMap.put("result",Constant.RESULT_FALSE);
			resultMap.put("message",messageSource.getMessage("common.system.error",new Object[0],locale));		
		}
		
		return resultMap;
	}
		
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 폴더 상세 내용 가져오기 
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : folderDetail
	 * @param sessionVO
	 * @param model
	 * @param map
	 * @param request
	 * @return Map<String,Object>
	 */
	@RequestMapping(value="folderDetail.do", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> folderDetail(@ModelAttribute SessionVO sessionVO, Model model, @RequestParam HashMap<String, Object> map,
			HttpServletRequest request) {

		Locale locale = LocaleLibrary.setLocale(sessionVO.getSessLanguage() != null  ? sessionVO.getSessLanguage() : ConfigData.getString("LANGUAGE"));
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> resultAclVoMap = new HashMap<String, Object>();
		HashMap<String,Object> param = new  HashMap<String, Object>();
		List<AclItemListVO> listAclItemListVO = new ArrayList<AclItemListVO>();
		
		String folder_id = map.get("folder_id") != null ? map.get("folder_id").toString() : null;
		try {
			
			// FOLDER  정보 얻기
			param.put("folder_id", folder_id);		
			FolderVO folderVO = folderService.folderDetail(param);			
			resultMap.put("folderDetail", folderVO);
			
			//ACL 명칭 얻기
			param.put("acl_id", folderVO.getAcl_id());
			resultAclVoMap = aclService.aclDetail(param); // acl_id에 대한 AclVO를 얻는다. (aclDetail);
			
			//ACLItem LIst
			listAclItemListVO = aclService.aclItemList(param);
			resultMap.put("aclDetail", resultAclVoMap.get("aclDetail"));
			resultMap.put("aclItemList", listAclItemListVO);
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
	 * 즐겨찾기 폴더 공통 처리
	 * @param sessionVO
	 * @param model
	 * @param map
	 * @return
	 */
	@RequestMapping(value="favoriteControl.do", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> favoriteControl(@ModelAttribute SessionVO sessionVO, Model model, @RequestParam HashMap<String, Object> map) {
		
		Locale locale = LocaleLibrary.setLocale(sessionVO.getSessLanguage() != null  ? sessionVO.getSessLanguage() : ConfigData.getString("LANGUAGE"));
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		List<String> groupIdList = new ArrayList<String>();
		
		/**
		 * Required Parameters
		 * 1. parent_id :: XR_FAVORITE_FOLDER.FOLDER_ID
		 * 2. folder_id
		 * 3. folder_nm
		 * 4. user_id :: Session에서 취함
		 * 5. is_virtual
		 * 6. sorts :: DB에서 취함
		 * 7. group_id_list :: Session에서 취함
		 */
		try {
			
			String type = StringUtil.getMapString(map, Constant.TYPE);
			groupIdList.add(sessionVO.getSessGroup_id());
			resultMap.put("result",  Constant.RESULT_TRUE);
			
			map.put("group_id_list", groupIdList);
			map.put("user_id", sessionVO.getSessId());
			
			// 작업에 따른 분기
			if (type.equals(Constant.ACTION_CREATE)) {
				// 1. 즐겨찾기 폴더 생성
				
				// 폴더를 생성할 경우 ID를 발급받아 사용한다
				map.put("folder_id", CommonUtil.getStringID(Constant.ID_PREFIX_FOLDER, commonService.commonNextVal(Constant.COUNTER_ID_FILE)));
				
				// 폴더 생성
				folderService.writeFavoriteFolder(map);
				
			} else if (type.equals(Constant.ACTION_ADD_TO_FAVORITES)) {
				// 2. 즐겨찾기 폴더 추가 (바로가기)
				// required : user_id, folder_id, folder_name, parent_id 
				folderService.writeFavoriteFolder(map);
				
			} else if (type.equals(Constant.ACTION_UPDATE)) {
				folderService.updateFavoriteFolder(map);
				
			} else if (type.equals(Constant.ACTION_DELETE)) {
				// 3. 즐겨찾기 폴더 삭제
				// -> 삭제 시 하위 폴더 및 즐겨찾기 문서 모두 삭제 해야함.
				folderService.deleteFavoriteFolder(map);
				
			} else if (type.equals(Constant.ACTION_CHECK_EXISTS)) {
				// required : user_id, folder_id
				resultMap = folderService.existsFavoriteFolder(map);
				
			} else if (type.equals(Constant.ACTION_SWAP_INDEX)) {
				folderService.swapFavoriteFolderIndex(map);
				
			} else if (type.equals(Constant.ACTION_MOVE)) {
				folderService.moveFavoriteFolder(map);
			}
			
		} catch(BizException e) {
			resultMap.put("result", Constant.RESULT_FALSE);
			resultMap.put("message", e.getMessage());
		} catch(Exception e) {										
			resultMap.put("result", Constant.RESULT_FALSE);
			resultMap.put("message", messageSource.getMessage("common.system.error",new Object[0],locale));			
		}
		
		return resultMap;
	}
	
	/**
	 * 공유폴더 공통업무 처리
	 * @param sessionVO
	 * @param model
	 * @param map
	 * @return
	 */
	@RequestMapping(value="shareFolderList.do", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> shareFolderList(@ModelAttribute SessionVO sessionVO, Model model, @RequestParam HashMap<String, Object> map) {
		
		Locale locale = LocaleLibrary.setLocale(sessionVO.getSessLanguage() != null  ? sessionVO.getSessLanguage() : ConfigData.getString("LANGUAGE"));
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		HashMap<String,Object> param = new  HashMap<String, Object>();
		List<FolderVO> folderList = new ArrayList<FolderVO>();
		String folder_menu_part = "";
		
		try {
			
			// 그룹+프로젝트 그룹 ID
			String[] group_id_list = sessionVO.getSessProjectGroup().toArray(new String[sessionVO.getSessProjectGroup().size()+1]);
			group_id_list[sessionVO.getSessProjectGroup().size()] = sessionVO.getSessGroup_id();
			String parentId = map.get(Constant.TREE_PARENT_ID) != null ? map.get(Constant.TREE_PARENT_ID).toString() : null;
			
			folder_menu_part = CommonUtil.getMenuPart(sessionVO, Constant.USER_FOLDER_MENU_CODE);
					
			param.put("parentId", parentId);
			param.put("folder_menu_part",folder_menu_part);
			param.put("user_id",sessionVO.getSessId());
			param.put("group_id", sessionVO.getSessGroup_id());
			param.put("group_id_list",group_id_list);
			param.put("acl_check", String.valueOf(true));
			
			if (StringUtil.isNull(parentId)) {				
				folderList = folderService.rootShareFolderList(param);
			} else {
				
				folderList = folderService.childFolderList(param);
			}
			
			resultMap.put("folderList", folderList);
			
		} catch(BizException e) {
			resultMap.put("result", Constant.RESULT_FALSE);
			resultMap.put("message", e.getMessage());				
		} catch(Exception e) {										
			resultMap.put("result", Constant.RESULT_FALSE);
			resultMap.put("message", messageSource.getMessage("common.system.error",new Object[0],locale));			
		}
		
		return resultMap;
	}
	
	/**
	 * 즐겨찾기 폴더 리스트 
	 * @param sessionVO
	 * @param model
	 * @param map
	 * @return
	 */
	@RequestMapping(value="favoriteFolderList.do", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> favoriteFolderList(@ModelAttribute SessionVO sessionVO, Model model, @RequestParam HashMap<String, Object> map) {
		
		Locale locale = LocaleLibrary.setLocale(sessionVO.getSessLanguage() != null  ? sessionVO.getSessLanguage() : ConfigData.getString("LANGUAGE"));
		Map<String, Object> resultMap = new HashMap<String, Object>();
		HashMap<String,Object> param = new  HashMap<String, Object>();
		List<FavoriteFolderVO> favoriteList = null;
		
		try {
			
			// 그룹+프로젝트 그룹 ID
			String[] group_id_list = sessionVO.getSessProjectGroup().toArray(new String[sessionVO.getSessProjectGroup().size()+1]);
			group_id_list[sessionVO.getSessProjectGroup().size()] = sessionVO.getSessGroup_id();
			
			String parentId = map.get(Constant.TREE_PARENT_ID) != null ? map.get(Constant.TREE_PARENT_ID).toString() : null;

			param.put("user_id",sessionVO.getSessId());
			param.put("parent_folder_id", StringUtil.getMapString(map, Constant.TREE_PARENT_ID));
			param.put("only_virtual", StringUtil.getMapString(map, "only_virtual"));			
			param.put("group_id", sessionVO.getSessGroup_id());
			param.put("group_id_list",group_id_list);
			param.put("acl_check", String.valueOf(true));
			
			if (StringUtil.isNull(parentId)) {
				favoriteList = folderService.rootFavoriteFolderList(param);
				
			} else {
				favoriteList = folderService.childFavoriteFolderList(param);
			}
			
			resultMap.put("favoriteList", favoriteList);
			
		} catch(BizException e) {
			resultMap.put("result", Constant.RESULT_FALSE);
			resultMap.put("message", e.getMessage());				
		} catch(Exception e) {										
			resultMap.put("result", Constant.RESULT_FALSE);
			resultMap.put("message", messageSource.getMessage("common.system.error",new Object[0],locale));			
		}
		
		return resultMap;
	}
	
	/**
	 * 최근 사용 폴더 목록
	 * @param sessionVO
	 * @param model
	 * @param map
	 * @return
	 */
	@RequestMapping(value="recentlyFolderList.do", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> recentlyFolderList(@ModelAttribute SessionVO sessionVO, Model model, @RequestParam HashMap<String, Object> map) {
		Locale locale = LocaleLibrary.setLocale(sessionVO.getSessLanguage() != null  ? sessionVO.getSessLanguage() : ConfigData.getString("LANGUAGE"));
		List<RecentlyObjectVO> recentlyFolderList = new ArrayList<RecentlyObjectVO>();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		try {
			
			// 최근 폴더 목록
			recentlyFolderList = folderService.recentlyFolderList(sessionVO.getSessId());
			
			resultMap.put("recentlyFolderList", recentlyFolderList);
			resultMap.put("result",  Constant.RESULT_TRUE);
		} catch(BizException e) {
			resultMap.put("result", Constant.RESULT_FALSE);
			resultMap.put("message", e.getMessage());				
		} catch(Exception e) {										
			resultMap.put("result", Constant.RESULT_FALSE);
			resultMap.put("message", messageSource.getMessage("common.system.error",new Object[0],locale));			
		}
		return resultMap;
	}
	/**
	 * 최근 사용 폴더 삭제
	 * @param sessionVO
	 * @param model
	 * @param map
	 * @return
	 */
	@RequestMapping(value="recentlyFolderDelete.do", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> recentlyFolderDelete(@ModelAttribute SessionVO sessionVO, Model model, @RequestParam HashMap<String, Object> map) {
		Locale locale = LocaleLibrary.setLocale(sessionVO.getSessLanguage() != null  ? sessionVO.getSessLanguage() : ConfigData.getString("LANGUAGE"));
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		try {
			
			// 최근 폴더 목록
			resultMap = folderService.recentlyFolderDelete(StringUtil.getMapString(map, "idx"));
			
		} catch(BizException e) {
			resultMap.put("result", Constant.RESULT_FALSE);
			resultMap.put("message", e.getMessage());				
		} catch(Exception e) {										
			resultMap.put("result", Constant.RESULT_FALSE);
			resultMap.put("message", messageSource.getMessage("common.system.error",new Object[0],locale));			
		}
		return resultMap;
	}
}

package kr.co.exsoft.user.controller;

import java.util.Map;
import java.util.HashMap;
import java.util.Locale;
import java.util.List;
import java.util.ArrayList;
import java.io.File;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springmodules.validation.commons.DefaultBeanValidator;
import org.springframework.web.multipart.MultipartFile;
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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import kr.co.exsoft.common.service.CommonService;
import kr.co.exsoft.common.vo.CodeVO;
import kr.co.exsoft.common.vo.MenuAuthVO;
import kr.co.exsoft.common.vo.SessionVO;
import kr.co.exsoft.eframework.configuration.Constant;
import kr.co.exsoft.eframework.exception.BizException;
import kr.co.exsoft.eframework.library.LocaleLibrary;
import kr.co.exsoft.eframework.util.CommonUtil;
import kr.co.exsoft.eframework.util.ConfigData;
import kr.co.exsoft.eframework.util.StringUtil;
import kr.co.exsoft.eframework.util.UtilFileApp;
import kr.co.exsoft.user.service.UserService;
import kr.co.exsoft.user.vo.UserVO;
							
/**
 * User Admin Controller
 * @author 패키지 개발팀
 * @since 2014.07.17
 * @version 3.0
 *
 */
@Controller
@SessionAttributes("sessionVO")
@RequestMapping("/admin")
public class UserAdminController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private CommonService commonService;
	
	@Autowired
	private MessageSource messageSource;
	
    @Autowired
    private DefaultBeanValidator beanValidator;
    
    protected static final Log logger = LogFactory.getLog(UserAdminController.class);
	
	/**
	 * 
	 * <pre>
	 * 1. 개요 : 관리자 레이아웃 초기화면
	 * 2. 처리내용 :
	 * </pre>
	 * @Method Name : adminLayout
	 * @param sessionVO
	 * @param model
	 * @param map
	 * @return
	 */
	@RequestMapping("/adminLayout.do")
	public String adminLayout(@ModelAttribute SessionVO sessionVO,Model model,@RequestParam HashMap<String,Object> map,HttpServletRequest request) {

		List<MenuAuthVO> menuAuthList = new ArrayList<MenuAuthVO>();
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("role_id",sessionVO.getSessRole_id());
		param.put("is_use",Constant.YES);
				
		try {
		
			// [APPLIANCE VERSION] 
			if(ConfigData.getString("VERSION_INFO") != null && 
					ConfigData.getString("VERSION_INFO").equals(Constant.PRODUCT_EDMS_APPLIANCE)) {
				param.put("is_appliance",Constant.T);
			}
			
			menuAuthList = commonService.adminMenuAuthList(param)	;
			
			// 시스템관리자외 접속시 초기 메뉴구하기
			if(menuAuthList != null && menuAuthList.size() > 0 && !sessionVO.getSessRole_id().equals(Constant.SYSTEM_ROLE)){
				
				for(MenuAuthVO menu : menuAuthList )	{
					
					// 문서관리 && 통계하위메뉴인 경우 && 역할메뉴(문서관리 제외)
					if(!(menu.getMenu_cd().equals(Constant.DOC_MENU) || menu.getMenu_cd().equals(Constant.STATICS_MENU) 
							|| menu.getMenu_cd().equals(Constant.USERAUTH_MENU) || menu.getMenu_cd().equals(Constant.DOC_AUTH_MENU) ) )	{
						
						// 권한관리 메뉴는 ALL 일때만 보인다.
						if(menu.getMenu_cd().equals(Constant.ACL_AUTH_MENU)) { 
							if(menu.getPart().equals(Constant.ACL_ACL_TYPE_ALL)) {
								model.addAttribute("contents",sessionVO.getSessContextRoot() + menu.getLink_path()+"?menu_cd="+menu.getMenu_cd() );
							}else {
								continue;
							}
						}else {
							model.addAttribute("contents",sessionVO.getSessContextRoot() + menu.getLink_path()+"?menu_cd="+menu.getMenu_cd() );
						}
						break;
					}					
				}				
			}else {
				model.addAttribute("contents",sessionVO.getSessContextRoot() + "/admin/groupManager.do?menu_cd=M010");
			}
			
			
			
		}catch(BizException e){	
			logger.error(e.getMessage());
			CommonUtil.setErrorMsg(model, Constant.ERROR_403, e.getMessage(),sessionVO.getSessContextRoot());
			return "error/message";
		}catch(Exception e)	{										
			logger.error(e.getMessage());
			CommonUtil.setErrorMsg(model, Constant.ERROR_505, e.getMessage(),sessionVO.getSessContextRoot());
			return "error/message";
		}
				
		CommonUtil.setSessionToModel(model, sessionVO);					// call by reference
				
		model.addAttribute("user_name",sessionVO.getSessName());		
		model.addAttribute("menuAuthList",menuAuthList);
		model.addAttribute("location",sessionVO.getSessLocation());
		model.addAttribute("sysMenu",Constant.SYS_MENU);
		model.addAttribute("docMenu",Constant.DOC_MENU);
		model.addAttribute("rgateMenu",Constant.RGATE_MENU);
		model.addAttribute("staticsMenu",Constant.STATICS_MENU);
		model.addAttribute("roleMenu",Constant.USERAUTH_MENU);
		model.addAttribute("language",sessionVO.getSessLanguage());
		
		return "adminLayout";
	}

	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 사용자관리
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : userAdmin
	 * @param sessionVO
	 * @param model
	 * @param map
	 * @return String
	 */
	@RequestMapping("userManager.do")
	public String userAdmin(@ModelAttribute SessionVO sessionVO,Model model,@RequestParam HashMap<String,Object> map) {
		
		@SuppressWarnings("unused")
		Locale locale = LocaleLibrary.setLocale(sessionVO.getSessLanguage() != null  ? sessionVO.getSessLanguage() : ConfigData.getString("LANGUAGE"));
		
		Map<String, Object> menuInfo = new HashMap<String, Object>();
		Map<String, Object> partInfo = new HashMap<String, Object>();
		
		HashMap<String, Object> param = new HashMap<String, Object>();
		
		
		try {
		
			param.put("gcode_id", Constant.CODE_ROLE);
			List<CodeVO> roleList = commonService.codeList(param);
			
			param.put("gcode_id", Constant.CODE_POSITION);
			List<CodeVO> jobtitleList = commonService.codeList(param);
			
			param.put("gcode_id", Constant.CODE_DUTY);
			List<CodeVO> positionList = commonService.codeList(param);
			
			// 관리자 ROLE 접근권한 및 페이지 네비게이션 :: 상위메뉴명 / 현재메뉴명
			map.put("menu_cd",Constant.MANAGER_INDEX_MENU);
			commonService.setPageToModel(map,menuInfo,partInfo,sessionVO);
									
			CommonUtil.setSessionToModel(model, sessionVO);		// call by reference
			
			model.addAttribute("part",partInfo.get("part").toString());
			model.addAttribute("menuInfo",menuInfo);
			model.addAttribute("roleList", roleList);
			model.addAttribute("jobtitleList", jobtitleList);
			model.addAttribute("positionList", positionList);
			model.addAttribute("topSelect",Constant.TOPMENU_SYSTEM);
			model.addAttribute("subSelect",Constant.SYSTEM_USERMANAGER);
			
		} catch (BizException e){
			logger.error(e.getMessage());
			CommonUtil.setErrorMsg(model, Constant.ERROR_403, e.getMessage(),sessionVO.getSessContextRoot());
			return "error/message";
		} catch (Exception e)	{			
			logger.error(e.getMessage());
			CommonUtil.setErrorMsg(model, Constant.ERROR_505, e.getMessage(),sessionVO.getSessContextRoot());
			return "error/message";
		}
		
		return "sysadmin/userManager";
	}
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 부서 사용자 목록 조회 공통
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : groupUserList
	 * @param model
	 * @param request
	 * @return Map<String,Object>
	 */
	@RequestMapping(value="/groupUserList.do", method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> groupUserList(Model model, @ModelAttribute SessionVO sessionVO,HttpServletRequest request,@RequestParam HashMap<String,Object> map) {
		
		Locale locale = LocaleLibrary.setLocale(sessionVO.getSessLanguage() != null  ? sessionVO.getSessLanguage() : ConfigData.getString("LANGUAGE"));		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		HashMap<String, Object> param = new HashMap<String, Object>();
		
		param.put("groupId",map.get("groupId") != null ? map.get("groupId") : "" );
		param.put("orderCol",map.get("sidx") != null ? map.get("sidx") : "USER_NM");				
		param.put("orderType",map.get("sord") != null ? map.get("sord") : "ASC");
		param.put("isSystem",Constant.SYSTEM_ACCOUNT);		// 시스템 계정 조회
		
		try {

			// 검색 조건이 있는 경우에만 수행처리..
			if( (param.get("groupId") != null && !param.get("groupId").equals("") )) 	{
				
				resultMap = userService.groupUserList(param);
			}
	
			resultMap.put("result", Constant.RESULT_SUCCESS);
		} catch(BizException e){
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
	 * 1. 개요 : 사용자 등록 / 수정 / 삭제 / 사용 / 중지
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : userInfoManager
	 * @param model
	 * @param sessionVO
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/userInfoManager.do", method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> userInfoManager(Model model, @ModelAttribute SessionVO sessionVO, @RequestParam HashMap<String,Object> map) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Locale locale = LocaleLibrary.setLocale(sessionVO.getSessLanguage() != null  ? sessionVO.getSessLanguage() : ConfigData.getString("LANGUAGE"));
		
		// 작업 유형
		String type = StringUtil.getMapString(map, Constant.TYPE);
		
		try {
			UserVO userVO = new UserVO();
			List<UserVO> userList = new ArrayList<UserVO>();
			
			if (type.equals(Constant.INSERT)) {
				
				userVO = userService.userWriteValid(map);
				resultMap = userService.userWrite(userVO, map);
				
			} else if (type.equals(Constant.UPDATE)) {
				
				userVO = userService.userUpdateValid(map);
				
				// Update를 하기 위해 UserVO를 List로 변환
				userList.add(userVO);
				
				// 업데이트 수행
				resultMap = userService.userUpdate(userList);
				
			} else if (type.equals(Constant.DELETE)) {
				userList = userService.userDeleteValid(map);
				
				resultMap = userService.userDelete(userList);
			} else if (type.equals(Constant.MOVE)) {
			
				userList = userService.userGroupedUpdateValid(map);
				resultMap = userService.userGroupedUpdate(userList);
				
			} else if (type.equals(Constant.UPDATE_STATUS)) {
				
				userList = userService.userStatusUpdateValid(map);
				resultMap = userService.userUpdate(userList);
				
			} else if (type.equals(Constant.RESET_PASS)) {
				
				userList = userService.userPassResetValid(map);
				resultMap = userService.userUpdate(userList);
				
			} else if (type.equals(Constant.SELECT)) {
				
				resultMap.put("userInfo", userService.userGroupDetail(map));
				
			} else {
				throw new Exception("[" + type + "]은 처리할수 있는 작업유형이 아닙니다.");
			}
			
			resultMap.put("result", Constant.RESULT_SUCCESS);
			
		} catch (BizException e){
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
	 * 1. 개용 : 사용자일괄 등록처리
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : groupUpload
	 * @param model
	 * @param request
	 * @param sessionVO
	 * @param map
	 * @param report
	 * @return Map<String,Object>
	 */
	@RequestMapping(value="/userUpload.do", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> userUpload(Model model,HttpServletRequest request,@ModelAttribute SessionVO sessionVO,
			@RequestParam Map<String, String> map,@RequestParam("Filedata") MultipartFile report) {
		
		Locale locale = LocaleLibrary.setLocale(sessionVO.getSessLanguage() != null  ? sessionVO.getSessLanguage() : ConfigData.getString("LANGUAGE"));
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		String tmpDir = map.get("uniqStr") != null  ?  map.get("uniqStr").toString() : UUID.randomUUID().toString();
		String targetPath = ConfigData.getString("FILE_UPLOAD_PATH") + tmpDir;			
		String targetFileName = "";		

		List<UserVO> userList =  new ArrayList<UserVO>();
		HashMap<String,Object> param = new HashMap<String,Object>();
		
		try {
			
			UtilFileApp.createDir(targetPath);		
			targetFileName = targetPath+ "/"+UUID.randomUUID().toString() + ".xls";

			File file = new File(targetFileName);
			report.transferTo(file);
									
			if(file.exists())	{

				// 1. 사용자 일괄등록 목록 및 유효성 체크
				userList = userService.userExcelList(targetFileName);

				if(userList != null && userList.size() > 0) {
					
					for(UserVO userVO : userList)	{
						
						try {							

							// 2.1 XR_GROUPED 정보 설정
							param.put("user_id",userVO.getUser_id());
							param.put("group_id",userVO.getGroup_id());
							
							// 2.2 사용자 등록처리
							userService.userWrite(userVO,param);
							
						}catch (Exception e) {
							logger.error(e.getMessage());			
						}						
					}					
				}
		
				resultMap.put("retype", "success");				
			}else {				
				resultMap.put("retype", "fail");
				resultMap.put("message",messageSource.getMessage("group.fail.excel.upload",new Object[0],locale));
			}			
			
		}catch(Exception e) {				
			logger.error(e.getMessage());			
			resultMap.put("retype", "fail");
			resultMap.put("message",messageSource.getMessage("group.fail.excel.upload",new Object[0],locale));
		}	

		return resultMap;
		
	}
}

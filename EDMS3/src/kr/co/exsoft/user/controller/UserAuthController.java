package kr.co.exsoft.user.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Locale;

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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import kr.co.exsoft.common.service.CommonService;
import kr.co.exsoft.common.vo.CodeVO;
import kr.co.exsoft.common.vo.SessionVO;
import kr.co.exsoft.document.service.TypeService;
import kr.co.exsoft.document.vo.TypeVO;
import kr.co.exsoft.eframework.configuration.Constant;
import kr.co.exsoft.eframework.exception.BizException;
import kr.co.exsoft.eframework.library.LocaleLibrary;
import kr.co.exsoft.eframework.util.CommonUtil;
import kr.co.exsoft.eframework.util.ConfigData;
import kr.co.exsoft.eframework.util.StringUtil;
import kr.co.exsoft.user.service.UserService;
import kr.co.exsoft.user.vo.UserVO;
									
/**
 * User Controller
 * @author 패키지 개발팀
 * @since 2014.07.17
 * @version 3.0
 *
 */
@Controller
@SessionAttributes("sessionVO")
@RequestMapping("/user")
public class UserAuthController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private TypeService typeService;
	
	@Autowired
	private MessageSource messageSource;
	
    @Autowired
    private DefaultBeanValidator beanValidator;
	@Autowired
	private CommonService commonService;
    
	protected static final Log logger = LogFactory.getLog(UserAuthController.class);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : loginStatistics
	 * @param sessionVO
	 * @param model
	 * @param map
	 * @return String
	 */
	@RequestMapping(value="/userConfig.do", method=RequestMethod.POST)
	public String loginStatistics(@ModelAttribute SessionVO sessionVO,Model model,@RequestParam HashMap<String,Object> map) {
		
		@SuppressWarnings("unused")
		Locale locale = LocaleLibrary.setLocale(sessionVO.getSessLanguage() != null  ? sessionVO.getSessLanguage() : ConfigData.getString("LANGUAGE"));
		
		String tabType = map.get("tabType") != null ? map.get("tabType").toString() : Constant.CONFIG_TAB_MYINFO;

		model.addAttribute("tabType",tabType);
		model.addAttribute("width",Constant.CONFIG_WIDTH);
		model.addAttribute("height",Constant.CONFIG_HEIGHT.get(tabType));

		CommonUtil.setSessionToModel(model, sessionVO);
		
		return "config/userConfig";
	}

	/**
	 * 
	 * <pre>
	 * 1. 개용 : 사용자 환경설정 조회 및 변경처리
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : userConfig
	 * @param model
	 * @param sessionVO
	 * @param request
	 * @param map
	 * @return Map<String,Object>
	 */
	@RequestMapping(value="/userConfigProc.do", method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> userConfig(Model model, @ModelAttribute SessionVO sessionVO,HttpServletRequest request,@RequestParam HashMap<String,Object> map) {
		
		Locale locale = LocaleLibrary.setLocale(sessionVO.getSessLanguage() != null  ? sessionVO.getSessLanguage() : ConfigData.getString("LANGUAGE"));		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		String type = StringUtil.getMapString(map, Constant.TYPE);
		String updateType = StringUtil.getMapString(map, Constant.UPDATE_TYPE);
		
		UserVO userVO = new UserVO();
		
		try {
			
			
			// 1차 고도화에 따른 소스변경처리
		
			if (type.equals(Constant.UPDATE)) {
				
				map.put("user_id",sessionVO.getSessId());
				resultMap = userService.userConfig(map);

				if(updateType.equals(Constant.CONFIG_TAB_CONFIG))	{
					
					sessionVO.setSessTheme(map.get("theme").toString());
					sessionVO.setSessPage_size(map.get("page_size").toString());
					sessionVO.setSessDocSearch(map.get("doc_search").toString());
					sessionVO.setSessLanguage(map.get("language").toString());				// 고도화시 처리한다.
					sessionVO.setSessViewType(map.get("view_type").toString());
					
					// 나의 문서 표시기간 변경에 따른 세션값 변경처리 :: 1차고도화 제외처리
					/********************************************************************************************
					sessionVO.setSessDocSearch(map.get("doc_search").toString());
					
					Calendar eday = Calendar.getInstance();
					Calendar sday = (Calendar)eday.clone();				
					
					sday.add(Calendar.YEAR,-(Integer.parseInt(map.get("doc_search").toString())));
					sessionVO.setSessStartDt(StringUtil.print(sday));
					sessionVO.setSessEndDt(StringUtil.print(eday));
					********************************************************************************************/
				}
				
				resultMap.put("result", Constant.RESULT_TRUE);
				resultMap.put("message",messageSource.getMessage("common.success.msg",new Object[0],locale));
				
			}else {
				
				// 1.사용자 상세정보  
				if(updateType.equals(Constant.CONFIG_TAB_MYINFO))	{
					map.put("user_id",sessionVO.getSessId());
					userVO = userService.userDetailInfo(map);
				}
				
				// 2. 사용자 기본정보 
				userVO.setUser_id(sessionVO.getSessId());
				userVO.setUser_nm(sessionVO.getSessName());
				userVO.setGroup_nm(sessionVO.getSessGroup_nm());
				userVO.setRole_nm(sessionVO.getSessRole_nm());
				userVO.setTheme(sessionVO.getSessTheme());
				userVO.setDoc_search(sessionVO.getSessDocSearch());
				userVO.setLanguage(sessionVO.getSessLanguage());
				userVO.setPage_size(sessionVO.getSessPage_size());
				userVO.setView_type(sessionVO.getSessViewType());			// LIST / RIGHT / BOTTOM
				userVO.setIcon_preview(sessionVO.getSessIconPrewiew());		
				
				// 3. 결과값 전달
				resultMap.put("userVO",userVO);
				resultMap.put("result", Constant.RESULT_TRUE);
			}
			
		} catch (BizException e){
			resultMap.put("result",Constant.RESULT_FALSE);
			resultMap.put("message",e.getMessage());		
		} catch(Exception e)	{										
			resultMap.put("result",Constant.RESULT_FALSE);
			resultMap.put("message",messageSource.getMessage("common.system.error",new Object[0],locale));			
		}
		
		
		
		return resultMap;
	}
	
	/**
	 * <pre>
	 * 1. 개용 : 사용자 검색 후 리스트 조회 공통
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : searchGroupUser
	 * @param model
	 * @param sessionVO
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/searchUserList.do", method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> searchUserList(Model model, @ModelAttribute SessionVO sessionVO, @RequestParam HashMap<String,Object> map) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Locale locale = LocaleLibrary.setLocale(sessionVO.getSessLanguage() != null  ? sessionVO.getSessLanguage() : ConfigData.getString("LANGUAGE"));
		HashMap<String, Object> param = new HashMap<String, Object>();
		
		// 입력 파라미터 유효성 체크
		param.put("orderCol", map.get("sidx") != null ? map.get("sidx") : "USER_NM");				
		param.put("orderType", map.get("sord") != null ? map.get("sord") : "ASC");
		param.put("userName", map.get("userName") != null ? map.get("userName") : "" );
		param.put("groupName", map.get("groupName") != null ? map.get("groupName") : "" );
		param.put("groupId", map.get("groupId") != null ? map.get("groupId") : "" );
		// 검색조건 추가
		param.put("search_type", map.get("search_type") != null ? map.get("search_type") : "" );
		param.put("search_txt", map.get("search_txt") != null ? map.get("search_txt") : "" );
		
		// ** 전체 기능 점검을 위해 'edmsadmin' 계정도 조회 되도록 주석처리함
//		param.put("isSystem", Constant.SYSTEM_ACCOUNT);		// 시스템 계정 조회
		
		try {

			map.put("LANGUAGE", locale);
			
			// 검색 조건이 있는 경우에만 수행처리..
			if(!StringUtil.getMapString(map, "groupId").equals("") 
					|| !StringUtil.getMapString(map, "userName").equals("")
					|| !StringUtil.getMapString(map, "groupName").equals("")) {				
				resultMap = userService.searchUserList(param);
			}
	
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
	 * 1. 개용 : 퀵메뉴 관리
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : quickMenu
	 * @param model
	 * @param sessionVO
	 * @param request
	 * @param map
	 * @return Map<String,Object>
	 */
	@RequestMapping(value="/quickMenu.do", method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> quickMenu(Model model, @ModelAttribute SessionVO sessionVO,HttpServletRequest request,
			@RequestParam HashMap<String,Object> map) {
		
		Locale locale = LocaleLibrary.setLocale(sessionVO.getSessLanguage() != null  ? sessionVO.getSessLanguage() : ConfigData.getString("LANGUAGE"));		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		try {
									
			map.put("user_id",sessionVO.getSessId());			
			resultMap = userService.quickMenuProc(map);
						
		} catch (BizException e){
			resultMap.put("result",Constant.RESULT_FALSE);
			resultMap.put("message",e.getMessage());		
		} catch(Exception e)	{										
			resultMap.put("result",Constant.RESULT_FALSE);
			resultMap.put("message",messageSource.getMessage("common.system.error",new Object[0],locale));			
		}
		
		return resultMap;
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
	 * 1. 개용 : 초기화면
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : loginStatistics
	 * @param sessionVO
	 * @param model
	 * @param map
	 * @return String
	 */
	@RequestMapping("/mainContent.do")
	public String mainContent(@ModelAttribute SessionVO sessionVO,Model model,@RequestParam HashMap<String,Object> map) {
		
		@SuppressWarnings("unused")
		Locale locale = LocaleLibrary.setLocale(sessionVO.getSessLanguage() != null  ? sessionVO.getSessLanguage() : ConfigData.getString("LANGUAGE"));

		CommonUtil.setSessionToModel(model, sessionVO);
		
		// 문서유형 : 파라미터 - is_doc
		HashMap<String, Object> param = new HashMap<String, Object>();
		HashMap<String, Object> param1 = new HashMap<String, Object>();
		List<TypeVO> typeList = new ArrayList<TypeVO>();
		List<CodeVO> preservation = new ArrayList<CodeVO>();
		List<CodeVO> sercurity = new ArrayList<CodeVO>();
		List<CodeVO> position = new ArrayList<CodeVO>();
		
		try{
			param.put("is_doc", Constant.T);
			param.put("is_hidden", Constant.T); // 쿼리가 != 비교여서 T를 넘김
			typeList = typeService.typeList(param);
			
			// 보존년한 : 파라미터 - gcode_id	
			param1.put("gcode_id", Constant.CODE_PRESERVATION_YEAR);
			preservation = commonService.codeList(param1);
			
			// 보안등급 : 파라미터 - gcode_id 
			param1.put("gcode_id", Constant.CODE_SECURITY_LEVEL);
			sercurity = commonService.codeList(param1);
			
			// 조회등급 : 파라미터 - gcode_id,is_use 
			param1.put("gcode_id", Constant.CODE_POSITION);
			param1.put("is_use", Constant.YES);
			position = commonService.codeList(param1);
			
			
		}catch (BizException e){
			logger.info(StringUtil.getErrorTrace(e));
		} catch (Exception e)	{			
			logger.info(StringUtil.getErrorTrace(e));
		}
				
		model.addAttribute("typeList",typeList);
		model.addAttribute("preservation_year", preservation);
		model.addAttribute("sercurity", sercurity);
		model.addAttribute("position", position);
		
		return "main/mainContent";
	}

	/**
	 * 
	 * <pre>
	 * 1. 개용 : Layer 사용자/부서 검색 후 리스트 조회 공통
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : searchGroupUser
	 * @param model
	 * @param sessionVO
	 * @param request
	 * @param map
	 * @return Map<String,Object>
	 */
	@RequestMapping(value="/searchGroupUser.do", method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> searchGroupUser(Model model, @ModelAttribute SessionVO sessionVO,HttpServletRequest request,@RequestParam HashMap<String,Object> map) {
		
		Locale locale = LocaleLibrary.setLocale(sessionVO.getSessLanguage() != null  ? sessionVO.getSessLanguage() : ConfigData.getString("LANGUAGE"));		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		HashMap<String, Object> param = new HashMap<String, Object>();
				
		// 입력 파라미터 유효성 체크
		param.put("orderCol",map.get("sidx") != null ? map.get("sidx") : "UNIQUE_NM");				
		param.put("orderType",map.get("sord") != null ? map.get("sord") : "ASC");
		param.put("strIndex",map.get("strIndex") != null ? map.get("strIndex") : "" );
		param.put("strKeyword",map.get("strKeyword") != null ? map.get("strKeyword") : "" );
		param.put("group_id",map.get("groupId") != null ? map.get("groupId") : "" );
		param.put("isSystem",Constant.SYSTEM_ACCOUNT);		// 시스템 계정 조회
		
		try {

			// 검색 조건이 있는 경우에만 수행처리..
			if( param.get("strKeyword") != null &&  !param.get("strKeyword").equals("")) 	{				
				resultMap = userService.userGroupSearch(param);
			}
	
		} catch(BizException e){
			resultMap.put("result",Constant.RESULT_FALSE);
			resultMap.put("message",e.getMessage());				
		}catch(Exception e)	{										
			resultMap.put("result",Constant.RESULT_FALSE);
			resultMap.put("message",messageSource.getMessage("common.system.error",new Object[0],locale));			
		}
		
		return resultMap;
	}
}
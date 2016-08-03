package kr.co.exsoft.common.controller;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import kr.co.exsoft.eframework.util.CommonUtil;
import kr.co.exsoft.eframework.util.ConfigData;
import kr.co.exsoft.common.service.CommonService;
import kr.co.exsoft.common.vo.SessionVO;
import kr.co.exsoft.eframework.configuration.Constant;
import kr.co.exsoft.eframework.exception.BizException;
import kr.co.exsoft.eframework.library.LocaleLibrary;

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
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.FieldError;

import kr.co.exsoft.common.vo.CodeVO;
import kr.co.exsoft.common.vo.MenuAuthVO;


/**
 * 메뉴/코드 관련 관리자 컨트롤러
 * @author 패키지 개발팀
 * @since 2014.07.17
 * @version 3.0
 *
 */
@Controller
@RequestMapping("/admin")
@SessionAttributes("sessionVO")
public class CommonAdminController {
	
	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	private CommonService commonService;
	
    @Autowired
    private DefaultBeanValidator beanValidator;
    
	protected static final Log logger = LogFactory.getLog(CommonAdminController.class);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 메뉴 접근권한 관리
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : userAdmin
	 * @param sessionVO
	 * @param model
	 * @param map
	 * @return String
	 */
	@RequestMapping("menuAuthManager.do")
	public String menuAuthManager(@ModelAttribute SessionVO sessionVO,Model model,@RequestParam HashMap<String,Object> map) {

		Locale locale = LocaleLibrary.setLocale(sessionVO.getSessLanguage() != null  ? sessionVO.getSessLanguage() : ConfigData.getString("LANGUAGE"));
		
		HashMap<String,Object> param = new HashMap<String,Object>();
		Map<String, Object> menuInfo = new HashMap<String, Object>();
		
		param.put("menu_cd",map.get("menu_cd") != null ?  map.get("menu_cd").toString() : "" );
		param.put("role_id",sessionVO.getSessRole_id());
		
		String part = "";			// ALL/GROUP/TEAM - 관리자 페이지마다 다르게 적용처리한다.
		
		try {
			
			// 1. 관리자 ROLE 접근권한
			part = commonService.getMenuAuth(param);
			
			// 2. 페이지 네비게이션 :: 상위메뉴명 / 현재메뉴명 
			menuInfo = commonService.pageMenuInfo(param);
			
		}catch(BizException e){	
			logger.error(e.getMessage());
		}catch(Exception e)	{										
			logger.error(e.getMessage());
		}
		
		// 접근권한이 없거나 메뉴코드가 없는 경우 403 ERROR 페이지 이동 처리
		if(part.equals("") || param.get("menu_cd").toString().equals(""))	{			
			CommonUtil.setErrorMsg(model, Constant.ERROR_403, messageSource.getMessage("common.connect.error",new Object[0],locale),sessionVO.getSessContextRoot());
		}

		// call by reference
		CommonUtil.setSessionToModel(model, sessionVO);
		
		model.addAttribute("menuInfo",menuInfo);
		model.addAttribute("topSelect",Constant.TOPMENU_SYSTEM);
		model.addAttribute("subSelect",Constant.SYSTEM_MENUMANAGER);
		
		return "sysadmin/menuAuthManager";
	}
	
	/**
	 * 
	 * <pre>
	 * 1. 개요 : 코드 목록 가져오기 - ROLE
	 * 2. 처리내용 :
	 * </pre>
	 * @Method Name : codePageList
	 * @param model
	 * @param sessionVO
	 * @param map
	 * @param request
	 * @return Map
	 */
	@RequestMapping(value="/codePage.do", method=RequestMethod.POST)
	@ResponseBody 
	public Map<String,Object> codePageList(Model model, @ModelAttribute SessionVO sessionVO, @RequestParam HashMap<String,Object> map,
			HttpServletRequest request) {
		
		Locale locale = LocaleLibrary.setLocale(sessionVO.getSessLanguage() != null  ? sessionVO.getSessLanguage() : ConfigData.getString("LANGUAGE"));
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		HashMap<String, Object> param = new HashMap<String, Object>();		
				
		// 입력 파라미터 유효성 체크
		param.put("strKeyword",map.get("strKeyword") != null ? map.get("strKeyword") : "" );
		param.put("orderCol",map.get("sidx") != null ? map.get("sidx") : "SORT_INDEX");
		param.put("orderType",map.get("sord") != null ? map.get("sord") : "ASC");
		param.put("page_size",map.get("rows") != null ? map.get("rows") : sessionVO.getSessPage_size());		
		param.put("gcode_id",map.get("gcode_id") != null ? map.get("gcode_id") :  Constant.CODE_ROLE);
		param.put("is_use",map.get("is_use") != null ? map.get("is_use") : Constant.YES );
		
		// page 설정
		param.put("nPage",CommonUtil.getPage(map));

		try {
						
			// param 정의 : orderCol , orderType , page_size , nPage , gcode_id , is_use
			resultMap = commonService.codePageList(param);
			
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
	 * 1. 개요 : 코드 등록/삭제/수정 처리 
	 * 2. 처리내용 :
	 * </pre>
	 * @Method Name : codeWrite
	 * @param model
	 * @param sessionVO
	 * @param map
	 * @param request
	 * @return
	 */	
	@RequestMapping(value = "/codeWrite.do", method = RequestMethod.POST)
	@ResponseBody 
	public Map<String,Object> codeWrite(Model model, @ModelAttribute SessionVO sessionVO, @ModelAttribute("codeVO") CodeVO codeVO,
			@RequestParam HashMap<String,Object> map,BindingResult bindingResult,HttpServletRequest request) {
		
		Locale locale = LocaleLibrary.setLocale(sessionVO.getSessLanguage() != null  ? sessionVO.getSessLanguage() : ConfigData.getString("LANGUAGE"));
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		try {
			
			// 서버 파라미터 유효성 체크
			beanValidator.validate(codeVO, bindingResult);
			
			if (bindingResult.hasErrors()) { 		
				
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
			
				// 중복값 체크
			
				resultMap = commonService.codeManager(codeVO, map);
				
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
	 * 1. 개요 : 권한 삭제 처리
	 * 2. 처리내용 :
	 * </pre>
	 * @Method Name : codeDelete
	 * @param model
	 * @param sessionVO
	 * @param codeVO
	 * @param map
	 * @param bindingResult
	 * @param request
	 * @return Map
	 */
	@RequestMapping(value = "/codeDelete.do", method = RequestMethod.POST)
	@ResponseBody 
	public Map<String,Object> codeDelete(Model model, @ModelAttribute SessionVO sessionVO,
			@RequestParam HashMap<String,Object> map,HttpServletRequest request) {
		
		Locale locale = LocaleLibrary.setLocale(sessionVO.getSessLanguage() != null  ? sessionVO.getSessLanguage() : ConfigData.getString("LANGUAGE"));
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		String[] arrData = map.get("inputStr") != null ? map.get("inputStr").toString().split(",") : null ;
		
		try {
			
			 if(arrData != null) {
				 
				 for(String data : arrData) {				

					 CodeVO codeVO = new CodeVO();					 
					 codeVO.setCode_id(data);
					 codeVO.setGcode_id(Constant.CODE_ROLE);
					 
					 resultMap = commonService.codeManager(codeVO, map);										 
				 }
				 			
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
	 * 1. 개요 : 메뉴권한 목록 Tree 형태로 가져오기
	 * 2. 처리내용 :
	 * </pre>
	 * @Method Name : menuAuthList
	 * @param model
	 * @param sessionVO
	 * @param map
	 * @param request
	 * @return Map
	 */
	@RequestMapping("/menuAuth.do")
	@ResponseBody 
	public Map<String,Object> menuAuthList(Model model, @ModelAttribute SessionVO sessionVO, @RequestParam HashMap<String,Object> map,
			HttpServletRequest request) {
		
		Locale locale = LocaleLibrary.setLocale(sessionVO.getSessLanguage() != null  ? sessionVO.getSessLanguage() : ConfigData.getString("LANGUAGE"));

		Map<String, Object> resultMap = new HashMap<String, Object>();
		HashMap<String, Object> param = new HashMap<String, Object>();

		param.put("role_id",map.get("role_id") != null ? map.get("role_id") : "" );
		
		try {

			// [APPLIANCE VERSION] 
			if(ConfigData.getString("VERSION_INFO") != null && 
						ConfigData.getString("VERSION_INFO").equals(Constant.PRODUCT_EDMS_APPLIANCE)) {
				param.put("is_appliance",Constant.T);
			}
			
			resultMap = commonService.menuAuthList(param);
		
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
	 * 1. 개요 : 메뉴권한 등록/수정/삭제 처리
	 * 2. 처리내용 :
	 * </pre>
	 * @Method Name : menuAuthManager
	 * @param model
	 * @param sessionVO
	 * @param map
	 * @param bindingResult
	 * @param request
	 * @return Map
	 */
	@RequestMapping(value = "/menuAuthManager.do", method = RequestMethod.POST)
	@ResponseBody 
	public Map<String,Object> menuAuthManager(Model model, @ModelAttribute SessionVO sessionVO,
			@RequestParam HashMap<String,Object> map,BindingResult bindingResult,HttpServletRequest request) {
		
		Locale locale = LocaleLibrary.setLocale(sessionVO.getSessLanguage() != null  ? sessionVO.getSessLanguage() : ConfigData.getString("LANGUAGE"));
		
		List<MenuAuthVO> menuAuthList = new ArrayList<MenuAuthVO>();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		String[] arrData = map.get("inputStr") != null ? map.get("inputStr").toString().split(",") : null;
		String type = map.get(Constant.TYPE) != null ? map.get(Constant.TYPE).toString() : "";
		 
		try {
			
			// 처리할 메뉴 목록 가져오기.
			if(type.equals(Constant.INSERT))	{
				menuAuthList = commonService.setMenuAuthParam(arrData,map);
			}else {
				menuAuthList = commonService.setMenuAuthParam(arrData) ;
			}
			
			// 타입에 따른 메뉴권한 처리 
			resultMap = commonService.menuAuthManager(menuAuthList, map);
			
		}catch(BizException e){
			resultMap.put("result",Constant.RESULT_FALSE);
			resultMap.put("message",e.getMessage());				
		}
		catch(Exception e)	{										
			resultMap.put("result",Constant.RESULT_FALSE);
			resultMap.put("message",messageSource.getMessage("common.system.error",new Object[0],locale));			
		}
		
		return resultMap;
	}
	
	/**
	 * 
	 * <pre>
	 * 1. 개요 : 메뉴권한 관리 메뉴 목록 리스트 가져오기.www
	 * 2. 처리내용 :
	 * </pre>
	 * @Method Name : menuList
	 * @param model
	 * @param sessionVO
	 * @param map
	 * @param request
	 * @return Map
	 */
	@RequestMapping("/menuList.do")
	@ResponseBody 
	public Map<String,Object> menuList(Model model, @ModelAttribute SessionVO sessionVO, @RequestParam HashMap<String,Object> map,
			HttpServletRequest request) {
		
		Locale locale = LocaleLibrary.setLocale(sessionVO.getSessLanguage() != null  ? sessionVO.getSessLanguage() : ConfigData.getString("LANGUAGE"));			
		Map<String, Object> resultMap = new HashMap<String, Object>();

		try {
			
			// 검색조건없음 / 파라미터 없음 - 등록된 모믄 메뉴 한번에 보여준다.			
			resultMap = commonService.menuList(map);		
									
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
	 * 1. 개용 : 시스템관리 - 환경설정관리
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : localExtManager
	 * @param sessionVO
	 * @param model
	 * @param map
	 * @return String
	 */
	@RequestMapping("confManager.do")
	public String confManager(@ModelAttribute SessionVO sessionVO,Model model,@RequestParam HashMap<String,Object> map) {
		
		Locale locale = LocaleLibrary.setLocale(sessionVO.getSessLanguage() != null  ? sessionVO.getSessLanguage() : ConfigData.getString("LANGUAGE"));
		
		List<CodeVO> versionList = new ArrayList<CodeVO>(); 
		
		HashMap<String,Object> param = new HashMap<String,Object>();
		Map<String, Object> menuInfo = new HashMap<String, Object>();
		
		param.put("menu_cd",map.get("menu_cd") != null ?  map.get("menu_cd").toString() : "" );
		param.put("role_id",sessionVO.getSessRole_id());
		
		String part = "";			// ALL/GROUP/TEAM - 관리자 페이지마다 다르게 적용처리한다.
		String productInfo = "";
		
		try {
			
			// 1. 관리자 ROLE 접근권한
			part = commonService.getMenuAuth(param);
			
			// 2. 페이지 네비게이션 :: 상위메뉴명 / 현재메뉴명
			menuInfo = commonService.pageMenuInfo(param);
			
			// 3. 버전관리 목록 정보 가져오기 :: parameter gcode_id
			param.put("gcode_id", Constant.CODE_VERSION);
			versionList = commonService.codeList(param);
			
			// 4. [APPLIANCE VERSION] 
			productInfo = ConfigData.getString("VERSION_INFO");
						
		}catch(BizException e){	
			logger.error(e.getMessage());
		}catch(Exception e)	{										
			logger.error(e.getMessage());
		}
		
		// 접근권한이 없거나 메뉴코드가 없는 경우 403 ERROR 페이지 이동 처리
		if(part.equals("") || param.get("menu_cd").toString().equals(""))	{			
			CommonUtil.setErrorMsg(model, Constant.ERROR_403, messageSource.getMessage("common.connect.error",new Object[0],locale),sessionVO.getSessContextRoot());
			return "error/message";
		}
		
		// call by reference
		CommonUtil.setSessionToModel(model, sessionVO);
		
		model.addAttribute("defaultFileSize",ConfigData.getInt("DOC.DEFAULT.FILESIZE"));
		model.addAttribute("menuInfo",menuInfo);
		model.addAttribute("versionList",versionList);
		model.addAttribute("productInfo",productInfo);
		model.addAttribute("topSelect",Constant.TOPMENU_SYSTEM);
		model.addAttribute("subSelect",Constant.SYSTEM_CONFMANAGER);
		
		return "sysadmin/confManager";
	}
	
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 시스템 환경설정 정보 조회 및 변경처리
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : confControl
	 * @param model
	 * @param sessionVO
	 * @param map
	 * @param request
	 * @return Map<String,Object>
	 */
	@RequestMapping(value="/confControl.do", method=RequestMethod.POST)
	@ResponseBody 
	public Map<String,Object> confControl(Model model, @ModelAttribute SessionVO sessionVO, @RequestParam HashMap<String,Object> map,
			HttpServletRequest request) {
		
		Locale locale = LocaleLibrary.setLocale(sessionVO.getSessLanguage() != null  ? sessionVO.getSessLanguage() : ConfigData.getString("LANGUAGE"));
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		// 업무구분자 - 환경설정 조회 및 수정처리
		String type = map.get(Constant.TYPE) != null ? map.get(Constant.TYPE).toString() : "";
		
		try {
			
			if(type.equals(Constant.UPDATE) )	{
				// 환경설정 수정처리
				resultMap = commonService.confProc(map);
				
			}else {
				// 환경설정 조회처리
				resultMap = commonService.confDetail(map);
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
	
}

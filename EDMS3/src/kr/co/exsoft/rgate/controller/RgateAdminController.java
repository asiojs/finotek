package kr.co.exsoft.rgate.controller;

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

import kr.co.exsoft.rgate.service.RgateService;
import kr.co.exsoft.rgate.vo.*;

/**
 * Rgate Admin Controller
 * @author 패키지 개발팀
 * @since 2014.07.17
 * @version 3.0
 *
 */
@Controller
@SessionAttributes("sessionVO")
@RequestMapping("/admin")
public class RgateAdminController {

	@Autowired
	private RgateService rgateService;
	
	@Autowired
	private CommonService commonService;
	
	@Autowired
	private MessageSource messageSource;
	
    @Autowired
    private DefaultBeanValidator beanValidator;
    
    protected static final Log logger = LogFactory.getLog(RgateAdminController.class);
    
    /**
     * 
     * <pre>
     * 1. 개요 : 저장금지 확장자 관리
     * 2. 처리내용 :
     * </pre>
     * @Method Name : groupAdmin
     * @param sessionVO
     * @param model
     * @param map
     * @return String
     */
	@RequestMapping("extManager.do")
	public String localExtManager(@ModelAttribute SessionVO sessionVO,Model model,@RequestParam HashMap<String,Object> map) {
		
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
		model.addAttribute("topSelect",Constant.TOPMENU_RGATE);
		model.addAttribute("subSelect",Constant.RGATE_EXTMANAGER);
		
		return "rgate/extManager";
	}
	
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : RGate 저장금지 확장자 설정 관리 
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : localRgateList
	 * @param model
	 * @param sessionVO
	 * @param map
	 * @param request
	 * @return Map<String,Object>
	 */
	@RequestMapping(value="/rgateList.do", method=RequestMethod.POST)
	@ResponseBody 
	public Map<String,Object> localRgateList(Model model, @ModelAttribute SessionVO sessionVO, @RequestParam HashMap<String,Object> map,
			HttpServletRequest request) {
		
		Locale locale = LocaleLibrary.setLocale(sessionVO.getSessLanguage() != null  ? sessionVO.getSessLanguage() : ConfigData.getString("LANGUAGE"));
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		HashMap<String, Object> param = new HashMap<String, Object>();
		
		// 입력 파라미터 유효성 체크
		param.put("strIndex",map.get("strIndex") != null ? map.get("strIndex") : "" );
		param.put("strKeyword",map.get("strKeyword") != null ? map.get("strKeyword") : "" );
		param.put("orderCol",map.get("sidx") != null ? map.get("sidx") : "WORK_TYPE");				
		param.put("orderType",map.get("sord") != null ? map.get("sord") : "ASC");
		param.put("page_size",map.get("rows") != null ? map.get("rows") : sessionVO.getSessPage_size());
		
		// 작업유형 구분자
		param.put("work_type",map.get("work_type") != null ? map.get("work_type") : Constant.LSC_EXTENSION);
		
		// page 설정
		param.put("nPage",CommonUtil.getPage(map));
		
		try {
				
			// RGATE 정책 목록 가져오기
			resultMap = rgateService.rgatePageList(param);
			
			
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
	 * 1. 개용 :  확장자/프로세스 목록 가져오기
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : extProcessList
	 * @param model
	 * @param sessionVO
	 * @param map
	 * @param request
	 * @return Map<String,Object>
	 */
	@RequestMapping(value="/extProcess.do", method=RequestMethod.POST)
	@ResponseBody 
	public Map<String,Object> extProcessList(Model model, @ModelAttribute SessionVO sessionVO, @RequestParam HashMap<String,Object> map,
			HttpServletRequest request) {
		
		Locale locale = LocaleLibrary.setLocale(sessionVO.getSessLanguage() != null  ? sessionVO.getSessLanguage() : ConfigData.getString("LANGUAGE"));
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		HashMap<String, Object> param = new HashMap<String, Object>();
		
		// 입력 파라미터 유효성 체크
		param.put("strIndex",map.get("strIndex") != null ? map.get("strIndex") : "" );
		param.put("strKeyword",map.get("strKeyword") != null ? map.get("strKeyword") : "" );
		param.put("orderCol",map.get("sidx") != null ? map.get("sidx") : "MANAGE_NAME");				
		param.put("orderType",map.get("sord") != null ? map.get("sord") : "ASC");		
		param.put("manage_type",map.get("manage_type") != null ? map.get("manage_type") : Constant.MANAGE_EXT);

		// 확장자/프로세스 목록 리스트 페이지 SKIPPED
		
		try {
			
			// 확장자/프로세스 목록 가져오기
			resultMap = rgateService.rgateExtProcList(param,sessionVO);
			
			
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
	 * 1. 개용 : DB에 입력된 XR_MAPPING 리스트 목록을 가져온다. 
	 * 2. 처리내용 : 로컬저장금지 정책 입력전 중복입력여부 체크
	 * </pre>
	 * @Method Name : extMappingList
	 * @param model
	 * @param sessionVO
	 * @param map
	 * @param request
	 * @return Map<String,Object>
	 */
	@RequestMapping(value="/extMappingList.do", method=RequestMethod.POST)
	@ResponseBody 
	public Map<String,Object> extMappingList(Model model, @ModelAttribute SessionVO sessionVO, @RequestParam HashMap<String,Object> map,
			HttpServletRequest request) {
		
		Locale locale = LocaleLibrary.setLocale(sessionVO.getSessLanguage() != null  ? sessionVO.getSessLanguage() : ConfigData.getString("LANGUAGE"));
		
		Map<String, Object> resultMap = new HashMap<String, Object>();

		try {
			
			// 설정된 XR_RGATE_MAPPING 리스트 가져오기	
			resultMap = rgateService.rgateMappingList(map);			
			
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
	 * 1. 개용 : 프로그램별 작업폴더 매핑 목록 가져오기.
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : exceptionMappingList
	 * @param model
	 * @param sessionVO
	 * @param map
	 * @param request
	 * @return Map<String,Object>
	 */
	@RequestMapping(value="/exceptionMappingList.do", method=RequestMethod.POST)
	@ResponseBody 
	public Map<String,Object> exceptionMappingList(Model model, @ModelAttribute SessionVO sessionVO, @RequestParam HashMap<String,Object> map,
			HttpServletRequest request) {
		
		Locale locale = LocaleLibrary.setLocale(sessionVO.getSessLanguage() != null  ? sessionVO.getSessLanguage() : ConfigData.getString("LANGUAGE"));
		
		Map<String, Object> resultMap = new HashMap<String, Object>();

		try {
			
			// 설정된 XR_RGATE_PROCESS 리스트를 가져온다.
			resultMap = rgateService.rgateExceptionList(map);
			
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
	 * 1. 개용 : RGate 정책 등록/수정/삭제 처리
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : rgatePolicyManager
	 * @param model
	 * @param sessionVO
	 * @param map
	 * @param bindingResult
	 * @param request
	 * @return Map<String,Object>
	 */
	@RequestMapping(value = "/rgatePolicyManager.do", method = RequestMethod.POST)
	@ResponseBody 
	public Map<String,Object> rgatePolicyManager(Model model, @ModelAttribute SessionVO sessionVO,
			@RequestParam HashMap<String,Object> map,BindingResult bindingResult,HttpServletRequest request) {
		
		Locale locale = LocaleLibrary.setLocale(sessionVO.getSessLanguage() != null  ? sessionVO.getSessLanguage() : ConfigData.getString("LANGUAGE"));
		
		List<RGateMappingVO> rgateMappingVO = new ArrayList<RGateMappingVO>();
		List<HashMap<String,Object>> policyList = new ArrayList<HashMap<String,Object>>();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		// 업무구분자 - RGATE 정책코드
		String type = map.get(Constant.TYPE) != null ? map.get(Constant.TYPE).toString() : "";
		 
		try {
			
			// 저장금지 확장자 관리 - LSC_EXTENSION
			// 저장허용 프로그램 관리 - LSC_EPROC
			// 로컬 저장 허용 관리 - LSC_CONTROL
			// USB 저장 허용관리 - LSC_ENABLE_USB
			// 네트워크 접속 허용관리 - LSC_NETDRIVE_ADDR
			// 프로세스 예외폴더 설정 - LSC_WDIRS_PROC
			// 클라이언트 제거 비밀번호 설정 - RGC_UNINSTALL_PASS
			
			if(type.equals(Constant.INSERT))	{
				
				// 입력데이터 유효성 체크 및 등록 파라미터 설정	
				rgateMappingVO = rgateService.setRgateMappingList(map);
				
				// 데이터베이스 처리
				resultMap = rgateService.rgatePolicyManageWrite(rgateMappingVO, map);
				
			}else if(type.equals(Constant.UPDATE))	{
				
				// 입력데이터 유효성 체크 및 수정 파라미터 설정	
				policyList = rgateService.setRgateMappingUpdateList(map);
				
				// 데이터베이스 처리
				resultMap = rgateService.rgatePolicyManageUpdate(policyList, map);
				
			}else if(type.equals(Constant.DELETE))	{
				
				// 입력데이터 유효성 체크 및 삭제 파라미터 설정	
				policyList = rgateService.setRgateMappingDelList(map);
			
				// 데이터베이스 처리
				resultMap = rgateService.rgatePolicyManageDelete(policyList, map);
				
			}		
			
		
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
	 * 1. 개용 : 저장 허용 프로그램 관리
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : procManager
	 * @param sessionVO
	 * @param model
	 * @param map
	 * @return String
	 */
	@RequestMapping("procManager.do")
	public String procManager(@ModelAttribute SessionVO sessionVO,Model model,@RequestParam HashMap<String,Object> map) {
		
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
		model.addAttribute("topSelect",Constant.TOPMENU_RGATE);
		model.addAttribute("subSelect",Constant.RGATE_PROCMANAGER);
		
		return "rgate/procManager";
	}
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 로컬 저장 허용관리
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : controlManager
	 * @param sessionVO
	 * @param model
	 * @param map
	 * @return String
	 */
	@RequestMapping("controlManager.do")
	public String controlManager(@ModelAttribute SessionVO sessionVO,Model model,@RequestParam HashMap<String,Object> map) {
		
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

		// call by reference
		CommonUtil.setSessionToModel(model, sessionVO);
		
		model.addAttribute("part",partInfo.get("part").toString());
		model.addAttribute("menuInfo",menuInfo);
		model.addAttribute("topSelect",Constant.TOPMENU_RGATE);
		model.addAttribute("subSelect",Constant.RGATE_CONTROLMANAGER);
		
		return "rgate/controlManager";
	}
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : USB 저장 허용관리
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : usbManager
	 * @param sessionVO
	 * @param model
	 * @param map
	 * @return String
	 */
	@RequestMapping("usbManager.do")
	public String usbManager(@ModelAttribute SessionVO sessionVO,Model model,@RequestParam HashMap<String,Object> map) {
		
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
		model.addAttribute("topSelect",Constant.TOPMENU_RGATE);
		model.addAttribute("subSelect",Constant.RGATE_USBMANAGER);
		
		return "rgate/usbManager";
	}
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 네트워크 접속 허용관리
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : networkManager
	 * @param sessionVO
	 * @param model
	 * @param map
	 * @return String
	 */
	@RequestMapping("networkManager.do")
	public String networkManager(@ModelAttribute SessionVO sessionVO,Model model,@RequestParam HashMap<String,Object> map) {
		
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
		model.addAttribute("topSelect",Constant.TOPMENU_RGATE);
		model.addAttribute("subSelect",Constant.RGATE_NETWORKMANAGER);
		
		return "rgate/networkManager";
	}
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 프로세스 예외폴더 설정 
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : exceptionManager
	 * @param sessionVO
	 * @param model
	 * @param map
	 * @return String
	 */
	@RequestMapping("exceptionManager.do")
	public String exceptionManager(@ModelAttribute SessionVO sessionVO,Model model,@RequestParam HashMap<String,Object> map) {
		
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
		model.addAttribute("topSelect",Constant.TOPMENU_RGATE);
		model.addAttribute("subSelect",Constant.RGATE_EXCEPTIONMANAGER);
		
		return "rgate/exceptionManager";
	}
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 :  클라이언트 제거 비밀번호 설정
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : uninstallManager
	 * @param sessionVO
	 * @param model
	 * @param map
	 * @return String
	 */
	@RequestMapping("uninstallManager.do")
	public String uninstallManager(@ModelAttribute SessionVO sessionVO,Model model,@RequestParam HashMap<String,Object> map) {
		
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
		model.addAttribute("topSelect",Constant.TOPMENU_RGATE);
		model.addAttribute("subSelect",Constant.RGATE_UNINSTALLMANAGER);
		
		return "rgate/uninstallManager";
	}
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 프로그램별 작업폴더 관리 조회처리
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : exceptionList
	 * @param model
	 * @param sessionVO
	 * @param map
	 * @param request
	 * @return Map<String,Object>
	 */
	@RequestMapping(value="/exceptionList.do", method=RequestMethod.POST)
	@ResponseBody 
	public Map<String,Object> exceptionList(Model model, @ModelAttribute SessionVO sessionVO, @RequestParam HashMap<String,Object> map,
			HttpServletRequest request) {
		
		Locale locale = LocaleLibrary.setLocale(sessionVO.getSessLanguage() != null  ? sessionVO.getSessLanguage() : ConfigData.getString("LANGUAGE"));
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		HashMap<String, Object> param = new HashMap<String, Object>();

		try {
				
			// RGATE 프로그램별 작업폴더 목록 가져오기
			resultMap = rgateService.exceptionPageList(param);
						
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
	 * 1. 개용 : 프로그램 작업폴더 관리 : 목록추가/삭제/허용폴더수정처리
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : exceptionProcess
	 * @param model
	 * @param sessionVO
	 * @param map
	 * @param bindingResult
	 * @param request
	 * @return Map<String,Object>
	 */
	@RequestMapping(value = "/exceptionProcess.do", method = RequestMethod.POST)
	@ResponseBody 
	public Map<String,Object> exceptionProcess(Model model, @ModelAttribute SessionVO sessionVO,
			@RequestParam HashMap<String,Object> map,BindingResult bindingResult,HttpServletRequest request) {
		
		Locale locale = LocaleLibrary.setLocale(sessionVO.getSessLanguage() != null  ? sessionVO.getSessLanguage() : ConfigData.getString("LANGUAGE"));
		
		List<RGateProcessVO> rgateProcess = new ArrayList<RGateProcessVO>();
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		 
		try {

			// 프로세스 예외폴더 설정 - LSC_WDIRS_PROC
			
			// 프로그램 작업폴더 수행 목록 및 유효성 체크		
			rgateProcess = rgateService.exceptInsertValid(map);
			
			// 프로그램 작업폴더 DB처리
			resultMap = rgateService.exceptProcess(rgateProcess, map);
			
		
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
	 * 1. 개용 : 프로그램/IP 등록 삭제
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name = manageListControll
	 * @param model
	 * @param rGateListVO
	 * @param map
	 * @param bindingResult
	 * @param request
	 * @return
	 */
	@RequestMapping("manageListControll.do")
	@ResponseBody 
	public Map<String,Object> manageListControll(Model model, @ModelAttribute RGateListVO rGateListVO,
			@RequestParam HashMap<String,Object> map,BindingResult bindingResult,HttpServletRequest request) {
		
		
		List<HashMap<String,Object>> policyList = new ArrayList<HashMap<String,Object>>();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		// 업무구분자 - RGATE 리스트
		String type = map.get(Constant.TYPE) != null ? map.get(Constant.TYPE).toString() : "";
		String manageType = map.get("manage_type") != null ? map.get("manage_type").toString() : "";
		String manageName = map.get("manage_name") != null ? map.get("manage_name").toString() : "";
		String isDefault = map.get("is_default") != null ? map.get("is_default").toString() : "F";
		 
		try {
			// XR_RGATE_LIST
			// 저장허용 프로그램 리스트 - PROC
			// 네트워크 접속 허용관리 리스트 - IP						
			
			if(type.equals(Constant.INSERT)) {
				
				rGateListVO.setManage_type(manageType);
				rGateListVO.setManage_name(manageName);
				rGateListVO.setIs_default(isDefault);				
				
				resultMap = rgateService.rgateListWrite(rGateListVO);
				
			}else if(type.equals(Constant.DELETE))	{
				
				policyList = rgateService.setRgateListDelList(map);
				resultMap = rgateService.rgateListDelete(policyList, map);
				
			}		
			
		
		}catch(Exception e)	{										
			resultMap.put("result",Constant.RESULT_FALSE);	
			resultMap.put("message",e.getMessage());		
		}
		
		return resultMap;
		
	}
	
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 저장 허용 프로그램 관리
	 * 2. 처리내용 : 
	 * </pre>
	 * 
	 * @Method Name : procList
	 * @param model
	 * @param sessionVO
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/procList.do", method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> procList(Model model, @ModelAttribute SessionVO sessionVO, @RequestParam HashMap<String,Object> map,
			HttpServletRequest request) {
		
		Locale locale = LocaleLibrary.setLocale(sessionVO.getSessLanguage() != null  ? sessionVO.getSessLanguage() : ConfigData.getString("LANGUAGE"));
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		HashMap<String, Object> param = new HashMap<String, Object>();
		
		//입력 파라미터 유효성 체크
		param.put("strIndex",map.get("strIndex") != null ? map.get("strIndex") : "" );
		param.put("strKeyword",map.get("strKeyword") != null ? map.get("strKeyword") : "" );
		param.put("orderCol",map.get("sidx") != null ? map.get("sidx") : "WORK_TYPE");				
		param.put("orderType",map.get("sord") != null ? map.get("sord") : "ASC");
		param.put("page_size",map.get("rows") != null ? map.get("rows") : sessionVO.getSessPage_size());
		
		// 작업유형 구분자
		param.put("work_type",map.get("work_type") != null ? map.get("work_type") : Constant.LSC_EPROC);
				
		// page 설정
		param.put("nPage",CommonUtil.getPage(map));

		try {
			// RGATE 정책 목록 가져오기
			resultMap = rgateService.rgatePageList(param);
			
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

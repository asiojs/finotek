package kr.co.exsoft.document.controller;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import kr.co.exsoft.common.service.CommonService;
import kr.co.exsoft.common.vo.SessionVO;
import kr.co.exsoft.document.service.PageService;
import kr.co.exsoft.eframework.configuration.Constant;
import kr.co.exsoft.eframework.exception.BizException;
import kr.co.exsoft.eframework.library.LocaleLibrary;
import kr.co.exsoft.eframework.util.CommonUtil;
import kr.co.exsoft.eframework.util.ConfigData;

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

/**
 * Page Admin Controller
 *
 * @author 패키지팀
 * @since 2014. 9. 16.
 * @version 1.0
 * 
 */
@Controller
@SessionAttributes("sessionVO")
@RequestMapping("/admin")
public class PageAdminController {

	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	private PageService pageService;
	
	@Autowired
	private CommonService commonService;
		
    @Autowired
    private DefaultBeanValidator beanValidator;
    
	protected static final Log logger = LogFactory.getLog(TypeAdminController.class);
	
	
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 중복파일 관리 
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : duplicateManager
	 * @param sessionVO
	 * @param model
	 * @param map
	 * @return String
	 */
	@RequestMapping("duplicateManager.do")
	public String duplicateManager(@ModelAttribute SessionVO sessionVO,Model model,@RequestParam HashMap<String,Object> map) {
		
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
			return "error/message";
		}

		CommonUtil.setSessionToModel(model, sessionVO);
		
		model.addAttribute("part",part);
		model.addAttribute("menuInfo",menuInfo);
		model.addAttribute("topSelect",Constant.TOPMENU_DOCUMENT);
		model.addAttribute("subSelect",Constant.DOCUMENT_DUPLICATEMANAGER);
		
		return "docadmin/duplicateManager";
	}

	
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 중복파일 목록 가져오기
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : duplicatePage
	 * @param model
	 * @param sessionVO
	 * @param map
	 * @param request
	 * @return Map<String,Object>
	 */
	@RequestMapping(value="/duplicatePage.do", method=RequestMethod.POST)
	@ResponseBody 
	public Map<String,Object> duplicatePage(Model model, @ModelAttribute SessionVO sessionVO, @RequestParam HashMap<String,Object> map,
			HttpServletRequest request) {
		
		Locale locale = LocaleLibrary.setLocale(sessionVO.getSessLanguage() != null  ? sessionVO.getSessLanguage() : ConfigData.getString("LANGUAGE"));
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		HashMap<String, Object> param = new HashMap<String, Object>();
		
		// 입력 파라미터 유효성 체크		
		param.put("contextRoot",sessionVO.getSessContextRoot());
		param.put("strKeyword",map.get("strKeyword") != null ? map.get("strKeyword") : "" );
		param.put("orderCol",map.get("sidx") != null ? map.get("sidx") : "page_name");
		param.put("orderType",map.get("sord") != null ? map.get("sord") : "ASC");
		param.put("page_size",map.get("rows") != null ? map.get("rows") : sessionVO.getSessPage_size());		
		param.put("sdate",map.get("sdate") != null ? map.get("sdate").toString() : "");
		param.put("edate",map.get("edate") != null ? map.get("edate").toString() : "");
		param.put("nPage",CommonUtil.getPage(map));
		
		try {
			
			// 1.중복수인 경우 orderCol 변경처리
			if(param.get("orderCol").toString().toLowerCase().equals("page_count")) {
				param.put("orderCol","COUNT(P.PAGE_NAME)");
			}

			// 2.중복파일 목록 가져오기
			resultMap = pageService.duplicatePageList(param)	;
			
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

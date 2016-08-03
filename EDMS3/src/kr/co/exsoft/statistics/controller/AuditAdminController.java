package kr.co.exsoft.statistics.controller;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import kr.co.exsoft.common.service.CommonService;
import kr.co.exsoft.common.vo.SessionVO;
import kr.co.exsoft.statistics.service.AuditService;
import kr.co.exsoft.eframework.configuration.Constant;
import kr.co.exsoft.eframework.exception.BizException;
import kr.co.exsoft.eframework.library.LocaleLibrary;
import kr.co.exsoft.eframework.util.CommonUtil;
import kr.co.exsoft.eframework.util.ConfigData;
import kr.co.exsoft.eframework.util.StringUtil;

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


/**
 * 열람감사관리
 *
 * @author 패키지팀
 * @since 2014. 9. 15.
 * @version 1.0
 * 
 */
@Controller
@SessionAttributes("sessionVO")
@RequestMapping("/admin")
public class AuditAdminController {

	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	private AuditService auditService;
	
	@Autowired
	private CommonService commonService;
	
	protected static final Log logger = LogFactory.getLog(AuditAdminController.class);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 대량문서 열람 감사 관리
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : auditManager
	 * @param sessionVO
	 * @param model
	 * @param map
	 * @return String
	 */
	@RequestMapping("auditManager.do")
	public String auditManager(@ModelAttribute SessionVO sessionVO,Model model,@RequestParam HashMap<String,Object> map) {
		
		Locale locale = LocaleLibrary.setLocale(sessionVO.getSessLanguage() != null  ? sessionVO.getSessLanguage() : ConfigData.getString("LANGUAGE"));
		
		HashMap<String,Object> param = new HashMap<String,Object>();
		Map<String, Object> menuInfo = new HashMap<String, Object>();
		
		param.put("menu_cd",map.get("menu_cd") != null ?  map.get("menu_cd").toString() : "" );
		param.put("role_id",sessionVO.getSessRole_id());
				
		String part = "";			// ALL/GROUP/TEAM - 관리자 페이지마다 다르게 적용처리한다.
		String sdate = "";
		String edate = "";
		
		try {
						
			// 0.기본날짜 설정(1MONTH)
			Calendar eday = Calendar.getInstance();
			Calendar sday = (Calendar)eday.clone();
			
			sday.add(Calendar.DATE,-30);	
			
			sdate = StringUtil.print(sday);
			edate = StringUtil.print(eday);
			
			// 1. 관리자 ROLE 접근권한
			part = commonService.getMenuAuth(param);
						
			// 2. 페이지 네비게이션 :: 상위메뉴명 / 현재메뉴명
			menuInfo = commonService.pageMenuInfo(param);
			
		}catch(BizException e){	
			logger.error(e.getMessage());
		}catch(Exception e)	{										
			logger.error(e.getMessage());
		}
		
		// 접근권한이 없는 경우
		if(part.equals(""))	{			
			CommonUtil.setErrorMsg(model, Constant.ERROR_403, messageSource.getMessage("common.connect.error",new Object[0],locale),sessionVO.getSessContextRoot());
			return "error/message";
		}
		
		// call by reference
		CommonUtil.setSessionToModel(model, sessionVO);
		
		model.addAttribute("part",part);
		model.addAttribute("sdate",sdate);
		model.addAttribute("edate",edate);
		model.addAttribute("menuInfo",menuInfo);
		model.addAttribute("topSelect",Constant.TOPMENU_DOCUMENT);
		model.addAttribute("subSelect",Constant.DOCUMENT_AUDITMANAGER);
		
		return "docadmin/auditManager";
	}
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 대량문서 열람 감사 관리 목록 가져오기
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : auditPageList
	 * @param model
	 * @param sessionVO
	 * @param map
	 * @param request
	 * @return Map<String,Object>
	 */
	@RequestMapping(value="/auditPage.do", method=RequestMethod.POST)
	@ResponseBody 
	public Map<String,Object> auditPageList(Model model, @ModelAttribute SessionVO sessionVO, @RequestParam HashMap<String,Object> map,
			HttpServletRequest request) {
		
		Locale locale = LocaleLibrary.setLocale(sessionVO.getSessLanguage() != null  ? sessionVO.getSessLanguage() : ConfigData.getString("LANGUAGE"));
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		HashMap<String, Object> param = new HashMap<String, Object>();
		
		// 입력 파라미터 유효성 체크
		param.put("orderCol",map.get("sidx") != null ? map.get("sidx") : "audit_date");
		param.put("orderType",map.get("sord") != null ? map.get("sord") : "ASC");
		param.put("page_size",map.get("rows") != null ? map.get("rows") : sessionVO.getSessPage_size());					
		param.put("sdate",map.get("sdate") != null ? map.get("sdate").toString().replaceAll("-","") : "");
		param.put("edate",map.get("edate") != null ? map.get("edate").toString().replaceAll("-","") : "");
		param.put("nPage",CommonUtil.getPage(map));
		param.put("contextRoot",sessionVO.getSessContextRoot());
		
		try {
			
			// 1.검색조건 파라미터 재설정
			if(param.get("orderCol").toString().equals("user_name")) {
				param.put("orderCol","U.USER_NAME_KO");
			}else {
				param.put("orderCol","T."+param.get("orderCol").toString());
			}
			
			// 2.권한관리대상 부서 목록 리스트 가져온다.
			String part = map.get("part") != null ?  map.get("part").toString() : Constant.MENU_TEAM;									
			List<String> authGroupList = commonService.authGroupList(part,sessionVO);
			param.put("part",part);
			param.put("authGroupList",authGroupList);			
			
			// 4.대량문서 열람 감사 목록 조회처리
			resultMap = auditService.auditPageList(param);
			
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
	 * 1. 개용 : 대량문서 열람 감사 관리 상세 목록 가져오기
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : auditDetailPageList
	 * @param model
	 * @param sessionVO
	 * @param map
	 * @param request
	 * @return Map<String,Object>
	 */
	@RequestMapping(value="/auditDetailPage.do", method=RequestMethod.POST)
	@ResponseBody 
	public Map<String,Object> auditDetailPageList(Model model, @ModelAttribute SessionVO sessionVO, @RequestParam HashMap<String,Object> map,
			HttpServletRequest request) {
		
		Locale locale = LocaleLibrary.setLocale(sessionVO.getSessLanguage() != null  ? sessionVO.getSessLanguage() : ConfigData.getString("LANGUAGE"));
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		HashMap<String, Object> param = new HashMap<String, Object>();
		
		// 입력 파라미터 유효성 체크
		param.put("orderCol",map.get("sidx") != null ? map.get("sidx") : "audit_date");
		param.put("orderType",map.get("sord") != null ? map.get("sord") : "ASC");
		param.put("page_size",map.get("rows") != null ? map.get("rows") : sessionVO.getSessPage_size());					
		
		param.put("audit_date",map.get("audit_date") != null ? map.get("audit_date") : "");
		param.put("user_id",map.get("user_id") != null ? map.get("user_id") : "");
		
		// page 설정
		param.put("nPage",CommonUtil.getPage(map));
		param.put("contextRoot",sessionVO.getSessContextRoot());
		
		try {
		
			if(!param.get("audit_date").toString().equals("") 
					&& !param.get("user_id").toString().equals("")) {
				
				// 대량문서 열람 감사 목록 조회처리
				resultMap = auditService.auditDetailPageList(param);
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

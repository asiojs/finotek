package kr.co.exsoft.statistics.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;
import org.springmodules.validation.commons.DefaultBeanValidator;

import kr.co.exsoft.common.service.CommonService;
import kr.co.exsoft.common.vo.SessionVO;
import kr.co.exsoft.eframework.configuration.Constant;
import kr.co.exsoft.eframework.exception.BizException;
import kr.co.exsoft.eframework.library.ExcelCntView;
import kr.co.exsoft.eframework.library.ExcelSumView;
import kr.co.exsoft.eframework.library.ExcelView;
import kr.co.exsoft.eframework.library.LocaleLibrary;
import kr.co.exsoft.eframework.util.ChartUtil;
import kr.co.exsoft.eframework.util.CommonUtil;
import kr.co.exsoft.eframework.util.ConfigData;
import kr.co.exsoft.statistics.service.StatisticsService;

/**
 * 통계/감사 정책 관련 관리자 컨트롤
 * @author 패키지 개발팀
 * @since 2014.07.17
 * @version 3.0
 *
 */
@Controller
@SessionAttributes("sessionVO")
@RequestMapping("/statistics")
public class StatisticsAuthController {

	@Autowired
	private StatisticsService statisticsService;
	
	@Autowired
	private CommonService commonService;
	
	@Autowired
	private MessageSource messageSource;
	
    @Autowired
    private DefaultBeanValidator beanValidator;
    
	protected static final Log logger = LogFactory.getLog(StatisticsAuthController.class);

	/**
	 * 
	 * <pre>
	 * 1. 개용 : 통계 > 좌측 메뉴별 JSP Url 
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : statisticsMenu
	 * @param sessionVO
	 * @param model
	 * @param map
	 * @return String
	 */
	@RequestMapping("/statisticsMenu.do")
	public String statisticsMenu(@ModelAttribute SessionVO sessionVO,Model model,@RequestParam HashMap<String,Object> map) {
		
		Locale locale = LocaleLibrary.setLocale(sessionVO.getSessLanguage() != null  ? sessionVO.getSessLanguage() : ConfigData.getString("LANGUAGE"));
		
		Map<String, Object> menuInfo = new HashMap<String, Object>();
		Map<String, Object> partInfo = new HashMap<String, Object>();
		
		String strMenuType = map.get("type") != null ? (String)map.get("type") : "";
		String strMenuUrl = "";
		
		try {
			
			switch (strMenuType) {
			
				case "myStatistics"				: strMenuUrl = "statistics/myStatistics"; 				break;
				case "M040"			: strMenuUrl = "statistics/loginStatistics";			break;
				case "M041"		: strMenuUrl = "statistics/userDocStatistics";			break;
				case "M042"		: strMenuUrl = "statistics/groupDocStatistics";			break;
				case "M043"		: strMenuUrl = "statistics/decadeDocStatistics";		break;
				case "M044"		: strMenuUrl = "statistics/userFolderStatistics"; 		model.addAttribute("workType",Constant.WORK_TYPE_USER);		break;
				case "M045"		: strMenuUrl = "statistics/folderDocStatistics";		model.addAttribute("workType",Constant.WORK_TYPE_FOLDER);	break;
				case "M046"			: strMenuUrl = "statistics/typeStatistics";				break;
				case "M047"	: strMenuUrl = "statistics/securityLevelStatistics";	break;
				case "M048"			: strMenuUrl = "statistics/quotaStatistics";			break;

				default:
					CommonUtil.setErrorMsg(model, Constant.ERROR_403, messageSource.getMessage("common.nopage.error",new Object[0],locale),sessionVO.getSessContextRoot());
					return "error/message";
			}
			
			// 통계페이지 접속권한 체크 - 내문서현황제외
			if(!strMenuType.equals("myStatistics")) {
				commonService.setPageToModel(map,menuInfo,partInfo,sessionVO);
				model.addAttribute("part",partInfo.get("part").toString());		
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
		
		CommonUtil.setSessionToModel(model, sessionVO); 
		
		model.addAttribute("menuType",Constant.TOPMENU_STATISTICS);		
		model.addAttribute("menuInfo",menuInfo);		
		model.addAttribute("menuAuthList",sessionVO.getSessMenuAuth());
		model.addAttribute("staticsMenu",Constant.STATICS_MENU);
		
		return strMenuUrl;
	}

	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 사용자별 등록/활용 현황 그리드 리스트 
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : userDocGridList
	 * @param model
	 * @param sessionVO
	 * @param map
	 * @param request
	 * @return Map<String,Object>
	 */
	@RequestMapping(value="/userDocGridList.do")
	@ResponseBody 
	public ModelAndView userDocGridList(Model model, @ModelAttribute SessionVO sessionVO, @RequestParam HashMap<String,Object> map,
			HttpServletRequest request) {
		
		Locale locale = LocaleLibrary.setLocale(sessionVO.getSessLanguage() != null  ? sessionVO.getSessLanguage() : ConfigData.getString("LANGUAGE"));
		
		List<String> authGroupList = new ArrayList<String>();
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		HashMap<String, Object> param = new HashMap<String, Object>();
		
		// 입력 파라미터 유효성 체크
		param.put("contextRoot",sessionVO.getSessContextRoot());
		param.put("orderCol",map.get("sidx") != null ? map.get("sidx") : "user_nm");
		param.put("orderType",map.get("sord") != null ? map.get("sord") : "ASC");
		param.put("page_size",map.get("rows") != null ? map.get("rows") : sessionVO.getSessPage_size());					
		param.put("sdate",map.get("sdate") != null ? map.get("sdate").toString() : "");
		param.put("edate",map.get("edate") != null ? map.get("edate").toString() : "");
		param.put("nPage",CommonUtil.getPage(map));		// page 설정
 		
		// 사용자별 등록/활용 현황 검색조건
		param.put("strIndex",map.get("strIndex") != null ? map.get("strIndex").toString() : Constant.IS_USER);
		param.put("strKeyword",map.get("strKeyword") != null ? map.get("strKeyword").toString() : "");

		// 권한 : ALL/GROUP/TEAM
		param.put("part",map.get("part") != null ? map.get("part").toString() : Constant.MENU_TEAM);
		
		// Excel Down 처리  : 파리미터=oper
		String oper = map.get("oper") != null ? map.get("oper").toString() : "";
		if(oper.equals(Constant.EXCEL_FORMAT)) {
			param.put("nPage",1);			
			param.put("page_size",ConfigData.getInt("EXCEL_MAX_LIMIT"));		// 엑셀 저장 row수		
		}

		try {
	
			// 1. 검색필터추가 :: 관리자 접근 권한 구분 (ALL/GROUP/TEAM)
			if(!param.get("part").toString().equals(Constant.MENU_ALL)) {				
				authGroupList = commonService.authGroupList(param.get("part").toString(), sessionVO);
				param.put("authGroupList",authGroupList);
			}else {
				param.put("authGroupList","");
			}
			
			// 1.1 엑셀다운로드시 한글검색어 인코딩 처리
			if(oper.equals(Constant.EXCEL_FORMAT)) {				
				param.put("strKeyword",map.get("strKeyword") != null ? new String(map.get("strKeyword").toString().getBytes("8859_1"),"utf-8") : "");	
			}
						
			// 2. 사용자별 등록/활용 현황 목록 가져오기
			resultMap = statisticsService.userDocStatisticsList(param);
			
			// Excel Down 처리 :: 목록
			if(oper.equals(Constant.EXCEL_FORMAT)) {
				
				CommonUtil.getExcelList(resultMap,model);
				
				String[]  members = { "user_nm", "user_id", "group_nm", "type_name","create_cnt","read_cnt","update_cnt","delete_cnt"};
				String[]  cell_headers = { "사용자명", "사용자ID", "부서명", "문서유형", "등록","조회","수정","삭제" };
				int[] cell_widths = { 30,30,30,20,15,15,15,15};
				
        		model.addAttribute("members",members);
        		model.addAttribute("cell_headers",cell_headers);
        		model.addAttribute("cell_widths",cell_widths);
        		model.addAttribute("fileName","downLoad.xls");
        		model.addAttribute("groupField","user_nm");
        		
				return new ModelAndView(new ExcelSumView());
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
	 * 
	 * <pre>
	 * 1. 개용 : 부서별 등록/활용 현황 그리드 리스트 
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : groupDocGridList
	 * @param model
	 * @param sessionVO
	 * @param map
	 * @param request
	 * @return ModelAndView
	 */
	@RequestMapping(value="/groupDocGridList.do")
	@ResponseBody 
	public ModelAndView groupDocGridList(Model model, @ModelAttribute SessionVO sessionVO, @RequestParam HashMap<String,Object> map,
			HttpServletRequest request) {
		
		Locale locale = LocaleLibrary.setLocale(sessionVO.getSessLanguage() != null  ? sessionVO.getSessLanguage() : ConfigData.getString("LANGUAGE"));
		
		List<String> authGroupList = new ArrayList<String>();
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		HashMap<String, Object> param = new HashMap<String, Object>();
		
		// 입력 파라미터 유효성 체크
		param.put("contextRoot",sessionVO.getSessContextRoot());
		param.put("orderCol",map.get("sidx") != null ? map.get("sidx") : "group_nm");
		param.put("orderType",map.get("sord") != null ? map.get("sord") : "ASC");
		param.put("page_size",map.get("rows") != null ? map.get("rows") : sessionVO.getSessPage_size());					
		param.put("sdate",map.get("sdate") != null ? map.get("sdate").toString() : "");
		param.put("edate",map.get("edate") != null ? map.get("edate").toString() : "");
		param.put("nPage",CommonUtil.getPage(map));		// page 설정 		
		param.put("strKeyword",map.get("strKeyword") != null ? map.get("strKeyword").toString() : "");		// 부서별 등록/활용 현황 검색조건		
		param.put("part",map.get("part") != null ? map.get("part").toString() : Constant.MENU_TEAM);		// 권한 : ALL/GROUP/TEAM
				
		String oper = map.get("oper") != null ? map.get("oper").toString() : "";										// Excel Down 처리  : 파리미터=oper
		String isChart = map.get("isChart") != null ? map.get("isChart").toString() : "";							// Chart
		
		String[]  members ;
		String[]  cell_headers ;
		int[] cell_widths;
		
		try {
			
			// 1. 검색필터추가 :: 관리자 접근 권한 구분 (ALL/GROUP/TEAM)
			if(!param.get("part").toString().equals(Constant.MENU_ALL)) {				
				authGroupList = commonService.authGroupList(param.get("part").toString(), sessionVO);
				param.put("authGroupList",authGroupList);
			}else {
				param.put("authGroupList","");
			}
						
			// 1.1 엑셀다운로드시 한글검색어 인코딩 처리
			if(oper.equals(Constant.EXCEL_FORMAT)) {				
				param.put("nPage",1);			
				param.put("page_size",ConfigData.getInt("EXCEL_MAX_LIMIT"));		// 엑셀 저장 row수		
				param.put("strKeyword",map.get("strKeyword") != null ? new String(map.get("strKeyword").toString().getBytes("8859_1"),"utf-8") : "");	
			}
						
			// 2. 부서별 등록/활용 현황 목록 가져오기
			resultMap = statisticsService.groupDocStatisticsList(param);
			
			// Excel Down 처리 :: 목록
			if(oper.equals(Constant.EXCEL_FORMAT)) {
				
				CommonUtil.getExcelList(resultMap,model);
				
				members = new String[]{ "group_nm", "group_id", "type_name","create_cnt","read_cnt","update_cnt","delete_cnt"};
				cell_headers = new String[]{ "부서명", "부서ID", "문서유형", "등록","조회","수정","삭제" };
				cell_widths = new int[]{ 30,30,20,15,15,15,15};
				
        		model.addAttribute("members",members);
        		model.addAttribute("cell_headers",cell_headers);
        		model.addAttribute("cell_widths",cell_widths);
        		model.addAttribute("fileName","downLoad.xls");
        		model.addAttribute("groupField","group_nm");
        		
				return new ModelAndView(new ExcelSumView());
			}
			
			// CHART 정보가져오기
			if(isChart.equals(Constant.CHART))	{
				
				HashMap<String,Object> chartMap = new HashMap<String,Object>();
								
				if(Integer.parseInt(resultMap.get("records").toString()) == 0)	{
					resultMap.put("result",Constant.RESULT_FALSE);
					resultMap.put("message",messageSource.getMessage("common.data.exist.error",new Object[0],locale));			
				}else {
								
					CommonUtil.getChartList(resultMap,chartMap);
					
					// Chart 필수값 입력체크
					String chartType = map.get("chartType") != null ? map.get("chartType").toString() : "";	
					String colType = map.get("colType") != null ? map.get("colType").toString() : "";
					String yTitle = map.get("yTitle") != null ? map.get("yTitle").toString() : "";
					
					if(chartType.equals("") || colType.equals("") || yTitle.equals("")) {
						resultMap.put("result",Constant.RESULT_FALSE);
						resultMap.put("message",messageSource.getMessage("common.required.error",new Object[0],locale));			
					}else {
					
						members = new String[]{ "group_nm", "group_id", "type_name","create_cnt","read_cnt","update_cnt","delete_cnt"};
						chartMap.put("members",members);
						
						// Chat 객체     		
		        		chartMap.put("width",650);
		        		chartMap.put("height",400);        		
		        		chartMap.put("mTitle","부서별 등록/활용 현황");
		        		chartMap.put("xTitle","부서");  
		        		chartMap.put("groupField","group_nm");        		
		        		chartMap.put("tooltips",true);		        		
		        		chartMap.put("statisticsType",Constant.CHART_GROUP_STATUS);
		        		chartMap.put("yTitle",yTitle);
		        		chartMap.put("colType",colType);
		        		chartMap.put("chartType",chartType);
		        		
		        		// Chart.js 데이터 생성 
						resultMap.put("chartData",ChartUtil.getChartDataSet(chartMap));
						resultMap.put("chartType",chartType);						
									        		
		        		resultMap.put("result",Constant.RESULT_TRUE);        		
					}
				}
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
	 * 
	 * <pre>
	 * 1. 개용 : 기간별 등록/활용 현황
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : decadeDocGridList
	 * @param model
	 * @param sessionVO
	 * @param map
	 * @param request
	 * @return ModelAndView
	 */
	@RequestMapping(value="/decadeDocGridList.do")
	@ResponseBody 
	public ModelAndView decadeDocGridList(Model model, @ModelAttribute SessionVO sessionVO, @RequestParam HashMap<String,Object> map,
			HttpServletRequest request) {
		
		Locale locale = LocaleLibrary.setLocale(sessionVO.getSessLanguage() != null  ? sessionVO.getSessLanguage() : ConfigData.getString("LANGUAGE"));
		
		List<String> authGroupList = new ArrayList<String>();
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		HashMap<String, Object> param = new HashMap<String, Object>();
		
		// 입력 파라미터 유효성 체크
		param.put("contextRoot",sessionVO.getSessContextRoot());
		param.put("orderCol",map.get("sidx") != null ? map.get("sidx") : "order_str");
		param.put("orderType",map.get("sord") != null ? map.get("sord") : "ASC");
		param.put("page_size",map.get("rows") != null ? map.get("rows") : sessionVO.getSessPage_size());					
		param.put("sdate",map.get("sdate") != null ? map.get("sdate").toString() : "");
		param.put("edate",map.get("edate") != null ? map.get("edate").toString() : "");

		// 기간별 등록/활용 현황 검색조건		
		param.put("strIndex",map.get("strIndex") != null ? map.get("strIndex").toString() : Constant.IS_USER);
		param.put("strKeyword",map.get("strKeyword") != null ? map.get("strKeyword").toString() : "");
		param.put("term",map.get("term") != null ? map.get("term").toString() : Constant.DAY_TERM);
		param.put("strYear",map.get("strYear") != null ? map.get("strYear").toString() : "");
		
		param.put("part",map.get("part") != null ? map.get("part").toString() : Constant.MENU_TEAM);		// 권한 : ALL/GROUP/TEAM
		
		// 월별 검색인 경우 sdate~edate 변경처리
		if(param.get("term").toString().equals(Constant.MONTH_TERM)) {
			param.put("sdate",param.get("strYear").toString()+"-01-01");
			param.put("edate",param.get("strYear").toString()+"-12-31");
		}
		
		// Excel Down 처리  : 파리미터=oper && 기간별 등록현황 페이징처리 없음 
		String oper = map.get("oper") != null ? map.get("oper").toString() : "";
		String isChart = map.get("isChart") != null ? map.get("isChart").toString() : "";		// Chart
		
		String[]  members ;
		String[]  cell_headers;
		
		try {
			
			// 1. 검색필터추가 :: 관리자 접근 권한 구분 (ALL/GROUP/TEAM)
			if(!param.get("part").toString().equals(Constant.MENU_ALL)) {				
				authGroupList = commonService.authGroupList(param.get("part").toString(), sessionVO);
				param.put("authGroupList",authGroupList);
			}else {
				param.put("authGroupList","");
			}
			
			// 1.1 엑셀다운로드시 한글검색어 인코딩 처리
			if(oper.equals(Constant.EXCEL_FORMAT)) {				
				param.put("strKeyword",map.get("strKeyword") != null ? new String(map.get("strKeyword").toString().getBytes("8859_1"),"utf-8") : "");	
			}
						
			// 2. 사용자 - 일별/월별에 따른 서비스 구분 처리
			if(param.get("strIndex").toString().equals(Constant.IS_USER)) {	
				resultMap = statisticsService.decadeUserDocStatisticsList(param);
			}else {
				resultMap = statisticsService.decadeGroupDocStatisticsList(param);
			}
			
			// Excel Down 처리 :: 목록
			if(oper.equals(Constant.EXCEL_FORMAT)) {
				
				CommonUtil.getExcelList(resultMap,model);
				
				members = new String[]{ "order_str","create_cnt","read_cnt","update_cnt","delete_cnt"};
				if(param.get("term").toString().equals(Constant.MONTH_TERM)) {
					cell_headers = new String[] { "년월","등록","조회","수정","삭제" };	
				}else {
					cell_headers = new String[] { "일자","등록","조회","수정","삭제" };
				}
				
				int[] cell_widths = { 30,15,15,15,15};
				
        		model.addAttribute("members",members);
        		model.addAttribute("cell_headers",cell_headers);
        		model.addAttribute("cell_widths",cell_widths);
        		model.addAttribute("fileName","downLoad.xls");
        		
				return new ModelAndView(new ExcelCntView());
			}
		
			// CHART 정보가져오기
			if(isChart.equals(Constant.CHART))	{
				
				HashMap<String,Object> chartMap = new HashMap<String,Object>();
				
				if(Integer.parseInt(resultMap.get("records").toString()) == 0)	{
					resultMap.put("result",Constant.RESULT_FALSE);
					resultMap.put("message",messageSource.getMessage("common.data.exist.error",new Object[0],locale));			
				}else {
									
					CommonUtil.getChartList(resultMap,chartMap);
					
					// Chart 필수값 입력체크
					String chartType = map.get("chartType") != null ? map.get("chartType").toString() : "";	
					String colType = map.get("colType") != null ? map.get("colType").toString() : "";
					String yTitle = map.get("yTitle") != null ? map.get("yTitle").toString() : "";
					
					if(chartType.equals("") || colType.equals("") || yTitle.equals("")) {
						resultMap.put("result",Constant.RESULT_FALSE);
						resultMap.put("message",messageSource.getMessage("common.required.error",new Object[0],locale));			
					}else {
																	
						members = new String[]{ "order_str","create_cnt","read_cnt","update_cnt","delete_cnt"};
						
		        		chartMap.put("width",650);
		        		chartMap.put("height",400); 
		        		chartMap.put("mTitle","기간별 등록/활용 현황");
		        		chartMap.put("xTitle","기간");        		        				        		
		        		chartMap.put("groupField","order_str");        		
		        		chartMap.put("tooltips",true);
						chartMap.put("members",members);		
		        		chartMap.put("statisticsType",Constant.CHART_DECADE_STATUS);
		        		chartMap.put("yTitle",yTitle);
		        		chartMap.put("colType",colType);
		        		chartMap.put("chartType",chartType);

		        		// Chart.js 데이터 생성
						resultMap.put("chartData",ChartUtil.getChartDataSet(chartMap));
						resultMap.put("chartType",chartType);		
						
		        		resultMap.put("result",Constant.RESULT_TRUE);        		
					}					
				}
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
	 * 
	 * <pre>
	 * 1. 개용 : 사용자/문서함별 소유 현황 || 문서함/폴더별 보유 현황
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : userFolderStatisticsList
	 * @param model
	 * @param sessionVO
	 * @param map
	 * @param request
	 * @return ModelAndView
	 */
	@RequestMapping(value="/userFolderStatisticsList.do")
	@ResponseBody 
	public ModelAndView userFolderStatisticsList(Model model, @ModelAttribute SessionVO sessionVO, @RequestParam HashMap<String,Object> map,
			HttpServletRequest request) {
		
		Locale locale = LocaleLibrary.setLocale(sessionVO.getSessLanguage() != null  ? sessionVO.getSessLanguage() : ConfigData.getString("LANGUAGE"));
		
		List<String> authGroupList = new ArrayList<String>();
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		HashMap<String, Object> param = new HashMap<String, Object>();
		
		// 입력 파라미터 유효성 체크
		param.put("contextRoot",sessionVO.getSessContextRoot());
		param.put("page_size",map.get("rows") != null ? map.get("rows") : sessionVO.getSessPage_size());					
		param.put("sdate",map.get("sdate") != null ? map.get("sdate").toString() : "");
		param.put("edate",map.get("edate") != null ? map.get("edate").toString() : "");
		param.put("nPage",CommonUtil.getPage(map));		// page 설정
 		
		// 사용자/문서함별 소유 현황 || 문서함/폴더별 보유 현황 검색조건
		param.put("strKeyword",map.get("strKeyword") != null ? map.get("strKeyword").toString() : "");				// USER-사용자/문서함별 구분값
		param.put("strIndex",map.get("strIndex") != null ? map.get("strIndex").toString() : Constant.IS_USER); 	// USER-사용자/문서함별 검색키워드값
		param.put("strFolderIdx",map.get("strFolderIdx") != null ? map.get("strFolderIdx").toString() : "");			// FOLDER-문서함/폴더함별 문서함 구분값
		param.put("workType",map.get("workType") != null ? map.get("workType").toString() : "");					// 업무구분 :: USER-사용자/문서함별  , FOLDER-문서함/폴더함별
		param.put("part",map.get("part") != null ? map.get("part").toString() : Constant.MENU_TEAM);				// 권한 : ALL/GROUP/TEAM
		
		if(param.get("workType").toString().equals(Constant.WORK_TYPE_FOLDER))	{
			param.put("orderCol",map.get("sidx") != null ? map.get("sidx").toString() : "FOLDER_NAME_KO");
			param.put("orderType",map.get("sord") != null ? map.get("sord") : "ASC");
		}else {
			//  : 그룹컬럼 문자열 변경처리 => LIST.USER_NM ASC, , 
			param.put("orderCol",map.get("sidx") != null ? map.get("sidx").toString().replaceAll(",","") : "user_nm");		
		}
				
		String oper = map.get("oper") != null ? map.get("oper").toString() : "";					// Excel Down 처리  : 파리미터=oper	
		String isChart = map.get("isChart") != null ? map.get("isChart").toString() : "";		// Chart
		
		String[]  members ;
		String[]  cell_headers ;
		int[] cell_widths;
		
		try {
			
			// 1. 검색필터추가 :: 관리자 접근 권한 구분 (ALL/GROUP/TEAM)
			if(!param.get("part").toString().equals(Constant.MENU_ALL)) {				
				authGroupList = commonService.authGroupList(param.get("part").toString(), sessionVO);
				param.put("authGroupList",authGroupList);
			}else {
				param.put("authGroupList","");
			}
				
			// 1.1 엑셀다운로드시 한글검색어 인코딩 처리
			if(oper.equals(Constant.EXCEL_FORMAT)) {				
				param.put("nPage",1);			
				param.put("page_size",ConfigData.getInt("EXCEL_MAX_LIMIT"));		// 엑셀 저장 row수					
				param.put("strKeyword",map.get("strKeyword") != null ? new String(map.get("strKeyword").toString().getBytes("8859_1"),"utf-8") : "");	
			}
						
			// 2. 사용자/문서함별 소유 현황 || 문서함/폴더별 보유 현황 목록 가져오기
			if(param.get("workType").toString().equals(Constant.WORK_TYPE_FOLDER))	{
				//문서함/폴더별 보유 현황 목록 가져오기 :: 문서함/폴더별 보유 현황 합계 없음 
				resultMap = statisticsService.folderStatisticsList(param);
			}else {
				// 사용자/문서함별 소유 현황
				resultMap = statisticsService.userFolderStatisticsList(param);
			}

			// Excel Down 처리 :: 목록
			if(oper.equals(Constant.EXCEL_FORMAT)) {
				
				CommonUtil.getExcelList(resultMap,model);
				
				if(param.get("workType").toString().equals(Constant.WORK_TYPE_FOLDER))	{
					members = new String[]{ "group_nm", "doc_cnt", "page_cnt","fsize"};
					cell_headers = new String[]{ "구분", "문서수", "파일수", "용량"};
					cell_widths = new int[]{ 30,15,15,15};
				}else {
					members = new String[]{ "user_nm", "owner_id", "group_nm","map_nm","doc_cnt","page_cnt","page_total"};
					cell_headers = new String[]{ "사용자명", "사용자ID", "부서명", "문서함","문서수","파일수","용량" };
					cell_widths = new int[]{ 30,30,20,15,15,15,15};
	        		model.addAttribute("groupField",Constant.USER_DOC_STATISTICS);
				}
						
        		model.addAttribute("members",members);
        		model.addAttribute("cell_headers",cell_headers);
        		model.addAttribute("cell_widths",cell_widths);
        		model.addAttribute("fileName","downLoad.xls");

        		if(param.get("workType").toString().equals(Constant.WORK_TYPE_FOLDER))	{
        			return new ModelAndView(new ExcelCntView());        			
        		}else {
        			return new ModelAndView(new ExcelSumView());	
        		}
							
			}
			
			// CHART 정보가져오기 :: 문서함/폴더별 보유 현황만 적용
			if(isChart.equals(Constant.CHART))	{
				
				HashMap<String,Object> chartMap = new HashMap<String,Object>();
				
				if(Integer.parseInt(resultMap.get("records").toString()) == 0)	{
					resultMap.put("result",Constant.RESULT_FALSE);
					resultMap.put("message",messageSource.getMessage("common.data.exist.error",new Object[0],locale));			
				}else {
									
					CommonUtil.getChartList(resultMap,chartMap);
					
					// Chart 필수값 입력체크
					String chartType = map.get("chartType") != null ? map.get("chartType").toString() : "";	
					String colType = map.get("colType") != null ? map.get("colType").toString() : "";
					String yTitle = map.get("yTitle") != null ? map.get("yTitle").toString() : "";
					String xTitle = map.get("xTitle") != null ? map.get("xTitle").toString() : "";
					
					if(chartType.equals("") || colType.equals("") || yTitle.equals("")) {
						resultMap.put("result",Constant.RESULT_FALSE);
						resultMap.put("message",messageSource.getMessage("common.required.error",new Object[0],locale));			
					}else {
						
						// Chat 객체
						members = new String[]{ "group_nm", "doc_cnt", "page_cnt","page_total"};
						chartMap.put("members",members);
						chartMap.put("width",650);
		        		chartMap.put("height",400);        		
		        		chartMap.put("mTitle","문서함/폴더별 보유 현황");
		        		chartMap.put("xTitle",xTitle);        		        		
		        		chartMap.put("groupField","group_nm");        		
		        		chartMap.put("tooltips",true);
		        		chartMap.put("yTitle",yTitle);
		        		chartMap.put("colType",colType);	        		
		        		chartMap.put("statisticsType",Constant.CHART_FOLDER_STATUS);	
		        		chartMap.put("chartType",chartType);
		        		
		        		// Chart.js 데이터 생성 
						resultMap.put("chartData",ChartUtil.getChartDataSet(chartMap));
						resultMap.put("chartType",chartType);	        		
	        		
		        		resultMap.put("result",Constant.RESULT_TRUE);       
					}
				
				}
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
	 * 
	 * <pre>
	 * 1. 개용 : 문서유형별 보유 현황
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : typeStatisticsList
	 * @param model
	 * @param sessionVO
	 * @param map
	 * @param request
	 * @return ModelAndView
	 */
	@RequestMapping(value="/typeStatisticsList.do")
	@ResponseBody 
	public ModelAndView typeStatisticsList(Model model, @ModelAttribute SessionVO sessionVO, @RequestParam HashMap<String,Object> map,
			HttpServletRequest request) {
		
		Locale locale = LocaleLibrary.setLocale(sessionVO.getSessLanguage() != null  ? sessionVO.getSessLanguage() : ConfigData.getString("LANGUAGE"));
		
		List<String> authGroupList = new ArrayList<String>();
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		HashMap<String, Object> param = new HashMap<String, Object>();
		
		// 입력 파라미터 유효성 체크						
		param.put("sdate",map.get("sdate") != null ? map.get("sdate").toString() : "");
		param.put("edate",map.get("edate") != null ? map.get("edate").toString() : "");		
		param.put("strKeyword",map.get("strKeyword") != null ? map.get("strKeyword").toString() : "");				
		param.put("strIndex",map.get("strIndex") != null ? map.get("strIndex").toString() : Constant.IS_USER); 
			
		// 권한 : ALL/GROUP/TEAM
		param.put("part",map.get("part") != null ? map.get("part").toString() : Constant.MENU_TEAM);
				
		String oper = map.get("oper") != null ? map.get("oper").toString() : "";					// Excel Down 처리  : 파리미터=oper				
		String isChart = map.get("isChart") != null ? map.get("isChart").toString() : "";		// Chart
			
		String[]  members ;
		String[]  cell_headers ;
		int[] cell_widths;
		
		try {
			
			// 1. 검색필터추가 :: 관리자 접근 권한 구분 (ALL/GROUP/TEAM)
			if(!param.get("part").toString().equals(Constant.MENU_ALL)) {				
				authGroupList = commonService.authGroupList(param.get("part").toString(), sessionVO);
				param.put("authGroupList",authGroupList);
			}else {
				param.put("authGroupList","");
			}
				
			// 1.1 엑셀다운로드시 한글검색어 인코딩 처리
			if(oper.equals(Constant.EXCEL_FORMAT)) {				
				param.put("strKeyword",map.get("strKeyword") != null ? new String(map.get("strKeyword").toString().getBytes("8859_1"),"utf-8") : "");	
			}
			
			// 2. 문서유형별 보유 현황 리스트 
			resultMap =  statisticsService.statisticsTypeList(param);
			
			// Excel Down 처리 :: 목록
			if(oper.equals(Constant.EXCEL_FORMAT)) {
				
				CommonUtil.getExcelList(resultMap,model);
				
				members = new String[]{ "type_name","map_nm","doc_cnt", "page_cnt","page_total"};
				cell_headers = new String[]{ "문서유형","문서함", "문서수", "파일수", "용량"};
				cell_widths = new int[]{ 30,20,15,15,15};
						
        		model.addAttribute("members",members);
        		model.addAttribute("cell_headers",cell_headers);
        		model.addAttribute("cell_widths",cell_widths);
        		model.addAttribute("fileName","downLoad.xls");
        		model.addAttribute("groupField","type_name");
        		
        		return new ModelAndView(new ExcelSumView());	
			}
			
			// CHART 정보가져오기
			if(isChart.equals(Constant.CHART))	{
				
				HashMap<String,Object> chartMap = new HashMap<String,Object>();
				
				if(Integer.parseInt(resultMap.get("records").toString()) == 0)	{
					resultMap.put("result",Constant.RESULT_FALSE);
					resultMap.put("message",messageSource.getMessage("common.data.exist.error",new Object[0],locale));			
				}else {
				
					CommonUtil.getChartList(resultMap,chartMap);
					
					// Chart 필수값 입력체크
					String chartType = map.get("chartType") != null ? map.get("chartType").toString() : "";	
					String colType = map.get("colType") != null ? map.get("colType").toString() : "";
					String yTitle = map.get("yTitle") != null ? map.get("yTitle").toString() : "";
					
					if(chartType.equals("") || colType.equals("") || yTitle.equals("")) {
						resultMap.put("result",Constant.RESULT_FALSE);
						resultMap.put("message",messageSource.getMessage("common.required.error",new Object[0],locale));			
					}else {
							
						members = new String[]{ "type_name","map_nm","doc_cnt", "page_cnt","page_total"};						
						chartMap.put("members",members);
		        		
		        		// Chat 객체        		
		        		chartMap.put("width",650);
		        		chartMap.put("height",400);        		
		        		chartMap.put("mTitle","문서유형별 보유 현황");
		        		chartMap.put("xTitle","문서유형");        		        		
		        		chartMap.put("groupField","type_name");        		
		        		chartMap.put("tooltips",true);
		        		chartMap.put("statisticsType",Constant.CHART_DOC_TYPE);
		        		chartMap.put("yTitle",yTitle);
		        		chartMap.put("colType",colType);
		        		chartMap.put("chartType",chartType);
		        		
		        		// Chart.js 데이터 생성
						resultMap.put("chartData",ChartUtil.getChartDataSet(chartMap));
						resultMap.put("chartType",chartType);		
						        		
		        		resultMap.put("result",Constant.RESULT_TRUE);        	
					}
				}
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
	 * 
	 * <pre>
	 * 1. 개용 : 보안등급별 보유 현황
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : securityStatisticsList
	 * @param model
	 * @param sessionVO
	 * @param map
	 * @param request
	 * @return ModelAndView
	 */
	@RequestMapping(value="/securityStatisticsList.do")
	@ResponseBody 
	public ModelAndView securityStatisticsList(Model model, @ModelAttribute SessionVO sessionVO, @RequestParam HashMap<String,Object> map,
			HttpServletRequest request) {
		
		Locale locale = LocaleLibrary.setLocale(sessionVO.getSessLanguage() != null  ? sessionVO.getSessLanguage() : ConfigData.getString("LANGUAGE"));
		
		List<String> authGroupList = new ArrayList<String>();
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		HashMap<String, Object> param = new HashMap<String, Object>();
		
		// 입력 파라미터 유효성 체크					
		param.put("contextRoot",sessionVO.getSessContextRoot());
		param.put("sdate",map.get("sdate") != null ? map.get("sdate").toString() : "");
		param.put("edate",map.get("edate") != null ? map.get("edate").toString() : "");		
		param.put("strKeyword",map.get("strKeyword") != null ? map.get("strKeyword").toString() : "");				
		param.put("strIndex",map.get("strIndex") != null ? map.get("strIndex").toString() : Constant.IS_USER); 
		param.put("orderCol",map.get("sidx") != null ? map.get("sidx").toString() : "code_nm");
		param.put("orderType",map.get("sord") != null ? map.get("sord") : "ASC");			
		param.put("page_size",map.get("rows") != null ? map.get("rows") : sessionVO.getSessPage_size());					
		param.put("part",map.get("part") != null ? map.get("part").toString() : Constant.MENU_TEAM);			// 권한 : ALL/GROUP/TEAM
		param.put("nPage",CommonUtil.getPage(map));		// page 설정
		
		String oper = map.get("oper") != null ? map.get("oper").toString() : "";							// Excel Down 처리  : 파리미터=oper
		String isChart = map.get("isChart") != null ? map.get("isChart").toString() : "";				// Chart
		
		String[]  members ;
		String[]  cell_headers ;
		int[] cell_widths;
		
		try {
			
			// 1. 검색필터추가 :: 관리자 접근 권한 구분 (ALL/GROUP/TEAM)
			if(!param.get("part").toString().equals(Constant.MENU_ALL)) {				
				authGroupList = commonService.authGroupList(param.get("part").toString(), sessionVO);
				param.put("authGroupList",authGroupList);
			}else {
				param.put("authGroupList","");
			}
				
			// 1.1 엑셀다운로드시 한글검색어 인코딩 처리
			if(oper.equals(Constant.EXCEL_FORMAT)) {				
				param.put("nPage",1);			
				param.put("page_size",ConfigData.getInt("EXCEL_MAX_LIMIT"));		// 엑셀 저장 row수				
				param.put("strKeyword",map.get("strKeyword") != null ? new String(map.get("strKeyword").toString().getBytes("8859_1"),"utf-8") : "");	
			}
			
			// 2. 보안등급별 보유 현황 리스트 
			resultMap =  statisticsService.statisticsSecurityList(param);
			
			// Excel Down 처리 :: 목록
			if(oper.equals(Constant.EXCEL_FORMAT)) {
				
				CommonUtil.getExcelList(resultMap,model);
				
				members = new String[]{ "code_nm","user_nm","doc_cnt", "page_cnt","page_total"};
				cell_headers = new String[]{ "보안등급","사용자", "문서수", "파일수", "용량"};
				cell_widths = new int[]{ 30,20,15,15,15};
						
        		model.addAttribute("members",members);
        		model.addAttribute("cell_headers",cell_headers);
        		model.addAttribute("cell_widths",cell_widths);
        		model.addAttribute("fileName","downLoad.xls");
        		model.addAttribute("groupField","code_nm");
        		
        		return new ModelAndView(new ExcelSumView());	
			}

			// CHART 처리
			if(isChart.equals(Constant.CHART))	{
				
				HashMap<String,Object> chartMap = new HashMap<String,Object>();
				
				if(Integer.parseInt(resultMap.get("records").toString()) == 0)	{
					resultMap.put("result",Constant.RESULT_FALSE);
					resultMap.put("message",messageSource.getMessage("common.data.exist.error",new Object[0],locale));			
				}else {
					
					CommonUtil.getChartList(resultMap,chartMap);
					
					// Chart 필수값 입력체크
					String chartType = map.get("chartType") != null ? map.get("chartType").toString() : "";	
					String colType = map.get("colType") != null ? map.get("colType").toString() : "";
					String yTitle = map.get("yTitle") != null ? map.get("yTitle").toString() : "";
					
					if(chartType.equals("") || colType.equals("") || yTitle.equals("")) {
						resultMap.put("result",Constant.RESULT_FALSE);
						resultMap.put("message",messageSource.getMessage("common.required.error",new Object[0],locale));			
					}else {
							
						members =  new String[]{ "code_nm","user_nm","doc_cnt", "page_cnt","page_total"};	
						
						// Chat 객체  
						chartMap.put("members",members);
						chartMap.put("width",650);
		        		chartMap.put("height",400);        		
		        		chartMap.put("mTitle","보안등급별 보유현황");
		        		chartMap.put("xTitle","보안등급");   
		        		chartMap.put("groupField","code_nm");        		
		        		chartMap.put("tooltips",true);
		        		chartMap.put("statisticsType",Constant.CHART_SECURITY_STATUS);
		        		chartMap.put("yTitle",yTitle);
		        		chartMap.put("colType",colType);	
		        		chartMap.put("chartType",chartType);
		        		
		        		// Chart.js 데이터 생성 
						resultMap.put("chartData",ChartUtil.getChartDataSet(chartMap));
						resultMap.put("chartType",chartType);		
		        		
		        		resultMap.put("result",Constant.RESULT_TRUE);        	
					}
				}
				
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
	 * 
	 * <pre>
	 * 1. 개용 : 문서 Quota 현황 리스트 가져오기
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : quotaStatisticsList
	 * @param model
	 * @param sessionVO
	 * @param map
	 * @param request
	 * @return ModelAndView
	 */
	@RequestMapping(value="/quotaStatisticsList.do")
	@ResponseBody 
	public ModelAndView quotaStatisticsList(Model model, @ModelAttribute SessionVO sessionVO, @RequestParam HashMap<String,Object> map,
			HttpServletRequest request) {
		
		Locale locale = LocaleLibrary.setLocale(sessionVO.getSessLanguage() != null  ? sessionVO.getSessLanguage() : ConfigData.getString("LANGUAGE"));
		
		List<String> authGroupList = new ArrayList<String>();
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		HashMap<String, Object> param = new HashMap<String, Object>();
		
		/// 입력 파라미터 유효성 체크
		param.put("contextRoot",sessionVO.getSessContextRoot());
		param.put("page_size",map.get("rows") != null ? map.get("rows") : sessionVO.getSessPage_size());					
		param.put("sdate",map.get("sdate") != null ? map.get("sdate").toString() : "");
		param.put("edate",map.get("edate") != null ? map.get("edate").toString() : "");		
		param.put("strIndex",map.get("strIndex") != null ? map.get("strIndex").toString() : Constant.IS_USER); 
		param.put("nPage",CommonUtil.getPage(map));		
		param.put("orderType",map.get("sord") != null ? map.get("sord") : "ASC");
		param.put("part",map.get("part") != null ? map.get("part").toString() : Constant.MENU_TEAM);			// 권한 : ALL/GROUP/TEAM
		param.put("work_type",Constant.WORK_TYPE_FOLDER);
		
		// 개인,부서에 따라서 정렬컬럼이 다름
		if(param.get("strIndex").toString().equals(Constant.IS_USER))	{
			param.put("orderCol","U.USER_NAME_KO");			
		}else {
			param.put("orderCol","G.GROUP_NAME_KO");		
		}
		
		String oper = map.get("oper") != null ? map.get("oper").toString() : "";				// Excel Down 처리  : 파리미터=oper		
		String[]  members ;
		String[]  cell_headers ;
		int[] cell_widths;
		
		try {
			
			// 1. 검색필터추가 :: 관리자 접근 권한 구분 (ALL/GROUP/TEAM)
			if(!param.get("part").toString().equals(Constant.MENU_ALL)) {				
				authGroupList = commonService.authGroupList(param.get("part").toString(), sessionVO);
				param.put("authGroupList",authGroupList);
			}else {
				param.put("authGroupList","");
			}
			
			// 1.1 엑셀다운로드 처리
			if(oper.equals(Constant.EXCEL_FORMAT)) {
				param.put("nPage",1);			
				param.put("page_size",ConfigData.getInt("EXCEL_MAX_LIMIT"));		// 엑셀 저장 row수		
				param.put("strKeyword",map.get("strKeyword") != null ? new String(map.get("strKeyword").toString().getBytes("8859_1"),"utf-8") : "");	
			}
			

			// 2. 문서 Quota 현황 가져오기
			resultMap = statisticsService.statisticsQuotaList(param);
			
			// Excel Down 처리 :: 목록
			if(oper.equals(Constant.EXCEL_FORMAT)) {
				
				CommonUtil.getExcelList(resultMap,model);
				
				members = new String[]{ "part_nm", "ssize", "fsize"};
				cell_headers = new String[]{ "구분", "할당량", "현사용량"};
				cell_widths = new int[]{ 30,15,15};
						
        		model.addAttribute("members",members);
        		model.addAttribute("cell_headers",cell_headers);
        		model.addAttribute("cell_widths",cell_widths);
        		model.addAttribute("fileName","downLoad.xls");

        		return new ModelAndView(new ExcelCntView());        									
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
	 * 
	 * <pre>
	 * 1. 개용 : 로그인 이력 리스트 
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : loginLogList
	 * @param model
	 * @param sessionVO
	 * @param map
	 * @param request
	 * @return ModelAndView
	 */
	@RequestMapping(value="/loginLogList.do")
	@ResponseBody 
	public ModelAndView loginLogList(Model model, @ModelAttribute SessionVO sessionVO, @RequestParam HashMap<String,Object> map,
			HttpServletRequest request) {
		
		Locale locale = LocaleLibrary.setLocale(sessionVO.getSessLanguage() != null  ? sessionVO.getSessLanguage() : ConfigData.getString("LANGUAGE"));
		
		List<String> authGroupList = new ArrayList<String>();
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		HashMap<String, Object> param = new HashMap<String, Object>();
		
		// 입력 파라미터 유효성 체크		
		param.put("contextRoot",sessionVO.getSessContextRoot());
		param.put("page_size",map.get("rows") != null ? map.get("rows") : sessionVO.getSessPage_size());					
		param.put("sdate",map.get("sdate") != null ? map.get("sdate").toString() : "");
		param.put("edate",map.get("edate") != null ? map.get("edate").toString() : "");		
		param.put("strIndex",map.get("strIndex") != null ? map.get("strIndex").toString() : Constant.IS_USER); 
		param.put("nPage",CommonUtil.getPage(map));		
		param.put("orderCol",map.get("sidx") != null ? map.get("sidx").toString() : "connect_time");
		param.put("orderType",map.get("sord") != null ? map.get("sord") : "ASC");
		param.put("part",map.get("part") != null ? map.get("part").toString() : Constant.MENU_TEAM);				// 권한 : ALL/GROUP/TEAM
		param.put("strKeyword",map.get("strKeyword") != null ? map.get("strKeyword").toString() : "");				
		param.put("strIndex",map.get("strIndex") != null ? map.get("strIndex").toString() : Constant.IS_USER); 	
		
		// 검색조건 재정의 
		if(param.get("orderCol").toString().equals("code_nm"))	{
			param.put("orderCol","C."+param.get("orderCol").toString());
		}else {
			param.put("orderCol","L."+param.get("orderCol").toString());
		}
		
		// Excel Down 처리  : 파리미터=oper		
		String oper = map.get("oper") != null ? map.get("oper").toString() : "";
		String[]  members ;
		String[]  cell_headers ;
		int[] cell_widths;
		
		try {
			
			// 1. 검색필터추가 :: 관리자 접근 권한 구분 (ALL/GROUP/TEAM)
			if(!param.get("part").toString().equals(Constant.MENU_ALL)) {				
				authGroupList = commonService.authGroupList(param.get("part").toString(), sessionVO);
				param.put("authGroupList",authGroupList);
			}else {
				param.put("authGroupList","");
			}
				
			// 1.1 엑셀다운로드시 한글검색어 인코딩 처리
			if(oper.equals(Constant.EXCEL_FORMAT)) {
				param.put("nPage",1);			
				param.put("page_size",ConfigData.getInt("EXCEL_MAX_LIMIT"));		// 엑셀 저장 row수					
				param.put("strKeyword",map.get("strKeyword") != null ? new String(map.get("strKeyword").toString().getBytes("8859_1"),"utf-8") : "");	
			}

			// 2. 로그인 이력 리스트 가져오기 
			resultMap = statisticsService.loginLogList(param);
			
			// Excel Down 처리 :: 목록
			if(oper.equals(Constant.EXCEL_FORMAT)) {
				
				CommonUtil.getExcelList(resultMap,model);
				
				members = new String[]{ "connect_time", "user_nm", "user_id","group_nm","connect_ip","cert_nm"};
				cell_headers = new String[]{ "접속일자", "사용자명", "사용자ID","부서명","IP주소","인증여부"};
				cell_widths = new int[]{ 30,20,20,20,15,15,15};
						
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
		
		ModelAndView mav = new ModelAndView("jsonView",resultMap);
		return mav;
		
	}

	/**
	 * 
	 * <pre>
	 * 1. 개용 : 내문서현황 리스트 
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : myStatisticsList
	 * @param model
	 * @param sessionVO
	 * @param map
	 * @param request
	 * @return ModelAndView
	 */
	@RequestMapping(value="/myStatisticsList.do")
	@ResponseBody 
	public ModelAndView myStatisticsList(Model model, @ModelAttribute SessionVO sessionVO, @RequestParam HashMap<String,Object> map,
			HttpServletRequest request) {
		
		Locale locale = LocaleLibrary.setLocale(sessionVO.getSessLanguage() != null  ? sessionVO.getSessLanguage() : ConfigData.getString("LANGUAGE"));
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		HashMap<String, Object> param = new HashMap<String, Object>();
		
		// 입력 파라미터 유효성 체크					
		param.put("sdate",map.get("sdate") != null ? map.get("sdate").toString() : "");
		param.put("edate",map.get("edate") != null ? map.get("edate").toString() : "");		
		param.put("orderCol",map.get("sidx") != null ? map.get("sidx").toString() : "type_id");
		param.put("orderType",map.get("sord") != null ? map.get("sord") : "ASC");
		
		// 검색조건 재정의 
		if(param.get("orderCol").toString().equals("type_id"))	{
			param.put("orderCol","H."+param.get("orderCol").toString());
		}
		
		param.put("user_id", sessionVO.getSessId());
		
		// Excel Down 처리  : 파리미터=oper		
		String oper = map.get("oper") != null ? map.get("oper").toString() : "";
		String[]  members ;
		String[]  cell_headers ;
		int[] cell_widths;
				
		try {
			
				
			// 1.1 엑셀다운로드시 한글검색어 인코딩 처리
			if(oper.equals(Constant.EXCEL_FORMAT)) {
				param.put("nPage",1);			
				param.put("page_size",ConfigData.getInt("EXCEL_MAX_LIMIT"));		// 엑셀 저장 row수					
				//param.put("strKeyword",map.get("strKeyword") != null ? new String(map.get("strKeyword").toString().getBytes("8859_1"),"utf-8") : "");	
			}

			// 2.내문서현황 리스트 
			resultMap = statisticsService.myStatisticsList(param);

			// Excel Down 처리 :: 목록
			if(oper.equals(Constant.EXCEL_FORMAT)) {
				
				CommonUtil.getExcelList(resultMap,model);
				
				// TO DO
				
				members = new String[]{ "type_name", "create_cnt", "read_cnt","update_cnt","delete_cnt"};
				cell_headers = new String[]{ "문서유형",  "등록","조회","수정","삭제"};
				cell_widths = new int[]{ 30,15,15,15,15};
				
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
		
		ModelAndView mav = new ModelAndView("jsonView",resultMap);
		return mav;
		
	}
}

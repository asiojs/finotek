package kr.co.exsoft.permission.controller;

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
import kr.co.exsoft.common.vo.SessionVO;
import kr.co.exsoft.eframework.configuration.Constant;
import kr.co.exsoft.eframework.exception.BizException;
import kr.co.exsoft.eframework.library.LocaleLibrary;
import kr.co.exsoft.eframework.util.CommonUtil;
import kr.co.exsoft.eframework.util.ConfigData;
import kr.co.exsoft.eframework.util.StringUtil;
import kr.co.exsoft.permission.service.AclService;
import kr.co.exsoft.permission.vo.AclExItemVO;
import kr.co.exsoft.permission.vo.AclItemListVO;
import kr.co.exsoft.permission.vo.AclItemVO;
import kr.co.exsoft.permission.vo.AclVO;

/**
 * Acl Controller
 * @author 패키지 개발팀
 * @since 2014.07.17
 * @version 3.0
 *
 */
@Controller
@SessionAttributes("sessionVO")
@RequestMapping("/permission")
public class AclAuthController {
	
	@Autowired
	private AclService aclService;
	
	@Autowired
	private CommonService commonService;
	
	@Autowired
	private MessageSource messageSource;
	
    @Autowired
    private DefaultBeanValidator beanValidator;
    
	protected static final Log logger = LogFactory.getLog(AclAuthController.class);
	
	@RequestMapping(value="/aclControl.do", method=RequestMethod.POST)
	@ResponseBody 
	public Map<String,Object> aclControl(Model model, @ModelAttribute SessionVO sessionVO, @RequestParam HashMap<String,Object> map,
			@ModelAttribute("aclVO") AclVO aclVO,BindingResult bindingResult,HttpServletRequest request) {
		
		Locale locale = LocaleLibrary.setLocale(sessionVO.getSessLanguage() != null  ? sessionVO.getSessLanguage() : ConfigData.getString("LANGUAGE"));
		
		List<AclItemVO> aclItemList = new ArrayList<AclItemVO>();
		List<String> delList = new ArrayList<String>();
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		// 업무구분자 - 문서유형 등록/수정/삭제
		String type = map.get(Constant.TYPE) != null ? map.get(Constant.TYPE).toString() : "";
		String target_acl_id = "";
		
		try {
		
			// 등록||수정 TypeVO 객체 유효성 체크
			if(type.equals(Constant.INSERT) || type.equals(Constant.UPDATE) || type.equals(Constant.COPY) )	{
				
				beanValidator.validate(aclVO,bindingResult);
				
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
					
					String aclItemListArrayList =  map.get("aclItemArrayList") != null ? map.get("aclItemArrayList").toString() : "";
					
					if(type.equals(Constant.INSERT))	{
						
						// AclItem 속성 
						int acl_id = commonService.commonNextVal(Constant.COUNTER_ID_ACL);
						aclVO.setAcl_id(CommonUtil.getStringID(Constant.ID_PREFIX_ACL, acl_id));
						aclVO.setCreator_id(sessionVO.getSessId());						
						aclItemList = CommonUtil.getAclItemVOFromAclItemListVO(aclItemListArrayList, aclVO.getAcl_id());
					
						// 문서유형 & 문서속성 & 문서속성 데이터베이스 처리 및 문서유형 테이블 수정처리
						resultMap = aclService.aclWrite(map, aclVO, aclItemList, sessionVO);
						target_acl_id = (String)resultMap.get("acl_id");
						
					}else if(type.equals(Constant.UPDATE))	{

						// AclItem 속성 
						aclItemList = CommonUtil.getAclItemVOFromAclItemListVO(aclItemListArrayList, aclVO.getAcl_id());
						target_acl_id = aclVO.getAcl_id();
						
						// 문서유형 & 문서속성 & 문서속성 데이터베이스 처리 및 문서유형 테이블 수정처리
						resultMap = aclService.aclUpdate(map, aclVO, aclItemList, sessionVO);
					}
					
				}
				
			}else if(type.equals(Constant.DELETE))	{
				// 삭제 권한 목록 유효성 체크
				delList = aclService.aclDeleteValid(map);
				
				// 권한 삭제처리
				resultMap = aclService.aclDelete(map, delList, sessionVO);
			}
			
			// XR_HISTORY 등록처리
			String result =  resultMap.get("result") != null ? resultMap.get("result").toString() : "";
			if(!StringUtil.isEmpty(type) && result.equals(Constant.RESULT_TRUE)){
				long history_seq = 0;
				HistoryVO historyVO = new HistoryVO();
				
				if(type.equals(Constant.DELETE)) {
					for(String del_acl_id : delList) {
						history_seq = commonService.commonNextVal(Constant.COUNTER_ID_HISTORY);
						historyVO = CommonUtil.setHistoryVO(history_seq, del_acl_id, type,  Constant.TARGET_ACL, sessionVO);
						commonService.historyWrite(historyVO);
					}
				} else {
					history_seq = commonService.commonNextVal(Constant.COUNTER_ID_HISTORY);
					historyVO = CommonUtil.setHistoryVO(history_seq, target_acl_id, type,  Constant.TARGET_ACL, sessionVO);
					commonService.historyWrite(historyVO);
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
	 * 1. 개용 : 관리자 권한 관리 권한 목록
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : aclList
	 * @param model
	 * @param sessionVO
	 * @param map
	 * @param request
	 * @return Map<String,Object>
	 */
	@RequestMapping(value="/aclList.do", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> aclList(Model model, @ModelAttribute SessionVO sessionVO, @RequestParam HashMap<String, Object> map,
			HttpServletRequest request){
		
		Locale locale = LocaleLibrary.setLocale(sessionVO.getSessLanguage() != null  ? sessionVO.getSessLanguage() : ConfigData.getString("LANGUAGE"));
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		HashMap<String, Object> param = new HashMap<String, Object>();
		
		//입력 파라미터 유효성 체크
		param.put("strIndex",map.get("strIndex") != null ? map.get("strIndex") : "" );
		param.put("strKeyword1",map.get("strKeyword1") != null ? map.get("strKeyword1") : "" );
		param.put("strKeyword2",map.get("strKeyword2") != null ? map.get("strKeyword2") : "" );
		param.put("orderCol",map.get("sidx") != null ? map.get("sidx") : "work_type");				
		param.put("orderType",map.get("sord") != null ? map.get("sord") : "ASC");
		param.put("page_size",map.get("rows") != null ? map.get("rows") : sessionVO.getSessPage_size());		
		param.put("nPage",CommonUtil.getPage(map));
		
		// acl_id 설정 :: 공개범위에 해당하는 권한 목록 가져 오기
		// 세션 개책를 List객체에 사용할 경우 call by reference인 관계로 add value 지속적으로 늘어남
		// 별도의 array 객체에 담아서 처림
		String[] dept_id_list = sessionVO.getSessParentGroup().toArray(new String[sessionVO.getSessParentGroup().size()+1]);
		dept_id_list[sessionVO.getSessParentGroup().size()] = sessionVO.getSessGroup_id();  // 배열 시작은 0부터임
		
		String[] team_id_list = sessionVO.getSessProjectGroup().toArray(new String[sessionVO.getSessProjectGroup().size()+1]);
		team_id_list[sessionVO.getSessProjectGroup().size()] = sessionVO.getSessGroup_id();
		
		param.put("type",map.get("type") != null ? map.get("type") : "" );
		param.put("dept_id_list", dept_id_list);
		param.put("team_id_list", team_id_list);
		param.put("user_id", sessionVO.getSessId());
		
		if(sessionVO.getSessRole_id().equals(Constant.SYSTEM_ROLE))
			param.put("type", Constant.SYSTEM_ROLE);
		
		try {
			
			//권한 목록을 가져 온다.
			resultMap = aclService.aclList(param);
			
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
	 * 1. 개용 : 권한속성 리스트 가져오기
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : aclItemList
	 * @param model
	 * @param sessionVO
	 * @param map
	 * @param request
	 * @return Map<String,Object>
	 */
	@RequestMapping(value="/aclItemList.do", method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> aclItemList(Model model, @ModelAttribute SessionVO sessionVO, @RequestParam HashMap<String, Object> map,
			HttpServletRequest request) {
		
		Locale locale = LocaleLibrary.setLocale(sessionVO.getSessLanguage() != null  ? sessionVO.getSessLanguage() : ConfigData.getString("LANGUAGE"));
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> resultAclVoMap = new HashMap<String, Object>();
		HashMap<String,Object> param = new  HashMap<String, Object>();
		List<AclItemListVO> listAclItemListVO = new ArrayList<AclItemListVO>();
		
		String acl_id = map.get("acl_id") != null ? (String)map.get("acl_id") : "";
		
		try {
			 		
			param.put("acl_id", acl_id);
			
			//ACL 명칭 얻기
			resultAclVoMap = aclService.aclDetail(param); 			// acl_id에 대한 AclVO를 얻는다. (aclDetail);
			listAclItemListVO = aclService.aclItemList(param);
			
			resultMap.put("aclDetail", resultAclVoMap.get("aclDetail"));
			resultMap.put("list",listAclItemListVO);
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
	 * 1. 개용 : 문서 또늘 폴더 권한변경 작업 시 상위권한 정보를 가져오기
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : aclInheritDetail
	 * @param model
	 * @param sessionVO
	 * @param map
	 * @param request
	 * @return Map<String,Object>
	 */
	@RequestMapping(value="/aclInheritDetail.do", method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> aclInheritDetail(Model model, @ModelAttribute SessionVO sessionVO, @RequestParam HashMap<String, Object> map,
			HttpServletRequest request) {
		
		Locale locale = LocaleLibrary.setLocale(sessionVO.getSessLanguage() != null  ? sessionVO.getSessLanguage() : ConfigData.getString("LANGUAGE"));
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		try {
			
			resultMap = aclService.aclInheritDetail(map);
			
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
	 * 1. 개용 : 문서 추가 접근자 목록 가져오기 
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : exAclItemList
	 * @param model
	 * @param sessionVO
	 * @param map
	 * @param request
	 * @return Map<String,Object>
	 */
	@RequestMapping(value="/exAclItemList.do", method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> exAclItemList(Model model, @ModelAttribute SessionVO sessionVO, @RequestParam HashMap<String, Object> map,
			HttpServletRequest request) {
		
		Locale locale = LocaleLibrary.setLocale(sessionVO.getSessLanguage() != null  ? sessionVO.getSessLanguage() : ConfigData.getString("LANGUAGE"));
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		HashMap<String,Object> param = new  HashMap<String, Object>();
		List<AclExItemVO> listAclExItemListVO = new ArrayList<AclExItemVO>();
		
		String doc_id = map.get("doc_id") != null ? (String)map.get("doc_id") : "";
		
		try {
			
			param.put("doc_id", doc_id);
			
			// 문서 추가 접근자 목록 얻기
			listAclExItemListVO = aclService.exAclItemList(param);
			
			resultMap.put("list",listAclExItemListVO);
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
	
}

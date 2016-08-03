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
import kr.co.exsoft.common.vo.SessionVO;
import kr.co.exsoft.eframework.configuration.Constant;
import kr.co.exsoft.eframework.exception.BizException;
import kr.co.exsoft.eframework.library.LocaleLibrary;
import kr.co.exsoft.eframework.util.CommonUtil;
import kr.co.exsoft.eframework.util.ConfigData;
import kr.co.exsoft.eframework.util.UtilFileApp;
import kr.co.exsoft.user.service.GroupService;
import kr.co.exsoft.user.vo.GroupVO;
							
/**
 * Group Admin Controller
 * @author 패키지 개발팀
 * @since 2014.07.17
 * @version 3.0
 *
 */
@Controller
@SessionAttributes("sessionVO")
@RequestMapping("/admin")
public class GroupAdminController {

	@Autowired
	private GroupService groupService;
	
	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	private CommonService commonService;
	
    @Autowired
    private DefaultBeanValidator beanValidator;
    
	protected static final Log logger = LogFactory.getLog(GroupAdminController.class);


	/**
	 * 
	 * <pre>
	 * 1. 개요 : 그룹관리(부서/프로젝트)
	 * 2. 처리내용 :
	 * </pre>
	 * @Method Name : groupAdmin
	 * @param sessionVO
	 * @param model
	 * @param map
	 * @return
	 */
	@RequestMapping("/groupManager.do")
	public String groupManager(@ModelAttribute SessionVO sessionVO,Model model,@RequestParam HashMap<String,Object> map) {
		
		@SuppressWarnings("unused")
		Locale locale = LocaleLibrary.setLocale(sessionVO.getSessLanguage() != null  ? sessionVO.getSessLanguage() : ConfigData.getString("LANGUAGE"));
		
		HashMap<String,Object> param = new HashMap<String,Object>();
		Map<String, Object> menuInfo = new HashMap<String, Object>();
		
		param.put("menu_cd",map.get("menu_cd") != null ?  map.get("menu_cd").toString() : "" );
		param.put("role_id",sessionVO.getSessRole_id());
		
		try {
			
			//  페이지 네비게이션 :: 상위메뉴명 / 현재메뉴명
			menuInfo = commonService.pageMenuInfo(param);
			
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
		
		model.addAttribute("menuInfo",menuInfo);
		model.addAttribute("topSelect",Constant.TOPMENU_SYSTEM);
		model.addAttribute("subSelect",Constant.SYSTEM_GROUPMANAGER);
		
		return "sysadmin/groupManager";
	}
	
	/**
	 * 
	 * <pre>
	 * 1. 개요 : 부서 상세 조회
	 * 2. 처리내용 :
	 * </pre>
	 * @Method Name : groupDetail
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/groupDetail.do", method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> groupDetail(Model model, HttpServletRequest request,@RequestParam HashMap<String,Object> map) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		try {
			
			String groupId = map.get("groupId") != null ? map.get("groupId").toString() : null;
			
			GroupVO groupVO = groupService.groupDetail(groupId);
			
			resultMap.put("groupDetail", groupVO);
			resultMap.put("result", Constant.RESULT_SUCCESS);
			
		} catch(BizException e){
			resultMap.put("result",Constant.RESULT_FALSE);
			resultMap.put("message",e.getMessage());				
		} catch (Exception e) {
			resultMap.put("result", Constant.RESULT_FAIL);
			resultMap.put("message", e.getMessage());
			e.printStackTrace();
		}
		return resultMap;
	}
	
	/**
	 * 
	 * <pre>
	 * 1. 개요 : 그룹 등록 / 수정 / 삭제 / 이동
	 * 2. 처리내용 :
	 * </pre>
	 * @Method Name : groupInfoManager
	 * @param model
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/groupInfoManager.do", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> groupInfoManager(Model model, @RequestParam HashMap<String, Object> map, @ModelAttribute SessionVO sessionVO) {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		String type = map.get(Constant.TYPE) != null ? map.get(Constant.TYPE).toString() : "";				// 작업 유형
		
		try {
			
			GroupVO groupVO = null;
			
			if (type.equals(Constant.INSERT)) {
				
				// 1. 유효성 검사
				groupVO = groupService.groupWriteValid(map);
				
				// 2. 그룹 등록
				resultMap = groupService.groupWrite(groupVO, sessionVO);
				
			} else if (type.equals(Constant.UPDATE)) {
				
				// 1. 유효성 검사
				groupVO = groupService.groupUpdateValid(map);
				
				// 2. 그룹 수정
				resultMap = groupService.groupUpdate(groupVO, sessionVO);
				
			} else if (type.equals(Constant.DELETE)) {
				
				// 1. 유효성 검사
				groupVO = groupService.groupDeleteValid(map);
				
				// 2. 그룹 삭제
				resultMap = groupService.groupDelete(groupVO, sessionVO);
				
			} else if (type.equals(Constant.MOVE)) {
				
				// 1. 유효성 검사
				groupVO = groupService.groupMoveValid(map);
				
				// 2. 그룹 이동
				resultMap = groupService.groupMove(groupVO, sessionVO);
			}
			
		} catch(BizException e){
			resultMap.put("result",Constant.RESULT_FALSE);
			resultMap.put("message",e.getMessage());				
		} catch (Exception e) {
			resultMap.put("result", Constant.RESULT_FAIL);
			resultMap.put("message", e.getMessage());			
		}
		
		return resultMap;
	}
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 그룹일괄 등록처리 
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
	@RequestMapping(value="/groupUpload.do", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> groupUpload(Model model,HttpServletRequest request,@ModelAttribute SessionVO sessionVO,
			@RequestParam Map<String, String> map,@RequestParam("Filedata") MultipartFile report) {
		
		Locale locale = LocaleLibrary.setLocale(sessionVO.getSessLanguage() != null  ? sessionVO.getSessLanguage() : ConfigData.getString("LANGUAGE"));
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		String tmpDir = map.get("uniqStr") != null  ?  map.get("uniqStr").toString() : UUID.randomUUID().toString();
		String targetPath = ConfigData.getString("FILE_UPLOAD_PATH") + tmpDir;			
		String targetFileName = "";		

		List<GroupVO> groupList =  new ArrayList<GroupVO>();
		
		try {
			
			UtilFileApp.createDir(targetPath);		
			targetFileName = targetPath+ "/"+UUID.randomUUID().toString() + ".xls";

			File file = new File(targetFileName);
			report.transferTo(file);
									
			if(file.exists())	{
			
				// Group 객체 생성 및 DB 유효성 체크 처리
				groupList = groupService.groupExcelList(targetFileName);	

				if(groupList != null && groupList.size() > 0) {
					
					for(GroupVO groupVO : groupList)	{						
						try {							
							groupService.groupWrite(groupVO, sessionVO);							
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

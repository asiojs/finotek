package kr.co.exsoft.folder.controller;

import java.util.Map;
import java.util.HashMap;
import java.util.Locale;

import org.springmodules.validation.commons.DefaultBeanValidator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import kr.co.exsoft.common.service.CommonService;
import kr.co.exsoft.common.vo.SessionVO;
import kr.co.exsoft.document.service.TypeService;
import kr.co.exsoft.eframework.configuration.Constant;
import kr.co.exsoft.eframework.exception.BizException;
import kr.co.exsoft.eframework.library.LocaleLibrary;
import kr.co.exsoft.eframework.util.CommonUtil;
import kr.co.exsoft.eframework.util.ConfigData;
import kr.co.exsoft.folder.service.FolderService;
import kr.co.exsoft.permission.service.AclService;

/**
 * Folder Admin Controller
 * @author 패키지 개발팀
 * @since 2014.07.17
 * @version 3.0
 *
 */
@Controller
@SessionAttributes("sessionVO")
@RequestMapping("/admin")
public class FolderAdminController {
	
	@Autowired
	private CommonService commonService;

	@Autowired
	private FolderService folderService;
	
	@Autowired
	private AclService aclService;
	
	@Autowired
	private TypeService typeService;
	
	@Autowired
	private MessageSource messageSource;
	
    @Autowired
    private DefaultBeanValidator beanValidator;
    
    protected static final Log logger = LogFactory.getLog(FolderAuthController.class);

    /**
     * 
     * <pre>
     * 1. 개용 : 폴더관리
     * 2. 처리내용 : 
     * </pre>
     * @Method Name : folderAdminMainPage
     * @param sessionVO
     * @param model
     * @param map
     * @return String
     */
	@RequestMapping("folderManager.do")
	public String folderAdminMainPage(@ModelAttribute SessionVO sessionVO,Model model,@RequestParam HashMap<String,Object> map) {
		
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

		CommonUtil.setSessionToModel(model, sessionVO);			// call by reference			

		model.addAttribute("part",partInfo.get("part").toString());
		model.addAttribute("menuInfo",menuInfo);
		model.addAttribute("topSelect",Constant.TOPMENU_DOCUMENT);
		model.addAttribute("subSelect",Constant.DOCUMENT_FOLDERMANAGER);
		
		return "docadmin/folderManager";
	}
	
}

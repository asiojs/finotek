package kr.co.exsoft.document.controller;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import kr.co.exsoft.common.service.CacheService;
import kr.co.exsoft.common.service.CommonService;
import kr.co.exsoft.common.vo.SessionVO;
import kr.co.exsoft.document.service.DocumentService;
import kr.co.exsoft.document.service.PageService;
import kr.co.exsoft.document.service.TypeService;
import kr.co.exsoft.eframework.configuration.Constant;
import kr.co.exsoft.eframework.exception.BizException;
import kr.co.exsoft.eframework.library.LocaleLibrary;
import kr.co.exsoft.eframework.util.CommonUtil;
import kr.co.exsoft.eframework.util.ConfigData;
import kr.co.exsoft.folder.service.FolderService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springmodules.validation.commons.DefaultBeanValidator;

/**
 * Document Controller
 * @author 패키지 개발팀
 * @since 2014.07.17
 * @version 3.0
 *
 */
@Controller
@SessionAttributes("sessionVO")
@RequestMapping("/search")
public class SearchAuthController {

	@Autowired
	private DocumentService documentService;
	
	@Autowired
	private PageService pageService;
	
	@Autowired
	private CommonService commonService;
	
	@Autowired
	private TypeService typeService;
	
	@Autowired
	private CacheService cacheService;
	
	@Autowired
	private MessageSource messageSource;
	
    @Autowired
    private DefaultBeanValidator beanValidator;
    
    @Autowired
	private FolderService folderService;
    
    protected static final Log logger = LogFactory.getLog(SearchAuthController.class);
    
    /**
     * 
     * <pre>
     * 1. 개용 : 통합검색 Layout
     * 2. 처리내용 : 
     * </pre>
     * @Method Name : searchLayout
     * @param sessionVO
     * @param model
     * @param map
     * @param request
     * @return String
     */
	@RequestMapping("/searchLayout.do")
	public String searchLayout(@ModelAttribute SessionVO sessionVO,Model model,@RequestParam HashMap<String,Object> map,HttpServletRequest request) {
				
		// 좌측 메뉴 URL 
		String url = map.get("href") != null ? map.get("href").toString() : sessionVO.getSessContextRoot()+"/search/searchList.do";
		
		CommonUtil.setSessionToModel(model, sessionVO);
		
		model.addAttribute("contents",url);			
		model.addAttribute("user_name",sessionVO.getSessName());		
		model.addAttribute("role_id",sessionVO.getSessRole_id());
		model.addAttribute("role_nm",sessionVO.getSessRole_nm());
		model.addAttribute("user_role",Constant.USER_ROLE);
				
		return "searchLayout";
	}
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 통합검색 PAGE
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : myPageDocList
	 * @param sessionVO
	 * @param model
	 * @param map
	 * @return String
	 */
	@RequestMapping("/searchList.do")
	public String myPageDocList(@ModelAttribute SessionVO sessionVO,Model model,@RequestParam HashMap<String,Object> map) {
		
		@SuppressWarnings("unused")
		Locale locale = LocaleLibrary.setLocale(sessionVO.getSessLanguage() != null ? sessionVO.getSessLanguage() : ConfigData.getString("LANGUAGE"));

		model.addAttribute("contextRoot",sessionVO.getSessContextRoot());
		
		return "search/searchDocList";		
	}
}

package kr.co.exsoft.external.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springmodules.validation.commons.DefaultBeanValidator;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.co.exsoft.common.service.CommonService;
import kr.co.exsoft.document.vo.PageVO;
import kr.co.exsoft.external.service.ExternalService;
import kr.co.exsoft.eframework.configuration.Constant;
import kr.co.exsoft.eframework.library.DownloadView;

/**
 * 외부연계 컨트롤
 * @author 패키지 개발팀
 * @since 2014.07.17
 * @version 3.0
 *
 */
@Controller
@RequestMapping("/external")
public class ExternalPublicController {

	@Autowired
	private ExternalService externalService;
	
	@Autowired
	private CommonService commonService;
	
	@Autowired
	private MessageSource messageSource;
	
    @Autowired
    private DefaultBeanValidator beanValidator;
    
	protected static final Log logger = LogFactory.getLog(ExternalPublicController.class);

	/**
	 * 
	 * <pre>
	 * 1. 개용 : URL 링크복사 다운로드 처리
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : urlDownLoad
	 * @param model
	 * @param map
	 * @param sessionVO
	 * @return ModelAndView
	 */
	@RequestMapping("/urlDownLoad.do")
	public ModelAndView urlDownLoad(Model model, @RequestParam HashMap<String,Object> map) {
		
		List<PageVO> pageList = new ArrayList<PageVO>();
		PageVO pageVO = new PageVO();
		
		try {
			
			// 1.다운로드 대상 목록 및 PageVO 객체 구하기
			pageVO = commonService.urlPageInfo(map);
			pageList.add(pageVO);
						
			// 2.다운로드 VIEW 		
			model.addAttribute("isZip",Constant.F);
			model.addAttribute("pageList",pageList);
					
		}catch(Exception e) {
			logger.error(e.getMessage());
			model.addAttribute("message",e.getMessage());
			return new ModelAndView("error/404");
		}
		
		return new ModelAndView(new DownloadView());
	}
	
	@RequestMapping(value = "/interface", method = RequestMethod.POST)
	@ResponseBody
    public String restfulInterface(@RequestParam HashMap<String,Object> map, @ModelAttribute("uploadForm") ExternalMultiFileUpload uploadForm) 
    		throws IllegalStateException, IOException {
        
		/**
		 * TODO : 구현체 작성
		 * map.get("key") : ARIA 암호화 키
		 * returnType : json, string 
		 */
		
		return "";
    }
	
	
	/** Restful Test Start... */
	@RequestMapping(value = "/{name}", method = RequestMethod.GET)
	public String getMovie(@PathVariable String name, ModelMap model) {
 
		model.addAttribute("movie", name);
		return "user/restFul";
 
	}
 
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String getDefaultMovie(ModelMap model) {
 
		model.addAttribute("movie", "this is default movie");
		return "/user/restFul";

	}
	
	// Get 방식
	@RequestMapping(value = "/restful.jsonget/{key}", method = RequestMethod.GET)
	@ResponseBody
	public List<HashMap<String,Object>> getTest(Model model, @PathVariable String key, @RequestParam HashMap<String,Object> map) {
	
		logger.info("json return Sample");
		
		List<HashMap<String,Object>> list = new ArrayList<HashMap<String,Object>>();
		HashMap<String,Object> data1 = new HashMap<String,Object>();
		data1.put("name", "a : " + key);
         
	    HashMap<String,Object> data2 = new HashMap<String,Object>();
		data2.put("userid", "b : " + key);
         
	    list.add(data1);
		list.add(data2);
	
	        return list;
	}
	
	// Post 방식
	@RequestMapping(value = "/restful.jsonpost", method = RequestMethod.POST)
	@ResponseBody
	public List<HashMap<String,Object>> postTest(Model model, @RequestParam HashMap<String,Object> map) {
	
		logger.info("json return Sample");
		
		List<HashMap<String,Object>> list = new ArrayList<HashMap<String,Object>>();
		HashMap<String,Object> data1 = new HashMap<String,Object>();
		data1.put("name", "a : " + map.get("key"));
         
	    HashMap<String,Object> data2 = new HashMap<String,Object>();
		data2.put("userid", "b : "  + map.get("userid"));
         
	    list.add(data1);
		list.add(data2);
	
	    return list;
	}
	
	// Head 방식
	@RequestMapping(value = "/restful.jsonhead", method = RequestMethod.HEAD)
	@ResponseBody
	public List<HashMap<String,Object>> haedTest(Model model, @RequestHeader String key) {
	
		logger.info("json return Sample");
		
		List<HashMap<String,Object>> list = new ArrayList<HashMap<String,Object>>();
		HashMap<String,Object> data1 = new HashMap<String,Object>();
		data1.put("name", "a : " + key);
         
	    HashMap<String,Object> data2 = new HashMap<String,Object>();
		data2.put("userid", "b : " + key);
         
	    list.add(data1);
		list.add(data2);
	
	        return list;
	}
	
	// Multipart single upload
	@RequestMapping(value="/restful.singleUpload", method=RequestMethod.POST)
	@ResponseBody
    public String singleFileUploadTest(@RequestParam("uploadBox") MultipartFile file){ // <input type="file" name="uploadBox">
            String name = "File not found";
        if (!file.isEmpty()) {
            try {
            	name = file.getName();
                byte[] bytes = file.getBytes();
                BufferedOutputStream stream = 
                        new BufferedOutputStream(new FileOutputStream(new File(name + "-uploaded")));
                stream.write(bytes);
                stream.close();
                return "You successfully uploaded " + name + " into " + name + "-uploaded !";
            } catch (Exception e) {
                return "You failed to upload " + name + " => " + e.getMessage();
            }
        } else {
            return "You failed to upload " + name + " because the file was empty.";
        }
    }
	
	// Multipart multi upload
	@RequestMapping(value = "/restful.multiUpload", method = RequestMethod.POST)
	@ResponseBody
    public String multiFileUploadTest( @ModelAttribute("uploadForm") ExternalMultiFileUpload uploadForm, Model map) 
    		throws IllegalStateException, IOException {
        
		String saveDirectory = "d:/temp/";
 
        List<MultipartFile> crunchifyFiles = uploadForm.getFiles();
        List<String> fileNames = new ArrayList<String>();
 
        if (null != crunchifyFiles && crunchifyFiles.size() > 0) {
            for (MultipartFile multipartFile : crunchifyFiles) {
 
                String fileName = multipartFile.getOriginalFilename();
                if (!"".equalsIgnoreCase(fileName)) {
                    // Handle file content - multipartFile.getInputStream()
                    multipartFile.transferTo(new File(saveDirectory + fileName));
                    fileNames.add(fileName);
                }
            }
        } else {
        	return "File not found";
        }
 
        map.addAttribute("files", fileNames);
        return "uploadfilesuccess";
    }
	
	@RequestMapping(value = "/restful.postwithfile", method = RequestMethod.POST)
	@ResponseBody
    public List<HashMap<String,Object>> uploadMultipleFileHandler(
    		@RequestHeader HashMap<String,Object> headMap, 
    		@RequestParam HashMap<String,Object> paraMap, 
    		@RequestParam("fileUpload") MultipartFile[] files,
    		HttpServletRequest request)
    				throws IllegalStateException, IOException {
		
		List<HashMap<String,Object>> list = new ArrayList<HashMap<String,Object>>();
		HashMap<String,Object> resultMap = new HashMap<String,Object>();
	
		// Head 정보
		for( String key : headMap.keySet() ){
			HashMap<String,Object> headData = new HashMap<String,Object>();
			headData.put(key,headMap.get(key));
			list.add(headData);
	    }
		
		// Attr 정보
		Enumeration<String> attrEnume = request.getAttributeNames();		
		while(attrEnume.hasMoreElements()){
			HashMap<String,Object> attrData = new HashMap<String,Object>();
			String key = attrEnume.nextElement();
			attrData.put(key,headMap.get(key));
			list.add(attrData);
		}
				
		// parameter 정보
		for( String key : paraMap.keySet() ){
			HashMap<String,Object> paramData = new HashMap<String,Object>();
			paramData.put(key,paraMap.get(key));
			list.add(paramData);
	    }
		
		String saveDirectory = "d:/temp/00.test";
		 
        List<String> fileNames = new ArrayList<String>();
 
        if (null != files && files.length > 0) {
            for (MultipartFile multipartFile : files) {
 
                String fileName = multipartFile.getOriginalFilename();
                if (!"".equalsIgnoreCase(fileName)) {
                    // Handle file content - multipartFile.getInputStream()
                    multipartFile.transferTo(new File(saveDirectory + fileName));
                    fileNames.add(fileName);
                }
            }
        } else {
        	resultMap.put("result", "File not found");
        	list.add(resultMap);
        	return list;
        }
        
        resultMap.put("result", "process success");
    	list.add(resultMap); 
	    return list;
 
    }
	
	/** Restful Test End... */
	
}

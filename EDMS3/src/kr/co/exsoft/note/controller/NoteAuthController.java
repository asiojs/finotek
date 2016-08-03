package kr.co.exsoft.note.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import kr.co.exsoft.common.vo.SessionVO;
import kr.co.exsoft.document.service.DocumentService;
import kr.co.exsoft.eframework.configuration.Constant;
import kr.co.exsoft.eframework.exception.BizException;
import kr.co.exsoft.eframework.library.LocaleLibrary;
import kr.co.exsoft.eframework.util.CommonUtil;
import kr.co.exsoft.eframework.util.ConfigData;
import kr.co.exsoft.eframework.util.StringUtil;
import kr.co.exsoft.note.service.NoteService;
import kr.co.exsoft.note.vo.NoteVO;
import kr.co.exsoft.permission.vo.AclItemVO;
import kr.co.exsoft.permission.vo.AclVO;
import kr.co.exsoft.user.vo.GroupedVO;
import net.sf.json.JSONArray;

import org.apache.commons.collections.map.CaseInsensitiveMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

/**
 * Note Controller
 * @author 패키지 개발팀
 * @since 2015.03.19
 * @version 3.0
 *
 */
@Controller
@SessionAttributes("sessionVO")
@RequestMapping("/note")
public class NoteAuthController {
	
	@Autowired
	private NoteService noteService;

	@Autowired
	private MessageSource messageSource;
	

	/**
	 * 
	 * <pre>
	 * 1. 개용 : 쪽지관리메인 ( 대화함/받은쪽지함/보낸쪽지함/쪽지보관함)
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : noteMain
	 * @param sessionVO
	 * @param model
	 * @param map
	 * @return String
	 */
	@RequestMapping(value="/noteMain.do", method=RequestMethod.POST)
	public String noteMain(@ModelAttribute SessionVO sessionVO,Model model,@RequestParam HashMap<String,Object> map) {
		
		@SuppressWarnings("unused")
		Locale locale = LocaleLibrary.setLocale(sessionVO.getSessLanguage() != null  ? sessionVO.getSessLanguage() : ConfigData.getString("LANGUAGE"));

		/**********************************************
		 * 쪽지관리 탭메뉴 정의
		 * 	대화함 - TALK
		 * 	받은쪽지함(기본) - RECEIVE
		 * 	보낸쪽지함 - SEND
		 * 	쪽지보관함 - BOX
		 ***********************************************/
		model.addAttribute("tabType",map.get("tabType") != null ? map.get("tabType").toString() : Constant.NOTE_TAB_RECEIVE);
		
		CommonUtil.setSessionToModel(model, sessionVO);
		
		return "note/noteMain";
	}
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 쪽지관리 - 사용자선택
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : noteUserSelect
	 * @param sessionVO
	 * @param model
	 * @param map
	 * @return String
	 */
	@RequestMapping(value="/noteUserSelect.do", method=RequestMethod.POST)
	public String noteUserSelect(@ModelAttribute SessionVO sessionVO,Model model,@RequestParam HashMap<String,Object> map) {
		
		@SuppressWarnings("unused")
		Locale locale = LocaleLibrary.setLocale(sessionVO.getSessLanguage() != null  ? sessionVO.getSessLanguage() : ConfigData.getString("LANGUAGE"));

		model.addAttribute("tabType",map.get("tabType") != null ? map.get("tabType").toString() : "myinfo");
		
		CommonUtil.setSessionToModel(model, sessionVO);
		
		return "note/noteUserSelect";
	}
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 쪽지 등록 
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : noteRegist
	 * @param sessionVO
	 * @param model
	 * @param map
	 * @return String
	 */
	
	@RequestMapping(value="/noteSendControl.do", method=RequestMethod.POST)
	@ResponseBody 
	public Map<String,Object> noteSendControl(Model model, @ModelAttribute SessionVO sessionVO, @RequestParam HashMap<String,Object> map,
			@ModelAttribute("noteVO") NoteVO noteVO,BindingResult bindingResult,HttpServletRequest request) {
		
		Locale locale = LocaleLibrary.setLocale(sessionVO.getSessLanguage() != null  ? sessionVO.getSessLanguage() : ConfigData.getString("LANGUAGE"));		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<HashMap<String,Object>> noteList = new ArrayList<HashMap<String,Object>>();
		try {
			noteList = noteService.noteValidList(map);
			//resultMap = noteService.deleteDocument(docList, map, sessionVO);
			
			//String reciverArrayList =  map.get("reciverArrayList") != null ? map.get("reciverArrayList").toString() : "";
			// 쪾지 등록
			resultMap = noteService.noteListForInserting(noteList, map, sessionVO);
			//resultMap = noteService.noteListForInserting(map, reciverArrayList, sessionVO);

		}catch(Exception e)	{										
			resultMap.put("result",Constant.RESULT_FALSE);
			resultMap.put("message",messageSource.getMessage("common.system.error",new Object[0],locale));			
		}
		
		return resultMap;
		
	}
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 쪽지 답장 등록 
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : noteReSendControl
	 * @param sessionVO
	 * @param model
	 * @param map
	 * @return String
	 */
	
	@RequestMapping(value="/noteReSendControl.do", method=RequestMethod.POST)
	@ResponseBody 
	public Map<String,Object> noteReSendControl(Model model, @ModelAttribute SessionVO sessionVO, @RequestParam HashMap<String,Object> map,
			@ModelAttribute("noteVO") NoteVO noteVO,BindingResult bindingResult,HttpServletRequest request) {
		
		Locale locale = LocaleLibrary.setLocale(sessionVO.getSessLanguage() != null  ? sessionVO.getSessLanguage() : ConfigData.getString("LANGUAGE"));		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<HashMap<String,Object>> noteList = new ArrayList<HashMap<String,Object>>();
		try {
			// 쪾지 등록
			resultMap = noteService.noteListForReInserting( map, sessionVO);
			//resultMap = noteService.noteListForInserting(map, reciverArrayList, sessionVO);

		}catch(Exception e)	{										
			resultMap.put("result",Constant.RESULT_FALSE);
			resultMap.put("message",messageSource.getMessage("common.system.error",new Object[0],locale));			
		}
		
		return resultMap;
		
	}
	/**
	 * <pre>
	 * 1. 개용 : 새쪽지  조회
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : noteTop5List
	 * @param model
	 * @param sessionVO
	 * @param map
	 * @return
	 */	
	@RequestMapping(value="/noteTopNSelect.do", method=RequestMethod.POST)
	@ResponseBody 
	public Map<String,Object> noteTopNList(Model model, @ModelAttribute SessionVO sessionVO, @RequestParam HashMap<String,Object> map) {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Locale locale = LocaleLibrary.setLocale(sessionVO.getSessLanguage() != null  ? sessionVO.getSessLanguage() : ConfigData.getString("LANGUAGE"));
		
		try {
			map.put("user_id", sessionVO.getSessId());
			if(map.get("new_count").equals("ALL")){
				map.put("new_count","");
			}else{
				map.put("new_count", Constant.NOTE_RECENT_CNT); //가져올 최신 쪽지 갯수 제한 defalut = 5
			}
			
			resultMap = noteService.noteNewTopNInfoList(map);

		} catch (Exception e) {
			resultMap.put("result",Constant.RESULT_FALSE);
			resultMap.put("message",messageSource.getMessage("common.system.error",new Object[0],locale));
		}
		return resultMap;
	}
	
	/**
	 * <pre>
	 * 1. 개용 : 대화함 조회
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : noteSelectTalk
	 * @param model
	 * @param sessionVO
	 * @param map
	 * @return
	 */	
	@RequestMapping(value="/noteSelectTalk.do", method=RequestMethod.POST)
	@ResponseBody 
	public Map<String,Object> noteSelectTalk(Model model, @ModelAttribute SessionVO sessionVO, @RequestParam HashMap<String,Object> map) {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Locale locale = LocaleLibrary.setLocale(sessionVO.getSessLanguage() != null  ? sessionVO.getSessLanguage() : ConfigData.getString("LANGUAGE"));
		
		try {
			map.put("user_id", sessionVO.getSessId());
			
			resultMap = noteService.noteTalkList(map);
			
		} catch (Exception e) {
			resultMap.put("result",Constant.RESULT_FALSE);
			resultMap.put("message",messageSource.getMessage("common.system.error",new Object[0],locale));
		}
		return resultMap;
	}
	/**
	 * <pre>
	 * 1. 개용 : 대화함 조회
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : noteSelectTalk
	 * @param model
	 * @param sessionVO
	 * @param map
	 * @return
	 */	
	@RequestMapping(value="/noteSelectTalkDetail.do", method=RequestMethod.POST)
	@ResponseBody 
	public Map<String,Object> noteSelectTalkDetail(Model model, @ModelAttribute SessionVO sessionVO, @RequestParam HashMap<String,Object> map) {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Locale locale = LocaleLibrary.setLocale(sessionVO.getSessLanguage() != null  ? sessionVO.getSessLanguage() : ConfigData.getString("LANGUAGE"));
		
		try {
			map.put("user_id", sessionVO.getSessId());
			
			resultMap = noteService.noteTalkDetailList(map);

		} catch (Exception e) {
			resultMap.put("result",Constant.RESULT_FALSE);
			resultMap.put("message",messageSource.getMessage("common.system.error",new Object[0],locale));
		}
		return resultMap;
	}
	/**
	 * <pre>
	 * 1. 개용 : 쪽지함 목록조회
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : documentVersionList
	 * @param model
	 * @param sessionVO
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/noteReceiveSendList.do", method=RequestMethod.POST)
	@ResponseBody 
	public Map<String,Object> noteReceiveSendList(Model model, @ModelAttribute SessionVO sessionVO, @RequestParam HashMap<String,Object> map) {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Locale locale = LocaleLibrary.setLocale(sessionVO.getSessLanguage() != null  ? sessionVO.getSessLanguage() : ConfigData.getString("LANGUAGE"));
		
		try {
			map.put("user_id", sessionVO.getSessId());
			
			//System.out.println("Client Parameter : "+ map.get("note_name"));
			
			if(map.get("note_name").equals("Receive")){//받은 쪽지함
				map.put("note_type", "R"); 
			}else if(map.get("note_name").equals("Send")){//보낸 쪽지함
				map.put("note_type", "S"); 
			}else if(map.get("note_name").equals("Save")){//보관 쪽지함
				map.put("note_save", "Y"); 
			}

			resultMap = noteService.noteAllReceiveSendInfoList(map);
			
		} catch (Exception e) {
			resultMap.put("result",Constant.RESULT_FALSE);
			resultMap.put("message",messageSource.getMessage("common.system.error",new Object[0],locale));
		}
		
		return resultMap;
	}
	
	/**
	 * <pre>
	 * 1. 개용 : 보관함 update
	 * 2. 처리내용 : 받은 쪽지나 보낸 쪽지를 보관
	 * </pre>
	 * @Method Name : noteStorageboxUpdate
	 * @param model
	 * @param sessionVO
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/noteSaveUpdate.do", method=RequestMethod.POST)
	@ResponseBody 
	public Map<String,Object> noteSaveUpdate(Model model, @ModelAttribute SessionVO sessionVO, @RequestParam HashMap<String,Object> map) {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		int result = 0;
		Locale locale = LocaleLibrary.setLocale(sessionVO.getSessLanguage() != null  ? sessionVO.getSessLanguage() : ConfigData.getString("LANGUAGE"));
		
		try {

			result = noteService.noteSaveUpdate(map, sessionVO);
			if(result > 0) {
				resultMap.put("result",Constant.RESULT_TRUE);
			}
						
		}catch(Exception e)	{										
			resultMap.put("result",Constant.RESULT_FALSE);
			resultMap.put("message",messageSource.getMessage("common.system.error",new Object[0],locale));			
		}
		
		return resultMap;
	}

	/**
	 * <pre>
	 * 1. 개용 : 수신함 쪽지 읽음 update
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : noteReadUpdate
	 * @param model
	 * @param sessionVO
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/noteReadUpdate.do", method=RequestMethod.POST)
	@ResponseBody 
	public Map<String,Object> noteReadUpdate(Model model, @ModelAttribute SessionVO sessionVO, @RequestParam HashMap<String,Object> map) {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		int result = 0;
		Locale locale = LocaleLibrary.setLocale(sessionVO.getSessLanguage() != null  ? sessionVO.getSessLanguage() : ConfigData.getString("LANGUAGE"));
		
		try {

			result = noteService.noteReadUpdate(map, sessionVO);
			if(result > 0) {
				resultMap.put("result",Constant.RESULT_TRUE);
			}
						
		}catch(Exception e)	{										
			resultMap.put("result",Constant.RESULT_FALSE);
			resultMap.put("message",messageSource.getMessage("common.system.error",new Object[0],locale));			
		}
		
		return resultMap;
	}
	/**
	 * <pre>
	 * 1. 개용 : noteDelete
	 * 2. 처리내용 : 받은 쪽지나 보낸 쪽지를 삭제
	 * </pre>
	 * @Method Name : noteDelete
	 * @param model
	 * @param sessionVO
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/noteDelete.do", method=RequestMethod.POST)
	@ResponseBody 
	public Map<String,Object> noteDelete(Model model, @ModelAttribute SessionVO sessionVO, @RequestParam HashMap<String,Object> map) {
		int result = 0;
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Locale locale = LocaleLibrary.setLocale(sessionVO.getSessLanguage() != null  ? sessionVO.getSessLanguage() : ConfigData.getString("LANGUAGE"));
		
		try {

			result = noteService.noteDelete(map, sessionVO);
			if(result > 0) {
				resultMap.put("result",Constant.RESULT_TRUE);
			}
		} catch (Exception e) {
			resultMap.put("result",Constant.RESULT_FALSE);
			resultMap.put("message",messageSource.getMessage("common.system.error",new Object[0],locale));
		}
		
		return resultMap;
	}
	
	
	
}

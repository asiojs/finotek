package kr.co.exsoft.user.controller;

import java.util.Map;
import java.util.HashMap;
import java.util.List;

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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import kr.co.exsoft.eframework.configuration.Constant;
import kr.co.exsoft.eframework.util.StringUtil;
import kr.co.exsoft.user.service.GroupService;
import kr.co.exsoft.user.vo.GroupVO;

/**
 * Group Controller
 * @author 패키지 개발팀
 * @since 2014.07.17
 * @version 3.0
 *
 */
@Controller
@RequestMapping("/group")
public class GroupAuthController {

	@Autowired
	private GroupService groupService;
	
	@Autowired
	private MessageSource messageSource;
	
    @Autowired
    private DefaultBeanValidator beanValidator;
    
	protected static final Log logger = LogFactory.getLog(GroupAuthController.class);
	

	
	/**
	 * 
	 * <pre>
	 * 1. 개요 : 부서 목록 조회 샘플
	 * 2. 처리내용 :
	 * </pre>
	 * @Method Name : groupList
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/groupList.do", method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> groupLists(Model model, HttpServletRequest request,@RequestParam HashMap<String,Object> map) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		HashMap<String,Object> param = new  HashMap<String, Object>();
		
		try {
			String mapId = map.get(Constant.TREE_MAP_ID) != null ? map.get(Constant.TREE_MAP_ID).toString() : null;
			String parentId = map.get(Constant.TREE_PARENT_ID) != null ? map.get(Constant.TREE_PARENT_ID).toString() : null;
			String rootId = map.get(Constant.TREE_ROOT_ID) != null ? map.get(Constant.TREE_ROOT_ID).toString() : null;
			String treeType = map.get("treeType") != null ? map.get("treeType").toString() : ""; // 그룹관리 > 구성원추가시 미소속그룹을 보이기위한 변수
			
			param.put("mapId", mapId);
			param.put("parentId", parentId);
			param.put("rootId", rootId);
			param.put("treeType", treeType);
			
			List<GroupVO> groupList = null;
			
			if (StringUtil.isNull(parentId) || (StringUtil.isNull(parentId) && !StringUtil.isNull(rootId))) {
				groupList = groupService.rootGroupList(param);
			} else {
				groupList = groupService.childGroupList(param);
			}
			
			resultMap.put("groupList", groupList);
			resultMap.put("result", Constant.SUCCESS);
			
		} catch (Exception e) {
			resultMap.put("result", Constant.RESULT_FAIL);
			resultMap.put("message", e.getMessage());
			e.printStackTrace();
		}
		return resultMap;
	}
}

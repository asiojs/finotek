package kr.co.exsoft.user.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import kr.co.exsoft.common.service.CommonService;
import kr.co.exsoft.common.vo.SessionVO;
import kr.co.exsoft.document.dao.DocumentDao;
import kr.co.exsoft.eframework.configuration.Constant;
import kr.co.exsoft.eframework.library.ExsoftAbstractServiceImpl;
import kr.co.exsoft.eframework.util.CommonUtil;
import kr.co.exsoft.eframework.util.ConfigData;
import kr.co.exsoft.eframework.util.PatternUtil;
import kr.co.exsoft.eframework.util.StringUtil;
import kr.co.exsoft.eframework.util.XlsUtil;
import kr.co.exsoft.folder.dao.FolderDao;
import kr.co.exsoft.folder.vo.FolderVO;
import kr.co.exsoft.permission.dao.AclDao;
import kr.co.exsoft.permission.service.AclService;
import kr.co.exsoft.permission.vo.AclItemVO;
import kr.co.exsoft.permission.vo.AclVO;
import kr.co.exsoft.user.dao.GroupDao;
import kr.co.exsoft.user.dao.UserDao;
import kr.co.exsoft.user.vo.GroupVO;
import kr.co.exsoft.user.vo.GroupedVO;
import kr.co.exsoft.user.vo.UserVO;


/**
 * Group 서비스 구현 부분
 * @author 패키지 개발팀
 * @since 2014.07.17
 * @version 3.0
 *
 */
@Service("groupService")
public class GroupServiceImpl extends ExsoftAbstractServiceImpl implements GroupService {
	
	@Autowired
	@Qualifier("sqlSession")
	private SqlSession sqlSession;

	@Autowired
	@Qualifier("sqlSessionBatch")
	private SqlSession sqlSessionBatch;
	
	@Autowired
	private CommonService commonService;
	
	@Autowired
	private AclService aclService;
	
	@Override
	public int groupedWrite(HashMap<String,Object> map) {
		
		int ret = 0;
		
		GroupDao groupDao = sqlSession.getMapper(GroupDao.class);
		
		ret = groupDao.groupedWrite(map);
		
		return ret;
	}
	
	@Override
	public void batchUserWrite(List<HashMap<String,Object>> userList) throws Exception {
		
		GroupDao groupDao = sqlSessionBatch.getMapper(GroupDao.class);
		
		for(HashMap<String,Object> map : userList) {
			
			groupDao.groupedWrite(map);
		}
	}
	
	@Override
	public List<GroupVO> rootGroupList(HashMap<String,Object> params) throws Exception {
		
		GroupDao groupDao = sqlSession.getMapper(GroupDao.class);
		
		List<GroupVO> groupList = groupDao.rootGroupList(params);
		
		return groupList;
	}
	
	@Override
	public List<GroupVO> childGroupList(HashMap<String,Object> params) throws Exception {
		
		GroupDao groupDao = sqlSession.getMapper(GroupDao.class);
		
		List<GroupVO> groupList = groupDao.childGroupList(params);
		
		return groupList;
	}

	@Override
	public GroupVO groupDetail(String groupId) throws Exception {
		GroupDao groupDao = sqlSession.getMapper(GroupDao.class);
		
		GroupVO groupVO = groupDao.groupDetail(groupId);
		
		return groupVO;
	}

	@Override
	public Map<String, Object> groupWrite(GroupVO groupVO, SessionVO sessionVO) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		GroupDao groupDao = sqlSession.getMapper(GroupDao.class);
		FolderDao folderDao = sqlSession.getMapper(FolderDao.class);
		AclDao aclDao = sqlSession.getMapper(AclDao.class);
		
		// ----------------------------------
		// 1. 그룹 ID 생성. 
		// ----------------------------------
		groupVO.setGroup_id(String.format("%s%012d", Constant.ID_PREFIX_GROUP, commonService.commonNextVal(Constant.COUNTER_ID_FILE)));
		groupVO.setDept_cd(groupVO.getGroup_id());

		// ----------------------------------
		// 2. 그룹 등록
		// ----------------------------------
		groupDao.groupWrite(groupVO);
		
		// ----------------------------------
		// 3. 부서맵일 경우 (기본 ACL, 부서 폴더 등록)
		// ----------------------------------
		if (groupVO.getMap_id().equals(Constant.MAP_ID_DEPT)) {
			List<String> groupIdList = new ArrayList<String>();
			List<AclItemVO> aclItemList = new ArrayList<AclItemVO>();
			
			groupIdList.add(sessionVO.getSessGroup_id());
			
			// ----------------------------------
			// 3-1. 부서 기본 ACL 생성
			// ----------------------------------
			// 3-1-1.ACL
			AclVO aclVO = new AclVO();
			aclVO.setAcl_id(CommonUtil.getStringID(Constant.ID_PREFIX_ACL, commonService.commonNextVal(Constant.COUNTER_ID_ACL)));
			aclVO.setAcl_name(String.format("%s 부서 기본 권한", groupVO.getGroup_name_ko()));
			aclVO.setAcl_type(Constant.ACL_ACL_TYPE_TEAM);
			aclVO.setOpen_id(groupVO.getGroup_id());
			aclVO.setOpen_name(groupVO.getGroup_name_ko());
			aclVO.setOpen_isgroup(Constant.T);
			aclVO.setCreator_id(sessionVO.getSessId());
			aclVO.setSort_index("0");
			
			// 3-1-2.ACL Item
			AclItemVO ownerFolderAclItem = 	new AclItemVO(aclVO.getAcl_id(), "F", "OWNER", "F", "T", "T", "T", "T", "T", "T", "T", "T");
			AclItemVO ownerDocAclItem = 	new AclItemVO(aclVO.getAcl_id(), "D", "OWNER", "F", "T", "T", "T", "T", "T", "T", "T", "T");
			AclItemVO worldFolderAclItem = 	new AclItemVO(aclVO.getAcl_id(), "F", "WORLD", "F", "T", "T", "T", "F", "F", "F", "F", "F");
			AclItemVO worldDocAclItem = 	new AclItemVO(aclVO.getAcl_id(), "D", "WORLD", "F", "T", "T", "T", "F", "F", "F", "F", "F");
			AclItemVO groupFolderAclItem = 	new AclItemVO(aclVO.getAcl_id(), "F", groupVO.getGroup_id(), "T", "F", "T", "T", "F", "F", "F", "F", "F");
			AclItemVO groupDocAclItem = 	new AclItemVO(aclVO.getAcl_id(), "D", groupVO.getGroup_id(), "T", "F", "T", "T", "T", "F", "T", "F", "F");
			
			aclItemList.add(ownerFolderAclItem);
			aclItemList.add(ownerDocAclItem);
			aclItemList.add(worldFolderAclItem);
			aclItemList.add(worldDocAclItem);
			aclItemList.add(groupFolderAclItem);
			aclItemList.add(groupDocAclItem);
			
			// 3-1-3. 동일한 이름의 권한이 있는지 체크
			HashMap<String, Object> validation = new HashMap<String, Object>();
			validation.put("acl_name", aclVO.getAcl_name());
			
			if (aclDao.aclCountByAclName(validation) > 0) {
				throw processException("acl.fail.acl.name.duplication");
			}
			
			// 3-1-4. ACL 생성
			if (aclDao.aclWrite(aclVO) == 0) {
				throw processException("common.system.error");
			}
			
			// 3-1-5. ACL Item 생성
			for (AclItemVO aclItemVO : aclItemList) {
				if (aclDao.aclItemWrite(aclItemVO) == 0) {
					throw processException("common.system.error");
				}
			}
			
			// ----------------------------------
			// 3-2. 부서 폴더 등록
			// ----------------------------------
			
			// 부서 폴더 ID 얻기
			String folder_id = CommonUtil.getChangedResourceIDByPrefix(groupVO.getGroup_id(), Constant.ID_PREFIX_FOLDER);
			
			// 상위 부서 폴더 ID 얻기
			String parent_id = CommonUtil.getChangedResourceIDByPrefix(groupVO.getParent_id(), Constant.ID_PREFIX_FOLDER);
			
			// .a 부서 폴더 VO를 생성한다
			FolderVO folderVO = new FolderVO();
			folderVO.setFolder_id(folder_id);
			folderVO.setFolder_name_ko(groupVO.getGroup_name_ko());
			folderVO.setFolder_name_en(groupVO.getGroup_name_en());
//			folderVO.setFolder_name_zh(groupVO.getGroup_name_zh());
			folderVO.setParent_id(parent_id);
			folderVO.setFolder_type(Constant.FOLDER_TYPE_DEPT);
			folderVO.setMap_id(Constant.MAP_ID_DEPT);
			folderVO.setSort_index(groupVO.getSort_index());
			folderVO.setFolder_status(Constant.FOLDER_STATUS_CREATE);
			folderVO.setAcl_id(aclVO.getAcl_id());
			folderVO.setCreator_id(sessionVO.getSessId());
			folderVO.setCreator_name(sessionVO.getSessName());
			folderVO.setIs_type(Constant.FOLDER_TYPE_ALL_TYPE);
			folderVO.setIs_save(Constant.FOLDER_SAVE_NO);
			
			// .b 폴더를 등록한다
			folderDao.folderWrite(folderVO);
			
			// ----------------------------------
			// 3-3. rGate 폴더 등록
			// ----------------------------------
			// 추후 구현 /applecode
		
		}
		
		// ----------------------------------
		// 4. 히스토리 등록
		// ----------------------------------
		groupVO.setGroup_seq(commonService.commonNextVal(Constant.COUNTER_ID_GROUP_HT));
		groupVO.setStatus(Constant.HISTORY_STATUS_CREATE);
		
		groupDao.groupHistoryWrite(groupVO);
		
		resultMap.put("result", Constant.RESULT_SUCCESS);
		return resultMap;
	}

	@Override
	public Map<String, Object> groupUpdate(GroupVO groupVO, SessionVO sessionVO) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		GroupDao groupDao = sqlSession.getMapper(GroupDao.class);
		FolderDao folderDao = sqlSession.getMapper(FolderDao.class);
		AclDao aclDao = sqlSession.getMapper(AclDao.class);
		
		// ------------------------------------
		// 1. 그룹 수정. 
		// ------------------------------------
		groupDao.groupUpdate(groupVO);
		
		// ------------------------------------
		// 2. 부서 기본 ACL, 부서 폴더 수정. 
		// ------------------------------------
		
		// 부서 맵일 경우
		if (groupVO.getMap_id().equals(Constant.MAP_ID_DEPT)) {
		
			// ------------------------------------
			// 2-1. 부서 폴더 수정. 
			// ------------------------------------
			
			// 부서 폴더 ID 얻기
			String folder_id = CommonUtil.getChangedResourceIDByPrefix(groupVO.getGroup_id(), Constant.ID_PREFIX_FOLDER);
			
			// 상위 부서 폴더 ID 얻기
			String parent_id = CommonUtil.getChangedResourceIDByPrefix(groupVO.getParent_id(), Constant.ID_PREFIX_FOLDER);
			
			HashMap<String, Object> folderParam = new HashMap<String, Object>();
			folderParam.put("folder_id", folder_id);

			// 기존 폴더 정보 얻기
			FolderVO folderVO = folderDao.folderDetail(folderParam);
			
			if (folderVO != null) {
				// 부서 폴더 수정
				folderVO.setFolder_name_ko(groupVO.getGroup_name_ko());
				folderVO.setFolder_name_en(groupVO.getGroup_name_en());
//				folderVO.setFolder_name_zh(groupVO.getGroup_name_zh());
				folderVO.setFolder_name_ja(groupVO.getGroup_name_ja());
				folderVO.setParent_id(parent_id);
				folderVO.setSort_index(groupVO.getSort_index());
				folderVO.setFolder_status(groupVO.getStatus());
				folderVO.setUpdate_action(Constant.ACTION_UPDATE);
				
				folderDao.folderUpdate(folderVO);
			}
			
			// ------------------------------------
			// 2-2. 부서 기본 ACL 수정. : 추후 구현
			// ------------------------------------
			AclVO aclVO = new AclVO();
			aclVO.setAcl_id(folderVO.getAcl_id());
			aclVO.setOpen_isgroup(""); // VO 생성시 초기값으로 'F'를 갖기때문에 의도치 않게 값이 변경되는것을 방지하기 위함.
			aclVO.setAcl_name(String.format("%s 부서 기본 권한", groupVO.getGroup_name_ko()));
			
			aclDao.aclUpdate(aclVO);
			
			// ------------------------------------
			// 2-3. rGate 폴더 수정 : 추후 구현				
			// ------------------------------------
			
		}
		
		// ------------------------------------
		// 3. Grouped 수정. (그룹-사용자 맵핑)
		// ------------------------------------
		
		// 3-1.부서 맵일 경우 사용자의 부서를 이동 처리한다
		if (groupVO.getMap_id().equals(Constant.MAP_ID_DEPT)) {

			HashMap<String, Object> selectedUserListInfo = new HashMap<String, Object>();
			List<String> removeUserList = new ArrayList<String>();
			List<String> appendUserList = new ArrayList<String>();
			
			// ### 기존 사용자를 모두 미소속 부서로 이동시킨다
			// 3-1-2.기존 사용자 목록 얻기
			List<GroupedVO> groupUserList = groupDao.groupedList(groupVO.getGroup_id()); 
			
			
			// 3-1-3.기존 사용자 중 목록에서 제거된 사용자를 추출한다
			for (GroupedVO groupUser : groupUserList) {
				if (!groupVO.getUser_id_list().contains(groupUser.getUser_id())) {
					removeUserList.add(groupUser.getUser_id());
				}
			}
			
			// 3-1-4. 제거된 사용자가 있을경우 미소속으로 변경 처리한다
			// 미소속 그룹을 사용할 경우에만 변경 처리함.
			if (removeUserList.size() > 0 && ConfigData.getBoolean("USE_TEMP_GROUP")) {
				GroupVO tempGroup = groupDao.independentGroupDetail();
				
				if (tempGroup == null) {
					throw processException("group.fail.independent.null");
				}

				selectedUserListInfo.put("group_id", tempGroup.getGroup_id());
				
				// 삭제할 사용자 수 만큼 루프
				for (String removeUserId : removeUserList) {
					selectedUserListInfo.put("user_id", removeUserId);
					groupDao.groupedUpdate(selectedUserListInfo);
				}
			}
			
			// 3-1-5. 추가된 사용자를 추출한다
			for (String user : groupVO.getUser_id_list()) {
				boolean isMember = false;
				
				// 최종 선택된 사용자를 기존 부서원목록에서 검색후 없으면 신규 추가원임
				for (GroupedVO groupUser : groupUserList) {
					if (groupUser.getUser_id().equals(user)) {
						isMember = true;
					}
				}
				
				if (!isMember)
					appendUserList.add(user);
			}
			
			// 3-1-5. 선택된 사용자가 있을경우 현재 부서로 이동시킨다.
			if (appendUserList.size() > 0) {
				selectedUserListInfo.put("group_id", groupVO.getGroup_id());
				
				for (String user : appendUserList) {
					selectedUserListInfo.put("user_id", user);
					groupDao.groupedUpdate(selectedUserListInfo);
				}
			}
		}
		
		// 3-2.기타 맵의 경우 최종 선택된 사용자를 추가한다
		else {
			// 3-2-1. 해당 그룹의 모든 구성원 제거.
			HashMap<String, Object> groupedOptions = new HashMap<String, Object>();
			groupedOptions.put("group_id", groupVO.getGroup_id());
			groupDao.groupedDelete(groupedOptions);
			
			for (String userId : groupVO.getUser_id_list()) {
				// 그룹구성원을 선택했을때
				if(userId != null && userId != "") {
					HashMap<String, Object> userInfo = new HashMap<String, Object>();
					userInfo.put("group_id", groupVO.getGroup_id());
					userInfo.put("user_id", userId);
					userInfo.put("is_default", Constant.F);
					
					groupDao.groupedWrite(userInfo);
				}
			}
			
		}
		
		// ------------------------------------
		// 4. 히스토리 등록. (그룹 수정)
		// ------------------------------------
		groupVO.setGroup_seq(commonService.commonNextVal(Constant.COUNTER_ID_GROUP_HT));
		groupVO.setStatus(Constant.HISTORY_STATUS_UPDATE);
		groupVO.setDept_cd(groupVO.getGroup_id());
		
		groupDao.groupHistoryWrite(groupVO);

		resultMap.put("result", Constant.RESULT_SUCCESS);
		return resultMap;
	}

	@Override
	public Map<String, Object> groupDelete(GroupVO groupVO, SessionVO sessionVO) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		HashMap<String, Object> folderDeleteParam = new HashMap<String, Object>();
		List<String> aclIds = new ArrayList<String>();
		
		GroupDao groupDao = sqlSession.getMapper(GroupDao.class);
		FolderDao folderDao = sqlSession.getMapper(FolderDao.class);
		
		// ------------------------------------
		// 1. 그룹 삭제. 
		// ------------------------------------
		groupDao.groupDelete(groupVO);
		
		// 부서 맵의 경우.
		if (groupVO.getMap_id().equals(Constant.MAP_ID_DEPT)) {
			// ------------------------------------
			// 2. 부서 폴더 삭제. 
			// ------------------------------------
			
			// 부서 폴더 ID 얻기.
			folderDeleteParam.put("folder_id", CommonUtil.getChangedResourceIDByPrefix(groupVO.getGroup_id(), Constant.ID_PREFIX_FOLDER));
			
			// 부서 폴더 정보 얻기
			FolderVO groupFolder = folderDao.folderDetail(folderDeleteParam);
			
			// 부서 폴더 삭제.
			folderDao.folderDelete(folderDeleteParam);
			
			// ------------------------------------
			// 3. rGate 폴더 삭제. (고도화 시 구현)
			// ------------------------------------
			
			// ------------------------------------
			// 4. ACL 정보 삭제
			// ------------------------------------
			aclIds.add(groupFolder.getAcl_id());
			aclService.aclDelete(null, aclIds, sessionVO);
		}
		
		// ------------------------------------
		// 4. 히스토리 등록. (그룹 수정)
		// ------------------------------------
		groupVO.setGroup_seq(commonService.commonNextVal(Constant.COUNTER_ID_GROUP_HT));
		groupVO.setStatus(Constant.HISTORY_STATUS_DELETE);
		groupVO.setDept_cd(groupVO.getGroup_id());
		
		groupDao.groupHistoryWrite(groupVO);
			

		resultMap.put("result", Constant.RESULT_SUCCESS);
		return resultMap;
	}

	
	@Override
	public Map<String, Object> groupMove(GroupVO groupVO, SessionVO sessionVO) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		GroupDao groupDao = sqlSession.getMapper(GroupDao.class);
		FolderDao folderDao = sqlSession.getMapper(FolderDao.class);
		
		// ------------------------------------
		// 1. 그룹 이동. 
		// ------------------------------------
		groupDao.groupMove(groupVO);
		
		// ------------------------------------
		// 2. 부서 맵일 경우. 
		// ------------------------------------
		if (groupVO.getMap_id().equals(Constant.MAP_ID_DEPT)) {

			// ------------------------------------
			// 2-1. 부서 폴더 수정. 
			// ------------------------------------
			
			// 부서 폴더 ID 얻기
			String folder_id = CommonUtil.getChangedResourceIDByPrefix(groupVO.getGroup_id(), Constant.ID_PREFIX_FOLDER);
			
			// 상위 부서 폴더 ID 얻기
			String parent_id = CommonUtil.getChangedResourceIDByPrefix(groupVO.getParent_id(), Constant.ID_PREFIX_FOLDER);
			
			HashMap<String, Object> folderParam = new HashMap<String, Object>();
			folderParam.put("folder_id", folder_id);
	
			// 기존 폴더 정보 얻기
			FolderVO folderVO = folderDao.folderDetail(folderParam);
			
			if (folderVO != null) {
				// 부서 폴더 부모ID 변경
				folderVO.setParent_id(parent_id);
				folderVO.setUpdate_action(Constant.ACTION_MOVE);
				
				// 부서 폴더 수정
				folderDao.folderUpdate(folderVO);
			}
		}
		
		// ------------------------------------
		// 3. 히스토리 등록. 
		// ------------------------------------
		groupVO.setGroup_seq(commonService.commonNextVal(Constant.COUNTER_ID_GROUP_HT));
		groupVO.setStatus(Constant.HISTORY_STATUS_UPDATE);
		groupVO.setDept_cd(groupVO.getGroup_id());
		
		groupDao.groupHistoryWrite(groupVO);
		
		resultMap.put("result", Constant.RESULT_SUCCESS);
		return resultMap;
	}

	@Override
	public GroupVO groupWriteValid(HashMap<String, Object> map) throws Exception {
		GroupDao groupDao = sqlSession.getMapper(GroupDao.class);
		FolderDao folderDao = sqlSession.getMapper(FolderDao.class);
		
		// ----------------------------------
		// 1. GroupVO로 변환
		// ----------------------------------
		GroupVO groupVO = new GroupVO();
		groupVO.setGroup_name_ko(map.get("group_name_ko").toString());
		groupVO.setGroup_name_en(map.get("group_name_en").toString());
		groupVO.setGroup_status(map.get("group_status").toString());
		groupVO.setParent_id(map.get("parent_id").toString());
		groupVO.setSort_index(Integer.parseInt(map.get("sort_index").toString()));
		groupVO.setMap_id(map.get("map_id").toString());
		
		// ----------------------------------
		// 2. 유효성 체크
		// ----------------------------------
		if (groupVO.getMap_id().equals("") || groupVO.getGroup_name_ko().equals("") || groupVO.getGroup_status().equals("")
				|| groupVO.getParent_id().equals("") || groupVO.getMap_id().equals("")) {
			throw processException("common.required.error");
		}
		
		// ----------------------------------
		// 3. 동일한 상위 그룹 하위에 동일한 이름의 그룹이 있는지 확인한다.
		// ----------------------------------
		if (groupDao.groupIsExistByGroupNameAndParentIdList(groupVO) > 0) {
			throw processException("groupName.duplication.error");
		}
		
		// ----------------------------------
		// 4. 동일한 상위 폴더 하위에 동일한 이름의 폴더가 있는지 확인한다
		// ----------------------------------
		FolderVO folderVO = new FolderVO();
		folderVO.setFolder_name_ko(groupVO.getGroup_name_ko());
		folderVO.setParent_id(CommonUtil.getChangedResourceIDByPrefix(groupVO.getParent_id(), Constant.ID_PREFIX_FOLDER));
		
		if (folderDao.folderIsExistByFolderNameAndParentID(folderVO) > 0) {
			throw processException("folderName.duplication.error");
		}
		
		return groupVO;
	}

	@Override
	public GroupVO groupUpdateValid(HashMap<String, Object> map) throws Exception {
		GroupDao groupDao = sqlSession.getMapper(GroupDao.class);
		FolderDao folderDao = sqlSession.getMapper(FolderDao.class);
				
		// ----------------------------------
		// 1. GroupVO로 변환
		// ----------------------------------
		GroupVO groupVO = new GroupVO();
		groupVO.setGroup_id(StringUtil.getMapString(map, "group_id"));
		groupVO.setGroup_name_ko(StringUtil.getMapString(map, "group_name_ko"));
		groupVO.setGroup_name_en(StringUtil.getMapString(map, "group_name_en"));
		groupVO.setGroup_status(StringUtil.getMapString(map, "group_status"));
		groupVO.setParent_id(StringUtil.getMapString(map, "parent_id"));
		groupVO.setSort_index(StringUtil.getMapInteger(map, "sort_index"));
		groupVO.setMap_id(StringUtil.getMapString(map, "map_id"));
		
		String[] userList = map.get("user_id_list").toString().split(",");
		if (userList.length > 0) {
			groupVO.setUser_id_list(Arrays.asList(userList));
		}
		
		// ----------------------------------
		// 2. 유효성 체크
		// ----------------------------------
		if (groupVO.getGroup_id().equals("") || groupVO.getMap_id().equals("") || groupVO.getGroup_name_ko().equals("") 
				|| groupVO.getGroup_status().equals("") || groupVO.getParent_id().equals("") ) {
			throw processException("common.required.error");
		}
		
		// ----------------------------------
		// 3. 부서 이동의 경우 추가 유효성 체크 
		// ----------------------------------
		
		if (StringUtil.getMapString(map, "is_changed_parent").equals(Constant.TRUE)) {
			
			// ----------------------------------
			// 3-1. 동일한 상위 그룹 하위에 동일한 그룹이 있는지 확인한다.
			// ----------------------------------
			if (groupDao.groupIsExistByGroupNameAndParentIdList(groupVO) > 0) {
				throw processException("groupName.duplication.error");
			}
			
			// ----------------------------------
			// 3-2. 동일한 상위 폴더 하위에 동일한 이름의 폴더가 있는지 확인한다.
			// ----------------------------------
			FolderVO folderVO = new FolderVO();
			folderVO.setFolder_name_ko(groupVO.getGroup_name_ko());
			folderVO.setParent_id(CommonUtil.getChangedResourceIDByPrefix(groupVO.getParent_id(), Constant.ID_PREFIX_FOLDER));
			
			if (folderDao.folderIsExistByFolderNameAndParentID(folderVO) > 0) {
				throw processException("folderName.duplication.error");
			}
		}
		return groupVO;
	}

	@Override
	public GroupVO groupDeleteValid(HashMap<String, Object> map) throws Exception {
		GroupDao groupDao = sqlSession.getMapper(GroupDao.class);
		FolderDao folderDao = sqlSession.getMapper(FolderDao.class);
		UserDao userDao = sqlSession.getMapper(UserDao.class);
		DocumentDao documentDao = sqlSession.getMapper(DocumentDao.class);
		AclDao aclDao = sqlSession.getMapper(AclDao.class);
		
		HashMap<String, Object> groupInfo = new HashMap<String, Object>();
		groupInfo.put("groupId", map.get("group_id"));
		groupInfo.put("parentId", map.get("group_id")); // 하위 그룹 존재 여부 확인을 위해 group_id를 parentId로 사용함
		groupInfo.put("accessor_id", map.get("group_id")); // ACL Item 조회를 위해 사용
		groupInfo.put("folder_id", CommonUtil.getChangedResourceIDByPrefix(StringUtil.getMapString(map, "group_id"), Constant.ID_PREFIX_FOLDER));
		
		// ---------------------------------------------------
		// 1. 소속구성원이 존재하는지 확인한다.
		// ---------------------------------------------------
		List<UserVO> userList = userDao.groupUserList(groupInfo);
		
		if (userList.size() > 0) {
			throw processException("group.fail.user.exists");
		}
		
		// ---------------------------------------------------
		// 2. 직계 하위 그룹이 존재하는지 확인한다.
		// ---------------------------------------------------
		List<GroupVO> groupList = groupDao.childGroupList(groupInfo);
		
		if (groupList.size() > 0) {
			throw processException("group.fail.child.group.exists");
		}
		
		// ---------------------------------------------------
		// 3. 그룹이 접근자로 지정된 ACLITEM이 있는지 확인한다.
		// ---------------------------------------------------
		FolderVO groupFolder = folderDao.folderDetail(groupInfo);
		groupInfo.put("acl_id", groupFolder.getAcl_id());

		if (aclDao.aclItemCountByAccessorId(groupInfo) > 0) {
			throw processException("group.fail.bind.accessors");
		}
		
		// ---------------------------------------------------
		// 4. 부서 폴더의 하위 폴더가 존재하는지 확인한다.
		// ---------------------------------------------------
		if (folderDao.existChildFolder(groupInfo) > 0) {
			throw processException("group.fail.child.exists");
		}
		
		// ---------------------------------------------------
		// 5. 부서 폴더에 문서가 존재하는지 확인한다.
		// ---------------------------------------------------
		if (documentDao.getDocumentCountByFolderId(groupInfo) > 0) {
			throw processException("group.fail.document.exist");
		}
		
		// ---------------------------------------------------
		// 6. 부서를 관리부서로 설정한 유저가 있는지 확인한다
		// ---------------------------------------------------
		if (groupDao.groupManagerCnt(groupInfo) > 0) {
			throw processException("group.fail.manager.exist");
		}
		
		// 그룹 정보를 가져온다
		GroupVO groupVO = groupDao.groupDetail(StringUtil.getMapString(map, "group_id"));
		
		return groupVO;
	}

	@Override
	public GroupVO groupMoveValid(HashMap<String, Object> map) throws Exception {
		
		GroupDao groupDao = sqlSession.getMapper(GroupDao.class);
		FolderDao folderDao = sqlSession.getMapper(FolderDao.class);
		
		// ----------------------------------
		// 1. GroupVO로 변환
		// ----------------------------------
		
		// 기존 그룹 정보를 조회한다
		GroupVO groupVO = groupDao.groupDetail(StringUtil.getMapString(map, "group_id"));
		FolderVO folderVO = new FolderVO();
		
		// 기존 그룹 정보가 있을 경우에만 진행한다
		if (groupVO != null) {
			
			// 기존 그룹정보를 변경된 정보로 수정한다 
			groupVO.setParent_id(StringUtil.getMapString(map, "parent_id"));
			groupVO.setMap_id(StringUtil.getMapString(map, "map_id"));
			
			// ----------------------------------
			// 2. 동일한 상위 그룹 하위에 동일한 그룹이 있는지 확인한다.
			// ----------------------------------
			if (groupDao.groupIsExistByGroupNameAndParentIdList(groupVO) > 0) {
				throw processException("groupName.duplication.error");
			}
			
			// ----------------------------------
			// 3. 동일한 상위 폴더 하위에 동일한 이름의 폴더가 있는지 확인한다.
			// ----------------------------------
			folderVO.setFolder_name_ko(groupVO.getGroup_name_ko());
			folderVO.setParent_id(CommonUtil.getChangedResourceIDByPrefix(groupVO.getParent_id(), Constant.ID_PREFIX_FOLDER));
			
			if (folderDao.folderIsExistByFolderNameAndParentID(folderVO) > 0) {
				throw processException("folderName.duplication.error");
			}
		
		}
		return groupVO;
	}
	
	@Override
	public List<GroupVO> groupExcelList(String fileName) throws Exception	{
		
		GroupDao groupDao = sqlSession.getMapper(GroupDao.class);
		FolderDao folderDao = sqlSession.getMapper(FolderDao.class);
		
		HashMap<String,Object> param = new HashMap<String,Object>();
		
		List<GroupVO> ret = new ArrayList<GroupVO>();		
		@SuppressWarnings("rawtypes")
		List rowList = new ArrayList();
		
		String tmpLine = "";
		String grpName = "";
		String grpEnName = "";
		String grpSuName = ""; 
		
		boolean isDup = false;
		
		XlsUtil excelUtil = new XlsUtil();
		
		rowList = excelUtil.getExcelList(fileName);
		
		for(int i=0; i < rowList.size(); i++){
			
			tmpLine = rowList.get(i).toString().replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("[ ]", "");
			
			String[] grpList = tmpLine.toString().split(",");
						
			if(grpList.length != 3) {		continue;		}
			
			grpName = grpList[0];
			grpEnName = grpList[1];
			grpSuName = grpList[2];
						
			if(StringUtil.isEmpty(grpName) || StringUtil.isEmpty(grpSuName) ){
				continue;				
			}

			// 0.그룹명이 푤더명으로 적절하지 체크한다.
			if(PatternUtil.webfolderCheck(grpName))	{
				continue;
			}
			
			// 1. 상위그룹명이 존재하는지 체크한다.(상위그룹명이 2개이상이면 SKIP)			
			param.put("map_id",Constant.MAP_ID_DEPT);
			param.put("grpNm",grpSuName);			
			
			// 상위그룹 갯수 체크 
			int result = groupDao.chkGroupCnt(param);			
			if(result != 1) { continue; }
			
			// 상위그룹정보 가져오기
			GroupVO suGroup = groupDao.chkGroupName(param);			
			if(suGroup == null) {	continue;		}
			
			// 2.그룹명이 존재하는지 체크한다.			
			param.put("grpNm",grpName);
			GroupVO cuGroup = groupDao.chkGroupName(param);			
			if(cuGroup != null) {	continue;	}
			
			// 3. 신규입력인 경우 객체를 생성하여 초기값을 담는다. 
			GroupVO groupVO = new GroupVO();
			groupVO.setGroup_name_ko(grpName);
			groupVO.setGroup_name_en(grpEnName);
			groupVO.setParent_id(suGroup.getGroup_id());
			groupVO.setMap_id(Constant.MAP_ID_DEPT);
			
			// 4 동일한 상위 그룹 하위에 동일한 이름의 그룹이 있는지 확인한다.
			if (groupDao.groupIsExistByGroupNameAndParentIdList(groupVO) > 0) {
				continue;	
			}
			
			// 5. 동일한 상위 폴더 하위에 동일한 이름의 폴더가 있는지 확인한다
			FolderVO folderVO = new FolderVO();
			folderVO.setFolder_name_ko(groupVO.getGroup_name_ko());
			folderVO.setParent_id(CommonUtil.getChangedResourceIDByPrefix(groupVO.getParent_id(), Constant.ID_PREFIX_FOLDER));
			if (folderDao.folderIsExistByFolderNameAndParentID(folderVO) > 0) {
				continue;	
			}
			
			// 6. 중복제거 로직 처리
			if(ret != null && ret.size() > 0 )	{
				for(GroupVO vo : ret) {
					if(vo.getGroup_name_ko().equals(groupVO.getGroup_name_ko())) {
						isDup = true;
						break;
					}
				}
				
			}
			
			// 7. 정상적인 데이터인 경우 
			if(!isDup) { ret.add(groupVO); }
			
		}
		
		return ret;
	}
	
}

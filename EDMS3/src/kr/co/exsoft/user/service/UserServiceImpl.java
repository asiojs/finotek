package kr.co.exsoft.user.service;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.io.FileNotFoundException;

import javax.servlet.http.HttpServletRequest;

import kr.co.exsoft.permission.dao.AclDao;
import kr.co.exsoft.user.vo.ConnectLogVO;
import kr.co.exsoft.user.vo.GroupVO;
import kr.co.exsoft.user.vo.QuickMenuVO;
import kr.co.exsoft.user.vo.UserVO;
import kr.co.exsoft.user.dao.UserDao;
import kr.co.exsoft.user.dao.GroupDao;
import net.sf.json.JSONArray;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.apache.commons.collections.map.CaseInsensitiveMap;

import kr.co.exsoft.common.service.CommonService;
import kr.co.exsoft.document.dao.DocumentDao;
import kr.co.exsoft.eframework.configuration.Constant;
import kr.co.exsoft.eframework.library.ExsoftAbstractServiceImpl;
import kr.co.exsoft.eframework.util.ARIAUtil;
import kr.co.exsoft.eframework.util.Converter;
import kr.co.exsoft.eframework.util.PatternUtil;
import kr.co.exsoft.eframework.util.StringUtil;
import kr.co.exsoft.eframework.util.XlsUtil;
import kr.co.exsoft.folder.dao.FolderDao;
import kr.co.exsoft.folder.vo.FavoriteFolderVO;
import kr.co.exsoft.folder.vo.FolderVO;

/**
 * User 서비스 구현 부분
 * @author 패키지 개발팀
 * @since 2014.07.17
 * @version 3.0
 *
 */
@Service("userService")
public class UserServiceImpl  extends ExsoftAbstractServiceImpl implements UserService {

	@Autowired
	@Qualifier("sqlSession")
	private SqlSession sqlSession;
				

	@Autowired
	@Qualifier("sqlSessionBatch")
	private SqlSession sqlSessionBatch;
	
	@Autowired
	private CommonService commonService;
	
	@Autowired
	private MessageSource messageSource;
	
	
	@Override
	public UserVO UserDetail(HashMap<String,Object> map) throws Exception{
		
		UserDao userDao = sqlSession.getMapper(UserDao.class);
		
		UserVO ret = userDao.userDetail(map);
		
		if (ret == null)	
			throw processException("result.nodata.msg");
		
		return ret;
	}
	
	@Override
	public UserVO userGroupDetail(HashMap<String, Object> map) throws Exception {
		
		UserDao userDao = sqlSession.getMapper(UserDao.class);
		
		UserVO ret = userDao.userGroupDetail(map);
		
		if (ret == null)
			throw processException("result.nodata.msg");
		
		return ret;
	}

	@Override
	public int userWriteTx(UserVO userVO,HashMap<String,Object> map) throws Exception,FileNotFoundException {
		
		int ret = 0;
		
		UserDao userDao = sqlSession.getMapper(UserDao.class);
		GroupDao groupDao = sqlSession.getMapper(GroupDao.class);
		
		ret = userDao.userDetailWrite(userVO);

		/***********************************************************************
		* ret 정의 [1:success,0:fail,1보다큰경우 TooManyAffectedException
		************************************************************************/		
		if(ret != 1)	throw processException("result.insert.fail");

		ret = groupDao.groupedWrite(map);
		
		if(ret != 1)	throw processException("result.insert.fail");

		/**********************************************
		 * 강제로 FileNotFoundException 적용시 
		**********************************************/
		//if(ret ==1)	{	throw new FileNotFoundException();	}
		 
		
		return ret;
	}
	
	@Override
	public UserVO userLogin(UserVO userVO,HttpServletRequest request) throws Exception {
		
		UserVO resultVO = new UserVO();
		
		UserDao userDao = sqlSession.getMapper(UserDao.class);		

		// 1. 사용자 로그인 처리				
		resultVO = userDao.userLogin(userVO);
		
		// 1.0 사용자 정보 체크
		if (resultVO == null)	{
			throw processException("login.fail.id.error");
		}

		return resultVO;
	}

	@Override
	public void userLogFailWrite(UserVO userVO,HashMap<String,Object> param,HttpServletRequest request) throws Exception {
		
		UserDao userDao = sqlSession.getMapper(UserDao.class);		
		ConnectLogVO connectLogVO = new ConnectLogVO();
		
		// XR_CONNECT_LOG 시퀀스 값 가져오기.
		connectLogVO.setConnect_log_seq(commonService.commonNextVal(Constant.COUNTER_ID_CONNECT_LOG));
		connectLogVO.setUser_id(userVO.getUser_id());
		connectLogVO.setUser_nm(userVO.getUser_name_ko());
		connectLogVO.setGroup_id(userVO.getGroup_id());
		connectLogVO.setGroup_nm(userVO.getGroup_nm());
		connectLogVO.setLogin_type(param.get("login_type").toString());
		connectLogVO.setConnect_type(param.get("connect_type").toString());			
		connectLogVO.setConnect_ip(request.getRemoteAddr());
		connectLogVO.setCert_yn(param.get("cert_yn").toString());
		connectLogVO.setError_cd(param.get("error_cd").toString());
		connectLogVO.setError_content(param.get("error_content").toString());
		userDao.connectLogWrite(connectLogVO);
		
	}

	
	@Override
	public Map<String, Object> groupUserList(HashMap<String,Object> param) throws Exception {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		UserDao userDao = sqlSession.getMapper(UserDao.class);
		List<UserVO> ret = new ArrayList<UserVO>();
		
		ret = userDao.groupUserList(param);
		
		resultMap.put("records",ret.size());
		resultMap.put("list",ret);
		resultMap.put("result",Constant.RESULT_TRUE);		
		
		return resultMap;
	}

	@Override
	public Map<String, Object> userGroupSearch(HashMap<String,Object> param) throws Exception{
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		UserDao userDao = sqlSession.getMapper(UserDao.class);
		List<CaseInsensitiveMap> ret = new ArrayList<CaseInsensitiveMap>();
		
		ret = userDao.userGroupSearch(param);
		
		resultMap.put("records",ret.size());
		resultMap.put("list",ret);
		resultMap.put("result",Constant.RESULT_TRUE);		
		
		return resultMap;
	}

	@Override
	public Map<String, Object> searchUserList(HashMap<String, Object> param) throws Exception {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		UserDao userDao = sqlSession.getMapper(UserDao.class);
		List<UserVO> ret = new ArrayList<UserVO>();
		
		// 검색조건 추가 start
		String search_type = param.get("search_type") != null ? param.get("search_type").toString() : "";
		
		if(search_type != "") {
			if("user_name".equals(search_type)) {
				param.put("userName", param.get("search_txt").toString());
				param.put("groupId", "");
			} else {
				param.put("groupName", param.get("search_txt").toString());
				param.put("groupId", "");
			}
		}
		// 검색조건 추가 end
		
		ret = userDao.searchUserList(param);
		
		resultMap.put("records",ret.size());
		resultMap.put("list",ret);
		resultMap.put("result",Constant.RESULT_TRUE);		
		
		return resultMap;
	}


	@Override
	public Map<String, Object> userWrite(UserVO userVO, HashMap<String, Object> groupedInfo) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		UserDao userDao = sqlSession.getMapper(UserDao.class);
		GroupDao groupDao = sqlSession.getMapper(GroupDao.class);
		FolderDao folderDao = sqlSession.getMapper(FolderDao.class);
		
		// 유저 기본 정보 등록
		if (userDao.userDefaultWrite(userVO) != 1) {
			throw processException("result.insert.fail");
		}
		
		// 유저 상세 정보 등록
		if (userDao.userDetailWrite(userVO) != 1) {
			throw processException("result.insert.fail");
		}
		
		// 유저 config (사용자 개인 환경설정) 정보 등록
		if(userDao.userConfigWrite(userVO) != 1) {
			throw processException("result.insert.fail");
		}
		
		// 부서 - 유저 맵핑 정보 등록
		// 기본부서로 설정함
		groupedInfo.put("is_default", "T");
		if (groupDao.groupedWrite(groupedInfo) != 1) {
			throw processException("result.insert.fail");
		}
		
		// Folder VO 구성
		FolderVO folderVO = new FolderVO();
		folderVO.setFolder_id(userVO.getUser_id());
		folderVO.setFolder_name_ko(userVO.getUser_name_ko());
		folderVO.setFolder_name_en(userVO.getUser_name_en());
//		folderVO.setFolder_name_zh(userVO.getUser_name_zh());
		folderVO.setFolder_name_ja(userVO.getUser_name_ja());
		folderVO.setParent_id(userVO.getUser_id());
		folderVO.setFolder_type(Constant.FOLDER_TYPE_DOCUMENT);
		folderVO.setSort_index(0);
		folderVO.setFolder_status(Constant.FOLDER_STATUS_CREATE);
		folderVO.setMap_id(Constant.MAP_ID_MYPAGE);
		folderVO.setAcl_id(Constant.ACL_ID_OWNER);
		folderVO.setCreator_id(userVO.getUser_id());
		folderVO.setCreator_name(userVO.getUser_name_ko());
		folderVO.setIs_type(Constant.FOLDER_TYPE_ALL_TYPE);
		
		// 유저 개인 폴더 등록
		folderDao.folderWrite(folderVO);
		
		// FavoriteFolder VO 구성 (사용자 기본 즐겨찾기 폴더)
		FavoriteFolderVO favoriteFolderVO = new FavoriteFolderVO();
//		favoriteFolderVO.setFolder_id(CommonUtil.getStringID(Constant.ID_PREFIX_FOLDER, commonService.commonNextVal(Constant.COUNTER_ID_FILE)));
		favoriteFolderVO.setFolder_id(userVO.getUser_id());
		favoriteFolderVO.setFavorite_nm("즐겨찾기");
		favoriteFolderVO.setIs_virtual("Y");
		favoriteFolderVO.setSorts(0);
		favoriteFolderVO.setParent_folder_id(userVO.getUser_id());
		favoriteFolderVO.setUser_id(userVO.getUser_id());
		
		// 유저 기본 즐겨찾기 폴더 등록
		folderDao.writeFavoriteFolder(favoriteFolderVO);
		
		// 히스토리 등록
		userVO.setUser_seq(commonService.commonNextVal(Constant.COUNTER_ID_USER_HT));
		userVO.setStatus(Constant.HISTORY_STATUS_CREATE);
		if (userDao.userHistoryWrite(userVO) != 1) {
			throw processException("result.insert.fail");
		}
		
		resultMap.put("result", Constant.RESULT_SUCCESS);
		
		return resultMap;
	}

	@Override
	public Map<String, Object> userUpdate(List<UserVO> userList) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		UserDao userDao = sqlSessionBatch.getMapper(UserDao.class);
		
		for (UserVO userVO : userList) {
			// 기본정보 수정
			userDao.userUpdate(userVO);
			
			// 상세정보 수정
			userDao.userDetailUpdate(userVO);
			
			// 스토리지 할당량 변경
			userDao.updateUserStorageQuota(userVO);
			
			// 히스토리 등록
			userVO.setUser_seq(commonService.commonNextVal(Constant.COUNTER_ID_USER_HT));
			userVO.setStatus(Constant.HISTORY_STATUS_UPDATE);
			userDao.userHistoryWrite(userVO);
		}
		
		resultMap.put("result", Constant.RESULT_SUCCESS);
		return resultMap;
	}
	
	@Override
	public Map<String, Object> userDelete(List<UserVO> userList) throws Exception {
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		HashMap<String, Object> userInfo = new HashMap<String, Object>();
		UserDao userDao = sqlSession.getMapper(UserDao.class);
		GroupDao groupDao = sqlSession.getMapper(GroupDao.class);
		FolderDao folderDao = sqlSession.getMapper(FolderDao.class);
		DocumentDao documentDao = sqlSession.getMapper(DocumentDao.class);
		
		for (UserVO userVO : userList) {
			userInfo.put("user_id", userVO.getUser_id());
			userInfo.put("folder_id", userVO.getUser_id());

			// XR_USER 삭제
			userDao.userDelete(userInfo);
			
			// XR_USER_DT 삭제
			userDao.userDetailDelete(userInfo);
			
			// XR_USER_CONFIG 삭제
			userDao.userConfigDelete(userInfo);
			
			// XR_GROUPED 삭제
			groupDao.groupedDelete(userInfo);
			
			// XR_FOLDER 삭제
			folderDao.folderDelete(userInfo);
			
			// XR_FAVORITE_FOLDER 삭제
			folderDao.deleteFavoriteFolderOfUser(userInfo);
			
			// XR_FAVORITE_DOC 삭제
			documentDao.deleteFavoriteDocOfUser(userInfo);
			
			// 히스토리 등록
			userVO.setUser_seq(commonService.commonNextVal(Constant.COUNTER_ID_USER_HT));
			userVO.setStatus(Constant.HISTORY_STATUS_DELETE);
			userDao.userHistoryWrite(userVO);
		}
		
		resultMap.put("result", Constant.RESULT_SUCCESS);
		return resultMap;
	}

	@Override
	public Map<String, Object> userGroupedUpdate(List<UserVO> userList) throws Exception {
		GroupDao groupDao = sqlSession.getMapper(GroupDao.class);
		UserDao userDao = sqlSession.getMapper(UserDao.class);
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		HashMap<String, Object> param = new HashMap<String, Object>();
		
		for(UserVO userVO : userList) {
			param.put("group_id", userVO.getGroup_id());
			param.put("manage_group", userVO.getManage_group());
			param.put("user_id", userVO.getUser_id());
			groupDao.groupedUpdate(param);
			userDao.updateManageGroup(param);
			
			// 히스토리 등록
			userVO.setUser_seq(commonService.commonNextVal(Constant.COUNTER_ID_USER_HT));
			userVO.setStatus(Constant.HISTORY_STATUS_UPDATE);
			if (userDao.userHistoryWrite(userVO) != 1) {
				throw processException("result.insert.fail");
			}
		}
		resultMap.put("result", Constant.RESULT_SUCCESS);
		return resultMap;
	}
	
	
	@Override
	public UserVO userWriteValid(HashMap<String, Object> map) throws Exception {
		UserDao userDao = sqlSession.getMapper(UserDao.class);
		
		
		// UserVO로 변환
		UserVO userVO = new UserVO();
		userVO.setUser_id(StringUtil.getMapString(map, "user_id"));
		userVO.setEmp_no(StringUtil.getMapString(map, "user_id"));
		userVO.setUser_pass(ARIAUtil.ariaEncrypt(userVO.getUser_id(), userVO.getUser_id()));
		userVO.setUser_name_ko(StringUtil.getMapString(map, "user_name_ko"));
		userVO.setUser_name_en(StringUtil.getMapString(map, "user_name_en"));
		userVO.setUser_name_zh(StringUtil.getMapString(map, "user_name_zh"));
		userVO.setUser_name_ja(StringUtil.getMapString(map, "user_name_ja"));
		userVO.setGroup_id(StringUtil.getMapString(map, "group_id"));
		userVO.setGroup_nm(StringUtil.getMapString(map, "group_nm"));
		userVO.setPosition(StringUtil.getMapString(map, "position"));
		userVO.setJobtitle(StringUtil.getMapString(map, "jobtitle"));
		userVO.setRole_id(StringUtil.getMapString(map, "role_id"));
		userVO.setTelephone(StringUtil.getMapString(map, "telephone"));
		userVO.setEmail(StringUtil.getMapString(map, "email"));
		userVO.setUser_status(StringUtil.getMapString(map, "user_status"));
		userVO.setManage_group(StringUtil.getMapString(map, "manage_group"));
		userVO.setPage_size("15");
		userVO.setShare_name("MYPAGE,MYDEPT");
		
		// 최소 입력값 확인
		if (userVO.getUser_id().equals("") || userVO.getUser_name_ko().equals("") || userVO.getGroup_id().equals("")) {
			throw processException("common.required.error");
		}
		
		// 이미 아이디가 존재하는지 확인
		HashMap<String, Object> searchOptions = new HashMap<String, Object>();
		searchOptions.put("strIndex", "user_id");
		searchOptions.put("strKeyword", userVO.getUser_id());
		if (userDao.userExists(searchOptions) > 0) {
			throw processException("user.fail.regist.exist.id");
		}
		
		return userVO;
	}

	@Override
	public UserVO userUpdateValid(HashMap<String, Object> map) throws Exception {
		UserDao userDao = sqlSession.getMapper(UserDao.class);
		
		UserVO userVO = userDao.userGroupDetail(map);
		
		if (userVO == null) {
			throw processException("common.required.error");
		}
		
		userVO.setUser_name_ko(StringUtil.getMapString(map, "user_name_ko"));
		userVO.setUser_name_en(StringUtil.getMapString(map, "user_name_en"));
		userVO.setUser_name_zh(StringUtil.getMapString(map, "user_name_zh"));
		userVO.setUser_name_ja(StringUtil.getMapString(map, "user_name_ja"));
		userVO.setTelephone(StringUtil.getMapString(map, "telephone"));
		userVO.setEmail(StringUtil.getMapString(map, "email"));
		userVO.setPosition(StringUtil.getMapString(map, "position"));
		userVO.setJobtitle(StringUtil.getMapString(map, "jobtitle"));
		userVO.setRole_id(StringUtil.getMapString(map, "role_id"));
		userVO.setUser_status(StringUtil.getMapString(map, "user_status"));
		userVO.setManage_group(StringUtil.getMapString(map, "manage_group"));
		userVO.setStorage_quota(StringUtil.getMapLong(map, "storage_quota"));
		
		long getQuota = StringUtil.getMapLong(map, "storage_quota");
		long savedUsage = userVO.getStorage_usage();
		
		// 최소 입력값 확인
		if (userVO.getUser_id().equals("") || userVO.getUser_name_ko().equals("") || userVO.getGroup_id().equals("")) {
			throw processException("common.required.error");
		}
		
		// 스토리지 할당량 확인
		if( (getQuota > -1) && (getQuota < savedUsage) ){
			throw processException("storage.quota.set.error");
		}
		
		return userVO;
	}
	
	@Override
	public List<UserVO> userDeleteValid(HashMap<String, Object> map) throws Exception {
		List<UserVO> userList = new ArrayList<UserVO>();
		UserDao userDao = sqlSession.getMapper(UserDao.class);
		DocumentDao documentDao = sqlSession.getMapper(DocumentDao.class);
		FolderDao folderDao = sqlSession.getMapper(FolderDao.class);
		AclDao aclDao = sqlSession.getMapper(AclDao.class);
		
		HashMap<String, Object> searchOption = new HashMap<String, Object>();
		
		
		// validation
		String[] userIdList = StringUtil.getMapString(map, "userIdList").split(",");
		
		if (userIdList.length == 0) {
			throw processException("common.required.error");
		}
		
		for (String userId : userIdList) {
			searchOption.put("user_id", userId);
			UserVO userVO = userDao.userGroupDetail(searchOption);

			// 1. 사용자가 만든 문서가 있는지 확인
			if (documentDao.documentCountByCreatorId(userVO.getUser_id()) > 0) {
				throw processException("user.fail.have.created.doc");
			}
			
			// 2. 사용자가 소유한 문서가 있는지 확인
			if (documentDao.documentCountByOwnerId(userVO.getUser_id()) > 0) {
				throw processException("user.fail.have.owner.doc");
			}
			
			// 3. 사용자가 생성한 폴더가 있는지 확인
			if (folderDao.folderCountByCreatorId(userVO.getUser_id()) > 0) {
				throw processException("user.fail.have.created.folder");
			}
			
			// 4. 사용자가 생성한 ACL이 있는지 확인
			if (aclDao.aclCountByCreatorId(Converter.objectToHashMapAsCustomKey(userVO, "user_id", "creator_id")) > 0) {
				throw processException("user.fail.have.created.acl");
			}
			
			// 5. 사용자가 접근자로 지정된 ACL이 있는지 확인
			if (aclDao.aclItemCountByAccessorId(Converter.objectToHashMapAsCustomKey(userVO, "user_id", "accessor_id")) > 0) {
				throw processException("user.fail.have.accessor.acl");
			}
			
			userList.add(userVO);
		}
		
		return userList;
	}
	
	@Override
	public List<UserVO> userPassResetValid(HashMap<String, Object> map) throws Exception {
		List<UserVO> userList = new ArrayList<UserVO>();
		UserDao userDao = sqlSession.getMapper(UserDao.class);
		HashMap<String, Object> searchOption = new HashMap<String, Object>();
		
		// validation
		String[] userIdList = StringUtil.getMapString(map, "userIdList").split(",");
		
		if (userIdList.length == 0) {
			throw processException("common.required.error");
		}
		
		for (String userId : userIdList) {
			searchOption.put("user_id", userId);
			UserVO userVO = userDao.userGroupDetail(searchOption);
			userVO.setUser_pass(ARIAUtil.ariaEncrypt(userId, userId));

			userList.add(userVO);
		}
		
		return userList;
	}
	
	@Override
	public List<UserVO> userStatusUpdateValid(HashMap<String, Object> map) throws Exception {
		List<UserVO> userList = new ArrayList<UserVO>();
		UserDao userDao = sqlSession.getMapper(UserDao.class);
		HashMap<String, Object> searchOption = new HashMap<String, Object>();
		
		// validation
		String[] userIdList = StringUtil.getMapString(map, "userIdList").split(",");
		String userStatus = StringUtil.getMapString(map, "user_status");
		
		if (userIdList.length == 0 || userStatus.length() == 0) {
			throw processException("common.required.error");
		}
		
		for (String userId : userIdList) {
			searchOption.put("user_id", userId);
			UserVO userVO = userDao.userGroupDetail(searchOption);
			
			if (userVO == null) {
				throw processException("common.required.error");
			}

			userVO.setUser_status(userStatus);
			userList.add(userVO);
		}
		
		
		
		return userList;
	}
	
	@Override
	public List<UserVO> userGroupedUpdateValid(HashMap<String, Object> map) throws Exception {
		UserDao userDao = sqlSession.getMapper(UserDao.class);
		HashMap<String, Object> searchOption = new HashMap<String, Object>();
		List<UserVO> userList = new ArrayList<UserVO>();
		
		// validation
		String[] userIdList = StringUtil.getMapString(map, "userIdList").split(",");
		String groupId = StringUtil.getMapString(map, "groupId");
		if (userIdList.length == 0 || groupId.length() == 0) {
			throw processException("common.required.error");
		}

		for (String userId : userIdList) {
			searchOption.put("user_id", userId);
			UserVO userVO = userDao.userGroupDetail(searchOption);
			
			if (userVO == null) {
				throw processException("common.required.error");
			}
			
			userVO.setGroup_id(groupId);
			userVO.setManage_group(groupId);
			userList.add(userVO);
		}
		
		return userList;
	}
	
	@Override
	public Map<String, Object> userConfig(HashMap<String, Object> map) throws Exception {
		
		UserDao userDao = sqlSession.getMapper(UserDao.class);
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		int result = 0 ;
	
		// 0. 입력파라미터 유효성 체크
		if(map.get(Constant.UPDATE_TYPE) == null) {
			throw processException("common.required.error");
		}
		
		String updateType = map.get(Constant.UPDATE_TYPE).toString();
		
		// 1. 패스워드 변경처리의 경우 		
		if(updateType.equals(Constant.CONFIG_TAB_PASSWD)) {
			if( map.get("user_id") == null || map.get("user_pass") == null) {
				throw processException("common.required.error");
			}
			
			// 1.1 사용자 기존 패스워드 정보 가져오기. :: parameter = user_id
			UserVO userVO = userDao.userDetail(map);		
			
			String current_pass = ARIAUtil.ariaDecrypt(userVO.getUser_pass(),userVO.getUser_id());
					
			// 1.2 현재비밀번호 일치여부
			if(!map.get("current_pass").toString().equals(current_pass)) {
				throw processException("user.fail.passwd.not.equal");				
			}
									
			map.put("user_pass", ARIAUtil.ariaEncrypt(map.get("user_pass").toString(),map.get("user_id").toString()));		
		}
		
		// 2. DB처리 : 각 항목별로 테이블명이 다름		
		switch(updateType)	{
			case Constant.CONFIG_TAB_MYINFO :
				map.put("tableName",Constant.XR_USER_DT);
				break;
			case Constant.CONFIG_TAB_PASSWD :
				map.put("tableName",Constant.XR_USER);
				break;
			case Constant.CONFIG_TAB_CONFIG :
				map.put("tableName",Constant.XR_USER_CONFIG);
				break;
		}
		
		// 3. 결과
		result = userDao.userConfig(map);
		if(result == 0)	{
			throw processException("common.system.error");
		}

		// 사용자명(영) 변경처리
		if(updateType.equals(Constant.CONFIG_TAB_MYINFO)) {
			map.put("updateType",Constant.CONFIG_USERNM);
			map.put("tableName",Constant.XR_USER);			
			result = userDao.userConfig(map);
			if(result == 0)	{
				throw processException("common.system.error");
			}
		}
		
		resultMap.put("result", Constant.RESULT_TRUE);
		return resultMap;
	}
	
	@Override
	public List<UserVO> userExcelList(String fileName) throws Exception {
	
		UserDao userDao = sqlSession.getMapper(UserDao.class);
		GroupDao groupDao = sqlSession.getMapper(GroupDao.class);

		HashMap<String,Object> param = new HashMap<String,Object>();
				
		List<UserVO> ret = new ArrayList<UserVO>();
		@SuppressWarnings("rawtypes")
		List rowList = new ArrayList();
		
		String tmpLine = "";
		String userId = "";
		String userNm = "";
		String userEnNm = "";
		String suGroupNm = "";
		String groupNm = "";
		String emailAddr = "";
		String telNo = "";
		String roleId = "";
		
		boolean isDup = false;
		
		XlsUtil excelUtil = new XlsUtil();
		
		rowList = excelUtil.getExcelList(fileName);
		
		for(int i=0; i < rowList.size(); i++){
			
			tmpLine = rowList.get(i).toString().replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("[ ]", "");
			
			String[] userList = tmpLine.toString().split(",");
			
			// (주) 유효성 체크에서 걸리는 항목는 등록 리스트에서 제외된다. 
			
			// 1.엑셀기본포맷 형식 체크
			if(userList.length != 8) {		continue;		}
			
			// 2. 셀 파라미터 할당처리
			userId = userList[0];
			userNm = userList[1];
			userEnNm = userList[2];
			suGroupNm = userList[3];
			groupNm = userList[4];
			emailAddr = userList[5];
			telNo = userList[6];
			roleId = userList[7];
			
			// 3. 필수입력함목 EMPTY 체크
			if(StringUtil.isEmpty(userId) || StringUtil.isEmpty(userNm) || StringUtil.isEmpty(suGroupNm) || StringUtil.isEmpty(groupNm) || StringUtil.isEmpty(roleId)  ){
				continue;				
			}
			
			// 3.1 사용자명과 사용자아이디 유효성 체크 : 자바 패턴
			if(PatternUtil.webfolderCheck(userNm) || !PatternUtil.userIdCheck(userId)) {
				continue;
			}
			
			// 4. ROLE Check
			if(!roleId.equals(Constant.USER_ROLE))		{
				continue;				
			}
			
			// 5.부서정보체크 (상위부서명/부서명)
			param.put("map_id", Constant.MAP_ID_DEPT);
			param.put("grpNm", groupNm);
			param.put("suGrpNm", suGroupNm);
			int result = groupDao.chkUserGroupCnt(param);
			if(result != 1)	{	continue;		}
			
			// 6. 사용자의 부서정보
			GroupVO groupVO = groupDao.chkUserGroupInfo(param);
			
			
			// 7. 사용자 객체를 생성한다. 필수입력값외에는 기본값으로 지정한다. :: 직위,상태,할당량/기본검색기간/언어/테마/등록일/SHARE_NAME/USER_TYPE
			UserVO userVO = new UserVO();			
			userVO.setUser_id(userId);
			userVO.setEmp_no(userId);
			userVO.setUser_name_ko(userNm);
			userVO.setUser_name_en(userEnNm);
			userVO.setGroup_id(groupVO.getGroup_id());
			userVO.setRole_id(roleId);			
			userVO.setEmail(emailAddr);
			userVO.setTelephone(telNo);
			userVO.setManage_group(groupVO.getGroup_id());
			userVO.setUser_pass(ARIAUtil.ariaEncrypt(userVO.getUser_id(),userVO.getUser_id()));
			userVO.setGroup_nm(groupVO.getGroup_name_ko());	
			
			// 8. 아이디 중복 체크
			param.clear();
			param.put("strIndex", "user_id");
			param.put("strKeyword", userVO.getUser_id());
			if (userDao.userExists(param) > 0) {
				continue;		
			}
			
			// 9. 중복 아이디 ROW 체크
			if(ret != null && ret.size() > 0 )	{
				
				for(UserVO vo : ret )		{
					
					if(vo.getUser_id().equals(userVO.getUser_id()))	{
						isDup = true;
						break;
					}					
				}
				
			}

			// 10. 정상적인 데이터인 경우
			if(!isDup) {	ret.add(userVO);	}
									
			isDup = false;
		}
		
		
		return ret;
	}
	
	@Override
	public UserVO userDetailInfo(HashMap<String, Object> map) throws Exception {
		
		UserDao userDao = sqlSession.getMapper(UserDao.class);
		UserVO userVO = userDao.userDetailInfo(map);

		return userVO;
		
	}
	
	@Override
	public Map<String, Object> quickMenuProc(HashMap<String, Object> map) throws Exception {
	
		UserDao userDao = sqlSession.getMapper(UserDao.class);
		
		List<HashMap<String,Object>> selectMenuList = new ArrayList<HashMap<String,Object>>();
		List<CaseInsensitiveMap> selMenuList = new ArrayList<CaseInsensitiveMap>();
		List<QuickMenuVO> quickMenuList = new ArrayList<QuickMenuVO>();
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		String menu_cds =  map.get("menu_cd") != null ? map.get("menu_cd").toString() : "";
		
		
		// 1. 입력파라미터 유효성 체크
		if(map.get(Constant.TYPE) == null) {
			throw processException("common.required.error");
		}
		
		// 2.TYPE별 데이터 처리
		if(map.get(Constant.TYPE).toString().equals(Constant.SELECT))		{		
			
			// 퀵메뉴 목록
			quickMenuList = userDao.quickMenuInfo(map);
			resultMap.put("quickMenuListCnt",quickMenuList.size());
			resultMap.put("quickMenuList",quickMenuList);
			
			// 선택한 퀵메뉴 목록
			selMenuList = userDao.userQuickMenu(map);
			resultMap.put("userSelectMenuCnt",selMenuList.size());
			for(CaseInsensitiveMap caseMap : selMenuList) {
				HashMap<String,Object> menuInfo = new HashMap<String, Object>();
				menuInfo.put("menu_cd",caseMap.get("menu_cd").toString());
				selectMenuList.add(menuInfo);
			}
			
			resultMap.put("userSelectMenu",selectMenuList);
			
		}else if(map.get(Constant.TYPE).toString().equals(Constant.UPDATE))		{		
		
			// JsonArray 객체 파싱 처리
			JSONArray jsonArray = JSONArray.fromObject(menu_cds);
						
			// 사용자 선택 퀵메뉴 초기화			
			userDao.quickMenuDelete(map);
									
			// 사용자 선택 퀵메뉴 입력처리
			if(jsonArray.size() > 0 ) {		
				 for(int j=0;j < jsonArray.size();j++)	{					 
					 map.put("menu_cd", jsonArray.getJSONObject(j).getString("menu_cd").toString());
					 userDao.quickMenuWrite(map);
				 }
			}
					
		}else {

			// 탑영역 퀵메뉴 정보 가져오기
			quickMenuList = userDao.topQuickMenu(map);
			resultMap.put("quickMenuListCnt",quickMenuList.size());
			resultMap.put("quickMenuList",quickMenuList);
			
		}

		resultMap.put("result", Constant.RESULT_TRUE);
		
		return resultMap;
	}
}

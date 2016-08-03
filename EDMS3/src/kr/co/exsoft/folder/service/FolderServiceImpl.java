package kr.co.exsoft.folder.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import kr.co.exsoft.common.service.CacheService;
import kr.co.exsoft.common.service.CommonService;
import kr.co.exsoft.common.vo.RecentlyObjectVO;
import kr.co.exsoft.common.vo.SessionVO;
import kr.co.exsoft.document.dao.DocumentDao;
import kr.co.exsoft.document.vo.DocumentVO;
import kr.co.exsoft.eframework.configuration.Constant;
import kr.co.exsoft.eframework.library.ExsoftAbstractServiceImpl;
import kr.co.exsoft.eframework.util.CommonUtil;
import kr.co.exsoft.eframework.util.ConfigData;
import kr.co.exsoft.eframework.util.StringUtil;
import kr.co.exsoft.folder.dao.FolderDao;
import kr.co.exsoft.folder.vo.FavoriteFolderVO;
import kr.co.exsoft.folder.vo.FolderVO;
import kr.co.exsoft.permission.dao.AclDao;

import org.apache.commons.collections.map.CaseInsensitiveMap;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * 폴더 서비스 구현 부분
 * @author 패키지 개발팀
 * @since 2014.07.17
 * @version 3.0
 *
 */
@Service("folderService")
public class FolderServiceImpl extends ExsoftAbstractServiceImpl implements FolderService {

	@Autowired
	@Qualifier("sqlSession")
	private SqlSession sqlSession;
	
	@Autowired
	@Qualifier("sqlSessionBatch")
	private SqlSession sqlSessionBatch;

	@Autowired
	private CommonService commonService;
	
	@Autowired
	private CacheService cacheService;
	
	private long delimiter = ConfigData.getLong("BATCH_LIMIT");
			
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 해당 폴더 문서의 acl_id를 변경 
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : updateDocumentACL_ID
	 * @param folder_id : 변경할 Folder_id
	 * @param acl_id : 변경할 acl_id2
	 * @throws Exception void
	 */
	private void updateDocumentACL_ID(String folder_id, String acl_id) throws Exception {
		
		DocumentDao documentDao = sqlSession.getMapper(DocumentDao.class);
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("folder_id", folder_id);
		
		// 1. folder_id 기준으로 acl 변경대상 문서 목록을 가져 온다. :: XR_DOCUMENT.IS_INHERIT_ACL = F
		List<DocumentVO> docList = documentDao.documentAclChangeListByFolderId(param);
		
		// 2. is_inherit_acl=F 문서에 대한 권한을 변경한다. 추가 접근자는 삭제 한다.
		int cnt = 0;
		List<DocumentVO> tempDocVO = new ArrayList<DocumentVO>();
		
		for(DocumentVO documentVO : docList) {
			
			documentVO.setAcl_id(acl_id);
			documentVO.setUpdate_action(Constant.ACTION_CHANGE_ACL_ID);
			tempDocVO.add(documentVO);
			
			if( cnt !=0 && ( cnt % delimiter == 0 ) ) {	
				for(DocumentVO updateDocVO : tempDocVO){
					documentDao.documentUpdate(updateDocVO);
				}
				tempDocVO.clear();
			} else if( cnt == (docList.size()-1)) {
				for(DocumentVO updateDocVO : tempDocVO){
					documentDao.documentUpdate(updateDocVO);
				}
				tempDocVO.clear();
			}
			
			cnt++;
		}
	}
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 역할에 따른 폴더 ACL 셋팅 
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : setFolderRoleAcl
	 * @param folderVo
	 * @param folder_menu_part
	 * @param folder_id
	 * @param map
	 * @throws Exception void
	 */
	private void setFolderRoleAcl(FolderVO folderVo, String folder_menu_part, String folder_id, HashMap<String, Object> map) throws Exception {
		
		// 폴더 메뉴 접근 권한 rule 적용
		if(folder_menu_part.equals(Constant.MENU_ALL)) {
			folderVo.setAcl_level(Constant.ACL_DELETE);
			folderVo.setAcl_create(Constant.T);
			folderVo.setAcl_changePermission(Constant.T);
		} else if(folder_menu_part.equals(Constant.MENU_GROUP) || folder_menu_part.equals(Constant.MENU_TEAM)) {
			if(cacheService.menuAuthByFolderID(folder_id, (String)map.get("manage_group_id"))){
				folderVo.setAcl_level(Constant.ACL_DELETE);
				folderVo.setAcl_create(Constant.T);
				folderVo.setAcl_changePermission(Constant.T);
			}
		}
	}
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 역할에 따른 문서 ACL 셋팅 
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : setDocumentRoleAcl
	 * @param folderVo
	 * @param document_menu_part
	 * @param folder_id
	 * @param map
	 * @throws Exception void
	 */
	private void setDocumentRoleAcl(FolderVO folderVo, String document_menu_part, String folder_id, HashMap<String, Object> map) throws Exception {
		
		// 문서 메뉴 접근 권한 rule 적용
		if(document_menu_part.equals(Constant.MENU_ALL)) {
			folderVo.setAcl_document_create(Constant.T);
		} else if(document_menu_part.equals(Constant.MENU_GROUP) || document_menu_part.equals(Constant.MENU_TEAM)) {
			if(cacheService.menuAuthByFolderID(folder_id, (String)map.get("manage_group_id"))){
				folderVo.setAcl_document_create(Constant.T);
			}
		} 
	}
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 하위 폴더 목록 가져오기 RECURSIVE
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : getLowLevelNode
	 * @param folder_id void
	 */
	private void getLowLevelNode(String folder_id, List<String> subFolderList, boolean is_current_dept) {
		
		FolderDao folderDao = sqlSession.getMapper(FolderDao.class);
	
		List<String> folderList  = new ArrayList<String>();
		
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("parent_id", folder_id);
		
		if(is_current_dept)
			param.put("folder_type", Constant.FOLDER_TYPE_DOCUMENT);

		folderList =  folderDao.childFolderIds(param);
		
		if(folderList.size() > 0 ) {
			
			for(String folderId : folderList)  {			
				subFolderList.add(folderId);
				getLowLevelNode(folderId,subFolderList, is_current_dept);
			}
			
		}else {
			subFolderList.add(folder_id);
		}
		
	}
	
	@Override
	public List<FolderVO> rootFolderList(HashMap<String, Object> map) throws Exception {
		
		FolderDao folderDao = sqlSession.getMapper(FolderDao.class);
		
		List<FolderVO> folderList = folderDao.rootFolderList(map);
		
		setFolderAcl(folderList, map);
		
		return folderList;
	}
	
	@Override
	public List<FolderVO> rootShareFolderList(HashMap<String, Object> map) throws Exception {
		
		FolderDao folderDao = sqlSession.getMapper(FolderDao.class);
		
		List<FolderVO> folderList = folderDao.rootShareFolderList(map);
		
		setFolderAcl(folderList, map);
		
		return folderList;
	}
	
	@Override
	public List<FavoriteFolderVO> rootFavoriteFolderList(HashMap<String, Object> map) throws Exception {
		
		FolderDao folderDao = sqlSession.getMapper(FolderDao.class);
		
		List<FavoriteFolderVO> folderList = folderDao.rootFavoriteFolderList(map);
		
		return folderList;
	}
	

	@Override
	public List<FavoriteFolderVO> childFavoriteFolderList(HashMap<String, Object> map) throws Exception {
		
		FolderDao folderDao = sqlSession.getMapper(FolderDao.class);

		List<FavoriteFolderVO> folderList = folderDao.childFavoriteFolderList(map);
		
		return folderList;
	}

	@Override
	public void writeFavoriteFolder(HashMap<String, Object> map) throws Exception {
		
		FolderDao folderDao = sqlSession.getMapper(FolderDao.class);
		List<FavoriteFolderVO> favoriteList = new ArrayList<FavoriteFolderVO>();
		HashMap<String, Object> param = new HashMap<String, Object>();
		HashMap<String, Object> countParam = new HashMap<String, Object>();
		
		// 1. 유효성 검사
		if (StringUtil.getMapString(map, "folder_id").equals("")
			|| StringUtil.getMapString(map, "user_id").equals("")
			|| StringUtil.getMapString(map, "target_folder_id").equals("")) {
			
			throw processException("common.required.error");
		}

		// 2. index 번호를 조회한다
		countParam.put("user_id", StringUtil.getMapString(map, "user_id"));
		countParam.put("parent_id", StringUtil.getMapString(map, "target_folder_id"));
		int index = folderDao.favoriteFolderChildCount(countParam);
		
		// 3. 사용자가 선택한 폴더를 추가한다
		FavoriteFolderVO favoriteFolderVO = new FavoriteFolderVO();
		favoriteFolderVO.setFolder_id(StringUtil.getMapString(map, "folder_id"));
		favoriteFolderVO.setUser_id(StringUtil.getMapString(map, "user_id"));
		favoriteFolderVO.setFavorite_nm(StringUtil.getMapString(map, "folder_nm"));
		favoriteFolderVO.setParent_folder_id(StringUtil.getMapString(map, "target_folder_id"));
		favoriteFolderVO.setIs_virtual(StringUtil.getMapString(map, "is_virtual", "N")); // 가상폴더 생성시에만 is_virtual 값을 전송하고있음
		favoriteFolderVO.setSorts(index);
		favoriteList.add(favoriteFolderVO);

		
		// 4. 하위 폴더 포함일 경우. 권한있는 폴더를 검색하여 추가한다
		if (StringUtil.getMapBoolean(map, Constant.INCLUDE_SUB_FOLDER)) {
			
			param.put("user_id", StringUtil.getMapString(map, "user_id"));
			param.put("parentId", StringUtil.getMapString(map, "folder_id"));
			param.put("group_id_list", map.get("group_id_list"));
			
			// 권한있는 하위폴더를 가져온다
			List<FolderVO> childFolderList = getFolderTreeToLow(param);
			
			// 목록을 구성한다.
			if (childFolderList != null && childFolderList.size() > 0) {
				int sortCnt = 0;
				String curParentId = "";
				
				for(FolderVO folderVO : childFolderList) {
					// sorts 값 초기화
					if (!folderVO.getParent_id().equals(curParentId)) {
						curParentId = folderVO.getParent_id();
						sortCnt = 0;
					}
					
					favoriteFolderVO = new FavoriteFolderVO();
					favoriteFolderVO.setFolder_id(folderVO.getFolder_id());
					favoriteFolderVO.setUser_id(StringUtil.getMapString(map, "user_id"));
					favoriteFolderVO.setFavorite_nm(folderVO.getFolder_name_ko());
					favoriteFolderVO.setParent_folder_id(folderVO.getParent_id());
					favoriteFolderVO.setIs_virtual("N");
					favoriteFolderVO.setSorts(sortCnt++);
					
					favoriteList.add(favoriteFolderVO);
				}
			}
		}
		
		// 5. 즐겨찾기 폴더 DB에 등록
		HashMap<String, Object> existsParam = new HashMap<String, Object>();
		for (FavoriteFolderVO favorite : favoriteList) {
			
			existsParam.put("user_id", favorite.getUser_id());
			existsParam.put("folder_id", favorite.getFolder_id());
			
			// 1. 즐겨찾기가 중복등록이 아닐경우에만 Write
			if (folderDao.existsFavoriteFolder(existsParam) == 0) {
				folderDao.writeFavoriteFolder(favorite);
			}
		}

		
	}
	

	@Override
	public void updateFavoriteFolder(HashMap<String, Object> map) throws Exception {
		
		FolderDao folderDao = sqlSession.getMapper(FolderDao.class);
		
		// 1. 유효성 검사
		if (StringUtil.getMapString(map, "folder_id").equals("")
			|| StringUtil.getMapString(map, "user_id").equals("")) {
			
			throw processException("common.required.error");
		}
		
		// 2. 폴더 수정
		folderDao.updateFavoriteFolder(map);
	}
	

	@Override
	public void deleteFavoriteFolder(HashMap<String, Object> map) throws Exception {
		
		FolderDao folderDao = sqlSession.getMapper(FolderDao.class);		
		DocumentDao documentDao = sqlSession.getMapper(DocumentDao.class);
		
		HashMap<String, Object> param = new HashMap<String, Object>();
		HashMap<String, Object> deleteInfo = new HashMap<String, Object>();
		
		List<FavoriteFolderVO> childFolderList = new ArrayList<FavoriteFolderVO>();
		
		// 1. 유효성 검사
		if (StringUtil.getMapString(map, "folder_id").equals("")
			|| StringUtil.getMapString(map, "user_id").equals("")) {
			
			throw processException("common.required.error");
		}
		
		// 2. 삭제 대상 폴더 목록 구성
		
		// 2-1. 삭제할 최상위 루트를 목록에 추가한다
		childFolderList.add(folderDao.favoriteFolderDetail(map));
		
		// 2-2. 모든 하위 폴더를 구한다
		param.put("user_id", StringUtil.getMapString(map, "user_id"));
		param.put("parent_folder_id", StringUtil.getMapString(map, "folder_id"));
		
		childFolderList.addAll(getFavoriteFolderTreeToLow(param));
		
		for (FavoriteFolderVO favoriteFolder : childFolderList) {
			deleteInfo.put("user_id", StringUtil.getMapString(map, "user_id"));
			deleteInfo.put("folder_id", favoriteFolder.getFolder_id());
			
			// 3. 폴더와 맵핑된 모든 문서를 삭제한다.
			documentDao.deleteFavoriteDocByFolderId(deleteInfo);
			
			// 4. 폴더를 삭제한다
			folderDao.deleteFavoriteFolder(deleteInfo);
		}
		
	}
	

	@Override
	public List<FolderVO> childFolderList(HashMap<String, Object> map) throws Exception {
		
		FolderDao folderDao = sqlSession.getMapper(FolderDao.class);
		
		List<FolderVO> folderList = folderDao.childFolderList(map);
		
		setFolderAcl(folderList, map);
		
		return folderList;
	}


	@Override
	public FolderVO folderDetail(HashMap<String, Object> map) throws Exception {
		
		FolderDao folderDao = sqlSession.getMapper(FolderDao.class);
		
		FolderVO folderVO = folderDao.folderDetail(map);
		
		return folderVO;
	}

	@Override
	public Map<String, Object> folderWrite(FolderVO folderVO, HashMap<String, Object> map, SessionVO sessionVO) throws Exception {
		
		FolderDao folderDao = sqlSession.getMapper(FolderDao.class);
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		int ret = 0;
		int folder_id = 0;
		int recently_id = commonService.commonNextVal(Constant.COUNTER_ID_RECENTLY);
		
		// 동일한 부모 폴더 아래 동일 폴더명이 존재하는 검사
		ret = folderDao.folderIsExistByFolderNameAndParentID(folderVO);
		
		if(ret > 0) {
			resultMap.put("result",Constant.RESULT_FALSE);
			resultMap.put("message_code","folder.fail.update.exist.foldername");
			return resultMap;
		}

		folder_id = commonService.commonNextVal(Constant.COUNTER_ID_FILE);
		folderVO.setFolder_id(CommonUtil.getStringID(Constant.ID_PREFIX_FOLDER, folder_id));
		folderVO.setCreator_id(sessionVO.getSessId()); //사용자 ID
		folderVO.setStorage_usage(0L);
		folderVO.setFolder_type(
			StringUtil.isEmpty(folderVO.getFolder_type()) ? Constant.FOLDER_TYPE_DOCUMENT : folderVO.getFolder_type()
		);
		
		
		
		ret = folderDao.folderWrite(folderVO);
		
		// 4. recently set
		RecentlyObjectVO recentlyObjectVo = new RecentlyObjectVO();
		recentlyObjectVo.setIdx(CommonUtil.getStringID(Constant.ID_PREFIX_RECENTLY, recently_id));
		recentlyObjectVo.setUser_id(sessionVO.getSessId());
		recentlyObjectVo.setTarget_id(folderVO.getFolder_id()); // 문서 : 문서ID, 폴더 : 폴더ID
		recentlyObjectVo.setTarget_type(Constant.RECENTLY_TYPE_FOLDER);  // 문서 : D, 폴더 : F => Constant 이용
		commonService.insertRecentlyObject(recentlyObjectVo);
		
		if(ret == 1) {
			resultMap.put("folder_id", folderVO.getFolder_id());
			resultMap.put("result",Constant.RESULT_TRUE);
		} else {
			resultMap.put("result",Constant.RESULT_FALSE);
		}
		
		return resultMap;
	}

	@Override
	public Map<String, Object> folderUpdate(FolderVO folderVO, HashMap<String, Object> map, SessionVO sessionVO) 
			throws Exception {
		
		FolderDao folderDao = sqlSession.getMapper(FolderDao.class);

		Map<String, Object> resultMap = new HashMap<String, Object>();
		String src_parent_id =  map.get("src_parent_id") != null ? map.get("src_parent_id").toString() : "";
		int ret = 0;
		int recently_id = commonService.commonNextVal(Constant.COUNTER_ID_RECENTLY);
		
		// sqlSessionBatch의 경우 return값이 정상적으로 return 되지 않는다. 비교 의미 없음.
		// 폴더ID가 존재하지 않으면 실행 안한다.
		if(StringUtil.isEmpty(folderVO.getFolder_id())){
			resultMap.put("result",Constant.RESULT_FALSE);
			resultMap.put("message_code","folder.fail.update.folder_id");
			return resultMap;
		}
		
		if(!StringUtil.isEmpty(src_parent_id) && !src_parent_id.equals(folderVO.getParent_id())) {
			// 1. 동일한 부모 폴더 아래 동일 폴더명이 존재하는 검사
			ret = folderDao.folderIsExistByFolderNameAndParentID(folderVO);			
			if(ret > 0) {		throw processException("folder.fail.update.exist.foldername");	}
		}
		
		String type =  map.get(Constant.TYPE) != null ? map.get(Constant.TYPE).toString() : "";
		
		// 폴더 Storage Quota 값을 검사한다. 
		if(!StringUtil.isEmpty(type) && type.equals(Constant.ACTION_UPDATE)) {
			
			if(map.get("storage_quota_chk") == null && map.get("storage_quota") != null) {
				long getQuota = StringUtil.parseLong(map.get("storage_quota").toString());
				long savedUsage = (folderDao.getFolderStorage(folderVO)).getStorage_usage();	
				
				/**
				 * 2015.03.25 김선재 대리 수정
				 * View에서 서버로 쿼터를 넘길 때 GB -> byte로 계산해서 넘겨줘야한다.
				 * 서버에서 데이터를 가공 할 경우 데이터가 꼬일 수 있음.
				 * 
				 * 
				 */
				// 
//				if(getQuota > 0){	getQuota = getQuota*1024*1024*1024;	}
				
				folderVO.setStorage_quota(getQuota);
			
				if(getQuota > -1 && getQuota < savedUsage) {
					throw processException("storage.quota.set.error");
				}
			}
		} else if(!StringUtil.isEmpty(type) && type.equals(Constant.ACTION_MOVE)) {
			
			if(map.get("root_folder_change").toString().equals(Constant.T)) {
				
				String targetRootId = map.get("parentGroup_id") != null ? map.get("parentGroup_id").toString() : "";
				
				if(!targetRootId.equals("")) {
					
					FolderVO parentInfo = new FolderVO();
					FolderVO targetInfo = new FolderVO();
					
					parentInfo.setFolder_id(targetRootId);
					
					// 상위 Root 폴더의 Storage 정보를 얻는다.
					targetInfo = folderDao.getFolderStorage(parentInfo);
					long targetQuota = targetInfo.getStorage_quota();
					long targetUsage = targetInfo.getStorage_usage();
					long savedUsage = (folderDao.getFolderStorage(folderVO)).getStorage_usage();
					
					if(targetQuota > -1){
						if((targetQuota - targetUsage) < savedUsage) {
							throw processException("storage.quota.overflow");
						}
					}
				}
			}			
		}
		
		// 2. folder update
		ret = folderDao.folderUpdate(folderVO);
		
		// 4. recently set
		RecentlyObjectVO recentlyObjectVo = new RecentlyObjectVO();
		recentlyObjectVo.setIdx(CommonUtil.getStringID(Constant.ID_PREFIX_RECENTLY, recently_id));
		recentlyObjectVo.setUser_id(sessionVO.getSessId());
		recentlyObjectVo.setTarget_id(folderVO.getFolder_id()); // 문서 : 문서ID, 폴더 : 폴더ID
		recentlyObjectVo.setTarget_type(Constant.RECENTLY_TYPE_FOLDER);  // 문서 : D, 폴더 : F => Constant 이용
		commonService.insertRecentlyObject(recentlyObjectVo);
		
		
		// 3. 또는 폴더 수정 시 하위폴더/문서 권한 일괄변경 권한 옵션이 넘어왔을 경우도 포함됨		
		 // input checkbox에 대한 별도 처리 안함(체크 시 on, 체크 해제 시 is_acl_batch는 null로 넘어 옴)
		boolean isChangeACL =( map.get("is_acl_batch") != null &&  map.get("is_acl_batch").toString().equals("on")) ? true : false;
		
		if(!StringUtil.isEmpty(type) && type.equals(Constant.ACTION_UPDATE)) {
			// 폴더 수정 :: 권한 일괄 변경 옵션 처리 해야 함
			resultMap.put("message_code","folder.fail.update.acl_change");
			if(isChangeACL) { // 개인폴더는 기능 제공 안함
				String parent_acl_id = (folderDao.getFolderAcl(folderVO)).getAcl_id();
				changeAclFromSubFolNDoc(folderVO.getFolder_id(), parent_acl_id);
			}
			resultMap.put("message_code","folder.save.msg");
		} else if(!StringUtil.isEmpty(type) && type.equals(Constant.ACTION_MOVE)) {
			// 폴더 이동 :: 폴더 및 문서 권한 변경
			resultMap.put("message_code","folder.fail.update.acl_change");
			if(!folderVO.getMap_id().equals(Constant.MAP_ID_MYPAGE)) {
				FolderVO parentFolderVO = new FolderVO();
				parentFolderVO.setFolder_id(folderVO.getParent_id());
				String parent_acl_id = (folderDao.getFolderAcl(parentFolderVO)).getAcl_id();
				changeAclFromSubFolNDoc(folderVO.getFolder_id(), parent_acl_id);
			}			
			resultMap.put("message_code","folder.move.msg");
		} else {
			resultMap.put("result",Constant.RESULT_FALSE);
			resultMap.put("message_code","folder.fail.update.type");
			return resultMap;
		}
		
		resultMap.put("folder_id", folderVO.getFolder_id());			
		resultMap.put("result",Constant.RESULT_TRUE);
		return resultMap;
	}
	
	@Override
	public Map<String, Object> folderDelete(HashMap<String, Object> map, SessionVO sessionVO) throws Exception {
		
		FolderDao folderDao = sqlSession.getMapper(FolderDao.class);
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		int ret = 0;
		
		// 1. 하위에 폴더가 존재하는지 체크
		ret = folderDao.existChildFolder(map);
		
		if(ret > 0) {
			resultMap.put("result",Constant.RESULT_FALSE);
			resultMap.put("message_code","folder.fail.delete.exist.child");
			return resultMap;
		}
		
		// 2. 폴더에 문서가 존재하는지 체크(단, 개인휴지통은 있는 것으로 간주)
		ret = folderDao.existDocument(map);
		
		if(ret > 0) {
			resultMap.put("result",Constant.RESULT_FALSE);
			resultMap.put("message_code","folder.fail.delete.exist.document");
			return resultMap;
		}
		
		ret = folderDao.folderDelete(map);
		
		if(ret == 1) {
			resultMap.put("message_code","folder.del.msg");
			resultMap.put("result",Constant.RESULT_TRUE);
		} else {
			resultMap.put("result",Constant.RESULT_FALSE);
		}
		
		return resultMap;
	}

	@Override
	public List<FolderVO> getFolderTreeToLow(HashMap<String, Object> param) throws Exception {
		
		FolderDao folderDao = sqlSession.getMapper(FolderDao.class);
		List<FolderVO> ret = new ArrayList<FolderVO>();
		
		// 1. 하위 폴더를 검색한다.
		List<FolderVO> childList = folderDao.childFolderList(param); 
		
		// 2. 하위 폴더 갯수만큼 재귀
		for (FolderVO child : childList) {
			param.put("parentId", child.getFolder_id());
			ret.addAll(getFolderTreeToLow(param));
		}
		
		// 3. 최종 결과를 반환한다.
		ret.addAll(childList);
		
		return ret;
	}

	@Override
	public List<FavoriteFolderVO> getFavoriteFolderTreeToLow(HashMap<String, Object> param) throws Exception {
		
		FolderDao folderDao = sqlSession.getMapper(FolderDao.class);
		List<FavoriteFolderVO> ret = new ArrayList<FavoriteFolderVO>();
		
		// 1. 하위 폴더를 검색한다
		List<FavoriteFolderVO> childList = folderDao.childFavoriteFolderList(param);
		
		// 2. 하위 폴더 갯수만큼 재귀
		for (FavoriteFolderVO child : childList) {
			param.put("parent_folder_id", child.getFolder_id());
			ret.addAll(getFavoriteFolderTreeToLow(param));
		}
		
		// 3. 최종 결과를 반환한다.
		ret.addAll(childList);
		
		return ret;
	}

	@Override
	public FolderVO getFolderTreeToHigh(String folderId, FolderVO childFolder) throws Exception {
		
		FolderDao folderDao = sqlSession.getMapper(FolderDao.class);
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("folder_id", folderId);
		
		FolderVO folder = folderDao.folderDetail(param);
		folder.setChildVO(childFolder);
		
		// 루트가 아닐 경우 상위폴더를 찾아간다
		if (folder.getParent_id() != folder.getFolder_id()) {
			getFolderTreeToHigh(folder.getParent_id(), folder);
		} 
		
		return folder;
	}
	
	@Override
	public FolderVO ownerChangeFolder(String ownerId,DocumentVO documentVO,String preFolderPath) throws Exception {
		
		FolderDao folderDao = sqlSession.getMapper(FolderDao.class);
	
		FolderVO ret = new FolderVO();
		FolderVO lastFolderInfo = new FolderVO();
		
		int result = 0;
		
		// 경로=> /연민정/개인보고서
		// 경로=> /연민정/개인보고서/자료실

		// 1.인수자 폴더에 인계자 폴더가 존재하는지 체크한다. 
		// 1.1 인계자 폴더가 없을 경우 신규 생성한다.
		
		FolderVO chkFolder = new FolderVO();
		chkFolder.setParent_id(ownerId);
		chkFolder.setFolder_name_ko(documentVO.getOwner_name());		
		if (folderDao.folderIsExistByFolderNameAndParentID(chkFolder) == 0) {
			
			// 1,2 인계자 폴더를 생성한다. :: XR_FOLDER
			chkFolder.setFolder_name_en("");
			chkFolder.setCreator_id(ownerId);
			chkFolder.setFolder_id(CommonUtil.getStringID(Constant.ID_PREFIX_FOLDER, commonService.commonNextVal(Constant.COUNTER_ID_FILE)));
			chkFolder.setAcl_id(Constant.ACL_ID_OWNER);
			chkFolder.setIs_type(Constant.FOLDER_TYPE_ALL_TYPE);
			chkFolder.setMap_id(Constant.MAP_ID_MYPAGE);
			chkFolder.setFolder_type(Constant.FOLDER_TYPE_DOCUMENT);			
			result = folderDao.folderWrite(chkFolder);
			if(result == 0)	{	throw processException("common.system.error");	}					
		}
		
		// 1.2 인계자 기본폴더
		HashMap<String,Object> param = new HashMap<String,Object>();
		param.put("parent_id", ownerId);
		param.put("folder_name_ko", documentVO.getCreator_name());
		FolderVO rootFolder = folderDao.folderDetail(param);
		
		// 1.4 입력값 preFolderPath 를 기준으로 폴더를 생성하고 최종 폴더값을 리턴한다.
		String inputFolder = preFolderPath.replace("/"+documentVO.getOwner_name(),"");
		
		// /개인보고서 , /개인보고서/자료실
		String[] folderStr = null;

		if(inputFolder.length() > 0)	{
		
			// 1.5 개인ROOT 폴더를 생성한다.
			if(inputFolder.startsWith("/")) {
				inputFolder = inputFolder.substring(1);
			}
			
			folderStr = inputFolder.split("/");		
			
			for(int i=0;i<folderStr.length;i++)	{
				
				HashMap<String,Object> folderMap = new HashMap<String,Object>();
				folderMap.put("map_id",Constant.MAP_ID_MYPAGE);
				folderMap.put("creator_id",ownerId);
				
				if(i==0)	{					
					folderMap.put("parent_name", rootFolder.getFolder_name_ko());
				}else {
					folderMap.put("parent_name", folderStr[i-1]);
				}
				
				folderMap.put("folder_name", folderStr[i]);
				
				CaseInsensitiveMap folderInfo = folderDao.getFolderInfo(folderMap);
				if(folderInfo == null)	{
					// 1.6 해당 폴더 생성					
					FolderVO newFolder = new FolderVO();
					if(i==0)	{		
						newFolder.setParent_id(rootFolder.getFolder_id());
					}else {
						
						HashMap<String,Object> pMap = new HashMap<String,Object>();
						pMap.put("map_id",Constant.MAP_ID_MYPAGE);
						pMap.put("creator_id",ownerId);
						pMap.put("folder_name", folderStr[i-1]);
						CaseInsensitiveMap parentFolderInfo = folderDao.getFolderInfo(pMap);
						if(parentFolderInfo != null)	{
							newFolder.setParent_id(parentFolderInfo.get("folder_id").toString());
						}else {
							throw processException("common.system.error");	
						}
					}
					
					newFolder.setFolder_name_ko(folderStr[i]);
					newFolder.setFolder_name_en("");
					newFolder.setCreator_id(ownerId);
					newFolder.setFolder_id(CommonUtil.getStringID(Constant.ID_PREFIX_FOLDER, commonService.commonNextVal(Constant.COUNTER_ID_FILE)));
					newFolder.setAcl_id(Constant.ACL_ID_OWNER);
					newFolder.setIs_type(Constant.FOLDER_TYPE_ALL_TYPE);
					newFolder.setMap_id(Constant.MAP_ID_MYPAGE);
					newFolder.setFolder_type(Constant.FOLDER_TYPE_DOCUMENT);					
					result = folderDao.folderWrite(newFolder);
					if(result == 0)	{	throw processException("common.system.error");	}
											
					if(i == (folderStr.length -1)) {
						lastFolderInfo = newFolder;
					}
				}else {
					// 이미 생성된 폴더인 경우 SKIPPED
					if(i == (folderStr.length -1)) {
												
						lastFolderInfo.setFolder_id(folderInfo.get("folder_id").toString());
						lastFolderInfo.setFolder_name_ko(folderInfo.get("folder_name").toString());
						lastFolderInfo.setParent_id(folderInfo.get("parent_id").toString());
					}
				}
						
				//lastFolderInfo
			}
			
			ret = lastFolderInfo;
			
		}else {
			
			// 1.6 개인ROOT 폴더에 있는 소유권 이관의 경우
			ret = rootFolder;			
		}
		
		return ret;
	}


	@Override
	public FolderVO cloneMyFolder(String ownerId, FolderVO srcFolder) throws Exception {
		
		FolderDao folderDao = sqlSession.getMapper(FolderDao.class);
		
		// 소유권 이전할 문서의 폴더 경로를 얻는다
		FolderVO fullPath = getFolderTreeToHigh(srcFolder.getParent_id(), srcFolder);
		
		FolderVO temp = fullPath;
		temp.setParent_id(ownerId);
		
		while (temp.getChildVO() != null) {
			// 인수인계 폴더가 없을경우에 생성한다
			if (folderDao.folderIsExistByFolderNameAndParentID(temp) == 0) {
			
				// 폴더의 소유자를 변경하고 ID를 새로 발급받는다
				temp.setCreator_id(ownerId);
				temp.setFolder_id(CommonUtil.getStringID(Constant.ID_PREFIX_FOLDER, commonService.commonNextVal(Constant.COUNTER_ID_FILE)));
				temp.getChildVO().setParent_id(temp.getFolder_id());
			}
			
			// 다음 자식 폴더로 이동
			temp = temp.getChildVO();
		}
		return temp;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void changeFolderCacheByFolderID(String folder_id, FolderVO folderVo, String type) throws Exception {
		Map<String, String> objIds = (HashMap<String, String>)cacheService.getCache(Constant.EHCACHE_CACHE_NAME_FOLDERLIST, Constant.EHCACHE_CACHE_KEY_FOLDERIDS);
		Map<String, String> objNames = (HashMap<String, String>)cacheService.getCache(Constant.EHCACHE_CACHE_NAME_FOLDERLIST, Constant.EHCACHE_CACHE_KEY_FOLDERNAMES);
		
		if(!StringUtil.isEmpty(type) && type.equals(Constant.ACTION_DELETE)) {
			objIds.remove(folder_id);
			objNames.remove(folder_id);
		} else {
			objIds.put(folder_id, folderVo.getParent_id());
			objNames.put(folder_id, folderVo.getFolder_name_ko());
		}
		
		cacheService.replaceCache(Constant.EHCACHE_CACHE_NAME_FOLDERLIST, Constant.EHCACHE_CACHE_KEY_FOLDERIDS, objIds);
		cacheService.replaceCache(Constant.EHCACHE_CACHE_NAME_FOLDERLIST, Constant.EHCACHE_CACHE_KEY_FOLDERNAMES, objNames);
	}
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : FolderList에 대한 권한 매핑 작업 
	 * 2. 처리내용 : map값에서는 반드시 user_id, group_id가 param 값으로 존재해야 함.
	 * </pre>
	 * @Method Name : setFolderAcl
	 * @param folder_list
	 * @param map
	 * @throws Exception void
	 */
	private void setFolderAcl(List<FolderVO> folder_list, HashMap<String, Object> map) throws Exception {
		
		// map값에서는 반드시 user_id, group_id가 param 값으로 존재해야 함.
		AclDao aclDao = sqlSession.getMapper(AclDao.class);
		
		// 1. 문서에 대한 권한을 셋팅 한다.
		Map<String, Integer> tempMap = new HashMap<String, Integer>();
		List<CaseInsensitiveMap> aclList = new ArrayList<CaseInsensitiveMap>();
		List<String> folder_idList = new ArrayList<String>();
		
		// 2. docList의 doc_id를 list값으로 map에 담는다
		int listCnt = 0;
		for(FolderVO folderVo : folder_list) {
			tempMap.put(folderVo.getFolder_id(), listCnt++);
			folder_idList.add(folderVo.getFolder_id());
		}
		// 3. 1번 기준 acl값 가져온다
		map.put("fol_idList", folder_idList);
		if( folder_idList.size() > 0)
			aclList = aclDao.alcListByFolderIDs(map);
		
		// 4. folder_list에 최종 값을 담는다.
		listCnt = 0;
		String folder_menu_part = map.get("folder_menu_part") != null ? map.get("folder_menu_part").toString() : "";
		String document_menu_part = map.get("document_menu_part") != null ? map.get("document_menu_part").toString() : "";
		
		for(CaseInsensitiveMap caseMap : aclList) {
			listCnt = (Integer)tempMap.get(caseMap.get("FOLDER_ID").toString());
			FolderVO tempFolderVo = new FolderVO(); 
			tempFolderVo =	folder_list.get(listCnt);
			
			if(caseMap.get("IS_TYPE").toString().equals(Constant.D)){
				//type이 문서 일때
				tempFolderVo.setAcl_document_create(caseMap.get("ACT_CREATE").toString());
				
				// 문서 메뉴 접근 권한 rule 적용
				setDocumentRoleAcl(tempFolderVo, document_menu_part, caseMap.get("FOLDER_ID").toString(), map);
			} else {
				//type이 폴더 일때
				if(caseMap.get("ACT_DELETE").toString().equals(Constant.T)) {
					tempFolderVo.setAcl_level(Constant.ACL_DELETE);
				} else if(caseMap.get("ACT_UPDATE").toString().equals(Constant.T)) {
					tempFolderVo.setAcl_level(Constant.ACL_UPDATE);
				} else if(caseMap.get("ACT_READ").toString().equals(Constant.T)) {
					tempFolderVo.setAcl_level(Constant.ACL_READ);
				} else if(caseMap.get("ACT_BROWSE").toString().equals(Constant.T)) {
					tempFolderVo.setAcl_level(Constant.ACL_BROWSE);
				} else {
					tempFolderVo.setAcl_level(Constant.ACL_NONE);
				}
				
				tempFolderVo.setAcl_create(caseMap.get("ACT_CREATE").toString());
				tempFolderVo.setAcl_changePermission(caseMap.get("ACT_CHANGE_PERMISSION").toString());
				
				// 메뉴 접근 권한 rule 적용
				setFolderRoleAcl(tempFolderVo, folder_menu_part, caseMap.get("FOLDER_ID").toString(), map);
			}
			
			// 권한 필터링 된 폴더ID는 제거 한다.
			if(folder_idList.contains(caseMap.get("FOLDER_ID").toString()))
				folder_idList.remove(caseMap.get("FOLDER_ID").toString());
					
			// 기존 내용 변경 시 add가 아닌 set을 이용한다.
			folder_list.set(listCnt, tempFolderVo); 
		}
		
		// 권한 없는 폴더에 대한 메뉴 접근 권한(role) 처리
		if(!StringUtil.isEmpty(folder_menu_part)){
			
			for(String rest_id : folder_idList){
				listCnt = (Integer)tempMap.get(rest_id);
				FolderVO tempFolderVo = new FolderVO(); 
				tempFolderVo =	folder_list.get(listCnt);
				
				// 폴더 메뉴 접근 권한 rule 적용
				setFolderRoleAcl(tempFolderVo, folder_menu_part, rest_id, map);
				
				// 문서 메뉴 접근 권한 rule 적용
				setDocumentRoleAcl(tempFolderVo, document_menu_part, rest_id, map);
				
				// 기존 내용 변경 시 add가 아닌 set을 이용한다.
				folder_list.set(listCnt, tempFolderVo); 
			}
		}
		
	}
	
	@Override
	public void changeAclFromSubFolNDoc(String folder_id, String parent_acl_id) throws Exception {
		
		FolderDao folderDao = sqlSession.getMapper(FolderDao.class);
		
		// 1. 변경 대상 폴더 List를 가져 온다.
		@SuppressWarnings("unchecked")
		Map<String, String> srcFolderMap = (HashMap<String, String>)cacheService.getCache(Constant.EHCACHE_CACHE_NAME_FOLDERLIST, Constant.EHCACHE_CACHE_KEY_FOLDERIDS);
		Map<String, String> tempMap = new HashMap<String, String>();
		for(Map.Entry<String, String> element : srcFolderMap.entrySet()) {
			tempMap.put(element.getKey(), element.getValue());
		}
		
		// 2. Queue FIFO 방식으로 대상 폴더 cache에서 추출
		String targetFolderId = folder_id;
		Queue<String> queue = new LinkedList<String>(); // FIFO 자료 구조형
		queue.add(targetFolderId);
		
		List<String> targetFolderIdsList = new ArrayList<String>();
		targetFolderIdsList.add(folder_id);
		
		while(!queue.isEmpty()){
			targetFolderId = queue.remove();  // queue에서 제거한 값  FIFO 방식 제거
			for(Map.Entry<String, String> element : tempMap.entrySet()) {
				if(targetFolderId.equals(element.getValue())){
					targetFolderIdsList.add(element.getKey());
					queue.add(element.getKey());
				}
			}
			tempMap.remove(targetFolderId);
		}
		
		// 3. is_inherit_acl=F 폴더에 대한 권한을 변경한다. xml에서 where 조건에 추가 => 폴더가 변경 대상이 아니어도 문서는 변경 될 수 있음
		int cnt = 0;
		List<FolderVO> tempFolderVO = new ArrayList<FolderVO>();
		for(String updateFolderId : targetFolderIdsList) {
			
			// 문서를 먼저 업데이트 한다. updateDocumentACL_ID(folder_id)
			updateDocumentACL_ID(updateFolderId, parent_acl_id);
			
			FolderVO folderVO = new FolderVO();
			folderVO.setFolder_id(updateFolderId);
			folderVO = folderDao.getFolderAcl(folderVO); // ACL_ID, IS_INHERIT_ACL 정보를 가져온다			
			folderVO.setUpdate_action(Constant.ACTION_CHANGE_ACL_ID);
			folderVO.setAcl_id(parent_acl_id);  // 상위폴더 ACL_ID로 변경 한다.
			
			if( folderVO.getIs_inherit_acl().equals(Constant.F)) // F이면 폴더에 대한 권한 변경
				tempFolderVO.add(folderVO);
			
			if( cnt !=0 && ( cnt % delimiter == 0 ) ) {	
				for(FolderVO updateFolder : tempFolderVO){
					folderDao.folderUpdate(updateFolder);
				}
				tempFolderVO.clear();
			} else if( cnt == (targetFolderIdsList.size()-1)) {
				for(FolderVO updateFolder : tempFolderVO){
					folderDao.folderUpdate(updateFolder);
				}
				tempFolderVO.clear();
			}
			cnt++;
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<String> folderFullpathIdsByfolderID(String folder_id) throws Exception {
		
		List<String> fullpath_ids = new ArrayList<String>();
		Map<String, String> folderIDs = new HashMap<String, String>();
		folderIDs = (Map<String, String>)cacheService.getCache(Constant.EHCACHE_CACHE_NAME_FOLDERLIST, Constant.EHCACHE_CACHE_KEY_FOLDERIDS);
		
		fullpath_ids.add(folder_id); // 자신의 폴더 ID를 넣는다
		int loopCnt = 0;
		while(!StringUtil.isEmpty(folder_id) && !folder_id.equals(Constant.FOLDER_TOP_ID)){
			if(folderIDs.containsKey(folder_id)){
				folder_id = folderIDs.get(folder_id);
				fullpath_ids.add(folder_id);
			} else {
				Collections.reverse(fullpath_ids); // 최상위 폴더ID가 맨 처음 오도록 정렬 한다.
				return fullpath_ids;
			}
			
			//무한 loop 방지 :: 부서 depth 최대한 10000개
			loopCnt++;
			if( loopCnt == 10000)
				break;
		}
		
		Collections.reverse(fullpath_ids); // 최상위 폴더ID가 맨 처음 오도록 정렬 한다.
		return fullpath_ids;
	}
	
	
	@Override
	public List<String> childFolderIdsByfolderId(String folder_id, String folder_menu_part) throws Exception {
		
		List<String> subFolderList = new ArrayList<String>();
		boolean is_current_dept = true;
		
		//폴더 role 처리
		if(!StringUtil.isEmpty(folder_menu_part) && !folder_menu_part.equals(Constant.MENU_TEAM))
			is_current_dept = false;
		
		getLowLevelNode(folder_id,subFolderList, is_current_dept);
		
		subFolderList.add(folder_id);
		
		return subFolderList;
	}

	@Override
	public Map<String, Object> existsFavoriteFolder(HashMap<String, Object> map) throws Exception {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		FolderDao folderDao = sqlSession.getMapper(FolderDao.class);
		int ret = 0;
		
		// 동일한 폴더가 추가되있는지 체크
		ret = folderDao.existsFavoriteFolder(map);
		
		if (ret > 0) {
			resultMap.put("result",Constant.RESULT_FALSE);
			resultMap.put("message_code","folder.fail.delete.exist.child");
		} else {
			resultMap.put("result",Constant.RESULT_TRUE);
		}
		
		return resultMap;
	}

	@Override
	public void swapFavoriteFolderIndex(HashMap<String, Object> map) throws Exception {
		
		HashMap<String, Object> param = new HashMap<String, Object>();
		FolderDao folderDao = sqlSession.getMapper(FolderDao.class);
		
		param.put("user_id", StringUtil.getMapString(map, "user_id"));
		
		// 1. source Folder Index
		param.put("folder_id", StringUtil.getMapString(map, "sourceFolderId"));
		param.put("sorts", StringUtil.getMapString(map, "sourceFolderIndex"));
		folderDao.updateFavoriteFolderIndex(param);
		
		// 2. target Folder Index
		param.put("folder_id", StringUtil.getMapString(map, "targetFolderId"));
		param.put("sorts", StringUtil.getMapString(map, "targetFolderIndex"));
		folderDao.updateFavoriteFolderIndex(param);
	}

	@Override
	public void moveFavoriteFolder(HashMap<String, Object> map) throws Exception {
		
		FolderDao folderDao = sqlSession.getMapper(FolderDao.class);
		
		HashMap<String, Object> param = new HashMap<String, Object>();
		HashMap<String, Object> tempFavorite = new HashMap<String, Object>();
		HashMap<String, Object> countParam = new HashMap<String, Object>();
		
		// user_id, source_parent_id, source_sorts, target_folder_id, source_folder_id
		param.put("user_id", StringUtil.getMapString(map, "user_id"));
		param.put("parent_folder_id", StringUtil.getMapString(map, "source_parent_id"));
		param.put("sorts", StringUtil.getMapInteger(map, "source_sorts"));
		
		// 1. 인덱스 정렬 대상 즐겨찾기 폴더 목록 조회
		List<FavoriteFolderVO> targetList = folderDao.childFavoriteFolderList(param);
		
		// 2. 인덱스 정렬 실행
		for (FavoriteFolderVO favoriteFolder : targetList) {
			tempFavorite.put("user_id", favoriteFolder.getUser_id());
			tempFavorite.put("folder_id", favoriteFolder.getFolder_id());
			tempFavorite.put("sorts", favoriteFolder.getSorts()-1);
			
			folderDao.updateFavoriteFolderIndex(tempFavorite);
		}
		
		// 3. 이동할 폴더 인덱스 계산
		countParam.put("user_id", StringUtil.getMapString(map, "user_id"));
		countParam.put("parent_id", StringUtil.getMapString(map, "target_folder_id"));
		int sorts = folderDao.favoriteFolderChildCount(countParam);
		
		// 4. 대상 폴더 정보 설정
		tempFavorite.clear();
		tempFavorite.put("folder_id", StringUtil.getMapString(map, "source_folder_id"));
		tempFavorite.put("user_id", StringUtil.getMapString(map, "user_id"));
		tempFavorite.put("sorts", sorts);
		tempFavorite.put("parent_id", StringUtil.getMapString(map, "target_folder_id"));
		
		// 5. 폴더 이동
		folderDao.updateFavoriteFolder(tempFavorite);
	}
	
	@Override
	public FolderVO getRootFolder(String mapId, String folderId, FolderVO childFolder) throws Exception {
		FolderDao folderDao = sqlSession.getMapper(FolderDao.class);
		
		HashMap<String,Object> param = new HashMap<String, Object>();		
		param.put("folder_id", folderId);
		
		FolderVO folder = folderDao.folderDetail(param);
		folder.setChildVO(childFolder);		

		if(mapId.equals("MYDEPT")) {
			if(folder.getMap_id().equals("MYDEPT") && folder.getFolder_type().equals("MYDEPT")){
				return folder;
			}
		} else if(mapId.equals("PROJECT")) {
			FolderVO projectInfo = new FolderVO();
			projectInfo.setMap_id(mapId);
			projectInfo.setFolder_type(mapId);
			
			FolderVO getProject = new FolderVO();
			getProject = folderDao.getRootFolderId(projectInfo);
			
			if(folder.getParent_id().equals(getProject.getFolder_id())){
				return folder;
			}
		}

		return getRootFolder(mapId, folder.getParent_id(), folder);		
	}

	@Override
	public List<RecentlyObjectVO> recentlyFolderList(String userId) throws Exception {
		FolderDao folderDao = sqlSession.getMapper(FolderDao.class);
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("user_id", userId);
		
		List<RecentlyObjectVO> list = folderDao.recentlyFolderList(param);
		return list;
	}

	@Override
	public Map<String, Object> recentlyFolderDelete(String idx) throws Exception {
		FolderDao folderDao = sqlSession.getMapper(FolderDao.class);
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		HashMap<String, Object> param = new HashMap<String, Object>();
		int ret = 0;
		
		param.put("idx", idx);
		
		ret = folderDao.recentlyFolderDelete(param);
		
		if(ret == 1) {
			resultMap.put("result",Constant.RESULT_TRUE);
		} else {
			resultMap.put("result",Constant.RESULT_FALSE);
		}
		
		return resultMap;
	}
	
	
}

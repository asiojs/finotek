package kr.co.exsoft.folder.dao;

import java.util.HashMap;
import java.util.List;

import kr.co.exsoft.common.vo.RecentlyObjectVO;
import kr.co.exsoft.folder.vo.FavoriteFolderVO;
import kr.co.exsoft.folder.vo.FolderVO;

import org.apache.commons.collections.map.CaseInsensitiveMap;
import org.springframework.stereotype.Repository;

/**
 * Folder 매퍼클래스
 * @author 패키지 개발팀
 * @since 2014.07.21
 * @version 3.0
 *
 */
@Repository(value = "folderDao")
public interface FolderDao {
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 권한이 사용중인지 체크
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : isUsingAcl
	 * @param map
	 * @return int
	 */
	public int isUsingAcl(HashMap<String,Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 :  MapID, ParentID 기준의 root 폴더 목록
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : rootFolderList
	 * @param map
	 * @return List<FolderVO>
	 */
	public List<FolderVO> rootFolderList(HashMap<String, Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 하위 부서 목록 조회 
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : childFolderList
	 * @param map
	 * @return List<FolderVO>
	 */
	public List<FolderVO> childFolderList(HashMap<String, Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 폴더 상세 정보 가져오기 
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : folderDetail
	 * @param map
	 * @return FolderVO
	 */
	public FolderVO folderDetail(HashMap<String, Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 폴더 등록 
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : folderWrite
	 * @param folderVO
	 * @return int
	 */
	public int folderWrite(FolderVO folderVO);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 폴더 수정
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : folderUpdate
	 * @param folderVO
	 * @return int
	 */
	public int folderUpdate(FolderVO folderVO);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 폴더 삭제 
	 * 2. 처리내용 : 하위 폴더 및 현재 폴더에 문서 또는 개인 휴지통에 현재 폴더에 대한 문서가 존재할 경우 삭제 불가 
	 * </pre>
	 * @Method Name : folderDelete
	 * @param map
	 * @return int
	 */
	public int folderDelete(HashMap<String, Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 하위폴더 존재 여부 확인
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : existChildFolder
	 * @param map
	 * @return int
	 */
	public int existChildFolder(HashMap<String, Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 현재폴더에 문서 존재 여부 및 개인 휴지통에 현재 폴더에 대한 문서 존재 여부 확인
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : existDocument
	 * @param map
	 * @return int
	 */
	public int existDocument(HashMap<String, Object> map);

	/**
	 * 
	 * <pre>
	 * 1. 개용 : 동일한 상위 폴더 하위에 동일한 이름의 폴더가 있는지 확인한다.
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : selectFolderIsExistByFolderNameAndParentID
	 * @param folderVO
	 * @return
	 */
	public int folderIsExistByFolderNameAndParentID(FolderVO folderVO);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 폴더-문서 링크 정보 삭제처리
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : xrLinkedDelete
	 * @param map
	 * @return int
	 */
	public int xrLinkedDelete(HashMap<String, Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : ROLE 권한자 관리대상 폴더여부 체크
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : isAuthFolder
	 * @param map
	 * @return int
	 */
	public int isAuthFolder(HashMap<String, Object> map);
	
	/**
	 * <pre>
	 * 1. 개용 : 폴더 이름 조회
	 * 2. 처리내용: 폴더 ID로 폴더 이름 조회
	 * </pre>
	 * @param map
	 * @return
	 */
	public List<FolderVO> getFolderName(HashMap<String, Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 폴더-문서 관련 정보 등록 :: 다중분류체계 포함
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : writeXrLinked
	 * @param map
	 * @return int
	 */
	public int writeXrLinked(HashMap<String, Object> map);
	
	/**
	 * <pre>
	 * 1. 개용 : 폴더 ACL 조회
	 * 2. 처리내용 : 폴더 ID로 폴더ACL 조회
	 * </pre>
	 * @Method Name : getFolderAcl
	 * @param folderVO
	 * @return folderVO
	 */
	public FolderVO getFolderAcl(FolderVO folderVO);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 개인문서함을 제외함 모든 폴더 정보 가져오기 
	 * 2. 처리내용 : folder_id, parent_id 리스트를 가져 온다.
	 * </pre>
	 * @Method Name : folderIdsList
	 * @return List<CaseInsensitiveMap>
	 */
	public List<CaseInsensitiveMap> folderIdsList();
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 소유권 이전대상 폴더 체크하기
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : getFolderInfo
	 * @param map
	 * @return CaseInsensitiveMap
	 */
	public CaseInsensitiveMap getFolderInfo(HashMap<String,Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 내용 : 공유받은 폴더 루트 목록
	 * 2. 처리내용 :
	 * </pre>
	 * @Method Name : rootSharefolderList
	 * @param map
	 * @return
	 */
	public List<FolderVO> rootShareFolderList(HashMap<String, Object> map);
	
	/**
	 * <pre>
	 * 1. 내용 : 즐겨찾는 루트 폴더 목록
	 * 2. 처리내용 :
	 * </pre>
	 * @Method Name : rootFavoriteFolderList
	 * @param map
	 * @return
	 */
	public List<FavoriteFolderVO> rootFavoriteFolderList(HashMap<String, Object> map);
	
	/**
	 * <pre>
	 * 1. 내용 : 즐겨찾는 자식 폴더 목록
	 * 2. 처리내용 :
	 * </pre>
	 * @Method Name : childFavoriteFolderList
	 * @param map
	 * @return
	 */
	public List<FavoriteFolderVO> childFavoriteFolderList(HashMap<String, Object> map);
	
	
	/**
	 * <pre>
	 * 1. 내용 : 즐겨찾는 폴더 추가
	 * 2. 처리내용 :
	 * </pre>
	 * @Method Name : rootFavoriteFolderList
	 * @param favoriteFolderVO
	 * @return
	 */
	public int writeFavoriteFolder(FavoriteFolderVO favoriteFolderVO);
	
	/**
	 * <pre>
	 * 1. 내용 : 즐겨찾는 폴더 수정
	 * 2. 처리내용 :
	 * </pre>
	 * @Method Name : updateFavoriteFolder
	 * @param favoriteFolderVO
	 * @return
	 */
	public int updateFavoriteFolder(HashMap<String, Object> map);
	
	/**
	 * <pre>
	 * 1. 내용 : 즐겨찾는 폴더 삭제
	 * 2. 처리내용 :
	 * </pre>
	 * @Method Name : deleteFavoriteFolder
	 * @param map
	 * @return
	 */
	public int deleteFavoriteFolder(HashMap<String, Object> map);
	
	/**
	 * <pre>
	 * 1. 내용 : 즐겨찾는 폴더의 자식폴더 갯수 카운트
	 * 2. 처리내용 :
	 * </pre>
	 * @Method Name : favoriteFolderChildCount
	 * @param map
	 * @return
	 */
	public int favoriteFolderChildCount(HashMap<String, Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 하위 폴더 ID 가져오기
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : childFolderIds
	 * @param map
	 * @return List<String>
	 */
	public List<String> childFolderIds(HashMap<String,Object> map);
	
	/**
	 * <pre>
	 * 1. 개용 : 즐겨찾기 폴더 중복 체크
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : existsFavoriteFolder
	 * @param map
	 * @return
	 */
	public int existsFavoriteFolder(HashMap<String, Object> map);
	
	/**
	 * <pre>
	 * 1. 개용 : 즐겨찾기 폴더 Index 스왑
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : swapFavoriteFolderIndex
	 * @param map
	 * @return
	 */
	public int updateFavoriteFolderIndex(HashMap<String, Object> map);
	
	/**
	 * <pre>
	 * 1. 개용 : 즐겨찾기 폴더 상세조회
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : favoriteFolderDetail
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public FavoriteFolderVO favoriteFolderDetail(HashMap<String, Object> map);
	
	/**
	 * <pre>
	 * 1. 개용 : 즐겨찾기 폴더 상세조회
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : folderCountByCreatorId
	 * @param creatorId
	 * @return
	 */
	public int folderCountByCreatorId(String creatorId);
	
	/**
	 * <pre>
	 * 1. 개용 : 사용자의 모든 즐겨찾기폴더 삭제
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : deleteFavoriteFolderOfUser
	 * @param map
	 * @return
	 */
	public int deleteFavoriteFolderOfUser(HashMap<String, Object> map);	

	/**
	 * <pre>
	 * 1. 개용 : 폴더 Storage 정보 조회
	 * 2. 처리내용 : 폴더 ID로 폴더 Storage Quota/Usage 조회
	 * </pre>
	 * @Method Name : getFolderStorage
	 * @param folderVO
	 * @return folderVO
	 */
	public FolderVO getFolderStorage(FolderVO folderVO);
	
	
	/**
	 * <pre>
	 * 1. 개용 : map_id와 folder_type으로 폴더 정보 조회
	 * 2. 처리내용 :
	 * </pre>
	 * @param folderVO
	 * @return
	 */
	public FolderVO getRootFolderId(FolderVO folderVO);
	
	/**
	 * <pre>
	 * 1. 개용 : user_id로 최근 사용한 폴더 목록 조회
	 * 2. 처리내용 :
	 * </pre>
	 * @param map
	 * @return
	 */
	public List<RecentlyObjectVO> recentlyFolderList(HashMap<String, Object> map);
	
	/**
	 * <pre>
	 * 1. 개용 : user_id로 최근 사용한 폴더 삭제
	 * 2. 처리내용 :
	 * </pre>
	 * @param map
	 * @return
	 */
	public int recentlyFolderDelete(HashMap<String, Object> map);
}



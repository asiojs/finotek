package kr.co.exsoft.folder.service;

import java.sql.SQLException;
import java.util.*;

import kr.co.exsoft.common.vo.RecentlyObjectVO;
import kr.co.exsoft.common.vo.SessionVO;
import kr.co.exsoft.document.vo.DocumentVO;
import kr.co.exsoft.folder.vo.FavoriteFolderVO;
import kr.co.exsoft.folder.vo.FolderVO;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Folder 서비스 인터페이스
 * @author 패키지 개발팀
 * @since 2014.07.17
 * @version 3.0
 *
 */
@Transactional
public interface FolderService {
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : MapID, ParentID 기준의 root 폴더 목록
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : rootFolderList
	 * @param map
	 * @return
	 * @throws Exception List<FolderVO>
	 */
	public List<FolderVO> rootFolderList(HashMap<String, Object> map) throws Exception;
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 하위 부서 목록 조회 
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : childFolderList
	 * @param map
	 * @return
	 * @throws Exception List<FolderVO>
	 */
	public List<FolderVO> childFolderList(HashMap<String, Object> map) throws Exception;
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 폴더 상세 내용 가져오기
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : folderDetail
	 * @param map
	 * @return
	 * @throws Exception FolderVO
	 */
	public FolderVO folderDetail(HashMap<String, Object> map) throws Exception;
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 폴더 등록 
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : folderWrite
	 * @param folderVO
	 * @param map
	 * @param sessionVo
	 * @return
	 * @throws Exception Map<String,Object>
	 */
	public Map<String, Object> folderWrite(FolderVO folderVO, HashMap<String, Object> map, SessionVO sessionVO) throws Exception;
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 폴더 수정
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : folderUpdate
	 * @param folderVO
	 * @param map
	 * @param sessionVo
	 * @return
	 * @throws Exception Map<String,Object>
	 */
	@Transactional(propagation = Propagation.REQUIRED,rollbackFor ={ Exception.class,SQLException.class})
	public Map<String, Object> folderUpdate(FolderVO folderVO, HashMap<String, Object> map, SessionVO sessionVO) throws Exception;
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 폴더 삭제 
	 * 2. 처리내용 : 하위 폴더 및 현재 폴더에 문서 또는 개인 휴지통에 현재 폴더에 대한 문서가 존재할 경우 삭제 불가
	 * </pre>
	 * @Method Name : folderDelete
	 * @param map
	 * @param sessionVo
	 * @return
	 * @throws Exception Map<String,Object>
	 */
	public Map<String, Object> folderDelete(HashMap<String, Object> map, SessionVO sessionVO) throws Exception;

	/**
	 * 
	 * <pre>
	 * 1. 내용 : 특정 폴더의 모든 하위 폴더를 가져온다. 
	 * 2. 처리내용 : 
	 *     - 최초로 주어진 rootId를 기준으로 모든 하위 폴더를 검색하여 List<FolderVO>형태로 반환한다
	 *     - recursive function으로 로직을 구현한다.
	 *     - 단, 모든 검색이 끝나기 전엔 리소스를 반환하지 않으니 for문으로 교체를 고려해야 한다.
	 * </pre>
	 * @Method Name : getFolderTree
	 * @param rootId
	 * @return
	 * @throws Exception
	 */
	public List<FolderVO> getFolderTreeToLow(HashMap<String, Object> param) throws Exception;

	/**
	 * <pre>
	 * 1. 개용 : 현재폴더를 기준으로 root 폴더까지 구한다.
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : existDocument
	 * @param folderId
	 * @return
	 * @throws Exception
	 */
	public FolderVO getFolderTreeToHigh(String folderId, FolderVO childFolder) throws Exception;
	
	/**
	 * <pre>
	 * 1. 개용 : 인계자의 특정 폴더의 경로를 인수자의 인수인계폴더에 구성한다.
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : getFavoriteFolderTreeToLow
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public List<FavoriteFolderVO> getFavoriteFolderTreeToLow(HashMap<String, Object> param) throws Exception;
	
	/**
	 * <pre>
	 * 1. 개용 : 인계자의 특정 폴더의 경로를 인수자의 인수인계폴더에 구성한다.
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : cloneMyFolder
	 * @param targetUserId
	 * @param fromUserId
	 * @param folderId
	 * @return
	 * @throws Exception
	 */
	public FolderVO cloneMyFolder(String ownerId, FolderVO srcFolder) throws Exception;
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 인계자의 특정 폴더의 경로를 인수자의 인수인계폴더에 구성한다.  
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : ownerChangeFolder
	 * @param ownerId
	 * @param documentVO
	 * @param preFolderPath
	 * @return
	 * @throws Exception FolderVO
	 */
	public FolderVO ownerChangeFolder(String ownerId,DocumentVO documentVO,String preFolderPath) throws Exception;
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 캐쉬의 parent_id 값을 변경 한다.
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : changeFolderCacheByFolderID
	 * @param folder_id : 폴더 ID
	 * @param folderVo : folderVo
	 * @param type : CREATE|UPDATE|DELETE|MOVE
	 * @throws Exception void
	 */
	public void changeFolderCacheByFolderID(String folder_id, FolderVO folderVo, String type) throws Exception;
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 폴더 이동, 폴더 수정 시 하위 폴더/문서 권한 일괄변경 요청 시 
	 * 2. 처리내용 : 변경할 폴더 및 문서에 대한 is_inherit_acl값이 'F' 값일 경우 변경
	 * </pre>
	 * @Method Name : changeAclFromSubFolNDoc
	 * @param folder_id
	 * @param parent_acl_id
	 * @throws Exception void
	 */
	@Transactional(propagation = Propagation.REQUIRED,rollbackFor ={ Exception.class,SQLException.class})
	public void changeAclFromSubFolNDoc(String folder_id, String parent_acl_id) throws Exception;
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : folder_id 기준으로 상위 폴더 id 요청 
	 * 2. 처리내용 : ex) arg:FOL000000000008 => FOL000000000000,FOL000000000002,FOL000000000008
	 * </pre>
	 * @Method Name : folderFullpathIDsByfolderID
	 * @param folder_id
	 * @return
	 * @throws Exception List<String>
	 */
	public List<String> folderFullpathIdsByfolderID(String folder_id) throws Exception;
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 하위 폴더 목록 가져오기 MAIN
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : childFolderIdsByfolderId
	 * @param folder_id
	 * @param folder_menu_part
	 * @return List<String>
	 */
	public List<String> childFolderIdsByfolderId(String folder_id, String folder_menu_part) throws Exception;
	
	/**
	 * <pre>
	 * 1. 개용 : 공유 폴더 목록 
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : rootShareFolderList
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<FolderVO> rootShareFolderList(HashMap<String, Object> map) throws Exception;
	
	/**
	 * <pre>
	 * 1. 개용 : 즐겨찾기 루트 폴더 목록 
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : rootFavoriteFolderList
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<FavoriteFolderVO> rootFavoriteFolderList(HashMap<String, Object> map) throws Exception;
	
	/**
	 * <pre>
	 * 1. 개용 : 즐겨찾기 자식 폴더 목록 
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : childFavoriteFolderList
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<FavoriteFolderVO> childFavoriteFolderList(HashMap<String, Object> map) throws Exception;
	
	/**
	 * <pre>
	 * 1. 개용 : 즐겨찾기 폴더 추가 
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : writeFavoriteFolderList
	 * @param map
	 * @throws Exception
	 */
	public void writeFavoriteFolder(HashMap<String, Object> map) throws Exception;
	
	/**
	 * <pre>
	 * 1. 개용 : 즐겨찾기 폴더 수정
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : updateFavoriteFolder
	 * @param map
	 * @throws Exception
	 */
	public void updateFavoriteFolder(HashMap<String, Object> map) throws Exception;
	
	/**
	 * <pre>
	 * 1. 개용 : 즐겨찾기 폴더 삭제
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : deleteFavoriteFolder
	 * @param map
	 * @throws Exception
	 */
	public void deleteFavoriteFolder(HashMap<String, Object> map) throws Exception;
	
	/**
	 * <pre>
	 * 1. 개용 : 즐겨찾기 폴더 중복 확인
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : existsFavoriteFolder
	 * @param map
	 * @throws Exception
	 */
	public Map<String, Object> existsFavoriteFolder(HashMap<String, Object> map) throws Exception;
	
	/**
	 * <pre>
	 * 1. 개용 : 즐겨찾기 폴더 Index 스왑
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : swapFavoriteFolderIndex
	 * @param map
	 * @throws Exception
	 */
	public void swapFavoriteFolderIndex(HashMap<String, Object> map) throws Exception;
	
	/**
	 * <pre>
	 * 1. 개용 : 즐겨찾기 폴더 이동
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : moveFavoriteFolder
	 * @param map
	 * @throws Exception
	 */
	public void moveFavoriteFolder(HashMap<String, Object> map) throws Exception;
	
	
	/**
	 * <pre>
	 * 1. 개용 : 폴더의 루트폴더 조회
	 * 2. 처리내용 : 스토리지 할당량 확인을 위해 해당 폴더의 루트폴더 (GroupFolder)를 가져온다 
	 * </pre>
	 * @param MapId
	 * @param folderId
	 * @param childFolder
	 * @return
	 * @throws Exception
	 */
	public FolderVO getRootFolder(String MapId, String folderId, FolderVO childFolder) throws Exception;
	
	/**
	 * <pre>
	 * 1. 개용 : folder_id와 user_id로 최근 사용한 폴더 목록 조회
	 * 2. 처리내용 :
	 * </pre>
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public List<RecentlyObjectVO> recentlyFolderList(String userId) throws Exception;
	
	/**
	 * <pre>
	 * 1. 개용 : folder_id와 user_id로 최근 사용한 폴더 목록 조회
	 * 2. 처리내용 :
	 * </pre>
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> recentlyFolderDelete(String idx) throws Exception;
}


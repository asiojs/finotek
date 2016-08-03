package kr.co.exsoft.document.dao;

import java.util.HashMap;
import java.util.List;

import kr.co.exsoft.common.vo.CommentVO;
import kr.co.exsoft.document.vo.DocumentVO;

import org.springframework.stereotype.Repository;

/**
 * Document 매퍼클래스
 * @author 패키지 개발팀
 * @since 2014.07.29
 * @version 3.0
 *
 */
@Repository(value = "documentDao")
public interface DocumentDao {

	/**
	 * 
	 * <pre>
	 * 1. 개용 : 문서유형이 사용되는지 체크한다. 
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : isUsingType
	 * @param map
	 * @return int
	 */
	public int isUsingType(HashMap<String,Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 권한이 사용되는지 체크
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
	 * 1. 개용 :  중복 파일 포함 문서 카운트 얻기. 
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : duplicateDocCount
	 * @param map
	 * @return int
	 */
	public int duplicateDocCount(HashMap<String,Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 중복 파일 포함 문서 목록 얻기.
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : duplicateDocList
	 * @param map
	 * @return List<DocumentVO>
	 */
	public List<DocumentVO> duplicateDocList(HashMap<String,Object> map);
		
	
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 폴더에 등록된 문서 수를 얻는다 
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : getDocumentCountByFolderId
	 * @param map
	 * @return
	 */
	public int getDocumentCountByFolderId(HashMap<String,Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 만기문서 목록
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : expiredPagingCount
	 * @param map
	 * @return int
	 */
	public int expiredPagingCount(HashMap<String,Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 만기문서 카운트
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : expiredDocumentList
	 * @param map
	 * @return List<DocumentVO>
	 */
	public List<DocumentVO> expiredDocumentList(HashMap<String,Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 문서 삭제처리
	 * 2. 처리내용 : 대상테이블 XR_DOCUMENT/XR_DOCUMENT_DEL/XR_FAVORITE_DOC
	 * </pre>
	 * @Method Name : docCommonDelete
	 * @param map
	 * @return int
	 */
	public int docCommonDelete(HashMap<String,Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : ROOT_ID로 모든 버전의 문서 목록을 가져온다 :: XR_DOCUMENT
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : allVersionDocumentList
	 * @param map
	 * @return List<DocumentVO>
	 */
	public List<DocumentVO> allVersionDocumentList(HashMap<String,Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 :  ROOT_ID로 모든 버전의 문서 목록을 가져온다 :: XR_DOCUMENT_DEL
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : allVersionDelDocumentList
	 * @param map
	 * @return List<DocumentVO>
	 */
	public List<DocumentVO> allVersionDelDocumentList(HashMap<String,Object> map);
	
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 문서속성 정보 변경처리
	 * 2. 처리내용 : isType : 업무구분자 필수입력 요망.
	 * </pre>
	 * @Method Name : docInfoUpdate
	 * @param map
	 * @return int
	 */
	public int docInfoUpdate(HashMap<String,Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 개인휴지통에서 관리자휴지통으로 문서이동처리
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : adminTrashMove
	 * @param map
	 * @return int
	 */
	public int adminTrashMove(HashMap<String,Object> map);

	/**
	 * <pre>
	 * 1. 개용 : 휴지통 문서 카운트
	 * 2. 처리내용 
	 * </pre>
	 * @Method Name : wastePagingCount
	 * @param map
	 * @return
	 */
	public int wastePagingCount(HashMap<String,Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 휴지통 문서 리스트
	 * 2. 처리내용 
	 * </pre>
	 * @Method Name : wasteList
	 * @param map
	 * @return
	 */
	public List<DocumentVO> wasteList(HashMap<String,Object> map);
	
	/**
	 * <pre>
	 * 1. 개용 : 휴지통 문서 상세 정보 조회 :: delete 예정
	 * 2. 처리내용
	 * </pre>
	 * @Method Name : wasteDocDetail
	 * @param map
	 * @return
	 */
	public DocumentVO wasteDocDetail(HashMap<String,Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 관리자 휴지통 문서 상세보기 
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : commonDocWasteDetail
	 * @param map
	 * @return DocumentVO
	 */
	public DocumentVO commonDocWasteDetail(HashMap<String,Object> map);

	/**
	 * <pre>
	 * 1. 내용 : 문서 만기 일자 변경
	 * 2. 처리내용
	 * </pre>
	 * @Method Name : preservationYearUpdate
	 * @param map
	 * @return
	 */
	public int preservationYearUpdate(HashMap<String, Object> map);
	
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 관리자 문서상세보기 공통
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : commonDocDetail
	 * @param map
	 * @return DocumentVO
	 */
	public DocumentVO commonDocDetail(HashMap<String, Object> map);
		
	/**
	 * <pre>
	 * 1. 개용 : 휴지통 비우기 문서 리스트 조회
	 * 2. 처리내용
	 * </pre>
	 * 
	 * @Method Name : wasteAllDocList
	 * @param map
	 * @return
	 */
	public List<DocumentVO> wasteAllDocList(HashMap<String,Object> map);
	
	
	/**
	 * <pre>
	 * 1. 내용 : 소유권 변경 문서 목록 카운트
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : ownerPagingCount
	 * @param map
	 * @return
	 */
	public int ownerPagingCount(HashMap<String, Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 내용 : 소유권 변경 문서 목록
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : ownerDocumentList
	 * @param map
	 * @return
	 */
	public List<DocumentVO> ownerDocumentList(HashMap<String, Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 소유권 변경 문서 목록 페이징처리 없이
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : ownerDocumentNoPageList
	 * @param map
	 * @return List<DocumentVO>
	 */
	public List<DocumentVO> ownerDocumentNoPageList(HashMap<String, Object> map);
	
	/**
	 * <pre>
	 * 1. 내용 : 문서 이동
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : documentMove
	 * @param map
	 * @return
	 */
	public int documentMove(DocumentVO documentVO);
	
	/**
	 * <pre>
	 * 1. 내용 : 문서 수정
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : documentUpdate
	 * @param map
	 * @return
	 */
	public int documentUpdate(DocumentVO documentVO);
	
	/**
	 * <pre>
	 * 1. 내용 : 문서 수정
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : refDocumentList
	 * @param map
	 * @return
	 */
	public List<DocumentVO> refDocumentList(HashMap<String, Object> map);

	/**
	 * <pre>
	 * 1. 개용 : 다중분류문서체크
	 * 2. 처리내용 :
	 * </pre>
	 * @Method Name : multiDocument
	 * @param map
	 * @return
	 */
	public List<DocumentVO> multiDocument(HashMap<String, Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 폴더내 동일 문서제목 존재하는지 체크 :: 문서상태 D 는 제외함
	 * 2. 처리내용 : folder_id/doc_id/doc_name/is_current/doc_status 이며 등록시에는 doc_id를 skip
	 * </pre>
	 * @Method Name : isExitsDocName
	 * @param map 
	 * @return int
	 */
	public int isExitsDocName(HashMap<String, Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 문서등록처리 :: 공통 신규
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : writeDocument
	 * @param documentVO
	 * @return int
	 */
	public int writeDocument(DocumentVO documentVO);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 관련문서 등록처리
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : writeRefDoc
	 * @param map
	 * @return int
	 */
	public int writeRefDoc(HashMap<String, Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 다중분류체계 리스트 가져오기 :: 문서상세보기/수정모드
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : multiFolderList
	 * @param map
	 * @return List<String>
	 */
	public List<String> multiFolderList(HashMap<String, Object> map);
	
	
	/**
	 * <pre>
	 * 1. 개용 : 즐거찾기 문서 존재여부
	 * 2. 처리내용 : 즐겨찾기 폴더에 문서가 추가여부 확인 
	 * </pre>
	 * @Method Name : favoriteDocSearch
	 * @param map
	 * @return
	 */
	public int favoriteDocSearch(HashMap<String,Object> map);
	/**
	 * <pre>
	 * 1. 개용 : 즐겨찾기 문서 추가
	 * 2. 처리내용 :
	 * </pre>
	 * @Method Name : favoriteDocWrite
	 * @param map
	 */
	public void favoriteDocWrite(HashMap<String,Object> map);
	
	/**
	 * <pre>
	 * 1. 개용 : 대상 문서명과 비슷한 문서명을 검색. (같은 폴더 안의 다른 문서들을 대상으로 함) 
	 * 2. 처리내용 : 중복 문서명 검색 시 사용
	 * </pre>
	 * @Method Name :selectSimilarDocumentListByDocNameAndFolderID
	 * @param map
	 * @return
	 */
	public List<String> selectSimilarDocumentListByDocNameAndFolderID(HashMap<String,Object>map);
	
	/**
	 * <pre>
	 * 1. 개용 : 문서 정보 얻기
	 * 2. 문서ID로 XR_DOCUMENT에 저장된 문서 정보를 얻는다
	 * </pre>
	 * @Method Name :getDocumentInfo
	 * @param map
	 * @return
	 */
	public DocumentVO getDocumentInfo(HashMap<String, Object> map);
	
	/**
	 * <pre>
	 * 1. 개용 : 사용자 휴지통 비우기 문서 리스트 조회
	 * 2. 처리내용
	 * </pre>
	 * 
	 * @Method Name : authWasteAllDocList
	 * @param map
	 * @return
	 */
	public List<DocumentVO> authWasteAllDocList(HashMap<String,Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 임시작업함 문서 갯수 조희(임시작업함에 문서가 추가되어있는지 체크)
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : tempDocumentCount
	 * @param map
	 * @return int
	 */
	public int tempDocumentCount(HashMap<String,Object> map);

	/**
	 * 
	 * <pre>
	 * 1. 개용 : 임시작업함 문서 추가 처리
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : tempDocInsert
	 * @param map
	 * @return int
	 */
	public int tempDocInsert(HashMap<String,Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 폴더ID 기준으로 acl 변경 대상 문서 가져오기
	 * 2. 처리내용 : 현재 버전이면서 in_inherit
	 * </pre>
	 * @Method Name : documentAclChangeListByFolderId
	 * @param map
	 * @return List<DocumentVO>
	 */
	public List<DocumentVO> documentAclChangeListByFolderId(HashMap<String,Object> map);

	/**
	 * 
	 * <pre>
	 * 1. 개용 : 임시작업함 문서 삭제 처리
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : deleteTempDoc
	 * @param map
	 * @return int
	 */
	public int tempDocDelete(HashMap<String,Object> map);

	/**
	 * 
	 * <pre>
	 * 1. 개용 : 즐겨찾기 문서 삭제
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : deleteFavoriteDoc
	 * @param map
	 * @return
	 */
	public int deleteFavoriteDoc(HashMap<String, Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 사용자의 모든 즐겨찾기 문서 삭제
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : deleteFavoriteDocOfUser
	 * @param map
	 * @return
	 */
	public int deleteFavoriteDocOfUser(HashMap<String, Object> map);
	
	
	/**
	 * <pre>
	 * 1. 개용 : 즐겨찾기 폴더에 등록된 즐겨찾기 문서 삭제
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : deleteFavoriteDocByFolderId
	 * @param map
	 * @return
	 */
	public int deleteFavoriteDocByFolderId(HashMap<String, Object> map);
	

	/**
	 * 
	 * <pre>
	 * 1. 개용 : 관련문서에 메인문서로 사용되는지 확인한다.
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : isUsingRefDocId
	 * @param map
	 * @return int
	 */
	public int isUsingRefDocId(HashMap<String,Object> map);
	
	/**
	 * <pre>
	 * 1. 개용 : 사용자가 만든 문서 카운트
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : documentCountByCreatorId
	 * @param creatorId
	 * @return
	 */
	public int documentCountByCreatorId(String creatorId);
	
	/**
	 * <pre>
	 * 1. 개용 : 사용자가 소유한  문서 카운트
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : documentCountByOwnerId
	 * @param ownerId
	 * @return
	 */
	public int documentCountByOwnerId(String ownerId);

	/**
	 * <pre>
	 * 1. 개용 : 문서 댓글 카운트
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : docCommentPagingCount
	 * @param map
	 * @return
	 */
	public int docCommentPagingCount(String root_id);
	/**
	 * <pre>
	 * 1. 개용 : 문서 댓글 리스트
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : docCommentList
	 * @param ownerId
	 * @return
	 */
	public List<CommentVO> docCommentList(HashMap<String,Object> map);
	
	/**
	 * <pre>
	 * 1. 개용 : 문서 댓글 리스트
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : docComment
	 * @param ownerId
	 * @return
	 */
	public CommentVO docComment(String comId);
	
	/**
	 * <pre>
	 * 1. 개용 : 문서 의견등록
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : docCommentWrite
	 * @param CommentVO
	 * @return
	 */
	public int docCommentWrite(CommentVO commentVO);
	/**
	 * <pre>
	 * 1. 개용 : 문서 의견update
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : docCommentUpdate
	 * @param map
	 * @return
	 */
	public int docCommentUpdate(HashMap<String,Object> map);
	/**
	 * <pre>
	 * 1. 개용 : 문서 의견 최상위 order 값 가져오기
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : checkMaxOrder
	 * @param map
	 * @return
	 */
	public int checkMaxOrder(HashMap<String,Object> map);

	/**
	 * <pre>
	 * 1. 개용 : 문서 의견 최상위 step 값 가져오기
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : checkMaxStep
	 * @param map
	 * @return
	 */
	public int checkMaxStep(HashMap<String,Object> map);
	
	/**
	 * <pre>
	 * 1. 개용 : 문서 의견 delete
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : docCommentDelete
	 * @param map
	 * @return
	 */
	public int docCommentDelete(HashMap<String,Object> map);
	
	/**
	 * <pre>
	 * 1. 개용 : 조회수 update
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : docReadCountUpdate
	 * @param map
	 * @return
	 */
	public int docReadCountUpdate(HashMap<String,Object> map);
	/**
	 * <pre>
	 * 1. 개용 : 삭제시 원글의 상태값 확인 
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : checkCommentStatus
	 * @param map
	 * @return
	 */
	public int checkCommentStatus(HashMap<String,Object> map);
	/**
	 * <pre>
	 * 1. 개용 : 삭제
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : deleteComment2
	 * @param map
	 * @return
	 */
	public int deleteComment2(HashMap<String,Object> map);
	
}

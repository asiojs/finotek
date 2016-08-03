package kr.co.exsoft.document.service;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import kr.co.exsoft.common.vo.SessionVO;
import kr.co.exsoft.document.vo.AttrVO;
import kr.co.exsoft.document.vo.DocumentVO;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Document 서비스 인터페이스
 * @author 패키지 개발팀
 * @since 2014.07.21
 * @version 3.0
 *
 */
@Transactional
public interface DocumentService {
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 중복파일 문서 목록 가져오기.
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : duplicateDocList
	 * @param map
	 * @return
	 * @throws Exception Map<String,Object>
	 */
	public Map<String, Object> duplicateDocList(HashMap<String,Object> map) throws Exception;
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 만기문서 목록 가져오기 
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : expiredDocumentList
	 * @param map
	 * @return
	 * @throws Exception Map<String,Object>
	 */
	public Map<String, Object> expiredDocumentList(HashMap<String,Object> map) throws Exception;
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 문서완전 삭제처리 
	 * 2. 처리내용 : 중복파일관리/만기문서관리 등
	 * </pre>
	 * @Method Name : terminateDocument
	 * @param map
	 * @return
	 * @throws Exception Map<String,Object>
	 */
	@Transactional(propagation = Propagation.REQUIRED,rollbackFor ={ Exception.class,SQLException.class})
	public Map<String, Object> terminateDocument(List<HashMap<String, Object>> docList ,HashMap<String,Object> map,SessionVO sessionVO) throws Exception;
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 :문서삭제처리
	 * 2. 처리내용 : 개인함/부서함/프로젝트함에서 문서삭제시
	 * </pre>
	 * @Method Name : deleteDocument
	 * @param docList
	 * @param map
	 * @param sessionVO
	 * @return
	 * @throws Exception Map<String,Object>
	 */
	@Transactional(propagation = Propagation.REQUIRED,rollbackFor ={ Exception.class,SQLException.class})
	public Map<String, Object> deleteDocument(List<HashMap<String, Object>> docList ,HashMap<String,Object> map,SessionVO sessionVO) throws Exception;
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 문서버전보기 완전삭제처리
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : versionDocDelete
	 * @param docList
	 * @param map
	 * @param sessionVO
	 * @return
	 * @throws Exception Map<String,Object>
	 */
	@Transactional(propagation = Propagation.REQUIRED,rollbackFor ={ Exception.class,SQLException.class})
	public Map<String, Object> versionDocDelete(List<HashMap<String, Object>> docList ,HashMap<String,Object> map,SessionVO sessionVO) throws Exception;
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 개인휴지통 문서삭제처리
	 * 2. 처리내용 : 개인휴지통 문서삭제/휴지통비우기  :: 대상테이블 XR_DOCUMENT
	 * </pre>
	 * @Method Name : trashDeleteDoc
	 * @param docList
	 * @param map
	 * @param sessionVO
	 * @return
	 * @throws Exception Map<String,Object>
	 */
	@Transactional(propagation = Propagation.REQUIRED,rollbackFor ={ Exception.class,SQLException.class})
	public Map<String, Object> trashDeleteDoc(List<HashMap<String, Object>> docList ,HashMap<String,Object> map,SessionVO sessionVO) throws Exception;
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 관리자휴지통 문서삭제처리
	 * 2. 처리내용 : 관리자휴지통 문서삭제/휴지통비우기 :: 대상테이블 XR_DOCUMENT_DEL
	 * </pre>
	 * @Method Name : adminTrashDeleteDoc
	 * @param docList
	 * @param map
	 * @param sessionVO
	 * @return
	 * @throws Exception Map<String,Object>
	 */
	@Transactional(propagation = Propagation.REQUIRED,rollbackFor ={ Exception.class,SQLException.class})
	public Map<String, Object> adminTrashDeleteDoc(List<HashMap<String, Object>> docList ,HashMap<String,Object> map,SessionVO sessionVO) throws Exception;
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 문서처리(삭제/완전삭제/휴지통비우기) 대상 목록 구하기.
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : documentValidList
	 * @param map
	 * @return
	 * @throws Exception List<HashMap<String,Object>>
	 */
	public List<HashMap<String, Object>>  documentValidList(HashMap<String,Object> map) throws Exception;
		
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 휴지통 문서 목록 구하기.
	 * 2. 처리내용
	 * </pre>
	 * @Method Name : wasteDocList
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> wasteDocList(HashMap<String,Object> map) throws Exception;
	
	/**
	 * <pre>
	 * 1. 개용 : 보존기간을 변경한다
	 * 2. 처리내용
	 * </pre>
	 * @Method Name : preservationYearUpdate
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> preservationYearUpdate(HashMap<String, Object> map, SessionVO sessionVO) throws Exception;
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 관리자 페이지내 문서상세정보를 가져온다.
	 * 2. 처리내용 : 문서수정시 체크아웃 및 로그기록으로 인해 트랜잭션처리 적용
	 * </pre>
	 * @Method Name : docCommonView
	 * @param map
	 * @param sessionVO
	 * @return
	 * @throws Exception Map<String,Object>
	 */
	@Transactional(propagation = Propagation.REQUIRED,rollbackFor ={ Exception.class,SQLException.class})
	public Map<String, Object> docCommonView(HashMap<String, Object> map, SessionVO sessionVO) throws Exception;

	/**
	 * <pre>
	 * 1. 개용 : 소유권 변경 문서 목록을 가져온다
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : ownerDocList
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> ownerDocList(HashMap<String, Object> map) throws Exception;
	
	/**
	 * <pre>
	 * 1. 개용 : 문서의 소유권을 변경한다.
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : chagneDocOwner
	 * @param map
	 * @param sessionVO
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> changeDocOwner(HashMap<String, Object> map, SessionVO sessionVO) throws Exception;
	
	/**
	 * <pre>
	 * 1. 개용 : 휴지통 비우기 대상 목록 구하기.
	 * 2. 처리내용
	 * </pre>
	 * @Method Name : allDocumentValidList
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<HashMap<String, Object>> allDocumentValidList(HashMap<String, Object> map) throws Exception ;

		
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 만기문서 만기처리 - 배치프로그램 
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : expiredDocProc
	 * @param docList
	 * @param map
	 * @return
	 * @throws Exception Map<String,Object>
	 */
	@Transactional(propagation = Propagation.REQUIRED,rollbackFor ={ Exception.class,SQLException.class})
	public Map<String, Object> expiredDocProc(List<HashMap<String, Object>> docList ,HashMap<String,Object> map) throws Exception;

	/**
	 * 
	 * <pre>
	 * 1. 개용 : 모든 버전 문서와 첨부파일 목록 리스트 가져오기 :: 공통함수
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : docAllVersionInfoList
	 * @param documentDao
	 * @param root_id
	 * @return List<DocumentVO>
	 */
	public Map<String, Object> docAllVersionInfoList(HashMap<String, Object> map);
	
	/**
	 * <pre>
	 * 1. 내용 : 문서 이력 목록 조회 :: 삭제예정
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : documentHtList
	 * @param map
	 * @return
	 */
	public Map<String, Object> documentHtList(HashMap<String, Object> map) throws Exception;
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 문서이력 목록 조회 : GRID
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : docHistoryList
	 * @param map
	 * @return
	 * @throws Exception Map<String,Object>
	 */
	public Map<String, Object> docHistoryList(HashMap<String, Object> map) throws Exception;
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 나의문서 목록에 대한 기본 리스트
	 * 2. 처리내용 : acl 필터링을 통한 문서 목록 가져오기
	 * </pre> 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> myDocumentBasicList(HashMap<String, Object> map) throws Exception;
	
	/**
	 *  
	 * <pre>
	 * 1. 개용 : 업무문서/개인문서 목록에 대한 기본 리스트
	 * 2. 처리내용 : acl 필터링을 통한 문서 목록 가져오기
	 * </pre> 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> workDocumentBasicList(HashMap<String, Object> map) throws Exception;
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 문서등록을 위한 정보 얻기 :: 공통함수(개인/업무문서함)
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : DocumentInfoForInserting
	 * @param map
	 * @param sessionVO
	 * @return
	 * @throws Exception Map<String,Object>
	 */
	public Map<String, Object> documentInfoForInserting(HashMap<String, Object> map,SessionVO sessionVO) throws Exception;
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 문서리스트(개인/업무) 등록/수정을 위한 공통 정보 얻기 :: 공통함수(개인/업무문서함)
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : DocumentListForInserting
	 * @param map
	 * @param sessionVO
	 * @return
	 * @throws Exception Map<String,Object>
	 */
	public Map<String, Object> documentListForInserting(HashMap<String, Object> map,SessionVO sessionVO) throws Exception;
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 각종 문서 수정 작업 시 
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : documentUpdate
	 * @param map
	 * @param documentVO
	 * @param sessionVO
	 * @return
	 * @throws Exception Map<String,Object>
	 */
	@Transactional(propagation = Propagation.REQUIRED,rollbackFor ={ Exception.class,SQLException.class})
	public Map<String, Object> documentUpdate(HashMap<String, Object> map, DocumentVO documentVO, SessionVO sessionVO) throws Exception;

	/**
	 * 
	 * <pre>
	 * 1. 개용 : doc_id 기준으로 관련문서 가져오기 
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : documentRelationDocByDoc_id
	 * @param map
	 * @return
	 * @throws Exception Map<String,Object>
	 */
	public Map<String, Object> documentRelationDocByDoc_id(HashMap<String, Object> map) throws Exception;

	/**
	 * <pre>
	 * 1. 개용 : 문서 이동 리스트 업데이트
	 * 2. 처리내용:
	 * </pre>
	 * @Method Name : moveDocListUpdate
	 * @param docList
	 * @param map
	 * @param sessionVO
	 * @return
	 * @throws Exception
	 */
	@Transactional(propagation = Propagation.REQUIRED,rollbackFor ={ Exception.class,SQLException.class})
	public Map<String, Object> moveDocListUpdate(List<HashMap<String, Object>> docList ,HashMap<String,Object> map,SessionVO sessionVO) throws Exception;
	
	/**
	 * <pre>
	 * 1. 개용 : 문서 히스토리 목록 체크
	 * 2. 처리내용 : before_nm, after_nm을 구한다.
	 * </pre>
	 * @Method Name : moveDocumentHT
	 * @param oriFolderId
	 * @param targetFolderId
	 * @return
	 * @throws Exception
	 */
	public HashMap<String, Object> moveDocumentHT(String oriFolderId, String targetFolderId) throws Exception;
	
	/**
	 * <pre>
	 * 1. 개용 : 문서 소유권 변경 소유자 대상자 정보얻기
	 * 2. 처리내용 : before_nm, after_nm을 구한다.
	 * </pre>
	 * @Method Name : changeOwnerDocumentHT
	 * @param srcUserId
	 * @param desUserId
	 * @return
	 * @throws Exception
	 */
	public HashMap<String, Object> changeOwnerDocumentHT(String srcUserId, String desUserId) throws Exception;
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 문서 등록/수정 대상 유효성 체크
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : writeDocValid
	 * @param map
	 * @param documentVO
	 * @param sessionVO
	 * @return
	 * @throws Exception DocumentVO
	 */
	public DocumentVO  writeDocValid(HashMap<String,Object> map,DocumentVO documentVO,SessionVO sessionVO) throws Exception;
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 문서 등록/수정 처리
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : writeDocProc
	 * @param map
	 * @param documentVO
	 * @param attrList
	 * @return
	 * @throws Exception Map<String,Object>
	 */
	@Transactional(propagation = Propagation.REQUIRED,rollbackFor ={ Exception.class,SQLException.class})
	public Map<String, Object>  writeDocProc(HashMap<String,Object> map,DocumentVO documentVO,List<HashMap<String,Object>> attrList,SessionVO sessionVO) throws Exception;
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 문서 등록/수정 확장속성 정보 가져오기
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : docExtendedAttrList
	 * @param request
	 * @param doc_type
	 * @return
	 * @throws Exception List<HashMap<String,Object>>
	 */
	public List<HashMap<String,Object>> docExtendedAttrList(HttpServletRequest request,String doc_type) throws Exception;
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 문서수정을 위한 권한 체크
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : docCommonUpdateValid
	 * @param map
	 * @param sessionVO
	 * @return
	 * @throws Exception Map<String,Object>
	 */
	public Map<String, Object> docCommonUpdateValid(HashMap<String,Object> map,SessionVO sessionVO) throws Exception;
	
	public Map<String, Object> copyDocListUpdate(List<HashMap<String, Object>> copyList, HashMap<String, Object> map, SessionVO sessionVO) throws Exception;
	
	/**
	 * <pre>
	 * 1. 개용 : 즐겨찾기 문서 목록 체크
	 * 2. 처리내용 :
	 * </pre>
	 * @Method Name : favoriteDocValidList
	 * @param map
	 * @param sessionVO
	 * @return
	 * @throws Exception
	 */
	public List<HashMap<String,Object>> favoriteDocValidList(HashMap<String, Object> map, SessionVO sessionVO) throws Exception;
	
	/**
	 * <pre>
	 * 1. 개용 : 즐거찾기 문서 추가
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : favoriteInsert
	 * @param docList
	 * @param map
	 * @param sessionVO
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> favoriteInsert(List<HashMap<String,Object>> docList, HashMap<String,Object>map, SessionVO sessionVO) throws Exception;
	
	/**
	 * <pre>
	 * 1. 개용 : 즐거찾기 문서 삭제
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : favoriteDelete
	 * @param docList
	 * @param map
	 * @param sessionVO
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> favoriteDelete(List<HashMap<String,Object>> docList, HashMap<String,Object>map, SessionVO sessionVO) throws Exception;
	
	
	/**
	 * <pre>
	 * 1. 개용 : 체크아웃 취소 문서 목록 체크
	 * 2. 처리내용 :
	 * </pre>
	 * @Method Name : cancelCheckoutValidList
	 * @param map
	 * @param sessionVO
	 * @return
	 * @throws Exception
	 */
	public List<HashMap<String, Object>> cancelCheckoutValidList(HashMap<String,Object> map, SessionVO sessionVO) throws Exception;
	
	/**
	 * <pre>
	 * 1. 개용 : 동일 문서명 체크
	 * 2. 처리내용 : 동일 문서명이 있으면 새로운 문서명을 얻는다
	 * </pre>
	 * @Method Name : getUniqueDocumentName
	 * @param resourceName
	 * @param folderId
	 * @param actionStatus
	 * @return
	 * @throws Exception
	 */
	public String getUniqueDocumentName(String resourceName, String folderId, String actionStatus) throws Exception;
	
	/**
	 * <pre>
	 * 1. 개용 : 문서 복원 목록 체크
	 * 2. 처리내용:
	 * </pre>
	 * @Method Name : restoreValidList
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<HashMap<String, Object>> restoreValidList(HashMap<String,Object> map) throws Exception;

	/**
	 * <pre>
	 * 1. 개용 : 문서 복원 처리
	 * 2. 처리내용:
	 * </pre>
	 * @Method Name : restoreDocument
	 * @param docList
	 * @param map
	 * @param sessionVO
	 * @return
	 * @throws Exception
	 */
	@Transactional(propagation = Propagation.REQUIRED,rollbackFor ={ Exception.class,SQLException.class})
	public Map<String, Object> restoreDocument(List<HashMap<String, Object>> docList ,HashMap<String,Object> map,SessionVO sessionVO) throws Exception;
	
	/**
	 * <pre>
	 * 1. 개용 : 문서 이동 대상 목록 체크
	 * 2. 처리내용 :
	 * </pre>
	 * @Method Name : moveCopyDocValidList
	 * @param map
	 * @param sessionVO
	 * @return
	 * @throws Exception
	 */
	public List<HashMap<String, Object>> moveCopyDocValidList(HashMap<String,Object> map, SessionVO sessionVO) throws Exception;
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 임시작업함 대상 목록 구하기/체크
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : tempDocValidList
	 * @param map
	 * @return
	 * @throws Exception List<HashMap<String,Object>>
	 */
	public List<HashMap<String, Object>> tempDocValidList(HashMap<String,Object> map, SessionVO sessionVO) throws Exception;
	
	/**
	 * <pre>
	 * 1. 개용 : 임시작업함 문서 추가
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : tempDocInsert
	 * @param docList
	 * @param map
	 * @param sessionVO
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> tempDocInsert(List<HashMap<String,Object>> docList, HashMap<String,Object>map, SessionVO sessionVO) throws Exception;

	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 사용자 휴지통 문서 비우기 목록 체크
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : authWasteDocValidList
	 * @param map
	 * @return
	 * @throws Exception List<HashMap<String,Object>>
	 */
	public List<HashMap<String, Object>> authWasteDocValidList(HashMap<String,Object> map, SessionVO sessionVO) throws Exception;
		
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 문서 상세검색에서 체크된 확장문서
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : extendedAttrListByDoc_type
	 * @param request
	 * @param doc_type
	 * @return
	 * @throws Exception List<AttrVO>
	 */
	public List<AttrVO> extendedAttrListByDocType(HttpServletRequest request, String doc_type) throws Exception;
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : URL LINK 유효기간 설정 값 가져오기
	 * 2. 처리내용 
	 * </pre>
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> urlLinkInfo(HashMap<String, Object> map) throws Exception;
	
	
	/**
	 * <pre>
	 * 1. 개용 : 임시작업함에 문서를 제외한다
	 * 2. 처리내용
	 * </pre>
	 * @Method Name : tempDocDelete
	 * @param docList
	 * @param map
	 * @param sessionVO
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> tempDocDelete(List<HashMap<String,Object>> docList, HashMap<String,Object>map, SessionVO sessionVO) throws Exception;
	
	/**
	 * <pre>
	 * 1. 개용 : 관련문서에 root_doc_id가 있는지 확인한다.
	 * 2. 처리내용 
	 * </pre>
	 * @Method Name : tempRefDocIsUsing
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> tempRefDocIsUsing(HashMap<String,Object> map) throws Exception;

	/**
	 * <pre>
	 * 1. 개용 : 관련문서 목록 체크
	 * 2. 처리내용 : 중복 리스트 체크
	 * </pre>
	 * @Method Name : tempRefValidList
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<HashMap<String,Object>> tempRefValidList(HashMap<String,Object>map) throws Exception;
	
	/**
	 * <pre>
	 * 1. 개용 : 임시작업함 > 관련문서 등록
	 * 2. 처리내용 : 관련문서를 등록한다
	 * </pre>
	 * @Method Name : tempRefInsert
	 * @param docList
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> tempRefInsert(List<HashMap<String, Object>> docList, HashMap<String,Object> map) throws Exception;
	
	/**
	 * <pre>
	 * 1. 개용 : 임시작업함 > 다운로드
	 * 2. 처리내용 : doc_id로 첨부파일 목록을 구성한다
	 * </pre>
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public HashMap<String,Object> getPageListByTempDocList(HashMap<String,Object>map) throws Exception;
	
	/**
	 * <pre>
	 * 1. 내용 : 문서 댓글 목록 조회 
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : documentCommentList
	 * @param map
	 * @return
	 */
	public Map<String, Object> documentCommentList(HashMap<String, Object> map) throws Exception;
	
	/**
	 * <pre>
	 * 1. 내용 : 문서 댓글 목록 수정
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : docCommentUpdate
	 * @param map
	 * @return
	 */
	public int docCommentAction(HashMap<String, Object> map, SessionVO sessionVO) throws Exception;
	
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 메인 문서목록 리스트 가져오기
	 * 2. 처리내용 : acl 필터링을 통한 문서 목록 가져오기
	 * </pre> 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> mainDocumentBasicList(HashMap<String, Object> map) throws Exception;

	/**
	 * 
	 * <pre>
	 * 1. 개용 : 조회수 업데이트
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : docReadCountUpdate
	 * @param map
	 * @return map
	 */
	public int docReadCountUpdate(HashMap<String, Object> map) throws Exception;
}

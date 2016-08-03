package kr.co.exsoft.permission.dao;


import java.util.List;
import java.util.HashMap;

import kr.co.exsoft.permission.vo.AclExItemVO;
import kr.co.exsoft.permission.vo.AclItemVO;
import kr.co.exsoft.permission.vo.AclVO;

import org.apache.commons.collections.map.CaseInsensitiveMap;
import org.springframework.stereotype.Repository;

/**
 * Acl 매퍼클래스
 * @author 패키지 개발팀
 * @since 2014.07.17
 * @version 3.0
 *
 */
@Repository(value = "aclDao")
public interface AclDao {
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 권한 목록 카운트
	 * 2. 처리내용 : 관리자는 전체, 사용자는 본인 소유 및 public(전체) 권한 카운트
	 * </pre>
	 * @Method Name : aclPagingCount
	 * @param map
	 * @return int
	 */
	public int aclPagingCount(HashMap<String,Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 권한 목록을 얻는다 
	 * 2. 처리내용 : 관리자는 전체, 사용자는 본인 소유 및 public(전체) 권한 목록
	 * </pre>
	 * @Method Name : aclList
	 * @param map
	 * @return List<AclVO>
	 */
	public List<AclVO> aclList(HashMap<String,Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : ACL 상세 정보를 얻는다.
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : aclDetail
	 * @param map
	 * @return AclVO
	 */
	public AclVO aclDetail(HashMap<String, Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 권한 상세조회 화면에서  
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : aclDetailItem
	 * @param map
	 * @return List<AclItemVO>
	 */
	public List<AclItemVO> aclDetailItems(HashMap<String, Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 폴더의 폴더/문서 권한 속성 체크 :: 문서등록시 사용됨
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : isAuthCheck
	 * @param map
	 * @return AclItemVO
	 */
	public AclItemVO isAuthCheck(HashMap<String, Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 문서확장 권한 목록 리스트 가져오기.
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : aclExDetailItems
	 * @param map
	 * @return List<AclExItemVO>
	 */
	public List<AclExItemVO> aclExDetailItems(HashMap<String, Object> map);

	/**
	 * <pre>
	 * 1. 개요 : 권한 등록
	 * 2. 처리내용 :
	 * </pre>
	 * @Method Name : aclWrite
	 * @param aclVO
	 * @return
	 */
	public int aclWrite(AclVO aclVO);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 권한 정보 변경
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : aclUpdate
	 * @param aclVO
	 * @return int
	 */
	public int aclUpdate(AclVO aclVO);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : acl_id로 XR_ACL 삭제
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : aclDelete
	 * @param map
	 * @return int
	 */
	public int aclDelete(HashMap<String, Object> map);
	
	/**
	 * <pre>
	 * 1. 개요 : 권한 아이템 등록
	 * 2. 처리내용 :
	 * </pre>
	 * @Method Name : aclItemWrite
	 * @param aclVO
	 * @return
	 */
	public int aclItemWrite(AclItemVO aclItemVO);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : acl_id에 해당하는 AclItems 삭제
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : aclItemDelete
	 * @param acl_id
	 * @param isDeleteOwner
	 * @return int
	 */
	public int aclItemDelete(HashMap<String, Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : doc_id에 대한 로그인한 사용자 권한 체크
	 * 2. 처리내용 : [없음|목록|조회|수정|삭제], 등록, 반출취소, 권한 변경에 대한 값을 가져 온다.
	 * </pre>
	 * @Method Name : alcListByDocumentIDs
	 * @param map
	 * @return List<CaseInsensitiveMap>
	 */
	public List<CaseInsensitiveMap> alcListByDocumentIDs(HashMap<String, Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : folder_id에 대한 로그인한 사용자 권한 체크
	 * 2. 처리내용 : [없음|목록|조회|수정|삭제], 등록, 권한 변경에 대한 값을 가져 온다.
	 * </pre>
	 * @Method Name : alcListByFolderIDs
	 * @param map
	 * @return List<CaseInsensitiveMap>
	 */
	public List<CaseInsensitiveMap> alcListByFolderIDs(HashMap<String, Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 문서 또늘 폴더 권한변경 작업 시 상위권한 정보를 가져오기
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : aclInheritDetail
	 * @param map
	 * @return AclVO
	 */
	public AclVO aclInheritDetail(HashMap<String, Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 문서 추가 접근자 삭제
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : aclExItemDelete
	 * @param map
	 * @return int
	 */
	public int aclExItemDelete(HashMap<String, Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 문서 추가 접근자 등록
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : aclExItemWrite
	 * @param aclExItemVO
	 * @return int
	 */
	public int aclExItemWrite(AclExItemVO aclExItemVO);
	
	/**
	 * <pre>
	 * 1. 개용 : AccessorID로 AclItem 목록 수를 카운트
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : aclItemCountByAccessorId
	 * @param param
	 * @return
	 */
	public int aclItemCountByAccessorId(HashMap<String, Object> param);
	
	/**
	 * <pre>
	 * 1. 개용 : Acl 이름으로 목록 수를 카운트
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : aclCountByAclName
	 * @param param
	 * @return
	 */
	public int aclCountByAclName(HashMap<String, Object> param);
	
	/**
	 * <pre>
	 * 1. 개용 : Acl 생성자 ID로 목록 수를 카운트
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : aclCountByCreatorId
	 * @param param
	 * @return
	 */
	public int aclCountByCreatorId(HashMap<String, Object> param);
	
}

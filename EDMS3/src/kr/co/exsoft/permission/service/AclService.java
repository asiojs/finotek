package kr.co.exsoft.permission.service;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.co.exsoft.common.vo.SessionVO;
import kr.co.exsoft.permission.vo.AclExItemVO;
import kr.co.exsoft.permission.vo.AclItemListVO;
import kr.co.exsoft.permission.vo.AclItemVO;
import kr.co.exsoft.permission.vo.AclVO;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


/**
 * Acl 서비스 인터페이스
 * @author 패키지 개발팀
 * @since 2014.07.17
 * @version 3.0
 *
 */
@Transactional
public interface AclService {
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : ACL List 가져오기
	 * 2. 처리내용 : 관리자 화면, 사용자 화면에 따른 조건 분리는 map에 담는다.
	 * </pre>
	 * @Method Name : aclList
	 * @param map
	 * @return
	 * @throws Exception List<AclVO>
	 */
	public Map<String, Object> aclList(HashMap<String,Object> map) throws Exception;
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : ACL 정보 가져오기
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : aclDetail
	 * @param map
	 * @return
	 * @throws Exception AclVO
	 */
	public Map<String, Object> aclDetail(HashMap<String,Object> map) throws Exception;
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : ACL 상세 정보에서 접근자 목록
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : aclItemList
	 * @param List<AclItemListVO>
	 * @return
	 * @throws Exception Map<String,Object>
	 */
	public List<AclItemListVO> aclItemList(HashMap<String,Object> map) throws Exception;
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 문서 추가 접근자 목록
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : aclItemList
	 * @param List<AclExItemVO>
	 * @return
	 * @throws Exception Map<String,Object>
	 */
	public List<AclExItemVO> exAclItemList(HashMap<String,Object> map) throws Exception;
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 권한 정보 수정
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : aclUpdate
	 * @param map
	 * @param aclVO
	 * @param aclItemList
	 * @param sessionVO
	 * @return
	 * @throws Exception Map<String,Object>
	 */
	@Transactional(propagation = Propagation.REQUIRED,rollbackFor ={ Exception.class,SQLException.class})
	public Map<String, Object> aclUpdate(HashMap<String,Object> map,AclVO aclVO,List<AclItemVO> aclItemList,SessionVO sessionVO) throws Exception;
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : ACL 신규 등록
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : aclWrite
	 * @param map
	 * @param aclVO
	 * @param aclItemList
	 * @param sessionVO
	 * @return
	 * @throws Exception Map<String,Object>
	 */
	@Transactional(propagation = Propagation.REQUIRED,rollbackFor ={ Exception.class,SQLException.class})
	public Map<String, Object> aclWrite(HashMap<String,Object> map,AclVO aclVO,List<AclItemVO> aclItemList,SessionVO sessionVO) throws Exception;
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 권한 삭제처리 Valid
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : aclDeleteValid
	 * @param map
	 * @return
	 * @throws Exception List<String>
	 */
	public List<String> aclDeleteValid(HashMap<String,Object> map) throws Exception;
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : ACL 삭제
	 * 2. 처리내용 : 등록된 문서(휴지통) 또는 폴더가 있을 경우 삭제 금지
	 *           ACL 속성 값, ACLItem List 객체
	 * </pre>
	 * @Method Name : aclDelete
	 * @param map
	 * @param delList
	 * @param sessionVO
	 * @return
	 * @throws Exception Map<String,Object>
	 */
	@Transactional(propagation = Propagation.REQUIRED,rollbackFor ={ Exception.class,SQLException.class})
	public Map<String, Object> aclDelete(HashMap<String,Object> map,List<String> delList,SessionVO sessionVO) throws Exception;
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : ACLITEM 정보 가져오기 - 문서등록/수정시
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : aclItem
	 * @param map
	 * @return
	 * @throws Exception Map<String,Object>
	 */
	public Map<String, Object> aclItem(HashMap<String,Object> map) throws Exception;
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 문서 또늘 폴더 권한변경 작업 시 상위권한 정보를 가져오기
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : aclInheritDetail
	 * @param map
	 * @return
	 * @throws Exception Map<String,Object>
	 */
	public Map<String, Object> aclInheritDetail(HashMap<String,Object> map) throws Exception;
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 문서 추가 접근자 등록
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : aclExItemWrite
	 * @param map
	 * @param aclExItemVO
	 * @param sessionVO
	 * @return
	 * @throws Exception Map<String,Object>
	 */
	@Transactional(propagation = Propagation.REQUIRED,rollbackFor ={ Exception.class,SQLException.class})
	public Map<String, Object> aclExItemWrite(HashMap<String,Object> map, AclExItemVO aclExItemVO, SessionVO sessionVO) throws Exception;
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 문서 추가 접근자 삭제
	 * 2. 처리내용 : 문서ID 기준으로 추가 접근자 삭제
	 * </pre>
	 * @Method Name : aclExItemDelete
	 * @param map
	 * @param delDoc_id
	 * @param sessionVO
	 * @return
	 * @throws Exception Map<String,Object>
	 */
	@Transactional(propagation = Propagation.REQUIRED,rollbackFor ={ Exception.class,SQLException.class})
	public Map<String, Object> aclExItemDelete(HashMap<String,Object> map, String delDoc_id, SessionVO sessionVO) throws Exception;
}

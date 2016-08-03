package kr.co.exsoft.external.service;

import java.util.List;
import java.util.HashMap;
import org.apache.commons.collections.map.CaseInsensitiveMap;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;
import java.sql.SQLException;



/**
 * External 서비스 인터페이스
 * @author 패키지 개발팀
 * @since 2014.07.17
 * @version 3.0
 *
 */
@Transactional
public interface ExternalService {

	/**
	 * <pre>
	 * 1. 개요 : 외부연계 샘플 조회 처리
	 * 2. 처리내용 : 
	 * - 사용자ID를 가지고 사용자 정보 조회 
	 * </pre>
	 * @Method Name : externalUserDetail
	 * @param map
	 * @return CaseInsensitiveMap
	 * @throws Exception
	 */
	public CaseInsensitiveMap externalUserDetail(HashMap<String,Object> map) throws Exception ;
	
	/**
	 * 
	 * <pre>
	 * 1. 개요 : 외부사용자 등록 처리 샘플
	 * 2. 처리내용 :
	 * </pre>
	 * @Method Name : externalUserWrite
	 * @param map
	 * @return int
	 * @throws Exception
	 */
	public int externalUserWrite(HashMap<String,Object> map) throws Exception ;
	
	/**
	 * 
	 * <pre>
	 * 1. 개요 : 외부사용자 등록 Transaction 처리 샘플
	 * 2. 처리내용 :
	 * </pre>
	 * @Method Name : externalUserWriteTx
	 * @param map
	 * @return int
	 * @throws Exception
	 */
	@Transactional(propagation = Propagation.REQUIRED,rollbackFor ={ Exception.class,SQLException.class})
	public int externalUserWriteTx(HashMap<String,Object> map) throws Exception ;
	
	/**
	 * 
	 * <pre>
	 * 1. 개요 : 외부사용자-부서 베치 등록 처리 샘플
	 * 2. 처리내용 :
	 * </pre>
	 * @Method Name : batchUserWrite
	 * @param userList
	 * @throws Exception
	 */
	@Transactional(propagation = Propagation.REQUIRED,rollbackFor ={ Exception.class,SQLException.class})
	public void batchUserWrite(List<HashMap<String,Object>> userList) throws Exception ;
	
}

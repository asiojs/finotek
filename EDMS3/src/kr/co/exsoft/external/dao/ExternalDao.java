package kr.co.exsoft.external.dao;

import java.util.HashMap;
import org.apache.commons.collections.map.CaseInsensitiveMap;

import org.springframework.stereotype.Repository;

/**
 * External 매퍼클래스
 * @author 패키지 개발팀
 * @since 2014.07.21
 * @version 3.0
 *
 */
@Repository(value = "externalDao")
public interface ExternalDao {

	/**
	 * 
	 * <pre>
	 * 1. 개요 : 외부 사용자 정보 조회 Sample
	 * 2. 처리내용 :
	 * </pre>
	 * @Method Name : externalUserDetail
	 * @param map
	 * @return
	 */
	public CaseInsensitiveMap externalUserDetail(HashMap<String,Object> map);

	/**
	 * 
	 * <pre>
	 * 1. 개요 : 외부 사용자 등록 처리 Sample
	 * 2. 처리내용 :
	 * </pre>
	 * @Method Name : externalUserWrite
	 * @param map
	 * @return
	 */
	public int externalUserWrite(HashMap<String,Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 개요 : 외부 사용자 소속부서 등록 처리 Sample
	 * 2. 처리내용 :
	 * </pre>
	 * @Method Name : externalGroupedWrite
	 * @param map
	 * @return
	 */
	public int externalGroupedWrite(HashMap<String,Object> map);
}

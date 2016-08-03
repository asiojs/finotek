package kr.co.exsoft.statistics.service;

import java.util.HashMap;
import java.util.Map;
import org.springframework.transaction.annotation.Transactional;

/**
 * 대량문서 열람 감사 서비스
 *
 * @author 패키지팀
 * @since 2014. 9. 15.
 * @version 1.0
 * 
 */

@Transactional
public interface AuditService {

	/**
	 * 
	 * <pre>
	 * 1. 개용 : 대량문서 열람 목록 가져오기. 
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : auditPageList
	 * @param map
	 * @return
	 * @throws Exception Map<String,Object>
	 */
	public Map<String, Object> auditPageList(HashMap<String,Object> map) throws Exception;
	
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : auditDetailPageList
	 * @param map
	 * @return
	 * @throws Exception Map<String,Object>
	 */
	public Map<String, Object> auditDetailPageList(HashMap<String,Object> map) throws Exception;
	
}

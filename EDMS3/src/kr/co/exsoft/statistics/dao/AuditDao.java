package kr.co.exsoft.statistics.dao;

import java.util.HashMap;
import java.util.List;
import org.apache.commons.collections.map.CaseInsensitiveMap;
import kr.co.exsoft.quartz.vo.AuditTrailVO;

import org.springframework.stereotype.Repository;

/**
 * 열람감사관리 DAO
 *
 * @author 패키지팀
 * @since 2014. 9. 15.
 * @version 1.0
 * 
 */
@Repository(value = "auditDao")
public interface AuditDao {

	/**
	 * 
	 * <pre>
	 * 1. 개용 : 대량문서 열람 카운트 가져오기.
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : auditPagingCount
	 * @param map
	 * @return int
	 */
	public int auditPagingCount(HashMap<String,Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 대량문서 열람 리스트 가져오기
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : auditPagingList
	 * @param map
	 * @return List<AuditTrailVO>
	 */
	public List<AuditTrailVO> auditPagingList(HashMap<String,Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 :  대량문서열람 상세 목록 카운트 
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : auditDetailCount
	 * @param map
	 * @return int
	 */
	public int auditDetailCount(HashMap<String,Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 대량문서열람 상세 목록 가져오기
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : auditDetailList
	 * @param map
	 * @return List<CaseInsensitiveMap>
	 */
	public List<CaseInsensitiveMap> auditDetailList(HashMap<String,Object> map);
	
	/**
	 * 감사기록 등록처리
	 * @param map
	 * @return
	 */
	public int writeAudit(HashMap<String,Object> map);
}

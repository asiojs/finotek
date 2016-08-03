package kr.co.exsoft.statistics.service;

import java.util.HashMap;
import java.util.Map;
import org.springframework.transaction.annotation.Transactional;


/**
 * Statistics 서비스 인터페이스
 * @author 패키지 개발팀
 * @since 2014.07.17
 * @version 3.0
 *
 */
@Transactional
public interface StatisticsService {
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 사용자별 등록/활용 현황 가져오기
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : userDocStatisticsList
	 * @param map
	 * @return
	 * @throws Exception Map<String,Object>
	 */
	public Map<String, Object> userDocStatisticsList(HashMap<String,Object> map) throws Exception;
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 부서별 등록/활용 현황 가져오기
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : groupDocStatisticsList
	 * @param map
	 * @return
	 * @throws Exception Map<String,Object>
	 */
	public Map<String, Object> groupDocStatisticsList(HashMap<String,Object> map) throws Exception;
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 기간별 등록/활용 현황 :: 사용자 일별/월별
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : decadeUserDocStatisticsList
	 * @param map
	 * @return
	 * @throws Exception Map<String,Object>
	 */
	public Map<String, Object> decadeUserDocStatisticsList(HashMap<String,Object> map) throws Exception;
	
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 기간별 등록/활용 현황 :: 부서 일별/월별
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : decadeGroupDocStatisticsList
	 * @param map
	 * @return
	 * @throws Exception Map<String,Object>
	 */
	public Map<String, Object> decadeGroupDocStatisticsList(HashMap<String,Object> map) throws Exception;
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 사용자/문서함별 소유 현황 
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : userFolderStatisticsList
	 * @param map
	 * @return
	 * @throws Exception Map<String,Object>
	 */
	public Map<String, Object> userFolderStatisticsList(HashMap<String,Object> map) throws Exception;
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 문서함/폴더별 보유 현황
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : FolderStatisticsList
	 * @param map
	 * @return
	 * @throws Exception Map<String,Object>
	 */
	public Map<String, Object> folderStatisticsList(HashMap<String,Object> map) throws Exception;
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 문서유형별 보유 현황 
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : statisticsTypeList
	 * @param map
	 * @return
	 * @throws Exception Map<String,Object>
	 */
	public Map<String, Object> statisticsTypeList(HashMap<String,Object> map) throws Exception;
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 보안등급별 보유 현황 
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : statisticsSecurityList
	 * @param map
	 * @return
	 * @throws Exception Map<String,Object>
	 */
	public Map<String, Object> statisticsSecurityList(HashMap<String,Object> map) throws Exception;
	
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 문서 Quota 현황
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : statisticsQuotaList
	 * @param map
	 * @return
	 * @throws Exception Map<String,Object>
	 */
	public Map<String, Object> statisticsQuotaList(HashMap<String,Object> map) throws Exception;
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 로그인 이력 현황
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : loginLogList
	 * @param map
	 * @return
	 * @throws Exception Map<String,Object>
	 */
	public Map<String, Object> loginLogList(HashMap<String,Object> map) throws Exception;
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 내문서 현황
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : myStatisticsList
	 * @param map
	 * @return
	 * @throws Exception Map<String,Object>
	 */
	public Map<String, Object> myStatisticsList(HashMap<String,Object> map) throws Exception;

}

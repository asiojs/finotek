package kr.co.exsoft.statistics.dao;

import java.util.HashMap;
import java.util.List;

import kr.co.exsoft.statistics.vo.DocumentDecadeVO;
import kr.co.exsoft.statistics.vo.DocumentGroupHtVO;
import kr.co.exsoft.statistics.vo.DocumentStatusVO;
import kr.co.exsoft.statistics.vo.DocumentUserHtVO;
import kr.co.exsoft.user.vo.ConnectLogVO;

import org.apache.commons.collections.map.CaseInsensitiveMap;
import org.springframework.stereotype.Repository;

/**
 * Statistics 매퍼클래스
 * @author 패키지 개발팀
 * @since 2014.07.21
 * @version 3.0
 *
 */
@Repository(value = "statisticsDao")
public interface StatisticsDao {

	/**
	 * 
	 * <pre>
	 * 1. 개용 : 사용자별 등록/활용 현황 목록수
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : userDocStatisticsCnt
	 * @param map
	 * @return int
	 */
	public int userDocStatisticsCnt(HashMap<String,Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 사용자별 등록/활용 현황 목록
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : userDocStatisticsList
	 * @param map
	 * @return List<CaseInsensitiveMap>
	 */
	public List<DocumentUserHtVO> userDocStatisticsList(HashMap<String,Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 부서별 등록/활용 현황 목록수
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : groupDocStatisticsCnt
	 * @param map
	 * @return int
	 */
	public int groupDocStatisticsCnt(HashMap<String,Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 부서별 등록/활용 현황 목록
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : groupDocStatisticsList
	 * @param map
	 * @return List<DocumentGroupHtVO>
	 */
	public List<DocumentGroupHtVO> groupDocStatisticsList(HashMap<String,Object> map);
	
	
	/***
	 * 
	 * <pre>
	 * 1. 개용 : 기간별 등록/활용 현황 :: 사용자 일별/월별
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : decadeUserDocStatisticsList
	 * @param map
	 * @return List<DocumentDecadeVO>
	 */
	public List<DocumentDecadeVO> decadeUserDocStatisticsList(HashMap<String,Object> map);
	
	/***
	 * 
	 * <pre>
	 * 1. 개용 : 기간별 등록/활용 현황 :: 부서 일별/월별
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : decadeUserDocStatisticsList
	 * @param map
	 * @return List<DocumentDecadeVO>
	 */
	public List<DocumentDecadeVO> decadeGroupDocStatisticsList(HashMap<String,Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 사용자/문서함별 소유 현황
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : userFoldertatisticsCnt
	 * @param map
	 * @return int
	 */
	public int userFoldertatisticsCnt(HashMap<String,Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 사용자/문서함별 소유 현황
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : userFolderStatisticsList
	 * @param map
	 * @return List<DocumentStatusVO>
	 */
	public List<DocumentStatusVO> userFolderStatisticsList(HashMap<String,Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 문서함/폴더별 보유 현황 기준 폴더 리스트
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : statisticsFolderList
	 * @param map
	 * @return List<CaseInsensitiveMap>
	 */
	public List<CaseInsensitiveMap> statisticsFolderList(HashMap<String,Object> map);
	
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 문서함/폴더별 보유 현황 기준 폴더 리스트 수
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : statisticsFolderListCnt
	 * @param map
	 * @return int
	 */
	public int statisticsFolderListCnt(HashMap<String,Object> map);

	/**
	 * 
	 * <pre>
	 * 1. 개용 : 프로젝트 ROOT_ID
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : statisticsProjectRootId
	 * @param map
	 * @return String
	 */
	public String statisticsProjectRootId(HashMap<String,Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 문서유형별 보유 현황 리스트 가져오기.
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : statisticsTypeList
	 * @param map
	 * @return List<DocumentStatusVO>
	 */
	public List<DocumentStatusVO> statisticsTypeList(HashMap<String,Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 보안등급별 보유 현황 목록 가져오기
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : statisticsSecurityList
	 * @param map
	 * @return List<DocumentStatusVO>
	 */
	public List<DocumentStatusVO> statisticsSecurityList(HashMap<String,Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 보안등급별 보유 현황 목록 수
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : statisticsSecurityCnt
	 * @param map
	 * @return int
	 */
	public int statisticsSecurityCnt(HashMap<String,Object> map);

	/**
	 * 
	 * <pre>
	 * 1. 개용 : 문서 Quota 현황 페이지 목록 리스트
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : statisticsQuotaPageList
	 * @param map
	 * @return List<CaseInsensitiveMap>
	 */
	public List<CaseInsensitiveMap> statisticsQuotaPageList(HashMap<String,Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 문서 Quota 현황 페이지 목록 수
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : statisticsQuotaPageCnt
	 * @param map
	 * @return int
	 */
	public int statisticsQuotaPageCnt(HashMap<String,Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 문서 Quota 현황 리스트
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : statisticsQuotaList
	 * @param map
	 * @return List<DocumentStatusVO>
	 */
	public List<DocumentStatusVO> statisticsQuotaList(HashMap<String,Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 로그인 이력 목록 수
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : loginLogPageCnt
	 * @param map
	 * @return int
	 */
	public int loginLogPageCnt(HashMap<String,Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 로그인 이력 목록 가져오기
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : loginLogPageList
	 * @param map
	 * @return List<ConnectLogVO>
	 */
	public List<ConnectLogVO> loginLogPageList(HashMap<String,Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 내문서 현황
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : myStatisticsList
	 * @param map
	 * @return List<DocumentUserHtVO>
	 */
	public List<DocumentUserHtVO> myStatisticsList(HashMap<String,Object> map);
}


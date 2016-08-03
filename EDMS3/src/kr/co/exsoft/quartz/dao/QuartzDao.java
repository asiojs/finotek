package kr.co.exsoft.quartz.dao;

import java.util.HashMap;
import java.util.List;

import kr.co.exsoft.document.vo.PageVO;
import kr.co.exsoft.quartz.vo.BatchWorkVO;
import kr.co.exsoft.quartz.vo.FileQueueDeleteVO;

import org.apache.commons.collections.map.CaseInsensitiveMap;
import org.springframework.stereotype.Repository;

/**
 * Quartz 매퍼클래스
 * @author 패키지 개발팀
 * @since 2014.07.21
 * @version 3.0
 *
 */
@Repository(value = "quartzDao")
public interface QuartzDao {

	/**
	 * 
	 * <pre>
	 * 1. 개용 : 배치작업 로그 등록처리
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : batchWorkWrite
	 * @param batchWorkVO
	 * @return int
	 */
	public int batchWorkWrite(BatchWorkVO batchWorkVO);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 배치작업 로그 수행완료처리 
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : batchWorkUpdate
	 * @param map
	 * @return int
	 */
	public int batchWorkUpdate(HashMap<String,Object> map);
	
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 배치작업 수행여부 체크
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : isBatchWork
	 * @param map
	 * @return int
	 */
	public int isBatchWork(HashMap<String,Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 기준 열람수 초과자 목록 열람
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : auditExceedList
	 * @param map
	 * @return List<CaseInsensitiveMap>
	 */
	public List<CaseInsensitiveMap> auditExceedList(HashMap<String,Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 만기 처리 대상 목록 얻기
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : expiredDocList
	 * @param map
	 * @return List<HashMap<String,Object>>
	 */
	public List<CaseInsensitiveMap> expiredDocList(HashMap<String,Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 개인휴지통 대상 목록 얻기
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : privateTrashDocList
	 * @param map
	 * @return List<CaseInsensitiveMap>
	 */
	public List<CaseInsensitiveMap> privateTrashDocList(HashMap<String,Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 시스템 휴지통 대상 목록 얻기
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : systemTrashDocList
	 * @param map
	 * @return List<CaseInsensitiveMap>
	 */
	public List<CaseInsensitiveMap> systemTrashDocList(HashMap<String,Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : EDMS 시스템 관리자 정보 얻기
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : systemUserInfo
	 * @param map
	 * @return CaseInsensitiveMap
	 */
	public CaseInsensitiveMap systemUserInfo(HashMap<String,Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 사용자별 문서현황 집계
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : userDocStatus
	 * @param map
	 * @return List<CaseInsensitiveMap>
	 */
	public List<CaseInsensitiveMap> userDocStatus(HashMap<String,Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 부서별 문서현황 집계
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : groupDocStatus
	 * @param map
	 * @return List<CaseInsensitiveMap>
	 */
	public List<CaseInsensitiveMap> groupDocStatus(HashMap<String,Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 사용자별 문서현황 집계 등록처리
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : userDocHtWrite
	 * @param map
	 * @return int
	 */
	public int userDocHtWrite(HashMap<String,Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 부서별 문서현황 집계 등록처리
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : groupDocHtWrite
	 * @param map
	 * @return int
	 */
	public int groupDocHtWrite(HashMap<String,Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 폐기문서 첨부파일 삭제 목록 가져오기.
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : delPageList
	 * @param map
	 * @return List<PageVO>
	 */
	public List<PageVO> delPageList(HashMap<String,Object> map);
	
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 :DELETEFILE_QUEUE테이블 조회
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : delPageList
	 * @param map
	 * @return List<FileQueueDeleteVO>
	 */
	public List<FileQueueDeleteVO> fileQueueDeleteList(HashMap<String,Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 :DELETEFILE_QUEUE테이블 삭제
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : delPageList
	 * @param map
	 */
	public int deleteQueue(HashMap<String,Object> map);
	
	
	
	/***
	 * 
	 * <pre>
	 * 1. 개용 : 임시작업함 대상 목록 얻기
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : tempDelDocList
	 * @param map
	 * @return List<CaseInsensitiveMap>
	 */
	public List<CaseInsensitiveMap> tempDelDocList(HashMap<String,Object> map);	
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 임시작업함 삭제처리
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : tempDocDelete
	 * @param map
	 * @return int
	 */
	public int tempDocDelete(HashMap<String,Object> map);	
}

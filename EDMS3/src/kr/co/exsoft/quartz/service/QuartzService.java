package kr.co.exsoft.quartz.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import kr.co.exsoft.document.vo.PageVO;
import kr.co.exsoft.eframework.repository.EXrepClient;
import kr.co.exsoft.quartz.vo.FileQueueDeleteVO;

/**
 * 배치프로그램 서비스 인터페이스
 * @author 패키지 개발팀
 * @since 2014.07.17
 * @version 3.0
 *
 */
@Transactional
public interface QuartzService {

	/**
	 * 
	 * <pre>
	 * 1. 개용 : 배치작업 로그 등록처리
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : batchWorkWrite
	 * @param batchWorkVO
	 * @return
	 * @throws Exception int
	 */
	public int batchWorkWrite(long work_idx,String work_type,String work_nm) throws Exception ;

	/**
	 * 
	 * <pre>
	 * 1. 개용 : 
	 * 2. 처리내용 : 배치작업 로그 수행완료처리 
	 * </pre>
	 * @Method Name : batchWorkUpdate
	 * @param map
	 * @return
	 * @throws Exception int
	 */
	public int batchWorkUpdate(HashMap<String,Object> map) throws Exception ;

	/**
	 * 
	 * <pre>
	 * 1. 개용 : 배치작업 수행여부 체크
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : isBatchWork
	 * @param map
	 * @return
	 * @throws Exception boolean
	 */
	public boolean isBatchWork(HashMap<String,Object> map) throws Exception ;
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 기준 열람수 초과자 목록 열람
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : auditExceedList
	 * @param map
	 * @return
	 * @throws Exception List<HashMap<String,Object>>
	 */
	public List<HashMap<String,Object>> auditExceedList(HashMap<String,Object> map) throws Exception ;
	
	/**
	 * 기준 열람수 초과자 등록처리
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public int writeAudit(HashMap<String,Object> map) throws Exception ;
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 문서 배치처리 대상 목록 얻기 - 만기/개인휴지통/시스템휴지통
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : batchDocList
	 * @param map
	 * @return
	 * @throws Exception List<HashMap<String,Object>>
	 */
	public List<HashMap<String, Object>> batchDocList(HashMap<String,Object> map) throws Exception ;
	
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 :  시스템 관리자 정보 얻기..
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : systemUserInfo
	 * @param map
	 * @return
	 * @throws Exception HashMap<String,Object>
	 */
	public HashMap<String,Object> systemUserInfo(HashMap<String,Object> map) throws Exception;
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 사용자별/부서별 문서현황 집계 처리
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : userDocStatus
	 * @param map
	 * @throws Exception void
	 */
	public void docStatusProc(HashMap<String,Object> map) throws Exception;
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 폐기문서 첨부파일 삭제 목록 가져오기
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : delPageList
	 * @param map
	 * @return
	 * @throws Exception List<PageVO>
	 */
	public List<PageVO> delPageList(HashMap<String,Object> map) throws Exception;
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 폐기문서 첨부파일 삭제처리 :: eXrep 연동처리
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : delPageProc
	 * @param pageList
	 * @param eXrepClient
	 * @return
	 * @throws Exception HashMap<String,Object>
	 */
	public HashMap<String,Object> delPageProc(List<PageVO> pageList,EXrepClient eXrepClient ) throws Exception;
	
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : DELETEFILE_QUEUE 삭제 목록 가져오기
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : fileQueueDeleteList
	 * @param map
	 * @return
	 * @throws Exception List<FileQueueDeleteVO>
	 */
	public List<FileQueueDeleteVO> fileQueueDeleteList(HashMap<String,Object> map) throws Exception;
	
	

	/**
	 * 
	 * <pre>
	 * 1. 개용 : DELETEFILE_QUEUE 삭제처리 :: eXrep 연동처리
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : delFileProc
	 * @param pageList
	 * @param eXrepClient
	 * @return
	 * @throws Exception HashMap<String,Object>
	 */
	public HashMap<String,Object> delFileProc(List<FileQueueDeleteVO> fileQueueDeleteList,EXrepClient eXrepClient ) throws Exception;
	
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 임시작업함 삭제 대상 목록 얻기
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : tempDelDocList
	 * @param map
	 * @return
	 * @throws Exception List<HashMap<String,Object>>
	 */
	public List<HashMap<String,Object>> tempDelDocList(HashMap<String,Object> map) throws Exception;
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 :  임시작업함 문서 삭제처리 
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : tempDocDelete
	 * @param map
	 * @throws Exception void
	 */
	public void tempDocDelete(HashMap<String,Object> map) throws Exception;

	
}

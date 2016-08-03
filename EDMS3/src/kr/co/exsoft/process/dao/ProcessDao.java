package kr.co.exsoft.process.dao;
import java.util.HashMap;
import java.util.List;

import kr.co.exsoft.process.vo.ProcessExecutorVO;
import kr.co.exsoft.process.vo.ProcessVO;

import org.apache.commons.collections.map.CaseInsensitiveMap;
import org.springframework.stereotype.Repository;

/**
 * 협업 매퍼클래스
 * @author 패키지 개발팀
 * @since 2015.03.12
 * @version 3.0
 *
 */
@Repository(value = "processDao")
public interface ProcessDao {

	/**
	 * 
	 * <pre>
	 * 1. 개용 : 좌측 메뉴 상단엣 문서에 대한 count를 가져오기 
	 * 2. 처리내용 : 사용자 ID기준으로 협업 관련 문서 count를 가져오기
	 * </pre>
	 * @Method Name : processCount
	 * @param map(user_id, type)
	 * @return int
	 */
	public int processCount(HashMap<String,Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 협업 메뉴별 목록 카운트 
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : processListCount
	 * @param map
	 * @return int
	 */
	public int processListCount(HashMap<String,Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 협업 메뉴별 목록 
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : processList
	 * @param map
	 * @return List<ProcessVO>
	 */
	public List<ProcessVO> processList(HashMap<String,Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 협업 프로세스에 해당하는 실행자 목록
	 * 2. 처리내용 : process_id 기준으로 실행자(작성자,승인자,수신자) 목록을 가져온다
	 * </pre>
	 * @Method Name : processExcutorList
	 * @param map
	 * @return List<ProcessExecutorVO>
	 */
	public List<ProcessExecutorVO> processExcutorList(HashMap<String,Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 최근 등록한 업무 목록
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : processRecentlyRegistList
	 * @param map
	 * @return List<ProcessVO>
	 */
	public List<ProcessVO> processRecentlyRegistList(HashMap<String,Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 업무명 가져오기 
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : processInfo
	 * @param map
	 * @return ProcessVO
	 */
	public ProcessVO processInfo(HashMap<String,Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : doc_id로 folder_id를 가져 온다.
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : processFolderIdByDocId
	 * @param map
	 * @return String
	 */
	public String processFolderIdByDocId(HashMap<String,Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 업무(협업) 메인 정보 등록
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : inertProcess
	 * @param processVo
	 * @return int
	 */
	public int insertProcess(ProcessVO processVo);
	
	/**
	 * 업무(협업) 실행자 등록
	 * <pre>
	 * 1. 개용 : 
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : insertProcessExecutor
	 * @param proExecutorVo
	 * @return int
	 */
	public int insertProcessExecutor(ProcessExecutorVO proExecutorVo);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 업무(협업) 정보 변경
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : updateProcess
	 * @param processVo
	 * @return int
	 */
	public int updateProcess(ProcessVO processVo);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 업무(협업) 실행자 정보 변경
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : updateProcessExecutor
	 * @param processExecutorVo
	 * @return int
	 */
	public int updateProcessExecutor(ProcessExecutorVO processExecutorVo);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 현재 승인 할 승인자 정보 가져오기
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : currentApproverInfo
	 * @param map
	 * @return ProcessExecutorVO
	 */
	public ProcessExecutorVO currentApproverInfo(HashMap<String,Object> map);
}

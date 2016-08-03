package kr.co.exsoft.user.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;

import java.sql.SQLException;

import kr.co.exsoft.common.vo.SessionVO;
import kr.co.exsoft.user.vo.GroupVO;

/**
 * Group 서비스 인터페이스
 * @author 패키지 개발팀
 * @since 2014.07.17
 * @version 3.0
 *
 */
@Transactional
public interface GroupService {
	
	
	/**
	 * 
	 * <pre>
	 * 1. 개요 : 사용자 소속 부서 등록 처리(기본/겸직)
	 * 2. 처리내용 :
	 * </pre>
	 * @Method Name : groupedWrite
	 * @param map
	 * @return
	 */
	public int groupedWrite(HashMap<String,Object> map);

	
	/**
	 * 
	 * <pre>
	 * 1. 개요 : 사용자 소속 부서 배치 등록 처리 샘플
	 * 2. 처리내용 :
	 * </pre>
	 * @Method Name : batchUserWrite
	 * @param userList
	 * @throws Exception
	 */
	@Transactional(propagation = Propagation.REQUIRED,rollbackFor ={ Exception.class,SQLException.class})
	public void batchUserWrite(List<HashMap<String,Object>> userList) throws Exception ;
	
	/**
	 * 
	 * <pre>
	 * 1. 개요 : 최상위 부서 목록 조회
	 * 2. 처리내용 :
	 * </pre>
	 * @Method Name : rootGroupList
	 * @param map
	 * @throws Exception
	 */
	public List<GroupVO> rootGroupList(HashMap<String,Object> map) throws Exception;
	
	/**
	 * 
	 * <pre>
	 * 1. 개요 : 하위 부서 목록 조회
	 * 2. 처리내용 :
	 * </pre>
	 * @Method Name : childGroupList
	 * @param map
	 * @throws Exception
	 */
	public List<GroupVO> childGroupList(HashMap<String,Object> map) throws Exception;
	
	/**
	 * 
	 * <pre>
	 * 1. 개요 : 부서 상세정보 조회
	 * 2. 처리내용 :
	 * </pre>
	 * @Method Name : groupDetail
	 * @param groupId
	 * @throws Exception
	 */
	public GroupVO groupDetail(String groupId) throws Exception;
	
	/**
	 * 
	 * <pre>
	 * 1. 개요 : 부서 등록
	 * 2. 처리내용 :
	 * </pre>
	 * @Method Name : groupWrite
	 * @param map
	 * @throws Exception
	 */
	public Map<String, Object> groupWrite(GroupVO groupVO, SessionVO sessionVO) throws Exception;
	
	/**
	 * 
	 * <pre>
	 * 1. 개요 : 부서 수정
	 * 2. 처리내용 :
	 * </pre>
	 * @Method Name : groupUpdate
	 * @param map
	 * @throws Exception
	 */
	public Map<String, Object> groupUpdate(GroupVO groupVO, SessionVO sessionVO) throws Exception;
	
	/**
	 * 
	 * <pre>
	 * 1. 개요 : 부서 삭제
	 * 2. 처리내용 :
	 * </pre>
	 * @Method Name : groupDelete
	 * @param map
	 * @throws Exception
	 */
	public Map<String, Object> groupDelete(GroupVO groupVO, SessionVO sessionVO) throws Exception;
	
	/**
	 * 
	 * <pre>
	 * 1. 개요 : 부서 이동
	 * 2. 처리내용 :
	 * </pre>
	 * @Method Name : groupMove
	 * @param groupVO
	 * @param sessionVO
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> groupMove(GroupVO groupVO, SessionVO sessionVO) throws Exception;
	
	/**
	 *
	 * <pre>
	 * 1. 개요 : 부서 등록을 위한 유효성 검사
	 * 2. 처리내용 :
	 * </pre>
	 * @Method Name : groupWriteValid
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public GroupVO groupWriteValid(HashMap<String, Object> map) throws Exception;
	
	/**
	 * 
	 * <pre>
	 * 1. 개요 : 부서 수정을 위한 유효성 검사
	 * 2. 처리내용 :
	 * </pre>
	 * @Method Name : groupUpdateValid
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public GroupVO groupUpdateValid(HashMap<String, Object> map) throws Exception;
	
	/**
	 * 
	 * <pre>
	 * 1. 개요 : 부서 삭제를 위한 유효성 검사
	 * 2. 처리내용 :
	 * </pre>
	 * @Method Name : groupDeleteValid
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public GroupVO groupDeleteValid(HashMap<String, Object> map) throws Exception;
	
	/**
	 * 
	 * <pre>
	 * 1. 개요 : 부서 이동을 위한 유효성 검사
	 * 2. 처리내용 :
	 * </pre>
	 * @Method Name : groupMoveValid
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public GroupVO groupMoveValid(HashMap<String, Object> map) throws Exception;
	
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 일괄업로드 목록 가져오기 
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : groupExcelList
	 * @param fileName
	 * @return
	 * @throws Exception List<GroupVO>
	 */
	public List<GroupVO> groupExcelList(String fileName) throws Exception;
	
}

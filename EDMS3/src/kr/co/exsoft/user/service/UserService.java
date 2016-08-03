package kr.co.exsoft.user.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;

import java.sql.SQLException;
import javax.servlet.http.HttpServletRequest;
import kr.co.exsoft.user.vo.UserVO;


/**
 * User 서비스 인터페이스
 * @author 패키지 개발팀
 * @since 2014.07.17
 * @version 3.0
 *
 */
@Transactional
public interface UserService {


	/**
	 * <pre>
	 * 1. 개요 : 사용자 정보 조회 Sample
	 * 2. 처리내용 :
	 * - 사용자ID를 가지고 사용자 정보 조회
	 * </pre>
	 * @Method Name : UserDetail
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public UserVO UserDetail(HashMap<String,Object> map) throws Exception ;
	
	/**
	 * <pre>
	 * 1. 개요 : 사용자 상세 정보 및 그룹 정보 조회
	 * 2. 처리내용 :
	 * </pre>
	 * @Method Name : userGroupDetail
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public UserVO userGroupDetail(HashMap<String, Object> map) throws Exception;
	
	/**
	 * 
	 * <pre>
	 * 1. 개요 : 사용자 등록 트랜잭션 처리 Sample
	 * 2. 처리내용 :
	 * </pre>
	 * @Method Name : userWriteTx
	 * @param userVO 사용자 상세정보
	 * @param map 사용자-부서 정보
	 * @return int
	 * @throws Exception
	 */
	@Transactional(propagation = Propagation.REQUIRED,rollbackFor ={ Exception.class,SQLException.class})
	public int userWriteTx(UserVO userVO,HashMap<String,Object> map) throws Exception;
	
	/**
	 * 
	 * <pre>
	 * 1. 개요 : 사용자 로그인 처리 
	 * 2. 처리내용 :
	 * </pre>
	 * @Method Name : userLogin
	 * @param userVO
	 * @param request
	 * @return UserVO
	 * @throws Exception
	 */
	public UserVO userLogin(UserVO userVO,HttpServletRequest request) throws Exception;
	
	/**
	 * 
	 * <pre>
	 * 1. 개요 : 사용자 접속 로그 처리 - 실패시
	 * 2. 처리내용 :
	 * </pre>
	 * @Method Name : userLogFailWrite
	 * @param connectLogVO
	 * @param request
	 * @throws Exception
	 */
	public void userLogFailWrite(UserVO userVO,HashMap<String,Object> param,HttpServletRequest request) throws Exception;
	
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 그룹 사용자 목록 조회
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : groupUserList
	 * @param groupId
	 * @return
	 * @throws Exception Map<String,Object>
	 */
	public Map<String, Object> groupUserList(HashMap<String,Object> param) throws Exception;
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 사용자/부서 공통 검색 리스트 
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : userGroupSearch
	 * @param param
	 * @return
	 * @throws Exception Map<String,Object>
	 */
	public Map<String, Object> userGroupSearch(HashMap<String,Object> param) throws Exception;
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 사용자 목록 검색 
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : searchUserList
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> searchUserList(HashMap<String,Object> param) throws Exception;
	
	
	
	/**
	 * 
	 * <pre>
	 * 1. 개요 : 사용자 등록
	 * 2. 처리내용 :
	 * </pre>
	 * @Method Name : userWrite
	 * @param userVO
	 * @param groupedInfo
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> userWrite(UserVO userVO, HashMap<String, Object> groupedInfo) throws Exception;
	
	/**
	 * <pre>
	 * 1. 내용 : 사용자 수정
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : userUpdate
	 * @param userVO
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> userUpdate(List<UserVO> userList) throws Exception;
	
	/**
	 * <pre>
	 * 1. 내용 : 사용자 삭제
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : userDelete
	 * @param userList
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> userDelete(List<UserVO> userList) throws Exception;
	
	/**
	 * <pre>
	 * 1. 내용 : 사용자 부서 변경
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : userGroupedUpdate
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> userGroupedUpdate(List<UserVO> userList) throws Exception;

	
	/**
	 * <pre>
	 * 1. 개용 : 사용자 등록을 위한 유효성 검사 
	 * 2. 처리내용 :
	 * </pre>
	 * @Method Name : userWriteValid
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public UserVO userWriteValid(HashMap<String, Object> map) throws Exception;
	
	/**
	 * <pre>
	 * 1. 개용 : 사용자 수정을 위한 유효성 검사 
	 * 2. 처리내용 :
	 * </pre>
	 * @Method Name : userUpdateValid
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public UserVO userUpdateValid(HashMap<String, Object> map) throws Exception;
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 사용자 환경설정 및 비밀번호 변경처리 
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : userConfig
	 * @param map
	 * @return
	 * @throws Exception Map<String,Object>
	 */
	@Transactional(propagation = Propagation.REQUIRED,rollbackFor ={ Exception.class,SQLException.class})
	public Map<String, Object> userConfig(HashMap<String, Object> map) throws Exception;
	
	/**
	 * <pre>
	 * 1. 개용 : 사용자 삭제를 위한 유효성 검사 
	 * 2. 처리내용 :
	 * </pre>
	 * @Method Name : userDeleteValid
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<UserVO> userDeleteValid(HashMap<String, Object> map) throws Exception;
	
	public List<UserVO> userPassResetValid(HashMap<String, Object> map) throws Exception;
	
	public List<UserVO> userStatusUpdateValid(HashMap<String, Object> map) throws Exception;
	
	public List<UserVO> userGroupedUpdateValid(HashMap<String, Object> map) throws Exception;
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 사용자 일괄 등록처리 
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : userExcelList
	 * @param fileName
	 * @return
	 * @throws Exception List<UserVO>
	 */
	public List<UserVO> userExcelList(String fileName) throws Exception;
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 사용자 상세 정보 조회
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : userDetailInfo
	 * @param map
	 * @return
	 * @throws Exception UserVO
	 */
	public UserVO userDetailInfo(HashMap<String, Object> map) throws Exception;
	
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 퀵메뉴 관리
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : quickMenuProc
	 * @param map
	 * @return
	 * @throws Exception Map<String,Object>
	 */
	@Transactional(propagation = Propagation.REQUIRED,rollbackFor ={ Exception.class,SQLException.class})
	public Map<String, Object> quickMenuProc(HashMap<String, Object> map) throws Exception;
}


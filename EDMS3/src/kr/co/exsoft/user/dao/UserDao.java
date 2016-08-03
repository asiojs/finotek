package kr.co.exsoft.user.dao;

import java.util.List;
import java.util.HashMap;

import org.springframework.stereotype.Repository;
import org.apache.commons.collections.map.CaseInsensitiveMap;

import kr.co.exsoft.user.vo.QuickMenuVO;
import kr.co.exsoft.user.vo.UserVO;
import kr.co.exsoft.user.vo.ConnectLogVO;
import kr.co.exsoft.user.vo.LoginLogVO;

/**
 * User 매퍼클래스
 * @author 패키지 개발팀
 * @since 2014.07.17
 * @version 3.0
 *
 */
@Repository(value = "userDao")
public interface UserDao {
	
	/**
	 * 사용자 정보 조회 Sample
	 * @param map - user_id
	 * @return UserVO
	 */
	public UserVO userDetail(HashMap<String,Object> map);


	/**
	 * 
	 * <pre>
	 * 1. 개요 : 사용자 상세 정보 및 그룹 정보 조회
	 * 2. 처리내용 :
	 * </pre>
	 * @Method Name : userGroupDetail
	 * @param map
	 * @return
	 */
	public UserVO userGroupDetail(HashMap<String, Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 개요 : 사용자 상세 정보 등록처리
	 * 2. 처리내용 :
	 * </pre>
	 * @Method Name : userDetailWrite
	 * @param userVO
	 * @return int
	 */
	public int userDetailWrite(UserVO userVO);
	
	/**
	 * 
	 * <pre>
	 * 1. 개요 : 사용자 기본 정보 등록처리
	 * 2. 처리내용 :
	 * </pre>
	 * @Method Name : userDefaultWrite
	 * @param userVO
	 * @return int
	 */
	public int userDefaultWrite(UserVO userVO);
	
	
	/**
	 * 
	 * <pre>
	 * 1. 개요 : 사용자 히스토리 등록
	 * 2. 처리내용 :
	 * </pre>
	 * @Method Name : userHistoryWrite
	 * @param userVO
	 * @return
	 */
	public int userHistoryWrite(UserVO userVO);
	
	/**
	 * 
	 * <pre>
	 * 1. 개요 : 사용자 접속 로그 등록 처리.
	 * 2. 처리내용 :
	 * </pre>
	 * @Method Name : connectLogWrite
	 * @param connectLogVO
	 * @return int
	 */
	public int connectLogWrite(ConnectLogVO connectLogVO);
	
	
	/**
	 * 
	 * <pre>
	 * 1. 개요 : 사옹자 로그인 세션 정보 등록 처리.
	 * 2. 처리내용 :
	 * </pre>
	 * @Method Name : loginLogWrite
	 * @param loginLogVO
	 * @return int 
	 */
	public int loginLogWrite(LoginLogVO loginLogVO);
	
	
	/**
	 * 
	 * <pre>
	 * 1. 개요 : 사용자 접속로그 카운트
	 * 2. 처리내용 :
	 * </pre>
	 * @Method Name : connectLogCnt
	 * @param map
	 * @return int
	 */
	public int connectLogCnt(HashMap<String,Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 개요 : 사용자 접속로그 리스트
	 * 2. 처리내용 :
	 * </pre>
	 * @Method Name : connectLogList
	 * @param map
	 * @return List
	 */
	public List<ConnectLogVO> connectLogList(HashMap<String,Object> map);
	
	
	/**
	 * 
	 * <pre>
	 * 1. 개요 : 사용자 로그인 세션 정보 조회
	 * 2. 처리내용 :
	 * </pre>
	 * @Method Name : loginLogDetail
	 * @param map
	 * @return LoginLogVO
	 */
	public LoginLogVO loginLogDetail(HashMap<String,Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 개요 : 사용자 로그인 세션 정보 삭제
	 * 2. 처리내용 :
	 * </pre>
	 * @Method Name : loginLogDelete
	 * @param map
	 * @return int
	 */
	public int loginLogDelete(HashMap<String,Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 개요 : 사용자 리스트 카운트 
	 * 2. 처리내용 :
	 * </pre>
	 * @Method Name : userExists
	 * @param map
	 * @return int
	 */
	public int userExists(HashMap<String,Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 개요 : 사용자 리스트
	 * 2. 처리내용 :
	 * </pre>
	 * @Method Name : userList
	 * @param map
	 * @return List
	 */
	public List<UserVO> userList(HashMap<String,Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 개요 : 사용자 로그인 처리
	 * 2. 처리내용 :
	 * </pre>
	 * @Method Name : userLogin
	 * @param userVO
	 * @return UserVO
	 */
	public UserVO userLogin(UserVO userVO);
	
	
	/**
	 * 
	 * <pre>
	 * 1. 개요 : Named 사용자 수를 얻는다. 
	 * 2. 처리내용 :
	 * </pre>
	 * @Method Name : namedUserCount
	 * @return int 
	 */
	public int namedUserCount();
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 부서내 사용자 리스트
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : groupUserList
	 * @param map
	 * @return List<UserVO>
	 */
	public List<UserVO> groupUserList(HashMap<String,Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 사용자/부서 공통 검색 리스트 
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : userGroupSearch
	 * @param map
	 * @return List<CaseInsensitiveMap>
	 */
	public List<CaseInsensitiveMap> userGroupSearch(HashMap<String,Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 사용자 리스트 검색
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : searchUserList
	 * @param map
	 * @return List<UserVO>
	 */
	public List<UserVO> searchUserList(HashMap<String,Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 내용 : 사용자 수정
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : userUpdate
	 * @param userVO
	 * @return
	 */
	public int userUpdate(UserVO userVO);
	
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 사용자 상세정보 조회
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : userDetailInfo
	 * @param map
	 * @return UserVO
	 */
	public UserVO userDetailInfo(HashMap<String,Object> map);
	
	
	/**
	 * 
	 * <pre>
	 * 1. 내용 : 사용자 상세내용 수정
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : userDetailUpdate
	 * @param userVO
	 * @return
	 */
	public int userDetailUpdate(UserVO userVO);

	/**
	 * <pre>
	 * 1. 내용 : 사용자 삭제
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : userDelete
	 * @param map
	 * @return
	 */
	public int userDelete(HashMap<String,Object> map);
	
	/**
	 * <pre>
	 * 1. 내용 : 사용자 상세내용 삭제
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : userDetailDelete
	 * @param map
	 * @return
	 */
	public int userDetailDelete(HashMap<String,Object> map);
	
	/**
	 * <pre>
	 * 1. 개용 : 사용자 환경설정 삭제
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : userConfigDelete
	 * @param map
	 * @return int
	 */
	public int userConfigDelete(HashMap<String,Object> map);
	
	/**
	 * <pre>
	 * 1. 내용 : 사용자 관리부서 변경
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : updateManageGroup
	 * @param map
	 * @return
	 */
	public int updateManageGroup(HashMap<String, Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 사용자 환경설정 및 비밀번호 변경처리 -
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : userConfig
	 * @param map
	 * @return int
	 */
	public int userConfig(HashMap<String, Object> map);
	
	
	/**
	 * <pre>
	 * 1. 개용 : 사용자 스토리지 할당량 변경
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : updateUserStorageQuota
	 * @param userVO
	 * @return
	 */
	public int updateUserStorageQuota(UserVO userVO);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 퀵메뉴 정보 가져오기
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : quickMenuInfo
	 * @param map
	 * @return QuickMenuVO
	 */
	public List<QuickMenuVO>  quickMenuInfo(HashMap<String, Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : TOP 메뉴 정보 가져오기 
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : topQuickMenu
	 * @param map
	 * @return List<QuickMenuVO>
	 */
	public List<QuickMenuVO>  topQuickMenu(HashMap<String, Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 사용자별 퀵메뉴 정보 가져오기 
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : userQuickMenu
	 * @param map
	 * @return List<HashMap<String,Object>>
	 */
	public List<CaseInsensitiveMap> userQuickMenu(HashMap<String, Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 퀵메뉴 삭제처리
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : quickMenuDelete
	 * @param map
	 * @return int
	 */
	public int quickMenuDelete(HashMap<String, Object> map);
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 사용자별 퀵메뉴 설정하기
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : quickMenuWrite
	 * @param map
	 * @return int
	 */
	public int quickMenuWrite(HashMap<String, Object> map);
	
	/**
	 * <pre>
	 * 1. 개용 : 유저 config (사용자 개인 환경설정) 정보 등록
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : userConfigWrite
	 * @param userVO
	 * @return int
	 */
	public int userConfigWrite(UserVO userVO);
}

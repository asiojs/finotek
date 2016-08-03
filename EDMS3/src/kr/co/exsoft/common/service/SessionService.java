package kr.co.exsoft.common.service;

import java.sql.SQLException;
import java.util.HashMap;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import kr.co.exsoft.common.vo.SessionVO;
import kr.co.exsoft.user.vo.UserVO;

/**
 * 세션처리 인터페이스
 * @author 패키지 개발팀
 * @since 2014.08.05
 * @version 3.0
 *
 */
@Transactional
public interface SessionService {

	/**
	 * 
	 * <pre>
	 * 1. 개요 : 세션 저장 처리
	 * 2. 처리내용 :
	 * </pre>
	 * @Method Name : setSessionVO
	 * @param userVO
	 * @param request
	 * @param param
	 * @return SessionVO
	 * @throws Exception
	 */
	@Transactional(propagation = Propagation.REQUIRED,rollbackFor ={ Exception.class,SQLException.class})
	public SessionVO setSessionVO(UserVO userVO,HttpServletRequest request,HashMap<String,Object> param) throws Exception;
	
	
	/**
	 * 
	 * <pre>
	 * 1. 개요 : 로그아웃 처리
	 * 2. 처리내용 :
	 * </pre>
	 * @Method Name : sessionOut
	 * @param session
	 * @param sessionVO
	 * @throws Exception
	 */
	public void sessionOut(HttpSession session,SessionVO sessionVO,HttpServletRequest request,HashMap<String,Object> param) throws Exception;
	
	
}

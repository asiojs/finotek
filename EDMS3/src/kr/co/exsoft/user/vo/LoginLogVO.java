package kr.co.exsoft.user.vo;

/**
 * 사용자 로그인 세션 정보 VO
 * @author 패키지 개발팀
 * @since 2014.07.16
 * @version 3.0
 *
 */
public class LoginLogVO {

	// 테이블 객체(XR_LOGIN_LOG)
	private String user_id;							// 사용자 ID
	private String session_id;						// 세션 ID
	private String connect_ip;						// 접속 IP
	private String login_time;						// 로그인 시간	
	
	public LoginLogVO() {
		
		this.user_id = "";
		this.session_id = "";
		this.connect_ip = "";
		this.login_time = "";
		
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getSession_id() {
		return session_id;
	}

	public void setSession_id(String session_id) {
		this.session_id = session_id;
	}

	public String getConnect_ip() {
		return connect_ip;
	}

	public void setConnect_ip(String connect_ip) {
		this.connect_ip = connect_ip;
	}

	public String getLogin_time() {
		return login_time;
	}

	public void setLogin_time(String login_time) {
		this.login_time = login_time;
	}
	
	
	
}

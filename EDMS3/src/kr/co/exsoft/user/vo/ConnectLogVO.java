package kr.co.exsoft.user.vo;

import kr.co.exsoft.eframework.vo.VO;

/**
 * 사용자 접속로그 VO
 * @author 패키지 개발팀
 * @since 2014.07.16
 * @version 3.0
 *
 */
public class ConnectLogVO extends VO {

	// 테이블 객체(XR_CONNECT_LOG)
	private long connect_log_seq;							// XR_COUNTER SEQ
	private String user_id;										// 사용자 ID
	private String user_nm;									// 사용자명	
	private String group_id;									// 소속그룹 ID
	private String group_nm;									// 소속그룹명
	private String login_type;									// 로그인타입 = NORMAL - 일반웹접속 / SSO - Single Sign On 접속
	private String connect_ip;									// 접속IP = IPV6 : 2001:0230:abcd:ffff:0000:0000:ffff:1111
	private String connect_type;								// 접속구분 = LOGIN/LOGOUT
	private String cert_yn;										// 인증여부 = Y:인증성공 N:인증실패(패스워드/라이선스 에러등)	
	private String error_cd;										// 에러코드
	private String error_content;								// 에러내용
	private String connect_time;								// 접속시간(종료시간) = 로그인-접속시간 / 로그아웃-종료시간
	
	// 조회항목
	private String cert_nm;										// 인증코드값
	
	public ConnectLogVO() {
		
		this.connect_log_seq = 0;
		this.user_id = "";
		this.user_nm = "";
		this.group_id = "";
		this.group_nm = "";
		this.login_type = "";
		this.connect_ip = "";
		this.connect_type = "";
		this.cert_yn = "";
		this.error_cd = "";
		this.error_content = "";
		this.connect_time = "";
		this.cert_nm = "";
	}
	
	public String getCert_nm() {
		return cert_nm;
	}

	public void setCert_nm(String cert_nm) {
		this.cert_nm = cert_nm;
	}

	public long getConnect_log_seq() {
		return connect_log_seq;
	}

	public void setConnect_log_seq(long connect_log_seq) {
		this.connect_log_seq = connect_log_seq;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getUser_nm() {
		return user_nm;
	}

	public void setUser_nm(String user_nm) {
		this.user_nm = user_nm;
	}

	public String getGroup_id() {
		return group_id;
	}

	public void setGroup_id(String group_id) {
		this.group_id = group_id;
	}

	public String getGroup_nm() {
		return group_nm;
	}

	public void setGroup_nm(String group_nm) {
		this.group_nm = group_nm;
	}

	public String getLogin_type() {
		return login_type;
	}

	public void setLogin_type(String login_type) {
		this.login_type = login_type;
	}

	public String getConnect_ip() {
		return connect_ip;
	}

	public void setConnect_ip(String connect_ip) {
		this.connect_ip = connect_ip;
	}

	public String getConnect_type() {
		return connect_type;
	}

	public void setConnect_type(String connect_type) {
		this.connect_type = connect_type;
	}

	public String getCert_yn() {
		return cert_yn;
	}

	public void setCert_yn(String cert_yn) {
		this.cert_yn = cert_yn;
	}

	public String getError_cd() {
		return error_cd;
	}

	public void setError_cd(String error_cd) {
		this.error_cd = error_cd;
	}

	public String getError_content() {
		return error_content;
	}

	public void setError_content(String error_content) {
		this.error_content = error_content;
	}

	public String getConnect_time() {
		return connect_time;
	}

	public void setConnect_time(String connect_time) {
		this.connect_time = connect_time;
	}
	
	
	
}

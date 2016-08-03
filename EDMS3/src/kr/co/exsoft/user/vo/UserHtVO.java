package kr.co.exsoft.user.vo;

/**
 * 사용자변경이력 VO
 * @author 패키지 개발팀
 * @since 2014.07.31
 * @version 3.0
 *
 */
public class UserHtVO {
	
	// 테이블 객체(XR_USER_HT)
	private long user_seq;												// 사용자 변경IDX : XR_COUNTER KEY='XR_USER_HT'
	private String user_id;												// 사용자ID
	private String emp_no;												// 사원번호
	private String user_name_ko;										// 	사용자명 - 한국어
	private String user_name_en;										// 	사용자명 - 영어	
	private String user_name_ja;										// 	사용자명 - 일본어
	private String user_name_zh;										// 	사용자명 - 중국어		
	private String group_id;											// 부서ID
	private String group_nm;											// 부서명
	private String jobtitle;												// 직위코드
	private String jobtitle_nm;										// 직위명
	private String position;												// 직책코드			
	private String position_nm;										// 직책명
	private String email;												// 이메일		
	private String telephone;											// 전화번호
	private String user_status;											// 사용자 상태 - C:활성(재직) U:수정 D:비활성(퇴사)
	private String role_id;												// 사용자 ROLE ID
	private String create_date;										// 수행일		
	private String status;												// 수행구분 - C:생성(입사),U:변경(소속변경/직위변경/직책변경),D:비활성(퇴사),R(사번변경),E(완전삭제) XR_CODE REF
	
	// 조회항목
	private String user_nm;											// 사용자명
	private String role_nm;												// ROLE명	
	private String user_status_nm;									// 사용자 상태명
	private String status_nm;											// 수행구분명
	
	public UserHtVO() {
		
		this.user_seq = 0;
		this.user_id = "";
		this.emp_no = "";
		this.user_name_ko = "";
		this.user_name_en = "";
		this.user_name_ja = "";
		this.user_name_zh = "";
		this.group_id = "";
		this.group_nm = "";
		this.jobtitle = "";
		this.jobtitle_nm = "";
		this.position = "";
		this.position_nm = "";
		this.email = "";
		this.telephone = "";
		this.user_status = "";
		this.role_id = "";
		this.create_date = "";
		this.status = "";
		this.user_nm = "";
		this.role_nm = "";
		this.user_status_nm = "";
		this.status_nm = "";
		
	}

	public long getUser_seq() {
		return user_seq;
	}

	public void setUser_seq(long user_seq) {
		this.user_seq = user_seq;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getEmp_no() {
		return emp_no;
	}

	public void setEmp_no(String emp_no) {
		this.emp_no = emp_no;
	}

	public String getUser_name_ko() {
		return user_name_ko;
	}

	public void setUser_name_ko(String user_name_ko) {
		this.user_name_ko = user_name_ko;
	}

	public String getUser_name_en() {
		return user_name_en;
	}

	public void setUser_name_en(String user_name_en) {
		this.user_name_en = user_name_en;
	}

	public String getUser_name_ja() {
		return user_name_ja;
	}

	public void setUser_name_ja(String user_name_ja) {
		this.user_name_ja = user_name_ja;
	}

	public String getUser_name_zh() {
		return user_name_zh;
	}

	public void setUser_name_zh(String user_name_zh) {
		this.user_name_zh = user_name_zh;
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

	public String getJobtitle() {
		return jobtitle;
	}

	public void setJobtitle(String jobtitle) {
		this.jobtitle = jobtitle;
	}

	public String getJobtitle_nm() {
		return jobtitle_nm;
	}

	public void setJobtitle_nm(String jobtitle_nm) {
		this.jobtitle_nm = jobtitle_nm;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getPosition_nm() {
		return position_nm;
	}

	public void setPosition_nm(String position_nm) {
		this.position_nm = position_nm;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getUser_status() {
		return user_status;
	}

	public void setUser_status(String user_status) {
		this.user_status = user_status;
	}

	public String getRole_id() {
		return role_id;
	}

	public void setRole_id(String role_id) {
		this.role_id = role_id;
	}

	public String getCreate_date() {
		return create_date;
	}

	public void setCreate_date(String create_date) {
		this.create_date = create_date;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getUser_nm() {
		return user_nm;
	}

	public void setUser_nm(String user_nm) {
		this.user_nm = user_nm;
	}

	public String getRole_nm() {
		return role_nm;
	}

	public void setRole_nm(String role_nm) {
		this.role_nm = role_nm;
	}

	public String getUser_status_nm() {
		return user_status_nm;
	}

	public void setUser_status_nm(String user_status_nm) {
		this.user_status_nm = user_status_nm;
	}

	public String getStatus_nm() {
		return status_nm;
	}

	public void setStatus_nm(String status_nm) {
		this.status_nm = status_nm;
	}
	
	
	
}

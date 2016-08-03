package kr.co.exsoft.rgate.vo;

/**
 * RGATE 매핑 VO
 * @author 패키지 개발팀
 * @since 2014.07.29
 * @version 3.0
 *
 */
public class RGateMappingVO {

	// 테이블 객체(XR_RGATE_MAPPING)
	private String ckey;											// 정책키(WORK_TYPE,USER_TYPE,USER_ID)
	private String work_type;									// 작업유형 - LSC_EXTENSION:확장자 LSC_EPROC:예외프로세스 LSC_CONTROL:사용여부 RGC_UNINSTALL_PASS : 삭제비번
	private String user_type;									// 사용자 유형  - ALL:전체 GROUP:그룹ID USER:사용자ID
	private String user_id;										// 사용자ID - ALL:NULL GROUP:그룹ID USER:사용자ID
	private String extension;									// 확장자 목록
	private String process;										// 프로세스 목록
	private String is_unactive;									// 저장금지 해제 설정	
	private String is_usb_active;								// USB 활성화 여부
	private String ip_address;									// IP 주소 목록
	private String passwd;										// 삭제 비번
	
	// 조회항목
	private String group_id;									// 그룹ID
	private String work_type_nm;							// 작업유형명
	private String user_type_nm;								// 사용자 유형명
	private String user_name;									// 사용자명
	private String group_name;								// 그룹명		
	
	public RGateMappingVO() {
		
		this.ckey = "";
		this.work_type = "";
		this.user_type = "";
		this.user_id = "";
		this.extension = "";
		this.process = "";
		this.is_unactive = "";
		this.is_usb_active = "";
		this.ip_address = "";
		this.passwd = "";
		this.work_type_nm = "";
		this.user_type_nm = "";
		this.user_name = "";
		this.group_name = "";
		this.group_id = "";
		
	}
	
	public String getIs_usb_active() {
		return is_usb_active;
	}

	public void setIs_usb_active(String is_usb_active) {
		this.is_usb_active = is_usb_active;
	}

	public String getGroup_id() {
		return group_id;
	}

	public void setGroup_id(String group_id) {
		this.group_id = group_id;
	}

	public String getUser_type_nm() {
		return user_type_nm;
	}

	public void setUser_type_nm(String user_type_nm) {
		this.user_type_nm = user_type_nm;
	}

	public String getWork_type_nm() {
		return work_type_nm;
	}

	public void setWork_type_nm(String work_type_nm) {
		this.work_type_nm = work_type_nm;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getGroup_name() {
		return group_name;
	}

	public void setGroup_name(String group_name) {
		this.group_name = group_name;
	}

	public String getCkey() {
		return ckey;
	}

	public void setCkey(String ckey) {
		this.ckey = ckey;
	}

	public String getWork_type() {
		return work_type;
	}

	public void setWork_type(String work_type) {
		this.work_type = work_type;
	}

	public String getUser_type() {
		return user_type;
	}

	public void setUser_type(String user_type) {
		this.user_type = user_type;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}

	public String getProcess() {
		return process;
	}

	public void setProcess(String process) {
		this.process = process;
	}

	public String getIs_unactive() {
		return is_unactive;
	}

	public void setIs_unactive(String is_unactive) {
		this.is_unactive = is_unactive;
	}

	public String getIp_address() {
		return ip_address;
	}

	public void setIp_address(String ip_address) {
		this.ip_address = ip_address;
	}

	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}
	
	
	
	
}

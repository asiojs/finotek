package kr.co.exsoft.quartz.vo;

/**
 * 감사기록 VO
 * @author 패키지 개발팀
 * @since 2014.07.31
 * @version 3.0
 *
 */
public class AuditTrailVO {

	// 테이블 객체(XR_AUDIT_TRAIL)
	private String audit_date;														// 감사일 - YYYYMMDD
	private String user_id;															// 사용자 아이디
	private int read_count;															// 조회수		
	private String report_mail_sent_date;										// 메일 발송일
	private String report_mail_receiver_address;								// 감사메일 수신주소	
	
	// 조회항목
	private String user_name;
	private String group_name;
	private String group_id;
	
	public AuditTrailVO() {
		
		this.audit_date = "";
		this.user_id = "";
		this.read_count = 0;
		this.report_mail_sent_date = "";
		this.report_mail_receiver_address = "";
	}
	
	public String getReport_mail_sent_date() {
		return report_mail_sent_date;
	}

	public void setReport_mail_sent_date(String report_mail_sent_date) {
		this.report_mail_sent_date = report_mail_sent_date;
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

	public String getGroup_id() {
		return group_id;
	}

	public void setGroup_id(String group_id) {
		this.group_id = group_id;
	}

	public String getAudit_date() {
		
		return this.audit_date.substring(0,4) + "-" + this.audit_date.substring(4,6) + "-" + this.audit_date.substring(6);
	}

	public void setAudit_date(String audit_date) {
		this.audit_date = audit_date;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public int getRead_count() {
		return read_count;
	}

	public void setRead_count(int read_count) {
		this.read_count = read_count;
	}

	public String getReport_mail_receiver_address() {
		return report_mail_receiver_address;
	}

	public void setReport_mail_receiver_address(String report_mail_receiver_address) {
		this.report_mail_receiver_address = report_mail_receiver_address;
	}
	
	
	
}

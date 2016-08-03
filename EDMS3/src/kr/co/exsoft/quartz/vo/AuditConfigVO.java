package kr.co.exsoft.quartz.vo;

/**
 * 감사환경설정 VO - 삭제예정
 * @author 패키지 개발팀
 * @since 2014.07.31
 * @version 3.0
 *
 */
public class AuditConfigVO {

	// 테이블 객체(XR_AUDIT_CONFIG)
	private int read_count_threshold;									// 감사조회 횟수 - 기준 열람건수 : 기본 100 , 최대조회건수 99999
	private String send_report_mail;										// 메일발송여부
	private String report_mail_receiver_address;						// 메일수신주소 : 다수 메일 구분자(;)		 
	
	public AuditConfigVO() {
		
		this.read_count_threshold = 0;
		this.send_report_mail = "";
		this.report_mail_receiver_address = "";
	}

	public int getRead_count_threshold() {
		return read_count_threshold;
	}

	public void setRead_count_threshold(int read_count_threshold) {
		this.read_count_threshold = read_count_threshold;
	}

	public String getSend_report_mail() {
		return send_report_mail;
	}

	public void setSend_report_mail(String send_report_mail) {
		this.send_report_mail = send_report_mail;
	}

	public String getReport_mail_receiver_address() {
		return report_mail_receiver_address;
	}

	public void setReport_mail_receiver_address(String report_mail_receiver_address) {
		this.report_mail_receiver_address = report_mail_receiver_address;
	}
	
	
	
	
}

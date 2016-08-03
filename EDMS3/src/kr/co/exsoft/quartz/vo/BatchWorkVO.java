package kr.co.exsoft.quartz.vo;

import kr.co.exsoft.eframework.configuration.Constant;

/**
 * 배치작업로그 VO
 * @author 패키지 개발팀
 * @since 2014.07.31
 * @version 3.0
 *
 */
public class BatchWorkVO {

	// 테이블 객체(XR_BATCHWORK)
	private long work_idx;													// 작업IDX - XR_COUNTER TABLE KEY='XR_BATCHWORK'
	private String work_nm;												// 작업명 - 만기문서관리/휴지통관리 등..
	private String work_type;												// 작업구분 - BATCH/WEB
	private String work_sdate;												// 작업시작시간 - YYYY-MM-DD HH24:MI:SS
	private String work_edate;											// 작업종료시간 - YYYY-MM-DD HH24:MI:SS
	private String work_state;												// 최종상태 - S:성공,F:실패
	private String message;												// 메세지 : 알림/에러	
	
	// 조회항목
	
	
	public BatchWorkVO() {
		
		this.work_idx = 0;
		this.work_nm = "";
		this.work_type = "";
		this.work_sdate = "";
		this.work_edate = "";
		this.work_state = Constant.F;
		this.message = "";
	}

	public long getWork_idx() {
		return work_idx;
	}

	public void setWork_idx(long work_idx) {
		this.work_idx = work_idx;
	}

	public String getWork_nm() {
		return work_nm;
	}

	public void setWork_nm(String work_nm) {
		this.work_nm = work_nm;
	}

	public String getWork_type() {
		return work_type;
	}

	public void setWork_type(String work_type) {
		this.work_type = work_type;
	}

	public String getWork_sdate() {
		return work_sdate;
	}

	public void setWork_sdate(String work_sdate) {
		this.work_sdate = work_sdate;
	}

	public String getWork_edate() {
		return work_edate;
	}

	public void setWork_edate(String work_edate) {
		this.work_edate = work_edate;
	}

	public String getWork_state() {
		return work_state;
	}

	public void setWork_state(String work_state) {
		this.work_state = work_state;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	
}

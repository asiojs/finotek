package kr.co.exsoft.common.vo;

/**
 * 첨부이력 VO
 *
 * @author 패키지팀
 * @since 2014. 11. 12.
 * @version 1.0
 * 
 */

public class PageHtVO {

	// 테이블 객체(XR_PAGE_HT)
	private long page_seq;															// 첨부이력 IDX
	private String doc_id;															// 문서루트 ID
	private String action_date;													// 수행(처리)일
	private String actor_id;															// 수행자ID
	private String actor_nm;														// 수행자명
	private String group_id;														// 부서ID
	private String group_nm;														// 부서명	
	private String action_id;														// 처리구분	 - READ		
	private String target_id;														// 업무대상 - XR_PAGE PAGE_ID		
	private String connect_ip;														// 접속IP
	private String action_place;													// 로그구분 - RGATE/EDMS
			
	public PageHtVO() {
		this.page_seq = 0;
		this.doc_id = "";
		this.action_date = "";
		this.actor_id = "";
		this.actor_nm = "";
		this.group_id = "";
		this.group_nm = "";
		this.action_id = "";
		this.target_id = "";
		this.connect_ip = "";
		this.action_place = "";
	}

	public long getPage_seq() {
		return page_seq;
	}

	public void setPage_seq(long page_seq) {
		this.page_seq = page_seq;
	}

	public String getDoc_id() {
		return doc_id;
	}

	public void setDoc_id(String doc_id) {
		this.doc_id = doc_id;
	}

	public String getAction_date() {
		return action_date;
	}

	public void setAction_date(String action_date) {
		this.action_date = action_date;
	}

	public String getActor_id() {
		return actor_id;
	}

	public void setActor_id(String actor_id) {
		this.actor_id = actor_id;
	}

	public String getActor_nm() {
		return actor_nm;
	}

	public void setActor_nm(String actor_nm) {
		this.actor_nm = actor_nm;
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

	public String getAction_id() {
		return action_id;
	}

	public void setAction_id(String action_id) {
		this.action_id = action_id;
	}

	public String getTarget_id() {
		return target_id;
	}

	public void setTarget_id(String target_id) {
		this.target_id = target_id;
	}

	public String getConnect_ip() {
		return connect_ip;
	}

	public void setConnect_ip(String connect_ip) {
		this.connect_ip = connect_ip;
	}

	public String getAction_place() {
		return action_place;
	}

	public void setAction_place(String action_place) {
		this.action_place = action_place;
	}
	
	
}

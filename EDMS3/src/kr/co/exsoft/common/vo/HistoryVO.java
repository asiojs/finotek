package kr.co.exsoft.common.vo;

/**
 * 폴더/문서유형/권한 이력 VO
 * @author 패키지 개발팀
 * @since 2014.07.29
 * @version 3.0
 *
 */
public class HistoryVO {
	
	// 테이블 객체(XR_HISTORY)
	private long history_seq;										// 히스토리 IDX - XR_COUNTER KEY='XR_HISTORY'
	private String basic_date;										// 수행(처리) 기준일 : YYYY-MM-DD HH24:MI:SS
	private String actor_id;											// 수행자ID
	private String actor_nm;										// 수행자명	
	private String group_id;										// 부서ID	
	private String group_nm;										// 부서명
	private String action_id;										// 처리구분	- CREATE/DELETE/MOVE/READ/CHEKCOUT/CHECKIN/TERMINDATE 
	private String target_type;										// 업무구분 - FOLDER/TYPE/ACL
	private String target_id;										// 업무대상 - FOLDER_ID/TYPE_ID/ACL_ID
	private String action_place;									// 로그구분 - RGATE/EDMS

	
	// 조회항목
	private String action_nm;										// 처리구분명 : XR_CODE REF GCODE_ID-ACTION_ID
	private String target_nm;										// 업무구분명 : XR_CODE REF GCODE_ID-TARGET_TYPE
	
	public HistoryVO() {
		
		this.history_seq = 0;
		this.basic_date = "";
		this.actor_id = "";
		this.actor_nm = "";
		this.group_id = "";
		this.group_nm = "";
		this.action_id = "";
		this.target_type = "";
		this.target_id = "";
		this.action_place = "";		
	}

	public long getHistory_seq() {
		return history_seq;
	}

	public void setHistory_seq(long history_seq) {
		this.history_seq = history_seq;
	}

	public String getBasic_date() {
		return basic_date;
	}

	public void setBasic_date(String basic_date) {
		this.basic_date = basic_date;
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

	public String getTarget_type() {
		return target_type;
	}

	public void setTarget_type(String target_type) {
		this.target_type = target_type;
	}

	public String getTarget_id() {
		return target_id;
	}

	public void setTarget_id(String target_id) {
		this.target_id = target_id;
	}

	public String getAction_place() {
		return action_place;
	}

	public void setAction_place(String action_place) {
		this.action_place = action_place;
	}

	public String getAction_nm() {
		return action_nm;
	}

	public void setAction_nm(String action_nm) {
		this.action_nm = action_nm;
	}

	public String getTarget_nm() {
		return target_nm;
	}

	public void setTarget_nm(String target_nm) {
		this.target_nm = target_nm;
	}
	
	
	
}


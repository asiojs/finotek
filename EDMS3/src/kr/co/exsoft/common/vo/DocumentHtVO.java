package kr.co.exsoft.common.vo;

import kr.co.exsoft.eframework.configuration.Constant;
import kr.co.exsoft.eframework.vo.VO;

/**
 * 문서이력 VO
 * @author 패키지 개발팀
 * @since 2014.07.31
 * @version 3.0
 *
 */
public class DocumentHtVO extends VO {
	
	// 테이블 객체(XR_DOCUMENT_HT)
	private long doc_seq;															// 문서이력 IDX
	private String root_id;															// 문서루트 ID
	private String action_date;													// 수행(처리)일
	private String actor_id;															// 수행자ID
	private String actor_nm;														// 수행자명
	private String group_id;														// 부서ID
	private String group_nm;														// 부서명	
	private String action_id;														// 처리구분	 - CREATE/DELETE/MOVE/READ/CHEKCOUT/CHECKIN/TERMINDATE/CHANGE_PERMISSION		
	private String target_id;														// 업무대상 - XR_DOCUMENT DOC_ID
	private String type_id;															// 문서유형 - XR_DOCUMENT/RGATE
	private String type_nm;														// 문서유형명 - 일반문서/파일문서
	private String connect_ip;														// 접속IP
	private String action_place;													// 로그구분 - RGATE/EDMS
	private String before_id;														// 문서이동:이전폴더ID / 소유권변경:이전소유자ID 										
	private String before_nm;														// 문서이동:이전폴더명 / 소유권변경:이전소유자명
	private String after_id;															// 문서이동:변경폴더ID / 소유권변경:변경소유자ID		 
	private String after_nm;														// 문서이동:이전폴더명 / 소유권변경:이전소유자명
	private String doc_name;														// 문서제목: 사용자/부서별 문서이력 상세조회
	private String version_no;														// 문서버전정보
	
	//조회항목
	private String action_name;													// 처리명	
	private String etc;																// 비고
	
	public DocumentHtVO() {
		
		this.doc_seq = 0;
		this.root_id = "";
		this.action_date = "";
		this.actor_id = "";
		this.actor_nm = "";
		this.group_id = "";
		this.group_nm = "";
		this.action_id = "";
		this.target_id = "";
		this.type_id = "";
		this.type_nm = "";
		this.connect_ip = "";
		this.action_place = "";
		this.before_id = "";
		this.before_nm = "";
		this.after_id = "";
		this.after_nm = "";
		this.doc_name = "";
		this.version_no = "1.0";
		this.action_name = "";
		this.etc = "";
	}
			
	public String getAction_name() {
		return action_name;
	}

	public void setAction_name(String action_name) {
		this.action_name = action_name;
	}

	public String getEtc() {
		
		if(this.action_id.equals(Constant.ACTION_MOVE))	{
			etc = this.before_nm + "폴더에서" + " " + this.after_nm + "폴더로 이동";
		}else if(this.action_id.equals(Constant.ACTION_CHANGE_CREATOR))	{
			etc = this.before_nm + "에서" + " " + this.after_nm + "소유권 이전";
		}else {
			etc = "";
		}
		return etc;
	}

	public void setEtc(String etc) {
		this.etc = etc;
	}

	public String getVersion_no() {
		return version_no;
	}

	public void setVersion_no(String version_no) {
		this.version_no = version_no;
	}

	public String getDoc_name() {
		return doc_name;
	}

	public void setDoc_name(String doc_name) {
		this.doc_name = doc_name;
	}

	public String getBefore_id() {
		return before_id;
	}

	public void setBefore_id(String before_id) {
		this.before_id = before_id;
	}

	public String getBefore_nm() {
		return before_nm;
	}

	public void setBefore_nm(String before_nm) {
		this.before_nm = before_nm;
	}

	public String getAfter_id() {
		return after_id;
	}

	public void setAfter_id(String after_id) {
		this.after_id = after_id;
	}

	public String getAfter_nm() {
		return after_nm;
	}

	public void setAfter_nm(String after_nm) {
		this.after_nm = after_nm;
	}


	public long getDoc_seq() {
		return doc_seq;
	}

	public void setDoc_seq(long doc_seq) {
		this.doc_seq = doc_seq;
	}

	public String getRoot_id() {
		return root_id;
	}

	public void setRoot_id(String root_id) {
		this.root_id = root_id;
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

	public String getType_id() {
		return type_id;
	}

	public void setType_id(String type_id) {
		this.type_id = type_id;
	}

	public String getType_nm() {
		return type_nm;
	}

	public void setType_nm(String type_nm) {
		this.type_nm = type_nm;
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

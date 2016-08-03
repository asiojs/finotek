package kr.co.exsoft.note.vo;

import kr.co.exsoft.eframework.configuration.Constant;
import kr.co.exsoft.eframework.util.StringUtil;

/**
 * 쪽지기본정보 VO
 * @author 패키지 개발팀
 * @since 2015.03.02
 * @version 3.0
 *
 */
public class NoteVO {

	private String note_id;														// 쪽지키값 :: 	XR_COUNTER REF
	private String root_id;														// 	대화함 구성을 위한 쪽지 수발신 그룹 키값
	private String create_date;												// 등록일
	private String creator_id;													// 등록자 아이디
	private String content;														// 쪽지 내용
	private String note_from;													// 수/발신자 모두 포함되어야 함=>답장 시 그대로 상속 받음			
	private String note_from_userid;										// 수신 사용자 아이디	
	private String note_from_groupid;										// 수신 그룹 아이디
	private String note_from_pgroupid;									// 수신 프로젝트 그룹 아이디	
	
	private String rsender_name;												// 수발신자 이름
	private long newnote_cnt;												// 새쪽지카운트
	private int note_kbn;														// tr구분위함
	
	private String manage_id;													// 쪽지 수/발신 정보 관리 키값 :: XR_COUNTER TABLE REF
	private String note_target_id;												// 수발신 대상자 ID
	private String note_type;													// 	수발신 여부, 수신 : R, 발신 : S
	private String note_save;													// 보관함 저장 여부, 저장 : Y, 미저장 : N
	private String note_read;													// 쪽지읽음 여부 :: 읽음-Y,읽지않음-N
	@SuppressWarnings("unused")
	private String subject;														// 발신자/내용
	@SuppressWarnings("unused")
	private String main_date;													// 메인쪽지 날짜
	
	public NoteVO() {
		
		this.note_id = "";
		this.root_id = "";
		this.create_date = "";
		this.creator_id = "";
		this.content = "";
		this.note_from  = "";
		this.note_from_userid = "";
		this.note_from_groupid = "";
		this.note_from_pgroupid = "";
		this.rsender_name="";
		this.newnote_cnt=0;
		this.note_kbn=0;		
		this.manage_id = "";
		this.note_target_id = "";
		this.note_type = "";
		this.note_save = "";
		this.note_read = Constant.NO;
		this.subject = "";
		this.main_date = "";
	}
	
	public String getMain_date() {		
		return (!StringUtil.isEmpty(create_date) && create_date.length() > 10 ) ? create_date.substring(0,10) : create_date;
	}

	public void setMain_date(String main_date) {
		this.main_date = main_date;
	}

	public String getSubject() {
		
		if(this.content.length() > 25)	{
			return "[" + this.rsender_name +"]" + this.content.substring(0,25) + "...";
		}else {
			return "[" + this.rsender_name +"]" + this.content;
		}
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public int getNote_kbn() {
		return note_kbn;
	}

	public void setNote_kbn(int note_kbn) {
		this.note_kbn = note_kbn;
	}
	public String getRsender_name() {
		return rsender_name;
	}

	public void setRsender_name(String rsender_name) {
		this.rsender_name = rsender_name;
	}

	public long getNewnote_cnt() {
		return newnote_cnt;
	}

	public void setNewnote_cnt(long newnote_cnt) {
		this.newnote_cnt = newnote_cnt;
	}

	public String getNote_id() {
		return note_id;
	}

	public void setNote_id(String note_id) {
		this.note_id = note_id;
	}

	public String getRoot_id() {
		return root_id;
	}

	public void setRoot_id(String root_id) {
		this.root_id = root_id;
	}

	public String getCreate_date() {
		return create_date;
	}

	public void setCreate_date(String create_date) {
		this.create_date = create_date;
	}

	public String getCreator_id() {
		return creator_id;
	}

	public void setCreator_id(String creator_id) {
		this.creator_id = creator_id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getNote_from() {
		return note_from;
	}

	public void setNote_from(String note_from) {
		this.note_from = note_from;
	}

	public String getNote_from_userid() {
		return note_from_userid;
	}

	public void setNote_from_userid(String note_from_userid) {
		this.note_from_userid = note_from_userid;
	}

	public String getNote_from_groupid() {
		return note_from_groupid;
	}

	public void setNote_from_groupid(String note_from_groupid) {
		this.note_from_groupid = note_from_groupid;
	}

	public String getNote_from_pgroupid() {
		return note_from_pgroupid;
	}

	public void setNote_from_pgroupid(String note_from_pgroupid) {
		this.note_from_pgroupid = note_from_pgroupid;
	}
	

	
	
	
	//====================================================

	public String getManage_id() {
		return manage_id;
	}

	public void setManage_id(String manage_id) {
		this.manage_id = manage_id;
	}

	public String getNote_target_id() {
		return note_target_id;
	}

	public void setNote_target_id(String note_target_id) {
		this.note_target_id = note_target_id;
	}

	public String getNote_type() {
		return note_type;
	}

	public void setNote_type(String note_type) {
		this.note_type = note_type;
	}

	public String getNote_save() {
		return note_save;
	}

	public void setNote_save(String note_save) {
		this.note_save = note_save;
	}

	public String getNote_read() {
		return note_read;
	}

	public void setNote_read(String note_read) {
		this.note_read = note_read;
	}

	
	
	
	
}

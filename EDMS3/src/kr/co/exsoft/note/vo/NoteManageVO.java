package kr.co.exsoft.note.vo;

import kr.co.exsoft.eframework.configuration.Constant;

/**
 * 쪽지 수/발신 정보 관리 VO
 * @author 패키지 개발팀
 * @since 2015.03.02
 * @version 3.0
 *
 */
public class NoteManageVO {
	
	private String manage_id;										// 쪽지 수/발신 정보 관리 키값 :: XR_COUNTER TABLE REF
	private String note_id;											// XR_NOTE.NOTE_ID
	private String root_id;											// XR_NOTE.ROOT_ID
	private String note_target_id;									// 수발신 대상자 ID
	private String note_type;										// 	수발신 여부, 수신 : R, 발신 : S
	private String note_save;										// 보관함 저장 여부, 저장 : Y, 미저장 : N
	private String note_read;										// 쪽지읽음 여부 :: 읽음-Y,읽지않음-N
	
	public NoteManageVO() {
		
		this.manage_id = "";
		this.note_id = "";
		this.root_id = "";
		this.note_target_id = "";
		this.note_type = "";
		this.note_save = "";
		this.note_read = Constant.NO;
	}
	
	public String getNote_read() {
		return note_read;
	}

	public void setNote_read(String note_read) {
		this.note_read = note_read;
	}

	public String getManage_id() {
		return manage_id;
	}

	public void setManage_id(String manage_id) {
		this.manage_id = manage_id;
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
	
	
}

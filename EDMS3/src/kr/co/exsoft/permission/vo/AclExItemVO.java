package kr.co.exsoft.permission.vo;

/**
 * 문서확장 권한속성 아이템 VO
 *
 * @author 패키지팀
 * @since 2014. 9. 24.
 * @version 1.0
 * 
 */

public class AclExItemVO {
	
	private String doc_id;										// 문서ID
	private String accessor_id;								// 접근자ID - ALIAS/USER_ID/GROUP_ID
	private String accessor_isgroup;							// 접근자 그룹여부 : 접근자ID가  GROUP_ID인 경우 T , 기본값 F로 변경
	private String accessor_isalias;							// 접근자 가칭 여부 : 접근자ID가 ALIAS(WORLD,OWNER,OGROUP) 인 경우 T
	private String act_browse;									// 목록보기(BROWSE) 권한 : 권한 있는 경우 T 없는 경우 F 
    private String act_read;									// 조회(READ) 권한  : 권한 있는 경우 T 없는 경우 F 
    private String act_update;									// 수정(UPDATE) 권한  : 권한 있는 경우 T 없는 경우 F 
    private String act_delete;									// 삭제(DELETE) 권한  : 권한 있는 경우 T 없는 경우 F 	
    private String act_create;									// 생성(CREATE)권한 : 문서권한에서는 제외된다.
    private String act_cancel_checkout;					// 반출취소(CANCEL CHECKOUT) 권한  : 권한 있는 경우 T 없는 경우 F 
    private String act_change_permission;					// 권한변경	(CHANGE PERMISSION) 권한 : 권한 있는 경우 T 없는 경우 F  

    // 조회항목
    private String accessor_name;
    private String doc_default_acl;							// 기본권한
    @SuppressWarnings("unused")
	private String doc_act_create;							// 문서기본권한 항목 통일 위해 값을 추가함
    @SuppressWarnings("unused")
	private String doc_act_cancel_checkout;
    @SuppressWarnings("unused")
	private String doc_act_change_permission;
    
    public AclExItemVO() {
    	
    	this.doc_id = "";
    	this.accessor_id = "";
    	this.accessor_isgroup = "F";
    	this.accessor_isalias = "F";
    	this.act_browse = "F";
    	this.act_read = "F";
    	this.act_create = "F";
    	this.act_update = "F";
    	this.act_delete = "F";
    	this.act_cancel_checkout = "F";
    	this.act_change_permission = "F";
    	this.accessor_name = "";
    	this.doc_default_acl = "";
    	this.doc_act_create = "";
    	this.doc_act_cancel_checkout = "";
    	this.doc_act_change_permission = "";

    }

	public String getDoc_act_create() {
		return act_create;
	}

	public void setDoc_act_create(String doc_act_create) {
		this.doc_act_create = doc_act_create;
	}

	public String getDoc_act_cancel_checkout() {
		return act_cancel_checkout;
	}

	public void setDoc_act_cancel_checkout(String doc_act_cancel_checkout) {
		this.doc_act_cancel_checkout = doc_act_cancel_checkout;
	}

	public String getDoc_act_change_permission() {
		return act_change_permission;
	}

	public void setDoc_act_change_permission(String doc_act_change_permission) {
		this.doc_act_change_permission = doc_act_change_permission;
	}

	public String getDoc_default_acl() {
		return doc_default_acl;
	}

	public void setDoc_default_acl(String doc_default_acl) {
		this.doc_default_acl = doc_default_acl;
	}

	public String getDoc_id() {
		return doc_id;
	}

	public void setDoc_id(String doc_id) {
		this.doc_id = doc_id;
	}

	public String getAccessor_id() {
		return accessor_id;
	}

	public void setAccessor_id(String accessor_id) {
		this.accessor_id = accessor_id;
	}

	public String getAccessor_isgroup() {
		return accessor_isgroup;
	}

	public void setAccessor_isgroup(String accessor_isgroup) {
		this.accessor_isgroup = accessor_isgroup;
	}

	public String getAccessor_isalias() {
		return accessor_isalias;
	}

	public void setAccessor_isalias(String accessor_isalias) {
		this.accessor_isalias = accessor_isalias;
	}

	public String getAct_browse() {
		return act_browse;
	}

	public void setAct_browse(String act_browse) {
		this.act_browse = act_browse;
	}

	public String getAct_read() {
		return act_read;
	}

	public void setAct_read(String act_read) {
		this.act_read = act_read;
	}

	public String getAct_update() {
		return act_update;
	}

	public void setAct_update(String act_update) {
		this.act_update = act_update;
	}

	public String getAct_delete() {
		return act_delete;
	}

	public void setAct_delete(String act_delete) {
		this.act_delete = act_delete;
	}

	public String getAct_create() {
		return act_create;
	}

	public void setAct_create(String act_create) {
		this.act_create = act_create;
	}

	public String getAct_cancel_checkout() {
		return act_cancel_checkout;
	}

	public void setAct_cancel_checkout(String act_cancel_checkout) {
		this.act_cancel_checkout = act_cancel_checkout;
	}

	public String getAct_change_permission() {
		return act_change_permission;
	}

	public void setAct_change_permission(String act_change_permission) {
		this.act_change_permission = act_change_permission;
	}

	public String getAccessor_name() {
		return accessor_name;
	}

	public void setAccessor_name(String accessor_name) {
		this.accessor_name = accessor_name;
	}
    
    
}

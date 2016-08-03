package kr.co.exsoft.permission.vo;

/**
 * 각종 상세화면에서 사용 될 접근자 VO
 * 폴더 및 문서에 대한 권한 조합 VO
 *
 * @author 패키지팀
 * @since 2014. 9. 19.
 * @version 1.0
 * 
 */

public class AclItemListVO {
	
	// 테이블 객체(XR_ACLITEM)
	private String acl_id;											// XR_ACL.ACL_ID
	private String accessor_id;										// 접근자ID - ALIAS/USER_ID/GROUP_ID
    private String accessor_name;
	private String accessor_isgroup;								// 접근자 그룹여부 : 접근자ID가  GROUP_ID인 경우 T , 기본값 F로 변경
	private String accessor_isalias;								// 접근자 가칭 여부 : 접근자ID가 ALIAS(WORLD,OWNER,OGROUP) 인 경우 T
	
	private String fol_act_browse;									// 목록보기(BROWSE) 권한 : 권한 있는 경우 T 없는 경우 F 
    private String fol_act_read;									// 조회(READ) 권한  : 권한 있는 경우 T 없는 경우 F 
    private String fol_act_update;									// 수정(UPDATE) 권한  : 권한 있는 경우 T 없는 경우 F 
    private String fol_act_delete;									// 삭제(DELETE) 권한  : 권한 있는 경우 T 없는 경우 F 	
    private String fol_act_create;									// 생성(CREATE)권한 : 문서권한에서는 제외된다.
    private String fol_act_change_permission;						// 폴더 권한 변경
    
    private String doc_act_browse;									// 목록보기(BROWSE) 권한 : 권한 있는 경우 T 없는 경우 F 
    private String doc_act_read;									// 조회(READ) 권한  : 권한 있는 경우 T 없는 경우 F 
    private String doc_act_update;									// 수정(UPDATE) 권한  : 권한 있는 경우 T 없는 경우 F 
    private String doc_act_delete;									// 삭제(DELETE) 권한  : 권한 있는 경우 T 없는 경우 F 	
    private String doc_act_create;									// 생성(CREATE)권한 : 문서권한에서는 제외된다.
    private String doc_act_cancel_checkout;							// 반출취소(CANCEL CHECKOUT) 권한  : 권한 있는 경우 T 없는 경우 F 
    private String doc_act_change_permission;						// 문서 권한 변경
    
    private String fol_default_acl;									// 폴더 현재 권한(기본권한)
    private String doc_default_acl;									// 문서 현재 권한(기본권한)
    
    public AclItemListVO() {
    	this.acl_id = "";
    	this.accessor_id = "";
    	this.accessor_isgroup = "";
    	this.accessor_isalias = "";
    	this.fol_act_browse = ""; 
    	this.fol_act_read = ""; 
    	this.fol_act_update = ""; 
    	this.fol_act_delete = ""; 	
    	this.fol_act_create = "";
    	this.fol_act_change_permission = "";
    	this.doc_act_browse = ""; 
    	this.doc_act_read = ""; 
    	this.doc_act_update = ""; 
    	this.doc_act_delete = ""; 	
    	this.doc_act_create = "";
    	this.doc_act_cancel_checkout = ""; 
    	this.doc_act_change_permission = "";
    	this.fol_default_acl = "";
    	this.doc_default_acl = "";
    	this.accessor_name = "";
    }

	public String getAcl_id() {
		return acl_id;
	}

	public void setAcl_id(String acl_id) {
		this.acl_id = acl_id;
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

	public String getFol_act_browse() {
		return fol_act_browse;
	}

	public void setFol_act_browse(String fol_act_browse) {
		this.fol_act_browse = fol_act_browse;
	}

	public String getFol_act_read() {
		return fol_act_read;
	}

	public void setFol_act_read(String fol_act_read) {
		this.fol_act_read = fol_act_read;
	}

	public String getFol_act_update() {
		return fol_act_update;
	}

	public void setFol_act_update(String fol_act_update) {
		this.fol_act_update = fol_act_update;
	}

	public String getFol_act_delete() {
		return fol_act_delete;
	}

	public void setFol_act_delete(String fol_act_delete) {
		this.fol_act_delete = fol_act_delete;
	}

	public String getFol_act_create() {
		return fol_act_create;
	}

	public void setFol_act_create(String fol_act_create) {
		this.fol_act_create = fol_act_create;
	}

	public String getFol_act_change_permission() {
		return fol_act_change_permission;
	}

	public void setFol_act_change_permission(String fol_act_change_permission) {
		this.fol_act_change_permission = fol_act_change_permission;
	}

	public String getDoc_act_browse() {
		return doc_act_browse;
	}

	public void setDoc_act_browse(String doc_act_browse) {
		this.doc_act_browse = doc_act_browse;
	}

	public String getDoc_act_read() {
		return doc_act_read;
	}

	public void setDoc_act_read(String doc_act_read) {
		this.doc_act_read = doc_act_read;
	}

	public String getDoc_act_update() {
		return doc_act_update;
	}

	public void setDoc_act_update(String doc_act_update) {
		this.doc_act_update = doc_act_update;
	}

	public String getDoc_act_delete() {
		return doc_act_delete;
	}

	public void setDoc_act_delete(String doc_act_delete) {
		this.doc_act_delete = doc_act_delete;
	}

	public String getDoc_act_create() {
		return doc_act_create;
	}

	public void setDoc_act_create(String doc_act_create) {
		this.doc_act_create = doc_act_create;
	}

	public String getDoc_act_cancel_checkout() {
		return doc_act_cancel_checkout;
	}

	public void setDoc_act_cancel_checkout(String doc_act_cancel_checkout) {
		this.doc_act_cancel_checkout = doc_act_cancel_checkout;
	}

	public String getDoc_act_change_permission() {
		return doc_act_change_permission;
	}

	public void setDoc_act_change_permission(String doc_act_change_permission) {
		this.doc_act_change_permission = doc_act_change_permission;
	}

	public String getFol_default_acl() {
		return fol_default_acl;
	}

	public void setFol_default_acl(String fol_default_acl) {
		this.fol_default_acl = fol_default_acl;
	}

	public String getDoc_default_acl() {
		return doc_default_acl;
	}

	public void setDoc_default_acl(String doc_default_acl) {
		this.doc_default_acl = doc_default_acl;
	}
	
	public String getAccessor_name() {
		return accessor_name;
	}

	public void setAccessor_name(String accessor_name) {
		this.accessor_name = accessor_name;
	}

}

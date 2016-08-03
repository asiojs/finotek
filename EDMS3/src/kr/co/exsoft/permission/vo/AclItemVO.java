package kr.co.exsoft.permission.vo;

/**
 * 권한속성 아이템 VO
 * @author 패키지 개발팀
 * @since 2014.07.29
 * @version 3.0
 *
 */
public class AclItemVO {

	// 테이블 객체(XR_ACLITEM)
	private String acl_id;										// XR_ACL.ACL_ID
	private String is_type;										// F : 폴더, D : 문서 권한
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
    
    public AclItemVO() {
    	this.acl_id = "";
    	this.is_type = "";
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
    }
    
    public AclItemVO(String aclId, String isType, String accessorId, String accessorIsGroup, String accessorIsAlias, String actBrowse,
    		String actRead, String actUpdate, String actDelete, String actCreate, String actCancelCheckout, String actChangePermission) {
    	this.acl_id = aclId;
    	this.is_type = isType;
    	this.accessor_id = accessorId;
    	this.accessor_isgroup = accessorIsGroup;
    	this.accessor_isalias = accessorIsAlias;
    	this.act_browse = actBrowse;
    	this.act_read = actRead;
    	this.act_update = actUpdate;
    	this.act_delete = actDelete;
    	this.act_create = actCreate;
    	this.act_cancel_checkout = actCancelCheckout;
    	this.act_change_permission = actChangePermission;
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

	public String getAct_create() {
		return act_create;
	}

	public void setAct_create(String act_create) {
		this.act_create = act_create;
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

	public String getAcl_id() {
		return acl_id;
	}

	public void setAcl_id(String acl_id) {
		this.acl_id = acl_id;
	}

	public String getIs_type() {
		return is_type;
	}

	public void setIs_type(String is_type) {
		this.is_type = is_type;
	}

	public String getAccessor_name() {
		return accessor_name;
	}

	public void setAccessor_name(String accessor_name) {
		this.accessor_name = accessor_name;
	}

}

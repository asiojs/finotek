package kr.co.exsoft.permission.vo;

import kr.co.exsoft.eframework.configuration.Constant;

/**
 * 권한(ACL) VO
 * @author 패키지 개발팀
 * @since 2014.07.29
 * @version 3.0
 *
 */
public class AclVO {

	// 테이블 객체(XR_ACL)
	private String acl_id;				// ACL_ID
	private String acl_name;			// 권한명			
	private String acl_type;			// 권한구분 ALL:전사, TEAM:부서, DEPT:하위부서포함, PRIVATE:사용자
	private String open_id;				// 공개대상 ID - 사용자/부서ID
	private String open_name;			// 공개대상 NAME - 사용자/부서NAME
	private String open_isgroup;		// 공개대상부서여부 : 공개대상이 부서 = T 공개대상이 사용자 = F
	private String creator_id;			// 권한 등록자(권한에 대한 수정, 삭제에 대하 권한 부여)								
	private String create_date;			// 권한 등록일
	private String sort_index;			// 1:개인(PRIVATE)>2:하위부서포함(DEPT)>3:부서(TEAM)>:4전사(ALL)
	
	// 조회항목
	private String acl_type_name;
	private String creator_name;
	
	public AclVO() {
		
		this.acl_id = "";
		this.acl_name = "";
		this.acl_type = "";
		this.open_id = "";
		this.open_name = "";
		this.open_isgroup = "F";
		this.acl_type_name = "";
		this.creator_id = "";
		this.create_date = "";
		this.creator_name = "";
		this.sort_index = "";
	}

	public String getAcl_id() {
		return acl_id;
	}

	public void setAcl_id(String acl_id) {
		this.acl_id = acl_id;
	}

	public String getAcl_name() {
		return acl_name;
	}

	public void setAcl_name(String acl_name) {
		this.acl_name = acl_name;
	}

	public String getAcl_type() {
		return acl_type;
	}

	public void setAcl_type(String acl_type) {
		this.acl_type = acl_type;
		
		// [onlyvJDK 1.7]
		switch (acl_type) {
		case Constant.ACL_ACL_TYPE_ALL:		setAcl_type_name(Constant.ACL_ACL_TYPE_ALL_NAME); 	break;
		case Constant.ACL_ACL_TYPE_TEAM:	setAcl_type_name(Constant.ACL_ACL_TYPE_TEAM_NAME);	break;
		case Constant.ACL_ACL_TYPE_DEPT:	setAcl_type_name(Constant.ACL_ACL_TYPE_DEPT_NAME); 	break;
		case Constant.ACL_ACL_TYPE_PRIVATE:	setAcl_type_name(Constant.ACL_ACL_TYPE_PRIVATE_NAME); break;
		default:break;
		}
	}

	public String getAcl_type_name() {
		return acl_type_name;
	}

	public void setAcl_type_name(String acl_type_name) {
		this.acl_type_name = acl_type_name;
	}

	public String getOpen_id() {
		return open_id;
	}

	public void setOpen_id(String open_id) {
		this.open_id = open_id;
	}

	public String getOpen_name() {
		return open_name;
	}

	public void setOpen_name(String open_name) {
		this.open_name = open_name;
	}

	public String getOpen_isgroup() {
		return open_isgroup;
	}

	public void setOpen_isgroup(String open_isgroup) {
		this.open_isgroup = open_isgroup;
	}

	public String getCreator_id() {
		return creator_id;
	}

	public void setCreator_id(String creator_id) {
		this.creator_id = creator_id;
	}

	public String getCreate_date() {
		return create_date;
	}

	public void setCreate_date(String create_date) {
		this.create_date = create_date;
	}

	public String getCreator_name() {
		return creator_name;
	}

	public void setCreator_name(String creator_name) {
		this.creator_name = creator_name;
	}

	public String getSort_index() {
		return sort_index;
	}

	public void setSort_index(String sort_index) {
		this.sort_index = sort_index;
	}
	
}

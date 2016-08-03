package kr.co.exsoft.document.vo;

import kr.co.exsoft.eframework.configuration.Constant;
/**
 * 문서유형 VO
 * @author 패키지 개발팀
 * @since 2014.08.19
 * @version 3.0
 *
 */
public class TypeVO {

	private String type_id;									// 문서유형ID XR_DOCUMENT : 일반문서, RGATE : 파일문서
	private String type_name;								// 문서유형명
	private String is_base;									// 기본여부 - XR_DOCUMENT 인 경우에만 T
	private String is_hidden;								// 히든여부 - T:사용안함 F:사용함 
	private String tbl_name;								// 테이블명 PREFIX T=>XR로 변경함
	private int sort_index;									// 정렬순서
	private String create_date;							// 등록일	
	private String is_modify;								// 수정가능여부 - F:수정불가(전자결재 이관문서인 경우)
	private String is_system;								// 시스템 제공여부 - T:시스템 제공 F:사용자 추가
	private String is_admin;								// 관리화면 제공여부
	private String status_nm;								// 상태명
	
	public TypeVO() {
		
		this.type_id = "";
		this.tbl_name = "";
		this.type_name = "";
		this.is_base = Constant.F;
		this.is_hidden = Constant.T;
		this.is_modify = Constant.T;
		this.is_system = Constant.F;
		this.is_admin = Constant.T;
		this.sort_index = 0;
		this.status_nm = "";
	}
	
	public String getStatus_nm() {
		return status_nm;
	}

	public void setStatus_nm(String status_nm) {
		this.status_nm = status_nm;
	}

	public int getSort_index() {
		return sort_index;
	}

	public void setSort_index(int sort_index) {
		this.sort_index = sort_index;
	}

	public String getType_id() {
		return type_id;
	}

	public void setType_id(String type_id) {
		this.type_id = type_id;
	}

	public String getType_name() {
		return type_name;
	}

	public void setType_name(String type_name) {
		this.type_name = type_name;
	}

	public String getIs_base() {
		return is_base;
	}

	public void setIs_base(String is_base) {
		this.is_base = is_base;
	}

	public String getIs_hidden() {
		return is_hidden;
	}

	public void setIs_hidden(String is_hidden) {
		this.is_hidden = is_hidden;
	}

	public String getTbl_name() {
		return tbl_name;
	}

	public void setTbl_name(String tbl_name) {
		this.tbl_name = tbl_name;
	}

	public String getCreate_date() {
		return create_date;
	}

	public void setCreate_date(String create_date) {
		this.create_date = create_date;
	}

	public String getIs_modify() {
		return is_modify;
	}

	public void setIs_modify(String is_modify) {
		this.is_modify = is_modify;
	}

	public String getIs_system() {
		return is_system;
	}

	public void setIs_system(String is_system) {
		this.is_system = is_system;
	}

	public String getIs_admin() {
		return is_admin;
	}

	public void setIs_admin(String is_admin) {
		this.is_admin = is_admin;
	}
	
	
	
	
	
}

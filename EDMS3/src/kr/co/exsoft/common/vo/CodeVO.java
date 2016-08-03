package kr.co.exsoft.common.vo;

/**
 * 코드 VO
 * @author 패키지 개발팀
 * @since 2014.07.28
 * @version 3.0
 *
 */
public class CodeVO {

	// 테이블 객체(XR_CODE)
	private String code_id;								// 코드ID
	private String code_nm;								// 코드명
	private String gcode_id;								// 그룹코드ID
	private int sort_index;									// 그룹내 정렬순서
	private String is_use;									// 사용여부 Y:사용 N:사용안함
	private String is_sys;									// 시스템제공여부 Y:시스템 N:사용자 - 시스템 제공 코드값은 삭제가 불가능.
	
	public CodeVO() {
		
		this.code_id = "";
		this.code_nm = "";
		this.gcode_id = "";
		this.sort_index = 0;
		this.is_use = "Y";
		this.is_sys = "N";
	}

	
	public String getCode_id() {
		return code_id;
	}

	public void setCode_id(String code_id) {
		this.code_id = code_id;
	}

	public String getGcode_id() {
		return gcode_id;
	}

	public void setGcode_id(String gcode_id) {
		this.gcode_id = gcode_id;
	}


	public String getCode_nm() {
		return code_nm;
	}

	public void setCode_nm(String code_nm) {
		this.code_nm = code_nm;
	}

	public int getSort_index() {
		return sort_index;
	}

	public void setSort_index(int sort_index) {
		this.sort_index = sort_index;
	}

	public String getIs_use() {
		return is_use;
	}

	public void setIs_use(String is_use) {
		this.is_use = is_use;
	}

	public String getIs_sys() {
		return is_sys;
	}

	public void setIs_sys(String is_sys) {
		this.is_sys = is_sys;
	}
	
	
	
}

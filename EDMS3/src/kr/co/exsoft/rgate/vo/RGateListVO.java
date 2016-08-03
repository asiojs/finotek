package kr.co.exsoft.rgate.vo;

/**
 * RGATE 대상 VO
 * @author 패키지 개발팀
 * @since 2014.07.29
 * @version 3.0
 *
 */
public class RGateListVO {

	// 테이블 객체(XR_RGATE_LIST)
	private String manage_type;									// 유형구분 - EXT:확장자 PROC:프로세스
	private String manage_name;								// 유형명 - MANAGE_TYPE이 EXT일 때 확장자 명 / MANAGE_TYPE이 PROC일 때 프로세스 
	private String is_default;										// 기본여부(또는 삭제여부)	
																			// T:확장자, 지정된 확장자 이외에는 사용이 불가하므로, 삭제하지 못하도록 'T'처리
																			// F:프로세스, 지정된 프로세스 이외에도 사용이 가능하므로, 편집이 가능하도록 'F'처리
	
	// 조회항목
	private String ext_icon;										// 확장자 아이콘
	
	public RGateListVO() {
		
		this.manage_type = "";
		this.manage_name = "";
		this.is_default = "F";
		this.ext_icon = "";		
	}
	
	public String getExt_icon() {
		return ext_icon;
	}

	public void setExt_icon(String ext_icon) {
		this.ext_icon = ext_icon;
	}

	public String getManage_type() {
		return manage_type;
	}

	public void setManage_type(String manage_type) {
		this.manage_type = manage_type;
	}

	public String getManage_name() {
		return manage_name;
	}

	public void setManage_name(String manage_name) {
		this.manage_name = manage_name;
	}

	public String getIs_default() {
		return is_default;
	}

	public void setIs_default(String is_default) {
		this.is_default = is_default;
	}
	
	
}

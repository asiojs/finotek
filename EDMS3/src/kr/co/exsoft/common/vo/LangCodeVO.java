package kr.co.exsoft.common.vo;

/**
 * 코드언어 VO
 * @author 패키지 개발팀
 * @since 2014.07.29
 * @version 3.0
 *
 */
public class LangCodeVO {

	private String code_id;											// CODE_ID - XR_CODE			
	private String lang_cd;											// 언어코드 : KO-한국어,JA-일본어,ZH-중국어,EN-영어
	private String code_nm;										// 코드명		
	private String gcode_id;										// 그룹코드값
	
	public LangCodeVO() {

		this.code_id = "";
		this.lang_cd = "";
		this.code_nm = "";
		this.gcode_id = "";
	}

	public String getCode_id() {
		return code_id;
	}

	public void setCode_id(String code_id) {
		this.code_id = code_id;
	}

	public String getLang_cd() {
		return lang_cd;
	}

	public void setLang_cd(String lang_cd) {
		this.lang_cd = lang_cd;
	}

	public String getCode_nm() {
		return code_nm;
	}

	public void setCode_nm(String code_nm) {
		this.code_nm = code_nm;
	}

	public String getGcode_id() {
		return gcode_id;
	}

	public void setGcode_id(String gcode_id) {
		this.gcode_id = gcode_id;
	}
	
	
}

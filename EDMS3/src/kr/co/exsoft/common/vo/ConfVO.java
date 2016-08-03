package kr.co.exsoft.common.vo;

import kr.co.exsoft.eframework.configuration.Constant;

/**
 * 시스템환경설정 VO
 * @author 패키지 개발팀
 * @since 2015.02.24
 * @version 3.0
 *
 */
public class ConfVO {

	// 테이블 객체(XR_SYSCONFIG) 
	// 해당 테이블은 기존(XR_URL_CONFIG/XR_FILE_CONFIG/XR_VERSION_CONFIG)를 대체한다.
	private String skey;												// 키
	private String sval;												// 값
	private String stype;											// 구분(FILE-파일관리 || VERSION-버전관리 || URL-URL복사유통기간)
	private String is_use;											// 사용유무
	
	public ConfVO() {
		this.skey = "";
		this.sval = "";
		this.stype = "";
		this.is_use = Constant.YES;
	}

	public String getSkey() {
		return skey;
	}

	public void setSkey(String skey) {
		this.skey = skey;
	}

	public String getSval() {
		return sval;
	}

	public void setSval(String sval) {
		this.sval = sval;
	}

	public String getStype() {
		return stype;
	}

	public void setStype(String stype) {
		this.stype = stype;
	}

	public String getIs_use() {
		return is_use;
	}

	public void setIs_use(String is_use) {
		this.is_use = is_use;
	}
	
	
	
}

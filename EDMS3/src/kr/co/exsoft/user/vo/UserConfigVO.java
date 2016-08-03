package kr.co.exsoft.user.vo;

import kr.co.exsoft.eframework.configuration.Constant;

/**
 * 개인환경설정 VO
 * @author 패키지 개발팀
 * @since 2015.03.02
 * @version 3.0
 *
 */
public class UserConfigVO {

	private String user_id;																	// 사용자아이디
	private String language;																// 언어
	private String theme;																	// 기본스킨
	private int page_size;																	// 목록개수(10/15/20/30/50)
	private String doc_search;																// 나의문서 검색기준 :: 단위 년
	private String view_type;																// LIST:목록 RIGTH:우측미리보기 BOTTOM:하단미리보기
	private String icon_preview;															// 문서명 옆에 아이콘 표시여부: 기본값(N)
	
	public UserConfigVO() {
		
		this.user_id = "";
		this.language = Constant.KOR;
		this.theme = "";
		this.page_size  = 20;
		this.doc_search = "3";
		this.view_type = Constant.PREVIEW_LIST;
		this.icon_preview =  Constant.NO;
		
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getTheme() {
		return theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}

	public int getPage_size() {
		return page_size;
	}

	public void setPage_size(int page_size) {
		this.page_size = page_size;
	}

	public String getDoc_search() {
		return doc_search;
	}

	public void setDoc_search(String doc_search) {
		this.doc_search = doc_search;
	}

	public String getView_type() {
		return view_type;
	}

	public void setView_type(String view_type) {
		this.view_type = view_type;
	}

	public String getIcon_preview() {
		return icon_preview;
	}

	public void setIcon_preview(String icon_preview) {
		this.icon_preview = icon_preview;
	}
	
	
}

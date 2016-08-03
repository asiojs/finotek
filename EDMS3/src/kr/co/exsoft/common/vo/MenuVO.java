package kr.co.exsoft.common.vo;

import kr.co.exsoft.eframework.configuration.Constant;

/**
 * 메뉴 VO
 * @author 패키지 개발팀
 * @since 2014.07.31
 * @version 3.0
 *
 */
@SuppressWarnings("unused")
public class MenuVO {

	// 테이블 객체(XR_MENU)
	private String menu_cd;											// 메뉴코드
	private String menu_nm_ko;										// 메뉴명 - 한국어
	private String menu_nm_en;										// 메뉴명 - 영어
	private String menu_nm_ja;										// 메뉴명 - 일본어	
	private String menu_nm_zh;										// 메뉴명 - 중국어	
	private String su_menu_cd;										// 상위메뉴코드 - 최상위 메뉴코드는 MENU_CD = SU_MENU_CD
	private String link_path;											// 링크주소 - 사용하지 않는다.
	private String icon;													// 아이콘명 - 사용하지 않는다.
	private int ord;														// 정렬순서	
	private String is_use;												// 사용여부
	private String menu_type;											// 메뉴구분 - A:관리자메뉴권한 U:사용자메뉴속성권한 - 폴더관리(공유폴더)/권한관리/문서관리
	private int menu_level;												// 메뉴레빌
	
	// 조회항목
	private String menu_nm;											// 메뉴명
	private String su_menu_nm;										// 상위메뉴명
	private String menu_type_nm;									// 메뉴구분명
	
	// treegrid 관련 항목
	private int level;														// 메뉴레벨
	private String loaded;												// treegrid
	private String isLeaf;												// 자식여부 true/fase
	private String expanded;											// 확장성 여부
	
	private String parent;												// 부모코드	
	private String checkBox;											// 체크박스옵션
	
	
	public MenuVO() {
		
		this.menu_cd = "";
		this.menu_nm_ko = "";
		this.su_menu_cd = "";
		this.ord = 0;
		this.menu_nm = "";
		this.su_menu_nm = "";
		this.menu_type = "";
		this.menu_type_nm = "";
		this.loaded = "true";
		
	}
	
	public int getMenu_level() {
		return menu_level;
	}

	public void setMenu_level(int menu_level) {
		this.menu_level = menu_level;
	}

	public int getLevel() {
		return menu_level;
	}
	
	public void setLevel(int level) {
		this.level = level;
	}

	public String getLoaded() {
		return loaded;
	}

	public void setLoaded(String loaded) {
		this.loaded = loaded;
	}

	public String getIsLeaf() {
		if(menu_level == 0)	{
			return Constant.RESULT_FALSE;
		}else {
			return Constant.RESULT_TRUE;
		}		
	}
	
	public void setIsLeaf(String isLeaf) {
		this.isLeaf = isLeaf;
	}

	public String getExpanded() {
		if(menu_level == 0)	{
			return Constant.RESULT_TRUE;
		}else {
			return Constant.RESULT_FALSE;
		}		
	}

	public void setExpanded(String expanded) {
		this.expanded = expanded;
	}

	public String getParent() {
		if(menu_level == 0)	{
			return "";
		}else {
			return su_menu_cd;
		}
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public String getCheckBox() {
		return menu_cd;
	}

	public void setCheckBox(String checkBox) {
		this.checkBox = checkBox;
	}

	public String getMenu_cd() {
		return menu_cd;
	}

	public void setMenu_cd(String menu_cd) {
		this.menu_cd = menu_cd;
	}

	public String getMenu_nm_ko() {
		return menu_nm_ko;
	}

	public void setMenu_nm_ko(String menu_nm_ko) {
		this.menu_nm_ko = menu_nm_ko;
	}

	public String getMenu_nm_en() {
		return menu_nm_en;
	}

	public void setMenu_nm_en(String menu_nm_en) {
		this.menu_nm_en = menu_nm_en;
	}

	public String getMenu_nm_ja() {
		return menu_nm_ja;
	}

	public void setMenu_nm_ja(String menu_nm_ja) {
		this.menu_nm_ja = menu_nm_ja;
	}

	public String getMenu_nm_zh() {
		return menu_nm_zh;
	}

	public void setMenu_nm_zh(String menu_nm_zh) {
		this.menu_nm_zh = menu_nm_zh;
	}

	public String getSu_menu_cd() {
		return su_menu_cd;
	}

	public void setSu_menu_cd(String su_menu_cd) {
		this.su_menu_cd = su_menu_cd;
	}

	public String getLink_path() {
		return link_path;
	}

	public void setLink_path(String link_path) {
		this.link_path = link_path;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public int getOrd() {
		return ord;
	}

	public void setOrd(int ord) {
		this.ord = ord;
	}

	public String getIs_use() {
		return is_use;
	}

	public void setIs_use(String is_use) {
		this.is_use = is_use;
	}

	public String getMenu_type() {
		return menu_type;
	}

	public void setMenu_type(String menu_type) {
		this.menu_type = menu_type;
	}

	public String getMenu_nm() {
		return menu_nm;
	}

	public void setMenu_nm(String menu_nm) {
		this.menu_nm = menu_nm;
	}

	public String getSu_menu_nm() {
		return su_menu_nm;
	}

	public void setSu_menu_nm(String su_menu_nm) {
		this.su_menu_nm = su_menu_nm;
	}

	public String getMenu_type_nm() {
		return menu_type_nm;
	}

	public void setMenu_type_nm(String menu_type_nm) {
		this.menu_type_nm = menu_type_nm;
	}
	

}

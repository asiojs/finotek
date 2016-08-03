package kr.co.exsoft.common.vo;

import kr.co.exsoft.eframework.configuration.Constant;
/**
 * 메뉴권한 VO
 * @author 패키지 개발팀
 * @since 2014.07.31
 * @version 3.0
 *
 */
@SuppressWarnings("unused")
public class MenuAuthVO {

	// 테이블 객체(XR_MENU_AUTH)
	private String role_id;												// XR_CODE REF ROLE_ID
	private String gcode_id;											// XR_CODE REF GCODE_ID
	private String menu_cd;											// XR_MENU MENU_CD
	private String part;													// 권한속성
	
	// 조회항목
	private String role_nm;												// 권한명
	private String su_menu_cd;										// 상위 메뉴코드
	private int ord;														// 메뉴 정렬코드
	private String menu_nm;											// 메뉴명
	private int child_cnt;												// 하위메뉴 카운트
	private int menu_level;												// 메뉴레빌
	private String link_path;											// 링크주소
	
	// TreeGrid Column
	private int level;														// 메뉴레벨
	private String loaded;												// treegrid
	private String isLeaf;												// 자식여부 true/fase
	private String expanded;											// 확장성 여부
	
	private String parent;												// 부모코드	
	private String all;													// 전체선택옵션
	private String group;												//	부서선택옵션	
	private String team;													//	팀선택옵션		
	private String checkBox;											// 체크박스옵션
	
	public MenuAuthVO() {
		
		this.role_id = "";
		this.gcode_id = "";
		this.menu_cd = "";
		this.part = Constant.MENU_TEAM;
		this.role_nm = "";
		this.loaded = "true";
		this.level = 0;
		this.parent = "";
		this.link_path = "";
	}
		
	public String getLink_path() {
		return link_path;
	}

	public void setLink_path(String link_path) {
		this.link_path = link_path;
	}

	public String getCheckBox() {		
		return role_id+"#"+gcode_id+"#"+menu_cd;
	}

	public void setCheckBox(String checkBox) {
		this.checkBox = checkBox;
	}

	public String getAll() {
		
		if(part.equals(Constant.MENU_ALL)) {
			return part;
		}else {
			return "";
		}
		
	}

	public void setAll(String all) {
		this.all = all;
	}

	public String getGroup() {
		if(part.equals(Constant.MENU_GROUP)) {
			return part;
		}else {
			return "";
		}
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getTeam() {
		if(part.equals(Constant.MENU_TEAM)) {
			return part;
		}else {
			return "";
		}
	}

	public void setTeam(String team) {
		this.team = team;
	}

	public String getRole_id() {
		return role_id;
	}

	public void setRole_id(String role_id) {
		this.role_id = role_id;
	}

	public String getGcode_id() {
		return gcode_id;
	}

	public void setGcode_id(String gcode_id) {
		this.gcode_id = gcode_id;
	}

	public String getMenu_cd() {
		return menu_cd;
	}

	public void setMenu_cd(String menu_cd) {
		this.menu_cd = menu_cd;
	}

	public String getPart() {
		return part;
	}

	public void setPart(String part) {
		this.part = part;
	}

	public String getRole_nm() {
		return role_nm;
	}

	public void setRole_nm(String role_nm) {
		this.role_nm = role_nm;
	}

	public String getSu_menu_cd() {
		return su_menu_cd;
	}

	public void setSu_menu_cd(String su_menu_cd) {
		this.su_menu_cd = su_menu_cd;
	}

	public int getOrd() {
		return ord;
	}

	public void setOrd(int ord) {
		this.ord = ord;
	}

	public String getMenu_nm() {
		return menu_nm;
	}

	public void setMenu_nm(String menu_nm) {
		this.menu_nm = menu_nm;
	}

	public int getChild_cnt() {
		return child_cnt;
	}

	public void setChild_cnt(int child_cnt) {
		this.child_cnt = child_cnt;
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

	
	
}

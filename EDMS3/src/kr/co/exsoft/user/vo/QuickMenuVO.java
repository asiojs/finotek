package kr.co.exsoft.user.vo;

/**
 * 퀵메뉴 관리
 * @author 패키지 개발팀
 * @since 2015.03.30
 * @version 1.0
 *
 */
public class QuickMenuVO {

	private String menu_cd;
	private String menu_nm;
	private String link_path;
	private int ord;
	private String chk;
	private String user_id;
	
	public QuickMenuVO() {
		this.menu_cd = "";
		this.menu_nm = "";
		this.link_path = "";
		this.ord = 0;
		this.chk = "";
		this.user_id = "";
	}

	public String getMenu_cd() {
		return menu_cd;
	}

	public void setMenu_cd(String menu_cd) {
		this.menu_cd = menu_cd;
	}

	public String getMenu_nm() {
		return menu_nm;
	}

	public void setMenu_nm(String menu_nm) {
		this.menu_nm = menu_nm;
	}

	public String getLink_path() {
		return link_path;
	}

	public void setLink_path(String link_path) {
		this.link_path = link_path;
	}

	public int getOrd() {
		return ord;
	}

	public void setOrd(int ord) {
		this.ord = ord;
	}

	public String getChk() {
		return chk;
	}

	public void setChk(String chk) {
		this.chk = chk;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	
	
}

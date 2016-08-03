package kr.co.exsoft.user.vo;

/**
 * 부서변경이력 VO
 * @author 패키지 개발팀
 * @since 2014.07.31
 * @version 3.0
 *
 */
public class GroupHtVO {

	// 테이블 객체(XR_GROUP_HT)
	private long group_seq;													// 그룹변경 IDX
	private String group_id;													// 그룹(부서) 아이디 
	private String dept_cd;														// 부서 ALIAS ID - 연계시 사용됨 / 미연계시 GROUP_ID 와 동일
	private String group_name_ko;											// 부서명-한국어
	private String group_name_en;											// 부서명-영어
	private String group_name_ja;											// 부서명-일본어	
	private String group_name_zh;											// 부서명-중국어
	private String group_status;												// 그룹(부서)상태
	private String parent_id;													// 상위부서 ID	
	private int sort_index;														// 정렬순서						
	private String map_id;														// 맵ID
	private String status;														// 수행구분	
	private String create_date;												// 수행일
	
	// 조회항목
	private String group_nm;													// 그룹명
	private String map_nm;													// 맵명
	private String su_group_nm;												// 상위그룹명
	private String group_path;												// 그룹경로
	
	public GroupHtVO() {
	
		this.group_seq = 0;
		this.group_id = "";
		this.dept_cd = "";
		this.group_name_ko = "";
		this.group_name_en = "";
		this.group_name_ja = "";
		this.group_name_zh = "";
		this.group_status = "";
		this.parent_id = "";
		this.sort_index = 0;
		this.map_id = "";
		this.status = "";
		this.create_date = "";
		this.group_nm = "";
		this.map_nm = "";
		this.su_group_nm = "";
		this.group_path = "";
		
	}

	public long getGroup_seq() {
		return group_seq;
	}

	public void setGroup_seq(long group_seq) {
		this.group_seq = group_seq;
	}

	public String getGroup_id() {
		return group_id;
	}

	public void setGroup_id(String group_id) {
		this.group_id = group_id;
	}

	public String getDept_cd() {
		return dept_cd;
	}

	public void setDept_cd(String dept_cd) {
		this.dept_cd = dept_cd;
	}

	public String getGroup_name_ko() {
		return group_name_ko;
	}

	public void setGroup_name_ko(String group_name_ko) {
		this.group_name_ko = group_name_ko;
	}

	public String getGroup_name_en() {
		return group_name_en;
	}

	public void setGroup_name_en(String group_name_en) {
		this.group_name_en = group_name_en;
	}

	public String getGroup_name_ja() {
		return group_name_ja;
	}

	public void setGroup_name_ja(String group_name_ja) {
		this.group_name_ja = group_name_ja;
	}

	public String getGroup_name_zh() {
		return group_name_zh;
	}

	public void setGroup_name_zh(String group_name_zh) {
		this.group_name_zh = group_name_zh;
	}

	public String getGroup_status() {
		return group_status;
	}

	public void setGroup_status(String group_status) {
		this.group_status = group_status;
	}

	public String getParent_id() {
		return parent_id;
	}

	public void setParent_id(String parent_id) {
		this.parent_id = parent_id;
	}

	public int getSort_index() {
		return sort_index;
	}

	public void setSort_index(int sort_index) {
		this.sort_index = sort_index;
	}

	public String getMap_id() {
		return map_id;
	}

	public void setMap_id(String map_id) {
		this.map_id = map_id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCreate_date() {
		return create_date;
	}

	public void setCreate_date(String create_date) {
		this.create_date = create_date;
	}

	public String getGroup_nm() {
		return group_nm;
	}

	public void setGroup_nm(String group_nm) {
		this.group_nm = group_nm;
	}

	public String getMap_nm() {
		return map_nm;
	}

	public void setMap_nm(String map_nm) {
		this.map_nm = map_nm;
	}

	public String getSu_group_nm() {
		return su_group_nm;
	}

	public void setSu_group_nm(String su_group_nm) {
		this.su_group_nm = su_group_nm;
	}

	public String getGroup_path() {
		return group_path;
	}

	public void setGroup_path(String group_path) {
		this.group_path = group_path;
	}
	
	
	
	
}


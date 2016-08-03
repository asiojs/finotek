package kr.co.exsoft.user.vo;

import java.util.ArrayList;
import java.util.List;

/**
 * 그룹(부서) VO
 * @author 패키지 개발팀
 * @since 2014.07.16
 * @version 3.0
 *
 */
public class GroupVO {

	// 테이블 객체(XR_GROUP/XR_GROUIP_HT)
	private long group_seq;						// 그룹변경 IDX - XR_GROUP_HT
    private String group_id;						// 부서ID
    private String dept_cd;							// 부서 ALIAS ID
    private String group_name_ko;				// 부서명_한국어
    private String group_name_en;				// 부서명_영어
    private String group_name_ja;				// 부서명_일본어
//    private String group_name_zh;				// 부서명_중국어
    private String group_status;					// 부서상태 = C:활성 U:수정 D:비활성(폐쇄)
    private String parent_id;						// 상위 부서 ID = 최상위 루트인 경우 GROUP_ID = PARENT_ID 동일함.
    private int sort_index;							// 정렬 순서
    private String map_id;							// 맵 ID
    private String create_date;					// 등록일
        
    // 조회 객체        
    private String group_nm;						// 그룹명
    private String parent_nm;						// 상위그룹명
    private String map_nm;        				// 맵명
    private List<String> user_id_list;			// 소속 사용자 목록

    
    // JS Tree 관련
    private int children_count;
    
    // 히스토리 관련
    private String status;
    
    public GroupVO() {
    	
    	this.group_id = "";
    	this.dept_cd = "";
    	this.group_name_ko = "";
    	this.group_name_en = "";
    	this.group_name_ja = "";
//    	this.group_name_zh = "";
    	this.group_status = "C";					
    	this.parent_id = "";
    	this.sort_index = 0;
    	this.map_id = "";
    	this.create_date = "";
    	this.user_id_list = new ArrayList<String>();
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


//	public String getGroup_name_zh() {
//		return group_name_zh;
//	}
//
//
//	public void setGroup_name_zh(String group_name_zh) {
//		this.group_name_zh = group_name_zh;
//	}


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


	public String getParent_nm() {
		return parent_nm;
	}


	public void setParent_nm(String parent_nm) {
		this.parent_nm = parent_nm;
	}


	public String getMap_nm() {
		return map_nm;
	}


	public void setMap_nm(String map_nm) {
		this.map_nm = map_nm;
	}


	public int getChildren_count() {
		return children_count;
	}


	public void setChildren_count(int children_count) {
		this.children_count = children_count;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public List<String> getUser_id_list() {
		return user_id_list;
	}


	public void setUser_id_list(List<String> user_id_list) {
		this.user_id_list = user_id_list;
	}
    
    
    
}


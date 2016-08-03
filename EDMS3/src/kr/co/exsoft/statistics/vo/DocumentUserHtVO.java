package kr.co.exsoft.statistics.vo;

import kr.co.exsoft.eframework.vo.VO;

/**
 * 사용자별 문서현황
 *
 * @author 패키지팀
 * @since 2014. 10. 6.
 * @version 1.0
 * 
 */

public class DocumentUserHtVO extends VO {

	private String udate;														// 기준일:YYYY-MM-DD
	private String user_id;														// 사용자ID
	private String type_id;														// 문서유형ID
	private String user_nm;													// 사용자명
	private String group_id;													// 그룹ID
	private String group_nm;													// 그룹명
	private int create_cnt;														// 등록
	private int read_cnt;														// 조회
	private int update_cnt;														// 수정		
	private int delete_cnt;														// 삭제	
	private String type_name;													// 문서유형명
	
	public DocumentUserHtVO() {
		this.udate = "";
		this.user_id =  "";
		this.user_nm = "";
		this.type_id = "";
		this.user_nm = "";
		this.group_id = "";
		this.group_nm = "";
		this.create_cnt = 0;
		this.read_cnt = 0;
		this.update_cnt = 0;
		this.delete_cnt = 0;
		this.type_name = "";
	}

	public String getType_name() {
		return type_name;
	}

	public void setType_name(String type_name) {
		this.type_name = type_name;
	}

	public String getUdate() {
		return udate;
	}

	public void setUdate(String udate) {
		this.udate = udate;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getType_id() {
		return type_id;
	}

	public void setType_id(String type_id) {
		this.type_id = type_id;
	}

	public String getUser_nm() {
		return user_nm;
	}

	public void setUser_nm(String user_nm) {
		this.user_nm = user_nm;
	}

	public String getGroup_id() {
		return group_id;
	}

	public void setGroup_id(String group_id) {
		this.group_id = group_id;
	}

	public String getGroup_nm() {
		return group_nm;
	}

	public void setGroup_nm(String group_nm) {
		this.group_nm = group_nm;
	}

	public int getCreate_cnt() {
		return create_cnt;
	}

	public void setCreate_cnt(int create_cnt) {
		this.create_cnt = create_cnt;
	}

	public int getRead_cnt() {
		return read_cnt;
	}

	public void setRead_cnt(int read_cnt) {
		this.read_cnt = read_cnt;
	}

	public int getUpdate_cnt() {
		return update_cnt;
	}

	public void setUpdate_cnt(int update_cnt) {
		this.update_cnt = update_cnt;
	}

	public int getDelete_cnt() {
		return delete_cnt;
	}

	public void setDelete_cnt(int delete_cnt) {
		this.delete_cnt = delete_cnt;
	}
	
	
	
}

package kr.co.exsoft.common.vo;

import kr.co.exsoft.eframework.vo.VO;

/**
 * 최신문서, 최신업무(협업), 최신폴더 VO
 * @author 패키지 개발팀
 * @since 2015.03.02
 * @version 3.0
 *
 */
public class RecentlyObjectVO extends VO{

	private String idx;									// XR_COUNTER TABLE REF
	private String user_id;								// 사용자아이디
	private String target_id;							// 대상 객체 ID
	private String target_name;							// 대상 객체명
	private String target_type;							// 대상 객체타입(D:문서, P:프로세스, F:폴더)
	private String last_used_date;						// 등록일

	
	public RecentlyObjectVO() {
		
		this.idx = "";
		this.user_id = "";
		this.target_id = "";
		this.target_type = "";
		this.last_used_date = "";
	}


	public String getIdx() {
		return idx;
	}


	public void setIdx(String idx) {
		this.idx = idx;
	}


	public String getUser_id() {
		return user_id;
	}


	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}


	public String getTarget_id() {
		return target_id;
	}


	public void setTarget_id(String target_id) {
		this.target_id = target_id;
	}


	public String getTarget_type() {
		return target_type;
	}


	public void setTarget_type(String target_type) {
		this.target_type = target_type;
	}


	public String getLast_used_date() {
		return last_used_date;
	}


	public void setLast_used_date(String last_used_date) {
		this.last_used_date = last_used_date;
	}


	public String getTarget_name() {
		return target_name;
	}


	public void setTarget_name(String target_name) {
		this.target_name = target_name;
	}

	
}

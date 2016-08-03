package kr.co.exsoft.user.vo;

/**
 * 그룹(부서) VO
 * @author 패키지 개발팀
 * @since 2014.09.10
 * @version 1.0
 *
 */
public class GroupedVO {
	private String group_id;
	private String user_id;
	private String is_default;
	
	public String getGroup_id() {
		return group_id;
	}
	public void setGroup_id(String group_id) {
		this.group_id = group_id;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getIs_default() {
		return is_default;
	}
	public void setIs_default(String is_default) {
		this.is_default = is_default;
	}
	
	
}

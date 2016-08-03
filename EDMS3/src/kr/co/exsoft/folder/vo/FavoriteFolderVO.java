package kr.co.exsoft.folder.vo;

/**
 * 즐겨찾기 폴더 VO
 * @author 패키지 개발팀
 * @since 2014.07.31
 * @version 3.0
 *
 */
public class FavoriteFolderVO {

	// 테이블 객체(XR_FAVORITE_FOLDER)
	private String folder_id;					// 폴더 아이디
	private String favorite_nm;				// 즐겨찾기 폴더명	- 미입력시 XR_FOLDER NAME이 입력된다.
	private String user_id;						// 사용자 아이디
	private String parent_folder_id;			// 상위 폴더 아이디	- 즐겨찾기 폴더내에서의 상위폴더
	private String is_virtual;					// 실제폴더여부 : Y-실제폴더  N-사용자 추가 즐겨찾기 폴더	
	private int sorts;
	private int children_count;
	
	public FavoriteFolderVO() {
		
		this.folder_id = "";
		this.favorite_nm = "";
		this.user_id = "";
		this.parent_folder_id = "";
		this.is_virtual = "";
		this.sorts = 0;
	}
	
	public int getChildren_count() {
		return children_count;
	}

	public void setChildren_count(int children_count) {
		this.children_count = children_count;
	}

	public int getSorts() {
		return sorts;
	}

	public void setSorts(int sorts) {
		this.sorts = sorts;
	}

	public String getIs_virtual() {
		return is_virtual;
	}
	
	public void setIs_virtual(String is_virtual) {
		this.is_virtual = is_virtual;
	}

	public String getFolder_id() {
		return folder_id;
	}

	public void setFolder_id(String folder_id) {
		this.folder_id = folder_id;
	}

	public String getFavorite_nm() {
		return favorite_nm;
	}

	public void setFavorite_nm(String favorite_nm) {
		this.favorite_nm = favorite_nm;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getParent_folder_id() {
		return parent_folder_id;
	}

	public void setParent_folder_id(String parent_folder_id) {
		this.parent_folder_id = parent_folder_id;
	}
	

}

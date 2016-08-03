package kr.co.exsoft.document.vo;

/**
 * 작업카트 VO
 *
 * @author 패키지팀
 * @since 2014. 10. 8.
 * @version 1.0
 * 
 */

public class TempDocVO {

	// 테이블 객체(XR_TEMP_DOC)
	private String doc_id;									// 문서ID
	private String user_id;									// 사용자ID :: 작업카트 추가한 사용자
	private String doc_name;								// 문서명	
	private String type_id;									// 문서유형ID
	private String type_name;								// 문서유형명
	private String version_no;								// 문서버전
	private String root_id;									// 루트버전 문서ID - NULL : 최상위버전
	private String creator_id;								// 등록자ID	
	private String creator_name;							// 등록자명
	private String create_date;							// 등록일	
	
	public TempDocVO() {
		
		this.doc_id = "";
		this.user_id = "";
		this.doc_name = "";
		this.type_id = "";
		this.type_name = "";
		this.version_no = "";
		this.root_id = "";
		this.creator_id = "";
		this.creator_name = "";
		this.create_date = "";		
	}

	public String getDoc_id() {
		return doc_id;
	}

	public void setDoc_id(String doc_id) {
		this.doc_id = doc_id;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getDoc_name() {
		return doc_name;
	}

	public void setDoc_name(String doc_name) {
		this.doc_name = doc_name;
	}

	public String getType_id() {
		return type_id;
	}

	public void setType_id(String type_id) {
		this.type_id = type_id;
	}

	public String getType_name() {
		return type_name;
	}

	public void setType_name(String type_name) {
		this.type_name = type_name;
	}

	public String getVersion_no() {
		return version_no;
	}

	public void setVersion_no(String version_no) {
		this.version_no = version_no;
	}

	public String getRoot_id() {
		return root_id;
	}

	public void setRoot_id(String root_id) {
		this.root_id = root_id;
	}

	public String getCreator_id() {
		return creator_id;
	}

	public void setCreator_id(String creator_id) {
		this.creator_id = creator_id;
	}

	public String getCreator_name() {
		return creator_name;
	}

	public void setCreator_name(String creator_name) {
		this.creator_name = creator_name;
	}

	public String getCreate_date() {
		return create_date;
	}

	public void setCreate_date(String create_date) {
		this.create_date = create_date;
	}
	
	
}

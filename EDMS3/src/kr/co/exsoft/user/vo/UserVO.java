package kr.co.exsoft.user.vo;

import java.util.List;

import kr.co.exsoft.eframework.configuration.Constant;
import kr.co.exsoft.eframework.util.ConfigData;

/**
 * 사용자 정보 VO
 * @author 패키지 개발팀
 * @since 2014.07.16
 * @version 3.0
 *
 */
public class UserVO {
    	
	// 테이블 객체(XR_USER/XR_USER_HT)
	private long user_seq;								// 사용자변경 IDX - XR_USER_HT
    private String user_id;								// 사용자 ID
    private String emp_no;								// 사원번호
    private String user_name_ko;						// 사용자명	한국어
    private String user_name_en;						// 사용자명 영어
    private String user_name_ja;						// 사용자명 일본어
    private String user_name_zh;						// 사용자명 중국어	
    private String user_pass;							// 사용자 패스워드(ARIA 암호화)
    private String user_status;							// 사용자상태 = C:활성(재직) U:수정 D:비활성(퇴사)
    private String role_id;								// 권한ID 
    private String create_date;						// 등록일
    private String share_name;						// 공유명 = RGATE에서 사용됨	
    private String user_type;							// 유저구분 = 0:내부사용자 1:외부사용자    
    private String manage_group;					// 관리부서 id
    
    // 테이블 객체(XR_USER_DT)
    private String jobtitle;								// 직위
    private String position;								// 직급
    private String email;								// 이메일
    private String telephone;							// 전화번호
    
    // 테이블 객체(XR_USER_COFIG)
    private String language;							// 기본 로케일(KO-한국어,EN-영어,JA-일본어,ZH-중국어)
    private String theme;								// 테마스킨
    private String page_size;							// 기본 목록 사이즈
    private String doc_search;							// 문서목록 기본검색기간 0/1/3/5/10 :: 0는 전체기간 그외는 현재일 기준으로 년단위 검색
    private String view_type;							// 문서목록보기(LIST/RIGHT/BOTTOM)
    private String icon_preview;						// 문서목록 첨부파일 아이콘 미리보기 여부
    
    // 조회 객체        
    private String user_nm;							// 사용자명
    private String user_type_nm;						// 사용자 구분명
    private String role_nm;								// 권한명
    private String group_id;							// 사용자 기본 소속 부서ID
    private String group_nm;							// 사용자 기본 소속 부서명
    private String jobtitle_nm;    					// 직위명	
    private String position_nm;						// 직급명	    	
    private long storage_quota;						// 개인문서함 할당용량 = XR_FODLER 참조
    private long storage_usage;						// 개인문서함 사용량 = XR_FOLDER 참조
    private String login_type;							// 로그인타입(관리자/일반)
    private String user_status_nm;					// 사용자 상태명
    private String manage_group_nm;				// 관리부서 명
    private List<String> group_id_list;				// 사용자 속한 부서 목록 리스트
    
    // 히스토리
    private String status;
    
    public UserVO() {
    	
    	this.user_seq = 0;
    	this.user_id = "";
    	this.emp_no = "";
    	this.user_name_ko = "";
    	this.user_name_en = "";
    	this.user_name_ja = "";    	
    	this.user_name_zh = "";
    	this.user_pass = "";
    	this.language = "KO";
    	this.theme = "Blue";
    	this.page_size = "15";
    	this.user_status = "C";
    	this.role_id = "";
    	this.create_date = "";
    	this.share_name = "MYPAGE,MYDEPT";
    	this.user_type = "0";
    	this.jobtitle = ConfigData.getString("DEFAULT.POS");
    	this.position = "";
    	this.email = "";
    	this.telephone = "";
    	this.user_nm = "";
    	this.user_type_nm = "";
    	this.role_nm = "";
    	this.group_id = "";
    	this.group_nm = "";
    	this.jobtitle_nm = "";
    	this.position_nm = "";
    	this.storage_quota = 204800000;				// 사용자 기본 할당량 2GB
    	this.storage_usage = 0;
    	this.login_type = "";
    	this.user_status_nm = "";
    	this.doc_search = "3";								// 기본검색기간 3년
    	this.manage_group = "";
    	this.manage_group_nm = "";
    	this.view_type = Constant.PREVIEW_LIST;
    	this.icon_preview = Constant.NO;
    }
    
	public String getView_type() {
		return view_type;
	}

	public void setView_type(String view_type) {
		this.view_type = view_type;
	}

	public String getIcon_preview() {
		return icon_preview;
	}

	public void setIcon_preview(String icon_preview) {
		this.icon_preview = icon_preview;
	}

	public String getManage_group() {
		return manage_group;
	}

	public void setManage_group(String manage_group) {
		this.manage_group = manage_group;
	}

	public String getDoc_search() {
		return doc_search;
	}

	public void setDoc_search(String doc_search) {
		this.doc_search = doc_search;
	}

	public String getUser_status_nm() {
		return user_status_nm;
	}

	public void setUser_status_nm(String user_status_nm) {
		this.user_status_nm = user_status_nm;
	}

	public String getLogin_type() {
		return login_type;
	}

	public void setLogin_type(String login_type) {
		this.login_type = login_type;
	}

	public String getUser_type_nm() {
		return user_type_nm;
	}

	public void setUser_type_nm(String user_type_nm) {
		this.user_type_nm = user_type_nm;
	}

	public List<String> getGroup_id_list() {
		return group_id_list;
	}

	public void setGroup_id_list(List<String> group_id_list) {
		this.group_id_list = group_id_list;
	}

	public long getUser_seq() {
		return user_seq;
	}


	public void setUser_seq(long user_seq) {
		this.user_seq = user_seq;
	}


	public String getUser_id() {
		return user_id;
	}


	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}


	public String getEmp_no() {
		return emp_no;
	}


	public void setEmp_no(String emp_no) {
		this.emp_no = emp_no;
	}


	public String getUser_name_ko() {
		return user_name_ko;
	}


	public void setUser_name_ko(String user_name_ko) {
		this.user_name_ko = user_name_ko;
	}


	public String getUser_name_en() {
		return user_name_en;
	}


	public void setUser_name_en(String user_name_en) {
		this.user_name_en = user_name_en;
	}


	public String getUser_name_ja() {
		return user_name_ja;
	}


	public void setUser_name_ja(String user_name_ja) {
		this.user_name_ja = user_name_ja;
	}


	public String getUser_name_zh() {
		return user_name_zh;
	}


	public void setUser_name_zh(String user_name_zh) {
		this.user_name_zh = user_name_zh;
	}


	public String getUser_pass() {
		return user_pass;
	}


	public void setUser_pass(String user_pass) {
		this.user_pass = user_pass;
	}


	public String getLanguage() {
		return language;
	}


	public void setLanguage(String language) {
		this.language = language;
	}


	public String getTheme() {
		return theme;
	}


	public void setTheme(String theme) {
		this.theme = theme;
	}


	public String getPage_size() {
		return page_size;
	}


	public void setPage_size(String page_size) {
		this.page_size = page_size;
	}


	public String getUser_status() {
		return user_status;
	}


	public void setUser_status(String user_status) {
		this.user_status = user_status;
	}


	public String getRole_id() {
		return role_id;
	}


	public void setRole_id(String role_id) {
		this.role_id = role_id;
	}


	public String getCreate_date() {
		return create_date;
	}


	public void setCreate_date(String create_date) {
		this.create_date = create_date;
	}


	public String getShare_name() {
		return share_name;
	}


	public void setShare_name(String share_name) {
		this.share_name = share_name;
	}


	public String getUser_type() {
		return user_type;
	}


	public void setUser_type(String user_type) {
		this.user_type = user_type;
	}


	public String getJobtitle() {
		return jobtitle;
	}


	public void setJobtitle(String jobtitle) {
		this.jobtitle = jobtitle;
	}


	public String getPosition() {
		return position;
	}


	public void setPosition(String position) {
		this.position = position;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getTelephone() {
		return telephone;
	}


	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}


	public String getUser_nm() {
		return user_nm;
	}


	public void setUser_nm(String user_nm) {
		this.user_nm = user_nm;
	}


	public String getRole_nm() {
		return role_nm;
	}


	public void setRole_nm(String role_nm) {
		this.role_nm = role_nm;
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


	public String getJobtitle_nm() {
		return jobtitle_nm;
	}


	public void setJobtitle_nm(String jobtitle_nm) {
		this.jobtitle_nm = jobtitle_nm;
	}


	public String getPosition_nm() {
		return position_nm;
	}


	public void setPosition_nm(String position_nm) {
		this.position_nm = position_nm;
	}


	public long getStorage_quota() {
		return storage_quota;
	}


	public void setStorage_quota(long storage_quota) {
		this.storage_quota = storage_quota;
	}


	public long getStorage_usage() {
		return storage_usage;
	}


	public void setStorage_usage(long storage_usage) {
		this.storage_usage = storage_usage;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getManage_group_nm() {
		return manage_group_nm;
	}

	public void setManage_group_nm(String manage_group_nm) {
		this.manage_group_nm = manage_group_nm;
	}
    
}

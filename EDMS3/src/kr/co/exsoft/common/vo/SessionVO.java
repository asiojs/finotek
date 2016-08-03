package kr.co.exsoft.common.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import kr.co.exsoft.eframework.configuration.Constant;
import kr.co.exsoft.eframework.util.ConfigData;

/**
 * 세션 VO
 * @author 패키지 개발팀
 * @since 2014.07.16
 * @version 3.0
 *
 */
public class SessionVO implements Serializable {

	private static final long serialVersionUID = -6551289232514106558L;
	
	private String sessionId;										// 새션 객체ID
	private String sessId;											// 사용자ID
	private String sessName;										// 사용자명
	private String sessJobtitle;										// 직위
	private String sessRole_id;										// 권한
	private String sessRole_nm;									// 권한명
	private String sessGroup_id;									// 기본 소속 부서
	private String sessGroup_nm;								// 기본 소속 부서명
	private String sessTheme;										// 테마스킨
    private String sessPage_size;									// 기본 목록 사이즈
    private String sessLocation;									// 사용자/관리자 접속 = U-사용자 A-관리자
    private String sessLanguage;									// 기본 로케일(KO-한국어,EN-영어,JA-일본어,ZH-중국어)
    private String sessRemoteIp;									// 접속IP
    private String sessContent;									// 세션Content
    private String sessMenuContent;							// 마지막 접속 메뉴
    private String sessContextRoot;								// contextRootPath
    private List<String> sessProjectGroup;					// 사용자 소속 프로젝트 부서 리스트
    private List<String> sessParentGroup;						// 사용자 상위 부서 목록 :: ACL 리스트에서 DEPT 타입에서 사용 함.
    private List<MenuAuthVO> sessMenuAuth;				// XR_MENU_AUTH에서 사용자의 role_id에 해당하는 모든 인증 정보를 보관 함
    private String sessSearchYear;								// 사용자 문서목록 기본 검색기간
    private String sessStartDt;										// 사용자 문서목록 기본 검색시작일		
    private String sessEndDt;										// 사용자 문서목록 기본 검색종료일
    private String sessManage_group;							// 관리부서 ID
    private String sessManage_group_nm;					// 관리부서 명
    private String sessDocSearch;								// 나의문서 메뉴 문서 표시 기간
    private String sessViewType;									// 문서목록 미리보기 타입
    private String sessIconPrewiew;								// 문서목록 대표첨부 미리보기 여부
    private String sessEmail;										// 사용자 Email
        
    public SessionVO() {
    	
    	this.sessionId = "";
    	this.sessId = "";
    	this.sessName = "";
    	this.sessJobtitle = "";
    	this.sessRole_id = "";
    	this.sessRole_nm = "";
    	this.sessGroup_id = "";
    	this.sessGroup_nm = "";
    	this.sessTheme = "";
    	this.sessPage_size = "";
    	this.sessLanguage = ConfigData.getString("LANGUAGE");
    	this.sessLocation = "";
    	this.sessRemoteIp = "";
    	this.sessContent = "";
    	this.sessMenuContent = "";
    	this.sessContextRoot = "";
    	this.sessProjectGroup = new ArrayList<String>();
    	this.sessParentGroup = new ArrayList<String>();
    	this.sessMenuAuth = new ArrayList<MenuAuthVO>();
    	this.sessSearchYear = "";
    	this.sessStartDt = "";
    	this.sessEndDt = "";
    	this.sessManage_group = "";
    	this.sessDocSearch = "";
    	this.sessViewType = Constant.PREVIEW_LIST;
    	this.sessIconPrewiew = Constant.NO;
    	this.sessEmail = "";
    }
    
	public String getSessViewType() {
		return sessViewType;
	}

	public void setSessViewType(String sessViewType) {
		this.sessViewType = sessViewType;
	}

	public String getSessIconPrewiew() {
		return sessIconPrewiew;
	}

	public void setSessIconPrewiew(String sessIconPrewiew) {
		this.sessIconPrewiew = sessIconPrewiew;
	}

	public String getSessDocSearch() {
		return sessDocSearch;
	}

	public void setSessDocSearch(String sessDocSearch) {
		this.sessDocSearch = sessDocSearch;
	}

	public String getSessRole_nm() {
		return sessRole_nm;
	}

	public void setSessRole_nm(String sessRole_nm) {
		this.sessRole_nm = sessRole_nm;
	}

	public List<String> getSessProjectGroup() {
		return sessProjectGroup;
	}

	public void setSessProjectGroup(List<String> sessProjectGroup) {
		this.sessProjectGroup = sessProjectGroup;
	}

	public String getSessSearchYear() {
		return sessSearchYear;
	}

	public void setSessSearchYear(String sessSearchYear) {
		this.sessSearchYear = sessSearchYear;
	}

	public String getSessStartDt() {
		return sessStartDt;
	}

	public void setSessStartDt(String sessStartDt) {
		this.sessStartDt = sessStartDt;
	}

	public String getSessEndDt() {
		return sessEndDt;
	}

	public void setSessEndDt(String sessEndDt) {
		this.sessEndDt = sessEndDt;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	/**
	 * 
	 * <pre>
	 * 1. 개용 : 로그인 사용자 ID 
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : getSessId
	 * @return String
	 */
	public String getSessId() {
		return sessId;
	}

	public void setSessId(String sessId) {
		this.sessId = sessId;
	}

	/**
	 * 
	 * <pre>
	 * 1. 개용 : 로그인 사용자 명 
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : getSessName
	 * @return String
	 */
	public String getSessName() {
		return sessName;
	}

	public void setSessName(String sessName) {
		this.sessName = sessName;
	}

	public String getSessJobtitle() {
		return sessJobtitle;
	}

	public void setSessJobtitle(String sessJobtitle) {
		this.sessJobtitle = sessJobtitle;
	}

	public String getSessRole_id() {
		return sessRole_id;
	}


	public void setSessRole_id(String sessRole_id) {
		this.sessRole_id = sessRole_id;
	}


	public String getSessGroup_id() {
		return sessGroup_id;
	}


	public void setSessGroup_id(String sessGroup_id) {
		this.sessGroup_id = sessGroup_id;
	}


	public String getSessGroup_nm() {
		return sessGroup_nm;
	}


	public void setSessGroup_nm(String sessGroup_nm) {
		this.sessGroup_nm = sessGroup_nm;
	}


	public String getSessTheme() {
		return sessTheme;
	}


	public void setSessTheme(String sessTheme) {
		this.sessTheme = sessTheme;
	}


	public String getSessPage_size() {
		return sessPage_size;
	}


	public void setSessPage_size(String sessPage_size) {
		this.sessPage_size = sessPage_size;
	}


	public String getSessLocation() {
		return sessLocation;
	}


	public void setSessLocation(String sessLocation) {
		this.sessLocation = sessLocation;
	}


	public String getSessLanguage() {
		return sessLanguage;
	}


	public void setSessLanguage(String sessLanguage) {
		this.sessLanguage = sessLanguage;
	}


	public String getSessRemoteIp() {
		return sessRemoteIp;
	}


	public void setSessRemoteIp(String sessRemoteIp) {
		this.sessRemoteIp = sessRemoteIp;
	}


	public String getSessContent() {
		return sessContent;
	}


	public void setSessContent(String sessContent) {
		this.sessContent = sessContent;
	}


	public String getSessMenuContent() {
		return sessMenuContent;
	}


	public void setSessMenuContent(String sessMenuContent) {
		this.sessMenuContent = sessMenuContent;
	}


	public String getSessContextRoot() {
		return sessContextRoot;
	}


	public void setSessContextRoot(String sessContextRoot) {
		this.sessContextRoot = sessContextRoot;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public List<String> getSessParentGroup() {
		return sessParentGroup;
	}

	public void setSessParentGroup(List<String> sessParentGroup) {
		this.sessParentGroup = sessParentGroup;
	}

	public String getSessEmail() {
		return sessEmail;
	}

	public void setSessEmail(String sessEmail) {
		this.sessEmail = sessEmail;
	}

	/**
	 * 
	 * <pre>
	 * 1. 개용 : 관리자 및 사용자 메뉴 접근권한 정보 
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : getSessMenuAuth
	 * @return List<MenuAuthVO>
	 */
	public List<MenuAuthVO> getSessMenuAuth() {
		return sessMenuAuth;
	}

	public void setSessMenuAuth(List<MenuAuthVO> sessMenuAuth) {
		this.sessMenuAuth = sessMenuAuth;
	}

	/**
	 * 
	 * <pre>
	 * 1. 개용 : 관리부서 ID를 가져온다. 
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : getSessManage_group_id
	 * @return String
	 */
	public String getSessManage_group() {
		return sessManage_group;
	}

	public void setSessManage_group(String sessManage_group) {
		this.sessManage_group = sessManage_group;
	}

	public String getSessManage_group_nm() {
		return sessManage_group_nm;
	}

	public void setSessManage_group_nm(String sessManage_group_nm) {
		this.sessManage_group_nm = sessManage_group_nm;
	}

	
	
}

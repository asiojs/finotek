package kr.co.exsoft.statistics.vo;

import kr.co.exsoft.eframework.util.StringUtil;
import kr.co.exsoft.eframework.vo.VO;

/**
 * 문서보유현황 - 사용자/문서별, 문서함/폴더별, 문서유형별, 보안등급별 보유 현황
 *
 * @author 패키지팀
 * @since 2014. 11. 21.
 * @version 1.0
 * 
 */

public class DocumentStatusVO extends VO {

	// VIEW : VW_DOC_STATUS => ALIAS : DocumentStatus
	private String user_nm;							// 사용자명
	private String owner_id;							// 사용자ID :: 소유자ID
	private String group_nm;							// 부서명	
	private String group_id;							// 부서ID
	private String map_nm;							// 맵명	
	private String map_id;								// 맵ID
	private int doc_cnt;									// 문서수
	private int page_cnt;								// 파일수
	private long page_total;							// 용량
	private String folder_nm;							// 폴더명 :: 문서함/폴더별 보유현황 기준
	private String folder_id;							// 폴더ID
	private String doc_type;							// 문서유형ID
	private String type_name;							// 문서유형명
	private String code_nm;							// 코드명
	private String security_levle;						// 보안등급	
	private String part_id;								// 문서Quota 현황(개인ID/부서ID)	
	private String part_nm;							// 문서Quota 현황(사용자명/부서명)
	private long storage_quota;						// 개인/부서함 할당량
	
	@SuppressWarnings("unused")
	private String fsize;									// 파일사이즈
	@SuppressWarnings("unused")
	private String ssize;									// 할당량사이즈	
	
	public DocumentStatusVO() {
		
		this.user_nm = "";
		this.owner_id = "";
		this.group_nm = "";
		this.group_id = "";
		this.map_nm = "";
		this.map_id = "";
		this.doc_cnt = 0;
		this.page_cnt = 0;
		this.page_total = 0;
		this.fsize = "";
		this.folder_nm = "";
		this.folder_id = "";
		this.doc_type = "";
		this.type_name = "";
		this.code_nm = "";
		this.security_levle = "";
		this.part_id = "";
		this.part_nm = "";
		this.storage_quota = 0;		
		this.ssize = "";
	}
	
	public String getSsize() {
		return StringUtil.fileSize(storage_quota);
	}

	public void setSsize(String ssize) {
		this.ssize = ssize;
	}
	
	public long getStorage_quota() {
		return storage_quota;
	}

	public void setStorage_quota(long storage_quota) {
		this.storage_quota = storage_quota;
	}

	public String getPart_id() {
		return part_id;
	}

	public void setPart_id(String part_id) {
		this.part_id = part_id;
	}

	public String getPart_nm() {
		return part_nm;
	}

	public void setPart_nm(String part_nm) {
		this.part_nm = part_nm;
	}

	public String getSecurity_levle() {
		return security_levle;
	}

	public void setSecurity_levle(String security_levle) {
		this.security_levle = security_levle;
	}

	public String getCode_nm() {
		return code_nm;
	}

	public void setCode_nm(String code_nm) {
		this.code_nm = code_nm;
	}

	public String getDoc_type() {
		return doc_type;
	}
	
	public void setDoc_type(String doc_type) {
		this.doc_type = doc_type;
	}

	public String getType_name() {
		return type_name;
	}


	public void setType_name(String type_name) {
		this.type_name = type_name;
	}


	public String getFolder_id() {
		return folder_id;
	}

	public void setFolder_id(String folder_id) {
		this.folder_id = folder_id;
	}

	public String getFolder_nm() {
		return folder_nm;
	}

	public void setFolder_nm(String folder_nm) {
		this.folder_nm = folder_nm;
	}

	public String getFsize() {
		return StringUtil.fileSize(page_total);
	}
	
	public void setFsize(String fsize) {
		this.fsize = fsize;
	}

	public String getUser_nm() {
		return user_nm;
	}
	public void setUser_nm(String user_nm) {
		this.user_nm = user_nm;
	}
	public String getOwner_id() {
		return owner_id;
	}
	public void setOwner_id(String owner_id) {
		this.owner_id = owner_id;
	}
	public String getGroup_nm() {
		return group_nm;
	}
	public void setGroup_nm(String group_nm) {
		this.group_nm = group_nm;
	}
	public String getGroup_id() {
		return group_id;
	}
	public void setGroup_id(String group_id) {
		this.group_id = group_id;
	}
	public String getMap_nm() {
		return map_nm;
	}
	public void setMap_nm(String map_nm) {
		this.map_nm = map_nm;
	}
	public String getMap_id() {
		return map_id;
	}
	public void setMap_id(String map_id) {
		this.map_id = map_id;
	}
	public int getDoc_cnt() {
		return doc_cnt;
	}
	public void setDoc_cnt(int doc_cnt) {
		this.doc_cnt = doc_cnt;
	}
	public int getPage_cnt() {
		return page_cnt;
	}
	public void setPage_cnt(int page_cnt) {
		this.page_cnt = page_cnt;
	}
	public long getPage_total() {
		return page_total;
	}
	public void setPage_total(long page_total) {
		this.page_total = page_total;
	}
		
}

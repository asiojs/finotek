package kr.co.exsoft.document.vo;

import kr.co.exsoft.eframework.configuration.Constant;
import kr.co.exsoft.eframework.util.StringUtil;

/**
 * 페이지 VO
 * @author 패키지 개발팀
 * @since 2014.07.29
 * @version 3.0
 *
 */
public class PageVO {
	
	// 테이블 객체(XR_PAGE)
	private String page_id;													// 페이지 ID
	private String page_name;											// 페이지명
	private String page_extension;										// 페이지 확장자
	private long page_size;												// 페이지 크기
	private String volume_id;												// 볼륨 ID(ARIA 암호)
	private String content_path;											// 볼륨 경로(ARIA 암호)
	private String create_date;											// 등록일	
	private String doc_id;													// ECM 문서ID - ECM XR_DOCUMENT DOC_ID / 인덱스볼륨인 경우에만 유효
	private String volume_type;											// 볼륨구분 - INDEX/FILE
	private String is_body;													// 기안문 원문여부 - 전자결재 이관문서 기안문 자체 파일인 경우 = T , 그외 F
	private String is_deleted;												// 삭제여부	- 배치수행시 T 인 파일 목록을 EXREP 에서 완전삭제처리한다.
	
	// 조회항목
	private int page_count;												// 중복파일관리 수
	@SuppressWarnings("unused")
	private String fsize;														// 파일사이즈 변환값
	private String down_path;												// 파일경로 :: 파일다운로드시 사용됨
	private String is_exrep;													// EXREP 파일 존재여부
	
	private String doc_name;												// 문서제목(ZIP파일다운로드 제목)
	private String ref_doc_id;												// XR_DOCUMENT DOC_ID
	
	public PageVO() {
		
		this.page_id = "";
		this.page_name = "";
		this.page_extension = "";
		this.page_size = 0;
		this.volume_id = "";
		this.content_path = "";
		this.create_date = "";
		this.doc_id = "";
		this.volume_type = Constant.VOLUME_FILE;
		this.is_body = Constant.F;
		this.is_deleted = Constant.F;
		this.page_count = 0;
		this.fsize = "";
		this.down_path = "";
		this.is_exrep = Constant.T;
		this.doc_name = "";
		this.ref_doc_id = "";
	}
	
	public String getRef_doc_id() {
		return ref_doc_id;
	}

	public void setRef_doc_id(String ref_doc_id) {
		this.ref_doc_id = ref_doc_id;
	}

	public String getDoc_name() {
		return doc_name;
	}

	public void setDoc_name(String doc_name) {
		this.doc_name = doc_name;
	}

	public String getIs_exrep() {
		return is_exrep;
	}

	public void setIs_exrep(String is_exrep) {
		this.is_exrep = is_exrep;
	}

	public String getDown_path() {
		return down_path;
	}

	public void setDown_path(String down_path) {
		this.down_path = down_path;
	}

	public String getFsize() {
		return StringUtil.fileSize(page_size);
	}

	public void setFsize(String fsize) {
		this.fsize = fsize;
	}

	public int getPage_count() {
		return page_count;
	}

	public void setPage_count(int page_count) {
		this.page_count = page_count;
	}

	public String getPage_id() {
		return page_id;
	}

	public void setPage_id(String page_id) {
		this.page_id = page_id;
	}

	public String getPage_name() {
		return page_name;
	}

	public void setPage_name(String page_name) {
		this.page_name = page_name;
	}

	public String getPage_extension() {
		return page_extension;
	}

	public void setPage_extension(String page_extension) {
		this.page_extension = page_extension;
	}

	public long getPage_size() {
		return page_size;
	}

	public void setPage_size(long page_size) {
		this.page_size = page_size;
	}

	public String getVolume_id() {
		return volume_id;
	}

	public void setVolume_id(String volume_id) {
		this.volume_id = volume_id;
	}

	public String getContent_path() {
		return content_path;
	}

	public void setContent_path(String content_path) {
		this.content_path = content_path;
	}

	public String getCreate_date() {
		return create_date;
	}

	public void setCreate_date(String create_date) {
		this.create_date = create_date;
	}

	public String getDoc_id() {
		return doc_id;
	}

	public void setDoc_id(String doc_id) {
		this.doc_id = doc_id;
	}

	public String getVolume_type() {
		return volume_type;
	}

	public void setVolume_type(String volume_type) {
		this.volume_type = volume_type;
	}

	public String getIs_body() {
		return is_body;
	}

	public void setIs_body(String is_body) {
		this.is_body = is_body;
	}

	public String getIs_deleted() {
		return is_deleted;
	}

	public void setIs_deleted(String is_deleted) {
		this.is_deleted = is_deleted;
	}
	
	
	
	
}

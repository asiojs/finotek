package kr.co.exsoft.process.vo;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import kr.co.exsoft.eframework.configuration.Constant;
import kr.co.exsoft.eframework.util.StringUtil;
import kr.co.exsoft.eframework.vo.VO;

/**
 * 협업 VO
 * @author 패키지 개발팀
 * @since 2015.03.12
 * @version 3.0
 *
 */
public class ProcessVO extends VO {
	
	private String process_id;			// 업무(프로세스) 키값
	private String doc_root_id;			// 협업 대상 문서 root_id
	private String creator_id;			// 등록자 user_id
	private String creator_name;		// 등록자 이름
	private String create_date;			// 업무(프로세스) 등록일
	private String name;				// 업무(프로세스) 명
	private String status;				// 업무요청(Q)>작성중(W)>승인중(A)>승인완료(AE)>보완중(M)>완료(E)
	private String expect_date;			// 업무 완료 예상일
	private Date expect_dateDB;			// DB insert 및 update에서 사용
	private String complete_date;		// 업무 실제 완료일
	private Date complete_dateDB;		// DB insert 및 update에서 사용
	private String status_nm;			// 협업 상태 한글화
	private String author_nm;			// 대표 작성자 이름
	private String write_count;			// 작성현황 ex) 1/3
	private List<String> write_list;	// 작성자 목록
	private String approval_count;		// 승인현홍 ex) 1/3
	private List<String> approval_list;	// 승인현황 목록
	private String receiver_count;		// 열람현황 ex) 2/4
	private List<String> receiver_list;	// 열람현황 목록
	private String recently_id;			// 최근 협업 등록 목록 ID
	private String content;				// 업무 요청내용
	private String status_number;		// 상세 조회에서 단계 이미지 li 순번
	
	
	public ProcessVO(){
		this.process_id = "";
		this.doc_root_id = "";
		this.creator_id = "";
		this.creator_name = "";
		this.create_date = "";
		this.name = "";
		this.status = Constant.PROCESS_STATUS_REQUEST; //Q
		this.expect_date = "";
		this.expect_dateDB = null;
		this.complete_date = "";
		this.complete_dateDB = null;
		this.status_nm = "요청";
		this.author_nm = "없음";
		this.write_count = "0/0";
		this.write_list = new ArrayList<String>();
		this.approval_count = "0/0";
		this.approval_list = new ArrayList<String>();
		this.receiver_count = "0/0";
		this.receiver_list = new ArrayList<String>();
		this.recently_id = "";
		this.content = "";
		this.status_number = "1";
	}

	public String getProcess_id() {
		return process_id;
	}

	public void setProcess_id(String process_id) {
		this.process_id = process_id;
	}

	public String getDoc_root_id() {
		return doc_root_id;
	}

	public void setDoc_root_id(String doc_root_id) {
		this.doc_root_id = doc_root_id;
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
		return (!StringUtil.isEmpty(create_date) && create_date.length() > 10 ) ? create_date.substring(0,10) : create_date;
	}

	public void setCreate_date(String create_date) {
		this.create_date = create_date;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
		//setStatus_nm()
		//업무요청(Q)>작성중(W)>승인중(A)>승인완료(AE)>보완중(M)>완료(E)
		switch (status) {
		case Constant.PROCESS_STATUS_REQUEST 		: setStatus_nm("업무요청"); 	break;
		case Constant.PROCESS_STATUS_WRITE			: setStatus_nm("작성중"); 	setStatus_number("1");	break;
		case Constant.PROCESS_STATUS_APPROVAL 		: setStatus_nm("승인중"); 	setStatus_number("2");	break;
		case Constant.PROCESS_STATUS_APPROVAL_END 	: setStatus_nm("승인완료"); 	break;
		case Constant.PROCESS_STATUS_MODIFY 		: setStatus_nm("보완중"); setStatus_number("3");		break;
		case Constant.PROCESS_STATUS_END 			: setStatus_nm("완료"); 	setStatus_number("4");	break;
		
		default:
			setStatus_nm("업무요청"); 	break;
		}
	}

	public String getExpect_date() {
		return (!StringUtil.isEmpty(expect_date) && expect_date.length() > 10 ) ? expect_date.substring(0,10) : expect_date;
	}

	public void setExpect_date(String expect_date) {
		this.expect_date = expect_date;
	}

	public String getComplete_date() {
		return (!StringUtil.isEmpty(complete_date) && complete_date.length() > 10 ) ? complete_date.substring(0,10) : complete_date;
	}

	public void setComplete_date(String complete_date) {
		this.complete_date = complete_date;
	}
	
	public String getStatus_nm() {
		return status_nm;
	}

	public void setStatus_nm(String status_nm) {
		this.status_nm = status_nm;
	}

	public String getAuthor_nm() {
		return author_nm;
	}

	public void setAuthor_nm(String author_nm) {
		this.author_nm = author_nm;
	}

	public String getWrite_count() {
		return write_count;
	}

	public void setWrite_count(String write_count) {
		this.write_count = write_count;
	}

	public List<String> getWrite_list() {
		return write_list;
	}

	public void setWrite_list(List<String> write_list) {
		this.write_list = write_list;
	}

	public String getApproval_count() {
		return approval_count;
	}

	public void setApproval_count(String approval_count) {
		this.approval_count = approval_count;
	}

	public List<String> getApproval_list() {
		return approval_list;
	}

	public void setApproval_list(List<String> approval_list) {
		this.approval_list = approval_list;
	}

	public String getReceiver_count() {
		return receiver_count;
	}

	public void setReceiver_count(String receiver_count) {
		this.receiver_count = receiver_count;
	}

	public List<String> getReceiver_list() {
		return receiver_list;
	}

	public void setReceiver_list(List<String> receiver_list) {
		this.receiver_list = receiver_list;
	}

	public String getRecently_id() {
		return recently_id;
	}

	public void setRecently_id(String recently_id) {
		this.recently_id = recently_id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getExpect_dateDB() {
		return expect_dateDB;
	}

	public void setExpect_dateDB(Date expect_dateDB) {
		this.expect_dateDB = expect_dateDB;
	}

	public String getStatus_number() {
		return status_number;
	}

	public void setStatus_number(String status_number) {
		this.status_number = status_number;
	}

	public Date getComplete_dateDB() {
		return complete_dateDB;
	}

	public void setComplete_dateDB(Date complete_dateDB) {
		this.complete_dateDB = complete_dateDB;
	}

}

package kr.co.exsoft.process.vo;

import java.sql.Date;

import kr.co.exsoft.eframework.util.StringUtil;
import kr.co.exsoft.eframework.vo.VO;

/**
 * 협업 VO
 * @author 패키지 개발팀
 * @since 2015.03.12
 * @version 3.0
 *
 */
public class ProcessExecutorVO extends VO {
	
	private String execute_id;				// 업무 실행자 키값
	private String process_id;				// 업무(프로세스) ID
	private String doc_root_id;				// 협업 대상 문서 root_id, 목록 조회 편의를 위해 사용
	private String type;					// TYPE에 따라 실행자의 역할이 결정. 업무요청자(Q),작성자(R),공동작성자(C),승인자(A),수신자(I)" 
	private String executor_id;				// 실행자 ID
	private String executor_name;			// 실행자 이름
	private int sort_index;					// 승인의 경우 승인 순서로도 사용
	private String status;					// 실행상태. 초기값(N), 대기(W), 시작(S), 종료(E)
	private String start_date;				// 작업 시작일 
	private String end_date;				// 작업 완료일
	private Date start_dateDB;				// DB insert 및 update에서 사용
	private Date end_dateDB;				// DB insert 및 update에서 사용
	private String updateDBType;			// db update type;
	
	private String group_nm;				// 협업자 선택 팝업에서 협업자 테이블에 정의 되어 있는 name 속성 값
	private String user_nm;					// 협업자 선택 팝업에서 협업자 테이블에 정의 되어 있는 name 속성 값
	private String user_id;					// 협업자 선택 팝업에서 협업자 테이블에 정의 되어 있는 name 속성 값
	
	public ProcessExecutorVO(){
		this.execute_id = "";
		this.process_id = "";
		this.doc_root_id = "";
		this.type = "";
		this.executor_id = "";
		this.executor_name = "";
		this.sort_index = 0;
		this.status = "";     //N
		this.start_date = "";
		this.end_date = "";
		this.group_nm = "";
		this.user_nm = "";
		this.user_id = "";
		this.updateDBType = "";
	}

	public String getExecute_id() {
		return execute_id;
	}

	public void setExecute_id(String execute_id) {
		this.execute_id = execute_id;
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getExecutor_id() {
		return executor_id;
	}

	public void setExecutor_id(String executor_id) {
		this.executor_id = executor_id;
		setUser_id(executor_id);
	}

	public String getExecutor_name() {
		return executor_name;
	}

	public void setExecutor_name(String executor_name) {
		this.executor_name = executor_name;
		setUser_nm(executor_name);
	}

	public int getSort_index() {
		return sort_index;
	}

	public void setSort_index(int sort_index) {
		this.sort_index = sort_index;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStart_date() {
		return (!StringUtil.isEmpty(start_date) && start_date.length() > 10 ) ? start_date.substring(0,10) : start_date;
	}

	public void setStart_date(String start_date) {
		this.start_date = start_date;
	}

	public String getEnd_date() {
		return (!StringUtil.isEmpty(end_date) && end_date.length() > 10 ) ? end_date.substring(0,10) : end_date;
	}

	public void setEnd_date(String end_date) {
		this.end_date = end_date;
	}

	public String getGroup_nm() {
		return group_nm;
	}

	public void setGroup_nm(String group_nm) {
		this.group_nm = group_nm;
	}

	public String getUser_nm() {
		return user_nm;
	}

	public void setUser_nm(String user_nm) {
		this.user_nm = user_nm;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public Date getStart_dateDB() {
		return start_dateDB;
	}

	public void setStart_dateDB(Date start_dateDB) {
		this.start_dateDB = start_dateDB;
	}

	public Date getEnd_dateDB() {
		return end_dateDB;
	}

	public void setEnd_dateDB(Date end_dateDB) {
		this.end_dateDB = end_dateDB;
	}

	public String getUpdateDBType() {
		return updateDBType;
	}

	public void setUpdateDBType(String updateDBType) {
		this.updateDBType = updateDBType;
	}
}


package kr.co.exsoft.common.vo;

import kr.co.exsoft.eframework.configuration.Constant;

/**
 * 문서댓글 VO
 * @author 패키지 개발팀
 * @since 2015.03.02
 * @version 3.0
 *
 */
public class CommentVO {

	private String com_id;													// 키값 :: XR_COUNTER TABLE REF
	private String doc_root_id;											// 댓글이 달린 문서에 대한 ROOT_ID(XR_DOCUMENT)
	private String com_step;												// 댓글의 그룹번호 즉, 원글 값이 1일경우 원글의 대한 답글 및 답글의 답글 값도 1이 된다.
	private String com_order;												// 동일한 COM_STEP내에서 정렬순서. 원글은 무조건 0
	private String creator_id;												// 댓글 등록자 ID
	private String creator_name;											// 댓글 등록자 이름
	private String parent_creator_name;								// 답글일 경우 원글 등록자 이름, 답글의 답글일 경우 답글의 등록자 이름
	private String create_date;											// 댓글 등록일
	private String content;													// 등록된 댓글의 내용
	private String status;													// C:댓글등록, D:댓글삭제
	
	public CommentVO() {
		this.com_id="";
		this.doc_root_id="";
		this.com_step="";
		this.com_order="0";
		this.creator_id="";
		this.creator_name="";
		this.parent_creator_name="";
		this.create_date="";
		this.content="";
		this.status=Constant.C;
	}

	public String getCom_id() {
		return com_id;
	}

	public void setCom_id(String com_id) {
		this.com_id = com_id;
	}

	public String getDoc_root_id() {
		return doc_root_id;
	}

	public void setDoc_root_id(String doc_root_id) {
		this.doc_root_id = doc_root_id;
	}

	public String getCom_step() {
		return com_step;
	}

	public void setCom_step(String com_step) {
		this.com_step = com_step;
	}

	public String getCom_order() {
		return com_order;
	}

	public void setCom_order(String com_order) {
		this.com_order = com_order;
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

	public String getParent_creator_name() {
		return parent_creator_name;
	}

	public void setParent_creator_name(String parent_creator_name) {
		this.parent_creator_name = parent_creator_name;
	}

	public String getCreate_date() {
		return create_date;
	}

	public void setCreate_date(String create_date) {
		this.create_date = create_date;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	
}

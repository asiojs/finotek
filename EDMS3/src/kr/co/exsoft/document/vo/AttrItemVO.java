package kr.co.exsoft.document.vo;

/**
 * 문서유형 속성 아이템 VO
 * @author 패키지 개발팀
 * @since 2014.07.29
 * @version 3.0
 *
 */
public class AttrItemVO {

	// 테이블 객체(XR_ATTRITEM)
	private String type_id;								// 문서유형ID
	private String attr_id;								// 속성ID
	private int item_index;								// 아이템 번호	
	private String item_name;							// 아이템명
	
	public AttrItemVO() {
		this.type_id = "";
		this.attr_id = "";
		this.item_index = 0;
		this.item_name = "";
	}

	public String getType_id() {
		return type_id;
	}

	public void setType_id(String type_id) {
		this.type_id = type_id;
	}

	public String getAttr_id() {
		return attr_id;
	}

	public void setAttr_id(String attr_id) {
		this.attr_id = attr_id;
	}

	public int getItem_index() {
		return item_index;
	}

	public void setItem_index(int item_index) {
		this.item_index = item_index;
	}

	public String getItem_name() {
		return item_name;
	}

	public void setItem_name(String item_name) {
		this.item_name = item_name;
	}
	
	
}

package kr.co.exsoft.document.vo;

import java.util.List;

import kr.co.exsoft.eframework.configuration.Constant;
/**
 * 문서유형 속성 VO
 * @author 패키지 개발팀
 * @since 2014.07.29
 * @version 3.0
 *
 */
public class AttrVO {

	// 테이블 객체(XR_ATTR)
	private String type_id;								// 문서유형ID
	private String attr_id;								// 속성ID
	private String attr_name;							// 속성명
	private int attr_size;									// 속성크기	
	private String is_extended;						// 확장속성 여부(T/F)
	private String is_editable;							// 편집가능 여부(T/F)
	private String is_mandatory;						// 필수 여부(T/F)
	private String is_search;							// 검색기능 여부(T/F)
	private int sort_index;								// 정렬 순서
	private String display_type;						// 표현 유형 - INPUT/TEXTBOX/SELECTBOX/CHECKBOX/RADIOBUTTON 		
	private String has_item;							// 속성 아이템 소유 여부(T/F)
	private int default_item_index;					// 기본 속성 아이템 번호 XR_ATTRITEM 테이블의 ITEM_INDEX
	private String has_item_list;						// 그리드 화면에서 사용하기 위한 값
	private List<AttrItemVO> item_list;				// 속성아이템 리스트
	private String attr_stauts;							// 속성상태(insert/update/delete) 문서유형 수정시 사용됨	
	private String is_locked;							// 잠김여부 :: 문서속성ID 수정못하게 하는 옵션
	
	// 조회항목
	private String attr_value;							// 속성값 :: 확장속성인 경우 해당됨
	
	// 검색어
	private String search_word;							// 속성별 검색어
	
	public AttrVO() {
		
		this.type_id = "";
		this.attr_id = "";
		this.attr_name = "";
		this.attr_size = 1;
		this.is_extended = Constant.T;
		this.is_editable = Constant.T;
		this.is_mandatory = Constant.F;;
		this.is_search = Constant.T;
		this.sort_index = 0;
		this.has_item_list = "";
		this.attr_stauts = "";
		this.is_locked = Constant.F;
		this.attr_value = "";
		this.search_word = "";
	}
			
	public String getAttr_value() {
		return attr_value;
	}

	public void setAttr_value(String attr_value) {
		this.attr_value = attr_value;
	}

	public String getIs_locked() {
		return is_locked;
	}

	public void setIs_locked(String is_locked) {
		this.is_locked = is_locked;
	}

	public String getAttr_stauts() {
		return attr_stauts;
	}

	public void setAttr_stauts(String attr_stauts) {
		this.attr_stauts = attr_stauts;
	}

	public String getHas_item_list() {
		return has_item_list;
	}

	public void setHas_item_list(String has_item_list) {
		this.has_item_list = has_item_list;
	}

	public List<AttrItemVO> getItem_list() {
		return item_list;
	}

	public void setItem_list(List<AttrItemVO> item_list) {
		this.item_list = item_list;
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

	public String getAttr_name() {
		return attr_name;
	}

	public void setAttr_name(String attr_name) {
		this.attr_name = attr_name;
	}

	public int getAttr_size() {
		return attr_size;
	}

	public void setAttr_size(int attr_size) {
		this.attr_size = attr_size;
	}

	public String getIs_extended() {
		return is_extended;
	}

	public void setIs_extended(String is_extended) {
		this.is_extended = is_extended;
	}

	public String getIs_editable() {
		return is_editable;
	}

	public void setIs_editable(String is_editable) {
		this.is_editable = is_editable;
	}

	public String getIs_mandatory() {
		return is_mandatory;
	}

	public void setIs_mandatory(String is_mandatory) {
		this.is_mandatory = is_mandatory;
	}

	public String getIs_search() {
		return is_search;
	}

	public void setIs_search(String is_search) {
		this.is_search = is_search;
	}

	public int getSort_index() {
		return sort_index;
	}

	public void setSort_index(int sort_index) {
		this.sort_index = sort_index;
	}

	public String getDisplay_type() {
		return display_type;
	}

	public void setDisplay_type(String display_type) {
		this.display_type = display_type;
	}

	public String getHas_item() {
		return has_item;
	}

	public void setHas_item(String has_item) {
		this.has_item = has_item;
	}

	public int getDefault_item_index() {
		return default_item_index;
	}

	public void setDefault_item_index(int default_item_index) {
		this.default_item_index = default_item_index;
	}

	public String getSearch_word() {
		return search_word;
	}

	public void setSearch_word(String search_word) {
		this.search_word = search_word;
	}
	
	
}

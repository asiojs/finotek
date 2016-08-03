package kr.co.exsoft.common.vo;

/**
 * 맵정보 VO
 * @author 패키지 개발팀
 * @since 2014.07.29
 * @version 3.0
 *
 */
public class MapVO {

	// 테이블 객체(XR_MAP)
    private String map_id;															// 맵ID
    private String map_nm_ko;													// 	맵명 - 한국어
    private String map_nm_en;													// 	맵명 - 영어
    private String map_nm_ja;													// 	맵명 - 일본어	
    private String map_nm_zh;													// 	맵명 - 중국어	
    private String map_type;    													// 맵구분 : FOLDER - 분류체계 GROUP - 부서/프로젝트그룹
    private int sort_index;															// 정렬순서
    private String is_use;															// 사용여부
    private String is_sys;															// 시스템제공여부 - 시스템제공 맵은 삭제할 수 없다.
    private String is_sub;															// 하위폴더추가여부 : MAP_TYPE=FOLDER 인 경우 N이면 하위분류쳬계 생성못함(연계로만 처리)
	
    // 조회항목
    private String map_nm;														//	맵명
    private String map_type_nm;													//	맵구분명
    
    public MapVO() {
    	
    	this.map_id = "";
    	this.map_nm_ko = "";
    	this.map_nm_en = "";
    	this.map_nm_ja = "";
    	this.map_nm_zh = "";
    	this.map_type = "";
    	this.sort_index = 0;
    	this.is_use = "Y";
    	this.is_sys = "N";
    	this.is_sub = "Y";
    	this.map_nm = "";
    	this.map_type_nm = "";
    	
    }

	public String getMap_id() {
		return map_id;
	}

	public void setMap_id(String map_id) {
		this.map_id = map_id;
	}

	public String getMap_nm_ko() {
		return map_nm_ko;
	}

	public void setMap_nm_ko(String map_nm_ko) {
		this.map_nm_ko = map_nm_ko;
	}

	public String getMap_nm_en() {
		return map_nm_en;
	}

	public void setMap_nm_en(String map_nm_en) {
		this.map_nm_en = map_nm_en;
	}

	public String getMap_nm_ja() {
		return map_nm_ja;
	}

	public void setMap_nm_ja(String map_nm_ja) {
		this.map_nm_ja = map_nm_ja;
	}

	public String getMap_nm_zh() {
		return map_nm_zh;
	}

	public void setMap_nm_zh(String map_nm_zh) {
		this.map_nm_zh = map_nm_zh;
	}

	public String getMap_type() {
		return map_type;
	}

	public void setMap_type(String map_type) {
		this.map_type = map_type;
	}

	public int getSort_index() {
		return sort_index;
	}

	public void setSort_index(int sort_index) {
		this.sort_index = sort_index;
	}

	public String getIs_use() {
		return is_use;
	}

	public void setIs_use(String is_use) {
		this.is_use = is_use;
	}

	public String getIs_sys() {
		return is_sys;
	}

	public void setIs_sys(String is_sys) {
		this.is_sys = is_sys;
	}

	public String getIs_sub() {
		return is_sub;
	}

	public void setIs_sub(String is_sub) {
		this.is_sub = is_sub;
	}

	public String getMap_nm() {
		return map_nm;
	}

	public void setMap_nm(String map_nm) {
		this.map_nm = map_nm;
	}

	public String getMap_type_nm() {
		return map_type_nm;
	}

	public void setMap_type_nm(String map_type_nm) {
		this.map_type_nm = map_type_nm;
	}
    
    
    
}

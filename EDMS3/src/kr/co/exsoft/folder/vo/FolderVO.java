package kr.co.exsoft.folder.vo;

import kr.co.exsoft.eframework.configuration.Constant;
import kr.co.exsoft.eframework.util.StringUtil;

/**
 * 폴더 VO
 * @author 패키지 개발팀
 * @since 2014.07.29
 * @version 3.0
 *
 */
public class FolderVO {

	// 테이블 객체(XR_FOLDER)
	private String folder_id;										// 폴더ID
	private String folder_name_ko;									// 폴더명 - 한국어
	private String folder_name_en;									// 폴더명 - 영어
	private String folder_name_ja;									// 폴더명 - 일본어	
//	private String folder_name_zh;									// 폴더명 - 중국어
	private String parent_id;										// 상위 폴더ID - 최상위 폴더인 경우 FOLDER_ID = PARENT_ID 가 동일함
	private String folder_type;										// 폴더타입(유형)
	private int sort_index;											// 정렬순서
	private String folder_status;									// 폴더상태 - C:활성 U:수정 D:비활성(폐쇄)
	private String map_id;											// 맵ID
	private String acl_id;											// ACL ID
	private String creator_id;										// 등록자 ID
	private long storage_quota;										// 폴더 할당량 -1 : 할당량 제한없음
	private long storage_usage;										// 폴더 사용량		
	private String create_date;										// 등록일
	private String is_save;											// 문서 저장여부	: Y:저장 N:저장안됨 - 부서폴더는 기본적으로 문서저장안됨			
	private String is_type;											// 문서유형 : 부서폴더-ALL_TYPE이 기본선택
	private String is_inherit_acl;									// 상위권한 상속여부 - 상위권한변경 또는 폴더이동시 T=상위권한 상속 F=변경안함 
	private String is_share;										// 공유여부
	
	// 조회 객체        
	private String folder_nm;										// 폴더명 - 기본언어		
	private String storage_qutoa_vw;								// 폴더 할당량 조회값 MB/GB 단위 표현
	private String storage_usage_vw;								// 폴더 사용량 조회값 MB/GB 단위 표현
	private String folder_status_nm;								// 폴더상태명
	private String map_nm;											// 맵명
	private String creator_name;									// 등록자 명
	private String folder_description;								// 폴더 등록 내용
	//Data + time 출력
	private String create_date_withTime;							// 등록일	
	
	//조회 시 권한 관련 셋팅 
	private String acl_level;										// 폴더 목록에서 사용되는 권한 명
	private String acl_create;										// 폴더 등록 권한
	private String acl_changePermission;							// 폴더 권한 변경
	private String acl_document_create;								// 해당 폴더에 문서 등록 권한
	
	// 계층구조 표현을 위한 FolderVO
	private FolderVO childVO;
	
	// JS Tree 관련
    private int children_count;
    
    // update action 정의
 	private String update_action;							// 업데이트 시  action 값
	
	public FolderVO() {
		
		this.folder_id = "";
		this.folder_name_ko = "";
		this.parent_id = "";
		this.folder_type = "";
		this.sort_index = 0;
		this.folder_status = "C";
		this.map_id = "";
		this.acl_id = "";
		this.creator_id = "";
		this.storage_quota = -1;
		this.storage_usage = 0;
		this.create_date = "";
		this.is_save = "Y";
		this.is_type = "";
		this.is_inherit_acl = Constant.F;
		this.children_count = 0;
		this.creator_name = "";
		this.folder_description = "";	
		this.acl_level = "";
		this.acl_create = Constant.F;
		this.acl_changePermission = Constant.F;
		this.update_action= "";
		this.is_share = Constant.F;
		this.create_date_withTime = "";
		this.acl_document_create = Constant.F;
	}

	public String getFolder_id() {
		return folder_id;
	}

	public void setFolder_id(String folder_id) {
		this.folder_id = folder_id;
	}

	public String getFolder_name_ko() {
		return folder_name_ko;
	}

	public void setFolder_name_ko(String folder_name_ko) {
		this.folder_name_ko = folder_name_ko;
	}

	public String getFolder_name_en() {
		return folder_name_en;
	}

	public void setFolder_name_en(String folder_name_en) {
		this.folder_name_en = folder_name_en;
	}

	public String getFolder_name_ja() {
		return folder_name_ja;
	}

	public void setFolder_name_ja(String folder_name_ja) {
		this.folder_name_ja = folder_name_ja;
	}

//	public String getFolder_name_zh() {
//		return folder_name_zh;
//	}
//
//	public void setFolder_name_zh(String folder_name_zh) {
//		this.folder_name_zh = folder_name_zh;
//	}

	public String getParent_id() {
		return parent_id;
	}

	public void setParent_id(String parent_id) {
		this.parent_id = parent_id;
	}

	public String getFolder_type() {
		return folder_type;
	}

	public void setFolder_type(String folder_type) {
		this.folder_type = folder_type;
	}

	public int getSort_index() {
		return sort_index;
	}

	public void setSort_index(int sort_index) {
		this.sort_index = sort_index;
	}

	public String getFolder_status() {
		return folder_status;
	}

	public void setFolder_status(String folder_status) {
		this.folder_status = folder_status;
	}

	public String getMap_id() {
		return map_id;
	}

	public void setMap_id(String map_id) {
		this.map_id = map_id;
	}

	public String getCreator_id() {
		return creator_id;
	}

	public void setCreator_id(String creator_id) {
		this.creator_id = creator_id;
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

	public String getCreate_date() {
		return (!StringUtil.isEmpty(create_date) && create_date.length() > 10 ) ? create_date.substring(0,10) : create_date;
	}

	public void setCreate_date(String create_date) {
		this.create_date = create_date;
	}

	public String getIs_save() {
		return is_save;
	}

	public void setIs_save(String is_save) {
		this.is_save = is_save;
	}

	public String getIs_type() {
		return is_type;
	}

	public void setIs_type(String is_type) {
		this.is_type = is_type;
	}

	public String getIs_inherit_acl() {
		return is_inherit_acl;
	}

	public void setIs_inherit_acl(String is_inherit_acl) {
		this.is_inherit_acl = is_inherit_acl;
	}

	public String getAcl_id() {
		return acl_id;
	}

	public void setAcl_id(String acl_id) {
		this.acl_id = acl_id;
	}

	public String getFolder_nm() {
		return folder_nm;
	}

	public void setFolder_nm(String folder_nm) {
		this.folder_nm = folder_nm;
	}

	public String getStorage_qutoa_vw() {
		return storage_qutoa_vw;
	}

	public void setStorage_qutoa_vw(String storage_qutoa_vw) {
		this.storage_qutoa_vw = storage_qutoa_vw;
	}

	public String getStorage_usage_vw() {
		return storage_usage_vw;
	}

	public void setStorage_usage_vw(String storage_usage_vw) {
		this.storage_usage_vw = storage_usage_vw;
	}

	public String getFolder_status_nm() {
		return folder_status_nm;
	}

	public void setFolder_status_nm(String folder_status_nm) {
		this.folder_status_nm = folder_status_nm;
	}

	public String getMap_nm() {
		return map_nm;
	}

	public void setMap_nm(String map_nm) {
		this.map_nm = map_nm;
	}

	public int getChildren_count() {
		return children_count;
	}

	public void setChildren_count(int children_count) {
		this.children_count = children_count;
	}

	public String getCreator_name() {
		return creator_name;
	}

	public void setCreator_name(String creator_name) {
		this.creator_name = creator_name;
	}

	public String getFolder_description() {
		return folder_description;
	}

	public void setFolder_description(String folder_description) {
		this.folder_description = folder_description;
	}

	public FolderVO getChildVO() {
		return childVO;
	}

	public void setChildVO(FolderVO childVO) {
		this.childVO = childVO;
	}

	public String getAcl_level() {
		return acl_level;
	}

	public void setAcl_level(String acl_level) {
		this.acl_level = acl_level;
	}

	public String getAcl_create() {
		return acl_create;
	}

	public void setAcl_create(String acl_create) {
		this.acl_create = acl_create;
	}

	public String getAcl_changePermission() {
		return acl_changePermission;
	}

	public void setAcl_changePermission(String acl_changePermission) {
		this.acl_changePermission = acl_changePermission;
	}

	public String getUpdate_action() {
		return update_action;
	}

	public void setUpdate_action(String update_action) {
		this.update_action = update_action;
	}
	
	public String getIs_share() {
		return is_share;
	}

	public void setIs_share(String is_share) {
		this.is_share = is_share;
	}
	
	public String getCreate_date_withTime() {
		return !StringUtil.isEmpty(create_date_withTime) ? create_date_withTime : create_date;
	}

	public void setCreate_date_withTime(String create_date_withTime) {
		this.create_date_withTime = create_date_withTime;
	}

	public String getAcl_document_create() {
		return acl_document_create;
	}

	public void setAcl_document_create(String acl_document_create) {
		this.acl_document_create = acl_document_create;
	}
	
}

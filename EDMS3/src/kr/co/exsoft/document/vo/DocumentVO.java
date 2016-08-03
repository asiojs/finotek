package kr.co.exsoft.document.vo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import kr.co.exsoft.eframework.configuration.Constant;
import kr.co.exsoft.eframework.util.StringUtil;
import kr.co.exsoft.eframework.util.UtilFileApp;
import kr.co.exsoft.eframework.vo.VO;
import kr.co.exsoft.permission.vo.AclExItemVO;
/**
 * 문서 VO
 * @author 패키지 개발팀
 * @since 2014.07.29
 * @version 3.0
 *
 */
public class DocumentVO extends VO {

	// 테이블 객체(XR_DOCUMENT,XR_DOCUMENT_DEL)
	private String doc_id;									// 문서ID
	private String doc_name;								// 문서명
	private String doc_type;								// 문서유형ID
	private int page_cnt;									// 첨부파일수
	private String root_id;									// 루트버전 문서ID - NULL : 최상위버전
	private String is_current;								// 최신 여부
	private String is_locked;								// 잠금 여부
	private String lock_owner;							// 잠금 사용자 ID
	private String lock_date;								// 잠금 날짜
	private String version_no;								// 버전
	private String version_note;							// 버전노트
	private int preservation_year;							// 보존년한 - 0:무한 - 1/3/5/10/15/0
	private String expired_date;							// 만기날짜
	private String is_expired;								// 만기여부	
	private String doc_status;								// 문서상태 - C:활성 U:수정 D:개인휴지통 E:관리자 휴지통
	private String acl_id;									// ACL_ID
	private String creator_id;								// 등록자ID	
	private String creator_name;							// 등록자명
	private String create_date;							// 등록일	
	private String updater_id;								// 수정자ID
	private String updater_name;						// 수정자명		
	private String update_date;							// 수정일		
	private String deleter_id;								// 삭제자ID
	private String deleter_name;							// 삭제자명
	private String delete_date;							// 삭제일
	private String ref_id;									// 참조문서ID - URL LINK 복사시 사용되며, RGATE 문서유형의 경우 문서수정시 DOC_ID 변경됨
	private String doc_no;									// 문서번호
	private String is_trans;									// 기존문서 이관여부
	private String is_inherit_acl;							// 상위권한 상속여부 - 폴더권한변경 또는 문서이동시 T=상위권한 상속 F=변경안함 
	private String location;									// 문서생성위치 - 탐색기등록:C 웹등록:W				
	private String waste_date;								// 휴지통 등록일 - 개인휴지통에서 삭제처리 후 이동한 날짜		
	private String access_grade;							// 직급권한
	private String folder_id;								// 메인폴더ID
	private long page_total;								// 첨부파일용량
	private String security_level;							// 보안등급 :: XR_CODE 정의 일반/대외비/극비
	private String keyword;								// 키워드		
	private String owner_id;								// 소유자 ID
	private String owner_name;							// 소유자 명
	private String doc_description;						// 문서 상세 설명
	private String is_share;									// 공유여부
	private String author_list;								// 작성자 리스트
	private long read_cnt;									// 문서조회수(첨부파일 기준)
	private String lock_owner_name;							// 체크아웃한 사람명
	private String show_date;								// 화면에보일 날짜(나의문서 분기용)
	
	// 조회항목
	private String type_name;								// 문서유형명
	@SuppressWarnings("unused")
	private String lock_image;								// 잠금여부 이미지 경로		
	private String lnk_folder_id;							// 폴더ID - XR_LINKED 정보
	private String is_system;								// 확장문서유형여부 :: T:기본문서유형 F:확장문서유형
	private String tbl_name;								// 문서유형 테이블명
	private String ref_image;								// 참조문서 이미지 경로
	private String relation_doc;							// 연결문서
	private String lock_status;								// 잠금 상태 확인
	private String page_extension;						// 첨부파일 확장자
	@SuppressWarnings("unused")
	private String page_extension_img;				// 첨부파일 확장자 이미지
	
	//Data + time 출력
	private String lock_date_withTime;					// 잠금 날짜
	private String expired_date_withTime;				// 만기날짜
	private String create_date_withTime;				// 등록일	
	private String update_date_withTime;				// 수정일		
	private String delete_date_withTime;				// 삭제일
	private String waste_date_withTime;				// 휴지통 등록일 - 개인휴지통에서 삭제처리 후 이동한 날짜
	
	//조회 시 권한 관련 셋팅 
	private String acl_level;								// 문서 목록에서 사용되는 권한 명
	private String acl_create;								// 문서 등록 권한
	private String acl_checkoutCancel;					// 반출 취소
	private String acl_changePermission;				// 권한 변경
	
	// 첨부파일 목록
	private List<PageVO> pageList;						//첨부파일 목록
	
	// update action 정의
	private String update_action;							// 업데이트 시  action 값
	
	// 문서등록 파라미터 정의
	private List<String> refList;											// 관련문서 리스트
	private List<String> multiFolders;									// 다차원분류 리스트
	private List<AclExItemVO> aclExItemList;						// 추가접근자 리스트
	private List<HashMap<String,Object>> insertFileList;		// 신규추가되는 파일 목록 리스트
	private List<HashMap<String,Object>> delFileList;			// 이전버전에서 삭제되는 파일 목록 리스트
	private String map_id;													// 문서구분 map_id
	private String pre_doc_id;												// 문서수정시 사용됨 이전버전ID
	private String work_date;												// 임시작업함 등록일
	@SuppressWarnings("unused")
	private String doc_name_limit;										// 문서제목(줄임)
	private String read_date;												// 문서조회일
	
	public DocumentVO() {
		
		this.doc_id = "";
		this.doc_name = "";
		this.doc_type = "";
		this.page_cnt = 0;
		this.root_id = "";
		this.is_current = "";
		this.is_locked = Constant.F;
		this.lock_owner = "";
		this.lock_date = "";
		this.version_no = "1.0";
		this.version_note = "";
		this.preservation_year = 0;
		this.expired_date = "";
		this.is_expired = Constant.F;
		this.doc_description = "";
		this.doc_status = "C";
		this.acl_id = "";
		this.creator_id = "";
		this.creator_name = "";
		this.create_date = "";
		this.updater_id = "";
		this.updater_name = "";
		this.update_date = "";
		this.deleter_id = "";
		this.deleter_name = "";
		this.delete_date = "";
		this.ref_id = "";
		this.doc_no = "";
		this.is_trans = Constant.F;
		this.is_inherit_acl = Constant.F;
		this.location = "W";
		this.waste_date = "";
		this.type_name = "";
		this.lock_image = "";
		this.folder_id = "";
		this.is_system = Constant.F;
		this.tbl_name = "";
		this.access_grade = "";
		this.page_total = 0;
		this.keyword = "";
		this.security_level = "";
		this.lnk_folder_id = "";
		this.ref_image = "";
		this.acl_level = "";
		this.acl_create = "F";
		this.acl_checkoutCancel = Constant.F;
		this.acl_changePermission = Constant.F;
		this.update_action= "";
		this.is_share = Constant.F;
		this.refList = new ArrayList<String>();
		this.multiFolders = new ArrayList<String>();
		this.aclExItemList = new ArrayList<AclExItemVO>();
		this.insertFileList = new ArrayList<HashMap<String,Object>>();
		this.lock_date_withTime = "";
		this.expired_date_withTime = "";
		this.create_date_withTime = "";	
		this.update_date_withTime = "";		
		this.delete_date_withTime = "";
		this.waste_date_withTime = "";		
		this.map_id = "";
		this.delFileList = new ArrayList<HashMap<String,Object>>();
		this.pre_doc_id = "";
		this.author_list = "";
		this.work_date = "";
		this.read_cnt = 0;
		this.page_extension = "";
		this.page_extension_img = "";
		this.doc_name_limit = "";
		this.read_date = "";
		this.show_date = "";
	}
	
	public String getRead_date() {
		return read_date;
	}

	public void setRead_date(String read_date) {
		this.read_date = read_date;
	}

	public String getDoc_name_limit() {
		if(this.doc_name.length() > 25)	{			
			return this.doc_name.substring(0,25) + "...";
		}else {
			return this.doc_name;
		}
	}

	public void setDoc_name_limit(String doc_name_limit) {
		this.doc_name_limit = doc_name_limit;
	}

	public String getPage_extension_img() {
		if(this.page_extension != null && !this.page_extension.equals("")) {
			String[] fileType = UtilFileApp.getFileType(this.page_extension);
			return  "/img/extension/" + fileType[1];
		}else {
			return "/img/extension/no_file.png";			
		}
	}

	public void setPage_extension_img(String page_extension_img) {
		this.page_extension_img = page_extension_img;
	}


	public long getRead_cnt() {
		return read_cnt;
	}

	public void setRead_cnt(long read_cnt) {
		this.read_cnt = read_cnt;
	}

	public String getAuthor_list() {
		return author_list;
	}

	public void setAuthor_list(String author_list) {
		this.author_list = author_list;
	}

	public List<HashMap<String, Object>> getDelFileList() {
		return delFileList;
	}

	public void setDelFileList(List<HashMap<String, Object>> delFileList) {
		this.delFileList = delFileList;
	}

	public String getPre_doc_id() {
		return pre_doc_id;
	}

	public void setPre_doc_id(String pre_doc_id) {
		this.pre_doc_id = pre_doc_id;
	}

	public String getMap_id() {
		return map_id;
	}

	public void setMap_id(String map_id) {
		this.map_id = map_id;
	}

	public List<HashMap<String, Object>> getInsertFileList() {
		return insertFileList;
	}

	public void setInsertFileList(List<HashMap<String, Object>> insertFileList) {
		this.insertFileList = insertFileList;
	}

	public List<String> getRefList() {
		return refList;
	}

	public void setRefList(List<String> refList) {
		this.refList = refList;
	}

	public List<String> getMultiFolders() {
		return multiFolders;
	}

	public void setMultiFolders(List<String> multiFolders) {
		this.multiFolders = multiFolders;
	}

	public List<AclExItemVO> getAclExItemList() {
		return aclExItemList;
	}

	public void setAclExItemList(List<AclExItemVO> aclExItemList) {
		this.aclExItemList = aclExItemList;
	}

	public String getIs_share() {
		return is_share;
	}

	public void setIs_share(String is_share) {
		this.is_share = is_share;
	}

	public String getRef_image() {
		return ref_image;
	}

	public void setRef_image(String ref_image) {
		this.ref_image = ref_image;
	}

	public String getAccess_grade() {
		return access_grade;
	}

	public void setAccess_grade(String access_grade) {
		this.access_grade = access_grade;
	}

	public long getPage_total() {
		return page_total;
	}

	public void setPage_total(long page_total) {
		this.page_total = page_total;
	}

	public String getSecurity_level() {
		return security_level;
	}

	public void setSecurity_level(String security_level) {
		this.security_level = security_level;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getLnk_folder_id() {
		return lnk_folder_id;
	}

	public void setLnk_folder_id(String lnk_folder_id) {
		this.lnk_folder_id = lnk_folder_id;
	}

	public String getTbl_name() {
		return tbl_name;
	}

	public void setTbl_name(String tbl_name) {
		this.tbl_name = tbl_name;
	}

	public String getIs_system() {
		return is_system;
	}

	public void setIs_system(String is_system) {
		this.is_system = is_system;
	}

	public String getFolder_id() {
		return folder_id;
	}
	
	public void setFolder_id(String folder_id) {
		this.folder_id = folder_id;
	}

	public String getLock_image() {
		
		String path = "";		
		if(is_locked.equals(Constant.T))	{
			path = "<image src='../../img/locked_small.png'>";
		}
		return path;
	}

	public void setLock_image(String lock_image) {
		this.lock_image = lock_image;
	}

	public String getWaste_date() {
		return (!StringUtil.isEmpty(waste_date) && waste_date.length() > 10 ) ? waste_date.substring(0,10) : waste_date;
	}

	public void setWaste_date(String waste_date) {
		this.waste_date = waste_date;
	}

	public String getDoc_id() {
		return doc_id;
	}

	public void setDoc_id(String doc_id) {
		this.doc_id = doc_id;
	}

	public String getDoc_name() {
		return doc_name;
	}

	public void setDoc_name(String doc_name) {
		this.doc_name = doc_name;
	}

	public String getDoc_type() {
		return doc_type;
	}

	public void setDoc_type(String doc_type) {
		this.doc_type = doc_type;
	}

	public int getPage_cnt() {
		return page_cnt;
	}

	public void setPage_cnt(int page_cnt) {
		this.page_cnt = page_cnt;
	}

	public String getRoot_id() {
		return root_id;
	}

	public void setRoot_id(String root_id) {
		this.root_id = root_id;
	}

	public String getIs_current() {
		return is_current;
	}

	public void setIs_current(String is_current) {
		this.is_current = is_current;
	}

	public String getIs_locked() {
		lock_status = is_locked;		
		return is_locked;
	}

	public void setIs_locked(String is_locked) {
		lock_status = is_locked;
		this.is_locked = is_locked;
	}

	public String getLock_owner() {
		return lock_owner;
	}

	public void setLock_owner(String lock_owner) {
		this.lock_owner = lock_owner;
	}

	public String getLock_date() {
		return (!StringUtil.isEmpty(lock_date) && lock_date.length() > 10 ) ? lock_date.substring(0,10) : lock_date;
	}

	public void setLock_date(String lock_date) {
		this.lock_date = lock_date;
	}

	public String getVersion_no() {
		return version_no;
	}

	public void setVersion_no(String version_no) {
		this.version_no = version_no;
	}

	public String getVersion_note() {
		return version_note;
	}

	public void setVersion_note(String version_note) {
		this.version_note = version_note;
	}

	public int getPreservation_year() {
		return preservation_year;
	}

	public void setPreservation_year(int preservation_year) {
		this.preservation_year = preservation_year;
	}

	public String getExpired_date() {
		return (!StringUtil.isEmpty(expired_date) && expired_date.length() > 10 ) ? expired_date.substring(0,10) : expired_date;
	}

	public void setExpired_date(String expired_date) {
		this.expired_date = expired_date;
	}

	public String getIs_expired() {
		return is_expired;
	}

	public void setIs_expired(String is_expired) {
		this.is_expired = is_expired;
	}

	public String getDoc_status() {
		return doc_status;
	}

	public void setDoc_status(String doc_status) {
		this.doc_status = doc_status;
	}

	public String getAcl_id() {
		return acl_id;
	}

	public void setAcl_id(String acl_id) {
		this.acl_id = acl_id;
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

	public String getUpdater_id() {
		return updater_id;
	}

	public void setUpdater_id(String updater_id) {
		this.updater_id = updater_id;
	}

	public String getUpdater_name() {
		return updater_name;
	}

	public void setUpdater_name(String updater_name) {
		this.updater_name = updater_name;
	}

	public String getUpdate_date() {
		return (!StringUtil.isEmpty(update_date) && update_date.length() > 10 ) ? update_date.substring(0,10) : update_date;
	}

	public void setUpdate_date(String update_date) {
		this.update_date = update_date;
	}

	public String getDeleter_id() {
		return deleter_id;
	}

	public void setDeleter_id(String deleter_id) {
		this.deleter_id = deleter_id;
	}

	public String getDeleter_name() {
		return deleter_name;
	}

	public void setDeleter_name(String deleter_name) {
		this.deleter_name = deleter_name;
	}

	public String getDelete_date() {
		return (!StringUtil.isEmpty(delete_date) && delete_date.length() > 10 ) ? delete_date.substring(0,10) : delete_date;
	}

	public void setDelete_date(String delete_date) {
		this.delete_date = delete_date;
	}

	public String getRef_id() {
		return ref_id;
	}

	public void setRef_id(String ref_id) {
		this.ref_id = ref_id;
	}

	public String getDoc_no() {
		return doc_no;
	}

	public void setDoc_no(String doc_no) {
		this.doc_no = doc_no;
	}

	public String getIs_trans() {
		return is_trans;
	}

	public void setIs_trans(String is_trans) {
		this.is_trans = is_trans;
	}

	public String getIs_inherit_acl() {
		return is_inherit_acl;
	}

	public void setIs_inherit_acl(String is_inherit_acl) {
		this.is_inherit_acl = is_inherit_acl;
	}

//	public String getEx_acl_id() {
//		return ex_acl_id;
//	}
//
//	public void setEx_acl_id(String ex_acl_id) {
//		this.ex_acl_id = ex_acl_id;
//	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getType_name() {
		return type_name;
	}

	public void setType_name(String type_name) {
		this.type_name = type_name;
	}

	public String getOwner_id() {
		return owner_id;
	}

	public void setOwner_id(String owner_id) {
		this.owner_id = owner_id;
	}

	public String getDoc_description() {
		return doc_description;
	}

	public void setDoc_description(String doc_description) {
		this.doc_description = doc_description;
	}

	public String getOwner_name() {
		return owner_name;
	}

	public void setOwner_name(String owner_name) {
		this.owner_name = owner_name;
	}

	public List<PageVO> getPageList() {
		return pageList;
	}

	public void setPageList(List<PageVO> pageList) {
		this.pageList = pageList;
	}

	public String getAcl_level() {
		return acl_level;
	}

	public void setAcl_level(String acl_level) {
		this.acl_level = acl_level;
	}

	public String getRelation_doc() {
		return relation_doc;
	}

	public void setRelation_doc(String relation_doc) {
		this.relation_doc = relation_doc;
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

	public String getAcl_checkoutCancel() {
		return acl_checkoutCancel;
	}

	public void setAcl_checkoutCancel(String acl_checkoutCancel) {
		this.acl_checkoutCancel = acl_checkoutCancel;
	}

	public String getUpdate_action() {
		return update_action;
	}

	public void setUpdate_action(String update_action) {
		this.update_action = update_action;
	}

	public String getLock_date_withTime() {
		return !StringUtil.isEmpty(lock_date_withTime) ? lock_date_withTime : lock_date;
	}

	public void setLock_date_withTime(String lock_date_withTime) {
		this.lock_date_withTime = lock_date_withTime;
	}

	public String getExpired_date_withTime() {
		return !StringUtil.isEmpty(expired_date_withTime) ? expired_date_withTime : expired_date;
	}

	public void setExpired_date_withTime(String expired_date_withTime) {
		this.expired_date_withTime = expired_date_withTime;
	}

	public String getCreate_date_withTime() {
		return !StringUtil.isEmpty(create_date_withTime) ? create_date_withTime : create_date;
	}

	public void setCreate_date_withTime(String create_date_withTime) {
		this.create_date_withTime = create_date_withTime;
	}

	public String getUpdate_date_withTime() {
		return !StringUtil.isEmpty(update_date_withTime) ? update_date_withTime : update_date;
	}

	public void setUpdate_date_withTime(String update_date_withTime) {
		this.update_date_withTime = update_date_withTime;
	}

	public String getDelete_date_withTime() {
		return !StringUtil.isEmpty(delete_date_withTime) ? delete_date_withTime : delete_date;
	}

	public void setDelete_date_withTime(String delete_date_withTime) {
		this.delete_date_withTime = delete_date_withTime;
	}

	public String getWaste_date_withTime() {
		return !StringUtil.isEmpty(waste_date_withTime) ? waste_date_withTime : waste_date;
	}

	public void setWaste_date_withTime(String waste_date_withTime) {
		this.waste_date_withTime = waste_date_withTime;
	}
	
	public String getLock_status() {
		return lock_status;
	}

	public void setLock_status(String lock_status) {
		this.lock_status = lock_status;
	}
	
	public String getWork_date() {
		return (!StringUtil.isEmpty(work_date) && work_date.length() > 10 ) ? work_date.substring(0,10) : work_date;
	}

	public void setWork_date(String work_date) {
		this.work_date = work_date;
	}

	public String getPage_extension() {
		return page_extension;
	}

	public void setPage_extension(String page_extension) {
		this.page_extension = page_extension;
	}

	public String getLock_owner_name() {
		return lock_owner_name;
	}

	public void setLock_owner_name(String lock_owner_name) {
		this.lock_owner_name = lock_owner_name;
	}

	public String getShow_date() {
		return show_date;
	}

	public void setShow_date(String show_date) {
		this.show_date = show_date;
	}
	
}

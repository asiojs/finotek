package kr.co.exsoft.eframework.configuration;

import java.util.HashMap;

/**
 * 전역 상수값 정의
 * @author 패키지 개발팀
 * @since 2014.07.15
 * @version 3.0
 *
 */
public class Constant {
	
	//Ehcache
	public static final String EHCACHE_CACHE_NAME_FOLDERLIST				= "FolderListCache"; //ehcace-default.xml 설정값
	public static final String EHCACHE_CACHE_KEY_FOLDERIDS					= "FOLDERIDS"; //factory에 저장 되어 있는 캐쉬 key
	public static final String EHCACHE_CACHE_KEY_FOLDERNAMES				= "FOLDERNAMES"; //factory에 저장 되어 있는 캐쉬 key
	
	//document
	public static final String DOCUMENT_DOC_ID = "doc_id";
	
	// ---------------------------------------------------------------------------
	// ACL
	// ---------------------------------------------------------------------------
	public static final String ACL_ACL_TYPE_ALL					= "ALL";
	public static final String ACL_ACL_TYPE_TEAM				= "TEAM";
	public static final String ACL_ACL_TYPE_DEPT				= "DEPT";
	public static final String ACL_ACL_TYPE_PRIVATE				= "PRIVATE";
	public static final String ACL_ACL_TYPE_ALL_NAME			= "전사";
	public static final String ACL_ACL_TYPE_TEAM_NAME			= "부서";
	public static final String ACL_ACL_TYPE_DEPT_NAME			= "하위부서포함";
	public static final String ACL_ACL_TYPE_PRIVATE_NAME		= "공유안함";
	public static final String ACL_IS_TYPE_FOLDER				= "F";
	public static final String ACL_IS_TYPE_DOCUMENT				= "D";
	public static final String ACL_DELETE						= "DELETE";
	public static final String ACL_UPDATE						= "UPDATE";
	public static final String ACL_READ							= "READ";
	public static final String ACL_BROWSE						= "BROWSE";
	public static final String ACL_NONE							= "NONE";
	
	// Folder type 정의
	public static final String FOLDER_TYPE_DOCUMENT = "DOCUMENT";
	
	// Locale 정의
	public static final String KOR = "KO";
	public static final String ENG = "EN";
	public static final String JPN = "JA";
	public static final String CHN = "ZH";
	
	// 라이센스 관련 정의
	public static final String LICENSE_TYPE_CONCURRENT = "CONCURRENT";
	public static final String LICENSE_TYPE_NAMED = "NAMED";
	
	// 접속로그
	public static final String NORMAL_LOGIN_TYPE = "NORAML";
	public static final String SSO_LOGIN_TYPE = "SSO";
	public static final String CONNECT_TYPE_LOGIN = "LOGIN";
	public static final String CONNECT_TYPE_LOGOUT = "LOGOUT";
	
	// 코드값 정의
	public static final String YES = "Y";
	public static final String NO = "N";
	public static final String NOTHING = "F";
	public static final String T = "T";
	public static final String F = "F";
	public static final String C = "C";
	public static final String D = "D";

	// 세션 구분 코드값
	public static final String SESSION_ADMIN = "admin";
	public static final String SESSION_USER = "user";
	public static final String LOCATION_ADMIN = "A";
	public static final String LOCATION_USER = "U";
	
	// ResultMap 정의
	public static final String RESULT_TRUE = "true";
	public static final String RESULT_FALSE = "false";
	public static final String RESULT_SUCCESS = "success";
	public static final String RESULT_FAIL = "fail";
	
	// 시퀀스 테이블 카운터값 정의
	public static final String COUNTER_ID_FILE 				= "ID_FILE";						// 문서, 폴더, 그룹.
	public static final String COUNTER_ID_PAGE 				= "ID_PAGE";						// 파일
	public static final String COUNTER_ID_CONNECT_LOG 		= "XR_CONNECT_LOG";					// 접속로그
	public static final String COUNTER_ID_HISTORY 			= "XR_HISTORY";						// 폴더/권한/문서 이력
	public static final String COUNTER_ID_GROUP_HT 			= "XR_GROUP_HT"; 					// 그룹 히스토리
	public static final String COUNTER_ID_DOCUMENT_HT 		= "XR_DOCUMENT_HT"; 				// 문서이력
	public static final String COUNTER_ID_PAGE_HT 			= "XR_PAGE_HT"; 					// 문서이력
	public static final String COUNTER_ID_USER_HT 			= "XR_USER_HT"; 					// 사용자 히스토리
	public static final String COUNTER_ID_BATCH_WORK 		= "XR_BATCHWORK"; 					// 사용자 히스토리
	public static final String COUNTER_ID_ACL 				= "XR_ACL";							// 권한
	public static final String COUNTER_ID_NOTE 				= "XR_NOTE"; 						// 쪽지 정보
	public static final String COUNTER_ID_NOTEMANAGE 		= "XR_NOTEMANAGE";					// 쪽지 수발신 정보
	public static final String COUNTER_ID_PROCESS 			= "XR_PROCESS"; 					// 협업 정보
	public static final String COUNTER_ID_PROCESS_EXECUTOR 	= "XR_PROCESS_EXECUTOR";			// 협업실행자 정보
	public static final String COUNTER_ID_COMMENT 			= "XR_COMMENT";						// 댓글
	public static final String COUNTER_ID_RECENTLY 			= "XR_RECENTLY_OBJECT";				// 최근 등록한 문서, 폴더, 협업
	
	// prefix 및 시퀀스 테이블 관련 prefix
	public static final String ID_PREFIX_FOLDER 			= "FOL";
	public static final String ID_PREFIX_DOCUMENT 			= "DOC";
	public static final String ID_PREFIX_REF 				= "REF";
	public static final String ID_PREFIX_PAGE 				= "PAG";
	public static final String ID_PREFIX_GROUP 				= "GRP";
	public static final String ID_PREFIX_ACL				= "ACL";
	public static final String ID_PREFIX_NOTE				= "NNI";			// XR_NOTE 
	public static final String ID_PREFIX_NOTEMANAGE			= "NMI";			// XR_NOTEMANAGE
	public static final String ID_PREFIX_PROCESS			= "PMI";			// XR_PROCESS
	public static final String ID_PREFIX_PROCESS_EXECUTOR	= "PEI";			// XR_PROCESS_EXECUTE
	public static final String ID_PREFIX_COMMENT			= "COM";			// XR_COMMONT
	public static final String ID_PREFIX_RECENTLY			= "REC";			// XR_RECENTLY_OBJECT
	
	// 사용자 업무처리 구분
	public static final String TYPE = "type";
	public static final String STYPE = "stype";
	public static final String DELETE = "delete";
	public static final String EXPIRED = "expired";
	public static final String INSERT = "insert";
	public static final String UPDATE = "update";
	public static final String REPLY = "reply";
	public static final String UPDATE_TYPE = "updateType";
	public static final String COPY = "copy";
	public static final String SELECT = "select";
	public static final String MOVE = "move";
	public static final String UPDATE_STATUS = "update_status";
	public static final String RESET_PASS = "reset_pass";
	public static final String PTRASH = "privateTrash";
	public static final String STRASH = "systemTrash";
	public static final String OWNER = "OWNER";
	public static final String WORLD = "WORLD";
	
	// 코드값 정의
	public static final String CODE_ROLE = "ROLE";
	public static final String CODE_POSITION = "POSITION";
	public static final String CODE_DUTY = "DUTY";
	public static final String CODE_VERSION = "VERSION";
	public static final String CODE_SECURITY_LEVEL = "SECURITY_LEVEL";
	public static final String CODE_ACTION_ID = "ACTION_ID";
	public static final String CODE_PRESERVATION_YEAR = "YEAR";
	
	// 메뉴접근권한
	public static final String MENU_ALL = "ALL";
	public static final String MENU_GROUP = "GROUP";
	public static final String MENU_TEAM= "TEAM";
	public static final String MENU_DISABLE= "DISABLE";
			
	public static final String RESULT = "result";
	public static final String SUCCESS = "success";

	// 로컬저장금지 관련
	public static final String LSC_CONTROL = "LSC_CONTROL";
	public static final String LSC_ENABLE_USB = "LSC_ENABLE_USB";
	public static final String LSC_EPROC = "LSC_EPROC";
	public static final String LSC_NETDRIVE_ADDR = "LSC_NETDRIVE_ADDR";
	public static final String RGC_UNINSTALL_PASS = "RGC_UNINSTALL_PASS";
	public static final String LSC_EXTENSION = "LSC_EXTENSION";
	public static final String LSC_WDIRS_PROC = "LSC_WDIRS_PROC";
	public static final String LSC_POLICY_UPDATE_CYCLE = "LSC_POLICY_UPDATE_CYCLE";
	
	// 로컬저장금지 테이블명
	public static final String LSC_RGATE_PROCESS = "XR_RGATE_PROCESS";
	public static final String LSC_RGATE_MAPPING = "XR_RGATE_MAPPING";
	
	public static final String MANAGE_EXT = "EXT";
	public static final String MANAGE_PROC = "PROC";
	public static final String MANAGE_IP = "IP";
	
	// 정책 활성/비활성
	public static final String IS_OFF = "OFF";
	public static final String IS_ON = "ON";
	
	// 구분(사용자/부서)
	public static final String IS_USER = "USER";
	public static final String IS_GROUP = "GROUP";
	public static final String IS_ALL = "ALL";
	
	// 시스템계정
	public static final String SYSTEM_ACCOUNT = "edmsadmin";
	public static final String SYSTEM_ROLE = "SYSTEM_OPERATOR";
	// 사용자 ROLE 정의
	public static final String USER_ROLE = "CREATOR";	
	
	// 문서유형 테이블 PREFIX
	public static final String TABLE_PREFIX = "XR_";
	public static final String DOC_TABLE = "XR_DOCUMENT";
	public static final String DOC_DEL_TABLE = "XR_DOCUMENT_DEL";
	public static final String DOC_FAVORITE_TABLE = "XR_FAVORITE_DOC";
	public static final String DOC_REF_TABLE = "XR_REF_DOC";
	public static final String DOC_TABLE_ALL_TYPE = "ALL_TYPE";
	
	// 이력관리
	public static final String ACTION_PLACE = "EDMS";
	
	// 이력관리 TARGET_TYPE
	public static final String TARGET_TYPE = "TYPE";
	public static final String TARGET_FOLDER = "FOLDER";
	public static final String TARGET_ACL = "ACL";
	
	// 환경설정 관리
	public static final String PRIVATE_TRASH = "PTRASH";
	public static final String SYSTEM_TRASH= "STRASH";
	
	// URL 유효기간
	public static final String SYSTEM_EXPIRED= "EXPIRED";
	
	// 볼륨타입
	public static final String VOLUME_FILE = "FILE";
	public static final String VOLUME_INDEX= "INDEX";
	
	// 문서상태
    public static final String DOC_STATUS_CREATE = "C";
    public static final String DOC_STATUS_DELETE = "D";
    public static final String DOC_STATUS_ERASE = "E";
    
    // 메뉴관리 :: 상위메뉴코드
    public static final String SYS_MENU = "M001";
    public static final String DOC_MENU = "M002";
    public static final String RGATE_MENU = "M003";
    public static final String STATICS_MENU = "M004";
    public static final String USERAUTH_MENU = "M005";
    public static final String DOC_AUTH_MENU = "M052";
    public static final String ACL_AUTH_MENU = "M051";
    
    // 관리자 초기메뉴
    public static final String MANAGER_INDEX_MENU = "M015";
    
    
    // 사용자 권한메뉴 
    public static final String USER_FOLDER_MENU_CODE = "M050";
    public static final String USER_ACL_MENU_CODE = "M051";
    public static final String USER_DOC_MENU_CODE = "M052";
    
    // 문서등록 :: 파일첨부관련
    public static final String FILE_EXT = "EXT";
    public static final String FILE_CNT = "FILECNT";
    public static final String FILE_SIZE = "FILESIZE";
    public static final String FILE_TOTAL= "FILETOTAL";
    
    // 배치프로그램 TYPE    
    public static final String WORK_BATCH = "BATCH";
    public static final String WORK_WEB = "WEB";
    
    public static final String BATCH_TEMP_LOG_FOLDER_DELETE = "LogTempFolderDelete";    
    public static final String BATCH_AUDIT = "Audit";
    public static final String BATCH_EXPIRED_DOC = "ExpiredDoc";
    public static final String BATCH_PTRASH_DOC = "PTrashDoc";
    public static final String BATCH_STRASH_DOC = "STrashDoc";
    public static final String BATCH_STATISITCS = "Statistics";
    public static final String BATCH_TERMINATED = "DeletePage";
    public static final String BATCH_TEMP_DOC = "TempDocDelete";
	
    public static final String BATCH_IP = "127.0.0.1";
    
    // 제품버전 정보
    public static final String PRODUCT_EDMS = "EDMS";
    public static final String PRODUCT_EDMS_RGATE = "EDMS_RGATE";
    public static final String PRODUCT_EDMS_FC = "EDMS_FC";
    public static final String PRODUCT_EDMS_APPLIANCE = "EDMS_APPLIANCE_RGATE";
    
    // 문서유형 확장속성 DISPLAY_TYPE
    public static final String DISPLAY_TYPE_CHECK = "CHECK";
    public static final String DISPLAY_TYPE_INPUT = "INPUT";
    public static final String DISPLAY_TYPE_SELECT = "SELECT";
    public static final String DISPLAY_TYPE_RADIO = "RADIO";
    
    // EXREP
    public static final String EXREP_VOLUME_NM = "EXREP_VOLUME";
    public static final String EXREP_ROOT_EDMS_NM = "EXREP_ROOT_EDMS";
    
    // EXCEL DOWN OPTION
    public static final String EXCEL_FORMAT = "excel";
    public static final String EXCEL_LIST = "list";
    public static final String CHART = "chart";
    
	public static final String FAIL = "fail";
	public static final String FAIL_UNKNOWN = "fail_unknown";
	public static final String NEXT = "next";
	
	public static final String INVALID_SESSION = "invalid_session";
	public static final String EXCEPTION = "exception";
	public static final String FAIL_MESSAGE = "fail_message";
	public static final String EXCEPTION_MESSAGE = "exception_message";
	
	public static final String SESSION_CHECK = "session_check";
	public static final String SESSION_USER_ID = "session_user_id";
	public static final String USER_DTO = "user_dto";

	
	public static final String TRUE = "TRUE";
	public static final String FALSE = "FALSE";
	
	public static final String DATE_START 	= "date_start";
	public static final String DATE_END 	= "date_end";
	public static final String KEYWORD1 	= "keyword1";
	public static final String KEYWORD2 	= "keyword2";
	public static final String KEYWORD3 	= "keyword3";
	public static final String SEARCH_TYPE 	= "search_type";
	public static final String SEARCH_RANGE	= "search_range";
	
	// 문서목록 미리보기 타입
	public static final String PREVIEW_LIST = "LIST";
	public static final String PREVIEW_RIGHT = "RIGHT";
	public static final String PREVIEW_BOTTOM = "BOTTOM";
	
	public static final String STREAM = "stream";

	public static enum Status {
        INSERT,
        UPDATE,
        DELETE
	}
	
	// ---------------------------------------------------------------------------
	// Tree.
	// ---------------------------------------------------------------------------	
	public static final String TREE_MAP_ID = "map_id";
	public static final String TREE_PARENT_ID = "parent_id";
	public static final String TREE_ROOT_ID = "root_id";
	public static final String TREE_WORK_TYPE = "work_Type";
	public static final String TREE_WORKTYPE_MYDEPT = "WORK_MYDEPT";		// 업무 문서함 부서 work type
	public static final String TREE_WORKTYPE_ALLDEPT = "WORK_ALLDEPT";		// 업무 문서함 전사 work type
	public static final String TREE_WORKTYPE_PROJECT = "WORK_PROJECT";		// 업무 문서함 프로젝트 work type

	// ---------------------------------------------------------------------------
	// Performance.
	// ---------------------------------------------------------------------------	

	public static final String ACTION_LOGIN = "LOGIN";
	public static final String ACTION_VIEW = "VIEW";
	public static final String ACTION_BROWSE = "BROWSE";
	public static final String ACTION_READ = "READ";
	public static final String ACTION_CREATE = "CREATE";
	public static final String ACTION_CREATE_FOLDER = "CREATE_FOLDER";
	public static final String ACTION_UPDATE = "UPDATE";
	public static final String ACTION_DELETE = "DELETE";
	public static final String ACTION_CHECKIN = "CHECKIN";
	public static final String ACTION_CHECKOUT = "CHECKOUT";
	public static final String ACTION_MODIFY = "MODIFY";
	public static final String ACTION_CANCEL_CHECKOUT = "CANCEL_CHECKOUT";
	public static final String ACTION_CHANGE_PERMISSION = "CHANGE_PERMISSION";
	public static final String ACTION_PAGE = "PAGE";
	public static final String ACTION_CHANGE_ACL_ID = "CHANGE_ACL_ID";
	
	public static final String ACTION_ADD_TO_FAVORITES = "ADD_TO_FAVORITES";
	public static final String ACTION_DELETE_FAVORITES = "DELETE_FAVORITES";
	public static final String ACTION_COPY = "COPY";
	public static final String ACTION_MOVE = "MOVE";
	public static final String ACTION_CHANGE_PASSWORD = "CHANGE_PASSWORD";
	public static final String ACTION_CHANGE_CREATOR = "CHANGE_CREATOR";
	public static final String ACTION_CHANGE_OWNER = "CHANGE_OWNER";
	public static final String ACTION_CHANGE_OWNER_WORK = "CHANGE_OWNER_WORK";
	public static final String ACTION_RESTORE = "RESTORE";
	public static final String ACTION_ERASE = "ERASE";
	public static final String ACTION_CHECK_EXISTS = "CHECK_EXISTS";
	
	public static final String ACTION_TIME_EXPIRED = "TIME_EXPIRED";
	public static final String ACTION_TIME_EXTEND = "TIME_EXTEND";
	public static final String ACTION_TERMINATE = "TERMINATE";
	public static final String ACTION_VERSION_TERMINATE = "VERSION_DELETE";
	
	public static final String ACTION_ADD_TO_TEMPWORK = "ADD_TO_TEMPWORK";
	public static final String ACTION_SWAP_INDEX = "SWAP_INDEX";
	
	public static final String ACC_MODE_FILE = "420";
	public static final String ACC_MODE_FOLDER = "493";

	public static final String MAP_ID = "map_id";
	public static final String MAP_ID_MYPAGE = "MYPAGE";
	public static final String MAP_ID_DEPT = "MYDEPT";
	public static final String MAP_ID_PROJECT = "PROJECT";
	public static final String MAP_ID_WORKSPACE = "WORKSPACE";
	
	// ---------------------------------------------------------------------------
	// Document.
	// ---------------------------------------------------------------------------	
	
	public static final String VERSION_NEW_DOCUMENT = "NEW";
	public static final String VERSION_SAME_VERSION = "SAME";
	public static final String VERSION_MAJOR_VERSION = "MAJOR";
	public static final String VERSION_MINOR_VERSION = "MINOR";
	public static final String DOCUMENT_DEFALUT_ACCESSGRADE = "P001";			// 문서 기본 접근등급(사원)
	public static final String DOCUMENT_DEFALUT_SECURITY_LEVEL = "COMMON";		// 문서 기본 보안등급(일반)
	public static final String DOCUMENT_STATUS_PROCESS_ING	= "P";				// 프로세스 진행 단계 완료 후 C로 전환
	
	public static final String DEFAULT_VERSION_NO = "1.0";
	public static final String MAJOR_VERSION_UP = "1";
	public static final String MINOR_VERSION_UP = "1";
	public static final String DEFAULT_BRANCH_VERSION_NO = "1.0";
	
	// 문서 리스트 관련 정의
	public static final String DOCUMENT_LIST_TYPE				= "LIST_TYPE";							// 리스트 타입 KEY 명칭
	public static final String DOCUMENT_LIST_TYPE_GENERAL		= "GENERAL";  				// 보편적인 문서 리스트
	public static final String DOCUMENT_LIST_TYPE_EXPIRED		= "EXPIRED";  					// 만기 문서 관리 문서 리스트
	public static final String DOCUMENT_LIST_TYPE_TRASHCAN		= "TRASHCAN"; 			// 관리자 / 사용자 휴지통 관리 문서 리스트
	public static final String DOCUMENT_LIST_TYPE_OWNER			= "OWNER";				// 소유권 변경 문서 리스트
	public static final String DOCUMENT_LIST_TYPE_DUPLICATE		= "DUPLICATE";			// 중복 관리 문서 리스트
	public static final String DOCUMENT_LIST_TYPE_AUDIT			= "AUDIT";						// 대량 문서 리스트
	public static final String DOCUMENT_LIST_TYPE_CHECKOUT 		= "CHECKOUT"; 			// 내 수정 중 문서 리스트
	public static final String DOCUMENT_LIST_TYPE_FAVORITE	 	= "FAVORITE"; 				// 즐겨찾기(가상폴더) 문서 리스트
	public static final String DOCUMENT_LIST_TYPE_SHARE			= "SHARE";						// 공유 문서
	public static final String DOCUMENT_LIST_TYPE_SHARE_FOLDER	= "SHARE_FOLDER";		// 공유 폴더 문서
	public static final String DOCUMENT_LIST_TYPE_TEMPDOC 	= "TEMPDOC";				// 작업카트 문서 리스트
	public static final String DOCUMENT_LIST_TYPE_RECENTLYDOC 	= "RECENTLYDOC";		// 최신문서 리스트
	
	// 메인페이지 문서목록 타입정의
	public static final String MAIN_NEWDOC = "NEWDOC";												// 새로운문서
	public static final String MAIN_MOSTVIEWDOC = "MOSTVIEWDOC";							// 최다조회 문서	
	public static final String MAIN_MOSTMYDOC = "MOSTMYDOC";									// 최다조회 내문서
	public static final String MAIN_RECENTLYDOC = "RECENTLYDOC";								// 최근조회 문서
	public static final String MAIN_CHECKOUTDOC = "CHECKOUTDOC";							// 내수정중인 문서
	
	
	// 소유권 변경 관련
	public static final String CHANGE_SELECT_DOC		= "SELECT_DOC";	// 선택한 문서를 소유권 이전함
	public static final String CHANGE_SEARCH_DOC		= "SEARCH_DOC";	// 검색 결과 문서를 소유권 이전함
	public static final String CHANGE_ALL_DOC			= "ALL_DOC";	// 소유자의 전체문서를 소유권 이전함
	
	public static final String CHANGE_SCOPE_MYWORK		= "MYWORK";		// 개인 문서함
	public static final String CHANGE_SCOPE_WORKSPACE	= "WORKSPACE";	// 업무 문서함
	
	// ---------------------------------------------------------------------------
	// Config. (구 kr.co.exsoft.xframework.configuration.Config)
	// ---------------------------------------------------------------------------
	public static final String ACL_DEPT_DEFAULT = "부서 기본 권한";
	public static final String ACL_ID_OWNER = "acl_public_owner";	
	public static final String ACL_ID_WORLD = "acl_public_world";
	public static final String GROUP_TOP_ID = ID_PREFIX_GROUP + "000000000000"; // 최상위 그룹ID
	public static final String FOLDER_TOP_ID = ID_PREFIX_FOLDER + "000000000000"; // 최상위 그룹ID
	
	// ---------------------------------------------------------------------------
	// Folder
	// ---------------------------------------------------------------------------
	public static final String FOLDER_TABLE = "XR_FOLDER";
	public static final String FOLDER_TYPE_DEPT = "MYDEPT";
	public static final String FOLDER_TYPE_ALL_TYPE = "ALL_TYPE";
	
	public static final String FOLDER_STATUS_CREATE = "C";
	public static final String FOLDER_STATUS_UPDATE = "U";
	public static final String FOLDER_STATUS_DELETE = "D";
	public static final String FOLDER_SAVE_YES = "Y";
	public static final String FOLDER_SAVE_NO = "N";
	
	// ---------------------------------------------------------------------------
	// History
	// ---------------------------------------------------------------------------
	public static final String HISTORY_STATUS_CREATE = "C";	// 생성
	public static final String HISTORY_STATUS_UPDATE = "U";	// 수정
	public static final String HISTORY_STATUS_CLOSE = "D";	// 폐쇄
	public static final String HISTORY_STATUS_DELETE = "E";	// 삭제
	
	// ---------------------------------------------------------------------------
	// trash
	// ---------------------------------------------------------------------------
	public static final String TRASH_DELETE = "DELETE";							//휴지통 문서 삭제(선택삭제)
	public static final String TRASH_ALL_DELETE = "ALL_DELETE";				//휴지통 비우기 (전체삭제)
	
	// EXCEL DOWNLOAD - 통계
	public static final String SUM_CREATE_CNT = "create_cnt";					// 등록건수
	public static final String SUM_READ_CNT = "read_cnt";						// 조회건수
	public static final String SUM_UPDATE_CNT = "update_cnt";				// 수정건수
	public static final String SUM_DELETE_CNT = "delete_cnt";					// 삭제건수
	public static final String SUM_DOC_CNT = "doc_cnt";							// 문서수
	public static final String SUM_FILE_CNT = "page_cnt";							// 파일수
	public static final String SUM_FSIZE = "fsize";										// 용량::치환
	public static final String SUM_PAGE_TOTAL = "page_total";					// 용량
	
	// CHART GRAPH TYPE - 통계
	public static final String LINE_CHART = "line";									// 라인차트
	public static final String BAR_CHART = "bar";										// 막대차트
	public static final String PIE_CHART = "pie";										// 파이차트
	
	// CHART GRAPH 구분 - 통계
	public static final String CHART_DOC_TYPE = "typeChart";							// 문서유형별 보유현황 차트
	public static final String CHART_GROUP_STATUS = "groupChart";					// 부서별 등록/활용 현황
	public static final String CHART_DECADE_STATUS = "decadeChart";				// 기간별 등록/활용 현황
	public static final String CHART_FOLDER_STATUS = "folderChart";					// 문서함/폴더별 등록/활용 현황
	public static final String CHART_SECURITY_STATUS = "securityChart";			// 보안등급별 보유현황
	
	public static final String USER_DOC_STATISTICS = "user_nm";						// 사용자명 
	public static final String GROUP_DOC_STATISTICS = "group_nm";					// 그룹명
	public static final String FOLDER_DOC_STATISTICS = "folder_nm";				// 폴더명
	public static final String TYPE_DOC_STATISTICS = "type_name";					// 타입명
	public static final String CODE_DOC_STATISTICS = "code_nm";						// 보안등급
	
	public static final String DAY_TERM = "daily";												// 일별
	public static final String MONTH_TERM = "monthly";									// 월별
	
	public static final String WORK_TYPE_USER = "USER";									// 사용자/문서함별
	public static final String WORK_TYPE_FOLDER = "FOLDER";							// 문서함/폴더별
	
	// 시스템 환경 설정 구분 값 
	public static final String SYS_TYPE_VERSION = "VERSION";							// 버전관리 정책
	public static final String SYS_TYPE_URL = "URL";											// URL 복사 기간 정책
	public static final String SYS_TYPE_AUDIT = "AUDIT";									// 감사정책
	public static final String SYS_TYPE_TRASH = "TRASH";									// 휴지통관리 정책
	public static final String SYS_TYPE_FILE = "FILE";											// 문서첨부 정책

	// 사용자메뉴
	public static final String TOPMENU_MYDOC = "myDocMenu";						// 내문서
	public static final String TOPMENU_MYWORK = "myWorkMenu";					// 개인문서함
	public static final String TOPMENU_WORKSPACE = "workSpaceMenu";			// 업무문서함
	public static final String TOPMENU_WORKPROCESS = "workProcessMenu";	// 협업함
	public static final String TOPMENU_STATISTICS = "statisticsMenu";				// 통계
	
	// 관리자메뉴
	public static final String TOPMENU_SYSTEM = "systemMenu";						// 시스템관리
	public static final String TOPMENU_DOCUMENT = "documentMenu";			// 문서관리
	public static final String TOPMENU_RGATE = "rGateMenu";							// RGATE관리
	
	// 관리자 시스템 메뉴 
	public static final String SYSTEM_CONFMANAGER = "confManager";							// 시스템관리-환경설정관리
	public static final String SYSTEM_MENUMANAGER = "menuAuthManager";					// 시스템관리-메뉴접속관리
	public static final String SYSTEM_USERMANAGER = "userManager";								// 시스템관리-사용자관리
	public static final String SYSTEM_GROUPMANAGER = "groupManager";						// 시스템관리-그룹관리
	
	public static final String DOCUMENT_ACLMANAGER = "aclManager";							// 문서관리-권한관리
	public static final String DOCUMENT_TYPEMANAGER = "typeManager";						// 문서관리-문서유형관리
	public static final String DOCUMENT_FOLDERMANAGER = "folderManager";					// 문서관리-폴더관리
	public static final String DOCUMENT_EXPIREDMANAGER = "expiredManager";				// 문서관리-만기문서관리
	public static final String DOCUMENT_OWNERMANAGER = "ownerManager";					// 문서관리-소유권변경관리
	public static final String DOCUMENT_WASTEMANAGER = "wasteManager";					// 문서관리-휴지통관리
	public static final String DOCUMENT_DUPLICATEMANAGER = "duplicateManager";		// 문서관리-중복문서관리	
	public static final String DOCUMENT_AUDITMANAGER = "auditManager";						// 문서관리-대량문서열람감사관리
	
	
	public static final String RGATE_EXTMANAGER = "extManager";							// 저장금지 확장자 관리
	public static final String RGATE_PROCMANAGER = "procManager";						// 저장 허용 프로그램 관리
	public static final String RGATE_CONTROLMANAGER = "controlManager";				// 로컬 저장 허용관리
	public static final String RGATE_USBMANAGER = "usbManager";							// USB 저장 허용관리
	public static final String RGATE_NETWORKMANAGER = "networkManager";			// USB 저장 허용관리
	public static final String RGATE_EXCEPTIONMANAGER = "exceptionManager";		// 프로세스 예외폴더 설정
	public static final String RGATE_UNINSTALLMANAGER = "uninstallManager";			// CLIENT 제거 비밀번호 설정
	
	// 협업메뉴
	public static final String PROCESS_REQUEST_MENU 			= "REQUEST";				// 요청 문서
	public static final String PROCESS_WRITE_ING_MENU 			= "WRITE_ING";				// 작성중 문서
	public static final String PROCESS_APPROVAL_ING_MENU 		= "APPROVAL_ING";			// 승인중 문서
	public static final String PROCESS_WRITE_END_MENU 			= "WRITE_END";				// 작성한 문서
	public static final String PROCESS_APPROVAL_END_MENU 		= "APPROVAL_END";			// 승인한 문서
	public static final String PROCESS_RECEIVE_MENU 			= "RECEIVE";				// 수신문서
	public static final String PROCESS_STATUS_REQUEST 			= "Q";		// 업무요청
	public static final String PROCESS_STATUS_WRITE 			= "W";		// 작성중
	public static final String PROCESS_STATUS_APPROVAL 			= "A";		// 승인중
	public static final String PROCESS_STATUS_APPROVAL_END 		= "AE";		// 승인완료
	public static final String PROCESS_STATUS_MODIFY 			= "M";		// 보완중
	public static final String PROCESS_STATUS_END 				= "E";		// 완료
	public static final String PROCESS_TYPE_REQUESTOR			= "Q";		// 업무 요청자
	public static final String PROCESS_TYPE_AUTHOR				= "R";		// Responsible(대표 작성자)
	public static final String PROCESS_TYPE_COAUTHOR			= "C";		// 공동 작성자
	public static final String PROCESS_TYPE_APPROVER			= "A";		// Accountable(의사결정권자::승인자)
	public static final String PROCESS_TYPE_RECEIVER			= "I";		// Informed(사후에 결과를 통보 반는자::수신자)
	public static final String PROCESS_EXECUTOR_WAIT			= "N";		// 실행자 해당 단계 대기
	public static final String PROCESS_EXECUTOR_START			= "S";		// 실행자 해당 단계 시작
	public static final String PROCESS_EXECUTOR_END				= "E";		// 실행자 해당 단계 종료
	public static final String PROCESS_ACTION_APPROVEREQUEST	= "APPROVEREQUEST";		// 승인요청	
	public static final String PROCESS_ACTION_APPROVE			= "APPROVE";			// 승인	
	public static final String PROCESS_ACTION_APPROVEREJECT		= "APPROVEREJECT";		// 반려	
	
	// 쪽지관리 TAB
	public static final String NOTE_TAB_TALK = "TALK";									// 대화함
	public static final String NOTE_TAB_RECEIVE = "RECEIVE";							// 받은쪽지함
	public static final String NOTE_TAB_SEND = "SEND";									// 보낸쪽지함
	public static final String NOTE_TAB_BOX = "BOX";										// 쪽지보관함
	
	// 환경설정 TAB
	public static final String CONFIG_TAB_MYINFO = "myinfo";							// 환경설정-내정보
	public static final String CONFIG_TAB_PASSWD= "passwdConf";					// 환경설정-비밀번호 관리
	public static final String CONFIG_TAB_CONFIG = "myconfig";						// 환경설정-기본환경
	public static final String CONFIG_USERNM = "usernm";								// 환경설정-내정보 기본정보 수정
	
	// 환경설정 AUDIT
	public static final String READ_COUNT_THRESHOLD = "read_count_threshold";		
	public static final String SEND_REPORT_MAIL = "send_report_mail";						
	public static final String REPORT_MAIL_RECEIVER_ADDRESS = "report_mail_receiver_address";						
	
	
	// 환경설정 새창크기 
	public static final String CONFIG_WIDTH = "740";										// 환경설정-내정보 창넓이
	public static final HashMap<String, Object> CONFIG_HEIGHT = new HashMap<String, Object>() {
		
		private static final long serialVersionUID = 1L;

		{
        	put(Constant.CONFIG_TAB_MYINFO,572);
        	put(Constant.CONFIG_TAB_PASSWD,592);
        	put(Constant.CONFIG_TAB_CONFIG,552);
         }
    };

    // 환경설정 TABLE
    public static final String XR_USER = "XR_USER";									
    public static final String XR_USER_DT = "XR_USER_DT";						
    public static final String XR_USER_CONFIG = "XR_USER_CONFIG";				
    
	// 에러메세지 코드
    public static final String ERROR_403 = "403";
    public static final String ERROR_404 = "404";
    public static final String ERROR_503 = "503";
    public static final String ERROR_505 = "505";
    
	// ---------------------------------------------------------------------------
	// 기타 시스템 상수
	// ---------------------------------------------------------------------------
	public static final String INCLUDE_SUB_FOLDER = "includeSubFolder";
	
	public static final String NOTE_RECENT_CNT="5";
	
	// 최근 문서,폴더,업무(협업) 등록 현황
	public static final String RECENTLY_TYPE_DOCUMENT = "D";
	public static final String RECENTLY_TYPE_FOLDER = "F";
	public static final String RECENTLY_TYPE_PROCESS = "P";
}

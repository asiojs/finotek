package kr.co.exsoft.quartz.vo;

/**
 * 관리자 휴지통 정책 VO - 삭제예정
 * @author 패키지 개발팀
 * @since 2014.08.01
 * @version 3.0
 *
 */
public class TrashConfigVO {

	// 테이블 객체(XR_TRASH_CONFIG)
	private int decade;												// 휴지통비우기 기본기준일
	private String is_active;										// 배치활성화 여부 - T:배치수행 F:배치수행안함 - 관리자화면에서 수행처리와는 상관없음									
	private String empty_option;									// 휴지통비우기 옵션 - TERMINATE : 완전삭제 BACKUP : 스토리지 백업
	private String backup_path;									// 백업경로 - 스토리지 백업 경로 : 유효 디렉토리 체크
	
	// 조회항목
	private String empty_option_nm;							// 휴지통비우기 옵션명 - XR_CODE REF GCODE='EMPTY_OPTION'
	
	public TrashConfigVO() {
		
		this.decade = 0;
		this.is_active = "";
		this.empty_option = "";
		this.backup_path = "";
		this.empty_option_nm = "";
	}

	public String getEmpty_option_nm() {
		return empty_option_nm;
	}

	public void setEmpty_option_nm(String empty_option_nm) {
		this.empty_option_nm = empty_option_nm;
	}

	public int getDecade() {
		return decade;
	}

	public void setDecade(int decade) {
		this.decade = decade;
	}

	public String getIs_active() {
		return is_active;
	}

	public void setIs_active(String is_active) {
		this.is_active = is_active;
	}

	public String getEmpty_option() {
		return empty_option;
	}

	public void setEmpty_option(String empty_option) {
		this.empty_option = empty_option;
	}

	public String getBackup_path() {
		return backup_path;
	}

	public void setBackup_path(String backup_path) {
		this.backup_path = backup_path;
	}
	
	
}

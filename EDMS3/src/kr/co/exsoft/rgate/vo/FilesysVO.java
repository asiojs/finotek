package kr.co.exsoft.rgate.vo;

/**
 * Filesys VO
 * @author 패키지 개발팀
 * @since 2014.07.29
 * @version 3.0
 *
 */
public class FilesysVO {

	// 테이블 객체(SD_FILESYS)
	private String file_id;												// 파일 ID
	private String file_name;											// 파일명
	private long file_size;												// 파일 크기	
	private long create_date;											// 생성일
	private long modify_date;											// 수정일
	private long access_date;											// 접근일	
	private long change_date;										// 변경일
	private int is_readonly;												// 읽기전용여부	0:FALSE 1:TRUE
	private int is_archived;												// 보존속성여부 0:FALSE 1:TRUE
	private int is_directory;												// 디렉토리여부 0:FALSE 1:TRUE		 
	private int is_systemfile;											// 시스템파일여부 0:FALSE 1:TRUE
	private int is_hidden;												// 숨김여부	 0:FALSE 1:TRUE
	private int is_symlink;												// 링크파일여부 0:FALSE 1:TRUE
	private int owner_uid;												// 소유자 UID - NFS에서 사용됨
	private int owner_gid;												// 소유자 GID - NFS에서 사용됨
	private int acc_mode;												// 파일접근모드 - 디렉토리:493,파일:420	
	private int is_deleted;												// 삭제여부 0:FALSE 1:TRUE
	private String share_name;										// 공유명 - MYDEPT:부서함,MYPAGE:개인문서함,PROJECT:프로젝트문서함,SHARED:공유문서함,MYCOMPANY:전사함
	
	
	public FilesysVO() {
		
		this.file_id = "";
		this.file_name = "";
		this.file_size = 0;
		this.is_readonly = 0;
		this.is_archived = 0;
		this.is_directory = 0;
		this.is_systemfile = 0;
		this.is_hidden = 0;
		this.is_symlink = 0;
		this.acc_mode = 420;
		this.is_deleted = 0;
	}


	public String getFile_id() {
		return file_id;
	}


	public void setFile_id(String file_id) {
		this.file_id = file_id;
	}


	public String getFile_name() {
		return file_name;
	}


	public void setFile_name(String file_name) {
		this.file_name = file_name;
	}


	public long getFile_size() {
		return file_size;
	}


	public void setFile_size(long file_size) {
		this.file_size = file_size;
	}


	public long getCreate_date() {
		return create_date;
	}


	public void setCreate_date(long create_date) {
		this.create_date = create_date;
	}


	public long getModify_date() {
		return modify_date;
	}


	public void setModify_date(long modify_date) {
		this.modify_date = modify_date;
	}


	public long getAccess_date() {
		return access_date;
	}


	public void setAccess_date(long access_date) {
		this.access_date = access_date;
	}


	public long getChange_date() {
		return change_date;
	}


	public void setChange_date(long change_date) {
		this.change_date = change_date;
	}


	public int getIs_readonly() {
		return is_readonly;
	}


	public void setIs_readonly(int is_readonly) {
		this.is_readonly = is_readonly;
	}


	public int getIs_archived() {
		return is_archived;
	}


	public void setIs_archived(int is_archived) {
		this.is_archived = is_archived;
	}


	public int getIs_directory() {
		return is_directory;
	}


	public void setIs_directory(int is_directory) {
		this.is_directory = is_directory;
	}


	public int getIs_systemfile() {
		return is_systemfile;
	}


	public void setIs_systemfile(int is_systemfile) {
		this.is_systemfile = is_systemfile;
	}


	public int getIs_hidden() {
		return is_hidden;
	}


	public void setIs_hidden(int is_hidden) {
		this.is_hidden = is_hidden;
	}


	public int getIs_symlink() {
		return is_symlink;
	}


	public void setIs_symlink(int is_symlink) {
		this.is_symlink = is_symlink;
	}


	public int getOwner_uid() {
		return owner_uid;
	}


	public void setOwner_uid(int owner_uid) {
		this.owner_uid = owner_uid;
	}


	public int getOwner_gid() {
		return owner_gid;
	}


	public void setOwner_gid(int owner_gid) {
		this.owner_gid = owner_gid;
	}


	public int getAcc_mode() {
		return acc_mode;
	}


	public void setAcc_mode(int acc_mode) {
		this.acc_mode = acc_mode;
	}


	public int getIs_deleted() {
		return is_deleted;
	}


	public void setIs_deleted(int is_deleted) {
		this.is_deleted = is_deleted;
	}


	public String getShare_name() {
		return share_name;
	}


	public void setShare_name(String share_name) {
		this.share_name = share_name;
	}
	
	
}

package kr.co.exsoft.rgate.vo;

/**
 * RGATE 프로세스 VO
 * @author 패키지 개발팀
 * @since 2014.07.29
 * @version 3.0
 *
 */
public class RGateProcessVO {

	// 테이블 객체(XR_RGATE_PROCESS)
	private String work_type;								// 작업유형
	private String process;									// 프로세스명
	private String folder_path;							// 폴더경로
	
	public RGateProcessVO() {
	
		this.work_type = "";
		this.process = "";
		this.folder_path = "";
	}

	public String getWork_type() {
		return work_type;
	}

	public void setWork_type(String work_type) {
		this.work_type = work_type;
	}

	public String getProcess() {
		return process;
	}

	public void setProcess(String process) {
		this.process = process;
	}

	public String getFolder_path() {
		return folder_path;
	}

	public void setFolder_path(String folder_path) {
		this.folder_path = folder_path;
	}
	
	
}

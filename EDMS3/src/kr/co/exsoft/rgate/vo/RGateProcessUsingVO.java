package kr.co.exsoft.rgate.vo;

import java.util.List;

/**
 * RGATE 예외프로세스 VO
 * @author 패키지 개발팀
 * @since 2014.07.29
 * @version 3.0
 *
 */
public class RGateProcessUsingVO {

	private String process;													// 프로세스명
	private List<String> work_type_list;								// 예외폴더리스트
	
	public String getProcess() {
		return process;
	}
	public void setProcess(String process) {
		this.process = process;
	}
	public List<String> getWork_type_list() {
		return work_type_list;
	}
	public void setWork_type_list(List<String> work_type_list) {
		this.work_type_list = work_type_list;
	}
	
	
}
 
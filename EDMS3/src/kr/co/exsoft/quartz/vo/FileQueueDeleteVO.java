package kr.co.exsoft.quartz.vo;

/**
 * FileQueueDelete VO 
 * @author 패키지 개발팀
 * @since 2015.04.22
 * @version 3.0
 *
 */
public class FileQueueDeleteVO {

	// 테이블 객체
	private String volume_id;	
	private String content_path;

	public FileQueueDeleteVO() {
		
		this.volume_id = "";
		this.content_path = "";
	}

	public String getVolume_id() {
		return volume_id;
	}

	public void setVolume_id(String volume_id) {
		this.volume_id = volume_id;
	}

	public String getContent_path() {
		return content_path;
	}

	public void setContent_path(String content_path) {
		this.content_path = content_path;
	}
	
	
}

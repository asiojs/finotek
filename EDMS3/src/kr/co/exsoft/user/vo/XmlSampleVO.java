package kr.co.exsoft.user.vo;

import javax.xml.bind.annotation.XmlAccessType;  
import javax.xml.bind.annotation.XmlAccessorType;  
import javax.xml.bind.annotation.XmlElement;  
import javax.xml.bind.annotation.XmlRootElement;  
import javax.xml.bind.annotation.XmlType;  


@XmlType  
@XmlRootElement(name="samples")  
@XmlAccessorType(XmlAccessType.FIELD)  
public class XmlSampleVO {

	//생성자는 필수로 선언  
	public XmlSampleVO() {
		
	}  
	 
	@XmlElement  
	private String userId    = "";  
	      
	@XmlElement  
	private String userNm    = "";  
	      
	@XmlElement  
	private String userPw    = "";

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserNm() {
		return userNm;
	}

	public void setUserNm(String userNm) {
		this.userNm = userNm;
	}

	public String getUserPw() {
		return userPw;
	}

	public void setUserPw(String userPw) {
		this.userPw = userPw;
	}
	
	

}

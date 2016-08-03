package kr.co.exsoft.eframework.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Pattern 처리 Class
 *
 * @author 패키지팀
 * @since 2014. 12. 9.
 * @version 1.0
 * 
 */

public class PatternUtil {

	/**
	 * 
	 * <pre>
	 * 1. 개용 : 폴더/문서명 체크 : 특수문자 포함된 경우 true
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : webfolderCheck
	 * @param inputStr
	 * @return boolean
	 */
	public static boolean webfolderCheck(String inputStr) {
		
		boolean ret = false;
		
		Pattern p = Pattern.compile("[:\\\\/%*?:|\"<>]");
		Matcher m = p.matcher(inputStr);
		
		 if (m.find()){
			 ret = true;
		 }
		
		return ret;
	}
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 이메일 주소 체크 : 정상적인 이메일주소인 경우 true
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : emailCheck
	 * @param inputStr
	 * @return boolean
	 */
	public static boolean emailCheck(String inputStr) {
		
		boolean ret = false;
		
		Pattern p = Pattern.compile("^(?:\\w+\\.?)*\\w+@(?:\\w+\\.)+\\w+$");
		Matcher m = p.matcher(inputStr);
		
		 if (m.find()){
			 ret = true;
		 }
		
		return ret;
	}
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 사용자 아이디체크 - 정상적인 경우 true 
	 * 2. 처리내용 : 영문자로 시작하며 특수문자는 ._%- 만 허용한다.
	 * </pre>
	 * @Method Name : userIdCheck
	 * @param inputStr
	 * @return boolean
	 */
	public static boolean userIdCheck(String inputStr) {
		
		boolean ret = false;
		
		Pattern p = Pattern.compile("^[a-z]{1}[a-z0-9._%-]*$");
		Matcher m = p.matcher(inputStr);
		
		 if (m.find()){
			 ret = true;
		 }
		
		return ret;
		
	}

}


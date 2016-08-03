package kr.co.exsoft.eframework.library;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 실제적인 validation check 로직을 수행한다.
 *
 * @author 패키지 개발팀
 * @since 2014.07.15
 * @version 3.0
 */
@SuppressWarnings("serial")
public class RteGenericValidator implements Serializable {


	/**
	 * 주민등록번호 유효성 체크
	 * 
	 * @param value
	 * @return boolean
	 */
	public static boolean isValidIdIhNum(String value) {


		//값의 길이가 13자리이며, 7번째 자리가 1,2,3,4 중에 하나인지 check.
		String regex = "\\d{6}[1234]\\d{6}";
		if (!value.matches(regex)) {
			return false;
		}

		//앞 6자리의 값이 유효한 날짜인지 check.
		try {

			String strDate = value.substring(0, 6);
			strDate = ((value.charAt(6) == '1' || value.charAt(6) == '2') ? "19" : "20") + strDate;
			strDate = strDate.substring(0, 4) + "/" + strDate.substring(4, 6) + "/" + strDate.substring(6, 8);

			SimpleDateFormat dateformat = new SimpleDateFormat("yyyy/MM/dd");
			Date date = dateformat.parse(strDate);
			String resultStr = dateformat.format(date);

			if (!resultStr.equals(strDate)) {
				return false;
			}

		} catch (ParseException e) {
			e.printStackTrace();
			return false;
		}


		//주민등록번호 마지막 자리를 이용한 check.
		char[] charArray = value.toCharArray();
		long sum = 0;
		int[] arrDivide = new int[] { 2, 3, 4, 5, 6, 7, 8, 9, 2, 3, 4, 5 };
		for (int i = 0; i < charArray.length - 1; i++) {
			sum += Integer.parseInt(String.valueOf(charArray[i])) * arrDivide[i];
		}

		int checkdigit = (int) ((int) (11 - sum % 11)) % 10;

		return (checkdigit == Integer.parseInt(String.valueOf(charArray[12]))) ? true : false;
	}


	/**
	 * 한글여부 체크
	 * 
	 * @param value
	 * @return boolean
	 */
	public static boolean isKorean(String value) {


		char[] charArray = value.toCharArray();
		for (int i = 0; i < charArray.length; i++) {
			if (Character.getType(charArray[i]) != 5) {
				return false;
			}
		}
		return true;
	}


	/**
	 * 영어여부 체크
	 * 
	 * @param value
	 * @return boolean
	 */
	public static boolean isEnglish(String value) {


		char[] charArray = value.toCharArray();
		for (int i = 0; i < charArray.length; i++) {
			if (Character.getType(charArray[i]) != 1 && Character.getType(charArray[i]) != 2) {
				return false;
			}
		}
		return true;
	}


	/**
	 * Html tag 포함 여부
	 * 
	 * @param value
	 * @return boolean
	 */
	public static boolean isHtmlTag(String value){
		//모든 태그 "<[^<|>]*>"				  
		//스크립트 "<script[^>]*>(.*?)</SCRIPT>"		  
		//스타일 "<STYLE[^>]*>(.*?)</STYLE>"

		Pattern re = Pattern.compile("<[^<|>]*>");
		Matcher m = re.matcher(value);


		if (m.find()) {
			return false;
		}
		return true;
	}


	/**
     * 패스워드 점검 : 8~20자 이내
     * 
     * @param password
     * @return boolean
     */
	public static boolean checkLength(String password) {

		if (password.length() < 8 || password.length() > 20) {
			return false;
		}

		return true;
	}
    
    /**
     * 패스워드 점검 : 한글,특수문자,띄어쓰기는 안됨
     * 
     * @param password
     * @return boolean
     */
	public static boolean checkCharacterType(String password) {
		char[] charArray = password.toCharArray();


		for (int i = 0; i < charArray.length; i++) {
			char ch = charArray[i];
			/*
			// javascript 부분에는 33~47로 되어 있으나 공백을 처리하기 위해 32~47로 변경함..
			if ((ch >= 32 && ch <= 47) || (ch >= 58 && ch <= 64) || (ch >= 91 && ch <= 96) || (ch >= 123 && ch <= 126)) {
			return false;
			}
			*/
			if (ch < 33 || ch > 126) { // 대부분의 문자를 사용하도록 변경 (javascript도 변경)
				return false;
			}
		}


		return true;
	}
    
    /**
     *  패스워드 점검 : 연속된 문자나 순차적인 문자 4개이상 사용금지
     *  
     * @param password
     * @return boolean
     */
	public static boolean checkSeries(String password) {
		int countSequence = 0, countSame = 0;


		for (int i = 0; i < password.length() - 1; i++) {
			char pass = password.charAt(i);
			char next = (char) (pass + 1);
			if (password.charAt(i + 1) == next) {
				countSequence++;
			} else {
				countSequence = 0;
			}


			if (pass == password.charAt(i + 1)) {
				countSame++;
			} else {
				countSame = 0;
			}


			if (countSequence > 2 || countSame > 2) {
				return false;
			}
		}


		return true;
	}
}


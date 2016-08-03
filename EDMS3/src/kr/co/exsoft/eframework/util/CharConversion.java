package kr.co.exsoft.eframework.util;

import java.io.CharConversionException;
import java.io.UnsupportedEncodingException;

/**
 * 캐릭터셋 변환 클래스
 * @author 패키지 개발팀
 * @since 2014.07.15
 * @version 3.0
 *
 */
public final class CharConversion {

	/**
	 * Don't let anyone instantiate this class
	 */
	private CharConversion() {
	}

	/**
	 * 8859_1 --> KSC5601.
	 */
	public static String E2K( String english ){
		String korean = null;
		if (english == null ) return null;
		//if (english == null ) return "";
		try { 
			korean = new String(english.getBytes("8859_1"), "KSC5601");
		}catch( UnsupportedEncodingException e ){
			korean = english;
		}
		return korean;
	}

	/**
	 * KSC5601 --> 8859_1.
	 */

	public static String K2E( String korean ){
		String english = null;

		if (korean == null ) return null;
		try { 
			english = new String(korean.getBytes("KSC5601"), "8859_1");
		}catch( UnsupportedEncodingException e ){
			english =  korean;
		}
		return english;
	}

	public static String K2U( String korean ){
		String english = null;

		if (korean == null ) return null;
		try { 
			english = new String(korean.getBytes("KSC5601"), "UTF-8");
		}catch( UnsupportedEncodingException e ){
			english =  korean;
		}
		return english;
	}
	

	public static String U2K( String korean ){
		String english = null;

		if (korean == null ) return null;
		try { 
			english = new String(korean.getBytes("UTF-8"), "KSC5601");
		}catch( UnsupportedEncodingException e ){
			english =  korean;
		}
		return english;
	}
	
	public static String E2U( String korean ){
		String english = null;

		if (korean == null ) return null;
		try { 
			english = new String(korean.getBytes("ISO-8859-1"), "UTF-8");
		}catch( UnsupportedEncodingException e ){
			english =  korean;
		}
		return english;
	}
	

	public static String U2E( String korean ) throws CharConversionException{
		String english = null;

		if (korean == null ) return null;
		try { 
			english = new String(korean.getBytes("UTF-8"), "ISO-8859-1");
		}catch( UnsupportedEncodingException e ){
			english =  korean;
		}catch( Exception e ){
			english =  korean;
		}
		return english;
	}
	public static String EUC2U( String korean ){
		String english = null;

		if (korean == null ) return null;
		try { 
			english = new String(korean.getBytes("EUC-KR"), "UTF-8");
		}catch( UnsupportedEncodingException e ){
			english =  korean;
		}
		return english;
	}
	
	public static String U2EUC( String korean ){
		String english = null;

		if (korean == null ) return null;
		try { 
			english = new String(korean.getBytes("UTF-8"), "ISO-8859-1");
		}catch( UnsupportedEncodingException e ){
			english =  korean;
		}
		return english;
	}
	
	public static String U28859( String korean ){
		String english = null;

		if (korean == null ) return null;
		try { 
			english =new String(korean.getBytes("UTF-8"), "8859_1");
		}catch( UnsupportedEncodingException e ){
			english =  korean;
		}
		return english;
	}
}

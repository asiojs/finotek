package kr.co.exsoft.eframework.util;

import java.io.IOException;
import java.util.Properties;
import java.util.Locale;
import org.apache.ibatis.io.Resources;

/***
 * 서버 설정값 처리 클래스
 * @author 패키지 개발팀
 * @since 2014.07.15
 * @version 3.0
 *
 */
public class ConfigData {

	private static Properties prop = null;
	private static final String resource ="../config/config.properties";
	
	static {	
	
		try {
			prop = Resources.getResourceAsProperties(resource);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * String 파라미터
	 * @param key
	 * @return String
	 */
	public static String getString(String key) {
		
		return prop.get(key).toString().trim();
	}
	
	/**
	 * Int 파라미터
	 * @param key
	 * @return int
	 */
	public static int getInt(String key) {
		
		try{
			return Integer.parseInt(getString(key));
		}catch (Exception e){
			throw new IllegalArgumentException("Illegal integer Key : " + key);
		}
		
	}
	
	/**
	 * Boolean 파라미터
	 * @param key
	 * @return boolean
	 */
	public static boolean getBoolean(String key)
	{
		try{
			return (new Boolean(getString(key))).booleanValue();			
		}catch (Exception e){
			throw new IllegalArgumentException("Illegal boolean Key : " + key);
		}
	}
	
	/**
	 * long 파라미터
	 * @param key
	 * @return long
	 */
	public static long getLong(String key)	{
		try{
			return Long.parseLong(getString(key));
		}catch(Exception e){
			throw new IllegalArgumentException("Illegal long Key : " + key);
		}
	}
	
	/**
	 * Locale  파라미터
	 * @param key
	 * @return Locale
	 */
	public  static Locale getLocale(String key)	{
	
		try{
			return  new Locale(getString(key));
		}catch(Exception e){
			throw new IllegalArgumentException("Illegal Locale Key : " + key);
		}

	}
		
	/**
	 * String 문자열 값이 없을 경우   defaultVal 로 대체한다.
	 * @param key
	 * @param defaultVal
	 * @return String
	 */
	public static String getString(String key, String defaultVal)	{
		try{
			String result = getString(key);
			if (result != null)
				return result;
		}
		catch (Exception ex){	}
		
		return defaultVal;
	}
	
	/**
	 * 정수형으로 키값을 가져온다. Key가 없거나, 정수형이 아닌경우 기본값을 가져온다.
	 * @param key
	 * @param defaultVal
	 * @return int
	 */
	public static int getInt(String key, int defaultVal){
		
		try{
			return Integer.parseInt(getString(key));			
		}catch (Exception ex){
			return defaultVal;
		}
	}
	
	/**
	 * long형으로 키값을 가져온다. Key가 없거나,long형이 아닌경우 기본값을 가져온다.
	 * @param key
	 * @param defaultVal
	 * @return long
	 */
	public static long getLong(String key, long defaultVal){
		try{
			return Long.parseLong(getString(key));
		}catch (Exception ex){
			return defaultVal;
		}
	}
	
	/**
	 * boolean형으로 키값을 가져온다. Key가 없거나,boolean형이 아닌경우 기본값을 가져온다.
	 * @param key
	 * @param defaultVal
	 * @return boolean
	 */
	public static boolean getBoolean(String key, boolean defaultVal)
	{
		try
		{
			String x = getString(key);

			if (x == null || x.length() == 0)
				return defaultVal;

			return (new Boolean(x)).booleanValue();
		}catch (Exception ex){
			return defaultVal;
		}
	}
	
	/**
	 *  Locale 형으로  키값을 가져온다. Key가 없거나,Locale 형이 아닌경우 기본값을 가져온다.
	 * @param key
	 * @param defaultVal
	 * @return Locale
	 */
	public  static Locale getLocale(String key,Locale defaultVal)	{
		
		try{
			return  new Locale(getString(key));
		}catch(Exception e){
			return defaultVal;
		}

	}
}

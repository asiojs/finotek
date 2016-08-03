package kr.co.exsoft.eframework.util;

import java.lang.reflect.Field;
import java.util.HashMap;

public class Converter {
	
	/**
	 * <pre>
	 * 1. 개요 : 오브젝트 멤버를 HashMap<String, Object>로 변환한다
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : objectToHashMap
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	public static HashMap<String, Object> objectToHashMap(Object obj) throws Exception {
		HashMap<String, Object> result = new HashMap<String, Object>();
		
		for (Field field : obj.getClass().getDeclaredFields()) {
		    field.setAccessible(true);
		    Object value = field.get(obj); 
		    if (value != null) {
		    	result.put(field.getName(), value);
		    }
		}
		return result;
	}
	
	/**
	 * <pre>
	 * 1. 개요 : 오브젝트의 특정 멤버들만 HashMap<String, Object>로 변환한다
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : objectToHashMap
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	public static HashMap<String, Object> objectToHashMap(Object obj, String... params) throws Exception {
		HashMap<String, Object> result = new HashMap<String, Object>();
		
		for (String param : params) {
			Field field = obj.getClass().getDeclaredField(param);
			field.setAccessible(true);
		    Object value = field.get(obj); 
		    if (value != null) {
		    	result.put(field.getName(), value);
		    }
		}
		return result;
	}
	
	/**
	 * <pre>
	 * 1. 개요 : 오브젝트의 특정 멤버들만 키이름을 바꿔서 HashMap<String, Object>로 변환한다
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : objectToHashMap
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	public static HashMap<String, Object> objectToHashMapAsCustomKey(Object obj, String findKey, String resultKey) throws Exception {
		HashMap<String, Object> result = new HashMap<String, Object>();
		
		Field field = obj.getClass().getDeclaredField(findKey);
		field.setAccessible(true);
	    Object value = field.get(obj); 
	    if (value != null) {
	    	result.put(resultKey, value);
	    }
	    
		return result;
	}
}

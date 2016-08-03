package kr.co.exsoft.eframework.util;

import com.nhncorp.lucy.security.xss.XssFilter;

/**
 * Lucy-Xss 필터 적용
 *
 * @author 패키지팀
 * @since 2014. 12. 11.
 * @version 1.0
 * 
 */

public class LucyFilter {

	public XssFilter filter ;
	
	public LucyFilter() {		
		//filter = XssFilter.getInstance("../config/lucy-xss-superset.xml");				
		filter = XssFilter.getInstance();
	}
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : Lucy xss 변경처리
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : doFilter
	 * @param data
	 * @return String
	 */
	public String doFilter(String data) {
		
		String cleanData = filter.doFilter(data);		
		return cleanData;		
	}
}

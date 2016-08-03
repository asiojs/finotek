package kr.co.exsoft.eframework.util;

import javax.servlet.http.*;

/**
 * 쿠키처리 
 * @author 패키지 개발팀
 * @since 2015.03.32
 * @version 3.0
 *
 */
public class UserCookie {
	
	/**
	 *  
	 * <pre>
	 * 1. 개용 : 쿠기설정
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : setUserCookie
	 * @param res
	 * @param strCookieName
	 * @param strValue
	 * @param nMaxAge
	 * @param strComment void
	 */
	public static void setUserCookie(HttpServletResponse res,String strCookieName,
			 String strValue,int nMaxAge,String strComment) {
		 
		Cookie cookie = new Cookie(strCookieName, strValue);
		cookie.setVersion(0);
		cookie.setSecure(false);
		cookie.setPath("/");
		cookie.setMaxAge(nMaxAge);
		cookie.setComment(strComment);
		res.addCookie(cookie);
	 }

	/**
	 * 
	 * <pre>
	 * 1. 개용 : 쿠키설정
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : setUserCookie
	 * @param res
	 * @param strDomain
	 * @param strCookieName
	 * @param strValue
	 * @param nMaxAge
	 * @param strComment void
	 */
	 public static void setUserCookie(HttpServletResponse res,String strDomain,String strCookieName,String strValue,
			    int nMaxAge,String strComment) {
		 
		Cookie cookie = new Cookie(strCookieName, strValue);
		cookie.setVersion(0);
		cookie.setSecure(false);
		cookie.setDomain(strDomain);
		cookie.setPath("/");
		cookie.setMaxAge(nMaxAge);
		cookie.setComment(strComment);
		res.addCookie(cookie);
	 }

	 /**
	  * 
	  * <pre>
	  * 1. 개용 : 쿠키정보 가져오기
	  * 2. 처리내용 : 
	  * </pre>
	  * @Method Name : getUserCookie
	  * @param req
	  * @param strCookieName
	  * @return String
	  */
	 public static String getUserCookie(HttpServletRequest req, String strCookieName) {
		 
		 Cookie cookies[] = req.getCookies();
		 Cookie cookie = null;
		 
		 if (cookies != null) {
			 for (int i = 0; i < cookies.length; ++i) {
				 if (cookies[i].getName().equals(strCookieName)) {
			          cookie = cookies[i];
			          break;
			     }
			 }
		 }
		
		 String strValue = "";
		 try {
		     strValue = cookie.getValue();
		  } catch (Exception e) {}
		 	return strValue;
	 }

}

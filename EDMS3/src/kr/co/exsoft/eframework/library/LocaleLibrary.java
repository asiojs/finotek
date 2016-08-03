package kr.co.exsoft.eframework.library;

import java.util.Locale;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import kr.co.exsoft.eframework.configuration.Constant;

/**
 * Locale 관련 클래스
 * @author 패키지 개발팀
 * @since 2014.07.15
 * @version 3.0
 *
 */
public class LocaleLibrary {

	/**
	 * 로케일 변경
	 * @param request
	 * @param lang
	 */
	public void modifyLocale(HttpServletRequest request, String lang) {
		
		HttpSession session = request.getSession();
		
		if(lang.length() > 2) {
			lang = lang.substring(0, 2);
		}
		
		if(lang.equals(Constant.JPN)) {
			session.setAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME, Locale.JAPAN);
		} else if(lang.equals(Constant.ENG)) {
			session.setAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME, Locale.ENGLISH);
		}else if(lang.equals(Constant.CHN)) {
			session.setAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME, Locale.CHINESE);
		} else {
			session.setAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME, Locale.KOREAN);
		}
	}
	
	/**
	 * 기본 로케일 설정
	 * @param lang
	 * @return Locale
	 */
	public static Locale setLocale(String lang) {
    	
    	if(lang.length() > 2) {
			lang = lang.substring(0, 2);
		}
		
		if(lang.equals(Constant.JPN)) {
			Locale.setDefault(Locale.JAPAN);
		} else if(lang.equals(Constant.ENG)) {
			Locale.setDefault(Locale.ENGLISH);
		}else if(lang.equals(Constant.CHN)) {
			Locale.setDefault(Locale.CHINESE);
		} else {
			Locale.setDefault(Locale.KOREAN);
		}
		
		return Locale.getDefault();
    }
	
	/***
	 * locale sesson 
	 * @param req
	 * @param lang
	 */
	public static void setLocaleInfo(HttpServletRequest req,String lang) {
		
		HttpSession session = req.getSession(true);
		
		if(lang.equals(Constant.JPN)) {
			session.setAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME, Locale.JAPAN);			
		} else if(lang.equals(Constant.ENG)) {
			session.setAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME, Locale.ENGLISH);
		} else if(lang.equals(Constant.CHN)) {
			session.setAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME, Locale.CHINESE);
		}else {
			session.setAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME, Locale.KOREAN);
		}
		
	}
}

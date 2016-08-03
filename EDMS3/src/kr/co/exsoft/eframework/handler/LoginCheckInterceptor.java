package kr.co.exsoft.eframework.handler;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import kr.co.exsoft.eframework.configuration.Constant;
import kr.co.exsoft.common.vo.SessionVO;
import kr.co.exsoft.eframework.library.LocaleLibrary;
import kr.co.exsoft.eframework.util.ConfigData;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;


/**
 * 로그인 체크 Interceptor 클래스
 * @author 패키지 개발팀
 * @since 2014.07.15
 * @version 3.0
 *
 */
@Service
public class LoginCheckInterceptor extends HandlerInterceptorAdapter {

	protected static final Log logger = LogFactory.getLog(LoginCheckInterceptor.class);
		
	@Autowired
	private MessageSource messageSource;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		
		logger.info("LoginCheckInterceptor preHandle");
		
		boolean ret = true;
				
		try {
			
			HttpSession session = request.getSession();
			
			// 1.세션체크 
			if(session.getAttribute("sessionVO") == null) {
				
				logger.info("sessionBean not exist");
				
				if( !isAjax(request))	{
					response.sendRedirect("/login.do");
					return false;
				}else {
					reponseAjaxMessage(response,"common.session.error");
					return false;
				}

			}			
			
			// 2.관리자/사용자 페이지 접속 구분에 따른 권한 체크(통계페이지 사용자로 변경처리 적용)
			if(request.getRequestURL().indexOf("/admin/") > -1 ) {
				
				// 2.1 ROLE_ID 체크
				SessionVO sessionVO = (SessionVO)session.getAttribute("sessionVO");

				// 2.2 관리자 ROLE 체크
				if(sessionVO.getSessRole_id() != null && sessionVO.getSessRole_id().equals(Constant.USER_ROLE)) {					
					if( !isAjax(request))	{
						response.sendRedirect("/common/errorMessage.do?code=common.connect.error");		
						return false;
					}else {
						reponseAjaxMessage(response,"common.connect.error");
						return false;
					}
				}				
			}
			
			// 3,페이지 Refresh 후에 페이지 이동처리 :: 추후
						
						
			
		}catch(Exception e) {
			logger.debug(e.getMessage());			
		}

		return ret;
	}
	
	/**
	 * 
	 * <pre>
	 * 1. 개요 : Ajax Session 
	 * 2. 처리내용 :
	 * </pre>
	 * @Method Name : isAjax
	 * @param request
	 * @return boolean
	 */
	public boolean isAjax(HttpServletRequest request) {
		return "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
	}

	/**
	 * 
	 * <pre>
	 * 1. 개요 : Ajax 에러메세지 출력
	 * 2. 처리내용 :
	 * </pre>
	 * @Method Name : reponseAjaxMessage
	 * @param response
	 * @param ExtSuccessMessage
	 * @throws IOException
	 */
	protected void reponseAjaxMessage(HttpServletResponse response,String ExtSuccessMessage) throws IOException{
		
		Locale locale = LocaleLibrary.setLocale(ConfigData.getString("LANGUAGE"));
		
		String errorMsg = "";
		
		try{
			
			response.setContentType("text/xml");
			response.setCharacterEncoding("utf-8");
			java.io.PrintWriter out = response.getWriter();
		
			errorMsg =  messageSource.getMessage("common.session.error",new Object[0],locale);				

			out.print("<RESPONSES>");
			out.print("<RESPONSE>");
			out.print("<RESULT>" + ExtSuccessMessage + "</RESULT>");
			out.print("<MESSAGE><![CDATA[" +	errorMsg + "]]></MESSAGE>");
			out.print("</RESPONSE>");
			out.print("</RESPONSES>");
			out.flush();
			out.close();
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
}

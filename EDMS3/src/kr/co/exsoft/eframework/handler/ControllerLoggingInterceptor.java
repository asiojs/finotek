package kr.co.exsoft.eframework.handler;

import javax.servlet.http.HttpServletRequest; 
import javax.servlet.http.HttpServletResponse; 
import org.springframework.web.servlet.HandlerInterceptor; 
import org.springframework.web.servlet.ModelAndView; 
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Controller Interceptor 클래스
 * @author 패키지 개발팀
 * @since 2014.07.15
 * @version 3.0
 *
 */
public class ControllerLoggingInterceptor implements HandlerInterceptor {

	protected static final Log logger = LogFactory.getLog(ControllerLoggingInterceptor.class);
	
	protected long start = 0;
	protected long end = 0;
	
	// Controller 실행 요청전 
	public boolean preHandle(HttpServletRequest request,HttpServletResponse response,
			Object handler) throws Exception {
		
		logger.info("pretHandle");
		start = System.currentTimeMillis();		
		return true;
	}
	
	// view(jsp)로 forward되기 전에 
	public void postHandle(HttpServletRequest request,HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		
		logger.info("postHandle");		
	}
	
	// 끝난뒤
	public void afterCompletion(HttpServletRequest request,HttpServletResponse response, 
			Object handler, Exception ex) throws Exception {
		
		logger.info("afterCompletion");
		
		end = System.currentTimeMillis()  - start;
		
		logger.info("proess time = " + end + " ms");
	}

	
}

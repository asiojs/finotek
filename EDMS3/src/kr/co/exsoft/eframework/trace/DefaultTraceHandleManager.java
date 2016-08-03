package kr.co.exsoft.eframework.trace;

import kr.co.exsoft.eframework.handler.ExsoftTraceHandler;

/**
 * AbsTraceHandleManager 클래스의 구현 클래스
 * @author 패키지 개발팀
 * @since 2014.07.15
 * @version 3.0
 *
 */
public class DefaultTraceHandleManager extends AbsTraceHandleManager implements TraceHandlerService {
	
	/**
	 * trace 메소드 
	 * 
	 * @param clazz 클래스정보
	 * @param message 보여주고자하는 메세지
	 * @return boolean true|false
	 */
	@Override
	public boolean trace(Class<?> clazz, String message) {
		log.debug(" DefaultExceptionHandleManager.run() ");

		// 매칭조건이 false 인 경우
		if (!enableMatcher())
			return false;

		for (String pattern : patterns) {
			
			log.debug("pattern = " + pattern + ", thisPackageName = " + getPackageName());
			log.debug("pm.match(pattern, getPackageName()) =" + pm.match(pattern, getPackageName()));
			
			if (pm.match(pattern, getPackageName())) {
				for (ExsoftTraceHandler eh : handlers) {
					eh.todo(clazz, message);
					log.debug("trace end?");
				}
				break;
			}
		}

		return true;

	}	

}

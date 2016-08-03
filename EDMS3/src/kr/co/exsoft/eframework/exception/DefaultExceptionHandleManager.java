package kr.co.exsoft.eframework.exception;

import kr.co.exsoft.eframework.handler.ExceptionHandler;

/***
 * ExceptionHandlerService의 기본 구현체 AbsExceptionHandleManager을 상속함 
 * 일치하는 패턴이 있는 경우 ExcpetionHandler.occur() 호출
 * @author 패키지 개발팀
 * @since 2014.07.15
 * @version 3.0
 *
 */
public class DefaultExceptionHandleManager extends AbsExceptionHandleManager implements ExceptionHandlerService {

	@Override
	public boolean run(Exception exception) throws Exception {

		logger.debug(" DefaultExceptionHandleManager.run() ");

		// 매칭조건이 false 인 경우
		if (!enableMatcher())
			return false;

		for (String pattern : patterns) {
			
			logger.debug("pattern = " + pattern + ", thisPackageName = " + thisPackageName);
			logger.debug("pm.match(pattern, thisPackageName) =" + pm.match(pattern, thisPackageName));
			
			if (pm.match(pattern, thisPackageName)) {
				for (ExceptionHandler eh : handlers) {
					eh.occur(exception, getPackageName());
				}
				break;
			}
		}

		return true;
	}

}

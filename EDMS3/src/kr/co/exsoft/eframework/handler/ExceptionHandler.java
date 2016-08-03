package kr.co.exsoft.eframework.handler;

/***
 * AbsExceptionHandleManager, 또는 그 하위 클래스에서 예외 처리 목적으로 호출 됨
 * @author 패키지 개발팀
 * @since 2014.07.15
 * @version 3.0
 *
 */
public interface ExceptionHandler {

	public void occur(Exception exception, String packageName);
	
}

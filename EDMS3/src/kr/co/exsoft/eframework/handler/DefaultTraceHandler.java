package kr.co.exsoft.eframework.handler;

/***
 * Handler 구현 클래스
 * @author 패키지 개발팀
 * @since 2014.07.15
 * @version 3.0
 *
 */
public class DefaultTraceHandler implements ExsoftTraceHandler{

	public void todo(Class<?> clazz, String message) {
		System.out.println("[DefaultTraceHandler] run...............");
	}

}

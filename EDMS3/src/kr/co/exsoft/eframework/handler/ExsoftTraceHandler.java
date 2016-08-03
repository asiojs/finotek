package kr.co.exsoft.eframework.handler;

/**
 * 실행되는 Handler 인터페이스이다.
 * @author 패키지 개발팀
 * @since 2014.07.15
 * @version 3.0
 *
 */
public interface ExsoftTraceHandler {

	/**
	 * todo 메소드
	 * @param clazz  발생시키는 클래스 정보
	 * @param message 메세지키를 통해 보여주고자 하는 메세지 
	 */
	public void todo(Class<?> clazz, String message);
	
}

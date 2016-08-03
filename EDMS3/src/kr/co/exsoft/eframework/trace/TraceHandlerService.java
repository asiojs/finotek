package kr.co.exsoft.eframework.trace;

import org.springframework.util.PathMatcher;
import kr.co.exsoft.eframework.handler.ExsoftTraceHandler;

/**
 * TraceHandlerService 인터페이스
 * @author 패키지 개발팀
 * @since 2014.07.15
 * @version 3.0
 *
 */
public interface TraceHandlerService {

	/**
	 * setPatterns 메소드
	 * 
	 * 패키지,클래스 이름으로 패턴등록(Ant형식의 매칭)
	 * @param patterns 패턴리스트
	 */
	public void setPatterns(String[] patterns);
	
	/**
	 * setHandlers 메소드
	 * ExceptionHandler 리스트 등록
	 * @param handlers handler리스트
	 */
	public void setHandlers(ExsoftTraceHandler[] handlers);
	
	/**
	 * setPackageName 메소드
	 * 비교할 클래스 정보 
	 * 
	 * @param canonicalName 비교할 클래스명
	 */
	public void setPackageName(String canonicalName);
	
	/**
	 * setReqExpMatcher 메소드
	 * 
	 * @param pm 사용자에 의해 사용하고자하는 PathMatcher 
	 */
	public void setReqExpMatcher(PathMatcher pm);
	
	/**
	 * hasReqExpMatcher 메소드
	 * PathMatcher 가 있는지 여부 반환
	 * @return boolean true|false
	 */
	public boolean hasReqExpMatcher();
	
	/**
	 * trace 메소드 
	 * @param clazz 클래스정보
	 * @param message 보여주고자하는 메세지
	 * @return boolean true|false
	 */
	public boolean trace(Class<?> clazz, String message);

}

package kr.co.exsoft.eframework.trace;

import javax.annotation.Resource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.MessageSource;
import org.springframework.util.PathMatcher;

import kr.co.exsoft.eframework.handler.ExsoftTraceHandler;

/**
 * DefaultTraceHandleManager 클래스의 부모 추상 클래스
 * @author 패키지 개발팀
 * @since 2014.07.15
 * @version 3.0
 *
 */
public abstract class AbsTraceHandleManager {

	protected Log log = LogFactory.getLog(this.getClass());

	@Resource(name = "messageSource")
	protected MessageSource messageSource;

	protected String packageName;
	protected String[] patterns;
	protected ExsoftTraceHandler[] handlers;
	protected PathMatcher pm;

	/**
	 * setPatterns 메소드
	 * 
	 * 패키지,클래스 이름으로 패턴등록(Ant형식의 매칭)
	 * @param patterns 패턴리스트
	 */
	public void setPatterns(String[] patterns) {
		this.patterns = patterns;
	}
	
	/**
	 * setHandlers 메소드
	 * ExceptionHandler 리스트 등록
	 * @param handlers handler리스트
	 */
	public void setHandlers(ExsoftTraceHandler[] handlers) {
		this.handlers = handlers;
	}
	
	/**
	 * setPackageName 메소드
	 * 비교할 클래스 정보 
	 * 
	 * @param canonicalName 비교할 클래스명
	 */
	public void setPackageName(String canonicalName) {
		this.packageName = canonicalName;
	}
	
	/**
	 * getPackageName 메소드
	 */
	public String getPackageName() {
		return this.packageName;
	}
	
	/**
	 * setReqExpMatcher 메소드
	 * 
	 * @param pm 사용자에 의해 사용하고자하는 PathMatcher 
	 */
	public void setReqExpMatcher(PathMatcher pm) {
		this.pm = pm;
	}
	
	/**
	 * hasReqExpMatcher 메소드
	 * PathMatcher 가 있는지 여부 반환
	 */
	public boolean hasReqExpMatcher() {
		return this.enableMatcher();
	}
	
	/**
	 * enableMatcher 메소드
	 * PathMatcher 가 있는지 여부 반환
	 */
	public boolean enableMatcher() {
		return (this.pm == null) ? false : true;
	}
	
	/**
	 * trace 메소드 
	 * 
	 * 상속받아 구현해야할 메스드 하지만 미리구현은 먼저 해둠. 실 구현체에서 override 하여 구현해야 함.
	 * @param clazz 클래스정보
	 * @param message 보여주고자하는 메세지
	 * @return boolean true|false
	 */
	public boolean trace(Class<?> clazz, String message) {
		log.debug(" DefaultExceptionHandleManager.run() ");

		// 매칭조건이 false 인 경우
		if (!enableMatcher())
			return false;

		for (String pattern : patterns) {
			
			log.debug("pattern = " + pattern + ", thisPackageName = " + packageName);
			log.debug("pm.match(pattern, thisPackageName) =" + pm.match(pattern, packageName));
			
			if (pm.match(pattern, packageName)) {
				for (ExsoftTraceHandler eh : handlers) {
					eh.todo(clazz, message);
				}
				break;
			}
		}

		return true;
	}

}

package kr.co.exsoft.eframework.exception;

/***
 * ExrepClient Exception.
 * @author 패키지 개발팀
 * @since 2014.07.15
 * @version 3.0
 *
 */
public class ExrepClientException extends Exception {

	private static final long serialVersionUID = 4128687515321283906L;

	public ExrepClientException() {
        super();
    }

    public ExrepClientException(String message) {
        super(message);
    }

    public ExrepClientException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExrepClientException(Throwable cause) {
        super(cause);
    }
    
}

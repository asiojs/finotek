package kr.co.exsoft.eframework.exception;

import java.text.MessageFormat;
import java.util.Locale; 
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.MessageSource;
 
/**
 * 예외 상속구조의 최상단
 * @author 패키지 개발팀
 * @since 2014.07.15
 * @version 3.0
 *
 */
public class BaseException extends Exception {
	
	protected static final Log logger = LogFactory.getLog(BaseException.class);
	private static final long serialVersionUID = 1L; 
    protected String message = null;
    protected String messageKey = null;
    protected Object[] messageParameters = null;
    protected Exception wrappedException = null;

    public String getMessage() {    	 
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageKey() {
        return this.messageKey;
    }

    public void setMessageKey(String messageKey) {
        this.messageKey = messageKey;
    }

    public Object[] getMessageParameters() {
        return this.messageParameters;
    }

    public void setMessageParameters(Object[] messageParameters) {
        this.messageParameters = messageParameters;
    }

    public Throwable getWrappedException() {
        return this.wrappedException;
    }

    public void setWrappedException(Exception wrappedException) {
        this.wrappedException = wrappedException;
    }

    public BaseException() {
        this("BaseException without message", null, null);
    }

    public BaseException(String defaultMessage) {
        this(defaultMessage, null, null);
    }

    public BaseException(Throwable wrappedException) {
        this("BaseException without message", null, wrappedException);
    }

    public BaseException(String defaultMessage, Throwable wrappedException) {
    	this(defaultMessage, null, wrappedException);
    }

    public BaseException(String defaultMessage, Object[] messageParameters,Throwable wrappedException) {
    	
        super(wrappedException);

        String userMessage = defaultMessage;

        if (messageParameters != null) {
                userMessage = MessageFormat.format(defaultMessage,messageParameters);
        }

        this.message = userMessage;

    }

    public BaseException(MessageSource messageSource, String messageKey) {
        this(messageSource, messageKey, null, null, Locale.getDefault(), null);
    }

    public BaseException(MessageSource messageSource, String messageKey,
    		Throwable wrappedException) {
        this(messageSource, messageKey, null, null, Locale.getDefault(),wrappedException);
    }

    public BaseException(MessageSource messageSource, String messageKey,
    		Locale locale, Throwable wrappedException) {
        this(messageSource, messageKey, null, null, locale, wrappedException);
    }

    public BaseException(MessageSource messageSource, String messageKey,
    		Object[] messageParameters, Locale locale,Throwable wrappedException) {
        this(messageSource, messageKey, messageParameters, null, locale,wrappedException);
    }

    public BaseException(MessageSource messageSource, String messageKey,
    		Object[] messageParameters, Throwable wrappedException) {
        this(messageSource, messageKey, messageParameters, null, Locale.getDefault(), wrappedException);
    }

    public BaseException(MessageSource messageSource, String messageKey,
    		Object[] messageParameters, String defaultMessage,Throwable wrappedException) {
        this(messageSource, messageKey, messageParameters, defaultMessage,Locale.getDefault(), wrappedException);
    }

    public BaseException(MessageSource messageSource, String messageKey,Object[] messageParameters, 
    		String defaultMessage, Locale locale,Throwable wrappedException) {

        super(wrappedException);

        this.messageKey = messageKey;
        this.messageParameters = messageParameters;
        this.message = messageSource.getMessage(messageKey, messageParameters,defaultMessage, locale);

    }
	
}

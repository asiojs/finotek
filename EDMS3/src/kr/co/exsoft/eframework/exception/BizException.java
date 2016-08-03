package kr.co.exsoft.eframework.exception;

import java.text.MessageFormat;
import java.util.Locale;
import org.springframework.context.MessageSource; 

/***
 * 서비스 레이어레서 발생시키는 비즈니스 예외 / BaseException 하위 클래스
 * @author 패키지 개발팀
 * @since 2014.07.15
 * @version 3.0
 *
 */
public class BizException extends BaseException {

    private static final long serialVersionUID = 1L;
        
    public BizException() {
    	this("BaseException without message", null, null);
    }

    public BizException(String defaultMessage) {
            this(defaultMessage, null, null);
    }

    public BizException(String defaultMessage, Exception wrappedException) {
            this(defaultMessage, null, wrappedException);
    }

    public BizException(String defaultMessage, Object[] messageParameters,Exception wrappedException) {

            String userMessage = defaultMessage;

            if (messageParameters != null) {
                    userMessage = MessageFormat.format(defaultMessage,messageParameters);
            }

            this.message = userMessage;
            this.wrappedException = wrappedException;
    }

    public BizException(MessageSource messageSource, String messageKey) {
    	this(messageSource, messageKey, null, null, Locale.getDefault(), null);
    }

    public BizException(MessageSource messageSource, String messageKey,Exception wrappedException) {
    	this(messageSource, messageKey, null, null, Locale.getDefault(),wrappedException);
    }

    public BizException(MessageSource messageSource, String messageKey,Locale locale, 
    		Exception wrappedException) {
    	this(messageSource, messageKey, null, null, locale, wrappedException);
    }

    public BizException(MessageSource messageSource, String messageKey,
    		Object[] messageParameters, Locale locale,Exception wrappedException) {
            this(messageSource, messageKey, messageParameters, null, locale,wrappedException);
    }

    public BizException(MessageSource messageSource, String messageKey,Object[] messageParameters, 
    		Exception wrappedException) {
            this(messageSource, messageKey, messageParameters, null, Locale.getDefault(), wrappedException);
    }

    public BizException(MessageSource messageSource, String messageKey,Object[] messageParameters, 
    		String defaultMessage,Exception wrappedException) {
            this(messageSource, messageKey, messageParameters, defaultMessage,Locale.getDefault(), wrappedException);
    }

    public BizException(MessageSource messageSource, String messageKey,Object[] messageParameters, 
    		String defaultMessage, Locale locale,Exception wrappedException) {

            this.messageKey = messageKey;
            this.messageParameters = messageParameters;
            this.message = messageSource.getMessage(messageKey, messageParameters,defaultMessage, locale);
            this.wrappedException = wrappedException;
    } 

}

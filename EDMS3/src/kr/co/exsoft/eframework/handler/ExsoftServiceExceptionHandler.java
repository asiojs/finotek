package kr.co.exsoft.eframework.handler;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import kr.co.exsoft.eframework.exception.*;

/**
 * Handler 구현 클래스
 * @author 패키지 개발팀
 * @since 2014.07.15
 * @version 3.0
 *
 */
public class ExsoftServiceExceptionHandler implements ExceptionHandler{

    protected Log logger = LogFactory.getLog(this.getClass());
    
    public void occur(Exception exception, String packageName) {
    	
    	logger.debug("ExsoftServiceExceptionHandler run..............."+ ((BizException) exception).getWrappedException());
    	
    	 try {
    		 
             if (exception instanceof BizException) {
                 Exception exx = (Exception) ((BizException) exception).getWrappedException();
                 
                 if (exx != null) {

                	 logger.debug("error msg......"+exx.getMessage());                     
                     exx.printStackTrace();
                 }
             }
            
         } catch (Exception e) {
             e.printStackTrace();
         }
    }
    
}

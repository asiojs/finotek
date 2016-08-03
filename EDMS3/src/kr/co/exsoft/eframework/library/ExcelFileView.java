package kr.co.exsoft.eframework.library;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.co.exsoft.eframework.util.CharConversion;

import org.springframework.util.FileCopyUtils;
import org.springframework.web.servlet.view.AbstractView;

/**
 * 엑셀일괄업로드 샘플 다운로드 처리
 *
 * @author 패키지팀
 * @since 2014. 12. 2.
 * @version 1.0
 * 
 */

public class ExcelFileView extends AbstractView {

    public ExcelFileView() {
    	setContentType("application/octet-stream; charset=UTF-8");
    }
    
    @Override
    protected void renderMergedOutputModel(@SuppressWarnings("rawtypes") Map model, HttpServletRequest request,
        HttpServletResponse response) throws Exception {
    	
    	String downloadFile = (String)model.get("filePath");
    	
    	File file = new File(downloadFile);
    	
    	response.setContentType(getContentType());
        response.setContentLength((int)file.length());      
        response.setHeader("Content-Disposition", "attachment; fileName=\"" + CharConversion.K2E(file.getName()) + "\";");             
        response.setHeader("Content-Transfer-Encoding", "binary");
        
        OutputStream out = response.getOutputStream();        
        FileInputStream fis = null;

        try {
        	
             fis = new FileInputStream(file);
             FileCopyUtils.copy(fis, out);
             
         }catch(Exception e){
        	 response.setHeader("Content-Disposition", "attachment; fileName=\"" + "File Not Found" + "\";");             
        	 response.setContentLength(0);        	 
         }finally {
             if(fis != null) {
                 try {
                     fis.close();
                 } catch(IOException ex) {	}
             }
         }
         
         out.flush();
    }
    
}

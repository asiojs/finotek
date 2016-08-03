package kr.co.exsoft.eframework.library;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map; 

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.co.exsoft.document.vo.PageVO;
import kr.co.exsoft.eframework.configuration.Constant;
import kr.co.exsoft.eframework.util.CharConversion;
import kr.co.exsoft.eframework.util.ConfigData;
import kr.co.exsoft.eframework.util.UtilFileApp;

import org.springframework.util.FileCopyUtils;
import org.springframework.web.servlet.view.AbstractView;

import kr.co.exsoft.eframework.util.ZipUtils;


/**
 * DownloadView 정의
 * @author 패키지 개발팀
 * @since 2014.07.15
 * @version 3.0
 *
 */
public class DownloadView extends AbstractView {
	
    public DownloadView() {
    	setContentType("application/octet-stream; charset=UTF-8");
    }

    @Override
    protected void renderMergedOutputModel(@SuppressWarnings("rawtypes") Map model, HttpServletRequest request,
        HttpServletResponse response) throws Exception {
    	
    	@SuppressWarnings("unchecked")
		List<PageVO> list = (ArrayList<PageVO>)model.get("pageList");
    	String isZip = (String)model.get("isZip");
    	String[] filename = new String[list.size()];
    	String downloadFile = "";
    	String zipFileName = "";
    	
    	if (list != null && list.size() > 0) {
    		
    		// 파일갯수에 따라서 별도 처리한다. 1개이상인 경우 zip으로 압축해서 다운로드 처리함.
    		if(list.size() == 1 && isZip.equals(Constant.F))	{
    		
    			for(PageVO pageVO : list){
    				downloadFile = pageVO.getDown_path();	
        		}    	
    			
    		}else {
    			    			
    			for(int i=0;i<list.size();i++)	{    			
    				PageVO pageVO = (PageVO)list.get(i);    				
    				filename[i] = pageVO.getDown_path();
    				if(i == 0){
    					zipFileName = pageVO.getDoc_name() + ".zip";
    				}
    			}

    			String zipPath = ConfigData.getString("FILE_DOWN_PATH") + UtilFileApp.removeToken(zipFileName);    			
    			ZipUtils.createZipFile(filename, zipPath);    			   
    			downloadFile = zipPath;
    		}	
    	}
    	
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
        	 response.setHeader("Content-Disposition", "attachment; fileName=\"" + "Exrep File Not Found" + "\";");             
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

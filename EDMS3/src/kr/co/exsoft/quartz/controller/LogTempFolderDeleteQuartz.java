package kr.co.exsoft.quartz.controller;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import kr.co.exsoft.common.service.CommonService;
import kr.co.exsoft.eframework.configuration.Constant;
import kr.co.exsoft.eframework.exception.BizException;
import kr.co.exsoft.eframework.util.ConfigData;
import kr.co.exsoft.quartz.service.QuartzService;

/**
 * 로그폴더/임시폴더 삭제처리
 *
 * @author 패키지팀
 * @since 2014. 9. 29.
 * @version 1.0
 * 
 */

public class LogTempFolderDeleteQuartz extends QuartzJob {

	protected static final Log logger = LogFactory.getLog(LogTempFolderDeleteQuartz.class);
	protected SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	protected String[] folderList =  ConfigData.getString("TEMP_FOLDER_LIST").split("[|]");
	protected List<String> folderArray = new ArrayList<String>();
	
	public LogTempFolderDeleteQuartz() {
	
		if(folderList != null && folderList.length > 0) {			 
			 for(String folderName : folderList) {		
				 folderArray.add(folderName);				 
			 }		
		}
	}
	
	@Override
	protected void executeJob(JobExecutionContext context){

		CommonService commonService = (CommonService)super.getBean("commonService");
		QuartzService quartzService = (QuartzService)super.getBean("quartzService");

		/*********************************************************************************************
		 * 배치프로그램명 : 로그/임시폴더 삭제처리
		 * 내용 : 임시폴더 및 로그폴더를 주기적으로 삭제처리한다. 폴더구분자는 | 설정한다. 관리폴더 즉 대상폴더는 삭제되지 않는다.
		 * 관련설정파일 : config.properites
		 * TEMP_FOLDER_LIST = C:/exdown/tmp|C:/exdown/upload 
		 * DELETE_DECADE = 7
		 *********************************************************************************************/
		
		long sTime = System.currentTimeMillis();
		
		logger.info("LogTempFolderDeleteQuartz START ="+df.format(sTime));
		
		Calendar cal = Calendar.getInstance();
		int decade = ConfigData.getInt("DELETE_DECADE");		// 삭제주기 
		
		HashMap<String,Object> resultMap = new HashMap<String,Object>();
		long work_idx = 0;		
		
		try {
			
			cal.add(Calendar.DATE,-decade);		
			
			if(folderArray.size() > 0 )	{
				
				work_idx = commonService.commonNextVal(Constant.COUNTER_ID_BATCH_WORK);

				quartzService.batchWorkWrite(work_idx,Constant.WORK_BATCH,Constant.BATCH_TEMP_LOG_FOLDER_DELETE);
				 
				 for(String folderName : folderArray) {

					 File folderInfo = new File(folderName);
					 if (folderInfo.isDirectory()) {
						 deleteTempFolderAll(folderInfo, cal.getTimeInMillis());						 
					 }					
				 }
				 
				 resultMap.put("message","정상 처리");			
				 resultMap.put("work_state",Constant.T);			
			}
			
		}catch(BizException e){	
			
			logger.error(e.getMessage());
			resultMap.put("work_state",Constant.F);
			resultMap.put("message","BizException Error");
			
		}catch(Exception e) {
			
			logger.error(e.getMessage());
			resultMap.put("work_state",Constant.F);
			resultMap.put("message","Exception Error");			
			
		}finally{
		
			Date now = new Date();					 
			resultMap.put("work_idx",work_idx);
			resultMap.put("work_edate",df.format(now));
			
			try {
				
				if(folderArray.size() > 0 )	{
					quartzService.batchWorkUpdate(resultMap);
				}
				
			}catch(Exception e){
				logger.error(e.getMessage());
			}
		}
		
		long eTime = System.currentTimeMillis();
		
		logger.info("LogTempFolderDeleteQuartz END ="+df.format(eTime));
		
	}

	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 폴더가 관리대상 폴더인지 체크한다.
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : isMainFolder
	 * @param folderName
	 * @return boolean
	 */
	private boolean isMainFolder(String folderName) {
		
		boolean ret = false;
		
		if(folderArray.size() > 0 )	{
			 
			 for(String folderNm : folderArray) {
				 
				 if(folderNm.equals(folderName)) {
					 ret = true;
					 break;
				 }
			 }		
		}
		
		
		return ret;
	}
	
	/**
	 * 
	 * <pre>
	 * 1. 개용 : 폴더삭제처리
	 * 2. 처리내용 : 
	 * </pre>
	 * @Method Name : deleteTempFolderAll
	 * @param directory
	 * @param longDate void
	 */
	private void deleteTempFolderAll(File directory,long longDate) {
	    	try {
	    		// 파일 리스트를 구한다.
		    	File[] fileList = directory.listFiles();
		    	
				for (File f : fileList) {
					// 해당 File 객체가 존재하는지 확인.
					if (f.exists()) {
						// 파일일 경우
						if (f.isFile()) {
							
							if(longDate >= f.lastModified()) {
								if (f.delete()) {
									logger.info(longDate + "[ Delete File Name ]" + f.getName());
								}
							}

						// 디렉토리일 경우는 재귀를 통해서 삭제처리한다.
						// 파일이 0개인 경우도 삭제하는데, 관리대상 폴더인 경우는 제외한다.	
						} else if (f.isDirectory()) {

							deleteTempFolderAll(f, longDate);

							File[] fList = f.listFiles();
							
							if (fList.length == 0 && !isMainFolder(f.getName())) {
								
								logger.info(longDate + "[ Delete Folder Name ]" + f.getName());
								
								if (f.delete()) {
									logger.info(longDate + "[ Delete Folder Name ]" + f.getName());
								}

							}
							
						}
					}
				}
	    	} catch (Exception e) {
	    		logger.error("File delete error", e);	    		
	    	}
	    }
	
}

package kr.co.exsoft.quartz.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import kr.co.exsoft.common.service.CommonService;
import kr.co.exsoft.eframework.configuration.Constant;
import kr.co.exsoft.eframework.exception.BizException;
import kr.co.exsoft.eframework.exception.ExrepClientException;
import kr.co.exsoft.eframework.repository.EXrepClient;
import kr.co.exsoft.eframework.util.ConfigData;
import kr.co.exsoft.eframework.util.StringUtil;
import kr.co.exsoft.quartz.service.QuartzService;
import kr.co.exsoft.quartz.vo.FileQueueDeleteVO;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;

public class FileQueueDelete extends QuartzJob {

	protected static final Log logger = LogFactory.getLog(FileQueueDelete.class);
	protected SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	protected boolean isDevelop = false;
	protected int delimiter =  ConfigData.getInt("BATCH_LIMIT");							// 배치실행건수
	protected long rows = ConfigData.getLong("TERMINATE_PAGE_LIMIT");				// 하루 최대 처리건수(0:제한없음 - 사이트 서버상태고려)
	 
	
	@Override
	protected void executeJob(JobExecutionContext context){
		
		CommonService commonService = (CommonService)super.getBean("commonService");
		QuartzService quartzService = (QuartzService)super.getBean("quartzService");
		List<FileQueueDeleteVO> pageList = new ArrayList<FileQueueDeleteVO>();
		List<FileQueueDeleteVO> fileQueueDeleteList = new ArrayList<FileQueueDeleteVO>();
		
		HashMap<String,Object> resultMap = new HashMap<String,Object>();
		HashMap<String,Object> param = new HashMap<String,Object>();
		
		long work_idx = 0;
		long successCnt = 0;
		//long totalSize = 0;
		long sTime = System.currentTimeMillis();
		// eXrep C/S Client 생성. 
		EXrepClient eXrepClient = new EXrepClient();
		
		try {
			
			logger.info("TerminatePage START ="+df.format(sTime));
			
			// 1. 로그등록처리
			work_idx = commonService.commonNextVal(Constant.COUNTER_ID_BATCH_WORK);
			quartzService.batchWorkWrite(work_idx,Constant.WORK_BATCH,Constant.BATCH_TERMINATED);
			
			// 2. 삭제대상 페이지 목록 가져오기 
			pageList = quartzService.fileQueueDeleteList(param);
			
			// 3.페이지 삭제처리
			if(pageList != null && pageList.size() > 0)	{
				
				eXrepClient.connect();				
				
				// 페이지 삭제 비지니스 로직 처리
				for(int i=0;i<pageList.size();i++)		{
					
					FileQueueDeleteVO filequeuedeleteVO = pageList.get(i);					
					fileQueueDeleteList.add(filequeuedeleteVO);
					
					if( i !=0 && ( i % delimiter == 0 ) ) {			
						
						try {							
							resultMap = quartzService.delFileProc(fileQueueDeleteList, eXrepClient);
							successCnt = successCnt + Integer.parseInt(resultMap.get("successCnt").toString());
							//totalSize = totalSize + Long.parseLong(resultMap.get("totalSize").toString());
						}catch(Exception e) {
							logger.error(e.getMessage());
						}

						fileQueueDeleteList.clear();
						
					}else if(i == (pageList.size()-1) ) {
					
						try {
							resultMap = quartzService.delFileProc(fileQueueDeleteList, eXrepClient);
							successCnt = successCnt + Integer.parseInt(resultMap.get("successCnt").toString());
							//totalSize = totalSize + Long.parseLong(resultMap.get("totalSize").toString());
						}catch(Exception e) {
							logger.error(e.getMessage());
						}
						
						fileQueueDeleteList.clear();
					}

				}
				
				// eXrep C/S Client Close
				try {
					eXrepClient.disconnect();
				} catch (ExrepClientException e) {
					throw new Exception(e);
				}
				
				resultMap.put("message","폐기문서 삭제건수="+successCnt);
				// +":: 총용량="+StringUtil.fileSize(totalSize)
				
			}else {
				resultMap.put("message","삭제 대상 페이지가 없습니다.");
			}
			
			// 6. 로그결과처리
			resultMap.put("work_state",Constant.T);				
			
		}catch(BizException e){	
			
			logger.error(e.getMessage());
			resultMap.put("work_state",Constant.F);
			resultMap.put("message","비지니스 로직 에러");
			
		}catch(Exception e) {
				
			logger.error(e.getMessage());
			resultMap.put("work_state",Constant.F);
			resultMap.put("message","EXCEPTION ERROR");			
				
		}finally{
		
			Date now = new Date();				
			resultMap.put("work_idx",work_idx);
			resultMap.put("work_edate",df.format(now));
	
			try {
			
				quartzService.batchWorkUpdate(resultMap);
				
			}catch(Exception e){
				logger.error(e.getMessage());
			}
		}
		
		long eTime = System.currentTimeMillis();
		
		logger.info("TerminatePage END ="+df.format(eTime));
		
	}

}

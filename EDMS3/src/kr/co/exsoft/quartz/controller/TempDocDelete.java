package kr.co.exsoft.quartz.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import kr.co.exsoft.common.service.CommonService;
import kr.co.exsoft.eframework.configuration.Constant;
import kr.co.exsoft.eframework.util.ConfigData;
import kr.co.exsoft.quartz.service.QuartzService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;

/**
 * 임시작업함 삭제처리 배치프로그램 
 *
 * @author 패키지팀
 * @since 2014. 10. 13.
 * @version 1.0
 * 
 */

public class TempDocDelete  extends QuartzJob {

	protected static final Log logger = LogFactory.getLog(TempDocDelete.class);
	protected SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

	@Override
	protected void executeJob(JobExecutionContext context){
		
		CommonService commonService = (CommonService)super.getBean("commonService");
		QuartzService quartzService = (QuartzService)super.getBean("quartzService");
		
		List<HashMap<String,Object>> tempDocList = new ArrayList<HashMap<String,Object>>();			
		
		HashMap<String,Object> resultMap = new HashMap<String,Object>();
		HashMap<String,Object> param1 = new HashMap<String,Object>();

		long work_idx = 0;
		long sTime = System.currentTimeMillis();

		try {	
			
			logger.info("TempDocDelete START ="+df.format(sTime));
			
			// 1. 로그등록처리
			work_idx = commonService.commonNextVal(Constant.COUNTER_ID_BATCH_WORK);
			quartzService.batchWorkWrite(work_idx,Constant.WORK_BATCH,Constant.BATCH_TEMP_DOC);
			
			// 2.임시작업함 삭제대상 목록 가져오기
			param1.put("decade", ConfigData.getInt("TEMP_DOC_DECADE"));
			tempDocList = quartzService.tempDelDocList(param1);
			
			// 3.임시작업함 목록 삭제처리
			if(tempDocList != null && tempDocList.size() > 0)	{
				
				for(HashMap<String,Object> docInfo : tempDocList)	{
					
					HashMap<String,Object> param2 = new HashMap<String,Object>();
					param2.put("root_id",docInfo.get("root_id"));
					param2.put("user_id",docInfo.get("user_id"));
					quartzService.tempDocDelete(param2);					
				}
				
				resultMap.put("message","임시작업함 삭제건수 :: "+tempDocList.size());
				
			}else {
				resultMap.put("message","삭제 대상 목록이 없습니다.");
			}
			
			// 4. 로그결과처리
			resultMap.put("work_state",Constant.T);			
			
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
		logger.info("TempDocDelete END ="+df.format(eTime));
		
	}

}

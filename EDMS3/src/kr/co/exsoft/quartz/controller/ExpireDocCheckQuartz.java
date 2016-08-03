package kr.co.exsoft.quartz.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.co.exsoft.document.service.DocumentService;
import kr.co.exsoft.eframework.configuration.Constant;
import kr.co.exsoft.eframework.exception.BizException;
import kr.co.exsoft.eframework.util.StringUtil;
import kr.co.exsoft.common.service.CommonService;
import kr.co.exsoft.quartz.service.QuartzService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;

/**
 * 보존년한이 지난 문서에 대해 만기 처리한다
 *
 * @author 패키지팀
 * @since 2014. 10. 01.
 * @version 1.0
 * 
 */

public class ExpireDocCheckQuartz extends QuartzJob {

	protected static final Log logger = LogFactory.getLog(ExpireDocCheckQuartz.class);
	protected SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	
	@Override
	protected void executeJob(JobExecutionContext context) {
		
		CommonService commonService = (CommonService)super.getBean("commonService");
		DocumentService documentService = (DocumentService)super.getBean("documentService");
		QuartzService quartzService = (QuartzService)super.getBean("quartzService");
		
		List<HashMap<String,Object>> expiredDocList = new ArrayList<HashMap<String,Object>>();
		
		HashMap<String,Object> resultMap = new HashMap<String,Object>();
		Map<String,Object> result = new HashMap<String,Object>();
		HashMap<String,Object> param = new HashMap<String,Object>();
				
		// 만기문서처리일 :: 현재시간기준
		String expiredDate = StringUtil.getCurrentTime();
		
		long work_idx = 0;
		long sTime = System.currentTimeMillis();
		
		try {
			
			logger.info("ExpireDocCheckQuartz START ="+df.format(sTime));
			
			// 1. 로그등록처리
			work_idx = commonService.commonNextVal(Constant.COUNTER_ID_BATCH_WORK);
			quartzService.batchWorkWrite(work_idx,Constant.WORK_BATCH,Constant.BATCH_EXPIRED_DOC);
			
			// 2.만기문서 목록 리스트 가져오기 : 전체문서 대상
			param.put("workType",Constant.EXPIRED);
			param.put("expiredDate", expiredDate);
			expiredDocList = quartzService.batchDocList(param);

			if(expiredDocList != null && expiredDocList.size() > 0 )	{
				
				// 3.만기문서 처리 :: param : 사용안함
				result = documentService.expiredDocProc(expiredDocList, param);				
				resultMap.put("message","현재버전 문서수::"+expiredDocList.size()+"/" + "모든 버전 문서수::"+result.get("total"));
				
			}else {				
				resultMap.put("message","만기문서가 존재하지 않습니다.");
			}
			
			// 4. 로그결과처리
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
				
				// 배치로그 수정처리 : 완료시간/상태/메세지
				quartzService.batchWorkUpdate(resultMap);
							
			}catch(Exception e){
				logger.error(e.getMessage());
			}
		}
		
		long eTime = System.currentTimeMillis();
		
		logger.info("ExpireDocCheckQuartz END ="+df.format(eTime));
	
	}

	
}

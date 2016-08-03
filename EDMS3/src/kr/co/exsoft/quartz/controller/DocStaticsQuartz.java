package kr.co.exsoft.quartz.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;

import kr.co.exsoft.common.service.CommonService;
import kr.co.exsoft.eframework.configuration.Constant;
import kr.co.exsoft.eframework.exception.BizException;
import kr.co.exsoft.eframework.util.StringUtil;
import kr.co.exsoft.quartz.service.QuartzService;

/**
 * 문서일별 집계처리
 *
 * @author 패키지팀
 * @since 2014. 9. 29.
 * @version 1.0
 * 
 */

public class DocStaticsQuartz extends QuartzJob {

	protected static final Log logger = LogFactory.getLog(DocStaticsQuartz.class);
	protected SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	
	@Override
	protected void  executeJob(JobExecutionContext context){

		CommonService commonService = (CommonService)super.getBean("commonService");
		QuartzService quartzService = (QuartzService)super.getBean("quartzService");
		
		HashMap<String,Object> resultMap = new HashMap<String,Object>();
		HashMap<String,Object> param1 = new HashMap<String,Object>();
		HashMap<String,Object> param2 = new HashMap<String,Object>();
		
		// 통계 집계 수행처리 기준일
		String sDate = StringUtil.getYesterday();
		
		long work_idx = 0;
		long sTime = System.currentTimeMillis();
		boolean isProcess = false;
		
		try {
				
			/**************************************************************************************************
			 * 1.사용자별 문서현황
			 * - 기준일 기준으로 XR_DOCUMENT_HT 사용자별 등록/조회/수정/삭제건수를 가져온다.
			 * - XR_DOCUMENT_USER_HT 데이터를 등록처리한다.
			 * 
			 * 2.부서별 문서현황
			 * - 기준일 기준으로 XR_DOCUMENT_HT 그룹별 등록/조회/수정/삭제건수를 가져온다.
			 * - XR_DOCUMENT_GROUP_HT 데이터를 등록처리한다.  
			 *************************************************************************************************/
			
			logger.info("DocStaticsQuartz START ="+df.format(sTime));

			// 1.수행일 기준 감사정책 배치업무 수행여부 체크
			param1.put("work_nm",Constant.BATCH_STATISITCS);
			param1.put("work_type",Constant.WORK_BATCH);
			param1.put("work_sdate", StringUtil.getToday());
			param1.put("work_state",Constant.T);
			
			// 2.미수행인 경우에만 수행처리를 한다.
			isProcess = quartzService.isBatchWork(param1);
			if(!isProcess)	{
			
				// 3.배치로그 등록처리
				work_idx = commonService.commonNextVal(Constant.COUNTER_ID_BATCH_WORK);
				quartzService.batchWorkWrite(work_idx,Constant.WORK_BATCH,Constant.BATCH_STATISITCS);

				// 4. 사용자별/부서별 문서현황 처리
				param2.put("sdate", sDate);
				quartzService.docStatusProc(param2);
				
				// 5. 로그결과처리
				resultMap.put("message","정상 처리");			
				resultMap.put("work_state",Constant.T);				
				
			}else {
				logger.info("이미 처리되었습니다.");
			}
			
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
			
				// 배치수행시만 로그를 변경처리한다.
				if(!isProcess && work_idx != 0)	{
					quartzService.batchWorkUpdate(resultMap);
				}
			
			}catch(Exception e){			
				logger.error(e.getMessage());
			}
				
		}
	
		long eTime = System.currentTimeMillis();
		
		logger.info("DocStaticsQuartz END ="+df.format(eTime));
		
	}
	


}

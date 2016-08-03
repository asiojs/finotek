package kr.co.exsoft.quartz.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.co.exsoft.common.service.CommonService;
import kr.co.exsoft.eframework.configuration.Constant;
import kr.co.exsoft.eframework.exception.BizException;
import kr.co.exsoft.eframework.util.CommonUtil;
import kr.co.exsoft.eframework.util.StringUtil;
import kr.co.exsoft.quartz.service.QuartzService;
import kr.co.exsoft.eframework.util.MailSendUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;


/**
 * 기준 조회수 초과자에 대한 감사 처리 
 *
 * @author 패키지팀
 * @since 2014. 9. 29.
 * @version 1.0
 * 
 */

public class AuditQuartz extends QuartzJob {

	protected static final Log logger = LogFactory.getLog(AuditQuartz.class);
	protected SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	protected boolean isDevelop = false;
	
	@Override
	protected void executeJob(JobExecutionContext context){
		
		CommonService commonService = (CommonService)super.getBean("commonService");
		QuartzService quartzService = (QuartzService)super.getBean("quartzService");
		
		List<HashMap<String,Object>> auditList = new ArrayList<HashMap<String,Object>>();			// 기준 조회수 초과자 리스트
		List<String> receiverMailList = new ArrayList<String>();													// 메일수신자 리스트
		
		HashMap<String,Object> resultMap = new HashMap<String,Object>();
		HashMap<String,Object> param1 = new HashMap<String,Object>();
		HashMap<String,Object> param2 = new HashMap<String,Object>();
		
		Map<String,Object> auditConfig = new HashMap<String,Object>();
		
		// 감사기준일 :: YESTERDAY
		String auditDate = StringUtil.getYesterday();
		String report_mail_receiver_address = "";

		boolean isProcess = false;
		long work_idx = 0;
		long sTime = System.currentTimeMillis();

		try {
			
			logger.info("AuditQuartz START ="+df.format(sTime));
			
			// 1.수행일 기준 감사정책 배치업무 수행여부 체크
			param1.put("work_nm",Constant.BATCH_AUDIT);
			param1.put("work_type",Constant.WORK_BATCH);
			param1.put("work_sdate", StringUtil.getToday());
			param1.put("work_state",Constant.T);
			
			// 2.미수행인 경우에만 수행처리를 한다.
			isProcess = quartzService.isBatchWork(param1);
			if(!isProcess)	{
								
				// 3. 로그등록처리
				work_idx = commonService.commonNextVal(Constant.COUNTER_ID_BATCH_WORK);
				quartzService.batchWorkWrite(work_idx,Constant.WORK_BATCH,Constant.BATCH_AUDIT);
				
				// 4.감사 설정 조회.
				param1.clear();
				param1.put("stype",Constant.SYS_TYPE_AUDIT);
				auditConfig = commonService.auditConfig(param1);				
				if (auditConfig == null) {
					throw new Exception("감사 설정 정보가 존재하지 않습니다.");
				}

				String isMailSend = auditConfig.get("send_report_mail") != null ? auditConfig.get("send_report_mail").toString() : Constant.F;
				report_mail_receiver_address = auditConfig.get("report_mail_receiver_address") != null ? auditConfig.get("report_mail_receiver_address").toString() : "";
				
				// 5. 기준 조회수 초과자 목록 조회
				param2.put("audit_date",auditDate);
				if(isDevelop) {
					param2.put("read_count_threshold",1);
				}else {
					param2.put("read_count_threshold",auditConfig.get("read_count_threshold"));
				}
				
				
				auditList = quartzService.auditExceedList(param2);
				
				// 5.1기준 조회수 초과자가 존재하는 경우.
				if(auditList != null && auditList.size() > 0 && isMailSend.equals(Constant.T))	{
					
					// 5,2 메일발송정보 설정
					String subject = "EDMS Audit Report " + auditDate;
		    		String messageText = CommonUtil.getAuditReportMessage(auditDate, auditConfig, auditList).toString();
		    		
		    		// 5.3 메일수신 담당자
		    		String[] receiverAddress = StringUtil.split2Array(report_mail_receiver_address, ";", false);		    	
		    		if(receiverAddress.length > 0) {
		    			
		    			receiverMailList = Arrays.asList(receiverAddress);
		    			
		    			// 5.4 메일전송처리				    	
			    		MailSendUtil.sendAuditMail(subject,receiverMailList,messageText);		    					    		
			    		resultMap.put("message","기준 조회수 초과가 메일발송처리");
			    		
		    		}else {
		    			resultMap.put("message","메일수신자 담당없음 :: 메일발송안함");
		    		}

				}else {
					
					if(auditList != null && auditList.size() > 0 )	{
						resultMap.put("message","기준 조회수 초과자 있음 :: 메일발송안함");
					}else {
						resultMap.put("message","기준 조회수 초과자 없음");
					}
					
				}
				
				// 기준초과자수 등록처리 - 대량문서 열람 감사 관리 데이티 업력 부분				
				if(auditList != null && auditList.size() > 0) {
					
					for(HashMap<String,Object> auditMap :  auditList)	{
						
						auditMap.put("audit_date",auditDate.replaceAll("-",""));
						auditMap.put("report_mail_receiver_address",report_mail_receiver_address);						
						quartzService.writeAudit(auditMap);
					}
					
				}
					
				
				// 6. 로그결과처리
				resultMap.put("work_state",Constant.T);				
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
		
		logger.info("AuditQuartz END ="+df.format(eTime));

	}


}

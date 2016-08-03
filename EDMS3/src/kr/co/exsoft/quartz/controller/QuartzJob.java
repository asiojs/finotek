package kr.co.exsoft.quartz.controller;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * Quartz Job Abstract 클래스
 *
 * @author 패키지팀
 * @since 2014. 9. 30.
 * @version 1.0
 * 
 */

public abstract class QuartzJob extends QuartzJobBean {
	
	private ApplicationContext ctx;
	
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		ctx = (ApplicationContext)context.getJobDetail().getJobDataMap().get("applicationContext");	   
		executeJob(context);
	}
	  
	protected Object getBean(String beanId) {
		return ctx.getBean(beanId);
	}
	  
	protected abstract void executeJob(JobExecutionContext jobexecutioncontext);

}

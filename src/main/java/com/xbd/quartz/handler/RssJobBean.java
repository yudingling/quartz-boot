package com.xbd.quartz.handler;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

@DisallowConcurrentExecution
public class RssJobBean extends QuartzJobBean {
	private final Log logger = LogFactory.getLog(RssJobBean.class);
	
	public static final String RSSIDKEY = "rssId";
	
	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		try {
			Long rssId = (Long) context.getMergedJobDataMap().get(RSSIDKEY);
			
			this.getHandler(context).executeRss(rssId, context);
			
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
	
	private ExecutelHandler getHandler(JobExecutionContext context) throws SchedulerException{
		ApplicationContext ctx = (ApplicationContext) context.getScheduler().getContext().get("applicationContext");
		return ctx.getBean(ExecutelHandler.class);
	}
}

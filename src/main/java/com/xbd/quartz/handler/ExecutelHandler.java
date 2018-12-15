package com.xbd.quartz.handler;

import org.quartz.JobExecutionContext;

public interface ExecutelHandler {
	void executeRss(Long rssId, JobExecutionContext context);
	void executeNormal(String name, JobExecutionContext context);
}

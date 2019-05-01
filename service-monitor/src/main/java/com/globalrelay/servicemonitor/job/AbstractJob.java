package com.globalrelay.servicemonitor.job;

import org.quartz.JobExecutionContext;

import com.globalrelay.servicemonitor.exception.ServiceMonitorJobException;


public abstract class AbstractJob {
	
	public abstract void execute(JobExecutionContext context) throws ServiceMonitorJobException;

}

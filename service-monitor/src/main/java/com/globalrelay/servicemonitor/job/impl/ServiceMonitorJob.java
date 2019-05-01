package com.globalrelay.servicemonitor.job.impl;

import javax.annotation.Resource;

import org.quartz.JobExecutionContext;

import com.globalrelay.servicemonitor.constant.ServiceMonitorJobConstant;
import com.globalrelay.servicemonitor.exception.ErrorCode;
import com.globalrelay.servicemonitor.exception.ServiceMonitorJobException;
import com.globalrelay.servicemonitor.job.AbstractJob;
import com.globalrelay.servicemonitor.service.ServiceMonitorService;

/**
 * An child class for {@linkplain AbstractJob} class, this Scheduler job will
 * execute the logic related to monitoring a service.
 * 
 * @author User
 *
 */
public class ServiceMonitorJob extends AbstractJob {

	@Resource
	private ServiceMonitorService monitorService;

	@Override
	public void execute(JobExecutionContext context) throws ServiceMonitorJobException {

		try {

			Long taskId = context.getJobDetail().getJobDataMap().getLong(ServiceMonitorJobConstant.TASK_ID_KEY);
			monitorService.monitorStatus(taskId);
		} catch (Exception e) {
			throw new ServiceMonitorJobException("Failed to run the monitor job with context:" + context, e,
					ErrorCode.JOB_ERROR);
		}
	}

}

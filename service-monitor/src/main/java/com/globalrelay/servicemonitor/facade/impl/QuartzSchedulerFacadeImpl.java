package com.globalrelay.servicemonitor.facade.impl;

import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.globalrelay.servicemonitor.constant.ServiceMonitorJobConstant;
import com.globalrelay.servicemonitor.domain.ServiceMonitorTask;
import com.globalrelay.servicemonitor.exception.ErrorCode;
import com.globalrelay.servicemonitor.exception.ServiceMonitorFacadeException;
import com.globalrelay.servicemonitor.facade.SchedulerFacade;
import com.globalrelay.servicemonitor.job.impl.RouterJob;

/**
 * An implementation of {@linkplain SchedulerFacade}. This implementation uses
 * Quartz Scheduler API to create, start, stop and retrieve Jobs using an
 * scheduler
 * 
 * @author Ravikiran Butti
 *
 */
@Component
public class QuartzSchedulerFacadeImpl implements SchedulerFacade {

	private static final String GROUP = "GROUP";
	private static Logger LOG = LoggerFactory.getLogger(QuartzSchedulerFacadeImpl.class);

	@Autowired
	private Scheduler scheduler;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.globalrelay.servicemonitor.facade.SchedulerFacade#createScheduler(com.
	 * globalrelay.servicemonitor.domain.ServiceMonitorTask)
	 */
	public void createJob(ServiceMonitorTask task) throws ServiceMonitorFacadeException {

		LOG.debug("Started to create and schedule a Job | Input task ", task);
		try {

			Trigger trigger = org.quartz.TriggerBuilder.newTrigger().withIdentity(task.getId().toString(), GROUP)
					.withSchedule(org.quartz.CronScheduleBuilder.cronSchedule(task.getStatus().getCronExpression()))
					.startNow().build();

			TriggerKey existTg = org.quartz.TriggerKey.triggerKey(task.getId().toString(), GROUP);

			if (scheduler.checkExists(existTg)) {
				scheduler.unscheduleJob(existTg);
				scheduler.deleteJob(org.quartz.JobKey.jobKey(task.getId().toString(), GROUP));
			}

			JobDetail job = org.quartz.JobBuilder.newJob(RouterJob.class).withIdentity(task.getId().toString(), GROUP)
					.usingJobData(ServiceMonitorJobConstant.JOB_CLASS_KEY, task.getJobClass())
					.usingJobData(ServiceMonitorJobConstant.TASK_ID_KEY, task.getId()).build();

			scheduler.scheduleJob(job, trigger);

			LOG.info("Successfully created and scheduled a Job | Input task ", task);
		} catch (Exception e) {
			throw new ServiceMonitorFacadeException("Failed to create and schedule the task :" + task, e,
					ErrorCode.FACADE_ERROR);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.globalrelay.servicemonitor.facade.SchedulerFacade#stopJob(java.lang.
	 * String)
	 */
	public void stopJob(String id) throws ServiceMonitorFacadeException {

		LOG.debug("Started to stop a Job | Job Id ", id);
		try {
			TriggerKey tg = org.quartz.TriggerKey.triggerKey(id, GROUP);
			JobKey key = org.quartz.JobKey.jobKey(id, GROUP);

			if (scheduler.checkExists(tg)) {
				scheduler.unscheduleJob(tg);
				scheduler.deleteJob(key);
			}

			LOG.debug("Successfully stopped a Job | Job Id ", id);
		} catch (Exception e) {
			throw new ServiceMonitorFacadeException("Failed to stop the task with Id :" + id, e,
					ErrorCode.FACADE_ERROR);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.globalrelay.servicemonitor.facade.SchedulerFacade#getJobStatus(java.lang.
	 * String)
	 */
	public boolean getJobStatus(String id) throws ServiceMonitorFacadeException {

		LOG.debug("Started to retrieve a Job | Job Id ", id);
		try {
			TriggerKey tg = org.quartz.TriggerKey.triggerKey(id, GROUP);

			LOG.debug("Successfully retrieved a Job | Job Id ", id);
			return scheduler.checkExists(tg);
		} catch (Exception e) {
			throw new ServiceMonitorFacadeException("Failed to get the task status  with Id :" + id, e,
					ErrorCode.FACADE_ERROR);
		}

	}
}

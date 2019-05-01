package com.globalrelay.servicemonitor.facade.impl;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.springframework.test.context.junit4.SpringRunner;

import com.globalrelay.servicemonitor.domain.ServiceMonitorStatus;
import com.globalrelay.servicemonitor.domain.ServiceMonitorTask;

@RunWith(SpringRunner.class)
public class QuartzSchedulerFacadeImplTest {

	@Mock
	private Scheduler scheduler;

	@InjectMocks
	QuartzSchedulerFacadeImpl facade;

	@Test
	public void testCreateJob() throws Exception {
		ServiceMonitorTask task = new ServiceMonitorTask();
		task.setId(1l);
		task.setJobClass("abd");

		ServiceMonitorStatus status = new ServiceMonitorStatus();
		status.setCronExpression("0 0/1 * 1/1 * ? *");
		task.setStatus(status);

		Mockito.when(scheduler.checkExists(Mockito.any(TriggerKey.class))).thenReturn(true);
		Mockito.when(scheduler.unscheduleJob(Mockito.any(TriggerKey.class))).thenReturn(true);
		Mockito.when(scheduler.deleteJob(Mockito.any(JobKey.class))).thenReturn(true);
		Mockito.when(scheduler.scheduleJob(Mockito.any(JobDetail.class), Mockito.any(Trigger.class)))
				.thenReturn(new Date());

		facade.createJob(task);
	}

	@Test
	public void testStopJob() throws Exception {

		Mockito.when(scheduler.checkExists(Mockito.any(TriggerKey.class))).thenReturn(true);
		Mockito.when(scheduler.unscheduleJob(Mockito.any(TriggerKey.class))).thenReturn(true);
		Mockito.when(scheduler.deleteJob(Mockito.any(JobKey.class))).thenReturn(true);

		facade.stopJob("1");
	}

	@Test
	public void testGetJobStatus() throws Exception {

		Mockito.when(scheduler.checkExists(Mockito.any(TriggerKey.class))).thenReturn(true);

		facade.getJobStatus("1");
	}
}

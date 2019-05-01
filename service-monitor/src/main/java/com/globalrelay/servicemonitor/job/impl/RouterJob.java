package com.globalrelay.servicemonitor.job.impl;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.globalrelay.servicemonitor.configuration.ServiceMonitorApplicationConfiguration;
import com.globalrelay.servicemonitor.job.AbstractJob;

@DisallowConcurrentExecution
public class RouterJob implements Job {

	private static Logger LOG = LoggerFactory.getLogger(RouterJob.class);

	@SuppressWarnings("deprecation")
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {

		LOG.debug("Stated running the Job | Job context : {}", context);
		String jobClass = context.getJobDetail().getJobDataMap().getString("jobClass");

		try {

			AbstractJob b = (AbstractJob) Class.forName(jobClass).newInstance();
			ServiceMonitorApplicationConfiguration.getApplicationContext().getAutowireCapableBeanFactory()
					.autowireBean(b);
			b.execute(context);

			LOG.debug("Successfully executed the Job | Job context : {}", context);

		} catch (Exception e) {
			throw new RuntimeException("Failed to executed the job :" + jobClass, e);
		}
	}
}

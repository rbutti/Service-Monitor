package com.globalrelay.servicemonitor.facade;

import com.globalrelay.servicemonitor.domain.ServiceMonitorTask;
import com.globalrelay.servicemonitor.exception.ServiceMonitorFacadeException;

/**
 * Facade interface for Scheduling Job.
 * <p>
 * Concrete implementation of this interface should implement logic to create,
 * start, stop and retrieve Jobs using an scheduler. Each Job should be capable
 * of running a task
 * 
 * @author Ravikiran Butti
 *
 */
public interface SchedulerFacade {

	/**
	 * Concrete implementation should create a Job for the input task and schedule
	 * it as per the tasks CRON expression
	 * 
	 * @param task - Task for which a job needs to be created
	 * @throws ServiceMonitorFacadeException - Exception thrown if creating and
	 *                                       scheduling a Job fails
	 */
	public void createJob(ServiceMonitorTask task) throws ServiceMonitorFacadeException;

	/**
	 * Concrete implementation should stop a Job based on the input Job/Task Id
	 * 
	 * @param id - An identifier which is same for Job or Task that needs to be
	 *           stopped
	 * @throws ServiceMonitorFacadeException - Exception thrown if there a failure
	 *                                       to stop the job
	 */
	public void stopJob(String id) throws ServiceMonitorFacadeException;

	/**
	 * Concrete implementation should retrieve a Job based on the input Job/Task Id
	 * 
	 * @param id - An identifier which is same for Job or Task that needs to be
	 *           retrieved
	 * @return - True if the job is successfully executed or else false
	 * @throws ServiceMonitorFacadeException - Exception thrown if there a failure
	 *                                       to retrieve the job
	 */
	public boolean getJobStatus(String id) throws ServiceMonitorFacadeException;
}

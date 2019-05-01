package com.globalrelay.servicemonitor.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.globalrelay.servicemonitor.domain.ServiceMonitorTask;
import com.globalrelay.servicemonitor.exception.ServiceMonitorServiceException;

/**
 * Service interface for Scheduling tasks
 * <p>
 * Concrete implementation of this interface should implement logic to create,
 * schedule, start, stop and search for tasks that needs to be scheduled using a
 * Scheduler
 * 
 * @author Ravikiran Butti
 *
 */
public interface SchedulerService {

	/**
	 * Concrete implementation of this method should create a ServiceMonitorTask record in the
	 * database and also schedule the task using a scheduler
	 * 
	 * @param task - ServiceMonitorTask that needs to be scheduled
	 * @return Scheduled ServiceMonitorTask
	 * @throws ServiceMonitorServiceException - Exception thrown if an error occurs
	 *                                        while creating and scheduling a ServiceMonitorTask
	 */
	public ServiceMonitorTask createTask(ServiceMonitorTask task) throws ServiceMonitorServiceException;

	/**
	 * Concrete implementation of this method should start a ServiceMonitorTask identified by the
	 * input TaskId
	 * 
	 * @param taskId - Id of the ServiceMonitorTask that needs to be started
	 * @return Started ServiceMonitorTask
	 * @throws ServiceMonitorServiceException - Exception thrown if an error occurs
	 *                                        while starting a ServiceMonitorTask
	 */
	public ServiceMonitorTask startTask(Long taskId) throws ServiceMonitorServiceException;

	/**
	 * Concrete implementation of this method should stop a ServiceMonitorTask identified by the
	 * input TaskId
	 * 
	 * @param taskId - Id of the ServiceMonitorTask that needs to be stopped
	 * @return Stopped ServiceMonitorTask
	 * @throws ServiceMonitorServiceException - Exception thrown if an error occurs
	 *                                        while stopping a ServiceMonitorTask
	 */
	public ServiceMonitorTask stopTask(Long taskId) throws ServiceMonitorServiceException;

	/**
	 * Concrete implementation of this method should delete a ServiceMonitorTask identified by the
	 * input TaskId
	 * 
	 * @param taskId - Id of the ServiceMonitorTask that needs to be deleted
	 * @return Deleted ServiceMonitorTask
	 * @throws ServiceMonitorServiceException - Exception thrown if an error occurs
	 *                                        while deleting a ServiceMonitorTask
	 */
	public ServiceMonitorTask deleteTask(Long taskId) throws ServiceMonitorServiceException;

	/**
	 * Concrete implementation of this method should search a ServiceMonitorTask identified by the
	 * input TaskId
	 * 
	 * @param taskId - Id of the ServiceMonitorTask that needs to be search
	 * @return Search ServiceMonitorTask
	 * @throws ServiceMonitorServiceException - Exception thrown if an error occurs
	 *                                        while searching a ServiceMonitorTask
	 */
	public ServiceMonitorTask getTask(Long taskId) throws ServiceMonitorServiceException;

	/**
	 * Concrete implementation of this method should search for Tasks identified by
	 * the input Pageable criteria
	 * 
	 * @param pageAble - criteria to retrieve the records
	 * @return Page of tasks
	 * @throws ServiceMonitorServiceException - Exception thrown if an error occurs
	 *                                        while searching for tasks based on
	 *                                        input criteria
	 */
	public Page<ServiceMonitorTask> getTasks(Pageable pageAble) throws ServiceMonitorServiceException;
}

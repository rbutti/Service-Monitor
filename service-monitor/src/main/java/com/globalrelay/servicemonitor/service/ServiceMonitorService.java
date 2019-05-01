package com.globalrelay.servicemonitor.service;

import com.globalrelay.servicemonitor.domain.ServiceMonitorStatus;
import com.globalrelay.servicemonitor.exception.ServiceMonitorServiceException;

/**
 * Service interface for Service Monitor Application.
 * <p>
 * Concrete implementation of this interface should implement logic to validate
 * and monitor status of a service
 * 
 * @author Ravikiran Butti
 *
 */
public interface ServiceMonitorService {

	/**
	 * Service interface for Service Monitor Application.
	 * <p>
	 * Concrete implementation of this interface should implement logic to validate
	 * and monitor status of a service
	 * 
	 * @param taskId - Id of the task that contains of the details of the service to
	 *               be monitored
	 * @return : Status of the Service
	 * @throws ServiceMonitorServiceException : Exception thrown if there is failure
	 *                                        while monitoring the service
	 */
	public ServiceMonitorStatus monitorStatus(Long taskId) throws ServiceMonitorServiceException;
}

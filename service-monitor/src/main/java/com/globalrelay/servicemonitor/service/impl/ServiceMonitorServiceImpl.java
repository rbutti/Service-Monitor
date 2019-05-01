package com.globalrelay.servicemonitor.service.impl;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cronutils.utils.StringUtils;
import com.globalrelay.servicemonitor.constant.ServiceMonitorStatusCode;
import com.globalrelay.servicemonitor.domain.ServiceMonitorStatus;
import com.globalrelay.servicemonitor.domain.ServiceMonitorTask;
import com.globalrelay.servicemonitor.exception.ErrorCode;
import com.globalrelay.servicemonitor.exception.ServiceMonitorFacadeException;
import com.globalrelay.servicemonitor.exception.ServiceMonitorServiceException;
import com.globalrelay.servicemonitor.facade.EmailFacade;
import com.globalrelay.servicemonitor.repository.ServiceMonitorTaskRepository;
import com.globalrelay.servicemonitor.service.ServiceMonitorService;
import com.globalrelay.servicemonitor.strategy.ServiceMonitorStrategy;

/**
 * An implementation of {@linkplain ServiceMonitorService} interface. This class
 * implements logic to retrieve task based on taskId, validate and perform
 * monitoring of the service defined in the task
 * 
 * @author Ravikiran Butti
 *
 */
@Service
public class ServiceMonitorServiceImpl implements ServiceMonitorService {

	private static Logger LOG = LoggerFactory.getLogger(ServiceMonitorServiceImpl.class);

	@Resource
	private ServiceMonitorTaskRepository taskRepository;

	@Autowired
	private EmailFacade emailFacade;

	@Autowired
	private ServiceMonitorStrategy serviceMonitorStrategy;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.globalrelay.servicemonitor.service.ServiceMonitorService#monitorStatus(
	 * java.lang.Long)
	 */
	@Override
	public ServiceMonitorStatus monitorStatus(Long taskId) throws ServiceMonitorServiceException {

		LOG.debug("Started to monitor Status | Input taskId : {}", taskId);
		try {

			// retrieve the task from database
			ServiceMonitorTask task = findByTaskId(taskId);
			ServiceMonitorStatus status = task.getStatus();

			// Check for planned outage and duplicate service processing window
			if (isOutagePeriod(task) || isduplicateServiceProcessed(status)) {

				LOG.info("Cancelling Service Monitoring | Input taskId : {}", taskId);
				return status;
			}

			// check service status
			if (serviceMonitorStrategy.monitorService(status.getHostName(), status.getPort())) {
				// process record for service active
				processServiceStatusActive(task);

			} else {
				// process records for service inactive
				processServiceStatusInactive(task);
			}

			// update the task details
			taskRepository.save(task);

			LOG.info("Completed monitoring of service | Input taskId : {}, host Name : {}, port Number: {}", taskId,
					status.getHostName(), status.getPort());
			return status;

		} catch (Exception e) {
			throw new ServiceMonitorServiceException("Failed monitor the status for  task  with Id:" + taskId, e,
					ErrorCode.SERVICE_ERROR);
		}
	}

	/**
	 * Private method to process the task when the service monitored is inactive.
	 * This service also checks to see if any grace period was registered for the
	 * user before sending out a failure notification via email
	 * 
	 * @param task - task which was executed
	 * @throws ServiceMonitorFacadeException - Exception thrown if there is an error
	 *                                       while processing active status
	 */
	private void processServiceStatusInactive(ServiceMonitorTask task) throws ServiceMonitorFacadeException {

		ServiceMonitorStatus status = task.getStatus();

		// check if service has moved from active status to inactive status
		if (StringUtils.isEmpty(status.getStatusCd())
				|| status.getStatusCd() == ServiceMonitorStatusCode.ACTIVE.toString()) {

			status.setStatusCd(ServiceMonitorStatusCode.INACTIVE.toString());
			status.setLastFailedTime(LocalDateTime.now());

		}

		// check if there is a grace period set and send the notification accordingly
		if (status.getLastFailedTime().until(LocalDateTime.now(), ChronoUnit.SECONDS) >= task.getGraceTime()) {

			// TODO: If the grace time is less than the polling frequency, the monitor
			// should schedule extra checks of the service.

			// send email notification
			emailFacade.sendSimpleMessage(task.getEmail(), "Serivice Monitor Status INACTIVE:" + new Date(),
					"Service is currently not active at host: " + status.getHostName() + " and port :"
							+ status.getPort());
		}

		LOG.info("Service is currently Inactive | Host Name: {}, Port Number : {}, taskId : {}", status.getHostName(),
				status.getPort(), task.getId());
	}

	/**
	 * Private method to process the task when the service monitored is active. This
	 * method check if the service came back to active from an inactive status and
	 * send an email notification. if the service continues to be in active status
	 * no email will be sent
	 * 
	 * @param task - Task which was executed
	 * @throws ServiceMonitorFacadeException - Exception thrown if there is an error
	 *                                       while processing active status
	 */
	private void processServiceStatusActive(ServiceMonitorTask task) throws ServiceMonitorFacadeException {

		ServiceMonitorStatus status = task.getStatus();

		// check if the service came back to active status from inactive status
		if (StringUtils.isEmpty(status.getStatusCd())
				|| status.getStatusCd() == ServiceMonitorStatusCode.INACTIVE.toString()) {

			status.setStatusCd(ServiceMonitorStatusCode.ACTIVE.toString());
			status.setLastFailedTime(null);

			// send email notification
			emailFacade.sendSimpleMessage(task.getEmail(), "Serivice Monitor Status ACTIVE:" + new Date(),
					"Service is currently in active status at host: " + status.getHostName() + " and port :"
							+ status.getPort());
		}

		LOG.info("Service is currently Active | Host Name: {}, Port Number : {}, taskId : {}", status.getHostName(),
				status.getPort(), task.getId());
	}

	/**
	 * Private method to detect multiple callers registering interest in the same
	 * service, and not poll any service more frequently than once a second.
	 * 
	 * @param status - Status containing the details of Hostname and Port number
	 * @return - Return true if a duplicate service got executed within a second ,
	 *         or else false.
	 */
	private boolean isduplicateServiceProcessed(ServiceMonitorStatus status) {

		// retrieve the list of existing tasks from the database for same port number
		// and host name
		List<ServiceMonitorTask> existingTasks = taskRepository.findByHostNameAndPort(status.getHostName(),
				status.getPort());

		// check if any of these duplicate tasks was executed in last one second
		for (ServiceMonitorTask t : existingTasks) {
			if (t.getUpdatedAt().until(LocalDateTime.now(), ChronoUnit.SECONDS) == 0) {
				return true;
			}

		}
		return false;
	}

	/**
	 * Private method to determine if an outage period is currently going on.
	 * Returns true if outage is going on or else false
	 * 
	 * @param task - Task which needs to be executed
	 * @return : Returns true if outage is going on or else false
	 */
	private boolean isOutagePeriod(ServiceMonitorTask task) {
		
		if (task.getOutageFrom() != null && task.getOutageTo() != null) {
			return LocalDateTime.now().isAfter(task.getOutageFrom())
					&& LocalDateTime.now().isBefore(task.getOutageTo());
		} else {
			return false;
		}
	}

	/**
	 * Private method to retrieve the task details from the database using the input
	 * taskId
	 * 
	 * @param taskId : Id of the task
	 * @return : Task retruned from the database
	 * @throws ServiceMonitorServiceException : Exception occured while retrieving
	 *                                        the task
	 */
	private ServiceMonitorTask findByTaskId(Long taskId) throws ServiceMonitorServiceException {

		// get task from database
		Optional<ServiceMonitorTask> model = this.taskRepository.findById(taskId);

		// if no task found , return error
		if (!model.isPresent()) {
			throw new ServiceMonitorServiceException("ServiceMonitorTask not found with Id " + taskId,
					ErrorCode.SERVICE_ERROR);
		}

		ServiceMonitorTask task = model.get();
		LOG.trace("Task retrieved from database |  Task : {}", task);
		return task;
	}
}

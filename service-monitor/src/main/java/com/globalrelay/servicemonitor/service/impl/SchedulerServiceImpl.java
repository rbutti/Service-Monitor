package com.globalrelay.servicemonitor.service.impl;

import java.util.Locale;
import java.util.Optional;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.cronutils.descriptor.CronDescriptor;
import com.cronutils.model.CronType;
import com.cronutils.model.definition.CronDefinition;
import com.cronutils.model.definition.CronDefinitionBuilder;
import com.cronutils.parser.CronParser;
import com.globalrelay.servicemonitor.domain.ServiceMonitorTask;
import com.globalrelay.servicemonitor.exception.ErrorCode;
import com.globalrelay.servicemonitor.exception.ServiceMonitorServiceException;
import com.globalrelay.servicemonitor.facade.impl.QuartzSchedulerFacadeImpl;
import com.globalrelay.servicemonitor.job.impl.ServiceMonitorJob;
import com.globalrelay.servicemonitor.repository.ServiceMonitorTaskRepository;
import com.globalrelay.servicemonitor.service.SchedulerService;

/**
 * An implementation of {@linkplain SchedulerService} interface. This class
 * implements logic to create, schedule, start, stop and search for tasks that
 * needs to be scheduled using a Scheduler
 * 
 * @author Ravikiran Butti
 *
 */
@Service
public class SchedulerServiceImpl implements SchedulerService {

	private static Logger LOG = LoggerFactory.getLogger(SchedulerServiceImpl.class);

	@Resource
	private ServiceMonitorTaskRepository taskRepository;

	@Resource
	private QuartzSchedulerFacadeImpl schedulerFacade;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.globalrelay.servicemonitor.service.SchedulerService#createTask(com.
	 * globalrelay.servicemonitor.scheduler.model.ServiceMonitorTask)
	 */
	@Override
	@Transactional
	public synchronized ServiceMonitorTask createTask(ServiceMonitorTask task) throws ServiceMonitorServiceException {

		LOG.debug("Started to create and schedule task | Input task : {}", task);

		try {
			task.setJobClass(ServiceMonitorJob.class.getName());

			// persist the task to database
			ServiceMonitorTask persistedTask = this.taskRepository.save(task);

			// schedule the task in quartz scheduler
			this.schedulerFacade.createJob(persistedTask);

			LOG.debug("Successfully created and scheduled task | Input task : {}", persistedTask);
			return persistedTask;
		} catch (Exception e) {
			throw new ServiceMonitorServiceException("Failed to create and schedule the task :" + task, e,
					ErrorCode.SERVICE_ERROR);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.globalrelay.servicemonitor.service.SchedulerService#startTask(java.
	 * lang.Long)
	 */
	@Override
	public ServiceMonitorTask startTask(Long taskId) throws ServiceMonitorServiceException {

		LOG.debug("Started to start task | Task Id : {}", taskId);
		try {
			Optional<ServiceMonitorTask> model = findByTaskId(taskId);
			this.schedulerFacade.createJob(model.get());
			
			LOG.info("Successully started task | Task Id : {}", taskId);
			return model.get();
		} catch (Exception e) {
			throw new ServiceMonitorServiceException("Failed to start the task with Id :" + taskId, e,
					ErrorCode.SERVICE_ERROR);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.globalrelay.servicemonitor.service.SchedulerService#stopTask(java.
	 * lang.Long)
	 */
	@Override
	public ServiceMonitorTask stopTask(Long taskId) throws ServiceMonitorServiceException {
		
		LOG.debug("Started to stop task | Task Id : {}", taskId);
		try {
			Optional<ServiceMonitorTask> model = findByTaskId(taskId);
			this.schedulerFacade.stopJob(Long.toString(taskId));
			
			LOG.info("Successully stopped task | Task Id : {}", taskId);
			return model.get();
		} catch (Exception e) {
			throw new ServiceMonitorServiceException("Failed to stop the task with Id :" + taskId, e,
					ErrorCode.SERVICE_ERROR);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.globalrelay.servicemonitor.service.SchedulerService#deleteTask(java.
	 * lang.Long)
	 */
	@Override
	@Transactional
	public ServiceMonitorTask deleteTask(Long taskId) throws ServiceMonitorServiceException {

		LOG.debug("Started to delete task | Task Id : {}", taskId);
		try {
			Optional<ServiceMonitorTask> model = findByTaskId(taskId);
			
			//delete task in repository
			this.taskRepository.deleteById(taskId);
			
			//delete task in scheduler
			this.schedulerFacade.stopJob(Long.toString(taskId));
		
			LOG.info("Successully deleted the task | Task Id : {}", taskId);
			return model.get();
		} catch (Exception e) {
			throw new ServiceMonitorServiceException("Failed to delete the task with Id :" + taskId, e,
					ErrorCode.SERVICE_ERROR);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.globalrelay.servicemonitor.service.SchedulerService#getTask(java.lang
	 * .Long)
	 */
	@Override
	public ServiceMonitorTask getTask(Long taskId) throws ServiceMonitorServiceException {

		LOG.debug("Started to retrieve task | Task Id {}", taskId);
		try {
			Optional<ServiceMonitorTask> model = findByTaskId(taskId);
			
			LOG.debug("Successully retrieved the task | Task  {}", model.get());
			return model.get();
		} catch (Exception e) {
			throw new ServiceMonitorServiceException("Failed to get the task with Id :" + taskId, e,
					ErrorCode.SERVICE_ERROR);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.globalrelay.servicemonitor.service.SchedulerService#getTasks(org.
	 * springframework.data.domain.Pageable)
	 */
	@Override
	public Page<ServiceMonitorTask> getTasks(Pageable pageAble) throws ServiceMonitorServiceException {

		LOG.debug("Started to retrieve tasks | Criteria {}", pageAble);
		try {

			Page<ServiceMonitorTask> page = this.taskRepository.findAll(pageAble);
			LOG.trace("Data from Repository | Page {}", page);
			
			if (CollectionUtils.isEmpty(page.getContent())) {
				return page;
			}

			// get a predefined instance
			CronDefinition cronDefinition = CronDefinitionBuilder.instanceDefinitionFor(CronType.QUARTZ);

			// create a parser based on provided definition
			CronParser parser = new CronParser(cronDefinition);

			// create a descriptor for a specific Locale
			CronDescriptor descriptor = CronDescriptor.instance(Locale.UK);

			//set the Human reable Cron and Job status values
			page.getContent().stream().forEach(s -> {
				s.getStatus()
						.setCronHumanExpression(descriptor.describe(parser.parse(s.getStatus().getCronExpression())));
				try {
					s.setActive(this.schedulerFacade.getJobStatus(s.getId().toString()));
				} catch (Exception e) {
					LOG.warn("Exception occured while setting the Job status",e);
				}
			});

			LOG.debug("Successfully retrieved the tasks| Page {}", page);
			return page;

		} catch (Exception e) {
			throw new ServiceMonitorServiceException("Failed to get the tasks for criteria :" + pageAble, e,
					ErrorCode.SERVICE_ERROR);
		}
	}

	/**
	 * A private method to return task for the database using the taskId
	 * 
	 * @param taskId - Id of the task to be search
	 * @return - Optional of Found ServiceMonitorTask
	 * @throws ServiceMonitorServiceException - Exception thrown when failed to
	 *                                        search a task by Id
	 */
	private Optional<ServiceMonitorTask> findByTaskId(Long taskId) throws ServiceMonitorServiceException {

		Optional<ServiceMonitorTask> model = this.taskRepository.findById(taskId);
		if (!model.isPresent()) {
			throw new ServiceMonitorServiceException("ServiceMonitorTask not found with Id " + taskId,
					ErrorCode.SERVICE_ERROR);
		}
		return model;
	}

}

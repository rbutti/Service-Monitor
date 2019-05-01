package com.globalrelay.servicemonitor.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.globalrelay.servicemonitor.domain.ServiceMonitorResponse;
import com.globalrelay.servicemonitor.domain.ServiceMonitorTask;
import com.globalrelay.servicemonitor.service.SchedulerService;

/**
 * REST Endpoints controller for Service Monitoring Application
 * <p>
 * A service monitor application will be used to monitor the status of multiple
 * services. This class has the  endpoints for following features:
 * <ul>
 * <li>Create a Service Monitoring task
 * <li>Schedule a Service Monitoring task
 * <li>Start a Service Monitoring task
 * <li>Stop a Service Monitoring task
 * <li>Retrieve a Service Monitoring task
 * <li>Get list of Service Monitoring tasks
 * </ul>
 * 
 * @author Ravikiran Butti
 *
 */
@RestController
public class ServiceMonitorController {

	private static Logger LOG = LoggerFactory.getLogger(ServiceMonitorController.class);

	@Autowired
	private SchedulerService taskService;

	/**
	 * A resource Endpoint to creates and schedules a Service Monitoring task
	 * <p>
	 * Service details
	 * <ul>
	 * <li>Path : /create
	 * <li>HTTP Method: POST
	 * <li>Consumes : JSON
	 * <li>Produces : JSON
	 * </ul>
	 * 
	 * @param task - task that needs to be created and scheduled
	 * @return - {@linkplain ServiceMonitorResponse} object containing success or
	 *         failure
	 */
	@PostMapping(path = "create", consumes = {
			MediaType.APPLICATION_JSON_VALUE }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ServiceMonitorResponse createTask(@RequestBody ServiceMonitorTask task) {

		LOG.debug("Started to create task | task : {}", task);
		ServiceMonitorTask createdTask = null;
		try {
			createdTask = taskService.createTask(task);
		} catch (Exception e) {
			LOG.error("Failed to create a task | task : {}", task, e);
			return ServiceMonitorResponse.error("Failed to create a task", task);
		}

		LOG.info("Task created successfully | task : {}", createdTask);
		return ServiceMonitorResponse.success("ServiceMonitorTask is created and scheduled", createdTask);

	}

	/**
	 * A resource Endpoint to retrieve a Service Monitoring task
	 * <p>
	 * Service details
	 * <ul>
	 * <li>Path : /findone/{id}
	 * <li>HTTP Method: GET
	 * <li>Produces : JSON
	 * </ul>
	 * 
	 * @param id - Id of the task that needs to be retrieved
	 * @return - {@linkplain ServiceMonitorResponse} object containing success or
	 *         failure response
	 */
	@GetMapping(path = "findone/{id}")
	public ServiceMonitorResponse getTask(@PathVariable("id") Long id) {

		LOG.debug("Started to retrieve task | task Id : {}", id);
		ServiceMonitorTask task = null;
		try {
			task = taskService.getTask(id);
		} catch (Exception e) {
			LOG.error("Failed to retrieve a task | task Id : {}", id, e);
			return ServiceMonitorResponse.error("Failed to find a task with Id: " + id);
		}

		LOG.debug("Task retrieved successfully | task : {}", task);
		return ServiceMonitorResponse.success("ServiceMonitorTask found successfully", task);

	}

	/**
	 * A resource Endpoint to start a Service Monitoring task
	 * <p>
	 * Service details
	 * <ul>
	 * <li>Path : /start/{id}
	 * <li>HTTP Method: GET
	 * <li>Produces : JSON
	 * </ul>
	 * 
	 * @param id - Id of the task that needs to be started
	 * @return - {@linkplain ServiceMonitorResponse} object containing success or
	 *         failure response
	 */
	@GetMapping(path = "start/{id}")
	public ServiceMonitorResponse startTask(@PathVariable("id") Long id) {

		LOG.debug("Started to start/schedule task | task Id : {}", id);
		ServiceMonitorTask task = null;
		try {
			task = taskService.startTask(id);
		} catch (Exception e) {
			LOG.error("Failed to start a task | task Id : {}", id, e);
			return ServiceMonitorResponse.error("Failed to start a task with Id: " + id);
		}
		LOG.info("Task started successfully | task : {}", task);
		return ServiceMonitorResponse.success("ServiceMonitorTask started successfully", task);
	}

	/**
	 * A resource Endpoint to stop a Service Monitoring task
	 * <p>
	 * Service details
	 * <ul>
	 * <li>Path : /stop/{id}
	 * <li>HTTP Method: GET
	 * <li>Produces : JSON
	 * </ul>
	 * 
	 * @param id - Id of the task that needs to be stopped
	 * @return - {@linkplain ServiceMonitorResponse} object containing success or
	 *         failure response
	 */
	@GetMapping(path = "stop/{id}")
	public ServiceMonitorResponse stopTask(@PathVariable("id") Long id) {

		LOG.debug("Started to stop task | task Id : {}", id);
		ServiceMonitorTask task = null;
		try {
			task = taskService.stopTask(id);
		} catch (Exception e) {
			LOG.error("Failed to stop a task | task Id : {}", id, e);
			return ServiceMonitorResponse.error("Failed to stop a task with Id: " + id);
		}

		LOG.info("Task stopped successfully | task : {}", task);
		return ServiceMonitorResponse.success("ServiceMonitorTask stopped successfully", task);
	}

	/**
	 * A resource Endpoint to delete a Service Monitoring task
	 * <p>
	 * Service details
	 * <ul>
	 * <li>Path : /delete/{id}
	 * <li>HTTP Method: GET
	 * <li>Produces : JSON
	 * </ul>
	 * 
	 * @param id - Id of the task that needs to be deleted
	 * @return - {@linkplain ServiceMonitorResponse} object containing success or
	 *         failure response
	 */
	@GetMapping(path = "delete/{id}")
	public ServiceMonitorResponse deleteTask(@PathVariable("id") Long id) {

		LOG.debug("Started to delete task | task Id : {}", id);
		ServiceMonitorTask task = null;
		try {
			task = taskService.deleteTask(id);
		} catch (Exception e) {
			LOG.error("Failed to delete a task | task Id : {}", id, e);
			return ServiceMonitorResponse.error("Failed to delete a task with Id: " + id);
		}

		LOG.info("Task deleted successfully | task : {}", task);
		return ServiceMonitorResponse.success("ServiceMonitorTask deleted successfully", task);
	}

	/**
	 * A resource Endpoint to list all the Service Monitoring tasks
	 * <p>
	 * Service details
	 * <ul>
	 * <li>Path : /list
	 * <li>HTTP Method: POST
	 * <li>Consumes : JSON
	 * <li>Produces : JSON
	 * </ul>
	 * 
	 * @param param - Key value pair of the search criteria
	 * @return - {@linkplain ServiceMonitorResponse} object containing success or
	 *         failure response
	 */
	@PostMapping(path = "list", consumes = {
			MediaType.APPLICATION_JSON_VALUE }, produces = MediaType.APPLICATION_JSON_VALUE)
	@SuppressWarnings("deprecation")
	public ServiceMonitorResponse getTasks(@RequestBody HashMap<String, Object> param) {

		LOG.debug("Started to retrieve tasks | search criteria : {}", param);
		Map<String, Object> resp = new HashMap<String, Object>();
		try {

			// Set page limit and offsets
			Pageable pageAble = new PageRequest(Integer.valueOf(param.get("offset").toString()),
					Integer.valueOf(param.get("limit").toString()));

			// retrieve the records
			Page<ServiceMonitorTask> page = taskService.getTasks(pageAble);

			// return current page number and total number of records
			resp.put("total", page.getTotalPages());
			resp.put("rows", page.getContent());
		} catch (Exception e) {
			LOG.error("Failed to retrieve tasks | search criteria : {}", param, e);
			return ServiceMonitorResponse.error("Failed to find tasks with criteria: " + param);
		}

		LOG.debug("Tasks retrieved successfully | response : {}", resp);
		return ServiceMonitorResponse.success("data found", resp);

	}
}

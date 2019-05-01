package com.globalrelay.servicemonitor.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.CollectionUtils;

import com.globalrelay.servicemonitor.domain.ServiceMonitorStatus;
import com.globalrelay.servicemonitor.domain.ServiceMonitorTask;
import com.globalrelay.servicemonitor.exception.ServiceMonitorFacadeException;
import com.globalrelay.servicemonitor.facade.impl.QuartzSchedulerFacadeImpl;
import com.globalrelay.servicemonitor.repository.ServiceMonitorTaskRepository;

@RunWith(SpringRunner.class)
public class SchedulerServiceImplTest {

	@Mock
	private ServiceMonitorTaskRepository taskRepository;

	@Mock
	private QuartzSchedulerFacadeImpl schedulerFacade;

	@InjectMocks
	SchedulerServiceImpl service;

	@Test
	public void testCreateTask() throws Exception {

		ServiceMonitorTask task = new ServiceMonitorTask();
		task.setId(1l);
		Mockito.when(taskRepository.save(task)).thenReturn(task);
		Mockito.doNothing().when(schedulerFacade).createJob(task);

		ServiceMonitorTask result = service.createTask(task);
		assertEquals(result.getId(), task.getId());

	}

	@Test(expected = Exception.class)
	public void testCreateTask_exception() throws Exception {

		ServiceMonitorTask task = new ServiceMonitorTask();
		task.setId(1l);
		Mockito.when(taskRepository.save(task)).thenThrow(ServiceMonitorFacadeException.class);

		service.createTask(task);

	}

	@Test
	public void testStartTask() throws Exception {

		ServiceMonitorTask task = new ServiceMonitorTask();
		task.setId(1l);
		Mockito.when(taskRepository.findById(1l)).thenReturn(Optional.of(task));
		Mockito.doNothing().when(schedulerFacade).createJob(task);

		ServiceMonitorTask result = service.startTask(1l);
		assertEquals(result.getId(), task.getId());

	}

	@Test(expected = Exception.class)
	public void testStartTask_exception() throws Exception {

		ServiceMonitorTask task = new ServiceMonitorTask();
		task.setId(1l);
		Mockito.when(taskRepository.findById(1l)).thenThrow(ServiceMonitorFacadeException.class);

		service.startTask(1l);

	}

	@Test
	public void testStopTask() throws Exception {

		ServiceMonitorTask task = new ServiceMonitorTask();
		task.setId(1l);
		Mockito.when(taskRepository.findById(1l)).thenReturn(Optional.of(task));
		Mockito.doNothing().when(schedulerFacade).stopJob("1");

		ServiceMonitorTask result = service.stopTask(1l);
		assertEquals(result.getId(), task.getId());

	}

	@Test(expected = Exception.class)
	public void testStopTask_exception() throws Exception {

		ServiceMonitorTask task = new ServiceMonitorTask();
		task.setId(1l);
		Mockito.when(taskRepository.findById(1l)).thenThrow(ServiceMonitorFacadeException.class);

		service.stopTask(1l);

	}

	@Test
	public void testDeleteTask() throws Exception {

		ServiceMonitorTask task = new ServiceMonitorTask();
		task.setId(1l);
		Mockito.when(taskRepository.findById(1l)).thenReturn(Optional.of(task));
		Mockito.doNothing().when(schedulerFacade).stopJob("1");
		Mockito.doNothing().when(taskRepository).deleteById(1l);

		ServiceMonitorTask result = service.deleteTask(1l);
		assertEquals(result.getId(), task.getId());
	}

	@Test(expected = Exception.class)
	public void testDeleteTask_exception() throws Exception {

		ServiceMonitorTask task = new ServiceMonitorTask();
		task.setId(1l);
		Mockito.when(taskRepository.findById(1l)).thenThrow(ServiceMonitorFacadeException.class);

		service.deleteTask(1l);

	}

	@Test
	public void testGetTask() throws Exception {

		ServiceMonitorTask task = new ServiceMonitorTask();
		task.setId(1l);
		Mockito.when(taskRepository.findById(1l)).thenReturn(Optional.of(task));

		ServiceMonitorTask result = service.getTask(1l);
		assertEquals(result.getId(), task.getId());
	}

	@Test(expected = Exception.class)
	public void testGetTask_exception() throws Exception {

		ServiceMonitorTask task = new ServiceMonitorTask();
		task.setId(1l);
		Mockito.when(taskRepository.findById(1l)).thenThrow(ServiceMonitorFacadeException.class);

		service.getTask(1l);

	}

	@Test
	public void testGetTasks() throws Exception {

		@SuppressWarnings("deprecation")
		Pageable pageable = new PageRequest(1, 100);
		ServiceMonitorTask task = new ServiceMonitorTask();
		task.setId(1l);
		task.setJobClass("abd");

		ServiceMonitorStatus status = new ServiceMonitorStatus();
		status.setCronExpression("0 0/1 * 1/1 * ? *");
		task.setStatus(status);

		List<ServiceMonitorTask> list = new ArrayList<ServiceMonitorTask>();
		list.add(task);
		Page<ServiceMonitorTask> page = new PageImpl<ServiceMonitorTask>(list);

		Mockito.when(taskRepository.findAll(pageable)).thenReturn(page);

		assertFalse(CollectionUtils.isEmpty(service.getTasks(pageable).getContent()));

	}

	@Test(expected = Exception.class)
	public void testGetTasks_exception() throws Exception {

		@SuppressWarnings("deprecation")
		Pageable pageable = new PageRequest(1, 100);
		ServiceMonitorTask task = new ServiceMonitorTask();
		task.setId(1l);
		task.setJobClass("abd");

		ServiceMonitorStatus status = new ServiceMonitorStatus();
		status.setCronExpression("0 0/1 * 1/1 * ? *");
		task.setStatus(status);

		List<ServiceMonitorTask> list = new ArrayList<ServiceMonitorTask>();
		list.add(task);
		
		Mockito.when(taskRepository.findAll(pageable)).thenThrow(ServiceMonitorFacadeException.class);

		service.getTasks(pageable);

	}

}

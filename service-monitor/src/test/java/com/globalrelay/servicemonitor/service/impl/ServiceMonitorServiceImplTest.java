package com.globalrelay.servicemonitor.service.impl;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringRunner;

import com.globalrelay.servicemonitor.constant.ServiceMonitorStatusCode;
import com.globalrelay.servicemonitor.domain.ServiceMonitorStatus;
import com.globalrelay.servicemonitor.domain.ServiceMonitorTask;
import com.globalrelay.servicemonitor.facade.EmailFacade;
import com.globalrelay.servicemonitor.repository.ServiceMonitorTaskRepository;
import com.globalrelay.servicemonitor.strategy.ServiceMonitorStrategy;

@RunWith(SpringRunner.class)
public class ServiceMonitorServiceImplTest {

	@Mock
	private ServiceMonitorTaskRepository taskRepository;

	@Mock
	private EmailFacade emailFacade;

	@Mock
	private ServiceMonitorStrategy serviceMonitorStrategy;

	@InjectMocks
	ServiceMonitorServiceImpl service;

	ServiceMonitorTask task;
	ServiceMonitorStatus status;
	List<ServiceMonitorTask> existingTasks = new ArrayList<ServiceMonitorTask>();

	@Before
	public void setUp() {

		task = new ServiceMonitorTask();
		task.setId(1l);
		task.setJobClass("abd");
		task.setOutageFrom(LocalDateTime.now().plusYears(1));
		task.setOutageTo(LocalDateTime.now().plusYears(1));

		status = new ServiceMonitorStatus();
		status.setCronExpression("0 0/1 * 1/1 * ? *");
		status.setHostName("localhost");
		status.setPort(12);

		task.setStatus(status);

		ServiceMonitorTask existingTask = new ServiceMonitorTask();
		existingTask.setId(2l);
		existingTask.setUpdatedAt(LocalDateTime.now().minusYears(1));

		existingTasks.add(existingTask);
	}

	@Test
	public void testMonitorStatus_Active() throws Exception {

		status.setStatusCd(ServiceMonitorStatusCode.INACTIVE.toString());
		Mockito.when(taskRepository.findById(1l)).thenReturn(Optional.of(task));
		Mockito.when(taskRepository.findByHostNameAndPort(status.getHostName(), status.getPort()))
				.thenReturn(existingTasks);
		Mockito.when(serviceMonitorStrategy.monitorService(status.getHostName(), status.getPort())).thenReturn(true);
		Mockito.when(taskRepository.save(task)).thenReturn(task);
		Mockito.doNothing().when(emailFacade).sendSimpleMessage(Mockito.anyString(), Mockito.anyString(),
				Mockito.anyString());

		ServiceMonitorStatus result = service.monitorStatus(1l);
		assertEquals(result.getHostName(), status.getHostName());
		assertEquals(result.getPort(), status.getPort());
	}

	@Test
	public void testMonitorStatus_Inactive() throws Exception {

		status.setStatusCd(ServiceMonitorStatusCode.ACTIVE.toString());
		Mockito.when(taskRepository.findById(1l)).thenReturn(Optional.of(task));
		Mockito.when(taskRepository.findByHostNameAndPort(status.getHostName(), status.getPort()))
				.thenReturn(existingTasks);
		Mockito.when(serviceMonitorStrategy.monitorService(status.getHostName(), status.getPort())).thenReturn(false);
		Mockito.when(taskRepository.save(task)).thenReturn(task);
		Mockito.doNothing().when(emailFacade).sendSimpleMessage(Mockito.anyString(), Mockito.anyString(),
				Mockito.anyString());

		ServiceMonitorStatus result = service.monitorStatus(1l);
		assertEquals(result.getHostName(), status.getHostName());
		assertEquals(result.getPort(), status.getPort());

	}

	@Test
	public void testMonitorStatus_Outage() throws Exception {
	
		task.setOutageFrom(LocalDateTime.now());
		task.setOutageTo(LocalDateTime.now().plusYears(1));
		
		Mockito.when(taskRepository.findById(1l)).thenReturn(Optional.of(task));
		Mockito.when(taskRepository.findByHostNameAndPort(status.getHostName(), status.getPort()))
				.thenReturn(existingTasks);
		Mockito.when(serviceMonitorStrategy.monitorService(status.getHostName(), status.getPort())).thenReturn(false);
		Mockito.when(taskRepository.save(task)).thenReturn(task);
		Mockito.doNothing().when(emailFacade).sendSimpleMessage(Mockito.anyString(), Mockito.anyString(),
				Mockito.anyString());

		ServiceMonitorStatus result = service.monitorStatus(1l);
		assertEquals(result.getHostName(), status.getHostName());
		assertEquals(result.getPort(), status.getPort());

	}
	
	@Test
	public void testMonitorStatus_DuplicateProcessed() throws Exception {
	
		existingTasks.get(0).setUpdatedAt(LocalDateTime.now());
	
		Mockito.when(taskRepository.findById(1l)).thenReturn(Optional.of(task));
		Mockito.when(taskRepository.findByHostNameAndPort(status.getHostName(), status.getPort()))
				.thenReturn(existingTasks);
		Mockito.when(serviceMonitorStrategy.monitorService(status.getHostName(), status.getPort())).thenReturn(false);
		Mockito.when(taskRepository.save(task)).thenReturn(task);
		Mockito.doNothing().when(emailFacade).sendSimpleMessage(Mockito.anyString(), Mockito.anyString(),
				Mockito.anyString());

		ServiceMonitorStatus result = service.monitorStatus(1l);
		assertEquals(result.getHostName(), status.getHostName());
		assertEquals(result.getPort(), status.getPort());

	}
	

	@Test(expected = Exception.class)
	public void testMonitorStatus_Exception() throws Exception {
	
		Mockito.when(taskRepository.findById(1l)).thenReturn(Optional.empty());
		Mockito.when(taskRepository.findByHostNameAndPort(status.getHostName(), status.getPort()))
				.thenReturn(existingTasks);
		Mockito.when(serviceMonitorStrategy.monitorService(status.getHostName(), status.getPort())).thenReturn(false);
		Mockito.when(taskRepository.save(task)).thenReturn(task);
		Mockito.doNothing().when(emailFacade).sendSimpleMessage(Mockito.anyString(), Mockito.anyString(),
				Mockito.anyString());

		ServiceMonitorStatus result = service.monitorStatus(1l);
		assertEquals(result.getHostName(), status.getHostName());
		assertEquals(result.getPort(), status.getPort());

	}
}

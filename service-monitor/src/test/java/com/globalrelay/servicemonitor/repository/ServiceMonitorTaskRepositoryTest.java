package com.globalrelay.servicemonitor.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.CollectionUtils;

import com.globalrelay.servicemonitor.domain.ServiceMonitorStatus;
import com.globalrelay.servicemonitor.domain.ServiceMonitorTask;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ServiceMonitorTaskRepositoryTest {

	@Autowired
	private ServiceMonitorTaskRepository repository;

	@Test
	public void testSaveServiceMonitorTask() {

		ServiceMonitorTask task = new ServiceMonitorTask();
		task.setName("localhost");
		task = repository.save(task);
		ServiceMonitorTask task2 = repository.findById(task.getId()).get();
		assertNotNull(task2);
		assertEquals(task2.getName(), task.getName());

	}

	@Test
	public void testDeleteServiceMonitorTask() {
		ServiceMonitorTask task = new ServiceMonitorTask();
		task.setName("localhost");
		task = repository.save(task);
		ServiceMonitorTask task2 = repository.findById(task.getId()).get();
		assertNotNull(task2);
		assertEquals(task2.getName(), task.getName());
		repository.delete(task);
		Optional<ServiceMonitorTask> task3 = repository.findById(task.getId());
		assertFalse(task3.isPresent());

	}

	@Test
	public void testFindByHostNameAndPort() {

		String hostName = "localhost";
		int port = 23;

		ServiceMonitorTask task = new ServiceMonitorTask();
		task.setName(hostName);

		ServiceMonitorStatus status = new ServiceMonitorStatus();
		status.setHostName(hostName);
		status.setPort(23);

		task.setStatus(status);
		task = repository.save(task);

		List<ServiceMonitorTask> task2 = repository.findByHostNameAndPort(hostName, port);
		assertFalse(CollectionUtils.isEmpty(task2));
		assertEquals(task2.get(0).getName(), task.getName());
	}

}

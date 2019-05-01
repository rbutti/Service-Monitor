package com.globalrelay.servicemonitor.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.globalrelay.servicemonitor.domain.ServiceMonitorTask;
import com.globalrelay.servicemonitor.exception.ServiceMonitorServiceException;
import com.globalrelay.servicemonitor.service.SchedulerService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class ServiceMonitorControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private SchedulerService taskService;

	@Test
	public void testCreateTask() throws Exception {

		ServiceMonitorTask task = new ServiceMonitorTask();
		ObjectMapper mapper = new ObjectMapper();

		// mock response
		Mockito.when(taskService.createTask(task)).thenReturn(task);

		// make service call
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/create").accept(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(task)).contentType(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();

		assertEquals(HttpStatus.OK.value(), response.getStatus());
		assertTrue(response.getContentAsString().contains("ServiceMonitorTask is created and scheduled"));

	}
	
	@Test
	public void testCreateTask_Exception() throws Exception {

		ServiceMonitorTask task = new ServiceMonitorTask();
		ObjectMapper mapper = new ObjectMapper();

		// mock response
		Mockito.when(taskService.createTask(task)).thenThrow(ServiceMonitorServiceException.class);

		// make service call
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/create").accept(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(task)).contentType(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();

		assertEquals(HttpStatus.OK.value(), response.getStatus());
		assertTrue(response.getContentAsString().contains("Failed to create a task"));

	}
	
	
	@Test
	public void testGetTask() throws Exception {

		Long id = 123l;
		ServiceMonitorTask task = new ServiceMonitorTask();
		Mockito.when(
				taskService.getTask(id)).thenReturn(task);

		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
				"/findone/"+id).accept(
				MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		MockHttpServletResponse response = result.getResponse();

		assertEquals(HttpStatus.OK.value(), response.getStatus());
		assertTrue(response.getContentAsString().contains("ServiceMonitorTask found successfully"));
	}
	
	@Test
	public void testGetTask_Exception() throws Exception {

		Long id = 123l;
		Mockito.when(
				taskService.getTask(id)).thenThrow(ServiceMonitorServiceException.class);

		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
				"/findone/"+id).accept(
				MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		MockHttpServletResponse response = result.getResponse();

		assertEquals(HttpStatus.OK.value(), response.getStatus());
		assertTrue(response.getContentAsString().contains("Failed to find a task with Id"));
	}
	

	@Test
	public void testStartTask() throws Exception {

		Long id = 123l;
		ServiceMonitorTask task = new ServiceMonitorTask();
		Mockito.when(
				taskService.startTask(id)).thenReturn(task);

		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
				"/start/"+id).accept(
				MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		MockHttpServletResponse response = result.getResponse();

		assertEquals(HttpStatus.OK.value(), response.getStatus());
		assertTrue(response.getContentAsString().contains("ServiceMonitorTask started successfully"));
	}
	
	@Test
	public void testStartTask_Exception() throws Exception {

		Long id = 123l;

		Mockito.when(
				taskService.startTask(id)).thenThrow(ServiceMonitorServiceException.class);

		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
				"/start/"+id).accept(
				MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		MockHttpServletResponse response = result.getResponse();

		assertEquals(HttpStatus.OK.value(), response.getStatus());
		assertTrue(response.getContentAsString().contains("Failed to start a task"));
	}
	
	@Test
	public void testStopTask() throws Exception {

		Long id = 123l;
		ServiceMonitorTask task = new ServiceMonitorTask();
		Mockito.when(
				taskService.stopTask(id)).thenReturn(task);

		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
				"/stop/"+id).accept(
				MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		MockHttpServletResponse response = result.getResponse();

		assertEquals(HttpStatus.OK.value(), response.getStatus());
		assertTrue(response.getContentAsString().contains("ServiceMonitorTask stopped successfully"));
	}
	
	@Test
	public void testStopTask_Exception() throws Exception {

		Long id = 123l;

		Mockito.when(
				taskService.stopTask(id)).thenThrow(ServiceMonitorServiceException.class);

		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
				"/stop/"+id).accept(
				MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		MockHttpServletResponse response = result.getResponse();

		assertEquals(HttpStatus.OK.value(), response.getStatus());
		assertTrue(response.getContentAsString().contains("Failed to stop a task"));
	}
	
	
	@Test
	public void testDeleteTask() throws Exception {

		Long id = 123l;
		ServiceMonitorTask task = new ServiceMonitorTask();
		Mockito.when(
				taskService.deleteTask(id)).thenReturn(task);

		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
				"/delete/"+id).accept(
				MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		MockHttpServletResponse response = result.getResponse();

		assertEquals(HttpStatus.OK.value(), response.getStatus());
		assertTrue(response.getContentAsString().contains("ServiceMonitorTask deleted successfully"));
	}
	
	@Test
	public void testDeleteTask_Exception() throws Exception {

		Long id = 123l;

		Mockito.when(
				taskService.deleteTask(id)).thenThrow(ServiceMonitorServiceException.class);

		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
				"/delete/"+id).accept(
				MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		MockHttpServletResponse response = result.getResponse();

		assertEquals(HttpStatus.OK.value(), response.getStatus());
		assertTrue(response.getContentAsString().contains("Failed to delete a task"));
	}
	
	@Test
	public void testGetTasks() throws Exception {

		HashMap<String, Object> param = new HashMap<>();
		param.put("offset", "0");
		param.put("limit", "500");
		Page<ServiceMonitorTask> page = Page.empty();
		ObjectMapper mapper = new ObjectMapper();

		// mock response
		Mockito.when(taskService.getTasks(Mockito.any(Pageable.class))).thenReturn(page);

		// make service call
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/list").accept(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(param)).contentType(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();

		assertEquals(HttpStatus.OK.value(), response.getStatus());
		System.out.println(response.getContentAsString());
		assertTrue(response.getContentAsString().contains("data found"));

	}
	
	@Test
	public void testGetTasks_Exception() throws Exception {

		HashMap<String, Object> param = new HashMap<>();
		param.put("offset", "0");
		param.put("limit", "500");
		ObjectMapper mapper = new ObjectMapper();

		// mock response
		Mockito.when(taskService.getTasks(Mockito.any(Pageable.class))).thenThrow(ServiceMonitorServiceException.class);

		// make service call
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/list").accept(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(param)).contentType(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();

		assertEquals(HttpStatus.OK.value(), response.getStatus());
		System.out.println(response.getContentAsString());
		assertTrue(response.getContentAsString().contains("Failed to find tasks with criteria"));

	}
	
}

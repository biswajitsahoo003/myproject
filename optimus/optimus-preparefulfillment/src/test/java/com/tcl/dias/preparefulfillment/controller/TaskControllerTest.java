package com.tcl.dias.preparefulfillment.controller;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.flowable.engine.RuntimeService;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.beans.UserGroupBeans;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.preparefulfillment.objectcreator.ObjectCreator;
import com.tcl.dias.servicefulfillment.entity.repository.ProcessRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ProcessTaskLogRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceDetailRepository;
import com.tcl.dias.servicefulfillment.entity.repository.TaskAssignmentRepository;
import com.tcl.dias.servicefulfillment.entity.repository.TaskDataRepository;
import com.tcl.dias.servicefulfillment.entity.repository.TaskRepository;
import com.tcl.dias.servicefulfillmentutils.beans.AssignedGroupBean;
import com.tcl.dias.servicefulfillmentutils.beans.AssignedGroupingBean;
import com.tcl.dias.servicefulfillmentutils.beans.AssigneeResponse;
import com.tcl.dias.servicefulfillmentutils.beans.ProcessTaskLogBean;
import com.tcl.dias.servicefulfillmentutils.beans.TaskBean;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * This file contains the TaskControllerTest.java class.
 * 
 *
 * @author Mayank S
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TaskControllerTest {
	
	@Autowired
	private TaskController taskController;
	
	@MockBean
	TaskAssignmentRepository taskAssignmentRepository;
	
	@MockBean
	TaskRepository taskRepository;
	
	@MockBean
	TaskDataRepository taskDataRepository;
	
	@MockBean
	ProcessTaskLogRepository processTaskLogRepository;
	
	@MockBean
	ProcessRepository processRepository;
	
	@MockBean
	ScServiceDetailRepository scServiceDetailRepository;
	
	@MockBean
	RuntimeService runtimeService;
	
	@MockBean
	org.flowable.engine.TaskService flowableTaskService;

	@Autowired
	private ObjectCreator objectCreator;
	
	@MockBean
	MQUtils mqUtils;
	
	@Before
	public void init() {
		Mockito.when(taskRepository.findById(Mockito.anyInt()))
			.thenReturn(Optional.of(objectCreator.createTask()));
		Mockito.when(taskRepository.save(Mockito.any()))
			.thenReturn(objectCreator.createTask());
		Mockito.when(taskDataRepository.save(Mockito.any()))
		.thenReturn(objectCreator.createTaskData());
		Mockito.when(taskAssignmentRepository.save(Mockito.any()))
			.thenReturn(objectCreator.createTaskAssignment());
		Mockito.when(processTaskLogRepository.save(Mockito.any()))
			.thenReturn(objectCreator.createProcessTaskLog());
		Mockito.when(processRepository.findById(Mockito.any()))
			.thenReturn(Optional.of(objectCreator.createProcess()));
		Mockito.when(runtimeService.getVariables(Mockito.anyString()))
			.thenReturn(objectCreator.createMap());
		Mockito.when(scServiceDetailRepository.findById(Mockito.anyInt()))
		 	.thenReturn(Optional.of(objectCreator.createServiceDetails()));
	}
	
	/**
	 * Positive Test Case for getting Latest Activity
	 * 
	 * @throws TclCommonException
	 */
	
	/*@Test 
	public void testGetLatestActivity() throws TclCommonException {
		Mockito.when(processTaskLogRepository.findTop10ByGroupFromOrderByCreatedTime(Mockito.anyString()))
			.thenReturn(objectCreator.createProcessTaskLogList());
		ResponseResource<List<ProcessTaskLogBean>> response = taskController.getLatestActivity("OSP");
		assertTrue(response!=null && response.getData()!=null && response.getStatus()==Status.SUCCESS);
	}
	
	/**
	 * Negative Test Case for Null Group Input
	 * 
	 * @throws TclCommonException
	 */
	
	@Test
	public void testGetLatestActivityforNull() throws TclCommonException {
		ResponseResource<List<ProcessTaskLogBean>> response = taskController.getLatestActivity(null,null,null,null,null,null, null);
		assertTrue(response.getData().isEmpty());
	}
	
	/**
	 * Positive test case for getting Task Count
	 * 
	 * @throws TclCommonException
	 */
	
	/*@Test
	public void testGetTaskCount() throws TclCommonException {
		ResponseResource<List<AssignedGroupBean>> response = taskController.getTaskCount("OSP",null,null);
		assertTrue(response!=null && response.getData()!=null && response.getStatus()==Status.SUCCESS);
	}
	
	@Test
	public void testGetUserTaskCount() throws TclCommonException {
		ResponseResource<List<AssignedGroupBean>> response = taskController.getTaskCount(null,"scm_ml_user@legomail.com",null);
		assertTrue(response!=null && response.getData()!=null && response.getStatus()==Status.SUCCESS);
	}*/
	
	/**
	 * Negative test case for getting task count 
	 * 
	 * @throws TclCommonException
	 */
	
	/*@Test
	public void testGetTaskCountForNull() throws TclCommonException {
		ResponseResource<List<AssignedGroupBean>> response = taskController.getTaskCount(null,null,null);
		assertTrue(response.getData().isEmpty());
	}*/
	
	/**
	 * Positive Test Case for getting Task details
	 * 
	 * @throws TclCommonException
	 */
	
/*	@Test
	public void testGetTaskDetails() throws TclCommonException {
		ResponseResource<List<AssignedGroupingBean>> response = taskController.getTaskDetails("SCM_M_L",null,835,"taskname",new ArrayList<>());
		assertTrue(response!=null && response.getData()!=null && response.getStatus()==Status.SUCCESS);
	}
	
	@Test
	public void testGetTaskDetails2() throws TclCommonException {
		ResponseResource<List<AssignedGroupingBean>> response = taskController.getTaskDetails("OSP","test",1,"complete",new ArrayList<>());
		assertTrue(response!=null && response.getData()!=null && response.getStatus()==Status.SUCCESS);
	}*/
	
	/**
	 * Positive Test Case for assigning Task
	 * 
	 * @throws TclCommonException
	 */
	
	@Test
	public void testAssignTask() throws TclCommonException {
		ResponseResource<AssigneeResponse> response = taskController.assigneTask(objectCreator.createAssigneeRequest());
		assertTrue(response!=null && response.getData()!=null && response.getStatus()==Status.SUCCESS);
	}
	
	@Test
	public void testAssignTask2() throws TclCommonException {
		ResponseResource<AssigneeResponse> response = taskController.assigneTask(objectCreator.createAssigneeRequest2());
		assertTrue(response!=null && response.getData()!=null && response.getStatus()==Status.SUCCESS);
	}
	
	/**
	 * Positive Test Case for getting task based on task id
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testGetTaskBasedOnTaskId() throws TclCommonException {
		ResponseResource<TaskBean> response = taskController.getTaskBasedOnTaskId(1, null);
		assertTrue(response!=null && response.getData()!=null && response.getStatus()==Status.SUCCESS);
	}
	
	@Test
	public void testGetTaskBasedOnTaskId2() throws TclCommonException {
		Mockito.when(taskRepository.findById(Mockito.anyInt()))
			.thenReturn(Optional.of(objectCreator.createTaskWithKey("advanced_enrichment")));
		ResponseResource<TaskBean> response = taskController.getTaskBasedOnTaskId(2, null);
		assertTrue(response!=null && response.getData()!=null && response.getStatus()==Status.SUCCESS);
	}
	
	@Test
	public void testGetTaskBasedOnTaskId3() throws TclCommonException {
		Mockito.when(taskRepository.findById(Mockito.anyInt()))
			.thenReturn(Optional.of(objectCreator.createTaskWithKey("validate_supporting_document")));
		ResponseResource<TaskBean> response = taskController.getTaskBasedOnTaskId(2, null);
		assertTrue(response!=null && response.getData()!=null && response.getStatus()==Status.SUCCESS);
	}
	
	@Test
	public void testGetTaskBasedOnTaskId4() throws TclCommonException {
		Mockito.when(taskRepository.findById(Mockito.anyInt()))
			.thenReturn(Optional.of(objectCreator.createTaskWithKey("upload_supporting_document")));
		ResponseResource<TaskBean> response = taskController.getTaskBasedOnTaskId(2, null);
		assertTrue(response!=null && response.getData()!=null && response.getStatus()==Status.SUCCESS);
	}
	
	/**
	 * Positive test case for getting user details
	 * 
	 * testGetUserDetails
	 * @throws TclCommonException
	 */
	
	@Test
	public void testGetUserDetails() throws TclCommonException {
		System.out.println(objectCreator.createUserGroupBeans());
		Mockito.when(mqUtils.sendAndReceive(Mockito.any(),Mockito.any(), Mockito.any()))
			.thenReturn(objectCreator.createUserGroupBeans());
		ResponseResource<UserGroupBeans> response = taskController.getUserDetails("regus");
		assertTrue(response!=null && response.getData()!=null && response.getStatus()==Status.SUCCESS);
	}
}



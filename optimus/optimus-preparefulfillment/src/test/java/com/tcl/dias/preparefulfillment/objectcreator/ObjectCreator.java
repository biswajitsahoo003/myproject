package com.tcl.dias.preparefulfillment.objectcreator;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.tcl.dias.servicefulfillment.entity.entities.Activity;
import com.tcl.dias.servicefulfillment.entity.entities.Appointment;
import com.tcl.dias.servicefulfillment.entity.entities.FieldEngineer;
import com.tcl.dias.servicefulfillment.entity.entities.MstStatus;
import com.tcl.dias.servicefulfillment.entity.entities.MstTaskDef;
import com.tcl.dias.servicefulfillment.entity.entities.MstVendor;
import com.tcl.dias.servicefulfillment.entity.entities.Process;
import com.tcl.dias.servicefulfillment.entity.entities.ProcessTaskLog;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceDetail;
import com.tcl.dias.servicefulfillment.entity.entities.Stage;
import com.tcl.dias.servicefulfillment.entity.entities.Task;
import com.tcl.dias.servicefulfillment.entity.entities.TaskAssignment;
import com.tcl.dias.servicefulfillment.entity.entities.TaskData;
import com.tcl.dias.servicefulfillment.entity.entities.Vendors;
import com.tcl.dias.servicefulfillmentutils.beans.AssigneeRequest;
import com.tcl.dias.servicefulfillmentutils.beans.AttachmentIdBean;
import com.tcl.dias.servicefulfillmentutils.beans.CustomerAppointmentBean;
import com.tcl.dias.servicefulfillmentutils.beans.FieldEngineerDetailsBean;
import com.tcl.dias.servicefulfillmentutils.beans.MuxDetailBean;
import com.tcl.dias.servicefulfillmentutils.beans.Response;

/**
 * This file contains the ObjectCreator.java class.
 * It consists of the Test case values
 * 
 *
 * @author Mayank S
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Service
public class ObjectCreator {
	
	public Stage createStage() {
		Stage stage = new Stage();
		stage.setId(1);
		stage.setOrderCode("IAS150818XSVMKED");
		stage.setCompletedTime(new Timestamp(new Date().getTime()));
		stage.setCreatedTime(new Timestamp(new Date().getTime()));
		stage.setDueDate(new Timestamp(new Date().getTime()));
		stage.setScOrderId(301);
		stage.setServiceId(288);
		stage.setUpdatedTime(new Timestamp(new Date().getTime()));
		return stage;
	}
	
	public Activity createActivity() {
		Activity activity = new Activity();
		activity.setId(1);
		activity.setCompletedTime(new Timestamp(new Date().getTime()));
		activity.setDuedate(new Timestamp(new Date().getTime()));
		activity.setCreatedTime(new Timestamp(new Date().getTime()));
		activity.setUpdatedTime(new Timestamp(new Date().getTime()));
		
		return activity;
	}
	
	public MstTaskDef getMstTaskDef(String mstTaskDefKey) {
		MstTaskDef mstTaskDef = new MstTaskDef();
		mstTaskDef.setTat(100);
		mstTaskDef.setName("arrange-field-engineer-ss");
		mstTaskDef.setWaitTime("200");
		mstTaskDef.setKey(mstTaskDefKey);
		return mstTaskDef;
	}
	
	public Task createTask() {
		Task task = new Task();
		task.setActivity(createActivity());
		task.setMstTaskDef(getMstTaskDef("install_mux"));
		task.setWfTaskId("eaf7e6f3-2b4a-11e9-9d67-54bf643bdf47");
		task.setUpdatedTime(new Timestamp(new Date().getTime()));
		task.setWfProcessInstId("");
		task.setPriority(1);
		task.setProcessId(1);
		task.setMstStatus(getMstStatus());
		task.setTaskAssignments(createTaskAssignmentSet());
		task.setWfExecutorId("76f46c0f-343d-11e9-9909-0242ac110015");
		return task;
	}
	
	public MstStatus getMstStatus() {
		MstStatus mstStatus = new MstStatus();
		mstStatus.setActive("yes");
		mstStatus.setId(1);
		mstStatus.setCode("OPEN");
		return mstStatus;
	}

	public Set<Task> createTasksSet(){
		Set<Task> taskSet = new HashSet<>();
		taskSet.add(createTask());
		return taskSet;
	}
	
	public Set<Activity> createActivities(){
		Activity activity = new Activity();
		Set<Activity> activitySet = new HashSet<>();
		activity.setId(1);
		activity.setTasks(createTasksSet());
		activitySet.add(activity);
		return activitySet;
	}
	
	public Process createProcess() {
		Process process = new Process();
		process.setId(5);
		process.setStage(createStage());
		process.setActivities(createActivities());
		process.setMstStatus(createMstStatus("OPEN"));
		process.setUpdatedTime(new Timestamp(new Date().getTime()));
		process.setWfProcInstId("ae0ecb47-2b4a-11e9-80b1-54bf643bdf47");
		return process;
	}

	public List<ProcessTaskLog> createProcessTaskLogList() {
		List<ProcessTaskLog> processTaskLogList = new ArrayList<>();
		ProcessTaskLog processTaskLog = new ProcessTaskLog();
		processTaskLog.setAction("Claimed");
		processTaskLog.setActionFrom("from");
		processTaskLog.setActionTo("to");
		processTaskLog.setDescrption("description");
		processTaskLog.setGroupFrom("CID");
		processTaskLog.setGroupTo("test");
		processTaskLog.setId(1);
		processTaskLog.setScOrderId(301);
		processTaskLog.setOrderCode("IAS150818XSVMKED");
		processTaskLogList.add(processTaskLog);
		return processTaskLogList;
	}
	
	public MstStatus createMstStatus(String status) {
		MstStatus mstStatus = new MstStatus();
		mstStatus.setCode(status);
		return mstStatus;
	}

	public Task createTaskBasedOnStatus(String status) {
		Task task = new Task();
		task.setId(1);
		task.setActivity(createActivity());
		task.setMstTaskDef(getMstTaskDef("status"));
		task.setWfTaskId("eaf7e6f3-2b4a-11e9-9d67-54bf643bdf47");
		task.setMstStatus(createMstStatus(status));
		task.setTaskAssignments(createTaskAssignmentSet());
		return task;
	}
	
	public List<Task> createTaskList() {
		List<Task> taskList = new ArrayList<>();
		List<String> statuses = Arrays.asList("OPEN","CLOSED","ASSIGNED","HOLD","PENDING","INPROGRESS");
		statuses.stream().forEach( status ->{
			taskList.add(createTaskBasedOnStatus(status));
		});
		return taskList;
	}

	public TaskAssignment createTaskAssignment() {
		TaskAssignment taskAssignment = new TaskAssignment();
		taskAssignment.setGroupName("Test");
		taskAssignment.setOwner("CID");
		taskAssignment.setId(5);
		taskAssignment.setUserName("test");
		return taskAssignment;
	}
	
	public Set<TaskAssignment> createTaskAssignmentSet(){
		Set<TaskAssignment> taskAssignmentSet = new HashSet<>();
		taskAssignmentSet.add(createTaskAssignment());
		return taskAssignmentSet;
	}

	public ProcessTaskLog createProcessTaskLog() {
		ProcessTaskLog processTaskLog = new ProcessTaskLog();
		processTaskLog.setAction("Claimed");
		processTaskLog.setActive("Y");
		processTaskLog.setScOrderId(301);
		processTaskLog.setOrderCode("IAS150818XSVMKED");
		return processTaskLog;
	}

	public AssigneeRequest createAssigneeRequest() {
		AssigneeRequest assigneeRequest = new AssigneeRequest();
		assigneeRequest.setAssigneeNameTo("Manager");
		assigneeRequest.setAssigneeNameFrom("Manager");
		assigneeRequest.setTaskId(5);
		return assigneeRequest;
	}

	public AssigneeRequest createAssigneeRequest2() {
		AssigneeRequest assigneeRequest = new AssigneeRequest();
		assigneeRequest.setAssigneeNameTo("Employee");
		assigneeRequest.setAssigneeNameFrom("Manager");
		assigneeRequest.setTaskId(5);
		return assigneeRequest;
	}	
	
	private static final String requestJson = "{\r\n" + 
			"  \"orderInfo\":{\r\n" + 
			"    \"scOrderId\":1,\r\n" + 
			"    \"parentId\":2,\r\n" + 
			"    \"orderTermsInMonth\":2\r\n" + 
			"  },\r\n" + 
			"  \"customerInfo\":{\r\n" + 
			"    \"customerId\":\"112\",\r\n" + 
			"    \"customerLeId\":11,\r\n" + 
			"    \"billingMethod\":\"Cash\"\r\n" + 
			"  },\r\n" + 
			"  \"primaryServiceInfo\":{\r\n" + 
			"    \"serviceId\":19,\r\n" + 
			"    \"serviceCode\":\"A23D11\"\r\n" + 
			"  },\r\n" + 
			"  \"secondaryServiceInfo\":{\r\n" + 
			"    \"serviceId\":22,\r\n" + 
			"    \"serviceCode\":\"FG112L\"\r\n" + 
			"  },\r\n" + 
			"  \"productName\":\"IAS\",\r\n" + 
			"  \"offeringName\":\"MLPS\"\r\n" + 
			"}";
	
	public Map<String, Object> createMap() {
		Map<String,Object> map = new HashMap<>();
		map.put("L2O_INPUT", requestJson);
		map.put("serviceDeliveryDate",new Timestamp(new Date().getTime()));
		return map;
	}
	
	public MstTaskDef createMstTaskDefWithKey(String key) {
		MstTaskDef mstTaskDef = new MstTaskDef();
		mstTaskDef.setKey(key);
		return mstTaskDef;
	}

	public Task createTaskWithKey(String mstTaskDefKey) {
		Task task = new Task();
		task.setUpdatedTime(new Timestamp(new Date().getTime()));
		task.setActivity(createActivity());
		task.setMstTaskDef(getMstTaskDef(mstTaskDefKey));
		task.setWfTaskId("eaf7e6f3-2b4a-11e9-9d67-54bf643bdf47");
		task.setWfProcessInstId("");
		task.setPriority(1);
		task.setProcessId(1);
		task.setMstStatus(getMstStatus());
		task.setTaskAssignments(createTaskAssignmentSet());
		task.setWfExecutorId("76f46c0f-343d-11e9-9909-0242ac110015");
		return task;
	}

	
	
	public String createUserGroupBeans() {
		return "{\r\n" + 
				"  \"user\": [\r\n" + 
				"    {\r\n" + 
				"      \"userName\": \"\",\r\n" + 
				"      \"userId\": \"\"\r\n" + 
				"    }\r\n" + 
				"  ]\r\n" + 
				"}";
	}

	public TaskData createTaskData() {
		TaskData taskData = new TaskData();
		taskData.setCreatedTime(new Timestamp(new Date().getTime()));
		taskData.setData("data");
		taskData.setName("install_mux");
		taskData.setTask(createTask());
		return taskData;
	}

	public MuxDetailBean createMuxDetailBean() {
		MuxDetailBean muxDetailBean = new MuxDetailBean();
		muxDetailBean.setEndMUXNodeIP("IPNode");
		muxDetailBean.setEndMUXNodeName("EndMuxNode");
		return muxDetailBean;
	}
	
	

	/**
	 * createOptionalServiceDetails
	 * @return
	 */
	public ScServiceDetail createServiceDetails() {
		ScServiceDetail scServiceDetail = new ScServiceDetail();
		scServiceDetail.setPriority(1f);
		return scServiceDetail;
	}

	

	/**
	 * createFieldEngineer
	 * @return
	 */
	public FieldEngineer createFieldEngineer() {
		FieldEngineer fieldEngineer = new FieldEngineer();
		fieldEngineer.setAppointmentType("cable-extension");
		fieldEngineer.setId(1);
		fieldEngineer.setServiceId(12);
		return fieldEngineer;
	}

	/**
	 * createVendor
	 * @return
	 */
	public Vendors createVendor() {
		Vendors vendors = new Vendors();
		vendors.setEmail("vendor@gmail.com");
		vendors.setId(14);
		vendors.setName("vendor");
		vendors.setPhoneNumber("8100000099");
		return vendors;
	}

	/**
	 * createMstVendor
	 * @return
	 */
	public MstVendor createMstVendor() {
		MstVendor mstVendor = new MstVendor();
		mstVendor.setMstStatus(createMstStatus("OPEN"));
		mstVendor.setEmail("test@gmail.com");
		mstVendor.setId(8);
		return mstVendor;
	}

	/**
	 * createAppointment
	 * @return
	 */
	public Appointment createAppointment() {
		Appointment appointment = new Appointment();
		appointment.setAppointmentDate(new Timestamp(new Date().getTime()));
		appointment.setAppointmentType("cable-extension");
		appointment.setId(42);
		appointment.setMstStatus(createMstStatus("CLOSED"));
		return appointment;
	}

	/**
	 * createCustomerAppointmentBean
	 * @return
	 */
	public CustomerAppointmentBean createCustomerAppointmentBean() {
		CustomerAppointmentBean customerAppointmentBean = new CustomerAppointmentBean();
		customerAppointmentBean.setAppointmentSlot(2);
		customerAppointmentBean.setAttachmentPermissionId(1);
		customerAppointmentBean.setLocalContactEMail("test@gmail.com");
		customerAppointmentBean.setDocumentAttachments(Arrays.asList(1,2));
		customerAppointmentBean.setAppointmentDate(new String());
		return customerAppointmentBean;
	}

	
	
	public AttachmentIdBean createAttachmentBean() {
		
		AttachmentIdBean bean = new AttachmentIdBean();
		bean.setCategory("Tax");
		bean.setAttachmentId(1);
		return bean;		
	}

	public FieldEngineerDetailsBean createFieldEngineerDetailsBean() {
		FieldEngineerDetailsBean fieldEngineerDetailsBean = new FieldEngineerDetailsBean();
		fieldEngineerDetailsBean.setContactNumber("9876543210");
		fieldEngineerDetailsBean.setEmailId("sam@legomail.com");
		fieldEngineerDetailsBean.setName("sam");
		fieldEngineerDetailsBean.setType("contract");
		fieldEngineerDetailsBean.setSecondaryName("Allan");	
		fieldEngineerDetailsBean.setSecondaryEmailId("allan@legomail.com");
		fieldEngineerDetailsBean.setSecondaryContactNumber("9999988888");
		return fieldEngineerDetailsBean;
	}

	public Object createResponse() {
		Response createResponse= new Response();
		createResponse.setStatus(true);
		return createResponse;
	}

}

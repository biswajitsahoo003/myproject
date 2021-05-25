package com.tcl.dias.servicefulfillment.objectcreator;

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.tcl.dias.common.utils.DateUtil;
import com.tcl.dias.servicefulfillment.beans.*;
import com.tcl.dias.servicefulfillment.beans.PlannedEventBean;
import com.tcl.dias.servicefulfillment.beans.VendorDetailsBean;
import com.tcl.dias.servicefulfillmentutils.beans.*;
import com.tcl.dias.servicefulfillmentutils.beans.MuxDetailBean;
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

	public AdditionalTechnicalDetailsBean createAdditionalTechnicalDetailsBean() {
		AdditionalTechnicalDetailsBean additionalTechnicalDetailsBean = new AdditionalTechnicalDetailsBean();
		additionalTechnicalDetailsBean.setBfdRequired("yes");
		additionalTechnicalDetailsBean.setExtendedLanRequired("yes");
		additionalTechnicalDetailsBean.setRoutesExchanged("yes");
		return additionalTechnicalDetailsBean;
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

	public VendorDetailsBean createVendorDetailsBean() {
		VendorDetailsBean vendorDetailsBean = new VendorDetailsBean();
		vendorDetailsBean.setContactNumber("8100000099");
		vendorDetailsBean.setEmailId("test@gmail.com");
		vendorDetailsBean.setMstVendorId(14);
		vendorDetailsBean.setName("vendor");
		vendorDetailsBean.setVendorType("OSP");
		return vendorDetailsBean;
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
		mstVendor.setCircle("Chennai");
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
		customerAppointmentBean.setAttachmentPermissionId(2214);
		customerAppointmentBean.setLocalContactEMail("test@gmail.com");
		customerAppointmentBean.setDocumentAttachments(Arrays.asList(1,2));
		customerAppointmentBean.setAppointmentDate(new String());
		return customerAppointmentBean;
	}

	/**
	 * createRowBean
	 * @return
	 */
	public RowBeanRequest createRowBean() {
		RowBeanRequest rowBean = new RowBeanRequest();
		//rowBean.setBankGurantee("yes");
		rowBean.setDocumentIds(Arrays.asList(new AttachmentIdBean()));
		rowBean.setRecurringFixedCharge(1.2d);
		return rowBean;
	}

	/**
	 * createMrnMuxBean
	 * @return
	 */
	public MrnForMuxBean createMrnMuxBean() {
		MrnForMuxBean mrnForMuxBean = new MrnForMuxBean();
		//mrnForMuxBean.setApprovingPerson("vendor");
		//mrnForMuxBean.setMrnDate(new String());
		mrnForMuxBean.setMrnNumber("123");
		return mrnForMuxBean;
	}

	/**
	 * createInstallMuxBean
	 * @return
	 */
	public InstallMuxBean createInstallMuxBean() {
		InstallMuxBean installMuxBean = new InstallMuxBean();
		installMuxBean.setMuxInstallationDate(new String());
		installMuxBean.setMuxInstallationStatus("status");
		return installMuxBean;
	}

	public DefineScopeWorkProjectPlanBean createDefineScopeWorkProjectPlanBean(){
		DefineScopeWorkProjectPlanBean defineScopeWorkProjectPlanBean = new DefineScopeWorkProjectPlanBean();
		defineScopeWorkProjectPlanBean.setDeliveryMilestone("milestone");
		defineScopeWorkProjectPlanBean.setMilestoneCompletionDate(new String());
		return defineScopeWorkProjectPlanBean;
	}

	public DefineScopeWorkProjectPlanBeanList createDefineScopeWorkProjectPlanBeanList(){
		DefineScopeWorkProjectPlanBeanList defineScopeWorkProjectPlanBeanList = new DefineScopeWorkProjectPlanBeanList();
		defineScopeWorkProjectPlanBeanList.setDefineScopeWorkProjectPlanBeanList(Collections.singletonList(createDefineScopeWorkProjectPlanBean()));
		return defineScopeWorkProjectPlanBeanList;
	}


	/**
	 * createPreparePoGovernmentAuthorityBean
	 * @return
	 */
	public PreparePoGovernmentAuthorityBean createPreparePoGovernmentAuthorityBean() {
		PreparePoGovernmentAuthorityBean preparePoGovernmentAuthorityBean = new PreparePoGovernmentAuthorityBean();
		preparePoGovernmentAuthorityBean.setGovtAuthVendorId("871");
		preparePoGovernmentAuthorityBean.setRowPoNumber("17");
		return preparePoGovernmentAuthorityBean;
	}

	/**
	 * createCapexBean
	 * @return
	 */
	public CapexBean createCapexBean() {
		CapexBean capexBean = new CapexBean();
		capexBean.setApprovalDate(new String());
		capexBean.setApprovalRemarks("Everything OK");
		capexBean.setApprovalStatus("Approved");
		capexBean.setApproverEmail("test@gmail.com");
		return capexBean;
	}

	/**
	 * createOspBean
	 * @return
	 */
	public OSPBean createOspBean() {
		OSPBean ospBean = new OSPBean();
		ospBean.setCompletionDate("11/03/2019");
		ospBean.setDocumentIds(Arrays.asList(new AttachmentIdBean()));
		return ospBean;
	}

	/**
	 * createAttachmentIdBean
	 * @return
	 */
	public DocumentIds createAttachmentIdBean() {
		AttachmentIdBean attachmentIdBean = new AttachmentIdBean();
		attachmentIdBean.setAttachmentId(1);
		return new DocumentIds();
	}

	/**
	 * createCompleteAcceptanceTestingBean
	 * @return
	 */
	public CompleteAcceptanceTestingBean createCompleteAcceptanceTestingBean() {
		CompleteAcceptanceTestingBean completeAcceptanceTestingBean = new CompleteAcceptanceTestingBean();
		completeAcceptanceTestingBean.setIbdAcceptanceDate(new String());
		completeAcceptanceTestingBean.setIbdAcceptanceObservationsRemarks("remarks");
		return completeAcceptanceTestingBean;
	}

	/**
	 * createIbdBean
	 * @return
	 */
	public IBDBean createIbdBean() {
		IBDBean ibdBean = new IBDBean();
		ibdBean.setRackWidth(100.0);
		ibdBean.setRackDepth(100.0);
		ibdBean.setCompletionDate("11/03/2019");
		ibdBean.setDocumentIds(Arrays.asList(new AttachmentIdBean()));
		return ibdBean;
	}

	/**
	 * createWoSiteSurveyBean
	 * @return
	 */
	public WoSiteSurveyBean createWoSiteSurveyBean() {
		WoSiteSurveyBean woSiteSurveyBean = new WoSiteSurveyBean();
		woSiteSurveyBean.setWoNumber("123");
		woSiteSurveyBean.setWoReleaseDate(new String());
		woSiteSurveyBean.setWoReleaseDate("Yes");
		List<AttachmentIdBean> list = new ArrayList<>();
		AttachmentIdBean attachmentIdBean = new AttachmentIdBean();
		attachmentIdBean.setAttachmentId(1);
		attachmentIdBean.setCategory("category");
		list.add(attachmentIdBean);
		return woSiteSurveyBean;
	}
	
	@SuppressWarnings("deprecation")
	public ProvidePoForCPEOrderBean createProvidePoForCPEOrderBean() {
		
		ProvidePoForCPEOrderBean orderBean=new ProvidePoForCPEOrderBean();
		orderBean.setCpeSupplyHardwarePoNumber("po123");
		orderBean.setCpeSupplyHardwareVendorName("alcatel");
		//orderBean.setCpeSupplyHardwarePoDate(new Date("1992-02-03"));
		orderBean.setDocumentIds(Collections.singletonList(createAttachmentBean()));
		return orderBean;
		
	}
	
	public AttachmentIdBean createAttachmentBean() {
		
		AttachmentIdBean bean = new AttachmentIdBean();
		bean.setCategory("Tax");
		bean.setAttachmentId(2214);
		return bean;
	}

	public NegotiateCommercialsLMProvide createNegotiateCommercialsLMProvide() {
		NegotiateCommercialsLMProvide negotiateCommercialsLmProvide = new NegotiateCommercialsLMProvide();
		negotiateCommercialsLmProvide.setNegotiateOffnetARCCost("1200.00");
		negotiateCommercialsLmProvide.setNegotiateOffnetNRCCost("1500.00");
		return negotiateCommercialsLmProvide;
	}

	@SuppressWarnings("deprecation")
	public ProvidePOToLMProvider createProvidePOToLMProvider() {
		ProvidePOToLMProvider providePOToLMProvider = new ProvidePOToLMProvider();
		providePOToLMProvider.setDocumentIds(Collections.singletonList(createAttachmentBean()));
		//providePOToLMProvider.setOffNetPODate(new Date("1992-02-03"));
		//providePOToLMProvider.setOffNetPONumber("PO123");
		providePOToLMProvider.setSupplierCAFNumber("CAF123");
		return providePOToLMProvider;
	}

	@SuppressWarnings("deprecation")
	public ProvidePOToCCProvider createProvidePOToCCProvider() {
		ProvidePOToCCProvider providePOToCCProvider = new ProvidePOToCCProvider();
		//providePOToCCProvider.setCrossConnectPODate(new Date("1992-02-05"));
		providePOToCCProvider.setCrossConnectPONumber("PO456");
		providePOToCCProvider.setDocumentIds(Collections.singletonList(createAttachmentBean()));
		providePOToCCProvider.setSupplierCAFNumber("CAF345");
		return providePOToCCProvider;
	}

	@SuppressWarnings("deprecation")
	public SupplierAcceptance createSupplierAcceptance() {
		SupplierAcceptance supplierAcceptance = new SupplierAcceptance();
		supplierAcceptance.setProvideOrderreferenceId("REF123");
		//supplierAcceptance.setProviderOrderLogDate(new Date("1992-02-06"));
		return supplierAcceptance;
	}

	public RaiseJeopardy createRaiseJeopardy() {
		RaiseJeopardy raiseJeopardy = new RaiseJeopardy();
		raiseJeopardy.setComment("Testing123");
		raiseJeopardy.setJeopardyReason("Reason to test");
		return raiseJeopardy;
	}

	public DefineSowProjPlan createDefineSowProjPlan() {
		
		DefineSowProjPlan defineSowProjPlan = new DefineSowProjPlan();
		defineSowProjPlan.setAttributes(Collections.singletonList(createSowAttributesBean()));
		return defineSowProjPlan;
	}
	
	@SuppressWarnings("deprecation")
	public SowAttributesBean createSowAttributesBean() {
		
		SowAttributesBean sowAttributesBean = new SowAttributesBean();
		sowAttributesBean.setApplicable(true);
		//sowAttributesBean.setEndDate(new Date("1994-02-03"));
		sowAttributesBean.setName("Testing");
		//sowAttributesBean.setStartDate(new Date("1994-01-03"));
		return sowAttributesBean;
	}

	public LmDelivery createLmDelivery() {
		LmDelivery lmDelivery = new LmDelivery();
		lmDelivery.setStatus("OPEN");
		lmDelivery.setHandoffDetails(createHandoffDetailsBean());
		lmDelivery.setAttributes(Collections.singletonList(createLmDeliveryAttributesBean()));
		return lmDelivery;
	}
	
	@SuppressWarnings("deprecation")
	public LmDeliveryAttributesBean createLmDeliveryAttributesBean() {
		LmDeliveryAttributesBean lmDeliveryAttributesBean = new LmDeliveryAttributesBean();
		lmDeliveryAttributesBean.setApplicable(false);
		lmDeliveryAttributesBean.setName("Testing");
		//lmDeliveryAttributesBean.setStartDate(new Date("1994-01-03"));
		//lmDeliveryAttributesBean.setEndDate(new Date("1994-05-03"));
		return lmDeliveryAttributesBean;
	}
	
	public HandoffDetailsBean createHandoffDetailsBean() {
		
		HandoffDetailsBean handoffDetailsBean = new HandoffDetailsBean();
		handoffDetailsBean.setAu4("AU4");
		handoffDetailsBean.setCustomerEndHandoff("Completed");
		handoffDetailsBean.setCustomerEndPort("10.1.1.09");
		handoffDetailsBean.setCustomerInnerVlan("VLAN1.2");
		handoffDetailsBean.setDocumentIds(Collections.singletonList(createAttachmentBean()));
		handoffDetailsBean.setHandoverType("online");
		handoffDetailsBean.setKlmPopEnd("POP12");
		handoffDetailsBean.setLmTestResults("SUCCESS");
		handoffDetailsBean.setNniId("NNID1234");
		handoffDetailsBean.setNniTimeSlotKlm("Time slot - afternoon");
		handoffDetailsBean.setOffnetLmDeviationApprovals("Available");
		handoffDetailsBean.setOffnetLmDeviationRemarks("Nothing");
		handoffDetailsBean.setProviderReferenceId("REF-123");
		handoffDetailsBean.setSupplierMuxDdf("mux 12.2.2.0");
		handoffDetailsBean.setSupplierMuxInterface("Alcatel");
		handoffDetailsBean.setSupplierMuxPort("8099");
		return handoffDetailsBean;
	}

	public AdditionalTechnicalDetailsBean createAdditionalTechnicalDetails() {
		AdditionalTechnicalDetailsBean additionalTechnicalDetailsBean = new AdditionalTechnicalDetailsBean();
		additionalTechnicalDetailsBean.setAccessRequired("yes");
		additionalTechnicalDetailsBean.setAdditionalIps("12.0.0.1");
		additionalTechnicalDetailsBean.setAsNumber("12");
		additionalTechnicalDetailsBean.setAsNumberFormat("1321");
		additionalTechnicalDetailsBean.setAsPassword("password");
		additionalTechnicalDetailsBean.setAuthenticationMode("online");
		additionalTechnicalDetailsBean.setAuthenticationProtocol("tcp");
		additionalTechnicalDetailsBean.setBfdMultiplier("12");
		additionalTechnicalDetailsBean.setBfdReceiveInterval("23");
		additionalTechnicalDetailsBean.setBfdRequired("yes");
		additionalTechnicalDetailsBean.setBfdTransmitInterval("344");
		additionalTechnicalDetailsBean.setBgpAsNumber("13");
		additionalTechnicalDetailsBean.setBgpPeeringOn("22");
		additionalTechnicalDetailsBean.setConnectorType("onnet");
		additionalTechnicalDetailsBean.setCpe("cpe123");
		additionalTechnicalDetailsBean.setCustomerPrefixes("testing");
		additionalTechnicalDetailsBean.setExtendedLanRequired("yes");
		additionalTechnicalDetailsBean.setRoutesExchanged("routes");
		additionalTechnicalDetailsBean.setDns("www.example.com");
		additionalTechnicalDetailsBean.setWanIpProvidedBy("TCL");
		additionalTechnicalDetailsBean.setIsAuthenticationRequiredForProtocol("yes");
		additionalTechnicalDetailsBean.setResiliency("resiliency");
		additionalTechnicalDetailsBean.setSharedLastMile("lastmile");
		additionalTechnicalDetailsBean.setSharedLastMileServiceId("1234");
		additionalTechnicalDetailsBean.setRoutingProtocol("HTTP");
		return additionalTechnicalDetailsBean;
	}

	public SiteReadinessDetailBean createSiteReadinessDetailBean() {
		SiteReadinessDetailBean siteReadinessDetailBean = new SiteReadinessDetailBean();
		siteReadinessDetailBean.setIsSiteReady("Ready");
		return siteReadinessDetailBean;
	}
	
	

	public FieldEngineerDetailsBean createFieldEngineerDetailsBean() {
		FieldEngineerDetailsBean fieldEngineerDetailsBean = new FieldEngineerDetailsBean();
		fieldEngineerDetailsBean.setContactNumber("9876543210");
		fieldEngineerDetailsBean.setEmailId("sam@legomail.com");
		fieldEngineerDetailsBean.setName("sam");
		fieldEngineerDetailsBean.setType("contract");
		fieldEngineerDetailsBean.setSecondaryName("Peter");
		fieldEngineerDetailsBean.setSecondaryContactNumber("1234567890");
		fieldEngineerDetailsBean.setSecondaryEmailId("test@legomail.com");
		return fieldEngineerDetailsBean;
	}

	/*public DefineScopeWorkProjectPlanBeanList createDefineScopeWorkProjectPlanBeanList() {
		DefineScopeWorkProjectPlanBeanList defineScopeWorkProjectPlanBeanList=new DefineScopeWorkProjectPlanBeanList();
		defineScopeWorkProjectPlanBeanList.setDefineScopeWorkProjectPlanBeanList(Collections.singletonList(createDefineScopeWorkProjectPlanBean()));
		return defineScopeWorkProjectPlanBeanList;
	}*/
	


	public ConductSiteSurveyBean createConductSiteSurveyBean() {
		ConductSiteSurveyBean conductSiteSurveyBean = new ConductSiteSurveyBean();

		conductSiteSurveyBean.setCableLength("2m");
		conductSiteSurveyBean.setDemarcationBuildingName("jtp");
		conductSiteSurveyBean.setDemarcationFloor("4");
		conductSiteSurveyBean.setDemarcationRoom("2");
		conductSiteSurveyBean.setDemarcationWing("first");
		conductSiteSurveyBean.setDeviationReason("change of plan");
		conductSiteSurveyBean.setDitPath("testing");
		conductSiteSurveyBean.setDitType("test");
//		conductSiteSurveyBean.setDocumentIds(Collections.singletonList(createAttachmentBean()));
		//conductSiteSurveyBean.setEarthingAvailable("yes");
		conductSiteSurveyBean.setFdf("testing");
		conductSiteSurveyBean.setFdName("test");
		conductSiteSurveyBean.setFloorDiagramType("jpeg image");
		conductSiteSurveyBean.setHeightOfServerRoom("1200");
		//conductSiteSurveyBean.setHygienicRoomNearNetworkRoom("yes");
		//conductSiteSurveyBean.setIbdLength("3m");
		conductSiteSurveyBean.setIbdName("ibd123");
		//conductSiteSurveyBean.setIbdPath("via floor");
		conductSiteSurveyBean.setIbdType("testing");
		conductSiteSurveyBean.setOspCapexDeviation("yes");
		conductSiteSurveyBean.setOspDistance("500m");
		conductSiteSurveyBean.setOspLength("100m");
		conductSiteSurveyBean.setOspPath("path");
		conductSiteSurveyBean.setOspType("type");
		//conductSiteSurveyBean.setPowerAvailable("yes");
		conductSiteSurveyBean.setPowerBackup("yes");
		//conductSiteSurveyBean.setPowerSocketAvailable("yes");
		conductSiteSurveyBean.setProwRequired("yes");
		conductSiteSurveyBean.setRackSize("10");
		//conductSiteSurveyBean.setRackSizeOfMuxInstallation("50");
		//conductSiteSurveyBean.setRackSpaceAvailability("yes");
		conductSiteSurveyBean.setRowRequired("yes");
		conductSiteSurveyBean.setSiteAddress("JTP Chennai");
		//conductSiteSurveyBean.setSiteDeficiencyObservations("observations");
		//conductSiteSurveyBean.setSiteReadinessStatus("Ready");
		conductSiteSurveyBean.setSurveyRemarks("Done");
		conductSiteSurveyBean.setTypeOfCable("coaxial");
		conductSiteSurveyBean.setTypeOfRack("rack_type");
		return conductSiteSurveyBean;
	}


	public PRowRequest createPRowRequest() {
		PRowRequest pRowRequest= new PRowRequest();
		pRowRequest.setBuildingAuthorityAddress("JTP Nandhambakkam Chennai");
		pRowRequest.setBuildingAuthorityContactNumber("9876543210");
		pRowRequest.setBuildingAuthorityName("SAM");
		pRowRequest.setContractRequired("Yes");
		pRowRequest.setDocumentIds(Collections.singletonList(createAttachmentBean()));
		return pRowRequest;
	}
	
	public RoadTypeLengthBean createRoadTypeLengthBean() {
		RoadTypeLengthBean roadTypeLengthBean=new RoadTypeLengthBean();
		roadTypeLengthBean.setLength(120.00);
		roadTypeLengthBean.setTypeOfRoad("Cement");
		return roadTypeLengthBean;
	}

	public RowBeanRequest createRowBeanRequest() {
		RowBeanRequest rowBeanRequest=new RowBeanRequest();
		rowBeanRequest.setDocumentIds(null);
		rowBeanRequest.setOneTimeCharge(100.00);
		rowBeanRequest.setRecurringFixedCharge(25000.00);
		rowBeanRequest.setRecurringVariableCharge(1000.00);
		rowBeanRequest.setTaxes(12500.00);
		return rowBeanRequest;
	}

	public PaymentBean createPaymentBean() {
		PaymentBean paymentBean=new PaymentBean();
		paymentBean.setProwTransactionId("PROW292");
		paymentBean.setRowTransactionId("ROW123");
		paymentBean.setDocumentIds(Collections.singletonList(createAttachmentBean()));
		return paymentBean;
	}

	public BuildingAuthorityContractRequest createBuildingAuthorityContractRequest() {
		BuildingAuthorityContractRequest buildingAuthorityContractRequest= new BuildingAuthorityContractRequest();
		/*buildingAuthorityContractRequest.setContractStartDate(new String());
		buildingAuthorityContractRequest.setContractEndDate(new String());*/
	
		return buildingAuthorityContractRequest;
	}

	public CreateInventoryRecord createOSPInventoryRecord() {
		CreateInventoryRecord createInventoryRecord=new CreateInventoryRecord();
		createInventoryRecord.setDateOfRecord(new String());
		createInventoryRecord.setEndMuxNodeIp("12.0.0.1");
		createInventoryRecord.setEndMuxNodeName("muxend123");
		createInventoryRecord.setEndMuxNodePort("9087");
		createInventoryRecord.setEorDetails("eor123");
		createInventoryRecord.setIorDetails("ior123");
		return createInventoryRecord;
	}

	public MrnOspMuxRequest createMrnOspMuxRequest() {
		MrnOspMuxRequest mrnOspMuxRequest=new MrnOspMuxRequest();
		mrnOspMuxRequest.setMrnDate(new String());
		mrnOspMuxRequest.setMrnNumber("MRN123");
		return mrnOspMuxRequest;
	}

	public OSPBean createOSPBean() {
        OSPBean ospBean = new OSPBean();
        ospBean.setCompletionDate("11/03/2019");
        ospBean.setDocumentIds(Collections.singletonList(createAttachmentBean()));
        return ospBean;

	}

	public IBDBean createIBDBean() {
		IBDBean iBDBean=new IBDBean();
		iBDBean.setCompletionDate("1992-02-02");
		iBDBean.setDocumentIds(Collections.singletonList(createAttachmentBean()));		
		return iBDBean;
	}

	public CompleteOspAcceptanceBean createCompleteOSPAcceptanceBean() {
        CompleteOspAcceptanceBean completeOspAcceptanceBean = new CompleteOspAcceptanceBean();
        completeOspAcceptanceBean.setOspAcceptanceDate(new String());
        completeOspAcceptanceBean.setOspAcceptanceObservationsRemarks("accepted");
        completeOspAcceptanceBean.setOspAcceptanceReport("Accepted");
        return completeOspAcceptanceBean;
     }
	
	public MrnForMuxBean createMrnForMuxBean() {
        MrnForMuxBean mrnForMuxBean = new MrnForMuxBean();
        mrnForMuxBean.setDocumentIds(Collections.singletonList(createAttachmentBean()));
      //  mrnForMuxBean.setMrnDate(new String());
        mrnForMuxBean.setMrnNumber("222");
        return mrnForMuxBean;
	}

	public DeliverMuxBean createDeliverMuxBean() {
        DeliverMuxBean deliverMuxBean = new DeliverMuxBean();
       // deliverMuxBean.setMaterialDeliveryDate(new String());
        deliverMuxBean.setMaterialTrackingStatus("Confirmed");
        deliverMuxBean.setShipmentNumber("123WA");
        return deliverMuxBean;
	}

	public PlannedEventBean createPlannedEventBean(){
		PlannedEventBean plannedEventBean = new PlannedEventBean();
		plannedEventBean.setStartTime("2019-06-15 09:00:00");
		plannedEventBean.setEndTime("2019-06-22 18:00:00");
		plannedEventBean.setEventId(17);
		plannedEventBean.setMuxIntegrationDate("2019-06-20");
		return plannedEventBean;
	}

	public IntegrateMuxBean createIntegrateMuxBean(){
		IntegrateMuxBean integrateMuxBean = new IntegrateMuxBean();
		integrateMuxBean.setMuxIntegrationDate(new String());
		integrateMuxBean.setMuxIntegrationStatus("complete");
		return integrateMuxBean;
	}

	public ConfigureMuxBean createConfigureMuxBean(){
		ConfigureMuxBean configureMuxBean = new ConfigureMuxBean();
		configureMuxBean.setMuxInventoryStatus("ready");
		return configureMuxBean;
	}

	public List<AttachmentBean> validateSupportingDocument() {
		AttachmentBean attachmentBean = new AttachmentBean();
		attachmentBean.setId(2);
		attachmentBean.setName("Alex");
		attachmentBean.setCategory("test");
		attachmentBean.setType("test");
		attachmentBean.setStoragePathUrl("Testtesttest");
		attachmentBean.setVerified("Y");
		attachmentBean.setVerificationFailureReason("Testing Reason");

		return Arrays.asList(attachmentBean);
	}

	public RaiseJeopardy createServiceManual() {
	
		RaiseJeopardy jeopardy= new RaiseJeopardy();
		return jeopardy;
	}


    public ConductSiteSurveyManBean createConductSiteSurveyManBean() {
		ConductSiteSurveyManBean conductSiteSurveyManBean = new ConductSiteSurveyManBean();
		conductSiteSurveyManBean.setSiteReadinessStatus("Ready");
//		conductSiteSurveyManBean.setDocumentIds(Arrays.asList(new AttachmentIdBean()));
		conductSiteSurveyManBean.setMuxMake("Hello");
		conductSiteSurveyManBean.setMuxMakeModel("Test");
		conductSiteSurveyManBean.setEarthingAvailable("yes");
		conductSiteSurveyManBean.setHygienicRoomNearNetworkRoom("yes");
		conductSiteSurveyManBean.setPowerSocketAvailable("yes");
		conductSiteSurveyManBean.setRackSpaceAvailability("yes");
		conductSiteSurveyManBean.setSiteDeficiencyObservations("observations");
		conductSiteSurveyManBean.setSiteReadinessStatus("Ready");
		return conductSiteSurveyManBean;

    }

	public ProwCostApproval createProwCostApproval() {
		ProwCostApproval approval=new ProwCostApproval();
		approval.setpRowCostApproved("yes");
		approval.setCostApprovalRemarks("Test message");
		return approval;
	}

	public ProvidePoBuildingAuthorityBean createProvidePoBuildingAuthorityBean() {
		ProvidePoBuildingAuthorityBean authorityBean = new ProvidePoBuildingAuthorityBean();
		authorityBean.setContractStartDate("01-01-2019");
		authorityBean.setContractEndDate("12-12-2019");
		return authorityBean;
	}

	public CreateMrnBean createCreateMrnBean() {
		CreateMrnBean bean= new CreateMrnBean();
		return bean;
	}

	public DeliverRfEquipmentDetails createDeliverRfEquipmentDetails() {
		DeliverRfEquipmentDetails equipmentDetails= new DeliverRfEquipmentDetails();
		return equipmentDetails;
	}

	public ConductRfSiteSurveyBean createConductRfSiteSurveyBean() {
		ConductRfSiteSurveyBean bean= new ConductRfSiteSurveyBean();
		return bean;
	}

	public Object createResponse()
	{
			Response createResponse= new Response();
			createResponse.setStatus(true);
			return createResponse;
	}


}

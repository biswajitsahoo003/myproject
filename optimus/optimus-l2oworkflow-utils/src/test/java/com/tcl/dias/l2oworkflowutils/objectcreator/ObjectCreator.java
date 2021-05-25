package com.tcl.dias.l2oworkflowutils.objectcreator;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.tcl.dias.common.servicefulfillment.beans.ServiceFulfillmentRequest;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.l2oworkflow.entity.entities.Activity;
import com.tcl.dias.l2oworkflow.entity.entities.ActivityPlan;
import com.tcl.dias.l2oworkflow.entity.entities.MstActivityDef;
import com.tcl.dias.l2oworkflow.entity.entities.MstProcessDef;
import com.tcl.dias.l2oworkflow.entity.entities.MstStageDef;
import com.tcl.dias.l2oworkflow.entity.entities.MstStatus;
import com.tcl.dias.l2oworkflow.entity.entities.MstTaskDef;
import com.tcl.dias.l2oworkflow.entity.entities.Process;
import com.tcl.dias.l2oworkflow.entity.entities.ProcessPlan;
import com.tcl.dias.l2oworkflow.entity.entities.ProcessTaskLog;
import com.tcl.dias.l2oworkflow.entity.entities.Stage;
import com.tcl.dias.l2oworkflow.entity.entities.StagePlan;
import com.tcl.dias.l2oworkflow.entity.entities.Task;
import com.tcl.dias.l2oworkflow.entity.entities.TaskAssignment;
import com.tcl.dias.l2oworkflowutils.constants.MasterDefConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;


/**
 * this class is used for creation of mock data
 * @author diksha garg
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Service
public class ObjectCreator {

	/**
	 * @author diksha garg
	 * @return
	 */
	@SuppressWarnings("unused")
	public Map<String, Object> getMap() throws TclCommonException {
		String MapJson = "{\r\n" + "\r\n" + "               \"orderInfo\": {\r\n" + "\r\n"
				+ "                              \"optimusOrderCode\": \"IAS150818XSVMKED\",\r\n" + "\r\n"
				+ "                              \"scOrderId\": 301,\r\n" + "\r\n"
				+ "                              \"parentId\": null,\r\n" + "\r\n"
				+ "                              \"parentOptimusOrderCode\": \"IAS150818XSVMKED\",\r\n" + "\r\n"
				+ "                              \"optyClassification\": \"Sell To\",\r\n" + "\r\n"
				+ "                              \"isMultipleLe\": \"N\",\r\n" + "\r\n"
				+ "                              \"isBundleOrder\": \"N\",\r\n" + "\r\n"
				+ "                              \"orderType\": \"NEW\",\r\n" + "\r\n"
				+ "                              \"orderCategory\": \"NEW\",\r\n" + "\r\n"
				+ "                              \"orderSource\": \"OPTIMUS\",\r\n" + "\r\n"
				+ "                              \"orderTermsInMonth\": 12,\r\n" + "\r\n"
				+ "                              \"contractStartDate\": null,\r\n" + "\r\n"
				+ "                              \"contractEndDate\": null,\r\n" + "\r\n"
				+ "                              \"lastMacdDate\": null,\r\n" + "\r\n"
				+ "                              \"orderAttributes\": {                                  \r\n" + "\r\n"
				+ "                                             \"Invoice Method\": \"Paper/Electronic\",\r\n" + "\r\n"
				+ "                                             \"customerLegalEntityId\": \"124\",\r\n" + "\r\n"
				+ "                                             \"CONTACTNAME\": \"Sendil V\",\r\n" + "\r\n"
				+ "                                             \"Customer Contracting Entity\": \"1646\",\r\n" + "\r\n"
				+ "                                             \"Billing Currency\": \"INR\",\r\n" + "\r\n"
				+ "                                             \"Payment Currency\": \"INR\",\r\n" + "\r\n"
				+ "                                             \"Customer Contact Title / Designation\": \"Account Manager\",\r\n"
				+ "\r\n"
				+ "                                             \"CONTACTID\": \"vsendil@shriramvalue.com\",\r\n"
				+ "\r\n"
				+ "                                             \"CUSTOMER_ACCOUNT_ID\": \"0012000000FtM59AAF\",\r\n"
				+ "\r\n" + "                                             \"ERF_LE_STATE_GST_INFO_ID\": \"219\",\r\n"
				+ "\r\n" + "                                             \"CUSTOMER_LE_CUID\": \"101603\",\r\n" + "\r\n"
				+ "                                             \"BILLING_CONTACT_ID\": \"268\",\r\n" + "\r\n"
				+ "                                             \"louRequired\": \"false\",\r\n" + "\r\n"
				+ "                                             \"LEGAL_ENTITY_NAME\": \"Shriram Value Services Limited\",\r\n"
				+ "\r\n" + "                                             \"Supplier Mobile\": \"9382008082\",\r\n"
				+ "\r\n" + "                                             \"entityName\": \"single\",\r\n" + "\r\n"
				+ "                                             \"DESIGNATION\": null,\r\n" + "\r\n"
				+ "                                             \"Notice_Address\": \"76,VENKATAKRISHNA ROAD,RAJA ANNAMALAIPURAM   Chennai Tamil Nadu India 600028\",\r\n"
				+ "\r\n"
				+ "                                             \"Supplier Contracting Entity\": \"Tata Communications Limited, India\",\r\n"
				+ "\r\n" + "                                             \"MSA\": \"111\",\r\n" + "\r\n"
				+ "                                             \"Le Contact\": \"-\",\r\n" + "\r\n"
				+ "                                             \"SFDC_STAGE\": \"Closed Won â€“ COF Received\"\r\n"
				+ "\r\n" + "                              }\r\n" + "\r\n" + "               },\r\n" + "\r\n"
				+ "               \"customerInfo\": {\r\n" + "\r\n"
				+ "                              \"customerId\": \"5\",\r\n" + "\r\n"
				+ "                              \"customerName\": \"Shriram Value Services Private Limited\",\r\n"
				+ "\r\n" + "                              \"customerLeId\": 124,\r\n" + "\r\n"
				+ "                              \"customerLeName\": \"Shriram Value Services Limited\",\r\n" + "\r\n"
				+ "                              \"sfdcCuid\": \"101603\",\r\n" + "\r\n"
				+ "                              \"supplierId\": 5,\r\n" + "\r\n"
				+ "                              \"supplierName\": \"Tata Communications Limited\",\r\n" + "\r\n"
				+ "                              \"sfdcAccountId\": \"SFDC - 014536\",\r\n" + "\r\n"
				+ "                              \"cusomerContactEmail\": \"VSENDIL@SHRIRAMVALUE.COM\",\r\n" + "\r\n"
				+ "                              \"customerContact\": \"9840484034\",\r\n" + "\r\n"
				+ "                              \"accountManager\": null,\r\n" + "\r\n"
				+ "                              \"accountManagerEmail\": null,\r\n" + "\r\n"
				+ "                              \"billingFrequency\": \"Quarterly\",\r\n" + "\r\n"
				+ "                              \"billingAddress\": \"76,VENKATAKRISHNA ROAD,RAJA ANNAMALAIPURAM   Chennai Tamil Nadu India 600028\",\r\n"
				+ "\r\n" + "                              \"billingMethod\": null,\r\n" + "\r\n"
				+ "                              \"paymentTerm\": null\r\n" + "\r\n" + "               },\r\n" + "\r\n"
				+ "               \"primaryServiceInfo\": {\r\n" + "\r\n"
				+ "                              \"serviceId\": 288,\r\n" + "\r\n"
				+ "                              \"siteCode\": null,\r\n" + "\r\n"
				+ "                              \"arc\": 87038.0,\r\n" + "\r\n"
				+ "                              \"nrc\": 7500.0,\r\n" + "\r\n"
				+ "                              \"serviceCommisionDate\": 1539475875000,\r\n" + "\r\n"
				+ "                              \"billingAccountId\": null,\r\n" + "\r\n"
				+ "                              \"bwPortSpeed\": \"20\",\r\n" + "\r\n"
				+ "                              \"bwPortspeedAltName\": null,\r\n" + "\r\n"
				+ "                              \"bwUnit\": \"MB\",\r\n" + "\r\n"
				+ "                              \"createdTime\": 1534291875000,\r\n" + "\r\n"
				+ "                              \"demarcationFloor\": \"2nd Floor\",\r\n" + "\r\n"
				+ "                              \"demarcationRack\": \"4th Rack\",\r\n" + "\r\n"
				+ "                              \"demarcationRoom\": \"3rd Room\",\r\n" + "\r\n"
				+ "                              \"popLocationId\": \"1546\",\r\n" + "\r\n"
				+ "                              \"locationId\": \"203\",\r\n" + "\r\n"
				+ "                              \"siteAddress\": \"Office No. 275 Statesman One     RK Mutt Road  Mylapore Chennai Tamil Nadu INDIA 600004\",\r\n"
				+ "\r\n"
				+ "                              \"popSiteAddress\": \"VSB, No. 4, Swami Sivananda Salai, Chennai - 600002   Triplicane Chennai TAMIL NADU INDIA 600005\",\r\n"
				+ "\r\n" + "                              \"copfId\": \"1939023\",\r\n" + "\r\n"
				+ "                              \"feasibilityId\": \"YDY2G26FI44WJB9D\",\r\n" + "\r\n"
				+ "                              \"lastMileType\": \"OnnetWL\",\r\n" + "\r\n"
				+ "                              \"lastMileProvider\": \"TATA COMMUNICATIONS\",\r\n" + "\r\n"
				+ "                              \"lastMileBw\": \"20\",\r\n" + "\r\n"
				+ "                              \"lastMileBwUnit\": \"MB\",\r\n" + "\r\n"
				+ "                              \"siteEndInterface\": \"Fast Ethernet\",\r\n" + "\r\n"
				+ "                              \"serviceOption\": \"Managed\",\r\n" + "\r\n"
				+ "                              \"localItContactName\": null,\r\n" + "\r\n"
				+ "                              \"localItContactEmailId\": null,\r\n" + "\r\n"
				+ "                              \"localItContactMobileNo\": null,\r\n" + "\r\n"
				+ "                              \"serviceDetailsAttributes\": {\r\n" + "\r\n"
				+ "                                             \"IPv6 Address Pool Size\": null,\r\n" + "\r\n"
				+ "                                             \"IP Address Provided By\": \"TCL\",\r\n" + "\r\n"
				+ "                                             \"List of numbers to be ported\": \"0\",\r\n" + "\r\n"
				+ "                                             \"Shared CPE\": \"No\",\r\n" + "\r\n"
				+ "                                             \"Extended LAN Required?\": \"No\",\r\n" + "\r\n"
				+ "                                             \"LOCAL_IT_CONTACT\": \"1\",\r\n" + "\r\n"
				+ "                                             \"DNS\": \"No\",\r\n" + "\r\n"
				+ "                                             \"Routing Protocol\": \"Static\",\r\n" + "\r\n"
				+ "                                             \"Porting service needed\": \"No\",\r\n" + "\r\n"
				+ "                                             \"Burstable Bandwidth\": null,\r\n" + "\r\n"
				+ "                                             \"BFD Required\": \"No\",\r\n" + "\r\n"
				+ "                                             \"IPv4 Address Pool Size\": \"TCL IPv4/29\",\r\n"
				+ "\r\n" + "                                             \"Service Variant\": \"Standard\",\r\n"
				+ "\r\n" + "                                             \"is_feasiblity_check_done\": \"true\",\r\n"
				+ "\r\n"
				+ "                                             \"CPE Management Type\": \"Fully Managed\",\r\n"
				+ "\r\n" + "                                             \"Shared Last Mile\": \"No\",\r\n" + "\r\n"
				+ "                                             \"Connector Type\": \"LC\",\r\n" + "\r\n"
				+ "                                             \"Termination Name\": null,\r\n" + "\r\n"
				+ "                                             \"IP Address Arrangement\": \"IPv4\",\r\n" + "\r\n"
				+ "                                             \"Rate per Minute(fixed)\": null,\r\n" + "\r\n"
				+ "                                             \"Resiliency\": \"No\",\r\n" + "\r\n"
				+ "                                             \"Rate per Minute(mobile)\": null,\r\n" + "\r\n"
				+ "                                             \"Shared Last Mile Service ID\": null,\r\n" + "\r\n"
				+ "                                             \"is_pricing_check_done\": \"true\",\r\n" + "\r\n"
				+ "                                             \"Routes Exchanged\": \"Default routes\",\r\n" + "\r\n"
				+ "                                             \"Shared CPE Service ID\": null,\r\n" + "\r\n"
				+ "                                             \"Quantity Of Numbers\": \"2\",\r\n" + "\r\n"
				+ "                                             \"Access Required\": \"Yes\",\r\n" + "\r\n"
				+ "                                             \"Service type\": \"Fixed\",\r\n" + "\r\n"
				+ "                                             \"BGP Peering on\": null,\r\n" + "\r\n"
				+ "                                             \"Rate per Minute(special)\": null,\r\n" + "\r\n"
				+ "                                             \"AS Number\": \"TCL private AS Number\",\r\n" + "\r\n"
				+ "                                             \"CPE Basic Chassis\": \"ISR4321-APP/K9\",\r\n" + "\r\n"
				+ "                                             \"Additional IPs\": \"No\",\r\n" + "\r\n"
				+ "                                             \"+ required on A & B number (E.164)\": \"Yes\",\r\n"
				+ "\r\n"
				+ "                                             \"isAuthenticationRequired for protocol\": \"No\",\r\n"
				+ "\r\n" + "                                             \"CPE Management\": \"false\",\r\n" + "\r\n"
				+ "                                             \"Expected Delivery Date\": \"2019-01-08T06:24:38.833\",\r\n"
				+ "\r\n" + "                                             \"CPE\": \"TATACOMM(Outright sale)\",\r\n"
				+ "\r\n"
				+ "                                             \"Requestor Date for Service\": \"2018-11-19T06:24:38.831\",\r\n"
				+ "\r\n" + "                                             \"Usage Model\": null,\r\n" + "\r\n"
				+ "                                             \"Backup Configuration\": null\r\n" + "\r\n"
				+ "                              },\r\n" + "\r\n"
				+ "                              \"feasibilityAttributes\": {}\r\n" + "\r\n" + "               },\r\n"
				+ "\r\n" + "               \"secondaryServiceInfo\": null,\r\n" + "\r\n"
				+ "               \"productName\": null,\r\n" + "\r\n" + "               \"offeringName\": null\r\n"
				+ "\r\n" + "}";

		ServiceFulfillmentRequest resultReq = (ServiceFulfillmentRequest) Utils.convertJsonToObject(MapJson,
				ServiceFulfillmentRequest.class);

		Map<String, Object> ServiceFulfillmentMap = new HashMap<String, Object>();
		ServiceFulfillmentMap.put(MasterDefConstants.L2O_INPUT, resultReq);

		return ServiceFulfillmentMap;

	}

	/**
	 * @author diksha garg
	 * @return
	 */
	public MstProcessDef getMstProcessDef() {

		MstProcessDef mstProcessDef = new MstProcessDef();
		mstProcessDef.setCode("1");
		mstProcessDef.setCustomerView("cust_view");
		mstProcessDef.setKey("K1");
		mstProcessDef.setName("Admin");
		mstProcessDef.setSequence((byte) 1);
		MstStageDef mstStageDef=getMstStageDef();
		mstProcessDef.setMstStageDef(mstStageDef);
		return mstProcessDef;
	}

	/**
	 * @author diksha garg
	 * @return
	 */	
	public Process getProcess()
	{
		Process process = new Process();
		process.setCompletedTime(new Timestamp(new Date().getTime()));
		process.setCreatedTime(new Timestamp(new Date().getTime()));
		process.setDuedate(new Timestamp(new Date().getTime()));
		process.setId(2);
		process.setUpdatedTime(new Timestamp(new Date().getTime()));
		process.setWfProcInstId("WF1");;

		return process;

	}

	/**
	 * @author diksha garg
	 * @return
	 */
	public Stage getStage() {

		Stage stage = new Stage();
		stage.setCompletedTime(new Timestamp(new Date().getTime()));
		stage.setCreatedTime(new Timestamp(new Date().getTime()));
		stage.setDueDate(new Timestamp(new Date().getTime()));
		stage.setId(1);

		MstStageDef mstStageDef = new MstStageDef();
		mstStageDef.setCustomerView("cust_view");
		mstStageDef.setKey("K1");
		mstStageDef.setName("admin");
		mstStageDef.setSequence((byte)1);
		stage.setMstStageDef(mstStageDef);

		MstStatus mstStatus=new MstStatus();
		mstStatus.setActive("Y");
		mstStatus.setCode("123");
		mstStatus.setCode("Code2");
		stage.setMstStatus(mstStatus);

		stage.setOrderCode("OC1");
		stage.setScOrderId(1);
		stage.setServiceId(1);
		stage.setUpdatedTime(new Timestamp(new Date().getTime()));

		return stage;

	}

	/**
	 * @author diksha garg
	 * @return
	 */
	public MstStatus getMstStatus() {
		MstStatus mstStatus=new MstStatus();
		//mstStatus.MstStatusConstant="OPEN"
		mstStatus.setActive("Y");
		mstStatus.setCode("123");
		return mstStatus;
	}

	/**
	 * @author diksha garg
	 * @return
	 */
	public MstStageDef getMstStageDef() {
		MstStageDef mstStageDef= new MstStageDef();
		mstStageDef.setCustomerView("cust_view");
		mstStageDef.setKey("K2");
		mstStageDef.setName("Administrator");
		mstStageDef.setSequence((byte) 3);
		return mstStageDef;
	}

	/**
	 * @author diksha garg
	 * @return
	 */
	public MstActivityDef getMstActivityDef() {
		MstActivityDef mstActivityDef = new MstActivityDef();
		mstActivityDef.setCustomerView("cust_view");
		mstActivityDef.setSequence((byte)12);
		mstActivityDef.setKey("K4");
		mstActivityDef.setName("User");
		MstProcessDef mstProcessDef= getMstProcessDef();
		mstActivityDef.setMstProcessDef(mstProcessDef);

		return mstActivityDef;
	}

	/**
	 * @author diksha garg
	 * @return
	 */
	public Process getProcessRep() {
		Process process = new Process();
		process.setCompletedTime(new Timestamp(new Date().getTime()));
		process.setCreatedTime(new Timestamp(new Date().getTime()));
		process.setDuedate(new Timestamp(new Date().getTime()));
		process.setId(2);
		process.setUpdatedTime(new Timestamp(new Date().getTime()));
		process.setWfProcInstId("WF1");
		return process;
	}

	/**
	 * @author diksha garg
	 * @return
	 */
	public Activity getActivity() {

		Activity activity =  new Activity();
		activity.setCompletedTime(new Timestamp(new Date().getTime()));
		activity.setCreatedTime(new Timestamp(new Date().getTime()));
		activity.setDuedate(new Timestamp(new Date().getTime()));
		activity.setId(6);
		activity.setUpdatedTime(new Timestamp(new Date().getTime()));
		activity.setWfActivityId("A1");
		MstActivityDef mstActivityDef = getMstActivityDef();
		Process process =getProcess();
		MstStatus mstStatus = getMstStatus();
		activity.setMstActivityDef(mstActivityDef);
		activity.setProcess(process);
		activity.setMstStatus(mstStatus);
		return activity;
	}

	/**
	 * @author diksha garg
	 * @return
	 */
	public MstTaskDef getMstTaskDef() {
		MstTaskDef mstTaskDef = new MstTaskDef();
		mstTaskDef.setAssignedGroup("group1");
		mstTaskDef.setIsCustomerTask("N");
		mstTaskDef.setIsManualTask("Y");
		mstTaskDef.setKey("K8");
		mstTaskDef.setName("User");
		mstTaskDef.setOwnerGroup("group_og");
		mstTaskDef.setReminderCycle("daily");
		mstTaskDef.setWaitTime("34");
		mstTaskDef.setButtonLabel("label");
		mstTaskDef.setDescription("description");
		mstTaskDef.setTat(45);
		return mstTaskDef;
	}

	/**
	 * @author diksha garg
	 * @return
	 */
	public Task getTask() {
		Task task = new Task();

		task.setCatagory("catagory");
		task.setClaimTime(new Timestamp(new Date().getTime()));
		task.setCompletedTime(new Timestamp(new Date().getTime()));
		task.setDuedate(new Timestamp(new Date().getTime()));
		task.setCreatedTime(new Timestamp(new Date().getTime()));
		task.setId(5);
		task.setOrderCode("O1");
		task.setPriority(5);
		task.setProcessId(9);
		task.setScOrderId(5);
		task.setServiceId(288);
		task.setUpdatedTime(new Timestamp(new Date().getTime()));
		task.setWfProcessInstId("2");
		task.setWfTaskId("2");
		task.setMstTaskDef(getMstTaskDef());


		return task;
	}

	/**
	 * @author diksha garg
	 * @return
	 */
	public TaskAssignment getTaskAssignment() {
//		TaskAssignment assignment= new TaskAssignment();
//		assignment.setGroupName("group_1");
//		assignment.setId(1);
//		assignment.setOwner("owner");
//		assignment.setProcessId(3);
//		Task task = getTask();
//		assignment.setTask(task);
//		assignment.setUserName("un");
		return new TaskAssignment();
	}

	/**
	 * @author diksha garg
	 * @return
	 */
	public ProcessTaskLog getProcessTaskLog() {
		ProcessTaskLog log=new ProcessTaskLog();
		log.setAction("action");
		log.setActionFrom("user");
		log.setActionTo("cust");
		log.setActive("Y");
		log.setCreatedTime(new Timestamp(new Date().getTime()));
		log.setDescrption("desc");
		log.setGroupFrom("groupfrom");
		log.setGroupTo("groupto");
		log.setId(1);
		log.setOrderCode("OC3");
		log.setScOrderId(3);
		log.setServiceId(288);
		Task task=getTask();
		log.setTask(task);
		return log;
	}

	public ProcessPlan getProcessPlan() {
		ProcessPlan processplan = new ProcessPlan();
		processplan.setActualEndTime(new Timestamp(new Date().getTime()));
		processplan.setActualStartTime(new Timestamp(new Date().getTime()));
		processplan.setEstimatedEndTime(new Timestamp(new Date().getTime()));
		processplan.setEstimatedStartTime(new Timestamp(new Date().getTime()));
		processplan.setId(1);
		processplan.setOrderId(1);
		processplan.setSequence(2);
		processplan.setServiceCode("S1");
		processplan.setServiceId(4);
		processplan.setTargettedEndTime(new Timestamp(new Date().getTime()));
		processplan.setTargettedStartTime(new Timestamp(new Date().getTime()));
		return processplan;
	}

	public StagePlan getStagePlan() {
		StagePlan stagePlan = new StagePlan();
		stagePlan.setActualEndTime(new Timestamp(new Date().getTime()));
		stagePlan.setActualStartTime(new Timestamp(new Date().getTime()));
		stagePlan.setEstimatedStartTime(new Timestamp(new Date().getTime()));
		stagePlan.setEstimatedStartTime(new Timestamp(new Date().getTime()));
		stagePlan.setId(1);
		stagePlan.setScOrderId(4);
		stagePlan.setTargettedEndTime(new Timestamp(new Date().getTime()));
		stagePlan.setTargettedStartTime(new Timestamp(new Date().getTime()));
		stagePlan.setSequence((byte)4);
		stagePlan.setServiceCode("S1");
		stagePlan.setServiceId(1);
		return stagePlan;
	}

	public ActivityPlan getActivityPlan() {
		ActivityPlan activityPlan = new ActivityPlan();
		Activity activity = getActivity();
		activityPlan.setActivity(activity);
		activityPlan.setActualEndTime(new Timestamp(new Date().getTime()));
		activityPlan.setActualStartTime(new Timestamp(new Date().getTime()));
		activityPlan.setEstimatedEndTime(new Timestamp(new Date().getTime()));
		activityPlan.setEstimatedStartTime(new Timestamp(new Date().getTime()));
		activityPlan.setId(1);
		activityPlan.setOrderId(1);
		activityPlan.setSequence(23);
		activityPlan.setServiceCode("s3");
		activityPlan.setServiceId(4);
		activityPlan.setTargettedEndTime(new Timestamp(new Date().getTime()));
		activityPlan.setTargettedStartTime(new Timestamp(new Date().getTime()));
		return activityPlan;
	}




}

package com.tcl.dias.networkaugment.entity.entities;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

/**
 * 
 * This file contains the Task.java class.
 * 
 *
 * @author Mayank S
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Entity
@Table(name = "task")
@NamedQuery(name = "Task.findAll", query = "SELECT t FROM Task t")
public class Task implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String catagory;

	@Column(name = "claim_time")
	private Timestamp claimTime;

	@Column(name = "completed_time")
	private Timestamp completedTime;

	@Column(name = "created_time")
	private Timestamp createdTime;

	private Timestamp duedate;

	@Column(name = "order_code")
	private String orderCode;

	private Integer priority;

	@Column(name = "process_id")
	private Integer processId;

	@Column(name = "sc_order_id")
	private Integer scOrderId;

	@Column(name = "service_id")
	private Integer serviceId;

	@Column(name = "service_code")
	private String serviceCode;

	@Column(name = "updated_time")
	private Timestamp updatedTime;

	@Column(name = "wf_executor_id")
	private String wfExecutorId;

	@Column(name = "wf_process_inst_id")
	private String wfProcessInstId;

	@Column(name = "wf_task_id")
	private String wfTaskId;

	@Column(name = "service_type")
	private String serviceType;

	@Column(name = "order_type")
	private String orderType;

	@Column(name = "scenario_type")
	private String scenarioType;

	@Column(name = "technology_type")
	private String technologyType;

	@Column(name = "order_category")
	private String orderCategory;

	@Column(name = "order_sub_category")
	private String orderSubCategory;

	@Column(name = "city")
	private String city;


	@Column(name = "state")
	private String state;

	@Column(name = "country")
	private String country;
	@Column(name = "quote_code")
	private String quoteCode;

	@Column(name = "quote_id")
	private Integer quoteId;

	@Column(name = "assignee")
	private String assignee;

	@Column(name = "site_type")
	private String siteType="A";
	
	@Column(name = "customer_name")
	private String customerName;

	@Column(name="is_jeopardy_task")
	private Byte isJeopardyTask;

	@Column(name="downtime")
	private String downtime;

	@Column(name="distribution_center_name")
	private String distributionCenterName;

	@Column(name = "site_id")
	private Integer  siteId;

	@Column(name = "site_code")
	private String  siteCode;

	@Column(name="feasibility_id")
	private String feasibilityId;

	@ManyToOne
	@JoinColumn(name="mf_detail_id")
	private MfDetail mfDetail;

	// bi-directional many-to-one association to ProcessTaskLog
	@OneToMany(mappedBy = "task")
	private Set<ProcessTaskLog> processTaskLogs;

	// bi-directional many-to-one association to Activity
	@ManyToOne(fetch = FetchType.LAZY)
	private Activity activity;

	// bi-directional many-to-one association to MstStatus
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "status")
	private MstStatus mstStatus;

	// bi-directional many-to-one association to MstTaskDef
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "task_def_key")
	private MstTaskDef mstTaskDef;

	// bi-directional many-to-one association to TaskAssignment
	@OneToMany(mappedBy = "task")
	private Set<TaskAssignment> taskAssignments;

	// bi-directional many-to-one association to TaskData
	@OneToMany(mappedBy = "task")
	private Set<TaskData> taskData;

	@OneToMany(mappedBy = "task")
	@OrderBy("createdTime DESC")
	private Set<TaskTatTime> tatTimes;

	// bi-directional many-to-one association to ScOrder
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sc_order_id", referencedColumnName = "id", insertable = false, updatable = false)
	//@JoinColumn(name = "sc_order_id")
	private ScOrder scOrder;

	@OneToMany(mappedBy = "task", fetch = FetchType.LAZY)
	private Set<Appointment> appointments;


	@Column(name = "lastmile_scenario")
	private String lastMileScenario;

	@Column(name = "lastmile_type")
	private String lmType;

	@Column(name = "lastmile_provider")
	private String lmProvider;

	@OneToMany(mappedBy = "task", fetch = FetchType.LAZY)
	private Set<TaskRemark> taskRemarks;

@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "sc_service_detail_id")
private ScServiceDetail scServiceDetail;

	@Column(name = "is_ip_down_time_required")
	private String isIpDownTimeRequired;

	@Column(name = "is_tx_downtime_reqd")
	private String isTxDowntimeReqd;

	@Column(name = "device_type")
	private String deviceType;

	@Column(name = "device_platform")
	private String devicePlatform;

	// bi-directional many-to-one association to ScServiceDetail
	@OneToMany(mappedBy = "task", fetch = FetchType.LAZY)
	private  Set<NwaSuspensionReason> nwaSuspensionReasons;

	// bi-directional many-to-one association to SiteDetail
	@ManyToOne
	@JoinColumn(name = "site_detail_id")
	private SiteDetail siteDetail;

	// bi-directional many-to-one association to ScServiceDetail
	@OneToMany(mappedBy = "task", fetch = FetchType.LAZY)
	private  Set<NwaRejectionDetails> nwaRejectionDetails;

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public String getDevicePlatform() {
		return devicePlatform;
	}

	public void setDevicePlatform(String devicePlatform) {
		this.devicePlatform = devicePlatform;
	}

	public String getIsIpDownTimeRequired() {
		return isIpDownTimeRequired;
	}

	public void setIsIpDownTimeRequired(String isIpDownTimeRequired) {
		this.isIpDownTimeRequired = isIpDownTimeRequired;
	}

	public String getIsTxDowntimeReqd() {
		return isTxDowntimeReqd;
	}

	public void setIsTxDowntimeReqd(String isTxDowntimeReqd) {
		this.isTxDowntimeReqd = isTxDowntimeReqd;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public Task() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCatagory() {
		return this.catagory;
	}

	public void setCatagory(String catagory) {
		this.catagory = catagory;
	}

	public Timestamp getClaimTime() {
		return this.claimTime;
	}

	public void setClaimTime(Timestamp claimTime) {
		this.claimTime = claimTime;
	}

	public Timestamp getCompletedTime() {
		return this.completedTime;
	}

	public void setCompletedTime(Timestamp completedTime) {
		this.completedTime = completedTime;
	}

	public Timestamp getCreatedTime() {
		return this.createdTime;
	}

	public void setCreatedTime(Timestamp createdTime) {
		this.createdTime = createdTime;
	}

	public Timestamp getDuedate() {
		return this.duedate;
	}

	public void setDuedate(Timestamp duedate) {
		this.duedate = duedate;
	}

	public String getOrderCode() {
		return this.orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public Integer getPriority() {
		return this.priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public Integer getProcessId() {
		return this.processId;
	}

	public void setProcessId(Integer processId) {
		this.processId = processId;
	}

	public Integer getScOrderId() {
		return this.scOrderId;
	}

	public void setScOrderId(Integer scOrderId) {
		this.scOrderId = scOrderId;
	}

	public Timestamp getUpdatedTime() {
		return this.updatedTime;
	}

	public void setUpdatedTime(Timestamp updatedTime) {
		this.updatedTime = updatedTime;
	}

	public String getWfExecutorId() {
		return this.wfExecutorId;
	}

	public void setWfExecutorId(String wfExecutorId) {
		this.wfExecutorId = wfExecutorId;
	}

	public String getWfProcessInstId() {
		return this.wfProcessInstId;
	}

	public void setWfProcessInstId(String wfProcessInstId) {
		this.wfProcessInstId = wfProcessInstId;
	}

	public String getWfTaskId() {
		return this.wfTaskId;
	}

	public void setWfTaskId(String wfTaskId) {
		this.wfTaskId = wfTaskId;
	}

	public Set<ProcessTaskLog> getProcessTaskLogs() {
		return this.processTaskLogs;
	}

	public void setProcessTaskLogs(Set<ProcessTaskLog> processTaskLogs) {
		this.processTaskLogs = processTaskLogs;
	}

	public ProcessTaskLog addProcessTaskLog(ProcessTaskLog processTaskLog) {
		getProcessTaskLogs().add(processTaskLog);
		processTaskLog.setTask(this);

		return processTaskLog;
	}

	public ProcessTaskLog removeProcessTaskLog(ProcessTaskLog processTaskLog) {
		getProcessTaskLogs().remove(processTaskLog);
		processTaskLog.setTask(null);

		return processTaskLog;
	}

	public Activity getActivity() {
		return this.activity;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

	public MstStatus getMstStatus() {
		return this.mstStatus;
	}

	public void setMstStatus(MstStatus mstStatus) {
		this.mstStatus = mstStatus;
	}

	public MstTaskDef getMstTaskDef() {
		return this.mstTaskDef;
	}

	public void setMstTaskDef(MstTaskDef mstTaskDef) {
		this.mstTaskDef = mstTaskDef;
	}

	public Set<TaskAssignment> getTaskAssignments() {
		return this.taskAssignments;
	}

	public void setTaskAssignments(Set<TaskAssignment> taskAssignments) {
		this.taskAssignments = taskAssignments;
	}

	public TaskAssignment addTaskAssignment(TaskAssignment taskAssignment) {
		getTaskAssignments().add(taskAssignment);
		taskAssignment.setTask(this);

		return taskAssignment;
	}

	public TaskAssignment removeTaskAssignment(TaskAssignment taskAssignment) {
		getTaskAssignments().remove(taskAssignment);
		taskAssignment.setTask(null);

		return taskAssignment;
	}

	public Set<TaskData> getTaskData() {
		return this.taskData;
	}

	public void setTaskData(Set<TaskData> taskData) {
		this.taskData = taskData;
	}

	public TaskData addTaskData(TaskData taskData) {
		getTaskData().add(taskData);
		taskData.setTask(this);

		return taskData;
	}

	public TaskData removeTaskData(TaskData taskData) {
		getTaskData().remove(taskData);
		taskData.setTask(null);

		return taskData;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}

	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	public Set<TaskTatTime> getTatTimes() {
		return tatTimes;
	}

	public void setTatTimes(Set<TaskTatTime> tatTimes) {
		this.tatTimes = tatTimes;
	}

	public Set<Appointment> getAppointments() {
		if (appointments == null) {
			appointments = new HashSet<>();
		}
		return appointments;
	}

	public void setAppointments(Set<Appointment> appointments)
	{
		this.appointments = appointments;
	}

	public String getQuoteCode() {
		return quoteCode;
	}

	public void setQuoteCode(String quoteCode) {
		this.quoteCode = quoteCode;
	}

	public Integer getQuoteId() {
		return quoteId;
	}

	public void setQuoteId(Integer quoteId) {
		this.quoteId = quoteId;
	}

	public String getAssignee() {
		return assignee;
	}

	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}

	public String getOrderCategory() {
		return orderCategory;
	}

	public void setOrderCategory(String orderCategory) {
		this.orderCategory = orderCategory;
	}

	public String getSiteType() {
		return siteType;
	}

	public void setSiteType(String siteType) {
		this.siteType = siteType;
	}


	public String getLastMileScenario() {
		return lastMileScenario;
	}

	public void setLastMileScenario(String lastMileScenario) {
		this.lastMileScenario = lastMileScenario;
	}

	public String getLmType() {
		return lmType;
	}

	public void setLmType(String lmType) {
		this.lmType = lmType;
	}

	public String getLmProvider() {
		return lmProvider;
	}

	public void setLmProvider(String lmProvider) {
		this.lmProvider = lmProvider;
	}



	public Byte getIsJeopardyTask() {
		return isJeopardyTask;
	}

	public void setIsJeopardyTask(Byte isJeopardyTask) {
		this.isJeopardyTask = isJeopardyTask;
	}

	public Set<TaskRemark> getTaskRemarks() {
		return taskRemarks;
	}

	public void setTaskRemarks(Set<TaskRemark> taskRemarks) {
		this.taskRemarks = taskRemarks;
	}

	public ScServiceDetail getScServiceDetail() {
		return scServiceDetail;
	}

	public void setScServiceDetail(ScServiceDetail scServiceDetail) {
		this.scServiceDetail = scServiceDetail;
	}

	public String getOrderSubCategory() {
		return orderSubCategory;
	}

	public void setOrderSubCategory(String orderSubCategory) {
		this.orderSubCategory = orderSubCategory;
	}

	public String getDowntime() {
		return downtime;
	}

	public void setDowntime(String downtime) {
		this.downtime = downtime;
	}
	public String getDistributionCenterName() {
		return distributionCenterName;
	}

	public void setDistributionCenterName(String distributionCenterName) {
		this.distributionCenterName = distributionCenterName;
	}

	public Integer getSiteId() {
		return siteId;
	}

	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}

	public String getSiteCode() {
		return siteCode;
	}

	public void setSiteCode(String siteCode) {
		this.siteCode = siteCode;
	}

	public String getFeasibilityId() {
		return feasibilityId;
	}

	public void setFeasibilityId(String feasibilityId) {
		this.feasibilityId = feasibilityId;
	}

	public MfDetail getMfDetail() {
		return mfDetail;
	}

	public void setMfDetail(MfDetail mfDetail) {
		this.mfDetail = mfDetail;
	}

	public ScOrder getScOrder() {
		return scOrder;
	}

	public void setScOrder(ScOrder scOrder) {
		this.scOrder = scOrder;
	}

	public Set<NwaSuspensionReason> getNwaSuspensionReasons() {
		return nwaSuspensionReasons;
	}

	public void setNwaSuspensionReasons(Set<NwaSuspensionReason> nwaSuspensionReasons) {
		this.nwaSuspensionReasons = nwaSuspensionReasons;
	}

	public SiteDetail getSiteDetail() {
		return siteDetail;
	}

	public void setSiteDetail(SiteDetail siteDetail) {
		this.siteDetail = siteDetail;
	}

	public String getScenarioType() {
		return scenarioType;
	}

	public void setScenarioType(String scenarioType) {
		this.scenarioType = scenarioType;
	}

	public String getTechnologyType() {
		return technologyType;
	}

	public void setTechnologyType(String technologyType) {
		this.technologyType = technologyType;
	}

	public Set<NwaRejectionDetails> getNwaRejectionDetails() {
		return nwaRejectionDetails;
	}

	public void setNwaRejectionDetails(Set<NwaRejectionDetails> nwaRejectionDetails) {
		this.nwaRejectionDetails = nwaRejectionDetails;
	}
}
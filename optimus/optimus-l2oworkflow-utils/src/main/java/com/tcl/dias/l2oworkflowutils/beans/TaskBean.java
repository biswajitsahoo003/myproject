package com.tcl.dias.l2oworkflowutils.beans;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.tcl.dias.common.beans.MfDetailsBean;
import com.tcl.dias.common.beans.SiteDetail;
import com.tcl.dias.l2oworkflow.entity.entities.Task;

import javax.persistence.Column;

import org.apache.commons.lang3.StringUtils;

/**
 * @author vivek
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited used to assign the task details
 *
 */
public class TaskBean {

	private String assignee;

	private Integer taskId;

	private String catagory;

	private Timestamp claimTime;

	private Timestamp completedTime;

	private Timestamp createdTime;

	private Timestamp duedate;

	private String orderCode;
	
	private String quoteCreatedUserType;

	private Float priority = 0.0F;

	private Integer scOrderId;

	private Integer serviceId;

	private String serviceCode;

	private String taskDefKey;

	private String status;

	private Timestamp updatedTime;

	private String wfProcessInstId;

	private String wfTaskId;

	private String assignedGroup;

	private String buttonLabel;

	private String formKey;

	private String isCustomerTask;

	private String isManualTask;

	private String name;

	private String ownerGroup;

	private String reminderCycle;

	private String description;

	private Integer tat;

	private String serviceType;

	private String orderType;

	private String city;

	private String title;

	private String waitTime;

	private Timestamp serviceDeliveryDate;

	private TaskDataBean taskDataBeans;

	private boolean isNonEditable;

	Map<String, Object> commonData;

	private List<TaskAssignmentBean> taskAssignments;

	private boolean isReschedule;

	private String quoteCode;

	private Integer quoteId;

	private String siteCode;

	private Integer siteId;
	
	private boolean isTaskDelayed;
	private Integer locationId;
	
	private List<SiteDetail> siteDetail;
	
	private Integer delayDays;
	
	private Timestamp actualEndTime;

	private Timestamp actualStartTime;

	private Timestamp estimatedEndTime;

	private Timestamp estimatedStartTime;
	

	private Timestamp targettedEndTime;

	private Timestamp targettedStartTime;

	private Timestamp plannedEndTime;

	private Timestamp plannedStartTime;
	
	private String quoteType;
	
	private String contractTerm;
	
	private String accountName;
	
	private String quoteCreatedBy;
	
	private String action;
	
	private MfDetailsBean mfDetail;
	
	private MfTaskDetailBean mfTaskDetailBean;

	private String remarks;
	
	private String reqComments;
	
	private String feasibilityId;
	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Integer getDelayDays() {
		return delayDays;
	}

	public void setDelayDays(Integer delayDays) {
		this.delayDays = delayDays;
	}

	public Timestamp getActualEndTime() {
		return actualEndTime;
	}

	public void setActualEndTime(Timestamp actualEndTime) {
		this.actualEndTime = actualEndTime;
	}

	public Timestamp getActualStartTime() {
		return actualStartTime;
	}

	public void setActualStartTime(Timestamp actualStartTime) {
		this.actualStartTime = actualStartTime;
	}

	public Timestamp getEstimatedEndTime() {
		return estimatedEndTime;
	}

	public void setEstimatedEndTime(Timestamp estimatedEndTime) {
		this.estimatedEndTime = estimatedEndTime;
	}

	public Timestamp getEstimatedStartTime() {
		return estimatedStartTime;
	}

	public void setEstimatedStartTime(Timestamp estimatedStartTime) {
		this.estimatedStartTime = estimatedStartTime;
	}

	public Timestamp getTargettedEndTime() {
		return targettedEndTime;
	}

	public void setTargettedEndTime(Timestamp targettedEndTime) {
		this.targettedEndTime = targettedEndTime;
	}

	public Timestamp getTargettedStartTime() {
		return targettedStartTime;
	}

	public void setTargettedStartTime(Timestamp targettedStartTime) {
		this.targettedStartTime = targettedStartTime;
	}

	public Timestamp getPlannedEndTime() {
		return plannedEndTime;
	}

	public void setPlannedEndTime(Timestamp plannedEndTime) {
		this.plannedEndTime = plannedEndTime;
	}

	public Timestamp getPlannedStartTime() {
		return plannedStartTime;
	}

	public void setPlannedStartTime(Timestamp plannedStartTime) {
		this.plannedStartTime = plannedStartTime;
	}

	public boolean isTaskDelayed() {
		return isTaskDelayed;
	}

	public void setTaskDelayed(boolean isTaskDelayed) {
		this.isTaskDelayed = isTaskDelayed;
	}

	public boolean isNonEditable() {
		return isNonEditable;
	}

	public void setNonEditable(boolean isNonEditable) {
		this.isNonEditable = isNonEditable;
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

	public String getSiteCode() {
		return siteCode;
	}

	public void setSiteCode(String siteCode) {
		this.siteCode = siteCode;
	}

	public Integer getSiteId() {
		return siteId;
	}

	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}

	public boolean isReschedule() {
		return isReschedule;
	}

	public void setReschedule(boolean isReschedule) {
		this.isReschedule = isReschedule;
	}

	public List<TaskAssignmentBean> getTaskAssignments() {
		if (taskAssignments == null) {
			taskAssignments = new ArrayList<>();
		}
		return taskAssignments;
	}

	public void setTaskAssignments(List<TaskAssignmentBean> taskAssignments) {
		this.taskAssignments = taskAssignments;
	}

	public Map<String, Object> getCommonData() {
		return commonData;
	}

	public void setCommonData(Map<String, Object> commonData) {
		this.commonData = commonData;
	}

	public String getAssignee() {
		return assignee;
	}

	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}

	public String getCatagory() {
		return catagory;
	}

	public void setCatagory(String catagory) {
		this.catagory = catagory;
	}

	public Timestamp getClaimTime() {
		return claimTime;
	}

	public void setClaimTime(Timestamp claimTime) {
		this.claimTime = claimTime;
	}

	public Timestamp getCompletedTime() {
		return completedTime;
	}

	public void setCompletedTime(Timestamp completedTime) {
		this.completedTime = completedTime;
	}

	public Timestamp getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Timestamp createdTime) {
		this.createdTime = createdTime;
	}

	public Timestamp getDuedate() {
		return duedate;
	}

	public void setDuedate(Timestamp duedate) {
		this.duedate = duedate;
	}

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public Float getPriority() {
		return priority;
	}

	public void setPriority(Float priority) {
		this.priority = priority;
	}

	public Integer getScOrderId() {
		return scOrderId;
	}

	public void setScOrderId(Integer scOrderId) {
		this.scOrderId = scOrderId;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Timestamp getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(Timestamp updatedTime) {
		this.updatedTime = updatedTime;
	}

	public String getWfProcessInstId() {
		return wfProcessInstId;
	}

	public void setWfProcessInstId(String wfProcessInstId) {
		this.wfProcessInstId = wfProcessInstId;
	}

	public String getWfTaskId() {
		return wfTaskId;
	}

	public void setWfTaskId(String wfTaskId) {
		this.wfTaskId = wfTaskId;
	}

	public Integer getTaskId() {
		return taskId;
	}

	public void setTaskId(Integer taskId) {
		this.taskId = taskId;
	}

	public String getAssignedGroup() {
		return assignedGroup;
	}

	public void setAssignedGroup(String assignedGroup) {
		this.assignedGroup = assignedGroup;
	}

	public String getButtonLabel() {
		return buttonLabel;
	}

	public void setButtonLabel(String buttonLabel) {
		this.buttonLabel = buttonLabel;
	}

	public String getFormKey() {
		return formKey;
	}

	public void setFormKey(String formKey) {
		this.formKey = formKey;
	}

	public String getIsCustomerTask() {
		return isCustomerTask;
	}

	public void setIsCustomerTask(String isCustomerTask) {
		this.isCustomerTask = isCustomerTask;
	}

	public String getIsManualTask() {
		return isManualTask;
	}

	public void setIsManualTask(String isManualTask) {
		this.isManualTask = isManualTask;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOwnerGroup() {
		return ownerGroup;
	}

	public void setOwnerGroup(String ownerGroup) {
		this.ownerGroup = ownerGroup;
	}

	public String getReminderCycle() {
		return reminderCycle;
	}

	public void setReminderCycle(String reminderCycle) {
		this.reminderCycle = reminderCycle;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getTat() {
		return tat;
	}

	public void setTat(Integer tat) {
		this.tat = tat;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getWaitTime() {
		return waitTime;
	}

	public void setWaitTime(String waitTime) {
		this.waitTime = waitTime;
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

	public String getTaskDefKey() {
		return taskDefKey;
	}

	public void setTaskDefKey(String taskDefKey) {
		this.taskDefKey = taskDefKey;
	}

	public Timestamp getServiceDeliveryDate() {
		return serviceDeliveryDate;
	}

	public void setServiceDeliveryDate(Timestamp serviceDeliveryDate) {
		this.serviceDeliveryDate = serviceDeliveryDate;
	}

	public Integer getLocationId() {
		return locationId;
	}

	public void setLocationId(Integer locationId) {
		this.locationId = locationId;
	}
	
	public String getReqComments() {
		return reqComments;
	}

	public void setReqComments(String reqComments) {
		this.reqComments = reqComments;
	}


	public static TaskBean mapToBean(Task task) {
		TaskBean taskBean = new TaskBean();
		taskBean.setTaskId(task.getId());
		taskBean.setCatagory(task.getCatagory());
		taskBean.setClaimTime(task.getClaimTime());
		taskBean.setCompletedTime(task.getCompletedTime());
		taskBean.setCreatedTime(task.getCreatedTime());
		taskBean.setDuedate(task.getDuedate());
		taskBean.setOrderCode(task.getOrderCode());
		taskBean.setServiceId(task.getServiceId());
		taskBean.setServiceCode(task.getServiceCode());
		taskBean.setStatus(task.getMstStatus().getCode());
		taskBean.setUpdatedTime(task.getUpdatedTime());
		taskBean.setWfProcessInstId(task.getWfProcessInstId());
		taskBean.setWfTaskId(task.getWfTaskId());
		taskBean.setCity(task.getCity());
		taskBean.setOrderType(task.getOrderType());
		taskBean.setServiceType(task.getServiceType());
		taskBean.setQuoteId(task.getQuoteId());
		taskBean.setQuoteCode(task.getQuoteCode());
		if(task.getMfDetail()!=null) {
			taskBean.setQuoteCreatedUserType(task.getMfDetail().getQuoteCreatedUserType());	
		}
		if(task.getSiteDetail()!=null && StringUtils.isBlank(taskBean.getQuoteCreatedUserType())) {
			taskBean.setQuoteCreatedUserType(task.getSiteDetail().getQuoteCreatedUserType());	
		}
		if (task.getMstTaskDef() != null) {
			taskBean.setButtonLabel(task.getMstTaskDef().getButtonLabel());
			taskBean.setDescription(task.getMstTaskDef().getDescription());
			taskBean.setTitle(task.getMstTaskDef().getTitle());
			taskBean.setAssignedGroup(task.getMstTaskDef().getAssignedGroup());
			taskBean.setTaskDefKey(task.getMstTaskDef().getKey());
			taskBean.setName(task.getMstTaskDef().getName());
		}
		
		return taskBean;
	}

	public TaskDataBean getTaskDataBeans() {
		return taskDataBeans;
	}

	public void setTaskDataBeans(TaskDataBean taskDataBeans) {
		this.taskDataBeans = taskDataBeans;
	}

	public List<SiteDetail> getSiteDetail() {
		return siteDetail;
	}

	public void setSiteDetail(List<SiteDetail> siteDetail) {
		this.siteDetail = siteDetail;
	}

	public String getQuoteType() {
		return quoteType;
	}

	public void setQuoteType(String quoteType) {
		this.quoteType = quoteType;
	}

	public String getContractTerm() {
		return contractTerm;
	}

	public void setContractTerm(String contractTerm) {
		this.contractTerm = contractTerm;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getQuoteCreatedBy() {
		return quoteCreatedBy;
	}

	public void setQuoteCreatedBy(String quoteCreatedBy) {
		this.quoteCreatedBy = quoteCreatedBy;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public MfDetailsBean getMfDetail() {
		return mfDetail;
	}

	public void setMfDetail(MfDetailsBean mfDetail) {
		this.mfDetail = mfDetail;
	}

	public MfTaskDetailBean getMfTaskDetailBean() {
		return mfTaskDetailBean;
	}

	public void setMfTaskDetailBean(MfTaskDetailBean mfTaskDetailBean) {
		this.mfTaskDetailBean = mfTaskDetailBean;
	}

	public String getFeasibilityId() {
		return feasibilityId;
	}

	public void setFeasibilityId(String feasibilityId) {
		this.feasibilityId = feasibilityId;
	}

	public String getQuoteCreatedUserType() {
		return quoteCreatedUserType;
	}

	public void setQuoteCreatedUserType(String quoteCreatedUserType) {
		this.quoteCreatedUserType = quoteCreatedUserType;
	}
	
	@Override
	public String toString() {
		return "TaskBean{" +
				"assignee='" + assignee + '\'' +
				", taskId=" + taskId +
				", catagory='" + catagory + '\'' +
				", claimTime=" + claimTime +
				", completedTime=" + completedTime +
				", createdTime=" + createdTime +
				", duedate=" + duedate +
				", orderCode='" + orderCode + '\'' +
				", quoteCreatedUserType='" + quoteCreatedUserType + '\'' +
				", priority=" + priority +
				", scOrderId=" + scOrderId +
				", serviceId=" + serviceId +
				", serviceCode='" + serviceCode + '\'' +
				", taskDefKey='" + taskDefKey + '\'' +
				", status='" + status + '\'' +
				", updatedTime=" + updatedTime +
				", wfProcessInstId='" + wfProcessInstId + '\'' +
				", wfTaskId='" + wfTaskId + '\'' +
				", assignedGroup='" + assignedGroup + '\'' +
				", buttonLabel='" + buttonLabel + '\'' +
				", formKey='" + formKey + '\'' +
				", isCustomerTask='" + isCustomerTask + '\'' +
				", isManualTask='" + isManualTask + '\'' +
				", name='" + name + '\'' +
				", ownerGroup='" + ownerGroup + '\'' +
				", reminderCycle='" + reminderCycle + '\'' +
				", description='" + description + '\'' +
				", tat=" + tat +
				", serviceType='" + serviceType + '\'' +
				", orderType='" + orderType + '\'' +
				", city='" + city + '\'' +
				", title='" + title + '\'' +
				", waitTime='" + waitTime + '\'' +
				", serviceDeliveryDate=" + serviceDeliveryDate +
				", taskDataBeans=" + taskDataBeans +
				", isNonEditable=" + isNonEditable +
				", commonData=" + commonData +
				", taskAssignments=" + taskAssignments +
				", isReschedule=" + isReschedule +
				", quoteCode='" + quoteCode + '\'' +
				", quoteId=" + quoteId +
				", siteCode='" + siteCode + '\'' +
				", siteId=" + siteId +
				", isTaskDelayed=" + isTaskDelayed +
				", locationId=" + locationId +
				", siteDetail=" + siteDetail +
				", delayDays=" + delayDays +
				", actualEndTime=" + actualEndTime +
				", actualStartTime=" + actualStartTime +
				", estimatedEndTime=" + estimatedEndTime +
				", estimatedStartTime=" + estimatedStartTime +
				", targettedEndTime=" + targettedEndTime +
				", targettedStartTime=" + targettedStartTime +
				", plannedEndTime=" + plannedEndTime +
				", plannedStartTime=" + plannedStartTime +
				", quoteType='" + quoteType + '\'' +
				", contractTerm='" + contractTerm + '\'' +
				", accountName='" + accountName + '\'' +
				", quoteCreatedBy='" + quoteCreatedBy + '\'' +
				", action='" + action + '\'' +
				", mfDetail=" + mfDetail +
				", mfTaskDetailBean=" + mfTaskDetailBean +
				", remarks='" + remarks + '\'' +
				", reqComments='" + reqComments + '\'' +
				", feasibilityId='" + feasibilityId + '\'' +
				'}';
	}



}

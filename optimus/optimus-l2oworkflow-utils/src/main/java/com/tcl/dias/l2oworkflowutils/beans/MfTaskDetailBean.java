package com.tcl.dias.l2oworkflowutils.beans;

import java.io.Serializable;
import java.sql.Timestamp;

import com.tcl.dias.l2oworkflow.entity.entities.MfTaskDetail;

public class MfTaskDetailBean implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1484023631601298023L;
	private Integer id;
	private String subject;
	private String requestorComments;
	private String assignedTo;
	private String assignedFrom;
	private Integer siteId;
	private Integer taskId;
	private String responderComments;
	private String status;
	private String reason;
	private Timestamp createdTime;
	private Timestamp updatedTime;
	private String assignedGroup;
	private String feasibilityId;
	private MfTaskData taskData;
	
	// Added for NPL
			private String taskRelatedTo;
			public String getTaskRelatedTo() {
				return taskRelatedTo;
			}
			public void setTaskRelatedTo(String taskRelatedTo) {
				this.taskRelatedTo = taskRelatedTo;
			}
		

	public MfTaskDetailBean() {
		// Do Nothing
	}

	public MfTaskDetailBean(MfTaskDetail mfTaskDetail) {
		if (mfTaskDetail != null) {
			this.id = mfTaskDetail.getId();
			this.subject = mfTaskDetail.getSubject();
			this.requestorComments = mfTaskDetail.getRequestorComments();
			this.assignedTo = mfTaskDetail.getAssignedTo();
			this.assignedFrom = mfTaskDetail.getAssignedFrom();
			this.siteId = mfTaskDetail.getSiteId();
			this.taskId = mfTaskDetail.getTask().getId();
			this.responderComments = mfTaskDetail.getResponderComments();
			this.status = mfTaskDetail.getStatus();
			this.createdTime = mfTaskDetail.getTask().getCreatedTime();
			this.updatedTime = mfTaskDetail.getTask().getUpdatedTime();
			this.assignedGroup = mfTaskDetail.getAssignedGroup();
			this.reason = mfTaskDetail.getReason();
			
		}

	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getRequestorComments() {
		return requestorComments;
	}

	public void setRequestorComments(String requestorComments) {
		this.requestorComments = requestorComments;
	}

	public String getAssignedTo() {
		return assignedTo;
	}

	public void setAssignedTo(String assignedTo) {
		this.assignedTo = assignedTo;
	}

	public String getAssignedFrom() {
		return assignedFrom;
	}

	public void setAssignedFrom(String assignedFrom) {
		this.assignedFrom = assignedFrom;
	}

	public Integer getSiteId() {
		return siteId;
	}

	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}

	public Integer getTaskId() {
		return taskId;
	}

	public void setTaskId(Integer taskId) {
		this.taskId = taskId;
	}

	public String getResponderComments() {
		return responderComments;
	}

	public void setResponderComments(String responderComments) {
		this.responderComments = responderComments;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public Timestamp getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Timestamp createdTime) {
		this.createdTime = createdTime;
	}

	public Timestamp getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(Timestamp updatedTime) {
		this.updatedTime = updatedTime;
	}

	public String getAssignedGroup() {
		return assignedGroup;
	}

	public void setAssignedGroup(String assignedGroup) {
		this.assignedGroup = assignedGroup;
	}

	public String getFeasibilityId() {
		return feasibilityId;
	}

	public void setFeasibilityId(String feasibilityId) {
		this.feasibilityId = feasibilityId;
	}

	public MfTaskData getTaskData() {
		return taskData;
	}

	public void setTaskData(MfTaskData taskData) {
		this.taskData = taskData;
	}

}

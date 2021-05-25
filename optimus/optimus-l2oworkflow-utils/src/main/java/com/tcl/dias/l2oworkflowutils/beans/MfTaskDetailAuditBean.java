package com.tcl.dias.l2oworkflowutils.beans;

import java.io.Serializable;
import java.sql.Timestamp;

import com.tcl.dias.l2oworkflow.entity.entities.MfTaskDetailAudit;

public class MfTaskDetailAuditBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private Integer mfTaskDetailId;
	private String subject;
	private String requestorComments;
	private Integer taskId;
	private String feasibilityId;
	private String responderComments;
	private String status;
	private String reason;
	private Integer requestorTaskId;
	private String prvComments;
	private String prvStatus;
	private Timestamp createdTime;
	private String createdBy;
	private MfTaskData taskData;
	private String assignedTo;
	private String assignedFrom;
	
	public MfTaskDetailAuditBean() {
		
	}

	public MfTaskDetailAuditBean(MfTaskDetailAudit mfTaskDetailAudit) {
		this.id = mfTaskDetailAudit.getId();
		this.mfTaskDetailId =  mfTaskDetailAudit.getMfTaskDetailId();
		this.subject =  mfTaskDetailAudit.getSubject();
		this.requestorComments =  mfTaskDetailAudit.getRequestorComments();
		this.taskId =  mfTaskDetailAudit.getTaskId();
		this.feasibilityId =  mfTaskDetailAudit.getFeasibilityId();
		this.responderComments =  mfTaskDetailAudit.getResponderComments();
		this.status =  mfTaskDetailAudit.getStatus();
		this.reason =  mfTaskDetailAudit.getReason();
		this.requestorTaskId =  mfTaskDetailAudit.getRequestorTaskId();
		this.prvComments =  mfTaskDetailAudit.getPrvComments();
		this.prvStatus =  mfTaskDetailAudit.getPrvStatus();
		this.createdTime =  mfTaskDetailAudit.getCreatedTime();
		this.createdBy =  mfTaskDetailAudit.getCreatedBy();
		this.assignedFrom = mfTaskDetailAudit.getAssignedFrom();
		this.assignedTo = mfTaskDetailAudit.getAssignedTo();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getMfTaskDetailId() {
		return mfTaskDetailId;
	}

	public void setMfTaskDetailId(Integer mfTaskDetailId) {
		this.mfTaskDetailId = mfTaskDetailId;
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

	public Integer getTaskId() {
		return taskId;
	}

	public void setTaskId(Integer taskId) {
		this.taskId = taskId;
	}

	public String getFeasibilityId() {
		return feasibilityId;
	}

	public void setFeasibilityId(String feasibilityId) {
		this.feasibilityId = feasibilityId;
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

	public Integer getRequestorTaskId() {
		return requestorTaskId;
	}

	public void setRequestorTaskId(Integer requestorTaskId) {
		this.requestorTaskId = requestorTaskId;
	}

	public String getPrvComments() {
		return prvComments;
	}

	public void setPrvComments(String prvComments) {
		this.prvComments = prvComments;
	}

	public String getPrvStatus() {
		return prvStatus;
	}

	public void setPrvStatus(String prvStatus) {
		this.prvStatus = prvStatus;
	}

	public Timestamp getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Timestamp createdTime) {
		this.createdTime = createdTime;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public MfTaskData getTaskData() {
		return taskData;
	}

	public void setTaskData(MfTaskData taskData) {
		this.taskData = taskData;
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
	

}

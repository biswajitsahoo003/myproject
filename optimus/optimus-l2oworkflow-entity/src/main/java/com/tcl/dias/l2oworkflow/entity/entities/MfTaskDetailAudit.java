package com.tcl.dias.l2oworkflow.entity.entities;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name="mf_task_detail_audit")
@NamedQuery(name = "MfTaskDetailAudit.findAll", query = "SELECT m FROM MfTaskDetailAudit m")
public class MfTaskDetailAudit implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name="mf_task_detail_id")
	private Integer mfTaskDetailId;
	
	@Column(name="subject")
	private String subject;
	
	@Column(name="requestor_comments")
	private String requestorComments;
		
	@Column(name="task_id")
	private Integer taskId;
	
	@Column(name="feasibility_id")
	private String feasibilityId;
	
	@Column(name="responder_comments")
	private String responderComments;
	
	@Column(name="status")
	private String status;
	
	@Column(name="reason")
	private String reason;
	
	@Column(name="requestor_task_id")
	private Integer requestorTaskId;
	
	@Column(name="prv_comments")
	private String prvComments;

	@Column(name="prv_status")
	private String prvStatus;
	
	@Column(name="created_time")
	private Timestamp createdTime;
	
	@Column(name="created_by")
	private String createdBy;
	
	@Column(name="task_data")
	private String taskData;
	
	@Column(name="assigned_to")
	private String assignedTo;
	
	@Column(name="assigned_from")
	private String assignedFrom;
	
	public MfTaskDetailAudit() {
		
	}
	
	public MfTaskDetailAudit(MfTaskDetail mftaskDetail) {
		this.setMfTaskDetailId(mftaskDetail.getId());
		this.setSubject(mftaskDetail.getSubject());
		this.setTaskId(mftaskDetail.getTask().getId());
		this.setFeasibilityId(mftaskDetail.getTask().getFeasibilityId());
		this.setRequestorTaskId(mftaskDetail.getRequestorTaskId());
		this.setRequestorComments(mftaskDetail.getRequestorComments());
		this.setResponderComments(mftaskDetail.getResponderComments());
		this.setPrvComments(mftaskDetail.getPrvComments());
		this.setPrvStatus(mftaskDetail.getPrvStatus());
		this.setStatus(mftaskDetail.getStatus());
		this.setReason(mftaskDetail.getReason());
		this.setCreatedTime(new Timestamp (System.currentTimeMillis()));
		this.setTaskData(mftaskDetail.getTaskData());
		this.setAssignedFrom(mftaskDetail.getAssignedFrom());
		this.setAssignedTo(mftaskDetail.getAssignedTo());
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

	public String getTaskData() {
		return taskData;
	}

	public void setTaskData(String taskData) {
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

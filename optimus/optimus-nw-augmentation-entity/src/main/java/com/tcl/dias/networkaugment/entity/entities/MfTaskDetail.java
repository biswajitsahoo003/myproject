package com.tcl.dias.networkaugment.entity.entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "mf_task_detail")
@NamedQuery(name = "MfTaskDetail.findAll", query = "SELECT m FROM MfTaskDetail m")
public class MfTaskDetail implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name="subject")
	private String subject;
	
	@Column(name="requestor_comments")
	private String requestorComments;
	
	@Column(name="assigned_to")
	private String assignedTo;
	
	@Column(name="assigned_from")
	private String assignedFrom;
	
	@Column(name="site_id")
	private Integer siteId;
	
	@Column(name="quote_id")
	private Integer quoteId;
		
	@ManyToOne
	@JoinColumn(name="task_id")
	private Task task;
	
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
	
	@Column(name="assigned_group")
	private String assignedGroup;
	
	@Column(name="task_data")
	private String taskData;
	
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

	public String getAssignedTo() {
		return assignedTo;
	}

	public void setAssignedTo(String assignedTo) {
		this.assignedTo = assignedTo;
	}

	public String getRequestorComments() {
		return requestorComments;
	}

	public void setRequestorComments(String requestorComments) {
		this.requestorComments = requestorComments;
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

	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
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

	public Integer getQuoteId() {
		return quoteId;
	}

	public void setQuoteId(Integer quoteId) {
		this.quoteId = quoteId;
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

	public String getAssignedGroup() {
		return assignedGroup;
	}

	public void setAssignedGroup(String assignedGroup) {
		this.assignedGroup = assignedGroup;
	}

	public String getTaskData() {
		return taskData;
	}

	public void setTaskData(String taskData) {
		this.taskData = taskData;
	}
	
	
}

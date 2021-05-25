package com.tcl.dias.l2oworkflowutils.beans;


public class MfTaskRequestBean {

	private String subject;
	private String assignedTo;
	private String assignedFrom;
	private String requestorComments;
	private String responderComments;
	private Integer siteId;
	private Integer quoteId;
	private Integer taskId;
	private String region;
	private String groupFrom;
	
	// Added for NPL
		private String taskRelatedTo;
		public String getTaskRelatedTo() {
			return taskRelatedTo;
		}
		public void setTaskRelatedTo(String taskRelatedTo) {
			this.taskRelatedTo = taskRelatedTo;
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
	public String getRequestorComments() {
		return requestorComments;
	}
	public void setRequestorComments(String requestorComments) {
		this.requestorComments = requestorComments;
	}
	public String getResponderComments() {
		return responderComments;
	}
	public void setResponderComments(String responderComments) {
		this.responderComments = responderComments;
	}
	public Integer getTaskId() {
		return taskId;
	}
	public void setTaskId(Integer taskId) {
		this.taskId = taskId;
	}
	public Integer getQuoteId() {
		return quoteId;
	}
	public void setQuoteId(Integer quoteId) {
		this.quoteId = quoteId;
	}
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	public String getGroupFrom() {
		return groupFrom;
	}
	public void setGroupFrom(String groupFrom) {
		this.groupFrom = groupFrom;
	}
	
	
	
}

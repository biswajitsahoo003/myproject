package com.tcl.dias.l2oworkflowutils.beans;

import java.sql.Timestamp;

public class OmsTaskDetailBean {

	// FOR NPL
	private String linkId;
	
	private String siteCode;
	private Integer taskId; 
	private String feasibilityId;
	private String feasibilityStatus;
	private String reason;
	private String remarks;
	private String team;
	
	private String assignedTo;
	private String assignedFrom;
	private Timestamp assignedOn;
	private Timestamp claimedOn;
	private Timestamp updatedOn;
	private String siteType;
	private String tastRealatedTo;
	
	public String getLinkId() {
		return linkId;
	}
	public void setLinkId(String linkId) {
		this.linkId = linkId;
	}
	public String getTastRealatedTo() {
		return tastRealatedTo;
	}
	public void setTastRealatedTo(String tastRealatedTo) {
		this.tastRealatedTo = tastRealatedTo;
	}
	public String getSiteCode() {
		return siteCode;
	}
	public void setSiteCode(String siteCode) {
		this.siteCode = siteCode;
	}
	public Integer getTaskId() {
		return taskId;
	}
	public void setTaskId(Integer taskId) {
		this.taskId = taskId;
	}
	public String getFeasibilityStatus() {
		return feasibilityStatus;
	}
	public void setFeasibilityStatus(String feasibilityStatus) {
		this.feasibilityStatus = feasibilityStatus;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getTeam() {
		return team;
	}
	public void setTeam(String team) {
		this.team = team;
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
	public Timestamp getAssignedOn() {
		return assignedOn;
	}
	public void setAssignedOn(Timestamp assignedOn) {
		this.assignedOn = assignedOn;
	}
	public Timestamp getUpdatedOn() {
		return updatedOn;
	}
	public void setUpdatedOn(Timestamp updatedOn) {
		this.updatedOn = updatedOn;
	}
	public String getFeasibilityId() {
		return feasibilityId;
	}
	public void setFeasibilityId(String feasibilityId) {
		this.feasibilityId = feasibilityId;
	}
	public Timestamp getClaimedOn() {
		return claimedOn;
	}
	public void setClaimedOn(Timestamp claimedOn) {
		this.claimedOn = claimedOn;
	}
	public String getSiteType() {
		return siteType;
	}
	public void setSiteType(String siteType) {
		this.siteType = siteType;
	}
	
	
	
}

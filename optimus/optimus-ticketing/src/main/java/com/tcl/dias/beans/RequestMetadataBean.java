package com.tcl.dias.beans;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({ "State", "Assignment Group", "Assigned To", "Requested By", "Channel", "Change Type", "Category",
		"Subcategory", "Created", "Created By", "Updated", "Updated By", "Approval Waiting On" })
public class RequestMetadataBean {

	@JsonProperty("State")
	private String state;
	@JsonProperty("Assignment Group")
	private String assignmentGroup;
	@JsonProperty("Assigned To")
	private String assignedTo;
	@JsonProperty("Requested By")
	private String requestedBy;
	@JsonProperty("Channel")
	private String channel;
	@JsonProperty("Change Type")
	private String changeType;
	@JsonProperty("Category")
	private String category;
	@JsonProperty("Subcategory")
	private String subcategory;
	@JsonProperty("Created")
	private String created;
	@JsonProperty("Created By")
	private String createdBy;
	@JsonProperty("Updated")
	private String updated;
	@JsonProperty("Updated By")
	private String updatedBy;
	@JsonProperty("Approval Waiting On")
	private String approvalWaitingOn;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("State")
	public String getState() {
		return state;
	}

	@JsonProperty("State")
	public void setState(String state) {
		this.state = state;
	}

	@JsonProperty("Assignment Group")
	public String getAssignmentGroup() {
		return assignmentGroup;
	}

	@JsonProperty("Assignment Group")
	public void setAssignmentGroup(String assignmentGroup) {
		this.assignmentGroup = assignmentGroup;
	}

	@JsonProperty("Assigned To")
	public String getAssignedTo() {
		return assignedTo;
	}

	@JsonProperty("Assigned To")
	public void setAssignedTo(String assignedTo) {
		this.assignedTo = assignedTo;
	}

	@JsonProperty("Requested By")
	public String getRequestedBy() {
		return requestedBy;
	}

	@JsonProperty("Requested By")
	public void setRequestedBy(String requestedBy) {
		this.requestedBy = requestedBy;
	}

	@JsonProperty("Channel")
	public String getChannel() {
		return channel;
	}

	@JsonProperty("Channel")
	public void setChannel(String channel) {
		this.channel = channel;
	}

	@JsonProperty("Change Type")
	public String getChangeType() {
		return changeType;
	}

	@JsonProperty("Change Type")
	public void setChangeType(String changeType) {
		this.changeType = changeType;
	}

	@JsonProperty("Category")
	public String getCategory() {
		return category;
	}

	@JsonProperty("Category")
	public void setCategory(String category) {
		this.category = category;
	}

	@JsonProperty("Subcategory")
	public String getSubcategory() {
		return subcategory;
	}

	@JsonProperty("Subcategory")
	public void setSubcategory(String subcategory) {
		this.subcategory = subcategory;
	}

	@JsonProperty("Created")
	public String getCreated() {
		return created;
	}

	@JsonProperty("Created")
	public void setCreated(String created) {
		this.created = created;
	}

	@JsonProperty("Created By")
	public String getCreatedBy() {
		return createdBy;
	}

	@JsonProperty("Created By")
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	@JsonProperty("Updated")
	public String getUpdated() {
		return updated;
	}

	@JsonProperty("Updated")
	public void setUpdated(String updated) {
		this.updated = updated;
	}

	@JsonProperty("Updated By")
	public String getUpdatedBy() {
		return updatedBy;
	}

	@JsonProperty("Updated By")
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	@JsonProperty("Approval Waiting On")
	public String getApprovalWaitingOn() {
		return approvalWaitingOn;
	}

	@JsonProperty("Approval Waiting On")
	public void setApprovalWaitingOn(String approvalWaitingOn) {
		this.approvalWaitingOn = approvalWaitingOn;
	}

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

}

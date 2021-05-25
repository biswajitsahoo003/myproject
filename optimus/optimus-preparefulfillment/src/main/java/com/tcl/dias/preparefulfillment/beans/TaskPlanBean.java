package com.tcl.dias.preparefulfillment.beans;

import java.sql.Timestamp;

public class TaskPlanBean {
	
	private Integer taskPlanId;
	private Integer scOrderId;
	private Integer serviceId;
	private Timestamp estimatedStartTime;
	private Timestamp estimatedEndTime;
	private Timestamp targetedStartTime;
	private Timestamp targetedEndTime;
	private Timestamp actualStartTime;
	private Timestamp actualEndTime;
	private Boolean isDelayed;
	private Boolean customerView;
	private Integer sequence;
	private String status;
	private String name;
	private Timestamp plannedStartTime;
	private Timestamp plannedEndTime;
	private boolean isCustomerTask;
	private String siteType;
	private String taskKey;

	private boolean adminView;
	
	
	
	
	public String getSiteType() {
		return siteType;
	}
	public void setSiteType(String siteType) {
		this.siteType = siteType;
	}
	public boolean isAdminView() {
		return adminView;
	}
	public void setAdminView(boolean adminView) {
		this.adminView = adminView;
	}
	public Integer getTaskPlanId() {
		return taskPlanId;
	}
	public void setTaskPlanId(Integer taskPlanId) {
		this.taskPlanId = taskPlanId;
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
	public Timestamp getEstimatedStartTime() {
		return estimatedStartTime;
	}
	public void setEstimatedStartTime(Timestamp estimatedStartTime) {
		this.estimatedStartTime = estimatedStartTime;
	}
	public Timestamp getEstimatedEndTime() {
		return estimatedEndTime;
	}
	public void setEstimatedEndTime(Timestamp estimatedEndTime) {
		this.estimatedEndTime = estimatedEndTime;
	}
	public Timestamp getTargetedStartTime() {
		return targetedStartTime;
	}
	public void setTargetedStartTime(Timestamp targetedStartTime) {
		this.targetedStartTime = targetedStartTime;
	}
	public Timestamp getTargetedEndTime() {
		return targetedEndTime;
	}
	public void setTargetedEndTime(Timestamp targetedEndTime) {
		this.targetedEndTime = targetedEndTime;
	}
	public Timestamp getActualStartTime() {
		return actualStartTime;
	}
	public void setActualStartTime(Timestamp actualStartTime) {
		this.actualStartTime = actualStartTime;
	}
	public Timestamp getActualEndTime() {
		return actualEndTime;
	}
	public void setActualEndTime(Timestamp actualEndTime) {
		this.actualEndTime = actualEndTime;
	}
	public Boolean getIsDelayed() {
		return isDelayed;
	}
	public void setIsDelayed(Boolean isDelayed) {
		this.isDelayed = isDelayed;
	}
	public Boolean getCustomerView() {
		return customerView;
	}
	public void setCustomerView(Boolean customerView) {
		this.customerView = customerView;
	}
	public Integer getSequence() {
		return sequence;
	}
	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Timestamp getPlannedStartTime() {
		return plannedStartTime;
	}
	public void setPlannedStartTime(Timestamp plannedStartTime) {
		this.plannedStartTime = plannedStartTime;
	}
	public Timestamp getPlannedEndTime() {
		return plannedEndTime;
	}
	public void setPlannedEndTime(Timestamp plannedEndTime) {
		this.plannedEndTime = plannedEndTime;
	}
	public boolean isCustomerTask() {
		return isCustomerTask;
	}
	public void setCustomerTask(boolean isCustomerTask) {
		this.isCustomerTask = isCustomerTask;
	}

	public String getTaskKey() { return taskKey; }

	public void setTaskKey(String taskKey) {
		this.taskKey = taskKey;
	}
}

package com.tcl.dias.networkaugmentation.beans;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * This file contains the ProcessPlanBean.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class ProcessPlanBean {

	private Integer processPlanId;
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
	private String name;
	
	private String processKey;

	private String status;
	private String siteType;
	
	private List<ActivityPlanBean> activityPlans;
	
	private boolean adminView;
	
	
	
	

	
	
	
	public String getProcessKey() {
		return processKey;
	}

	public void setProcessKey(String processKey) {
		this.processKey = processKey;
	}

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

	private Timestamp plannedStartTime;
	private Timestamp plannedEndTime;

	public Integer getProcessPlanId() {
		return processPlanId;
	}

	public void setProcessPlanId(Integer processPlanId) {
		this.processPlanId = processPlanId;
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

	public List<ActivityPlanBean> getActivityPlans() {

		if(activityPlans==null)
		{
			activityPlans=new ArrayList<>();
		}
		return activityPlans;
	}

	public void setActivityPlans(List<ActivityPlanBean> activityPlans) {
		this.activityPlans = activityPlans;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "ProcessPlanBean [processPlanId=" + processPlanId + ", scOrderId=" + scOrderId + ", serviceId="
				+ serviceId + ", estimatedStartTime=" + estimatedStartTime + ", estimatedEndTime=" + estimatedEndTime
				+ ", targetedStartTime=" + targetedStartTime + ", targetedEndTime=" + targetedEndTime
				+ ", actualStartTime=" + actualStartTime + ", actualEndTime=" + actualEndTime + ", isDelayed="
				+ isDelayed + ", customerView=" + customerView + ", sequence=" + sequence + ", activityPlans="
				+ activityPlans + "]";
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

	
}

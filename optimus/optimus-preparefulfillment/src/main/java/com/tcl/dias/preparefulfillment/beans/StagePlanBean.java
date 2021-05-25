package com.tcl.dias.preparefulfillment.beans;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * This file contains the StagePlanBean.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class StagePlanBean {

	private Integer stagePlanId;
	private Integer scOrderId;
	private Integer serviceId;
	private Timestamp estimatedStartTime;
	private Timestamp estimatedEndTime;
	private Timestamp targetedStartTime;
	private Timestamp targetedEndTime;
	private Timestamp actualStartTime;
	private String stageName;
	private String stageKey;

	private Timestamp actualEndTime;
	private String status;
	private Boolean isDelayed;
	private Boolean customerView;
	private List<ProcessPlanBean> processPlans;
	
	private boolean adminView;
	
	

	
	
	
	public String getStageKey() {
		return stageKey;
	}

	public void setStageKey(String stageKey) {
		this.stageKey = stageKey;
	}

	public boolean isAdminView() {
		return adminView;
	}

	public void setAdminView(boolean adminView) {
		this.adminView = adminView;
	}

	private Timestamp plannedStartTime;
	private Timestamp plannedEndTime;

	public Integer getStagePlanId() {
		return stagePlanId;
	}

	public void setStagePlanId(Integer stagePlanId) {
		this.stagePlanId = stagePlanId;
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

	public List<ProcessPlanBean> getProcessPlans() {
		
		if(processPlans==null) {
			processPlans=new ArrayList<ProcessPlanBean>();
		}
		return processPlans;
	}

	public void setProcessPlans(List<ProcessPlanBean> processPlans) {
		this.processPlans = processPlans;
	}

	public String getStageName() {
		return stageName;
	}

	public void setStageName(String stageName) {
		this.stageName = stageName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "StagePlanBean [stagePlanId=" + stagePlanId + ", scOrderId=" + scOrderId + ", serviceId=" + serviceId
				+ ", estimatedStartTime=" + estimatedStartTime + ", estimatedEndTime=" + estimatedEndTime
				+ ", targetedStartTime=" + targetedStartTime + ", targetedEndTime=" + targetedEndTime
				+ ", actualStartTime=" + actualStartTime + ", actualEndTime=" + actualEndTime + ", isDelayed="
				+ isDelayed + ", customerView=" + customerView + ", processPlans=" + processPlans + "]";
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

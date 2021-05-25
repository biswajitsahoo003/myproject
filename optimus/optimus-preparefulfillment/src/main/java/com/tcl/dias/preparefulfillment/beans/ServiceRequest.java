package com.tcl.dias.preparefulfillment.beans;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

public class ServiceRequest {

	private Integer page;

	private Integer size;

	private List<String> ordeType;

	private List<String> orderCategory;

	private String activityKey;

	private String customerName;
	private List<String> lastMileScenario;

	private List<String> productName;

	private List<String> groupName;

	private List<String> status;

	private List<String> assignedPM;

	private String serviceCode;
	
	private String internationalServiceCode;

	private String orderCode;

	private Boolean isJeopardyTask = false;

	private List<String> serviceConfigurationStatus;

	private List<String> activationConfigStatus;

	private List<String> billingStatus;

	private List<String> orderSubCategory;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private String billStartDate;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private String billEndDate;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private String commissionedStartDate;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private String commissionedEndDate;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private String billingCompletionStartDate;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private String billingCompletionEndDate;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private String serviceConfigStartDate;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private String serviceConfigEndDate;

	public String getServiceConfigStartDate() {
		return serviceConfigStartDate;
	}

	public void setServiceConfigStartDate(String serviceConfigStartDate) {
		this.serviceConfigStartDate = serviceConfigStartDate;
	}

	public String getServiceConfigEndDate() {
		return serviceConfigEndDate;
	}

	public void setServiceConfigEndDate(String serviceConfigEndDate) {
		this.serviceConfigEndDate = serviceConfigEndDate;
	}

	public String getBillStartDate() {
		return billStartDate;
	}

	public void setBillStartDate(String billStartDate) {
		this.billStartDate = billStartDate;
	}

	public String getBillEndDate() {
		return billEndDate;
	}

	public void setBillEndDate(String billEndDate) {
		this.billEndDate = billEndDate;
	}

	public String getCommissionedStartDate() {
		return commissionedStartDate;
	}

	public void setCommissionedStartDate(String commissionedStartDate) {
		this.commissionedStartDate = commissionedStartDate;
	}

	public String getCommissionedEndDate() {
		return commissionedEndDate;
	}

	public void setCommissionedEndDate(String commissionedEndDate) {
		this.commissionedEndDate = commissionedEndDate;
	}

	public List<String> getServiceConfigurationStatus() {
		return serviceConfigurationStatus;
	}

	public void setServiceConfigurationStatus(List<String> serviceConfigurationStatus) {
		this.serviceConfigurationStatus = serviceConfigurationStatus;
	}

	public List<String> getActivationConfigStatus() {
		return activationConfigStatus;
	}

	public void setActivationConfigStatus(List<String> activationConfigStatus) {
		this.activationConfigStatus = activationConfigStatus;
	}

	public List<String> getBillingStatus() {
		return billingStatus;
	}

	public void setBillingStatus(List<String> billingStatus) {
		this.billingStatus = billingStatus;
	}

	public Boolean getIsJeopardyTask() {
		return isJeopardyTask;
	}

	public void setIsJeopardyTask(Boolean isJeopardyTask) {
		this.isJeopardyTask = isJeopardyTask;
	}

	public List<String> getGroupName() {
		return groupName;
	}

	public void setGroupName(List<String> groupName) {
		this.groupName = groupName;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public List<String> getOrdeType() {
		return ordeType;
	}

	public void setOrdeType(List<String> ordeType) {
		this.ordeType = ordeType;
	}

	public List<String> getOrderCategory() {
		return orderCategory;
	}

	public void setOrderCategory(List<String> orderCategory) {
		this.orderCategory = orderCategory;
	}

	public String getActivityKey() {
		return activityKey;
	}

	public void setActivityKey(String activityKey) {
		this.activityKey = activityKey;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public List<String> getLastMileScenario() {
		return lastMileScenario;
	}

	public void setLastMileScenario(List<String> lastMileScenario) {
		this.lastMileScenario = lastMileScenario;
	}

	public List<String> getProductName() {
		return productName;
	}

	public void setProductName(List<String> productName) {
		this.productName = productName;
	}

	public List<String> getStatus() {
		return status;
	}

	public void setStatus(List<String> status) {
		this.status = status;
	}

	public List<String> getAssignedPM() {
		return assignedPM;
	}

	public void setAssignedPM(List<String> assignedPM) {
		this.assignedPM = assignedPM;
	}

	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public String getBillingCompletionStartDate() {
		return billingCompletionStartDate;
	}

	public void setBillingCompletionStartDate(String billingCompletionStartDate) {
		this.billingCompletionStartDate = billingCompletionStartDate;
	}

	public String getBillingCompletionEndDate() {
		return billingCompletionEndDate;
	}

	public void setBillingCompletionEndDate(String billingCompletionEndDate) {
		this.billingCompletionEndDate = billingCompletionEndDate;
	}
	public List<String> getOrderSubCategory() {
		return orderSubCategory;
	}

	public void setOrderSubCategory(List<String> orderSubCategory) {
		this.orderSubCategory = orderSubCategory;
	}

	public String getInternationalServiceCode() {
		return internationalServiceCode;
	}

	public void setInternationalServiceCode(String internationalServiceCode) {
		this.internationalServiceCode = internationalServiceCode;
	}
	
}

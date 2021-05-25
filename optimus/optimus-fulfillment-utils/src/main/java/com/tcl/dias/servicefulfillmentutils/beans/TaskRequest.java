package com.tcl.dias.servicefulfillmentutils.beans;

import java.util.List;

public class TaskRequest {

	private String userName;

	private Integer serviceId;

	private List<String> status;
	private List<String> serviceType;

	private List<String> orderType;

	private List<String> taskName;

	private List<String> city;

	private String serviceCode;

	private String groupBy;

	private String groupName;

	private String type;

	private String customerName;

	private List<String> state;

	private List<String> lastMileScenario;

	private List<String> lmProvider;

	private List<String> pmName;

	private Boolean isJeopardyTask = false;

	private List<String> orderCategory;

	private List<String> orderSubCategory;

	private List<String> distributionCenterName;

	private String isIpDownTimeRequired;

	private String isTxDowntimeReqd;

	private String deviceType;

	private String devicePlatform;

	private List<String> csmName;
	
	

	public String getIsIpDownTimeRequired() {
		return isIpDownTimeRequired;
	}

	public void setIsIpDownTimeRequired(String isIpDownTimeRequired) {
		this.isIpDownTimeRequired = isIpDownTimeRequired;
	}

	public String getIsTxDowntimeReqd() {
		return isTxDowntimeReqd;
	}

	public void setIsTxDowntimeReqd(String isTxDowntimeReqd) {
		this.isTxDowntimeReqd = isTxDowntimeReqd;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public String getDevicePlatform() {
		return devicePlatform;
	}

	public void setDevicePlatform(String devicePlatform) {
		this.devicePlatform = devicePlatform;
	}

	public List<String> getState() {
		return state;
	}

	public void setState(List<String> state) {
		this.state = state;
	}

	public List<String> getLastMileScenario() {
		return lastMileScenario;
	}

	public void setLastMileScenario(List<String> lastMileScenario) {
		this.lastMileScenario = lastMileScenario;
	}

	public List<String> getLmProvider() {
		return lmProvider;
	}

	public void setLmProvider(List<String> lmProvider) {
		this.lmProvider = lmProvider;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	private List<String> country;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getGroupBy() {
		return groupBy;
	}

	public void setGroupBy(String groupBy) {
		this.groupBy = groupBy;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public List<String> getServiceType() {
		return serviceType;
	}

	public void setServiceType(List<String> serviceType) {
		this.serviceType = serviceType;
	}

	public List<String> getStatus() {
		return status;
	}

	public void setStatus(List<String> status) {
		this.status = status;
	}

	public List<String> getOrderType() {
		return orderType;
	}

	public void setOrderType(List<String> orderType) {
		this.orderType = orderType;
	}

	public List<String> getTaskName() {
		return taskName;
	}

	public void setTaskName(List<String> taskName) {
		this.taskName = taskName;
	}

	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	public List<String> getCity() {
		return city;
	}

	public void setCity(List<String> city) {
		this.city = city;
	}

	public List<String> getCountry() {
		return country;
	}

	public void setCountry(List<String> country) {
		this.country = country;
	}

	public List<String> getPmName() {
		return pmName;
	}

	public void setPmName(List<String> pmName) {
		this.pmName = pmName;
	}

	public Boolean getIsJeopardyTask() {
		return isJeopardyTask;
	}

	public void setIsJeopardyTask(Boolean isJeopardyTask) {
		this.isJeopardyTask = isJeopardyTask;
	}

	public List<String> getOrderCategory() {
		return orderCategory;
	}

	public void setOrderCategory(List<String> orderCategory) {
		this.orderCategory = orderCategory;
	}

	public List<String> getOrderSubCategory() {
		return orderSubCategory;
	}

	public void setOrderSubCategory(List<String> orderSubCategory) {
		this.orderSubCategory = orderSubCategory;
	}

	public List<String> getDistributionCenterName() {
		return distributionCenterName;
	}

	public void setDistributionCenterName(List<String> distributionCenterName) {
		this.distributionCenterName = distributionCenterName;
	}
	public List<String> getCsmName() {
		return csmName;
	}

	public void setCsmName(List<String> csmName) {
		this.csmName = csmName;
	}
}

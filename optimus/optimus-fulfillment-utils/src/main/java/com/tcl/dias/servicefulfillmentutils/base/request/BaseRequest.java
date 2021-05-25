package com.tcl.dias.servicefulfillmentutils.base.request;

public class BaseRequest {

	private String deviceType = "WEB";
	private String devicePlatform;
	private String serviceCode;
	private String orderCode;
	private Integer serviceId;
	private Integer taskId;
	private String wfTaskId;
	private Integer version;
	private String orderType;
	private String latitude;
	private String longitude;

	private String delayReasonSubCategory;

	private String delayReasonCategory;
	
	private String delayReason;
	
	private String releasedBy;
	
	private String action;
	
	
	
	
	


	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getDelayReason() {
		return delayReason;
	}

	public void setDelayReason(String delayReason) {
		this.delayReason = delayReason;
	}

	public String getDelayReasonSubCategory() {
		return delayReasonSubCategory;
	}

	public void setDelayReasonSubCategory(String delayReasonSubCategory) {
		this.delayReasonSubCategory = delayReasonSubCategory;
	}

	public String getDelayReasonCategory() {
		return delayReasonCategory;
	}

	public void setDelayReasonCategory(String delayReasonCategory) {
		this.delayReasonCategory = delayReasonCategory;
	}

	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}

	public String getDevicePlatform() {
		return devicePlatform;
	}

	public void setDevicePlatform(String devicePlatform) {
		this.devicePlatform = devicePlatform;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
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

	public Integer getTaskId() {
		return taskId;
	}

	public void setTaskId(Integer taskId) {
		this.taskId = taskId;
	}

	public String getWfTaskId() {
		return wfTaskId;
	}

	public void setWfTaskId(String wfTaskId) {
		this.wfTaskId = wfTaskId;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}
	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getReleasedBy() {
		return releasedBy;
	}
	public void setReleasedBy(String releasedBy) {
		this.releasedBy = releasedBy;
	}

}

package com.tcl.dias.servicefulfillment.beans;

import java.io.Serializable;

public class CreateServiceSyncBean implements Serializable {

	private static final long serialVersionUID = -5601517231751222377L;
	private String copfId;
	private String serviceId;
	private String siteCode;
	private String orderId;
	private String customerName;
	private String serviceType;
	private String serviceBandwidthValue;
	private String serviceBandwidthUnit;
	private String lmBandwidthValue;
	private String lmBandwidthUnit;
	private String serviceOption;
	private String requestId;
	private String requestingSystem;

	public String getSiteCode() {
		return siteCode;
	}

	public void setSiteCode(String siteCode) {
		this.siteCode = siteCode;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public String getServiceBandwidthValue() {
		return serviceBandwidthValue;
	}

	public void setServiceBandwidthValue(String serviceBandwidthValue) {
		this.serviceBandwidthValue = serviceBandwidthValue;
	}

	public String getServiceBandwidthUnit() {
		return serviceBandwidthUnit;
	}

	public void setServiceBandwidthUnit(String serviceBandwidthUnit) {
		this.serviceBandwidthUnit = serviceBandwidthUnit;
	}

	public String getLmBandwidthValue() {
		return lmBandwidthValue;
	}

	public void setLmBandwidthValue(String lmBandwidthValue) {
		this.lmBandwidthValue = lmBandwidthValue;
	}

	public String getLmBandwidthUnit() {
		return lmBandwidthUnit;
	}

	public void setLmBandwidthUnit(String lmBandwidthUnit) {
		this.lmBandwidthUnit = lmBandwidthUnit;
	}

	public String getServiceOption() {
		return serviceOption;
	}

	public void setServiceOption(String serviceOption) {
		this.serviceOption = serviceOption;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public String getRequestingSystem() {
		return requestingSystem;
	}

	public void setRequestingSystem(String requestingSystem) {
		this.requestingSystem = requestingSystem;
	}

	public String getCopfId() {
		return copfId;
	}

	public void setCopfId(String copfId) {
		this.copfId = copfId;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
}

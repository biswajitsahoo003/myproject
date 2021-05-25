package com.tcl.dias.servicehandover.controller;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateAccount {

	private String orderId;
	private String serviceCode;
	private String serviceId;
	private String serviceType;
	private String invoiceType;
	private Integer parallelDays;
	@JsonProperty
	private boolean isParallelUpgrade;
	private String termEndDate;
	private String circuitStatus;
	private String siteType;
	@JsonProperty
	private boolean isDemoOrder;
 	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getServiceCode() {
		return serviceCode;
	}
	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}
	public String getServiceId() {
		return serviceId;
	}
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	public String getServiceType() {
		return serviceType;
	}
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
	public String getInvoiceType() {
		return invoiceType;
	}
	public void setInvoiceType(String invoiceType) {
		this.invoiceType = invoiceType;
	}
	public Integer getParallelDays() {
		return parallelDays;
	}
	public void setParallelDays(Integer parallelDays) {
		this.parallelDays = parallelDays;
	}
	public boolean isParallelUpgrade() {
		return isParallelUpgrade;
	}
	public void setParallelUpgrade(boolean isParallelUpgrade) {
		this.isParallelUpgrade = isParallelUpgrade;
	}
	public String getTermEndDate() {
		return termEndDate;
	}
	public void setTermEndDate(String termEndDate) {
		this.termEndDate = termEndDate;
	}
	public String getCircuitStatus() {
		return circuitStatus;
	}
	public void setCircuitStatus(String circuitStatus) {
		this.circuitStatus = circuitStatus;
	}
	public String getSiteType() {
		return siteType;
	}
	public void setSiteType(String siteType) {
		this.siteType = siteType;
	}
	public boolean isDemoOrder() {
		return isDemoOrder;
	}
	public void setDemoOrder(boolean isDemoOrder) {
		this.isDemoOrder = isDemoOrder;
	}
	
		
	
}

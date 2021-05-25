package com.tcl.dias.common.beans;

import java.util.List;

public class MfFeasibility3DRequestBean {
	private Integer customerId;
	private String customerName;
	private String customerCode;
	private String sfdcAccountId;
	private String opportunityId;
	private String productId;
	private String productName;
	private String lastMileContactTerm;
	private String bandwidth;
	private String requestData;
	private  Boolean isLatLong;
	private List<Mf3DSiteDetailBean> siteDetails;
	
	
	
	
	public Boolean getIsLatLong() {
		return isLatLong;
	}
	public void setIsLatLong(Boolean isLatLong) {
		this.isLatLong = isLatLong;
	}
	public Integer getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getCustomerCode() {
		return customerCode;
	}
	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}
	public String getSfdcAccountId() {
		return sfdcAccountId;
	}
	public void setSfdcAccountId(String sfdcAccountId) {
		this.sfdcAccountId = sfdcAccountId;
	}
	public String getOpportunityId() {
		return opportunityId;
	}
	public void setOpportunityId(String opportunityId) {
		this.opportunityId = opportunityId;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getLastMileContactTerm() {
		return lastMileContactTerm;
	}
	public void setLastMileContactTerm(String lastMileContactTerm) {
		this.lastMileContactTerm = lastMileContactTerm;
	}
	public String getBandwidth() {
		return bandwidth;
	}
	public void setBandwidth(String bandwidth) {
		this.bandwidth = bandwidth;
	}
	public String getRequestData() {
		return requestData;
	}
	public void setRequestData(String requestData) {
		this.requestData = requestData;
	}
	public List<Mf3DSiteDetailBean> getSiteDetails() {
		return siteDetails;
	}
	public void setSiteDetails(List<Mf3DSiteDetailBean> siteDetails) {
		this.siteDetails = siteDetails;
	}
	
}

package com.tcl.dias.common.beans;

import java.util.List;

public class Mf3DResponse {
	private Integer quoteId;
	private String quoteCode;
	private Boolean isTaskTrigger=false;
	private List<SiteDetail> siteDetails;
	private String customerId;
	private String productName;
	private String custAccountId;
	private String currencyIsoCode;
	private String siteCode;
	
	
	
	
	public String getSiteCode() {
		return siteCode;
	}
	public void setSiteCode(String siteCode) {
		this.siteCode = siteCode;
	}
	public Boolean getIsTaskTrigger() {
		return isTaskTrigger;
	}
	public void setIsTaskTrigger(Boolean isTaskTrigger) {
		this.isTaskTrigger = isTaskTrigger;
	}
	public List<SiteDetail> getSiteDetails() {
		return siteDetails;
	}
	public void setSiteDetails(List<SiteDetail> siteDetails) {
		this.siteDetails = siteDetails;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getCustAccountId() {
		return custAccountId;
	}
	public void setCustAccountId(String custAccountId) {
		this.custAccountId = custAccountId;
	}
	public String getCurrencyIsoCode() {
		return currencyIsoCode;
	}
	public void setCurrencyIsoCode(String currencyIsoCode) {
		this.currencyIsoCode = currencyIsoCode;
	}
	
	public Integer getQuoteId() {
		return quoteId;
	}
	public void setQuoteId(Integer quoteId) {
		this.quoteId = quoteId;
	}
	public String getQuoteCode() {
		return quoteCode;
	}
	public void setQuoteCode(String quoteCode) {
		this.quoteCode = quoteCode;
	}
	
	

}

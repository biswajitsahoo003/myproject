package com.tcl.dias.oms.beans;

/**
 * Bean for cancellation quote details
 * @author archchan
 *
 */
public class CancellationQuoteDetails {
	
	private Integer quoteId;
	private String quoteCode ;
	private Integer  quoteToLeId;
	private String cancelledParentOrderCode; 
	private String serviceId;
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
	public Integer getQuoteToLeId() {
		return quoteToLeId;
	}
	public void setQuoteToLeId(Integer quoteToLeId) {
		this.quoteToLeId = quoteToLeId;
	}
	public String getCancelledParentOrderCode() {
		return cancelledParentOrderCode;
	}
	public void setCancelledParentOrderCode(String cancelledParentOrderCode) {
		this.cancelledParentOrderCode = cancelledParentOrderCode;
	}
	public String getServiceId() {
		return serviceId;
	}
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	@Override
	public String toString() {
		return "CancellationQuoteDetails [quoteId=" + quoteId + ", quoteCode=" + quoteCode + ", quoteToLeId="
				+ quoteToLeId + ", cancelledParentOrderCode=" + cancelledParentOrderCode + ", serviceId=" + serviceId
				+ "]";
	}
	
	
	
	
	}

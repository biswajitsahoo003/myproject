package com.tcl.dias.oms.macd.beans;

import java.util.List;

import com.tcl.dias.oms.gde.beans.GdeQuoteDetail;

public class GdeMacdRequest {
	
	private String requestType;
	private List<ServiceDetailBean> serviceDetails;
	private String cancellationDate;
	private String cancellationReason;
	private GdeQuoteDetail quoteRequest;
	public String getRequestType() {
		return requestType;
	}
	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}
	public List<ServiceDetailBean> getServiceDetails() {
		return serviceDetails;
	}
	public void setServiceDetails(List<ServiceDetailBean> serviceDetails) {
		this.serviceDetails = serviceDetails;
	}
	public String getCancellationDate() {
		return cancellationDate;
	}
	public void setCancellationDate(String cancellationDate) {
		this.cancellationDate = cancellationDate;
	}
	public String getCancellationReason() {
		return cancellationReason;
	}
	public void setCancellationReason(String cancellationReason) {
		this.cancellationReason = cancellationReason;
	}
	public GdeQuoteDetail getQuoteRequest() {
		return quoteRequest;
	}
	public void setQuoteRequest(GdeQuoteDetail quoteRequest) {
		this.quoteRequest = quoteRequest;
	}
	@Override
	public String toString() {
		return "GdeMacdRequest [requestType=" + requestType + ", serviceDetails=" + serviceDetails
				+ ", cancellationDate=" + cancellationDate + ", cancellationReason=" + cancellationReason
				+ ", quoteRequest=" + quoteRequest + "]";
	}
	
	
	

}

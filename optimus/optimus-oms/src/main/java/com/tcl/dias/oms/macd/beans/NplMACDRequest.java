package com.tcl.dias.oms.macd.beans;

import java.util.List;

import com.tcl.dias.oms.npl.beans.NplQuoteDetail;

public class NplMACDRequest {

	private String requestType;

	private List<ServiceDetailBean> serviceDetails;

	private String cancellationDate;

	private String cancellationReason;


	private NplQuoteDetail quoteRequest;

	public String getRequestType() {
		return requestType;
	}

	public void setRequestType(String requestType) {
		this.requestType = requestType;
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

	public List<ServiceDetailBean> getServiceDetails() {
		return serviceDetails;
	}

	public void setServiceDetails(List<ServiceDetailBean> serviceDetails) {
		this.serviceDetails = serviceDetails;
	}

	public NplQuoteDetail getQuoteRequest() { return quoteRequest; }

	public void setQuoteRequest(NplQuoteDetail quoteRequest) { this.quoteRequest = quoteRequest; }
}

package com.tcl.dias.oms.macd.beans;

import java.util.List;

import com.tcl.dias.oms.beans.QuoteDetail;

public class MacdQuoteRequest {
	
	
	private String requestType;
	
    private List<ServiceDetailBean> serviceDetails;

	private String cancellationDate;

	private String cancellationReason;
	
	
	private QuoteDetail quoteRequest;

	public String getRequestType() {
		return requestType;
	}

	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}

	public QuoteDetail getQuoteRequest() {
		return quoteRequest;
	}

	public void setQuoteRequest(QuoteDetail quoteRequest) {
		this.quoteRequest = quoteRequest;
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

	@Override
	public String toString() {
		return "MacdQuoteRequest{" +
				"requestType='" + requestType + '\'' +
				", serviceDetails=" + serviceDetails +
				", cancellationDate='" + cancellationDate + '\'' +
				", cancellationReason='" + cancellationReason + '\'' +
				", quoteRequest=" + quoteRequest +
				'}';
	}
}

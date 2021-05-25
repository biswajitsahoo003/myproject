package com.tcl.dias.oms.macd.beans;

import com.tcl.dias.oms.beans.QuoteResponse;

public class MacdQuoteResponse {
	private String quoteType;
	private String quoteCategory;
	private String changeBandwidthFlag;
	private QuoteResponse quoteResponse;
	private String serviceId;
	
	public String getQuoteType() {
		return quoteType;
	}

	public void setQuoteType(String quoteType) {
		this.quoteType = quoteType;
	}

	public String getQuoteCategory() {
		return quoteCategory;
	}

	public void setQuoteCategory(String quoteCategory) {
		this.quoteCategory = quoteCategory;
	}

	public String getChangeBandwidthFlag() {
		return changeBandwidthFlag;
	}

	public void setChangeBandwidthFlag(String changeBandwidthFlag) {
		this.changeBandwidthFlag = changeBandwidthFlag;
	}
	
	public QuoteResponse getQuoteResponse() {
		return quoteResponse;
	}

	public void setQuoteResponse(QuoteResponse quoteResponse) {
		this.quoteResponse = quoteResponse;
	}

	public String getServiceId() { return serviceId; }

	public void setServiceId(String serviceId) { this.serviceId = serviceId; }
}

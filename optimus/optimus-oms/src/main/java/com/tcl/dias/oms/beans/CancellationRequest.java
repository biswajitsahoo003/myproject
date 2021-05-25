package com.tcl.dias.oms.beans;

import java.util.List;

public class CancellationRequest {
	
	List<CancelledServicesBean> cancelledServiceDetails;
	
	private Integer quoteToLeId;
	
	private Integer quoteId;

	public List<CancelledServicesBean> getCancelledServiceDetails() {
		return cancelledServiceDetails;
	}

	public void setCancelledServiceDetails(List<CancelledServicesBean> cancelledServiceDetails) {
		this.cancelledServiceDetails = cancelledServiceDetails;
	}

	public Integer getQuoteToLeId() {
		return quoteToLeId;
	}

	public void setQuoteToLeId(Integer quoteToLeId) {
		this.quoteToLeId = quoteToLeId;
	}

	public Integer getQuoteId() {
		return quoteId;
	}

	public void setQuoteId(Integer quoteId) {
		this.quoteId = quoteId;
	}

	@Override
	public String toString() {
		return "CancellationRequest [cancelledServiceDetails=" + cancelledServiceDetails + ", quoteToLeId="
				+ quoteToLeId + ", quoteId=" + quoteId + "]";
	}
	
	
	
	
	
	

}

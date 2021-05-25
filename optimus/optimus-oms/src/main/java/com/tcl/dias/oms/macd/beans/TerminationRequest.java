package com.tcl.dias.oms.macd.beans;

import java.util.List;

public class TerminationRequest {
	
	private List<TerminatingServiceDetails> terminatingServiceDetails;
	
	private Integer quoteId;
	
	private Integer quoteToLeId;

	public List<TerminatingServiceDetails> getTerminatingServiceDetails() {
		return terminatingServiceDetails;
	}

	public void setTerminatingServiceDetails(List<TerminatingServiceDetails> terminatingServiceDetails) {
		this.terminatingServiceDetails = terminatingServiceDetails;
	}

	public Integer getQuoteId() {
		return quoteId;
	}

	public void setQuoteId(Integer quoteId) {
		this.quoteId = quoteId;
	}

	public Integer getQuoteToLeId() {
		return quoteToLeId;
	}

	public void setQuoteToLeId(Integer quoteToLeId) {
		this.quoteToLeId = quoteToLeId;
	}

	@Override
	public String toString() {
		return "TerminationRequest [terminatingServiceDetails=" + terminatingServiceDetails + ", quoteId=" + quoteId
				+ ", quoteToLeId=" + quoteToLeId + "]";
	}
	
	
	

}

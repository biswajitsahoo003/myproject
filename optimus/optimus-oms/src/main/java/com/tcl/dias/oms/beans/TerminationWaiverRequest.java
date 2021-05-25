package com.tcl.dias.oms.beans;

import java.util.List;

/**
 * TerminationWaiverRequest file
 * 
 *
 * @author Veera Balasubramanian
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class TerminationWaiverRequest {
	
	private Integer quoteId;
	
	private Integer quoteToLeId;

	private List<TerminationWaiverDetailsBean> terminationWaiverDetails;

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

	public List<TerminationWaiverDetailsBean> getTerminationWaiverDetails() {
		return terminationWaiverDetails;
	}

	public void setTerminationWaiverDetails(List<TerminationWaiverDetailsBean> terminationWaiverDetails) {
		this.terminationWaiverDetails = terminationWaiverDetails;
	}

	@Override
	public String toString() {
		return "TerminationWaiverRequest [quoteId=" + quoteId + ", quoteToLeId=" + quoteToLeId
				+ ", terminationWaiverDetails=" + terminationWaiverDetails + "]";
	}
}

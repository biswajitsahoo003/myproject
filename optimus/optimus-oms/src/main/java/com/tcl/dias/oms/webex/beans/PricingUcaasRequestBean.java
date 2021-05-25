package com.tcl.dias.oms.webex.beans;

import java.util.List;

/**
 * Pricing Request Bean
 * 
 * @author Syed Ali
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
public class PricingUcaasRequestBean {
	private Integer quoteToLeId;
	private String dealId;
	private List<QuoteUcaasBean> ucaasQuotes;

	public PricingUcaasRequestBean() {

	}

	public Integer getQuoteToLeId() {
		return quoteToLeId;
	}

	public void setQuoteToLeId(Integer quoteToLeId) {
		this.quoteToLeId = quoteToLeId;
	}

	public String getDealId() {
		return dealId;
	}

	public void setDealId(String dealId) {
		this.dealId = dealId;
	}

	public List<QuoteUcaasBean> getUcaasQuotes() {
		return ucaasQuotes;
	}

	public void setUcaasQuotes(List<QuoteUcaasBean> ucaasQuotes) {
		this.ucaasQuotes = ucaasQuotes;
	}

	@Override
	public String toString() {
		return "PricingUcaasRequestBean [quoteToLeId=" + quoteToLeId + ", dealId=" + dealId + ", ucaasQuotes="
				+ ucaasQuotes + "]";
	}

}

package com.tcl.dias.oms.webex.beans;

import java.util.List;

/**
 * Response bean for Webex Pricing
 *
 * @author Syed Ali
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
public class WebexQuotePricingRequest {
	private Integer contractTerm;
	private List<WebexUcaasQuotePriceBean> ucaasQuotes;

	public WebexQuotePricingRequest() {
	}

	public List<WebexUcaasQuotePriceBean> getUcaasQuotes() {
		return ucaasQuotes;
	}

	public void setUcaasQuotes(List<WebexUcaasQuotePriceBean> ucaasQuotes) {
		this.ucaasQuotes = ucaasQuotes;
	}

	public Integer getContractTerm() {
		return contractTerm;
	}

	public void setContractTerm(Integer contractTerm) {
		this.contractTerm = contractTerm;
	}

	@Override
	public String toString() {
		return "WebexQuotePricingRequest [contractTerm=" + contractTerm + ", ucaasQuotes=" + ucaasQuotes + "]";
	}

}


package com.tcl.dias.pricingengine.ipc.beans;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * 
 * This file contains the PricingBean.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({ "quotes" })
public class PricingBean {

	@JsonProperty("quotes")
	private List<Quote> quotes = null;

	private String vmPricingResponse = null;

	private String errorResponse = null;

	@JsonProperty("quotes")
	public List<Quote> getQuotes() {
		return quotes;
	}

	@JsonProperty("quotes")
	public void setQuotes(List<Quote> quotes) {
		this.quotes = quotes;
	}

	public String getVmPricingResponse() {
		return vmPricingResponse;
	}

	public void setVmPricingResponse(String vmPricingResponse) {
		this.vmPricingResponse = vmPricingResponse;
	}

	public String getErrorResponse() {
		return errorResponse;
	}

	public void setErrorResponse(String errorResponse) {
		this.errorResponse = errorResponse;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("quotes", quotes).toString();
	}

}

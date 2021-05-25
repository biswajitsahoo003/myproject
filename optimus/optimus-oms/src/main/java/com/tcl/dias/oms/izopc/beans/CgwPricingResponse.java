package com.tcl.dias.oms.izopc.beans;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * 
 * This file contains the CgwPricingResponse.java class.
 * 
 *
 * @author AnandhiV
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "results", "error_flag", "error_message" })
public class CgwPricingResponse {

	@JsonProperty("results")
	private List<CgwPricingResults> results = null;

	@JsonProperty("error_flag")
	private Integer errorFlag;
	@JsonProperty("error_message")
	private Object errorMessage;

	public List<CgwPricingResults> getResults() {
		return results;
	}

	public void setResults(List<CgwPricingResults> results) {
		this.results = results;
	}

	public Integer getErrorFlag() {
		return errorFlag;
	}

	public void setErrorFlag(Integer errorFlag) {
		this.errorFlag = errorFlag;
	}

	public Object getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(Object errorMessage) {
		this.errorMessage = errorMessage;
	}

}

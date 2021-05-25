package com.tcl.dias.oms.izopc.beans;

import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * 
 * This file contains the CgwPricingRequest.java class.
 * 
 *
 * @author AnandhiV
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "input_data" })
public class CgwPricingRequest {
	
	@JsonProperty("input_data")
	private List<CgwPricingInputDatum> inputData = null;
	
	
	public List<CgwPricingInputDatum> getInputData() {
		return inputData;
	}


	public void setInputData(List<CgwPricingInputDatum> inputData) {
		this.inputData = inputData;
	}


	@Override
	public String toString() {
		return new ToStringBuilder(this).append("inputData", inputData).toString();
	}
}



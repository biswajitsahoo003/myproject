package com.tcl.dias.oms.izosdwan.beans;

import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.tcl.dias.oms.izopc.beans.CgwPricingInputDatum;

/**
 * 
 * This file contains the VproxyPricingRequest.java class.
 * 
 *
 * @author mpalanis
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "input_data" })
public class IzosdwanVproxyPricingRequest {
	
	@JsonProperty("input_data")
	private List<VproxyPricingInputDatum> inputData = null;
		
	public List<VproxyPricingInputDatum> getInputData() {
		return inputData;
	}

	public void setInputData(List<VproxyPricingInputDatum> inputData) {
		this.inputData = inputData;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("inputData", inputData).toString();
	}
}

package com.tcl.dias.oms.izosdwan.beans;

import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
/**
 * 
 * This file contains the CpePricingPriceInputDatum.java class.
 * 
 *
 * @author AnandhiV
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "input_data" })
public class CpePricingPriceInputDatum {
	@JsonProperty("input_data")
	private List<IzosdwanPriceApiRequest> inputData = null;

	@JsonProperty("input_data")
	public List<IzosdwanPriceApiRequest> getInputData() {
		return inputData;
	}

	@JsonProperty("input_data")
	public void setInputData(List<IzosdwanPriceApiRequest> inputData) {
		this.inputData = inputData;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("inputData", inputData).toString();
	}

}

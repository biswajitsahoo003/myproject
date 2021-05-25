package com.tcl.dias.oms.discount.bean;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "input_data"
})
public class DiscountResponse {

	@JsonProperty("input_data")
    private List<DiscountInputData> inputData = null;

	public List<DiscountInputData> getInputData() {
		return inputData;
	}

	public void setInputData(List<DiscountInputData> inputData) {
		this.inputData = inputData;
	}

}

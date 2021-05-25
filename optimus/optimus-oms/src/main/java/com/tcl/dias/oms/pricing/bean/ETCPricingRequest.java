package com.tcl.dias.oms.pricing.bean;

import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "input_data"
})
public class ETCPricingRequest {

    @JsonProperty("input_data")
    private List<ETCPricingInputDatum> inputData = null;

    @JsonProperty("input_data")
    public List<ETCPricingInputDatum> getInputData() {
        return inputData;
    }

    @JsonProperty("input_data")
    public void setInputData(List<ETCPricingInputDatum> inputData) {
        this.inputData = inputData;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("inputData", inputData).toString();
    }

}

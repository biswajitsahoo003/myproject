package com.tcl.dias.oms.pricing.bean;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "input_data"
})
public class PricingRequest {

    @JsonProperty("input_data")
    private List<PricingInputDatum> inputData = null;

    @JsonProperty("input_data")
    public List<PricingInputDatum> getInputData() {
        return inputData;
    }

    @JsonProperty("input_data")
    public void setInputData(List<PricingInputDatum> inputData) {
        this.inputData = inputData;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("inputData", inputData).toString();
    }

}

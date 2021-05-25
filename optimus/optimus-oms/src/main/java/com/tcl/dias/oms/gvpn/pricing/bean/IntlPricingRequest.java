
package com.tcl.dias.oms.gvpn.pricing.bean;

import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "input_data"
})
public class IntlPricingRequest {

    @JsonProperty("input_data")
    private List<IntlPricingInputDatum> inputData = null;

    @JsonProperty("input_data")
    public List<IntlPricingInputDatum> getInputData() {
        return inputData;
    }

    @JsonProperty("input_data")
    public void setInputData(List<IntlPricingInputDatum> inputData) {
        this.inputData = inputData;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("inputData", inputData).toString();
    }

}
package com.tcl.dias.oms.crossconnect.pricing.bean;




import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "input_data"
})
public class PricingCrossConnectRequest {

    @JsonProperty("input_data")
    private List<CrossconnectPrcingRequest> inputData = null;

    @JsonProperty("input_data")
    public List<CrossconnectPrcingRequest> getInputData() {
        return inputData;
    }

    @JsonProperty("input_data")
    public void setInputData(List<CrossconnectPrcingRequest> inputData) {
        this.inputData = inputData;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("inputData", inputData).toString();
    }

}


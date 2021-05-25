
package com.tcl.dias.oms.pricing.bean;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "input_data"
})
public class FeasibleBeanRequest {

    @JsonProperty("input_data")
    private List<InputData> inputData = null;

    @JsonProperty("input_data")
    public List<InputData> getInputData() {
        return inputData;
    }

    @JsonProperty("input_data")
    public void setInputData(List<InputData> inputData) {
        this.inputData = inputData;
    }

}

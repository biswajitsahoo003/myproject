
package com.tcl.dias.wfe.dto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "input_data"
})
public class ProcessInput {

    @JsonProperty("input_data")
    private List<InputDatum> inputData = null;
    
    @JsonProperty("input_data")
    public List<InputDatum> getInputData() {
        return inputData;
    }

    @JsonProperty("input_data")
    public void setInputData(List<InputDatum> inputData) {
        this.inputData = inputData;
    }
}

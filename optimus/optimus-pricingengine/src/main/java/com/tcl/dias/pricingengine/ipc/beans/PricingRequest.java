
package com.tcl.dias.pricingengine.ipc.beans;

import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
/**
 * 
 * This file contains the PricingRequest.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
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

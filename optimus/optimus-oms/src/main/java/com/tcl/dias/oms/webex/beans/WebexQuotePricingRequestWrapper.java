package com.tcl.dias.oms.webex.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

/*
 *  Wrapper class for pricing request.
 *  @author Syed Ali
 *  @link http://www.tatacommunications.com/
 *  @copyright 2020 Tata Communications Limited
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({ "input_data" })
public class WebexQuotePricingRequestWrapper {

    @JsonProperty("input_data")
    private List<WebexQuotePricingRequest> inputData;

    public List<WebexQuotePricingRequest> getInputData() {
        return inputData;
    }

    public void setInputData(List<WebexQuotePricingRequest> inputData) {
        this.inputData = inputData;
    }

    @Override
    public String toString() {
        return "WebexQuotePricingRequestWrapper{" +
                "inputData=" + inputData +
                '}';
    }
}

package com.tcl.dias.oms.webex.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

/*
 *  Wrapper class for pricing response
 *  @author Syed Ali
 *  @link http://www.tatacommunications.com/
 *  @copyright 2020 Tata Communications Limited
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({ "results"})
public class WebexPricingResponseWrapper {

    @JsonProperty("results")
    private List<WebexPricingResponse> results;

    public List<WebexPricingResponse> getResults() {
        return results;
    }

    public void setResults(List<WebexPricingResponse> results) {
        this.results = results;
    }

    @Override
    public String toString() {
        return "WebexPricingResponseWrapper{" +
                "results=" + results +
                '}';
    }
}

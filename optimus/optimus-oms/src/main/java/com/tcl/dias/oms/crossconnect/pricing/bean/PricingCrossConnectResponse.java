package com.tcl.dias.oms.crossconnect.pricing.bean;




import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
    "results"
})
public class PricingCrossConnectResponse {

    @JsonProperty("results")
    private List<CrossConnectPricingResponse> results = null;

    @JsonProperty("results")
    public List<CrossConnectPricingResponse> getResults() {
        return results;
    }

    @JsonProperty("results")
    public void setResults(List<CrossConnectPricingResponse> results) {
        this.results = results;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("results", results).toString();
    }

}


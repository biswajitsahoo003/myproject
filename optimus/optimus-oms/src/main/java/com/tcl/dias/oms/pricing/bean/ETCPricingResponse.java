package com.tcl.dias.oms.pricing.bean;

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
public class ETCPricingResponse {

    @JsonProperty("results")
    private List<ETCResult> results = null;

    @JsonProperty("results")
    public List<ETCResult> getResults() {
        return results;
    }

    @JsonProperty("results")
    public void setResults(List<ETCResult> results) {
        this.results = results;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("results", results).toString();
    }

}
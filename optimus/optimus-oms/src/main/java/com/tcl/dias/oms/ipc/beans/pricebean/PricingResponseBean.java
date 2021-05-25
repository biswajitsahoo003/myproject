package com.tcl.dias.oms.ipc.beans.pricebean;

import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tcl.dias.oms.pricing.bean.Result;

public class PricingResponseBean {

	@JsonProperty("results")
    private List<Result> results = null;

    @JsonProperty("results")
    public List<Result> getResults() {
        return results;
    }

    @JsonProperty("results")
    public void setResults(List<Result> results) {
        this.results = results;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("results", results).toString();
    }
}

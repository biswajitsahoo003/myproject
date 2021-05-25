package com.tcl.dias.oms.pricing.bean;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GvpnFeasibilityResponse {

	
	@JsonProperty("Feasible")
    private List<Feasible> feasible = null;
    @JsonProperty("NotFeasible")
    private List<NotFeasible> notFeasible = null;

    @JsonProperty("Feasible")
    public List<Feasible> getFeasible() {
        return feasible;
    }

    @JsonProperty("Feasible")
    public void setFeasible(List<Feasible> feasible) {
        this.feasible = feasible;
    }

    @JsonProperty("NotFeasible")
    public List<NotFeasible> getNotFeasible() {
        return notFeasible;
    }

    @JsonProperty("NotFeasible")
    public void setNotFeasible(List<NotFeasible> notFeasible) {
        this.notFeasible = notFeasible;
    }
	
}

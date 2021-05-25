package com.tcl.dias.oms.gvpn.pricing.bean;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GvpnFeasibilityResponse {

	
	@JsonProperty("Feasible")
    private List<Feasible> feasible = null;
    @JsonProperty("NotFeasible")
    private List<NotFeasible> notFeasible = null;
    @JsonProperty("IntlFeasible")
    private List<IntlFeasible> intlFeasible = null;
    @JsonProperty("IntlNotFeasible")
    private List<IntlNotFeasible> intlNotFeasible = null;
    
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

    @JsonProperty("IntlFeasible")
	public List<IntlFeasible> getIntlFeasible() {
		return intlFeasible;
	}

    @JsonProperty("IntlFeasible")
	public void setIntlFeasible(List<IntlFeasible> intlFeasible) {
		this.intlFeasible = intlFeasible;
	}
    @JsonProperty("IntlNotFeasible")
	public List<IntlNotFeasible> getIntlNotFeasible() {
		return intlNotFeasible;
	}
	@JsonProperty("IntlNotFeasible")
	public void setIntlNotFeasible(List<IntlNotFeasible> intlNotFeasible) {
		this.intlNotFeasible = intlNotFeasible;
	}
	
}

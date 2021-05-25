package com.tcl.dias.servicefulfillmentutils.beans.feasibility;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This file is used to get feasibility response for O2C workflow.
 */
public class O2CFeasibilityResponse{

	@JsonProperty("results")
	private List<Results> results;

	public void setResults(List<Results> results){
		this.results = results;
	}

	public List<Results> getResults(){
		return results;
	}

	@Override
 	public String toString(){
		return 
			"O2CFeasibilityResponse{" + 
			"results = '" + results + '\'' + 
			"}";
		}
}
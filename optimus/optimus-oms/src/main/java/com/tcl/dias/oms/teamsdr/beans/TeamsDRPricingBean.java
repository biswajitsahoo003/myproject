package com.tcl.dias.oms.teamsdr.beans;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Price request bean for pricing team
 *
 * @author Srinivasa Raghavan
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({ "input_data", "results" })
public class TeamsDRPricingBean {

	@JsonProperty("input_data")
	private List<TeamsDRPriceInputDatum> inputDatum;

	@JsonProperty("results")
	private List<TeamsDRPriceResultsDatum> results;

	public List<TeamsDRPriceInputDatum> getInputDatum() {
		return inputDatum;
	}

	public void setInputDatum(List<TeamsDRPriceInputDatum> inputDatum) {
		this.inputDatum = inputDatum;
	}

	public List<TeamsDRPriceResultsDatum> getResults() {
		return results;
	}

	public void setResults(List<TeamsDRPriceResultsDatum> results) {
		this.results = results;
	}
}

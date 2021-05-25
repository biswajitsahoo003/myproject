package com.tcl.dias.oms.izosdwan.beans;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class DcfReponse {

	@JsonProperty("error_flag")
    private Double errorFlag;
	
	@JsonProperty("results")
    private List<IzosdwanDcfResponse> results;

	public Double getErrorFlag() {
		return errorFlag;
	}

	public void setErrorFlag(Double errorFlag) {
		this.errorFlag = errorFlag;
	}

	public List<IzosdwanDcfResponse> getResults() {
		return results;
	}

	public void setResults(List<IzosdwanDcfResponse> results) {
		this.results = results;
	}
	
	
}

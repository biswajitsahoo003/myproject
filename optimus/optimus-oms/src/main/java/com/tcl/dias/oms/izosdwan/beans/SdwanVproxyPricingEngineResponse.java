package com.tcl.dias.oms.izosdwan.beans;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
	"error_flag",
    "results",
    "error_message"
})
public class SdwanVproxyPricingEngineResponse {
	
	@JsonProperty("results")
    private List<VproxyPricingInputDatum> results = null;

    @JsonProperty("results")
    public List<VproxyPricingInputDatum> getResults() {
        return results;
    }

    @JsonProperty("results")
    public void setResults(List<VproxyPricingInputDatum> results) {
        this.results = results;
    }
    
    @JsonProperty("error_flag")
    private Integer errorFlag;

	public Integer getErrorFlag() {
		return errorFlag;
	}

	public void setErrorFlag(Integer errorFlag) {
		this.errorFlag = errorFlag;
	}
    
	@JsonProperty("error_message")
	private Object errorMessage;

	public Object getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(Object errorMessage) {
		this.errorMessage = errorMessage;
	}

}

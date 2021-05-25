
package com.tcl.dias.oms.izosdwan.beans;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
/**
 * 
 * This file contains the SdwanPricingEngineResponse.java class.
 * 
 *
 * @author AnandhiV
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
	"error_flag",
    "results",
    "error_message"
})
public class SdwanPricingEngineResponse {

    @JsonProperty("results")
    private List<IzosdwanPricingResult> results = null;

    @JsonProperty("results")
    public List<IzosdwanPricingResult> getResults() {
        return results;
    }

    @JsonProperty("results")
    public void setResults(List<IzosdwanPricingResult> results) {
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

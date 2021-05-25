
package com.tcl.dias.serviceinventory.izosdwan.beans.versa_userdefined_url;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "pattern-value", "reputation" })
public class Pattern {

	@JsonProperty("pattern-value")
	private String patternValue;
	@JsonProperty("reputation")
	private String reputation;

	@JsonProperty("pattern-value")
	public String getPatternValue() {
		return patternValue;
	}

	@JsonProperty("pattern-value")
	public void setPatternValue(String patternValue) {
		this.patternValue = patternValue;
	}

	@JsonProperty("reputation")
	public String getReputation() {
		return reputation;
	}

	@JsonProperty("reputation")
	public void setReputation(String reputation) {
		this.reputation = reputation;
	}

}

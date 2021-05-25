
package com.tcl.dias.serviceinventory.izosdwan.beans.versa_userdefined_url;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "patterns" })
public class Urls {

	@JsonProperty("patterns")
	private List<Pattern> patterns = null;

	@JsonProperty("patterns")
	public List<Pattern> getPatterns() {
		return patterns;
	}

	@JsonProperty("patterns")
	public void setPatterns(List<Pattern> patterns) {
		this.patterns = patterns;
	}

}

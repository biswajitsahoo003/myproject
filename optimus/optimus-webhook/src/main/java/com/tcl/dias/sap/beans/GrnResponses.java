
package com.tcl.dias.sap.beans;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * 
 * @author MRajakum
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "GRN_RESPONSE" })
public class GrnResponses {

	@JsonProperty("GRN_RESPONSE")
	private List<GrnResponse> grnResponse = null;

	@JsonProperty("GRN_RESPONSE")
	public List<GrnResponse> getGrnResponse() {
		return grnResponse;
	}

	@JsonProperty("GRN_RESPONSE")
	public void setGrnResponse(List<GrnResponse> grnResponse) {
		this.grnResponse = grnResponse;
	}

}

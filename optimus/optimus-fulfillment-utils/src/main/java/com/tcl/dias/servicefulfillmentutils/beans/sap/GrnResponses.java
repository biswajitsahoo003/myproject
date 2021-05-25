
package com.tcl.dias.servicefulfillmentutils.beans.sap;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

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

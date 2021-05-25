
package com.tcl.dias.serviceinventory.izosdwan.beans.cisco.applicationbeans;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Versa APIs stubs (CPE status)
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "data" })
public class CiscoApplicationByDeviceIdResponse implements Serializable {

	@JsonProperty("data")
	private List<CiscoApplicationDetails> ciscoAppDetails;
	@JsonProperty("data")
	public List<CiscoApplicationDetails> getCiscoAppDetails() {
		return ciscoAppDetails;
	}
	@JsonProperty("data")
	public void setCiscoAppDetails(List<CiscoApplicationDetails> ciscoAppDetails) {
		this.ciscoAppDetails = ciscoAppDetails;
	}
	@Override
	public String toString() {
		return "CiscoApplicationByDeviceIdResponse [ciscoAppDetails=" + ciscoAppDetails + "]";
	}

}

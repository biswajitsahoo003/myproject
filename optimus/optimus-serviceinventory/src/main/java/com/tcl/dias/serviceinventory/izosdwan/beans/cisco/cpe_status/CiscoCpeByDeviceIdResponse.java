
package com.tcl.dias.serviceinventory.izosdwan.beans.cisco.cpe_status;

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
public class CiscoCpeByDeviceIdResponse implements Serializable {

	@JsonProperty("data")
	private List<CiscoCpeDetails> ciscoCpeDetails;

	public List<CiscoCpeDetails> getCiscoCpeDetails() {
		return ciscoCpeDetails;
	}

	public void setCiscoCpeDetails(List<CiscoCpeDetails> ciscoCpeDetails) {
		this.ciscoCpeDetails = ciscoCpeDetails;
	}

	@Override
	public String toString() {
		return "CiscoCpeByDeviceIdResponse [ciscoCpeDetails=" + ciscoCpeDetails + "]";
	}
	

}

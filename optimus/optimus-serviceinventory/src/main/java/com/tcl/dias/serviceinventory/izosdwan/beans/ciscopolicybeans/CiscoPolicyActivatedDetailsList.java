
package com.tcl.dias.serviceinventory.izosdwan.beans.ciscopolicybeans;

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
public class CiscoPolicyActivatedDetailsList implements Serializable {

	@JsonProperty("data")
	private List<CiscoPolicyActivatedDetail> ciscoPolicyActivatedDetails;

	public List<CiscoPolicyActivatedDetail> getCiscoPolicyActivatedDetails() {
		return ciscoPolicyActivatedDetails;
	}

	public void setCiscoPolicyActivatedDetails(List<CiscoPolicyActivatedDetail> ciscoPolicyActivatedDetails) {
		this.ciscoPolicyActivatedDetails = ciscoPolicyActivatedDetails;
	}

	

	

}

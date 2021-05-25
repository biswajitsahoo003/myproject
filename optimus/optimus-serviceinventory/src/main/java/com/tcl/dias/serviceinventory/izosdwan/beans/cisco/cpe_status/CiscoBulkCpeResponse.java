
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
public class CiscoBulkCpeResponse implements Serializable {

	@JsonProperty("data")
	private List<CiscoBulkCpeDetails> ciscoBulkCpeDetails;
	
	@JsonProperty("data")
	public List<CiscoBulkCpeDetails> getCiscoBulkCpeDetails() {
		return ciscoBulkCpeDetails;
	}
	
	@JsonProperty("data")
	public void setCiscoBulkCpeDetails(List<CiscoBulkCpeDetails> ciscoBulkCpeDetails) {
		this.ciscoBulkCpeDetails = ciscoBulkCpeDetails;
	}

	@Override
	public String toString() {
		return "CiscoBulkCpeResponse [ciscoBulkCpeDetails=" + ciscoBulkCpeDetails + "]";
	}
	
	

}

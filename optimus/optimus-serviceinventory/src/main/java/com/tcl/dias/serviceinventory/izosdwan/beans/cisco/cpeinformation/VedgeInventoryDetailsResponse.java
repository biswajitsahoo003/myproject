
package com.tcl.dias.serviceinventory.izosdwan.beans.cisco.cpeinformation;

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
public class VedgeInventoryDetailsResponse implements Serializable {

	@JsonProperty("data")
	private List<VedgeInventoryDetail> vedgeInventoryDetailList;
	
	@JsonProperty("data")
	public List<VedgeInventoryDetail> getVedgeInventoryDetailList() {
		return vedgeInventoryDetailList;
	}
	@JsonProperty("data")
	public void setVedgeInventoryDetailList(List<VedgeInventoryDetail> vedgeInventoryDetailList) {
		this.vedgeInventoryDetailList = vedgeInventoryDetailList;
	}
	@Override
	public String toString() {
		return "VedgeInventoryDetailsResponse [vedgeInventoryDetailList=" + vedgeInventoryDetailList + "]";
	}

}

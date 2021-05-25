
package com.tcl.dias.serviceinventory.izosdwan.beans.cisco_site_list;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * CISCO SITE LIST APIs
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "data" })
public class CiscoBulkSiteListResponse implements Serializable {

	@JsonProperty("data")
	private List<CiscoSiteListDetails> ciscoSiteListDetails;
	
	@JsonProperty("data")
	public List<CiscoSiteListDetails> getCiscoSiteListDetails() {
		return ciscoSiteListDetails;
	}
	
	@JsonProperty("data")
	public void setCiscoSiteListDetails(List<CiscoSiteListDetails> ciscoSiteListDetails) {
		this.ciscoSiteListDetails = ciscoSiteListDetails;
	}

	@Override
	public String toString() {
		return "CiscoBulkSiteListResponse [ciscoSiteListDetails=" + ciscoSiteListDetails + "]";
	}
	
}

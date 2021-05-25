
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
public class SiteIdEntries implements Serializable {

	@JsonProperty("siteId")
	private String siteId;
	
	@JsonProperty("siteId")
	public String getSiteId() {
		return siteId;
	}
	
	@JsonProperty("siteId")
	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}

	@Override
	public String toString() {
		return "SiteIdEntries [siteId=" + siteId + "]";
	}
	
	
}

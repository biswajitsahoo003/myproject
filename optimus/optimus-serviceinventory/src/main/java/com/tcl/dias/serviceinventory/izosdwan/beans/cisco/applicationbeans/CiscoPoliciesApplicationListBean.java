
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
public class CiscoPoliciesApplicationListBean implements Serializable {

	@JsonProperty("data")
	private List<CiscoPolicyApplicationBean> ciscoPolicyApps;
	@JsonProperty("data")
	public List<CiscoPolicyApplicationBean> getCiscoPolicyApps() {
		return ciscoPolicyApps;
	}
	@JsonProperty("data")
	public void setCiscoPolicyApps(List<CiscoPolicyApplicationBean> ciscoPolicyApps) {
		this.ciscoPolicyApps = ciscoPolicyApps;
	}
	@Override
	public String toString() {
		return "CiscoPoliciesApplicationListBean [ciscoPolicyApps=" + ciscoPolicyApps + "]";
	}
	

}

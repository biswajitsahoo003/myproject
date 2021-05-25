
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
public class VpnListReponse implements Serializable {

	@JsonProperty("data")
	private List<VpnListDetails> vpnListDetails;
	

	public List<VpnListDetails> getVpnListDetails() {
		return vpnListDetails;
	}


	public void setVpnListDetails(List<VpnListDetails> vpnListDetails) {
		this.vpnListDetails = vpnListDetails;
	}


	@Override
	public String toString() {
		return "VpnListReponse [vpnListDetails=" + vpnListDetails + "]";
	}

	

	

	

}

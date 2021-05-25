
package com.tcl.dias.serviceinventory.izosdwan.beans.ciscopolicybeans;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Versa APIs stubs (CPE status)
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VpnEntries implements Serializable {
	
	@JsonProperty("vpn")
	private String vpn;

	public String getVpn() {
		return vpn;
	}

	public void setVpn(String vpn) {
		this.vpn = vpn;
	}
	
		
}


package com.tcl.dias.serviceinventory.izosdwan.beans.ciscopolicybeans;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Versa APIs stubs (CPE status)
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Entries implements Serializable {
	
	@JsonProperty("siteLists")
	private List<String> siteLists;
	
	@JsonProperty("direction")
	private String direction;
	
	@JsonProperty("vpnLists")
	private List<String> vpnLists;
	
	
	public List<String> getSiteLists() {
		return siteLists;
	}

	public void setSiteLists(List<String> siteLists) {
		this.siteLists = siteLists;
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public List<String> getVpnLists() {
		return vpnLists;
	}

	public void setVpnLists(List<String> vpnLists) {
		this.vpnLists = vpnLists;
	}

	@Override
	public String toString() {
		return "Entries [siteLists=" + siteLists + ", direction=" + direction + ", vpnLists=" + vpnLists + "]";
	}
	
}

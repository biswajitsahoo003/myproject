package com.tcl.dias.serviceinventory.izosdwan.beans.cisco.applicationbeans;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AddressEntries {
	@JsonProperty("ipPrefix")
	private String ipPrefix;

	public String getIpPrefix() {
		return ipPrefix;
	}

	public void setIpPrefix(String ipPrefix) {
		this.ipPrefix = ipPrefix;
	}

	@Override
	public String toString() {
		return "AddressEntries [ipPrefix=" + ipPrefix + "]";
	}
}

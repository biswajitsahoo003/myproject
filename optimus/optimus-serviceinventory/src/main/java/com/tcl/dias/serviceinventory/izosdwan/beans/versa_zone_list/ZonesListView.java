
package com.tcl.dias.serviceinventory.izosdwan.beans.versa_zone_list;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "zones" })
public class ZonesListView {

	@JsonProperty("zones")
	private Zones zones;

	@JsonProperty("zones")
	public Zones getZones() {
		return zones;
	}

	@JsonProperty("zones")
	public void setZones(Zones zones) {
		this.zones = zones;
	}

}

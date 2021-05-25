
package com.tcl.dias.serviceinventory.izosdwan.beans.versa_zone_list;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "zone" })
public class Zones {

	@JsonProperty("zone")
	private List<Zone> zone = null;

	@JsonProperty("zone")
	public List<Zone> getZone() {
		return zone;
	}

	@JsonProperty("zone")
	public void setZone(List<Zone> zone) {
		this.zone = zone;
	}

}

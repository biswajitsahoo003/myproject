
package com.tcl.dias.serviceinventory.izosdwan.beans.versa_cpe_interface;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "uplink",
    "downlink"
})
public class Bandwidth {

    @JsonProperty("uplink")
    private Integer uplink;
    @JsonProperty("downlink")
    private Integer downlink;

    @JsonProperty("uplink")
    public Integer getUplink() {
        return uplink;
    }

    @JsonProperty("uplink")
    public void setUplink(Integer uplink) {
        this.uplink = uplink;
    }

    @JsonProperty("downlink")
    public Integer getDownlink() {
        return downlink;
    }

    @JsonProperty("downlink")
    public void setDownlink(Integer downlink) {
        this.downlink = downlink;
    }

	@Override
	public String toString() {
		return "Bandwidth [uplink=" + uplink + ", downlink=" + downlink + "]";
	}
    

}

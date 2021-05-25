
package com.tcl.dias.serviceinventory.izosdwan.beans.versa_template_link_details;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "wan-interfaces"
})
public class WanPathDetails {

    @JsonProperty("wan-interfaces")
    private WanInterfaces wanInterfaces;

    @JsonProperty("wan-interfaces")
    public WanInterfaces getWanInterfaces() {
        return wanInterfaces;
    }

    @JsonProperty("wan-interfaces")
    public void setWanInterfaces(WanInterfaces wanInterfaces) {
        this.wanInterfaces = wanInterfaces;
    }

}


package com.tcl.dias.serviceinventory.izosdwan.beans.versa_wan_status;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "sdwan:status"
})
public class WanStatus {

    @JsonProperty("sdwan:status")
    private SdwanStatus sdwanStatus;

    @JsonProperty("sdwan:status")
    public SdwanStatus getSdwanStatus() {
        return sdwanStatus;
    }

    @JsonProperty("sdwan:status")
    public void setSdwanStatus(SdwanStatus sdwanStatus) {
        this.sdwanStatus = sdwanStatus;
    }

}

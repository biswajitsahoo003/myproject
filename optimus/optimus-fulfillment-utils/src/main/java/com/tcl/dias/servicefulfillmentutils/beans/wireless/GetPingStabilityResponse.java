package com.tcl.dias.servicefulfillmentutils.beans.wireless;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GetPingStabilityResponse {

    @JsonProperty("Response")
    private GetPingStability getPingStability;

    public GetPingStability getGetPingStability() {
        return getPingStability;
    }

    public void setGetPingStability(GetPingStability getPingStability) {
        this.getPingStability = getPingStability;
    }

}

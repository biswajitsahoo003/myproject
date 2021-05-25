
package com.tcl.dias.servicefulfillmentutils.beans.wireless;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SetPingStabilityResponseBean {

    @JsonProperty("Response")
    private SetPingStablility setPingStablility;

    public SetPingStablility getSetPingStablility() {
        return setPingStablility;
    }

    public void setSetPingStablility(SetPingStablility setPingStablility) {
        this.setPingStablility = setPingStablility;
    }

}

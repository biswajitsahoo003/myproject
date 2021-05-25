package com.tcl.dias.servicefulfillmentutils.beans.wireless;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SetPingStablility {

    @JsonProperty("ss_unique_id")
    private String ssUniqueId;

    public String getSsUniqueId() {
        return ssUniqueId;
    }

    public void setSsUniqueId(String ssUniqueId) {
        this.ssUniqueId = ssUniqueId;
    }

}

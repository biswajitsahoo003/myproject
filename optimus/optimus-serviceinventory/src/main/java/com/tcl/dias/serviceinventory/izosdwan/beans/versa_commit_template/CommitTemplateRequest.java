
package com.tcl.dias.serviceinventory.izosdwan.beans.versa_commit_template;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "versanms.devices"
})
public class CommitTemplateRequest {

    @JsonProperty("versanms.devices")
    private VersanmsDevices versanmsDevices;

    @JsonProperty("versanms.devices")
    public VersanmsDevices getVersanmsDevices() {
        return versanmsDevices;
    }

    @JsonProperty("versanms.devices")
    public void setVersanmsDevices(VersanmsDevices versanmsDevices) {
        this.versanmsDevices = versanmsDevices;
    }

    @Override
    public String toString() {
        return "CommitTemplateRequest{" +
                "versanmsDevices=" + versanmsDevices +
                '}';
    }
}

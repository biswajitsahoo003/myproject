
package com.tcl.dias.serviceinventory.izosdwan.beans.versa_commit_template;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "device-list"
})
public class VersanmsDevices {

    @JsonProperty("device-list")
    private List<String> deviceList = null;

    @JsonProperty("device-list")
    public List<String> getDeviceList() {
        return deviceList;
    }

    @JsonProperty("device-list")
    public void setDeviceList(List<String> deviceList) {
        this.deviceList = deviceList;
    }

    @Override
    public String toString() {
        return "VersanmsDevices{" +
                "deviceList=" + deviceList +
                '}';
    }
}


package com.tcl.dias.serviceinventory.izosdwan.beans.versa_devicegroup_template;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "device-group"
})
public class DevicegroupTemplateMapping {

    @JsonProperty("device-group")
	private Set<String> deviceGroup = null;

    @JsonProperty("device-group")
    public Set<String> getDeviceGroup() {
        return deviceGroup;
    }

    @JsonProperty("device-group")
    public void setDeviceGroup(Set<String> deviceGroup) {
        this.deviceGroup = deviceGroup;
    }

}

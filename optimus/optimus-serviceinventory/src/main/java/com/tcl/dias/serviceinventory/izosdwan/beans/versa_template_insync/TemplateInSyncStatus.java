
package com.tcl.dias.serviceinventory.izosdwan.beans.versa_template_insync;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "versanms.deviceGroups"
})
public class TemplateInSyncStatus {

    @JsonProperty("versanms.deviceGroups")
    private VersanmsDeviceGroups versanmsDeviceGroups;

    @JsonProperty("versanms.deviceGroups")
    public VersanmsDeviceGroups getVersanmsDeviceGroups() {
        return versanmsDeviceGroups;
    }

    @JsonProperty("versanms.deviceGroups")
    public void setVersanmsDeviceGroups(VersanmsDeviceGroups versanmsDeviceGroups) {
        this.versanmsDeviceGroups = versanmsDeviceGroups;
    }

	@Override
	public String toString() {
		return "TemplateInSyncStatus [versanmsDeviceGroups=" + versanmsDeviceGroups + "]";
	}
    
    
    

}

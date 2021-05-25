
package com.tcl.dias.serviceinventory.izosdwan.beans.versa_template_insync;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.tcl.dias.serviceinventory.izosdwan.beans.versa_template_insync.DeviceGroupDatum;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "deviceGroupData"
})
public class VersanmsDeviceGroups {

    @JsonProperty("deviceGroupDataList")
    private List<DeviceGroupDatum> deviceGroupData = null;
    
    @JsonProperty("template-name")
    private String templateName;

    @JsonProperty("deviceGroupDataList")
    public List<DeviceGroupDatum> getDeviceGroupData() {
        return deviceGroupData;
    }

    @JsonProperty("deviceGroupDataList")
    public void setDeviceGroupData(List<DeviceGroupDatum> deviceGroupData) {
        this.deviceGroupData = deviceGroupData;
    }
    @JsonProperty("template-name")
	public String getTemplateName() {
		return templateName;
	}
    @JsonProperty("template-name")
	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	@Override
	public String toString() {
		return "VersanmsDeviceGroups [deviceGroupData=" + deviceGroupData + ", templateName=" + templateName + "]";
	}

	
    
}

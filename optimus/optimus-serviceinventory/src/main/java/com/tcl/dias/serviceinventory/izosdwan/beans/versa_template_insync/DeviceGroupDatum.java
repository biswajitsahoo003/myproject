
package com.tcl.dias.serviceinventory.izosdwan.beans.versa_template_insync;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.tcl.dias.serviceinventory.izosdwan.beans.versa_template_insync.DeviceDatum;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "name",
        "deviceData"
})
public class DeviceGroupDatum {

    @JsonProperty("name")
    private String name;
    @JsonProperty("deviceDataList")
    private List<DeviceDatum> deviceData = null;

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("deviceDataList")
    public List<DeviceDatum> getDeviceData() {
        return deviceData;
    }

    @JsonProperty("deviceDataList")
    public void setDeviceData(List<DeviceDatum> deviceData) {
        this.deviceData = deviceData;
    }

	public boolean equals(DeviceGroupDatum other) {
		return this.name.equals(other.name);
	}

	public int hashCode() {
		return name.hashCode();
	}

	@Override
	public String toString() {
		return "DeviceGroupDatum [name=" + name + ", deviceData=" + deviceData + "]";
	}
	
}

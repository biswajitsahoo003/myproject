
package com.tcl.dias.serviceinventory.izosdwan.beans.versa_template_insync;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "name",
    "status",
    "deviceModified"
})
public class DeviceDatum {

    @JsonProperty("name")
    private String name;
    @JsonProperty("status")
    private String status;
    @JsonProperty("deviceModified")
    private Boolean deviceModified;

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("status")
    public String getStatus() {
        return status;
    }

    @JsonProperty("status")
    public void setStatus(String status) {
        this.status = status;
    }

    @JsonProperty("deviceModified")
    public Boolean getDeviceModified() {
        return deviceModified;
    }

    @JsonProperty("deviceModified")
    public void setDeviceModified(Boolean deviceModified) {
        this.deviceModified = deviceModified;
    }

	@Override
	public String toString() {
		return "DeviceDatum [name=" + name + ", status=" + status + ", deviceModified=" + deviceModified + "]";
	}
    

}

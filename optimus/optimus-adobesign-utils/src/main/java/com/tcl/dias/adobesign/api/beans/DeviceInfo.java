
package com.tcl.dias.adobesign.api.beans;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "applicationDescription",
    "deviceDescription",
    "deviceTime"
})
public class DeviceInfo {

    @JsonProperty("applicationDescription")
    private String applicationDescription;
    @JsonProperty("deviceDescription")
    private String deviceDescription;
    @JsonProperty("deviceTime")
    private String deviceTime;

    @JsonProperty("applicationDescription")
    public String getApplicationDescription() {
        return applicationDescription;
    }

    @JsonProperty("applicationDescription")
    public void setApplicationDescription(String applicationDescription) {
        this.applicationDescription = applicationDescription;
    }

    @JsonProperty("deviceDescription")
    public String getDeviceDescription() {
        return deviceDescription;
    }

    @JsonProperty("deviceDescription")
    public void setDeviceDescription(String deviceDescription) {
        this.deviceDescription = deviceDescription;
    }

    @JsonProperty("deviceTime")
    public String getDeviceTime() {
        return deviceTime;
    }

    @JsonProperty("deviceTime")
    public void setDeviceTime(String deviceTime) {
        this.deviceTime = deviceTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("applicationDescription", applicationDescription).append("deviceDescription", deviceDescription).append("deviceTime", deviceTime).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(deviceDescription).append(deviceTime).append(applicationDescription).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof DeviceInfo) == false) {
            return false;
        }
        DeviceInfo rhs = ((DeviceInfo) other);
        return new EqualsBuilder().append(deviceDescription, rhs.deviceDescription).append(deviceTime, rhs.deviceTime).append(applicationDescription, rhs.applicationDescription).isEquals();
    }

}

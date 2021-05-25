
package com.tcl.dias.serviceinventory.izosdwan.beans.versa_qos_profile;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "name",
        "peak-kbps-rate",
        "forwarding-class",
        "loss-priority",
        "dscp-rw-enable",
        "dot1p-rw-enable",
        "peak-burst-size"
})
public class QosProfile implements Serializable {

    @JsonProperty("name")
    private String name;
    @JsonProperty("peak-kbps-rate")
    private String peakKbpsRate;
    @JsonProperty("forwarding-class")
    private String forwardingClass;
    @JsonProperty("loss-priority")
    private String lossPriority;
    @JsonProperty("dscp-rw-enable")
    private String dscpRwEnable;
    @JsonProperty("dot1p-rw-enable")
    private String dot1pRwEnable;
    @JsonProperty("peak-burst-size")
    private String peakBurstSize;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    private final static long serialVersionUID = 5707093804084560394L;

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("peak-kbps-rate")
    public String getPeakKbpsRate() {
        return peakKbpsRate;
    }

    @JsonProperty("peak-kbps-rate")
    public void setPeakKbpsRate(String peakKbpsRate) {
        this.peakKbpsRate = peakKbpsRate;
    }

    @JsonProperty("forwarding-class")
    public String getForwardingClass() {
        return forwardingClass;
    }

    @JsonProperty("forwarding-class")
    public void setForwardingClass(String forwardingClass) {
        this.forwardingClass = forwardingClass;
    }

    @JsonProperty("loss-priority")
    public String getLossPriority() {
        return lossPriority;
    }

    @JsonProperty("loss-priority")
    public void setLossPriority(String lossPriority) {
        this.lossPriority = lossPriority;
    }

    @JsonProperty("dscp-rw-enable")
    public String getDscpRwEnable() {
        return dscpRwEnable;
    }

    @JsonProperty("dscp-rw-enable")
    public void setDscpRwEnable(String dscpRwEnable) {
        this.dscpRwEnable = dscpRwEnable;
    }

    @JsonProperty("dot1p-rw-enable")
    public String getDot1pRwEnable() {
        return dot1pRwEnable;
    }

    @JsonProperty("dot1p-rw-enable")
    public void setDot1pRwEnable(String dot1pRwEnable) {
        this.dot1pRwEnable = dot1pRwEnable;
    }

    @JsonProperty("peak-burst-size")
    public String getPeakBurstSize() {
        return peakBurstSize;
    }

    @JsonProperty("peak-burst-size")
    public void setPeakBurstSize(String peakBurstSize) {
        this.peakBurstSize = peakBurstSize;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }
}

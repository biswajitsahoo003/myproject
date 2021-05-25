
package com.tcl.dias.serviceinventory.izosdwan.beans.versa_policy_rule.traffic_steering;

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
    "interval",
    "threshold"
})
public class Monitor implements Serializable
{

    @JsonProperty("interval")
    private String interval;
    @JsonProperty("threshold")
    private String threshold;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    private final static long serialVersionUID = -4092405291881688900L;

    @JsonProperty("interval")
    public String getInterval() {
        return interval;
    }

    @JsonProperty("interval")
    public void setInterval(String interval) {
        this.interval = interval;
    }

    @JsonProperty("threshold")
    public String getThreshold() {
        return threshold;
    }

    @JsonProperty("threshold")
    public void setThreshold(String threshold) {
        this.threshold = threshold;
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

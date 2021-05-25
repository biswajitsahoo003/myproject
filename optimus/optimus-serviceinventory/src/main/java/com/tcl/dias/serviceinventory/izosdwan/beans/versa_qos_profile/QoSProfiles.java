
package com.tcl.dias.serviceinventory.izosdwan.beans.versa_qos_profile;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "qos-profile"
})
public class QoSProfiles implements Serializable
{

    @JsonProperty("qos-profile")
    private List<QosProfile> qosProfile = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    private final static long serialVersionUID = -1475110133636954790L;

    @JsonProperty("qos-profile")
    public List<QosProfile> getQosProfile() {
        return qosProfile;
    }

    @JsonProperty("qos-profile")
    public void setQosProfile(List<QosProfile> qosProfile) {
        this.qosProfile = qosProfile;
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

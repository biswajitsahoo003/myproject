
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
    "action",
    "forwarding-profile",
    "lef"
})
public class Set implements Serializable
{

    @JsonProperty("action")
    private String action;
    @JsonProperty("forwarding-profile")
    private String forwardingProfile;
    @JsonProperty("lef")
    private Lef lef;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    private final static long serialVersionUID = -3291545779385073145L;

    @JsonProperty("action")
    public String getAction() {
        return action;
    }

    @JsonProperty("action")
    public void setAction(String action) {
        this.action = action;
    }

    @JsonProperty("forwarding-profile")
    public String getForwardingProfile() {
        return forwardingProfile;
    }

    @JsonProperty("forwarding-profile")
    public void setForwardingProfile(String forwardingProfile) {
        this.forwardingProfile = forwardingProfile;
    }

    @JsonProperty("lef")
    public Lef getLef() {
        return lef;
    }

    @JsonProperty("lef")
    public void setLef(Lef lef) {
        this.lef = lef;
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

package com.tcl.dias.serviceinventory.izosdwan.beans.versa_firewall_get_configuration;

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
    "security-profile",
    "action",
    "lef"
})
public class Set {

    @JsonProperty("security-profile")
    private SecurityProfile securityProfile;
    @JsonProperty("action")
    private String action;
    @JsonProperty("lef")
    private Lef lef;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("security-profile")
    public SecurityProfile getSecurityProfile() {
        return securityProfile;
    }

    @JsonProperty("security-profile")
    public void setSecurityProfile(SecurityProfile securityProfile) {
        this.securityProfile = securityProfile;
    }

    @JsonProperty("action")
    public String getAction() {
        return action;
    }

    @JsonProperty("action")
    public void setAction(String action) {
        this.action = action;
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

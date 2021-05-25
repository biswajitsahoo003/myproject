package com.tcl.dias.serviceinventory.izosdwan.beans.versa_firewall_policies;

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
    "access-policy"
})
public class Rules {

    @JsonProperty("access-policy")
    private List<AccessPolicy> accessPolicy = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("access-policy")
    public List<AccessPolicy> getAccessPolicy() {
        return accessPolicy;
    }

    @JsonProperty("access-policy")
    public void setAccessPolicy(List<AccessPolicy> accessPolicy) {
        this.accessPolicy = accessPolicy;
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

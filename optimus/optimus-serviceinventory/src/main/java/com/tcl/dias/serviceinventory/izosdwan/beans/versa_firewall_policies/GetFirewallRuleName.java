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
    "access-policy-group"
})
public class GetFirewallRuleName {

    @JsonProperty("access-policy-group")
    private List<AccessPolicyGroup> accessPolicyGroup = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("access-policy-group")
    public List<AccessPolicyGroup> getAccessPolicyGroup() {
        return accessPolicyGroup;
    }

    @JsonProperty("access-policy-group")
    public void setAccessPolicyGroup(List<AccessPolicyGroup> accessPolicyGroup) {
        this.accessPolicyGroup = accessPolicyGroup;
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

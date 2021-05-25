
package com.tcl.dias.serviceinventory.izosdwan.beans.versa_sla_profile;

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
    "sla-profile"
})
public class SlaProfiles implements Serializable
{

    @JsonProperty("sla-profile")
    private List<SlaProfile_> slaProfile = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    private final static long serialVersionUID = 4846833882594395300L;

    @JsonProperty("sla-profile")
    public List<SlaProfile_> getSlaProfile() {
        return slaProfile;
    }

    @JsonProperty("sla-profile")
    public void setSlaProfile(List<SlaProfile_> slaProfile) {
        this.slaProfile = slaProfile;
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

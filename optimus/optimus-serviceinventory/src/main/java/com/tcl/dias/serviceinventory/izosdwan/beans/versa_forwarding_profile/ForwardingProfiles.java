
package com.tcl.dias.serviceinventory.izosdwan.beans.versa_forwarding_profile;

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
    "forwarding-profile"
})
public class ForwardingProfiles implements Serializable {

    @JsonProperty("forwarding-profile")
    private List<ForwardingProfile_> forwardingProfile = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    private final static long serialVersionUID = 9076281856459071576L;

    @JsonProperty("forwarding-profile")
    public List<ForwardingProfile_> getForwardingProfile() {
        return forwardingProfile;
    }

    @JsonProperty("forwarding-profile")
    public void setForwardingProfile(List<ForwardingProfile_> forwardingProfile) {
        this.forwardingProfile = forwardingProfile;
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

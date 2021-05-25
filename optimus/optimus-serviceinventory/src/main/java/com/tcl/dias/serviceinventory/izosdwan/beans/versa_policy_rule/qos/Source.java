
package com.tcl.dias.serviceinventory.izosdwan.beans.versa_policy_rule.qos;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.tcl.dias.serviceinventory.izosdwan.beans.versa_policy_rule.qos.Zone;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "user"
})
public class Source implements Serializable
{

    @JsonProperty("zone")
    private Zone zone;
    @JsonProperty("address")
    private Address address;
    @JsonProperty("user")
    private User user;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    private final static long serialVersionUID = 4635376910823418450L;

    @JsonProperty("user")
    public User getUser() {
        return user;
    }

    @JsonProperty("user")
    public void setUser(User user) {
        this.user = user;
    }


    @JsonProperty("zone")
    public Zone getZone() {
        return zone;
    }

    @JsonProperty("zone")
    public void setZone(Zone zone) {
        this.zone = zone;
    }

    @JsonProperty("address")
    public Address getAddress() {
        return address;
    }

    @JsonProperty("address")
    public void setAddress(Address address) {
        this.address = address;
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

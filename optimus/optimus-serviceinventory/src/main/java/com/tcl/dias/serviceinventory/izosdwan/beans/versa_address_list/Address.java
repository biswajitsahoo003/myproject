
package com.tcl.dias.serviceinventory.izosdwan.beans.versa_address_list;

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
    "ipv4-prefix"
})
public class Address implements Serializable
{

    @JsonProperty("name")
    private String name;
    @JsonProperty("ipv4-prefix")
    private String ipv4Prefix;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    private final static long serialVersionUID = 6734235689005933285L;

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("ipv4-prefix")
    public String getIpv4Prefix() {
        return ipv4Prefix;
    }

    @JsonProperty("ipv4-prefix")
    public void setIpv4Prefix(String ipv4Prefix) {
        this.ipv4Prefix = ipv4Prefix;
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

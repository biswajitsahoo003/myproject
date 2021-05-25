
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
    "local",
    "remote"
})
public class CircuitNames implements Serializable
{

    @JsonProperty("local")
    private List<String> local = null;
    @JsonProperty("remote")
    private List<String> remote = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    private final static long serialVersionUID = -2645728198011125027L;

    @JsonProperty("local")
    public List<String> getLocal() {
        return local;
    }

    @JsonProperty("local")
    public void setLocal(List<String> local) {
        this.local = local;
    }

    @JsonProperty("remote")
    public List<String> getRemote() {
        return remote;
    }

    @JsonProperty("remote")
    public void setRemote(List<String> remote) {
        this.remote = remote;
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

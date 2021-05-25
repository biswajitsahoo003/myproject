
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
    "priority",
    "avoid"
})
public class CircuitPriorities implements Serializable
{

    @JsonProperty("priority")
    private List<Priority> priority = null;
    @JsonProperty("avoid")
    private Avoid avoid;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    private final static long serialVersionUID = 2613355251702331551L;

    @JsonProperty("priority")
    public List<Priority> getPriority() {
        return priority;
    }

    @JsonProperty("priority")
    public void setPriority(List<Priority> priority) {
        this.priority = priority;
    }

    @JsonProperty("avoid")
    public Avoid getAvoid() {
        return avoid;
    }

    @JsonProperty("avoid")
    public void setAvoid(Avoid avoid) {
        this.avoid = avoid;
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

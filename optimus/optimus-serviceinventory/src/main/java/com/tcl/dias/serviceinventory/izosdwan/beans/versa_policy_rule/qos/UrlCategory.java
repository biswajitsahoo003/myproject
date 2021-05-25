
package com.tcl.dias.serviceinventory.izosdwan.beans.versa_policy_rule.qos;

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
    "predefined",
    "user-defined"
})
public class UrlCategory implements Serializable
{

    @JsonProperty("predefined")
    private List<String> predefined = null;
    @JsonProperty("user-defined")
    private List<String> userDefined = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    private final static long serialVersionUID = -2158003315058301136L;

    @JsonProperty("predefined")
    public List<String> getPredefined() {
        return predefined;
    }

    @JsonProperty("predefined")
    public void setPredefined(List<String> predefined) {
        this.predefined = predefined;
    }

    @JsonProperty("user-defined")
    public List<String> getUserDefined() {
        return userDefined;
    }

    @JsonProperty("user-defined")
    public void setUserDefined(List<String> userDefined) {
        this.userDefined = userDefined;
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

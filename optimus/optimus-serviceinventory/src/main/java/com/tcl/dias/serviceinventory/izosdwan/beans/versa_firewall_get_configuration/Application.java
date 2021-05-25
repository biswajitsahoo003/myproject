package com.tcl.dias.serviceinventory.izosdwan.beans.versa_firewall_get_configuration;


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
    "predefined-application-list",
    "user-defined-application-list"
})
public class Application {

    @JsonProperty("predefined-application-list")
    private List<String> predefinedApplicationList = null;
    @JsonProperty("user-defined-application-list")
    private List<String> userDefinedApplicationList = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("predefined-application-list")
    public List<String> getPredefinedApplicationList() {
        return predefinedApplicationList;
    }

    @JsonProperty("predefined-application-list")
    public void setPredefinedApplicationList(List<String> predefinedApplicationList) {
        this.predefinedApplicationList = predefinedApplicationList;
    }

    @JsonProperty("user-defined-application-list")
    public List<String> getUserDefinedApplicationList() {
        return userDefinedApplicationList;
    }

    @JsonProperty("user-defined-application-list")
    public void setUserDefinedApplicationList(List<String> userDefinedApplicationList) {
        this.userDefinedApplicationList = userDefinedApplicationList;
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

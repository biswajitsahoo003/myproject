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
    "predefined-services-list",
    "services-list"
})
public class Services {

    @JsonProperty("predefined-services-list")
    private List<String> predefinedServicesList = null;
    @JsonProperty("services-list")
    private List<String> servicesList = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("predefined-services-list")
    public List<String> getPredefinedServicesList() {
        return predefinedServicesList;
    }

    @JsonProperty("predefined-services-list")
    public void setPredefinedServicesList(List<String> predefinedServicesList) {
        this.predefinedServicesList = predefinedServicesList;
    }

    @JsonProperty("services-list")
    public List<String> getServicesList() {
        return servicesList;
    }

    @JsonProperty("services-list")
    public void setServicesList(List<String> servicesList) {
        this.servicesList = servicesList;
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

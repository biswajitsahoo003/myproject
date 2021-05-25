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
import org.apache.commons.lang3.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "@xmlns",
    "service"
})
public class PredefinedServices {

    @JsonProperty("@xmlns")
    private String xmlns;
    @JsonProperty("service")
    private List<Service> service = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("@xmlns")
    public String getXmlns() {
        return xmlns;
    }

    @JsonProperty("@xmlns")
    public void setXmlns(String xmlns) {
        this.xmlns = xmlns;
    }

    @JsonProperty("service")
    public List<Service> getService() {
        return service;
    }

    @JsonProperty("service")
    public void setService(List<Service> service) {
        this.service = service;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("xmlns", xmlns).append("service", service).append("additionalProperties", additionalProperties).toString();
    }

}

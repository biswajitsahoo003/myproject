package com.tcl.dias.serviceinventory.izosdwan.beans.versa_firewall_get_configuration;


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
    "source",
    "destination",
    "services",
    "url-category",
    "application"
})
public class Match {

    @JsonProperty("source")
    private Source source;
    @JsonProperty("destination")
    private Destination destination;
    @JsonProperty("services")
    private Services services;
    @JsonProperty("url-category")
    private UrlCategory urlCategory;
    @JsonProperty("application")
    private Application application;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("source")
    public Source getSource() {
        return source;
    }

    @JsonProperty("source")
    public void setSource(Source source) {
        this.source = source;
    }

    @JsonProperty("destination")
    public Destination getDestination() {
        return destination;
    }

    @JsonProperty("destination")
    public void setDestination(Destination destination) {
        this.destination = destination;
    }

    @JsonProperty("services")
    public Services getServices() {
        return services;
    }

    @JsonProperty("services")
    public void setServices(Services services) {
        this.services = services;
    }

    @JsonProperty("url-category")
    public UrlCategory getUrlCategory() {
        return urlCategory;
    }

    @JsonProperty("url-category")
    public void setUrlCategory(UrlCategory urlCategory) {
        this.urlCategory = urlCategory;
    }

    @JsonProperty("application")
    public Application getApplication() {
        return application;
    }

    @JsonProperty("application")
    public void setApplication(Application application) {
        this.application = application;
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

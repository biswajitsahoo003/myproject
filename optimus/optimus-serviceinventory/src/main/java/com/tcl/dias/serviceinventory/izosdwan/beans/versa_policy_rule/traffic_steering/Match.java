
package com.tcl.dias.serviceinventory.izosdwan.beans.versa_policy_rule.traffic_steering;

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
    "source",
    "destination",
    "application",
    "url-category",
    "dscp"
})
public class Match implements Serializable
{

    @JsonProperty("source")
    private Source source;
    @JsonProperty("destination")
    private Destination destination;
    @JsonProperty("application")
    private Application application;
    @JsonProperty("url-category")
    private UrlCategory urlCategory;
    @JsonProperty("dscp")
    private List<String> dscp = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    private final static long serialVersionUID = -5029159948698793620L;

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

    @JsonProperty("application")
    public Application getApplication() {
        return application;
    }

    @JsonProperty("application")
    public void setApplication(Application application) {
        this.application = application;
    }

    @JsonProperty("url-category")
    public UrlCategory getUrlCategory() {
        return urlCategory;
    }

    @JsonProperty("url-category")
    public void setUrlCategory(UrlCategory urlCategory) {
        this.urlCategory = urlCategory;
    }

    @JsonProperty("dscp")
    public List<String> getDscp() {
        return dscp;
    }

    @JsonProperty("dscp")
    public void setDscp(List<String> dscp) {
        this.dscp = dscp;
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

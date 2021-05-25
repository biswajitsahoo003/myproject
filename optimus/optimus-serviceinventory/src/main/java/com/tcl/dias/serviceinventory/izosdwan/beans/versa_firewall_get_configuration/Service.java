package com.tcl.dias.serviceinventory.izosdwan.beans.versa_firewall_get_configuration;

import java.util.HashMap;
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
    "name",
    "destination-port",
    "source-port",
    "proto-name",
    "proto-id",
    "spack-introduced"
})
public class Service {

    @JsonProperty("name")
    private String name;
    @JsonProperty("destination-port")
    private String destinationPort;
    @JsonProperty("source-port")
    private String sourcePort;
    @JsonProperty("proto-name")
    private String protoName;
    @JsonProperty("proto-id")
    private String protoId;
    @JsonProperty("spack-introduced")
    private String spackIntroduced;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("destination-port")
    public String getDestinationPort() {
        return destinationPort;
    }

    @JsonProperty("destination-port")
    public void setDestinationPort(String destinationPort) {
        this.destinationPort = destinationPort;
    }

    @JsonProperty("source-port")
    public String getSourcePort() {
        return sourcePort;
    }

    @JsonProperty("source-port")
    public void setSourcePort(String sourcePort) {
        this.sourcePort = sourcePort;
    }

    @JsonProperty("proto-name")
    public String getProtoName() {
        return protoName;
    }

    @JsonProperty("proto-name")
    public void setProtoName(String protoName) {
        this.protoName = protoName;
    }

    @JsonProperty("proto-id")
    public String getProtoId() {
        return protoId;
    }

    @JsonProperty("proto-id")
    public void setProtoId(String protoId) {
        this.protoId = protoId;
    }

    @JsonProperty("spack-introduced")
    public String getSpackIntroduced() {
        return spackIntroduced;
    }

    @JsonProperty("spack-introduced")
    public void setSpackIntroduced(String spackIntroduced) {
        this.spackIntroduced = spackIntroduced;
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
        return new ToStringBuilder(this).append("name", name).append("destinationPort", destinationPort).append("sourcePort", sourcePort).append("protoName", protoName).append("protoId", protoId).append("spackIntroduced", spackIntroduced).append("additionalProperties", additionalProperties).toString();
    }

}

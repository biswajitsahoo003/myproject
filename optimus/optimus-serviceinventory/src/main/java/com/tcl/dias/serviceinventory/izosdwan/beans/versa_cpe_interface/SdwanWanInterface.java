package com.tcl.dias.serviceinventory.izosdwan.beans.versa_cpe_interface;

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
    "intf-name",
    "circuit-family",
    "circuit-name",
    "link-id",
    "endpt-ip",
    "nat-status",
    "public-ip",
    "public-port",
    "link-encryption",
    "shaping-rate",
    "min-shaping-rate"
})
public class SdwanWanInterface {

    @JsonProperty("intf-name")
    private String intfName;
    @JsonProperty("circuit-family")
    private String circuitFamily;
    @JsonProperty("circuit-name")
    private String circuitName;
    @JsonProperty("link-id")
    private Integer linkId;
    @JsonProperty("endpt-ip")
    private String endptIp;
    @JsonProperty("nat-status")
    private String natStatus;
    @JsonProperty("public-ip")
    private String publicIp;
    @JsonProperty("public-port")
    private Integer publicPort;
    @JsonProperty("link-encryption")
    private String linkEncryption;
    @JsonProperty("shaping-rate")
    private Integer shapingRate;
    @JsonProperty("min-shaping-rate")
    private Integer minShapingRate;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("intf-name")
    public String getIntfName() {
        return intfName;
    }

    @JsonProperty("intf-name")
    public void setIntfName(String intfName) {
        this.intfName = intfName;
    }

    @JsonProperty("circuit-family")
    public String getCircuitFamily() {
        return circuitFamily;
    }

    @JsonProperty("circuit-family")
    public void setCircuitFamily(String circuitFamily) {
        this.circuitFamily = circuitFamily;
    }

    @JsonProperty("circuit-name")
    public String getCircuitName() {
        return circuitName;
    }

    @JsonProperty("circuit-name")
    public void setCircuitName(String circuitName) {
        this.circuitName = circuitName;
    }

    @JsonProperty("link-id")
    public Integer getLinkId() {
        return linkId;
    }

    @JsonProperty("link-id")
    public void setLinkId(Integer linkId) {
        this.linkId = linkId;
    }

    @JsonProperty("endpt-ip")
    public String getEndptIp() {
        return endptIp;
    }

    @JsonProperty("endpt-ip")
    public void setEndptIp(String endptIp) {
        this.endptIp = endptIp;
    }

    @JsonProperty("nat-status")
    public String getNatStatus() {
        return natStatus;
    }

    @JsonProperty("nat-status")
    public void setNatStatus(String natStatus) {
        this.natStatus = natStatus;
    }

    @JsonProperty("public-ip")
    public String getPublicIp() {
        return publicIp;
    }

    @JsonProperty("public-ip")
    public void setPublicIp(String publicIp) {
        this.publicIp = publicIp;
    }

    @JsonProperty("public-port")
    public Integer getPublicPort() {
        return publicPort;
    }

    @JsonProperty("public-port")
    public void setPublicPort(Integer publicPort) {
        this.publicPort = publicPort;
    }

    @JsonProperty("link-encryption")
    public String getLinkEncryption() {
        return linkEncryption;
    }

    @JsonProperty("link-encryption")
    public void setLinkEncryption(String linkEncryption) {
        this.linkEncryption = linkEncryption;
    }

    @JsonProperty("shaping-rate")
    public Integer getShapingRate() {
        return shapingRate;
    }

    @JsonProperty("shaping-rate")
    public void setShapingRate(Integer shapingRate) {
        this.shapingRate = shapingRate;
    }

    @JsonProperty("min-shaping-rate")
    public Integer getMinShapingRate() {
        return minShapingRate;
    }

    @JsonProperty("min-shaping-rate")
    public void setMinShapingRate(Integer minShapingRate) {
        this.minShapingRate = minShapingRate;
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

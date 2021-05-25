
package com.tcl.dias.serviceinventory.izosdwan.beans.versa_template_link_details;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "name",
    "circuit-name",
    "transport-domains",
    "inet"
})
public class Vni {

    @JsonProperty("name")
    private String name;
    @JsonProperty("circuit-name")
    private String circuitName;
    @JsonProperty("transport-domains")
    private List<String> transportDomains = null;
    @JsonProperty("inet")
    private Inet inet;

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("circuit-name")
    public String getCircuitName() {
        return circuitName;
    }

    @JsonProperty("circuit-name")
    public void setCircuitName(String circuitName) {
        this.circuitName = circuitName;
    }

    @JsonProperty("transport-domains")
    public List<String> getTransportDomains() {
        return transportDomains;
    }

    @JsonProperty("transport-domains")
    public void setTransportDomains(List<String> transportDomains) {
        this.transportDomains = transportDomains;
    }

    @JsonProperty("inet")
    public Inet getInet() {
        return inet;
    }

    @JsonProperty("inet")
    public void setInet(Inet inet) {
        this.inet = inet;
    }

}


package com.tcl.dias.serviceinventory.izosdwan.beans.versa_cpe_interface;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "name",
    "enable",
    "promiscuous",
    "bandwidth",
    "unit",
    "access-point",
    "ether-options"
})
public class Vni {

    @JsonProperty("name")
    private String name;
    @JsonProperty("enable")
    private Boolean enable;
    @JsonProperty("promiscuous")
    private Boolean promiscuous;
    @JsonProperty("bandwidth")
    private Bandwidth bandwidth;

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("enable")
    public Boolean getEnable() {
        return enable;
    }

    @JsonProperty("enable")
    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    @JsonProperty("promiscuous")
    public Boolean getPromiscuous() {
        return promiscuous;
    }

    @JsonProperty("promiscuous")
    public void setPromiscuous(Boolean promiscuous) {
        this.promiscuous = promiscuous;
    }

    @JsonProperty("bandwidth")
    public Bandwidth getBandwidth() {
        return bandwidth;
    }

    @JsonProperty("bandwidth")
    public void setBandwidth(Bandwidth bandwidth) {
        this.bandwidth = bandwidth;
    }
}

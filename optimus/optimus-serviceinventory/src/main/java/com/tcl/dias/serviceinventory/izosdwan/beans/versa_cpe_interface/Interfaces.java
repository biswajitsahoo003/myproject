
package com.tcl.dias.serviceinventory.izosdwan.beans.versa_cpe_interface;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "port",
    "wwan",
    "vni",
    "tvi",
    "ptvi",
    "operations"
})
public class Interfaces {

    @JsonProperty("vni")
    private List<Vni> vni = null;

    @JsonProperty("vni")
    public List<Vni> getVni() {
        return vni;
    }

    @JsonProperty("vni")
    public void setVni(List<Vni> vni) {
        this.vni = vni;
    }
}

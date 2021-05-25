
package com.tcl.dias.serviceinventory.izosdwan.beans.versa_cpe_interface;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "interfaces"
})
public class ConfiguredInterfaceBw {

    @JsonProperty("interfaces")
    private Interfaces interfaces;

    @JsonProperty("interfaces")
    public Interfaces getInterfaces() {
        return interfaces;
    }

    @JsonProperty("interfaces")
    public void setInterfaces(Interfaces interfaces) {
        this.interfaces = interfaces;
    }

}

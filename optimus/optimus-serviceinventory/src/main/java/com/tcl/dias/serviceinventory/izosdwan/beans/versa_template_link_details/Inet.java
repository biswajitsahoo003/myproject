
package com.tcl.dias.serviceinventory.izosdwan.beans.versa_template_link_details;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "circuit-name"
})
public class Inet {

    @JsonProperty("circuit-name")
    private String circuitName;

    @JsonProperty("circuit-name")
    public String getCircuitName() {
        return circuitName;
    }

    @JsonProperty("circuit-name")
    public void setCircuitName(String circuitName) {
        this.circuitName = circuitName;
    }

}

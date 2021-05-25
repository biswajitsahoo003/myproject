
package com.tcl.dias.servicefulfillmentutils.sap.cable.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "Display_Material"
})
public class CableMaterialResponse {

    @JsonProperty("Display_Material")
    private List<CableDisplayMaterial> displayMaterial = null;

    @JsonProperty("Display_Material")
    public List<CableDisplayMaterial> getDisplayMaterial() {
        return displayMaterial;
    }

    @JsonProperty("Display_Material")
    public void setDisplayMaterial(List<CableDisplayMaterial> displayMaterial) {
        this.displayMaterial = displayMaterial;
    }

}

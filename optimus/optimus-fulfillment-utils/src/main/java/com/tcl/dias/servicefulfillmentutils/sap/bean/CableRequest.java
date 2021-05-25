
package com.tcl.dias.servicefulfillmentutils.sap.bean;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "Display_Material"
})
public class CableRequest {

    @JsonProperty("Display_Material")
    private List<DisplayMaterial> displayMaterial = null;

    @JsonProperty("Display_Material")
    public List<DisplayMaterial> getDisplayMaterial() {
    	
    	if(displayMaterial==null) {
    		displayMaterial=new ArrayList<DisplayMaterial>();
    	}
        return displayMaterial;
    }

    @JsonProperty("Display_Material")
    public void setDisplayMaterial(List<DisplayMaterial> displayMaterial) {
        this.displayMaterial = displayMaterial;
    }

}

package com.tcl.dias.servicefulfillmentutils.sap.response;



import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.tcl.dias.common.utils.Status;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "Display_Material"
})
public class SapQuantityResponse {
	
	private Status status;
	private String errorMessage;

    @JsonProperty("Display_Material")
    private List<DisplayMaterialResponse> displayMaterial = null;

    @JsonProperty("Display_Material")
    public List<DisplayMaterialResponse> getDisplayMaterial() {
        return displayMaterial;
    }

    @JsonProperty("Display_Material")
    public void setDisplayMaterial(List<DisplayMaterialResponse> displayMaterial) {
        this.displayMaterial = displayMaterial;
    }

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

    
    
}

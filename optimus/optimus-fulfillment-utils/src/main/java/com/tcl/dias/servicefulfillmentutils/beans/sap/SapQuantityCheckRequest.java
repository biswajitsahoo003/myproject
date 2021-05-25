package com.tcl.dias.servicefulfillmentutils.beans.sap;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "Display_Material" })
public class SapQuantityCheckRequest {

	@JsonProperty("Display_Material")
	private List<DisplayMaterialRequest> displayMaterial = null;

	@JsonProperty("Display_Material")
	public List<DisplayMaterialRequest> getDisplayMaterial() {

		if (displayMaterial == null) {
			displayMaterial = new ArrayList<DisplayMaterialRequest>();
		}
		return displayMaterial;
	}

	@JsonProperty("Display_Material")
	public void setDisplayMaterial(List<DisplayMaterialRequest> displayMaterial) {
		this.displayMaterial = displayMaterial;
	}

}

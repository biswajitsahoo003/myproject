package com.tcl.dias.servicefulfillmentutils.sap.response;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SapQuantityAvailableRequest {

	private List<DisplayMaterialResponse> displayMaterial;

	public List<DisplayMaterialResponse> getDisplayMaterial() {
		
		if(displayMaterial==null) {
			displayMaterial=new ArrayList<DisplayMaterialResponse>();
		}
		return displayMaterial;
	}

	public void setDisplayMaterial(List<DisplayMaterialResponse> displayMaterial) {
		this.displayMaterial = displayMaterial;
	}

}

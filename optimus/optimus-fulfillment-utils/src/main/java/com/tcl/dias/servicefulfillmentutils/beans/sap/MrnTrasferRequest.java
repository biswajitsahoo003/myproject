package com.tcl.dias.servicefulfillmentutils.beans.sap;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "Material_Transfer" })
public class MrnTrasferRequest {

	@JsonProperty("Material_Transfer")
	private List<MaterialTransfer> materialTransfer = null;

	@JsonProperty("Material_Transfer")
	public List<MaterialTransfer> getMaterialTransfer() {
		
		if(materialTransfer==null) {
			materialTransfer=new ArrayList<MaterialTransfer>();
		}
		return materialTransfer;
	}

	@JsonProperty("Material_Transfer")
	public void setMaterialTransfer(List<MaterialTransfer> materialTransfer) {
		this.materialTransfer = materialTransfer;
	}

}

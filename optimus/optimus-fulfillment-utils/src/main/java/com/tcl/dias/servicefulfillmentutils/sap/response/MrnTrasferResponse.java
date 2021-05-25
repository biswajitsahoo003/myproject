package com.tcl.dias.servicefulfillmentutils.sap.response;



import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "Material_Transfer"
})
public class MrnTrasferResponse {

    @JsonProperty("Material_Transfer")
    private List<MaterialTransfer> materialTransfer;

	public List<MaterialTransfer> getMaterialTransfer() {
		
		if(materialTransfer==null) {
			materialTransfer=new ArrayList<MaterialTransfer>();
		}
		return materialTransfer;
	}

	public void setMaterialTransfer(List<MaterialTransfer> materialTransfer) {
		this.materialTransfer = materialTransfer;
	}


}

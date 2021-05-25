
package com.tcl.dias.servicefulfillmentutils.beans.sap.hana;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "OPTIMUS_Material_Stock_Dis"
})
public class InventoryDetailRequest {

    @JsonProperty("OPTIMUS_Material_Stock_Dis")
    private List<OPTIMUSMaterialStockDi> oPTIMUSMaterialStockDis = null;

    @JsonProperty("OPTIMUS_Material_Stock_Dis")
    public List<OPTIMUSMaterialStockDi> getOPTIMUSMaterialStockDis() {
    	if(oPTIMUSMaterialStockDis == null) {
    		oPTIMUSMaterialStockDis = new ArrayList<>();
    	}
        return oPTIMUSMaterialStockDis;
    }

    @JsonProperty("OPTIMUS_Material_Stock_Dis")
    public void setOPTIMUSMaterialStockDis(List<OPTIMUSMaterialStockDi> oPTIMUSMaterialStockDis) {
        this.oPTIMUSMaterialStockDis = oPTIMUSMaterialStockDis;
    }

}

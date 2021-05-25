
package com.tcl.dias.servicefulfillmentutils.beans.sap.hana;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "S4HANA_OPTIMUS_Material_Stock_Dis_Response"
})
public class MTS4HANAOPTIMUSMaterialStockDisResponse {

    @JsonProperty("S4HANA_OPTIMUS_Material_Stock_Dis_Response")
    private List<S4HANAOPTIMUSMaterialStockDisResponse> s4HANAOPTIMUSMaterialStockDisResponse = null;

    @JsonProperty("S4HANA_OPTIMUS_Material_Stock_Dis_Response")
    public List<S4HANAOPTIMUSMaterialStockDisResponse> getS4HANAOPTIMUSMaterialStockDisResponse() {
    	if(s4HANAOPTIMUSMaterialStockDisResponse == null) {
    		s4HANAOPTIMUSMaterialStockDisResponse = new ArrayList<>();
    	}
        return s4HANAOPTIMUSMaterialStockDisResponse;
    }

    @JsonProperty("S4HANA_OPTIMUS_Material_Stock_Dis_Response")
    public void setS4HANAOPTIMUSMaterialStockDisResponse(List<S4HANAOPTIMUSMaterialStockDisResponse> s4HANAOPTIMUSMaterialStockDisResponse) {
        this.s4HANAOPTIMUSMaterialStockDisResponse = s4HANAOPTIMUSMaterialStockDisResponse;
    }

}

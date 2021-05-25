
package com.tcl.dias.servicefulfillmentutils.beans.sap.hana;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "MT_S4HANA_OPTIMUS_Material_Stock_Dis_Response"
})
public class InventoryDetailResponse {

    @JsonProperty("MT_S4HANA_OPTIMUS_Material_Stock_Dis_Response")
    private MTS4HANAOPTIMUSMaterialStockDisResponse mTS4HANAOPTIMUSMaterialStockDisResponse;

    @JsonProperty("MT_S4HANA_OPTIMUS_Material_Stock_Dis_Response")
    public MTS4HANAOPTIMUSMaterialStockDisResponse getMTS4HANAOPTIMUSMaterialStockDisResponse() {
    	if(mTS4HANAOPTIMUSMaterialStockDisResponse == null) {
    		mTS4HANAOPTIMUSMaterialStockDisResponse = new MTS4HANAOPTIMUSMaterialStockDisResponse();
    	}
        return mTS4HANAOPTIMUSMaterialStockDisResponse;
    }

    @JsonProperty("MT_S4HANA_OPTIMUS_Material_Stock_Dis_Response")
    public void setMTS4HANAOPTIMUSMaterialStockDisResponse(MTS4HANAOPTIMUSMaterialStockDisResponse mTS4HANAOPTIMUSMaterialStockDisResponse) {
        this.mTS4HANAOPTIMUSMaterialStockDisResponse = mTS4HANAOPTIMUSMaterialStockDisResponse;
    }

}

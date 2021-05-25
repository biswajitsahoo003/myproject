
package com.tcl.dias.servicefulfillmentutils.beans.sap.hana;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "MT_VMI_STOCK_RES"
})
public class InventoryCheckResponse {

    @JsonProperty("MT_VMI_STOCK_RES")
    private MTVMISTOCKRES mTVMISTOCKRES;

    @JsonProperty("MT_VMI_STOCK_RES")
    public MTVMISTOCKRES getMTVMISTOCKRES() {
        return mTVMISTOCKRES;
    }

    @JsonProperty("MT_VMI_STOCK_RES")
    public void setMTVMISTOCKRES(MTVMISTOCKRES mTVMISTOCKRES) {
        this.mTVMISTOCKRES = mTVMISTOCKRES;
    }

}


package com.tcl.dias.servicefulfillmentutils.beans.sap.hana;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "VMI_STOCK"
})
public class InventoryCheckRequest {

    @JsonProperty("VMI_STOCK")
    private List<VmIStock> vMISTOCK = null;

    @JsonProperty("VMI_STOCK")
    public List<VmIStock> getVMISTOCK() {
        if(vMISTOCK==null){
            vMISTOCK=new ArrayList<>();
        }
        return vMISTOCK;
    }

    @JsonProperty("VMI_STOCK")
    public void setVMISTOCK(List<VmIStock> vMISTOCK) {
        this.vMISTOCK = vMISTOCK;
    }

}

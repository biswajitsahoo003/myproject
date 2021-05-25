
package com.tcl.dias.servicefulfillmentutils.beans.sap.hana;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "VMI_STOCK_UPDATE"
})
public class MTVMISTOCKRES {

    @JsonProperty("VMI_STOCK_UPDATE")
    private List<VMISTOCKUPDATE> vMISTOCKUPDATE = null;

    @JsonProperty("VMI_STOCK_UPDATE")
    public List<VMISTOCKUPDATE> getVMISTOCKUPDATE() {
        return vMISTOCKUPDATE;
    }

    @JsonProperty("VMI_STOCK_UPDATE")
    public void setVMISTOCKUPDATE(List<VMISTOCKUPDATE> vMISTOCKUPDATE) {
        this.vMISTOCKUPDATE = vMISTOCKUPDATE;
    }

}


package com.tcl.dias.servicefulfillmentutils.beans.sap.hana;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "Material_Transfer"
})
public class WBSTransferRequest {

    @JsonProperty("Material_Transfer")
    private List<MaterialTransfer> materialTransfer = null;

    @JsonProperty("Material_Transfer")
    public List<MaterialTransfer> getMaterialTransfer() {
        return materialTransfer;
    }

    @JsonProperty("Material_Transfer")
    public void setMaterialTransfer(List<MaterialTransfer> materialTransfer) {
        this.materialTransfer = materialTransfer;
    }

}

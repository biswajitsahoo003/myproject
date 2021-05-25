
package com.tcl.dias.servicefulfillmentutils.beans.sap.hana;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "MATERIAL_CODE"
})
public class VmIStock {

    @JsonProperty("MATERIAL_CODE")
    private String mATERIALCODE;

    @JsonProperty("MATERIAL_CODE")
    public String getMATERIALCODE() {
        return mATERIALCODE;
    }

    @JsonProperty("MATERIAL_CODE")
    public void setMATERIALCODE(String mATERIALCODE) {
        this.mATERIALCODE = mATERIALCODE;
    }

}

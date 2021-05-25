
package com.tcl.dias.servicefulfillmentutils.beans.sap.hana;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "GOODS_RECEIPT"
})
public class TrackCBERequest {

    @JsonProperty("GOODS_RECEIPT")
    private List<GOODSRECEIPT> gOODSRECEIPT = null;

    @JsonProperty("GOODS_RECEIPT")
    public List<GOODSRECEIPT> getGOODSRECEIPT() {
        return gOODSRECEIPT;
    }

    @JsonProperty("GOODS_RECEIPT")
    public void setGOODSRECEIPT(List<GOODSRECEIPT> gOODSRECEIPT) {
        this.gOODSRECEIPT = gOODSRECEIPT;
    }

}

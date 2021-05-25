
package com.tcl.dias.servicefulfillmentutils.beans.sap.hana;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "GOODS_RECEIPT"
})
public class InstallCpeRequest {

    @JsonProperty("GOODS_RECEIPT")
    private List<GOODSRECEIPT> goodsReceipt = null;

    @JsonProperty("GOODS_RECEIPT")
    public List<GOODSRECEIPT> getGoodsReceipt() {
        if(goodsReceipt==null){
            goodsReceipt=new ArrayList<>();
        }
        return goodsReceipt;
    }

    @JsonProperty("GOODS_RECEIPT")
    public void setGoodsReceipt(List<GOODSRECEIPT> goodsReceipt) {
        this.goodsReceipt = goodsReceipt;
    }

}

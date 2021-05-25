
package com.tcl.dias.servicefulfillmentutils.beans.sap;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CpeInventoryResponseBean {

    @JsonProperty("Inventory_Detail")
    private List<InventoryDetailResponseBean> inventoryDetail;

    public List<InventoryDetailResponseBean> getInventoryDetail() {
        return inventoryDetail;
    }

    public void setInventoryDetail(List<InventoryDetailResponseBean> inventoryDetail) {
        this.inventoryDetail = inventoryDetail;
    }

}

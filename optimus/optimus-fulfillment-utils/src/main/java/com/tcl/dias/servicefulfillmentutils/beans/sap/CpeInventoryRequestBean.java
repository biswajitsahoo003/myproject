package com.tcl.dias.servicefulfillmentutils.beans.sap;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CpeInventoryRequestBean {

    @JsonProperty("Inventory_Detail")
    private InventoryDetailRequestBean inventoryDetail;

    public InventoryDetailRequestBean getInventoryDetail() {
        return inventoryDetail;
    }

    public void setInventoryDetail(InventoryDetailRequestBean inventoryDetail) {
        this.inventoryDetail = inventoryDetail;
    }

    @Override
    public String toString() {
        return "CpeInventoryRequestBean{" +
                "inventoryDetail=" + inventoryDetail +
                '}';
    }
}

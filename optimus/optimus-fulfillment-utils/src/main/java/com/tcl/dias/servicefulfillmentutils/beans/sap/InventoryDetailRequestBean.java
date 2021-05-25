package com.tcl.dias.servicefulfillmentutils.beans.sap;

import com.fasterxml.jackson.annotation.JsonProperty;

public class InventoryDetailRequestBean {

    @JsonProperty("BM_Bundled")
    private String bMBundled;
    @JsonProperty("Material_code")
    private String materialCode;

    public String getBMBundled() {
        return bMBundled;
    }

    public void setBMBundled(String bMBundled) {
        this.bMBundled = bMBundled;
    }

    public String getMaterialCode() {
        return materialCode;
    }

    public void setMaterialCode(String materialCode) {
        this.materialCode = materialCode;
    }

    @Override
    public String toString() {
        return "InventoryDetailRequestBean{" +
                "bMBundled='" + bMBundled + '\'' +
                ", materialCode='" + materialCode + '\'' +
                '}';
    }
}

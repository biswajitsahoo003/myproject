package com.tcl.dias.servicefulfillmentutils.beans;

public class SdwanOrderDetailsBean extends TaskDetailsBaseBean {

    private String tigerOrderId;
    private String legacyServiceId;
    private String orderPlacedStatus;

    public String getTigerOrderId() {
        return tigerOrderId;
    }

    public void setTigerOrderId(String tigerOrderId) {
        this.tigerOrderId = tigerOrderId;
    }

    public String getLegacyServiceId() {
        return legacyServiceId;
    }

    public void setLegacyServiceId(String legacyServiceId) {
        this.legacyServiceId = legacyServiceId;
    }

    public String getOrderPlacedStatus() {
        return orderPlacedStatus;
    }

    public void setOrderPlacedStatus(String orderPlacedStatus) {
        this.orderPlacedStatus = orderPlacedStatus;
    }
}

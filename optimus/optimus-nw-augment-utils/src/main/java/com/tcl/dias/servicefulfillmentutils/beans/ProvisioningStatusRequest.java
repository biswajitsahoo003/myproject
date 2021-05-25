package com.tcl.dias.servicefulfillmentutils.beans;

public class ProvisioningStatusRequest {
    private String orderId;
    private String orderType;
    private String provisioningStatus;

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setProvisioningStatus(String provisioningStatus) {
        this.provisioningStatus = provisioningStatus;
    }

    public String getProvisioningStatus() {
        return provisioningStatus;
    }
}

package com.tcl.dias.servicefulfillmentutils.beans;

public class ProvisioningStatusResponse {

    String orderId;
    String orderType;
    String provisioningStatus;
    String output;

    String errorCode;
    String errorShortDescription;
    String errorLongDescription;
    String module;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getProvisioningStatus() {
        return provisioningStatus;
    }

    public void setProvisioningStatus(String provisioningStatus) {
        this.provisioningStatus = provisioningStatus;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorShortDescription() {
        return errorShortDescription;
    }

    public void setErrorShortDescription(String errorShortDescription) {
        this.errorShortDescription = errorShortDescription;
    }

    public String getErrorLongDescription() {
        return errorLongDescription;
    }

    public void setErrorLongDescription(String errorLongDescription) {
        this.errorLongDescription = errorLongDescription;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }
}

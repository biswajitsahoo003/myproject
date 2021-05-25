package com.tcl.dias.oms.gsc.tiger.beans;

public class GetOrderDetail {

    private String orderId;
    private String orderName;
    private String state;
    private String serviceType;
    private String serviceAbbr;
    private String orderDate;
    private String requestedCompletionDate;
    private String optimusId;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getServiceAbbr() {
        return serviceAbbr;
    }

    public void setServiceAbbr(String serviceAbbr) {
        this.serviceAbbr = serviceAbbr;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getRequestedCompletionDate() {
        return requestedCompletionDate;
    }

    public void setRequestedCompletionDate(String requestedCompletionDate) {
        this.requestedCompletionDate = requestedCompletionDate;
    }

    public String getOptimusId() {
        return optimusId;
    }

    public void setOptimusId(String optimusId) {
        this.optimusId = optimusId;
    }
}

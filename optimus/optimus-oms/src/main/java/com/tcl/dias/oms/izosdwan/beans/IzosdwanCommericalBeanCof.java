package com.tcl.dias.oms.izosdwan.beans;

import java.io.Serializable;

public class IzosdwanCommericalBeanCof implements Serializable {
    private String serviceId;
    private String serviceType;
    private String product;
    private String OrderType;
    public String getOrderType() {
		return OrderType;
	}

	public void setOrderType(String orderType) {
		OrderType = orderType;
	}

	private String actionType;
    private String chareableLineItem;
    private String hsnCode;
    private String bandwidth;
    private String arc;
    private String nrc;


    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public String getChareableLineItem() {
        return chareableLineItem;
    }

    public void setChareableLineItem(String chareableLineItem) {
        this.chareableLineItem = chareableLineItem;
    }

    public String getBandwidth() {
        return bandwidth;
    }

    public void setBandwidth(String bandwidth) {
        this.bandwidth = bandwidth;
    }

    public String getArc() {
        return arc;
    }

    public void setArc(String arc) {
        this.arc = arc;
    }

    public String getNrc() {
        return nrc;
    }

    public void setNrc(String nrc) {
        this.nrc = nrc;
    }

    public String getHsnCode() {
        return hsnCode;
    }

    public void setHsnCode(String hsnCode) {
        this.hsnCode = hsnCode;
    }
}

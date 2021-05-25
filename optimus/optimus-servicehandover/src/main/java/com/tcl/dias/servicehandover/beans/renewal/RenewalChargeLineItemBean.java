package com.tcl.dias.servicehandover.beans.renewal;

import com.tcl.dias.servicefulfillmentutils.beans.LineItemDetailsBean;

import java.util.List;

public class RenewalChargeLineItemBean {

    private  Integer serviceId;
    private List<LineItemDetailsBean> lineItemDetails;
    private String accountNumber;
    private String serviceCode;

    public Integer getServiceId() {
        return serviceId;
    }

    public void setServiceId(Integer serviceId) {
        this.serviceId = serviceId;
    }

    public List<LineItemDetailsBean> getLineItemDetails() {
        return lineItemDetails;
    }

    public void setLineItemDetails(List<LineItemDetailsBean> lineItemDetails) {
        this.lineItemDetails = lineItemDetails;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }
}

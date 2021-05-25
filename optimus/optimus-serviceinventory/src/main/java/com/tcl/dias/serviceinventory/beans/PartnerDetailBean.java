package com.tcl.dias.serviceinventory.beans;

import java.util.List;

public class PartnerDetailBean {

    private Integer partnerLeId;
    private String partnerLeName;
    private List<CustomerOrderDetailsBean> customers;

    public Integer getPartnerLeId() {
        return partnerLeId;
    }

    public void setPartnerLeId(Integer partnerLeId) {
        this.partnerLeId = partnerLeId;
    }

    public String getPartnerLeName() {
        return partnerLeName;
    }

    public void setPartnerLeName(String partnerLeName) {
        this.partnerLeName = partnerLeName;
    }

    public List<CustomerOrderDetailsBean> getCustomers() {
        return customers;
    }

    public void setCustomers(List<CustomerOrderDetailsBean> customers) {
        this.customers = customers;
    }

}

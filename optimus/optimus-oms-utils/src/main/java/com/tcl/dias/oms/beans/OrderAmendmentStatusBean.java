package com.tcl.dias.oms.beans;

import com.tcl.dias.oms.beans.SiteToServiceMapping;

import java.util.List;

public class OrderAmendmentStatusBean {

    String orderCode;
    String parentOrderCode;
    boolean isO2c;
    List<Integer> sites;
    List<SiteToServiceMapping> siteToServiceMappings;


    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getParentOrderCode() {
        return parentOrderCode;
    }

    public void setParentOrderCode(String parentOrderCode) {
        this.parentOrderCode = parentOrderCode;
    }

    public boolean isO2c() {
        return isO2c;
    }

    public void setO2c(boolean o2c) {
        isO2c = o2c;
    }

    public List<Integer> getSites() {
        return sites;
    }

    public void setSites(List<Integer> sites) {
        this.sites = sites;
    }

    public List<SiteToServiceMapping> getSiteToServiceMappings() {
        return siteToServiceMappings;
    }

    public void setSiteToServiceMappings(List<SiteToServiceMapping> siteToServiceMappings) {
        this.siteToServiceMappings = siteToServiceMappings;
    }
}

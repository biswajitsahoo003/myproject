package com.tcl.dias.common.ordertocash.beans;

import java.util.List;

public class ScServiceDetailForOrderAmend {

    private String orderCode;
    private String dualCase;
    List<SiteToService> siteToServices;

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public List<SiteToService> getSiteToServices() {
        return siteToServices;
    }

    public void setSiteToServices(List<SiteToService> siteToServices) {
        this.siteToServices = siteToServices;
    }

    public String getDualCase() {
        return dualCase;
    }

    public void setDualCase(String dualCase) {
        this.dualCase = dualCase;
    }

    @Override
    public String toString() {
        return "ScServiceDetailForOrderAmend{" +
                "orderCode='" + orderCode + '\'' +
                ", dualCase='" + dualCase + '\'' +
                ", siteToServices=" + siteToServices +
                '}';
    }
}

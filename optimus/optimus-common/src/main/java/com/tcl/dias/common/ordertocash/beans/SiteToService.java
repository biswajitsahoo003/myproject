package com.tcl.dias.common.ordertocash.beans;

public class SiteToService {

    private String siteCode;
    private String serviceId;
    private String primarySecondary;
    private String clrTask;

    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getPrimarySecondary() {
        return primarySecondary;
    }

    public void setPrimarySecondary(String primarySecondary) {
        this.primarySecondary = primarySecondary;
    }

    @Override
    public String toString() {
        return "SiteToService{" +
                "siteCode='" + siteCode + '\'' +
                ", serviceId='" + serviceId + '\'' +
                ", primarySecondary='" + primarySecondary + '\'' +
                ", clrTask='" + clrTask + '\'' +
                '}';
    }

    public String getClrTask() {
        return clrTask;
    }

    public void setClrTask(String clrTask) {
        this.clrTask = clrTask;
    }

}
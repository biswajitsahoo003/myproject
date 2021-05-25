package com.tcl.dias.oms.beans;

import java.util.Date;

public class ServiceDetailBeanForAmendment {

    private String serviceId;
    private String siteCode;
    private Date date;
    private String product;
    private String type;
    private String status;
    private String primarySecondary;
    private String allowAmendment;

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
    }

    public String getPrimarySecondary() {
        return primarySecondary;
    }

    public void setPrimarySecondary(String primarySecondary) {
        this.primarySecondary = primarySecondary;
    }

    public String getAllowAmendment() {
        return allowAmendment;
    }

    public void setAllowAmendment(String allowAmendment) {
        this.allowAmendment = allowAmendment;
    }

    public ServiceDetailBeanForAmendment(){

    }

    @Override
    public String toString() {
        return "ServiceDetailBeanForAmendment{" +
                "serviceId='" + serviceId + '\'' +
                ", siteCode='" + siteCode + '\'' +
                ", date=" + date +
                ", product='" + product + '\'' +
                ", type='" + type + '\'' +
                ", status='" + status + '\'' +
                ", primarySecondary='" + primarySecondary + '\'' +
                '}';
    }
}

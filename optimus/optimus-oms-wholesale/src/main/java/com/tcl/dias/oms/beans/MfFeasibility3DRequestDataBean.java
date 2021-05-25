package com.tcl.dias.oms.beans;

import java.util.List;

public class MfFeasibility3DRequestDataBean {

    private Integer quoteId;
    private String quoteCode;
    private Integer customerId;
    private String customerName;
    private String customerCode;
    private String sfdcAccountId;
    private Integer opportunityId;
    private Integer productId;
    private String productName;
    private String lastMileContactTerms;
    private String bandwidth;
    private Boolean isLatLong;
    private List<FeasibilitySiteDetailBean> feasibilitySiteDetailBeanList;


    public Boolean getIsLatLong() {
        return isLatLong;
    }

    public void setIsLatLong(Boolean isLatLong) {
        this.isLatLong = isLatLong;
    }

    public Integer getQuoteId() {
        return quoteId;
    }

    public void setQuoteId(Integer quoteId) {
        this.quoteId = quoteId;
    }

    public String getQuoteCode() {
        return quoteCode;
    }

    public void setQuoteCode(String quoteCode) {
        this.quoteCode = quoteCode;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getSfdcAccountId() {
        return sfdcAccountId;
    }

    public void setSfdcAccountId(String sfdcAccountId) {
        this.sfdcAccountId = sfdcAccountId;
    }

    public Integer getOpportunityId() {
        return opportunityId;
    }

    public void setOpportunityId(Integer opportunityId) {
        this.opportunityId = opportunityId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getLastMileContactTerms() {
        return lastMileContactTerms;
    }

    public void setLastMileContactTerms(String lastMileContactTerms) {
        this.lastMileContactTerms = lastMileContactTerms;
    }

    public String getBandwidth() {
        return bandwidth;
    }

    public void setBandwidth(String bandwidth) {
        this.bandwidth = bandwidth;
    }

    public List<FeasibilitySiteDetailBean> getFeasibilitySiteDetailBeanList() {
        return feasibilitySiteDetailBeanList;
    }

    public void setFeasibilitySiteDetailBeanList(List<FeasibilitySiteDetailBean> feasibilitySiteDetailBeanList) {
        this.feasibilitySiteDetailBeanList = feasibilitySiteDetailBeanList;
    }

    @Override
    public String toString() {
        return "MfFeasibility3DRequestDataBean{" +
                "quoteId=" + quoteId +
                ", quoteCode='" + quoteCode + '\'' +
                ", customerId=" + customerId +
                ", customerName='" + customerName + '\'' +
                ", customerCode='" + customerCode + '\'' +
                ", sfdcAccountId='" + sfdcAccountId + '\'' +
                ", opportunityId='" + opportunityId + '\'' +
                ", productId='" + productId + '\'' +
                ", productName='" + productName + '\'' +
                ", lastMileContactTerms='" + lastMileContactTerms + '\'' +
                ", bandwidth=" + bandwidth +
                ", feasibilitySiteDetailBeanList=" + feasibilitySiteDetailBeanList +
                '}';
    }
}

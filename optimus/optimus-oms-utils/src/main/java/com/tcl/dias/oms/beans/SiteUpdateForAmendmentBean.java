package com.tcl.dias.oms.beans;

import java.util.List;

public class SiteUpdateForAmendmentBean {
    private int customerId;
    private int parentAmendmentOrderCode;
    private int quoteId;
    private int quoteleId;
    private String productName;
    List<SiteDetailsForAmendmentBean> sites;


    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getParentAmendmentOrderCode() {
        return parentAmendmentOrderCode;
    }

    public void setParentAmendmentOrderCode(int parentAmendmentOrderCode) {
        this.parentAmendmentOrderCode = parentAmendmentOrderCode;
    }

    public int getQuoteId() {
        return quoteId;
    }

    public void setQuoteId(int quoteId) {
        this.quoteId = quoteId;
    }

    public int getQuoteleId() {
        return quoteleId;
    }

    public void setQuoteleId(int quoteleId) {
        this.quoteleId = quoteleId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public List<SiteDetailsForAmendmentBean> getSites() {
        return sites;
    }

    public void setSites(List<SiteDetailsForAmendmentBean> sites) {
        this.sites = sites;
    }

    @Override
    public String toString() {
        return "SiteUpdateForAmendmentBean{" +
                "customerId=" + customerId +
                ", parentAmendmentOrderId=" + parentAmendmentOrderCode +
                ", quoteId=" + quoteId +
                ", quoteleId=" + quoteleId +
                ", productName='" + productName + '\'' +
                ", sitesDetailForAmendmentBean=" + sites +
                '}';
    }
}

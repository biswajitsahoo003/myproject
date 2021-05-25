package com.tcl.dias.oms.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class QuoteBeanForOrderAmendment implements Serializable {
    List<ServiceDetailBeanForAmendment> serviceDetailBeanForAmendments = new ArrayList<>();
    List<String> siteCodes;
    boolean isQuoteCreated ;
    boolean isO2c;
    private int quoteId;
    private int quoteToLeId;
    private String parentOrderCode;

    public List<String> getSiteCodes() {
        return siteCodes;
    }

    public void setSiteCodes(List<String> siteCodes) {
        this.siteCodes = siteCodes;
    }

    public boolean isQuoteCreated() {
        return isQuoteCreated;
    }

    public void setIsQuoteCreated(boolean isQuoteCreated) {
        this.isQuoteCreated = isQuoteCreated;
    }

    public boolean isO2c() {
        return isO2c;
    }

    public void setIsO2c(boolean isO2c) {
        this.isO2c = isO2c;
    }

    public List<ServiceDetailBeanForAmendment> getServiceDetailBeanForAmendments() {
        return serviceDetailBeanForAmendments;
    }

    public void setServiceDetailBeanForAmendments(List<ServiceDetailBeanForAmendment> serviceDetailBeanForAmendments) {
        this.serviceDetailBeanForAmendments = serviceDetailBeanForAmendments;
    }




    public int getQuoteId() {
        return quoteId;
    }

    public void setQuoteId(int quoteId) {
        this.quoteId = quoteId;
    }

    public int getQuoteToLeId() {
        return quoteToLeId;
    }

    public void setQuoteToLeId(int quoteToLeId) {
        this.quoteToLeId = quoteToLeId;
    }

    public String getParentOrderCode() {
        return parentOrderCode;
    }

    public void setParentOrderCode(String parentOrderCode) {
        this.parentOrderCode = parentOrderCode;
    }

    @Override
    public String toString() {
        return "QuoteBeanForOrderAmendment{" +
                "serviceDetailBeanForAmendments=" + serviceDetailBeanForAmendments +
                ", siteCodes=" + siteCodes +
                ", isQuoteCreated=" + isQuoteCreated +
                ", isO2c=" + isO2c +
                ", quoteId=" + quoteId +
                ", quoteToLeId=" + quoteToLeId +
                ", parentOrderCode='" + parentOrderCode + '\'' +
                '}';
    }
}

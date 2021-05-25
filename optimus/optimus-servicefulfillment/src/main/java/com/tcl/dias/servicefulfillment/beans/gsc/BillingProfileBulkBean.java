package com.tcl.dias.servicefulfillment.beans.gsc;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BillingProfileBulkBean {
    private String startIndex;

    private List<BillingProfileDetailBean> billingProfiles;

    private String endIndex;

    private String message;

    private String totalCount;

    private String status;

    public String getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(String startIndex) {
        this.startIndex = startIndex;
    }

    public List<BillingProfileDetailBean> getBillingProfiles() {
        return billingProfiles;
    }

    public void setBillingProfiles(List<BillingProfileDetailBean> billingProfiles) {
        this.billingProfiles = billingProfiles;
    }

    public String getEndIndex() {
        return endIndex;
    }

    public void setEndIndex(String endIndex) {
        this.endIndex = endIndex;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(String totalCount) {
        this.totalCount = totalCount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

package com.tcl.dias.servicefulfillmentutils.beans;

import java.util.List;

import com.tcl.dias.servicefulfillmentutils.base.request.BaseRequest;

public class ValidateSupportingDocBean extends BaseRequest {
    private String deemedAcceptanceApplicable;
    private String billFreePeriod;
    private List<AttachmentBean> documents;
    private String poDocType;
    private String taxExemption;
    private String taxExemptionReason;
    private String customerPoNumber;
    private String customerPoDate;

    public String getDeemedAcceptanceApplicable() {
        return deemedAcceptanceApplicable;
    }

    public void setDeemedAcceptanceApplicable(String deemedAcceptanceApplicable) {
        this.deemedAcceptanceApplicable = deemedAcceptanceApplicable;
    }

    public String getBillFreePeriod() {
        return billFreePeriod;
    }

    public void setBillFreePeriod(String billFreePeriod) {
        this.billFreePeriod = billFreePeriod;
    }

    public List<AttachmentBean> getDocuments() {
        return documents;
    }

    public void setDocuments(List<AttachmentBean> documents) {
        this.documents = documents;
    }

    public String getPoDocType() { return poDocType; }

    public void setPoDocType(String poDocType) { this.poDocType = poDocType; }

    public String getTaxExemption() { return taxExemption; }

    public void setTaxExemption(String taxExemption) { this.taxExemption = taxExemption; }

    public String getTaxExemptionReason() { return taxExemptionReason; }

    public void setTaxExemptionReason(String taxExemptionReason) { this.taxExemptionReason = taxExemptionReason;  }

    public String getCustomerPoNumber() {
        return customerPoNumber;
    }

    public void setCustomerPoNumber(String customerPoNumber) {
        this.customerPoNumber = customerPoNumber;
    }

    public String getCustomerPoDate() {
        return customerPoDate;
    }

    public void setCustomerPoDate(String customerPoDate) {
        this.customerPoDate = customerPoDate;
    }
}

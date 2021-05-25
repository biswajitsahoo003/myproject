package com.tcl.dias.servicefulfillment.beans.gsc;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BillingProfileDetailBean {
    private String profileRelNo;

    private String parentOrgId;

    private String billingTerm;

    private String paymentMethod;

    private String currency;

    private String invoiceEmail;

    private String isTrial;

    private String paymentTerm;

    private String orgId;

    private String baas;

    public String getProfileRelNo() {
        return profileRelNo;
    }

    public void setProfileRelNo(String profileRelNo) {
        this.profileRelNo = profileRelNo;
    }

    public String getParentOrgId() {
        return parentOrgId;
    }

    public void setParentOrgId(String parentOrgId) {
        this.parentOrgId = parentOrgId;
    }

    public String getBillingTerm() {
        return billingTerm;
    }

    public void setBillingTerm(String billingTerm) {
        this.billingTerm = billingTerm;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getInvoiceEmail() {
        return invoiceEmail;
    }

    public void setInvoiceEmail(String invoiceEmail) {
        this.invoiceEmail = invoiceEmail;
    }

    public String getIsTrial() {
        return isTrial;
    }

    public void setIsTrial(String isTrial) {
        this.isTrial = isTrial;
    }

    public String getPaymentTerm() {
        return paymentTerm;
    }

    public void setPaymentTerm(String paymentTerm) {
        this.paymentTerm = paymentTerm;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getBaas() {
        return baas;
    }

    public void setBaas(String baas) {
        this.baas = baas;
    }
}

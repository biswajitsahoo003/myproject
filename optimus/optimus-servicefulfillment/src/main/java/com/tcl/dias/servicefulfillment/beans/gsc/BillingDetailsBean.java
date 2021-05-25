package com.tcl.dias.servicefulfillment.beans.gsc;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BillingDetailsBean {
    private String profileEndDate;

    private List<String> cmsIdDetails;

    private String billingTerm;

    private String billGrpCd;

    private String invoiceEmail;

    private String isTrial;

    private List<ProductDetailsBean> productDetails;

    private String billingEntity;

    private String orgId;

    private String billingFormattingRequired;

    private List<String> incomTypeCdDetails;

    private String profileType;

    private String baasEffectDate;

    private String currency;

    private String department;

    private String baas;

    private List<String> documentNmDetails;

    private List<String> docFmtIdDetails;

    private String billingMethod;

    private String printFlag;

    private String profileStartDate;

    private String termsAcceptDate;

    private String emailFlag;

    private String parentOrgId;

    private TrialDetailsBean trialDetails;

    private String billingEndDate;

    private String paymentMethod;

    private String rapidCDRID;

    private String rapidCDRFlag;

    private String paymentTerm;
    
    private List<CustTaxDetailsBean> custTaxDetails;

    public String getProfileEndDate() {
        return profileEndDate;
    }

    public void setProfileEndDate(String profileEndDate) {
        this.profileEndDate = profileEndDate;
    }

    public List<String> getCmsIdDetails() {
        return cmsIdDetails;
    }

    public void setCmsIdDetails(List<String> cmsIdDetails) {
        this.cmsIdDetails = cmsIdDetails;
    }

    public String getBillingTerm() {
        return billingTerm;
    }

    public void setBillingTerm(String billingTerm) {
        this.billingTerm = billingTerm;
    }

    public String getBillGrpCd() {
        return billGrpCd;
    }

    public void setBillGrpCd(String billGrpCd) {
        this.billGrpCd = billGrpCd;
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

    public List<ProductDetailsBean> getProductDetails() {
        return productDetails;
    }

    public void setProductDetails(List<ProductDetailsBean> productDetails) {
        this.productDetails = productDetails;
    }

    public String getBillingEntity() {
        return billingEntity;
    }

    public void setBillingEntity(String billingEntity) {
        this.billingEntity = billingEntity;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getBillingFormattingRequired() {
        return billingFormattingRequired;
    }

    public void setBillingFormattingRequired(String billingFormattingRequired) {
        this.billingFormattingRequired = billingFormattingRequired;
    }

    public List<String> getIncomTypeCdDetails() {
        return incomTypeCdDetails;
    }

    public void setIncomTypeCdDetails(List<String> incomTypeCdDetails) {
        this.incomTypeCdDetails = incomTypeCdDetails;
    }

    public String getProfileType() {
        return profileType;
    }

    public void setProfileType(String profileType) {
        this.profileType = profileType;
    }

    public String getBaasEffectDate() {
        return baasEffectDate;
    }

    public void setBaasEffectDate(String baasEffectDate) {
        this.baasEffectDate = baasEffectDate;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getBaas() {
        return baas;
    }

    public void setBaas(String baas) {
        this.baas = baas;
    }

    public List<String> getDocumentNmDetails() {
        return documentNmDetails;
    }

    public void setDocumentNmDetails(List<String> documentNmDetails) {
        this.documentNmDetails = documentNmDetails;
    }

    public List<String> getDocFmtIdDetails() {
        return docFmtIdDetails;
    }

    public void setDocFmtIdDetails(List<String> docFmtIdDetails) {
        this.docFmtIdDetails = docFmtIdDetails;
    }

    public String getBillingMethod() {
        return billingMethod;
    }

    public void setBillingMethod(String billingMethod) {
        this.billingMethod = billingMethod;
    }

    public String getPrintFlag() {
        return printFlag;
    }

    public void setPrintFlag(String printFlag) {
        this.printFlag = printFlag;
    }

    public String getProfileStartDate() {
        return profileStartDate;
    }

    public void setProfileStartDate(String profileStartDate) {
        this.profileStartDate = profileStartDate;
    }

    public String getTermsAcceptDate() {
        return termsAcceptDate;
    }

    public void setTermsAcceptDate(String termsAcceptDate) {
        this.termsAcceptDate = termsAcceptDate;
    }

    public String getEmailFlag() {
        return emailFlag;
    }

    public void setEmailFlag(String emailFlag) {
        this.emailFlag = emailFlag;
    }

    public String getParentOrgId() {
        return parentOrgId;
    }

    public void setParentOrgId(String parentOrgId) {
        this.parentOrgId = parentOrgId;
    }

    public TrialDetailsBean getTrialDetails() {
        return trialDetails;
    }

    public void setTrialDetails(TrialDetailsBean trialDetails) {
        this.trialDetails = trialDetails;
    }

    public String getBillingEndDate() {
        return billingEndDate;
    }

    public void setBillingEndDate(String billingEndDate) {
        this.billingEndDate = billingEndDate;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getRapidCDRID() {
        return rapidCDRID;
    }

    public void setRapidCDRID(String rapidCDRID) {
        this.rapidCDRID = rapidCDRID;
    }

    public String getRapidCDRFlag() {
        return rapidCDRFlag;
    }

    public void setRapidCDRFlag(String rapidCDRFlag) {
        this.rapidCDRFlag = rapidCDRFlag;
    }

    public String getPaymentTerm() {
        return paymentTerm;
    }

    public void setPaymentTerm(String paymentTerm) {
        this.paymentTerm = paymentTerm;
    }

	public List<CustTaxDetailsBean> getCustTaxDetails() {
		return custTaxDetails;
	}

	public void setCustTaxDetails(List<CustTaxDetailsBean> custTaxDetails) {
		this.custTaxDetails = custTaxDetails;
	}    
    
}

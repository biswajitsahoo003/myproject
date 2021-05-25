package com.tcl.dias.sfdc.bean;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
        "OpptyCreation",
        "documentName",
        "RetryStage",
        "operation",
        "sfOpportunityId_quote",
        "pdfBytes",
        "productType",
        "source",
        "PortalTransactionID",
        "isRetry",
        "serviceType",
        "bm_id"
})


public class SfdcOptyCreationRequestBean extends  BaseBean {


    @JsonProperty("OpptyCreation")
    private SfdcPartnerOpportunityRequest sfdcPartnerOpportunityRequest;

    @JsonProperty("documentName")
    private String documentName;

    @JsonProperty("RetryStage")
    private String retryStage;
    @JsonProperty("operation")
    private String operation;
    @JsonProperty("sfOpportunityId_quote")
    private String optyQuoteCode;
    @JsonProperty("pdfBytes")
    private String pdfBytes;

    @JsonProperty("productType")
    private String productType;
    @JsonProperty("source")
    private String source;
    @JsonProperty("PortalTransactionID")
    private String portalTransactionID;
    @JsonProperty("isRetry")
    private String isRetry;
    @JsonProperty("serviceType")
    private String serviceType;
    @JsonProperty("bm_id")
    private String bmId;

    @JsonProperty("OpptyCreation")
    public SfdcPartnerOpportunityRequest getSfdcPartnerOpportunityRequest() {
        return sfdcPartnerOpportunityRequest;
    }

    @JsonProperty("OpptyCreation")
    public void setSfdcPartnerOpportunityRequest(SfdcPartnerOpportunityRequest sfdcPartnerOpportunityRequest) {
        this.sfdcPartnerOpportunityRequest = sfdcPartnerOpportunityRequest;
    }

    @JsonProperty("documentName")
    public String getDocumentName() {
        return documentName;
    }

    @JsonProperty("documentName")
    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    @JsonProperty("RetryStage")
    public String getRetryStage() {
        return retryStage;
    }

    @JsonProperty("RetryStage")
    public void setRetryStage(String retryStage) {
        this.retryStage = retryStage;
    }

    @JsonProperty("operation")
    public String getOperation() {
        return operation;
    }

    @JsonProperty("operation")
    public void setOperation(String operation) {
        this.operation = operation;
    }

    @JsonProperty("sfOpportunityId_quote")
    public String getOptyQuoteCode() {
        return optyQuoteCode;
    }

    @JsonProperty("sfOpportunityId_quote")
    public void setOptyQuoteCode(String optyQuoteCode) {
        this.optyQuoteCode = optyQuoteCode;
    }

    @JsonProperty("pdfBytes")
    public String getPdfBytes() {
        return pdfBytes;
    }

    @JsonProperty("pdfBytes")
    public void setPdfBytes(String pdfBytes) {
        this.pdfBytes = pdfBytes;
    }

    @JsonProperty("productType")
    public String getProductType() {
        return productType;
    }

    @JsonProperty("productType")
    public void setProductType(String productType) {
        this.productType = productType;
    }

    @JsonProperty("source")
    public String getSource() {
        return source;
    }

    @JsonProperty("source")
    public void setSource(String source) {
        this.source = source;
    }

    @JsonProperty("PortalTransactionID")
    public String getPortalTransactionID() {
        return portalTransactionID;
    }

    @JsonProperty("PortalTransactionID")
    public void setPortalTransactionID(String portalTransactionID) {
        this.portalTransactionID = portalTransactionID;
    }

    @JsonProperty("isRetry")
    public String getIsRetry() {
        return isRetry;
    }

    @JsonProperty("isRetry")
    public void setIsRetry(String isRetry) {
        this.isRetry = isRetry;
    }

    @JsonProperty("serviceType")
    public String getServiceType() {
        return serviceType;
    }

    @JsonProperty("serviceType")
    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    @JsonProperty("bm_id")
    public String getBmId() {
        return bmId;
    }

    @JsonProperty("bm_id")
    public void setBmId(String bmId) {
        this.bmId = bmId;
    }


}

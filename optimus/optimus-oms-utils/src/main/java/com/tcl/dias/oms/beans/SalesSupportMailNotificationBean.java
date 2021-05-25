package com.tcl.dias.oms.beans;

/**
 * Sales support mail notification params
 *
 * @author Gnana prakash
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
public class SalesSupportMailNotificationBean {

    private String opportunityId;

    private String quoteRefId;

    private String partnerLe;

    private String endCustomerName;

    private String SecsId;

    private String status;

    private String accountName;

    private String completePercentage;

    private String optyClassification;

    public String getOpportunityId() {
        return opportunityId;
    }

    public void setOpportunityId(String opportunityId) {
        this.opportunityId = opportunityId;
    }

    public String getQuoteRefId() {
        return quoteRefId;
    }

    public void setQuoteRefId(String quoteRefId) {
        this.quoteRefId = quoteRefId;
    }

    public String getPartnerLe() {
        return partnerLe;
    }

    public void setPartnerLe(String partnerLe) {
        this.partnerLe = partnerLe;
    }

    public String getEndCustomerName() {
        return endCustomerName;
    }

    public void setEndCustomerName(String endCustomerName) {
        this.endCustomerName = endCustomerName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSecsId() {
        return SecsId;
    }

    public void setSecsId(String secsId) {
        SecsId = secsId;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getCompletePercentage() {
        return completePercentage;
    }

    public void setCompletePercentage(String completePercentage) {
        this.completePercentage = completePercentage;
    }

    public String getOptyClassification() {
        return optyClassification;
    }

    public void setOptyClassification(String optyClassification) {
        this.optyClassification = optyClassification;
    }

    @Override
    public String toString() {
        return "SalesSupportMailNotificationBean{" +
                "opportunityId='" + opportunityId + '\'' +
                ", quoteRefId='" + quoteRefId + '\'' +
                ", partnerLe='" + partnerLe + '\'' +
                ", endCustomerName='" + endCustomerName + '\'' +
                ", SecsId='" + SecsId + '\'' +
                ", status='" + status + '\'' +
                ", accountName='" + accountName + '\'' +
                ", completePercentage='" + completePercentage + '\'' +
                ", optyClassification='" + optyClassification + '\'' +
                '}';
    }
}


package com.tcl.dias.oms.beans;

public class CheckAmendmentQuoteBean {

    private String serviceId;
    private String siteCode;
    private int quoteId;
    private String stage;
    private String quoteType;
    private int quoteToLeId;
    private String allowAmendment;

    public int getQuoteToLeId() {
        return quoteToLeId;
    }

    public void setQuoteToLeId(int quoteToLeId) {
        this.quoteToLeId = quoteToLeId;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
    }

    public int getQuoteId() {
        return quoteId;
    }

    public void setQuoteId(int quoteId) {
        this.quoteId = quoteId;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public String getQuoteType() {
        return quoteType;
    }

    public void setQuoteType(String quoteType) {
        this.quoteType = quoteType;
    }

    @Override
    public String toString() {
        return "CheckAmendmentQuoteBean{" +
                "serviceId='" + serviceId + '\'' +
                ", siteCode='" + siteCode + '\'' +
                ", quoteId=" + quoteId +
                ", stage='" + stage + '\'' +
                ", quoteType='" + quoteType + '\'' +
                ", quoteToLeId=" + quoteToLeId +
                ", allowAmendment='" + allowAmendment + '\'' +
                '}';
    }

    public String getAllowAmendment() {
        return allowAmendment;
    }

    public void setAllowAmendment(String allowAmendment) {
        this.allowAmendment = allowAmendment;
    }

}

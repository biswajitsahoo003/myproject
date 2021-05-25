package com.tcl.dias.oms.gsc.beans;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Quote Data Bean which is contains quote information and product solution
 * details
 *
 * @author AVALLAPI
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class GscQuoteDataBean {

    private Integer quoteId;
    private Integer quoteLeId;
    private String quoteCode;
    private Integer customerId;
    private Integer customerLeId;
    private String productFamilyName;
    private String accessType;
    private String profileName;
    private Integer quoteVersion;
    private List<GscSolutionBean> solutions;
    private List<GscQuoteToLeBean> legalEntities;
//    private GvpnQuotePdfBean gvpnQuotes;
    private String quoteType;
    private String quoteCategory;
    private Boolean isDocusign;
    private Boolean isManualCofSigned = false;
    // Only for MACD case
    private Integer supplierLegalId;
    private String engagementOptyId;
    private String classification;
    private String isGscMultiMacd;
    private Double partnerOptyExpectedArc;
    private Double partnerOptyExpectedNrc;
    private String partnerOptyExpectedCurrency;
    private String secsId;
    private Boolean isWholesale = false;
    private String interconnectId;

    public static GscOrderDataBean fromGscQuoteDataBean(GscQuoteDataBean gscQuoteDataBean) {
        GscOrderDataBean gscOrderDataBean = new GscOrderDataBean();
        gscOrderDataBean.setCustomerId(gscQuoteDataBean.getCustomerId());
        gscOrderDataBean.setProductFamilyName(gscQuoteDataBean.getProductFamilyName());
        gscOrderDataBean.setSolutions(gscQuoteDataBean.getSolutions().stream().map(GscSolutionBean::fromGscSolutionBean)
                .collect(Collectors.toList()));
        return gscOrderDataBean;
    }

    public Double getPartnerOptyExpectedArc() {
        return partnerOptyExpectedArc;
    }

    public void setPartnerOptyExpectedArc(Double partnerOptyExpectedArc) {
        this.partnerOptyExpectedArc = partnerOptyExpectedArc;
    }

    public Double getPartnerOptyExpectedNrc() {
        return partnerOptyExpectedNrc;
    }

    public void setPartnerOptyExpectedNrc(Double partnerOptyExpectedNrc) {
        this.partnerOptyExpectedNrc = partnerOptyExpectedNrc;
    }

    public String getPartnerOptyExpectedCurrency() {
        return partnerOptyExpectedCurrency;
    }

    public void setPartnerOptyExpectedCurrency(String partnerOptyExpectedCurrency) {
        this.partnerOptyExpectedCurrency = partnerOptyExpectedCurrency;
    }

    public Integer getQuoteId() {
        return quoteId;
    }

    public void setQuoteId(Integer quoteId) {
        this.quoteId = quoteId;
    }

    public Integer getQuoteLeId() {
        return quoteLeId;
    }

    public void setQuoteLeId(Integer quoteLeId) {
        this.quoteLeId = quoteLeId;
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

    public String getProductFamilyName() {
        return productFamilyName;
    }

    public void setProductFamilyName(String productFamilyName) {
        this.productFamilyName = productFamilyName;
    }

    public String getAccessType() {
        return accessType;
    }

    public void setAccessType(String accessType) {
        this.accessType = accessType;
    }

    public Integer getQuoteVersion() {
        return quoteVersion;
    }

    public void setQuoteVersion(Integer quoteVersion) {
        this.quoteVersion = quoteVersion;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public List<GscSolutionBean> getSolutions() {
        return solutions;
    }

    public void setSolutions(List<GscSolutionBean> solutions) {
        this.solutions = solutions;
    }

    public List<GscQuoteToLeBean> getLegalEntities() {
        return legalEntities;
    }

    public void setLegalEntities(List<GscQuoteToLeBean> legalEntities) {
        this.legalEntities = legalEntities;
    }

//    public GvpnQuotePdfBean getGvpnQuotes() {
//        return gvpnQuotes;
//    }
//
//    public void setGvpnQuotes(GvpnQuotePdfBean gvpnQuotes) {
//        this.gvpnQuotes = gvpnQuotes;
//    }

    public String getQuoteType() {
        return quoteType;
    }

    public void setQuoteType(String quoteType) {
        this.quoteType = quoteType;
    }

    public String getQuoteCategory() {
        return quoteCategory;
    }

    public void setQuoteCategory(String quoteCategory) {
        this.quoteCategory = quoteCategory;
    }

    public Boolean getDocusign() {
        return isDocusign;
    }

    public void setDocusign(Boolean docusign) {
        isDocusign = docusign;
    }

    public Boolean getManualCofSigned() {
        return isManualCofSigned;
    }

    public void setManualCofSigned(Boolean manualCofSigned) {
        isManualCofSigned = manualCofSigned;
    }

    public Integer getSupplierLegalId() {
        return supplierLegalId;
    }

    public void setSupplierLegalId(Integer supplierLegalId) {
        this.supplierLegalId = supplierLegalId;
    }

    public String getEngagementOptyId() {
        return engagementOptyId;
    }

    public void setEngagementOptyId(String engagementOptyId) {
        this.engagementOptyId = engagementOptyId;
    }

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public Integer getCustomerLeId() {
        return customerLeId;
    }

    public void setCustomerLeId(Integer customerLeId) {
        this.customerLeId = customerLeId;
    }

    public String getIsGscMultiMacd() {
        return isGscMultiMacd;
    }

    public void setIsGscMultiMacd(String isGscMultiMacd) {
        this.isGscMultiMacd = isGscMultiMacd;
    }

    public String getSecsId() {
        return secsId;
    }

    public void setSecsId(String secsId) {
        this.secsId = secsId;
    }

    public Boolean getWholesale() {
        return isWholesale;
    }

    public void setWholesale(Boolean wholesale) {
        isWholesale = wholesale;
    }

    public String getInterconnectId() {
        return interconnectId;
    }

    public void setInterconnectId(String interconnectId) {
        this.interconnectId = interconnectId;
    }

    @Override
    public String toString() {
        return "GscQuoteDataBean{" +
                "quoteId=" + quoteId +
                ", quoteLeId=" + quoteLeId +
                ", quoteCode='" + quoteCode + '\'' +
                ", customerId=" + customerId +
                ", customerLeId=" + customerLeId +
                ", productFamilyName='" + productFamilyName + '\'' +
                ", accessType='" + accessType + '\'' +
                ", profileName='" + profileName + '\'' +
                ", quoteVersion=" + quoteVersion +
                ", solutions=" + solutions +
                ", legalEntities=" + legalEntities +
                ", quoteType='" + quoteType + '\'' +
                ", quoteCategory='" + quoteCategory + '\'' +
                ", isDocusign=" + isDocusign +
                ", isManualCofSigned=" + isManualCofSigned +
                ", supplierLegalId=" + supplierLegalId +
                ", engagementOptyId='" + engagementOptyId + '\'' +
                ", classification='" + classification + '\'' +
                ", isGscMultiMacd='" + isGscMultiMacd + '\'' +
                ", partnerOptyExpectedArc=" + partnerOptyExpectedArc +
                ", partnerOptyExpectedNrc=" + partnerOptyExpectedNrc +
                ", partnerOptyExpectedCurrency='" + partnerOptyExpectedCurrency + '\'' +
                ", secsId='" + secsId + '\'' +
                ", isWholesale=" + isWholesale +
                ", interconnectId='" + interconnectId + '\'' +
                '}';
    }
}

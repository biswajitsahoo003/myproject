package com.tcl.dias.oms.gsc.beans;

import com.tcl.dias.oms.gsc.pdf.beans.GscOrderAttributesBean;

import java.util.Date;
import java.util.List;

/**
 * Order Data Bean which is contains order information and product solution
 * details
 *
 * @author AVALLAPI
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class GscOrderDataBean {

    private Integer orderId;
    private Integer orderLeId;
    private Integer quoteId;
    private Integer customerId;
    private Integer createdBy;
    private Date createdTime;
    private String orderCode;
    private String productFamilyName;
    private Integer orderVersion;
    private String accessType;
    private String downstreamRequestStatus;
    private String engagementOptyId;
    private List<GscOrderSolutionBean> solutions;
    private List<GscOrderLeBean> legalEntities;
    private GscOrderAttributesBean attributes;
    private String classification;
    private Double partnerOptyExpectedArc;
    private Double partnerOptyExpectedNrc;
    private String partnerOptyExpectedCurrency;
    private String isGscMultiMacd;
    private Boolean isWholesale = false;

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

    public Double getPartnerOptyExpectedArc() {
        return partnerOptyExpectedArc;
    }

    public void setPartnerOptyExpectedArc(Double partnerOptyExpectedArc) {
        this.partnerOptyExpectedArc = partnerOptyExpectedArc;
    }

    /**
     * @return the orderId
     */
    public Integer getOrderId() {
        return orderId;
    }

    /**
     * @param orderId the orderId to set
     */
    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    /**
     * @return the orderLeId
     */
    public Integer getOrderLeId() {
        return orderLeId;
    }

    /**
     * @param orderLeId the orderLeId to set
     */
    public void setOrderLeId(Integer orderLeId) {
        this.orderLeId = orderLeId;
    }

    /**
     * @return the customerId
     */
    public Integer getCustomerId() {
        return customerId;
    }

    /**
     * @param customerId the customerId to set
     */
    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    /**
     * @return the productFamilyName
     */
    public String getProductFamilyName() {
        return productFamilyName;
    }

    /**
     * @param productFamilyName the productFamilyName to set
     */
    public void setProductFamilyName(String productFamilyName) {
        this.productFamilyName = productFamilyName;
    }

    /**
     * @return the solutions
     */
    public List<GscOrderSolutionBean> getSolutions() {
        return solutions;
    }

    /**
     * @param solutions the solutions to set
     */
    public void setSolutions(List<GscOrderSolutionBean> solutions) {
        this.solutions = solutions;
    }

    public List<GscOrderLeBean> getLegalEntities() {
        return legalEntities;
    }

    public void setLegalEntities(List<GscOrderLeBean> legalEntities) {
        this.legalEntities = legalEntities;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public Integer getOrderVersion() {
        return orderVersion;
    }

    public void setOrderVersion(Integer orderVersion) {
        this.orderVersion = orderVersion;
    }

    public GscOrderAttributesBean getAttributes() {
        return attributes;
    }

    public void setAttributes(GscOrderAttributesBean attributes) {
        this.attributes = attributes;
    }

    public String getAccessType() {
        return accessType;
    }

    public void setAccessType(String accessType) {
        this.accessType = accessType;
    }

    public Integer getQuoteId() {
        return quoteId;
    }

    public void setQuoteId(Integer quoteId) {
        this.quoteId = quoteId;
    }

    public String getDownstreamRequestStatus() {
        return downstreamRequestStatus;
    }

    public void setDownstreamRequestStatus(String downstreamRequestStatus) {
        this.downstreamRequestStatus = downstreamRequestStatus;
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

    public String getIsGscMultiMacd() {
        return isGscMultiMacd;
    }

    public void setIsGscMultiMacd(String isGscMultiMacd) {
        this.isGscMultiMacd = isGscMultiMacd;
    }

    public Boolean getWholesale() {
        return isWholesale;
    }

    public void setWholesale(Boolean wholesale) {
        isWholesale = wholesale;
    }

    @Override
    public String toString() {
        return "GscOrderDataBean{" +
                "orderId=" + orderId +
                ", orderLeId=" + orderLeId +
                ", quoteId=" + quoteId +
                ", customerId=" + customerId +
                ", createdBy=" + createdBy +
                ", createdTime=" + createdTime +
                ", orderCode='" + orderCode + '\'' +
                ", productFamilyName='" + productFamilyName + '\'' +
                ", orderVersion=" + orderVersion +
                ", accessType='" + accessType + '\'' +
                ", downstreamRequestStatus='" + downstreamRequestStatus + '\'' +
                ", engagementOptyId='" + engagementOptyId + '\'' +
                ", solutions=" + solutions +
                ", legalEntities=" + legalEntities +
                ", attributes=" + attributes +
                ", classification='" + classification + '\'' +
                ", partnerOptyExpectedArc=" + partnerOptyExpectedArc +
                ", partnerOptyExpectedNrc=" + partnerOptyExpectedNrc +
                ", partnerOptyExpectedCurrency='" + partnerOptyExpectedCurrency + '\'' +
                ", isGscMultiMacd='" + isGscMultiMacd + '\'' +
                ", isWholesale=" + isWholesale +
                '}';
    }
}

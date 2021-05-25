package com.tcl.dias.oms.gsc.pdf.beans;

import com.tcl.dias.oms.gsc.beans.GscQuoteToLeBean;
import com.tcl.dias.oms.gsc.beans.GscSolutionBean;

import java.util.List;

/**
 * Bean related to generate quote pdf
 *
 * @author PRABUBALASUBRAMANIAN
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class GscQuotePdfBean {

    private Integer quoteId;
    private String orderDate;
    private Integer quoteLeId;
    private Integer customerId;
    private String productFamilyName;
    private String accessType;
    private String profileName;
    private List<GscSolutionBean> solutions;
    private List<GscQuoteToLeBean> legalEntities;
    private Boolean isDocusign = false;
    private String customerContactName;
    private String customerContactNumber;
    private String customerEmailId;

    public Integer getQuoteId() {
        return quoteId;
    }

    public void setQuoteId(Integer quoteId) {
        this.quoteId = quoteId;
    }

    /**
     * @return the orderDate
     */
    public String getOrderDate() {
        return orderDate;
    }

    /**
     * @param orderDate the orderDate to set
     */
    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public Integer getQuoteLeId() {
        return quoteLeId;
    }

    public void setQuoteLeId(Integer quoteLeId) {
        this.quoteLeId = quoteLeId;
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

    public Boolean getIsDocusign() {
        return isDocusign;
    }

    public void setIsDocusign(Boolean isDocusign) {
        this.isDocusign = isDocusign;
    }

    public String getCustomerContactName() {
        return customerContactName;
    }

    public void setCustomerContactName(String customerContactName) {
        this.customerContactName = customerContactName;
    }

    public String getCustomerContactNumber() {
        return customerContactNumber;
    }

    public void setCustomerContactNumber(String customerContactNumber) {
        this.customerContactNumber = customerContactNumber;
    }

    public String getCustomerEmailId() {
        return customerEmailId;
    }

    public void setCustomerEmailId(String customerEmailId) {
        this.customerEmailId = customerEmailId;
    }

}

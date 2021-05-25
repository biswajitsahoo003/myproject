package com.tcl.dias.oms.beans;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.security.PrivateKey;
import java.util.Date;

/**
 * Bean file
 *
 *
 * @author Arunmani
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpportunityConfiguration {

    private String quoteCode;
    private String optyId;
    private String productName;
    private Date optyCreatedDate;
    private String optyStage;
    private String optyCreatedUserType;
    private Integer optyCreatedBy;
    private String percentage;
    private String dealStatus;


    private String customerName;

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }


    public String getQuoteCode() {
        return quoteCode;
    }

    public void setQuoteCode(String quoteCode) {
        this.quoteCode = quoteCode;
    }


    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }


    public String getOptyId() {
        return optyId;
    }

    public void setOptyId(String optyId) {
        this.optyId = optyId;
    }

    public Date getOptyCreatedDate() {
        return optyCreatedDate;
    }

    public void setOptyCreatedDate(Date optyCreatedDate) {
        this.optyCreatedDate = optyCreatedDate;
    }

    public String getOptyStage() {
        return optyStage;
    }

    public void setOptyStage(String optyStage) {
        this.optyStage = optyStage;
    }

    public String getOptyCreatedUserType() {
        return optyCreatedUserType;
    }

    public void setOptyCreatedUserType(String optyCreatedUserType) {
        this.optyCreatedUserType = optyCreatedUserType;
    }

    public Integer getOptyCreatedBy() {
        return optyCreatedBy;
    }

    public void setOptyCreatedBy(Integer optyCreatedBy) {
        this.optyCreatedBy = optyCreatedBy;
    }

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }


    public String getDealStatus() {
        return dealStatus;
    }

    public void setDealStatus(String dealStatus) {
        this.dealStatus = dealStatus;
    }


    @Override
    public String toString() {
        return "OpportunityConfiguration{" +
                "quoteCode='" + quoteCode + '\'' +
                ", optyId=" + optyId +
                ", productName='" + productName + '\'' +
                ", optyCreatedDate=" + optyCreatedDate +
                ", optyStage='" + optyStage + '\'' +
                ", optyCreatedUserType=" + optyCreatedUserType +
                ", optyCreatedBy='" + optyCreatedBy +
                '}';
    }
}

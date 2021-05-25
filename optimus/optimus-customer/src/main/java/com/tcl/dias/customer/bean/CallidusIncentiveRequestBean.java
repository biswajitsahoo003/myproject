package com.tcl.dias.customer.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Request Bean for Callidus Incentive
 *
 * @author AVALLAPI
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CallidusIncentiveRequestBean {

    private String productName;
    private String currency;
    private String commissionType;
    private String opportunityId;
    private String customerName;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCommissionType() {
        return commissionType;
    }

    public void setCommissionType(String commissionType) {
        this.commissionType = commissionType;
    }

    public String getOpportunityId() {
        return opportunityId;
    }

    public void setOpportunityId(String opportunityId) {
        this.opportunityId = opportunityId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
}

package com.tcl.dias.customer.bean;

import java.util.List;
/**
 * Bean including  Partner Commisions Response
 *
 * @author AVALLAPI
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
public class PartnerCommissionsResponse {

    private List<CallidusDetailsBean> commissions;
    private List<String> eligibleProducts;
    private List<String> eligibleCurrency;
    private List<String> customerNames;

    public List<String> getEligibleProducts() {
        return eligibleProducts;
    }

    public void setEligibleProducts(List<String> eligibleProducts) {
        this.eligibleProducts = eligibleProducts;
    }

    public List<String> getEligibleCurrency() {
        return eligibleCurrency;
    }

    public void setEligibleCurrency(List<String> eligibleCurrency) {
        this.eligibleCurrency = eligibleCurrency;
    }

    public List<CallidusDetailsBean> getCommissions() {
        return commissions;
    }

    public void setCommissions(List<CallidusDetailsBean> commissions) {
        this.commissions = commissions;
    }

    public List<String> getCustomerNames() {
        return customerNames;
    }

    public void setCustomerNames(List<String> customerNames) {
        this.customerNames = customerNames;
    }
}

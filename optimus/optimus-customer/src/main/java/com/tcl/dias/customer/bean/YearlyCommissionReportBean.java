package com.tcl.dias.customer.bean;
/**
 * This file contains the YearlyCommissionReportBean.java class.
 *
 *
 * @author Biswajit
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class YearlyCommissionReportBean {

    private String business;
    private String eligibleIncentives;
    private String incentivesProcesse;


    public String getBusiness() {
        return business;
    }

    public void setBusiness(String business) {
        this.business = business;
    }

    public String getEligibleIncentives() {
        return eligibleIncentives;
    }

    public void setEligibleIncentives(String eligibleIncentives) {
        this.eligibleIncentives = eligibleIncentives;
    }

    public String getIncentivesProcesse() {
        return incentivesProcesse;
    }

    public void setIncentivesProcesse(String incentivesProcesse) {
        this.incentivesProcesse = incentivesProcesse;
    }

}

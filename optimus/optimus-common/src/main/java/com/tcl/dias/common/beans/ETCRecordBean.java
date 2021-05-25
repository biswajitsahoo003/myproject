package com.tcl.dias.common.beans;


/**
 * This file contains the ETCRecordBean.java class. used for sfdc for termination
 *
 */
public class ETCRecordBean {

    private String opportunityName;
    private String etcWaiverTypeProposedBySales;
    private String etcValueProposedBySales;
    private String etcWaiverBasedOnEtcPolicy;
    private String agreeWithWaiverProposedBySales;
    private String finalEtcAmountToBeInvoiced;
    private String lmProviderPayout;
    private String comments;
    private String id;
    private String compensatoryDetails;

    public String getOpportunityName() {
        return opportunityName;
    }

    public void setOpportunityName(String opportunityName) {
        this.opportunityName = opportunityName;
    }

    public String getEtcWaiverTypeProposedBySales() {
        return etcWaiverTypeProposedBySales;
    }

    public void setEtcWaiverTypeProposedBySales(String etcWaiverTypeProposedBySales) {
        this.etcWaiverTypeProposedBySales = etcWaiverTypeProposedBySales;
    }

    public String getEtcValueProposedBySales() {
        return etcValueProposedBySales;
    }

    public void setEtcValueProposedBySales(String etcValueProposedBySales) {
        this.etcValueProposedBySales = etcValueProposedBySales;
    }

    public String getEtcWaiverBasedOnEtcPolicy() {
        return etcWaiverBasedOnEtcPolicy;
    }

    public void setEtcWaiverBasedOnEtcPolicy(String etcWaiverBasedOnEtcPolicy) {
        this.etcWaiverBasedOnEtcPolicy = etcWaiverBasedOnEtcPolicy;
    }

    public String getAgreeWithWaiverProposedBySales() {
        return agreeWithWaiverProposedBySales;
    }

    public void setAgreeWithWaiverProposedBySales(String agreeWithWaiverProposedBySales) {
        this.agreeWithWaiverProposedBySales = agreeWithWaiverProposedBySales;
    }

    public String getFinalEtcAmountToBeInvoiced() {
        return finalEtcAmountToBeInvoiced;
    }

    public void setFinalEtcAmountToBeInvoiced(String finalEtcAmountToBeInvoiced) {
        this.finalEtcAmountToBeInvoiced = finalEtcAmountToBeInvoiced;
    }

    public String getLmProviderPayout() {
        return lmProviderPayout;
    }

    public void setLmProviderPayout(String lmProviderPayout) {
        this.lmProviderPayout = lmProviderPayout;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

	public String getCompensatoryDetails() {
		return compensatoryDetails;
	}

	public void setCompensatoryDetails(String compensatoryDetails) {
		this.compensatoryDetails = compensatoryDetails;
	}
    
    
}

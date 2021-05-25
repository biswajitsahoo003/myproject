package com.tcl.dias.sfdc.bean;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
        "Opportunity_Name__c",
        "ETC_Waiver_Type_proposed_by_Sales__c",
        "ETC_Value_proposed_by_Sales__c",
        "ETC_Waiver_Based_on_ETC_policy__c",
        "Agree_with_Waiver_proposed_by_Sales__c",
        "Final_ETC_amount_to_be_invoiced__c",
        "LM_Provider_Payout__c",
        "Comments__c",
        "Id",
        "Corresponding_Circuit_ID__c"
})
public class EtcRecordBean {

    @JsonProperty("Opportunity_Name__c")
    private String opportunityNameC;

    @JsonProperty("ETC_Waiver_Type_proposed_by_Sales__c")
    private String etcWaiverTypeProposedBySalesC;

    @JsonProperty("ETC_Value_proposed_by_Sales__c")
    private String etcValueProposedBySalesC;

    @JsonProperty("ETC_Waiver_Based_on_ETC_policy__c")
    private String etcWaiverBasedOnEtcPolicyC;

    @JsonProperty("Agree_with_Waiver_proposed_by_Sales__c")
    private String agreeWithWaiverProposedBySalesC;

    @JsonProperty("Final_ETC_amount_to_be_invoiced__c")
    private String finalEtcAmountToBeInvoicedC;

    @JsonProperty("LM_Provider_Payout__c")
    private String lmProviderPayoutC;

    @JsonProperty("Comments__c")
    private String commentsC;
    
    @JsonProperty("Corresponding_Circuit_ID__c")
    private String correspondingCircuitIdC;

    @JsonProperty("Id") // for update
    private String id;

    public String getOpportunityNameC() {
        return opportunityNameC;
    }

    public void setOpportunityNameC(String opportunityNameC) {
        this.opportunityNameC = opportunityNameC;
    }

    public String getEtcWaiverTypeProposedBySalesC() {
        return etcWaiverTypeProposedBySalesC;
    }

    public void setEtcWaiverTypeProposedBySalesC(String etcWaiverTypeProposedBySalesC) {
        this.etcWaiverTypeProposedBySalesC = etcWaiverTypeProposedBySalesC;
    }

    public String getEtcValueProposedBySalesC() {
        return etcValueProposedBySalesC;
    }

    public void setEtcValueProposedBySalesC(String etcValueProposedBySalesC) {
        this.etcValueProposedBySalesC = etcValueProposedBySalesC;
    }

    public String getEtcWaiverBasedOnEtcPolicyC() {
        return etcWaiverBasedOnEtcPolicyC;
    }

    public void setEtcWaiverBasedOnEtcPolicyC(String etcWaiverBasedOnEtcPolicyC) {
        this.etcWaiverBasedOnEtcPolicyC = etcWaiverBasedOnEtcPolicyC;
    }

    public String getAgreeWithWaiverProposedBySalesC() {
        return agreeWithWaiverProposedBySalesC;
    }

    public void setAgreeWithWaiverProposedBySalesC(String agreeWithWaiverProposedBySalesC) {
        this.agreeWithWaiverProposedBySalesC = agreeWithWaiverProposedBySalesC;
    }

    public String getFinalEtcAmountToBeInvoicedC() {
        return finalEtcAmountToBeInvoicedC;
    }

    public void setFinalEtcAmountToBeInvoicedC(String finalEtcAmountToBeInvoicedC) {
        this.finalEtcAmountToBeInvoicedC = finalEtcAmountToBeInvoicedC;
    }

    public String getLmProviderPayoutC() {
        return lmProviderPayoutC;
    }

    public void setLmProviderPayoutC(String lmProviderPayoutC) {
        this.lmProviderPayoutC = lmProviderPayoutC;
    }

    public String getCommentsC() {
        return commentsC;
    }

    public void setCommentsC(String commentsC) {
        this.commentsC = commentsC;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

	public String getCorrespondingCircuitIdC() {
		return correspondingCircuitIdC;
	}

	public void setCorrespondingCircuitIdC(String correspondingCircuitIdC) {
		this.correspondingCircuitIdC = correspondingCircuitIdC;
	}
    
    
}

package com.tcl.dias.common.sfdc.response.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Deal Registration Details
 *
 * @author Sathishkumar Manogaran
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({"Opportunity_ID__c","Deal_Registration_Status__c","StageName","Probability"})
public class DealRegistrationResponseBean {

    @JsonProperty("Deal_Registration_Status__c")
    private String dealRegistrationStatus;

    @JsonProperty("StageName")
    private String stageName;

    @JsonProperty("Opportunity_ID__c")
    private String opportunityId;

    @JsonProperty("Probability")
    private Integer probability;

    public String getDealRegistrationStatus() {
        return dealRegistrationStatus;
    }

    public void setDealRegistrationStatus(String dealRegistrationStatus) {
        this.dealRegistrationStatus = dealRegistrationStatus;
    }


    public String getStageName() {
        return stageName;
    }

    public void setStageName(String stageName) {
        this.stageName = stageName;
    }


    public String getOpportunityId() {
        return opportunityId;
    }

    public void setOpportunityId(String opportunityId) {
        this.opportunityId = opportunityId;
    }

    public Integer getProbability() {
        return probability;
    }

    public void setProbability(Integer probability) {
        this.probability = probability;
    }


    @Override
    public String toString() {
        return "DealRegistrationResponseBean{" +
                "dealRegistrationStatus='" + dealRegistrationStatus + '\'' +
                '}';
    }
}

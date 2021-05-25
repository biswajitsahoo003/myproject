package com.tcl.dias.common.sfdc.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * SFDC SfdcPartnerOpportunityBean details
 *
 * @author AVALLAPI
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({"Id", "Name", "Opportunity_Classification__c", "Opportunity_ID__c", "Type","Portal_Transaction_Id__c","CreatedDate","CloseDate","Term_of_Months__c","Deal_Registration_Status__c",
        "StageName", "Probability", "Opportunity_MRC__c", "Opportunity_NRC__c", "Customer_Contracting_Entity__c",
        "Customer_Contracting_Entity__r" ,"Partner_Name__r","Account"})
public class SfdcPartnerOpportunityBean {

    @JsonProperty("Id")
    private String id;

    @JsonProperty("Name")
    private String name;

    @JsonProperty("Opportunity_Classification__c")
    private String opportunityClassfication;

    @JsonProperty("Opportunity_ID__c")
    private String opportunityId;

    @JsonProperty("Type")
    private String type;

    @JsonProperty("Portal_Transaction_Id__c")
    private String transactionId;

    @JsonProperty("CreatedDate")
    private String createdDate;

    @JsonProperty("CloseDate")
    private String closeDate;

    @JsonProperty("Term_of_Months__c")
    private String termsInMonth;

    @JsonProperty("Deal_Registration_Status__c")
    private String dealRegistrationStatus;

    @JsonProperty("StageName")
    private String stageName;

    @JsonProperty("Probability")
    private String probability;

    @JsonProperty("Opportunity_MRC__c")
    private Double opportunityMrc;

    @JsonProperty("Opportunity_NRC__c")
    private Double opportunityNrc;

    @JsonProperty("Customer_Contracting_Entity__c")
    private String customerContractingEntityC;

    @JsonProperty("Customer_Contracting_Entity__r")
    private SfdcCustomerContractingEntity sfdcCustomerContractingEntity;

    @JsonProperty("Partner_Name__r")
    private sfdcPartnerEntity sfdcPartnerContractingEntity;

    @JsonProperty("Account")
    private sfdcAccountEntity sfdcAccountEntity;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOpportunityClassfication() {
        return opportunityClassfication;
    }

    public void setOpportunityClassfication(String opportunityClassfication) {
        this.opportunityClassfication = opportunityClassfication;
    }

    public String getOpportunityId() {
        return opportunityId;
    }

    public void setOpportunityId(String opportunityId) {
        this.opportunityId = opportunityId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStageName() {
        return stageName;
    }

    public void setStageName(String stageName) {
        this.stageName = stageName;
    }

    public String getProbability() {
        return probability;
    }

    public void setProbability(String probability) {
        this.probability = probability;
    }

    public Double getOpportunityMrc() {
        return opportunityMrc;
    }

    public void setOpportunityMrc(Double opportunityMrc) {
        this.opportunityMrc = opportunityMrc;
    }

    public Double getOpportunityNrc() {
        return opportunityNrc;
    }

    public void setOpportunityNrc(Double opportunityNrc) {
        this.opportunityNrc = opportunityNrc;
    }

    public String getCustomerContractingEntityC() {
        return customerContractingEntityC;
    }

    public void setCustomerContractingEntityC(String customerContractingEntityC) {
        this.customerContractingEntityC = customerContractingEntityC;
    }

    public SfdcCustomerContractingEntity getSfdcCustomerContractingEntity() {
        return sfdcCustomerContractingEntity;
    }

    public void setSfdcCustomerContractingEntity(SfdcCustomerContractingEntity sfdcCustomerContractingEntity) {
        this.sfdcCustomerContractingEntity = sfdcCustomerContractingEntity;
    }


    public sfdcPartnerEntity getSfdcPartnerContractingEntity() {
        return sfdcPartnerContractingEntity;
    }

    public void setSfdcPartnerContractingEntity(sfdcPartnerEntity sfdcPartnerContractingEntity) {
        this.sfdcPartnerContractingEntity = sfdcPartnerContractingEntity;
    }


    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getCloseDate() {
        return closeDate;
    }

    public void setCloseDate(String closeDate) {
        this.closeDate = closeDate;
    }

    public String getTermsInMonth() {
        return termsInMonth;
    }

    public void setTermsInMonth(String termsInMonth) {
        this.termsInMonth = termsInMonth;
    }

    public String getDealRegistrationStatus() {
        return dealRegistrationStatus;
    }

    public void setDealRegistrationStatus(String dealRegistrationStatus) {
        this.dealRegistrationStatus = dealRegistrationStatus;
    }


    public sfdcAccountEntity getSfdcAccountEntity() {
        return sfdcAccountEntity;
    }

    public void setSfdcAccountEntity(sfdcAccountEntity sfdcAccountEntity) {
        this.sfdcAccountEntity = sfdcAccountEntity;
    }

    }


package com.tcl.dias.sfdc.response.bean;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "TermInDaysForDemoNew",
    "Tata_billing_account",
    "Sfdc_account_id",
    "Order_Type",
    "Oppty_Sub_Type",
    "Opportunity_Term",
    "Opportunity_StageName",
    "Opportunity_SF_Id",
    "Opportunity_Owner_Email",
    "Opportunity_Owner",
    "Opportunity_Name",
    "Opportunity_Internal_Id",
    "Opportunity_External_Id",
    "ETCCharges",
    "customerIndustry_quote",
    "Customer_industry",
    "Customer_contracting_entity",
    "Customer_category",
    "Account_name"
})
/**
 * SfdcPartnerDealRegistrationResponse.class is used for deal registration SFDC of partner
 * @author Biswajit
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class SfdcPartnerDealRegistrationResponse {

    @JsonProperty("TermInDaysForDemoNew")
    private Object termInDaysForDemoNew;
    @JsonProperty("Tata_billing_account")
    private String tataBillingAccount;
    @JsonProperty("Sfdc_account_id")
    private String sfdcAccountId;
    @JsonProperty("Order_Type")
    private Object orderType;
    @JsonProperty("Oppty_Sub_Type")
    private Object opptySubType;
    @JsonProperty("Opportunity_Term")
    private String opportunityTerm;
    @JsonProperty("Opportunity_StageName")
    private String opportunityStageName;
    @JsonProperty("Opportunity_SF_Id")
    private Object opportunitySFId;
    @JsonProperty("Opportunity_Owner_Email")
    private String opportunityOwnerEmail;
    @JsonProperty("Opportunity_Owner")
    private String opportunityOwner;
    @JsonProperty("Opportunity_Name")
    private String opportunityName;
    @JsonProperty("Opportunity_Internal_Id")
    private String opportunityInternalId;
    @JsonProperty("Opportunity_External_Id")
    private String opportunityExternalId;
    @JsonProperty("ETCCharges")
    private Object eTCCharges;
    @JsonProperty("customerIndustry_quote")
    private Object customerIndustryQuote;
    @JsonProperty("Customer_industry")
    private String customerIndustry;
    @JsonProperty("Customer_contracting_entity")
    private Object customerContractingEntity;
    @JsonProperty("Customer_category")
    private String customerCategory;
    @JsonProperty("Account_name")
    private String accountName;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("TermInDaysForDemoNew")
    public Object getTermInDaysForDemoNew() {
        return termInDaysForDemoNew;
    }

    @JsonProperty("TermInDaysForDemoNew")
    public void setTermInDaysForDemoNew(Object termInDaysForDemoNew) {
        this.termInDaysForDemoNew = termInDaysForDemoNew;
    }


    @JsonProperty("Tata_billing_account")
    public String getTataBillingAccount() {
        return tataBillingAccount;
    }

    @JsonProperty("Tata_billing_account")
    public void setTataBillingAccount(String tataBillingAccount) {
        this.tataBillingAccount = tataBillingAccount;
    }


    @JsonProperty("Sfdc_account_id")
    public String getSfdcAccountId() {
        return sfdcAccountId;
    }

    @JsonProperty("Sfdc_account_id")
    public void setSfdcAccountId(String sfdcAccountId) {
        this.sfdcAccountId = sfdcAccountId;
    }


    @JsonProperty("Order_Type")
    public Object getOrderType() {
        return orderType;
    }

    @JsonProperty("Order_Type")
    public void setOrderType(Object orderType) {
        this.orderType = orderType;
    }


    @JsonProperty("Oppty_Sub_Type")
    public Object getOpptySubType() {
        return opptySubType;
    }

    @JsonProperty("Oppty_Sub_Type")
    public void setOpptySubType(Object opptySubType) {
        this.opptySubType = opptySubType;
    }


    @JsonProperty("Opportunity_Term")
    public String getOpportunityTerm() {
        return opportunityTerm;
    }

    @JsonProperty("Opportunity_Term")
    public void setOpportunityTerm(String opportunityTerm) {
        this.opportunityTerm = opportunityTerm;
    }


    @JsonProperty("Opportunity_StageName")
    public String getOpportunityStageName() {
        return opportunityStageName;
    }

    @JsonProperty("Opportunity_StageName")
    public void setOpportunityStageName(String opportunityStageName) {
        this.opportunityStageName = opportunityStageName;
    }

    @JsonProperty("Opportunity_SF_Id")
    public Object getOpportunitySFId() {
        return opportunitySFId;
    }

    @JsonProperty("Opportunity_SF_Id")
    public void setOpportunitySFId(Object opportunitySFId) {
        this.opportunitySFId = opportunitySFId;
    }


    @JsonProperty("Opportunity_Owner_Email")
    public String getOpportunityOwnerEmail() {
        return opportunityOwnerEmail;
    }

    @JsonProperty("Opportunity_Owner_Email")
    public void setOpportunityOwnerEmail(String opportunityOwnerEmail) {
        this.opportunityOwnerEmail = opportunityOwnerEmail;
    }

    @JsonProperty("Opportunity_Owner")
    public String getOpportunityOwner() {
        return opportunityOwner;
    }

    @JsonProperty("Opportunity_Owner")
    public void setOpportunityOwner(String opportunityOwner) {
        this.opportunityOwner = opportunityOwner;
    }

    @JsonProperty("Opportunity_Name")
    public String getOpportunityName() {
        return opportunityName;
    }

    @JsonProperty("Opportunity_Name")
    public void setOpportunityName(String opportunityName) {
        this.opportunityName = opportunityName;
    }

    @JsonProperty("Opportunity_Internal_Id")
    public String getOpportunityInternalId() {
        return opportunityInternalId;
    }

    @JsonProperty("Opportunity_Internal_Id")
    public void setOpportunityInternalId(String opportunityInternalId) {
        this.opportunityInternalId = opportunityInternalId;
    }

    @JsonProperty("Opportunity_External_Id")
    public String getOpportunityExternalId() {
        return opportunityExternalId;
    }

    @JsonProperty("Opportunity_External_Id")
    public void setOpportunityExternalId(String opportunityExternalId) {
        this.opportunityExternalId = opportunityExternalId;
    }

    @JsonProperty("ETCCharges")
    public Object getETCCharges() {
        return eTCCharges;
    }

    @JsonProperty("ETCCharges")
    public void setETCCharges(Object eTCCharges) {
        this.eTCCharges = eTCCharges;
    }

    @JsonProperty("customerIndustry_quote")
    public Object getCustomerIndustryQuote() {
        return customerIndustryQuote;
    }

    @JsonProperty("customerIndustry_quote")
    public void setCustomerIndustryQuote(Object customerIndustryQuote) {
        this.customerIndustryQuote = customerIndustryQuote;
    }

    @JsonProperty("Customer_industry")
    public String getCustomerIndustry() {
        return customerIndustry;
    }

    @JsonProperty("Customer_industry")
    public void setCustomerIndustry(String customerIndustry) {
        this.customerIndustry = customerIndustry;
    }

    @JsonProperty("Customer_contracting_entity")
    public Object getCustomerContractingEntity() {
        return customerContractingEntity;
    }

    @JsonProperty("Customer_contracting_entity")
    public void setCustomerContractingEntity(Object customerContractingEntity) {
        this.customerContractingEntity = customerContractingEntity;
    }

    @JsonProperty("Customer_category")
    public String getCustomerCategory() {
        return customerCategory;
    }

    @JsonProperty("Customer_category")
    public void setCustomerCategory(String customerCategory) {
        this.customerCategory = customerCategory;
    }

    @JsonProperty("Account_name")
    public String getAccountName() {
        return accountName;
    }

    @JsonProperty("Account_name")
    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}

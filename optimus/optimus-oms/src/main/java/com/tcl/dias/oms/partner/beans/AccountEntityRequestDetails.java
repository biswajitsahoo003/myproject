package com.tcl.dias.oms.partner.beans;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({ "Account_Name__c","Industry__c","Industry_Sub_Type__c","HQ_Country__c","Customer_Website__c","Account_RTM__c",
        "Partner_Account_Manager_Email__c"})
public class AccountEntityRequestDetails {

    @JsonProperty("Account_Name__c")
    String accountName;
    @JsonProperty("Industry__c")
    String industry;
    @JsonProperty("Industry_Sub_Type__c")
    String industrySubType;
    @JsonProperty("HQ_Country__c")
    String country;
    @JsonProperty("Customer_Website__c")
    String website;
    @JsonProperty("Account_RTM__c")
    String accountRTM;
    @JsonProperty("Partner_Account_Manager_Email__c")
    String psamEmailId;

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getIndustrySubType() {
        return industrySubType;
    }

    public void setIndustrySubType(String industrySubType) {
        this.industrySubType = industrySubType;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getAccountRTM() {
        return accountRTM;
    }

    public void setAccountRTM(String accountRTM) {
        this.accountRTM = accountRTM;
    }

    public String getPsamEmailId() {
        return psamEmailId;
    }

    public void setPsamEmailId(String psamEmailId) {
        this.psamEmailId = psamEmailId;
    }

}

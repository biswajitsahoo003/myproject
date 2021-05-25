package com.tcl.dias.oms.partner.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({ "SFDC_Account_ID__c","FY_Segmentation__c"})
public class AccountParameters {

    @JsonProperty("SFDC_Account_ID__c")
    private String sfdcAccountId;

    @JsonProperty("FY_Segmentation__c")
    private String fySegmentation;

    @JsonProperty("Account_RTM__c")
    private String accountRTM;

    public String getSfdcAccountId() {
        return sfdcAccountId;
    }

    public void setSfdcAccountId(String sfdcAccountId) {
        this.sfdcAccountId = sfdcAccountId;
    }

    public String getFySegmentation() {
        return fySegmentation;
    }

    public void setFySegmentation(String fySegmentation) {
        this.fySegmentation = fySegmentation;
    }

    public String getAccountRTM() {
        return accountRTM;
    }

    public void setAccountRTM(String accountRTM) {
        this.accountRTM = accountRTM;
    }




}
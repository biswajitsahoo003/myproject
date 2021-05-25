package com.tcl.dias.common.sfdc.response.bean;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({ "Id","SFDC_Account_ID__c","FY_Segmentation__c","CustomerIdentifier__c","IsCreatedViaAPI__c"})
public class AccountParameters {

    @JsonProperty("Id")
    private String id;
    @JsonProperty("SFDC_Account_ID__c")
    private String sfdcAccountId;
    @JsonProperty("FY_Segmentation__c")
    private String fySegmentation;
    @JsonProperty("CustomerIdentifier__c")
    private String customerIdentifier;
    @JsonProperty("IsCreatedViaAPI__c")
    private Boolean iscreateViaApi;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public String getCustomerIdentifier() {
        return customerIdentifier;
    }

    public void setCustomerIdentifier(String customerIdentifier) {
        this.customerIdentifier = customerIdentifier;
    }

    public Boolean getIscreateViaApi() {
        return iscreateViaApi;
    }

    public void setIscreateViaApi(Boolean iscreateViaApi) {
        this.iscreateViaApi = iscreateViaApi;
    }


}
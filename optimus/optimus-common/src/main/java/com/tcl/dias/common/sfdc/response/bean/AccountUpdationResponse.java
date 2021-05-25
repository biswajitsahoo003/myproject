package com.tcl.dias.common.sfdc.response.bean;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({ "status","message","CustomerIdentifier","customAccId","RecordTypeName","Account"})
public class AccountUpdationResponse {


    @JsonProperty("status")
    private String status;
    @JsonProperty("message")
    private String message;
    @JsonProperty("CustomerIdentifier")
    private String customerIdentifier;
    @JsonProperty("customAccId")
    private String customAccId;
    @JsonProperty("RecordTypeName")
    private String recordTypeName;
    @JsonProperty("Account")
    private AccountParameters accountParameters;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCustomerIdentifier() {
        return customerIdentifier;
    }

    public void setCustomerIdentifier(String customerIdentifier) {
        this.customerIdentifier = customerIdentifier;
    }

    public String getCustomAccId() {
        return customAccId;
    }

    public void setCustomAccId(String customAccId) {
        this.customAccId = customAccId;
    }

    public String getRecordTypeName() {
        return recordTypeName;
    }

    public void setRecordTypeName(String recordTypeName) {
        this.recordTypeName = recordTypeName;
    }

    public AccountParameters getAccountParameters() {
        return accountParameters;
    }

    public void setAccountParameters(AccountParameters accountParameters) {
        this.accountParameters = accountParameters;
    }

}

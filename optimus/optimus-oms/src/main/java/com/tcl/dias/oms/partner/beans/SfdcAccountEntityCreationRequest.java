package com.tcl.dias.oms.partner.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({ "objAccountLegalEntityReqCreate","recordTypeName"})
public class SfdcAccountEntityCreationRequest {

    @JsonProperty("objAccountLegalEntityReqCreate")
    AccountEntityRequestDetails accountEntityRequestDetails;

    @JsonProperty("recordTypeName")
    String recordTypeName;

    public AccountEntityRequestDetails getAccountEntityRequestDetails() {
        return accountEntityRequestDetails;
    }

    public void setAccountEntityRequestDetails(AccountEntityRequestDetails accountEntityRequestDetails) {
        this.accountEntityRequestDetails = accountEntityRequestDetails;
    }

    public String getRecordTypeName() {
        return recordTypeName;
    }

    public void setRecordTypeName(String recordTypeName) {
        this.recordTypeName = recordTypeName;
    }


}

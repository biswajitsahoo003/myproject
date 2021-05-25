package com.tcl.dias.oms.partner.beans;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({ "reqWrapper"})
public class SfdcAccountEntitycreationWrapper {

    @JsonProperty("reqWrapper")
    private SfdcAccountEntityCreationRequest sfdcAccountEntityCreationRequest;

    public SfdcAccountEntityCreationRequest getSfdcAccountEntityCreationRequest() {
        return sfdcAccountEntityCreationRequest;
    }

    public void setSfdcAccountEntityCreationRequest(SfdcAccountEntityCreationRequest sfdcAccountEntityCreationRequest) {
        this.sfdcAccountEntityCreationRequest = sfdcAccountEntityCreationRequest;
    }

}

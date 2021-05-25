package com.tcl.dias.oms.partner.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({ "update_request_v1"})
public class AccountUpdateWrapper {

    @JsonProperty("update_request_v1")
    private AccountVariables accountVariables;

    public AccountVariables getAccountVariables() {
        return accountVariables;
    }

    public void setAccountVariables(AccountVariables accountVariables) {
        this.accountVariables = accountVariables;
    }



}

package com.tcl.dias.oms.partner.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({ "Account","Account_Owner_Name"})
public class AccountVariables {

    @JsonProperty("Account")
    private AccountParameters accountParameters;

    @JsonProperty("Account_Owner_Name")
    private String accountOwnerName;


    public AccountParameters getAccountParameters() {
        return accountParameters;
    }

    public void setAccountParameters(AccountParameters accountParameters) {
        this.accountParameters = accountParameters;
    }

    public String getAccountOwnerName() {
        return accountOwnerName;
    }

    public void setAccountOwnerName(String accountOwnerName) {
        this.accountOwnerName = accountOwnerName;
    }




}
package com.tcl.dias.common.sfdc.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * SFDC SfdcCustomerContractingEntity details
 *
 * @author AVALLAPI
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({ "Id", "Customer_Code__c", "Name", "is_Partner_account__c"})
public class SfdcCustomerContractingEntity {

    @JsonProperty("Id")
    private String id;

    @JsonProperty("Customer_Code__c")
    private String customerCode;

    @JsonProperty("Name")
    private String name;

    @JsonProperty("is_Partner_account__c")
    private String isPartnerAccount;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIsPartnerAccount() {
        return isPartnerAccount;
    }

    public void setIsPartnerAccount(String isPartnerAccount) {
        this.isPartnerAccount = isPartnerAccount;
    }

}

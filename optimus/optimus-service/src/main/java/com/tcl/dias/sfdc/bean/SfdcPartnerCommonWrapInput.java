
package com.tcl.dias.sfdc.bean;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
        "AccountId_NGP",
        "partnerLegalEntity",
        "currencyNGP",
        "modifyScenarioFlag",
        "customerSignedByNGP",
        "accountManagerNGP",
        "partnerAccountManager",
        "operation",
        "productType",
        "customerPartnerLegalEntityId"
})
public class SfdcPartnerCommonWrapInput extends  BaseBean{

    @JsonProperty("AccountId_NGP")
    private String accountIdNGP;
    @JsonProperty("partnerLegalEntity")
    private String partnerLegalEntity;
    @JsonProperty("currencyNGP")
    private String currencyNGP;
    @JsonProperty("modifyScenarioFlag")
    private String modifyScenarioFlag;
    @JsonProperty("customerSignedByNGP")
    private String customerSignedByNGP;
    @JsonProperty("migrationsourcesystem")
    private String migrationsourcesystem;
    @JsonProperty("accountManagerNGP")
    private String accountManagerNGP;
    @JsonProperty("partnerAccountManager")
    private String partnerAccountManager;
    @JsonProperty("operation")
    private String operation;
    @JsonProperty("productType")
    private String productType;
    @JsonProperty("customerPartnerLegalEntityId")
    private String customerPartnerLegalEntityId;
    @JsonProperty("partnerManagedCustomer")
    private String partnerManagedCustomer;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("AccountId_NGP")
    public String getAccountIdNGP() {
        return accountIdNGP;
    }

    @JsonProperty("AccountId_NGP")
    public void setAccountIdNGP(String accountIdNGP) {
        this.accountIdNGP = accountIdNGP;
    }


    @JsonProperty("partnerLegalEntity")
    public String getPartnerLegalEntity() {
        return partnerLegalEntity;
    }

    @JsonProperty("partnerLegalEntity")
    public void setPartnerLegalEntity(String partnerLegalEntity) {
        this.partnerLegalEntity = partnerLegalEntity;
    }

    @JsonProperty("currencyNGP")
    public String getCurrencyNGP() {
        return currencyNGP;
    }

    @JsonProperty("currencyNGP")
    public void setCurrencyNGP(String currencyNGP) {
        this.currencyNGP = currencyNGP;
    }

    @JsonProperty("modifyScenarioFlag")
    public String getModifyScenarioFlag() {
        return modifyScenarioFlag;
    }

    @JsonProperty("modifyScenarioFlag")
    public void setModifyScenarioFlag(String modifyScenarioFlag) {
        this.modifyScenarioFlag = modifyScenarioFlag;
    }

    @JsonProperty("customerSignedByNGP")
    public String getCustomerSignedByNGP() {
        return customerSignedByNGP;
    }

    @JsonProperty("customerSignedByNGP")
    public void setCustomerSignedByNGP(String customerSignedByNGP) {
        this.customerSignedByNGP = customerSignedByNGP;
    }


    @JsonProperty("accountManagerNGP")
    public String getAccountManagerNGP() {
        return accountManagerNGP;
    }

    @JsonProperty("accountManagerNGP")
    public void setAccountManagerNGP(String accountManagerNGP) {
        this.accountManagerNGP = accountManagerNGP;
    }


    @JsonProperty("partnerAccountManager")
    public String getPartnerAccountManager() {
        return partnerAccountManager;
    }

    @JsonProperty("partnerAccountManager")
    public void setPartnerAccountManager(String partnerAccountManager) {
        this.partnerAccountManager = partnerAccountManager;
    }

    @JsonProperty("operation")
    public String getOperation() {
        return operation;
    }

    @JsonProperty("operation")
    public void setOperation(String operation) {
        this.operation = operation;
    }

    @JsonProperty("productType")
    public String getProductType() {
        return productType;
    }

    @JsonProperty("productType")
    public void setProductType(String productType) {
        this.productType = productType;
    }


    @JsonProperty("customerPartnerLegalEntityId")
    public String getCustomerPartnerLegalEntityId() {
        return customerPartnerLegalEntityId;
    }

    @JsonProperty("customerPartnerLegalEntityId")
    public void setCustomerPartnerLegalEntityId(String customerPartnerLegalEntityId) {
        this.customerPartnerLegalEntityId = customerPartnerLegalEntityId;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public String getMigrationsourcesystem() {
        return migrationsourcesystem;
    }

    public void setMigrationsourcesystem(String migrationsourcesystem) {
        this.migrationsourcesystem = migrationsourcesystem;
    }

    public String getPartnerManagedCustomer() {
        return partnerManagedCustomer;
    }

    public void setPartnerManagedCustomer(String partnerManagedCustomer) {
        this.partnerManagedCustomer = partnerManagedCustomer;
    }
}

package com.tcl.dias.sfdc.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Used to pass the SFDC account related information in request payload
 *
 * @author Anusha Unni
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({"RecordTypeName","City","Account_ID","LegalEntity","contact"})
public class SfdcPartnerEntityContactBean extends BaseBean {

    @JsonProperty("RecordTypeName")
    private String recordTypeName;
    @JsonProperty("City")
    private String city;
    @JsonProperty("Account_ID")
    private String accountId;
    @JsonProperty("LegalEntity")
    private String legalEntity;
    @JsonProperty("contact")
    private SfdcPartnerEntityContact sfdcPartnerEntityContact;

    @JsonProperty("RecordTypeName")
    public String getRecordTypeName() {
        return recordTypeName;
    }

    @JsonProperty("RecordTypeName")
    public void setRecordTypeName(String recordTypeName) {
        this.recordTypeName = recordTypeName;
    }

    @JsonProperty("contact")
    public SfdcPartnerEntityContact getSfdcPartnerEntityContact() {
        return sfdcPartnerEntityContact;
    }

    @JsonProperty("contact")
    public void setSfdcPartnerEntityContact(SfdcPartnerEntityContact sfdcPartnerEntityContact) {
        this.sfdcPartnerEntityContact = sfdcPartnerEntityContact;
    }

    @JsonProperty("City")
    public String getCity() {
        return city;
    }

    @JsonProperty("City")
    public void setCity(String city) {
        this.city = city;
    }

    @JsonProperty("Account_ID")
    public String getAccountId() {
        return accountId;
    }

    @JsonProperty("Account_ID")
    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    @JsonProperty("LegalEntity")
    public String getLegalEntity() {
        return legalEntity;
    }

    @JsonProperty("LegalEntity")
    public void setLegalEntity(String legalEntity) {
        this.legalEntity = legalEntity;
    }
}

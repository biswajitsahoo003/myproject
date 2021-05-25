package com.tcl.dias.sfdc.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Used to pass the SFDC account related information in request payload
 *
 * @author Biswajit
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({"LegalEntity", "SFDC_Account_ID", "Registered_Address_City", "HQ_Contact", "Legal_Entity_Owner_Name", "Service_Manager_Name"})
public class SfdcPartnerEntityAccountBean extends BaseBean {

    @JsonProperty("SFDC_Account_ID")
    private String sfdcAccountId;
    @JsonProperty("Registered_Address_City")
    private String registeredAddressCity;
    @JsonProperty("HQ_Contact")
    private String hQContact;
    @JsonProperty("Legal_Entity_Owner_Name")
    private String legalEntityOwnerName;
    @JsonProperty("Service_Manager_Name")
    private String serviceManagerName;
    @JsonProperty("CustomerContact")
    private String customerContact;
    @JsonProperty("LegalEntity")
    private SfdcPartnerEntityBean sfdcPartnerEntityBean;

    @JsonProperty("LegalEntity")
    public SfdcPartnerEntityBean getSfdcPartnerEntityBean() {
        return sfdcPartnerEntityBean;
    }

    @JsonProperty("LegalEntity")
    public void setSfdcPartnerEntityBean(SfdcPartnerEntityBean sfdcPartnerEntityBean) {
        this.sfdcPartnerEntityBean = sfdcPartnerEntityBean;
    }

    @JsonProperty("SFDC_Account_ID")
    public String getSfdcAccountId() {
        return sfdcAccountId;
    }

    @JsonProperty("Registered_Address_City")
    public String getRegisteredAddressCity() {
        return registeredAddressCity;
    }

    @JsonProperty("HQ_Contact")
    public String gethQContact() {
        return hQContact;
    }

    @JsonProperty("Legal_Entity_Owner_Name")
    public String getLegalEntityOwnerName() {
        return legalEntityOwnerName;
    }

    @JsonProperty("Service_Manager_Name")
    public String getServiceManagerName() {
        return serviceManagerName;
    }

    @JsonProperty("SFDC_Account_ID")
    public void setSfdcAccountId(String sfdcAccountId) {
        this.sfdcAccountId = sfdcAccountId;
    }

    @JsonProperty("Registered_Address_City")
    public void setRegisteredAddressCity(String registeredAddressCity) {
        this.registeredAddressCity = registeredAddressCity;
    }

    @JsonProperty("HQ_Contact")
    public void sethQContact(String hQContact) {
        this.hQContact = hQContact;
    }

    @JsonProperty("Legal_Entity_Owner_Name")
    public void setLegalEntityOwnerName(String legalEntityOwnerName) {
        this.legalEntityOwnerName = legalEntityOwnerName;
    }

    @JsonProperty("Service_Manager_Name")
    public void setServiceManagerName(String serviceManagerName) {
        this.serviceManagerName = serviceManagerName;
    }
    @JsonProperty("CustomerContact")
    public String getCustomerContact() {
        return customerContact;
    }
    @JsonProperty("CustomerContact")
    public void setCustomerContact(String customerContact) {
        this.customerContact = customerContact;
    }

}

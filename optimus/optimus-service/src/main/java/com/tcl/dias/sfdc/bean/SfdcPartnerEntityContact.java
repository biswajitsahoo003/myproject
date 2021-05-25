package com.tcl.dias.sfdc.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * This file contains the SfdcPartnerEntityBean.java class.
 * used to pass the actual partner entity property value to SFDC
 *
 *
 * @author Anusha Unni
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({"FirstName","LastName","Email","Phone","BSSE_Mailing_Street__c","ADDRESS_LINE1__c","MailingCity","MailingState",
"BSSE_Mailing_Zip_Postal_Code__c","Zip_Postal_Code__c","MailingCountry","Active__c","MobilePhone","Role__c"})
public class SfdcPartnerEntityContact extends BaseBean{

    @JsonProperty("FirstName")
    private String firstName;
    @JsonProperty("LastName")
    private String lastName;
    @JsonProperty("Email")
    private String email;
    @JsonProperty("Phone")
    private String phone;
    @JsonProperty("BSSE_Mailing_Street__c")
    private String mailingStreet;
    @JsonProperty("ADDRESS_LINE1__c")
    private String addressLine1;
    @JsonProperty("MailingCity")
    private String mailingCity;
    @JsonProperty("MailingState")
    private String mailingState;
    @JsonProperty("BSSE_Mailing_Zip_Postal_Code__c")
    private String mailingZipCode;
    @JsonProperty("Zip_Postal_Code__c")
    private String zipPostalCode;
    @JsonProperty("MailingCountry")
    private String mailingCountry;
    @JsonProperty("Active__c")
    private String active;
    @JsonProperty("MobilePhone")
    private String mobilePhone;
    @JsonProperty("Role__c")
    private String role;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMailingStreet() {
        return mailingStreet;
    }

    public void setMailingStreet(String mailingStreet) {
        this.mailingStreet = mailingStreet;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getMailingCity() {
        return mailingCity;
    }

    public void setMailingCity(String mailingCity) {
        this.mailingCity = mailingCity;
    }

    public String getMailingState() {
        return mailingState;
    }

    public void setMailingState(String mailingState) {
        this.mailingState = mailingState;
    }

    public String getMailingZipCode() {
        return mailingZipCode;
    }

    public void setMailingZipCode(String mailingZipCode) {
        this.mailingZipCode = mailingZipCode;
    }

    public String getZipPostalCode() {
        return zipPostalCode;
    }

    public void setZipPostalCode(String zipPostalCode) {
        this.zipPostalCode = zipPostalCode;
    }

    public String getMailingCountry() {
        return mailingCountry;
    }

    public void setMailingCountry(String mailingCountry) {
        this.mailingCountry = mailingCountry;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}

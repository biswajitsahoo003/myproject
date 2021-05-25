package com.tcl.dias.oms.beans;

import com.fasterxml.jackson.annotation.JsonInclude;
/**
 *
 * Request DTO / Bean for Partner Entity
 *
 * @author Biswajit
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PartnerEntityRequest {

    private Integer id;
    private String customerName;
    private String industry;
    private String country;
    private String customerWebsite;
    private String industrySubType;
    private String registrationNumber;
    private String registeredAddressCity;
    private String subIndustry;
    private String registeredAddressStateProvince;
    private String registeredAddressStreet;
    private String registeredAddressZipPostalCode;
    private String typeOfBusiness;
    private Integer erfPartnerId;
    private Integer erfPartnerLegalEntityId;
    private String customerContactName;
    private String customerContactEmail;
    private String product;
    private String salesContractType;

    public Integer getErfPartnerId() {
        return erfPartnerId;
    }

    public void setErfPartnerId(Integer erfPartnerId) {
        this.erfPartnerId = erfPartnerId;
    }

    public String getSubIndustry() {
        return subIndustry;
    }

    public void setSubIndustry(String subIndustry) {
        this.subIndustry = subIndustry;
    }

    public String getRegisteredAddressStateProvince() {
        return registeredAddressStateProvince;
    }

    public void setRegisteredAddressStateProvince(String registeredAddressStateProvince) {
        this.registeredAddressStateProvince = registeredAddressStateProvince;
    }

    public String getRegisteredAddressStreet() {
        return registeredAddressStreet;
    }

    public void setRegisteredAddressStreet(String registeredAddressStreet) {
        this.registeredAddressStreet = registeredAddressStreet;
    }

    public String getRegisteredAddressZipPostalCode() {
        return registeredAddressZipPostalCode;
    }

    public void setRegisteredAddressZipPostalCode(String registeredAddressZipPostalCode) {
        this.registeredAddressZipPostalCode = registeredAddressZipPostalCode;
    }

    public String getTypeOfBusiness() {
        return typeOfBusiness;
    }

    public void setTypeOfBusiness(String typeOfBusiness) {
        this.typeOfBusiness = typeOfBusiness;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getRegisteredAddressCity() {
        return registeredAddressCity;
    }

    public void setRegisteredAddressCity(String registeredAddressCity) {
        this.registeredAddressCity = registeredAddressCity;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setCustomerWebsite(String customerWebsite) {
        this.customerWebsite = customerWebsite;
    }

    public void setIndustrySubType(String industrySubType) {
        this.industrySubType = industrySubType;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getIndustry() {
        return industry;
    }

    public String getCountry() {
        return country;
    }

    public String getCustomerWebsite() {
        return customerWebsite;
    }

    public String getIndustrySubType() {
        return industrySubType;
    }

    public Integer getErfPartnerLegalEntityId() {
        return erfPartnerLegalEntityId;
    }

    public void setErfPartnerLegalEntityId(Integer erfPartnerLegalEntityId) {
        this.erfPartnerLegalEntityId = erfPartnerLegalEntityId;
    }

    public String getCustomerContactName() {
        return customerContactName;
    }

    public void setCustomerContactName(String customerContactName) {
        this.customerContactName = customerContactName;
    }

    public String getCustomerContactEmail() {
        return customerContactEmail;
    }

    public void setCustomerContactEmail(String customerContactEmail) {
        this.customerContactEmail = customerContactEmail;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getSalesContractType() {
        return salesContractType;
    }

    public void setSalesContractType(String salesContractType) {
        this.salesContractType = salesContractType;
    }

    @Override
    public String toString() {
        return "PartnerEntityRequest{" +
                "id=" + id +
                ", customerName='" + customerName + '\'' +
                ", industry='" + industry + '\'' +
                ", country='" + country + '\'' +
                ", customerWebsite='" + customerWebsite + '\'' +
                ", industrySubType='" + industrySubType + '\'' +
                ", registrationNumber='" + registrationNumber + '\'' +
                ", registeredAddressCity='" + registeredAddressCity + '\'' +
                ", subIndustry='" + subIndustry + '\'' +
                ", registeredAddressStateProvince='" + registeredAddressStateProvince + '\'' +
                ", registeredAddressStreet='" + registeredAddressStreet + '\'' +
                ", registeredAddressZipPostalCode='" + registeredAddressZipPostalCode + '\'' +
                ", typeOfBusiness='" + typeOfBusiness + '\'' +
                ", erfPartnerId=" + erfPartnerId +
                ", erfPartnerLegalEntityId=" + erfPartnerLegalEntityId +
                ", customerContactName='" + customerContactName + '\'' +
                ", customerContactEmail='" + customerContactEmail + '\'' +
                ", product='" + product + '\'' +
                ", salesContractType='" + salesContractType + '\'' +
                '}';
    }
}

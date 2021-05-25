package com.tcl.dias.common.beans;

/**
 * Partner temp customer details bean
 *
 * @author Anusha Unni
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
public class PartnerTempCustomerDetailsBean {

    private Integer id;
    private String customerName;
    private String industry;
    private String subIndustry;
    private String industrySubtype;
    private String customerWebsite;
    private String customerContactName;
    private String customerContactEmail;
    private String customerContactNumber;
    private String registrationNumber;
    //private String customerType;
    private String erfPartnerId;
    private String erfPartnerLegalEntityId;
    private String secsId;
    private String orgId;
    private String street;
    private String city;
    private String state;
    private String postalCode;
    private String country;
    private String typeOfBusiness;
    private String supplierLeOwner;
    private String product;
    private String salesContractType;
    private String customerLegalEntityCuid;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getSubIndustry() {
        return subIndustry;
    }

    public void setSubIndustry(String subIndustry) {
        this.subIndustry = subIndustry;
    }

    public String getIndustrySubtype() {
        return industrySubtype;
    }

    public void setIndustrySubtype(String industrySubtype) {
        this.industrySubtype = industrySubtype;
    }

    public String getCustomerWebsite() {
        return customerWebsite;
    }

    public void setCustomerWebsite(String customerWebsite) {
        this.customerWebsite = customerWebsite;
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

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

//    public String getCustomerType() {
//        return customerType;
//    }

//    public void setCustomerType(String customerType) {
//        this.customerType = customerType;
//    }

    public String getErfPartnerId() {
        return erfPartnerId;
    }

    public void setErfPartnerId(String erfPartnerId) {
        this.erfPartnerId = erfPartnerId;
    }

    public String getErfPartnerLegalEntityId() {
        return erfPartnerLegalEntityId;
    }

    public void setErfPartnerLegalEntityId(String erfPartnerLegalEntityId) {
        this.erfPartnerLegalEntityId = erfPartnerLegalEntityId;
    }

    public String getSecsId() {
        return secsId;
    }

    public void setSecsId(String secsId) {
        this.secsId = secsId;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCustomerContactNumber() {
        return customerContactNumber;
    }

    public void setCustomerContactNumber(String customerContactNumber) {
        this.customerContactNumber = customerContactNumber;
    }

    public String getTypeOfBusiness() {
        return typeOfBusiness;
    }

    public void setTypeOfBusiness(String typeOfBusiness) {
        this.typeOfBusiness = typeOfBusiness;
    }

    public String getSupplierLeOwner() {
        return supplierLeOwner;
    }

    public void setSupplierLeOwner(String supplierLeOwner) {
        this.supplierLeOwner = supplierLeOwner;
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

    public String getCustomerLegalEntityCuid() {
        return customerLegalEntityCuid;
    }

    public void setCustomerLegalEntityCuid(String customerLegalEntityCuid) {
        this.customerLegalEntityCuid = customerLegalEntityCuid;
    }

    @Override
    public String toString() {
        return "PartnerTempCustomerDetailsBean{" +
                "id=" + id +
                ", customerName='" + customerName + '\'' +
                ", industry='" + industry + '\'' +
                ", subIndustry='" + subIndustry + '\'' +
                ", industrySubtype='" + industrySubtype + '\'' +
                ", customerWebsite='" + customerWebsite + '\'' +
                ", customerContactName='" + customerContactName + '\'' +
                ", customerContactEmail='" + customerContactEmail + '\'' +
                ", customerContactNumber='" + customerContactNumber + '\'' +
                ", registrationNumber='" + registrationNumber + '\'' +
                ", erfPartnerId='" + erfPartnerId + '\'' +
                ", erfPartnerLegalEntityId='" + erfPartnerLegalEntityId + '\'' +
                ", secsId='" + secsId + '\'' +
                ", orgId='" + orgId + '\'' +
                ", street='" + street + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", country='" + country + '\'' +
                ", typeOfBusiness='" + typeOfBusiness + '\'' +
                ", supplierLeOwner='" + supplierLeOwner + '\'' +
                ", product='" + product + '\'' +
                ", salesContractType='" + salesContractType + '\'' +
                ", customerLegalEntityCuid='" + customerLegalEntityCuid + '\'' +
                '}';
    }
}

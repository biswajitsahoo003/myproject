package com.tcl.dias.oms.entity.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Entity Class for Temp Customer
 *
 * @author Diksha Sharma
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@Entity
@Table(name = "partner_temp_customer_details")
@NamedQuery(name = "PartnerTempCustomerDetails.findAll", query = "SELECT p FROM PartnerTempCustomerDetails p")
public class PartnerTempCustomerDetails implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "customer_name", unique = true)
    private String customerName;

    @Column(name = "industry")
    private String industry;

    @Column(name = "sub_industry")
    private String subIndustry;

    @Column(name = "industry_subtype")
    private String industrySubtype;

    @Column(name = "customer_website")
    private String customerWebsite;

    @Column(name = "registration_no")
    private String registrationNo;

    @Column(name = "business_type")
    private String businessType;

    @Column(name = "erf_partner_id")
    private String erfPartnerId;

    @Column(name = "erf_partner_legal_entity_id")
    private String erfPartnerLegalEntityId;

    @Column(name = "secs_id")
    private String secsId;

    @Column(name = "org_id")
    private String orgId;

    @Column(name = "street")
    private String street;

    @Column(name = "city")
    private String city;

    @Column(name = "state")
    private String state;

    @Column(name = "postal_code")
    private String postalCode;

    @Column(name = "country")
    private String country;

    @Column(name = "customer_contact_name")
    private String customerContactName;

    @Column(name = "customer_contact_email")
    private String customerContactEmail;

    @Column(name = "record_id")
    private String recordId;

    @Column(name = "customer_contact_number")
    private String customerContactNumber;

    @Column(name = "third_party_service_job_reference_id")
    private String thirdPartyServiceJobReferenceId;

    @Column(name = "customer_legal_entity_id")
    private String customerLegalEntityId;

    @Column(name = "customer_legal_entity_cuid")
    private String customerLegalEntityCuid;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_time")
    private Timestamp createdTime;


    public PartnerTempCustomerDetails() {
        // DO NOTHING
    }

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

    public String getRegistrationNo() {
        return registrationNo;
    }

    public void setRegistrationNo(String registrationNo) {
        this.registrationNo = registrationNo;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getErfPartnerId() {
        return erfPartnerId;
    }

    public void setErfPartnerId(String erfPartnerId) {
        this.erfPartnerId = erfPartnerId;
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

    public String getErfPartnerLegalEntityId() {
        return erfPartnerLegalEntityId;
    }

    public void setErfPartnerLegalEntityId(String erfPartnerLegalEntityId) {
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

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }
    public String getCustomerContactNumber() {
        return customerContactNumber;
    }

    public void setCustomerContactNumber(String customerContactNumber) {
        this.customerContactNumber = customerContactNumber;
    }

    public String getThirdPartyServiceJobReferenceId() {
        return thirdPartyServiceJobReferenceId;
    }

    public void setThirdPartyServiceJobReferenceId(String thirdPartyServiceJobReferenceId) {
        this.thirdPartyServiceJobReferenceId = thirdPartyServiceJobReferenceId;
    }

    public String getCustomerLegalEntityId() {
        return customerLegalEntityId;
    }

    public void setCustomerLegalEntityId(String customerLegalEntityId) {
        this.customerLegalEntityId = customerLegalEntityId;
    }

    public String getCustomerLegalEntityCuid() {
        return customerLegalEntityCuid;
    }

    public void setCustomerLegalEntityCuid(String customerLegalEntityCuid) {
        this.customerLegalEntityCuid = customerLegalEntityCuid;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Timestamp getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Timestamp createdTime) {
        this.createdTime = createdTime;
    }

    @Override
    public String toString() {
        return "PartnerTempCustomerDetails{" +
                "id=" + id +
                ", customerName='" + customerName + '\'' +
                ", industry='" + industry + '\'' +
                ", subIndustry='" + subIndustry + '\'' +
                ", industrySubtype='" + industrySubtype + '\'' +
                ", customerWebsite='" + customerWebsite + '\'' +
                ", registrationNo='" + registrationNo + '\'' +
                ", businessType='" + businessType + '\'' +
                ", erfPartnerId='" + erfPartnerId + '\'' +
                ", erfPartnerLegalEntityId='" + erfPartnerLegalEntityId + '\'' +
                ", secsId='" + secsId + '\'' +
                ", orgId='" + orgId + '\'' +
                ", street='" + street + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", country='" + country + '\'' +
                ", customerContactName='" + customerContactName + '\'' +
                ", customerContactEmail='" + customerContactEmail + '\'' +
                ", customerContactNumber='" + customerContactNumber + '\'' +
                ", thirdPartyServiceJobReferenceId='" + thirdPartyServiceJobReferenceId + '\'' +
                ", customerLegalEntityId='" + customerLegalEntityId + '\'' +
                ", customerLegalEntityCuid='" + customerLegalEntityCuid + '\'' +
                '}';
    }
}

package com.tcl.dias.customer.entity.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * <Comments>
 *
 * @author Gnana prakash
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
@Entity
@Table(name = "partner_end_customer_mapping")
public class PartnerEndCustomerMapping implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "partner_id")
    private Integer partnerId;

    @Column(name = "partner_account_name")
    private String partnerAccountName;

    @Column(name = "end_customer_cuid")
    private String endCustomerCuid;

    @Column(name = "end_customer_le_name")
    private String endCustomerLeName;

    @Column(name = "end_customer_country_name")
    private String endCustomerCountryName;

    @Column(name = "end_customer_country_id")
    private Integer endCustomerCountryId;

    @Column(name = "end_customer_erf_loc_id")
    private Integer endCustomerErfLocId;

    @Column(name = "sfdc_verification_type")
    private String sfdcVerificationType;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_time")
    private Timestamp createdTime;

    @Column(name = "updated_by")
    private String updatedBy;

    @Column(name = "updated_time")
    private Timestamp updatedTime;

    /*@Column(name = "end_customer_website")
    private String endCustomerWebsite;

    @Column(name = "end_customer_contact_name")
    private String endCustomerContactName;

    @Column(name = "end_customer_contact_email")
    private String endCustomerContactEmail;*/


    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(Integer partnerId) {
        this.partnerId = partnerId;
    }

    public String getPartnerAccountName() {
        return partnerAccountName;
    }

    public void setPartnerAccountName(String partnerAccountName) {
        this.partnerAccountName = partnerAccountName;
    }

    public String getEndCustomerCuid() {
        return endCustomerCuid;
    }

    public void setEndCustomerCuid(String endCustomerCuid) {
        this.endCustomerCuid = endCustomerCuid;
    }

    public String getEndCustomerLeName() {
        return endCustomerLeName;
    }

    public void setEndCustomerLeName(String endCustomerLeName) {
        this.endCustomerLeName = endCustomerLeName;
    }

    public String getEndCustomerCountryName() {
        return endCustomerCountryName;
    }

    public void setEndCustomerCountryName(String endCustomerCountryName) {
        this.endCustomerCountryName = endCustomerCountryName;
    }

    public Integer getEndCustomerCountryId() {
        return endCustomerCountryId;
    }

    public void setEndCustomerCountryId(Integer endCustomerCountryId) {
        this.endCustomerCountryId = endCustomerCountryId;
    }

    public Integer getEndCustomerErfLocId() {
        return endCustomerErfLocId;
    }

    public void setEndCustomerErfLocId(Integer endCustomerErfLocId) {
        this.endCustomerErfLocId = endCustomerErfLocId;
    }

    public String getSfdcVerificationType() {
        return sfdcVerificationType;
    }

    public void setSfdcVerificationType(String sfdcVerificationType) {
        this.sfdcVerificationType = sfdcVerificationType;
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

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Timestamp getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Timestamp updatedTime) {
        this.updatedTime = updatedTime;
    }

    /*public String getEndCustomerWebsite() {
        return endCustomerWebsite;
    }

    public void setEndCustomerWebsite(String endCustomerWebsite) {
        this.endCustomerWebsite = endCustomerWebsite;
    }

    public String getEndCustomerContactName() {
        return endCustomerContactName;
    }

    public void setEndCustomerContactName(String endCustomerContactName) {
        this.endCustomerContactName = endCustomerContactName;
    }

    public String getEndCustomerContactEmail() {
        return endCustomerContactEmail;
    }

    public void setEndCustomerContactEmail(String endCustomerContactEmail) {
        this.endCustomerContactEmail = endCustomerContactEmail;
    }*/
}

package com.tcl.dias.serviceinventory.entity.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * This file contains the gsc service circuit link details
 *
 * @author Sathishkumar Manogaran
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
@Entity
@Table(name = "VW_GSC_PULSE_CHRG_CONFIG_DTLS")
public class ViewGscPulseChargeConfigDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "CMS_ID")
    private String cmsId;

    @Column(name = "PRODUCT_CUSTOMER_NM")
    private String productCustomerName;

    @Column(name = "SECS_ID")
    private String secsId;

    @Column(name = "PROD_OFFERING_NM")
    private String productOfferingName;

    @Column(name = "SERV_ABBR")
    private String serviceAbbr;

    @Column(name = "BILLING_ENTITY")
    private String billingEntity;

    @Column(name = "iso_ctry_cd")
    private String isoCountryCode;

    @Column(name = "intnl_ctry_dial_cd")
    private String internationalDialCode;

    @Column(name = "COUNTRY_NM")
    private String countryName;

    @Column(name = "ORIGINATION_NM")
    private String originationName;

    @Column(name = "CNTRY_ABBR")
    private String countryAbbr;

    @Column(name = "created_date")
    private Timestamp createdDate;

    @Column(name = "updated_date")
    private Timestamp updatedDate;

    public ViewGscPulseChargeConfigDetails() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCmsId() {
        return cmsId;
    }

    public void setCmsId(String cmsId) {
        this.cmsId = cmsId;
    }

    public String getProductCustomerName() {
        return productCustomerName;
    }

    public void setProductCustomerName(String productCustomerName) {
        this.productCustomerName = productCustomerName;
    }

    public String getSecsId() {
        return secsId;
    }

    public void setSecsId(String secsId) {
        this.secsId = secsId;
    }

    public String getProductOfferingName() {
        return productOfferingName;
    }

    public void setProductOfferingName(String productOfferingName) {
        this.productOfferingName = productOfferingName;
    }

    public String getServiceAbbr() {
        return serviceAbbr;
    }

    public void setServiceAbbr(String serviceAbbr) {
        this.serviceAbbr = serviceAbbr;
    }

    public String getBillingEntity() {
        return billingEntity;
    }

    public void setBillingEntity(String billingEntity) {
        this.billingEntity = billingEntity;
    }

    public String getIsoCountryCode() {
        return isoCountryCode;
    }

    public void setIsoCountryCode(String isoCountryCode) {
        this.isoCountryCode = isoCountryCode;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getOriginationName() {
        return originationName;
    }

    public void setOriginationName(String originationName) {
        this.originationName = originationName;
    }

    public String getCountryAbbr() {
        return countryAbbr;
    }

    public void setCountryAbbr(String countryAbbr) {
        this.countryAbbr = countryAbbr;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    public Timestamp getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Timestamp updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getInternationalDialCode() {
        return internationalDialCode;
    }

    public void setInternationalDialCode(String internationalDialCode) {
        this.internationalDialCode = internationalDialCode;
    }
}

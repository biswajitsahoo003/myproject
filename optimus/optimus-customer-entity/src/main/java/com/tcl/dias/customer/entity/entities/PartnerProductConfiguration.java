package com.tcl.dias.customer.entity.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * Partner Product Configuration Entity Class
 *
 * @author Sathishkumar Manogaran
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@Entity
@Table(name = "partner_product_configuration")
@NamedQuery(name = "PartnerProductConfiguration.findAll", query = "SELECT p FROM PartnerProductConfiguration p")
public class PartnerProductConfiguration implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "partner_id")
    private Integer partnerId;

    @Column(name = "partner_le_id")
    private Integer partnerLeId;


    @Column(name = "erf_loc_country_id")
    private Integer erfLocationCountryId;


    @Column(name = "erf_prod_catalog_product_family_id")
    private Integer erfProdCatalogProductFamilyId;

    @Column(name = "opty_classification")
    private String classification;

    @Column(name = "is_active")
    private String isActive;

    //bi-directional many-to-one association to CustomerLegalEntity
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "partner_le_id", insertable = false, updatable = false)
    private PartnerLegalEntity partnerLegalEntity;

    public PartnerProductConfiguration() {
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

    public Integer getPartnerLeId() {
        return partnerLeId;
    }

    public void setPartnerLeId(Integer partnerLeId) {
        this.partnerLeId = partnerLeId;
    }

    public Integer getErfLocationCountryId() {
        return erfLocationCountryId;
    }

    public void setErfLocationCountryId(Integer erfLocationCountryId) {
        this.erfLocationCountryId = erfLocationCountryId;
    }

    public Integer getErfProdCatalogProductFamilyId() {
        return erfProdCatalogProductFamilyId;
    }

    public void setErfProdCatalogProductFamilyId(Integer erfProdCatalogProductFamilyId) {
        this.erfProdCatalogProductFamilyId = erfProdCatalogProductFamilyId;
    }

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public PartnerLegalEntity getPartnerLegalEntity() {
        return partnerLegalEntity;
    }

    public void setPartnerLegalEntity(PartnerLegalEntity partnerLegalEntity) {
        this.partnerLegalEntity = partnerLegalEntity;
    }
}

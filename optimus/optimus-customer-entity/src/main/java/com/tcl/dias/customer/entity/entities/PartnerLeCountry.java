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
 * Partner Le Country Entity Class
 *
 * @author Sathishkumar Manogaran
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@Entity
@Table(name = "partner_le_country")
@NamedQuery(name = "PartnerLeCountry.findAll", query = "SELECT p FROM PartnerLeCountry p")
public class PartnerLeCountry implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "country_id")
    private Integer countryId;

    @Column(name = "is_default")
    private byte isDefault;

    //bi-directional many-to-one association to PartnerLegalEntity
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "partner_le_id")
    private PartnerLegalEntity partnerLegalEntity;

    public PartnerLeCountry() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCountryId() {
        return countryId;
    }

    public void setCountryId(Integer countryId) {
        this.countryId = countryId;
    }

    public byte getIsDefault() {
        return this.isDefault;
    }

    public void setIsDefault(byte isDefault) {
        this.isDefault = isDefault;
    }

    public PartnerLegalEntity getPartnerLegalEntity() {
        return this.partnerLegalEntity;
    }

    public void setPartnerLegalEntity(PartnerLegalEntity partnerLegalEntity) {
        this.partnerLegalEntity = partnerLegalEntity;
    }

}

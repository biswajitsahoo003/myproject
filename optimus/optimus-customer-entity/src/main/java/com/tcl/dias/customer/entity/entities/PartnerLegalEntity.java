package com.tcl.dias.customer.entity.entities;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Partner Legal Entity Class
 *
 * @author Sathishkumar Manogaran
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@Entity
@Table(name = "partner_legal_entity")
@NamedQuery(name = "PartnerLegalEntity.findAll", query = "SELECT p FROM PartnerLegalEntity p")
public class PartnerLegalEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "agreement_id")
    private String agreementId;

    @Column(name = "created_time")
    private Timestamp createdTime;

    @Column(name = "entity_name")
    private String entityName;

    private byte status;

    @Column(name = "tps_sfdc_cuid")
    private String tpsSfdcCuid;

    //bi-directional many-to-one association to PartnerLeAttributeValue
    @OneToMany(mappedBy = "partnerLegalEntity")
    private Set<PartnerLeAttributeValue> partnerLeAttributeValues;

    //bi-directional many-to-one association to PartnerLeCountry
    @OneToMany(mappedBy = "partnerLegalEntity")
    private Set<PartnerLeCountry> partnerLeCountries;

    //bi-directional many-to-one association to PartnerLeCurrency
    @OneToMany(mappedBy = "partnerLegalEntity")
    private Set<PartnerLeCurrency> partnerLeCurrencies;

    //bi-directional many-to-one association to Partner
    @ManyToOne(fetch = FetchType.LAZY)
    private Partner partner;

    @OneToMany(mappedBy = "partnerLegalEntity")
    private Set<PartnerProductConfiguration> partnerProductConfigurations;

    public PartnerLegalEntity() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAgreementId() {
        return this.agreementId;
    }

    public void setAgreementId(String agreementId) {
        this.agreementId = agreementId;
    }

    public Timestamp getCreatedTime() {
        return this.createdTime;
    }

    public void setCreatedTime(Timestamp createdTime) {
        this.createdTime = createdTime;
    }

    public String getEntityName() {
        return this.entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public byte getStatus() {
        return this.status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    public String getTpsSfdcCuid() {
        return this.tpsSfdcCuid;
    }

    public void setTpsSfdcCuid(String tpsSfdcCuid) {
        this.tpsSfdcCuid = tpsSfdcCuid;
    }

    public Set<PartnerLeAttributeValue> getPartnerLeAttributeValues() {
        return this.partnerLeAttributeValues;
    }

    public void setPartnerLeAttributeValues(Set<PartnerLeAttributeValue> partnerLeAttributeValues) {
        this.partnerLeAttributeValues = partnerLeAttributeValues;
    }

    public PartnerLeAttributeValue addPartnerLeAttributeValue(PartnerLeAttributeValue partnerLeAttributeValue) {
        getPartnerLeAttributeValues().add(partnerLeAttributeValue);
        partnerLeAttributeValue.setPartnerLegalEntity(this);

        return partnerLeAttributeValue;
    }

    public PartnerLeAttributeValue removePartnerLeAttributeValue(PartnerLeAttributeValue partnerLeAttributeValue) {
        getPartnerLeAttributeValues().remove(partnerLeAttributeValue);
        partnerLeAttributeValue.setPartnerLegalEntity(null);

        return partnerLeAttributeValue;
    }

    public Set<PartnerLeCountry> getPartnerLeCountries() {
        return this.partnerLeCountries;
    }

    public void setPartnerLeCountries(Set<PartnerLeCountry> partnerLeCountries) {
        this.partnerLeCountries = partnerLeCountries;
    }

    public PartnerLeCountry addPartnerLeCountry(PartnerLeCountry partnerLeCountry) {
        getPartnerLeCountries().add(partnerLeCountry);
        partnerLeCountry.setPartnerLegalEntity(this);

        return partnerLeCountry;
    }

    public PartnerLeCountry removePartnerLeCountry(PartnerLeCountry partnerLeCountry) {
        getPartnerLeCountries().remove(partnerLeCountry);
        partnerLeCountry.setPartnerLegalEntity(null);

        return partnerLeCountry;
    }

    public Set<PartnerLeCurrency> getPartnerLeCurrencies() {
        return this.partnerLeCurrencies;
    }

    public void setPartnerLeCurrencies(Set<PartnerLeCurrency> partnerLeCurrencies) {
        this.partnerLeCurrencies = partnerLeCurrencies;
    }

    public PartnerLeCurrency addPartnerLeCurrency(PartnerLeCurrency partnerLeCurrency) {
        getPartnerLeCurrencies().add(partnerLeCurrency);
        partnerLeCurrency.setPartnerLegalEntity(this);

        return partnerLeCurrency;
    }

    public PartnerLeCurrency removePartnerLeCurrency(PartnerLeCurrency partnerLeCurrency) {
        getPartnerLeCurrencies().remove(partnerLeCurrency);
        partnerLeCurrency.setPartnerLegalEntity(null);

        return partnerLeCurrency;
    }

    public Partner getPartner() {
        return this.partner;
    }

    public void setPartner(Partner partner) {
        this.partner = partner;
    }

    public Set<PartnerProductConfiguration> getPartnerProductConfigurations() {
        return partnerProductConfigurations;
    }

    public void setPartnerProductConfigurations(Set<PartnerProductConfiguration> partnerProductConfigurations) {
        this.partnerProductConfigurations = partnerProductConfigurations;
    }
}

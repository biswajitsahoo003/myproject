package com.tcl.dias.customer.entity.entities;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Partner Entity Class
 *
 * @author Sathishkumar Manogaran
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@Entity
@Table(name = "partner")
@NamedQuery(name = "Partner.findAll", query = "SELECT p FROM Partner p")
public class Partner implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_time")
    private Timestamp createdTime;

    private String name;

    private byte status;

    @Column(name = "tps_sfdc_account_id")
    private String tpsSfdcAccountId;

    @Column(name = "partner_profile_id")
    private Integer partnerProfileId;

    @Column(name = "account_id_18")
    private String accountId18;

    //bi-directional many-to-one association to PartnerAttributeValue
    @OneToMany(mappedBy = "partner")
    private Set<PartnerAttributeValue> partnerAttributeValues;

    //bi-directional many-to-one association to PartnerLegalEntity
    @OneToMany(mappedBy = "partner")
    private Set<PartnerLegalEntity> partnerLegalEntities;

    public Partner() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte getStatus() {
        return status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    public String getTpsSfdcAccountId() {
        return tpsSfdcAccountId;
    }

    public void setTpsSfdcAccountId(String tpsSfdcAccountId) {
        this.tpsSfdcAccountId = tpsSfdcAccountId;
    }

    public Set<PartnerAttributeValue> getPartnerAttributeValues() {
        return partnerAttributeValues;
    }

    public void setPartnerAttributeValues(Set<PartnerAttributeValue> partnerAttributeValues) {
        this.partnerAttributeValues = partnerAttributeValues;
    }

    public PartnerAttributeValue addPartnerAttributeValue(PartnerAttributeValue partnerAttributeValue) {
        getPartnerAttributeValues().add(partnerAttributeValue);
        partnerAttributeValue.setPartner(this);

        return partnerAttributeValue;
    }

    public PartnerAttributeValue removePartnerAttributeValue(PartnerAttributeValue partnerAttributeValue) {
        getPartnerAttributeValues().remove(partnerAttributeValue);
        partnerAttributeValue.setPartner(null);

        return partnerAttributeValue;
    }

    public Set<PartnerLegalEntity> getPartnerLegalEntities() {
        return this.partnerLegalEntities;
    }

    public void setPartnerLegalEntities(Set<PartnerLegalEntity> partnerLegalEntities) {
        this.partnerLegalEntities = partnerLegalEntities;
    }

    public PartnerLegalEntity addPartnerLegalEntity(PartnerLegalEntity partnerLegalEntity) {
        getPartnerLegalEntities().add(partnerLegalEntity);
        partnerLegalEntity.setPartner(this);

        return partnerLegalEntity;
    }

    public PartnerLegalEntity removePartnerLegalEntity(PartnerLegalEntity partnerLegalEntity) {
        getPartnerLegalEntities().remove(partnerLegalEntity);
        partnerLegalEntity.setPartner(null);

        return partnerLegalEntity;
    }

    public String getAccountId18() {
        return accountId18;
    }

    public void setAccountId18(String accountId18) {
        this.accountId18 = accountId18;
    }

    public Integer getPartnerProfileId() {
        return partnerProfileId;
    }

    public void setPartnerProfileId(Integer partnerProfileId) {
        this.partnerProfileId = partnerProfileId;
    }
}

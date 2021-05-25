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
 * Partner Le Currency Entity Class
 *
 * @author Sathishkumar Manogaran
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@Entity
@Table(name = "partner_le_currency")
@NamedQuery(name = "PartnerLeCurrency.findAll", query = "SELECT p FROM PartnerLeCurrency p")
public class PartnerLeCurrency implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "currency_id")
    private Integer currencyId;

    @Column(name = "is_default")
    private byte isDefault;

    private byte status;

    //bi-directional many-to-one association to PartnerLegalEntity
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "partner_le_id")
    private PartnerLegalEntity partnerLegalEntity;

    public PartnerLeCurrency() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Integer currencyId) {
        this.currencyId = currencyId;
    }

    public byte getIsDefault() {
        return this.isDefault;
    }

    public void setIsDefault(byte isDefault) {
        this.isDefault = isDefault;
    }

    public byte getStatus() {
        return this.status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    public PartnerLegalEntity getPartnerLegalEntity() {
        return this.partnerLegalEntity;
    }

    public void setPartnerLegalEntity(PartnerLegalEntity partnerLegalEntity) {
        this.partnerLegalEntity = partnerLegalEntity;
    }

}

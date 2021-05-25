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
 * Partner Le Attribute Value Entity Class
 *
 * @author Sathishkumar Manogaran
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@Entity
@Table(name = "partner_le_attribute_values")
@NamedQuery(name = "PartnerLeAttributeValue.findAll", query = "SELECT p FROM PartnerLeAttributeValue p")
public class PartnerLeAttributeValue implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "attribute_values")
    private String attributeValues;

    // bi-directional many-to-one association to MstLeAttribute
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mst_le_attributes_id")
    private MstLeAttribute mstLeAttribute;

    //bi-directional many-to-one association to PartnerLegalEntity
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "partner_le_id")
    private PartnerLegalEntity partnerLegalEntity;

    @Column(name = "product_name")
    private String productName;

    public PartnerLeAttributeValue() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAttributeValues() {
        return this.attributeValues;
    }

    public void setAttributeValues(String attributeValues) {
        this.attributeValues = attributeValues;
    }

    public MstLeAttribute getMstLeAttribute() {
        return mstLeAttribute;
    }

    public void setMstLeAttribute(MstLeAttribute mstLeAttribute) {
        this.mstLeAttribute = mstLeAttribute;
    }

    public PartnerLegalEntity getPartnerLegalEntity() {
        return this.partnerLegalEntity;
    }

    public void setPartnerLegalEntity(PartnerLegalEntity partnerLegalEntity) {
        this.partnerLegalEntity = partnerLegalEntity;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
}

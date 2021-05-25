package com.tcl.dias.customer.entity.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * Partner Attribute Value Entity Class
 *
 * @author Sathishkumar Manogaran
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@Entity
@Table(name = "partner_attribute_values")
@NamedQuery(name = "PartnerAttributeValue.findAll", query = "SELECT p FROM PartnerAttributeValue p")
public class PartnerAttributeValue implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "attribute_values")
    private String attributeValues;

    @Column(name = "mst_customer_sp_attribute_id")
    private Integer mstCustomerSpAttributeId;

    //bi-directional many-to-one association to Partner
    @ManyToOne(fetch = FetchType.LAZY)
    private Partner partner;

    public PartnerAttributeValue() {
    }


    public String getAttributeValues() {
        return this.attributeValues;
    }

    public void setAttributeValues(String attributeValues) {
        this.attributeValues = attributeValues;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMstCustomerSpAttributeId() {
        return mstCustomerSpAttributeId;
    }

    public void setMstCustomerSpAttributeId(Integer mstCustomerSpAttributeId) {
        this.mstCustomerSpAttributeId = mstCustomerSpAttributeId;
    }

    public Partner getPartner() {
        return this.partner;
    }

    public void setPartner(Partner partner) {
        this.partner = partner;
    }

}

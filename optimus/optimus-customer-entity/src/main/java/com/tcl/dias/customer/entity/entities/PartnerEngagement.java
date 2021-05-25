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
 * Partner Engagement Entity Class
 *
 * @author Sathishkumar Manogaran
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@Entity
@Table(name = "partner_engagements")
@NamedQuery(name = "PartnerEngagement.findAll", query = "SELECT p FROM PartnerEngagement p")
public class PartnerEngagement implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "is_active")
    private String isActive;

    //bi-directional many-to-one association to CustomerLegalEntity
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "le_id")
    private CustomerLegalEntity customerLegalEntity;

    public PartnerEngagement() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIsActive() {
        return this.isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public CustomerLegalEntity getCustomerLegalEntity() {
        return this.customerLegalEntity;
    }

    public void setCustomerLegalEntity(CustomerLegalEntity customerLegalEntity) {
        this.customerLegalEntity = customerLegalEntity;
    }

}

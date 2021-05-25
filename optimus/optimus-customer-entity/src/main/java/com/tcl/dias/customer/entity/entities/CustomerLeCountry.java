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
 * 
 * Entity Class
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name = "customer_le_country")
@NamedQuery(name = "CustomerLeCountry.findAll", query = "SELECT c FROM CustomerLeCountry c")
public class CustomerLeCountry implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "is_default")
	private Byte isDefault;

	// bi-directional many-to-one association to CustomerLegalEntity
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "customer_le_id")
	private CustomerLegalEntity customerLegalEntity;

	// bi-directional many-to-one association to MstCountry
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "country_id")
	private MstCountry mstCountry;

	public CustomerLeCountry() {
		// DO NOTHING
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Byte getIsDefault() {
		return this.isDefault;
	}

	public void setIsDefault(Byte isDefault) {
		this.isDefault = isDefault;
	}

	public CustomerLegalEntity getCustomerLegalEntity() {
		return this.customerLegalEntity;
	}

	public void setCustomerLegalEntity(CustomerLegalEntity customerLegalEntity) {
		this.customerLegalEntity = customerLegalEntity;
	}

	public MstCountry getMstCountry() {
		return this.mstCountry;
	}

	public void setMstCountry(MstCountry mstCountry) {
		this.mstCountry = mstCountry;
	}

}
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
 * Entity class
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name = "sp_le_country")
@NamedQuery(name = "SpLeCountry.findAll", query = "SELECT s FROM SpLeCountry s")
public class SpLeCountry implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "is_default")
	private Byte isDefault;

	// bi-directional many-to-one association to MstCountry
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "country_id")
	private MstCountry mstCountry;

	// bi-directional many-to-one association to ServiceProviderLegalEntity
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sp_le_id")
	private ServiceProviderLegalEntity serviceProviderLegalEntity;

	public SpLeCountry() {
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

	public MstCountry getMstCountry() {
		return this.mstCountry;
	}

	public void setMstCountry(MstCountry mstCountry) {
		this.mstCountry = mstCountry;
	}

	public ServiceProviderLegalEntity getServiceProviderLegalEntity() {
		return this.serviceProviderLegalEntity;
	}

	public void setServiceProviderLegalEntity(ServiceProviderLegalEntity serviceProviderLegalEntity) {
		this.serviceProviderLegalEntity = serviceProviderLegalEntity;
	}

}
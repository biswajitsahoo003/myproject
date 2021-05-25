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
@Table(name = "sp_le_currency")
@NamedQuery(name = "SpLeCurrency.findAll", query = "SELECT s FROM SpLeCurrency s")
public class SpLeCurrency implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "is_default")
	private Byte isDefault;

	private Byte status;

	// bi-directional many-to-one association to CurrencyMaster
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "currency_id")
	private CurrencyMaster currencyMaster;

	// bi-directional many-to-one association to ServiceProviderLegalEntity
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sp_le_id")
	private ServiceProviderLegalEntity serviceProviderLegalEntity;

	public SpLeCurrency() {
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

	public Byte getStatus() {
		return this.status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public CurrencyMaster getCurrencyMaster() {
		return this.currencyMaster;
	}

	public void setCurrencyMaster(CurrencyMaster currencyMaster) {
		this.currencyMaster = currencyMaster;
	}

	public ServiceProviderLegalEntity getServiceProviderLegalEntity() {
		return this.serviceProviderLegalEntity;
	}

	public void setServiceProviderLegalEntity(ServiceProviderLegalEntity serviceProviderLegalEntity) {
		this.serviceProviderLegalEntity = serviceProviderLegalEntity;
	}

}
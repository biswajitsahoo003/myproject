package com.tcl.dias.customer.entity.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * The persistent class for the service_provider_legal_entity database table.
 * 
 */
@Entity
@Table(name = "service_provider_legal_entity")
@NamedQuery(name = "ServiceProviderLegalEntity.findAll", query = "SELECT s FROM ServiceProviderLegalEntity s")
public class ServiceProviderLegalEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "created_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdTime;

	@Column(name = "entity_name")
	private String entityName;

	private Byte status;

	@Column(name = "tps_sfdc_cuid")
	private String tpsSfdcCuid;

	// bi-directional many-to-one association to ServiceProvider
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "service_provider_id")
	private ServiceProvider serviceProvider;

	// bi-directional many-to-one association to SpLeAttributeValue
	@OneToMany(mappedBy = "serviceProviderLegalEntity")
	private Set<SpLeAttributeValue> spLeAttributeValues;

	// bi-directional many-to-one association to SpLeCountry
	@OneToMany(mappedBy = "serviceProviderLegalEntity")
	private Set<SpLeCountry> spLeCountries;

	// bi-directional many-to-one association to SpLeCurrency
	@OneToMany(mappedBy = "serviceProviderLegalEntity")
	private Set<SpLeCurrency> spLeCurrencies;

	public ServiceProviderLegalEntity() {
		// DO NOTHING
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getCreatedTime() {
		return this.createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public String getEntityName() {
		return this.entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public Byte getStatus() {
		return this.status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public String getTpsSfdcCuid() {
		return this.tpsSfdcCuid;
	}

	public void setTpsSfdcCuid(String tpsSfdcCuid) {
		this.tpsSfdcCuid = tpsSfdcCuid;
	}

	public ServiceProvider getServiceProvider() {
		return this.serviceProvider;
	}

	public void setServiceProvider(ServiceProvider serviceProvider) {
		this.serviceProvider = serviceProvider;
	}

	public Set<SpLeAttributeValue> getSpLeAttributeValues() {
		return this.spLeAttributeValues;
	}

	public void setSpLeAttributeValues(Set<SpLeAttributeValue> spLeAttributeValues) {
		this.spLeAttributeValues = spLeAttributeValues;
	}

	public SpLeAttributeValue addSpLeAttributeValue(SpLeAttributeValue spLeAttributeValue) {
		getSpLeAttributeValues().add(spLeAttributeValue);
		spLeAttributeValue.setServiceProviderLegalEntity(this);

		return spLeAttributeValue;
	}

	public SpLeAttributeValue removeSpLeAttributeValue(SpLeAttributeValue spLeAttributeValue) {
		getSpLeAttributeValues().remove(spLeAttributeValue);
		spLeAttributeValue.setServiceProviderLegalEntity(null);

		return spLeAttributeValue;
	}

	public Set<SpLeCountry> getSpLeCountries() {
		return this.spLeCountries;
	}

	public void setSpLeCountries(Set<SpLeCountry> spLeCountries) {
		this.spLeCountries = spLeCountries;
	}

	public SpLeCountry addSpLeCountry(SpLeCountry spLeCountry) {
		getSpLeCountries().add(spLeCountry);
		spLeCountry.setServiceProviderLegalEntity(this);

		return spLeCountry;
	}

	public SpLeCountry removeSpLeCountry(SpLeCountry spLeCountry) {
		getSpLeCountries().remove(spLeCountry);
		spLeCountry.setServiceProviderLegalEntity(null);

		return spLeCountry;
	}

	public Set<SpLeCurrency> getSpLeCurrencies() {
		return this.spLeCurrencies;
	}

	public void setSpLeCurrencies(Set<SpLeCurrency> spLeCurrencies) {
		this.spLeCurrencies = spLeCurrencies;
	}

	public SpLeCurrency addSpLeCurrency(SpLeCurrency spLeCurrency) {
		getSpLeCurrencies().add(spLeCurrency);
		spLeCurrency.setServiceProviderLegalEntity(this);

		return spLeCurrency;
	}

	public SpLeCurrency removeSpLeCurrency(SpLeCurrency spLeCurrency) {
		getSpLeCurrencies().remove(spLeCurrency);
		spLeCurrency.setServiceProviderLegalEntity(null);

		return spLeCurrency;
	}

}
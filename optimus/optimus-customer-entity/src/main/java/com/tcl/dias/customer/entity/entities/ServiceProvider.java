package com.tcl.dias.customer.entity.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * 
 * Entity Class
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name = "service_provider")
@NamedQuery(name = "ServiceProvider.findAll", query = "SELECT s FROM ServiceProvider s")
public class ServiceProvider implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "created_by")
	private String createdBy;

	@Column(name = "created_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdTime;

	private String name;

	private Byte status;

	@Column(name = "tps_sfdc_account_id")
	private String tpsSfdcAccountId;

	// bi-directional many-to-one association to ServiceProviderLegalEntity
	@OneToMany(mappedBy = "serviceProvider")
	private Set<ServiceProviderLegalEntity> serviceProviderLegalEntities;

	// bi-directional many-to-one association to SpAttributeValue
	@OneToMany(mappedBy = "serviceProvider")
	private Set<SpAttributeValue> spAttributeValues;

	public ServiceProvider() {
		// DO NOTHING
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedTime() {
		return this.createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Byte getStatus() {
		return this.status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public String getTpsSfdcAccountId() {
		return this.tpsSfdcAccountId;
	}

	public void setTpsSfdcAccountId(String tpsSfdcAccountId) {
		this.tpsSfdcAccountId = tpsSfdcAccountId;
	}

	public Set<ServiceProviderLegalEntity> getServiceProviderLegalEntities() {
		return this.serviceProviderLegalEntities;
	}

	public void setServiceProviderLegalEntities(Set<ServiceProviderLegalEntity> serviceProviderLegalEntities) {
		this.serviceProviderLegalEntities = serviceProviderLegalEntities;
	}

	public ServiceProviderLegalEntity addServiceProviderLegalEntity(
			ServiceProviderLegalEntity serviceProviderLegalEntity) {
		getServiceProviderLegalEntities().add(serviceProviderLegalEntity);
		serviceProviderLegalEntity.setServiceProvider(this);

		return serviceProviderLegalEntity;
	}

	public ServiceProviderLegalEntity removeServiceProviderLegalEntity(
			ServiceProviderLegalEntity serviceProviderLegalEntity) {
		getServiceProviderLegalEntities().remove(serviceProviderLegalEntity);
		serviceProviderLegalEntity.setServiceProvider(null);

		return serviceProviderLegalEntity;
	}

	public Set<SpAttributeValue> getSpAttributeValues() {
		return this.spAttributeValues;
	}

	public void setSpAttributeValues(Set<SpAttributeValue> spAttributeValues) {
		this.spAttributeValues = spAttributeValues;
	}

	public SpAttributeValue addSpAttributeValue(SpAttributeValue spAttributeValue) {
		getSpAttributeValues().add(spAttributeValue);
		spAttributeValue.setServiceProvider(this);

		return spAttributeValue;
	}

	public SpAttributeValue removeSpAttributeValue(SpAttributeValue spAttributeValue) {
		getSpAttributeValues().remove(spAttributeValue);
		spAttributeValue.setServiceProvider(null);

		return spAttributeValue;
	}

}
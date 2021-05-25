package com.tcl.dias.productcatelog.entity.entities;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.tcl.dias.productcatelog.entity.base.BaseEntity;

/**
 * 
 * @author Manojkumar R
 *
 */
@Entity
@Table(name = "provider_type")
@NamedQuery(name = "ProviderType.findAll", query = "SELECT p FROM ProviderType p")
public class ProviderType extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name = "long_desc")
	private String description;

	@Column(name = "nm")
	private String name;

	// bi-directional many-to-one association to Provider
	@OneToMany(mappedBy = "providerType")
	private List<Provider> providers;


	public ProviderType() {
		// default constructor
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Provider> getProviders() {
		return this.providers;
	}

	public void setProviders(List<Provider> providers) {
		this.providers = providers;
	}

	public Provider addProvider(Provider provider) {
		getProviders().add(provider);
		provider.setProviderType(this);

		return provider;
	}

	public Provider removeProvider(Provider provider) {
		getProviders().remove(provider);
		provider.setProviderType(null);

		return provider;
	}

	

}
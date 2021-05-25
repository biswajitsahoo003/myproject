package com.tcl.dias.customer.entity.entities;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
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
@Table(name = "mst_countries")
@NamedQuery(name = "MstCountry.findAll", query = "SELECT m FROM MstCountry m")
public class MstCountry implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "name")
	private String name;

	@Column(name = "short_name")
	private String shortName;

	private Byte status;

	// bi-directional many-to-one association to CustomerLeCountry
	@OneToMany(mappedBy = "mstCountry")
	private Set<CustomerLeCountry> customerLeCountries;

	// bi-directional many-to-one association to SpLeCountry
	@OneToMany(mappedBy = "mstCountry")
	private Set<SpLeCountry> spLeCountries;

	public MstCountry() {
		// DO NOTHING
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getShortName() {
		return this.shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public Byte getStatus() {
		return this.status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public Set<CustomerLeCountry> getCustomerLeCountries() {
		return this.customerLeCountries;
	}

	public void setCustomerLeCountries(Set<CustomerLeCountry> customerLeCountries) {
		this.customerLeCountries = customerLeCountries;
	}

	public CustomerLeCountry addCustomerLeCountry(CustomerLeCountry customerLeCountry) {
		getCustomerLeCountries().add(customerLeCountry);
		customerLeCountry.setMstCountry(this);

		return customerLeCountry;
	}

	public CustomerLeCountry removeCustomerLeCountry(CustomerLeCountry customerLeCountry) {
		getCustomerLeCountries().remove(customerLeCountry);
		customerLeCountry.setMstCountry(null);

		return customerLeCountry;
	}

	public Set<SpLeCountry> getSpLeCountries() {
		return this.spLeCountries;
	}

	public void setSpLeCountries(Set<SpLeCountry> spLeCountries) {
		this.spLeCountries = spLeCountries;
	}

	public SpLeCountry addSpLeCountry(SpLeCountry spLeCountry) {
		getSpLeCountries().add(spLeCountry);
		spLeCountry.setMstCountry(this);

		return spLeCountry;
	}

	public SpLeCountry removeSpLeCountry(SpLeCountry spLeCountry) {
		getSpLeCountries().remove(spLeCountry);
		spLeCountry.setMstCountry(null);

		return spLeCountry;
	}

}
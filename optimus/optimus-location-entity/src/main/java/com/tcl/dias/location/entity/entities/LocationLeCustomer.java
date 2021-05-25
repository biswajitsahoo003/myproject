package com.tcl.dias.location.entity.entities;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * 
 * Entity Class
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name = "location_le_customer")
@NamedQuery(name = "LocationLeCustomer.findAll", query = "SELECT l FROM LocationLeCustomer l")
public class LocationLeCustomer implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "erf_cus_customer_le_id")
	private Integer erfCusCustomerLeId;

	// bi-directional many-to-one association to Location
	@ManyToOne(fetch = FetchType.LAZY)
	private Location location;

	// bi-directional many-to-one association to CustomerSiteLocationItContact
	@OneToMany(mappedBy = "customerLeLocation")
	private Set<CustomerSiteLocationItContact> customerSiteLocationItContacts;

	public LocationLeCustomer() {
		// DO NNOTHING
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getErfCusCustomerLeId() {
		return this.erfCusCustomerLeId;
	}

	public void setErfCusCustomerLeId(Integer erfCusCustomerLeId) {
		this.erfCusCustomerLeId = erfCusCustomerLeId;
	}

	public Set<CustomerSiteLocationItContact> getCustomerSiteLocationItContacts() {
		return customerSiteLocationItContacts;
	}

	public void setCustomerSiteLocationItContacts(Set<CustomerSiteLocationItContact> customerSiteLocationItContacts) {
		this.customerSiteLocationItContacts = customerSiteLocationItContacts;
	}

	public Location getLocation() {
		return this.location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

}
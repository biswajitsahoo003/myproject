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
@Table(name = "customer_location")
@NamedQuery(name = "CustomerLocation.findAll", query = "SELECT c FROM CustomerLocation c")
public class CustomerLocation implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "erf_cus_customer_id")
	private Integer erfCusCustomerId;

	// bi-directional many-to-one association to Location
	@ManyToOne(fetch = FetchType.LAZY)
	private Location location;

	// bi-directional many-to-one association to CustomerSiteLocationItContact
	@OneToMany(mappedBy = "customerLocation")
	private Set<CustomerSiteLocationItContact> customerSiteLocationItContacts;

	public CustomerLocation() {
		//DO NOTHING
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getErfCusCustomerId() {
		return this.erfCusCustomerId;
	}

	public void setErfCusCustomerId(Integer erfCusCustomerId) {
		this.erfCusCustomerId = erfCusCustomerId;
	}

	public Location getLocation() {
		return this.location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public Set<CustomerSiteLocationItContact> getCustomerSiteLocationItContacts() {
		return this.customerSiteLocationItContacts;
	}

	public void setCustomerSiteLocationItContacts(Set<CustomerSiteLocationItContact> customerSiteLocationItContacts) {
		this.customerSiteLocationItContacts = customerSiteLocationItContacts;
	}

	public CustomerSiteLocationItContact addCustomerSiteLocationItContact(
			CustomerSiteLocationItContact customerSiteLocationItContact) {
		getCustomerSiteLocationItContacts().add(customerSiteLocationItContact);
		customerSiteLocationItContact.setCustomerLocation(this);

		return customerSiteLocationItContact;
	}

	public CustomerSiteLocationItContact removeCustomerSiteLocationItContact(
			CustomerSiteLocationItContact customerSiteLocationItContact) {
		getCustomerSiteLocationItContacts().remove(customerSiteLocationItContact);
		customerSiteLocationItContact.setCustomerLocation(null);

		return customerSiteLocationItContact;
	}

}
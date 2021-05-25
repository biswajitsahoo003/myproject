package com.tcl.dias.location.entity.entities;

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
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name = "customer_site_location_it_contact")
@NamedQuery(name = "CustomerSiteLocationItContact.findAll", query = "SELECT c FROM CustomerSiteLocationItContact c")
public class CustomerSiteLocationItContact implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "contact_number")
	private String contactNumber;

	@Column(name = "email_id")
	private String emailId;

	@Column(name = "is_active")
	private Byte isActive;

	private String name;

	// bi-directional many-to-one association to CustomerLocation
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "customer_location_id")
	private CustomerLocation customerLocation;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "customer_le_location_id")
	private LocationLeCustomer customerLeLocation;

	public CustomerSiteLocationItContact() {
		// DO NOTHING
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getContactNumber() {
		return this.contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	public String getEmailId() {
		return this.emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public Byte getIsActive() {
		return this.isActive;
	}

	public void setIsActive(Byte isActive) {
		this.isActive = isActive;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public CustomerLocation getCustomerLocation() {
		return this.customerLocation;
	}

	public LocationLeCustomer getCustomerLeLocation() {
		return customerLeLocation;
	}

	public void setCustomerLeLocation(LocationLeCustomer customerLeLocation) {
		this.customerLeLocation = customerLeLocation;
	}

	public void setCustomerLocation(CustomerLocation customerLocation) {
		this.customerLocation = customerLocation;
	}

}
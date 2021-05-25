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
 * Bean class
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name = "le_state_gst_info")
@NamedQuery(name = "LeStateGst.findAll", query = "SELECT l FROM LeStateGst l")
public class LeStateGst implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "gst_no")
	private String gstNo;

	@Column(name = "address")
	private String address;

	private String state;
	
	@Column(name = "address_line_one")
	private String addresslineOne;
	
	@Column(name = "address_line_Two")
	private String addresslineTwo;
	
	@Column(name = "address_line_Three")
	private String addresslineThree;
	
	private String city;
	
	private String pincode;
	
	private String country;
	
	// bi-directional many-to-one association to CustomerLegalEntity
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "le_id")
	private CustomerLegalEntity customerLegalEntity;
	
	@Column(name = "is_active")
	private String isActive;

	public LeStateGst() {
		// DO NOTHING
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getGstNo() {
		return gstNo;
	}

	public void setGstNo(String gstNo) {
		this.gstNo = gstNo;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public CustomerLegalEntity getCustomerLegalEntity() {
		return customerLegalEntity;
	}

	public void setCustomerLegalEntity(CustomerLegalEntity customerLegalEntity) {
		this.customerLegalEntity = customerLegalEntity;
	}

	public String getAddresslineOne() {
		return addresslineOne;
	}

	public void setAddresslineOne(String addresslineOne) {
		this.addresslineOne = addresslineOne;
	}

	public String getAddresslineTwo() {
		return addresslineTwo;
	}

	public void setAddresslineTwo(String addresslineTwo) {
		this.addresslineTwo = addresslineTwo;
	}

	public String getAddresslineThree() {
		return addresslineThree;
	}

	public void setAddresslineThree(String addresslineThree) {
		this.addresslineThree = addresslineThree;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPincode() {
		return pincode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}
	
}
package com.tcl.dias.servicefulfillment.beans;

import com.tcl.dias.servicefulfillment.entity.entities.MstVendor;

/**
 * @author vivek
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 * used for the vendor details 
 */
public class VendorsBean {
	
	
	private Integer Id;
	
	private String vendorId;

	private String name;

	private String type;

	private String country;
	
	private String status;
	
	private String phoneNumber;
	
	private String email;

	private String circle;

	private String contactName;
	
	
	

	public String getVendorId() {
		return vendorId;
	}

	public void setVendorId(String vendorId) {
		this.vendorId = vendorId;
	}

	public VendorsBean(MstVendor vendors) {
		this.Id=vendors.getId();
		this.name=vendors.getName();
		this.country=vendors.getCountry();
		this.email=vendors.getEmail();
		this.phoneNumber=vendors.getPhoneNumber();
		this.type=vendors.getType();
		this.circle=vendors.getCircle();
		this.vendorId=vendors.getVendorId();
		this.contactName=vendors.getContactName();
	}
	
	public VendorsBean() {
		
	}

	public Integer getId() {
		return Id;
	}

	public void setId(Integer id) {
		Id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}


	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getCircle() {
		return circle;
	}

	public void setCircle(String circle) {
		this.circle = circle;
	}
	

}

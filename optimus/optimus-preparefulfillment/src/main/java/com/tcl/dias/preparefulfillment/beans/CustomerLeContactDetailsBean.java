package com.tcl.dias.preparefulfillment.beans;

import java.io.Serializable;

import java.util.List;

public class CustomerLeContactDetailsBean implements Serializable{
	private String customerLeName;
	private List<CustomerLeContactBean> contacts;
	public String getCustomerLeName() {
		return customerLeName;
	}
	public void setCustomerLeName(String customerLeName) {
		this.customerLeName = customerLeName;
	}
	public List<CustomerLeContactBean> getContacts() {
		return contacts;
	}
	public void setContacts(List<CustomerLeContactBean> contacts) {
		this.contacts = contacts;
	}
	
}

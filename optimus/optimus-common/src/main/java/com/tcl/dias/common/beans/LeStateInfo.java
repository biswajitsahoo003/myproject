package com.tcl.dias.common.beans;

import java.io.Serializable;

/**
 * This file contains the LeStateInfo.java class.
 * 
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class LeStateInfo implements Serializable{
	


	/**
	 * 
	 */
	private static final long serialVersionUID = 4788860266940102043L;


	private Integer id;


	private String gstNo;

	private String state;
	
	private String address;
	
	private String addresslineOne;
	
	private String addresslineTwo;
	
	private String addresslineThree;
	
	private String city;
	
	private String pincode;
	
	private String country;
	
	

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return the gstNo
	 */
	public String getGstNo() {
		return gstNo;
	}

	/**
	 * @param gstNo the gstNo to set
	 */
	public void setGstNo(String gstNo) {
		this.gstNo = gstNo;
	}

	/**
	 * @return the state
	 */
	public String getState() {
		return state;
	}

	/**
	 * @param state the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}

	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
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

	@Override
	public String toString() {
		return "LeStateInfo [id=" + id + ", gstNo=" + gstNo + ", state=" + state + ", address=" + address
				+ ", addresslineOne=" + addresslineOne + ", addresslineTwo=" + addresslineTwo + ", addresslineThree="
				+ addresslineThree + ", city=" + city + ", pincode=" + pincode + ", country=" + country + "]";
	}

	
}

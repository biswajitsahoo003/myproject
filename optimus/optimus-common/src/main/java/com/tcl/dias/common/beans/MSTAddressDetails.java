package com.tcl.dias.common.beans;
/**
 * 
 * This file contains the MSTAddressDetails bean
 * 
 *
 * @author ANANDHI VIJAY
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class MSTAddressDetails {
	
	private String address_Line_One;
	private String pinCode;
	private String locality;
	private String country;
	private Integer customer_Le_Id;
	private String city;
	private String state;
	private Integer location_Le_Id;
	private Integer country_To_Region_Id;
	
	
	
	public Integer getCountry_To_Region_Id() {
		return country_To_Region_Id;
	}
	public void setCountry_To_Region_Id(Integer country_To_Region_Id) {
		this.country_To_Region_Id = country_To_Region_Id;
	}
	public String getAddress_Line_One() {
		return address_Line_One;
	}
	public void setAddress_Line_One(String address_Line_One) {
		this.address_Line_One = address_Line_One;
	}
	
	public String getPinCode() {
		return pinCode;
	}
	public void setPinCode(String pinCode) {
		this.pinCode = pinCode;
	}
	public String getLocality() {
		return locality;
	}
	public void setLocality(String locality) {
		this.locality = locality;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public Integer getCustomer_Le_Id() {
		return customer_Le_Id;
	}
	public void setCustomer_Le_Id(Integer customer_Le_Id) {
		this.customer_Le_Id = customer_Le_Id;
	}
	public Integer getLocation_Le_Id() {
		return location_Le_Id;
	}
	public void setLocation_Le_Id(Integer location_Le_Id) {
		this.location_Le_Id = location_Le_Id;
	}
	
	
	

}

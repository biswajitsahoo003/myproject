package com.tcl.dias.location.beans;

public class CustomAddressBean {
	private String country;
	private String latitude;
	private String longitude;
	private String city;
	private String pincode;
	private String locality;
	private String address_line_one;
	private String remarks;
	private String source;
	private String created_by;
	private Integer customerId;
	private Integer customerLeId;
	private String lat_long;
	private String state;
	private Integer id;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
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

	public String getLocality() {
		return locality;
	}

	public void setLocality(String locality) {
		this.locality = locality;
	}

	public String getAddress_line_one() {
		return address_line_one;
	}

	public void setAddress_line_one(String address_line_one) {
		this.address_line_one = address_line_one;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getCreated_by() {
		return created_by;
	}

	public void setCreated_by(String created_by) {
		this.created_by = created_by;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public Integer getCustomerLeId() {
		return customerLeId;
	}

	public void setCustomerLeId(Integer customerLeId) {
		this.customerLeId = customerLeId;
	}

	public String getLat_long() {
		return lat_long;
	}

	public void setLat_long(String lat_long) {
		this.lat_long = lat_long;
	}
}

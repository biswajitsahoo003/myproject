package com.tcl.dias.serviceinventory.beans;

/**
 * 
 * @author Manojkumar R
 *
 */
public class CustomerOrderDetailBean {

	private String siteAddress;
	private String latLong;
	private String city;
	private String customerLeName;
	private Integer customerLeId;
	private String productName;
	private String serviceId;
	private String status;
	private String productShortName;
	private String siteAlias;
	

	public String getProductShortName() {
		return productShortName;
	}

	public void setProductShortName(String productShortName) {
		this.productShortName = productShortName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSiteAddress() {
		return siteAddress;
	}

	public void setSiteAddress(String siteAddress) {
		this.siteAddress = siteAddress;
	}

	public String getLatLong() {
		return latLong;
	}

	public void setLatLong(String latLong) {
		this.latLong = latLong;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCustomerLeName() {
		return customerLeName;
	}

	public void setCustomerLeName(String customerLeName) {
		this.customerLeName = customerLeName;
	}

	public Integer getCustomerLeId() {
		return customerLeId;
	}

	public void setCustomerLeId(Integer customerLeId) {
		this.customerLeId = customerLeId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getSiteAlias() {
		return siteAlias;
	}

	public void setSiteAlias(String siteAlias) {
		this.siteAlias = siteAlias;
	}

	@Override
	public String toString() {
		return "CustomerOrderDetailBean [siteAddress=" + siteAddress + ", latLong=" + latLong + ", city=" + city
				+ ", customerLeName=" + customerLeName + ", customerLeId=" + customerLeId + ", productName="
				+ productName + ", serviceId=" + serviceId + ", status=" + status + ", productShortName="
				+ productShortName + ", siteAlias=" + siteAlias + "]";
	}
	
	
	

	
}

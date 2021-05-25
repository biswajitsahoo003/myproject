package com.tcl.dias.serviceinventory.beans;

import java.util.Objects;
import java.util.stream.Collectors;

import com.tcl.dias.serviceinventory.entity.entities.SIServiceDetail;

/**
 * This file contains the CustomerServiceDetailDto.java class this class is used
 * for send response to dashboard of Customer portal .
 *
 *
 * @author Biswajit
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class CustomerServiceDetailBean {
	private String siteAddress;
	private String siteLatLng;
	private String serviceId;
	private String productName;
	private String billingType;
	private String city;
	private String cityLatLng;
	private String status;

	public String getCityLatLng() {
		return cityLatLng;
	}
	public void setCityLatLng(String cityLatLng) {
		this.cityLatLng = cityLatLng;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getSiteAddress() {
		return siteAddress;
	}

	public void setSiteAddress(String siteAddress) {
		this.siteAddress = siteAddress;
	}

	public String getSiteLatLng() {
		return siteLatLng;
	}

	public void setSiteLatLng(String siteLatLng) {
		this.siteLatLng = siteLatLng;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	public String getBillingType() {
		return billingType;
	}

	public void setBillingType(String billingType) {
		this.billingType = billingType;
	}
	public CustomerServiceDetailBean(SIServiceDetail entity) {
		if (Objects.nonNull(entity)) {
			this.serviceId = entity.getTpsServiceId();
			this.siteAddress = entity.getSiteAddress();
			this.productName = entity.getErfPrdCatalogProductName();
			this.billingType=entity.getBillingType();
			this.status=entity.getServiceStatus();
			this.city=entity.getSourceCity();
			if(entity.getLatLong()!=null) {
				this.cityLatLng=entity.getLatLong();
				this.siteLatLng=entity.getLatLong();
			}
			
			
		}
	}
}

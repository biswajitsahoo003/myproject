package com.tcl.dias.servicefulfillmentutils.beans;

import java.sql.Timestamp;

import javax.persistence.Column;

public class CpeCostDetailBean {

	private Integer id;
	
	private String oem;

	private String bundledBom;
	
	private String materialCode;

	private String serviceNumber;
	
	private String shortText;
	
	private String productCode;
	
	private String description;
	
	private Integer quantity;
	
	private Double calculatedPrice;
	
	private Integer serviceId;
	
	private String serviceCode;
	
	private Integer componentId;

	private Timestamp createdDate;
		
	private Integer vendorId;
	
	private String vendorCode;
	
	private String currency;
	
	private String category;
	
	private String hsnCode;
	
	private Double totalTPinINR;
	
	private Double fxSpotRate;
	
	private String poNumber;
	
	private String serialNumber;


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getOem() {
		return oem;
	}

	public void setOem(String oem) {
		this.oem = oem;
	}

	public String getBundledBom() {
		return bundledBom;
	}

	public void setBundledBom(String bundledBom) {
		this.bundledBom = bundledBom;
	}

	public String getMaterialCode() {
		return materialCode;
	}

	public void setMaterialCode(String materialCode) {
		this.materialCode = materialCode;
	}

	public String getServiceNumber() {
		return serviceNumber;
	}

	public void setServiceNumber(String serviceNumber) {
		this.serviceNumber = serviceNumber;
	}

	public String getShortText() {
		return shortText;
	}

	public void setShortText(String shortText) {
		this.shortText = shortText;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Double getCalculatedPrice() {
		return calculatedPrice;
	}

	public void setCalculatedPrice(Double calculatedPrice) {
		this.calculatedPrice = calculatedPrice;
	}

	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}

	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	public Integer getComponentId() {
		return componentId;
	}

	public void setComponentId(Integer componentId) {
		this.componentId = componentId;
	}

	public Timestamp getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	public Integer getVendorId() {
		return vendorId;
	}

	public void setVendorId(Integer vendorId) {
		this.vendorId = vendorId;
	}

	public String getVendorCode() {
		return vendorCode;
	}

	public void setVendorCode(String vendorCode) {
		this.vendorCode = vendorCode;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getHsnCode() {
		return hsnCode;
	}

	public void setHsnCode(String hsnCode) {
		this.hsnCode = hsnCode;
	}

	public Double getTotalTPinINR() {
		return totalTPinINR;
	}

	public void setTotalTPinINR(Double totalTPinINR) {
		this.totalTPinINR = totalTPinINR;
	}
	
	public Double getFxSpotRate() {
		return fxSpotRate;
	}

	public void setFxSpotRate(Double fxSpotRate) {
		this.fxSpotRate = fxSpotRate;
	}
	
	public String getPoNumber() {
		return poNumber;
	}

	public void setPoNumber(String poNumber) {
		this.poNumber = poNumber;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}
}

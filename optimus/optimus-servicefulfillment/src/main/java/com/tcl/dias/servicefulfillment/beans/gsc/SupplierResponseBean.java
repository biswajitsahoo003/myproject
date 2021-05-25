package com.tcl.dias.servicefulfillment.beans.gsc;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

public class SupplierResponseBean {
	
	private Integer outpulseNumberId;
	
	private String outpulseNumber;
	
	private Integer routingNumberId;

	private String routingNumber;
	
	private Integer tollFreeNumberId;

	private String tollFreeNumber;

	private String supplierId;
	
	private String supplierName;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private String activationDate;

	public Integer getOutpulseNumberId() {
		return outpulseNumberId;
	}

	public Integer getRoutingNumberId() {
		return routingNumberId;
	}

	public Integer getTollFreeNumberId() {
		return tollFreeNumberId;
	}

	public void setOutpulseNumberId(Integer outpulseNumberId) {
		this.outpulseNumberId = outpulseNumberId;
	}

	public void setRoutingNumberId(Integer routingNumberId) {
		this.routingNumberId = routingNumberId;
	}

	public void setTollFreeNumberId(Integer tollFreeNumberId) {
		this.tollFreeNumberId = tollFreeNumberId;
	}

	public String getRoutingNumber() {
		return routingNumber;
	}

	public String getTollFreeNumber() {
		return tollFreeNumber;
	}

	public String getSupplierId() {
		return supplierId;
	}

	public String getActivationDate() {
		return activationDate;
	}

	public String getOutpulseNumber() {
		return outpulseNumber;
	}

	public void setRoutingNumber(String routingNumber) {
		this.routingNumber = routingNumber;
	}

	public void setTollFreeNumber(String tollFreeNumber) {
		this.tollFreeNumber = tollFreeNumber;
	}

	public void setSupplierId(String supplierId) {
		this.supplierId = supplierId;
	}

	public void setActivationDate(String activationDate) {
		this.activationDate = activationDate;
	}

	public void setOutpulseNumber(String outpulseNumber) {
		this.outpulseNumber = outpulseNumber;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}
}

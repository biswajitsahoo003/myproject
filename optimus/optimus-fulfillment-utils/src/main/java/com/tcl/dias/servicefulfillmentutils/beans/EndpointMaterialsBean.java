package com.tcl.dias.servicefulfillmentutils.beans;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

public class EndpointMaterialsBean {
	
	private Integer id;

	private String serialNumber;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private String deliveryDate;
	
	private String endOfSale;
	
	private String endOfLife;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private String amcStartDate;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private String amcEndDate;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public String getDeliveryDate() {
		return deliveryDate;
	}

	public String getEndOfSale() {
		return endOfSale;
	}

	public String getEndOfLife() {
		return endOfLife;
	}

	public String getAmcStartDate() {
		return amcStartDate;
	}

	public String getAmcEndDate() {
		return amcEndDate;
	}

	public void setDeliveryDate(String deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public void setEndOfSale(String endOfSale) {
		this.endOfSale = endOfSale;
	}

	public void setEndOfLife(String endOfLife) {
		this.endOfLife = endOfLife;
	}

	public void setAmcStartDate(String amcStartDate) {
		this.amcStartDate = amcStartDate;
	}

	public void setAmcEndDate(String amcEndDate) {
		this.amcEndDate = amcEndDate;
	}
}

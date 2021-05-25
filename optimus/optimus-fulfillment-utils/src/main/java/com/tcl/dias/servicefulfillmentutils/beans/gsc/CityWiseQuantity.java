package com.tcl.dias.servicefulfillmentutils.beans.gsc;

public class CityWiseQuantity {
	
	private String cityName;
	private String cityCode;
	private Integer quantity;
	private String npa;

	public String getNpa() {
		return npa;
	}

	public void setNpa(String npa) {
		this.npa = npa;
	}

	public String getCityName() {
		return cityName;
	}

	public String getCityCode() {
		return cityCode;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
}

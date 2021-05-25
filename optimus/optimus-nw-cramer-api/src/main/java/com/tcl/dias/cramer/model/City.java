package com.tcl.dias.cramer.model;

public class City {
	String cityCode;
	String cityName;
	public City(String cityCode, String cityName) {
		super();
		this.cityCode = cityCode;
		this.cityName = cityName;
	}
	public String getCityCode() {
		return cityCode;
	}
	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	
}

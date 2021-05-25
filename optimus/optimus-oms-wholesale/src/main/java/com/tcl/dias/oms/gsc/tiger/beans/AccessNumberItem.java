package com.tcl.dias.oms.gsc.tiger.beans;

import java.util.List;

public class AccessNumberItem {
	private String numberItemId;
	private String originCountryAbbr;
	private String originCityAbbr;
	private String supplierID;
	private List<Number> numbers;

	public String getNumberItemId() {
		return numberItemId;
	}

	public void setNumberItemId(String numberItemId) {
		this.numberItemId = numberItemId;
	}

	public String getOriginCountryAbbr() {
		return originCountryAbbr;
	}

	public void setOriginCountryAbbr(String originCountryAbbr) {
		this.originCountryAbbr = originCountryAbbr;
	}

	public String getSupplierID() {
		return supplierID;
	}

	public void setSupplierID(String supplierID) {
		this.supplierID = supplierID;
	}

	public String getOriginCityAbbr() {
		return originCityAbbr;
	}

	public void setOriginCityAbbr(String originCityAbbr) {
		this.originCityAbbr = originCityAbbr;
	}

	public List<Number> getNumbers() {
		return numbers;
	}

	public void setNumbers(List<Number> numbers) {
		this.numbers = numbers;
	}
}

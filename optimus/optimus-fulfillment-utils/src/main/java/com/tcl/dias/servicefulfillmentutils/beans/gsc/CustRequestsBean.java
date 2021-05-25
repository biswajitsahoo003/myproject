package com.tcl.dias.servicefulfillmentutils.beans.gsc;

import java.util.List;

public class CustRequestsBean {

	private String originCountryAbbr;
	private String originCityAbbr;
	private String endCustomerName;
	private List<NumbersBean> numbers;

	public String getOriginCountryAbbr() {
		return originCountryAbbr;
	}

	public void setOriginCountryAbbr(String originCountryAbbr) {
		this.originCountryAbbr = originCountryAbbr;
	}

	public String getOriginCityAbbr() {
		return originCityAbbr;
	}

	public void setOriginCityAbbr(String originCityAbbr) {
		this.originCityAbbr = originCityAbbr;
	}

	public String getEndCustomerName() {
		return endCustomerName;
	}

	public void setEndCustomerName(String endCustomerName) {
		this.endCustomerName = endCustomerName;
	}

	public List<NumbersBean> getNumbers() {
		return numbers;
	}

	public void setNumbers(List<NumbersBean> numbers) {
		this.numbers = numbers;
	}
}

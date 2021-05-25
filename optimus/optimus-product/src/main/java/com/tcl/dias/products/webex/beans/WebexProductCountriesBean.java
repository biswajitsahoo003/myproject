package com.tcl.dias.products.webex.beans;

import java.util.Set;

/**
 * Countries for Webex Products
 *
 * @author ssyedali
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
public class WebexProductCountriesBean {
	private Set<WebexProductLocationDetailBean> countries;

	public Set<WebexProductLocationDetailBean> getCountries() {
		return countries;
	}

	public void setCountries(Set<WebexProductLocationDetailBean> countries) {
		this.countries = countries;
	}

}

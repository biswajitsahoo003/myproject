package com.tcl.dias.products.teamsdr.beans;

/**
 * City detail bean for Teamsdr.
 *
 * @author Syed Ali
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
public class TeamsDRCityDetailBean {
	private String code;
	private String country;
	private String city;

	public TeamsDRCityDetailBean() {
	}

	public TeamsDRCityDetailBean(String code, String country, String city) {
		this.code = code;
		this.country = country;
		this.city = city;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	@Override
	public String toString() {
		return "TeamsDRCityDetailBean{" + "code='" + code + '\'' + ", country='" + country + '\'' + ", city='" + city
				+ '\'' + '}';
	}
}

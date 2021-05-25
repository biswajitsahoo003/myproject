package com.tcl.dias.productcatelog.entity.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * The persistent class for the vw_mstmdr_license_country table
 *
 * @author Syed Ali
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
@Entity
@Table(name = "vw_mstmdr_license_country")
@NamedQuery(name = "TeamsDRLicenseCountry.findAll", query = "SELECT v FROM TeamsDRLicenseCountry v")
public class TeamsDRLicenseCountry {
	private static final long serialVersionUID = 1L;

	@Column(name = "disp_nm")
	private String name;

	@Column(name = "country_cd")
	private String countryCode;

	@Column(name = "iso_3_cd")
	private String isoCode;

	@Id
	@Column(name = "country")
	private String country;

	public TeamsDRLicenseCountry() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getIsoCode() {
		return isoCode;
	}

	public void setIsoCode(String isoCode) {
		this.isoCode = isoCode;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	@Override
	public String toString() {
		return "TeamsDRLicenseCountry{" +
				"name='" + name + '\'' +
				", countryCode='" + countryCode + '\'' +
				", isoCode='" + isoCode + '\'' +
				", country='" + country + '\'' +
				'}';
	}
}

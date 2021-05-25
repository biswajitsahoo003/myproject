package com.tcl.dias.productcatelog.entity.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * The persistent class for the vw_mstmdr_ctry_city database table.
 *
 * @author Syed Ali
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
@Entity
@Table(name = "vw_mstmdr_ctry_city")
@NamedQuery(name = "ServiceAreaMatrixTeamsDRCity.findAll", query = "SELECT w FROM ServiceAreaMatrixTeamsDRCity w")
public class ServiceAreaMatrixTeamsDRCity {
	private static final long serialVersionUID = 1L;

	@Id
	private String uid;

	@Column(name = "Iso_3_Ctry_Cd")
	private String countryCode;

	@Column(name = "Iso_Ctry_Name")
	private String countryName;

	@Column(name = "City_Name")
	private String cityName;

	public ServiceAreaMatrixTeamsDRCity() {
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
}

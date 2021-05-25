package com.tcl.dias.productcatelog.entity.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * The persistent class for the vw_ucaas_lns_all_ctry_price_book database table.
 *
 * @author Syed Ali
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
@Entity
@Table(name = "vw_mstmdr_country_matrix")
@NamedQuery(name = "ServiceAreaMatrixTeamsDR.findAll", query = "SELECT w FROM ServiceAreaMatrixTeamsDR w")
public class ServiceAreaMatrixTeamsDR {
	private static final long serialVersionUID = 1L;

	@Id
	private String uid;

	@Column(name = "country")
	private String country;

	@Column(name = "cd")
	private String code;

	@Column(name = "iso_3_cd")
	private String countryIsoCode;

	@Column(name = "isdCode")
	private String isdCode;

	@Column(name = "is_gsc")
	private String isGsc;

	@Column(name = "is_regulated")
	private String isRegulated;

	@Column(name = "is_exception")
	private String isException;

	@Column(name = "is_lns")
	private String isLns;

	@Column(name = "is_itfs")
	private String isItfs;

	@Column(name = "is_ob")
	private String isOutbound;

	@Column(name = "is_dv")
	private String isDomesticVoice;

	@Column(name = "is_gob")
	private String isGlobalOutbound;

	public ServiceAreaMatrixTeamsDR() {
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

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCountryIsoCode() {
		return countryIsoCode;
	}

	public void setCountryIsoCode(String countryIsoCode) {
		this.countryIsoCode = countryIsoCode;
	}

	public String getIsGsc() {
		return isGsc;
	}

	public void setIsGsc(String isGsc) {
		this.isGsc = isGsc;
	}

	public String getIsRegulated() {
		return isRegulated;
	}

	public void setIsRegulated(String isRegulated) {
		this.isRegulated = isRegulated;
	}

	public String getIsException() {
		return isException;
	}

	public void setIsException(String isException) {
		this.isException = isException;
	}

	public String getIsLns() {
		return isLns;
	}

	public void setIsLns(String isLns) {
		this.isLns = isLns;
	}

	public String getIsItfs() {
		return isItfs;
	}

	public void setIsItfs(String isItfs) {
		this.isItfs = isItfs;
	}

	public String getIsOutbound() {
		return isOutbound;
	}

	public void setIsOutbound(String isOutbound) {
		this.isOutbound = isOutbound;
	}

	public String getIsdCode() {
		return isdCode;
	}

	public void setIsdCode(String isdCode) {
		this.isdCode = isdCode;
	}

	public String getIsDomesticVoice() {
		return isDomesticVoice;
	}

	public void setIsDomesticVoice(String isDomesticVoice) {
		this.isDomesticVoice = isDomesticVoice;
	}

	public String getIsGlobalOutbound() {
		return isGlobalOutbound;
	}

	public void setIsGlobalOutbound(String isGlobalOutbound) {
		this.isGlobalOutbound = isGlobalOutbound;
	}

	@Override
	public String toString() {
		return "ServiceAreaMatrixTeamsDR{" +
				"uid='" + uid + '\'' +
				", country='" + country + '\'' +
				", code='" + code + '\'' +
				", countryIsoCode='" + countryIsoCode + '\'' +
				", isdCode='" + isdCode + '\'' +
				", isGsc='" + isGsc + '\'' +
				", isRegulated='" + isRegulated + '\'' +
				", isException='" + isException + '\'' +
				", isLns='" + isLns + '\'' +
				", isItfs='" + isItfs + '\'' +
				", isOutbound='" + isOutbound + '\'' +
				", isDomesticVoice='" + isDomesticVoice + '\'' +
				", isGlobalOutbound='" + isGlobalOutbound + '\'' +
				'}';
	}
}

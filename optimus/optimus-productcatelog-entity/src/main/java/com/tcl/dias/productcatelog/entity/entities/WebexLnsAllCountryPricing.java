package com.tcl.dias.productcatelog.entity.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * The persistent class for the vw_ucaas_lns_all_ctry_price_book database table.
 * 
 * @author Srinivasa Raghavan
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@Entity
@Table(name = "vw_ucaas_lns_all_ctry_price_book")
@NamedQuery(name = "WebexLnsAllCountryPricing.findAll", query = "SELECT w FROM WebexLnsAllCountryPricing w")
public class WebexLnsAllCountryPricing implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name = "country")
	private String country;

	@Column(name = "ctry_iso3_cd")
	private String ctryIso3Cd;

	@Column(name = "is_packaged_ind")
	private String isPackagedInd;

	@Id
	@Column(name = "isd_code")
	private String isdCode;

	@Column(name = "mrc")
	private Double mrc;

	@Column(name = "nrc")
	private Double nrc;

	@Column(name = "rate_per_min")
	private Double ratePerMin;

	public WebexLnsAllCountryPricing() {
	}

	public String getCountry() {
		return this.country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCtryIso3Cd() {
		return this.ctryIso3Cd;
	}

	public void setCtryIso3Cd(String ctryIso3Cd) {
		this.ctryIso3Cd = ctryIso3Cd;
	}

	public String getIsPackagedInd() {
		return this.isPackagedInd;
	}

	public void setIsPackagedInd(String isPackagedInd) {
		this.isPackagedInd = isPackagedInd;
	}

	public String getIsdCode() {
		return this.isdCode;
	}

	public void setIsdCode(String isdCode) {
		this.isdCode = isdCode;
	}

	public Double getMrc() {
		return this.mrc;
	}

	public void setMrc(Double mrc) {
		this.mrc = mrc;
	}

	public Double getNrc() {
		return this.nrc;
	}

	public void setNrc(Double nrc) {
		this.nrc = nrc;
	}

	public Double getRatePerMin() {
		return this.ratePerMin;
	}

	public void setRatePerMin(Double ratePerMin) {
		this.ratePerMin = ratePerMin;
	}

}
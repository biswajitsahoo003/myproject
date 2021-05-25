package com.tcl.dias.productcatelog.entity.entities;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * The persistent class for the vw_ucaas_itfs_dedicated_all_ctry_price_book
 * database table.
 * 
 * @author srraghav
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@Entity
@Table(name = "vw_ucaas_itfs_dedicated_all_ctry_price_book")
@NamedQuery(name = "WebexItfsDedicatedAllCountryPricing.findAll", query = "SELECT w FROM WebexItfsDedicatedAllCountryPricing w")
public class WebexItfsDedicatedAllCountryPricing implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String uid;

	@Column(name = "implement_fee_per_number_nrc")
	private BigDecimal implementFeePerNumberNrc;

	@Column(name = "origin_country")
	private String originCountry;

	@Column(name = "origin_country_iso_3_cd")
	private String originCountryIso3Cd;

	@Column(name = "phone_type")
	private String phoneType;

	@Column(name = "rate_per_min")
	private Double ratePerMin;

	@Column(name = "recurring_fee_per_number_mrc")
	private BigDecimal recurringFeePerNumberMrc;

	public WebexItfsDedicatedAllCountryPricing() {
	}

	public BigDecimal getImplementFeePerNumberNrc() {
		return this.implementFeePerNumberNrc;
	}

	public void setImplementFeePerNumberNrc(BigDecimal implementFeePerNumberNrc) {
		this.implementFeePerNumberNrc = implementFeePerNumberNrc;
	}

	public String getOriginCountry() {
		return this.originCountry;
	}

	public void setOriginCountry(String originCountry) {
		this.originCountry = originCountry;
	}

	public String getOriginCountryIso3Cd() {
		return this.originCountryIso3Cd;
	}

	public void setOriginCountryIso3Cd(String originCountryIso3Cd) {
		this.originCountryIso3Cd = originCountryIso3Cd;
	}

	public String getPhoneType() {
		return this.phoneType;
	}

	public void setPhoneType(String phoneType) {
		this.phoneType = phoneType;
	}

	public Double getRatePerMin() {
		return this.ratePerMin;
	}

	public void setRatePerMin(Double ratePerMin) {
		this.ratePerMin = ratePerMin;
	}

	public BigDecimal getRecurringFeePerNumberMrc() {
		return this.recurringFeePerNumberMrc;
	}

	public void setRecurringFeePerNumberMrc(BigDecimal recurringFeePerNumberMrc) {
		this.recurringFeePerNumberMrc = recurringFeePerNumberMrc;
	}

}
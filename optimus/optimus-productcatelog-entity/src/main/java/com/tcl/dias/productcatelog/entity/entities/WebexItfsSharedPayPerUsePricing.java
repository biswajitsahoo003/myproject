package com.tcl.dias.productcatelog.entity.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * The persistent class for the vw_ucaas_itfs_shared_pay_per_use_price_book
 * database table.
 *
 * @author Syed Ali
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
@Entity
@Table(name="vw_ucaas_itfs_shared_pay_per_use_price_book")
@NamedQuery(name="WebexItfsSharedPayPerUsePricing.findAll", query="SELECT v FROM WebexItfsSharedPayPerUsePricing v")
public class WebexItfsSharedPayPerUsePricing implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String uid;

	@Column(name="implement_fee_per_number_nrc")
	private String implementFeePerNumberNrc;

	@Column(name="origin_country")
	private String originCountry;

	@Column(name="origin_country_iso_3_cd")
	private String originCountryIso3Cd;

	@Column(name="phone_type")
	private String phoneType;

	@Column(name="rate_per_min")
	private double ratePerMin;

	@Column(name="recurring_fee_per_number_mrc")
	private String recurringFeePerNumberMrc;

	public WebexItfsSharedPayPerUsePricing() {
	}

	public String getImplementFeePerNumberNrc() {
		return this.implementFeePerNumberNrc;
	}

	public void setImplementFeePerNumberNrc(String implementFeePerNumberNrc) {
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

	public double getRatePerMin() {
		return this.ratePerMin;
	}

	public void setRatePerMin(double ratePerMin) {
		this.ratePerMin = ratePerMin;
	}

	public String getRecurringFeePerNumberMrc() {
		return this.recurringFeePerNumberMrc;
	}

	public void setRecurringFeePerNumberMrc(String recurringFeePerNumberMrc) {
		this.recurringFeePerNumberMrc = recurringFeePerNumberMrc;
	}

}
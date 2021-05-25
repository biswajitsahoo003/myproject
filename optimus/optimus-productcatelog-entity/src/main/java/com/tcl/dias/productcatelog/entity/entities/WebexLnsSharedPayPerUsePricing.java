package com.tcl.dias.productcatelog.entity.entities;


import java.io.Serializable;
import javax.persistence.*;
import java.math.BigInteger;


/**
 * The persistent class for the vw_ucaas_lns_shared_pay_per_use_price_book database table.
 *
 * @author ssyedali
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
@Entity
@Table(name="vw_ucaas_lns_shared_pay_per_use_price_book")
@NamedQuery(name="WebexLnsSharedPayPerUsePricing.findAll", query="SELECT v FROM WebexLnsSharedPayPerUsePricing v")
public class WebexLnsSharedPayPerUsePricing implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String uid;

	private String country;

	@Column(name="ctry_iso3_cd")
	private String ctryIso3Cd;

	@Column(name="is_packaged_ind")
	private String isPackagedInd;

	@Column(name="isd_code")
	private String isdCode;

	private BigInteger mrc;

	private BigInteger nrc;

	@Column(name="rate_per_min")
	private Double ratePerMin;

	public WebexLnsSharedPayPerUsePricing() {
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

	public BigInteger getMrc() {
		return this.mrc;
	}

	public void setMrc(BigInteger mrc) {
		this.mrc = mrc;
	}

	public BigInteger getNrc() {
		return this.nrc;
	}

	public void setNrc(BigInteger nrc) {
		this.nrc = nrc;
	}

	public Double getRatePerMin() {
		return this.ratePerMin;
	}

	public void setRatePerMin(Double ratePerMin) {
		this.ratePerMin = ratePerMin;
	}

}
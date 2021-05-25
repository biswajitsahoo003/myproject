package com.tcl.dias.productcatelog.entity.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * The persistent class for the vw_ucaas_ob_shared_pay_per_seat_price_book
 * database table.
 * 
 * @author Srinivasa Raghavan
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@Entity
@Table(name = "vw_ucaas_ob_shared_pay_per_seat_price_book")
@NamedQuery(name = "WebexOutboundSharedPayPerSeatPricing.findAll", query = "SELECT w FROM WebexOutboundSharedPayPerSeatPricing w")
public class WebexOutboundSharedPayPerSeatPricing implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String uid;

	@Column(name = "country")
	private String country;

	@Column(name = "ctry_cd_iso_3")
	private String ctryCdIso3;

	@Column(name = "currency_cd")
	private String currencyCd;

	@Column(name = "dest_id")
	private Integer destId;

	@Column(name = "destination_name")
	private String destinationName;

	@Column(name = "is_packaged_ind")
	private String isPackagedInd;

	@Column(name = "phone_type")
	private String phoneType;

	@Column(name = "rate_per_min")
	private Double ratePerMin;

	@Column(name = "region")
	private String region;

	@Column(name = "service_level")
	private String serviceLevel;

	public WebexOutboundSharedPayPerSeatPricing() {
	}

	public String getCountry() {
		return this.country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCtryCdIso3() {
		return this.ctryCdIso3;
	}

	public void setCtryCdIso3(String ctryCdIso3) {
		this.ctryCdIso3 = ctryCdIso3;
	}

	public String getCurrencyCd() {
		return this.currencyCd;
	}

	public void setCurrencyCd(String currencyCd) {
		this.currencyCd = currencyCd;
	}

	public Integer getDestId() {
		return this.destId;
	}

	public void setDestId(Integer destId) {
		this.destId = destId;
	}

	public String getDestinationName() {
		return this.destinationName;
	}

	public void setDestinationName(String destinationName) {
		this.destinationName = destinationName;
	}

	public String getIsPackagedInd() {
		return this.isPackagedInd;
	}

	public void setIsPackagedInd(String isPackagedInd) {
		this.isPackagedInd = isPackagedInd;
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

	public String getRegion() {
		return this.region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getServiceLevel() {
		return this.serviceLevel;
	}

	public void setServiceLevel(String serviceLevel) {
		this.serviceLevel = serviceLevel;
	}

}
package com.tcl.dias.productcatelog.entity.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * The persistent class for the vw_lns_dedicated_price_gsip_active database
 * table.
 * 
 * @author srraghav
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@Entity
@Table(name = "vw_lns_dedicated_price_gsip_active")
@NamedQuery(name = "GscLnsDedicatedPriceView.findAll", query = "SELECT v FROM GscLnsDedicatedPriceView v")
public class GscLnsDedicatedPriceView implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name="cda_floor_price")
	private double cdaFloorPrice;

	@Column(name="created_by")
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="created_dt")
	private Date createdDt;

	@Column(name="distributors_high_vol_price")
	private double distributorsHighVolPrice;

	@Column(name="distributors_low_vol_price")
	private double distributorsLowVolPrice;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="effective_from_dt")
	private Date effectiveFromDt;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="effective_to_dt")
	private Date effectiveToDt;

	@Column(name="enterprise_sales_floor")
	private double enterpriseSalesFloor;

	@Column(name="fee_per_number_mrc_usd")
	private BigDecimal feePerNumberMrcUsd;

	@Column(name="implement_fee_per_number_nrc")
	private BigDecimal implementFeePerNumberNrc;

	@Column(name="internal_comments")
	private String internalComments;

	@Column(name="is_active_ind")
	private String isActiveInd;

	@Column(name="is_portable")
	private String isPortable;

	@Column(name="list_price_usd")
	private float listPriceUsd;

	@Id
	@Column(name="lns_ev_code")
	private int lnsEvCode;

	@Column(name="origin_country")
	private String originCountry;

	@Column(name="origin_country_iso3_cd")
	private String originCountryIso3Cd;

	@Column(name="porting_charge_or_number_usd")
	private double portingChargeOrNumberUsd;

	@Column(name="reason_txt")
	private String reasonTxt;

	@Column(name="service_provider_floor")
	private double serviceProviderFloor;

	@Column(name="updated_by")
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="updated_dt")
	private Date updatedDt;

	public GscLnsDedicatedPriceView() {
	}

	public double getCdaFloorPrice() {
		return this.cdaFloorPrice;
	}

	public void setCdaFloorPrice(double cdaFloorPrice) {
		this.cdaFloorPrice = cdaFloorPrice;
	}

	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDt() {
		return this.createdDt;
	}

	public void setCreatedDt(Date createdDt) {
		this.createdDt = createdDt;
	}

	public double getDistributorsHighVolPrice() {
		return this.distributorsHighVolPrice;
	}

	public void setDistributorsHighVolPrice(double distributorsHighVolPrice) {
		this.distributorsHighVolPrice = distributorsHighVolPrice;
	}

	public double getDistributorsLowVolPrice() {
		return this.distributorsLowVolPrice;
	}

	public void setDistributorsLowVolPrice(double distributorsLowVolPrice) {
		this.distributorsLowVolPrice = distributorsLowVolPrice;
	}

	public Date getEffectiveFromDt() {
		return this.effectiveFromDt;
	}

	public void setEffectiveFromDt(Date effectiveFromDt) {
		this.effectiveFromDt = effectiveFromDt;
	}

	public Date getEffectiveToDt() {
		return this.effectiveToDt;
	}

	public void setEffectiveToDt(Date effectiveToDt) {
		this.effectiveToDt = effectiveToDt;
	}

	public double getEnterpriseSalesFloor() {
		return this.enterpriseSalesFloor;
	}

	public void setEnterpriseSalesFloor(double enterpriseSalesFloor) {
		this.enterpriseSalesFloor = enterpriseSalesFloor;
	}

	public BigDecimal getFeePerNumberMrcUsd() {
		return this.feePerNumberMrcUsd;
	}

	public void setFeePerNumberMrcUsd(BigDecimal feePerNumberMrcUsd) {
		this.feePerNumberMrcUsd = feePerNumberMrcUsd;
	}

	public BigDecimal getImplementFeePerNumberNrc() {
		return this.implementFeePerNumberNrc;
	}

	public void setImplementFeePerNumberNrc(BigDecimal implementFeePerNumberNrc) {
		this.implementFeePerNumberNrc = implementFeePerNumberNrc;
	}

	public String getInternalComments() {
		return this.internalComments;
	}

	public void setInternalComments(String internalComments) {
		this.internalComments = internalComments;
	}

	public String getIsActiveInd() {
		return this.isActiveInd;
	}

	public void setIsActiveInd(String isActiveInd) {
		this.isActiveInd = isActiveInd;
	}

	public String getIsPortable() {
		return this.isPortable;
	}

	public void setIsPortable(String isPortable) {
		this.isPortable = isPortable;
	}

	public float getListPriceUsd() {
		return this.listPriceUsd;
	}

	public void setListPriceUsd(float listPriceUsd) {
		this.listPriceUsd = listPriceUsd;
	}

	public int getLnsEvCode() {
		return this.lnsEvCode;
	}

	public void setLnsEvCode(int lnsEvCode) {
		this.lnsEvCode = lnsEvCode;
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

	public double getPortingChargeOrNumberUsd() {
		return this.portingChargeOrNumberUsd;
	}

	public void setPortingChargeOrNumberUsd(double portingChargeOrNumberUsd) {
		this.portingChargeOrNumberUsd = portingChargeOrNumberUsd;
	}

	public String getReasonTxt() {
		return this.reasonTxt;
	}

	public void setReasonTxt(String reasonTxt) {
		this.reasonTxt = reasonTxt;
	}

	public double getServiceProviderFloor() {
		return this.serviceProviderFloor;
	}

	public void setServiceProviderFloor(double serviceProviderFloor) {
		this.serviceProviderFloor = serviceProviderFloor;
	}

	public String getUpdatedBy() {
		return this.updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedDt() {
		return this.updatedDt;
	}

	public void setUpdatedDt(Date updatedDt) {
		this.updatedDt = updatedDt;
	}
}
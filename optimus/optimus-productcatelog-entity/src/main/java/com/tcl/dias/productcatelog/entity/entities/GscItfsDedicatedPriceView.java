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
 * The persistent class for the vw_itfs_dedicated_price_gsip_active database
 * table.
 * 
 * @author srraghav
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@Entity
@Table(name = "vw_itfs_dedicated_price_gsip")
@NamedQuery(name = "GscItfsDedicatedPriceView.findAll", query = "SELECT v FROM GscItfsDedicatedPriceView v")
public class GscItfsDedicatedPriceView implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name="cda_floor_price")
	private Double cdaFloorPrice;

	private String comments;

	@Column(name="created_by")
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="created_dt")
	private Date createdDt;

	@Column(name="distributors_high_vol_price")
	private Double distributorsHighVolPrice;

	@Column(name="distributors_low_vol_price")
	private Double distributorsLowVolPrice;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="effective_from_dt")
	private Date effectiveFromDt;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="effective_to_dt")
	private Date effectiveToDt;

	@Column(name="enterprise_sales_floor")
	private Double enterpriseSalesFloor;

	@Column(name="implement_fee_per_number_nrc")
	private BigDecimal implementFeePerNumberNrc;

	@Column(name="Integerernal_comments")
	private String IntegerernalComments;

	@Column(name="is_active_ind")
	private String isActiveInd;

	@Id
	@Column(name="itfs_ev_code")
	private Integer itfsEvCode;

	@Column(name="list_price_usd")
	private Float listPriceUsd;

	@Column(name="origin_country")
	private String originCountry;

	@Column(name="origin_country_iso_3_cd")
	private String originCountryIso3Cd;

	@Column(name="phone_type")
	private String phoneType;

	@Column(name="reason_txt")
	private String reasonTxt;

	@Column(name="recurring_fee_per_number_mrc")
	private BigDecimal recurringFeePerNumberMrc;

	@Column(name="service_provider_floor")
	private Double serviceProviderFloor;

	@Column(name="uifn_available")
	private String uifnAvailable;

	@Column(name="uifn_ev_code")
	private String uifnEvCode;

	@Column(name="updated_by")
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="updated_dt")
	private Date updatedDt;

	public GscItfsDedicatedPriceView() {
	}

	public Double getCdaFloorPrice() {
		return this.cdaFloorPrice;
	}

	public void setCdaFloorPrice(Double cdaFloorPrice) {
		this.cdaFloorPrice = cdaFloorPrice;
	}

	public String getComments() {
		return this.comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
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

	public Double getDistributorsHighVolPrice() {
		return this.distributorsHighVolPrice;
	}

	public void setDistributorsHighVolPrice(Double distributorsHighVolPrice) {
		this.distributorsHighVolPrice = distributorsHighVolPrice;
	}

	public Double getDistributorsLowVolPrice() {
		return this.distributorsLowVolPrice;
	}

	public void setDistributorsLowVolPrice(Double distributorsLowVolPrice) {
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

	public Double getEnterpriseSalesFloor() {
		return this.enterpriseSalesFloor;
	}

	public void setEnterpriseSalesFloor(Double enterpriseSalesFloor) {
		this.enterpriseSalesFloor = enterpriseSalesFloor;
	}

	public BigDecimal getImplementFeePerNumberNrc() {
		return this.implementFeePerNumberNrc;
	}

	public void setImplementFeePerNumberNrc(BigDecimal implementFeePerNumberNrc) {
		this.implementFeePerNumberNrc = implementFeePerNumberNrc;
	}

	public String getIntegerernalComments() {
		return this.IntegerernalComments;
	}

	public void setIntegerernalComments(String IntegerernalComments) {
		this.IntegerernalComments = IntegerernalComments;
	}

	public String getIsActiveInd() {
		return this.isActiveInd;
	}

	public void setIsActiveInd(String isActiveInd) {
		this.isActiveInd = isActiveInd;
	}

	public Integer getItfsEvCode() {
		return this.itfsEvCode;
	}

	public void setItfsEvCode(Integer itfsEvCode) {
		this.itfsEvCode = itfsEvCode;
	}

	public Float getListPriceUsd() {
		return this.listPriceUsd;
	}

	public void setListPriceUsd(Float listPriceUsd) {
		this.listPriceUsd = listPriceUsd;
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

	public String getReasonTxt() {
		return this.reasonTxt;
	}

	public void setReasonTxt(String reasonTxt) {
		this.reasonTxt = reasonTxt;
	}

	public BigDecimal getRecurringFeePerNumberMrc() {
		return this.recurringFeePerNumberMrc;
	}

	public void setRecurringFeePerNumberMrc(BigDecimal recurringFeePerNumberMrc) {
		this.recurringFeePerNumberMrc = recurringFeePerNumberMrc;
	}

	public Double getServiceProviderFloor() {
		return this.serviceProviderFloor;
	}

	public void setServiceProviderFloor(Double serviceProviderFloor) {
		this.serviceProviderFloor = serviceProviderFloor;
	}

	public String getUifnAvailable() {
		return this.uifnAvailable;
	}

	public void setUifnAvailable(String uifnAvailable) {
		this.uifnAvailable = uifnAvailable;
	}

	public String getUifnEvCode() {
		return this.uifnEvCode;
	}

	public void setUifnEvCode(String uifnEvCode) {
		this.uifnEvCode = uifnEvCode;
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
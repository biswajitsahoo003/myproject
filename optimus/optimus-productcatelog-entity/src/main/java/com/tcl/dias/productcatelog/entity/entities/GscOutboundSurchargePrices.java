package com.tcl.dias.productcatelog.entity.entities;

import java.math.BigInteger;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Entity class for vw_Intl_outbound_surcharges_price_gsip
 * 
 * @author AVALLAPI
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name = "vw_Intl_outbound_surcharges_price_gsip")
public class GscOutboundSurchargePrices {

	@Id
	@Column(name = "UID")
	private Integer uid;

	@Column(name = "obc_rgn_ctry")
	private String regionCountry;

	@Column(name = "obc_rgn_ctry_iso3_cd")
	private String regionCountryCode;

	@Column(name = "obc_rgn_dstn")
	private String regionCountryDestination;

	@Column(name = "dest_id")
	private Integer regionCountryDestId;

	@Column(name = "ext_rgn_nm")
	private String exteriorRegionName;

	@Column(name = "org_ctry_cd")
	private BigInteger originCountryCode;

	@Column(name = "org_ctry_nm")
	private String originCountryName;

	@Column(name = "org_ctry_nm_iso3_cd")
	private String originCountryNameCode;

	@Column(name = "surcharge")
	private String surcharge;

	@Column(name = "surcharge_amt")
	private Double surchargeAmount;

	@Column(name = "currency")
	private String currency;

	@Column(name = "is_active_ind")
	private String isActiveIndicator;

	@Column(name = "effective_from_dt")
	private Date effectiveFromDate;

	@Column(name = "effective_to_dt")
	private Date effectiveToDate;

	@Column(name = "reason_txt")
	private String reasonTxt;

	@Column(name = "created_by")
	private String createdBy;

	@Column(name = "created_dt")
	private Date createdDate;

	@Column(name = "updated_by")
	private String updatedBy;

	@Column(name = "updated_dt")
	private Date updatedDate;

	public Integer getUid() {
		return uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}

	public String getRegionCountry() {
		return regionCountry;
	}

	public void setRegionCountry(String regionCountry) {
		this.regionCountry = regionCountry;
	}

	public String getRegionCountryCode() {
		return regionCountryCode;
	}

	public void setRegionCountryCode(String regionCountryCode) {
		this.regionCountryCode = regionCountryCode;
	}

	public String getRegionCountryDestination() {
		return regionCountryDestination;
	}

	public void setRegionCountryDestination(String regionCountryDestination) {
		this.regionCountryDestination = regionCountryDestination;
	}

	public Integer getRegionCountryDestId() {
		return regionCountryDestId;
	}

	public void setRegionCountryDestId(Integer regionCountryDestId) {
		this.regionCountryDestId = regionCountryDestId;
	}

	public String getExteriorRegionName() {
		return exteriorRegionName;
	}

	public void setExteriorRegionName(String exteriorRegionName) {
		this.exteriorRegionName = exteriorRegionName;
	}

	public BigInteger getOriginCountryCode() {
		return originCountryCode;
	}

	public void setOriginCountryCode(BigInteger originCountryCode) {
		this.originCountryCode = originCountryCode;
	}

	public String getOriginCountryName() {
		return originCountryName;
	}

	public void setOriginCountryName(String originCountryName) {
		this.originCountryName = originCountryName;
	}

	public String getOriginCountryNameCode() {
		return originCountryNameCode;
	}

	public void setOriginCountryNameCode(String originCountryNameCode) {
		this.originCountryNameCode = originCountryNameCode;
	}

	public String getSurcharge() {
		return surcharge;
	}

	public void setSurcharge(String surcharge) {
		this.surcharge = surcharge;
	}

	public Double getSurchargeAmount() {
		return surchargeAmount;
	}

	public void setSurchargeAmount(Double surchargeAmount) {
		this.surchargeAmount = surchargeAmount;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getIsActiveIndicator() {
		return isActiveIndicator;
	}

	public void setIsActiveIndicator(String isActiveIndicator) {
		this.isActiveIndicator = isActiveIndicator;
	}

	public Date getEffectiveFromDate() {
		return effectiveFromDate;
	}

	public void setEffectiveFromDate(Date effectiveFromDate) {
		this.effectiveFromDate = effectiveFromDate;
	}

	public Date getEffectiveToDate() {
		return effectiveToDate;
	}

	public void setEffectiveToDate(Date effectiveToDate) {
		this.effectiveToDate = effectiveToDate;
	}

	public String getReasonTxt() {
		return reasonTxt;
	}

	public void setReasonTxt(String reasonTxt) {
		this.reasonTxt = reasonTxt;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

}

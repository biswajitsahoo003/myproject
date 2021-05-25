package com.tcl.dias.productcatelog.entity.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;

/**
 * Entity class for vw_intl_outbounding_price_gsip
 * 
 * @author AVALLAPI
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name = "vw_intl_outbounding_price_gsip")
@Immutable
@NamedQuery(name = "GscOutboundPricing.findAll", query = "SELECT v FROM GscOutboundPricing v")
public class GscOutboundPricing implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "UID")
	private Integer uID;

	@Column(name = "country")
	private String country;

	@Column(name = "ctry_cd_iso_3")
	private String countryCode;

	@Column(name = "phone_type")
	private String phoneType;

	@Column(name = "dest_id")
	private Integer destId;

	@Column(name = "destination_name")
	private String destinationName;

	@Column(name = "service_level")
	private String serviceLevel;

	@Column(name = "comments")
	private String comments;

	@Column(name = "region")
	private String region;

	@Column(name = "currency_cd")
	private String currencyCode;

	@Column(name = "cda_floor")
	private Double cdaFloor;

	@Column(name = "service_provider_floor")
	private Double serviceProviderFloor;

	@Column(name = "enterprise_sales_floor")
	private Double enterpriseSalesFloor;

	@Column(name = "highest_possible_obc_surcharge")
	private Double highestPossibleObcSurcharge;

	@Column(name = "internal_comments")
	private String internalComments;

	@Column(name = "is_active_ind")
	private String isActiveInd;

	@Column(name = "effective_from_dt")
	private Date effectiveFromDate;

	@Column(name = "effective_to_dt")
	private Date effectiveToDate;

	@Column(name = "reason_txt")
	private String reasonText;

	@Column(name = "created_by")
	private String createdBy;

	@Column(name = "created_dt")
	private Date createdDate;

	@Column(name = "updated_by")
	private String updatedBy;

	@Column(name = "updated_dt")
	private Date updatedDate;

	public Integer getuID() {
		return uID;
	}

	public void setuID(Integer uID) {
		this.uID = uID;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getPhoneType() {
		return phoneType;
	}

	public void setPhoneType(String phoneType) {
		this.phoneType = phoneType;
	}

	public Integer getDestId() {
		return destId;
	}

	public void setDestId(Integer destId) {
		this.destId = destId;
	}

	public String getDestinationName() {
		return destinationName;
	}

	public void setDestinationName(String destinationName) {
		this.destinationName = destinationName;
	}

	public String getServiceLevel() {
		return serviceLevel;
	}

	public void setServiceLevel(String serviceLevel) {
		this.serviceLevel = serviceLevel;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public Double getCdaFloor() {
		return cdaFloor;
	}

	public void setCdaFloor(Double cdaFloor) {
		this.cdaFloor = cdaFloor;
	}

	public Double getServiceProviderFloor() {
		return serviceProviderFloor;
	}

	public void setServiceProviderFloor(Double serviceProviderFloor) {
		this.serviceProviderFloor = serviceProviderFloor;
	}

	public Double getEnterpriseSalesFloor() {
		return enterpriseSalesFloor;
	}

	public void setEnterpriseSalesFloor(Double enterpriseSalesFloor) {
		this.enterpriseSalesFloor = enterpriseSalesFloor;
	}

	public Double getHighestPossibleObcSurcharge() {
		return highestPossibleObcSurcharge;
	}

	public void setHighestPossibleObcSurcharge(Double highestPossibleObcSurcharge) {
		this.highestPossibleObcSurcharge = highestPossibleObcSurcharge;
	}

	public String getInternalComments() {
		return internalComments;
	}

	public void setInternalComments(String internalComments) {
		this.internalComments = internalComments;
	}

	public String getIsActiveInd() {
		return isActiveInd;
	}

	public void setIsActiveInd(String isActiveInd) {
		this.isActiveInd = isActiveInd;
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

	public String getReasonText() {
		return reasonText;
	}

	public void setReasonText(String reasonText) {
		this.reasonText = reasonText;
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

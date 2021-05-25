package com.tcl.dias.oms.gsc.pricing.beans;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Gsc Outbound data format bean
 *
 * @author PRABUBALSUBRAMANIAN
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class GscOutboundPriceBean {

	private Integer uID;
	private String country;
	private String countryCode;
	private String phoneType;
	private Integer destId;
	private String destinationName;
	private String serviceLevel;
	private String comments;
	private String region;
	private String currencyCode;
	private Double cdaFloor;
	private Double spRegionDiscount;
	private Double spDiscount3;
	private Double spDiscount2;
	private Double spDiscount1;
	private Double serviceProviderFloor;
	private Double epRegionDiscount;
	private Double enterpriseDiscount3;
	private Double enterpriseDiscount2;
	private Double enterpriseDiscount1;
	private Double enterpriseSalesFloor;
	private Double highRate;
	private Double highestPossibleObcSurcharge;
	private String internalComments;
	private String isActiveInd;
	private Date effectiveFromDate;
	private Date effectiveToDate;
	private String reasonText;
	private String createdBy;
	private Date createdDate;
	private String updatedBy;
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

	public Double getSpRegionDiscount() {
		return spRegionDiscount;
	}

	public void setSpRegionDiscount(Double spRegionDiscount) {
		this.spRegionDiscount = spRegionDiscount;
	}

	public Double getSpDiscount3() {
		return spDiscount3;
	}

	public void setSpDiscount3(Double spDiscount3) {
		this.spDiscount3 = spDiscount3;
	}

	public Double getSpDiscount2() {
		return spDiscount2;
	}

	public void setSpDiscount2(Double spDiscount2) {
		this.spDiscount2 = spDiscount2;
	}

	public Double getSpDiscount1() {
		return spDiscount1;
	}

	public void setSpDiscount1(Double spDiscount1) {
		this.spDiscount1 = spDiscount1;
	}

	public Double getServiceProviderFloor() {
		return serviceProviderFloor;
	}

	public void setServiceProviderFloor(Double serviceProviderFloor) {
		this.serviceProviderFloor = serviceProviderFloor;
	}

	public Double getEpRegionDiscount() {
		return epRegionDiscount;
	}

	public void setEpRegionDiscount(Double epRegionDiscount) {
		this.epRegionDiscount = epRegionDiscount;
	}

	public Double getEnterpriseDiscount3() {
		return enterpriseDiscount3;
	}

	public void setEnterpriseDiscount3(Double enterpriseDiscount3) {
		this.enterpriseDiscount3 = enterpriseDiscount3;
	}

	public Double getEnterpriseDiscount2() {
		return enterpriseDiscount2;
	}

	public void setEnterpriseDiscount2(Double enterpriseDiscount2) {
		this.enterpriseDiscount2 = enterpriseDiscount2;
	}

	public Double getEnterpriseDiscount1() {
		return enterpriseDiscount1;
	}

	public void setEnterpriseDiscount1(Double enterpriseDiscount1) {
		this.enterpriseDiscount1 = enterpriseDiscount1;
	}

	public Double getEnterpriseSalesFloor() {
		return enterpriseSalesFloor;
	}

	public void setEnterpriseSalesFloor(Double enterpriseSalesFloor) {
		this.enterpriseSalesFloor = enterpriseSalesFloor;
	}

	public Double getHighRate() {
		return highRate;
	}

	public void setHighRate(Double highRate) {
		this.highRate = highRate;
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

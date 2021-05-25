package com.tcl.dias.products.gsc.beans;

import com.tcl.dias.productcatelog.entity.entities.GscOutboundSurchargePrices;

import java.math.BigInteger;
import java.util.Date;

/**
 * OutboundSurchargePricingBean for GscOutboundSurchargePrices Entity
 * 
 * @author AVALLAPI
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class OutboundSurchargePricingBean {

	private Integer UID;
	private String regionCountry;
	private String regionCountryCode;
	private String regionCountryDestination;
	private Integer regionCountryDestId;
	private String exteriorRegionName;
	private BigInteger originCountryCode;
	private String originCountryName;
	private String originCountryNameCode;
	private String surcharge;
	private Double surchargeAmount;
	private String currency;
	private String isActiveIndicator;
	private Date effectiveFromDate;
	private Date effectiveToDate;
	private String reasonTxt;

	public Integer getUID() {
		return UID;
	}

	public void setUID(Integer uID) {
		UID = uID;
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

	/**
	 * GscOutboundSurchargePrices to OutboundSurchargePricingBean
	 * 
	 * @param gscOutboundSurchargePrices
	 * @return {@link OutboundSurchargePricingBean}
	 */
	public static OutboundSurchargePricingBean fromGscOutboundSurchargePricing(
			GscOutboundSurchargePrices gscOutboundSurchargePrices) {
		OutboundSurchargePricingBean bean = new OutboundSurchargePricingBean();
		bean.setUID(gscOutboundSurchargePrices.getUid());
		bean.setCurrency(gscOutboundSurchargePrices.getCurrency());
		bean.setEffectiveFromDate(gscOutboundSurchargePrices.getEffectiveFromDate());
		bean.setEffectiveToDate(gscOutboundSurchargePrices.getEffectiveToDate());
		bean.setExteriorRegionName(gscOutboundSurchargePrices.getExteriorRegionName());
		bean.setIsActiveIndicator(gscOutboundSurchargePrices.getIsActiveIndicator());
		bean.setOriginCountryCode(gscOutboundSurchargePrices.getOriginCountryCode());
		bean.setOriginCountryName(gscOutboundSurchargePrices.getOriginCountryName());
		bean.setOriginCountryNameCode(gscOutboundSurchargePrices.getOriginCountryNameCode());
		bean.setReasonTxt(gscOutboundSurchargePrices.getReasonTxt());
		bean.setRegionCountry(gscOutboundSurchargePrices.getRegionCountry());
		bean.setRegionCountryCode(gscOutboundSurchargePrices.getRegionCountryCode());
		bean.setRegionCountryDestId(gscOutboundSurchargePrices.getRegionCountryDestId());
		bean.setRegionCountryDestination(gscOutboundSurchargePrices.getRegionCountryDestination());
		bean.setSurcharge(gscOutboundSurchargePrices.getSurcharge());
		bean.setSurchargeAmount(Double.valueOf(String.format("%.4f",gscOutboundSurchargePrices.getSurchargeAmount())));
		return bean;
	}
}

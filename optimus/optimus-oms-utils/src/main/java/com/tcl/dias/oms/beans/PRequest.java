package com.tcl.dias.oms.beans;

import org.springframework.lang.NonNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * This file contains the PRequest.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PRequest {

	@NonNull
	private Integer siteQuotePriceId;
	private String componentName;
	private String type;
	@NonNull
	private String pricingType;
	private Double effectiveUsagePrice;
	private Double effectiveNrc;
	private Double effectiveMrc;
	private Double effectiveArc;
	private Double tcv;

	public Integer getSiteQuotePriceId() {
		return siteQuotePriceId;
	}

	public void setSiteQuotePriceId(Integer siteQuotePriceId) {
		this.siteQuotePriceId = siteQuotePriceId;
	}

	public String getPricingType() {
		return pricingType;
	}

	public void setPricingType(String pricingType) {
		this.pricingType = pricingType;
	}

	public Double getEffectiveNrc() {
		return effectiveNrc;
	}

	public void setEffectiveNrc(Double effectiveNrc) {
		this.effectiveNrc = effectiveNrc;
	}

	public Double getEffectiveMrc() {
		return effectiveMrc;
	}

	public void setEffectiveMrc(Double effectiveMrc) {
		this.effectiveMrc = effectiveMrc;
	}

	public Double getEffectiveArc() {
		return effectiveArc;
	}

	public void setEffectiveArc(Double effectiveArc) {
		this.effectiveArc = effectiveArc;
	}

	public Double getTcv() {
		return tcv;
	}

	public void setTcv(Double tcv) {
		this.tcv = tcv;
	}

	public String getComponentName() {
		return componentName;
	}

	public void setComponentName(String componentName) {
		this.componentName = componentName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the effectiveUsagePrice
	 */
	public Double getEffectiveUsagePrice() {
		return effectiveUsagePrice;
	}

	/**
	 * @param effectiveUsagePrice
	 *            the effectiveUsagePrice to set
	 */
	public void setEffectiveUsagePrice(Double effectiveUsagePrice) {
		this.effectiveUsagePrice = effectiveUsagePrice;
	}

	@Override
	public String toString() {
		return "PRequest [siteQuotePriceId=" + siteQuotePriceId + ", pricingType=" + pricingType + ", effectiveNrc="
				+ effectiveNrc + ", effectiveMrc=" + effectiveMrc + ", effectiveArc=" + effectiveArc + ", tcv=" + tcv
				+ "]";
	}

}

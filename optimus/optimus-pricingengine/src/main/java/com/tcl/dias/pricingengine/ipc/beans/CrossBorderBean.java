package com.tcl.dias.pricingengine.ipc.beans;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * This file contains the CrossBorderBean.java class. Bean class
 *
 * @author Dimple S
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "dcLocationId", "customerLeCountry", "crossBorderWhTaxPercentage", "isCrossBorderTaxApplicable" })
public class CrossBorderBean {

	@JsonProperty("dcLocationId")
	private String dcLocationId;
	
	@JsonProperty("dcLocationCountry")
	private String dcLocationCountry;

	@JsonProperty("customerLeCountry")
	private String customerLeCountry;

	@JsonProperty("crossBorderWhTaxPercentage")
	private Double crossBorderWhTaxPercentage;

	@JsonProperty("isCrossBorderTaxApplicable")
	private Boolean isCrossBorderTaxApplicable = false;

	public String getDcLocationId() {
		return dcLocationId;
	}

	public void setDcLocationId(String dcLocationId) {
		this.dcLocationId = dcLocationId;
	}

	public String getCustomerLeCountry() {
		return customerLeCountry;
	}

	public void setCustomerLeCountry(String customerLeCountry) {
		this.customerLeCountry = customerLeCountry;
	}

	public Double getCrossBorderWhTaxPercentage() {
		return crossBorderWhTaxPercentage;
	}

	public void setCrossBorderWhTaxPercentage(Double crossBorderWhTaxPercentage) {
		this.crossBorderWhTaxPercentage = crossBorderWhTaxPercentage;
	}

	public Boolean getIsCrossBorderTaxApplicable() {
		return isCrossBorderTaxApplicable;
	}

	public void setIsCrossBorderTaxApplicable(Boolean isCrossBorderTaxApplicable) {
		this.isCrossBorderTaxApplicable = isCrossBorderTaxApplicable;
	}
	
	public String getDcLocationCountry() {
		return dcLocationCountry;
	}

	public void setDcLocationCountry(String dcLocationCountry) {
		this.dcLocationCountry = dcLocationCountry;
	}

	@Override
	public String toString() {
		return "CrossBorderBean [dcLocationId=" + dcLocationId + ", dcLocationCountry=" + dcLocationCountry
				+ ", customerLeCountry=" + customerLeCountry + ", crossBorderWhTaxPercentage="
				+ crossBorderWhTaxPercentage + ", isCrossBorderTaxApplicable=" + isCrossBorderTaxApplicable + "]";
	}

}
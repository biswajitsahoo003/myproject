package com.tcl.dias.pricingengine.ipc.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * This file contains the IpcDiscountBean.java class. Bean class
 *
 * @author Danish
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({ "countryCode", "cityCode", "customerId", "customerLedId", "additionalDiscountPercentage",
		"ipcFinalPrice" })
public class IpcDiscountBean {

	private String countryCode;

	private String cityCode;

	private Integer customerId;

	private Integer customerLedId;

	private Double additionalDiscountPercentage;

	private Double inputDiscountPercentage;

	private Double ipcFinalPrice;

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public Integer getCustomerLedId() {
		return customerLedId;
	}

	public void setCustomerLedId(Integer customerLedId) {
		this.customerLedId = customerLedId;
	}

	public Double getAdditionalDiscountPercentage() {
		return additionalDiscountPercentage;
	}

	public void setAdditionalDiscountPercentage(Double additionalDiscountPercentage) {
		this.additionalDiscountPercentage = additionalDiscountPercentage;
	}

	public Double getIpcFinalPrice() {
		return ipcFinalPrice;
	}

	public void setIpcFinalPrice(Double ipcFinalPrice) {
		this.ipcFinalPrice = ipcFinalPrice;
	}

	public Double getInputDiscountPercentage() {
		return inputDiscountPercentage;
	}

	public void setInputDiscountPercentage(Double inputDiscountPercentage) {
		this.inputDiscountPercentage = inputDiscountPercentage;
	}

	@Override
	public String toString() {
		return "IpcDiscountBean [countryCode=" + countryCode + ", cityCode=" + cityCode + ", customerId=" + customerId
				+ ", customerLedId=" + customerLedId + ", additionalDiscountPercentage=" + additionalDiscountPercentage
				+ ", inputDiscountPercentage=" + inputDiscountPercentage + ", ipcFinalPrice=" + ipcFinalPrice + "]";
	}

}

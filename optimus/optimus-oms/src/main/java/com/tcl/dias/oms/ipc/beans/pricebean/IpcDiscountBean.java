package com.tcl.dias.oms.ipc.beans.pricebean;

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

	private Integer quoteToLe;

	private String countryCode;

	private String cityCode;

	private Integer customerId;

	private Integer customerLedId;

	private Double additionalDiscountPercentage;

	private Double inputDiscountPercentage;

	private Double ipcFinalPrice;

	private Double askAccessPrice;

	private Double askAdditionalIpPrice;

	private Double existAdditionalIpPrice;

	private Double partnerCommissionPercentage;

	public Integer getQuoteToLe() {
		return quoteToLe;
	}

	public void setQuoteToLe(Integer quoteToLe) {
		this.quoteToLe = quoteToLe;
	}

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

	public Double getAskAccessPrice() {
		return askAccessPrice;
	}

	public void setAskAccessPrice(Double askAccessPrice) {
		this.askAccessPrice = askAccessPrice;
	}

	public Double getAskAdditionalIpPrice() {
		return askAdditionalIpPrice;
	}

	public void setAskAdditionalIpPrice(Double askAdditionalIpPrice) {
		this.askAdditionalIpPrice = askAdditionalIpPrice;
	}

	public Double getExistAdditionalIpPrice() {
		return existAdditionalIpPrice;
	}

	public void setExistAdditionalIpPrice(Double existAdditionalIpPrice) {
		this.existAdditionalIpPrice = existAdditionalIpPrice;
	}

	public Double getPartnerCommissionPercentage() {
		return partnerCommissionPercentage;
	}

	public void setPartnerCommissionPercentage(Double partnerCommissionPercentage) {
		this.partnerCommissionPercentage = partnerCommissionPercentage;
	}

	@Override
	public String toString() {
		return "IpcDiscountBean [quoteToLe=" + quoteToLe + ", countryCode=" + countryCode + ", cityCode=" + cityCode
				+ ", customerId=" + customerId + ", customerLedId=" + customerLedId + ", additionalDiscountPercentage="
				+ additionalDiscountPercentage + ", inputDiscountPercentage=" + inputDiscountPercentage
				+ ", ipcFinalPrice=" + ipcFinalPrice + ", askAccessPrice=" + askAccessPrice + ", askAdditionalIpPrice="
				+ askAdditionalIpPrice + ", existAdditionalIpPrice=" + existAdditionalIpPrice
				+ ", partnerCommissionPercentage=" + partnerCommissionPercentage + "]";
	}

}

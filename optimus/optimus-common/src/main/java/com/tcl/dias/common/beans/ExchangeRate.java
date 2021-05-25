package com.tcl.dias.common.beans;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * 
 * Bean Class
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "From_Currency", "To_Currency", "Rate_Date", "Rate_Type", "Exchange_Rate", "Status", "Remark" })
public class ExchangeRate {

	@JsonProperty("From_Currency")
	private String fromCurrency;
	@JsonProperty("To_Currency")
	private String toCurrency;
	@JsonProperty("Rate_Date")
	private String rateDate;
	@JsonProperty("Rate_Type")
	private String rateType;
	@JsonProperty("Rate")
	private String exhangeRate;
	@JsonProperty("Status")
	private String status;
	@JsonProperty("Remark")
	private String remark;

	public String getFromCurrency() {
		return fromCurrency;
	}

	public void setFromCurrency(String fromCurrency) {
		this.fromCurrency = fromCurrency;
	}

	public String getToCurrency() {
		return toCurrency;
	}

	public void setToCurrency(String toCurrency) {
		this.toCurrency = toCurrency;
	}

	public String getRateDate() {
		return rateDate;
	}

	public void setRateDate(String rateDate) {
		this.rateDate = rateDate;
	}

	public String getRateType() {
		return rateType;
	}

	public void setRateType(String rateType) {
		this.rateType = rateType;
	}

	public String getExhangeRate() {
		return exhangeRate;
	}

	public void setExhangeRate(String exhangeRate) {
		this.exhangeRate = exhangeRate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Override
	public String toString() {
		return "ExchangeRate [fromCurrency=" + fromCurrency + ", toCurrency=" + toCurrency + ", rateDate=" + rateDate
				+ ", rateType=" + rateType + ", exhangeRate=" + exhangeRate + ", status=" + status + ", remark="
				+ remark + "]";
	}

}

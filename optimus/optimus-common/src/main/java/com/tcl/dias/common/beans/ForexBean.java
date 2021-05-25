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
@JsonPropertyOrder({ "Exchange_Rate" })
public class ForexBean {

	@JsonProperty("Exchange_Rate")
	private ExchangeRate exchangeRate;

	public ExchangeRate getExchangeRate() {
		return exchangeRate;
	}

	public void setExchangeRate(ExchangeRate exchangeRate) {
		this.exchangeRate = exchangeRate;
	}

	@Override
	public String toString() {
		return "ForexRequest [exchangeRate=" + exchangeRate + "]";
	}

}

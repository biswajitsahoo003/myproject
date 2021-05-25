package com.tcl.dias.common.beans;

import java.util.List;

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
public class ForexResponseBean {

	@JsonProperty("Exchange_Rate")
	private List<ExchangeRate> exchangeRate;

	public List<ExchangeRate> getExchangeRate() {
		return exchangeRate;
	}

	public void setExchangeRate(List<ExchangeRate> exchangeRate) {
		this.exchangeRate = exchangeRate;
	}

	@Override
	public String toString() {
		return "ForexRequest [exchangeRate=" + exchangeRate + "]";
	}

}

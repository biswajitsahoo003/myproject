package com.tcl.dias.oms.gsc.pricing.beans;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Global Outbound Price format bean
 *
 * @author Thamizhselvi Perumal
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({ "destination", "currency", "term_name", "phone_type", "price" })
public class GlobalOutboundPriceBean {

	@JsonProperty("destination")
	private String destination;

	@JsonProperty("currency")
	private List<String> currency;

	@JsonProperty("term_name")
	private List<String> terminationName;

	@JsonProperty("phone_type")
	private List<String> phoneType;

	@JsonProperty("price")
	private List<String> price;

	@JsonProperty("destination")
	public String getDestination() {
		return destination;
	}

	@JsonProperty("destination")
	public void setDestination(String destination) {
		this.destination = destination;
	}

	@JsonProperty("currency")
	public List<String> getCurrency() {
		return currency;
	}

	@JsonProperty("currency")
	public void setCurrency(List<String> currency) {
		this.currency = currency;
	}

	@JsonProperty("term_name")
	public List<String> getTerminationName() {
		return terminationName;
	}

	@JsonProperty("term_name")
	public void setTerminationName(List<String> terminationName) {
		this.terminationName = terminationName;
	}

	@JsonProperty("phone_type")
	public List<String> getPhoneType() {
		return phoneType;
	}

	@JsonProperty("phone_type")
	public void setPhoneType(List<String> phoneType) {
		this.phoneType = phoneType;
	}

	@JsonProperty("price")
	public List<String> getPrice() {
		return price;
	}

	@JsonProperty("price")
	public void setPrice(List<String> price) {
		this.price = price;
	}

	@Override
	public String toString() {
		return "GlobalOutboundPriceBean [destination=" + destination + ", currency=" + currency + ", terminationName="
				+ terminationName + ", phoneType=" + phoneType + ", price=" + price + "]";
	}

}

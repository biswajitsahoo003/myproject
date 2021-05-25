package com.tcl.dias.pricingengine.ipc.beans;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "customer_id", "region", "country", "items", "attributes" })
public class RateCard {

	@JsonProperty("customer_id")
	private Integer customerId;

	@JsonProperty("region")
	private String region;

	@JsonProperty("country")
	private String country;

	@JsonProperty("items")
	private List<RateCardItem> items;

	@JsonProperty("attributes")
	private List<RateCardAttribute> attributes;

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public List<RateCardItem> getItems() {
		return items;
	}

	public void setItems(List<RateCardItem> items) {
		this.items = items;
	}

	public List<RateCardAttribute> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<RateCardAttribute> attributes) {
		this.attributes = attributes;
	}
}
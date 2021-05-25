package com.tcl.dias.oms.gsc.pricing.beans;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Domestic Voice Price Request bean
 *
 * @author Thamizhselvi Perumal
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({ "did_nrc", "did_mrc", "did_arc", "order_setup_nrc", "order_setup_mrc", "order_setup_arc",
		"channel_nrc", "channel_mrc", "channel_arc", "currency", "term_name", "phone_type", "usage_currency", "price" })
public class DomesticVoicePriceBean {

	@JsonProperty("did_nrc")
	private String didNrc;

	@JsonProperty("did_mrc")
	private String didMrc;

	@JsonProperty("did_arc")
	private String didArc;

	@JsonProperty("order_setup_nrc")
	private String orderSetupNrc;

	@JsonProperty("order_setup_mrc")
	private String orderSetupMrc;

	@JsonProperty("order_setup_arc")
	private String orderSetupArc;

	@JsonProperty("channel_nrc")
	private String channelNrc;

	@JsonProperty("channel_mrc")
	private String channelMrc;

	@JsonProperty("channel_arc")
	private String channelArc;

	@JsonProperty("currency")
	private String currency;

	@JsonProperty("term_name")
	private List<String> terminationName = new ArrayList<>();

	@JsonProperty("usage_currency")
	private List<String> usageCurrency = new ArrayList<>();

	@JsonProperty("phone_type")
	private List<String> phoneType = new ArrayList<>();

	@JsonProperty("price")
	private List<String> price = new ArrayList<>();

	@JsonProperty("did_nrc")
	public String getDidNrc() {
		return didNrc;
	}

	@JsonProperty("did_nrc")
	public void setDidNrc(String didNrc) {
		this.didNrc = didNrc;
	}

	@JsonProperty("did_mrc")
	public String getDidMrc() {
		return didMrc;
	}

	@JsonProperty("did_mrc")
	public void setDidMrc(String didMrc) {
		this.didMrc = didMrc;
	}

	@JsonProperty("did_arc")
	public String getDidArc() {
		return didArc;
	}

	@JsonProperty("did_arc")
	public void setDidArc(String didArc) {
		this.didArc = didArc;
	}

	@JsonProperty("order_setup_nrc")
	public String getOrderSetupNrc() {
		return orderSetupNrc;
	}

	@JsonProperty("order_setup_nrc")
	public void setOrderSetupNrc(String orderSetupNrc) {
		this.orderSetupNrc = orderSetupNrc;
	}

	@JsonProperty("order_setup_mrc")
	public String getOrderSetupMrc() {
		return orderSetupMrc;
	}

	@JsonProperty("order_setup_mrc")
	public void setOrderSetupMrc(String orderSetupMrc) {
		this.orderSetupMrc = orderSetupMrc;
	}

	@JsonProperty("order_setup_arc")
	public String getOrderSetupArc() {
		return orderSetupArc;
	}

	@JsonProperty("order_setup_arc")
	public void setOrderSetupArc(String orderSetupArc) {
		this.orderSetupArc = orderSetupArc;
	}

	@JsonProperty("channel_nrc")
	public String getChannelNrc() {
		return channelNrc;
	}

	@JsonProperty("channel_nrc")
	public void setChannelNrc(String channelNrc) {
		this.channelNrc = channelNrc;
	}

	@JsonProperty("channel_mrc")
	public String getChannelMrc() {
		return channelMrc;
	}

	@JsonProperty("channel_mrc")
	public void setChannelMrc(String channelMrc) {
		this.channelMrc = channelMrc;
	}

	@JsonProperty("channel_arc")
	public String getChannelArc() {
		return channelArc;
	}

	@JsonProperty("channel_arc")
	public void setChannelArc(String channelArc) {
		this.channelArc = channelArc;
	}

	@JsonProperty("term_name")
	public List<String> getTerminationName() {
		return terminationName;
	}

	@JsonProperty("term_name")
	public void setTerminationName(List<String> terminationName) {
		this.terminationName = terminationName;
	}

	@JsonProperty("price")
	public List<String> getPrice() {
		return price;
	}

	@JsonProperty("price")
	public void setPrice(List<String> price) {
		this.price = price;
	}

	@JsonProperty("currency")
	public String getCurrency() {
		return currency;
	}

	@JsonProperty("currency")
	public void setCurrency(String currency) {
		this.currency = currency;
	}

	@JsonProperty("usage_currency")
	public List<String> getUsageCurrency() {
		return usageCurrency;
	}

	@JsonProperty("usage_currency")
	public void setUsageCurrency(List<String> usageCurrency) {
		this.usageCurrency = usageCurrency;
	}

	@JsonProperty("phone_type")
	public List<String> getPhoneType() {
		return phoneType;
	}

	@JsonProperty("phone_type")
	public void setPhoneType(List<String> phoneType) {
		this.phoneType = phoneType;
	}

	@Override
	public String toString() {
		return "DomesticVoicePriceBean [didNrc=" + didNrc + ", didMrc=" + didMrc + ", didArc=" + didArc
				+ ", orderSetupNrc=" + orderSetupNrc + ", orderSetupMrc=" + orderSetupMrc + ", orderSetupArc="
				+ orderSetupArc + ", channelNrc=" + channelNrc + ", channelMrc=" + channelMrc + ", channelArc="
				+ channelArc + ", currency=" + currency + ", terminationName=" + terminationName + ", usageCurrency="
				+ usageCurrency + ", phoneType=" + phoneType + ", price=" + price + "]";
	}
}

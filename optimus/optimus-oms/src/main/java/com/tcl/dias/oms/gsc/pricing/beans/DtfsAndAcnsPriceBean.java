package com.tcl.dias.oms.gsc.pricing.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Dtfs and Acns Price format bean
 *
 * @author Thamizhselvi Perumal
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({ "origin", "dest", "currency", "NRC", "MRC", "ARC", "price_fixed", "price_mobile",
		"price_payphone" })
public class DtfsAndAcnsPriceBean {

	@JsonProperty("origin")
	private String origin;

	@JsonProperty("dest")
	private String dest;

	@JsonProperty("currency")
	private String currency;

	@JsonProperty("NRC")
	private String nrc;

	@JsonProperty("ARC")
	private String arc;

	@JsonProperty("MRC")
	private String mrc;

	@JsonProperty("price_fixed")
	private String priceFixed;

	@JsonProperty("price_mobile")
	private String priceMobile;

	@JsonProperty("price_payphone")
	private String pricePayphone;

	@JsonProperty("origin")
	public String getOrigin() {
		return origin;
	}

	@JsonProperty("origin")
	public void setOrigin(String origin) {
		this.origin = origin;
	}

	@JsonProperty("dest")
	public String getDest() {
		return dest;
	}

	@JsonProperty("dest")
	public void setDest(String dest) {
		this.dest = dest;
	}

	@JsonProperty("currency")
	public String getCurrency() {
		return currency;
	}

	@JsonProperty("currency")
	public void setCurrency(String currency) {
		this.currency = currency;
	}

	@JsonProperty("NRC")
	public String getNrc() {
		return nrc;
	}

	@JsonProperty("NRC")
	public void setNrc(String nrc) {
		this.nrc = nrc;
	}

	@JsonProperty("ARC")
	public String getArc() {
		return arc;
	}

	@JsonProperty("ARC")
	public void setArc(String arc) {
		this.arc = arc;
	}

	@JsonProperty("MRC")
	public String getMrc() {
		return mrc;
	}

	@JsonProperty("MRC")
	public void setMrc(String mrc) {
		this.mrc = mrc;
	}

	@JsonProperty("price_fixed")
	public String getPriceFixed() {
		return priceFixed;
	}

	@JsonProperty("price_fixed")
	public void setPriceFixed(String priceFixed) {
		this.priceFixed = priceFixed;
	}

	@JsonProperty("price_mobile")
	public String getPriceMobile() {
		return priceMobile;
	}

	@JsonProperty("price_mobile")
	public void setPriceMobile(String priceMobile) {
		this.priceMobile = priceMobile;
	}

	@JsonProperty("price_payphone")
	public String getPricePayphone() {
		return pricePayphone;
	}

	@JsonProperty("price_payphone")
	public void setPricePayphone(String pricePayphone) {
		this.pricePayphone = pricePayphone;
	}

	@Override
	public String toString() {
		return "DtfsAndAcnsPriceBean [origin=" + origin + ", dest=" + dest + ", nrc=" + nrc + ", arc=" + arc + ", mrc="
				+ mrc + ", priceFixed=" + priceFixed + ", priceMobile=" + priceMobile + ", pricePayphone="
				+ pricePayphone + "]";
	}

}

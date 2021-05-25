package com.tcl.dias.productcatelog.entity.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "pricing_ipc_ratecard")
@NamedQuery(name = "PricingIpcRateCard.findAll", query = "SELECT p FROM PricingIpcRateCard p")
public class PricingIpcRateCard implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "customer_id")
	private Integer customerId;

	@Column(name = "region")
	private String region;

	@Column(name = "country")
	private String country;

	@ManyToOne(fetch = FetchType.LAZY)
	private PricingIpcItem item;

	@Column(name = "price")
	private Double price;

	@Column(name = "currency_code")
	private String currencyCode;

	public PricingIpcRateCard() {
		// DO NOTHING
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

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

	public PricingIpcItem getItem() {
		return item;
	}

	public void setItem(PricingIpcItem item) {
		this.item = item;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	@Override
	public String toString() {
		return "PricingIpcRateCard [id=" + id + ", customerId=" + customerId + ", region=" + region + ", country="
				+ country + ", item=" + item + ", price=" + price + ", currencyCode=" + currencyCode + "]";
	}
}

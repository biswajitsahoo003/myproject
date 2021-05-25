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
@Table(name = "pricing_ipc_ratecard_attributes")
@NamedQuery(name = "PricingIpcRateCardAttribute.findAll", query = "SELECT p FROM PricingIpcRateCardAttribute p")
public class PricingIpcRateCardAttribute implements Serializable {

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
	private PricingIpcAttribute attribute;

	@Column(name = "value")
	private String value;

	public PricingIpcRateCardAttribute() {
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

	public PricingIpcAttribute getAttribute() {
		return attribute;
	}

	public void setAttribute(PricingIpcAttribute attribute) {
		this.attribute = attribute;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "PricingIpcRateCardAttribute [id=" + id + ", customerId=" + customerId + ", region=" + region
				+ ", country=" + country + ", attribute=" + attribute + ", value=" + value + "]";
	}
}

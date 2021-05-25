package com.tcl.dias.productcatelog.entity.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * 
 * This file holds the entity class for the IPC Customer net margin percentage
 * component.
 * 
 *
 * @author Mohamed Danish A
 * @link http://www.tatacommunications.com/
 * @copyright 2019 TATA Communications Limited
 * 
 */
@Entity
@Table(name = "pricing_ipc_customer_net_margin")
@NamedQuery(name = "PricingIpcCustomerNetMargin.findAll", query = "SELECT nm FROM PricingIpcCustomerNetMargin nm")
public class PricingIpcCustomerNetMargin implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "city_code")
	private String cityCode;

	@Column(name = "country_code")
	private String countryCode;

	@Column(name = "customer_id")
	private Integer customerId;

	@Column(name = "net_margin_percentage")
	private Double netMarginPercentage;

	@Column(name = "final_discount_percentage")
	private Double finalDiscountPercentage;

	public PricingIpcCustomerNetMargin() {
		// DO NOTHING
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public Double getNetMarginPercentage() {
		return netMarginPercentage;
	}

	public void setNetMarginPercentage(Double netMarginPercentage) {
		this.netMarginPercentage = netMarginPercentage;
	}

	public Double getFinalDiscountPercentage() {
		return finalDiscountPercentage;
	}

	public void setFinalDiscountPercentage(Double finalDiscountPercentage) {
		this.finalDiscountPercentage = finalDiscountPercentage;
	}

}
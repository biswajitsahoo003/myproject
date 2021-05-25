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
 * This file holds the entity class for the IPC Cross border with holding tax
 * component.
 * 
 *
 * @author Mohamed Danish A
 * @link http://www.tatacommunications.com/
 * @copyright 2019 TATA Communications Limited
 * 
 */
@Entity
@Table(name = "pricing_ipc_crossborder_wh_tax")
@NamedQuery(name = "PricingIpcCrossBorderWhTax.findAll", query = "SELECT t FROM PricingIpcCrossBorderWhTax t")
public class PricingIpcCrossBorderWhTax implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "customer_le_country")
	private String customerLeCountry;

	@Column(name = "dc_location_country")
	private String dcLocationCountry;

	@Column(name = "tax_percentage")
	private Double taxPercentage;

	public PricingIpcCrossBorderWhTax() {
		// DO NOTHING
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCustomerLeCountry() {
		return customerLeCountry;
	}

	public void setCustomerLeCountry(String customerLeCountry) {
		this.customerLeCountry = customerLeCountry;
	}

	public String getDcLocationCountry() {
		return dcLocationCountry;
	}

	public void setDcLocationCountry(String dcLocationCountry) {
		this.dcLocationCountry = dcLocationCountry;
	}

	public Double getTaxPercentage() {
		return taxPercentage;
	}

	public void setTaxPercentage(Double taxPercentage) {
		this.taxPercentage = taxPercentage;
	}

}
package com.tcl.dias.productcatelog.entity.entities;

import java.io.Serializable;
import javax.persistence.*;

/**
 * 
 * This file contains the PricingIpcDatatransfer.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@Entity
@Table(name = "pricing_ipc_datatransfer")
@NamedQuery(name = "PricingIpcDatatransfer.findAll", query = "SELECT p FROM PricingIpcDatatransfer p")
public class PricingIpcDatatransfer implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "end_limit")
	private Integer endLimit;

	@Column(name = "location_code")
	private String locationCode;

	private Double price;

	@Column(name = "start_limit")
	private Integer startLimit;

	@Column(name = "bandwidth")
	private Integer bandwidth;

	private String unit;

	public PricingIpcDatatransfer() {
		// DO NOTHING
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getEndLimit() {
		return this.endLimit;
	}

	public void setEndLimit(Integer endLimit) {
		this.endLimit = endLimit;
	}

	public String getLocationCode() {
		return this.locationCode;
	}

	public void setLocationCode(String locationCode) {
		this.locationCode = locationCode;
	}

	public Double getPrice() {
		return this.price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Integer getStartLimit() {
		return this.startLimit;
	}

	public void setStartLimit(Integer startLimit) {
		this.startLimit = startLimit;
	}

	public String getUnit() {
		return this.unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public Integer getBandwidth() {
		return bandwidth;
	}

	public void setBandwidth(Integer bandwidth) {
		this.bandwidth = bandwidth;
	}

}
package com.tcl.dias.productcatelog.entity.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * This file contains the VwSdwanIndiaSla.java class.
 * 
 * @author vpachava
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
@Entity
@Table(name="vw_sdwan_select_india_sla")	
public class VwSdwanSelectIndiaSla implements Serializable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="id")
	private Integer id;
	
	@Column(name="cd")
	private String productCode;
	
	@Column(name="prod_offr_cd")
	private String productOfferingCode;
	
	@Column(name="prod_offr_nm")
	private String productOfferingName;
	
	@Column(name="location_cd")
	private String locationCode;
	
	@Column(name="location_nm")
	private String locationName;
	
	@Column(name="city_nm")
	private String cityName;
	
	@Column(name="tier_type")
	private String tierType;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getProductOfferingCode() {
		return productOfferingCode;
	}

	public void setProductOfferingCode(String productOfferingCode) {
		this.productOfferingCode = productOfferingCode;
	}

	public String getProductOfferingName() {
		return productOfferingName;
	}

	public void setProductOfferingName(String productOfferingName) {
		this.productOfferingName = productOfferingName;
	}

	public String getLocationCode() {
		return locationCode;
	}

	public void setLocationCode(String locationCode) {
		this.locationCode = locationCode;
	}

	public String getLocationName() {
		return locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getTierType() {
		return tierType;
	}

	public void setTierType(String tierType) {
		this.tierType = tierType;
	}
	
	
	

}

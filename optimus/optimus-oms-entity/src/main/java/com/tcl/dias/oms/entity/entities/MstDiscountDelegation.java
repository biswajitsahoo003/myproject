package com.tcl.dias.oms.entity.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "mst_discount_delegation")
@NamedQuery(name = "MstDiscountDelegation.findAll", query = "SELECT m FROM MstDiscountDelegation m")
public class MstDiscountDelegation implements Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "product_name")
	private String productName;
	
	@Column(name = "attribute_name")
	private String attributeName;
	
	@Column(name = "min_value_in_kbps")
	private Double minValueInKbps;
	
	@Column(name = "max_value_in_kbps")
	private Double maxValueInKbps;
	
	@Column(name = "cda1")
	private Double CDA1;
	
	@Column(name = "cda2")
	private Double CDA2;
	
	@Column(name = "cda3")
	private Double CDA3;
	
	@Column(name = "country_to_region_id")
	private Integer countryToRegionId;
	
	@Column(name = "min_cd")
	private Integer minCd;
	
	@Column(name = "max_cd")
	private Integer maxCd;
	
	
	@Column(name = "interface_type")
	private String interfaceType;
	
	
	@Column(name = "type")
	private String type;
	
	
	

	public Integer getMinCd() {
		return minCd;
	}

	public void setMinCd(Integer minCd) {
		this.minCd = minCd;
	}

	public Integer getMaxCd() {
		return maxCd;
	}

	public void setMaxCd(Integer maxCd) {
		this.maxCd = maxCd;
	}

	public String getInterfaceType() {
		return interfaceType;
	}

	public void setInterfaceType(String interfaceType) {
		this.interfaceType = interfaceType;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getAttributeName() {
		return attributeName;
	}

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	public Double getCDA1() {
		return CDA1;
	}

	public void setCDA1(Double cDA1) {
		CDA1 = cDA1;
	}

	public Double getCDA2() {
		return CDA2;
	}

	public void setCDA2(Double cDA2) {
		CDA2 = cDA2;
	}

	public Double getCDA3() {
		return CDA3;
	}

	public void setCDA3(Double cDA3) {
		CDA3 = cDA3;
	}

	public Double getMinValueInKbps() {
		return minValueInKbps;
	}

	public void setMinValueInKbps(Double minValueInKbps) {
		this.minValueInKbps = minValueInKbps;
	}

	public Double getMaxValueInKbps() {
		return maxValueInKbps;
	}

	public void setMaxValueInKbps(Double maxValueInKbps) {
		this.maxValueInKbps = maxValueInKbps;
	}

	public Integer getCountryToRegionId() {
		return countryToRegionId;
	}

	public void setCountryToRegionId(Integer countryToRegionId) {
		this.countryToRegionId = countryToRegionId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	
}

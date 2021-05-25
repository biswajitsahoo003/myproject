package com.tcl.dias.common.webex.beans;

/**
 * Bean for fetching endpoint list from product catalog
 * 
 * @author Srinivasa Raghavan
 */
public class WebexEndpointCatalogBean {

	private String sku;

	private String cpeMakeVendor;

	private String equipmentCategory;

	private Integer hsnCode;

	private Integer taxPercent;

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getCpeMakeVendor() {
		return cpeMakeVendor;
	}

	public void setCpeMakeVendor(String cpeMakeVendor) {
		this.cpeMakeVendor = cpeMakeVendor;
	}

	public String getEquipmentCategory() {
		return equipmentCategory;
	}

	public void setEquipmentCategory(String equipmentCategory) {
		this.equipmentCategory = equipmentCategory;
	}

	public Integer getHsnCode() {
		return hsnCode;
	}

	public void setHsnCode(Integer hsnCode) {
		this.hsnCode = hsnCode;
	}

	public Integer getTaxPercent() {
		return taxPercent;
	}

	public void setTaxPercent(Integer taxPercent) {
		this.taxPercent = taxPercent;
	}
}

package com.tcl.dias.productcatelog.entity.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * The persistent class for the vw_ucaas_webex_endpt_hsn_codes database table.
 *
 * @author Srinivasa Raghavan
 *
 */
@Entity
@Table(name = "vw_ucaas_webex_endpt_hsn_codes")
@NamedQuery(name = "WebexEndpointHsnCodeView.findAll", query = "SELECT v FROM WebexEndpointHsnCodeView v")
public class WebexEndpointHsnCodeView implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String sku;

	@Column(name = "cpe_make_vendor")
	private String cpeMakeVendor;

	@Column(name = "equipment_category")
	private String equipmentCategory;

	@Column(name = "hsn_code")
	private Integer hsnCode;

	@Column(name = "tax_percent")
	private Integer taxPercent;

	public WebexEndpointHsnCodeView() {
	}

	public String getCpeMakeVendor() {
		return this.cpeMakeVendor;
	}

	public void setCpeMakeVendor(String cpeMakeVendor) {
		this.cpeMakeVendor = cpeMakeVendor;
	}

	public String getEquipmentCategory() {
		return this.equipmentCategory;
	}

	public void setEquipmentCategory(String equipmentCategory) {
		this.equipmentCategory = equipmentCategory;
	}

	public Integer getHsnCode() {
		return this.hsnCode;
	}

	public void setHsnCode(Integer hsnCode) {
		this.hsnCode = hsnCode;
	}

	public String getSku() {
		return this.sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public Integer getTaxPercent() {
		return this.taxPercent;
	}

	public void setTaxPercent(Integer taxPercent) {
		this.taxPercent = taxPercent;
	}

}
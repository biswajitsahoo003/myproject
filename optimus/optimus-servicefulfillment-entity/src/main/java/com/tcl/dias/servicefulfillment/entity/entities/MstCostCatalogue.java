package com.tcl.dias.servicefulfillment.entity.entities;

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
 * Entity Class
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@Entity
@Table(name = "mst_cost_catalogue")
@NamedQuery(name = "MstCostCatalogue.findAll", query = "SELECT c FROM MstCostCatalogue c")
public class MstCostCatalogue implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "bundled_bom")
	private String bundledBom;

	private String category;

	private String description;

	@Column(name = "hsn_code")
	private String hsnCode;

	private String oem;

	@Column(name = "per_list_price_usd")
	private Double perListPriceUsd;

	@Column(name = "total_list_price_usd")
	private Double totalListPriceUsd;

	@Column(name = "incremental_rate")
	private Double incrementalRate;

	@Column(name = "procurement_discount_percentage")
	private Double procurementDiscountPercentage;

	@Column(name = "total_price_usd")
	private Double totalPriceUsd;

	@Column(name = "margin_three_percentage")
	private Double marginThreePercentage;

	@Column(name = "total_price_margin")
	private Double totalPriceMargin;

	@Column(name = "ddp_charge")
	private Double ddpCharge;

	@Column(name = "total_price_ddp")
	private Double totalPriceDdp;

	@Column(name = "product_code")
	private String productCode;

	private Integer quantity;

	@Column(name = "rental_material_code")
	private String rentalMaterialCode;

	@Column(name = "sale_material_code")
	private String saleMaterialCode;

	@Column(name = "service_number")
	private String serviceNumber;

	@Column(name = "short_text")
	private String shortText;

	@Column(name = "created_date")
	private String createdDate;

	@Column(name = "vendor_code")
	private String vendorCode;
	
	@Column(name = "vendor_name")
	private String vendorName;

	private String currency;

	public MstCostCatalogue() {
		// DO NOTHING
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getBundledBom() {
		return bundledBom;
	}

	public void setBundledBom(String bundledBom) {
		this.bundledBom = bundledBom;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getHsnCode() {
		return hsnCode;
	}

	public void setHsnCode(String hsnCode) {
		this.hsnCode = hsnCode;
	}

	public String getOem() {
		return oem;
	}

	public void setOem(String oem) {
		this.oem = oem;
	}

	public Double getPerListPriceUsd() {
		return perListPriceUsd;
	}

	public void setPerListPriceUsd(Double perListPriceUsd) {
		this.perListPriceUsd = perListPriceUsd;
	}

	public Double getTotalListPriceUsd() {
		return totalListPriceUsd;
	}

	public void setTotalListPriceUsd(Double totalListPriceUsd) {
		this.totalListPriceUsd = totalListPriceUsd;
	}

	public Double getIncrementalRate() {
		return incrementalRate;
	}

	public void setIncrementalRate(Double incrementalRate) {
		this.incrementalRate = incrementalRate;
	}

	public Double getProcurementDiscountPercentage() {
		return procurementDiscountPercentage;
	}

	public void setProcurementDiscountPercentage(Double procurementDiscountPercentage) {
		this.procurementDiscountPercentage = procurementDiscountPercentage;
	}

	public Double getTotalPriceUsd() {
		return totalPriceUsd;
	}

	public void setTotalPriceUsd(Double totalPriceUsd) {
		this.totalPriceUsd = totalPriceUsd;
	}

	public Double getMarginThreePercentage() {
		return marginThreePercentage;
	}

	public void setMarginThreePercentage(Double marginThreePercentage) {
		this.marginThreePercentage = marginThreePercentage;
	}

	public Double getTotalPriceMargin() {
		return totalPriceMargin;
	}

	public void setTotalPriceMargin(Double totalPriceMargin) {
		this.totalPriceMargin = totalPriceMargin;
	}

	public Double getDdpCharge() {
		return ddpCharge;
	}

	public void setDdpCharge(Double ddpCharge) {
		this.ddpCharge = ddpCharge;
	}

	public Double getTotalPriceDdp() {
		return totalPriceDdp;
	}

	public void setTotalPriceDdp(Double totalPriceDdp) {
		this.totalPriceDdp = totalPriceDdp;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public String getRentalMaterialCode() {
		return rentalMaterialCode;
	}

	public void setRentalMaterialCode(String rentalMaterialCode) {
		this.rentalMaterialCode = rentalMaterialCode;
	}

	public String getSaleMaterialCode() {
		return saleMaterialCode;
	}

	public void setSaleMaterialCode(String saleMaterialCode) {
		this.saleMaterialCode = saleMaterialCode;
	}

	public String getServiceNumber() {
		return serviceNumber;
	}

	public void setServiceNumber(String serviceNumber) {
		this.serviceNumber = serviceNumber;
	}

	public String getShortText() {
		return shortText;
	}

	public void setShortText(String shortText) {
		this.shortText = shortText;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}


	public String getVendorCode() {
		return vendorCode;
	}

	public void setVendorCode(String vendorCode) {
		this.vendorCode = vendorCode;
	}
	
	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}
}
package com.tcl.dias.servicefulfillmentutils.beans;

import java.util.List;

public class MstCatalogueBean {

	private Integer id;

	private String bundledBom;

	private String category;

	private String description;

	private String hsnCode;

	private String oem;

	private Double perListPriceUsd;

	private Double totalListPriceUsd;

	private Double incrementalRate;

	private Double procurementDiscountPercentage;

	private Double totalPriceUsd;

	private Double marginThreePercentage;

	private Double totalPriceMargin;

	private Double ddpCharge;

	private Double totalPriceDdp;

	private String productCode;

	private Integer quantity;

	private String rentalMaterialCode;

	private String saleMaterialCode;

	private String serviceNumber;

	private String shortText;

	private Double totalTPinINR;

	private Double fxSpotRate;

	// For teamsdr ..
	private List<EndpointMaterialsBean> materialsList;
	private String vendorName;
	private String vendorCode;

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

	public Double getTotalTPinINR() {
		return totalTPinINR;
	}

	public void setTotalTPinINR(Double totalTPinINR) {
		this.totalTPinINR = totalTPinINR;
	}

	public Double getFxSpotRate() {
		return fxSpotRate;
	}

	public void setFxSpotRate(Double fxSpotRate) {
		this.fxSpotRate = fxSpotRate;
	}

	public List<EndpointMaterialsBean> getMaterialsList() {
		return materialsList;
	}

	public void setMaterialsList(List<EndpointMaterialsBean> materialsList) {
		this.materialsList = materialsList;
	}

	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	public String getVendorCode() {
		return vendorCode;
	}

	public void setVendorCode(String vendorCode) {
		this.vendorCode = vendorCode;
	}
}

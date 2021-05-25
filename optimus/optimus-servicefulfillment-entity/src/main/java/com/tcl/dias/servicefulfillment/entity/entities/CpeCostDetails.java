package com.tcl.dias.servicefulfillment.entity.entities;

import java.io.Serializable;
import java.sql.Timestamp;

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
 * @author Dimple S
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
@Entity
@Table(name = "cpe_cost_details")
@NamedQuery(name = "CpeCostDetails.findAll", query = "SELECT s FROM CpeCostDetails s")
public class CpeCostDetails implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private String oem;

	@Column(name = "bundled_bom")
	private String bundledBom;
	
	@Column(name = "material_code")
	private String materialCode;
	
	@Column(name = "service_number")
	private String serviceNumber;
	
	@Column(name = "short_text")
	private String shortText;
	
	@Column(name = "product_code")
	private String productCode;
	
	private String description;
	
	private Integer quantity;
	
	@Column(name = "calculated_price")
	private Double calculatedPrice;
	
	@Column(name = "service_id")
	private Integer serviceId;
	
	@Column(name = "service_code")
	private String serviceCode;
	
	@Column(name = "component_id")
	private Integer componentId;

	@Column(name = "created_date")
	private Timestamp createdDate;
	
	@Column(name = "vendor_code")
	private String vendorCode;
	
	@Column(name = "vendor_name")
	private String vendorName;
	
	private String currency;
	
	private String category;
	
	@Column(name = "hsn_code")
	private String hsnCode;
	
	@Column(name = "po_number")
	private String poNumber;
	
	@Column(name = "serial_number")
	private String serialNumber;
	
	@Column(name = "per_list_price_usd")
	private Double perListPriceUsd;

	@Column(name = "incremental_rate")
	private Double incrementalRate;

	@Column(name = "procurement_discount_percentage")
	private Double procurementDiscountPercentage;


	public CpeCostDetails() {
		// DO NOTHING
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getOem() {
		return oem;
	}

	public void setOem(String oem) {
		this.oem = oem;
	}

	public String getBundledBom() {
		return bundledBom;
	}

	public void setBundledBom(String bundledBom) {
		this.bundledBom = bundledBom;
	}

	public String getMaterialCode() {
		return materialCode;
	}

	public void setMaterialCode(String materialCode) {
		this.materialCode = materialCode;
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

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Double getCalculatedPrice() {
		return calculatedPrice;
	}

	public void setCalculatedPrice(Double calculatedPrice) {
		this.calculatedPrice = calculatedPrice;
	}

	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}

	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	public Integer getComponentId() {
		return componentId;
	}

	public void setComponentId(Integer componentId) {
		this.componentId = componentId;
	}

	public Timestamp getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
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

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getHsnCode() {
		return hsnCode;
	}

	public void setHsnCode(String hsnCode) {
		this.hsnCode = hsnCode;
	}

	public Double getPerListPriceUsd() {
		return perListPriceUsd;
	}

	public void setPerListPriceUsd(Double perListPriceUsd) {
		this.perListPriceUsd = perListPriceUsd;
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

	public String getPoNumber() {
		return poNumber;
	}

	public void setPoNumber(String poNumber) {
		this.poNumber = poNumber;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

}
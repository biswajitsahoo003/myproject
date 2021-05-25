package com.tcl.dias.serviceinventory.entity.entities;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * 
 * @author Dimple S
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited The persistent class for the
 *            vw_order_service_asset_info database table.
 * 
 */
@Entity
@Table(name = "vw_order_service_asset_info")
@NamedQuery(name = "VwOrderServiceAssetInfo.findAll", query = "SELECT v FROM VwOrderServiceAssetInfo v")
public class VwOrderServiceAssetInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "asset_id")
	private Integer assetId;

	@Column(name = "order_id")
	private Integer orderId;

	@Column(name = "commercial_id")
	private Integer commercialId;

	@Column(name = "pop_site_code")
	private String city;

	@Column(name = "uuid")
	private String serviceId;

	@Column(name = "service_status")
	private String status;
	
	@Column(name = "opportunity_classification")
	private String opportunityClassification;

	private String zone;

	private String name;

	@Column(name = "business_unit")
	private String businessUnit;

	private String type;

	@Column(name = "erf_prd_catalog_product_id")
	private String productCatalogId;

	@Column(name = "erf_cust_le_id")
	private String customerLeId;
	
	@Column(name = "erf_cust_partner_le_id")
	private String partnerLeId;
	
	@Column(name = "erf_cust_partner_name")
	private String partnerLeName;

	@Column(name = "created_date")
	private Timestamp serviceCreatedDate;

	private Double mrc;

	private Double nrc;

	private Double arc;

	@Column(name = "cloud_code")
	private String cloudCode;

	@Column(name = "parent_cloud_code")
	private String parentCloudCode;
	
	@Column(name = "site_type")
	private String siteType;

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getOpportunityClassification() {
		return opportunityClassification;
	}

	public void setOpportunityClassification(String opportunityClassification) {
		this.opportunityClassification = opportunityClassification;
	}

	public Integer getAssetId() {
		return assetId;
	}

	public void setAssetId(Integer assetId) {
		this.assetId = assetId;
	}

	public String getZone() {
		return zone;
	}

	public void setZone(String zone) {
		this.zone = zone;
	}

	public String getBusinessUnit() {
		return businessUnit;
	}

	public void setBusinessUnit(String businessUnit) {
		this.businessUnit = businessUnit;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getProductCatalogId() {
		return productCatalogId;
	}

	public void setProductCatalogId(String productCatalogId) {
		this.productCatalogId = productCatalogId;
	}

	public String getCustomerLeId() {
		return customerLeId;
	}

	public void setCustomerLeId(String customerLeId) {
		this.customerLeId = customerLeId;
	}

	public String getPartnerLeId() {
		return partnerLeId;
	}

	public void setPartnerLeId(String partnerLeId) {
		this.partnerLeId = partnerLeId;
	}

	public String getPartnerLeName() {
		return partnerLeName;
	}

	public void setPartnerLeName(String partnerLeName) {
		this.partnerLeName = partnerLeName;
	}

	public Timestamp getServiceCreatedDate() {
		return serviceCreatedDate;
	}

	public void setServiceCreatedDate(Timestamp serviceCreatedDate) {
		this.serviceCreatedDate = serviceCreatedDate;
	}

	public Double getMrc() {
		return mrc;
	}

	public void setMrc(Double mrc) {
		this.mrc = mrc;
	}

	public Double getNrc() {
		return nrc;
	}

	public void setNrc(Double nrc) {
		this.nrc = nrc;
	}

	public Double getArc() {
		return arc;
	}

	public void setArc(Double arc) {
		this.arc = arc;
	}

	public Integer getCommercialId() {
		return commercialId;
	}

	public void setCommercialId(Integer commercialId) {
		this.commercialId = commercialId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCloudCode() {
		return cloudCode;
	}

	public void setCloudCode(String cloudCode) {
		this.cloudCode = cloudCode;
	}

	public String getParentCloudCode() {
		return parentCloudCode;
	}

	public void setParentCloudCode(String parentCloudCode) {
		this.parentCloudCode = parentCloudCode;
	}

	public String getSiteType() {
		return siteType;
	}

	public void setSiteType(String siteType) {
		this.siteType = siteType;
	}
}
package com.tcl.dias.common.serviceinventory.beans;

import java.sql.Timestamp;
import java.util.List;
/**
 * Bean class to hold SI service asset component detail
 *
 * @author Dimple S
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
public class SIServiceAssetComponentBean {
	
	private Integer orderId;
	
	private Integer commercialId;
	
	private String city;
	
	private String serviceId;
	
	private String status;
	
	private Integer assetId;
	
	private String zone;
	
	private String businessUnit;
	
	private String type;
	
	private String productCatalogId;
	
	private String customerLeId;
	
	private String name;
		
	private Timestamp serviceCreatedDate;
	
	private Double mrc;
	
	private Double nrc;
	
	private Double arc;
	
	private String cloudCode;

	private String parentCloudCode;

	private List<SIAssetComponentBean> siAssetComponentList;
	
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

	public List<SIAssetComponentBean> getSiAssetComponentList() {
		return siAssetComponentList;
	}

	public void setSiAssetComponentList(List<SIAssetComponentBean> siAssetComponentList) {
		this.siAssetComponentList = siAssetComponentList;
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

}


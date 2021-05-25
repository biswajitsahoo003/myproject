package com.tcl.dias.productcatelog.entity.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;

/**
 * Entity class for  vw_product_datacntr_provider_assoc view
 * 
 *
 * @author vivek
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name = "vw_product_datacntr_provider_assoc")
@Immutable
@IdClass(ProductDataCentreViewId.class)
public class ProductDataCentreAssoc {

	@Column(name = "Product_Name")
	private String productName;

	@Column(name = "Data_Center_City")
	private String dataCenterCity;

	@Column(name = "Data_Center_TCL_Sitecode")
	private String dataCentrSiteCode;

	@Column(name = "Data_Center_Desc")
	private String dataCenterDec;

	@Column(name = "Data_Center_Address")
	private String dataCenterAddress;

	@Column(name = "Cloud_Prvdr_Name")
	private String cloudProviderName;

	@Column(name = "Interface_Name")
	private String interfaceName;

	@Column(name = "Remarks")
	private String remarks;
	
	@Column(name = "Data_Center_Longitude")
	private String dataCenterLongitude;
	
	@Column(name = "Data_Center_Latitude")
	private String dataCenterLatitude;


	@Id
	@Column(name = "Product_Catalog_Id")
	private Integer productCatalogId;
	
	@Column(name = "data_center_cd")
	private String dataCenterCd;

	@Id
	@Column(name = "provider_Id")
	private Integer providerId;

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getDataCentrCity() {
		return dataCenterCity;
	}

	public void setDataCentrCity(String dataCentrCity) {
		this.dataCenterCity = dataCentrCity;
	}

	public String getDataCentrSiteCode() {
		return dataCentrSiteCode;
	}

	public void setDataCentrSiteCode(String dataCentrSiteCode) {
		this.dataCentrSiteCode = dataCentrSiteCode;
	}

	public String getDataCentreDec() {
		return dataCenterDec;
	}

	public void setDataCentreDec(String dataCentreDec) {
		this.dataCenterDec = dataCentreDec;
	}

	public String getDataCentreAddress() {
		return dataCenterAddress;
	}

	public void setDataCentreAddress(String dataCentreAddress) {
		this.dataCenterAddress = dataCentreAddress;
	}

	public String getCloudProviderName() {
		return cloudProviderName;
	}

	public void setCloudProviderName(String cloudProviderName) {
		this.cloudProviderName = cloudProviderName;
	}

	public String getInterfaceName() {
		return interfaceName;
	}

	public void setInterfaceName(String interfaceName) {
		this.interfaceName = interfaceName;
	}

	
	public Integer getProductCatalogId() {
		return productCatalogId;
	}

	public void setProductCatalogId(Integer productCatalogId) {
		this.productCatalogId = productCatalogId;
	}

	public Integer getProviderId() {
		return providerId;
	}

	public void setProviderId(Integer providerId) {
		this.providerId = providerId;
	}

	public String getDataCenterCity() {
		return dataCenterCity;
	}

	public void setDataCenterCity(String dataCenterCity) {
		this.dataCenterCity = dataCenterCity;
	}

	public String getDataCenterDec() {
		return dataCenterDec;
	}

	public void setDataCenterDec(String dataCenterDec) {
		this.dataCenterDec = dataCenterDec;
	}

	public String getDataCenterAddress() {
		return dataCenterAddress;
	}

	public void setDataCenterAddress(String dataCenterAddress) {
		this.dataCenterAddress = dataCenterAddress;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getDataCenterLongitude() {
		return dataCenterLongitude;
	}

	public void setDataCenterLongitude(String dataCenterLongitude) {
		this.dataCenterLongitude = dataCenterLongitude;
	}

	public String getDataCenterLatitude() {
		return dataCenterLatitude;
	}

	public void setDataCenterLatitude(String dataCenterLatitude) {
		this.dataCenterLatitude = dataCenterLatitude;
	}

	/**
	 * @return the dataCenterCd
	 */
	public String getDataCenterCd() {
		return dataCenterCd;
	}

	/**
	 * @param dataCenterCd the dataCenterCd to set
	 */
	public void setDataCenterCd(String dataCenterCd) {
		this.dataCenterCd = dataCenterCd;
	}
	
	

}

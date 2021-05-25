package com.tcl.dias.products.izopc.beans;

import com.tcl.dias.productcatelog.entity.entities.ProductDataCentreAssoc;

/**
 * POJO class Product Data Center Provider related details
 * 
 *
 * @author vivek
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class ProductDataCenterProviderBean {
	
	private String productName;

	private String dataCenterCity;

	private String dataCenterSiteCode;

	private String dataCenterDec;

	private String dataCentreAddress;

	private String cloudProviderName;

	private String interfaceName;

	private String remarks;

	private Integer productCatalogId;

	private String dataCenterCd;

	private Integer providerId;

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getDataCenterCity() {
		return dataCenterCity;
	}

	public void setDataCenterCity(String dataCenterCity) {
		this.dataCenterCity = dataCenterCity;
	}

	public String getDataCenterSiteCode() {
		return dataCenterSiteCode;
	}

	public void setDataCenterSiteCode(String dataCenterSiteCode) {
		this.dataCenterSiteCode = dataCenterSiteCode;
	}

	public String getDataCenterDec() {
		return dataCenterDec;
	}

	public void setDataCenterDec(String dataCenterDec) {
		this.dataCenterDec = dataCenterDec;
	}

	public String getDataCentreAddress() {
		return dataCentreAddress;
	}

	public void setDataCentreAddress(String dataCentreAddress) {
		this.dataCentreAddress = dataCentreAddress;
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

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Integer getProductCatalogId() {
		return productCatalogId;
	}

	public void setProductCatalogId(Integer productCatalogId) {
		this.productCatalogId = productCatalogId;
	}

	public String getDataCenterCd() {
		return dataCenterCd;
	}

	public void setDataCenterCd(String dataCenterCd) {
		this.dataCenterCd = dataCenterCd;
	}

	public Integer getProviderId() {
		return providerId;
	}

	public void setProviderId(Integer providerId) {
		this.providerId = providerId;
	}

	public ProductDataCenterProviderBean(ProductDataCentreAssoc productDataCentreAssoc) {
		super();
		this.productName = productDataCentreAssoc.getProductName();
		this.dataCenterCity = productDataCentreAssoc.getDataCenterCity();
		this.dataCenterSiteCode = productDataCentreAssoc.getDataCentrSiteCode();
		this.dataCenterDec = productDataCentreAssoc.getDataCenterDec();
		this.dataCentreAddress = productDataCentreAssoc.getDataCenterAddress();
		this.cloudProviderName = productDataCentreAssoc.getCloudProviderName();
		this.interfaceName = productDataCentreAssoc.getInterfaceName();
		this.remarks = productDataCentreAssoc.getRemarks();
		this.productCatalogId = productDataCentreAssoc.getProductCatalogId();
		this.dataCenterCd = productDataCentreAssoc.getDataCenterCd();
		this.providerId = productDataCentreAssoc.getProviderId();
	}
	
	

}

package com.tcl.dias.beans;

/**
 * Bean to hold all uptime details which will be generated as Excel
 * 
 * @author KRUTSRIN
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 **/
public class UptimeExcelBean {
	private String accountName;
	private String customerServiceId;
	private String serviceType;
	private String productFlavour;
	private String legalEntity;
    private String CircuitUptimePercent;
	private String serviceOverallStatus;
	private String commissioningDate;
	private String bandwidth;
	private String aEndLLProvider;
	private String aEndSiteCity;
	private String aEndSiteAddress;
	private String bEndCity;
	private String bEndSiteAddress;
	private String priSec;
	public String getCustomerServiceId() {
		return customerServiceId;
	}
	public void setCustomerServiceId(String customerServiceId) {
		this.customerServiceId = customerServiceId;
	}
	public String getServiceType() {
		return serviceType;
	}
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
	public String getProductFlavour() {
		return productFlavour;
	}
	public void setProductFlavour(String productFlavour) {
		this.productFlavour = productFlavour;
	}
	public String getCircuitUptimePercent() {
		return CircuitUptimePercent;
	}
	public void setCircuitUptimePercent(String circuitUptimePercent) {
		CircuitUptimePercent = circuitUptimePercent;
	}
	public String getServiceOverallStatus() {
		return serviceOverallStatus;
	}
	public void setServiceOverallStatus(String serviceOverallStatus) {
		this.serviceOverallStatus = serviceOverallStatus;
	}
	
	public String getCommissioningDate() {
		return commissioningDate;
	}
	public void setCommissioningDate(String commissioningDate) {
		this.commissioningDate = commissioningDate;
	}
	public String getBandwidth() {
		return bandwidth;
	}
	public void setBandwidth(String bandwidth) {
		this.bandwidth = bandwidth;
	}
	
	public String getaEndSiteCity() {
		return aEndSiteCity;
	}
	public void setaEndSiteCity(String aEndSiteCity) {
		this.aEndSiteCity = aEndSiteCity;
	}
	public String getaEndSiteAddress() {
		return aEndSiteAddress;
	}
	public void setaEndSiteAddress(String aEndSiteAddress) {
		this.aEndSiteAddress = aEndSiteAddress;
	}
	public String getbEndCity() {
		return bEndCity;
	}
	public void setbEndCity(String bEndCity) {
		this.bEndCity = bEndCity;
	}
	public String getbEndSiteAddress() {
		return bEndSiteAddress;
	}
	public void setbEndSiteAddress(String bEndSiteAddress) {
		this.bEndSiteAddress = bEndSiteAddress;
	}
	public String getPriSec() {
		return priSec;
	}
	public void setPriSec(String priSec) {
		this.priSec = priSec;
	}
	
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public String getLegalEntity() {
		return legalEntity;
	}
	public void setLegalEntity(String legalEntity) {
		this.legalEntity = legalEntity;
	}
	public String getaEndLLProvider() {
		return aEndLLProvider;
	}
	public void setaEndLLProvider(String aEndLLProvider) {
		this.aEndLLProvider = aEndLLProvider;
	}	
	

}

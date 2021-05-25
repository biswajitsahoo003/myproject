package com.tcl.dias.serviceinventory.beans;

import java.sql.Timestamp;
import java.util.List;

/**
 * This file contains the SoultionsBean.java class.
 *
 *
 * @author DSIVALIN
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class SISolutionOffering {

	private String offeringName;

	private String siteAddress;

	private String siteAlias;

	private String slaCommittment;

	private String accessType;

	private String accessProvider;

	private String primaryOrSecondary;

	private String cpeProvider;

	private String location;

	private String latLong;

	private String usageModel;

	private String serviceStatus;

	private String commissioningDate;

	private String lastMileProvider;

	private String billingAccountId;
	private String billingAddress;
	private String billingGstNumber;
	private Integer siServiceDetailId;
	private String leId;
	private String leName;
	private String supplierLeId;
	private String supplierLeName;
	private String primaryServiceId;
	private String secondaryServiceId;
	private Integer siOrderId;
	private String taxExemptionFlag;
	private Timestamp contractStartDate;
	private Timestamp contractEndDate;
	private Double termInMonths;
	private List<ComponentBean> components;

	/**
	 * @return the offeringName
	 */
	public String getOfferingName() {
		return offeringName;
	}

	/**
	 * @param offeringName the offeringName to set
	 */
	public void setOfferingName(String offeringName) {
		this.offeringName = offeringName;
	}

	/**
	 * @return the components
	 */
	public List<ComponentBean> getComponents() {
		return components;
	}

	/**
	 * @param components the components to set
	 */
	public void setComponents(List<ComponentBean> components) {
		this.components = components;
	}

	/**
	 * @return the siteAddress
	 */
	public String getSiteAddress() {
		return siteAddress;
	}

	/**
	 * @param siteAddress the siteAddress to set
	 */
	public void setSiteAddress(String siteAddress) {
		this.siteAddress = siteAddress;
	}

	/**
	 * @return the siteAlias
	 */
	public String getSiteAlias() {
		return siteAlias;
	}

	/**
	 * @param siteAlias the siteAlias to set
	 */
	public void setSiteAlias(String siteAlias) {
		this.siteAlias = siteAlias;
	}

	/**
	 * @return the slaCommittment
	 */
	public String getSlaCommittment() {
		return slaCommittment;
	}

	/**
	 * @param slaCommittment the slaCommittment to set
	 */
	public void setSlaCommittment(String slaCommittment) {
		this.slaCommittment = slaCommittment;
	}

	/**
	 * @return the accessType
	 */
	public String getAccessType() {
		return accessType;
	}

	/**
	 * @param accessType the accessType to set
	 */
	public void setAccessType(String accessType) {
		this.accessType = accessType;
	}

	/**
	 * @return the accessProvider
	 */
	public String getAccessProvider() {
		return accessProvider;
	}

	/**
	 * @param accessProvider the accessProvider to set
	 */
	public void setAccessProvider(String accessProvider) {
		this.accessProvider = accessProvider;
	}

	/**
	 * @return the primaryOrSecondary
	 */
	public String getPrimaryOrSecondary() {
		return primaryOrSecondary;
	}

	/**
	 * @param primaryOrSecondary the primaryOrSecondary to set
	 */
	public void setPrimaryOrSecondary(String primaryOrSecondary) {
		this.primaryOrSecondary = primaryOrSecondary;
	}

	/**
	 * @return the cpeProvider
	 */
	public String getCpeProvider() {
		return cpeProvider;
	}

	/**
	 * @param cpeProvider the cpeProvider to set
	 */
	public void setCpeProvider(String cpeProvider) {
		this.cpeProvider = cpeProvider;
	}

	/**
	 * @return the location
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * @param location the location to set
	 */
	public void setLocation(String location) {
		this.location = location;
	}

	/**
	 * @return the latLong
	 */
	public String getLatLong() {
		return latLong;
	}

	/**
	 * @param latLong the latLong to set
	 */
	public void setLatLong(String latLong) {
		this.latLong = latLong;
	}

	/**
	 * @return the usageModel
	 */
	public String getUsageModel() {
		return usageModel;
	}

	/**
	 * @param usageModel the usageModel to set
	 */
	public void setUsageModel(String usageModel) {
		this.usageModel = usageModel;
	}

	/**
	 * @return the serviceStatus
	 */
	public String getServiceStatus() {
		return serviceStatus;
	}

	/**
	 * @param serviceStatus the serviceStatus to set
	 */
	public void setServiceStatus(String serviceStatus) {
		this.serviceStatus = serviceStatus;
	}

	/**
	 * @return the commissioningDate
	 */
	public String getCommissioningDate() {
		return commissioningDate;
	}

	/**
	 * @param commissioningDate the commissioningDate to set
	 */
	public void setCommissioningDate(String commissioningDate) {
		this.commissioningDate = commissioningDate;
	}

	/**
	 * @return the lastMileProvider
	 */
	public String getLastMileProvider() {
		return lastMileProvider;
	}

	/**
	 * @param lastMileProvider the lastMileProvider to set
	 */
	public void setLastMileProvider(String lastMileProvider) {
		this.lastMileProvider = lastMileProvider;
	}


	public String getBillingAccountId() {
		return billingAccountId;
	}

	public void setBillingAccountId(String billingAccountId) {
		this.billingAccountId = billingAccountId;
	}

	public String getBillingAddress() {
		return billingAddress;
	}

	public void setBillingAddress(String billingAddress) {
		this.billingAddress = billingAddress;
	}

	public String getBillingGstNumber() {
		return billingGstNumber;
	}

	public void setBillingGstNumber(String billingGstNumber) {
		this.billingGstNumber = billingGstNumber;
	}

	public Integer getSiServiceDetailId() {
		return siServiceDetailId;
	}

	public void setSiServiceDetailId(Integer siServiceDetailId) {
		this.siServiceDetailId = siServiceDetailId;
	}

	public String getLeId() {
		return leId;
	}

	public void setLeId(String leId) {
		this.leId = leId;
	}

	public String getLeName() {
		return leName;
	}

	public void setLeName(String leName) {
		this.leName = leName;
	}

	public String getSupplierLeId() {
		return supplierLeId;
	}

	public void setSupplierLeId(String supplierLeId) {
		this.supplierLeId = supplierLeId;
	}

	public String getSupplierLeName() {
		return supplierLeName;
	}

	public void setSupplierLeName(String supplierLeName) {
		this.supplierLeName = supplierLeName;
	}

	public String getPrimaryServiceId() {
		return primaryServiceId;
	}

	public void setPrimaryServiceId(String primaryServiceId) {
		this.primaryServiceId = primaryServiceId;
	}

	public String getSecondaryServiceId() {
		return secondaryServiceId;
	}

	public void setSecondaryServiceId(String secondaryServiceId) {
		this.secondaryServiceId = secondaryServiceId;
	}

	public Integer getSiOrderId() {
		return siOrderId;
	}

	public void setSiOrderId(Integer siOrderId) {
		this.siOrderId = siOrderId;
	}

	public String getTaxExemptionFlag() {
		return taxExemptionFlag;
	}

	public void setTaxExemptionFlag(String taxExemptionFlag) {
		this.taxExemptionFlag = taxExemptionFlag;
	}

	public Timestamp getContractStartDate() {
		return contractStartDate;
	}

	public void setContractStartDate(Timestamp contractStartDate) {
		this.contractStartDate = contractStartDate;
	}

	public Timestamp getContractEndDate() {
		return contractEndDate;
	}

	public void setContractEndDate(Timestamp contractEndDate) {
		this.contractEndDate = contractEndDate;
	}

	public Double getTermInMonths() {
		return termInMonths;
	}

	public void setTermInMonths(Double termInMonths) {
		this.termInMonths = termInMonths;
	}


}

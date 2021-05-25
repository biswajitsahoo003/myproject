package com.tcl.dias.serviceinventory.beans;

import java.util.Date;

/**
 * Bean class to hold attributes of SI Service
 * specific to Excel creation
 * @author KRUTSRIN
 *
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class SIServiceExcelBean {

	private String serviceId;
	private String siteType;
	private String billingAddress;
	private String taxExemptionFlag;
	private String billingGstNumber;
	public SIServiceExcelBean() {
	}

	private String siteLocationId;
	private String billingAccountId;
	private String orderID;
	private Date commissionedDate;
	private String prdFlavour;
	private String priSec;
	private String portSpeed;
	private String serviceTopology;
	private String aEndLlProvider;
	private String bEndLlProvider;
	private String sourceCity;
	private String destinationCity;
	private String sourceAddress;
	private String destinationAddress;
	private String aEndLlBandwidth;
	private String bEndLlBandwidth;
	private String accountName;
	private String legalEntity;
	private String serviceType;
	private String customerServiceID;
	private String alias;
	private String finalStatus;
	private String sourceCountry;
	private String serviceLink;
	private String scopeOfManagement;
	private String serviceOptionType;
	private String routingProtocol;
	private String aEndInterface;
	private String bEndInterface;
	private String  assetName;
	private String assetType;
	// voice inventory Attr
	// accname , legal entity share common
	private String orderSysID; // for voice inventory
	private String endCustName;
	private String originCity;
	private String accessNumber;
	private String outpulse;
	private String originNetwork;
	private String destCntryCode;
	private String accessType;
	private String parentID;
	private String parentService;
	private String accessNumberType;

	public String getAccessNumberType() {
		return accessNumberType;
	}

	public void setAccessNumberType(String accessNumberType) {
		this.accessNumberType = accessNumberType;
	}

	public String getaEndLlProvider() {
		return aEndLlProvider;
	}

	public void setaEndLlProvider(String aEndLlProvider) {
		this.aEndLlProvider = aEndLlProvider;
	}

	public String getbEndLlProvider() {
		return bEndLlProvider;
	}

	public void setbEndLlProvider(String bEndLlProvider) {
		this.bEndLlProvider = bEndLlProvider;
	}
	public String getSiteType() {
		return siteType;
	}

	public void setSiteType(String siteType) {
		this.siteType = siteType;
	}

	public String getOrderSysID() {
		return orderSysID;
	}

	public void setOrderSysID(String orderSysID) {
		this.orderSysID = orderSysID;
	}

	public String getBillingAddress() {
		return billingAddress;
	}

	public void setBillingAddress(String billingAddress) {
		this.billingAddress = billingAddress;
	}

	public String getTaxExemptionFlag() {
		return taxExemptionFlag;
	}

	public void setTaxExemptionFlag(String taxExemptionFlag) {
		this.taxExemptionFlag = taxExemptionFlag;
	}

	public String getBillingGstNumber() {
		return billingGstNumber;
	}

	public void setBillingGstNumber(String billingGstNumber) {
		this.billingGstNumber = billingGstNumber;
	}

	public String getSiteLocationId() {
		return siteLocationId;
	}

	public void setSiteLocationId(String siteLocationId) {
		this.siteLocationId = siteLocationId;
	}

	public String getBillingAccountId() {
		return billingAccountId;
	}

	public void setBillingAccountId(String billingAccountId) {
		this.billingAccountId = billingAccountId;
	}
	
	public String getOriginCity() {
		return originCity;
	}

	public void setOriginCity(String originCity) {
		this.originCity = originCity;
	}

	public String getAccessNumber() {
		return accessNumber;
	}

	public void setAccessNumber(String accessNumber) {
		this.accessNumber = accessNumber;
	}
	public String getOutpulse() {
		return outpulse;
	}

	public void setOutpulse(String outpulse) {
		this.outpulse = outpulse;
	}

	public String getOriginNetwork() {
		return originNetwork;
	}

	public void setOriginNetwork(String originNetwork) {
		this.originNetwork = originNetwork;
	}

	public String getDestCntryCode() {
		return destCntryCode;
	}

	public void setDestCntryCode(String destCntryCode) {
		this.destCntryCode = destCntryCode;
	}

	public String getAccessType() {
		return accessType;
	}

	public void setAccessType(String accessType) {
		this.accessType = accessType;
	}

	public String getParentID() {
		return parentID;
	}

	public void setParentID(String parentID) {
		this.parentID = parentID;
	}

	public String getParentService() {
		return parentService;
	}

	public void setParentService(String parentService) {
		this.parentService = parentService;
	}
	public String getScopeOfManagement() {
		return scopeOfManagement;
	}

	public void setScopeOfManagement(String scopeOfManagement) {
		this.scopeOfManagement = scopeOfManagement;
	}

	// ************************************************************
	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
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

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public String getPrdFlavour() {
		return prdFlavour;
	}

	public void setPrdFlavour(String prdFlavour) {
		this.prdFlavour = prdFlavour;
	}

	public String getOrderID() {
		return orderID;
	}

	public void setOrderID(String orderID) {
		this.orderID = orderID;
	}

	public String getCustomerServiceID() {
		return customerServiceID;
	}

	public void setCustomerServiceID(String customerServiceID) {
		this.customerServiceID = customerServiceID;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}
	
	public String getFinalStatus() {
		return finalStatus;
	}

	public void setFinalStatus(String finalStatus) {
		this.finalStatus = finalStatus;
	}

	public Date getCommissionedDate() {
		return commissionedDate;
	}

	public void setCommissionedDate(Date commissionedDate) {
		this.commissionedDate = commissionedDate;
	}

		public String getServiceOptionType() {
		return serviceOptionType;
	}

	public void setServiceOptionType(String serviceOptionType) {
		this.serviceOptionType = serviceOptionType;
	}

	public String getPriSec() {
		return priSec;
	}

	public void setPriSec(String priSec) {
		this.priSec = priSec;
	}

	public String getPortSpeed() {
		return portSpeed;
	}

	public void setPortSpeed(String portSpeed) {
		this.portSpeed = portSpeed;
	}

	public String getServiceTopology() {
		return serviceTopology;
	}

	public void setServiceTopology(String serviceTopology) {
		this.serviceTopology = serviceTopology;
	}

	public String getRoutingProtocol() {
		return routingProtocol;
	}

	public void setRoutingProtocol(String routingProtocol) {
		this.routingProtocol = routingProtocol;
	}

	public String getaEndInterface() {
		return aEndInterface;
	}

	public void setaEndInterface(String aEndInterface) {
		this.aEndInterface = aEndInterface;
	}

	public String getbEndInterface() {
		return bEndInterface;
	}

	public void setbEndInterface(String bEndInterface) {
		this.bEndInterface = bEndInterface;
	}

	public String getSourceCity() {
		return sourceCity;
	}

	public void setSourceCity(String sourceCity) {
		this.sourceCity = sourceCity;
	}

	public String getDestinationCity() {
		return destinationCity;
	}

	public void setDestinationCity(String destinationCity) {
		this.destinationCity = destinationCity;
	}

	public String getSourceAddress() {
		return sourceAddress;
	}

	public void setSourceAddress(String sourceAddress) {
		this.sourceAddress = sourceAddress;
	}

	public String getDestinationAddress() {
		return destinationAddress;
	}

	public void setDestinationAddress(String destinationAddress) {
		this.destinationAddress = destinationAddress;
	}

	public String getSourceCountry() {
		return sourceCountry;
	}

	public void setSourceCountry(String sourceCountry) {
		this.sourceCountry = sourceCountry;
	}
	
	public String getServiceLink() {
		return serviceLink;
	}

	public void setServiceLink(String serviceLink) {
		this.serviceLink = serviceLink;
	}
	
	public String getbEndLlBandwidth() {
		return bEndLlBandwidth;
	}

	public void setbEndLlBandwidth(String bEndLlBandwidth) {
		this.bEndLlBandwidth = bEndLlBandwidth;
	}
	
	public String getaEndLlBandwidth() {
		return aEndLlBandwidth;
	}

	public void setaEndLlBandwidth(String aEndLlBandwidth) {
		this.aEndLlBandwidth = aEndLlBandwidth;
	}

	public String getEndCustName() {
		return endCustName;
	}

	public void setEndCustName(String endCustName) {
		this.endCustName = endCustName;
	}
	
	public String getaEndllBandwidth() {
		return aEndLlBandwidth;
	}

	public void setaEndllBandwidth(String aEndllBandwidth) {
		this.aEndLlBandwidth = aEndllBandwidth;
	}

	public String getAssetName() {
		return assetName;
	}

	public void setAssetName(String assetName) {
		this.assetName = assetName;
	}

	public String getAssetType() {
		return assetType;
	}

	public void setAssetType(String assetType) {
		this.assetType = assetType;
	}

	@Override
	public String toString() {
		return "SIServiceExcelBean [serviceId=" + serviceId + ", siteType=" + siteType + ", billingAddress="
				+ billingAddress + ", taxExemptionFlag=" + taxExemptionFlag + ", billingGstNumber=" + billingGstNumber
				+ ", siteLocationId=" + siteLocationId + ", billingAccountId=" + billingAccountId + ", orderID="
				+ orderID + ", commissionedDate=" + commissionedDate + ", prdFlavour=" + prdFlavour + ", priSec="
				+ priSec + ", portSpeed=" + portSpeed + ", serviceTopology=" + serviceTopology + ", aEndLlProvider="
				+ aEndLlProvider + ", bEndLlProvider=" + bEndLlProvider + ", sourceCity=" + sourceCity
				+ ", destinationCity=" + destinationCity + ", sourceAddress=" + sourceAddress + ", destinationAddress="
				+ destinationAddress + ", aEndLlBandwidth=" + aEndLlBandwidth + ", bEndLlBandwidth=" + bEndLlBandwidth
				+ ", accountName=" + accountName + ", legalEntity=" + legalEntity + ", serviceType=" + serviceType
				+ ", customerServiceID=" + customerServiceID + ", alias=" + alias + ", finalStatus=" + finalStatus
				+ ", sourceCountry=" + sourceCountry + ", serviceLink=" + serviceLink + ", scopeOfManagement="
				+ scopeOfManagement + ", serviceOptionType=" + serviceOptionType + ", routingProtocol="
				+ routingProtocol + ", aEndInterface=" + aEndInterface + ", bEndInterface=" + bEndInterface
				+ ", assetName=" + assetName + ", assetType=" + assetType + ", orderSysID=" + orderSysID
				+ ", endCustName=" + endCustName + ", originCity=" + originCity + ", accessNumber=" + accessNumber
				+ ", outpulse=" + outpulse + ", originNetwork=" + originNetwork + ", destCntryCode=" + destCntryCode
				+ ", accessType=" + accessType + ", parentID=" + parentID + ", parentService=" + parentService
				+ ", accessNumberType=" + accessNumberType + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((customerServiceID == null) ? 0 : customerServiceID.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SIServiceExcelBean other = (SIServiceExcelBean) obj;
		if (customerServiceID == null) {
			if (other.customerServiceID != null)
				return false;
		} else if (!customerServiceID.equals(other.customerServiceID))
			return false;
		return true;
	}

	
}

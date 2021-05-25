package com.tcl.dias.serviceinventory.beans;

import java.io.Serializable;
import java.util.Date;

/**
 * Bean class to hold service detail and asset details
 * @author archchan
 *
 */
public class VwServiceAssetAttributeBean implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer sysId;
	private String serviceId;
	private String siteAlias;
	private Integer productFamilyId;
	private Integer productOfferingId;
	private String productFamilyName;
	private String productOfferingName;
	private String primaryServiceId;
	private String serviceRecordSequence;
	private String primaryOrSecondary;
	private String primarySecondaryLink;
	private String gscOrderSequenceId;
	private String latLong;
	private String serviceClassification;
	private String serviceManagementOption;
	private String gvpnSiteTopology;
	private String vpnTopology;
	private String vpnName;
	private String gvpnCos;
	private String customerSiteAddress;
	private String accessType;
	private String bandwidth;
	private String bandwidthUnit;
	private String bandwidthDisplayName;
	private String lastMileBandwidth;
	private String lastMileBandwidthUnit;
	private String lastMileBandwidthDisplayName;
	private String popAddress;
	private String popCode;
	private String sourceCountry;
	private String serviceStatus;
	private String commissionedDate;
	private String terminationDate;
	private String lastMileProvider;
	private String lastMileType;
	private String burstableBandwidth;
	private String burstableBandwidthUnit;
	private String burstableBandwidthDisplayName;
	private String destinationCountry;
	private String destinationCountryCode;
	private String destinationCountryCodeRepc;
	private String destinationCity;
	private String sourceCountryCode;
	private String sourceCountryCodeRepc;
	private String sourceCity;
	private String siteType;
	private String demarcationFloor;
	private String demarcationRoom;
	private Integer orderSysId;
	private String orderCode;
	private String opportunityType;
	private String orderDemoFlag;
	private Integer orderCustomerId;
	private String orderCustomer;
	private Integer orderCustLeId;
	private String orderCustLeName;
	private Integer orderSpLeId;
	private String orderSpLeName;
	private String orderPartner;
	private String orderPartnerName;
	private String orderInitiator;
	private String orderCreatedBy;
	private Date orderCreatedDate;
	private String orderIsActive;
	private String orderMultipleLeFlag;
	private String orderBundleOrderFlag;
	private Integer sfdcCuId;
	private String accountManager;
	private String accountManagerEmail;
	private String customerContact;
	private String customerContactEmail;
	private Double orderTermInMonths;
	private String billingFrequency;
	private Date contractStartDate;
	private Date contractEndDate;
	private Date lastMacdDate;
	private Double mrc;
	private Double nrc;
	private Double arc;
	private Double discountMrc;
	private Double discountNrc;
	private Double discountArc;
	private Integer currencyId;
	private String billingAddress;
	private String siteEndInterface;
	private String billingMethod;
	private Integer locationId;
	private String serviceType;
	private String additionalIpsReq;
	private String ipAddressArrangement;
	private String ipv4AddressPoolSize;
	private String ipv6AddressPoolSize;
	private Integer opportunityId;
	private String isActive;
	private String committedSla;
	private String partnerCuid;
	private String erfCustPartnerLeId;
	private String demarcationApartment;
	private String demarcationRack;
	private String izoSdwanSrvcId;
	private Integer assetId;
	private Integer assetSysId;
	private String assetServiceId;
	private String assetName;
	private String assetType;
	private String model;
	private String assetIsActive;
	private String scopeOfManagement;
	private String supportType;
	private String isSharedInd;
	private String assetAttrName;
	private String assetAttrValue;
	
	public Integer getSysId() {
		return sysId;
	}
	public void setSysId(Integer sysId) {
		this.sysId = sysId;
	}
	public String getServiceId() {
		return serviceId;
	}
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	public String getSiteAlias() {
		return siteAlias;
	}
	public void setSiteAlias(String siteAlias) {
		this.siteAlias = siteAlias;
	}
	public Integer getProductFamilyId() {
		return productFamilyId;
	}
	public void setProductFamilyId(Integer productFamilyId) {
		this.productFamilyId = productFamilyId;
	}
	public Integer getProductOfferingId() {
		return productOfferingId;
	}
	public void setProductOfferingId(Integer productOfferingId) {
		this.productOfferingId = productOfferingId;
	}
	public String getProductFamilyName() {
		return productFamilyName;
	}
	public void setProductFamilyName(String productFamilyName) {
		this.productFamilyName = productFamilyName;
	}
	public String getProductOfferingName() {
		return productOfferingName;
	}
	public void setProductOfferingName(String productOfferingName) {
		this.productOfferingName = productOfferingName;
	}
	public String getPrimaryServiceId() {
		return primaryServiceId;
	}
	public void setPrimaryServiceId(String primaryServiceId) {
		this.primaryServiceId = primaryServiceId;
	}
	public String getServiceRecordSequence() {
		return serviceRecordSequence;
	}
	public void setServiceRecordSequence(String serviceRecordSequence) {
		this.serviceRecordSequence = serviceRecordSequence;
	}
	public String getPrimaryOrSecondary() {
		return primaryOrSecondary;
	}
	public void setPrimaryOrSecondary(String primaryOrSecondary) {
		this.primaryOrSecondary = primaryOrSecondary;
	}
	public String getPrimarySecondaryLink() {
		return primarySecondaryLink;
	}
	public void setPrimarySecondaryLink(String primarySecondaryLink) {
		this.primarySecondaryLink = primarySecondaryLink;
	}
	public String getGscOrderSequenceId() {
		return gscOrderSequenceId;
	}
	public void setGscOrderSequenceId(String gscOrderSequenceId) {
		this.gscOrderSequenceId = gscOrderSequenceId;
	}
	public String getLatLong() {
		return latLong;
	}
	public void setLatLong(String latLong) {
		this.latLong = latLong;
	}
	public String getServiceClassification() {
		return serviceClassification;
	}
	public void setServiceClassification(String serviceClassification) {
		this.serviceClassification = serviceClassification;
	}
	public String getServiceManagementOption() {
		return serviceManagementOption;
	}
	public void setServiceManagementOption(String serviceManagementOption) {
		this.serviceManagementOption = serviceManagementOption;
	}
	public String getGvpnSiteTopology() {
		return gvpnSiteTopology;
	}
	public void setGvpnSiteTopology(String gvpnSiteTopology) {
		this.gvpnSiteTopology = gvpnSiteTopology;
	}
	public String getVpnTopology() {
		return vpnTopology;
	}
	public void setVpnTopology(String vpnTopology) {
		this.vpnTopology = vpnTopology;
	}
	public String getVpnName() {
		return vpnName;
	}
	public void setVpnName(String vpnName) {
		this.vpnName = vpnName;
	}
	public String getGvpnCos() {
		return gvpnCos;
	}
	public void setGvpnCos(String gvpnCos) {
		this.gvpnCos = gvpnCos;
	}
	public String getCustomerSiteAddress() {
		return customerSiteAddress;
	}
	public void setCustomerSiteAddress(String customerSiteAddress) {
		this.customerSiteAddress = customerSiteAddress;
	}
	public String getAccessType() {
		return accessType;
	}
	public void setAccessType(String accessType) {
		this.accessType = accessType;
	}
	public String getBandwidth() {
		return bandwidth;
	}
	public void setBandwidth(String bandwidth) {
		this.bandwidth = bandwidth;
	}
	public String getBandwidthUnit() {
		return bandwidthUnit;
	}
	public void setBandwidthUnit(String bandwidthUnit) {
		this.bandwidthUnit = bandwidthUnit;
	}
	public String getBandwidthDisplayName() {
		return bandwidthDisplayName;
	}
	public void setBandwidthDisplayName(String bandwidthDisplayName) {
		this.bandwidthDisplayName = bandwidthDisplayName;
	}
	public String getLastMileBandwidth() {
		return lastMileBandwidth;
	}
	public void setLastMileBandwidth(String lastMileBandwidth) {
		this.lastMileBandwidth = lastMileBandwidth;
	}
	public String getLastMileBandwidthUnit() {
		return lastMileBandwidthUnit;
	}
	public void setLastMileBandwidthUnit(String lastMileBandwidthUnit) {
		this.lastMileBandwidthUnit = lastMileBandwidthUnit;
	}
	public String getLastMileBandwidthDisplayName() {
		return lastMileBandwidthDisplayName;
	}
	public void setLastMileBandwidthDisplayName(String lastMileBandwidthDisplayName) {
		this.lastMileBandwidthDisplayName = lastMileBandwidthDisplayName;
	}
	public String getPopAddress() {
		return popAddress;
	}
	public void setPopAddress(String popAddress) {
		this.popAddress = popAddress;
	}
	public String getPopCode() {
		return popCode;
	}
	public void setPopCode(String popCode) {
		this.popCode = popCode;
	}
	public String getSourceCountry() {
		return sourceCountry;
	}
	public void setSourceCountry(String sourceCountry) {
		this.sourceCountry = sourceCountry;
	}
	public String getServiceStatus() {
		return serviceStatus;
	}
	public void setServiceStatus(String serviceStatus) {
		this.serviceStatus = serviceStatus;
	}
	public String getCommissionedDate() {
		return commissionedDate;
	}
	public void setCommissionedDate(String commissionedDate) {
		this.commissionedDate = commissionedDate;
	}
	public String getTerminationDate() {
		return terminationDate;
	}
	public void setTerminationDate(String terminationDate) {
		this.terminationDate = terminationDate;
	}
	public String getLastMileProvider() {
		return lastMileProvider;
	}
	public void setLastMileProvider(String lastMileProvider) {
		this.lastMileProvider = lastMileProvider;
	}
	public String getLastMileType() {
		return lastMileType;
	}
	public void setLastMileType(String lastMileType) {
		this.lastMileType = lastMileType;
	}
	public String getBurstableBandwidth() {
		return burstableBandwidth;
	}
	public void setBurstableBandwidth(String burstableBandwidth) {
		this.burstableBandwidth = burstableBandwidth;
	}
	public String getBurstableBandwidthUnit() {
		return burstableBandwidthUnit;
	}
	public void setBurstableBandwidthUnit(String burstableBandwidthUnit) {
		this.burstableBandwidthUnit = burstableBandwidthUnit;
	}
	public String getBurstableBandwidthDisplayName() {
		return burstableBandwidthDisplayName;
	}
	public void setBurstableBandwidthDisplayName(String burstableBandwidthDisplayName) {
		this.burstableBandwidthDisplayName = burstableBandwidthDisplayName;
	}
	public String getDestinationCountry() {
		return destinationCountry;
	}
	public void setDestinationCountry(String destinationCountry) {
		this.destinationCountry = destinationCountry;
	}
	public String getDestinationCountryCode() {
		return destinationCountryCode;
	}
	public void setDestinationCountryCode(String destinationCountryCode) {
		this.destinationCountryCode = destinationCountryCode;
	}
	public String getDestinationCountryCodeRepc() {
		return destinationCountryCodeRepc;
	}
	public void setDestinationCountryCodeRepc(String destinationCountryCodeRepc) {
		this.destinationCountryCodeRepc = destinationCountryCodeRepc;
	}
	public String getDestinationCity() {
		return destinationCity;
	}
	public void setDestinationCity(String destinationCity) {
		this.destinationCity = destinationCity;
	}
	public String getSourceCountryCode() {
		return sourceCountryCode;
	}
	public void setSourceCountryCode(String sourceCountryCode) {
		this.sourceCountryCode = sourceCountryCode;
	}
	public String getSourceCountryCodeRepc() {
		return sourceCountryCodeRepc;
	}
	public void setSourceCountryCodeRepc(String sourceCountryCodeRepc) {
		this.sourceCountryCodeRepc = sourceCountryCodeRepc;
	}
	public String getSourceCity() {
		return sourceCity;
	}
	public void setSourceCity(String sourceCity) {
		this.sourceCity = sourceCity;
	}
	public String getSiteType() {
		return siteType;
	}
	public void setSiteType(String siteType) {
		this.siteType = siteType;
	}
	public String getDemarcationFloor() {
		return demarcationFloor;
	}
	public void setDemarcationFloor(String demarcationFloor) {
		this.demarcationFloor = demarcationFloor;
	}
	public String getDemarcationRoom() {
		return demarcationRoom;
	}
	public void setDemarcationRoom(String demarcationRoom) {
		this.demarcationRoom = demarcationRoom;
	}
	public Integer getOrderSysId() {
		return orderSysId;
	}
	public void setOrderSysId(Integer orderSysId) {
		this.orderSysId = orderSysId;
	}
	public String getOrderCode() {
		return orderCode;
	}
	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}
	public String getOpportunityType() {
		return opportunityType;
	}
	public void setOpportunityType(String opportunityType) {
		this.opportunityType = opportunityType;
	}
	public String getOrderDemoFlag() {
		return orderDemoFlag;
	}
	public void setOrderDemoFlag(String orderDemoFlag) {
		this.orderDemoFlag = orderDemoFlag;
	}
	public Integer getOrderCustomerId() {
		return orderCustomerId;
	}
	public void setOrderCustomerId(Integer orderCustomerId) {
		this.orderCustomerId = orderCustomerId;
	}
	public String getOrderCustomer() {
		return orderCustomer;
	}
	public void setOrderCustomer(String orderCustomer) {
		this.orderCustomer = orderCustomer;
	}
	public Integer getOrderCustLeId() {
		return orderCustLeId;
	}
	public void setOrderCustLeId(Integer orderCustLeId) {
		this.orderCustLeId = orderCustLeId;
	}
	public String getOrderCustLeName() {
		return orderCustLeName;
	}
	public void setOrderCustLeName(String orderCustLeName) {
		this.orderCustLeName = orderCustLeName;
	}
	public Integer getOrderSpLeId() {
		return orderSpLeId;
	}
	public void setOrderSpLeId(Integer orderSpLeId) {
		this.orderSpLeId = orderSpLeId;
	}
	public String getOrderSpLeName() {
		return orderSpLeName;
	}
	public void setOrderSpLeName(String orderSpLeName) {
		this.orderSpLeName = orderSpLeName;
	}
	public String getOrderPartner() {
		return orderPartner;
	}
	public void setOrderPartner(String orderPartner) {
		this.orderPartner = orderPartner;
	}
	public String getOrderPartnerName() {
		return orderPartnerName;
	}
	public void setOrderPartnerName(String orderPartnerName) {
		this.orderPartnerName = orderPartnerName;
	}
	public String getOrderInitiator() {
		return orderInitiator;
	}
	public void setOrderInitiator(String orderInitiator) {
		this.orderInitiator = orderInitiator;
	}
	public String getOrderCreatedBy() {
		return orderCreatedBy;
	}
	public void setOrderCreatedBy(String orderCreatedBy) {
		this.orderCreatedBy = orderCreatedBy;
	}
	public Date getOrderCreatedDate() {
		return orderCreatedDate;
	}
	public void setOrderCreatedDate(Date orderCreatedDate) {
		this.orderCreatedDate = orderCreatedDate;
	}
	public String getOrderIsActive() {
		return orderIsActive;
	}
	public void setOrderIsActive(String orderIsActive) {
		this.orderIsActive = orderIsActive;
	}
	public String getOrderMultipleLeFlag() {
		return orderMultipleLeFlag;
	}
	public void setOrderMultipleLeFlag(String orderMultipleLeFlag) {
		this.orderMultipleLeFlag = orderMultipleLeFlag;
	}
	public String getOrderBundleOrderFlag() {
		return orderBundleOrderFlag;
	}
	public void setOrderBundleOrderFlag(String orderBundleOrderFlag) {
		this.orderBundleOrderFlag = orderBundleOrderFlag;
	}
	public Integer getSfdcCuId() {
		return sfdcCuId;
	}
	public void setSfdcCuId(Integer sfdcCuId) {
		this.sfdcCuId = sfdcCuId;
	}
	public String getAccountManager() {
		return accountManager;
	}
	public void setAccountManager(String accountManager) {
		this.accountManager = accountManager;
	}
	public String getAccountManagerEmail() {
		return accountManagerEmail;
	}
	public void setAccountManagerEmail(String accountManagerEmail) {
		this.accountManagerEmail = accountManagerEmail;
	}
	public String getCustomerContact() {
		return customerContact;
	}
	public void setCustomerContact(String customerContact) {
		this.customerContact = customerContact;
	}
	public String getCustomerContactEmail() {
		return customerContactEmail;
	}
	public void setCustomerContactEmail(String customerContactEmail) {
		this.customerContactEmail = customerContactEmail;
	}
	public Double getOrderTermInMonths() {
		return orderTermInMonths;
	}
	public void setOrderTermInMonths(Double orderTermInMonths) {
		this.orderTermInMonths = orderTermInMonths;
	}
	public String getBillingFrequency() {
		return billingFrequency;
	}
	public void setBillingFrequency(String billingFrequency) {
		this.billingFrequency = billingFrequency;
	}
	public Date getContractStartDate() {
		return contractStartDate;
	}
	public void setContractStartDate(Date contractStartDate) {
		this.contractStartDate = contractStartDate;
	}
	public Date getContractEndDate() {
		return contractEndDate;
	}
	public void setContractEndDate(Date contractEndDate) {
		this.contractEndDate = contractEndDate;
	}
	public Date getLastMacdDate() {
		return lastMacdDate;
	}
	public void setLastMacdDate(Date lastMacdDate) {
		this.lastMacdDate = lastMacdDate;
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
	public Double getDiscountMrc() {
		return discountMrc;
	}
	public void setDiscountMrc(Double discountMrc) {
		this.discountMrc = discountMrc;
	}
	public Double getDiscountNrc() {
		return discountNrc;
	}
	public void setDiscountNrc(Double discountNrc) {
		this.discountNrc = discountNrc;
	}
	public Double getDiscountArc() {
		return discountArc;
	}
	public void setDiscountArc(Double discountArc) {
		this.discountArc = discountArc;
	}
	public Integer getCurrencyId() {
		return currencyId;
	}
	public void setCurrencyId(Integer currencyId) {
		this.currencyId = currencyId;
	}
	public String getBillingAddress() {
		return billingAddress;
	}
	public void setBillingAddress(String billingAddress) {
		this.billingAddress = billingAddress;
	}
	public String getSiteEndInterface() {
		return siteEndInterface;
	}
	public void setSiteEndInterface(String siteEndInterface) {
		this.siteEndInterface = siteEndInterface;
	}
	public String getBillingMethod() {
		return billingMethod;
	}
	public void setBillingMethod(String billingMethod) {
		this.billingMethod = billingMethod;
	}
	public Integer getLocationId() {
		return locationId;
	}
	public void setLocationId(Integer locationId) {
		this.locationId = locationId;
	}
	public String getServiceType() {
		return serviceType;
	}
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
	public String getAdditionalIpsReq() {
		return additionalIpsReq;
	}
	public void setAdditionalIpsReq(String additionalIpsReq) {
		this.additionalIpsReq = additionalIpsReq;
	}
	public String getIpAddressArrangement() {
		return ipAddressArrangement;
	}
	public void setIpAddressArrangement(String ipAddressArrangement) {
		this.ipAddressArrangement = ipAddressArrangement;
	}
	public String getIpv4AddressPoolSize() {
		return ipv4AddressPoolSize;
	}
	public void setIpv4AddressPoolSize(String ipv4AddressPoolSize) {
		this.ipv4AddressPoolSize = ipv4AddressPoolSize;
	}
	public String getIpv6AddressPoolSize() {
		return ipv6AddressPoolSize;
	}
	public void setIpv6AddressPoolSize(String ipv6AddressPoolSize) {
		this.ipv6AddressPoolSize = ipv6AddressPoolSize;
	}
	public Integer getOpportunityId() {
		return opportunityId;
	}
	public void setOpportunityId(Integer opportunityId) {
		this.opportunityId = opportunityId;
	}
	public String getIsActive() {
		return isActive;
	}
	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}
	public String getCommittedSla() {
		return committedSla;
	}
	public void setCommittedSla(String committedSla) {
		this.committedSla = committedSla;
	}
	public String getPartnerCuid() {
		return partnerCuid;
	}
	public void setPartnerCuid(String partnerCuid) {
		this.partnerCuid = partnerCuid;
	}
	public String getErfCustPartnerLeId() {
		return erfCustPartnerLeId;
	}
	public void setErfCustPartnerLeId(String erfCustPartnerLeId) {
		this.erfCustPartnerLeId = erfCustPartnerLeId;
	}
	public String getDemarcationApartment() {
		return demarcationApartment;
	}
	public void setDemarcationApartment(String demarcationApartment) {
		this.demarcationApartment = demarcationApartment;
	}
	public String getDemarcationRack() {
		return demarcationRack;
	}
	public void setDemarcationRack(String demarcationRack) {
		this.demarcationRack = demarcationRack;
	}
	public String getIzoSdwanSrvcId() {
		return izoSdwanSrvcId;
	}
	public void setIzoSdwanSrvcId(String izoSdwanSrvcId) {
		this.izoSdwanSrvcId = izoSdwanSrvcId;
	}
	public Integer getAssetId() {
		return assetId;
	}
	public void setAssetId(Integer assetId) {
		this.assetId = assetId;
	}
	public Integer getAssetSysId() {
		return assetSysId;
	}
	public void setAssetSysId(Integer assetSysId) {
		this.assetSysId = assetSysId;
	}
	public String getAssetServiceId() {
		return assetServiceId;
	}
	public void setAssetServiceId(String assetServiceId) {
		this.assetServiceId = assetServiceId;
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
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getAssetIsActive() {
		return assetIsActive;
	}
	public void setAssetIsActive(String assetIsActive) {
		this.assetIsActive = assetIsActive;
	}
	public String getScopeOfManagement() {
		return scopeOfManagement;
	}
	public void setScopeOfManagement(String scopeOfManagement) {
		this.scopeOfManagement = scopeOfManagement;
	}
	public String getSupportType() {
		return supportType;
	}
	public void setSupportType(String supportType) {
		this.supportType = supportType;
	}
	public String getIsSharedInd() {
		return isSharedInd;
	}
	public void setIsSharedInd(String isSharedInd) {
		this.isSharedInd = isSharedInd;
	}
	public String getAssetAttrName() {
		return assetAttrName;
	}
	public void setAssetAttrName(String assetAttrName) {
		this.assetAttrName = assetAttrName;
	}
	public String getAssetAttrValue() {
		return assetAttrValue;
	}
	public void setAssetAttrValue(String assetAttrValue) {
		this.assetAttrValue = assetAttrValue;
	}
	
	
	
	
	
	

}

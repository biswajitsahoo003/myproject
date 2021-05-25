package com.tcl.dias.common.beans;

import java.util.Date;

/**
 * Bean class for service details
 * @author archchan
 *
 */
public class ServiceDetailedInfoBean {

	private String serviceId;
	private Integer id;
	private String productFamilyName;
	private Integer productFamilyId;
	private String productOfferingName;
	private Integer productOfferingId;
	private String primaryOrSecondary;
	private String primarySecondaryLink;
	private String isActive;
	private String serviceStatus;
	private String sourceCity;
	private String sourceCountry;
	private String siteAddress;
	private Integer locationId;
	private String siteType;
	private String latLong;
	private String serviceClassification;
	private String serviceManagementOption;
	private String accessType;
	private String lastMileProvider;
	private String bandwidth;
	private String lastMileBandwidth;
	private String bandwidthUnit;
	private String lastMileBandwidthUnit;
	private String lastMileType;
	private String siteEndInterface;
	private String opportunityType; 
	private Integer orderSysId;
	private String orderCode;
	private Integer orderCustomerId ; 
	private String orderCustomer; 
	private String orderCustLeName; 
	private Integer orderCustLeId; 
	private String partnerName; 
	private String orderPartner; 
	private String orderPartnerName; 
	private String erfCustPartnerLeId; 
	private Integer sfdcCuId; 
	private String partnerCuid; 
	private Double orderTermInMonths; 
	private String billingFrequency; 
	private String serviceVarient; 
	private String resiliencyInd; 
	private String serviceType; 
	private String billingCurrency; 
	private String paymentCurrency; 
	private String additionalIpFlag;
	private String accessRequired;
	private String additionalIps;
	private String additionalIpv4PoolSize;
	private String additionalIpv6PoolSize;
	private String additionalIpArrangement;
	private String additionalIpv4Count;
	private String additionalIpv6Count;
	private Integer assetAttrId;
	private String cpeModel;
	private Integer opportunityId;
	private Date circuitExpiryDate;
	private String izoSdwanSrvcId;
	private Date contractStartDate;
	private String accountManager;
	private String accountManagerEmail;
	private String remarks;
	public String getServiceId() {
		return serviceId;
	}
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getProductFamilyName() {
		return productFamilyName;
	}
	public void setProductFamilyName(String productFamilyName) {
		this.productFamilyName = productFamilyName;
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
	public String getProductOfferingName() {
		return productOfferingName;
	}
	public void setProductOfferingName(String productOfferingName) {
		this.productOfferingName = productOfferingName;
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
	public String getIsActive() {
		return isActive;
	}
	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}
	public String getServiceStatus() {
		return serviceStatus;
	}
	public void setServiceStatus(String serviceStatus) {
		this.serviceStatus = serviceStatus;
	}
	public String getSourceCity() {
		return sourceCity;
	}
	public void setSourceCity(String sourceCity) {
		this.sourceCity = sourceCity;
	}
	public String getSourceCountry() {
		return sourceCountry;
	}
	public void setSourceCountry(String sourceCountry) {
		this.sourceCountry = sourceCountry;
	}
	public String getSiteAddress() {
		return siteAddress;
	}
	public void setSiteAddress(String siteAddress) {
		this.siteAddress = siteAddress;
	}
	public Integer getLocationId() {
		return locationId;
	}
	public void setLocationId(Integer locationId) {
		this.locationId = locationId;
	}
	public String getSiteType() {
		return siteType;
	}
	public void setSiteType(String siteType) {
		this.siteType = siteType;
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
	public String getAccessType() {
		return accessType;
	}
	public void setAccessType(String accessType) {
		this.accessType = accessType;
	}
	public String getLastMileProvider() {
		return lastMileProvider;
	}
	public void setLastMileProvider(String lastMileProvider) {
		this.lastMileProvider = lastMileProvider;
	}
	public String getBandwidth() {
		return bandwidth;
	}
	public void setBandwidth(String bandwidth) {
		this.bandwidth = bandwidth;
	}
	public String getLastMileBandwidth() {
		return lastMileBandwidth;
	}
	public void setLastMileBandwidth(String lastMileBandwidth) {
		this.lastMileBandwidth = lastMileBandwidth;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getBandwidthUnit() {
		return bandwidthUnit;
	}
	public void setBandwidthUnit(String bandwidthUnit) {
		this.bandwidthUnit = bandwidthUnit;
	}
	public String getLastMileBandwidthUnit() {
		return lastMileBandwidthUnit;
	}
	public void setLastMileBandwidthUnit(String lastMileBandwidthUnit) {
		this.lastMileBandwidthUnit = lastMileBandwidthUnit;
	}
	public String getLastMileType() {
		return lastMileType;
	}
	public void setLastMileType(String lastMileType) {
		this.lastMileType = lastMileType;
	}
	public String getSiteEndInterface() {
		return siteEndInterface;
	}
	public void setSiteEndInterface(String siteEndInterface) {
		this.siteEndInterface = siteEndInterface;
	}
	public String getOpportunityType() {
		return opportunityType;
	}
	public void setOpportunityType(String opportunityType) {
		this.opportunityType = opportunityType;
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
	public String getOrderCustLeName() {
		return orderCustLeName;
	}
	public void setOrderCustLeName(String orderCustLeName) {
		this.orderCustLeName = orderCustLeName;
	}
	public Integer getOrderCustLeId() {
		return orderCustLeId;
	}
	public void setOrderCustLeId(Integer orderCustLeId) {
		this.orderCustLeId = orderCustLeId;
	}
	public String getPartnerName() {
		return partnerName;
	}
	public void setPartnerName(String partnerName) {
		this.partnerName = partnerName;
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
	public String getErfCustPartnerLeId() {
		return erfCustPartnerLeId;
	}
	public void setErfCustPartnerLeId(String erfCustPartnerLeId) {
		this.erfCustPartnerLeId = erfCustPartnerLeId;
	}
	public Integer getSfdcCuId() {
		return sfdcCuId;
	}
	public void setSfdcCuId(Integer sfdcCuId) {
		this.sfdcCuId = sfdcCuId;
	}
	public String getPartnerCuid() {
		return partnerCuid;
	}
	public void setPartnerCuid(String partnerCuid) {
		this.partnerCuid = partnerCuid;
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
	public String getServiceVarient() {
		return serviceVarient;
	}
	public void setServiceVarient(String serviceVarient) {
		this.serviceVarient = serviceVarient;
	}
	public String getResiliencyInd() {
		return resiliencyInd;
	}
	public void setResiliencyInd(String resiliencyInd) {
		this.resiliencyInd = resiliencyInd;
	}
	public String getServiceType() {
		return serviceType;
	}
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
	public String getBillingCurrency() {
		return billingCurrency;
	}
	public void setBillingCurrency(String billingCurrency) {
		this.billingCurrency = billingCurrency;
	}
	public String getPaymentCurrency() {
		return paymentCurrency;
	}
	public void setPaymentCurrency(String paymentCurrency) {
		this.paymentCurrency = paymentCurrency;
	}
	public String getAdditionalIpFlag() {
		return additionalIpFlag;
	}
	public void setAdditionalIpFlag(String additionalIpFlag) {
		this.additionalIpFlag = additionalIpFlag;
	}
	public String getAccessRequired() {
		return accessRequired;
	}
	public void setAccessRequired(String accessRequired) {
		this.accessRequired = accessRequired;
	}
	public String getAdditionalIps() {
		return additionalIps;
	}
	public void setAdditionalIps(String additionalIps) {
		this.additionalIps = additionalIps;
	}
	public String getAdditionalIpv4PoolSize() {
		return additionalIpv4PoolSize;
	}
	public void setAdditionalIpv4PoolSize(String additionalIpv4PoolSize) {
		this.additionalIpv4PoolSize = additionalIpv4PoolSize;
	}
	public String getAdditionalIpv6PoolSize() {
		return additionalIpv6PoolSize;
	}
	public void setAdditionalIpv6PoolSize(String additionalIpv6PoolSize) {
		this.additionalIpv6PoolSize = additionalIpv6PoolSize;
	}
	public String getAdditionalIpArrangement() {
		return additionalIpArrangement;
	}
	public void setAdditionalIpArrangement(String additionalIpArrangement) {
		this.additionalIpArrangement = additionalIpArrangement;
	}
	public String getadditionalIpv4Count() {
		return additionalIpv4Count;
	}
	public void setadditionalIpv4Count(String additionalIpv4Count) {
		this.additionalIpv4Count = additionalIpv4Count;
	}
	public String getAdditionalIpv6Count() {
		return additionalIpv6Count;
	}
	public void setAdditionalIpv6Count(String additionalIpv6Count) {
		this.additionalIpv6Count = additionalIpv6Count;
	}
	public Integer getAssetAttrId() {
		return assetAttrId;
	}
	public void setAssetAttrId(Integer assetAttrId) {
		this.assetAttrId = assetAttrId;
	}
	public String getCpeModel() {
		return cpeModel;
	}
	public void setCpeModel(String cpeModel) {
		this.cpeModel = cpeModel;
	}
	public Integer getOpportunityId() {
		return opportunityId;
	}
	public void setOpportunityId(Integer opportunityId) {
		this.opportunityId = opportunityId;
	}
	public Date getCircuitExpiryDate() {
		return circuitExpiryDate;
	}
	public void setCircuitExpiryDate(Date circuitExpiryDate) {
		this.circuitExpiryDate = circuitExpiryDate;
	}
	public String getIzoSdwanSrvcId() {
		return izoSdwanSrvcId;
	}
	public void setIzoSdwanSrvcId(String izoSdwanSrvcId) {
		this.izoSdwanSrvcId = izoSdwanSrvcId;
	}
	public Date getContractStartDate() {
		return contractStartDate;
	}
	public void setContractStartDate(Date contractStartDate) {
		this.contractStartDate = contractStartDate;
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
	@Override
	public String toString() {
		return "ServiceDetailedInfoBean [serviceId=" + serviceId + ", id=" + id + ", productFamilyName="
				+ productFamilyName + ", productFamilyId=" + productFamilyId + ", productOfferingName="
				+ productOfferingName + ", productOfferingId=" + productOfferingId + ", primaryOrSecondary="
				+ primaryOrSecondary + ", primarySecondaryLink=" + primarySecondaryLink + ", isActive=" + isActive
				+ ", serviceStatus=" + serviceStatus + ", sourceCity=" + sourceCity + ", sourceCountry=" + sourceCountry
				+ ", siteAddress=" + siteAddress + ", locationId=" + locationId + ", siteType=" + siteType
				+ ", latLong=" + latLong + ", serviceClassification=" + serviceClassification
				+ ", serviceManagementOption=" + serviceManagementOption + ", accessType=" + accessType
				+ ", lastMileProvider=" + lastMileProvider + ", bandwidth=" + bandwidth + ", lastMileBandwidth="
				+ lastMileBandwidth + ", bandwidthUnit=" + bandwidthUnit + ", lastMileBandwidthUnit="
				+ lastMileBandwidthUnit + ", lastMileType=" + lastMileType + ", siteEndInterface=" + siteEndInterface
				+ ", opportunityType=" + opportunityType + ", orderSysId=" + orderSysId + ", orderCode=" + orderCode
				+ ", orderCustomerId=" + orderCustomerId + ", orderCustomer=" + orderCustomer + ", orderCustLeName="
				+ orderCustLeName + ", orderCustLeId=" + orderCustLeId + ", partnerName=" + partnerName
				+ ", orderPartner=" + orderPartner + ", orderPartnerName=" + orderPartnerName + ", erfCustPartnerLeId="
				+ erfCustPartnerLeId + ", sfdcCuId=" + sfdcCuId + ", partnerCuid=" + partnerCuid
				+ ", orderTermInMonths=" + orderTermInMonths + ", billingFrequency=" + billingFrequency
				+ ", serviceVarient=" + serviceVarient + ", resiliencyInd=" + resiliencyInd + ", serviceType="
				+ serviceType + ", billingCurrency=" + billingCurrency + ", paymentCurrency=" + paymentCurrency
				+ ", additionalIpFlag=" + additionalIpFlag + ", accessRequired=" + accessRequired + ", additionalIps="
				+ additionalIps + ", additionalIpv4PoolSize=" + additionalIpv4PoolSize + ", additionalIpv6PoolSize="
				+ additionalIpv6PoolSize + ", additionalIpArrangement=" + additionalIpArrangement
				+ ", dditionalIpv4Count=" + additionalIpv4Count + ", additionalIpv6Count=" + additionalIpv6Count
				+ ", assetAttrId=" + assetAttrId + ", cpeModel=" + cpeModel + ", opportunityId=" + opportunityId
				+ ", circuitExpiryDate=" + circuitExpiryDate + ", izoSdwanSrvcId=" + izoSdwanSrvcId
				+ ", contractStartDate=" + contractStartDate + ", accountManager=" + accountManager
				+ ", accountManagerEmail=" + accountManagerEmail + ", remarks=" + remarks + "]";
	}
	

}

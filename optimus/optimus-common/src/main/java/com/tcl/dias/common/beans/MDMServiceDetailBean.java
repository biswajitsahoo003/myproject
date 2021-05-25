package com.tcl.dias.common.beans;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MDMServiceDetailBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String orderCode;
	private Integer siServiceDetailId;
	private String customerId;
	private String customerName;
	private String sfdcAccountId;
	private String sfdcCuid;
	private String leId;
	private String leName;
	private String customerSegment;
	private String opportunityClassification;
	private String supplierLeId;
	private String supplierLeName;
	private String serviceId;
	private Integer productId;
	private Integer offeringId;
	private String productName;
	private String offeringName;
	private String accessType;
	private String siteLinkLabel;
	private String latLong;
	private String siteAddress;
	private String siteTopology;
	private String serviceTopology;
	private String serviceClass;
	private String smName;
	private String smEmail;
	private String serviceClassification;
	private String siteType;
	private Date commissionedDate;
	private Date provisionedDate;
	private String sourceCity;
	private String destinationCity;
	private String portSpeed;
	private String portSpeedUnit;
	private String alias;
	private String linkType;
	private String primaryServiceId;
	private String secondaryServiceId;
	private String vpnName;
	private Integer siOrderId; 
	private Boolean isMacdInitiated =false;
	private String billingAccountId;
	private String siteLocationId;
	private String taxExemptionFlag;
	private String billingGstNumber;
	private String billingAddress;
	private Timestamp contractStartDate;
	private Timestamp contractEndDate;
	private Integer termInMonths;
	private String serviceStatus;
	private String ipAddressProvidedBy;
	private String isActive;
	private Timestamp orderDate;
	private String dcLocation;
	private String assetId;
	private Integer billingContactId;
	private String remarks;
	private String showCosMessage;
	private Integer serviceBandwidth;
	private String serviceBandwidthUnit;
	private String serviceState;
	private String serviceTerminationDate;
	private String billingFrequency;
	private String billingMethod;
	private String paymentTerm;
	private String countryName;
	private String erfCustPartnerId;
	private String erfCustPartnerLeId;
	private String erfCustPartnerName;
	private String partnerCuId;
	private String bodIdentifier;
	private String parentProductName;
	private String switchingUnit;
	private String ipAddress;
	//SDWANCP-399
	private String organisationName;
	
	//Cancellation Flow
	private String sourceSystem;
	private String opportunityId;
	private String orderType;
	private String orderCategory;
	private String siteCode;
	private Double mrc;
	private Double nrc;
	private Double arc;
	private Integer currencyId;
	private String billingCurrency;
	private String paymentCurrency;
	
    private String product;
    private String primarySecondary;
    private String allowCancellation;
    private String localLoopBandwidth;
    private String localLoopBandwidthUnit;
    private String localLoopBandwidthBEnd;
    private String linkCode;
    private String siteAddressBEnd;
    private String siteLocationIdBEnd;
    private String cloudCode;
	private String customerContactName;
	private String customerContactEmail;
	private String customerContactNumber;
	private String accountManager;
	private String programManager;
	private String scopeOfManagement;
	private String solutionType;
	private String cpeBasicChassis;
	private String poNumber;
	private String poDate;
	private String paymentType;
	private String paymentMethod;
	private String gstNumber;
	private String crnId;
	private String feasibilityId;
	private String billingType;
	private String isMultiVrf;
	private String cosModel;
	private String cosProfile;
	private String sltVariant;
	private String vpnTopology;
	private String cos1;
	private String cos2;
	private String cos3;
	private String cos4;
	private String cos5;
	private String cos6;
	private String additionalIPv4Address;
	private String wanIPAddressApprovedBy;
	private String siteInterface;
	private String connectorType;
	private String cpeManaged;
	private String cpeArrangedBy;
	private String equipmentMake;
	private String aEndConnectorType;
	private String bEndConnectorType;
	private String aEndChargeableDistance;
	private String bEndChargeableDistance;
	private String aEndInterface;
	private String bEndInterface;
	private String networkProtection;
	private String circuitType;
	private String portMode;
	private String copfId;
	private String nplProductFlavour;
	private String currencyCode;
	private String billingCurrencyPos;
	private String paymentCurrencyPos;
	private Double mrcPos;
	private Double nrcPos;
	private Double arcPos; 
	private String paymentMethodPos;
		
	public Integer getServiceBandwidth() {
		return serviceBandwidth;
	}

	public void setServiceBandwidth(Integer serviceBandwidth) {
		this.serviceBandwidth = serviceBandwidth;
	}

	public String getServiceBandwidthUnit() {
		return serviceBandwidthUnit;
	}

	public void setServiceBandwidthUnit(String serviceBandwidthUnit) {
		this.serviceBandwidthUnit = serviceBandwidthUnit;
	}

	public Date getProvisionedDate() {
		return provisionedDate;
	}

	public void setProvisionedDate(Date provisionedDate) {
		this.provisionedDate = provisionedDate;
	}

	public String getDcLocation() {
		return dcLocation;
	}

	public void setDcLocation(String dcLocation) {
		this.dcLocation = dcLocation;
	}

	public Timestamp getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Timestamp orderDate) {
		this.orderDate = orderDate;
	}

	public String getIpAddressProvidedBy() {
		return ipAddressProvidedBy;
	}

	public void setIpAddressProvidedBy(String ipAddressProvidedBy) {
		this.ipAddressProvidedBy = ipAddressProvidedBy;
	}

	public Boolean getMacdInitiated() {
		return isMacdInitiated;
	}

	public void setMacdInitiated(Boolean macdInitiated) {
		isMacdInitiated = macdInitiated;
	}

	public String getServiceStatus() {
		return serviceStatus;
	}

	public void setServiceStatus(String serviceStatus) {
		this.serviceStatus = serviceStatus;
	}

	public String getLastmileProvider() {
		return lastmileProvider;
	}

	public void setLastmileProvider(String lastmileProvider) {
		this.lastmileProvider = lastmileProvider;
	}

	private String lastmileProvider;


	private List<Map<String, Object>> serviceAssuranceContacts;

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getPortSpeed() {
		return portSpeed;
	}

	public void setPortSpeed(String portSpeed) {
		this.portSpeed = portSpeed;
	}

	public String getPortSpeedUnit() {
		return portSpeedUnit;
	}

	public void setPortSpeedUnit(String portSpeedUnit) {
		this.portSpeedUnit = portSpeedUnit;
	}

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getSfdcAccountId() {
		return sfdcAccountId;
	}

	public void setSfdcAccountId(String sfdcAccountId) {
		this.sfdcAccountId = sfdcAccountId;
	}

	public String getSfdcCuid() {
		return sfdcCuid;
	}

	public void setSfdcCuid(String sfdcCuid) {
		this.sfdcCuid = sfdcCuid;
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

	public String getCustomerSegment() {
		return customerSegment;
	}

	public void setCustomerSegment(String customerSegment) {
		this.customerSegment = customerSegment;
	}

	public String getOpportunityClassification() {
		return opportunityClassification;
	}

	public void setOpportunityClassification(String opportunityClassification) {
		this.opportunityClassification = opportunityClassification;
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

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public Integer getOfferingId() {
		return offeringId;
	}

	public void setOfferingId(Integer offeringId) {
		this.offeringId = offeringId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getOfferingName() {
		return offeringName;
	}

	public void setOfferingName(String offeringName) {
		this.offeringName = offeringName;
	}

	public String getAccessType() {
		return accessType;
	}

	public void setAccessType(String accessType) {
		this.accessType = accessType;
	}

	public String getSiteLinkLabel() {
		return siteLinkLabel;
	}

	public void setSiteLinkLabel(String siteLinkLabel) {
		this.siteLinkLabel = siteLinkLabel;
	}

	public String getLatLong() {
		return latLong;
	}

	public void setLatLong(String latLong) {
		this.latLong = latLong;
	}

	public String getSiteAddress() {
		return siteAddress;
	}

	public void setSiteAddress(String siteAddress) {
		this.siteAddress = siteAddress;
	}

	public String getSiteTopology() {
		return siteTopology;
	}

	public void setSiteTopology(String siteTopology) {
		this.siteTopology = siteTopology;
	}

	public String getServiceTopology() {
		return serviceTopology;
	}

	public void setServiceTopology(String serviceTopology) {
		this.serviceTopology = serviceTopology;
	}

	public String getServiceClass() {
		return serviceClass;
	}

	public void setServiceClass(String serviceClass) {
		this.serviceClass = serviceClass;
	}

	public String getSmName() {
		return smName;
	}

	public void setSmName(String smName) {
		this.smName = smName;
	}

	public String getSmEmail() {
		return smEmail;
	}

	public void setSmEmail(String smEmail) {
		this.smEmail = smEmail;
	}

	public String getServiceClassification() {
		return serviceClassification;
	}

	public void setServiceClassification(String serviceClassification) {
		this.serviceClassification = serviceClassification;
	}

	public String getSiteType() {
		return siteType;
	}

	public void setSiteType(String siteType) {
		this.siteType = siteType;
	}

	public Date getCommissionedDate() {
		return commissionedDate;
	}

	public void setCommissionedDate(Date commissionedDate) {
		this.commissionedDate = commissionedDate;
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

	public List<Map<String, Object>> getServiceAssuranceContacts() {
		return serviceAssuranceContacts;
	}

	public void setServiceAssuranceContacts(List<Map<String, Object>> serviceAssuranceContacts) {
		this.serviceAssuranceContacts = serviceAssuranceContacts;
	}

	public Integer getSiServiceDetailId() {
		return siServiceDetailId;
	}

	public void setSiServiceDetailId(Integer siServiceDetailId) {
		this.siServiceDetailId = siServiceDetailId;
	}
	
	public String getLinkType() {
		return linkType;
	}

	public void setLinkType(String linkType) {
		this.linkType = linkType;
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

	public String getVpnName() {
		return vpnName;
	}

	public void setVpnName(String vpnName) {
		this.vpnName = vpnName;
	}
	
	public Integer getSiOrderId() {
		return siOrderId;
	}

	public void setSiOrderId(Integer siOrderId) {
		this.siOrderId = siOrderId;
	}

	public Boolean getIsMacdInitiated() {
		return isMacdInitiated;
	}

	public void setIsMacdInitiated(Boolean isMacdInitiated) {
		this.isMacdInitiated = isMacdInitiated;
	}


	public String getBillingAccountId() {
		return billingAccountId;
	}

	public void setBillingAccountId(String billingAccountId) {
		this.billingAccountId = billingAccountId;
	}


	public String getSiteLocationId() {
		return siteLocationId;
	}

	public void setSiteLocationId(String siteLocationId) {
		this.siteLocationId = siteLocationId;
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

	public String getBillingAddress() {
		return billingAddress;
	}

	public void setBillingAddress(String billingAddress) {
		this.billingAddress = billingAddress;
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

	public Integer getTermInMonths() {
		return termInMonths;
	}

	public void setTermInMonths(Integer termInMonths) {
		this.termInMonths = termInMonths;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public String getAssetId() {
		return assetId;
	}

	public void setAssetId(String assetId) {
		this.assetId = assetId;
	}

	public Integer getBillingContactId() {
		return billingContactId;
	}

	public void setBillingContactId(Integer billingContactId) {
		this.billingContactId = billingContactId;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
	public String getShowCosMessage() {
		return showCosMessage;
	}

	public void setShowCosMessage(String showCosMessage) {
		this.showCosMessage = showCosMessage;
	}

	public String getServiceState() {
		return serviceState;
	}

	public void setServiceState(String serviceState) {
		this.serviceState = serviceState;
	}
	public String getServiceTerminationDate() {
		return serviceTerminationDate;
	}

	public void setServiceTerminationDate(String serviceTerminationDate) {
		this.serviceTerminationDate = serviceTerminationDate;
	}

	public String getBillingFrequency() { return billingFrequency; }

	public void setBillingFrequency(String billingFrequency) { this.billingFrequency = billingFrequency; }

	public String getBillingMethod() { return billingMethod; }

	public void setBillingMethod(String billingMethod) { this.billingMethod = billingMethod; }

	public String getPaymentTerm() { return paymentTerm; }

	public void setPaymentTerm(String paymentTerm) { this.paymentTerm = paymentTerm; }

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public String getErfCustPartnerId() {
		return erfCustPartnerId;
	}

	public void setErfCustPartnerId(String erfCustPartnerId) {
		this.erfCustPartnerId = erfCustPartnerId;
	}

	public String getErfCustPartnerLeId() {
		return erfCustPartnerLeId;
	}

	public void setErfCustPartnerLeId(String erfCustPartnerLeId) {
		this.erfCustPartnerLeId = erfCustPartnerLeId;
	}

	public String getErfCustPartnerName() {
		return erfCustPartnerName;
	}

	public void setErfCustPartnerName(String erfCustPartnerName) {
		this.erfCustPartnerName = erfCustPartnerName;
	}

	public String getPartnerCuId() {
		return partnerCuId;
	}

	public void setPartnerCuId(String partnerCuId) {
		this.partnerCuId = partnerCuId;
	}
	
	public String getBodIdentifier() {
		return bodIdentifier;
	}

	public void setBodIdentifier(String bodIdentifier) {
		this.bodIdentifier = bodIdentifier;
	}

	public String getParentProductName() {
		return parentProductName;
	}

	public void setParentProductName(String parentProductName) {
		this.parentProductName = parentProductName;
	}

	public String getSwitchingUnit() {
		return switchingUnit;
	}

	public void setSwitchingUnit(String switchingUnit) {
		this.switchingUnit = switchingUnit;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getOrganisationName() {
		return organisationName;
	}

	public void setOrganisationName(String organisationName) {
		this.organisationName = organisationName;
	}
	
	

	public String getSourceSystem() {
		return sourceSystem;
	}

	public void setSourceSystem(String sourceSystem) {
		this.sourceSystem = sourceSystem;
	}
	

	public String getOpportunityId() {
		return opportunityId;
	}

	public void setOpportunityId(String opportunityId) {
		this.opportunityId = opportunityId;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getOrderCategory() {
		return orderCategory;
	}

	public void setOrderCategory(String orderCategory) {
		this.orderCategory = orderCategory;
	}

	public String getSiteCode() {
		return siteCode;
	}

	public void setSiteCode(String siteCode) {
		this.siteCode = siteCode;
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

	public Integer getCurrencyId() {
		return currencyId;
	}

	public void setCurrencyId(Integer currencyId) {
		this.currencyId = currencyId;
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

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public String getPrimarySecondary() {
		return primarySecondary;
	}

	public void setPrimarySecondary(String primarySecondary) {
		this.primarySecondary = primarySecondary;
	}

	public String getAllowCancellation() {
		return allowCancellation;
	}

	public void setAllowCancellation(String allowCancellation) {
		this.allowCancellation = allowCancellation;
	}
	
	

	public String getLocalLoopBandwidth() {
		return localLoopBandwidth;
	}

	public void setLocalLoopBandwidth(String localLoopBandwidth) {
		this.localLoopBandwidth = localLoopBandwidth;
	}

	public String getLocalLoopBandwidthUnit() {
		return localLoopBandwidthUnit;
	}

	public void setLocalLoopBandwidthUnit(String localLoopBandwidthUnit) {
		this.localLoopBandwidthUnit = localLoopBandwidthUnit;
	}
	
	

	public String getLocalLoopBandwidthBEnd() {
		return localLoopBandwidthBEnd;
	}

	public void setLocalLoopBandwidthBEnd(String localLoopBandwidthBEnd) {
		this.localLoopBandwidthBEnd = localLoopBandwidthBEnd;
	}

	public String getLinkCode() {
		return linkCode;
	}

	public void setLinkCode(String linkCode) {
		this.linkCode = linkCode;
	}

	public String getSiteAddressBEnd() {
		return siteAddressBEnd;
	}

	public void setSiteAddressBEnd(String siteAddressBEnd) {
		this.siteAddressBEnd = siteAddressBEnd;
	}

	public String getSiteLocationIdBEnd() {
		return siteLocationIdBEnd;
	}

	public void setSiteLocationIdBEnd(String siteLocationIdBEnd) {
		this.siteLocationIdBEnd = siteLocationIdBEnd;
	}
	

	public String getCloudCode() {
		return cloudCode;
	}

	public void setCloudCode(String cloudCode) {
		this.cloudCode = cloudCode;
	}
	
	

	public String getCustomerContactName() {
		return customerContactName;
	}

	public void setCustomerContactName(String customerContactName) {
		this.customerContactName = customerContactName;
	}

	public String getCustomerContactEmail() {
		return customerContactEmail;
	}

	public void setCustomerContactEmail(String customerContactEmail) {
		this.customerContactEmail = customerContactEmail;
	}

	public String getCustomerContactNumber() {
		return customerContactNumber;
	}

	public void setCustomerContactNumber(String customerContactNumber) {
		this.customerContactNumber = customerContactNumber;
	}
	
	

	public String getAccountManager() {
		return accountManager;
	}

	public void setAccountManager(String accountManager) {
		this.accountManager = accountManager;
	}

	public String getProgramManager() {
		return programManager;
	}

	public void setProgramManager(String programManager) {
		this.programManager = programManager;
	}

	public String getScopeOfManagement() {
		return scopeOfManagement;
	}

	public void setScopeOfManagement(String scopeOfManagement) {
		this.scopeOfManagement = scopeOfManagement;
	}

	public String getSolutionType() {
		return solutionType;
	}

	public void setSolutionType(String solutionType) {
		this.solutionType = solutionType;
	}

	public String getCpeBasicChassis() {
		return cpeBasicChassis;
	}

	public void setCpeBasicChassis(String cpeBasicChassis) {
		this.cpeBasicChassis = cpeBasicChassis;
	}

	public String getPoNumber() {
		return poNumber;
	}

	public void setPoNumber(String poNumber) {
		this.poNumber = poNumber;
	}

	public String getPoDate() {
		return poDate;
	}

	public void setPoDate(String poDate) {
		this.poDate = poDate;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public String getGstNumber() {
		return gstNumber;
	}

	public void setGstNumber(String gstNumber) {
		this.gstNumber = gstNumber;
	}

	public String getCrnId() {
		return crnId;
	}

	public void setCrnId(String crnId) {
		this.crnId = crnId;
	}

	public String getFeasibilityId() {
		return feasibilityId;
	}

	public void setFeasibilityId(String feasibilityId) {
		this.feasibilityId = feasibilityId;
	}

	public String getBillingType() {
		return billingType;
	}

	public void setBillingType(String billingType) {
		this.billingType = billingType;
	}

	public String getIsMultiVrf() {
		return isMultiVrf;
	}

	public void setIsMultiVrf(String isMultiVrf) {
		this.isMultiVrf = isMultiVrf;
	}

	public String getCosModel() {
		return cosModel;
	}

	public void setCosModel(String cosModel) {
		this.cosModel = cosModel;
	}

	public String getCosProfile() {
		return cosProfile;
	}

	public void setCosProfile(String cosProfile) {
		this.cosProfile = cosProfile;
	}

	public String getSltVariant() {
		return sltVariant;
	}

	public void setSltVariant(String sltVariant) {
		this.sltVariant = sltVariant;
	}

	public String getVpnTopology() {
		return vpnTopology;
	}

	public void setVpnTopology(String vpnTopology) {
		this.vpnTopology = vpnTopology;
	}

	public String getCos1() {
		return cos1;
	}

	public void setCos1(String cos1) {
		this.cos1 = cos1;
	}

	public String getCos2() {
		return cos2;
	}

	public void setCos2(String cos2) {
		this.cos2 = cos2;
	}

	public String getCos3() {
		return cos3;
	}

	public void setCos3(String cos3) {
		this.cos3 = cos3;
	}

	public String getCos4() {
		return cos4;
	}

	public void setCos4(String cos4) {
		this.cos4 = cos4;
	}

	public String getCos5() {
		return cos5;
	}

	public void setCos5(String cos5) {
		this.cos5 = cos5;
	}

	public String getCos6() {
		return cos6;
	}

	public void setCos6(String cos6) {
		this.cos6 = cos6;
	}

	public String getAdditionalIPv4Address() {
		return additionalIPv4Address;
	}

	public void setAdditionalIPv4Address(String additionalIPv4Address) {
		this.additionalIPv4Address = additionalIPv4Address;
	}

	public String getWanIPAddressApprovedBy() {
		return wanIPAddressApprovedBy;
	}

	public void setWanIPAddressApprovedBy(String wanIPAddressApprovedBy) {
		this.wanIPAddressApprovedBy = wanIPAddressApprovedBy;
	}

	public String getSiteInterface() {
		return siteInterface;
	}

	public void setSiteInterface(String siteInterface) {
		this.siteInterface = siteInterface;
	}

	public String getConnectorType() {
		return connectorType;
	}

	public void setConnectorType(String connectorType) {
		this.connectorType = connectorType;
	}

	public String getCpeManaged() {
		return cpeManaged;
	}

	public void setCpeManaged(String cpeManaged) {
		this.cpeManaged = cpeManaged;
	}

	public String getCpeArrangedBy() {
		return cpeArrangedBy;
	}

	public void setCpeArrangedBy(String cpeArrangedBy) {
		this.cpeArrangedBy = cpeArrangedBy;
	}

	public String getEquipmentMake() {
		return equipmentMake;
	}

	public void setEquipmentMake(String equipmentMake) {
		this.equipmentMake = equipmentMake;
	}

	public String getaEndConnectorType() {
		return aEndConnectorType;
	}

	public void setaEndConnectorType(String aEndConnectorType) {
		this.aEndConnectorType = aEndConnectorType;
	}

	public String getbEndConnectorType() {
		return bEndConnectorType;
	}

	public void setbEndConnectorType(String bEndConnectorType) {
		this.bEndConnectorType = bEndConnectorType;
	}

	public String getaEndChargeableDistance() {
		return aEndChargeableDistance;
	}

	public void setaEndChargeableDistance(String aEndChargeableDistance) {
		this.aEndChargeableDistance = aEndChargeableDistance;
	}

	public String getbEndChargeableDistance() {
		return bEndChargeableDistance;
	}

	public void setbEndChargeableDistance(String bEndChargeableDistance) {
		this.bEndChargeableDistance = bEndChargeableDistance;
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

	public String getNetworkProtection() {
		return networkProtection;
	}

	public void setNetworkProtection(String networkProtection) {
		this.networkProtection = networkProtection;
	}

	public String getCircuitType() {
		return circuitType;
	}

	public void setCircuitType(String circuitType) {
		this.circuitType = circuitType;
	}

	public String getPortMode() {
		return portMode;
	}

	public void setPortMode(String portMode) {
		this.portMode = portMode;
	}
	
	

	public String getCopfId() {
		return copfId;
	}

	public void setCopfId(String copfId) {
		this.copfId = copfId;
	}
	
	

	public String getNplProductFlavour() {
		return nplProductFlavour;
	}

	public void setNplProductFlavour(String nplProductFlavour) {
		this.nplProductFlavour = nplProductFlavour;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public String getBillingCurrencyPos() {
		return billingCurrencyPos;
	}

	public void setBillingCurrencyPos(String billingCurrencyPos) {
		this.billingCurrencyPos = billingCurrencyPos;
	}

	public String getPaymentCurrencyPos() {
		return paymentCurrencyPos;
	}

	public void setPaymentCurrencyPos(String paymentCurrencyPos) {
		this.paymentCurrencyPos = paymentCurrencyPos;
	}

	public Double getMrcPos() {
		return mrcPos;
	}

	public void setMrcPos(Double mrcPos) {
		this.mrcPos = mrcPos;
	}

	public Double getNrcPos() {
		return nrcPos;
	}

	public void setNrcPos(Double nrcPos) {
		this.nrcPos = nrcPos;
	}

	public Double getArcPos() {
		return arcPos;
	}

	public void setArcPos(Double arcPos) {
		this.arcPos = arcPos;
	}

	public String getPaymentMethodPos() {
		return paymentMethodPos;
	}

	public void setPaymentMethodPos(String paymentMethodPos) {
		this.paymentMethodPos = paymentMethodPos;
	}

	@Override
	public String toString() {
		return "MDMServiceDetailBean [orderCode=" + orderCode + ", siServiceDetailId=" + siServiceDetailId
				+ ", customerId=" + customerId + ", customerName=" + customerName + ", sfdcAccountId=" + sfdcAccountId
				+ ", sfdcCuid=" + sfdcCuid + ", leId=" + leId + ", leName=" + leName + ", customerSegment="
				+ customerSegment + ", opportunityClassification=" + opportunityClassification + ", supplierLeId="
				+ supplierLeId + ", supplierLeName=" + supplierLeName + ", serviceId=" + serviceId + ", productId="
				+ productId + ", offeringId=" + offeringId + ", productName=" + productName + ", offeringName="
				+ offeringName + ", accessType=" + accessType + ", siteLinkLabel=" + siteLinkLabel + ", latLong="
				+ latLong + ", siteAddress=" + siteAddress + ", siteTopology=" + siteTopology + ", serviceTopology="
				+ serviceTopology + ", serviceClass=" + serviceClass + ", smName=" + smName + ", smEmail=" + smEmail
				+ ", serviceClassification=" + serviceClassification + ", siteType=" + siteType + ", commissionedDate="
				+ commissionedDate + ", provisionedDate=" + provisionedDate + ", sourceCity=" + sourceCity
				+ ", destinationCity=" + destinationCity + ", portSpeed=" + portSpeed + ", portSpeedUnit="
				+ portSpeedUnit + ", alias=" + alias + ", linkType=" + linkType + ", primaryServiceId="
				+ primaryServiceId + ", secondaryServiceId=" + secondaryServiceId + ", vpnName=" + vpnName
				+ ", siOrderId=" + siOrderId + ", isMacdInitiated=" + isMacdInitiated + ", billingAccountId="
				+ billingAccountId + ", siteLocationId=" + siteLocationId + ", taxExemptionFlag=" + taxExemptionFlag
				+ ", billingGstNumber=" + billingGstNumber + ", billingAddress=" + billingAddress
				+ ", contractStartDate=" + contractStartDate + ", contractEndDate=" + contractEndDate
				+ ", termInMonths=" + termInMonths + ", serviceStatus=" + serviceStatus + ", ipAddressProvidedBy="
				+ ipAddressProvidedBy + ", isActive=" + isActive + ", orderDate=" + orderDate + ", dcLocation="
				+ dcLocation + ", assetId=" + assetId + ", billingContactId=" + billingContactId + ", remarks="
				+ remarks + ", showCosMessage=" + showCosMessage + ", serviceBandwidth=" + serviceBandwidth
				+ ", serviceBandwidthUnit=" + serviceBandwidthUnit + ", serviceState=" + serviceState
				+ ", serviceTerminationDate=" + serviceTerminationDate + ", billingFrequency=" + billingFrequency
				+ ", billingMethod=" + billingMethod + ", paymentTerm=" + paymentTerm + ", countryName=" + countryName
				+ ", erfCustPartnerId=" + erfCustPartnerId + ", erfCustPartnerLeId=" + erfCustPartnerLeId
				+ ", erfCustPartnerName=" + erfCustPartnerName + ", partnerCuId=" + partnerCuId + ", bodIdentifier="
				+ bodIdentifier + ", parentProductName=" + parentProductName + ", switchingUnit=" + switchingUnit
				+ ", ipAddress=" + ipAddress + ", organisationName=" + organisationName + ", sourceSystem="
				+ sourceSystem + ", opportunityId=" + opportunityId + ", orderType=" + orderType + ", orderCategory="
				+ orderCategory + ", siteCode=" + siteCode + ", mrc=" + mrc + ", nrc=" + nrc + ", arc=" + arc
				+ ", currencyId=" + currencyId + ", billingCurrency=" + billingCurrency + ", paymentCurrency="
				+ paymentCurrency + ", product=" + product + ", primarySecondary=" + primarySecondary
				+ ", allowCancellation=" + allowCancellation + ", localLoopBandwidth=" + localLoopBandwidth
				+ ", localLoopBandwidthUnit=" + localLoopBandwidthUnit + ", localLoopBandwidthBEnd="
				+ localLoopBandwidthBEnd + ", linkCode=" + linkCode + ", siteAddressBEnd=" + siteAddressBEnd
				+ ", siteLocationIdBEnd=" + siteLocationIdBEnd + ", cloudCode=" + cloudCode + ", customerContactName="
				+ customerContactName + ", customerContactEmail=" + customerContactEmail + ", customerContactNumber="
				+ customerContactNumber + ", accountManager=" + accountManager + ", programManager=" + programManager
				+ ", scopeOfManagement=" + scopeOfManagement + ", solutionType=" + solutionType + ", cpeBasicChassis="
				+ cpeBasicChassis + ", poNumber=" + poNumber + ", poDate=" + poDate + ", paymentType=" + paymentType
				+ ", paymentMethod=" + paymentMethod + ", gstNumber=" + gstNumber + ", crnId=" + crnId
				+ ", feasibilityId=" + feasibilityId + ", billingType=" + billingType + ", isMultiVrf=" + isMultiVrf
				+ ", cosModel=" + cosModel + ", cosProfile=" + cosProfile + ", sltVariant=" + sltVariant
				+ ", vpnTopology=" + vpnTopology + ", cos1=" + cos1 + ", cos2=" + cos2 + ", cos3=" + cos3 + ", cos4="
				+ cos4 + ", cos5=" + cos5 + ", cos6=" + cos6 + ", additionalIPv4Address=" + additionalIPv4Address
				+ ", wanIPAddressApprovedBy=" + wanIPAddressApprovedBy + ", siteInterface=" + siteInterface
				+ ", connectorType=" + connectorType + ", cpeManaged=" + cpeManaged + ", cpeArrangedBy=" + cpeArrangedBy
				+ ", equipmentMake=" + equipmentMake + ", aEndConnectorType=" + aEndConnectorType
				+ ", bEndConnectorType=" + bEndConnectorType + ", aEndChargeableDistance=" + aEndChargeableDistance
				+ ", bEndChargeableDistance=" + bEndChargeableDistance + ", aEndInterface=" + aEndInterface
				+ ", bEndInterface=" + bEndInterface + ", networkProtection=" + networkProtection + ", circuitType="
				+ circuitType + ", portMode=" + portMode + ", copfId=" + copfId + ", nplProductFlavour="
				+ nplProductFlavour + ", currencyCode=" + currencyCode + ", billingCurrencyPos=" + billingCurrencyPos
				+ ", paymentCurrencyPos=" + paymentCurrencyPos + ", mrcPos=" + mrcPos + ", nrcPos=" + nrcPos
				+ ", arcPos=" + arcPos + ", paymentMethodPos=" + paymentMethodPos + ", lastmileProvider="
				+ lastmileProvider + ", serviceAssuranceContacts=" + serviceAssuranceContacts + "]";
	}

	
	
	
}

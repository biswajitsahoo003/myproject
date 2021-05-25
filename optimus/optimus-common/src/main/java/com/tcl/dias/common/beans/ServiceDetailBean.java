package com.tcl.dias.common.beans;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ServiceDetailBean implements Serializable {
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
	private Double termInMonths;
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
	
    private Boolean isMasterVrf=false;
    
    private MasterVRFBean primaryMasterVRFDetail;
    
    private MasterVRFBean secondaryMasterVRFDetail;
    
	private String multiVrfSolution;
	private Timestamp circuitExpiryDate;
	private String associateBillableId;
	private String billingType;
	private String cloudProvider;
	private String crossConnectType;
	

	public String getCloudProvider() {
		return cloudProvider;
	}

	public void setCloudProvider(String cloudProvider) {
		this.cloudProvider = cloudProvider;
	}

	public MasterVRFBean getPrimaryMasterVRFDetail() {
		return primaryMasterVRFDetail;
	}

	public void setPrimaryMasterVRFDetail(MasterVRFBean primaryMasterVRFDetail) {
		this.primaryMasterVRFDetail = primaryMasterVRFDetail;
	}

	public MasterVRFBean getSecondaryMasterVRFDetail() {
		return secondaryMasterVRFDetail;
	}

	public void setSecondaryMasterVRFDetail(MasterVRFBean secondaryMasterVRFDetail) {
		this.secondaryMasterVRFDetail = secondaryMasterVRFDetail;
	}

	public Boolean getIsMasterVrf() {
		return isMasterVrf;
	}

	public void setIsMasterVrf(Boolean isMasterVrf) {
		this.isMasterVrf = isMasterVrf;
	}
	
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

	public Double getTermInMonths() {
		return termInMonths;
	}

	public void setTermInMonths(Double termInMonths) {
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
	public String getMultiVrfSolution() {
		return multiVrfSolution;
	}

	public void setMultiVrfSolution(String multiVrfSolution) {
		this.multiVrfSolution = multiVrfSolution;
	}

	public Timestamp getCircuitExpiryDate() {
		return circuitExpiryDate;
	}

	public void setCircuitExpiryDate(Timestamp circuitExpiryDate) {
		this.circuitExpiryDate = circuitExpiryDate;
	}

	public String getAssociateBillableId() {
		return associateBillableId;
	}

	public void setAssociateBillableId(String associateBillableId) {
		this.associateBillableId = associateBillableId;
	}

	public String getBillingType() {
		return billingType;
	}

	public void setBillingType(String billingType) {
		this.billingType = billingType;
	}

	public String getCrossConnectType() {
		return crossConnectType;
	}

	public void setCrossConnectType(String crossConnectType) {
		this.crossConnectType = crossConnectType;
	}

	@Override
	public String toString() {
		return "ServiceDetailBean{" +
				"orderCode='" + orderCode + '\'' +
				", siServiceDetailId=" + siServiceDetailId +
				", customerId='" + customerId + '\'' +
				", customerName='" + customerName + '\'' +
				", sfdcAccountId='" + sfdcAccountId + '\'' +
				", sfdcCuid='" + sfdcCuid + '\'' +
				", leId='" + leId + '\'' +
				", leName='" + leName + '\'' +
				", customerSegment='" + customerSegment + '\'' +
				", opportunityClassification='" + opportunityClassification + '\'' +
				", supplierLeId='" + supplierLeId + '\'' +
				", supplierLeName='" + supplierLeName + '\'' +
				", serviceId='" + serviceId + '\'' +
				", productId=" + productId +
				", offeringId=" + offeringId +
				", productName='" + productName + '\'' +
				", offeringName='" + offeringName + '\'' +
				", accessType='" + accessType + '\'' +
				", siteLinkLabel='" + siteLinkLabel + '\'' +
				", latLong='" + latLong + '\'' +
				", siteAddress='" + siteAddress + '\'' +
				", siteTopology='" + siteTopology + '\'' +
				", serviceTopology='" + serviceTopology + '\'' +
				", serviceClass='" + serviceClass + '\'' +
				", smName='" + smName + '\'' +
				", smEmail='" + smEmail + '\'' +
				", serviceClassification='" + serviceClassification + '\'' +
				", siteType='" + siteType + '\'' +
				", commissionedDate=" + commissionedDate +
				", provisionedDate=" + provisionedDate +
				", sourceCity='" + sourceCity + '\'' +
				", destinationCity='" + destinationCity + '\'' +
				", portSpeed='" + portSpeed + '\'' +
				", portSpeedUnit='" + portSpeedUnit + '\'' +
				", alias='" + alias + '\'' +
				", linkType='" + linkType + '\'' +
				", primaryServiceId='" + primaryServiceId + '\'' +
				", secondaryServiceId='" + secondaryServiceId + '\'' +
				", vpnName='" + vpnName + '\'' +
				", siOrderId=" + siOrderId +
				", isMacdInitiated=" + isMacdInitiated +
				", billingAccountId='" + billingAccountId + '\'' +
				", siteLocationId='" + siteLocationId + '\'' +
				", taxExemptionFlag='" + taxExemptionFlag + '\'' +
				", billingGstNumber='" + billingGstNumber + '\'' +
				", billingAddress='" + billingAddress + '\'' +
				", contractStartDate=" + contractStartDate +
				", contractEndDate=" + contractEndDate +
				", termInMonths=" + termInMonths +
				", serviceStatus='" + serviceStatus + '\'' +
				", ipAddressProvidedBy='" + ipAddressProvidedBy + '\'' +
				", isActive='" + isActive + '\'' +
				", orderDate=" + orderDate +
				", dcLocation='" + dcLocation + '\'' +
				", assetId='" + assetId + '\'' +
				", billingContactId=" + billingContactId +
				", remarks='" + remarks + '\'' +
				", showCosMessage='" + showCosMessage + '\'' +
				", serviceBandwidth=" + serviceBandwidth +
				", serviceBandwidthUnit='" + serviceBandwidthUnit + '\'' +
				", serviceState='" + serviceState + '\'' +
				", serviceTerminationDate='" + serviceTerminationDate + '\'' +
				", billingFrequency='" + billingFrequency + '\'' +
				", billingMethod='" + billingMethod + '\'' +
				", billingType='" + billingType + '\'' +
				", paymentTerm='" + paymentTerm + '\'' +
				", countryName='" + countryName + '\'' +
				", erfCustPartnerId='" + erfCustPartnerId + '\'' +
				", erfCustPartnerLeId='" + erfCustPartnerLeId + '\'' +
				", erfCustPartnerName='" + erfCustPartnerName + '\'' +
				", partnerCuId='" + partnerCuId + '\'' +
				", parentProductName='" + parentProductName + '\'' +
				", switchingUnit='" + switchingUnit + '\'' +
				", ipAddress='" + ipAddress + '\'' +
				", lastmileProvider='" + lastmileProvider + '\'' +
				", serviceAssuranceContacts=" + serviceAssuranceContacts +
				", bodIdentifier=" + bodIdentifier +
				", circuitExpiryDate=" + circuitExpiryDate +
				", associateBillableId=" + associateBillableId +
				", organisationName=" + organisationName +
				", crossConnectType=" + crossConnectType +
				'}';
	}
}

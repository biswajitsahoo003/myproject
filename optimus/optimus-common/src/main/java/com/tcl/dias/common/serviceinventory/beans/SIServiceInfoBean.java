package com.tcl.dias.common.serviceinventory.beans;

import java.util.Date;
import java.util.List;
import java.util.Set;

import com.tcl.dias.common.beans.SIComponentBean;

/**
 * Bean class to hold SI service detail
 *
 * @author ANNE NISHA
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class SIServiceInfoBean {

	private Integer id;

	private String tpsServiceId;

	private String vpnId;

	private Integer siOrderId;

	private String serviceTopology;

	private String siteTopology;

	private String siteType;

	private String siteAddress;

	private String productName;

	private String parentProductName;

	private String parentProductOfferingName;

	private String bandwidthPortSpeed;

	private String bandwidthUnit;

	private String vpnName;

	private String callType;

	private String custOrgNo;

	private String supplOrgNo;
	
	private String primaryOrSecondary;
	
	private String siteEndInterface;
	
	private String latLong;
	
	private String lastMileProvider;
	
	private String lastMileType;
	
	private String lastMileBandwidth;
	
	private String lastMileBandwidthUnit;
	
	private String burstableBandwidthPortspeed;
	
	private String burstableBandwidthUnit;
	
	private String serviceOption;
	
	private Double mrc;
	
	private Double nrc;
	
	private Double arc;
	
	private String serviceStatus;
	
	private String taxExemptionFlag;
	
	private String serviceCommissionedDate;
	
	private String serviceTerminationDate;

	private Set<SIServiceAttributeBean> attributes;
	
	private String legalEnityName;
	
	private Integer locationId;

	private String serviceType;

	private String serviceManagementOption;

	private String additionalIpsReq;

	private String ipAddressArrangement;

	private String ipv4AddressPoolSize;

	private String ipv6AddressPoolSize;

	private String accountManager;
	
	private String priSecServiceLink;
	
	private String committedSla;

	private String billingFrequency;
	
	private String billingMethod;
	
	private Double contractTerm;
	
	private Integer parentProductOfferingId;
	
	private String billingType;
	
	private Date contractStartDate;
	
	private Date contractEndDate;
	
	private String sourceCity;
	
	private String destinationCity;
	
	private Integer tpsCrmCofId;
	
	private Integer tpsSfdcParentOptyId;
	
	private String isHub;
	
	private String ehsId;
	
	private String ethernetFlavour;

	private String demoFlag;
	private String demoType;
	private String tpsCopfId;
	private String orderCode;
	private Integer tpsSfdcCuId;
	private String orderCategory;
	private String orderSubCategory;
	
	private String billingCurrency;
	private String portMode;
	private Date circuitExpiryDate;
	private String crossConnectType;


	private Integer customerId;
	private Integer customerLeId;
	private String customerName;
	private Integer spLeId;
	private Integer customerCurrencyId;

	private String currentOpportunityType;
	private String uuid;

	public String getCrossConnectType() {
		return crossConnectType;
	}

	public void setCrossConnectType(String crossConnectType) {
		this.crossConnectType = crossConnectType;
	}
	public Date getCircuitExpiryDate() {
		return circuitExpiryDate;
	}
	public void setCircuitExpiryDate(Date circuitExpiryDate) {
		this.circuitExpiryDate = circuitExpiryDate;
	}
	public String getPortMode() {
		return portMode;
	}
	public void setPortMode(String portMode) {
		this.portMode = portMode;
	}
	public String getBillingCurrency() {
		return billingCurrency;
	}
	public void setBillingCurrency(String billingCurrency) {
		this.billingCurrency = billingCurrency;
	}
	
	private List<SIComponentBean> componentBean;
	
	private String productOfferingName;

	public String getDemoFlag() {
		return demoFlag;
	}

	public void setDemoFlag(String demoFlag) {
		this.demoFlag = demoFlag;
	}

	public String getDemoType() {
		return demoType;
	}

	public void setDemoType(String demoType) {
		this.demoType = demoType;
	}
	

	public String getIsHub() {
		return isHub;
	}

	public void setIsHub(String isHub) {
		this.isHub = isHub;
	}

	public String getEhsId() {
		return ehsId;
	}

	public void setEhsId(String ehsId) {
		this.ehsId = ehsId;
	}

	public String getEthernetFlavour() {
		return ethernetFlavour;
	}

	public void setEthernetFlavour(String ethernetFlavour) {
		this.ethernetFlavour = ethernetFlavour;
	}

	public String getLegalEnityName() {
		return legalEnityName;
	}

	/**
	 * @return the attributes
	 */
	public Set<SIServiceAttributeBean> getAttributes() {
		return attributes;
	}

	/**
	 * @param attributes the attributes to set
	 */
	public void setAttributes(Set<SIServiceAttributeBean> attributes) {
		this.attributes = attributes;
	}

	

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return the tpsServiceId
	 */
	public String getTpsServiceId() {
		return tpsServiceId;
	}

	/**
	 * @param tpsServiceId the tpsServiceId to set
	 */
	public void setTpsServiceId(String tpsServiceId) {
		this.tpsServiceId = tpsServiceId;
	}

	/**
	 * @return the vpnId
	 */
	public String getVpnId() {
		return vpnId;
	}

	/**
	 * @param vpnId the vpnId to set
	 */
	public void setVpnId(String vpnId) {
		this.vpnId = vpnId;
	}

	/**
	 * @return the siOrderId
	 */
	public Integer getSiOrderId() {
		return siOrderId;
	}

	/**
	 * @param siOrderId the siOrderId to set
	 */
	public void setSiOrderId(Integer siOrderId) {
		this.siOrderId = siOrderId;
	}

	/**
	 * @return the serviceTopology
	 */
	public String getServiceTopology() {
		return serviceTopology;
	}

	/**
	 * @param serviceTopology the serviceTopology to set
	 */
	public void setServiceTopology(String serviceTopology) {
		this.serviceTopology = serviceTopology;
	}

	/**
	 * @return the siteTopology
	 */
	public String getSiteTopology() {
		return siteTopology;
	}

	/**
	 * @param siteTopology the siteTopology to set
	 */
	public void setSiteTopology(String siteTopology) {
		this.siteTopology = siteTopology;
	}

	/**
	 * @return the siteType
	 */
	public String getSiteType() {
		return siteType;
	}

	/**
	 * @param siteType the siteType to set
	 */
	public void setSiteType(String siteType) {
		this.siteType = siteType;
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
	 * @return the productName
	 */
	public String getProductName() {
		return productName;
	}

	/**
	 * @param productName the productName to set
	 */
	public void setProductName(String productName) {
		this.productName = productName;
	}

	/**
	 * @return the parentProductName
	 */
	public String getParentProductName() {
		return parentProductName;
	}

	/**
	 * @param parentProductName the parentProductName to set
	 */
	public void setParentProductName(String parentProductName) {
		this.parentProductName = parentProductName;
	}

	/**
	 * @return the parentProductOfferingName
	 */
	public String getParentProductOfferingName() {
		return parentProductOfferingName;
	}

	/**
	 * @param parentProductOfferingName the parentProductOfferingName to set
	 */
	public void setParentProductOfferingName(String parentProductOfferingName) {
		this.parentProductOfferingName = parentProductOfferingName;
	}

	/**
	 * @return the bandwidthPortSpeed
	 */
	public String getBandwidthPortSpeed() {
		return bandwidthPortSpeed;
	}

	/**
	 * @param bandwidthPortSpeed the bandwidthPortSpeed to set
	 */
	public void setBandwidthPortSpeed(String bandwidthPortSpeed) {
		this.bandwidthPortSpeed = bandwidthPortSpeed;
	}

	/**
	 * @return the bandwidthUnit
	 */
	public String getBandwidthUnit() {
		return bandwidthUnit;
	}

	/**
	 * @param bandwidthUnit the bandwidthUnit to set
	 */
	public void setBandwidthUnit(String bandwidthUnit) {
		this.bandwidthUnit = bandwidthUnit;
	}

	/**
	 * @return the vpnName
	 */
	public String getVpnName() {
		return vpnName;
	}

	/**
	 * @param vpnName the vpnName to set
	 */
	public void setVpnName(String vpnName) {
		this.vpnName = vpnName;
	}

	/**
	 * @return the callType
	 */
	public String getCallType() {
		return callType;
	}

	/**
	 * @param callType the callType to set
	 */
	public void setCallType(String callType) {
		this.callType = callType;
	}

	/**
	 * @return the custOrgNo
	 */
	public String getCustOrgNo() {
		return custOrgNo;
	}

	/**
	 * @param custOrgNo the custOrgNo to set
	 */
	public void setCustOrgNo(String custOrgNo) {
		this.custOrgNo = custOrgNo;
	}

	/**
	 * @return the supplOrgNo
	 */
	public String getSupplOrgNo() {
		return supplOrgNo;
	}

	/**
	 * @param supplOrgNo the supplOrgNo to set
	 */
	public void setSupplOrgNo(String supplOrgNo) {
		this.supplOrgNo = supplOrgNo;
	}

	/**
	 * @param legalEnityName the legalEnityName to set
	 */
	public void setLegalEnityName(String legalEnityName) {
		this.legalEnityName = legalEnityName;
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
	 * @return the siteEndInterface
	 */
	public String getSiteEndInterface() {
		return siteEndInterface;
	}

	/**
	 * @param siteEndInterface the siteEndInterface to set
	 */
	public void setSiteEndInterface(String siteEndInterface) {
		this.siteEndInterface = siteEndInterface;
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

	/**
	 * @return the lastMileType
	 */
	public String getLastMileType() {
		return lastMileType;
	}

	/**
	 * @param lastMileType the lastMileType to set
	 */
	public void setLastMileType(String lastMileType) {
		this.lastMileType = lastMileType;
	}

	/**
	 * @return the lastMileBandwidth
	 */
	public String getLastMileBandwidth() {
		return lastMileBandwidth;
	}

	/**
	 * @param lastMileBandwidth the lastMileBandwidth to set
	 */
	public void setLastMileBandwidth(String lastMileBandwidth) {
		this.lastMileBandwidth = lastMileBandwidth;
	}

	/**
	 * @return the lastMileBandwidthUnit
	 */
	public String getLastMileBandwidthUnit() {
		return lastMileBandwidthUnit;
	}

	/**
	 * @param lastMileBandwidthUnit the lastMileBandwidthUnit to set
	 */
	public void setLastMileBandwidthUnit(String lastMileBandwidthUnit) {
		this.lastMileBandwidthUnit = lastMileBandwidthUnit;
	}

	/**
	 * @return the burstableBandwidthPortspeed
	 */
	public String getBurstableBandwidthPortspeed() {
		return burstableBandwidthPortspeed;
	}

	/**
	 * @param burstableBandwidthPortspeed the burstableBandwidthPortspeed to set
	 */
	public void setBurstableBandwidthPortspeed(String burstableBandwidthPortspeed) {
		this.burstableBandwidthPortspeed = burstableBandwidthPortspeed;
	}

	/**
	 * @return the burstableBandwidthUnit
	 */
	public String getBurstableBandwidthUnit() {
		return burstableBandwidthUnit;
	}

	/**
	 * @param burstableBandwidthUnit the burstableBandwidthUnit to set
	 */
	public void setBurstableBandwidthUnit(String burstableBandwidthUnit) {
		this.burstableBandwidthUnit = burstableBandwidthUnit;
	}

	/**
	 * @return the serviceOption
	 */
	public String getServiceOption() {
		return serviceOption;
	}

	/**
	 * @param serviceOption the serviceOption to set
	 */
	public void setServiceOption(String serviceOption) {
		this.serviceOption = serviceOption;
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
	 * @return the taxExemptionFlag
	 */
	public String getTaxExemptionFlag() {
		return taxExemptionFlag;
	}

	/**
	 * @param taxExemptionFlag the taxExemptionFlag to set
	 */
	public void setTaxExemptionFlag(String taxExemptionFlag) {
		this.taxExemptionFlag = taxExemptionFlag;
	}

	/**
	 * @return the serviceCommissionedDate
	 */
	public String getServiceCommissionedDate() {
		return serviceCommissionedDate;
	}

	/**
	 * @param serviceCommissionedDate the serviceCommissionedDate to set
	 */
	public void setServiceCommissionedDate(String serviceCommissionedDate) {
		this.serviceCommissionedDate = serviceCommissionedDate;
	}

	/**
	 * @return the serviceTerminationDate
	 */
	public String getServiceTerminationDate() {
		return serviceTerminationDate;
	}

	/**
	 * @param serviceTerminationDate the serviceTerminationDate to set
	 */
	public void setServiceTerminationDate(String serviceTerminationDate) {
		this.serviceTerminationDate = serviceTerminationDate;
	}

	/**
	 * @return the mrc
	 */
	public Double getMrc() {
		return mrc;
	}

	/**
	 * @param mrc the mrc to set
	 */
	public void setMrc(Double mrc) {
		this.mrc = mrc;
	}

	/**
	 * @return the nrc
	 */
	public Double getNrc() {
		return nrc;
	}

	/**
	 * @param nrc the nrc to set
	 */
	public void setNrc(Double nrc) {
		this.nrc = nrc;
	}

	/**
	 * @return the arc
	 */
	public Double getArc() {
		return arc;
	}

	/**
	 * @param arc the arc to set
	 */
	public void setArc(Double arc) {
		this.arc = arc;
	}

	/**
	 * @return the locationId
	 */
	public Integer getLocationId() {
		return locationId;
	}

	/**
	 * @param locationId the locationId to set
	 */
	public void setLocationId(Integer locationId) {
		this.locationId = locationId;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public String getServiceManagementOption() {
		return serviceManagementOption;
	}

	public void setServiceManagementOption(String serviceManagementOption) {
		this.serviceManagementOption = serviceManagementOption;
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

	public String getAccountManager() {
		return accountManager;
	}

	public void setAccountManager(String accountManager) {
		this.accountManager = accountManager;
	}

	public String getPriSecServiceLink() {
		return priSecServiceLink;
	}

	public void setPriSecServiceLink(String priSecServiceLink) {
		this.priSecServiceLink = priSecServiceLink;
	}

	public String getCommittedSla() {
		return committedSla;
	}

	public void setCommittedSla(String committedSla) {
		this.committedSla = committedSla;
	}

	public String getBillingFrequency() {
		return billingFrequency;
	}

	public void setBillingFrequency(String billingFrequency) {
		this.billingFrequency = billingFrequency;
	}

	public String getBillingMethod() {
		return billingMethod;
	}

	public void setBillingMethod(String billingMethod) {
		this.billingMethod = billingMethod;
	}

	public Double getContractTerm() {
		return contractTerm;
	}

	public void setContractTerm(Double contractTerm) {
		this.contractTerm = contractTerm;
	}

	public Integer getParentProductOfferingId() {
		return parentProductOfferingId;
	}

	public void setParentProductOfferingId(Integer parentProductOfferingId) {
		this.parentProductOfferingId = parentProductOfferingId;
	}

	public String getBillingType() {
		return billingType;
	}

	public void setBillingType(String billingType) {
		this.billingType = billingType;
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

	public Integer getTpsCrmCofId() {
		return tpsCrmCofId;
	}

	public void setTpsCrmCofId(Integer tpsCrmCofId) {
		this.tpsCrmCofId = tpsCrmCofId;
	}

	public Integer getTpsSfdcParentOptyId() {
		return tpsSfdcParentOptyId;
	}

	public void setTpsSfdcParentOptyId(Integer tpsSfdcParentOptyId) {
		this.tpsSfdcParentOptyId = tpsSfdcParentOptyId;
	}
	
	


	public String getTpsCopfId() {
		return tpsCopfId;
	}

	public void setTpsCopfId(String tpsCopfId) {
		this.tpsCopfId = tpsCopfId;
	}
	
	

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}
	
	

	public Integer getTpsSfdcCuId() {
		return tpsSfdcCuId;
	}

	public void setTpsSfdcCuId(Integer tpsSfdcCuId) {
		this.tpsSfdcCuId = tpsSfdcCuId;
	}
	
	

	public String getOrderCategory() {
		return orderCategory;
	}

	public void setOrderCategory(String orderCategory) {
		this.orderCategory = orderCategory;
	}

	public String getOrderSubCategory() {
		return orderSubCategory;
	}

	public void setOrderSubCategory(String orderSubCategory) {
		this.orderSubCategory = orderSubCategory;
	}

	public List<SIComponentBean> getComponentBean() {
		return componentBean;
	}

	public void setComponentBean(List<SIComponentBean> componentBean) {
		this.componentBean = componentBean;
	}
	
	

	public String getProductOfferingName() {
		return productOfferingName;
	}

	public void setProductOfferingName(String productOfferingName) {
		this.productOfferingName = productOfferingName;
	}


	public Integer getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}
	public Integer getCustomerLeId() {
		return customerLeId;
	}
	public void setCustomerLeId(Integer customerLeId) {
		this.customerLeId = customerLeId;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public Integer getSpLeId() {
		return spLeId;
	}
	public void setSpLeId(Integer spLeId) {
		this.spLeId = spLeId;
	}
	public Integer getCustomerCurrencyId() {
		return customerCurrencyId;
	}
	public void setCustomerCurrencyId(Integer customerCurrencyId) {
		this.customerCurrencyId = customerCurrencyId;
	}

	public String getCurrentOpportunityType() {
		return currentOpportunityType;
	}

	public void setCurrentOpportunityType(String currentOpportunityType) {
		this.currentOpportunityType = currentOpportunityType;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	@Override
	public String toString() {
		return "SIServiceInfoBean [id=" + id + ", tpsServiceId=" + tpsServiceId + ", vpnId=" + vpnId + ", siOrderId="
				+ siOrderId + ", serviceTopology=" + serviceTopology + ", siteTopology=" + siteTopology + ", siteType="
				+ siteType + ", siteAddress=" + siteAddress + ", productName=" + productName + ", parentProductName="
				+ parentProductName + ", parentProductOfferingName=" + parentProductOfferingName
				+ ", bandwidthPortSpeed=" + bandwidthPortSpeed + ", bandwidthUnit=" + bandwidthUnit + ", vpnName="
				+ vpnName + ", callType=" + callType + ", custOrgNo=" + custOrgNo + ", supplOrgNo=" + supplOrgNo
				+ ", primaryOrSecondary=" + primaryOrSecondary + ", siteEndInterface=" + siteEndInterface + ", latLong="
				+ latLong + ", lastMileProvider=" + lastMileProvider + ", lastMileType=" + lastMileType
				+ ", lastMileBandwidth=" + lastMileBandwidth + ", lastMileBandwidthUnit=" + lastMileBandwidthUnit
				+ ", burstableBandwidthPortspeed=" + burstableBandwidthPortspeed + ", burstableBandwidthUnit="
				+ burstableBandwidthUnit + ", serviceOption=" + serviceOption + ", mrc=" + mrc + ", nrc=" + nrc
				+ ", arc=" + arc + ", serviceStatus=" + serviceStatus + ", taxExemptionFlag=" + taxExemptionFlag
				+ ", serviceCommissionedDate=" + serviceCommissionedDate + ", serviceTerminationDate="
				+ serviceTerminationDate + ", attributes=" + attributes + ", legalEnityName=" + legalEnityName
				+ ", locationId=" + locationId + ", serviceType=" + serviceType + ", serviceManagementOption="
				+ serviceManagementOption + ", additionalIpsReq=" + additionalIpsReq + ", ipAddressArrangement="
				+ ipAddressArrangement + ", ipv4AddressPoolSize=" + ipv4AddressPoolSize + ", ipv6AddressPoolSize="
				+ ipv6AddressPoolSize + ", accountManager=" + accountManager + ", priSecServiceLink="
				+ priSecServiceLink + ", committedSla=" + committedSla + ", billingFrequency=" + billingFrequency
				+ ", billingMethod=" + billingMethod + ", contractTerm=" + contractTerm + ", parentProductOfferingId="
				+ parentProductOfferingId + ", billingType=" + billingType + ", contractStartDate=" + contractStartDate
				+ ", contractEndDate=" + contractEndDate + ", sourceCity=" + sourceCity + ", destinationCity="
				+ destinationCity + ", tpsCrmCofId=" + tpsCrmCofId + ", tpsSfdcParentOptyId=" + tpsSfdcParentOptyId
				+ ", isHub=" + isHub + ", ehsId=" + ehsId + ", ethernetFlavour=" + ethernetFlavour + ", demoFlag="
				+ demoFlag + ", demoType=" + demoType + ", tpsCopfId=" + tpsCopfId + ", orderCode=" + orderCode
				+ ", tpsSfdcCuId=" + tpsSfdcCuId + ", orderCategory=" + orderCategory + ", orderSubCategory="
				+ orderSubCategory + ", billingCurrency=" + billingCurrency + ", portMode=" + portMode
				+ ", circuitExpiryDate=" + circuitExpiryDate + ", customerId=" + customerId + ", customerLeId="
				+ customerLeId + ", customerName=" + customerName + ", spLeId=" + spLeId + ", customerCurrencyId="
				+ customerCurrencyId + ", currentOpportunityType=" + currentOpportunityType + ", componentBean="
				+ componentBean + ", productOfferingName=" + productOfferingName + ", uuid=" + uuid +"]";
	}

}

package com.tcl.dias.common.beans;

import java.io.Serializable;
import java.util.Date;
import java.util.List;	
/**
 * 
 * This is the Bean created for SDWAN to get the inventory details
 * 
 *
 * @author AnandhiV
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class SiServiceDetailBean implements Serializable{
	private String serviceId;
	private String offering;
	private String priSec;
	private String managmentType;
	private String siteTopology;
	private String vpnTopology;
	private String vpnName;
	private String cos;
	private String accessType;
	private String bandwidth;
	private String bandwidthUnit;
	private String lastMileBandwidth;
	private String lastMileBandwidthUnit;
	private String popAddress;
	private String serviceStatus;
	private String lastmileProvider;
	private String accessTopology;
	private String interfaceType;
	private Integer customerId;
	private Integer customerLeId;
	private Integer supplierId;
	private String termsInMonths;
	private String billingFrequency;
	private Double mrc;
	private Double nrc;
	private Double arc;
	private Integer currencyId;
	private String billingMethod;
	private String routingProtocol;
	private String serviceVariant;
	private String serviceType;
	private String additionalIpsRequired;
	private String addressId;
	private String portMode;
	private String siteType;
	private List<SiCpeBean> siCpeBeans;
	private String ipAddressArrangementType;
	private String ipv4AddressPoolSize;
	private String ipv6AddressPoolSize;
	private String product;
	private String latLong;
	private Integer siOrderId;
	private Integer siServiceDetailId;
	private String primaryServiceId;
	private String newLastMileBandwidth;
	private Date contractStartDate;
	private Date contractEndDate;
	private String country;
	private String city;
	private String siteAddress;
	private Integer parentOpportunityId;
	private String gvpnSiteTopology;
	
	private String popSiteAddress;
	private String popSiteCode;
	private String sourceCountry;
	private String sourceCity;
	private String sourceState;
	

	public String getPopSiteAddress() {
		return popSiteAddress;
	}
	public void setPopSiteAddress(String popSiteAddress) {
		this.popSiteAddress = popSiteAddress;
	}
	public String getPopSiteCode() {
		return popSiteCode;
	}
	public void setPopSiteCode(String popSiteCode) {
		this.popSiteCode = popSiteCode;
	}
	public String getSourceCountry() {
		return sourceCountry;
	}
	public void setSourceCountry(String sourceCountry) {
		this.sourceCountry = sourceCountry;
	}
	public String getSourceCity() {
		return sourceCity;
	}
	public void setSourceCity(String sourceCity) {
		this.sourceCity = sourceCity;
	}
	public String getSourceState() {
		return sourceState;
	}
	public void setSourceState(String sourceState) {
		this.sourceState = sourceState;
	}
	public Integer getParentOpportunityId() {
		return parentOpportunityId;
	}
	public void setParentOpportunityId(Integer parentOpportunityId) {
		this.parentOpportunityId = parentOpportunityId;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getNewLastMileBandwidth() {
		return newLastMileBandwidth;
	}
	public void setNewLastMileBandwidth(String newLastMileBandwidth) {
		this.newLastMileBandwidth = newLastMileBandwidth;
	}
	public String getPrimaryServiceId() {
		return primaryServiceId;
	}
	public void setPrimaryServiceId(String primaryServiceId) {
		this.primaryServiceId = primaryServiceId;
	}
	public String getIpAddressArrangementType() {
		return ipAddressArrangementType;
	}
	public void setIpAddressArrangementType(String ipAddressArrangementType) {
		this.ipAddressArrangementType = ipAddressArrangementType;
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
	public String getServiceId() {
		return serviceId;
	}
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	public String getOffering() {
		return offering;
	}
	public void setOffering(String offering) {
		this.offering = offering;
	}
	public String getPriSec() {
		return priSec;
	}
	public void setPriSec(String priSec) {
		this.priSec = priSec;
	}
	public String getManagmentType() {
		return managmentType;
	}
	public void setManagmentType(String managmentType) {
		this.managmentType = managmentType;
	}
	public String getSiteTopology() {
		return siteTopology;
	}
	public void setSiteTopology(String siteTopology) {
		this.siteTopology = siteTopology;
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
	public String getCos() {
		return cos;
	}
	public void setCos(String cos) {
		this.cos = cos;
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
	public String getPopAddress() {
		return popAddress;
	}
	public void setPopAddress(String popAddress) {
		this.popAddress = popAddress;
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
	public String getAccessTopology() {
		return accessTopology;
	}
	public void setAccessTopology(String accessTopology) {
		this.accessTopology = accessTopology;
	}
	public String getInterfaceType() {
		return interfaceType;
	}
	public void setInterfaceType(String interfaceType) {
		this.interfaceType = interfaceType;
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
	public Integer getSupplierId() {
		return supplierId;
	}
	public void setSupplierId(Integer supplierId) {
		this.supplierId = supplierId;
	}
	public String getTermsInMonths() {
		return termsInMonths;
	}
	public void setTermsInMonths(String termsInMonths) {
		this.termsInMonths = termsInMonths;
	}
	public String getBillingFrequency() {
		return billingFrequency;
	}
	public void setBillingFrequency(String billingFrequency) {
		this.billingFrequency = billingFrequency;
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
	public String getBillingMethod() {
		return billingMethod;
	}
	public void setBillingMethod(String billingMethod) {
		this.billingMethod = billingMethod;
	}
	public String getRoutingProtocol() {
		return routingProtocol;
	}
	public void setRoutingProtocol(String routingProtocol) {
		this.routingProtocol = routingProtocol;
	}
	public String getServiceVariant() {
		return serviceVariant;
	}
	public void setServiceVariant(String serviceVariant) {
		this.serviceVariant = serviceVariant;
	}
	public String getServiceType() {
		return serviceType;
	}
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
	public String getAdditionalIpsRequired() {
		return additionalIpsRequired;
	}
	public void setAdditionalIpsRequired(String additionalIpsRequired) {
		this.additionalIpsRequired = additionalIpsRequired;
	}
	public String getAddressId() {
		return addressId;
	}
	public void setAddressId(String addressId) {
		this.addressId = addressId;
	}
	public String getPortMode() {
		return portMode;
	}
	public void setPortMode(String portMode) {
		this.portMode = portMode;
	}
	public String getSiteType() {
		return siteType;
	}
	public void setSiteType(String siteType) {
		this.siteType = siteType;
	}
	public List<SiCpeBean> getSiCpeBeans() {
		return siCpeBeans;
	}
	public void setSiCpeBeans(List<SiCpeBean> siCpeBeans) {
		this.siCpeBeans = siCpeBeans;
	}
	public String getProduct() {
		return product;
	}
	public void setProduct(String product) {
		this.product = product;
	}
	public String getLatLong() {
		return latLong;
	}
	public void setLatLong(String latLong) {
		this.latLong = latLong;
	}
	public Integer getSiOrderId() {
		return siOrderId;
	}
	public void setSiOrderId(Integer siOrderId) {
		this.siOrderId = siOrderId;
	}
	public Integer getSiServiceDetailId() {
		return siServiceDetailId;
	}
	public void setSiServiceDetailId(Integer siServiceDetailId) {
		this.siServiceDetailId = siServiceDetailId;
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
	public String getSiteAddress() {
		return siteAddress;
	}
	public void setSiteAddress(String siteAddress) {
		this.siteAddress = siteAddress;
	}
	public String getGvpnSiteTopology() {
		return gvpnSiteTopology;
	}
	public void setGvpnSiteTopology(String gvpnSiteTopology) {
		this.gvpnSiteTopology = gvpnSiteTopology;
	}
	
}

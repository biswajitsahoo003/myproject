package com.tcl.dias.servicefulfillmentutils.beans;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * This file contains the ServiceAcceptancePdfBean class for service acceptance
 * pdf attributes
 *
 * @author diksha garg
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@JsonInclude(Include.NON_NULL)
public class ServiceAcceptancePdfBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private String orderId;
	private String serviceId;
	private String orderType;
	private String orderCategory="";
	private String customerContractingEntity;
	private String productName;
	private String customerGstNumberAddress;
	private String packetDrop="";
	private String rountTripDelay="";
	private String networkUptime="";
	private String serviceAvailability="";
	private String lastMileType;
	private String localLoopProvider;
	private String cpeManagement;
	private String cpeSerialNumbers;
	private String siteAddress;
	private String demarcationBuildingName;
	private String demarcationWing;
	private String demarcationFloor;
	private String demarcationRoom;
	private String billStartDate;
	private String billFreePeriod;
	private String saEscalationMatrix;
	private String portBandwidth;
	private String localLoopBandwidth;
	private String baSwitchHostName="";
	private String baSwitchIp="";
	private String baSwitchPort="";
	private String endMuxNodeIp="";
	private String endMuxNodeName="";
	private String wanInterfaceType="";
	private String interfaceType="";
	private String fibreType="";
	private String connectorType="";
	private String routingProtocol="";
	private String asNumber="";
	private String extendedLanRequired="";
	private String wanv4Address="";
	private String lanv4Address="";
	private String wanv6Address="";
	private String lanv6Address="";
	private String primarySecondary="";

	private String commissioningDate;

	private String deemedAcceptanceApplicable;
	
	private String cloudName;
	
	private String binterfaceType;
	
	private String bconnectorType;
	
	private String bsiteAddress;
	
	private String bdemarcationBuildingName;
	
	private String bdemarcationWing;
	
	private String bdemarcationFloor;
	
	private String bdemarcationRoom;
	
	private String associatedServiceIds;
	
	private String thirdPartyServiceId="";
	
	private String thirdPartyWanIpAddress="";
	
	private String thirdPartyProviderName="";
	private String thirdPartylinkUptimeAgreement="";
	private String cloudProvider="";
	
	private List<ServiceAcceptancePdfBean> serviceAcceptancePdfBeanList;
	
	public String getBsiteAddress() {
		return bsiteAddress;
	}

	public void setBsiteAddress(String bsiteAddress) {
		this.bsiteAddress = bsiteAddress;
	}

	public String getBdemarcationBuildingName() {
		return bdemarcationBuildingName;
	}

	public void setBdemarcationBuildingName(String bdemarcationBuildingName) {
		this.bdemarcationBuildingName = bdemarcationBuildingName;
	}

	public String getBdemarcationWing() {
		return bdemarcationWing;
	}

	public void setBdemarcationWing(String bdemarcationWing) {
		this.bdemarcationWing = bdemarcationWing;
	}

	public String getBdemarcationFloor() {
		return bdemarcationFloor;
	}

	public void setBdemarcationFloor(String bdemarcationFloor) {
		this.bdemarcationFloor = bdemarcationFloor;
	}

	public String getBdemarcationRoom() {
		return bdemarcationRoom;
	}

	public void setBdemarcationRoom(String bdemarcationRoom) {
		this.bdemarcationRoom = bdemarcationRoom;
	}

	public String getBinterfaceType() {
		return binterfaceType;
	}

	public void setBinterfaceType(String binterfaceType) {
		this.binterfaceType = binterfaceType;
	}

	public String getBconnectorType() {
		return bconnectorType;
	}

	public void setBconnectorType(String bconnectorType) {
		this.bconnectorType = bconnectorType;
	}

	public String getCloudName() {
		return cloudName;
	}

	public void setCloudName(String cloudName) {
		this.cloudName = cloudName;
	}

	public String getCommissioningDate() {
		return commissioningDate;
	}

	public void setCommissioningDate(String commissioningDate) {
		this.commissioningDate = commissioningDate;
	}

	public String getDeemedAcceptanceApplicable() {
		return deemedAcceptanceApplicable;
	}

	public void setDeemedAcceptanceApplicable(String deemedAcceptanceApplicable) {
		this.deemedAcceptanceApplicable = deemedAcceptanceApplicable;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getCustomerContractingEntity() {
		return customerContractingEntity;
	}

	public void setCustomerContractingEntity(String customerContractingEntity) {
		this.customerContractingEntity = customerContractingEntity;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getCustomerGstNumberAddress() {
		return customerGstNumberAddress;
	}

	public void setCustomerGstNumberAddress(String customerGstNumberAddress) {
		this.customerGstNumberAddress = customerGstNumberAddress;
	}

	public String getPacketDrop() {
		return packetDrop;
	}

	public void setPacketDrop(String packetDrop) {
		this.packetDrop = packetDrop;
	}

	public String getRountTripDelay() {
		return rountTripDelay;
	}

	public void setRountTripDelay(String rountTripDelay) {
		this.rountTripDelay = rountTripDelay;
	}

	public String getNetworkUptime() {
		return networkUptime;
	}

	public void setNetworkUptime(String networkUptime) {
		this.networkUptime = networkUptime;
	}

	public String getLastMileType() {
		return lastMileType;
	}

	public void setLastMileType(String lastMileType) {
		this.lastMileType = lastMileType;
	}

	public String getLocalLoopProvider() {
		return localLoopProvider;
	}

	public void setLocalLoopProvider(String localLoopProvider) {
		this.localLoopProvider = localLoopProvider;
	}

	public String getCpeManagement() {
		return cpeManagement;
	}

	public void setCpeManagement(String cpeManagement) {
		this.cpeManagement = cpeManagement;
	}

	public String getCpeSerialNumbers() {
		return cpeSerialNumbers;
	}

	public void setCpeSerialNumbers(String cpeSerialNumbers) {
		this.cpeSerialNumbers = cpeSerialNumbers;
	}

	public String getSiteAddress() {
		return siteAddress;
	}

	public void setSiteAddress(String siteAddress) {
		this.siteAddress = siteAddress;
	}

	public String getDemarcationBuildingName() {
		return demarcationBuildingName;
	}

	public void setDemarcationBuildingName(String demarcationBuildingName) {
		this.demarcationBuildingName = demarcationBuildingName;
	}

	public String getDemarcationWing() {
		return demarcationWing;
	}

	public void setDemarcationWing(String demarcationWing) {
		this.demarcationWing = demarcationWing;
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

	public String getBillStartDate() {
		return billStartDate;
	}

	public void setBillStartDate(String billStartDate) {
		this.billStartDate = billStartDate;
	}

	public String getBillFreePeriod() {
		return billFreePeriod;
	}

	public void setBillFreePeriod(String billFreePeriod) {
		this.billFreePeriod = billFreePeriod;
	}

	public String getSaEscalationMatrix() {
		return saEscalationMatrix;
	}

	public void setSaEscalationMatrix(String saEscalationMatrix) {
		this.saEscalationMatrix = saEscalationMatrix;
	}

	public String getPortBandwidth() {
		return portBandwidth;
	}

	public void setPortBandwidth(String portBandwidth) {
		this.portBandwidth = portBandwidth;
	}

	public String getLocalLoopBandwidth() {
		return localLoopBandwidth;
	}

	public void setLocalLoopBandwidth(String localLoopBandwidth) {
		this.localLoopBandwidth = localLoopBandwidth;
	}

	public String getBaSwitchHostName() {
		return baSwitchHostName;
	}

	public void setBaSwitchHostName(String baSwitchHostName) {
		this.baSwitchHostName = baSwitchHostName;
	}

	public String getBaSwitchIp() {
		return baSwitchIp;
	}

	public void setBaSwitchIp(String baSwitchIp) {
		this.baSwitchIp = baSwitchIp;
	}

	public String getBaSwitchPort() {
		return baSwitchPort;
	}

	public void setBaSwitchPort(String baSwitchPort) {
		this.baSwitchPort = baSwitchPort;
	}

	public String getEndMuxNodeIp() {
		return endMuxNodeIp;
	}

	public void setEndMuxNodeIp(String endMuxNodeIp) {
		this.endMuxNodeIp = endMuxNodeIp;
	}

	public String getEndMuxNodeName() {
		return endMuxNodeName;
	}

	public void setEndMuxNodeName(String endMuxNodeName) {
		this.endMuxNodeName = endMuxNodeName;
	}

	public String getInterfaceType() {
		return interfaceType;
	}

	public void setInterfaceType(String interfaceType) {
		this.interfaceType = interfaceType;
	}

	public String getFibreType() {
		return fibreType;
	}

	public void setFibreType(String fibreType) {
		this.fibreType = fibreType;
	}

	public String getConnectorType() {
		return connectorType;
	}

	public void setConnectorType(String connectorType) {
		this.connectorType = connectorType;
	}

	public String getRoutingProtocol() {
		return routingProtocol;
	}

	public void setRoutingProtocol(String routingProtocol) {
		this.routingProtocol = routingProtocol;
	}

	public String getAsNumber() {
		return asNumber;
	}

	public void setAsNumber(String asNumber) {
		this.asNumber = asNumber;
	}

	public String getExtendedLanRequired() {
		return extendedLanRequired;
	}

	public void setExtendedLanRequired(String extendedLanRequired) {
		this.extendedLanRequired = extendedLanRequired;
	}

	public String getWanv4Address() {
		return wanv4Address;
	}

	public void setWanv4Address(String wanv4Address) {
		this.wanv4Address = wanv4Address;
	}

	public String getLanv4Address() {
		return lanv4Address;
	}

	public void setLanv4Address(String lanv4Address) {
		this.lanv4Address = lanv4Address;
	}

	public String getWanv6Address() {
		return wanv6Address;
	}

	public void setWanv6Address(String wanv6Address) {
		this.wanv6Address = wanv6Address;
	}

	public String getLanv6Address() {
		return lanv6Address;
	}

	public void setLanv6Address(String lanv6Address) {
		this.lanv6Address = lanv6Address;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String string) {
		this.orderId = string;
	}

	public String getWanInterfaceType() {
		return wanInterfaceType;
	}

	public void setWanInterfaceType(String wanInterfaceType) {
		this.wanInterfaceType = wanInterfaceType;
	}

	public String getOrderCategory() {
		return orderCategory;
	}

	public void setOrderCategory(String orderCategory) {
		this.orderCategory = orderCategory;
	}

	public String getAssociatedServiceIds() {
		return associatedServiceIds;
	}

	public void setAssociatedServiceIds(String associatedServiceIds) {
		this.associatedServiceIds = associatedServiceIds;
	}

	public List<ServiceAcceptancePdfBean> getServiceAcceptancePdfBeanList() {
		return serviceAcceptancePdfBeanList;
	}

	public void setServiceAcceptancePdfBeanList(List<ServiceAcceptancePdfBean> serviceAcceptancePdfBeanList) {
		this.serviceAcceptancePdfBeanList = serviceAcceptancePdfBeanList;
	}

	public String getThirdPartyServiceId() {
		return thirdPartyServiceId;
	}

	public void setThirdPartyServiceId(String thirdPartyServiceId) {
		this.thirdPartyServiceId = thirdPartyServiceId;
	}

	public String getThirdPartyWanIpAddress() {
		return thirdPartyWanIpAddress;
	}

	public void setThirdPartyWanIpAddress(String thirdPartyWanIpAddress) {
		this.thirdPartyWanIpAddress = thirdPartyWanIpAddress;
	}

	public String getServiceAvailability() {
		return serviceAvailability;
	}

	public void setServiceAvailability(String serviceAvailability) {
		this.serviceAvailability = serviceAvailability;
	}

	public String getPrimarySecondary() {
		return primarySecondary;
	}

	public void setPrimarySecondary(String primarySecondary) {
		this.primarySecondary = primarySecondary;
	}

	public String getThirdPartyProviderName() {
		return thirdPartyProviderName;
	}

	public void setThirdPartyProviderName(String thirdPartyProviderName) {
		this.thirdPartyProviderName = thirdPartyProviderName;
	}

	public String getThirdPartylinkUptimeAgreement() {
		return thirdPartylinkUptimeAgreement;
	}

	public void setThirdPartylinkUptimeAgreement(String thirdPartylinkUptimeAgreement) {
		this.thirdPartylinkUptimeAgreement = thirdPartylinkUptimeAgreement;
	}

	public String getCloudProvider() {
		return cloudProvider;
	}

	public void setCloudProvider(String cloudProvider) {
		this.cloudProvider = cloudProvider;
	}

}

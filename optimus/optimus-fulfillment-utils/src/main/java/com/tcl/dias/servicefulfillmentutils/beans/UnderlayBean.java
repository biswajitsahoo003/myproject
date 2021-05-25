package com.tcl.dias.servicefulfillmentutils.beans;

import java.sql.Timestamp;
import java.util.List;

public class UnderlayBean {

	public Integer serviceId;

	public String serviceCode;

	public String orderCode;

	public Integer orderId;

	public String orderType;

	public String orderCategory;

	public String orderSubCategory;

	public String productName;

	public String offeringName;
	
	private String portBandwidth;
	private String localLoopBandwidth;
	private String lastMileType;
	private String siteAddress;
	private String demarcationBuildingName;
	private String demarcationWing;
	private String demarcationFloor;
	private String demarcationRoom;
	private String localItContactName;
	private String localItContactMobile;
	private String localItContactEmailId;
	private String customerContractingEntity;
	private String customerGstNumberAddress;
    private String localLoopProvider;
    private String serviceAvailability;
    private String commissioningDate;


	private String deemedAcceptanceApplicable;
	private String billStartDate;
	private String billFreePeriod;
    private String thirdPartyServiceId;
	
	private String thirdPartyWanIpAddress;
	
	private String thirdPartyProviderName;
	private String thirdPartylinkUptimeAgreement;
	
	private String endMuxNodeIp;
	private String endMuxNodeName;
	private String wanInterfaceType;
	private String interfaceType;
	private String connectorType;
	private String routingProtocol;
	private String asNumber;
	private String wanv4Address;
	private String lanv4Address;
	private String wanv6Address;
	private String lanv6Address;
	private String country;
    

	private Timestamp rrfsDate;

	private String priority;

	private Timestamp commitedDeliveryDate;
	
	private List<AttachmentIdBean> documentIds;

	public String getSiteAddress() {
		return siteAddress;
	}

	public void setSiteAddress(String siteAddress) {
		this.siteAddress = siteAddress;
	}

	public Timestamp getCommitedDeliveryDate() {
		return commitedDeliveryDate;
	}

	public void setCommitedDeliveryDate(Timestamp commitedDeliveryDate) {
		this.commitedDeliveryDate = commitedDeliveryDate;
	}

	public Timestamp getRrfsDate() {
		return rrfsDate;
	}

	public void setRrfsDate(Timestamp rrfsDate) {
		this.rrfsDate = rrfsDate;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}


	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}

	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
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

	public String getOrderSubCategory() {
		return orderSubCategory;
	}

	public void setOrderSubCategory(String orderSubCategory) {
		this.orderSubCategory = orderSubCategory;
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

	public String getLastMileType() {
		return lastMileType;
	}

	public void setLastMileType(String lastMileType) {
		this.lastMileType = lastMileType;
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

	public String getCustomerContractingEntity() {
		return customerContractingEntity;
	}

	public void setCustomerContractingEntity(String customerContractingEntity) {
		this.customerContractingEntity = customerContractingEntity;
	}

	public String getCustomerGstNumberAddress() {
		return customerGstNumberAddress;
	}

	public void setCustomerGstNumberAddress(String customerGstNumberAddress) {
		this.customerGstNumberAddress = customerGstNumberAddress;
	}

	public String getLocalLoopProvider() {
		return localLoopProvider;
	}

	public void setLocalLoopProvider(String localLoopProvider) {
		this.localLoopProvider = localLoopProvider;
	}

	public String getServiceAvailability() {
		return serviceAvailability;
	}

	public void setServiceAvailability(String serviceAvailability) {
		this.serviceAvailability = serviceAvailability;
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

	public String getLocalItContactName() {
		return localItContactName;
	}

	public void setLocalItContactName(String localItContactName) {
		this.localItContactName = localItContactName;
	}

	public String getLocalItContactMobile() {
		return localItContactMobile;
	}

	public void setLocalItContactMobile(String localItContactMobile) {
		this.localItContactMobile = localItContactMobile;
	}

	public String getLocalItContactEmailId() {
		return localItContactEmailId;
	}

	public void setLocalItContactEmailId(String localItContactEmailId) {
		this.localItContactEmailId = localItContactEmailId;
	}

	public List<AttachmentIdBean> getDocumentIds() {
		return documentIds;
	}

	public void setDocumentIds(List<AttachmentIdBean> documentIds) {
		this.documentIds = documentIds;
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

	public String getWanInterfaceType() {
		return wanInterfaceType;
	}

	public void setWanInterfaceType(String wanInterfaceType) {
		this.wanInterfaceType = wanInterfaceType;
	}

	public String getInterfaceType() {
		return interfaceType;
	}

	public void setInterfaceType(String interfaceType) {
		this.interfaceType = interfaceType;
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

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

}

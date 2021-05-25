package com.tcl.dias.common.serviceinventory.bean;

import org.apache.tomcat.jni.Multicast;

/**
 * ServiceDetailBean - Used to push Service Detail Attributes to Service
 * Inventory
 *
 * @author Mayank S
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class ServiceDetailBean {

	private String scopeOfManagement;
	private String svcLinkServiceId;
	private String redundancyRole;
	private String optyBidCategory;
	private Byte asdOpportunity;
	private String serviceStatus;
	private String uuid;
	private String parentUuid;
	private String remoteAsNumber;
	private String businessSwitchUplinkPort;
	private String multicastRPAddress;
	private String multicastType;
	private String autoDiscoveryOption;
	private String dataMdt;
	private String dataMdtThreshold;
	private String defaultMdt;
	private String rpLocation;
	private String wanPimMode;

	private String orderCategory;
	private String orderType;

	/**
	 * @return the orderCategory
	 */
	public String getOrderCategory() {
		return orderCategory;
	}

	/**
	 * @param orderCategory the orderCategory to set
	 */
	public void setOrderCategory(String orderCategory) {
		this.orderCategory = orderCategory;
	}

	/**
	 * @return the orderType
	 */
	public String getOrderType() {
		return orderType;
	}

	/**
	 * @param orderType the orderType to set
	 */
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getScopeOfManagement() {
		return scopeOfManagement;
	}

	public void setScopeOfManagement(String scopeOfManagement) {
		this.scopeOfManagement = scopeOfManagement;
	}

	public String getSvcLinkServiceId() {
		return svcLinkServiceId;
	}

	public void setSvcLinkServiceId(String svcLinkServiceId) {
		this.svcLinkServiceId = svcLinkServiceId;
	}

	public String getRedundancyRole() {
		return redundancyRole;
	}

	public void setRedundancyRole(String redundancyRole) {
		this.redundancyRole = redundancyRole;
	}

	public String getOptyBidCategory() {
		return optyBidCategory;
	}

	public void setOptyBidCategory(String optyBidCategory) {
		this.optyBidCategory = optyBidCategory;
	}

	public Byte getAsdOpportunity() {
		return asdOpportunity;
	}

	public void setAsdOpportunity(Byte asdOpportunity) {
		this.asdOpportunity = asdOpportunity;
	}

	public String getServiceStatus() {
		return serviceStatus;
	}

	public void setServiceStatus(String serviceStatus) {
		this.serviceStatus = serviceStatus;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getRemoteAsNumber() {
		return remoteAsNumber;
	}

	public void setRemoteAsNumber(String remoteAsNumber) {
		this.remoteAsNumber = remoteAsNumber;
	}

	public String getBusinessSwitchUplinkPort() {
		return businessSwitchUplinkPort;
	}

	public void setBusinessSwitchUplinkPort(String businessSwitchUplinkPort) {
		this.businessSwitchUplinkPort = businessSwitchUplinkPort;
	}

	public String getMulticastRPAddress() {
		return multicastRPAddress;
	}

	public void setMulticastRPAddress(String multicastRPAddress) {
		this.multicastRPAddress = multicastRPAddress;
	}

	public String getMulticastType() {
		return multicastType;
	}

	public void setMulticastType(String multicastType) {
		this.multicastType = multicastType;
	}

	public String getAutoDiscoveryOption() {
		return autoDiscoveryOption;
	}

	public void setAutoDiscoveryOption(String autoDiscoveryOption) {
		this.autoDiscoveryOption = autoDiscoveryOption;
	}

	public String getDataMdt() {
		return dataMdt;
	}

	public void setDataMdt(String dataMdt) {
		this.dataMdt = dataMdt;
	}

	public String getDataMdtThreshold() {
		return dataMdtThreshold;
	}

	public void setDataMdtThreshold(String dataMdtThreshold) {
		this.dataMdtThreshold = dataMdtThreshold;
	}

	public String getDefaultMdt() {
		return defaultMdt;
	}

	public void setDefaultMdt(String defaultMdt) {
		this.defaultMdt = defaultMdt;
	}

	public String getRpLocation() {
		return rpLocation;
	}

	public void setRpLocation(String rpLocation) {
		this.rpLocation = rpLocation;
	}

	public String getWanPimMode() {
		return wanPimMode;
	}

	public void setWanPimMode(String wanPimMode) {
		this.wanPimMode = wanPimMode;
	}

	public String getParentUuid() {
		return parentUuid;
	}

	public void setParentUuid(String parentUuid) {
		this.parentUuid = parentUuid;
	}

}

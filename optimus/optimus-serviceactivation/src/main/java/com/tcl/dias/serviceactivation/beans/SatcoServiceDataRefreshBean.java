package com.tcl.dias.serviceactivation.beans;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SatcoServiceDataRefreshBean {

	@JsonProperty("LAG_PORT")
	private String lagPort;

	@JsonProperty("LAG_INNER_VLAN")
	private String lagInnerVlan;

	@JsonProperty("LAG_OUTER_VLAN")
	private String lagOuterVlan;

	@JsonProperty("PEName")
	private String peName;

	@JsonProperty("PEMgmt_IP")
	private String peMgmtIp;
	
	@JsonProperty("WAN_IP")
	private String wanIp;
	
	@JsonProperty("VSNLWAN_IP")
	private String vsnlWanIp;
	
	@JsonProperty("CustomerWAN_IP")
	private String customerWanIp;
	
	@JsonProperty("ipaddr_wanv4_address")
	private String ipaddrWanv4Address;
	
	@JsonProperty("IPV6_WAN_IP")
	private String Ipv6WanIp;
	
	@JsonProperty("wanv6_address")
	private String wanv6Address;
	
	@JsonProperty("LAN_IPv6")
	private String lanIpv6;
	
	@JsonProperty("CustomerEndLoopback")
	private String customerEndLoopback;
	
	@JsonProperty("BusinessSwitchHostName")
	private String businessSwitchHostName;
	
	@JsonProperty("BusinessSwitch_IP")
	private String businessSwitchIp;
	
	@JsonProperty("BusinessSwitchHandoffPort")
	private String businessSwitchHandoffPort;
	
	@JsonProperty("IPV6_VSNL_WAN_IP")
	private String Ipv6VsnlWanIp;
	
	@JsonProperty("IPV6_Customer_WAN_IP")
	private String Ipv6CustomerWanIp;
	
	@JsonProperty("LAN_IP")
	private String lanIp;
	
	@JsonProperty("VLAN")
	private String vLAN;
	
	
	
	
	
	
	/**
	 * @return the vLAN
	 */
	public String getvLAN() {
		return vLAN;
	}

	/**
	 * @param vLAN the vLAN to set
	 */
	public void setvLAN(String vLAN) {
		this.vLAN = vLAN;
	}

	private String errorMsg;
	
	
	private String serviceId;
	
	private Integer scServiceDetailId;
	
	

	private boolean isSatsoc;
	

	public boolean isIsSatsoc() {
		return isSatsoc;
	}

	public void setIsSatsoc(boolean isSatsoc) {
		this.isSatsoc = isSatsoc;
	}

	public String getLanIp() {
		return lanIp;
	}

	public void setLanIp(String lanIp) {
		this.lanIp = lanIp;
	}

	public String getIpv6CustomerWanIp() {
		return Ipv6CustomerWanIp;
	}

	public void setIpv6CustomerWanIp(String ipv6CustomerWanIp) {
		Ipv6CustomerWanIp = ipv6CustomerWanIp;
	}

	public String getLagPort() {
		return lagPort;
	}

	public void setLagPort(String lagPort) {
		this.lagPort = lagPort;
	}

	public String getLagInnerVlan() {
		return lagInnerVlan;
	}

	public void setLagInnerVlan(String lagInnerVlan) {
		this.lagInnerVlan = lagInnerVlan;
	}

	public String getLagOuterVlan() {
		return lagOuterVlan;
	}

	public void setLagOuterVlan(String lagOuterVlan) {
		this.lagOuterVlan = lagOuterVlan;
	}

	public String getPeName() {
		return peName;
	}

	public void setPeName(String peName) {
		this.peName = peName;
	}

	public String getPeMgmtIp() {
		return peMgmtIp;
	}

	public void setPeMgmtIp(String peMgmtIp) {
		this.peMgmtIp = peMgmtIp;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public Integer getScServiceDetailId() {
		return scServiceDetailId;
	}

	public void setScServiceDetailId(Integer scServiceDetailId) {
		this.scServiceDetailId = scServiceDetailId;
	}
	

	public String getWanIp() {
		return wanIp;
	}

	public void setWanIp(String wanIp) {
		this.wanIp = wanIp;
	}

	public String getVsnlWanIp() {
		return vsnlWanIp;
	}

	public void setVsnlWanIp(String vsnlWanIp) {
		this.vsnlWanIp = vsnlWanIp;
	}

	public String getCustomerWanIp() {
		return customerWanIp;
	}

	public void setCustomerWanIp(String customerWanIp) {
		this.customerWanIp = customerWanIp;
	}

	public String getIpaddrWanv4Address() {
		return ipaddrWanv4Address;
	}

	public void setIpaddrWanv4Address(String ipaddrWanv4Address) {
		this.ipaddrWanv4Address = ipaddrWanv4Address;
	}

	public String getIpv6WanIp() {
		return Ipv6WanIp;
	}

	public void setIpv6WanIp(String ipv6WanIp) {
		Ipv6WanIp = ipv6WanIp;
	}

	public String getWanv6Address() {
		return wanv6Address;
	}

	public void setWanv6Address(String wanv6Address) {
		this.wanv6Address = wanv6Address;
	}

	public String getLanIpv6() {
		return lanIpv6;
	}

	public void setLanIpv6(String lanIpv6) {
		this.lanIpv6 = lanIpv6;
	}
	

	public String getCustomerEndLoopback() {
		return customerEndLoopback;
	}

	public void setCustomerEndLoopback(String customerEndLoopback) {
		this.customerEndLoopback = customerEndLoopback;
	}

	public String getBusinessSwitchHostName() {
		return businessSwitchHostName;
	}

	public void setBusinessSwitchHostName(String businessSwitchHostName) {
		this.businessSwitchHostName = businessSwitchHostName;
	}

	public String getBusinessSwitchIp() {
		return businessSwitchIp;
	}

	public void setBusinessSwitchIp(String businessSwitchIp) {
		this.businessSwitchIp = businessSwitchIp;
	}

	public String getBusinessSwitchHandoffPort() {
		return businessSwitchHandoffPort;
	}

	public void setBusinessSwitchHandoffPort(String businessSwitchHandoffPort) {
		this.businessSwitchHandoffPort = businessSwitchHandoffPort;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public String getIpv6VsnlWanIp() {
		return Ipv6VsnlWanIp;
	}

	public void setIpv6VsnlWanIp(String ipv6VsnlWanIp) {
		Ipv6VsnlWanIp = ipv6VsnlWanIp;
	}
	
	
	

}

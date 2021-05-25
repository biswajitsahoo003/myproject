package com.tcl.dias.servicefulfillmentutils.beans;

import java.io.Serializable;
import java.sql.Timestamp;


/**
 * 
 * RadwinLastmile Bean Class
 * 
 *
 * @author diksha garg
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */

public class RadwinLastmileBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer radwinLastmileId;
	private String allowedVlanid;
	private String btsIp;
	private String btsName;
	private String customerLocation;
	private String dataVlanPriority;
	private String dataVlanid;
	private Timestamp endDate;
	private String ethernet_Port_Config;
	private String frequency;
	private String gatewayIp;
	private String hsuEgressTraffic;
	private String hsuIngressTraffic;
	private String hsuIp;
	private String hsuMacAddr;
	private String hsuSubnet;
	private Timestamp lastModifiedDate;
	private String mgmtVlanid;
	private Float mirDl;
	private Float mirUl;
	private String modifiedBy;
	private String ntpOffset;
	private String ntpServerIp;
	private String protocolSnmp;
	private String protocolTelnet;
	private String protocolWebinterface;
	private String reqd_tx_power;
	private String sectorId;
	private String siteContact;
	private String siteLat;
	private String siteLong;
	private String siteName;
	private Timestamp startDate;
	private Byte untagVlanId;
	private String vlanMode;
	private boolean isEdited;
	public Integer getRadwinLastmileId() {
		return radwinLastmileId;
	}
	public void setRadwinLastmileId(Integer radwinLastmileId) {
		this.radwinLastmileId = radwinLastmileId;
	}
	public String getAllowedVlanid() {
		return allowedVlanid;
	}
	public void setAllowedVlanid(String allowedVlanid) {
		this.allowedVlanid = allowedVlanid;
	}
	public String getBtsIp() {
		return btsIp;
	}
	public void setBtsIp(String btsIp) {
		this.btsIp = btsIp;
	}
	public String getBtsName() {
		return btsName;
	}
	public void setBtsName(String btsName) {
		this.btsName = btsName;
	}
	public String getCustomerLocation() {
		return customerLocation;
	}
	public void setCustomerLocation(String customerLocation) {
		this.customerLocation = customerLocation;
	}
	public String getDataVlanPriority() {
		return dataVlanPriority;
	}
	public void setDataVlanPriority(String dataVlanPriority) {
		this.dataVlanPriority = dataVlanPriority;
	}
	public String getDataVlanid() {
		return dataVlanid;
	}
	public void setDataVlanid(String dataVlanid) {
		this.dataVlanid = dataVlanid;
	}
	public Timestamp getEndDate() {
		return endDate;
	}
	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
	}
	public String getEthernet_Port_Config() {
		return ethernet_Port_Config;
	}
	public void setEthernet_Port_Config(String ethernet_Port_Config) {
		this.ethernet_Port_Config = ethernet_Port_Config;
	}
	public String getFrequency() {
		return frequency;
	}
	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}
	public String getGatewayIp() {
		return gatewayIp;
	}
	public void setGatewayIp(String gatewayIp) {
		this.gatewayIp = gatewayIp;
	}
	public String getHsuEgressTraffic() {
		return hsuEgressTraffic;
	}
	public void setHsuEgressTraffic(String hsuEgressTraffic) {
		this.hsuEgressTraffic = hsuEgressTraffic;
	}
	public String getHsuIngressTraffic() {
		return hsuIngressTraffic;
	}
	public void setHsuIngressTraffic(String hsuIngressTraffic) {
		this.hsuIngressTraffic = hsuIngressTraffic;
	}
	public String getHsuIp() {
		return hsuIp;
	}
	public void setHsuIp(String hsuIp) {
		this.hsuIp = hsuIp;
	}
	public String getHsuMacAddr() {
		return hsuMacAddr;
	}
	public void setHsuMacAddr(String hsuMacAddr) {
		this.hsuMacAddr = hsuMacAddr;
	}
	public String getHsuSubnet() {
		return hsuSubnet;
	}
	public void setHsuSubnet(String hsuSubnet) {
		this.hsuSubnet = hsuSubnet;
	}
	public Timestamp getLastModifiedDate() {
		return lastModifiedDate;
	}
	public void setLastModifiedDate(Timestamp lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}
	public String getMgmtVlanid() {
		return mgmtVlanid;
	}
	public void setMgmtVlanid(String mgmtVlanid) {
		this.mgmtVlanid = mgmtVlanid;
	}
	
	public String getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	public String getNtpOffset() {
		return ntpOffset;
	}
	public void setNtpOffset(String ntpOffset) {
		this.ntpOffset = ntpOffset;
	}
	public String getNtpServerIp() {
		return ntpServerIp;
	}
	public void setNtpServerIp(String ntpServerIp) {
		this.ntpServerIp = ntpServerIp;
	}
	public String getProtocolSnmp() {
		return protocolSnmp;
	}
	public void setProtocolSnmp(String protocolSnmp) {
		this.protocolSnmp = protocolSnmp;
	}
	public String getProtocolTelnet() {
		return protocolTelnet;
	}
	public void setProtocolTelnet(String protocolTelnet) {
		this.protocolTelnet = protocolTelnet;
	}
	public String getProtocolWebinterface() {
		return protocolWebinterface;
	}
	public void setProtocolWebinterface(String protocolWebinterface) {
		this.protocolWebinterface = protocolWebinterface;
	}
	public String getReqd_tx_power() {
		return reqd_tx_power;
	}
	public void setReqd_tx_power(String reqd_tx_power) {
		this.reqd_tx_power = reqd_tx_power;
	}
	public String getSectorId() {
		return sectorId;
	}
	public void setSectorId(String sectorId) {
		this.sectorId = sectorId;
	}
	public String getSiteContact() {
		return siteContact;
	}
	public void setSiteContact(String siteContact) {
		this.siteContact = siteContact;
	}
	public String getSiteLat() {
		return siteLat;
	}
	public void setSiteLat(String siteLat) {
		this.siteLat = siteLat;
	}
	public String getSiteLong() {
		return siteLong;
	}
	public void setSiteLong(String siteLong) {
		this.siteLong = siteLong;
	}
	public String getSiteName() {
		return siteName;
	}
	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}
	public Timestamp getStartDate() {
		return startDate;
	}
	public void setStartDate(Timestamp startDate) {
		this.startDate = startDate;
	}
	public Byte getUntagVlanId() {
		return untagVlanId;
	}
	public void setUntagVlanId(Byte untagVlanId) {
		this.untagVlanId = untagVlanId;
	}
	public String getVlanMode() {
		return vlanMode;
	}
	public void setVlanMode(String vlanMode) {
		this.vlanMode = vlanMode;
	}

	public boolean isEdited() {
		return isEdited;
	}

	public void setEdited(boolean edited) {
		isEdited = edited;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public Float getMirDl() {
		return mirDl;
	}
	public void setMirDl(Float mirDl) {
		this.mirDl = mirDl;
	}
	public Float getMirUl() {
		return mirUl;
	}
	public void setMirUl(Float mirUl) {
		this.mirUl = mirUl;
	}

	
}
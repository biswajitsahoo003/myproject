package com.tcl.dias.servicefulfillmentutils.beans;

import java.io.Serializable;

public class IPServiceSyncBean implements Serializable {

	private static final long serialVersionUID = 2048243823193115272L;
	
	private CramerServiceHeader cramerServiceHeader;
	private String serviceID;
	private String serviceType;
	private String vpnID;
	private String relationshipType;
	private String wanIPAddress;
	public CramerServiceHeader getCramerServiceHeader() {
		return cramerServiceHeader;
	}
	public void setCramerServiceHeader(CramerServiceHeader cramerServiceHeader) {
		this.cramerServiceHeader = cramerServiceHeader;
	}
	public String getServiceID() {
		return serviceID;
	}
	public void setServiceID(String serviceID) {
		this.serviceID = serviceID;
	}
	public String getServiceType() {
		return serviceType;
	}
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
	public String getVpnID() {
		return vpnID;
	}
	public void setVpnID(String vpnID) {
		this.vpnID = vpnID;
	}
	public String getRelationshipType() {
		return relationshipType;
	}
	public void setRelationshipType(String relationshipType) {
		this.relationshipType = relationshipType;
	}
	public String getWanIPAddress() {
		return wanIPAddress;
	}
	public void setWanIPAddress(String wanIPAddress) {
		this.wanIPAddress = wanIPAddress;
	}
	

}

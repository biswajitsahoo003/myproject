package com.tcl.dias.serviceactivation.beans;

import java.io.Serializable;
/**
 * IsValidBtsSyncBean class
 *
 * @author Naveen
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class IsValidBtsSyncBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String btsIP;
	private String copfId;
	private String provider;
	private String requestId;
	private String scenarioType;
	private String sectorId;
	private String serviceId;
	private String hsuIP;
	public String getBtsIP() {
		return btsIP;
	}
	public void setBtsIP(String btsIP) {
		this.btsIP = btsIP;
	}
	public String getCopfId() {
		return copfId;
	}
	public void setCopfId(String copfId) {
		this.copfId = copfId;
	}
	public String getProvider() {
		return provider;
	}
	public void setProvider(String provider) {
		this.provider = provider;
	}
	public String getRequestId() {
		return requestId;
	}
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	public String getScenarioType() {
		return scenarioType;
	}
	public void setScenarioType(String scenarioType) {
		this.scenarioType = scenarioType;
	}
	public String getSectorId() {
		return sectorId;
	}
	public void setSectorId(String sectorId) {
		this.sectorId = sectorId;
	}
	public String getServiceId() {
		return serviceId;
	}
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	public String getHsuIP() {
		return hsuIP;
	}
	public void setHsuIP(String hsuIP) {
		this.hsuIP = hsuIP;
	}
}

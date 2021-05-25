
package com.tcl.dias.serviceinventory.izosdwan.beans.cisco.cpeinformation;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class VedgeInventoryDetail implements Serializable {

	@JsonProperty("deviceType")
	private String deviceType;
	
	@JsonProperty("validity")
	private String validity;
	
	@JsonProperty("chasisNumber")
	private String chasisNumber;
	
	@JsonProperty("serialNumber")
	private String serialNumber;
	
	@JsonProperty("host-name")
	private String hostName;
	
	@JsonProperty("site-id")
	private String siteId;
	
	@JsonProperty("system-ip")
	private String system_ip;
	
	@JsonProperty("local-system-ip")
	private String localsystemip;
	
	@JsonProperty("deviceType")
	public String getDeviceType() {
		return deviceType;
	}
	@JsonProperty("deviceType")
	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
	@JsonProperty("validity")
	public String getValidity() {
		return validity;
	}
	@JsonProperty("validity")
	public void setValidity(String validity) {
		this.validity = validity;
	}
	@JsonProperty("chasisNumber")
	public String getChasisNumber() {
		return chasisNumber;
	}
	@JsonProperty("chasisNumber")
	public void setChasisNumber(String chasisNumber) {
		this.chasisNumber = chasisNumber;
	}
	@JsonProperty("serialNumber")
	public String getSerialNumber() {
		return serialNumber;
	}
	@JsonProperty("serialNumber")
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}
	@JsonProperty("host-name")
	public String getHostName() {
		return hostName;
	}
	@JsonProperty("host-name")
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
	@JsonProperty("site-id")
	public String getSiteId() {
		return siteId;
	}
	@JsonProperty("site-id")
	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}
	@JsonProperty("system-ip")
	public String getSystem_ip() {
		return system_ip;
	}
	@JsonProperty("system-ip")
	public void setSystem_ip(String system_ip) {
		this.system_ip = system_ip;
	}
	@JsonProperty("local-system-ip")
	public String getLocalsystemip() {
		return localsystemip;
	}
	@JsonProperty("local-system-ip")
	public void setLocalsystemip(String localsystemip) {
		this.localsystemip = localsystemip;
	}

	@Override
	public String toString() {
		return "VedgeInventoryDetail [deviceType=" + deviceType + ", validity=" + validity + ", chasisNumber="
				+ chasisNumber + ", serialNumber=" + serialNumber + ", hostName=" + hostName + ", siteId=" + siteId
				+ ", system_ip=" + system_ip + ", localsystemip=" + localsystemip + "]";
	}
	
	
		
}
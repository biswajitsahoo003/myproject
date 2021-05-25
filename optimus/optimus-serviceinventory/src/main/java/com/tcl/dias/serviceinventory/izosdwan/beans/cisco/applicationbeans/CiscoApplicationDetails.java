
package com.tcl.dias.serviceinventory.izosdwan.beans.cisco.applicationbeans;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CiscoApplicationDetails implements Serializable {

	@JsonProperty("family-long-name")
	private String familyLongName;
	@JsonProperty("vdevice-dataKey")
	private String vdeviceDataKey;
	@JsonProperty("application")
	private String application;
	@JsonProperty("vdevice-name")
	private String vdeviceName;
	@JsonProperty("lastupdated")
	private String lastupdated;
	@JsonProperty("family")
	private String family;
	@JsonProperty("application-long-name")
	private String appLongName;
	@JsonProperty("vdevice-host-name")
	private String vdeviceHostName;
	
	@JsonProperty("family-long-name")
	public String getFamilyLongName() {
		return familyLongName;
	}
	@JsonProperty("family-long-name")
	public void setFamilyLongName(String familyLongName) {
		this.familyLongName = familyLongName;
	}
	@JsonProperty("vdevice-dataKey")
	public String getVdeviceDataKey() {
		return vdeviceDataKey;
	}
	@JsonProperty("vdevice-dataKey")
	public void setVdeviceDataKey(String vdeviceDataKey) {
		this.vdeviceDataKey = vdeviceDataKey;
	}
	@JsonProperty("application")
	public String getApplication() {
		return application;
	}
	@JsonProperty("application")
	public void setApplication(String application) {
		this.application = application;
	}
	@JsonProperty("vdevice-name")
	public String getVdeviceName() {
		return vdeviceName;
	}
	@JsonProperty("vdevice-name")
	public void setVdeviceName(String vdeviceName) {
		this.vdeviceName = vdeviceName;
	}
	@JsonProperty("lastupdated")
	public String getLastupdated() {
		return lastupdated;
	}
	@JsonProperty("lastupdated")
	public void setLastupdated(String lastupdated) {
		this.lastupdated = lastupdated;
	}
	@JsonProperty("family")
	public String getFamily() {
		return family;
	}
	@JsonProperty("family")
	public void setFamily(String family) {
		this.family = family;
	}
	@JsonProperty("application-long-name")
	public String getAppLongName() {
		return appLongName;
	}
	@JsonProperty("application-long-name")
	public void setAppLongName(String appLongName) {
		this.appLongName = appLongName;
	}
	@JsonProperty("vdevice-host-name")
	public String getVdeviceHostName() {
		return vdeviceHostName;
	}
	@JsonProperty("vdevice-host-name")
	public void setVdeviceHostName(String vdeviceHostName) {
		this.vdeviceHostName = vdeviceHostName;
	}
	@Override
	public String toString() {
		return "CiscoApplicationDetails [familyLongName=" + familyLongName + ", vdeviceDataKey=" + vdeviceDataKey
				+ ", application=" + application + ", vdeviceName=" + vdeviceName + ", lastupdated=" + lastupdated
				+ ", family=" + family + ", appLongName=" + appLongName + ", vdeviceHostName=" + vdeviceHostName + "]";
	}
	
	
	
	}
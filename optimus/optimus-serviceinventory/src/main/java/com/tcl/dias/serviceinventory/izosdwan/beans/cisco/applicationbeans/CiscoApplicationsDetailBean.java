package com.tcl.dias.serviceinventory.izosdwan.beans.cisco.applicationbeans;

import java.io.Serializable;

/**
 * Bean class for SDWAN application
 * @author archchan
 *
 */
public class CiscoApplicationsDetailBean implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//private String familyLongName;
	//private String vdeviceDataKey;
	private String application;
	//private String vdeviceName;
	//private String lastupdated;
	//private String family;
	private String appLongName;
	private String vdeviceHostName;
	private String applicationType;
//	public String getFamilyLongName() {
//		return familyLongName;
//	}
//	public void setFamilyLongName(String familyLongName) {
//		this.familyLongName = familyLongName;
//	}
//	public String getVdeviceDataKey() {
//		return vdeviceDataKey;
//	}
//	public void setVdeviceDataKey(String vdeviceDataKey) {
//		this.vdeviceDataKey = vdeviceDataKey;
//	}
	public String getApplication() {
		return application;
	}
	public void setApplication(String application) {
		this.application = application;
	}
//	public String getVdeviceName() {
//		return vdeviceName;
//	}
//	public void setVdeviceName(String vdeviceName) {
//		this.vdeviceName = vdeviceName;
//	}
//	public String getLastupdated() {
//		return lastupdated;
//	}
//	public void setLastupdated(String lastupdated) {
//		this.lastupdated = lastupdated;
//	}
//	public String getFamily() {
//		return family;
//	}
//	public void setFamily(String family) {
//		this.family = family;
//	}
	public String getAppLongName() {
		return appLongName;
	}
	public void setAppLongName(String appLongName) {
		this.appLongName = appLongName;
	}
	public String getVdeviceHostName() {
		return vdeviceHostName;
	}
	public void setVdeviceHostName(String vdeviceHostName) {
		this.vdeviceHostName = vdeviceHostName;
	}
	
	public String getApplicationType() {
		return applicationType;
	}
	public void setApplicationType(String applicationType) {
		this.applicationType = applicationType;
	}
	@Override
	public String toString() {
		return "CiscoApplicationsDetailBean [application=" + application + ", appLongName=" + appLongName
				+ ", vdeviceHostName=" + vdeviceHostName + ", applicationType=" + applicationType + "]";
	}
	
	

}

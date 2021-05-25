package com.tcl.dias.common.serviceinventory.bean;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 
 * Cpe Bean Class
 * 
 *
 * @author diksha garg
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */

public class CpeBean implements Serializable {
	private static final long serialVersionUID = 1L;

	
	private Integer cpeId;
	private Boolean cpeinitConfigparams;
	private Timestamp endDate;
	private String hostName;
	private String initLoginpwd;
	private String initUsername;
	private Boolean isaceconfigurable;
	private boolean isEdited;
	private Timestamp lastModifiedDate;
	private String loopbackInterfaceName;
	private String mgmtLoopbackV4address;
	private String mgmtLoopbackV6address;
	private String modifiedBy;
	private Boolean nniCpeConfig;
	private Boolean sendInittemplate;
	private String serviceId;
	private String snmpServerCommunity;
	private Timestamp startDate;
	private String model;
	private String unmanagedCePartnerdeviceWanip;
	private Boolean vsatCpeConfig;
	private String deviceId;
	private String make;

	public Integer getCpeId() {
		return cpeId;
	}

	public void setCpeId(Integer cpeId) {
		this.cpeId = cpeId;
	}

	public Boolean getCpeinitConfigparams() {
		return cpeinitConfigparams;
	}

	public void setCpeinitConfigparams(Boolean cpeinitConfigparams) {
		this.cpeinitConfigparams = cpeinitConfigparams;
	}

	public Timestamp getEndDate() {
		return endDate;
	}

	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public String getInitLoginpwd() {
		return initLoginpwd;
	}

	public void setInitLoginpwd(String initLoginpwd) {
		this.initLoginpwd = initLoginpwd;
	}

	public String getInitUsername() {
		return initUsername;
	}

	public void setInitUsername(String initUsername) {
		this.initUsername = initUsername;
	}

	public Boolean getIsaceconfigurable() {
		return isaceconfigurable;
	}

	public void setIsaceconfigurable(Boolean isaceconfigurable) {
		this.isaceconfigurable = isaceconfigurable;
	}

	public Timestamp getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(Timestamp lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public String getLoopbackInterfaceName() {
		return loopbackInterfaceName;
	}

	public void setLoopbackInterfaceName(String loopbackInterfaceName) {
		this.loopbackInterfaceName = loopbackInterfaceName;
	}

	public String getMgmtLoopbackV4address() {
		return mgmtLoopbackV4address;
	}

	public void setMgmtLoopbackV4address(String mgmtLoopbackV4address) {
		this.mgmtLoopbackV4address = mgmtLoopbackV4address;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Boolean getNniCpeConfig() {
		return nniCpeConfig;
	}

	public void setNniCpeConfig(Boolean nniCpeConfig) {
		this.nniCpeConfig = nniCpeConfig;
	}

	public Boolean getSendInittemplate() {
		return sendInittemplate;
	}

	public void setSendInittemplate(Boolean sendInittemplate) {
		this.sendInittemplate = sendInittemplate;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getSnmpServerCommunity() {
		return snmpServerCommunity;
	}

	public void setSnmpServerCommunity(String snmpServerCommunity) {
		this.snmpServerCommunity = snmpServerCommunity;
	}

	public Timestamp getStartDate() {
		return startDate;
	}

	public void setStartDate(Timestamp startDate) {
		this.startDate = startDate;
	}

	public String getUnmanagedCePartnerdeviceWanip() {
		return unmanagedCePartnerdeviceWanip;
	}

	public void setUnmanagedCePartnerdeviceWanip(String unmanagedCePartnerdeviceWanip) {
		this.unmanagedCePartnerdeviceWanip = unmanagedCePartnerdeviceWanip;
	}

	public Boolean getVsatCpeConfig() {
		return vsatCpeConfig;
	}

	public void setVsatCpeConfig(Boolean vsatCpeConfig) {
		this.vsatCpeConfig = vsatCpeConfig;
	}

	public boolean isEdited() {
		return isEdited;
	}

	public void setEdited(boolean edited) {
		isEdited = edited;
	}

	public String getMgmtLoopbackV6address() {
		return mgmtLoopbackV6address;
	}

	public void setMgmtLoopbackV6address(String mgmtLoopbackV6address) {
		this.mgmtLoopbackV6address = mgmtLoopbackV6address;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getMake() {
		return make;
	}

	public void setMake(String make) {
		this.make = make;
	}
}
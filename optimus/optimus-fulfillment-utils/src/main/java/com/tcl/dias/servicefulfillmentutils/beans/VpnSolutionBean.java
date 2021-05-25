package com.tcl.dias.servicefulfillmentutils.beans;

import java.io.Serializable;
import java.sql.Timestamp;


/**
 * 
 * VpnSolution Bean Class
 * 
 *
 * @author diksha garg
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */

public class VpnSolutionBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer vpnSolutionId;
	private Timestamp endDate;
	private String instanceId;
	private String interfaceName;
	private Boolean isE2eIntegrated;
	private String isUa;
	private Boolean isenableUccService;
	private Timestamp lastModifiedDate;
	private String legRole;
	private String managementVpnType1;
	private String managementVpnType2;
	private String modifiedBy;
	private String siteId;
	private Timestamp startDate;
	private String vpnLegId;
	private String vpnName;
	private String vpnSolutionName;
	private String vpnTopology;
	private String vpnType;
	private boolean isEdited;
	public Integer getVpnSolutionId() {
		return vpnSolutionId;
	}
	public void setVpnSolutionId(Integer vpnSolutionId) {
		this.vpnSolutionId = vpnSolutionId;
	}
	public Timestamp getEndDate() {
		return endDate;
	}
	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
	}
	public String getInstanceId() {
		return instanceId;
	}
	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}
	public String getInterfaceName() {
		return interfaceName;
	}
	public void setInterfaceName(String interfaceName) {
		this.interfaceName = interfaceName;
	}
	public Boolean getIsE2eIntegrated() {
		return isE2eIntegrated;
	}
	public void setIsE2eIntegrated(Boolean isE2eIntegrated) {
		this.isE2eIntegrated = isE2eIntegrated;
	}
	public String getIsUa() {
		return isUa;
	}
	public void setIsUa(String isUa) {
		this.isUa = isUa;
	}
	public Boolean getIsenableUccService() {
		return isenableUccService;
	}
	public void setIsenableUccService(Boolean isenableUccService) {
		this.isenableUccService = isenableUccService;
	}
	public Timestamp getLastModifiedDate() {
		return lastModifiedDate;
	}
	public void setLastModifiedDate(Timestamp lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}
	public String getLegRole() {
		return legRole;
	}
	public void setLegRole(String legRole) {
		this.legRole = legRole;
	}
	public String getManagementVpnType1() {
		return managementVpnType1;
	}
	public void setManagementVpnType1(String managementVpnType1) {
		this.managementVpnType1 = managementVpnType1;
	}
	public String getManagementVpnType2() {
		return managementVpnType2;
	}
	public void setManagementVpnType2(String managementVpnType2) {
		this.managementVpnType2 = managementVpnType2;
	}
	public String getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	public String getSiteId() {
		return siteId;
	}
	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}
	public Timestamp getStartDate() {
		return startDate;
	}
	public void setStartDate(Timestamp startDate) {
		this.startDate = startDate;
	}
	public String getVpnLegId() {
		return vpnLegId;
	}
	public void setVpnLegId(String vpnLegId) {
		this.vpnLegId = vpnLegId;
	}
	public String getVpnName() {
		return vpnName;
	}
	public void setVpnName(String vpnName) {
		this.vpnName = vpnName;
	}
	public String getVpnSolutionName() {
		return vpnSolutionName;
	}
	public void setVpnSolutionName(String vpnSolutionName) {
		this.vpnSolutionName = vpnSolutionName;
	}
	public String getVpnTopology() {
		return vpnTopology;
	}
	public void setVpnTopology(String vpnTopology) {
		this.vpnTopology = vpnTopology;
	}
	public String getVpnType() {
		return vpnType;
	}
	public void setVpnType(String vpnType) {
		this.vpnType = vpnType;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public boolean isEdited() {
		return isEdited;
	}

	public void setEdited(boolean edited) {
		isEdited = edited;
	}
}
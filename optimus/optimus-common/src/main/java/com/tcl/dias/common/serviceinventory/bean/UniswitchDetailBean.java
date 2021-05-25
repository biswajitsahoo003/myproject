package com.tcl.dias.common.serviceinventory.bean;

import java.io.Serializable;
import java.sql.Timestamp;


/**
 * UniswitchDetails Bean class
 * 
 * @author diksha garg
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 * 
 */

public class UniswitchDetailBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer uniswitchId;
	private String autonegotiationEnabled;
	private String duplex;
	private Timestamp endDate;
	private boolean isEdited;
	private String handoff;
	private String hostName;
	private String innerVlan;
	private String interfaceName;
	private Timestamp lastModifiedDate;
	private String maxMacLimit;
	private String mediaType;
	private String mgmtIp;
	private String mode;
	private String modifiedBy;
	private String outerVlan;
	private String physicalPort;
	private String portType;
	private String speed;
	private Timestamp startDate;
	private String switchModel;
	private Boolean syncVlanReqd;
	private Integer topologyServiceDetailsId;
	public Integer getUniswitchId() {
		return uniswitchId;
	}
	public void setUniswitchId(Integer uniswitchId) {
		this.uniswitchId = uniswitchId;
	}
	public String getAutonegotiationEnabled() {
		return autonegotiationEnabled;
	}
	public void setAutonegotiationEnabled(String autonegotiationEnabled) {
		this.autonegotiationEnabled = autonegotiationEnabled;
	}
	public String getDuplex() {
		return duplex;
	}
	public void setDuplex(String duplex) {
		this.duplex = duplex;
	}
	public Timestamp getEndDate() {
		return endDate;
	}
	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
	}
	public String getHandoff() {
		return handoff;
	}
	public void setHandoff(String handoff) {
		this.handoff = handoff;
	}
	public String getHostName() {
		return hostName;
	}
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
	public String getInnerVlan() {
		return innerVlan;
	}
	public void setInnerVlan(String innerVlan) {
		this.innerVlan = innerVlan;
	}
	public String getInterfaceName() {
		return interfaceName;
	}
	public void setInterfaceName(String interfaceName) {
		this.interfaceName = interfaceName;
	}
	public Timestamp getLastModifiedDate() {
		return lastModifiedDate;
	}
	public void setLastModifiedDate(Timestamp lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}
	public String getMaxMacLimit() {
		return maxMacLimit;
	}
	public void setMaxMacLimit(String maxMacLimit) {
		this.maxMacLimit = maxMacLimit;
	}
	public String getMediaType() {
		return mediaType;
	}
	public void setMediaType(String mediaType) {
		this.mediaType = mediaType;
	}
	public String getMgmtIp() {
		return mgmtIp;
	}
	public void setMgmtIp(String mgmtIp) {
		this.mgmtIp = mgmtIp;
	}
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	public String getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	public String getOuterVlan() {
		return outerVlan;
	}
	public void setOuterVlan(String outerVlan) {
		this.outerVlan = outerVlan;
	}
	public String getPhysicalPort() {
		return physicalPort;
	}
	public void setPhysicalPort(String physicalPort) {
		this.physicalPort = physicalPort;
	}
	public String getPortType() {
		return portType;
	}
	public void setPortType(String portType) {
		this.portType = portType;
	}
	public String getSpeed() {
		return speed;
	}
	public void setSpeed(String speed) {
		this.speed = speed;
	}
	public Timestamp getStartDate() {
		return startDate;
	}
	public void setStartDate(Timestamp startDate) {
		this.startDate = startDate;
	}
	public String getSwitchModel() {
		return switchModel;
	}
	public void setSwitchModel(String switchModel) {
		this.switchModel = switchModel;
	}
	public Boolean getSyncVlanReqd() {
		return syncVlanReqd;
	}
	public void setSyncVlanReqd(Boolean syncVlanReqd) {
		this.syncVlanReqd = syncVlanReqd;
	}
	public Integer getTopologyServiceDetailsId() {
		return topologyServiceDetailsId;
	}
	public void setTopologyServiceDetailsId(Integer topologyServiceDetailsId) {
		this.topologyServiceDetailsId = topologyServiceDetailsId;
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

}

package com.tcl.dias.serviceinventory.izosdwan.beans.cisco.cpe_status;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
//@JsonPropertyOrder({ "name", "uuid", "applianceLocation", "last-updated-time", "ping-status", "sync-status",
//		"createdAt", "yang-compatibility-status", "services-status", "overall-status", "controller-status",
//		"path-status", "nodes", "ucpe-nodes", "intra-chassis-ha-status", "ownerOrgUuid", "ownerOrg", "type", "cmsOrg",
//		"sngCount", "softwareVersion", "currentUpgradeStatus", "lastUpgradeStatus", "connector", "connectorType",
//		"branchId", "services", "ipAddress", "location", "alarmSummary", "cpeHealth", "controllers",
//		"refreshCycleCount", "subType", "branch-maintenance-mode" })
public class CiscoBulkCpeDetails implements Serializable {

	@JsonProperty("deviceId")
	private String deviceId;
	@JsonProperty("system-ip")
	private String systemIp;
	@JsonProperty("host-name")
	private String hostName;
	@JsonProperty("reachability")
	private String reachability;
	@JsonProperty("status")
	private String status;
	@JsonProperty("personality")
	private String personality;
	@JsonProperty("device-type")
	private String deviceType;
	@JsonProperty("timezone")
	private String timezone;
	@JsonProperty("device-groups")
	private List<String> deviceGroups;
	@JsonProperty("lastupdated")
	private String lastupdated;
	@JsonProperty("domain-id")
	private String domainId;
	@JsonProperty("board-serial")
	private String boardSerial;
	@JsonProperty("certificate-validity")
	private String certificateValidity;
	@JsonProperty("max-controllers")
	private String maxControllers;
	@JsonProperty("uuid")
	private String uuid;
	@JsonProperty("controlConnections")
	private String controlConnections;
	@JsonProperty("device-model")
	private String deviceModel;
	@JsonProperty("version")
	private String version;
	@JsonProperty("connectedVManages")
	private List<String> connectedVManages;
	@JsonProperty("siteId")
	private String siteId;
	@JsonProperty("latitude")
	private String latitude;
	@JsonProperty("longitude")
	private String longitude;
	@JsonProperty("isDeviceGeoData")
	private boolean isDeviceGeoData;
	@JsonProperty("platform")
	private String platform;
	@JsonProperty("uptime-date")
	private String uptimeDate;
	@JsonProperty("statusOrder")
	private String statusOrder;
	@JsonProperty("device-os")
	private String deviceOs;
	@JsonProperty("validity")
	private String validity;
	@JsonProperty("state")
	private String state;
	@JsonProperty("state_description")
	private String state_description;
	@JsonProperty("model_sku")
	private String modelSku;
	@JsonProperty("local-system-ip")
	private String localSystemIp;
	@JsonProperty("total_cpu_count")
	private String total_cpu_count;
	@JsonProperty("testbed_mode")
	private String testbed_mode;
	@JsonProperty("layoutLevel")
	private String layoutLevel;
	
	@JsonProperty("deviceId")
	public String getDeviceId() {
		return deviceId;
	}
	@JsonProperty("deviceId")
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	@JsonProperty("system-ip")
	public String getSystemIp() {
		return systemIp;
	}
	@JsonProperty("system-ip")
	public void setSystemIp(String systemIp) {
		this.systemIp = systemIp;
	}
	@JsonProperty("host-name")
	public String getHostName() {
		return hostName;
	}
	@JsonProperty("host-name")
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
	@JsonProperty("reachability")
	public String getReachability() {
		return reachability;
	}
	@JsonProperty("reachability")
	public void setReachability(String reachability) {
		this.reachability = reachability;
	}
	@JsonProperty("status")
	public String getStatus() {
		return status;
	}
	@JsonProperty("status")
	public void setStatus(String status) {
		this.status = status;
	}
	@JsonProperty("personality")
	public String getPersonality() {
		return personality;
	}
	@JsonProperty("personality")
	public void setPersonality(String personality) {
		this.personality = personality;
	}
	@JsonProperty("device-type")
	public String getDeviceType() {
		return deviceType;
	}
	@JsonProperty("device-type")
	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
	@JsonProperty("timezone")
	public String getTimezone() {
		return timezone;
	}
	@JsonProperty("timezone")
	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}
	@JsonProperty("device-groups")
	public List<String> getDeviceGroups() {
		return deviceGroups;
	}
	@JsonProperty("device-groups")
	public void setDeviceGroups(List<String> deviceGroups) {
		this.deviceGroups = deviceGroups;
	}
	@JsonProperty("lastupdated")
	public String getLastupdated() {
		return lastupdated;
	}
	@JsonProperty("lastupdated")
	public void setLastupdated(String lastupdated) {
		this.lastupdated = lastupdated;
	}
	@JsonProperty("domain-id")
	public String getDomainId() {
		return domainId;
	}
	@JsonProperty("domain-id")
	public void setDomainId(String domainId) {
		this.domainId = domainId;
	}
	@JsonProperty("board-serial")
	public String getBoardSerial() {
		return boardSerial;
	}
	@JsonProperty("board-serial")
	public void setBoardSerial(String boardSerial) {
		this.boardSerial = boardSerial;
	}
	@JsonProperty("certificate-validity")
	public String getCertificateValidity() {
		return certificateValidity;
	}
	@JsonProperty("certificate-validity")
	public void setCertificateValidity(String certificateValidity) {
		this.certificateValidity = certificateValidity;
	}
	@JsonProperty("max-controllers")
	public String getMaxControllers() {
		return maxControllers;
	}
	@JsonProperty("max-controllers")
	public void setMaxControllers(String maxControllers) {
		this.maxControllers = maxControllers;
	}
	@JsonProperty("uuid")
	public String getUuid() {
		return uuid;
	}
	@JsonProperty("uuid")
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	@JsonProperty("controlConnections")
	public String getControlConnections() {
		return controlConnections;
	}
	@JsonProperty("controlConnections")
	public void setControlConnections(String controlConnections) {
		this.controlConnections = controlConnections;
	}
	@JsonProperty("device-model")
	public String getDeviceModel() {
		return deviceModel;
	}
	@JsonProperty("device-model")
	public void setDeviceModel(String deviceModel) {
		this.deviceModel = deviceModel;
	}
	@JsonProperty("version")
	public String getVersion() {
		return version;
	}
	@JsonProperty("version")
	public void setVersion(String version) {
		this.version = version;
	}
	@JsonProperty("connectedVManages")
	public List<String> getConnectedVManages() {
		return connectedVManages;
	}
	@JsonProperty("connectedVManages")
	public void setConnectedVManages(List<String> connectedVManages) {
		this.connectedVManages = connectedVManages;
	}
	@JsonProperty("site-id")
	public String getSiteId() {
		return siteId;
	}
	@JsonProperty("site-id")
	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}
	@JsonProperty("latitude")
	public String getLatitude() {
		return latitude;
	}
	@JsonProperty("latitude")
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	@JsonProperty("longitude")
	public String getLongitude() {
		return longitude;
	}
	@JsonProperty("longitude")
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	@JsonProperty("isDeviceGeoData")
	public boolean isDeviceGeoData() {
		return isDeviceGeoData;
	}
	@JsonProperty("isDeviceGeoData")
	public void setDeviceGeoData(boolean isDeviceGeoData) {
		this.isDeviceGeoData = isDeviceGeoData;
	}
	@JsonProperty("platform")
	public String getPlatform() {
		return platform;
	}
	@JsonProperty("platform")
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	@JsonProperty("uptime-date")
	public String getUptimeDate() {
		return uptimeDate;
	}
	@JsonProperty("uptime-date")
	public void setUptimeDate(String uptimeDate) {
		this.uptimeDate = uptimeDate;
	}
	@JsonProperty("statusOrder")
	public String getStatusOrder() {
		return statusOrder;
	}
	@JsonProperty("statusOrder")
	public void setStatusOrder(String statusOrder) {
		this.statusOrder = statusOrder;
	}
	@JsonProperty("device-os")
	public String getDeviceOs() {
		return deviceOs;
	}
	@JsonProperty("device-os")
	public void setDeviceOs(String deviceOs) {
		this.deviceOs = deviceOs;
	}
	@JsonProperty("validity")
	public String getValidity() {
		return validity;
	}
	@JsonProperty("validity")
	public void setValidity(String validity) {
		this.validity = validity;
	}
	@JsonProperty("state")
	public String getState() {
		return state;
	}
	@JsonProperty("state")
	public void setState(String state) {
		this.state = state;
	}
	@JsonProperty("state_description")
	public String getState_description() {
		return state_description;
	}
	@JsonProperty("state_description")
	public void setState_description(String state_description) {
		this.state_description = state_description;
	}
	@JsonProperty("model_sku")
	public String getModelSku() {
		return modelSku;
	}
	@JsonProperty("model_sku")
	public void setModelSku(String modelSku) {
		this.modelSku = modelSku;
	}
	@JsonProperty("local-system-ip")
	public String getLocalSystemIp() {
		return localSystemIp;
	}
	@JsonProperty("local-system-ip")
	public void setLocalSystemIp(String localSystemIp) {
		this.localSystemIp = localSystemIp;
	}
	@JsonProperty("total_cpu_count")
	public String getTotal_cpu_count() {
		return total_cpu_count;
	}
	@JsonProperty("total_cpu_count")
	public void setTotal_cpu_count(String total_cpu_count) {
		this.total_cpu_count = total_cpu_count;
	}
	@JsonProperty("testbed_mode")
	public String getTestbed_mode() {
		return testbed_mode;
	}
	@JsonProperty("testbed_mode")
	public void setTestbed_mode(String testbed_mode) {
		this.testbed_mode = testbed_mode;
	}
	@JsonProperty("layoutLevel")
	public String getLayoutLevel() {
		return layoutLevel;
	}
	@JsonProperty("layoutLevel")
	public void setLayoutLevel(String layoutLevel) {
		this.layoutLevel = layoutLevel;
	}
	@Override
	public String toString() {
		return "CiscoBulkCpeDetails [deviceId=" + deviceId + ", systemIp=" + systemIp + ", hostName=" + hostName
				+ ", reachability=" + reachability + ", status=" + status + ", personality=" + personality
				+ ", deviceType=" + deviceType + ", timezone=" + timezone + ", deviceGroups=" + deviceGroups
				+ ", lastupdated=" + lastupdated + ", domainId=" + domainId + ", boardSerial=" + boardSerial
				+ ", certificateValidity=" + certificateValidity + ", maxControllers=" + maxControllers + ", uuid="
				+ uuid + ", controlConnections=" + controlConnections + ", deviceModel=" + deviceModel + ", version="
				+ version + ", connectedVManages=" + connectedVManages + ", siteId=" + siteId + ", latitude=" + latitude
				+ ", longitude=" + longitude + ", isDeviceGeoData=" + isDeviceGeoData + ", platform=" + platform
				+ ", uptimeDate=" + uptimeDate + ", statusOrder=" + statusOrder + ", deviceOs=" + deviceOs
				+ ", validity=" + validity + ", state=" + state + ", state_description=" + state_description
				+ ", modelSku=" + modelSku + ", localSystemIp=" + localSystemIp + ", total_cpu_count=" + total_cpu_count
				+ ", testbed_mode=" + testbed_mode + ", layoutLevel=" + layoutLevel + "]";
	}
	
	
	
	
	
}
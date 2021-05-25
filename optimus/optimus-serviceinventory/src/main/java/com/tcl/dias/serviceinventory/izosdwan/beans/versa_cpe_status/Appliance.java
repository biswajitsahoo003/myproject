
package com.tcl.dias.serviceinventory.izosdwan.beans.versa_cpe_status;

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
@JsonPropertyOrder({ "name", "uuid", "applianceLocation", "last-updated-time", "ping-status", "sync-status",
		"createdAt", "yang-compatibility-status", "services-status", "overall-status", "controller-status",
		"path-status", "nodes", "ucpe-nodes", "intra-chassis-ha-status", "ownerOrgUuid", "ownerOrg", "type", "cmsOrg",
		"sngCount", "softwareVersion", "currentUpgradeStatus", "lastUpgradeStatus", "connector", "connectorType",
		"branchId", "services", "ipAddress", "location", "alarmSummary", "cpeHealth", "controllers",
		"refreshCycleCount", "subType", "branch-maintenance-mode" })
public class Appliance implements Serializable {

	@JsonProperty("name")
	private String name;
	@JsonProperty("uuid")
	private String uuid;
	@JsonProperty("applianceLocation")
	private ApplianceLocation applianceLocation;
	@JsonProperty("last-updated-time")
	private String lastUpdatedTime;
	@JsonProperty("ping-status")
	private String pingStatus;
	@JsonProperty("sync-status")
	private String syncStatus;
	@JsonProperty("createdAt")
	private String createdAt;
	@JsonProperty("yang-compatibility-status")
	private String yangCompatibilityStatus;
	@JsonProperty("services-status")
	private String servicesStatus;
	@JsonProperty("overall-status")
	private String overallStatus;
	@JsonProperty("controller-status")
	private String controllerStatus;
	@JsonProperty("path-status")
	private String pathStatus;
	@JsonProperty("nodes")
	private Nodes nodes;
	@JsonProperty("ucpe-nodes")
	private UcpeNodeStatusList ucpeNodes;
	@JsonProperty("intra-chassis-ha-status")
	private IntraChassisHaStatus intraChassisHaStatus;
	@JsonProperty("ownerOrgUuid")
	private String ownerOrgUuid;
	@JsonProperty("ownerOrg")
	private String ownerOrg;
	@JsonProperty("type")
	private String type;
	@JsonProperty("cmsOrg")
	private String cmsOrg;
	@JsonProperty("sngCount")
	private Integer sngCount;
	@JsonProperty("softwareVersion")
	private String softwareVersion;
	@JsonProperty("currentUpgradeStatus")
	private String currentUpgradeStatus;
	@JsonProperty("lastUpgradeStatus")
	private String lastUpgradeStatus;
	@JsonProperty("connector")
	private String connector;
	@JsonProperty("connectorType")
	private String connectorType;
	@JsonProperty("branchId")
	private Integer branchId;
	@JsonProperty("services")
	private List<String> services = null;
	@JsonProperty("ipAddress")
	private String ipAddress;
	@JsonProperty("location")
	private String location;
	@JsonProperty("Hardware")
	private Hardware hardware;
	@JsonProperty("alarmSummary")
	private AlarmSummary alarmSummary;
	@JsonProperty("cpeHealth")
	private CpeHealth cpeHealth;
	@JsonProperty("controllers")
	private List<String> controllers = null;
	@JsonProperty("refreshCycleCount")
	private Integer refreshCycleCount;
	@JsonProperty("subType")
	private String subType;
	@JsonProperty("branch-maintenance-mode")
	private Boolean branchMaintenanceMode;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();
	private final static long serialVersionUID = -5994381211935002678L;

	@JsonProperty("name")
	public String getName() {
		return name;
	}

	@JsonProperty("name")
	public void setName(String name) {
		this.name = name;
	}

	@JsonProperty("uuid")
	public String getUuid() {
		return uuid;
	}

	@JsonProperty("uuid")
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	@JsonProperty("applianceLocation")
	public ApplianceLocation getApplianceLocation() {
		return applianceLocation;
	}

	@JsonProperty("applianceLocation")
	public void setApplianceLocation(ApplianceLocation applianceLocation) {
		this.applianceLocation = applianceLocation;
	}

	@JsonProperty("last-updated-time")
	public String getLastUpdatedTime() {
		return lastUpdatedTime;
	}

	@JsonProperty("last-updated-time")
	public void setLastUpdatedTime(String lastUpdatedTime) {
		this.lastUpdatedTime = lastUpdatedTime;
	}

	@JsonProperty("ping-status")
	public String getPingStatus() {
		return pingStatus;
	}

	@JsonProperty("ping-status")
	public void setPingStatus(String pingStatus) {
		this.pingStatus = pingStatus;
	}

	@JsonProperty("sync-status")
	public String getSyncStatus() {
		return syncStatus;
	}

	@JsonProperty("sync-status")
	public void setSyncStatus(String syncStatus) {
		this.syncStatus = syncStatus;
	}

	@JsonProperty("createdAt")
	public String getCreatedAt() {
		return createdAt;
	}

	@JsonProperty("createdAt")
	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	@JsonProperty("yang-compatibility-status")
	public String getYangCompatibilityStatus() {
		return yangCompatibilityStatus;
	}

	@JsonProperty("yang-compatibility-status")
	public void setYangCompatibilityStatus(String yangCompatibilityStatus) {
		this.yangCompatibilityStatus = yangCompatibilityStatus;
	}

	@JsonProperty("services-status")
	public String getServicesStatus() {
		return servicesStatus;
	}

	@JsonProperty("services-status")
	public void setServicesStatus(String servicesStatus) {
		this.servicesStatus = servicesStatus;
	}

	@JsonProperty("overall-status")
	public String getOverallStatus() {
		return overallStatus;
	}

	@JsonProperty("overall-status")
	public void setOverallStatus(String overallStatus) {
		this.overallStatus = overallStatus;
	}

	@JsonProperty("controller-status")
	public String getControllerStatus() {
		return controllerStatus;
	}

	@JsonProperty("controller-status")
	public void setControllerStatus(String controllerStatus) {
		this.controllerStatus = controllerStatus;
	}

	@JsonProperty("path-status")
	public String getPathStatus() {
		return pathStatus;
	}

	@JsonProperty("path-status")
	public void setPathStatus(String pathStatus) {
		this.pathStatus = pathStatus;
	}

	@JsonProperty("nodes")
	public Nodes getNodes() {
		return nodes;
	}

	@JsonProperty("nodes")
	public void setNodes(Nodes nodes) {
		this.nodes = nodes;
	}

	@JsonProperty("ucpe-nodes")
	public UcpeNodeStatusList getUcpeNodes() {
		return ucpeNodes;
	}

	@JsonProperty("ucpe-nodes")
	public void setUcpeNodes(UcpeNodeStatusList ucpeNodes) {
		this.ucpeNodes = ucpeNodes;
	}

	@JsonProperty("intra-chassis-ha-status")
	public IntraChassisHaStatus getIntraChassisHaStatus() {
		return intraChassisHaStatus;
	}

	@JsonProperty("intra-chassis-ha-status")
	public void setIntraChassisHaStatus(IntraChassisHaStatus intraChassisHaStatus) {
		this.intraChassisHaStatus = intraChassisHaStatus;
	}

	@JsonProperty("ownerOrgUuid")
	public String getOwnerOrgUuid() {
		return ownerOrgUuid;
	}

	@JsonProperty("ownerOrgUuid")
	public void setOwnerOrgUuid(String ownerOrgUuid) {
		this.ownerOrgUuid = ownerOrgUuid;
	}

	@JsonProperty("ownerOrg")
	public String getOwnerOrg() {
		return ownerOrg;
	}

	@JsonProperty("ownerOrg")
	public void setOwnerOrg(String ownerOrg) {
		this.ownerOrg = ownerOrg;
	}

	@JsonProperty("type")
	public String getType() {
		return type;
	}

	@JsonProperty("type")
	public void setType(String type) {
		this.type = type;
	}

	@JsonProperty("cmsOrg")
	public String getCmsOrg() {
		return cmsOrg;
	}

	@JsonProperty("cmsOrg")
	public void setCmsOrg(String cmsOrg) {
		this.cmsOrg = cmsOrg;
	}

	@JsonProperty("sngCount")
	public Integer getSngCount() {
		return sngCount;
	}

	@JsonProperty("sngCount")
	public void setSngCount(Integer sngCount) {
		this.sngCount = sngCount;
	}

	@JsonProperty("softwareVersion")
	public String getSoftwareVersion() {
		return softwareVersion;
	}

	@JsonProperty("softwareVersion")
	public void setSoftwareVersion(String softwareVersion) {
		this.softwareVersion = softwareVersion;
	}

	@JsonProperty("currentUpgradeStatus")
	public String getCurrentUpgradeStatus() {
		return currentUpgradeStatus;
	}

	@JsonProperty("currentUpgradeStatus")
	public void setCurrentUpgradeStatus(String currentUpgradeStatus) {
		this.currentUpgradeStatus = currentUpgradeStatus;
	}

	@JsonProperty("lastUpgradeStatus")
	public String getLastUpgradeStatus() {
		return lastUpgradeStatus;
	}

	@JsonProperty("lastUpgradeStatus")
	public void setLastUpgradeStatus(String lastUpgradeStatus) {
		this.lastUpgradeStatus = lastUpgradeStatus;
	}

	@JsonProperty("connector")
	public String getConnector() {
		return connector;
	}

	@JsonProperty("connector")
	public void setConnector(String connector) {
		this.connector = connector;
	}

	@JsonProperty("connectorType")
	public String getConnectorType() {
		return connectorType;
	}

	@JsonProperty("connectorType")
	public void setConnectorType(String connectorType) {
		this.connectorType = connectorType;
	}

	@JsonProperty("branchId")
	public Integer getBranchId() {
		return branchId;
	}

	@JsonProperty("branchId")
	public void setBranchId(Integer branchId) {
		this.branchId = branchId;
	}

	@JsonProperty("services")
	public List<String> getServices() {
		return services;
	}

	@JsonProperty("services")
	public void setServices(List<String> services) {
		this.services = services;
	}

	@JsonProperty("ipAddress")
	public String getIpAddress() {
		return ipAddress;
	}

	@JsonProperty("ipAddress")
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	@JsonProperty("location")
	public String getLocation() {
		return location;
	}

	@JsonProperty("location")
	public void setLocation(String location) {
		this.location = location;
	}

	public Hardware getHardware() {
		return hardware;
	}

	public void setHardware(Hardware hardware) {
		this.hardware = hardware;
	}

	@JsonProperty("alarmSummary")
	public AlarmSummary getAlarmSummary() {
		return alarmSummary;
	}

	@JsonProperty("alarmSummary")
	public void setAlarmSummary(AlarmSummary alarmSummary) {
		this.alarmSummary = alarmSummary;
	}

	@JsonProperty("cpeHealth")
	public CpeHealth getCpeHealth() {
		return cpeHealth;
	}

	@JsonProperty("cpeHealth")
	public void setCpeHealth(CpeHealth cpeHealth) {
		this.cpeHealth = cpeHealth;
	}

	@JsonProperty("controllers")
	public List<String> getControllers() {
		return controllers;
	}

	@JsonProperty("controllers")
	public void setControllers(List<String> controllers) {
		this.controllers = controllers;
	}

	@JsonProperty("refreshCycleCount")
	public Integer getRefreshCycleCount() {
		return refreshCycleCount;
	}

	@JsonProperty("refreshCycleCount")
	public void setRefreshCycleCount(Integer refreshCycleCount) {
		this.refreshCycleCount = refreshCycleCount;
	}

	@JsonProperty("subType")
	public String getSubType() {
		return subType;
	}

	@JsonProperty("subType")
	public void setSubType(String subType) {
		this.subType = subType;
	}

	@JsonProperty("branch-maintenance-mode")
	public Boolean getBranchMaintenanceMode() {
		return branchMaintenanceMode;
	}

	@JsonProperty("branch-maintenance-mode")
	public void setBranchMaintenanceMode(Boolean branchMaintenanceMode) {
		this.branchMaintenanceMode = branchMaintenanceMode;
	}

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

}

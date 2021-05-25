package com.tcl.dias.serviceinventory.entity.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;


/**
 * 
 * This is the entity class for vw_service_asset_info table
 * 
 *
 * @author AnandhiV
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name="vw_service_asset_info")
@NamedQuery(name="VwServiceAssetInfo.findAll", query="SELECT v FROM VwServiceAssetInfo v")
public class VwServiceAssetInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name="asset_name")
	private String assetName;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="asset_sys_id")
	private Integer assetSysId;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="asset_to_srv_support_end_date")
	private Date assetToSrvSupportEndDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="asset_to_srv_support_start_date")
	private Date assetToSrvSupportStartDate;

	@Column(name="asset_type")
	private String assetType;

	@Column(name="circuit_id")
	private String circuitId;

	@Column(name="circuit_status")
	private String circuitStatus;

	private String description;

	private String fqdn;

	@Column(name="gateway_ip")
	private String gatewayIp;

	@Column(name="is_active")
	private String isActive;

	@Column(name="is_shared_ind")
	private String isSharedInd;

	@Column(name="mac_id")
	private String macId;

	@Column(name="managed_by")
	private String managedBy;

	@Column(name="management_ip")
	private String managementIp;

	private String model;

	@Column(name="monitoring_tool")
	private String monitoringTool;

	@Column(name="origin_ntwrk")
	private String originNtwrk;

	private String owner;

	@Column(name="parent_id")
	private Integer parentId;

	@Column(name="public_ip")
	private String publicIp;

	@Column(name="scope_of_management")
	private String scopeOfManagement;

	@Column(name="serial_no")
	private String serialNo;

	@Column(name="service_id")
	private String serviceId;

	@Column(name="srv_sys_id")
	private Integer srvSysId;

	@Column(name="support_type")
	private String supportType;

	@Column(name="wan_ip_address")
	private String wanIpAddress;

	@Column(name="wan_ip_provider")
	private String wanIpProvider;

	public VwServiceAssetInfo() {
	}

	public String getAssetName() {
		return this.assetName;
	}

	public void setAssetName(String assetName) {
		this.assetName = assetName;
	}

	public Integer getAssetSysId() {
		return this.assetSysId;
	}

	public void setAssetSysId(Integer assetSysId) {
		this.assetSysId = assetSysId;
	}

	public Date getAssetToSrvSupportEndDate() {
		return this.assetToSrvSupportEndDate;
	}

	public void setAssetToSrvSupportEndDate(Date assetToSrvSupportEndDate) {
		this.assetToSrvSupportEndDate = assetToSrvSupportEndDate;
	}

	public Date getAssetToSrvSupportStartDate() {
		return this.assetToSrvSupportStartDate;
	}

	public void setAssetToSrvSupportStartDate(Date assetToSrvSupportStartDate) {
		this.assetToSrvSupportStartDate = assetToSrvSupportStartDate;
	}

	public String getAssetType() {
		return this.assetType;
	}

	public void setAssetType(String assetType) {
		this.assetType = assetType;
	}

	public String getCircuitId() {
		return this.circuitId;
	}

	public void setCircuitId(String circuitId) {
		this.circuitId = circuitId;
	}

	public String getCircuitStatus() {
		return this.circuitStatus;
	}

	public void setCircuitStatus(String circuitStatus) {
		this.circuitStatus = circuitStatus;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getFqdn() {
		return this.fqdn;
	}

	public void setFqdn(String fqdn) {
		this.fqdn = fqdn;
	}

	public String getGatewayIp() {
		return this.gatewayIp;
	}

	public void setGatewayIp(String gatewayIp) {
		this.gatewayIp = gatewayIp;
	}

	public String getIsActive() {
		return this.isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public String getIsSharedInd() {
		return this.isSharedInd;
	}

	public void setIsSharedInd(String isSharedInd) {
		this.isSharedInd = isSharedInd;
	}

	public String getMacId() {
		return this.macId;
	}

	public void setMacId(String macId) {
		this.macId = macId;
	}

	public String getManagedBy() {
		return this.managedBy;
	}

	public void setManagedBy(String managedBy) {
		this.managedBy = managedBy;
	}

	public String getManagementIp() {
		return this.managementIp;
	}

	public void setManagementIp(String managementIp) {
		this.managementIp = managementIp;
	}

	public String getModel() {
		return this.model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getMonitoringTool() {
		return this.monitoringTool;
	}

	public void setMonitoringTool(String monitoringTool) {
		this.monitoringTool = monitoringTool;
	}

	public String getOriginNtwrk() {
		return this.originNtwrk;
	}

	public void setOriginNtwrk(String originNtwrk) {
		this.originNtwrk = originNtwrk;
	}

	public String getOwner() {
		return this.owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public Integer getParentId() {
		return this.parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public String getPublicIp() {
		return this.publicIp;
	}

	public void setPublicIp(String publicIp) {
		this.publicIp = publicIp;
	}

	public String getScopeOfManagement() {
		return this.scopeOfManagement;
	}

	public void setScopeOfManagement(String scopeOfManagement) {
		this.scopeOfManagement = scopeOfManagement;
	}

	public String getSerialNo() {
		return this.serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public String getServiceId() {
		return this.serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public Integer getSrvSysId() {
		return this.srvSysId;
	}

	public void setSrvSysId(Integer srvSysId) {
		this.srvSysId = srvSysId;
	}

	public String getSupportType() {
		return this.supportType;
	}

	public void setSupportType(String supportType) {
		this.supportType = supportType;
	}

	public String getWanIpAddress() {
		return this.wanIpAddress;
	}

	public void setWanIpAddress(String wanIpAddress) {
		this.wanIpAddress = wanIpAddress;
	}

	public String getWanIpProvider() {
		return this.wanIpProvider;
	}

	public void setWanIpProvider(String wanIpProvider) {
		this.wanIpProvider = wanIpProvider;
	}

}
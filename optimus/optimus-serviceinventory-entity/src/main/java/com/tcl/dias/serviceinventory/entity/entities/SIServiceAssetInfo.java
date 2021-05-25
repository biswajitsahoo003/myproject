package com.tcl.dias.serviceinventory.entity.entities;


import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Class which defines the view Entity of SI Service Asset Info
 * 
 *
 * @author Thamizhselvi Perumal
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name="vw_service_asset_info")
public class SIServiceAssetInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="asset_sys_id")
	private Integer id;

	@Column(name="srv_sys_id")
	private Integer serviceSystemId;

	@Column(name = "service_id")
	private String serviceId;

	@Column(name="asset_name")
	private String assetName;
	
	@Column(name="fqdn")
	private String fqdn;
	
	@Column(name="management_ip")
	private String managementIp;

	@Column(name="public_ip")
	private String publicIp;
	
	@Column(name="gateway_ip")
	private String gatewayIp;
	
	@Column(name="parent_id")
	private Integer parentId;
	
	@Column(name="description")
	private String description;
	
	@Column(name="model")
	private String model;
	
	@Column(name="mac_id")
	private String macId;
	
	@Column(name="monitoring_tool")
	private String monitoringTool;
	
	@Column(name="managed_by")
	private String managedBy;
	
	@Column(name="serial_no")
	private String serialNo;
	
	@Column(name="owner")
	private String owner;
	
	@Column(name="circuit_id")
	private String circuit_id;
	
	@Column(name="is_active")
	private String isActive;
	
	@Column(name="asset_to_srv_support_start_date")
	private String assetToServiceSupportStartDate;
	
	@Column(name="asset_to_srv_support_end_date")
	private String assetToServiceSupportEndDate;
	
	@Column(name="circuit_status")
	private String circuitStatus;
	
	@Column(name ="asset_tag")
	private String assetTag;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getServiceSystemId() {
		return serviceSystemId;
	}

	public void setServiceSystemId(Integer serviceSystemId) {
		this.serviceSystemId = serviceSystemId;
	}

	public String getAssetName() {
		return assetName;
	}

	public void setAssetName(String assetName) {
		this.assetName = assetName;
	}

	public String getFqdn() {
		return fqdn;
	}

	public void setFqdn(String fqdn) {
		this.fqdn = fqdn;
	}

	public String getManagementIp() {
		return managementIp;
	}

	public void setManagementIp(String managementIp) {
		this.managementIp = managementIp;
	}

	public String getPublicIp() {
		return publicIp;
	}

	public void setPublicIp(String publicIp) {
		this.publicIp = publicIp;
	}

	public String getGatewayIp() {
		return gatewayIp;
	}

	public void setGatewayIp(String gatewayIp) {
		this.gatewayIp = gatewayIp;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getMacId() {
		return macId;
	}

	public void setMacId(String macId) {
		this.macId = macId;
	}

	public String getMonitoringTool() {
		return monitoringTool;
	}

	public void setMonitoringTool(String monitoringTool) {
		this.monitoringTool = monitoringTool;
	}

	public String getManagedBy() {
		return managedBy;
	}

	public void setManagedBy(String managedBy) {
		this.managedBy = managedBy;
	}

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getCircuit_id() {
		return circuit_id;
	}

	public void setCircuit_id(String circuit_id) {
		this.circuit_id = circuit_id;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public String getAssetToServiceSupportStartDate() {
		return assetToServiceSupportStartDate;
	}

	public void setAssetToServiceSupportStartDate(String assetToServiceSupportStartDate) {
		this.assetToServiceSupportStartDate = assetToServiceSupportStartDate;
	}

	public String getAssetToServiceSupportEndDate() {
		return assetToServiceSupportEndDate;
	}

	public void setAssetToServiceSupportEndDate(String assetToServiceSupportEndDate) {
		this.assetToServiceSupportEndDate = assetToServiceSupportEndDate;
	}

	public String getCircuitStatus() {
		return circuitStatus;
	}

	public void setCircuitStatus(String circuitStatus) {
		this.circuitStatus = circuitStatus;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getAssetTag() {
		return assetTag;
	}

	public void setAssetTag(String assetTag) {
		this.assetTag = assetTag;
	}
	
}

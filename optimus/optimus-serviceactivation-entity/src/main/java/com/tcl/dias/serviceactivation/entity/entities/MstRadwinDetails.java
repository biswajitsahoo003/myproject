package com.tcl.dias.serviceactivation.entity.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "mst_radwin_details")
@NamedQuery(name = "MstRadwinDetails.findAll", query = "SELECT m FROM MstRadwinDetails m")
public class MstRadwinDetails implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;

	@Column(name = "service_id")
	private String serviceCode;

	private String latitude;
	
	private String longitude;
	
	private String state;
	
	private String region;

	@Column(name = "customer_name")
	private String customerName;
	
	@Column(name = "device_type")
	private String deviceType;

	@Column(name = "last_modified_by")
	private String lastModifiedBy;

	@Column(name = "last_modified_date")
	private String lastModifiedDate;

	@Column(name = "hsu_mac")
	private String hsuMac;

	@Column(name = "bts_ip")
	private String btsIp;

	@Column(name = "bts_name")
	private String btsName;

	@Column(name = "sector_id")
	private String sectorId;

	@Column(name = "antenna_height")
	private String antennaHeight;
	
	@Column(name = "pole_height")
	private String poleHeight;
	
	@Column(name = "building_height")
	private String buildingHeight;
	
	@Column(name = "serial_number")
	private String serialNumber;

	@Column(name = "hsu_ip")
	private String hsuIp;

	@Column(name = "data_vlan")
	private String dataVlan;

	@Column(name = "mgmt_vlan")
	private String mgmtVlan;
	
	@Column(name = "netp_ref_id")
	private String netpRefId;
	
	@Column(name = "is_active")
	private String isActive;
	
	private String frequency;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getLastModifiedBy() {
		return lastModifiedBy;
	}

	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	public String getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(String lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public String getHsuMac() {
		return hsuMac;
	}

	public void setHsuMac(String hsuMac) {
		this.hsuMac = hsuMac;
	}

	public String getBtsIp() {
		return btsIp;
	}

	public void setBtsIp(String btsIp) {
		this.btsIp = btsIp;
	}

	public String getBtsName() {
		return btsName;
	}

	public void setBtsName(String btsName) {
		this.btsName = btsName;
	}

	public String getSectorId() {
		return sectorId;
	}

	public void setSectorId(String sectorId) {
		this.sectorId = sectorId;
	}

	public String getAntennaHeight() {
		return antennaHeight;
	}

	public void setAntennaHeight(String antennaHeight) {
		this.antennaHeight = antennaHeight;
	}

	public String getHsuIp() {
		return hsuIp;
	}

	public void setHsuIp(String hsuIp) {
		this.hsuIp = hsuIp;
	}

	public String getDataVlan() {
		return dataVlan;
	}

	public void setDataVlan(String dataVlan) {
		this.dataVlan = dataVlan;
	}

	public String getMgmtVlan() {
		return mgmtVlan;
	}

	public void setMgmtVlan(String mgmtVlan) {
		this.mgmtVlan = mgmtVlan;
	}

	public String getFrequency() {
		return frequency;
	}

	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
	
	public String getNetpRefId() {
		return netpRefId;
	}

	public void setNetpRefId(String netpRefId) {
		this.netpRefId = netpRefId;
	}
	
	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}
	
	public String getPoleHeight() {
		return poleHeight;
	}

	public void setPoleHeight(String poleHeight) {
		this.poleHeight = poleHeight;
	}

	public String getBuildingHeight() {
		return buildingHeight;
	}

	public void setBuildingHeight(String buildingHeight) {
		this.buildingHeight = buildingHeight;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

}

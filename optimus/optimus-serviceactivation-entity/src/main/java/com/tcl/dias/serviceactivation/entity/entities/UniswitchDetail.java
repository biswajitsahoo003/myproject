package com.tcl.dias.serviceactivation.entity.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * 
 * UniswitchDetail Entity Class
 * 
 *
 * @author Naveen
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name="uniswitch_details")
@NamedQuery(name="UniswitchDetail.findAll", query="SELECT u FROM UniswitchDetail u")
public class UniswitchDetail implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="uniswitch_id")
	private Integer uniswitchId;

	@Column(name="autonegotiation_enabled")
	private String autonegotiationEnabled;

	private String duplex;

	@Column(name="end_date")
	private Timestamp endDate;

	private String handoff;

	@Column(name="host_name")
	private String hostName;

	@Column(name="inner_vlan")
	private String innerVlan;

	@Column(name="interface_name")
	private String interfaceName;

	@Column(name="last_modified_date")
	private Timestamp lastModifiedDate;

	@Column(name="max_mac_limit")
	private String maxMacLimit;

	@Column(name="media_type")
	private String mediaType;

	@Column(name="mgmt_ip")
	private String mgmtIp;

	private String mode;

	@Column(name="modified_by")
	private String modifiedBy;

	@Column(name="outer_vlan")
	private String outerVlan;

	@Column(name="physical_port")
	private String physicalPort;

	@Column(name="port_type")
	private String portType;

	private String speed;

	@Column(name="start_date")
	private Timestamp startDate;

	@Column(name="switch_model")
	private String switchModel;
	
	@Column(name="unique_port_id")
	private String uniquePortId;

	@Column(name="sync_vlan_reqd")
	private Byte syncVlanReqd;

	//bi-directional many-to-one association to Topology
	@ManyToOne(fetch=FetchType.LAZY)
	private Topology topology;

	public UniswitchDetail() {
	}

	public Integer getUniswitchId() {
		return this.uniswitchId;
	}

	public void setUniswitchId(Integer uniswitchId) {
		this.uniswitchId = uniswitchId;
	}

	public String getAutonegotiationEnabled() {
		return this.autonegotiationEnabled;
	}

	public void setAutonegotiationEnabled(String autonegotiationEnabled) {
		this.autonegotiationEnabled = autonegotiationEnabled;
	}

	public String getDuplex() {
		return this.duplex;
	}

	public void setDuplex(String duplex) {
		this.duplex = duplex;
	}

	public Timestamp getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
	}

	public String getHandoff() {
		return this.handoff;
	}

	public void setHandoff(String handoff) {
		this.handoff = handoff;
	}

	public String getHostName() {
		return this.hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public String getInnerVlan() {
		return this.innerVlan;
	}

	public void setInnerVlan(String innerVlan) {
		this.innerVlan = innerVlan;
	}

	public String getInterfaceName() {
		return this.interfaceName;
	}

	public void setInterfaceName(String interfaceName) {
		this.interfaceName = interfaceName;
	}

	public Timestamp getLastModifiedDate() {
		return this.lastModifiedDate;
	}

	public void setLastModifiedDate(Timestamp lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public String getMaxMacLimit() {
		return this.maxMacLimit;
	}

	public void setMaxMacLimit(String maxMacLimit) {
		this.maxMacLimit = maxMacLimit;
	}

	public String getMediaType() {
		return this.mediaType;
	}

	public void setMediaType(String mediaType) {
		this.mediaType = mediaType;
	}

	public String getMgmtIp() {
		return this.mgmtIp;
	}

	public void setMgmtIp(String mgmtIp) {
		this.mgmtIp = mgmtIp;
	}

	public String getMode() {
		return this.mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public String getModifiedBy() {
		return this.modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public String getOuterVlan() {
		return this.outerVlan;
	}

	public void setOuterVlan(String outerVlan) {
		this.outerVlan = outerVlan;
	}

	public String getPhysicalPort() {
		return this.physicalPort;
	}

	public void setPhysicalPort(String physicalPort) {
		this.physicalPort = physicalPort;
	}

	public String getPortType() {
		return this.portType;
	}

	public void setPortType(String portType) {
		this.portType = portType;
	}

	public String getSpeed() {
		return this.speed;
	}

	public void setSpeed(String speed) {
		this.speed = speed;
	}

	public Timestamp getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Timestamp startDate) {
		this.startDate = startDate;
	}

	public String getSwitchModel() {
		return this.switchModel;
	}

	public void setSwitchModel(String switchModel) {
		this.switchModel = switchModel;
	}

	public Byte getSyncVlanReqd() {
		return this.syncVlanReqd;
	}

	public void setSyncVlanReqd(Byte syncVlanReqd) {
		this.syncVlanReqd = syncVlanReqd;
	}

	public Topology getTopology() {
		return this.topology;
	}

	public void setTopology(Topology topology) {
		this.topology = topology;
	}

	public String getUniquePortId() {
		return uniquePortId;
	}

	public void setUniquePortId(String uniquePortId) {
		this.uniquePortId = uniquePortId;
	}

}
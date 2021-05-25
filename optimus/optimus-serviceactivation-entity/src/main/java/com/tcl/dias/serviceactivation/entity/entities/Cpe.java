package com.tcl.dias.serviceactivation.entity.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Set;

/**
 * 
 * Cpe Entity Class
 * 
 *
 * @author Naveen
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name = "cpe")
@NamedQuery(name = "Cpe.findAll", query = "SELECT c FROM Cpe c")
public class Cpe implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cpe_id")
	private Integer cpeId;

	@Column(name = "cpe_shared")
	private Byte cpeShared;

	@Column(name = "cpe_shared_component")
	private String cpeSharedComponent;

	@Column(name = "cpeinit_configparams")
	private Byte cpeinitConfigparams;

	@Column(name = "device_id")
	private String deviceId;

	@Column(name = "dps_dmvpn_ip")
	private String dpsDmvpnIp;

	@Column(name = "dps_loopback_ip")
	private String dpsLoopbackIp;

	@Column(name = "end_date")
	private Timestamp endDate;

	@Column(name = "host_name")
	private String hostName;

	@Column(name = "init_enablepassword")
	private Byte initEnablepassword;

	@Column(name = "init_loginpwd")
	private String initLoginpwd;

	@Column(name = "init_username")
	private String initUsername;

	private Byte isaceconfigurable;

	@Column(name = "iscpe_reachable")
	private Byte iscpeReachable;

	@Column(name = "last_modified_date")
	private Timestamp lastModifiedDate;

	@Column(name = "loopback_interface_name")
	private String loopbackInterfaceName;

	private String make;

	@Column(name = "mgmt_loopback_v4address")
	private String mgmtLoopbackV4address;

	@Column(name = "mgmt_loopback_v6address")
	private String mgmtLoopbackV6address;

	private String model;

	@Column(name = "modified_by")
	private String modifiedBy;

	@Column(name = "nni_cpe_config")
	private Byte nniCpeConfig;

	@Column(name = "role_type")
	private String roleType;

	@Column(name = "send_inittemplate")
	private Byte sendInittemplate;

	@Column(name = "service_id")
	private String serviceId;

	@Column(name = "site_bgp_as")
	private String siteBgpAs;

	@Column(name = "site_name")
	private String siteName;

	@Column(name = "site_type")
	private String siteType;

	@Column(name = "snmp_server_community")
	private String snmpServerCommunity;

	@Column(name = "start_date")
	private Timestamp startDate;

	@Column(name = "unmanaged_ce_partnerdevice_wanip")
	private String unmanagedCePartnerdeviceWanip;

	@Column(name = "vsat_cpe_config")
	private Byte vsatCpeConfig;

	@Column(name = "wan_circuit_bw_mbps")
	private String wanCircuitBwMbps;

	@Column(name = "wan_interface_name")
	private String wanInterfaceName;

	@Column(name = "wanebgp_peer_ip")
	private String wanebgpPeerIp;

	// bi-directional many-to-one association to InterfaceProtocolMapping
	@OneToMany(mappedBy = "cpe")
	private Set<InterfaceProtocolMapping> interfaceProtocolMappings;

	public Cpe() {
	}

	public Integer getCpeId() {
		return this.cpeId;
	}

	public void setCpeId(Integer cpeId) {
		this.cpeId = cpeId;
	}

	public Byte getCpeShared() {
		return this.cpeShared;
	}

	public void setCpeShared(Byte cpeShared) {
		this.cpeShared = cpeShared;
	}

	public String getCpeSharedComponent() {
		return this.cpeSharedComponent;
	}

	public void setCpeSharedComponent(String cpeSharedComponent) {
		this.cpeSharedComponent = cpeSharedComponent;
	}

	public Byte getCpeinitConfigparams() {
		return this.cpeinitConfigparams;
	}

	public void setCpeinitConfigparams(Byte cpeinitConfigparams) {
		this.cpeinitConfigparams = cpeinitConfigparams;
	}

	public String getDeviceId() {
		return this.deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getDpsDmvpnIp() {
		return this.dpsDmvpnIp;
	}

	public void setDpsDmvpnIp(String dpsDmvpnIp) {
		this.dpsDmvpnIp = dpsDmvpnIp;
	}

	public String getDpsLoopbackIp() {
		return this.dpsLoopbackIp;
	}

	public void setDpsLoopbackIp(String dpsLoopbackIp) {
		this.dpsLoopbackIp = dpsLoopbackIp;
	}

	public Timestamp getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
	}

	public String getHostName() {
		return this.hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public Byte getInitEnablepassword() {
		return this.initEnablepassword;
	}

	public void setInitEnablepassword(Byte initEnablepassword) {
		this.initEnablepassword = initEnablepassword;
	}

	public String getInitLoginpwd() {
		return this.initLoginpwd;
	}

	public void setInitLoginpwd(String initLoginpwd) {
		this.initLoginpwd = initLoginpwd;
	}

	public String getInitUsername() {
		return this.initUsername;
	}

	public void setInitUsername(String initUsername) {
		this.initUsername = initUsername;
	}

	public Byte getIsaceconfigurable() {
		return this.isaceconfigurable;
	}

	public void setIsaceconfigurable(Byte isaceconfigurable) {
		this.isaceconfigurable = isaceconfigurable;
	}

	public Byte getIscpeReachable() {
		return this.iscpeReachable;
	}

	public void setIscpeReachable(Byte iscpeReachable) {
		this.iscpeReachable = iscpeReachable;
	}

	public Timestamp getLastModifiedDate() {
		return this.lastModifiedDate;
	}

	public void setLastModifiedDate(Timestamp lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public String getLoopbackInterfaceName() {
		return this.loopbackInterfaceName;
	}

	public void setLoopbackInterfaceName(String loopbackInterfaceName) {
		this.loopbackInterfaceName = loopbackInterfaceName;
	}

	public String getMake() {
		return this.make;
	}

	public void setMake(String make) {
		this.make = make;
	}

	public String getMgmtLoopbackV4address() {
		return this.mgmtLoopbackV4address;
	}

	public void setMgmtLoopbackV4address(String mgmtLoopbackV4address) {
		this.mgmtLoopbackV4address = mgmtLoopbackV4address;
	}

	public String getModel() {
		return this.model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getModifiedBy() {
		return this.modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Byte getNniCpeConfig() {
		return this.nniCpeConfig;
	}

	public void setNniCpeConfig(Byte nniCpeConfig) {
		this.nniCpeConfig = nniCpeConfig;
	}

	public String getRoleType() {
		return this.roleType;
	}

	public void setRoleType(String roleType) {
		this.roleType = roleType;
	}

	public Byte getSendInittemplate() {
		return this.sendInittemplate;
	}

	public void setSendInittemplate(Byte sendInittemplate) {
		this.sendInittemplate = sendInittemplate;
	}

	public String getServiceId() {
		return this.serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getSiteBgpAs() {
		return this.siteBgpAs;
	}

	public void setSiteBgpAs(String siteBgpAs) {
		this.siteBgpAs = siteBgpAs;
	}

	public String getSiteName() {
		return this.siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public String getSiteType() {
		return this.siteType;
	}

	public void setSiteType(String siteType) {
		this.siteType = siteType;
	}

	public String getSnmpServerCommunity() {
		return this.snmpServerCommunity;
	}

	public void setSnmpServerCommunity(String snmpServerCommunity) {
		this.snmpServerCommunity = snmpServerCommunity;
	}

	public Timestamp getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Timestamp startDate) {
		this.startDate = startDate;
	}

	public String getUnmanagedCePartnerdeviceWanip() {
		return this.unmanagedCePartnerdeviceWanip;
	}

	public void setUnmanagedCePartnerdeviceWanip(String unmanagedCePartnerdeviceWanip) {
		this.unmanagedCePartnerdeviceWanip = unmanagedCePartnerdeviceWanip;
	}

	public Byte getVsatCpeConfig() {
		return this.vsatCpeConfig;
	}

	public void setVsatCpeConfig(Byte vsatCpeConfig) {
		this.vsatCpeConfig = vsatCpeConfig;
	}

	public String getWanCircuitBwMbps() {
		return this.wanCircuitBwMbps;
	}

	public void setWanCircuitBwMbps(String wanCircuitBwMbps) {
		this.wanCircuitBwMbps = wanCircuitBwMbps;
	}

	public String getWanInterfaceName() {
		return this.wanInterfaceName;
	}

	public void setWanInterfaceName(String wanInterfaceName) {
		this.wanInterfaceName = wanInterfaceName;
	}

	public String getWanebgpPeerIp() {
		return this.wanebgpPeerIp;
	}

	public void setWanebgpPeerIp(String wanebgpPeerIp) {
		this.wanebgpPeerIp = wanebgpPeerIp;
	}

	public Set<InterfaceProtocolMapping> getInterfaceProtocolMappings() {
		return this.interfaceProtocolMappings;
	}

	public void setInterfaceProtocolMappings(Set<InterfaceProtocolMapping> interfaceProtocolMappings) {
		this.interfaceProtocolMappings = interfaceProtocolMappings;
	}

	public String getMgmtLoopbackV6address() {
		return mgmtLoopbackV6address;
	}

	public void setMgmtLoopbackV6address(String mgmtLoopbackV6address) {
		this.mgmtLoopbackV6address = mgmtLoopbackV6address;
	}

	public InterfaceProtocolMapping addInterfaceProtocolMapping(InterfaceProtocolMapping interfaceProtocolMapping) {
		getInterfaceProtocolMappings().add(interfaceProtocolMapping);
		interfaceProtocolMapping.setCpe(this);

		return interfaceProtocolMapping;
	}

	public InterfaceProtocolMapping removeInterfaceProtocolMapping(InterfaceProtocolMapping interfaceProtocolMapping) {
		getInterfaceProtocolMappings().remove(interfaceProtocolMapping);
		interfaceProtocolMapping.setCpe(null);

		return interfaceProtocolMapping;
	}

}
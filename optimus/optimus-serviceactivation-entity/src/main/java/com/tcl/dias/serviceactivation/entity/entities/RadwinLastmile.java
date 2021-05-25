package com.tcl.dias.serviceactivation.entity.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;


/**
 * 
 * RadwinLastmile Entity Class
 * 
 *
 * @author Naveen
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name="radwin_lastmile")
@NamedQuery(name="RadwinLastmile.findAll", query="SELECT r FROM RadwinLastmile r")
public class RadwinLastmile implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="radwin_lastmile_id")
	private Integer radwinLastmileId;

	@Column(name="allowed_vlanid")
	private String allowedVlanid;

	@Column(name="bts_ip")
	private String btsIp;

	@Column(name="bts_name")
	private String btsName;

	@Column(name="customer_location")
	private String customerLocation;

	@Column(name="data_vlan_priority")
	private String dataVlanPriority;

	@Column(name="data_vlanid")
	private String dataVlanid;

	@Column(name="end_date")
	private Timestamp endDate;

	private String ethernet_Port_Config;

	private String frequency;

	@Column(name="gateway_ip")
	private String gatewayIp;

	@Column(name="hsu_egress_traffic")
	private String hsuEgressTraffic;

	@Column(name="hsu_ingress_traffic")
	private String hsuIngressTraffic;

	@Column(name="hsu_ip")
	private String hsuIp;

	@Column(name="hsu_mac_addr")
	private String hsuMacAddr;

	@Column(name="hsu_subnet")
	private String hsuSubnet;

	@Column(name="last_modified_date")
	private Timestamp lastModifiedDate;

	@Column(name="mgmt_vlanid")
	private String mgmtVlanid;

	@Column(name="mir_dl")
	private Float mirDl;

	@Column(name="mir_ul")
	private Float mirUl;

	@Column(name="modified_by")
	private String modifiedBy;

	@Column(name="ntp_offset")
	private String ntpOffset;

	@Column(name="ntp_server_ip")
	private String ntpServerIp;

	@Column(name="protocol_snmp")
	private String protocolSnmp;

	@Column(name="protocol_telnet")
	private String protocolTelnet;

	@Column(name="protocol_webinterface")
	private String protocolWebinterface;

	private String reqd_tx_power;

	@Column(name="sector_id")
	private String sectorId;

	@Column(name="site_contact")
	private String siteContact;

	@Column(name="site_lat")
	private String siteLat;

	@Column(name="site_long")
	private String siteLong;

	@Column(name="site_name")
	private String siteName;

	@Column(name="start_date")
	private Timestamp startDate;

	@Column(name="untag_vlan_id")
	private Byte untagVlanId;

	@Column(name="vlan_mode")
	private String vlanMode;
	
	@Column(name="region")
	private String region;
	
	@Column(name="ssvlan_tagging")
	private int ssvlan_tagging;
	
	

	//bi-directional many-to-one association to LmComponent
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="lm_component_lm_component_id")
	private LmComponent lmComponent;

	public RadwinLastmile() {
	}

	public Integer getRadwinLastmileId() {
		return this.radwinLastmileId;
	}

	public void setRadwinLastmileId(Integer radwinLastmileId) {
		this.radwinLastmileId = radwinLastmileId;
	}
	

	public int getSsvlan_tagging() {
		return ssvlan_tagging;
	}

	public void setSsvlan_tagging(int ssvlan_tagging) {
		this.ssvlan_tagging = ssvlan_tagging;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getAllowedVlanid() {
		return this.allowedVlanid;
	}

	public void setAllowedVlanid(String allowedVlanid) {
		this.allowedVlanid = allowedVlanid;
	}

	public String getBtsIp() {
		return this.btsIp;
	}

	public void setBtsIp(String btsIp) {
		this.btsIp = btsIp;
	}

	public String getBtsName() {
		return this.btsName;
	}

	public void setBtsName(String btsName) {
		this.btsName = btsName;
	}

	public String getCustomerLocation() {
		return this.customerLocation;
	}

	public void setCustomerLocation(String customerLocation) {
		this.customerLocation = customerLocation;
	}

	public String getDataVlanPriority() {
		return this.dataVlanPriority;
	}

	public void setDataVlanPriority(String dataVlanPriority) {
		this.dataVlanPriority = dataVlanPriority;
	}

	public String getDataVlanid() {
		return this.dataVlanid;
	}

	public void setDataVlanid(String dataVlanid) {
		this.dataVlanid = dataVlanid;
	}

	public Timestamp getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
	}

	public String getEthernet_Port_Config() {
		return this.ethernet_Port_Config;
	}

	public void setEthernet_Port_Config(String ethernet_Port_Config) {
		this.ethernet_Port_Config = ethernet_Port_Config;
	}

	public String getFrequency() {
		return this.frequency;
	}

	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}

	public String getGatewayIp() {
		return this.gatewayIp;
	}

	public void setGatewayIp(String gatewayIp) {
		this.gatewayIp = gatewayIp;
	}

	public String getHsuEgressTraffic() {
		return this.hsuEgressTraffic;
	}

	public void setHsuEgressTraffic(String hsuEgressTraffic) {
		this.hsuEgressTraffic = hsuEgressTraffic;
	}

	public String getHsuIngressTraffic() {
		return this.hsuIngressTraffic;
	}

	public void setHsuIngressTraffic(String hsuIngressTraffic) {
		this.hsuIngressTraffic = hsuIngressTraffic;
	}

	public String getHsuIp() {
		return this.hsuIp;
	}

	public void setHsuIp(String hsuIp) {
		this.hsuIp = hsuIp;
	}

	public String getHsuMacAddr() {
		return Objects.nonNull(this.hsuMacAddr) ? this.hsuMacAddr.toLowerCase() : this.hsuMacAddr;
	}

	public void setHsuMacAddr(String hsuMacAddr) {
		this.hsuMacAddr = hsuMacAddr;
	}

	public String getHsuSubnet() {
		return this.hsuSubnet;
	}

	public void setHsuSubnet(String hsuSubnet) {
		this.hsuSubnet = hsuSubnet;
	}

	public Timestamp getLastModifiedDate() {
		return this.lastModifiedDate;
	}

	public void setLastModifiedDate(Timestamp lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public String getMgmtVlanid() {
		return this.mgmtVlanid;
	}

	public void setMgmtVlanid(String mgmtVlanid) {
		this.mgmtVlanid = mgmtVlanid;
	}

	

	public String getModifiedBy() {
		return this.modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public String getNtpOffset() {
		return this.ntpOffset;
	}

	public void setNtpOffset(String ntpOffset) {
		this.ntpOffset = ntpOffset;
	}

	public String getNtpServerIp() {
		return this.ntpServerIp;
	}

	public void setNtpServerIp(String ntpServerIp) {
		this.ntpServerIp = ntpServerIp;
	}

	public String getProtocolSnmp() {
		return this.protocolSnmp;
	}

	public void setProtocolSnmp(String protocolSnmp) {
		this.protocolSnmp = protocolSnmp;
	}

	public String getProtocolTelnet() {
		return this.protocolTelnet;
	}

	public void setProtocolTelnet(String protocolTelnet) {
		this.protocolTelnet = protocolTelnet;
	}

	public String getProtocolWebinterface() {
		return this.protocolWebinterface;
	}

	public void setProtocolWebinterface(String protocolWebinterface) {
		this.protocolWebinterface = protocolWebinterface;
	}

	public String getReqd_tx_power() {
		return this.reqd_tx_power;
	}

	public void setReqd_tx_power(String reqd_tx_power) {
		this.reqd_tx_power = reqd_tx_power;
	}

	public String getSectorId() {
		return this.sectorId;
	}

	public void setSectorId(String sectorId) {
		this.sectorId = sectorId;
	}

	public String getSiteContact() {
		return this.siteContact;
	}

	public void setSiteContact(String siteContact) {
		this.siteContact = siteContact;
	}

	public String getSiteLat() {
		return this.siteLat;
	}

	public void setSiteLat(String siteLat) {
		this.siteLat = siteLat;
	}

	public String getSiteLong() {
		return this.siteLong;
	}

	public void setSiteLong(String siteLong) {
		this.siteLong = siteLong;
	}

	public String getSiteName() {
		return this.siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public Timestamp getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Timestamp startDate) {
		this.startDate = startDate;
	}

	public Byte getUntagVlanId() {
		return this.untagVlanId;
	}

	public void setUntagVlanId(Byte untagVlanId) {
		this.untagVlanId = untagVlanId;
	}

	public String getVlanMode() {
		return this.vlanMode;
	}

	public void setVlanMode(String vlanMode) {
		this.vlanMode = vlanMode;
	}

	public LmComponent getLmComponent() {
		return this.lmComponent;
	}

	public void setLmComponent(LmComponent lmComponent) {
		this.lmComponent = lmComponent;
	}

	public Float getMirDl() {
		return mirDl;
	}

	public void setMirDl(Float mirDl) {
		this.mirDl = mirDl;
	}

	public Float getMirUl() {
		return mirUl;
	}

	public void setMirUl(Float mirUl) {
		this.mirUl = mirUl;
	}
	

}
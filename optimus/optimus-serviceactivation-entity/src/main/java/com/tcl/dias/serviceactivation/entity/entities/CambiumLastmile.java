package com.tcl.dias.serviceactivation.entity.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * 
 * CambiumLastmile Entity Class
 * 
 *
 * @author Naveen
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name = "cambium_lastmile")
@NamedQuery(name = "CambiumLastmile.findAll", query = "SELECT c FROM CambiumLastmile c")
public class CambiumLastmile implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cambium_lastmile_id")
	private Integer cambiumLastmileId;

	@Column(name = "accept_qinq_frames")
	private String acceptQinqFrames;

	@Column(name = "allow_frametypes")
	private String allowFrametypes;

	@Column(name = "allowlocallogin_afteraaareject")
	private String allowlocalloginAfteraaareject;

	@Column(name = "authentication_key")
	private String authenticationKey;

	@Column(name = "bridge_entry_timeout")
	private String bridgeEntryTimeout;

	@Column(name = "bs_ip")
	private String bsIp;

	@Column(name = "bs_name")
	private String bsName;

	@Column(name = "bw_downlink_sustained_rate")
	private String bwDownlinkSustainedRate;

	@Column(name = "bw_uplink_sustained_rate")
	private String bwUplinkSustainedRate;

	@Column(name = "cambium_lastmilecol")
	private String cambiumLastmilecol;

	@Column(name = "color_code1")
	private String colorCode1;

	@Column(name = "custom_radio_frequency_list")
	private String customRadioFrequencyList;

	@Column(name = "default_port_vid")
	private String defaultPortVid;

	@Column(name = "device_default_reset")
	private String deviceDefaultReset;

	@Column(name = "device_type")
	private String deviceType;

	@Column(name = "deviceaccess_tracking")
	private String deviceaccessTracking;

	@Column(name = "dhcp_state")
	private String dhcpState;

	@Column(name = "downlink_burst_allocation")
	private String downlinkBurstAllocation;

	@Column(name = "dynamic_learning")
	private String dynamicLearning;

	@Column(name = "dynamic_rate_adapt")
	private String dynamicRateAdapt;

	@Column(name = "enable_broadcast_multicast_datarate")
	private String enableBroadcastMulticastDatarate;

	@Column(name = "end_date")
	private Timestamp endDate;

	@Column(name = "enforce_authentication")
	private String enforceAuthentication;

	@Column(name = "ethernet_access")
	private String ethernetAccess;

	@Column(name = "frame_timing_pulse_gated")
	private String frameTimingPulseGated;

	@Column(name = "hipriority_channel")
	private String hipriorityChannel;

	@Column(name = "hipriority_downlink_cir")
	private String hipriorityDownlinkCir;

	@Column(name = "hipriority_uplink_cir")
	private String hipriorityUplinkCir;

	@Column(name = "home_region")
	private String homeRegion;

	private String identity;

	@Column(name = "installation_color_code")
	private String installationColorCode;

	@Column(name = "largevc_dataq")
	private String largevcDataq;

	@Column(name = "last_modified_date")
	private Timestamp lastModifiedDate;

	@Column(name = "latitude_settings")
	private String latitudeSettings;

	@Column(name = "link_speed")
	private String linkSpeed;

	@Column(name = "longitude_settings")
	private String longitudeSettings;

	@Column(name = "lowpriority_downlink_cir")
	private String lowpriorityDownlinkCir;

	@Column(name = "lowpriority_uplink_cir")
	private String lowpriorityUplinkCir;

	@Column(name = "mapped_mac_address")
	private String mappedMacAddress;

	@Column(name = "mapped_vid1")
	private String mappedVid1;

	@Column(name = "mapped_vid2")
	private Integer mappedVid2;

	@Column(name = "mgmt_ip_for_ss_su")
	private String mgmtIpForSsSu;

	@Column(name = "mgmt_ip_gateway")
	private String mgmtIpGateway;

	@Column(name = "mgmt_subnet_for_ss_su")
	private String mgmtSubnetForSsSu;

	@Column(name = "mgmt_vid")
	private String mgmtVid;

	@Column(name = "modified_by")
	private String modifiedBy;

	@Column(name = "multicast_destination_addr")
	private String multicastDestinationAddr;

	@Column(name = "network_accessibility")
	private String networkAccessibility;

	private String password;

	private String phase1;

	private String phase2;

	@Column(name = "port_speed")
	private Float portSpeed;

	@Column(name = "port_speed_unit")
	private String portSpeedUnit;

	@Column(name = "`powerupmode_withno802.3link`")
	private String powerupmodeWithno802_3link;

	private String provider;

	@Column(name = "provider_vid")
	private Integer providerVid;

	private String realm;

	private String region;

	@Column(name = "region_code")
	private String regionCode;

	@Column(name = "select_key")
	private String selectKey;

	@Column(name = "server_common_name")
	private String serverCommonName;

	@Column(name = "site_contact")
	private String siteContact;

	@Column(name = "site_location")
	private String siteLocation;

	@Column(name = "site_name")
	private String siteName;

	@Column(name = "siteinfo_viewableto_guestusers")
	private String siteinfoViewabletoGuestusers;

	@Column(name = "sm_height")
	private String smHeight;

	@Column(name = "sm_mgmt_vid_passthrough")
	private String smMgmtVidPassthrough;

	@Column(name = "snmp_accessing_ip1")
	private String snmpAccessingIp1;

	@Column(name = "snmp_accessing_ip2")
	private String snmpAccessingIp2;

	@Column(name = "snmp_accessing_ip3")
	private String snmpAccessingIp3;

	@Column(name = "snmp_accessing_subnet_mask1")
	private String snmpAccessingSubnetMask1;

	@Column(name = "snmp_accessing_subnet_mask2")
	private String snmpAccessingSubnetMask2;

	@Column(name = "snmp_accessing_subnet_mask3")
	private String snmpAccessingSubnetMask3;

	@Column(name = "snmp_trap_ip1")
	private String snmpTrapIp1;

	@Column(name = "snmp_trap_ip2")
	private String snmpTrapIp2;

	@Column(name = "snmp_trap_ip3")
	private String snmpTrapIp3;

	@Column(name = "snmp_trap_ip4")
	private String snmpTrapIp4;

	@Column(name = "snmp_trap_ip5")
	private String snmpTrapIp5;

	@Column(name = "start_date")
	private Timestamp startDate;

	@Column(name = "su_mac_address")
	private String suMacAddress;

	@Column(name = "transmitter_output_power")
	private String transmitterOutputPower;

	@Column(name = "uplink_burst_allocation")
	private String uplinkBurstAllocation;

	@Column(name = "use_realm_status")
	private String useRealmStatus;

	@Column(name = "user_authentication_mode")
	private String userAuthenticationMode;

	@Column(name = "user_name")
	private String userName;

	@Column(name = "vlan_ageing_timeout")
	private Integer vlanAgeingTimeout;

	@Column(name = "vlan_porttype")
	private String vlanPorttype;

	@Column(name = "webpage_auto_update")
	private Integer webpageAutoUpdate;

	@Column(name = "technology")
	private String technology;

	// bi-directional many-to-one association to LmComponent
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "lm_component_lm_component_id")
	private LmComponent lmComponent;

	@Column(name = "downlink_plan")
	private String downlinkPlan;

	@Column(name = "uplink_plan")
	private String uplinkPlan;

	@Column(name = "weight")
	private String weight;

	@Column(name = "user_lock_modulation")
	private String userLockModulation;

	@Column(name = "lock_modulation")
	private String lockModulation;

	@Column(name = "thershold_modulation")
	private String thersholdModulation;

	@Column(name = "prioritization_group")
	private String prioritizationGroup;

	public CambiumLastmile() {
	}

	public Integer getCambiumLastmileId() {
		return cambiumLastmileId;
	}

	public void setCambiumLastmileId(Integer cambiumLastmileId) {
		this.cambiumLastmileId = cambiumLastmileId;
	}

	public String getAcceptQinqFrames() {
		return acceptQinqFrames;
	}

	public void setAcceptQinqFrames(String acceptQinqFrames) {
		this.acceptQinqFrames = acceptQinqFrames;
	}

	public String getAllowFrametypes() {
		return allowFrametypes;
	}

	public void setAllowFrametypes(String allowFrametypes) {
		this.allowFrametypes = allowFrametypes;
	}

	public String getAllowlocalloginAfteraaareject() {
		return allowlocalloginAfteraaareject;
	}

	public void setAllowlocalloginAfteraaareject(String allowlocalloginAfteraaareject) {
		this.allowlocalloginAfteraaareject = allowlocalloginAfteraaareject;
	}

	public String getAuthenticationKey() {
		return authenticationKey;
	}

	public void setAuthenticationKey(String authenticationKey) {
		this.authenticationKey = authenticationKey;
	}

	public String getBridgeEntryTimeout() {
		return bridgeEntryTimeout;
	}

	public void setBridgeEntryTimeout(String bridgeEntryTimeout) {
		this.bridgeEntryTimeout = bridgeEntryTimeout;
	}

	public String getBsIp() {
		return bsIp;
	}

	public void setBsIp(String bsIp) {
		this.bsIp = bsIp;
	}

	public String getBsName() {
		return bsName;
	}

	public void setBsName(String bsName) {
		this.bsName = bsName;
	}

	public String getBwDownlinkSustainedRate() {
		return bwDownlinkSustainedRate;
	}

	public void setBwDownlinkSustainedRate(String bwDownlinkSustainedRate) {
		this.bwDownlinkSustainedRate = bwDownlinkSustainedRate;
	}

	public String getBwUplinkSustainedRate() {
		return bwUplinkSustainedRate;
	}

	public void setBwUplinkSustainedRate(String bwUplinkSustainedRate) {
		this.bwUplinkSustainedRate = bwUplinkSustainedRate;
	}

	public String getCambiumLastmilecol() {
		return cambiumLastmilecol;
	}

	public void setCambiumLastmilecol(String cambiumLastmilecol) {
		this.cambiumLastmilecol = cambiumLastmilecol;
	}

	public String getColorCode1() {
		return colorCode1;
	}

	public void setColorCode1(String colorCode1) {
		this.colorCode1 = colorCode1;
	}

	public String getCustomRadioFrequencyList() {
		return customRadioFrequencyList;
	}

	public void setCustomRadioFrequencyList(String customRadioFrequencyList) {
		this.customRadioFrequencyList = customRadioFrequencyList;
	}

	public String getDefaultPortVid() {
		return defaultPortVid;
	}

	public void setDefaultPortVid(String defaultPortVid) {
		this.defaultPortVid = defaultPortVid;
	}

	public String getDeviceDefaultReset() {
		return deviceDefaultReset;
	}

	public void setDeviceDefaultReset(String deviceDefaultReset) {
		this.deviceDefaultReset = deviceDefaultReset;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public String getDeviceaccessTracking() {
		return deviceaccessTracking;
	}

	public void setDeviceaccessTracking(String deviceaccessTracking) {
		this.deviceaccessTracking = deviceaccessTracking;
	}

	public String getDhcpState() {
		return dhcpState;
	}

	public void setDhcpState(String dhcpState) {
		this.dhcpState = dhcpState;
	}

	public String getDownlinkBurstAllocation() {
		return downlinkBurstAllocation;
	}

	public void setDownlinkBurstAllocation(String downlinkBurstAllocation) {
		this.downlinkBurstAllocation = downlinkBurstAllocation;
	}

	public String getDynamicLearning() {
		return dynamicLearning;
	}

	public void setDynamicLearning(String dynamicLearning) {
		this.dynamicLearning = dynamicLearning;
	}

	public String getDynamicRateAdapt() {
		return dynamicRateAdapt;
	}

	public void setDynamicRateAdapt(String dynamicRateAdapt) {
		this.dynamicRateAdapt = dynamicRateAdapt;
	}

	public String getEnableBroadcastMulticastDatarate() {
		return enableBroadcastMulticastDatarate;
	}

	public void setEnableBroadcastMulticastDatarate(String enableBroadcastMulticastDatarate) {
		this.enableBroadcastMulticastDatarate = enableBroadcastMulticastDatarate;
	}

	public Timestamp getEndDate() {
		return endDate;
	}

	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
	}

	public String getEnforceAuthentication() {
		return enforceAuthentication;
	}

	public void setEnforceAuthentication(String enforceAuthentication) {
		this.enforceAuthentication = enforceAuthentication;
	}

	public String getEthernetAccess() {
		return ethernetAccess;
	}

	public void setEthernetAccess(String ethernetAccess) {
		this.ethernetAccess = ethernetAccess;
	}

	public String getFrameTimingPulseGated() {
		return frameTimingPulseGated;
	}

	public void setFrameTimingPulseGated(String frameTimingPulseGated) {
		this.frameTimingPulseGated = frameTimingPulseGated;
	}

	public String getHipriorityChannel() {
		return hipriorityChannel;
	}

	public void setHipriorityChannel(String hipriorityChannel) {
		this.hipriorityChannel = hipriorityChannel;
	}

	public String getHipriorityDownlinkCir() {
		return hipriorityDownlinkCir;
	}

	public void setHipriorityDownlinkCir(String hipriorityDownlinkCir) {
		this.hipriorityDownlinkCir = hipriorityDownlinkCir;
	}

	public String getHipriorityUplinkCir() {
		return hipriorityUplinkCir;
	}

	public void setHipriorityUplinkCir(String hipriorityUplinkCir) {
		this.hipriorityUplinkCir = hipriorityUplinkCir;
	}

	public String getHomeRegion() {
		return homeRegion;
	}

	public void setHomeRegion(String homeRegion) {
		this.homeRegion = homeRegion;
	}

	public String getIdentity() {
		return identity;
	}

	public void setIdentity(String identity) {
		this.identity = identity;
	}

	public String getInstallationColorCode() {
		String enable = "ENABLE";
		return enable;
	}

	public void setInstallationColorCode(String installationColorCode) {
		this.installationColorCode = installationColorCode;
	}

	public String getLargevcDataq() {
		return largevcDataq;
	}

	public void setLargevcDataq(String largevcDataq) {
		this.largevcDataq = largevcDataq;
	}

	public Timestamp getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(Timestamp lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public String getLatitudeSettings() {
		return latitudeSettings;
	}

	public void setLatitudeSettings(String latitudeSettings) {
		this.latitudeSettings = latitudeSettings;
	}

	public String getLinkSpeed() {
		return linkSpeed;
	}

	public void setLinkSpeed(String linkSpeed) {
		this.linkSpeed = linkSpeed;
	}

	public String getLongitudeSettings() {
		return longitudeSettings;
	}

	public void setLongitudeSettings(String longitudeSettings) {
		this.longitudeSettings = longitudeSettings;
	}

	public String getLowpriorityDownlinkCir() {
		return lowpriorityDownlinkCir;
	}

	public void setLowpriorityDownlinkCir(String lowpriorityDownlinkCir) {
		this.lowpriorityDownlinkCir = lowpriorityDownlinkCir;
	}

	public String getLowpriorityUplinkCir() {
		return lowpriorityUplinkCir;
	}

	public void setLowpriorityUplinkCir(String lowpriorityUplinkCir) {
		this.lowpriorityUplinkCir = lowpriorityUplinkCir;
	}

	public String getMappedMacAddress() {
		return mappedMacAddress;
	}

	public void setMappedMacAddress(String mappedMacAddress) {
		this.mappedMacAddress = mappedMacAddress;
	}

	public String getMappedVid1() {
		return mappedVid1;
	}

	public void setMappedVid1(String mappedVid1) {
		this.mappedVid1 = mappedVid1;
	}

	public Integer getMappedVid2() {
		return mappedVid2;
	}

	public void setMappedVid2(Integer mappedVid2) {
		this.mappedVid2 = mappedVid2;
	}

	public String getMgmtIpForSsSu() {
		return mgmtIpForSsSu;
	}

	public void setMgmtIpForSsSu(String mgmtIpForSsSu) {
		this.mgmtIpForSsSu = mgmtIpForSsSu;
	}

	public String getMgmtIpGateway() {
		return mgmtIpGateway;
	}

	public void setMgmtIpGateway(String mgmtIpGateway) {
		this.mgmtIpGateway = mgmtIpGateway;
	}

	public String getMgmtSubnetForSsSu() {
		return mgmtSubnetForSsSu;
	}

	public void setMgmtSubnetForSsSu(String mgmtSubnetForSsSu) {
		this.mgmtSubnetForSsSu = mgmtSubnetForSsSu;
	}

	public String getMgmtVid() {
		return mgmtVid;
	}

	public void setMgmtVid(String mgmtVid) {
		this.mgmtVid = mgmtVid;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public String getMulticastDestinationAddr() {
		return multicastDestinationAddr;
	}

	public void setMulticastDestinationAddr(String multicastDestinationAddr) {
		this.multicastDestinationAddr = multicastDestinationAddr;
	}

	public String getNetworkAccessibility() {
		return networkAccessibility;
	}

	public void setNetworkAccessibility(String networkAccessibility) {
		this.networkAccessibility = networkAccessibility;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPhase1() {
		return phase1;
	}

	public void setPhase1(String phase1) {
		this.phase1 = phase1;
	}

	public String getPhase2() {
		return phase2;
	}

	public void setPhase2(String phase2) {
		this.phase2 = phase2;
	}

	public String getPortSpeedUnit() {
		return portSpeedUnit;
	}

	public void setPortSpeedUnit(String portSpeedUnit) {
		this.portSpeedUnit = portSpeedUnit;
	}

	public String getPowerupmodeWithno802_3link() {
		return powerupmodeWithno802_3link;
	}

	public void setPowerupmodeWithno802_3link(String powerupmodeWithno802_3link) {
		this.powerupmodeWithno802_3link = powerupmodeWithno802_3link;
	}

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public Integer getProviderVid() {
		return providerVid;
	}

	public void setProviderVid(Integer providerVid) {
		this.providerVid = providerVid;
	}

	public String getRealm() {
		return realm;
	}

	public void setRealm(String realm) {
		this.realm = realm;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getRegionCode() {
		return regionCode;
	}

	public void setRegionCode(String regionCode) {
		this.regionCode = regionCode;
	}

	public String getSelectKey() {
		return selectKey;
	}

	public void setSelectKey(String selectKey) {
		this.selectKey = selectKey;
	}

	public String getServerCommonName() {
		return serverCommonName;
	}

	public void setServerCommonName(String serverCommonName) {
		this.serverCommonName = serverCommonName;
	}

	public String getSiteContact() {
		return siteContact;
	}

	public void setSiteContact(String siteContact) {
		this.siteContact = siteContact;
	}

	public String getSiteLocation() {
		return siteLocation;
	}

	public void setSiteLocation(String siteLocation) {
		this.siteLocation = siteLocation;
	}

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public String getSiteinfoViewabletoGuestusers() {
		return siteinfoViewabletoGuestusers;
	}

	public void setSiteinfoViewabletoGuestusers(String siteinfoViewabletoGuestusers) {
		this.siteinfoViewabletoGuestusers = siteinfoViewabletoGuestusers;
	}

	public String getSmHeight() {
		return smHeight;
	}

	public void setSmHeight(String smHeight) {
		this.smHeight = smHeight;
	}

	public String getSmMgmtVidPassthrough() {
		return smMgmtVidPassthrough;
	}

	public void setSmMgmtVidPassthrough(String smMgmtVidPassthrough) {
		this.smMgmtVidPassthrough = smMgmtVidPassthrough;
	}

	public String getSnmpAccessingIp1() {
		return snmpAccessingIp1;
	}

	public void setSnmpAccessingIp1(String snmpAccessingIp1) {
		this.snmpAccessingIp1 = snmpAccessingIp1;
	}

	public String getSnmpAccessingIp2() {
		return snmpAccessingIp2;
	}

	public void setSnmpAccessingIp2(String snmpAccessingIp2) {
		this.snmpAccessingIp2 = snmpAccessingIp2;
	}

	public String getSnmpAccessingIp3() {
		return snmpAccessingIp3;
	}

	public void setSnmpAccessingIp3(String snmpAccessingIp3) {
		this.snmpAccessingIp3 = snmpAccessingIp3;
	}

	public String getSnmpAccessingSubnetMask1() {
		return snmpAccessingSubnetMask1;
	}

	public void setSnmpAccessingSubnetMask1(String snmpAccessingSubnetMask1) {
		this.snmpAccessingSubnetMask1 = snmpAccessingSubnetMask1;
	}

	public String getSnmpAccessingSubnetMask2() {
		return snmpAccessingSubnetMask2;
	}

	public void setSnmpAccessingSubnetMask2(String snmpAccessingSubnetMask2) {
		this.snmpAccessingSubnetMask2 = snmpAccessingSubnetMask2;
	}

	public String getSnmpAccessingSubnetMask3() {
		return snmpAccessingSubnetMask3;
	}

	public void setSnmpAccessingSubnetMask3(String snmpAccessingSubnetMask3) {
		this.snmpAccessingSubnetMask3 = snmpAccessingSubnetMask3;
	}

	public String getSnmpTrapIp1() {
		return snmpTrapIp1;
	}

	public void setSnmpTrapIp1(String snmpTrapIp1) {
		this.snmpTrapIp1 = snmpTrapIp1;
	}

	public String getSnmpTrapIp2() {
		return snmpTrapIp2;
	}

	public void setSnmpTrapIp2(String snmpTrapIp2) {
		this.snmpTrapIp2 = snmpTrapIp2;
	}

	public String getSnmpTrapIp3() {
		return snmpTrapIp3;
	}

	public void setSnmpTrapIp3(String snmpTrapIp3) {
		this.snmpTrapIp3 = snmpTrapIp3;
	}

	public Timestamp getStartDate() {
		return startDate;
	}

	public void setStartDate(Timestamp startDate) {
		this.startDate = startDate;
	}

	public String getSuMacAddress() {
		return Objects.nonNull(suMacAddress) ? suMacAddress.toLowerCase().replace(':',' ') : suMacAddress;
	}

	public void setSuMacAddress(String suMacAddress) {
		this.suMacAddress = suMacAddress;
	}

	public String getTransmitterOutputPower() {
		return transmitterOutputPower;
	}

	public void setTransmitterOutputPower(String transmitterOutputPower) {
		this.transmitterOutputPower = transmitterOutputPower;
	}

	public String getUplinkBurstAllocation() {
		return uplinkBurstAllocation;
	}

	public void setUplinkBurstAllocation(String uplinkBurstAllocation) {
		this.uplinkBurstAllocation = uplinkBurstAllocation;
	}

	public String getUseRealmStatus() {
		return useRealmStatus;
	}

	public void setUseRealmStatus(String useRealmStatus) {
		this.useRealmStatus = useRealmStatus;
	}

	public String getUserAuthenticationMode() {
		return userAuthenticationMode;
	}

	public void setUserAuthenticationMode(String userAuthenticationMode) {
		this.userAuthenticationMode = userAuthenticationMode;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Integer getVlanAgeingTimeout() {
		return vlanAgeingTimeout;
	}

	public void setVlanAgeingTimeout(Integer vlanAgeingTimeout) {
		this.vlanAgeingTimeout = vlanAgeingTimeout;
	}

	public String getVlanPorttype() {
		return vlanPorttype;
	}

	public void setVlanPorttype(String vlanPorttype) {
		this.vlanPorttype = vlanPorttype;
	}

	public Integer getWebpageAutoUpdate() {
		return webpageAutoUpdate;
	}

	public void setWebpageAutoUpdate(Integer webpageAutoUpdate) {
		this.webpageAutoUpdate = webpageAutoUpdate;
	}

	public String getTechnology() {
		return technology;
	}

	public void setTechnology(String technology) {
		this.technology = technology;
	}

	public LmComponent getLmComponent() {
		return lmComponent;
	}

	public void setLmComponent(LmComponent lmComponent) {
		this.lmComponent = lmComponent;
	}

	public String getSnmpTrapIp4() {
		return snmpTrapIp4;
	}

	public void setSnmpTrapIp4(String snmpTrapIp4) {
		this.snmpTrapIp4 = snmpTrapIp4;
	}

	public String getSnmpTrapIp5() {
		return snmpTrapIp5;
	}

	public void setSnmpTrapIp5(String snmpTrapIp5) {
		this.snmpTrapIp5 = snmpTrapIp5;
	}

	public Float getPortSpeed() {
		return portSpeed;
	}

	public void setPortSpeed(Float portSpeed) {
		this.portSpeed = portSpeed;
	}
	

	public String getDownlinkPlan() {
		return downlinkPlan;
	}

	public void setDownlinkPlan(String downlinkPlan) {
		this.downlinkPlan = downlinkPlan;
	}

	public String getUplinkPlan() {
		return uplinkPlan;
	}

	public void setUplinkPlan(String uplinkPlan) {
		this.uplinkPlan = uplinkPlan;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public String getUserLockModulation() {
		return userLockModulation;
	}

	public void setUserLockModulation(String userLockModulation) {
		this.userLockModulation = userLockModulation;
	}

	public String getLockModulation() {
		return lockModulation;
	}

	public void setLockModulation(String lockModulation) {
		this.lockModulation = lockModulation;
	}

	public String getThersholdModulation() {
		return thersholdModulation;
	}

	public void setThersholdModulation(String thersholdModulation) {
		this.thersholdModulation = thersholdModulation;
	}

	public String getPrioritizationGroup() {
		return prioritizationGroup;
	}

	public void setPrioritizationGroup(String prioritizationGroup) {
		this.prioritizationGroup = prioritizationGroup;
	}

}
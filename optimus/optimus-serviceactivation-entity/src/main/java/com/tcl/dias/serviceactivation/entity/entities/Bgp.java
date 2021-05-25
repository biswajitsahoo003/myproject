package com.tcl.dias.serviceactivation.entity.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Set;



/**
 * 
 * Bgp Entity Class
 * 
 *
 * @author Naveen
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name="bgp")
@NamedQuery(name="Bgp.findAll", query="SELECT b FROM Bgp b")
public class Bgp implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="bgp_id")
	private Integer bgpId;

	@Column(name="alu_backup_path")
	private String aluBackupPath;

	@Column(name="alu_disable_bgp_peer_grp_ext_community")
	private Byte aluDisableBgpPeerGrpExtCommunity;

	@Column(name="alu_keepalive")
	private String aluKeepalive;

	@Column(name="as_path")
	private String asPath;

	@Column(name="aso_override")
	private Byte asoOverride;
	
	@Column(name="split_horizon")
	private Byte splitHorizon;

	@Column(name="authentication_mode")
	private String authenticationMode;



	@Column(name="bgp_peer_name")
	private String bgpPeerName;

	private String bgpneighbourinboundv4routermapname;

	private String bgpneighbourinboundv6routermapname;

	@Column(name="end_date")
	private Timestamp endDate;

	@Column(name="hello_time")
	private String helloTime;

	@Column(name="hold_time")
	private String holdTime;

	@Column(name="isauthentication_required")
	private Byte isauthenticationRequired;

	@Column(name="isbgp_neighbour_inboundv4_routemap_enabled")
	private Byte isbgpNeighbourInboundv4RoutemapEnabled;

	@Column(name="isbgp_neighbour_inboundv6_routemap_enabled")
	private Byte isbgpNeighbourInboundv6RoutemapEnabled;

	@Column(name="isbgp_neighbourinboundv4_routemap_preprovisioned")
	private Byte isbgpNeighbourinboundv4RoutemapPreprovisioned;

	@Column(name="isbgp_neighbourinboundv6_routemap_preprovisioned")
	private Byte isbgpNeighbourinboundv6RoutemapPreprovisioned;

	@Column(name="isebgp_multihop_reqd")
	private Byte isebgpMultihopReqd;

	@Column(name="bgp_multihop_reqd")
	private Byte isbgpMultihopReqd;

	@Column(name="ismultihop_ttl")
	private String ismultihopTtl;

	private Byte isrtbh;

	@Column(name="last_modified_date")
	private Timestamp lastModifiedDate;

	@Column(name="local_as_number")
	private String localAsNumber;

	@Column(name="local_preference")
	private String localPreference;

	@Column(name="local_update_source_interface")
	private String localUpdateSourceInterface;

	@Column(name="local_update_source_ipv4_address")
	private String localUpdateSourceIpv4Address;

	@Column(name="local_update_source_ipv6_address")
	private String localUpdateSourceIpv6Address;

	@Column(name="max_prefix")
	private String maxPrefix;

	@Column(name="max_prefix_threshold")
	private String maxPrefixThreshold;

	@Column(name="med_value")
	private String medValue;

	@Column(name="modified_by")
	private String modifiedBy;

	@Column(name="neighbor_on")
	private String neighborOn;

	@Column(name="neighbour_community")
	private String neighbourCommunity;

	@Column(name="neighbourshutdown_required")
	private Byte neighbourshutdownRequired;

	private String password;

	@Column(name="peer_type")
	private String peerType;

	@Column(name="redistribute_connected_enabled")
	private Byte redistributeConnectedEnabled;

	@Column(name="redistribute_static_enabled")
	private Byte redistributeStaticEnabled;

	@Column(name="remote_as_number")
	private Integer remoteAsNumber;

	@Column(name="remote_ce_asnumber")
	private String remoteCeAsnumber;

	@Column(name="remote_ipv4_address")
	private String remoteIpv4Address;

	@Column(name="remote_ipv6_address")
	private String remoteIpv6Address;
	

	@Column(name="remote_update_source_interface")
	private String remoteUpdateSourceInterface;

	@Column(name="remote_update_source_ipv4_address")
	private String remoteUpdateSourceIpv4Address;

	@Column(name="remote_update_source_ipv6_address")
	private String remoteUpdateSourceIpv6Address;

	@Column(name="routes_exchanged")
	private String routesExchanged;

	@Column(name="rtbh_options")
	private String rtbhOptions;

	@Column(name="soo_required")
	private Byte sooRequired;

	@Column(name="start_date")
	private Timestamp startDate;

	@Column(name="v6_local_preference")
	private String v6LocalPreference;

	//bi-directional many-to-one association to InterfaceProtocolMapping
	@OneToMany(mappedBy="bgp")
	private Set<InterfaceProtocolMapping> interfaceProtocolMappings;

	//bi-directional many-to-one association to PolicyType
	@OneToMany(mappedBy="bgp")
	@OrderBy("policyId ASC")
	private Set<PolicyType> policyTypes;

	//bi-directional many-to-one association to WanStaticRoute
	@OneToMany(mappedBy="bgp")
	private Set<WanStaticRoute> wanStaticRoutes;

	public Bgp() {
	}

	public Integer getBgpId() {
		return this.bgpId;
	}

	public void setBgpId(Integer bgpId) {
		this.bgpId = bgpId;
	}

	public String getAluBackupPath() {
		return this.aluBackupPath;
	}

	public void setAluBackupPath(String aluBackupPath) {
		this.aluBackupPath = aluBackupPath;
	}

	public Byte getAluDisableBgpPeerGrpExtCommunity() {
		return this.aluDisableBgpPeerGrpExtCommunity;
	}

	public void setAluDisableBgpPeerGrpExtCommunity(Byte aluDisableBgpPeerGrpExtCommunity) {
		this.aluDisableBgpPeerGrpExtCommunity = aluDisableBgpPeerGrpExtCommunity;
	}

	public String getAluKeepalive() {
		return this.aluKeepalive;
	}

	public void setAluKeepalive(String aluKeepalive) {
		this.aluKeepalive = aluKeepalive;
	}

	public String getAsPath() {
		return this.asPath;
	}

	public void setAsPath(String asPath) {
		this.asPath = asPath;
	}

	public Byte getAsoOverride() {
		return this.asoOverride;
	}

	public void setAsoOverride(Byte asoOverride) {
		this.asoOverride = asoOverride;
	}

	public String getAuthenticationMode() {
		return this.authenticationMode;
	}

	public void setAuthenticationMode(String authenticationMode) {
		this.authenticationMode = authenticationMode;
	}



	public String getBgpPeerName() {
		return this.bgpPeerName;
	}

	public void setBgpPeerName(String bgpPeerName) {
		this.bgpPeerName = bgpPeerName;
	}

	public String getBgpneighbourinboundv4routermapname() {
		return this.bgpneighbourinboundv4routermapname;
	}

	public void setBgpneighbourinboundv4routermapname(String bgpneighbourinboundv4routermapname) {
		this.bgpneighbourinboundv4routermapname = bgpneighbourinboundv4routermapname;
	}

	public String getBgpneighbourinboundv6routermapname() {
		return this.bgpneighbourinboundv6routermapname;
	}

	public void setBgpneighbourinboundv6routermapname(String bgpneighbourinboundv6routermapname) {
		this.bgpneighbourinboundv6routermapname = bgpneighbourinboundv6routermapname;
	}

	public Timestamp getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
	}

	public String getHelloTime() {
		return this.helloTime;
	}

	public void setHelloTime(String helloTime) {
		this.helloTime = helloTime;
	}

	public String getHoldTime() {
		return this.holdTime;
	}

	public void setHoldTime(String holdTime) {
		this.holdTime = holdTime;
	}

	public Byte getIsauthenticationRequired() {
		return this.isauthenticationRequired;
	}

	public void setIsauthenticationRequired(Byte isauthenticationRequired) {
		this.isauthenticationRequired = isauthenticationRequired;
	}

	public Byte getIsbgpNeighbourInboundv4RoutemapEnabled() {
		return this.isbgpNeighbourInboundv4RoutemapEnabled;
	}

	public void setIsbgpNeighbourInboundv4RoutemapEnabled(Byte isbgpNeighbourInboundv4RoutemapEnabled) {
		this.isbgpNeighbourInboundv4RoutemapEnabled = isbgpNeighbourInboundv4RoutemapEnabled;
	}

	public Byte getIsbgpNeighbourInboundv6RoutemapEnabled() {
		return this.isbgpNeighbourInboundv6RoutemapEnabled;
	}

	public void setIsbgpNeighbourInboundv6RoutemapEnabled(Byte isbgpNeighbourInboundv6RoutemapEnabled) {
		this.isbgpNeighbourInboundv6RoutemapEnabled = isbgpNeighbourInboundv6RoutemapEnabled;
	}

	public Byte getIsbgpNeighbourinboundv4RoutemapPreprovisioned() {
		return this.isbgpNeighbourinboundv4RoutemapPreprovisioned;
	}

	public void setIsbgpNeighbourinboundv4RoutemapPreprovisioned(Byte isbgpNeighbourinboundv4RoutemapPreprovisioned) {
		this.isbgpNeighbourinboundv4RoutemapPreprovisioned = isbgpNeighbourinboundv4RoutemapPreprovisioned;
	}

	public Byte getIsbgpNeighbourinboundv6RoutemapPreprovisioned() {
		return this.isbgpNeighbourinboundv6RoutemapPreprovisioned;
	}

	public void setIsbgpNeighbourinboundv6RoutemapPreprovisioned(Byte isbgpNeighbourinboundv6RoutemapPreprovisioned) {
		this.isbgpNeighbourinboundv6RoutemapPreprovisioned = isbgpNeighbourinboundv6RoutemapPreprovisioned;
	}

	public Byte getIsebgpMultihopReqd() {
		return this.isebgpMultihopReqd;
	}

	public void setIsebgpMultihopReqd(Byte isebgpMultihopReqd) {
		this.isebgpMultihopReqd = isebgpMultihopReqd;
	}

	public String getIsmultihopTtl() {
		return this.ismultihopTtl;
	}

	public void setIsmultihopTtl(String ismultihopTtl) {
		this.ismultihopTtl = ismultihopTtl;
	}

	public Byte getIsrtbh() {
		return this.isrtbh;
	}

	public void setIsrtbh(Byte isrtbh) {
		this.isrtbh = isrtbh;
	}

	public Timestamp getLastModifiedDate() {
		return this.lastModifiedDate;
	}

	public void setLastModifiedDate(Timestamp lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public String getLocalAsNumber() {
		return this.localAsNumber;
	}

	public void setLocalAsNumber(String localAsNumber) {
		this.localAsNumber = localAsNumber;
	}

	public String getLocalPreference() {
		return this.localPreference;
	}

	public void setLocalPreference(String localPreference) {
		this.localPreference = localPreference;
	}

	public String getLocalUpdateSourceInterface() {
		return this.localUpdateSourceInterface;
	}

	public void setLocalUpdateSourceInterface(String localUpdateSourceInterface) {
		this.localUpdateSourceInterface = localUpdateSourceInterface;
	}

	public String getLocalUpdateSourceIpv4Address() {
		return this.localUpdateSourceIpv4Address;
	}

	public void setLocalUpdateSourceIpv4Address(String localUpdateSourceIpv4Address) {
		this.localUpdateSourceIpv4Address = localUpdateSourceIpv4Address;
	}

	public String getLocalUpdateSourceIpv6Address() {
		return this.localUpdateSourceIpv6Address;
	}

	public void setLocalUpdateSourceIpv6Address(String localUpdateSourceIpv6Address) {
		this.localUpdateSourceIpv6Address = localUpdateSourceIpv6Address;
	}

	public String getMaxPrefix() {
		return this.maxPrefix;
	}

	public void setMaxPrefix(String maxPrefix) {
		this.maxPrefix = maxPrefix;
	}

	public String getMaxPrefixThreshold() {
		return this.maxPrefixThreshold;
	}

	public void setMaxPrefixThreshold(String maxPrefixThreshold) {
		this.maxPrefixThreshold = maxPrefixThreshold;
	}

	public String getMedValue() {
		return this.medValue;
	}

	public void setMedValue(String medValue) {
		this.medValue = medValue;
	}

	public String getModifiedBy() {
		return this.modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public String getNeighborOn() {
		return this.neighborOn;
	}

	public void setNeighborOn(String neighborOn) {
		this.neighborOn = neighborOn;
	}

	public String getNeighbourCommunity() {
		return this.neighbourCommunity;
	}

	public void setNeighbourCommunity(String neighbourCommunity) {
		this.neighbourCommunity = neighbourCommunity;
	}

	public Byte getNeighbourshutdownRequired() {
		return this.neighbourshutdownRequired;
	}

	public void setNeighbourshutdownRequired(Byte neighbourshutdownRequired) {
		this.neighbourshutdownRequired = neighbourshutdownRequired;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPeerType() {
		return this.peerType;
	}

	public void setPeerType(String peerType) {
		this.peerType = peerType;
	}

	public Byte getRedistributeConnectedEnabled() {
		return this.redistributeConnectedEnabled;
	}

	public void setRedistributeConnectedEnabled(Byte redistributeConnectedEnabled) {
		this.redistributeConnectedEnabled = redistributeConnectedEnabled;
	}

	public Byte getRedistributeStaticEnabled() {
		return this.redistributeStaticEnabled;
	}

	public void setRedistributeStaticEnabled(Byte redistributeStaticEnabled) {
		this.redistributeStaticEnabled = redistributeStaticEnabled;
	}

	public Integer getRemoteAsNumber() {
		return this.remoteAsNumber;
	}

	public void setRemoteAsNumber(Integer remoteAsNumber) {
		this.remoteAsNumber = remoteAsNumber;
	}

	public String getRemoteCeAsnumber() {
		return this.remoteCeAsnumber;
	}

	public void setRemoteCeAsnumber(String remoteCeAsnumber) {
		this.remoteCeAsnumber = remoteCeAsnumber;
	}

	public String getRemoteIpv4Address() {
		return this.remoteIpv4Address;
	}

	public void setRemoteIpv4Address(String remoteIpv4Address) {
		this.remoteIpv4Address = remoteIpv4Address;
	}

	public String getRemoteIpv6Address() {
		return this.remoteIpv6Address;
	}

	public void setRemoteIpv6Address(String remoteIpv6Address) {
		this.remoteIpv6Address = remoteIpv6Address;
	}

	public String getRemoteUpdateSourceInterface() {
		return this.remoteUpdateSourceInterface;
	}

	public void setRemoteUpdateSourceInterface(String remoteUpdateSourceInterface) {
		this.remoteUpdateSourceInterface = remoteUpdateSourceInterface;
	}

	public String getRemoteUpdateSourceIpv4Address() {
		return this.remoteUpdateSourceIpv4Address;
	}

	public void setRemoteUpdateSourceIpv4Address(String remoteUpdateSourceIpv4Address) {
		this.remoteUpdateSourceIpv4Address = remoteUpdateSourceIpv4Address;
	}

	public String getRemoteUpdateSourceIpv6Address() {
		return this.remoteUpdateSourceIpv6Address;
	}

	public void setRemoteUpdateSourceIpv6Address(String remoteUpdateSourceIpv6Address) {
		this.remoteUpdateSourceIpv6Address = remoteUpdateSourceIpv6Address;
	}

	public String getRoutesExchanged() {
		return this.routesExchanged;
	}

	public void setRoutesExchanged(String routesExchanged) {
		this.routesExchanged = routesExchanged;
	}

	public String getRtbhOptions() {
		return this.rtbhOptions;
	}

	public void setRtbhOptions(String rtbhOptions) {
		this.rtbhOptions = rtbhOptions;
	}

	public Byte getSooRequired() {
		return this.sooRequired;
	}

	public void setSooRequired(Byte sooRequired) {
		this.sooRequired = sooRequired;
	}

	public Timestamp getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Timestamp startDate) {
		this.startDate = startDate;
	}

	public String getV6LocalPreference() {
		return this.v6LocalPreference;
	}

	public void setV6LocalPreference(String v6LocalPreference) {
		this.v6LocalPreference = v6LocalPreference;
	}

	public Set<InterfaceProtocolMapping> getInterfaceProtocolMappings() {
		return this.interfaceProtocolMappings;
	}

	public void setInterfaceProtocolMappings(Set<InterfaceProtocolMapping> interfaceProtocolMappings) {
		this.interfaceProtocolMappings = interfaceProtocolMappings;
	}

	public InterfaceProtocolMapping addInterfaceProtocolMapping(InterfaceProtocolMapping interfaceProtocolMapping) {
		getInterfaceProtocolMappings().add(interfaceProtocolMapping);
		interfaceProtocolMapping.setBgp(this);

		return interfaceProtocolMapping;
	}

	public InterfaceProtocolMapping removeInterfaceProtocolMapping(InterfaceProtocolMapping interfaceProtocolMapping) {
		getInterfaceProtocolMappings().remove(interfaceProtocolMapping);
		interfaceProtocolMapping.setBgp(null);

		return interfaceProtocolMapping;
	}

	public Set<PolicyType> getPolicyTypes() {
		return this.policyTypes;
	}

	public void setPolicyTypes(Set<PolicyType> policyTypes) {
		this.policyTypes = policyTypes;
	}

	public PolicyType addPolicyType(PolicyType policyType) {
		getPolicyTypes().add(policyType);
		policyType.setBgp(this);

		return policyType;
	}

	public PolicyType removePolicyType(PolicyType policyType) {
		getPolicyTypes().remove(policyType);
		policyType.setBgp(null);

		return policyType;
	}

	public Set<WanStaticRoute> getWanStaticRoutes() {
		return this.wanStaticRoutes;
	}

	public void setWanStaticRoutes(Set<WanStaticRoute> wanStaticRoutes) {
		this.wanStaticRoutes = wanStaticRoutes;
	}

	public WanStaticRoute addWanStaticRoute(WanStaticRoute wanStaticRoute) {
		getWanStaticRoutes().add(wanStaticRoute);
		wanStaticRoute.setBgp(this);

		return wanStaticRoute;
	}

	public WanStaticRoute removeWanStaticRoute(WanStaticRoute wanStaticRoute) {
		getWanStaticRoutes().remove(wanStaticRoute);
		wanStaticRoute.setBgp(null);

		return wanStaticRoute;
	}

	public Byte getSplitHorizon() {
		return splitHorizon;
	}

	public void setSplitHorizon(Byte splitHorizon) {
		this.splitHorizon = splitHorizon;
	}

	public Byte getIsbgpMultihopReqd() {
		return isbgpMultihopReqd;
	}

	public void setIsbgpMultihopReqd(Byte isbgpMultihopReqd) {
		this.isbgpMultihopReqd = isbgpMultihopReqd;
	}
}
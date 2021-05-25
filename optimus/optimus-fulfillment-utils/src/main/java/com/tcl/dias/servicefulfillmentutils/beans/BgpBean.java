package com.tcl.dias.servicefulfillmentutils.beans;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


/**
 * 
 * Bgp Bean Class
 * 
 *
 * @author diksha garg
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */

public class BgpBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer bgpId;
	private String aluBackupPath;
	private String aluBgpPeerName;
	private Boolean aluDisableBgpPeerGrpExtCommunity;
	private String aluKeepalive;
	private String asPath;
	private Boolean asoOverride;
	private String authenticationMode;
	private Boolean bgpMultihopReqd;
	private String bgpneighbourinboundv4routermapname;
	private String bgpneighbourinboundv6routermapname;
	private boolean isEdited;
	private Timestamp endDate;
	private String helloTime;
	private String holdTime;
	private Boolean isauthenticationRequired;
	private Boolean isbgpNeighbourInboundv4RoutemapEnabled;
	private Boolean isbgpNeighbourInboundv6RoutemapEnabled;
	private Boolean isbgpNeighbourinboundv4RoutemapPreprovisioned;
	private Boolean isbgpNeighbourinboundv6RoutemapPreprovisioned;
	private Boolean isebgpMultihopReqd;
	private String ismultihopTtl;
	private Boolean isrtbh;
	
	private Boolean splitHorizon;
	private Timestamp lastModifiedDate;
	private String localAsNumber;
	private String localPreference;
	private String localUpdateSourceInterface;
	private String localUpdateSourceIpv4Address;
	private String localUpdateSourceIpv6Address;
	private String maxPrefix;
	private String maxPrefixThreshold;
	private String medValue;
	private String modifiedBy;
	private String neighborOn;
	private String neighbourCommunity;
	private Boolean neighbourshutdownRequired;
	private String password;
	private String peerType;
	private Boolean redistributeConnectedEnabled;
	private Boolean redistributeStaticEnabled;
	private String remoteAsNumber;
	private String remoteCeAsnumber;
	private String remoteIpv4Address;
	private String remoteIpv6Address;
	private String remoteUpdateSourceInterface;
	private String remoteUpdateSourceIpv4Address;
	private String remoteUpdateSourceIpv6Address;
	private String routesExchanged;
	private String rtbhOptions;
	private Boolean ssoRequired;
	private Timestamp startDate;
	private String v6LocalPreference;

	public Boolean getSplitHorizon() {
		return splitHorizon;
	}

	public void setSplitHorizon(Boolean splitHorizon) {
		this.splitHorizon = splitHorizon;
	}

	private List<PolicyTypeBean> policyTypes;

	private List<WanStaticRouteBean> wanStaticRoutes;

	private InterfaceDetailBean interfaceDetailBean;

	public InterfaceDetailBean getInterfaceDetailBean() {
		return interfaceDetailBean;
	}

	public void setInterfaceDetailBean(InterfaceDetailBean interfaceDetailBean) {
		this.interfaceDetailBean = interfaceDetailBean;
	}

	public Integer getBgpId() {
		return bgpId;
	}

	public void setBgpId(Integer bgpId) {
		this.bgpId = bgpId;
	}

	public String getAluBackupPath() {
		return aluBackupPath;
	}

	public void setAluBackupPath(String aluBackupPath) {
		this.aluBackupPath = aluBackupPath;
	}

	public String getAluBgpPeerName() {
		return aluBgpPeerName;
	}

	public void setAluBgpPeerName(String aluBgpPeerName) {
		this.aluBgpPeerName = aluBgpPeerName;
	}

	public Boolean getAluDisableBgpPeerGrpExtCommunity() {
		return aluDisableBgpPeerGrpExtCommunity;
	}

	public void setAluDisableBgpPeerGrpExtCommunity(Boolean aluDisableBgpPeerGrpExtCommunity) {
		this.aluDisableBgpPeerGrpExtCommunity = aluDisableBgpPeerGrpExtCommunity;
	}

	public String getAluKeepalive() {
		return aluKeepalive;
	}

	public void setAluKeepalive(String aluKeepalive) {
		this.aluKeepalive = aluKeepalive;
	}

	public String getAsPath() {
		return asPath;
	}

	public void setAsPath(String asPath) {
		this.asPath = asPath;
	}

	public Boolean getAsoOverride() {
		return asoOverride;
	}

	public void setAsoOverride(Boolean asoOverride) {
		this.asoOverride = asoOverride;
	}

	public String getAuthenticationMode() {
		return authenticationMode;
	}

	public void setAuthenticationMode(String authenticationMode) {
		this.authenticationMode = authenticationMode;
	}

	public Boolean getBgpMultihopReqd() {
		return bgpMultihopReqd;
	}

	public void setBgpMultihopReqd(Boolean bgpMultihopReqd) {
		this.bgpMultihopReqd = bgpMultihopReqd;
	}

	public String getBgpneighbourinboundv4routermapname() {
		return bgpneighbourinboundv4routermapname;
	}

	public void setBgpneighbourinboundv4routermapname(String bgpneighbourinboundv4routermapname) {
		this.bgpneighbourinboundv4routermapname = bgpneighbourinboundv4routermapname;
	}

	public String getBgpneighbourinboundv6routermapname() {
		return bgpneighbourinboundv6routermapname;
	}

	public void setBgpneighbourinboundv6routermapname(String bgpneighbourinboundv6routermapname) {
		this.bgpneighbourinboundv6routermapname = bgpneighbourinboundv6routermapname;
	}

	public Timestamp getEndDate() {
		return endDate;
	}

	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
	}

	public String getHelloTime() {
		return helloTime;
	}

	public void setHelloTime(String helloTime) {
		this.helloTime = helloTime;
	}

	public String getHoldTime() {
		return holdTime;
	}

	public void setHoldTime(String holdTime) {
		this.holdTime = holdTime;
	}

	public Boolean getIsauthenticationRequired() {
		return isauthenticationRequired;
	}

	public void setIsauthenticationRequired(Boolean isauthenticationRequired) {
		this.isauthenticationRequired = isauthenticationRequired;
	}

	public Boolean getIsbgpNeighbourInboundv4RoutemapEnabled() {
		return isbgpNeighbourInboundv4RoutemapEnabled;
	}

	public void setIsbgpNeighbourInboundv4RoutemapEnabled(Boolean isbgpNeighbourInboundv4RoutemapEnabled) {
		this.isbgpNeighbourInboundv4RoutemapEnabled = isbgpNeighbourInboundv4RoutemapEnabled;
	}

	public Boolean getIsbgpNeighbourInboundv6RoutemapEnabled() {
		return isbgpNeighbourInboundv6RoutemapEnabled;
	}

	public void setIsbgpNeighbourInboundv6RoutemapEnabled(Boolean isbgpNeighbourInboundv6RoutemapEnabled) {
		this.isbgpNeighbourInboundv6RoutemapEnabled = isbgpNeighbourInboundv6RoutemapEnabled;
	}

	public Boolean getIsbgpNeighbourinboundv4RoutemapPreprovisioned() {
		return isbgpNeighbourinboundv4RoutemapPreprovisioned;
	}

	public void setIsbgpNeighbourinboundv4RoutemapPreprovisioned(Boolean isbgpNeighbourinboundv4RoutemapPreprovisioned) {
		this.isbgpNeighbourinboundv4RoutemapPreprovisioned = isbgpNeighbourinboundv4RoutemapPreprovisioned;
	}

	public Boolean getIsbgpNeighbourinboundv6RoutemapPreprovisioned() {
		return isbgpNeighbourinboundv6RoutemapPreprovisioned;
	}

	public void setIsbgpNeighbourinboundv6RoutemapPreprovisioned(Boolean isbgpNeighbourinboundv6RoutemapPreprovisioned) {
		this.isbgpNeighbourinboundv6RoutemapPreprovisioned = isbgpNeighbourinboundv6RoutemapPreprovisioned;
	}

	public Boolean getIsebgpMultihopReqd() {
		return isebgpMultihopReqd;
	}

	public void setIsebgpMultihopReqd(Boolean isebgpMultihopReqd) {
		this.isebgpMultihopReqd = isebgpMultihopReqd;
	}

	public String getIsmultihopTtl() {
		return ismultihopTtl;
	}

	public void setIsmultihopTtl(String ismultihopTtl) {
		this.ismultihopTtl = ismultihopTtl;
	}

	public Boolean getIsrtbh() {
		return isrtbh;
	}

	public void setIsrtbh(Boolean isrtbh) {
		this.isrtbh = isrtbh;
	}

	public Timestamp getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(Timestamp lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public String getLocalAsNumber() {
		return localAsNumber;
	}

	public void setLocalAsNumber(String localAsNumber) {
		this.localAsNumber = localAsNumber;
	}

	public String getLocalPreference() {
		return localPreference;
	}

	public void setLocalPreference(String localPreference) {
		this.localPreference = localPreference;
	}

	public String getLocalUpdateSourceInterface() {
		return localUpdateSourceInterface;
	}

	public void setLocalUpdateSourceInterface(String localUpdateSourceInterface) {
		this.localUpdateSourceInterface = localUpdateSourceInterface;
	}

	public String getLocalUpdateSourceIpv4Address() {
		return localUpdateSourceIpv4Address;
	}

	public void setLocalUpdateSourceIpv4Address(String localUpdateSourceIpv4Address) {
		this.localUpdateSourceIpv4Address = localUpdateSourceIpv4Address;
	}

	public String getLocalUpdateSourceIpv6Address() {
		return localUpdateSourceIpv6Address;
	}

	public void setLocalUpdateSourceIpv6Address(String localUpdateSourceIpv6Address) {
		this.localUpdateSourceIpv6Address = localUpdateSourceIpv6Address;
	}

	public String getMaxPrefix() {
		return maxPrefix;
	}

	public void setMaxPrefix(String maxPrefix) {
		this.maxPrefix = maxPrefix;
	}

	public String getMaxPrefixThreshold() {
		return maxPrefixThreshold;
	}

	public void setMaxPrefixThreshold(String maxPrefixThreshold) {
		this.maxPrefixThreshold = maxPrefixThreshold;
	}

	public String getMedValue() {
		return medValue;
	}

	public void setMedValue(String medValue) {
		this.medValue = medValue;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public String getNeighborOn() {
		return neighborOn;
	}

	public void setNeighborOn(String neighborOn) {
		this.neighborOn = neighborOn;
	}

	public String getNeighbourCommunity() {
		return neighbourCommunity;
	}

	public void setNeighbourCommunity(String neighbourCommunity) {
		this.neighbourCommunity = neighbourCommunity;
	}

	public Boolean getNeighbourshutdownRequired() {
		return neighbourshutdownRequired;
	}

	public void setNeighbourshutdownRequired(Boolean neighbourshutdownRequired) {
		this.neighbourshutdownRequired = neighbourshutdownRequired;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPeerType() {
		return peerType;
	}

	public void setPeerType(String peerType) {
		this.peerType = peerType;
	}

	public Boolean getRedistributeConnectedEnabled() {
		return redistributeConnectedEnabled;
	}

	public void setRedistributeConnectedEnabled(Boolean redistributeConnectedEnabled) {
		this.redistributeConnectedEnabled = redistributeConnectedEnabled;
	}

	public Boolean getRedistributeStaticEnabled() {
		return redistributeStaticEnabled;
	}

	public void setRedistributeStaticEnabled(Boolean redistributeStaticEnabled) {
		this.redistributeStaticEnabled = redistributeStaticEnabled;
	}

	public String getRemoteAsNumber() {
		return remoteAsNumber;
	}

	public void setRemoteAsNumber(String remoteAsNumber) {
		this.remoteAsNumber = remoteAsNumber;
	}

	public String getRemoteCeAsnumber() {
		return remoteCeAsnumber;
	}

	public void setRemoteCeAsnumber(String remoteCeAsnumber) {
		this.remoteCeAsnumber = remoteCeAsnumber;
	}

	public String getRemoteIpv4Address() {
		return remoteIpv4Address;
	}

	public void setRemoteIpv4Address(String remoteIpv4Address) {
		this.remoteIpv4Address = remoteIpv4Address;
	}

	public String getRemoteIpv6Address() {
		return remoteIpv6Address;
	}

	public void setRemoteIpv6Address(String remoteIpv6Address) {
		this.remoteIpv6Address = remoteIpv6Address;
	}

	public String getRemoteUpdateSourceInterface() {
		return remoteUpdateSourceInterface;
	}

	public void setRemoteUpdateSourceInterface(String remoteUpdateSourceInterface) {
		this.remoteUpdateSourceInterface = remoteUpdateSourceInterface;
	}

	public String getRemoteUpdateSourceIpv4Address() {
		return remoteUpdateSourceIpv4Address;
	}

	public void setRemoteUpdateSourceIpv4Address(String remoteUpdateSourceIpv4Address) {
		this.remoteUpdateSourceIpv4Address = remoteUpdateSourceIpv4Address;
	}

	public String getRemoteUpdateSourceIpv6Address() {
		return remoteUpdateSourceIpv6Address;
	}

	public void setRemoteUpdateSourceIpv6Address(String remoteUpdateSourceIpv6Address) {
		this.remoteUpdateSourceIpv6Address = remoteUpdateSourceIpv6Address;
	}

	public String getRoutesExchanged() {
		return routesExchanged;
	}

	public void setRoutesExchanged(String routesExchanged) {
		this.routesExchanged = routesExchanged;
	}

	public String getRtbhOptions() {
		return rtbhOptions;
	}

	public void setRtbhOptions(String rtbhOptions) {
		this.rtbhOptions = rtbhOptions;
	}

	public Boolean getSsoRequired() {
		return ssoRequired;
	}

	public void setSsoRequired(Boolean ssoRequired) {
		this.ssoRequired = ssoRequired;
	}

	public Timestamp getStartDate() {
		return startDate;
	}

	public void setStartDate(Timestamp startDate) {
		this.startDate = startDate;
	}

	public String getV6LocalPreference() {
		return v6LocalPreference;
	}

	public void setV6LocalPreference(String v6LocalPreference) {
		this.v6LocalPreference = v6LocalPreference;
	}

	public List<PolicyTypeBean> getPolicyTypes() {
		if(policyTypes==null){
			policyTypes = new ArrayList<>();
		}
		return policyTypes;
	}

	public void setPolicyTypes(List<PolicyTypeBean> policyTypes) {
		this.policyTypes = policyTypes;
	}

	public List<WanStaticRouteBean> getWanStaticRoutes() {
		if(wanStaticRoutes==null){
			wanStaticRoutes = new ArrayList<>();
		}
		return wanStaticRoutes;
	}

	public void setWanStaticRoutes(List<WanStaticRouteBean> wanStaticRoutes) {
		this.wanStaticRoutes = wanStaticRoutes;
	}


	public boolean isEdited() {
		return isEdited;
	}

	public void setEdited(boolean edited) {
		isEdited = edited;
	}
	
}
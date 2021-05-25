package com.tcl.dias.servicefulfillmentutils.beans;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;


/**
 * 
 * Ospf Bean Class
 * 
 *
 * @author diksha garg
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */

public class OspfBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer ospfId;
	private String areaId;
	private String authenticationMode;
	private String cost;
	private String domainId;
	private Timestamp endDate;
	private Boolean isauthenticationRequired;
	private Boolean isenableShamlink;
	private boolean isEdited;
	private Boolean isignoreipOspfMtu;
	private Boolean isnetworkP2p;
	private Boolean isredistributeConnectedEnabled;
	private Boolean isredistributeStaticEnabled;
	private Boolean isroutemapEnabled;
	private String isroutemapPreprovisioned;
	private Timestamp lastModifiedDate;
	private String localPreference;
	private String localPreferenceV6;
	private String modifiedBy;
	private String ospfNetworkType;
	private String password;
	private String processId;
	private String redistributeRoutermapname;
	private String shamlinkInterfaceName;
	private String shamlinkLocalAddress;
	private String shamlinkRemoteAddress;
	private Timestamp startDate;
	private String version;

	private InterfaceDetailBean interfaceDetailBean;

	public InterfaceDetailBean getInterfaceDetailBean() {
		return interfaceDetailBean;
	}

	public void setInterfaceDetailBean(InterfaceDetailBean interfaceDetailBean) {
		this.interfaceDetailBean = interfaceDetailBean;
	}

	private Set<PolicyTypeBean> policyTypes;

	public Integer getOspfId() {
		return ospfId;
	}

	public void setOspfId(Integer ospfId) {
		this.ospfId = ospfId;
	}

	public String getAreaId() {
		return areaId;
	}

	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}

	public String getAuthenticationMode() {
		return authenticationMode;
	}

	public void setAuthenticationMode(String authenticationMode) {
		this.authenticationMode = authenticationMode;
	}

	public String getCost() {
		return cost;
	}

	public void setCost(String cost) {
		this.cost = cost;
	}

	public String getDomainId() {
		return domainId;
	}

	public void setDomainId(String domainId) {
		this.domainId = domainId;
	}

	public Timestamp getEndDate() {
		return endDate;
	}

	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
	}

	public Boolean getIsauthenticationRequired() {
		return isauthenticationRequired;
	}

	public void setIsauthenticationRequired(Boolean isauthenticationRequired) {
		this.isauthenticationRequired = isauthenticationRequired;
	}

	public Boolean getIsenableShamlink() {
		return isenableShamlink;
	}

	public void setIsenableShamlink(Boolean isenableShamlink) {
		this.isenableShamlink = isenableShamlink;
	}

	public Boolean getIsignoreipOspfMtu() {
		return isignoreipOspfMtu;
	}

	public void setIsignoreipOspfMtu(Boolean isignoreipOspfMtu) {
		this.isignoreipOspfMtu = isignoreipOspfMtu;
	}

	public Boolean getIsnetworkP2p() {
		return isnetworkP2p;
	}

	public void setIsnetworkP2p(Boolean isnetworkP2p) {
		this.isnetworkP2p = isnetworkP2p;
	}

	public Boolean getIsredistributeConnectedEnabled() {
		return isredistributeConnectedEnabled;
	}

	public void setIsredistributeConnectedEnabled(Boolean isredistributeConnectedEnabled) {
		this.isredistributeConnectedEnabled = isredistributeConnectedEnabled;
	}

	public Boolean getIsredistributeStaticEnabled() {
		return isredistributeStaticEnabled;
	}

	public void setIsredistributeStaticEnabled(Boolean isredistributeStaticEnabled) {
		this.isredistributeStaticEnabled = isredistributeStaticEnabled;
	}

	public Boolean getIsroutemapEnabled() {
		return isroutemapEnabled;
	}

	public void setIsroutemapEnabled(Boolean isroutemapEnabled) {
		this.isroutemapEnabled = isroutemapEnabled;
	}

	public String getIsroutemapPreprovisioned() {
		return isroutemapPreprovisioned;
	}

	public void setIsroutemapPreprovisioned(String isroutemapPreprovisioned) {
		this.isroutemapPreprovisioned = isroutemapPreprovisioned;
	}

	public Timestamp getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(Timestamp lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public String getLocalPreference() {
		return localPreference;
	}

	public void setLocalPreference(String localPreference) {
		this.localPreference = localPreference;
	}

	public String getLocalPreferenceV6() {
		return localPreferenceV6;
	}

	public void setLocalPreferenceV6(String localPreferenceV6) {
		this.localPreferenceV6 = localPreferenceV6;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public String getOspfNetworkType() {
		return ospfNetworkType;
	}

	public void setOspfNetworkType(String ospfNetworkType) {
		this.ospfNetworkType = ospfNetworkType;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}

	public String getRedistributeRoutermapname() {
		return redistributeRoutermapname;
	}

	public void setRedistributeRoutermapname(String redistributeRoutermapname) {
		this.redistributeRoutermapname = redistributeRoutermapname;
	}

	public String getShamlinkInterfaceName() {
		return shamlinkInterfaceName;
	}

	public void setShamlinkInterfaceName(String shamlinkInterfaceName) {
		this.shamlinkInterfaceName = shamlinkInterfaceName;
	}

	public String getShamlinkLocalAddress() {
		return shamlinkLocalAddress;
	}

	public void setShamlinkLocalAddress(String shamlinkLocalAddress) {
		this.shamlinkLocalAddress = shamlinkLocalAddress;
	}

	public String getShamlinkRemoteAddress() {
		return shamlinkRemoteAddress;
	}

	public void setShamlinkRemoteAddress(String shamlinkRemoteAddress) {
		this.shamlinkRemoteAddress = shamlinkRemoteAddress;
	}

	public Timestamp getStartDate() {
		return startDate;
	}

	public void setStartDate(Timestamp startDate) {
		this.startDate = startDate;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}



	public Set<PolicyTypeBean> getPolicyTypes() {
		
		if(policyTypes==null) {
			policyTypes=new HashSet<>();
		}
		return policyTypes;
	}

	public void setPolicyTypes(Set<PolicyTypeBean> policyTypes) {
		this.policyTypes = policyTypes;
	}

	public boolean isEdited() {
		return isEdited;
	}

	public void setEdited(boolean edited) {
		isEdited = edited;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
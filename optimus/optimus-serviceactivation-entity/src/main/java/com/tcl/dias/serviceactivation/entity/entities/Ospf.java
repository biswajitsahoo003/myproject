package com.tcl.dias.serviceactivation.entity.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Set;


/**
 * 
 * Ospf Entity Class
 * 
 *
 * @author Naveen
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name = "ospf")
@NamedQuery(name="Ospf.findAll", query="SELECT o FROM Ospf o")
public class Ospf implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="ospf_id")
	private Integer ospfId;

	@Column(name="area_id")
	private String areaId;

	@Column(name="authentication_mode")
	private String authenticationMode;

	private String cost;

	@Column(name="domain_id")
	private String domainId;

	@Column(name="end_date")
	private Timestamp endDate;

	@Column(name="isauthentication_required")
	private Byte isauthenticationRequired;

	@Column(name="isenable_shamlink")
	private Byte isenableShamlink;

	@Column(name="isignoreip_ospf_mtu")
	private Byte isignoreipOspfMtu;

	@Column(name="isnetwork_p2p")
	private Byte isnetworkP2p;

	@Column(name="isredistribute_connected_enabled")
	private Byte isredistributeConnectedEnabled;

	@Column(name="isredistribute_static_enabled")
	private Byte isredistributeStaticEnabled;

	@Column(name="isroutemap_enabled")
	private Byte isroutemapEnabled;

	@Column(name="isroutemap_preprovisioned")
	private String isroutemapPreprovisioned;

	@Column(name="last_modified_date")
	private Timestamp lastModifiedDate;

	@Column(name="local_preference")
	private String localPreference;

	@Column(name="local_preference_v6")
	private String localPreferenceV6;

	@Column(name="modified_by")
	private String modifiedBy;

	@Column(name="ospf_network_type")
	private String ospfNetworkType;

	private String password;

	@Column(name="process_id")
	private String processId;

	@Column(name="redistribute_routermapname")
	private String redistributeRoutermapname;

	@Column(name="shamlink_interface_name")
	private String shamlinkInterfaceName;

	@Column(name="shamlink_local_address")
	private String shamlinkLocalAddress;

	@Column(name="shamlink_remote_address")
	private String shamlinkRemoteAddress;

	@Column(name="start_date")
	private Timestamp startDate;

	private String version;

	//bi-directional many-to-one association to InterfaceProtocolMapping
	@OneToMany(mappedBy="ospf")
	private Set<InterfaceProtocolMapping> interfaceProtocolMappings;

	//bi-directional many-to-one association to PolicyType
	@OneToMany(mappedBy="ospf")
	private Set<PolicyType> policyTypes;

	public Ospf() {
	}

	public Integer getOspfId() {
		return this.ospfId;
	}

	public void setOspfId(Integer ospfId) {
		this.ospfId = ospfId;
	}

	public String getAreaId() {
		return this.areaId;
	}

	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}

	public String getAuthenticationMode() {
		return this.authenticationMode;
	}

	public void setAuthenticationMode(String authenticationMode) {
		this.authenticationMode = authenticationMode;
	}

	public String getCost() {
		return this.cost;
	}

	public void setCost(String cost) {
		this.cost = cost;
	}

	public String getDomainId() {
		return this.domainId;
	}

	public void setDomainId(String domainId) {
		this.domainId = domainId;
	}

	public Timestamp getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
	}

	public Byte getIsauthenticationRequired() {
		return this.isauthenticationRequired;
	}

	public void setIsauthenticationRequired(Byte isauthenticationRequired) {
		this.isauthenticationRequired = isauthenticationRequired;
	}

	public Byte getIsenableShamlink() {
		return this.isenableShamlink;
	}

	public void setIsenableShamlink(Byte isenableShamlink) {
		this.isenableShamlink = isenableShamlink;
	}

	public Byte getIsignoreipOspfMtu() {
		return this.isignoreipOspfMtu;
	}

	public void setIsignoreipOspfMtu(Byte isignoreipOspfMtu) {
		this.isignoreipOspfMtu = isignoreipOspfMtu;
	}

	public Byte getIsnetworkP2p() {
		return this.isnetworkP2p;
	}

	public void setIsnetworkP2p(Byte isnetworkP2p) {
		this.isnetworkP2p = isnetworkP2p;
	}

	public Byte getIsredistributeConnectedEnabled() {
		return this.isredistributeConnectedEnabled;
	}

	public void setIsredistributeConnectedEnabled(Byte isredistributeConnectedEnabled) {
		this.isredistributeConnectedEnabled = isredistributeConnectedEnabled;
	}

	public Byte getIsredistributeStaticEnabled() {
		return this.isredistributeStaticEnabled;
	}

	public void setIsredistributeStaticEnabled(Byte isredistributeStaticEnabled) {
		this.isredistributeStaticEnabled = isredistributeStaticEnabled;
	}

	public Byte getIsroutemapEnabled() {
		return this.isroutemapEnabled;
	}

	public void setIsroutemapEnabled(Byte isroutemapEnabled) {
		this.isroutemapEnabled = isroutemapEnabled;
	}

	public String getIsroutemapPreprovisioned() {
		return this.isroutemapPreprovisioned;
	}

	public void setIsroutemapPreprovisioned(String isroutemapPreprovisioned) {
		this.isroutemapPreprovisioned = isroutemapPreprovisioned;
	}

	public Timestamp getLastModifiedDate() {
		return this.lastModifiedDate;
	}

	public void setLastModifiedDate(Timestamp lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public String getLocalPreference() {
		return this.localPreference;
	}

	public void setLocalPreference(String localPreference) {
		this.localPreference = localPreference;
	}

	public String getLocalPreferenceV6() {
		return this.localPreferenceV6;
	}

	public void setLocalPreferenceV6(String localPreferenceV6) {
		this.localPreferenceV6 = localPreferenceV6;
	}

	public String getModifiedBy() {
		return this.modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public String getOspfNetworkType() {
		return this.ospfNetworkType;
	}

	public void setOspfNetworkType(String ospfNetworkType) {
		this.ospfNetworkType = ospfNetworkType;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getProcessId() {
		return this.processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}

	public String getRedistributeRoutermapname() {
		return this.redistributeRoutermapname;
	}

	public void setRedistributeRoutermapname(String redistributeRoutermapname) {
		this.redistributeRoutermapname = redistributeRoutermapname;
	}

	public String getShamlinkInterfaceName() {
		return this.shamlinkInterfaceName;
	}

	public void setShamlinkInterfaceName(String shamlinkInterfaceName) {
		this.shamlinkInterfaceName = shamlinkInterfaceName;
	}

	public String getShamlinkLocalAddress() {
		return this.shamlinkLocalAddress;
	}

	public void setShamlinkLocalAddress(String shamlinkLocalAddress) {
		this.shamlinkLocalAddress = shamlinkLocalAddress;
	}

	public String getShamlinkRemoteAddress() {
		return this.shamlinkRemoteAddress;
	}

	public void setShamlinkRemoteAddress(String shamlinkRemoteAddress) {
		this.shamlinkRemoteAddress = shamlinkRemoteAddress;
	}

	public Timestamp getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Timestamp startDate) {
		this.startDate = startDate;
	}

	public String getVersion() {
		return this.version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public Set<InterfaceProtocolMapping> getInterfaceProtocolMappings() {
		return this.interfaceProtocolMappings;
	}

	public void setInterfaceProtocolMappings(Set<InterfaceProtocolMapping> interfaceProtocolMappings) {
		this.interfaceProtocolMappings = interfaceProtocolMappings;
	}

	public InterfaceProtocolMapping addInterfaceProtocolMapping(InterfaceProtocolMapping interfaceProtocolMapping) {
		getInterfaceProtocolMappings().add(interfaceProtocolMapping);
		interfaceProtocolMapping.setOspf(this);

		return interfaceProtocolMapping;
	}

	public InterfaceProtocolMapping removeInterfaceProtocolMapping(InterfaceProtocolMapping interfaceProtocolMapping) {
		getInterfaceProtocolMappings().remove(interfaceProtocolMapping);
		interfaceProtocolMapping.setOspf(null);

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
		policyType.setOspf(this);

		return policyType;
	}

	public PolicyType removePolicyType(PolicyType policyType) {
		getPolicyTypes().remove(policyType);
		policyType.setOspf(null);

		return policyType;
	}

}
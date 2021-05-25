package com.tcl.dias.serviceactivation.entity.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


/**
 * 
 * StaticProtocol Entity Class
 * 
 *
 * @author Naveen
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name="static")
@NamedQuery(name="StaticProtocol.findAll", query="SELECT s FROM StaticProtocol s")
public class StaticProtocol implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="staticprotocol_id")
	private Integer staticprotocolId;

	@Column(name="end_date")
	private Timestamp endDate;

	@Column(name="isroutemap_enabled")
	private Byte isroutemapEnabled;

	@Column(name="isroutemap_preprovisioned")
	private Byte isroutemapPreprovisioned;

	@Column(name="last_modified_date")
	private Timestamp lastModifiedDate;

	@Column(name="local_preference")
	private String localPreference;

	@Column(name="local_preference_v6")
	private String localPreferenceV6;

	@Column(name="modified_by")
	private String modifiedBy;

	@Column(name="redistribute_routemap_ipv4")
	private String redistributeRoutemapIpv4;

	@Column(name="start_date")
	private Timestamp startDate;

	//bi-directional many-to-one association to InterfaceProtocolMapping
	@OneToMany(mappedBy="staticProtocol")
	private Set<InterfaceProtocolMapping> interfaceProtocolMappings;

	//bi-directional many-to-one association to PolicyType
	@OneToMany(mappedBy="staticProtocol")
	private Set<PolicyType> policyTypes;

	//bi-directional many-to-one association to WanStaticRoute
	@OneToMany(mappedBy="staticProtocol")
	private Set<WanStaticRoute> wanStaticRoutes;

	public StaticProtocol() {
	}

	public Integer getStaticprotocolId() {
		return this.staticprotocolId;
	}

	public void setStaticprotocolId(Integer staticprotocolId) {
		this.staticprotocolId = staticprotocolId;
	}

	public Timestamp getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
	}

	public Byte getIsroutemapEnabled() {
		return this.isroutemapEnabled;
	}

	public void setIsroutemapEnabled(Byte isroutemapEnabled) {
		this.isroutemapEnabled = isroutemapEnabled;
	}

	public Byte getIsroutemapPreprovisioned() {
		return this.isroutemapPreprovisioned;
	}

	public void setIsroutemapPreprovisioned(Byte isroutemapPreprovisioned) {
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

	public String getRedistributeRoutemapIpv4() {
		return this.redistributeRoutemapIpv4;
	}

	public void setRedistributeRoutemapIpv4(String redistributeRoutemapIpv4) {
		this.redistributeRoutemapIpv4 = redistributeRoutemapIpv4;
	}

	public Timestamp getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Timestamp startDate) {
		this.startDate = startDate;
	}

	public Set<InterfaceProtocolMapping> getInterfaceProtocolMappings() {
		return this.interfaceProtocolMappings;
	}

	public void setInterfaceProtocolMappings(Set<InterfaceProtocolMapping> interfaceProtocolMappings) {
		this.interfaceProtocolMappings = interfaceProtocolMappings;
	}

	public InterfaceProtocolMapping addInterfaceProtocolMapping(InterfaceProtocolMapping interfaceProtocolMapping) {
		getInterfaceProtocolMappings().add(interfaceProtocolMapping);
		interfaceProtocolMapping.setStaticProtocol(this);

		return interfaceProtocolMapping;
	}

	public InterfaceProtocolMapping removeInterfaceProtocolMapping(InterfaceProtocolMapping interfaceProtocolMapping) {
		getInterfaceProtocolMappings().remove(interfaceProtocolMapping);
		interfaceProtocolMapping.setStaticProtocol(null);

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
		policyType.setStaticProtocol(this);

		return policyType;
	}

	public PolicyType removePolicyType(PolicyType policyType) {
		getPolicyTypes().remove(policyType);
		policyType.setStaticProtocol(null);

		return policyType;
	}

	public Set<WanStaticRoute> getWanStaticRoutes() {
		if(wanStaticRoutes==null){
			wanStaticRoutes=new HashSet<>();
		}
		return this.wanStaticRoutes;
	}

	public void setWanStaticRoutes(Set<WanStaticRoute> wanStaticRoutes) {
		this.wanStaticRoutes = wanStaticRoutes;
	}

	public WanStaticRoute addWanStaticRoute(WanStaticRoute wanStaticRoute) {
		getWanStaticRoutes().add(wanStaticRoute);
		wanStaticRoute.setStaticProtocol(this);

		return wanStaticRoute;
	}

	public WanStaticRoute removeWanStaticRoute(WanStaticRoute wanStaticRoute) {
		getWanStaticRoutes().remove(wanStaticRoute);
		wanStaticRoute.setStaticProtocol(null);

		return wanStaticRoute;
	}

}
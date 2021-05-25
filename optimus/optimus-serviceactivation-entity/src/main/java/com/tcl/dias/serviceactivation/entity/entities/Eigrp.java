package com.tcl.dias.serviceactivation.entity.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Set;

/**
 * 
 * Eigrp Entity Class
 * 
 *
 * @author Naveen
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name="eigrp")
@NamedQuery(name="Eigrp.findAll", query="SELECT e FROM Eigrp e")
public class Eigrp implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="eigrp_protocol_id")
	private Integer eigrpProtocolId;

	@Column(name="eigrp_bw_kbps")
	private String eigrpBwKbps;

	@Column(name="end_date")
	private Timestamp endDate;

	@Column(name="interface_delay")
	private String interfaceDelay;

	@Column(name="isredistribute_connected_enabled")
	private Byte isredistributeConnectedEnabled;

	@Column(name="isredistribute_static_enabled")
	private Byte isredistributeStaticEnabled;

	@Column(name="isroutemap_enabled")
	private Byte isroutemapEnabled;

	@Column(name="isroutemap_preprovisioned")
	private Byte isroutemapPreprovisioned;

	@Column(name="last_modified_date")
	private Timestamp lastModifiedDate;

	private String load;

	@Column(name="local_asnumber")
	private String localAsnumber;

	@Column(name="modified_by")
	private String modifiedBy;

	private String mtu;

	@Column(name="redistribute_routemap_name")
	private String redistributeRoutemapName;

	@Column(name="redistribution_delay")
	private String redistributionDelay;

	private String reliability;

	@Column(name="remote_asnumber")
	private String remoteAsnumber;

	@Column(name="remote_ce_asnumber")
	private String remoteCeAsnumber;

	@Column(name="soo_required")
	private Byte sooRequired;

	@Column(name="start_date")
	private Timestamp startDate;

	//bi-directional many-to-one association to InterfaceProtocolMapping
	@OneToMany(mappedBy="eigrp")
	private Set<InterfaceProtocolMapping> interfaceProtocolMappings;

	public Eigrp() {
	}

	public Integer getEigrpProtocolId() {
		return this.eigrpProtocolId;
	}

	public void setEigrpProtocolId(Integer eigrpProtocolId) {
		this.eigrpProtocolId = eigrpProtocolId;
	}

	public String getEigrpBwKbps() {
		return this.eigrpBwKbps;
	}

	public void setEigrpBwKbps(String eigrpBwKbps) {
		this.eigrpBwKbps = eigrpBwKbps;
	}

	public Timestamp getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
	}

	public String getInterfaceDelay() {
		return this.interfaceDelay;
	}

	public void setInterfaceDelay(String interfaceDelay) {
		this.interfaceDelay = interfaceDelay;
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

	public String getLoad() {
		return this.load;
	}

	public void setLoad(String load) {
		this.load = load;
	}

	public String getLocalAsnumber() {
		return this.localAsnumber;
	}

	public void setLocalAsnumber(String localAsnumber) {
		this.localAsnumber = localAsnumber;
	}

	public String getModifiedBy() {
		return this.modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public String getMtu() {
		return this.mtu;
	}

	public void setMtu(String mtu) {
		this.mtu = mtu;
	}

	public String getRedistributeRoutemapName() {
		return this.redistributeRoutemapName;
	}

	public void setRedistributeRoutemapName(String redistributeRoutemapName) {
		this.redistributeRoutemapName = redistributeRoutemapName;
	}

	public String getRedistributionDelay() {
		return this.redistributionDelay;
	}

	public void setRedistributionDelay(String redistributionDelay) {
		this.redistributionDelay = redistributionDelay;
	}

	public String getReliability() {
		return this.reliability;
	}

	public void setReliability(String reliability) {
		this.reliability = reliability;
	}

	public String getRemoteAsnumber() {
		return this.remoteAsnumber;
	}

	public void setRemoteAsnumber(String remoteAsnumber) {
		this.remoteAsnumber = remoteAsnumber;
	}

	public String getRemoteCeAsnumber() {
		return this.remoteCeAsnumber;
	}

	public void setRemoteCeAsnumber(String remoteCeAsnumber) {
		this.remoteCeAsnumber = remoteCeAsnumber;
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

	public Set<InterfaceProtocolMapping> getInterfaceProtocolMappings() {
		return this.interfaceProtocolMappings;
	}

	public void setInterfaceProtocolMappings(Set<InterfaceProtocolMapping> interfaceProtocolMappings) {
		this.interfaceProtocolMappings = interfaceProtocolMappings;
	}

	public InterfaceProtocolMapping addInterfaceProtocolMapping(InterfaceProtocolMapping interfaceProtocolMapping) {
		getInterfaceProtocolMappings().add(interfaceProtocolMapping);
		interfaceProtocolMapping.setEigrp(this);

		return interfaceProtocolMapping;
	}

	public InterfaceProtocolMapping removeInterfaceProtocolMapping(InterfaceProtocolMapping interfaceProtocolMapping) {
		getInterfaceProtocolMappings().remove(interfaceProtocolMapping);
		interfaceProtocolMapping.setEigrp(null);

		return interfaceProtocolMapping;
	}

}
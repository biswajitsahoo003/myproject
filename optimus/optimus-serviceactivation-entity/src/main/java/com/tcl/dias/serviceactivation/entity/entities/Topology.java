package com.tcl.dias.serviceactivation.entity.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Set;


/**
 * 
 * Topology Entity Class
 * 
 *
 * @author Naveen
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name="topology")
@NamedQuery(name="Topology.findAll", query="SELECT t FROM Topology t")
public class Topology implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="topology_id")
	private Integer topologyId;

	@Column(name="end_date")
	private Timestamp endDate;

	@Column(name="last_modified_date")
	private Timestamp lastModifiedDate;

	@Column(name="modified_by")
	private String modifiedBy;

	@Column(name="start_date")
	private Timestamp startDate;

	@Column(name="topology_name")
	private String topologyName;

	//bi-directional many-to-one association to RouterUplinkport
	@OneToMany(mappedBy="topology")
	private Set<RouterUplinkport> routerUplinkports;

	//bi-directional many-to-one association to ServiceDetail
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="service_details_id")
	private ServiceDetail serviceDetail;

	//bi-directional many-to-one association to UniswitchDetail
	@OneToMany(mappedBy="topology")
	private Set<UniswitchDetail> uniswitchDetails;

	public Topology() {
	}

	public Integer getTopologyId() {
		return this.topologyId;
	}

	public void setTopologyId(Integer topologyId) {
		this.topologyId = topologyId;
	}

	public Timestamp getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
	}

	public Timestamp getLastModifiedDate() {
		return this.lastModifiedDate;
	}

	public void setLastModifiedDate(Timestamp lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public String getModifiedBy() {
		return this.modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Timestamp getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Timestamp startDate) {
		this.startDate = startDate;
	}

	public String getTopologyName() {
		return this.topologyName;
	}

	public void setTopologyName(String topologyName) {
		this.topologyName = topologyName;
	}

	public Set<RouterUplinkport> getRouterUplinkports() {
		return this.routerUplinkports;
	}

	public void setRouterUplinkports(Set<RouterUplinkport> routerUplinkports) {
		this.routerUplinkports = routerUplinkports;
	}

	public RouterUplinkport addRouterUplinkport(RouterUplinkport routerUplinkport) {
		getRouterUplinkports().add(routerUplinkport);
		routerUplinkport.setTopology(this);

		return routerUplinkport;
	}

	public RouterUplinkport removeRouterUplinkport(RouterUplinkport routerUplinkport) {
		getRouterUplinkports().remove(routerUplinkport);
		routerUplinkport.setTopology(null);

		return routerUplinkport;
	}

	public ServiceDetail getServiceDetail() {
		return this.serviceDetail;
	}

	public void setServiceDetail(ServiceDetail serviceDetail) {
		this.serviceDetail = serviceDetail;
	}

	public Set<UniswitchDetail> getUniswitchDetails() {
		return this.uniswitchDetails;
	}

	public void setUniswitchDetails(Set<UniswitchDetail> uniswitchDetails) {
		this.uniswitchDetails = uniswitchDetails;
	}

	public UniswitchDetail addUniswitchDetail(UniswitchDetail uniswitchDetail) {
		getUniswitchDetails().add(uniswitchDetail);
		uniswitchDetail.setTopology(this);

		return uniswitchDetail;
	}

	public UniswitchDetail removeUniswitchDetail(UniswitchDetail uniswitchDetail) {
		getUniswitchDetails().remove(uniswitchDetail);
		uniswitchDetail.setTopology(null);

		return uniswitchDetail;
	}

}
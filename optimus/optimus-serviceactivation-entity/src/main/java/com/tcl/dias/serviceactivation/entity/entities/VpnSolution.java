package com.tcl.dias.serviceactivation.entity.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;

/**
 * 
 * VpnSolution Entity Class
 * 
 *
 * @author Naveen
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name = "vpn_solution")
@NamedQuery(name = "VpnSolution.findAll", query = "SELECT v FROM VpnSolution v")
public class VpnSolution implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "vpn_solution_id")
	private Integer vpnSolutionId;

	@Column(name = "end_date")
	private Timestamp endDate;

	@Column(name = "instance_id")
	private String instanceId;

	@Column(name = "interface_name")
	private String interfaceName;

	@Column(name = "last_modified_date")
	private Timestamp lastModifiedDate;

	@Column(name = "leg_role")
	private String legRole;

	@Column(name = "modified_by")
	private String modifiedBy;

	@Column(name = "site_id")
	private String siteId;

	@Column(name = "start_date")
	private Timestamp startDate;

	@Column(name = "vpn_leg_id")
	private String vpnLegId;

	@Column(name = "vpn_name")
	private String vpnName;

	@Column(name = "vpn_solution_name")
	private String vpnSolutionName;

	@Column(name = "vpn_topology")
	private String vpnTopology;

	@Column(name = "vpn_type")
	private String vpnType;

	// bi-directional many-to-one association to ServiceDetail
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "service_details_id")
	private ServiceDetail serviceDetail;

	public VpnSolution() {
	}

	public Integer getVpnSolutionId() {
		return this.vpnSolutionId;
	}

	public void setVpnSolutionId(Integer vpnSolutionId) {
		this.vpnSolutionId = vpnSolutionId;
	}

	public Timestamp getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
	}

	public String getInstanceId() {
		return this.instanceId;
	}

	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

	public String getInterfaceName() {
		return this.interfaceName;
	}

	public void setInterfaceName(String interfaceName) {
		this.interfaceName = interfaceName;
	}

	public Timestamp getLastModifiedDate() {
		return this.lastModifiedDate;
	}

	public void setLastModifiedDate(Timestamp lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public String getLegRole() {
		return this.legRole;
	}

	public void setLegRole(String legRole) {
		this.legRole = legRole;
	}

	public String getModifiedBy() {
		return this.modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public String getSiteId() {
		return this.siteId;
	}

	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}

	public Timestamp getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Timestamp startDate) {
		this.startDate = startDate;
	}

	public String getVpnLegId() {
		return this.vpnLegId;
	}

	public void setVpnLegId(String vpnLegId) {
		this.vpnLegId = vpnLegId;
	}

	public String getVpnName() {
		return this.vpnName;
	}

	public void setVpnName(String vpnName) {
		this.vpnName = vpnName;
	}

	public String getVpnSolutionName() {
		return this.vpnSolutionName;
	}

	public void setVpnSolutionName(String vpnSolutionName) {
		this.vpnSolutionName = vpnSolutionName;
	}

	public String getVpnTopology() {
		return this.vpnTopology;
	}

	public void setVpnTopology(String vpnTopology) {
		this.vpnTopology = vpnTopology;
	}

	public String getVpnType() {
		return this.vpnType;
	}

	public void setVpnType(String vpnType) {
		this.vpnType = vpnType;
	}

	public ServiceDetail getServiceDetail() {
		return this.serviceDetail;
	}

	public void setServiceDetail(ServiceDetail serviceDetail) {
		this.serviceDetail = serviceDetail;
	}

}
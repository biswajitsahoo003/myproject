package com.tcl.dias.serviceactivation.entity.entities;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "vpn_metat_data")
@NamedQuery(name = "VpnMetatData.findAll", query = "SELECT v FROM VpnMetatData v")
public class VpnMetatData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "vpn_solution_name")
	private String vpnSolutionName;

	@Column(name = "vpn_name")
	private String vpnName;

	@Column(name = "vpn_alias")
	private String vpnAlias;

	@Column(name = "vpn_leg_id")
	private Integer vpnLegId;

	@Column(name = "management_vpn_type1")
	private String managementVpnType1;

	@Column(name = "management_vpn_type2")
	private String managementVpnType2;

	@Column(name = "is_ua")
	private String isUa;

	@Column(name = "isenable_ucc_service")
	private boolean isenableUccService;

	@Column(name = "is_e2e_integrated")
	private boolean isE2eIntegrated;

	@Column(name = "solution_id")
	private String solutionId;

	@Column(name = "l2o_topology")
	private String l2OTopology;

	@Column(name = "l2o_Site_role")
	private String l2oSiteRole;

	private String topology;

	@Column(name = "end_date")
	private Timestamp endDate;

	@Column(name = "modified_by")
	private String modifiedBy;

	// bi-directional many-to-one association to ServiceDetail
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "service_details_id")
	private ServiceDetail serviceDetail;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getVpnSolutionName() {
		return vpnSolutionName;
	}

	public void setVpnSolutionName(String vpnSolutionName) {
		this.vpnSolutionName = vpnSolutionName;
	}

	public String getVpnName() {
		return vpnName;
	}

	public void setVpnName(String vpnName) {
		this.vpnName = vpnName;
	}

	public String getVpnAlias() {
		return vpnAlias;
	}

	public void setVpnAlias(String vpnAlias) {
		this.vpnAlias = vpnAlias;
	}

	public Integer getVpnLegId() {
		return vpnLegId;
	}

	public void setVpnLegId(Integer vpnLegId) {
		this.vpnLegId = vpnLegId;
	}

	public String getManagementVpnType1() {
		return managementVpnType1;
	}

	public void setManagementVpnType1(String managementVpnType1) {
		this.managementVpnType1 = managementVpnType1;
	}

	public String getManagementVpnType2() {
		return managementVpnType2;
	}

	public void setManagementVpnType2(String managementVpnType2) {
		this.managementVpnType2 = managementVpnType2;
	}

	public String getIsUa() {
		return isUa;
	}

	public void setIsUa(String isUa) {
		this.isUa = isUa;
	}

	public boolean isE2eIntegrated() {
		return isE2eIntegrated;
	}

	public void setE2eIntegrated(boolean isE2eIntegrated) {
		this.isE2eIntegrated = isE2eIntegrated;
	}

	public String getSolutionId() {
		return solutionId;
	}

	public void setSolutionId(String solutionId) {
		this.solutionId = solutionId;
	}

	public boolean isIsenableUccService() {
		return isenableUccService;
	}

	public void setIsenableUccService(boolean isenableUccService) {
		this.isenableUccService = isenableUccService;
	}

	public ServiceDetail getServiceDetail() {
		return serviceDetail;
	}

	public void setServiceDetail(ServiceDetail serviceDetail) {
		this.serviceDetail = serviceDetail;
	}

	public String getL2oSiteRole() {
		return l2oSiteRole;
	}

	public void setL2oSiteRole(String l2oSiteRole) {
		this.l2oSiteRole = l2oSiteRole;
	}

	public String getL2OTopology() {
		return l2OTopology;
	}

	public void setL2OTopology(String l2oTopology) {
		l2OTopology = l2oTopology;
	}

	public String getTopology() {
		return topology;
	}

	public void setTopology(String topology) {
		this.topology = topology;
	}

	public Timestamp getEndDate() {
		return endDate;
	}

	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
}

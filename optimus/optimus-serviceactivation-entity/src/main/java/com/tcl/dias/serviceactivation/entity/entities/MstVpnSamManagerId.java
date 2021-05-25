package com.tcl.dias.serviceactivation.entity.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "mst_vpn_sam_mgr_id")
@NamedQuery(name = "MstVpnSamManagerId.findAll", query = "SELECT m FROM MstVpnSamManagerId m")
public class MstVpnSamManagerId implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;

	@Column(name = "bgp_password")
	private String bgpPassword;

	@Column(name = "created_by")
	private String createdBy;

	@Column(name = "created_date")
	private String createdDate;

	private String cuid;

	@Column(name = "customer_rt")
	private String customerRt;

	@Column(name = "last_modified_by")
	private String lastModifiedBy;

	@Column(name = "last_modified_date")
	private String lastModifiedDate;

	@Column(name = "nni_id")
	private String nniId;

	@Column(name = "partner_rt")
	private String partnerRt;

	@Column(name = "project_name")
	private String projectName;

	@Column(name = "sam_mgr_id")
	private String samMgrId;

	@Column(name = "service_code")
	private String serviceCode;

	@Column(name = "spoke_sam_mgr_id")
	private String spokeSamMgrId;

	@Column(name = "vpn_id")
	private String vpnId;

	@Column(name = "vpn_name")
	private String vpnName;

	@Column(name = "vpn_topology")
	private String vpnTopology;
	
	@Column(name = "site_role")
	private String siteRole;
	
	

	@Column(name = "vpn_type")
	private String vpnType;
	
	


	public String getSiteRole() {
		return siteRole;
	}

	public void setSiteRole(String siteRole) {
		this.siteRole = siteRole;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getBgpPassword() {
		return this.bgpPassword;
	}

	public void setBgpPassword(String bgpPassword) {
		this.bgpPassword = bgpPassword;
	}

	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getCuid() {
		return this.cuid;
	}

	public void setCuid(String cuid) {
		this.cuid = cuid;
	}

	public String getCustomerRt() {
		return this.customerRt;
	}

	public void setCustomerRt(String customerRt) {
		this.customerRt = customerRt;
	}

	public String getLastModifiedBy() {
		return this.lastModifiedBy;
	}

	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	public String getLastModifiedDate() {
		return this.lastModifiedDate;
	}

	public void setLastModifiedDate(String lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public String getNniId() {
		return this.nniId;
	}

	public void setNniId(String nniId) {
		this.nniId = nniId;
	}

	public String getPartnerRt() {
		return this.partnerRt;
	}

	public void setPartnerRt(String partnerRt) {
		this.partnerRt = partnerRt;
	}

	public String getProjectName() {
		return this.projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getSamMgrId() {
		return this.samMgrId;
	}

	public void setSamMgrId(String samMgrId) {
		this.samMgrId = samMgrId;
	}

	public String getServiceCode() {
		return this.serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	public String getSpokeSamMgrId() {
		return this.spokeSamMgrId;
	}

	public void setSpokeSamMgrId(String spokeSamMgrId) {
		this.spokeSamMgrId = spokeSamMgrId;
	}

	public String getVpnId() {
		return this.vpnId;
	}

	public void setVpnId(String vpnId) {
		this.vpnId = vpnId;
	}

	public String getVpnName() {
		return this.vpnName;
	}

	public void setVpnName(String vpnName) {
		this.vpnName = vpnName;
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

}

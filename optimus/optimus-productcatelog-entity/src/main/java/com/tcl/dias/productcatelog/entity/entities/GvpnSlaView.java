package com.tcl.dias.productcatelog.entity.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;

/**
 * The persistent class for the vw_sla_pdt_with_id_GVPN database table.
 */
@Entity
@Table(name = "vw_sla_pdt_with_id_by_tier_GVPN")
@Immutable
@IdClass(GvpnSlaViewId.class)
public class GvpnSlaView implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name = "Pdt_Name")
	private String pdtName;

	@Id
	@Column(name = "pdt_catalog_id")
	private Integer pdtCatalogId;

	@Column(name = "SLA_Name")
	private String slaName;

	@Id
	@Column(name = "SLAIdNo")
	private Integer slaIdNo;


	@Column(name = "slt_varient")
	private String sltVariant;

	// SLA_Id

	@Column(name = "access_topology_nm")
	private String accessTopology;

	@Column(name = "tier1_value")
	private String tier1Value;

	@Column(name = "tier2_value")
	private String tier2Value;

	@Column(name = "tier3_value")
	private String tier3Value;

	@Column(name = "tier4_value")
	private String tier4Value;

	@Column(name = "remarks_txt")
	private String remarksTxt;

	@Id
	@Column(name = "SLA_Id")
	private Integer slaId;

	public GvpnSlaView() {
		// Do nothing
	}

	public Integer getPdtCatalogId() {
		return this.pdtCatalogId;
	}

	public void setPdtCatalogId(Integer pdtCatalogId) {
		this.pdtCatalogId = pdtCatalogId;
	}

	public String getAccessTopology() {
		return this.accessTopology;
	}

	public void setAccessTopology(String accessTopology) {
		this.accessTopology = accessTopology;
	}

	public String getRemarksTxt() {
		return this.remarksTxt;
	}

	public void setRemarksTxt(String remarksTxt) {
		this.remarksTxt = remarksTxt;
	}

	public String getTier1Value() {
		return this.tier1Value;
	}

	public void setTier1Value(String tier1Value) {
		this.tier1Value = tier1Value;
	}

	public String getTier2Value() {
		return this.tier2Value;
	}

	public void setTier2Value(String tier2Value) {
		this.tier2Value = tier2Value;
	}

	public String getTier3Value() {
		return this.tier3Value;
	}

	public void setTier3Value(String tier3Value) {
		this.tier3Value = tier3Value;
	}

	public String getTier4Value() {
		return this.tier4Value;
	}

	public void setTier4Value(String tier4Value) {
		this.tier4Value = tier4Value;
	}

	public String getSlaName() {
		return slaName;
	}

	public void setSlaName(String slaName) {
		this.slaName = slaName;
	}

	public Integer getSlaIdNo() {
		return slaIdNo;
	}

	public void setSlaIdNo(Integer slaIdNo) {
		this.slaIdNo = slaIdNo;
	}

	public String getPdtName() {
		return pdtName;
	}

	public void setPdtName(String pdtName) {
		this.pdtName = pdtName;
	}



	public String getSltVariant() {
		return sltVariant;
	}

	public void setSltVariant(String sltVariant) {
		this.sltVariant = sltVariant;
	}

	public Integer getSlaId() {
		return slaId;
	}

	public void setSlaId(Integer slaId) {
		this.slaId = slaId;
	}



	

}
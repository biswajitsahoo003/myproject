package com.tcl.dias.productcatelog.entity.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * This file contains the VwSlaProductByTierSiteSdwan.java class.
 * 
 * @author vpachava
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
@Entity
@Table(name="vw_sla_pdt_by_tier_site_sdwan_select")	
public class VwSlaPdtByTierSiteSdwanSelect implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="id")
	private Integer id;
	
	@Column(name="site_type_code")
	private String siteTypeCode;
	
	
	@Column(name="site_type_name")
	private String siteTypeName;
	
	
	@Column(name="slt_varient")
	private String sltVarient;
	
	@Column(name="access_topology_nm")
	private String accessTopologyName;
	
	@Column(name="tier1_value")
	private String Tier1Value;
	
	@Column(name="tier2_value")
	private String Tier2Value;
	
	@Column(name="tier3_value")
	private String Tier3Value;
	
	@Column(name="tier4_value")
	private String Tier4Value;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSiteTypeCode() {
		return siteTypeCode;
	}

	public void setSiteTypeCode(String siteTypeCode) {
		this.siteTypeCode = siteTypeCode;
	}

	public String getSiteTypeName() {
		return siteTypeName;
	}

	public void setSiteTypeName(String siteTypeName) {
		this.siteTypeName = siteTypeName;
	}

	public String getSltVarient() {
		return sltVarient;
	}

	public void setSltVarient(String sltVarient) {
		this.sltVarient = sltVarient;
	}

	public String getAccessTopologyName() {
		return accessTopologyName;
	}

	public void setAccessTopologyName(String accessTopologyName) {
		this.accessTopologyName = accessTopologyName;
	}

	public String getTier1Value() {
		return Tier1Value;
	}

	public void setTier1Value(String tier1Value) {
		Tier1Value = tier1Value;
	}

	public String getTier2Value() {
		return Tier2Value;
	}

	public void setTier2Value(String tier2Value) {
		Tier2Value = tier2Value;
	}

	public String getTier3Value() {
		return Tier3Value;
	}

	public void setTier3Value(String tier3Value) {
		Tier3Value = tier3Value;
	}

	public String getTier4Value() {
		return Tier4Value;
	}

	public void setTier4Value(String tier4Value) {
		Tier4Value = tier4Value;
	}
	
	
	

}

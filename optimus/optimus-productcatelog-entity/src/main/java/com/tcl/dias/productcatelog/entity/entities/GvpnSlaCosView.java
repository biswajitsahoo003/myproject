package com.tcl.dias.productcatelog.entity.entities;

import java.io.Serializable;
import javax.persistence.*;

import org.hibernate.annotations.Immutable;

import java.math.BigInteger;


/**
 * The persistent class for the vw_sla_pdt_with_id_cos_GVPN database view.
 */
@Entity
@Table(name="vw_sla_pdt_with_id_cos_GVPN")
@Immutable
@IdClass(GvpnSlaCosViewId.class)
public class GvpnSlaCosView implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Column(name="Pdt_Name")
	private String pdtName;
	

	@Id
	@Column(name="pdt_catalog_id")
	private Integer pdtCatalogId;
	

	@Column(name="SLA_Name")
	private String slaName;
	
	@Column(name="SLA_Id")
	private Integer slaId;
	
	@Column(name="cos_schema_nm")
	private String cosSchemaName;
	
	@Column(name="cos1_value")
	private String cos1Value;
	
	@Column(name="cos2_value")
	private String cos2Value;
	
	@Column(name="cos3_value")
	private String cos3Value;
	
	@Column(name="cos4_value")
	private String cos4Value;
	
	@Column(name="pop_tier_cd")
	private String popTierCd;
	
	@Column(name="uom_cd")
	private String uomCd;
	
	public String getCosSchemaName() {
		return cosSchemaName;
	}

	public void setCosSchemaName(String cosSchemaName) {
		this.cosSchemaName = cosSchemaName;
	}

	public String getCos1Value() {
		return cos1Value;
	}

	public void setCos1Value(String cos1Value) {
		this.cos1Value = cos1Value;
	}

	public String getCos2Value() {
		return cos2Value;
	}

	public void setCos2Value(String cos2Value) {
		this.cos2Value = cos2Value;
	}

	public String getCos3Value() {
		return cos3Value;
	}

	public void setCos3Value(String cos3Value) {
		this.cos3Value = cos3Value;
	}

	public String getCos4Value() {
		return cos4Value;
	}

	public void setCos4Value(String cos4Value) {
		this.cos4Value = cos4Value;
	}

	public String getCos5Value() {
		return cos5Value;
	}

	public void setCos5Value(String cos5Value) {
		this.cos5Value = cos5Value;
	}

	public String getCos6Value() {
		return cos6Value;
	}

	public void setCos6Value(String cos6Value) {
		this.cos6Value = cos6Value;
	}

	@Column(name="cos5_value")
	private String cos5Value;
	
	@Column(name="cos6_value")
	private String cos6Value;
	

	@Id
	@Column(name="SLAIdNo")
	private Integer slaIdNo;
	
	
    public GvpnSlaCosView() {
    	//Do nothing
    }
    
    public Integer getPdtCatalogId() {
    	return this.pdtCatalogId;
    }
    
    public void setPdtCatalogId(Integer pdtCatalogId) {
    	 this.pdtCatalogId=pdtCatalogId;
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

	public String getPopTierCd() {
		return popTierCd;
	}

	public String getUomCd() {
		return uomCd;
	}

	public void setPopTierCd(String popTierCd) {
		this.popTierCd = popTierCd;
	}

	public void setUomCd(String uomCd) {
		this.uomCd = uomCd;
	}

	public Integer getSlaId() {
		return slaId;
	}

	public void setSlaId(Integer slaId) {
		this.slaId = slaId;
	}
}
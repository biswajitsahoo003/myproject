package com.tcl.dias.productcatelog.entity.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
/**
 * 
 * This file contains the VwProductSlaSpecIAS.java class.
 * 
 *
 * @author ANANDHI VIJAY
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name="vw_product_sla_spec_IAS")
public class VwProductSlaSpecIAS implements Serializable{

	private static final long serialVersionUID = 1L;

	@Column(name="created_by")
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="created_dt")
	private Date createdDt;

	@Column(name="default_value")
	private String defaultValue;
	
	@Id
	private Integer id;

	@Column(name="pdt_catalog_id")
	private Integer pdtCatalogId;

	@Column(name="remarks_txt")
	private String remarksTxt;

	@Column(name="sla_metric_id")
	private Integer slaMetricId;

	@Column(name="slt_varient")
	private String sltVarient;

	@Column(name="tier1_value")
	private String tier1Value;

	@Column(name="tier2_value")
	private String tier2Value;

	@Column(name="tier3_value")
	private String tier3Value;

	@Column(name="updated_by")
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="updated_dt")
	private Date updatedDt;

	public VwProductSlaSpecIAS() {
	}

	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDt() {
		return this.createdDt;
	}

	public void setCreatedDt(Date createdDt) {
		this.createdDt = createdDt;
	}

	public String getDefaultValue() {
		return this.defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getPdtCatalogId() {
		return this.pdtCatalogId;
	}

	public void setPdtCatalogId(Integer pdtCatalogId) {
		this.pdtCatalogId = pdtCatalogId;
	}

	public String getRemarksTxt() {
		return this.remarksTxt;
	}

	public void setRemarksTxt(String remarksTxt) {
		this.remarksTxt = remarksTxt;
	}

	public Integer getSlaMetricId() {
		return this.slaMetricId;
	}

	public void setSlaMetricId(Integer slaMetricId) {
		this.slaMetricId = slaMetricId;
	}

	public String getSltVarient() {
		return this.sltVarient;
	}

	public void setSltVarient(String sltVarient) {
		this.sltVarient = sltVarient;
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

	public String getUpdatedBy() {
		return this.updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedDt() {
		return this.updatedDt;
	}

	public void setUpdatedDt(Date updatedDt) {
		this.updatedDt = updatedDt;
	}


}

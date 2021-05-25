package com.tcl.dias.productcatelog.entity.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;

/**
 * Entity class for vw_sla_pdt_with_id_GSC table
 * 
 * @author AVALLAPI
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name = "vw_sla_pdt_with_id_GSC")
@Immutable
@IdClass(GvpnSlaViewId.class)
public class GscSlaView {

	@Column(name = "Pdt_Name")
	private String pdtName;

	@Id
	@Column(name = "pdt_catalog_id")
	private Integer pdtCatalogId;

	@Column(name = "SLA_Name")
	private String slaName;

	@Column(name = "sla_metric_id")
	private Integer slaMetricId;

	@Column(name = "access_topology_nm")
	private String accessTopology;

	@Column(name = "default_value")
	private String defaultValue;

	@Column(name = "Factor_Name")
	private String factoryName;

	@Column(name = "Factor_Name_Id")
	private Integer factoryNameId;

	@Column(name = "Factor_Value")
	private String factoryValue;

	@Column(name = "Factor_Value_Id")
	private Integer factoryValueId;

	@Column(name = "SubGrp_Id")
	private Integer subGroupId;

	@Id
	@Column(name = "SLAIdNo")
	private Integer slaIdNo;

	@Column(name = "remarks_txt")
	private String remarksTxt;

	@Id
	@Column(name = "SLA_Id")
	private Integer slaId;

	public String getPdtName() {
		return pdtName;
	}

	public void setPdtName(String pdtName) {
		this.pdtName = pdtName;
	}

	public Integer getPdtCatalogId() {
		return pdtCatalogId;
	}

	public void setPdtCatalogId(Integer pdtCatalogId) {
		this.pdtCatalogId = pdtCatalogId;
	}

	public String getSlaName() {
		return slaName;
	}

	public void setSlaName(String slaName) {
		this.slaName = slaName;
	}

	public Integer getSlaMetricId() {
		return slaMetricId;
	}

	public void setSlaMetricId(Integer slaMetricId) {
		this.slaMetricId = slaMetricId;
	}

	public String getAccessTopology() {
		return accessTopology;
	}

	public void setAccessTopology(String accessTopology) {
		this.accessTopology = accessTopology;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public String getFactoryName() {
		return factoryName;
	}

	public void setFactoryName(String factoryName) {
		this.factoryName = factoryName;
	}

	public Integer getFactoryNameId() {
		return factoryNameId;
	}

	public void setFactoryNameId(Integer factoryNameId) {
		this.factoryNameId = factoryNameId;
	}

	public String getFactoryValue() {
		return factoryValue;
	}

	public void setFactoryValue(String factoryValue) {
		this.factoryValue = factoryValue;
	}

	public Integer getFactoryValueId() {
		return factoryValueId;
	}

	public void setFactoryValueId(Integer factoryValueId) {
		this.factoryValueId = factoryValueId;
	}

	public Integer getSubGroupId() {
		return subGroupId;
	}

	public void setSubGroupId(Integer subGroupId) {
		this.subGroupId = subGroupId;
	}

	public Integer getSlaIdNo() {
		return slaIdNo;
	}

	public void setSlaIdNo(Integer slaIdNo) {
		this.slaIdNo = slaIdNo;
	}

	public String getRemarksTxt() {
		return remarksTxt;
	}

	public void setRemarksTxt(String remarksTxt) {
		this.remarksTxt = remarksTxt;
	}

	public Integer getSlaId() {
		return slaId;
	}

	public void setSlaId(Integer slaId) {
		this.slaId = slaId;
	}

}

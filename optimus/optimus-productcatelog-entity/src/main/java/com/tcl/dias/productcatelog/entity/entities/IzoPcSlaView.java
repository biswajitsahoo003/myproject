package com.tcl.dias.productcatelog.entity.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;

/**
 * Entity corresponding to vw_sla_pdt_with_id_IZO view
 * 
 * @author Dinahar V
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Entity
@Immutable
@Table(name = "vw_sla_pdt_with_id_IZO")
@IdClass(IzoPcSlaViewId.class)
public class IzoPcSlaView {
	
	@Column(name = "Pdt_Name")
	private String productName;
	
	@Id
	@Column(name = "pdt_catalog_id")
	private Integer productCatalogId;

	@Column(name = "SLA_Name")
	private String slaName;
	
	@Id
	@Column(name = "SLA_Id")
	private Integer slaId;
	
	@Id
	@Column(name = "sla_metric_id")
	private Integer slaMetricId;
	
	@Column(name = "default_value")
	private String defaultValue;
	
	@Id
	@Column(name = "SLAIdNo")
	private Integer slaIdNo;

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Integer getProductCatalogId() {
		return productCatalogId;
	}

	public void setProductCatalogId(Integer productCatalogId) {
		this.productCatalogId = productCatalogId;
	}

	public String getSlaName() {
		return slaName;
	}

	public void setSlaName(String slaName) {
		this.slaName = slaName;
	}

	public Integer getSlaId() {
		return slaId;
	}

	public void setSlaId(Integer slaId) {
		this.slaId = slaId;
	}

	public Integer getSlaMetricId() {
		return slaMetricId;
	}

	public void setSlaMetricId(Integer slaMetricId) {
		this.slaMetricId = slaMetricId;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public Integer getSlaIdNo() {
		return slaIdNo;
	}

	public void setSlaIdNo(Integer slaIdNo) {
		this.slaIdNo = slaIdNo;
	}




}

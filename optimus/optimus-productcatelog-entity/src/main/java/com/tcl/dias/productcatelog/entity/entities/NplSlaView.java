package com.tcl.dias.productcatelog.entity.entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import org.hibernate.annotations.Immutable;

/**
 * 
 * @author Thamizhselvi Perumal 
 * The persistent view class for NPL product as vw_sla_pdt_with_id_NPL View.
 * 
 */
@Entity
@Immutable
@Table(name = "vw_sla_pdt_with_id_NPL")
@IdClass(NplSlaViewId.class)

public class NplSlaView implements Serializable {
	private static final long serialVersionUID = 1L;

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

	@Id
	@Column(name = "slt_varient")
	private String serviceVarient;

	@Id
	@Column(name = "access_topology_nm")
	private String accessTopology;

	@Column(name = "default_value")
	private String defaultValue;

	@Column(name = "remarks_txt")
	private String remarksText;

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

	public String getServiceVarient() {
		return serviceVarient;
	}

	public void setServiceVarient(String serviceVarient) {
		this.serviceVarient = serviceVarient;
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
	
	public Double getDefaultValueInDouble() {
		return Double.parseDouble(getDefaultValue());
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public String getRemarksText() {
		return remarksText;
	}

	public void setRemarksText(String remarksText) {
		this.remarksText = remarksText;
	}

}

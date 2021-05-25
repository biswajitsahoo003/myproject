package com.tcl.dias.servicefulfillment.entity.entities;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

/**
 * This file contains the ScTeamsDRServiceCommercial.java class.
 *
 * @author Syed Ali
 * @link http://www.tatacommunications.com/
 * @copyright 2021 Tata Communications Limited
 */
@Entity
@Table(name = "sc_teamsdr_service_commercial")
@NamedQuery(name = "ScTeamsDRServiceCommercial.findAll", query = "SELECT o FROM ScTeamsDRServiceCommercial o")
public class ScTeamsDRServiceCommercial implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	// bi-directional many-to-one association to OdrServiceDetail
	@ManyToOne
	@JoinColumn(name = "sc_service_detail_id")
	private ScServiceDetail scServiceDetail;

	@Column(name = "component_name")
	private String componentName;

	@Column(name = "component_desc")
	private String componentDesc;

	@Column(name = "charge_item")
	private String chargeItem;

	@Column(name = "component_type")
	private String componentType;

	@Column(name = "hsn_code")
	private String hsnCode;

	private Integer quantity;

	private Double arc;

	@Column(name = "unit_mrc")
	private Double unitMrc;

	private Double mrc;

	private Double nrc;

	@Column(name = "unit_nrc")
	private Double unitNrc;

	private Double tcv;

	@Column(name = "effective_usage")
	private Double usage;

	@Column(name = "effective_overage")
	private Double overage;

	@Column(name = "is_active")
	private String isActive;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_date")
	private Date createdDate;

	@Column(name = "created_by")
	private String createdBy;

	@Column(name = "updated_by")
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updated_date")
	private Date updatedDate;

	@Column(name = "contract_type")
	private String contractType;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getComponentName() {
		return componentName;
	}

	public void setComponentName(String componentName) {
		this.componentName = componentName;
	}

	public String getComponentDesc() {
		return componentDesc;
	}

	public void setComponentDesc(String componentDesc) {
		this.componentDesc = componentDesc;
	}

	public String getComponentType() {
		return componentType;
	}

	public void setComponentType(String componentType) {
		this.componentType = componentType;
	}

	public String getHsnCode() {
		return hsnCode;
	}

	public void setHsnCode(String hsnCode) {
		this.hsnCode = hsnCode;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Double getArc() {
		return arc;
	}

	public void setArc(Double arc) {
		this.arc = arc;
	}

	public Double getUnitMrc() {
		return unitMrc;
	}

	public void setUnitMrc(Double unitMrc) {
		this.unitMrc = unitMrc;
	}

	public Double getMrc() {
		return mrc;
	}

	public void setMrc(Double mrc) {
		this.mrc = mrc;
	}

	public Double getNrc() {
		return nrc;
	}

	public void setNrc(Double nrc) {
		this.nrc = nrc;
	}

	public Double getUnitNrc() {
		return unitNrc;
	}

	public void setUnitNrc(Double unitNrc) {
		this.unitNrc = unitNrc;
	}

	public Double getTcv() {
		return tcv;
	}

	public void setTcv(Double tcv) {
		this.tcv = tcv;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public String getContractType() {
		return contractType;
	}

	public void setContractType(String contractType) {
		this.contractType = contractType;
	}

	public String getChargeItem() {
		return chargeItem;
	}

	public void setChargeItem(String chargeItem) {
		this.chargeItem = chargeItem;
	}

	public Double getUsage() {
		return usage;
	}

	public void setUsage(Double usage) {
		this.usage = usage;
	}

	public Double getOverage() {
		return overage;
	}

	public void setOverage(Double overage) {
		this.overage = overage;
	}

	public ScServiceDetail getScServiceDetail() {
		return scServiceDetail;
	}

	public void setScServiceDetail(ScServiceDetail scServiceDetail) {
		this.scServiceDetail = scServiceDetail;
	}
}
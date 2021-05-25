package com.tcl.dias.oms.entity.entities;

import java.io.Serializable;
import java.util.Date;

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

/**
 * 
 * This file contains the OdrWebexServiceCommercial.java class.
 * 
 *
 * @author Selvakumar Palaniandy
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Entity
@Table(name="odr_webex_service_commercial")
@NamedQuery(name = "OdrWebexServiceCommercial.findAll", query = "SELECT o FROM OdrWebexServiceCommercial o")
public class OdrWebexServiceCommercial implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	//bi-directional many-to-one association to OdrServiceDetail
	@ManyToOne
	@JoinColumn(name="odr_service_detail_id")
	private OdrServiceDetail odrServiceDetail;

	@Column(name="component_name")
	private String componentName;
	
	@Column(name="component_desc")
	private String componentDesc;
	
	@Column(name="component_type")
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

	@Column(name = "cisco_unit_list_price")
	private Double ciscoUnitListPrice;

	@Column(name = "cisco_discnt_prct")
	private Double ciscoDiscountPercent;

	@Column(name = "cisco_unit_net_price")
	private Double ciscoUnitNetPrice;
	
	@Column(name="is_active")
	private String isActive;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="created_date")
	private Date createdDate;

	@Column(name="created_by")
	private String createdBy;
	
	@Column(name="updated_by")
	private String updatedBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="updated_date")
	private Date updatedDate;
	
	@Column(name = "contract_type")
	private String contractType;

	@Column(name = "endpoint_management_type")
	private String endpointManagementType;
	
	@Column(name = "short_text")
	private String shortText;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public OdrServiceDetail getOdrServiceDetail() {
		return odrServiceDetail;
	}

	public void setOdrServiceDetail(OdrServiceDetail odrServiceDetail) {
		this.odrServiceDetail = odrServiceDetail;
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

	public Double getCiscoUnitListPrice() {
		return ciscoUnitListPrice;
	}

	public void setCiscoUnitListPrice(Double ciscoUnitListPrice) {
		this.ciscoUnitListPrice = ciscoUnitListPrice;
	}

	public Double getCiscoDiscountPercent() {
		return ciscoDiscountPercent;
	}

	public void setCiscoDiscountPercent(Double ciscoDiscountPercent) {
		this.ciscoDiscountPercent = ciscoDiscountPercent;
	}

	public Double getCiscoUnitNetPrice() {
		return ciscoUnitNetPrice;
	}

	public void setCiscoUnitNetPrice(Double ciscoUnitNetPrice) {
		this.ciscoUnitNetPrice = ciscoUnitNetPrice;
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

	public String getEndpointManagementType() {
		return endpointManagementType;
	}

	public void setContractType(String contractType) {
		this.contractType = contractType;
	}

	public void setEndpointManagementType(String endpointManagementType) {
		this.endpointManagementType = endpointManagementType;
	}

	public String getShortText() {
		return shortText;
	}

	public void setShortText(String shortText) {
		this.shortText = shortText;
	}

	@Override
	public String toString() {
		return "OdrWebexServiceCommercial [id=" + id + ", odrServiceDetail=" + odrServiceDetail + ", componentName="
				+ componentName + ", componentDesc=" + componentDesc + ", quantity=" + quantity + ", arc=" + arc
				+ ", unitMrc=" + unitMrc + ", mrc=" + mrc + ", nrc=" + nrc + ", unitNrc=" + unitNrc + ", tcv=" + tcv
				+ ", ciscoUnitListPrice=" + ciscoUnitListPrice + ", ciscoDiscountPercent=" + ciscoDiscountPercent
				+ ", ciscoUnitNetPrice=" + ciscoUnitNetPrice + ", isActive=" + isActive + ", createdDate=" + createdDate
				+ ", createdBy=" + createdBy + ", updatedBy=" + updatedBy + ", updatedDate=" + updatedDate + "]";
	}
}
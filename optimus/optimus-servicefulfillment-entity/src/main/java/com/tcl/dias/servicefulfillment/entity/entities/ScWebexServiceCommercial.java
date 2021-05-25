package com.tcl.dias.servicefulfillment.entity.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * 
 * This file contains the ScWebexServiceCommercial.java class.
 * 
 *
 * @author Selvakumar Palaniandy
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Entity
@Table(name="sc_webex_service_commercial")
@NamedQuery(name = "ScWebexServiceCommercial.findAll", query = "SELECT o FROM ScWebexServiceCommercial o")
public class ScWebexServiceCommercial implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	//bi-directional many-to-one association to ScServiceDetail
	@ManyToOne
	@JoinColumn(name="sc_service_detail_id")
	private ScServiceDetail scServiceDetail;

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
	
	@Column(name = "contract_type")
	private String contractType;

	@Column(name = "endpoint_management_type")
	private String endpointManagementType;
	
	@Column(name="is_active")
	private String isActive;
	
	@Column(name = "short_text")
	private String shortText;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public ScServiceDetail getScServiceDetail() {
		return scServiceDetail;
	}

	public void setScServiceDetail(ScServiceDetail scServiceDetail) {
		this.scServiceDetail = scServiceDetail;
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

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public String getShortText() {
		return shortText;
	}

	public void setShortText(String shortText) {
		this.shortText = shortText;
	}

	@Override
	public String toString() {
		return "ScWebexServiceCommercial [id=" + id + ", scServiceDetail=" + scServiceDetail + ", componentName="
				+ componentName + ", componentDesc=" + componentDesc + ", componentType=" + componentType
				+ ", quantity=" + quantity + ", arc=" + arc + ", unitMrc=" + unitMrc + ", mrc=" + mrc + ", nrc=" + nrc
				+ ", unitNrc=" + unitNrc + ", tcv=" + tcv + ", ciscoUnitListPrice=" + ciscoUnitListPrice
				+ ", ciscoDiscountPercent=" + ciscoDiscountPercent + ", ciscoUnitNetPrice=" + ciscoUnitNetPrice
				+ ", isActive=" + isActive + "]";
	}

}
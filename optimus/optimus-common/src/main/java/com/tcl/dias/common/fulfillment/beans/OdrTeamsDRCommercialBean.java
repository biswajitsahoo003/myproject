package com.tcl.dias.common.fulfillment.beans;

import java.io.Serializable;

/**
 * This file contains the OdrTeamsDRCommercialBean.java class.
 *
 * @author Syed Ali
 * @link http://www.tatacommunications.com/
 * @copyright 2021 Tata Communications Limited
 */
public class OdrTeamsDRCommercialBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer serviceId;

	private String componentName;

	private String componentDesc;

	private String componentType;

	private String hsnCode;

	private Integer quantity;

	private Double arc;

	private Double unitMrc;

	private Double mrc;

	private Double nrc;

	private Double unitNrc;

	private Double tcv;

	private String isActive;

	private String contractType;

	private String createdBy;

	private String chargeItem;

	private Double effectiveUsage;

	private Double effectiveOverage;

	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
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

	public String getContractType() {
		return contractType;
	}

	public void setContractType(String contractType) {
		this.contractType = contractType;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getChargeItem() {
		return chargeItem;
	}

	public void setChargeItem(String chargeItem) {
		this.chargeItem = chargeItem;
	}

	public Double getEffectiveUsage() {
		return effectiveUsage;
	}

	public void setEffectiveUsage(Double effectiveUsage) {
		this.effectiveUsage = effectiveUsage;
	}

	public Double getEffectiveOverage() {
		return effectiveOverage;
	}

	public void setEffectiveOverage(Double effectiveOverage) {
		this.effectiveOverage = effectiveOverage;
	}

	@Override
	public String toString() {
		return "OdrTeamsDRCommercialBean{" + "serviceId=" + serviceId + ", componentName='" + componentName + '\'' +
				", componentDesc='" + componentDesc + '\'' + ", componentType='" + componentType + '\'' + ", hsnCode" +
				"='" + hsnCode + '\'' + ", quantity=" + quantity + ", arc=" + arc + ", unitMrc=" + unitMrc + ", mrc=" + mrc + ", nrc=" + nrc + ", unitNrc=" + unitNrc + ", tcv=" + tcv + ", isActive='" + isActive + '\'' + ", contractType='" + contractType + '\'' + ", createdBy='" + createdBy + '\'' + ", chargeItem='" + chargeItem + '\'' + '}';
	}
}

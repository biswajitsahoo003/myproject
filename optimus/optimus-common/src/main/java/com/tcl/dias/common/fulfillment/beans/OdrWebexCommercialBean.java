package com.tcl.dias.common.fulfillment.beans;

import java.io.Serializable;

/**
 * This file contains the OdrWebexCommercialBean.java class.
 * 
 *
 * @author Selvakumar Palaniandy
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
public class OdrWebexCommercialBean implements Serializable {
	
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

	private Double ciscoUnitListPrice;

	private Double ciscoDiscountPercent;

	private Double ciscoUnitNetPrice;
	
	private String contractType;

	private String endpointManagementType;
	
	private String isActive;
	
	private String shortText;

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
		return "OdrWebexCommercialBean [serviceId=" + serviceId + ", componentName=" + componentName
				+ ", componentDesc=" + componentDesc + ", componentType=" + componentType + ", quantity=" + quantity
				+ ", arc=" + arc + ", unitMrc=" + unitMrc + ", mrc=" + mrc + ", nrc=" + nrc + ", unitNrc=" + unitNrc
				+ ", tcv=" + tcv + ", ciscoUnitListPrice=" + ciscoUnitListPrice + ", ciscoDiscountPercent="
				+ ciscoDiscountPercent + ", ciscoUnitNetPrice=" + ciscoUnitNetPrice + ", isActive=" + isActive + "]";
	}

}

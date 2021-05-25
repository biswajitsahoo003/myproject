package com.tcl.dias.servicefulfillmentutils.beans;

import java.io.Serializable;
import java.util.List;

/**
 * @author Syed Ali.
 * @createdAt 29/01/2021, Friday, 17:24
 */

public class ScTeamsDRServiceCommercialBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer componentId;

	private String componentName;

	private String componentDesc;

	private String componentType;

	private String hsnCode;

	private String vendorName;

	private String rentalMaterialCode;

	private String saleMaterialCode;

	private String serviceNumber;

	private String shortText;

	private Integer quantity;

	private String contractType;

	private String supportType;

	private Double mrc;

	private Double nrc;

	private Double arc;

	private Double tcv;

	private Double effectiveUsage;

	private Double effectiveOverage;

	private List<EndpointMaterialsBean> materialsList;

	public Integer getComponentId() {
		return componentId;
	}

	public void setComponentId(Integer componentId) {
		this.componentId = componentId;
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

	public String getHsnCode() {
		return hsnCode;
	}

	public void setHsnCode(String hsnCode) {
		this.hsnCode = hsnCode;
	}

	public String getRentalMaterialCode() {
		return rentalMaterialCode;
	}

	public void setRentalMaterialCode(String rentalMaterialCode) {
		this.rentalMaterialCode = rentalMaterialCode;
	}

	public String getSaleMaterialCode() {
		return saleMaterialCode;
	}

	public void setSaleMaterialCode(String saleMaterialCode) {
		this.saleMaterialCode = saleMaterialCode;
	}

	public String getServiceNumber() {
		return serviceNumber;
	}

	public void setServiceNumber(String serviceNumber) {
		this.serviceNumber = serviceNumber;
	}

	public String getShortText() {
		return shortText;
	}

	public void setShortText(String shortText) {
		this.shortText = shortText;
	}

	public void setComponentType(String componentType) {
		this.componentType = componentType;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public List<EndpointMaterialsBean> getMaterialsList() {
		return materialsList;
	}

	public void setMaterialsList(List<EndpointMaterialsBean> materialsList) {
		this.materialsList = materialsList;
	}

	public String getContractType() {
		return contractType;
	}

	public void setContractType(String contractType) {
		this.contractType = contractType;
	}

	public String getSupportType() {
		return supportType;
	}

	public void setSupportType(String supportType) {
		this.supportType = supportType;
	}

	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
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

	public Double getArc() {
		return arc;
	}

	public void setArc(Double arc) {
		this.arc = arc;
	}

	public Double getTcv() {
		return tcv;
	}

	public void setTcv(Double tcv) {
		this.tcv = tcv;
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
}

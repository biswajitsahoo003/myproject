package com.tcl.dias.servicefulfillmentutils.beans;

import java.io.Serializable;
import java.util.List;

public class ScWebexServiceCommercialBean implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private String componentName;
	
	private String componentDesc;
	
	private String componentType;
	
	private String hsnCode;
	
	private String rentalMaterialCode;

	private String saleMaterialCode;

	private String serviceNumber;

	private String shortText;
	
	private Integer quantity;
	
	private Double ciscoUnitListPrice;
	
	private Double ciscoDiscntPrct;
	
	private Double ciscoUnitNetPrice;
	
	private String contractType;
	
	private String supportType;
	
	private List<EndpointMaterialsBean> materialsList;

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

	public Double getCiscoUnitListPrice() {
		return ciscoUnitListPrice;
	}

	public void setCiscoUnitListPrice(Double ciscoUnitListPrice) {
		this.ciscoUnitListPrice = ciscoUnitListPrice;
	}

	public Double getCiscoDiscntPrct() {
		return ciscoDiscntPrct;
	}

	public void setCiscoDiscntPrct(Double ciscoDiscntPrct) {
		this.ciscoDiscntPrct = ciscoDiscntPrct;
	}

	public Double getCiscoUnitNetPrice() {
		return ciscoUnitNetPrice;
	}

	public void setCiscoUnitNetPrice(Double ciscoUnitNetPrice) {
		this.ciscoUnitNetPrice = ciscoUnitNetPrice;
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
}

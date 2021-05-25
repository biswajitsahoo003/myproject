package com.tcl.dias.servicefulfillmentutils.beans;

import java.util.ArrayList;
import java.util.List;

public class SolutionPdfBean {

	private String name;
	private String cloudType;
	private String locationId;
	private Double mrc = Double.valueOf(0);
	private Double nrc = Double.valueOf(0);
	private Double arc = Double.valueOf(0);
	private Double ppuRate = Double.valueOf(0);
	private String actionType;
	private Integer quantity;
	private String pricingModel;
	private boolean hasAdditionalStorage;
	private boolean isFromServiceInventory = false;
	private String cloudCode;
	private String parentCloudCode;

	private List<ComponentPdfBean> components = new ArrayList<>();

	public SolutionPdfBean() {
		
	}
	
	public SolutionPdfBean(SolutionPdfBean selectedSolutionPdfBean) {
		this.name = selectedSolutionPdfBean.name;
		this.cloudType = selectedSolutionPdfBean.cloudType;
		this.locationId = selectedSolutionPdfBean.locationId;
		this.mrc = selectedSolutionPdfBean.mrc;
		this.nrc = selectedSolutionPdfBean.nrc;
		this.arc = selectedSolutionPdfBean.arc;
		this.actionType = "New";
		this.quantity = 1;
		this.pricingModel = selectedSolutionPdfBean.pricingModel;
		this.hasAdditionalStorage = selectedSolutionPdfBean.hasAdditionalStorage;
		this.components = selectedSolutionPdfBean.components;
		this.cloudCode = selectedSolutionPdfBean.cloudCode;
		this.parentCloudCode = selectedSolutionPdfBean.parentCloudCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCloudType() {
		return cloudType;
	}

	public void setCloudType(String cloudType) {
		this.cloudType = cloudType;
	}

	public String getLocationId() {
		return locationId;
	}

	public void setLocationId(String locationId) {
		this.locationId = locationId;
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
	
	public Double getPpuRate() {
		return ppuRate;
	}

	public void setPpuRate(Double ppuRate) {
		this.ppuRate = ppuRate;
	}

	public String getActionType() {
		return actionType;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	
	public String getPricingModel() {
		return pricingModel;
	}

	public void setPricingModel(String pricingModel) {
		this.pricingModel = pricingModel;
	}
	
	public boolean isHasAdditionalStorage() {
		return hasAdditionalStorage;
	}

	public void setHasAdditionalStorage(boolean hasAdditionalStorage) {
		this.hasAdditionalStorage = hasAdditionalStorage;
	}

	public List<ComponentPdfBean> getComponents() {
		return components;
	}

	public void setComponents(List<ComponentPdfBean> components) {
		this.components = components;
	}

	public boolean isFromServiceInventory() {
		return isFromServiceInventory;
	}

	public void setFromServiceInventory(boolean isFromServiceInventory) {
		this.isFromServiceInventory = isFromServiceInventory;
	}

	public String getCloudCode() {
		return cloudCode;
	}

	public void setCloudCode(String cloudCode) {
		this.cloudCode = cloudCode;
	}

	public String getParentCloudCode() {
		return parentCloudCode;
	}

	public void setParentCloudCode(String parentCloudCode) {
		this.parentCloudCode = parentCloudCode;
	}

	@Override
		public String toString() {
			return "SolutionPdfBean [name=" + name + ", locationId=" + locationId + ", mrc=" + mrc + ", nrc=" + nrc
					+ ", arc=" + arc + ", ppuRate=" + ppuRate + ", actionType=" + actionType + ", quantity=" + quantity
					+ ", pricingModel=" + pricingModel + ", hasAdditionalStorage=" + hasAdditionalStorage
					+ ", isFromServiceInventory=" + isFromServiceInventory + ", cloudCode=" + cloudCode
					+ ", parentCloudCode=" + parentCloudCode + "]";
		}

}

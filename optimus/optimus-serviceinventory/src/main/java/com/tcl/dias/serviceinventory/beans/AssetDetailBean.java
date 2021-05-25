package com.tcl.dias.serviceinventory.beans;

import java.util.List;
import java.util.Map;

/**
 * Bean class to hold vmdetails and its attributes
 *
 * @author Dimples
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
public class AssetDetailBean{
	
	private Integer assetId;
	private String assetName;
	private String businessUnit;
	private String zone;
	private String cloudCode;
	private String parentCloudCode;
	private List<ComponentBean> componentBeanList;
	private Map<String,String> attrValues;
	
	public Integer getAssetId() {
		return assetId;
	}
	public void setAssetId(Integer assetId) {
		this.assetId = assetId;
	}
	public String getAssetName() {
		return assetName;
	}
	public void setAssetName(String assetName) {
		this.assetName = assetName;
	}
	public String getBusinessUnit() {
		return businessUnit;
	}
	public void setBusinessUnit(String businessUnit) {
		this.businessUnit = businessUnit;
	}
	public String getZone() {
		return zone;
	}
	public void setZone(String zone) {
		this.zone = zone;
	}
	public Map<String, String> getAttrValues() {
		return attrValues;
	}
	public void setAttrValues(Map<String, String> attrValues) {
		this.attrValues = attrValues;
	}
	public List<ComponentBean> getComponentBeanList() {
		return componentBeanList;
	}
	public void setComponentBeanList(List<ComponentBean> componentBeanList) {
		this.componentBeanList = componentBeanList;
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
		
}

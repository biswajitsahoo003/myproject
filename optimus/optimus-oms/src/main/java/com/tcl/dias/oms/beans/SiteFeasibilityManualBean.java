package com.tcl.dias.oms.beans;

import java.util.ArrayList;

/**
 * request Bean for site feasibility response update 
 * @author archchan
 *
 */
public class SiteFeasibilityManualBean {

	private Integer siteId;
	private String responseJson;
	private String feasibilityCheck;
	private String feasibilityMode;
	private String provider;
	private String type;
	private Integer rank;
	private  Integer isSelected;
	private String feasibilityCode;
	private String columnName;
	private Integer siteFeasibilityRowId;
	private ArrayList<LinkJsonManualUpdateBean> listofFields;
	public Integer getSiteId() {
		return siteId;
	}
	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}
	public String getResponseJson() {
		return responseJson;
	}
	public void setResponseJson(String responseJson) {
		this.responseJson = responseJson;
	}
	public String getFeasibilityCheck() {
		return feasibilityCheck;
	}
	public void setFeasibilityCheck(String feasibilityCheck) {
		this.feasibilityCheck = feasibilityCheck;
	}
	public String getFeasibilityMode() {
		return feasibilityMode;
	}
	public void setFeasibilityMode(String feasibilityMode) {
		this.feasibilityMode = feasibilityMode;
	}
	public String getProvider() {
		return provider;
	}
	public void setProvider(String provider) {
		this.provider = provider;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Integer getRank() {
		return rank;
	}
	public void setRank(Integer rank) {
		this.rank = rank;
	}
	public Integer getIsSelected() {
		return isSelected;
	}
	public void setIsSelected(Integer isSelected) {
		this.isSelected = isSelected;
	}
	public String getFeasibilityCode() {
		return feasibilityCode;
	}
	public void setFeasibilityCode(String feasibilityCode) {
		this.feasibilityCode = feasibilityCode;
	}
	public String getColumnName() {
		return columnName;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	public Integer getSiteFeasibilityRowId() {
		return siteFeasibilityRowId;
	}
	public void setSiteFeasibilityRowId(Integer siteFeasibilityRowId) {
		this.siteFeasibilityRowId = siteFeasibilityRowId;
	}
	public ArrayList<LinkJsonManualUpdateBean> getListofFields() {
		return listofFields;
	}
	public void setListofFields(ArrayList<LinkJsonManualUpdateBean> listofFields) {
		this.listofFields = listofFields;
	}
	@Override
	public String toString() {
		return "SiteFeasibilityManualBean [siteId=" + siteId + ", responseJson=" + responseJson + ", feasibilityCheck="
				+ feasibilityCheck + ", feasibilityMode=" + feasibilityMode + ", provider=" + provider + ", type="
				+ type + ", rank=" + rank + ", isSelected=" + isSelected + ", feasibilityCode=" + feasibilityCode
				+ ", columnName=" + columnName + ", siteFeasibilityRowId=" + siteFeasibilityRowId + ", listofFields="
				+ listofFields + "]";
	}
	
}

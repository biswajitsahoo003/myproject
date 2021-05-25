package com.tcl.dias.oms.beans;

import java.util.ArrayList;


/**
 * @author krutsrin
 *Bean to hold all the attrs for manual insert in link_feasibility table in case of prod temp fix scenarios
 */
public class LinkFeasibilityManualBean {
	private Integer linkId;
	private String responseJson;
	private String feasibilityCheck;
	private String feasibilityModeA;
	private String providerA;
	private String type;
	private String feasibilityTypeA;
	private Integer rank;
	private  Integer isSelected;
	private String feasibilityModeB;
	private String providerB;
	private String feasibilityTypeB;
	private String feasibilityCode;
	
	private String columnName;
	private Integer linkFeasibilityRowId;
	private ArrayList<LinkJsonManualUpdateBean> listofFields;
	
	public String getColumnName() {
		return columnName;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	public Integer getLinkFeasibilityRowId() {
		return linkFeasibilityRowId;
	}
	public void setLinkFeasibilityRowId(Integer linkFeasibilityRowId) {
		this.linkFeasibilityRowId = linkFeasibilityRowId;
	}
	public ArrayList<LinkJsonManualUpdateBean> getListofFields() {
		return listofFields;
	}
	public void setListofFields(ArrayList<LinkJsonManualUpdateBean> listofFields) {
		this.listofFields = listofFields;
	}
	public Integer getLinkId() {
		return linkId;
	}
	public void setLinkId(Integer linkId) {
		this.linkId = linkId;
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
	public String getFeasibilityModeA() {
		return feasibilityModeA;
	}
	public void setFeasibilityModeA(String feasibilityModeA) {
		this.feasibilityModeA = feasibilityModeA;
	}
	public String getProviderA() {
		return providerA;
	}
	public void setProviderA(String providerA) {
		this.providerA = providerA;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getFeasibilityTypeA() {
		return feasibilityTypeA;
	}
	public void setFeasibilityTypeA(String feasibilityTypeA) {
		this.feasibilityTypeA = feasibilityTypeA;
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
	public String getFeasibilityModeB() {
		return feasibilityModeB;
	}
	public void setFeasibilityModeB(String feasibilityModeB) {
		this.feasibilityModeB = feasibilityModeB;
	}
	public String getProviderB() {
		return providerB;
	}
	public void setProviderB(String providerB) {
		this.providerB = providerB;
	}
	public String getFeasibilityTypeB() {
		return feasibilityTypeB;
	}
	public void setFeasibilityTypeB(String feasibilityTypeB) {
		this.feasibilityTypeB = feasibilityTypeB;
	}
	public String getFeasibilityCode() {
		return feasibilityCode;
	}
	public void setFeasibilityCode(String feasibilityCode) {
		this.feasibilityCode = feasibilityCode;
	}
	@Override
	public String toString() {
		return "LinkFeasibilityManualBean [linkId=" + linkId + ", responseJson=" + responseJson + ", feasibilityCheck="
				+ feasibilityCheck + ", feasibilityModeA=" + feasibilityModeA + ", providerA=" + providerA + ", type="
				+ type + ", feasibilityTypeA=" + feasibilityTypeA + ", rank=" + rank + ", isSelected=" + isSelected
				+ ", feasibilityModeB=" + feasibilityModeB + ", providerB=" + providerB + ", feasibilityTypeB="
				+ feasibilityTypeB + ", feasibilityCode=" + feasibilityCode + ", columnName=" + columnName
				+ ", linkFeasibilityRowId=" + linkFeasibilityRowId + ", listofFields=" + listofFields + "]";
	}

}

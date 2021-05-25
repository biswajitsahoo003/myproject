package com.tcl.dias.oms.npl.beans;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tcl.dias.oms.entity.entities.OrderLinkFeasibility;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LinkFeasibilityBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	private String feasibilityCheck;

	private String feasibilityMode;

	private String feasibilityCode;

	private Integer rank;

	private String responseJson;

	private String provider;

	private Byte isSelected;

	private String type;

	private Date createdTime;
	
	private String feasibilityModeB;
	
	private String providerB;

	
	

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

	public String getFeasibilityCode() {
		return feasibilityCode;
	}

	public void setFeasibilityCode(String feasibilityCode) {
		this.feasibilityCode = feasibilityCode;
	}

	public Integer getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}

	public String getResponseJson() {
		return responseJson;
	}

	public void setResponseJson(String responseJson) {
		this.responseJson = responseJson;
	}

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public Byte getIsSelected() {
		return isSelected;
	}

	public void setIsSelected(Byte isSelected) {
		this.isSelected = isSelected;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}
	
	public LinkFeasibilityBean() {
		
	}
	
	public LinkFeasibilityBean(OrderLinkFeasibility feas) {
		this.setCreatedTime(feas.getCreatedTime());
		this.setFeasibilityCheck(feas.getFeasibilityCheck());
		this.setFeasibilityCode(feas.getFeasibilityCode());
		this.setFeasibilityMode(feas.getFeasibilityMode());
		this.setIsSelected(feas.getIsSelected());
		this.setProvider(feas.getProvider());
		this.setRank(feas.getRank());
		this.setResponseJson(feas.getResponseJson());
		this.setType(feas.getType());
	}
	
	
}

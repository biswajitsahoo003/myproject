package com.tcl.dias.oms.npl.beans;

import java.io.Serializable;
import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class NplFeasiblityBean implements Serializable {
	private Integer id;

	private Integer linkId;

	private String feasibilityCheck;

	private String feasibilityCode;

	private String feasibilityMode;
	
	private String feasibilityType;

	private Integer rank;

	private String provider;

	private String response;

	private Byte isSelected;

	private String type;
	
	private String aEnd_feasibilityMode;
    private String bEnd_feasibilityMode;
    
	private String aEnd_feasibilityType;
    private String bEnd_feasibilityType;
    
    private String aEnd_provider;
    private String bEnd_provider;
    

	public String getaEnd_provider() {
		return aEnd_provider;
	}

	public void setaEnd_provider(String aEnd_provider) {
		this.aEnd_provider = aEnd_provider;
	}

	public String getbEnd_provider() {
		return bEnd_provider;
	}

	public void setbEnd_provider(String bEnd_provider) {
		this.bEnd_provider = bEnd_provider;
	}

	public String getFeasibilityType() {
		return feasibilityType;
	}

	public void setFeasibilityType(String feasibilityType) {
		this.feasibilityType = feasibilityType;
	}

	public String getaEnd_feasibilityMode() {
		return aEnd_feasibilityMode;
	}

	public void setaEnd_feasibilityMode(String aEnd_feasibilityMode) {
		this.aEnd_feasibilityMode = aEnd_feasibilityMode;
	}

	public String getbEnd_feasibilityMode() {
		return bEnd_feasibilityMode;
	}

	public void setbEnd_feasibilityMode(String bEnd_feasibilityMode) {
		this.bEnd_feasibilityMode = bEnd_feasibilityMode;
	}

	public String getaEnd_feasibilityType() {
		return aEnd_feasibilityType;
	}

	public void setaEnd_feasibilityType(String aEnd_feasibilityType) {
		this.aEnd_feasibilityType = aEnd_feasibilityType;
	}

	public String getbEnd_feasibilityType() {
		return bEnd_feasibilityType;
	}

	public void setbEnd_feasibilityType(String bEnd_feasibilityType) {
		this.bEnd_feasibilityType = bEnd_feasibilityType;
	}

	private Timestamp createdTime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public Integer getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
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

	public Timestamp getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Timestamp createdTime) {
		this.createdTime = createdTime;
	}

	public String getFeasibilityCode() {
		return feasibilityCode;
	}

	public void setFeasibilityCode(String feasibilityCode) {
		this.feasibilityCode = feasibilityCode;
	}

	public Integer getLinkId() {
		return linkId;
	}

	public void setLinkId(Integer linkId) {
		this.linkId = linkId;
	}

}

/**
 * 
 */
package com.tcl.dias.nso.beans;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author KarMani
 *
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class SiteFeasibilityBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3717631897964811254L;

	private String feasibilityCheck;

	private String feasibilityMode;

	private String feasibilityCode;

	private Integer rank;

	private String responseJson;

	private String provider;

	private Byte isSelected;

	private String type;

	private Date createdTime;

	private String feasibilityType;

	private String sfdcFeasibilityId;

	/**
	 * @return the feasibilityCheck
	 */
	public String getFeasibilityCheck() {
		return feasibilityCheck;
	}

	/**
	 * @param feasibilityCheck
	 *            the feasibilityCheck to set
	 */
	public void setFeasibilityCheck(String feasibilityCheck) {
		this.feasibilityCheck = feasibilityCheck;
	}

	/**
	 * @return the feasibilityMode
	 */
	public String getFeasibilityMode() {
		return feasibilityMode;
	}

	/**
	 * @param feasibilityMode
	 *            the feasibilityMode to set
	 */
	public void setFeasibilityMode(String feasibilityMode) {
		this.feasibilityMode = feasibilityMode;
	}

	/**
	 * @return the rank
	 */
	public Integer getRank() {
		return rank;
	}

	/**
	 * @param rank
	 *            the rank to set
	 */
	public void setRank(Integer rank) {
		this.rank = rank;
	}

	/**
	 * @return the responseJson
	 */
	public String getResponseJson() {
		return responseJson;
	}

	/**
	 * @param responseJson
	 *            the responseJson to set
	 */
	public void setResponseJson(String responseJson) {
		this.responseJson = responseJson;
	}

	/**
	 * @return the isSelected
	 */
	public Byte getIsSelected() {
		return isSelected;
	}

	/**
	 * @param isSelected
	 *            the isSelected to set
	 */
	public void setIsSelected(Byte isSelected) {
		this.isSelected = isSelected;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the provider
	 */
	public String getProvider() {
		return provider;
	}

	/**
	 * @param provider
	 *            the provider to set
	 */
	public void setProvider(String provider) {
		this.provider = provider;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public String getFeasibilityCode() {
		return feasibilityCode;
	}

	public void setFeasibilityCode(String feasibilityCode) {
		this.feasibilityCode = feasibilityCode;
	}

	public String getFeasibilityType() {
		return feasibilityType;
	}

	public void setFeasibilityType(String feasibilityType) {
		this.feasibilityType = feasibilityType;
	}

	public String getSfdcFeasibilityId() {
		return sfdcFeasibilityId;
	}

	public void setSfdcFeasibilityId(String sfdcFeasibilityId) {
		this.sfdcFeasibilityId = sfdcFeasibilityId;
	}

}


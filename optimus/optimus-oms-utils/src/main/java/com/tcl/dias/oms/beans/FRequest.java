package com.tcl.dias.oms.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * This file contains the FRequest.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class FRequest {

	private Integer siteFeasibilityId;
	private String feasibilityType;
	private String type;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getSiteFeasibilityId() {
		return siteFeasibilityId;
	}

	public void setSiteFeasibilityId(Integer siteFeasibilityId) {
		this.siteFeasibilityId = siteFeasibilityId;
	}

	public String getFeasibilityType() {
		return feasibilityType;
	}

	public void setFeasibilityType(String feasibilityType) {
		this.feasibilityType = feasibilityType;
	}

	@Override
	public String toString() {
		return "FRequest [siteFeasibilityId=" + siteFeasibilityId + ", feasibilityType=" + feasibilityType + "]";
	}

}

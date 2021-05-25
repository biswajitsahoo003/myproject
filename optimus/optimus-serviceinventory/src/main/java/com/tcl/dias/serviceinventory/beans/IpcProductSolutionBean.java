package com.tcl.dias.serviceinventory.beans;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class IpcProductSolutionBean implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String offeringName;
	private List<IpcSolutionDetail> cloudSolutions;

	public String getOfferingName() {
		return offeringName;
	}

	public void setOfferingName(String offeringName) {
		this.offeringName = offeringName;
	}

	public List<IpcSolutionDetail> getCloudSolutions() {
		return cloudSolutions;
	}

	public void setCloudSolutions(List<IpcSolutionDetail> cloudSolutions) {
		this.cloudSolutions = cloudSolutions;
	}
}

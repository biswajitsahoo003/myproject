package com.tcl.dias.servicefulfillmentutils.beans;

import java.util.List;

public class ProductSolutionBean {
	
	private String offeringName;

	private List<SolutionDetail> cloudSolutions;

	public String getOfferingName() {
		return offeringName;
	}

	public void setOfferingName(String offeringName) {
		this.offeringName = offeringName;
	}

	public List<SolutionDetail> getCloudSolutions() {
		return cloudSolutions;
	}

	public void setCloudSolutions(List<SolutionDetail> cloudSolutions) {
		this.cloudSolutions = cloudSolutions;
	}

	
}

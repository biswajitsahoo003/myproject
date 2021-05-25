
package com.tcl.dias.nso.beans;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Solution {

	private Integer productSolutionId;
	private List<Site> sites;
	private String profilename;
	private SolutionDetail solution;

	public Integer getProductSolutionId() {
		return productSolutionId;
	}

	public void setProductSolutionId(Integer productSolutionId) {
		this.productSolutionId = productSolutionId;
	}

	public List<Site> getSites() {
		return sites;
	}

	public void setSites(List<Site> sites) {
		this.sites = sites;
	}

	public String getProfilename() {
		return profilename;
	}

	public void setProfilename(String profilename) {
		this.profilename = profilename;
	}

	public SolutionDetail getSolution() {
		return solution;
	}

	public void setSolution(SolutionDetail solution) {
		this.solution = solution;
	}

	@Override
	public String toString() {
		return "Solution [productSolutionId=" + productSolutionId + ", sites=" + sites + ", profilename=" + profilename
				+ ", solution=" + solution + "]";
	}

}
package com.tcl.dias.oms.npl.pdf.beans;

import java.util.List;

/**
 * This file contains the NplSolution.java class.
 * 
 *
 * @author Thamizhselvi Perumal
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class NplSolution {

	private String networkProtection;
	private String solutionName;
	private List<NplSolutionLinkDetail> linkDetails;

	public String getNetworkProtection() {
		return networkProtection;
	}

	public void setNetworkProtection(String networkProtection) {
		this.networkProtection = networkProtection;
	}
	
	public String getSolutionName() {
		return solutionName;
	}
	public void setSolutionName(String solutionName) {
		this.solutionName = solutionName;
	}

	public List<NplSolutionLinkDetail> getLinkDetails() {
		return linkDetails;
	}

	public void setLinkDetails(List<NplSolutionLinkDetail> linkDetails) {
		this.linkDetails = linkDetails;
	}

	@Override
	public String toString() {
		return "NplSolution [networkProtection=" + networkProtection + ", solutionName=" + solutionName
				+ ", linkDetails=" + linkDetails + "]";
	}

	
	
	
}

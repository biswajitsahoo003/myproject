package com.tcl.dias.oms.pdf.beans;

import java.util.List;

/**
 * This file contains the IllSolution.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class IllSolution {

	private String serviceVariant;
	private String lastMile;
	private String resilency;
	private String cpe;
	private String portBandwidth;
	private String solutionImage;
	private String solutionName;
	private List<IllSolutionSiteDetail> siteDetails;
	private String isAllSitesColo;
	private String offeringName;
	
	public String getOfferingName() {
		return offeringName;
	}

	public void setOfferingName(String offeringName) {
		this.offeringName = offeringName;
	}

	public String getServiceVariant() {
		return serviceVariant;
	}

	public void setServiceVariant(String serviceVariant) {
		this.serviceVariant = serviceVariant;
	}

	public String getLastMile() {
		return lastMile;
	}

	public void setLastMile(String lastMile) {
		this.lastMile = lastMile;
	}

	public String getResilency() {
		return resilency;
	}

	public void setResilency(String resilency) {
		this.resilency = resilency;
	}

	public String getCpe() {
		return cpe;
	}

	public void setCpe(String cpe) {
		this.cpe = cpe;
	}

	public String getPortBandwidth() {
		return portBandwidth;
	}

	public void setPortBandwidth(String portBandwidth) {
		this.portBandwidth = portBandwidth;
	}

	public String getSolutionImage() {
		return solutionImage;
	}

	public void setSolutionImage(String solutionImage) {
		this.solutionImage = solutionImage;
	}

	public List<IllSolutionSiteDetail> getSiteDetails() {
		return siteDetails;
	}

	public void setSiteDetails(List<IllSolutionSiteDetail> siteDetails) {
		this.siteDetails = siteDetails;
	}

	public String getSolutionName() {
		return solutionName;
	}

	public void setSolutionName(String solutionName) {
		this.solutionName = solutionName;
	}

	public String getIsAllSitesColo() {
		return isAllSitesColo;
	}

	public void setIsAllSitesColo(String isAllSitesColo) {
		this.isAllSitesColo = isAllSitesColo;
	}

	@Override
	public String toString() {
		return "IllSolution{" +
				"serviceVariant='" + serviceVariant + '\'' +
				", lastMile='" + lastMile + '\'' +
				", resilency='" + resilency + '\'' +
				", cpe='" + cpe + '\'' +
				", portBandwidth='" + portBandwidth + '\'' +
				", solutionImage='" + solutionImage + '\'' +
				", solutionName='" + solutionName + '\'' +
				", siteDetails=" + siteDetails +
				", isAllSitesColo='" + isAllSitesColo + '\'' +
				'}';
	}
}

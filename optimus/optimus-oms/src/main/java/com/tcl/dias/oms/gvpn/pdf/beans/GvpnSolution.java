package com.tcl.dias.oms.gvpn.pdf.beans;

import java.util.List;

/**
 * 
 * This file contains the GvpnSolution.java class.
 * 
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class GvpnSolution {

	private String serviceVariant;
	private String lastMile;
	private String resilency;
	private String cpe;
	private String portBandwidth;
	private String solutionImage;
	private String solutionName;
	private List<GvpnSolutionSiteDetail> siteDetails;
	
	//added for mvrf
	private String billingType;
	private String mvrfSolution;
	private String noOfVrf;

	
	public String getBillingType() {
		return billingType;
	}

	public void setBillingType(String billingType) {
		this.billingType = billingType;
	}

	public String getMvrfSolution() {
		return mvrfSolution;
	}

	public void setMvrfSolution(String mvrfSolution) {
		this.mvrfSolution = mvrfSolution;
	}

	public String getNoOfVrf() {
		return noOfVrf;
	}

	public void setNoOfVrf(String noOfVrf) {
		this.noOfVrf = noOfVrf;
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

	public List<GvpnSolutionSiteDetail> getSiteDetails() {
		return siteDetails;
	}

	public void setSiteDetails(List<GvpnSolutionSiteDetail> siteDetails) {
		this.siteDetails = siteDetails;
	}

	public String getSolutionName() {
		return solutionName;
	}

	public void setSolutionName(String solutionName) {
		this.solutionName = solutionName;
	}

	@Override
	public String toString() {
		return "IllSolution [serviceVariant=" + serviceVariant + ", lastMile=" + lastMile + ", resilency=" + resilency
				+ ", cpe=" + cpe + ", portBandwidth=" + portBandwidth + ", solutionImage=" + solutionImage
				+ ", siteDetails=" + siteDetails + "]";
	}

}

package com.tcl.dias.oms.npl.pdf.beans;

/**
 * This file contains the NplComponentDetail.java class.
 * 
 *
 * @author Thamizhselvi Perumal
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class NplComponentDetail {

	private String accessType;
	private String accessProvider;
	private String feasibilityCreatedDate;
	private String validityOfFeasibility;
	private String serviceType;
	private String chargeableDistance;
	private String portBandwidth;
	private String serviceAvailability;
	private String chargeType;
	private NplSpecificAttribute siteAEnd= new NplSpecificAttribute();
	private NplSpecificAttribute siteBEnd= new NplSpecificAttribute();
	private String accessTypeB;
	private String accessProviderB;
	private String hubConnection;
	private String frameSize;
	private String portType;
	private String hubParentId;
	private String parallelRunDays;
	private String parallelBuild;
	private String frameASize;
	private String frameBSize;
	
	
	

	public String getFrameASize() {
		return frameASize;
	}

	public void setFrameASize(String frameASize) {
		this.frameASize = frameASize;
	}

	public String getFrameBSize() {
		return frameBSize;
	}

	public void setFrameBSize(String frameBSize) {
		this.frameBSize = frameBSize;
	}

	public String getHubParentId() {
		return hubParentId;
	}

	public void setHubParentId(String hubParentId) {
		this.hubParentId = hubParentId;
	}

	public String getHubConnection() {
		return hubConnection;
	}

	public void setHubConnection(String hubConnection) {
		this.hubConnection = hubConnection;
	}

	public String getFrameSize() {
		return frameSize;
	}

	public void setFrameSize(String frameSize) {
		this.frameSize = frameSize;
	}

	public String getPortType() {
		return portType;
	}

	public void setPortType(String portType) {
		this.portType = portType;
	}

	public String getAccessTypeB() {
		return accessTypeB;
	}

	public void setAccessTypeB(String accessTypeB) {
		this.accessTypeB = accessTypeB;
	}

	public String getAccessProviderB() {
		return accessProviderB;
	}

	public void setAccessProviderB(String accessProviderB) {
		this.accessProviderB = accessProviderB;
	}

	public String getAccessType() {
		return accessType;
	}

	public void setAccessType(String accessType) {
		this.accessType = accessType;
	}

	public String getAccessProvider() {
		return accessProvider;
	}

	public void setAccessProvider(String accessProvider) {
		this.accessProvider = accessProvider;
	}

	public String getFeasibilityCreatedDate() {
		return feasibilityCreatedDate;
	}

	public void setFeasibilityCreatedDate(String feasibilityCreatedDate) {
		this.feasibilityCreatedDate = feasibilityCreatedDate;
	}

	public String getValidityOfFeasibility() {
		return validityOfFeasibility;
	}

	public void setValidityOfFeasibility(String validityOfFeasibility) {
		this.validityOfFeasibility = validityOfFeasibility;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
	
	public String getChargeableDistance() {
		return chargeableDistance;
	}

	public void setChargeableDistance(String chargeableDistance) {
		this.chargeableDistance = chargeableDistance;
	}
	
	public String getPortBandwidth() {
		return portBandwidth;
	}
	public void setPortBandwidth(String portBandwidth) {
		this.portBandwidth = portBandwidth;
	}
	public String getServiceAvailability() {
		return serviceAvailability;
	}
	public void setServiceAvailability(String serviceAvailability) {
		this.serviceAvailability = serviceAvailability;
	}
	public String getChargeType() {
		return chargeType;
	}
	public void setChargeType(String chargeType) {
		this.chargeType = chargeType;
	}
	
	public NplSpecificAttribute getSiteAEnd() {
		return siteAEnd;
	}

	public void setSiteAEnd(NplSpecificAttribute siteAEnd) {
		this.siteAEnd = siteAEnd;
	}

	public NplSpecificAttribute getSiteBEnd() {
		return siteBEnd;
	}

	public void setSiteBEnd(NplSpecificAttribute siteBEnd) {
		this.siteBEnd = siteBEnd;
	}

	public String getParallelRunDays() {
		return parallelRunDays;
	}

	public void setParallelRunDays(String parallelRunDays) {
		this.parallelRunDays = parallelRunDays;
	}

	public String getParallelBuild() {
		return parallelBuild;
	}

	public void setParallelBuild(String parallelBuild) {
		this.parallelBuild = parallelBuild;
	}

	@Override
	public String toString() {
		return "NplComponentDetail{" +
				"accessType='" + accessType + '\'' +
				", accessProvider='" + accessProvider + '\'' +
				", feasibilityCreatedDate='" + feasibilityCreatedDate + '\'' +
				", validityOfFeasibility='" + validityOfFeasibility + '\'' +
				", serviceType='" + serviceType + '\'' +
				", chargeableDistance='" + chargeableDistance + '\'' +
				", portBandwidth='" + portBandwidth + '\'' +
				", serviceAvailability='" + serviceAvailability + '\'' +
				", chargeType='" + chargeType + '\'' +
				", siteAEnd=" + siteAEnd +
				", siteBEnd=" + siteBEnd +
				", accessTypeB='" + accessTypeB + '\'' +
				", accessProviderB='" + accessProviderB + '\'' +
				", hubConnection='" + hubConnection + '\'' +
				", frameSize='" + frameSize + '\'' +
				", portType='" + portType + '\'' +
				", hubParentId='" + hubParentId + '\'' +
				", parallelRunDays='" + parallelRunDays + '\'' +
				", parallelBuild='" + parallelBuild + '\'' +
				'}';
	}


}

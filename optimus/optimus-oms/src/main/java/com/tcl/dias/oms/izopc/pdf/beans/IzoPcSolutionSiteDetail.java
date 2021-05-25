package com.tcl.dias.oms.izopc.pdf.beans;

/**
 * 
 * Bean class holding site information of IZOPC quote pdf
 * 
 *
 * @author Dinahar V
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class IzoPcSolutionSiteDetail {

	private String siteAddress;
	private String offeringName;
	private String locationImage;
	private String portBandwidth;
	private String expectedDate;
	private IzoPcComponentDetail primaryComponent;
	
	private Boolean isDual=false;
	
	private String primaryBasicComponentList;
	private String primaryAdditionalComponentList;

	public String getSiteAddress() {
		return siteAddress;
	}

	public void setSiteAddress(String siteAddress) {
		this.siteAddress = siteAddress;
	}

	public String getOfferingName() {
		return offeringName;
	}

	public void setOfferingName(String offeringName) {
		this.offeringName = offeringName;
	}

	public String getLocationImage() {
		return locationImage;
	}

	public void setLocationImage(String locationImage) {
		this.locationImage = locationImage;
	}

	public String getPortBandwidth() {
		return portBandwidth;
	}

	public void setPortBandwidth(String portBandwidth) {
		this.portBandwidth = portBandwidth;
	}

	public IzoPcComponentDetail getPrimaryComponent() {
		return primaryComponent;
	}

	public void setPrimaryComponent(IzoPcComponentDetail primaryComponent) {
		this.primaryComponent = primaryComponent;
	}

	public Boolean getIsDual() {
		return isDual;
	}

	public void setIsDual(Boolean isDual) {
		this.isDual = isDual;
	}

	public String getPrimaryBasicComponentList() {
		return primaryBasicComponentList;
	}

	public void setPrimaryBasicComponentList(String primaryBasicComponentList) {
		this.primaryBasicComponentList = primaryBasicComponentList;
	}

	public String getExpectedDate() {
		return expectedDate;
	}

	public void setExpectedDate(String expectedDate) {
		this.expectedDate = expectedDate;
	}

	public String getPrimaryAdditionalComponentList() {
		return primaryAdditionalComponentList;
	}

	public void setPrimaryAdditionalComponentList(String primaryAdditionalComponentList) {
		this.primaryAdditionalComponentList = primaryAdditionalComponentList;
	}
}

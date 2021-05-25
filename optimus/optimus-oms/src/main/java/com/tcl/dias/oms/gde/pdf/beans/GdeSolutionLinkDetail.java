package com.tcl.dias.oms.gde.pdf.beans;

/**
 * Bean class for Gde Solution link 
 * @author archchan
 *
 */
public class GdeSolutionLinkDetail {
    private String siteAAddress;
    private String siteBAddress;
    private String offeringName;
    private String solutionImage;
    private String locationImage;
    private String portBandwidth;
    private String feasibility;
    private GdeComponentDetail primaryComponent;

    public String getSiteAAddress() {
        return siteAAddress;
    }

    public void setSiteAAddress(String siteAAddress) {
        this.siteAAddress = siteAAddress;
    }

    public String getSiteBAddress() {
        return siteBAddress;
    }

    public void setSiteBAddress(String siteBAddress) {
        this.siteBAddress = siteBAddress;
    }

    public String getOfferingName() {
        return offeringName;
    }

    public void setOfferingName(String offeringName) {
        this.offeringName = offeringName;
    }

    public String getSolutionImage() {
        return solutionImage;
    }

    public void setSolutionImage(String solutionImage) {
        this.solutionImage = solutionImage;
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

    public String getFeasibility() {
        return feasibility;
    }

    public void setFeasibility(String feasibility) {
        this.feasibility = feasibility;
    }

	public GdeComponentDetail getPrimaryComponent() {
		return primaryComponent;
	}

	public void setPrimaryComponent(GdeComponentDetail primaryComponent) {
		this.primaryComponent = primaryComponent;
	}



}

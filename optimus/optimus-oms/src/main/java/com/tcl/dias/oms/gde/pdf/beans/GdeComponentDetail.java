package com.tcl.dias.oms.gde.pdf.beans;

import com.tcl.dias.oms.gde.pdf.beans.GdeBodSpecificAttribute;

public class GdeComponentDetail {
	
    private String feasibilityCreatedDate;
    private String validityOfFeasibility;
    private String serviceType;
    private String siteFeasibility;
//    private GdeSpecificAttribute siteAEnd= new GdeSpecificAttribute();
//    private GdeSpecificAttribute siteBEnd= new GdeSpecificAttribute();
    private GdeBodSpecificAttribute configurationAttributes = new GdeBodSpecificAttribute();

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
    
    public String getSiteFeasibility() {
		return siteFeasibility;
	}

	public void setSiteFeasibility(String siteFeasibility) {
		this.siteFeasibility = siteFeasibility;
	}

	public GdeBodSpecificAttribute getConfigurationAttributes() {
		return configurationAttributes;
	}

	public void setConfigurationAttributes(GdeBodSpecificAttribute configurationAttributes) {
		this.configurationAttributes = configurationAttributes;
	}
	
	
}


/**
 * 
 */
package com.tcl.dias.common.beans;

/**
 * This method for npl-nde download report
 * @author 4016226-Sobhan
 *
 */
public class NplNdeMultiSiteInputs {

	AddressDetailsMultiSite addressDetailsMultiSite;
	ExistingMacdComponents existingMacdComponents;
	MultiSiteBasicAttributes multiSiteBasicAttributes;
	MultiSiteFeasibility multiSiteFeasibility;
	MultiSiteCpeAttributes multiSiteCpeAttributes;
	MultiSitePricingAttributes multiSitePricingAttributes;
	private Boolean macdFlag = false;
	private String commercialManagerComments;
	private String siteLevelAction;
	
	public AddressDetailsMultiSite getAddressDetailsMultiSite() {
		return addressDetailsMultiSite;
	}


	public void setAddressDetailsMultiSite(AddressDetailsMultiSite addressDetailsMultiSite) {
		this.addressDetailsMultiSite = addressDetailsMultiSite;
	}


	public ExistingMacdComponents getExistingMacdComponents() {
		return existingMacdComponents;
	}


	public void setExistingMacdComponents(ExistingMacdComponents existingMacdComponents) {
		this.existingMacdComponents = existingMacdComponents;
	}


	public MultiSiteBasicAttributes getMultiSiteBasicAttributes() {
		return multiSiteBasicAttributes;
	}


	public void setMultiSiteBasicAttributes(MultiSiteBasicAttributes multiSiteBasicAttributes) {
		this.multiSiteBasicAttributes = multiSiteBasicAttributes;
	}


	public MultiSiteFeasibility getMultiSiteFeasibility() {
		return multiSiteFeasibility;
	}


	public void setMultiSiteFeasibility(MultiSiteFeasibility multiSiteFeasibility) {
		this.multiSiteFeasibility = multiSiteFeasibility;
	}


	public MultiSiteCpeAttributes getMultiSiteCpeAttributes() {
		return multiSiteCpeAttributes;
	}


	public void setMultiSiteCpeAttributes(MultiSiteCpeAttributes multiSiteCpeAttributes) {
		this.multiSiteCpeAttributes = multiSiteCpeAttributes;
	}


	public MultiSitePricingAttributes getMultiSitePricingAttributes() {
		return multiSitePricingAttributes;
	}


	public void setMultiSitePricingAttributes(MultiSitePricingAttributes multiSitePricingAttributes) {
		this.multiSitePricingAttributes = multiSitePricingAttributes;
	}


	public Boolean getMacdFlag() {
		return macdFlag;
	}


	public void setMacdFlag(Boolean macdFlag) {
		this.macdFlag = macdFlag;
	}


	public String getCommercialManagerComments() {
		return commercialManagerComments;
	}


	public void setCommercialManagerComments(String commercialManagerComments) {
		this.commercialManagerComments = commercialManagerComments;
	}


	public String getSiteLevelAction() {
		return siteLevelAction;
	}


	public void setSiteLevelAction(String siteLevelAction) {
		this.siteLevelAction = siteLevelAction;
	}

}

package com.tcl.dias.common.beans;

public class IllGvpnMultiSiteInputs {

private AddressDetailsMultiSite addressDetailsMultiSite;
	
	private ExistingMacdComponents existingMacdComponents;
	
	private MultiSiteBasicAttributes multiSiteBasicAttributes;
	
	private MultiSiteFeasibility multiSiteFeasibility;
	
	private MultiSiteCpeAttributes multiSiteCpeAttributes;
	
	private MultiSitePricingAttributes multiSitePricingAttributes;
	
	private Boolean macdFlag = false;
	

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

	

	

}

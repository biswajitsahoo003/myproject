package com.tcl.dias.common.beans;

import java.io.Serializable;

public class MfDetailAttributes implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String quoteType;
	private String quoteCategory;
	private String macdServiceId;
	private Double localLoopBandwidth;
	private Double portCapacity;
	private String opportunityStage;
	private String opportunityAccountName;
	private String customerSegment;
	private String addressLineOne;
	private String addressLineTwo;
	private String city;
	private String state;
	private String latLong;
	private String pincode;
	private String country;
	private String lastMileContractTerm;
	private String lconName;
	private String lconContactNum;
	private String lconSalesRemarks;
	private String mfInterface;
	private String localLoopInterface;
	private Integer locationId;
	private String oppurtunityAccountEmail;
	private String quoteStage;
	private String locality;
	private String parallelRunDays;
	private String parallelBuild;
	private String changeBandwidthFlag;
	private String customerCode;
	private String secondarylastMileProvider;
	private String primarylastMileProvider;
	private String bandwidth;
	private String customerName;
	private String secondaryAccessType;
	private String primaryAccessType;
	 // For NPL
    private String mfLinkEndType;
    private String mfLinkType;
    private Integer linkId;

    private String addressLineOneSiteA;
	private String addressLineTwoSiteA;
	private String citySiteA;
	private String stateSiteA;
	private String latLongSiteA;
	private String pincodeSiteA;
	private String countrySiteA;
	private String localitySiteA;
	
    private String addressLineOneSiteB;
	private String addressLineTwoSiteB;
	private String citySiteB;
	private String stateSiteB;
	private String latLongSiteB;
	private String pincodeSiteB;
	private String countrySiteB;
	private String localitySiteB;
	private String aEndPredictedAcessFeasibility;
	private String bEndPredictedAcessFeasibility;
	
	// for NPL
	private String aEndlocalLoopInterface;
	private String bEndlocalLoopInterface;
	
	private String aEndLocalLoopBandwidth;
	private String bEndLocalLoopBandwidth;
	private String siteGettingShifted;
	private String aEndLMProvider;
	private String bEndLMProvider;
	private boolean retriggerTaskForFeasibleSites;

	private String serviceFlavor;
	private String localLoopType;

	// PIPF -55
	public boolean isRetriggerTaskForFeasibleSites() {
		return retriggerTaskForFeasibleSites;
	}

	public void setRetriggerTaskForFeasibleSites(boolean retriggerTaskForFeasibleSites) {
		this.retriggerTaskForFeasibleSites = retriggerTaskForFeasibleSites;
	}
	
	
	public String getaEndLMProvider() {
		return aEndLMProvider;
	}
	public void setaEndLMProvider(String aEndLMProvider) {
		this.aEndLMProvider = aEndLMProvider;
	}
	public String getbEndLMProvider() {
		return bEndLMProvider;
	}
	public void setbEndLMProvider(String bEndLMProvider) {
		this.bEndLMProvider = bEndLMProvider;
	}
	public String getSiteGettingShifted() {
		return siteGettingShifted;
	}
	public void setSiteGettingShifted(String siteGettingShifted) {
		this.siteGettingShifted = siteGettingShifted;
	}
	public String getaEndLocalLoopBandwidth() {
		return aEndLocalLoopBandwidth;
	}
	public void setaEndLocalLoopBandwidth(String aEndLocalLoopBandwidth) {
		this.aEndLocalLoopBandwidth = aEndLocalLoopBandwidth;
	}
	public String getbEndLocalLoopBandwidth() {
		return bEndLocalLoopBandwidth;
	}
	public void setbEndLocalLoopBandwidth(String bEndLocalLoopBandwidth) {
		this.bEndLocalLoopBandwidth = bEndLocalLoopBandwidth;
	}
	public String getaEndlocalLoopInterface() {
		return aEndlocalLoopInterface;
	}
	public void setaEndlocalLoopInterface(String aEndlocalLoopInterface) {
		this.aEndlocalLoopInterface = aEndlocalLoopInterface;
	}
	public String getbEndlocalLoopInterface() {
		return bEndlocalLoopInterface;
	}
	public void setbEndlocalLoopInterface(String bEndlocalLoopInterface) {
		this.bEndlocalLoopInterface = bEndlocalLoopInterface;
	}
	public String getLocalitySiteA() {
		return localitySiteA;
	}
	public void setLocalitySiteA(String localitySiteA) {
		this.localitySiteA = localitySiteA;
	}
	public String getLocalitySiteB() {
		return localitySiteB;
	}
	public void setLocalitySiteB(String localitySiteB) {
		this.localitySiteB = localitySiteB;
	}
	
	
	public String getaEndPredictedAcessFeasibility() {
		return aEndPredictedAcessFeasibility;
	}
	public void setaEndPredictedAcessFeasibility(String aEndPredictedAcessFeasibility) {
		this.aEndPredictedAcessFeasibility = aEndPredictedAcessFeasibility;
	}
	public String getbEndPredictedAcessFeasibility() {
		return bEndPredictedAcessFeasibility;
	}
	public void setbEndPredictedAcessFeasibility(String bEndPredictedAcessFeasibility) {
		this.bEndPredictedAcessFeasibility = bEndPredictedAcessFeasibility;
	}
	public String getAddressLineOneSiteA() {
		return addressLineOneSiteA;
	}
	public void setAddressLineOneSiteA(String addressLineOneSiteA) {
		this.addressLineOneSiteA = addressLineOneSiteA;
	}
	public String getAddressLineTwoSiteA() {
		return addressLineTwoSiteA;
	}
	public void setAddressLineTwoSiteA(String addressLineTwoSiteA) {
		this.addressLineTwoSiteA = addressLineTwoSiteA;
	}
	public String getCitySiteA() {
		return citySiteA;
	}
	public void setCitySiteA(String citySiteA) {
		this.citySiteA = citySiteA;
	}
	public String getStateSiteA() {
		return stateSiteA;
	}
	public void setStateSiteA(String stateSiteA) {
		this.stateSiteA = stateSiteA;
	}
	public String getLatLongSiteA() {
		return latLongSiteA;
	}
	public void setLatLongSiteA(String latLongSiteA) {
		this.latLongSiteA = latLongSiteA;
	}
	public String getPincodeSiteA() {
		return pincodeSiteA;
	}
	public void setPincodeSiteA(String pincodeSiteA) {
		this.pincodeSiteA = pincodeSiteA;
	}
	public String getCountrySiteA() {
		return countrySiteA;
	}
	public void setCountrySiteA(String countrySiteA) {
		this.countrySiteA = countrySiteA;
	}
	public String getAddressLineOneSiteB() {
		return addressLineOneSiteB;
	}
	public void setAddressLineOneSiteB(String addressLineOneSiteB) {
		this.addressLineOneSiteB = addressLineOneSiteB;
	}
	public String getAddressLineTwoSiteB() {
		return addressLineTwoSiteB;
	}
	public void setAddressLineTwoSiteB(String addressLineTwoSiteB) {
		this.addressLineTwoSiteB = addressLineTwoSiteB;
	}
	public String getCitySiteB() {
		return citySiteB;
	}
	public void setCitySiteB(String citySiteB) {
		this.citySiteB = citySiteB;
	}
	public String getStateSiteB() {
		return stateSiteB;
	}
	public void setStateSiteB(String stateSiteB) {
		this.stateSiteB = stateSiteB;
	}
	public String getLatLongSiteB() {
		return latLongSiteB;
	}
	public void setLatLongSiteB(String latLongSiteB) {
		this.latLongSiteB = latLongSiteB;
	}
	public String getPincodeSiteB() {
		return pincodeSiteB;
	}
	public void setPincodeSiteB(String pincodeSiteB) {
		this.pincodeSiteB = pincodeSiteB;
	}
	public String getCountrySiteB() {
		return countrySiteB;
	}
	public void setCountrySiteB(String countrySiteB) {
		this.countrySiteB = countrySiteB;
	}
	public Integer getLinkId() {
		return linkId;
	}
	public void setLinkId(Integer linkId) {
		this.linkId = linkId;
	}
	public String getMfLinkEndType() {
		return mfLinkEndType;
	}
	public void setMfLinkEndType(String mfLinkEndType) {
		this.mfLinkEndType = mfLinkEndType;
	}
	public String getMfLinkType() {
		return mfLinkType;
	}
	public void setMfLinkType(String mfLinkType) {
		this.mfLinkType = mfLinkType;
	}
	
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getBandwidth() {
		return bandwidth;
	}
	public void setBandwidth(String bandwidth) {
		this.bandwidth = bandwidth;
	}
	public String getSecondarylastMileProvider() {
		return secondarylastMileProvider;
	}
	public void setSecondarylastMileProvider(String secondarylastMileProvider) {
		this.secondarylastMileProvider = secondarylastMileProvider;
	}
	public String getPrimarylastMileProvider() {
		return primarylastMileProvider;
	}
	public void setPrimarylastMileProvider(String primarylastMileProvider) {
		this.primarylastMileProvider = primarylastMileProvider;
	}
	public String getCustomerCode() {
		return customerCode;
	}
	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}
	public String getChangeBandwidthFlag() {
		return changeBandwidthFlag;
	}
	public void setChangeBandwidthFlag(String changeBandwidthFlag) {
		this.changeBandwidthFlag = changeBandwidthFlag;
	}
	public String getParallelBuild() {
		return parallelBuild;
	}
	public void setParallelBuild(String parallelBuild) {
		this.parallelBuild = parallelBuild;
	}
	public String getParallelRunDays() {
		return parallelRunDays;
	}
	public void setParallelRunDays(String parallelRunDays) {
		this.parallelRunDays = parallelRunDays;
	}
	
	public String getOppurtunityAccountEmail() {
		return oppurtunityAccountEmail;
	}
	public void setOppurtunityAccountEmail(String oppurtunityAccountEmail) {
		this.oppurtunityAccountEmail = oppurtunityAccountEmail;
	}
	public String getMfInterface() {
		return mfInterface;
	}
	public void setMfInterface(String mfInterface) {
		this.mfInterface = mfInterface;
	}
	public String getLconName() {
		return lconName;
	}
	public void setLconName(String lconName) {
		this.lconName = lconName;
	}
	public String getLconContactNum() {
		return lconContactNum;
	}
	public void setLconContactNum(String lconContactNum) {
		this.lconContactNum = lconContactNum;
	}
	public String getLconSalesRemarks() {
		return lconSalesRemarks;
	}
	public void setLconSalesRemarks(String lconSalesRemarks) {
		this.lconSalesRemarks = lconSalesRemarks;
	}
	public String getLastMileContractTerm() {
		return lastMileContractTerm;
	}
	public void setLastMileContractTerm(String lastMileContractTerm) {
		this.lastMileContractTerm = lastMileContractTerm;
	}
	public String getAddressLineOne() {
		return addressLineOne;
	}
	public void setAddressLineOne(String addressLineOne) {
		this.addressLineOne = addressLineOne;
	}
	public String getAddressLineTwo() {
		return addressLineTwo;
	}
	public void setAddressLineTwo(String addressLineTwo) {
		this.addressLineTwo = addressLineTwo;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getLatLong() {
		return latLong;
	}
	public void setLatLong(String latLong) {
		this.latLong = latLong;
	}
	public String getPincode() {
		return pincode;
	}
	public void setPincode(String pincode) {
		this.pincode = pincode;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getQuoteType() {
		return quoteType;
	}
	public void setQuoteType(String quoteType) {
		this.quoteType = quoteType;
	}
	public String getQuoteCategory() {
		return quoteCategory;
	}
	public void setQuoteCategory(String quoteCategory) {
		this.quoteCategory = quoteCategory;
	}
	public String getMacdServiceId() {
		return macdServiceId;
	}
	public void setMacdServiceId(String macdServiceId) {
		this.macdServiceId = macdServiceId;
	}
	public Double getLocalLoopBandwidth() {
		return localLoopBandwidth;
	}
	public void setLocalLoopBandwidth(Double localLoopBandwidth) {
		this.localLoopBandwidth = localLoopBandwidth;
	}
	public Double getPortCapacity() {
		return portCapacity;
	}
	public void setPortCapacity(Double portCapacity) {
		this.portCapacity = portCapacity;
	}
	public String getOpportunityStage() {
		return opportunityStage;
	}
	public void setOpportunityStage(String opportunityStage) {
		this.opportunityStage = opportunityStage;
	}
	public String getOpportunityAccountName() {
		return opportunityAccountName;
	}
	public void setOpportunityAccountName(String opportunityAccountName) {
		this.opportunityAccountName = opportunityAccountName;
	}
	public String getCustomerSegment() {
		return customerSegment;
	}
	public void setCustomerSegment(String customerSegment) {
		this.customerSegment = customerSegment;
	}
	public String getLocalLoopInterface() {
		return localLoopInterface;
	}
	public void setLocalLoopInterface(String localLoopInterface) {
		this.localLoopInterface = localLoopInterface;
	}
	public Integer getLocationId() {
		return locationId;
	}
	public void setLocationId(Integer locationId) {
		this.locationId = locationId;
	}
	public String getQuoteStage() {
		return quoteStage;
	}
	public void setQuoteStage(String quoteStage) {
		this.quoteStage = quoteStage;
	}
	public String getLocality() {
		return locality;
	}
	public void setLocality(String locality) {
		this.locality = locality;
	}

	public String getSecondaryAccessType() {
		return secondaryAccessType;
	}
	public void setSecondaryAccessType(String secondaryAccessType) {
		this.secondaryAccessType = secondaryAccessType;
	}
	public String getPrimaryAccessType() {
		return primaryAccessType;
	}
	public void setPrimaryAccessType(String primaryAccessType) {
		this.primaryAccessType = primaryAccessType;
	}

	public String getServiceFlavor() {
		return serviceFlavor;
	}

	public void setServiceFlavor(String serviceFlavor) {
		this.serviceFlavor = serviceFlavor;
	}

	public String getLocalLoopType() {
		return localLoopType;
	}

	public void setLocalLoopType(String localLoopType) {
		this.localLoopType = localLoopType;
	}
	
}

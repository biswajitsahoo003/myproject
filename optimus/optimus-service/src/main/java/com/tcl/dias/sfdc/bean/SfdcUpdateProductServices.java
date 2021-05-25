
package com.tcl.dias.sfdc.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * This file contains the SfdcProductServices.java class. used to connect with
 * sdfc
 * 
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({ "Product_Type__c", "CurrencyIsoCode", "Product_Line_of_Business__c", "Type__c",
		"Do_you_need_training_for_this_Product__c", "IDC_Bandwidth__c", "IDC_Location__c", "IDC_Floor__c",
		"Product_MRC__c", "Product_NRC__c", "Multi_VRF_Solution__c", "Order_Type__c", "Big_Machines_MRC__c",
		"Big_Machines_ARC__c", "Big_Machines_NRC__c", "Big_Machines_TCV__c",

		/* for gvpn */"Cloud_Enablement__c", "Term_Months__c", "of_Ports__c",

		/* for npl */

		"Quantity__c", "Interface__c", "A_End_Country__c", "B_End_Country__c", "A_End_City__c", "B_End_City__c",
		 /* for gsc*/
		"Data_or_Mobile__c","POC_Attached__c","Interconnect_Type__c","Enabled_for_Unified_Access__c","Product_Category__c","Call_Type__c","Primary_Features__c",
		"LNS__c",
		"ITFS__c",
		"UIFN__c",
		"Audio_Conferencing_Access_No_Service__c",
		"Audio_Conferencing_DTF_Service__c",
		
		/* For IZOPC*/
		
		"Cloud_Provider__c",

		/* for NPL BM Transition*/
		"Sub_Type__c",
		"Bandwidth_Circuit_Speed__c",
		"Media_Type__c"

})
public class SfdcUpdateProductServices extends BaseBean {

	@JsonProperty("Product_Type__c")
	private String productTypeC;
	@JsonProperty("Id")
	private String id;
	@JsonProperty("CurrencyIsoCode")
	private String currencyIsoCode;
	@JsonProperty("Product_Line_of_Business__c")
	private String productLineOfBusinessC;
	@JsonProperty("Type__c")
	private String typeC;
	@JsonProperty("Name")
	private String name;
	@JsonProperty("Do_you_need_training_for_this_Product__c")
	private String doYouNeedTrainingForThisProductC;
	@JsonProperty("IDC_Bandwidth__c")
	private String iDCBandwidthC;
	@JsonProperty("IDC_Location__c")
	private String iDCLocationC;
	@JsonProperty("IDC_Floor__c")
	private String iDCFloorC;
	@JsonProperty("Product_MRC__c")
	private String productMRCC;
	@JsonProperty("Product_NRC__c")
	private String productNRCC;
	@JsonProperty("Multi_VRF_Solution__c")
	private String multiVRFSolutionC;
	@JsonProperty("Order_Type__c")
	private String orderTypeC;
	@JsonProperty("Big_Machines_MRC__c")
	private String bigMachinesMRCC;
	@JsonProperty("Big_Machines_ARC__c")
	private String bigMachinesARCC;
	@JsonProperty("Big_Machines_NRC__c")
	private String bigMachinesNRCC;
	@JsonProperty("Big_Machines_TCV__c")
	private String bigMachinesTCVC;
	
	@JsonProperty("Previous_MRC__c")
	private String previousMrc;
	@JsonProperty("Product_NRC__c")
	private String previousNrc;
	

	// gvn specific

	@JsonProperty("Cloud_Enablement__c")
	private String CloudEnablementC;

	// gvn specific

	@JsonProperty("Term_Months__c")
	private String TermMonthsC;

	// gvn specific
	@JsonProperty("of_Ports__c")
	private String ofPortsC;

	// for npl
	@JsonProperty("Interface__c")
	private String interfaceC;

	// for npl

	@JsonProperty("Quantity__c")
	private String quantityC;

	@JsonProperty("A_End_Country__c")
	private String aEndCountryC;

	@JsonProperty("B_End_Country__c")
	private String bEndCountryC;

	@JsonProperty("A_End_City__c")
	private String aEndCityC;

	@JsonProperty("B_End_City__c")
	private String bEndCityC;
	
	
	//gsc specific

  	@JsonProperty("Opportunity_Name__c")
	private String opportunityNameC;
	@JsonProperty("Data_or_Mobile__c")
	private String dataOrMobileC;
	@JsonProperty("POC_Attached__c")
	private String pOCAttachedC;
	@JsonProperty("Interconnect_Type__c")
	private String interconnectTypeC;
	@JsonProperty("Enabled_for_Unified_Access__c")
	private String enabledForUnifiedAccessC;
	@JsonProperty("Product_Category__c")
	private String productCategoryC;
	@JsonProperty("Call_Type__c")
	private String callTypeC;
	@JsonProperty("Primary_Features__c")
	private String primaryFeaturesC;
	@JsonProperty("LNS__c")
	private String lnsC;
	@JsonProperty("ITFS__c")
	private String itfsC;
	@JsonProperty("UIFN__c")
	private String uifnC;
	@JsonProperty("Audio_Conferencing_Access_No_Service__c")
	private String audioConferencingAccessNoServiceC;
	@JsonProperty("Audio_Conferencing_DTF_Service__c")
	private String audioConferencingDTFServiceC;

	//for npl BM transition specific
	@JsonProperty("Sub_Type__c")
	private String subTypeC;

	@JsonProperty("Bandwidth_Circuit_Speed__c")
	private String bandwidthCircuitSpeed;


	@JsonProperty("Media_Type__c")
	private String mediaType;

	@JsonProperty("of_Fiber__c")
	private String ofFiber;

	@JsonProperty("Type_of_Fiber_Entry__c")
	private String typeOfFiberEntry;


	//IZOPC specific
	@JsonProperty("Cloud_Provider__c")
	private String cloudProviderC;

	@JsonProperty("A_End_Country__c")
	public String getaEndCountryC() {
		return aEndCountryC;
	}

	@JsonProperty("A_End_Country__c")
	public void setaEndCountryC(String aEndCountryC) {
		this.aEndCountryC = aEndCountryC;
	}

	@JsonProperty("B_End_Country__c")
	public String getbEndCountryC() {
		return bEndCountryC;
	}

	@JsonProperty("B_End_Country__c")
	public void setbEndCountryC(String bEndCountryC) {
		this.bEndCountryC = bEndCountryC;
	}

	@JsonProperty("A_End_City__c")
	public String getaEndCityC() {
		return aEndCityC;
	}

	@JsonProperty("A_End_City__c")
	public void setaEndCityC(String aEndCityC) {
		this.aEndCityC = aEndCityC;
	}

	@JsonProperty("B_End_City__c")
	public String getbEndCityC() {
		return bEndCityC;
	}

	@JsonProperty("B_End_City__c")
	public void setbEndCityC(String bEndCityC) {
		this.bEndCityC = bEndCityC;
	}

	@JsonProperty("Product_Type__c")
	public String getProductTypeC() {
		return productTypeC;
	}

	@JsonProperty("Product_Type__c")
	public void setProductTypeC(String productTypeC) {
		this.productTypeC = productTypeC;
	}

	@JsonProperty("CurrencyIsoCode")
	public String getCurrencyIsoCode() {
		return currencyIsoCode;
	}

	@JsonProperty("CurrencyIsoCode")
	public void setCurrencyIsoCode(String currencyIsoCode) {
		this.currencyIsoCode = currencyIsoCode;
	}

	@JsonProperty("Product_Line_of_Business__c")
	public String getProductLineOfBusinessC() {
		return productLineOfBusinessC;
	}

	@JsonProperty("Product_Line_of_Business__c")
	public void setProductLineOfBusinessC(String productLineOfBusinessC) {
		this.productLineOfBusinessC = productLineOfBusinessC;
	}

	@JsonProperty("Type__c")
	public String getTypeC() {
		return typeC;
	}

	@JsonProperty("Type__c")
	public void setTypeC(String typeC) {
		this.typeC = typeC;
	}

	@JsonProperty("Do_you_need_training_for_this_Product__c")
	public String getDoYouNeedTrainingForThisProductC() {
		return doYouNeedTrainingForThisProductC;
	}

	@JsonProperty("Do_you_need_training_for_this_Product__c")
	public void setDoYouNeedTrainingForThisProductC(String doYouNeedTrainingForThisProductC) {
		this.doYouNeedTrainingForThisProductC = doYouNeedTrainingForThisProductC;
	}

	@JsonProperty("IDC_Bandwidth__c")
	public String getIDCBandwidthC() {
		return iDCBandwidthC;
	}

	@JsonProperty("IDC_Bandwidth__c")
	public void setIDCBandwidthC(String iDCBandwidthC) {
		this.iDCBandwidthC = iDCBandwidthC;
	}

	@JsonProperty("IDC_Location__c")
	public String getIDCLocationC() {
		return iDCLocationC;
	}

	@JsonProperty("IDC_Location__c")
	public void setIDCLocationC(String iDCLocationC) {
		this.iDCLocationC = iDCLocationC;
	}

	@JsonProperty("IDC_Floor__c")
	public String getIDCFloorC() {
		return iDCFloorC;
	}

	@JsonProperty("IDC_Floor__c")
	public void setIDCFloorC(String iDCFloorC) {
		this.iDCFloorC = iDCFloorC;
	}

	@JsonProperty("Product_MRC__c")
	public String getProductMRCC() {
		return productMRCC;
	}

	@JsonProperty("Product_MRC__c")
	public void setProductMRCC(String productMRCC) {
		this.productMRCC = productMRCC;
	}

	@JsonProperty("Product_NRC__c")
	public String getProductNRCC() {
		return productNRCC;
	}

	@JsonProperty("Product_NRC__c")
	public void setProductNRCC(String productNRCC) {
		this.productNRCC = productNRCC;
	}

	@JsonProperty("Multi_VRF_Solution__c")
	public String getMultiVRFSolutionC() {
		return multiVRFSolutionC;
	}

	@JsonProperty("Multi_VRF_Solution__c")
	public void setMultiVRFSolutionC(String multiVRFSolutionC) {
		this.multiVRFSolutionC = multiVRFSolutionC;
	}

	@JsonProperty("Order_Type__c")
	public String getOrderTypeC() {
		return orderTypeC;
	}

	@JsonProperty("Order_Type__c")
	public void setOrderTypeC(String orderTypeC) {
		this.orderTypeC = orderTypeC;
	}

	@JsonProperty("Big_Machines_MRC__c")
	public String getBigMachinesMRCC() {
		return bigMachinesMRCC;
	}

	@JsonProperty("Big_Machines_MRC__c")
	public void setBigMachinesMRCC(String bigMachinesMRCC) {
		this.bigMachinesMRCC = bigMachinesMRCC;
	}

	@JsonProperty("Big_Machines_ARC__c")
	public String getBigMachinesARCC() {
		return bigMachinesARCC;
	}

	@JsonProperty("Big_Machines_ARC__c")
	public void setBigMachinesARCC(String bigMachinesARCC) {
		this.bigMachinesARCC = bigMachinesARCC;
	}

	@JsonProperty("Big_Machines_NRC__c")
	public String getBigMachinesNRCC() {
		return bigMachinesNRCC;
	}

	@JsonProperty("Big_Machines_NRC__c")
	public void setBigMachinesNRCC(String bigMachinesNRCC) {
		this.bigMachinesNRCC = bigMachinesNRCC;
	}

	@JsonProperty("Big_Machines_TCV__c")
	public String getBigMachinesTCVC() {
		return bigMachinesTCVC;
	}

	@JsonProperty("Big_Machines_TCV__c")
	public void setBigMachinesTCVC(String bigMachinesTCVC) {
		this.bigMachinesTCVC = bigMachinesTCVC;
	}

	@JsonProperty("Name")
	public String getName() {
		return name;
	}

	@JsonProperty("Name")
	public void setName(String name) {
		this.name = name;
	}

	@JsonProperty("Id")
	public String getId() {
		return id;
	}

	@JsonProperty("Id")
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the cloudEnablementC
	 */
	@JsonProperty("Cloud_Enablement__c")
	public String getCloudEnablementC() {
		return CloudEnablementC;
	}

	/**
	 * @param cloudEnablementC the cloudEnablementC to set
	 */
	@JsonProperty("Cloud_Enablement__c")
	public void setCloudEnablementC(String cloudEnablementC) {
		CloudEnablementC = cloudEnablementC;
	}

	/**
	 * @return the termMonthsC
	 */
	@JsonProperty("Term_Months__c")

	public String getTermMonthsC() {
		return TermMonthsC;
	}

	/**
	 * @param termMonthsC the termMonthsC to set
	 */
	@JsonProperty("Term_Months__c")

	public void setTermMonthsC(String termMonthsC) {
		TermMonthsC = termMonthsC;
	}

	/**
	 * @return the ofPortsC
	 */
	@JsonProperty("of_Ports__c")
	public String getOfPortsC() {
		return ofPortsC;
	}

	/**
	 * @param ofPortsC the ofPortsC to set
	 */
	@JsonProperty("of_Ports__c")
	public void setOfPortsC(String ofPortsC) {
		this.ofPortsC = ofPortsC;
	}

	/**
	 * @return the interfaceC
	 */
	@JsonProperty("Interface__c")
	public String getInterfaceC() {
		return interfaceC;
	}

	/**
	 * @param interfaceC the interfaceC to set
	 */
	@JsonProperty("Interface__c")
	public void setInterfaceC(String interfaceC) {
		this.interfaceC = interfaceC;
	}

	/**
	 * @return the quantityC
	 */
	@JsonProperty("Quantity__c")
	public String getQuantityC() {
		return quantityC;
	}

	/**
	 * @param quantityC the quantityC to set
	 */
	@JsonProperty("Quantity__c")
	public void setQuantityC(String quantityC) {
		this.quantityC = quantityC;
	}
	
	
	 @JsonProperty("Opportunity_Name__c")
	 public String getOpportunityNameC() {
	     return opportunityNameC;
	 }

	 @JsonProperty("Opportunity_Name__c")
	 public void setOpportunityNameC(String opportunityNameC) {
	     this.opportunityNameC = opportunityNameC;
	 }

	
	@JsonProperty("Data_or_Mobile__c")
	public String getDataOrMobileC() {
		return dataOrMobileC;
	}

	@JsonProperty("Data_or_Mobile__c")
	public void setDataOrMobileC(String dataOrMobileC) {
		this.dataOrMobileC = dataOrMobileC;
	}

	@JsonProperty("POC_Attached__c")
	public String getpOCAttachedC() {
		return pOCAttachedC;
	}

	@JsonProperty("POC_Attached__c")
	public void setpOCAttachedC(String pOCAttachedC) {
		this.pOCAttachedC = pOCAttachedC;
	}

	@JsonProperty("Interconnect_Type__c")
	public String getInterconnectTypeC() {
		return interconnectTypeC;
	}
	
	@JsonProperty("Interconnect_Type__c")
	public void setInterconnectTypeC(String interconnectTypeC) {
		this.interconnectTypeC = interconnectTypeC;
	}

	@JsonProperty("Enabled_for_Unified_Access__c")
	public String getEnabledForUnifiedAccessC() {
		return enabledForUnifiedAccessC;
	}

	@JsonProperty("Enabled_for_Unified_Access__c")
	public void setEnabledForUnifiedAccessC(String enabledForUnifiedAccessC) {
		this.enabledForUnifiedAccessC = enabledForUnifiedAccessC;
	}

	@JsonProperty("Product_Category__c")
	public String getProductCategoryC() {
		return productCategoryC;
	}

	@JsonProperty("Product_Category__c")
	public void setProductCategoryC(String productCategoryC) {
		this.productCategoryC = productCategoryC;
	}

	@JsonProperty("Call_Type__c")
	public String getCallTypeC() {
		return callTypeC;
	}

	@JsonProperty("Call_Type__c")
	public void setCallTypeC(String callTypeC) {
		this.callTypeC = callTypeC;
	}

	@JsonProperty("Primary_Features__c")
	public String getPrimaryFeaturesC() {
		return primaryFeaturesC;
	}

	@JsonProperty("Primary_Features__c")
	public void setPrimaryFeaturesC(String primaryFeaturesC) {
		this.primaryFeaturesC = primaryFeaturesC;
	}
	
	@JsonProperty("LNS__c")
	public String getLnsC() {
		return lnsC;
	}

	@JsonProperty("LNS__c")
	public void setLnsC(String lnsC) {
		this.lnsC = lnsC;
	}

	@JsonProperty("ITFS__c")
	public String getItfsC() {
		return itfsC;
	}

	@JsonProperty("ITFS__c")
	public void setItfsC(String itfsC) {
		this.itfsC = itfsC;
	}

	@JsonProperty("UIFN__c")
	public String getUifnC() {
		return uifnC;
	}

	@JsonProperty("UIFN__c")
	public void setUifnC(String uifnC) {
		this.uifnC = uifnC;
	}

	@JsonProperty("Audio_Conferencing_Access_No_Service__c")
	public String getAudioConferencingAccessNoServiceC() {
		return audioConferencingAccessNoServiceC;
	}

	@JsonProperty("Audio_Conferencing_Access_No_Service__c")
	public void setAudioConferencingAccessNoServiceC(String audioConferencingAccessNoServiceC) {
		this.audioConferencingAccessNoServiceC = audioConferencingAccessNoServiceC;
	}

	@JsonProperty("Audio_Conferencing_DTF_Service__c")
	public String getAudioConferencingDTFServiceC() {
		return audioConferencingDTFServiceC;
	}

	@JsonProperty("Audio_Conferencing_DTF_Service__c")
	public void setAudioConferencingDTFServiceC(String audioConferencingDTFServiceC) {
		this.audioConferencingDTFServiceC = audioConferencingDTFServiceC;
	}

	@JsonProperty("Cloud_Provider__c")
	public String getCloudProviderC() {
		return cloudProviderC;
	}

	@JsonProperty("Cloud_Provider__c")
	public void setCloudProviderC(String cloudProviderC) {
		this.cloudProviderC = cloudProviderC;
	}

	@JsonProperty("Sub_Type__c")
	public String getSubTypeC() {
		return subTypeC;
	}

	@JsonProperty("Sub_Type__c")
	public void setSubTypeC(String subTypeC) {
		this.subTypeC = subTypeC;
	}

	@JsonProperty("Bandwidth_Circuit_Speed__c")
	public String getBandwidthCircuitSpeed() {
		return bandwidthCircuitSpeed;
	}
	@JsonProperty("Bandwidth_Circuit_Speed__c")
	public void setBandwidthCircuitSpeed(String bandwidthCircuitSpeed) {
		this.bandwidthCircuitSpeed = bandwidthCircuitSpeed;
	}

	@JsonProperty("Media_Type__c")
	public String getMediaType() {
		return mediaType;
	}

	@JsonProperty("Media_Type__c")
	public void setMediaType(String mediaType) {
		this.mediaType = mediaType;
	}
	@JsonProperty("of_Fiber__c")
	public String getOfFiber() {
		return ofFiber;
	}
	@JsonProperty("of_Fiber__c")
	public void setOfFiber(String ofFiber) {
		this.ofFiber = ofFiber;
	}
	@JsonProperty("Type_of_Fiber_Entry__c")
	public String getTypeOfFiberEntry() {
		return typeOfFiberEntry;
	}
	@JsonProperty("Type_of_Fiber_Entry__c")
	public void setTypeOfFiberEntry(String typeOfFiberEntry) {
		this.typeOfFiberEntry = typeOfFiberEntry;
	}

	public String getPreviousMrc() {
		return previousMrc;
	}

	public void setPreviousMrc(String previousMrc) {
		this.previousMrc = previousMrc;
	}

	public String getPreviousNrc() {
		return previousNrc;
	}

	public void setPreviousNrc(String previousNrc) {
		this.previousNrc = previousNrc;
	}
	
	

}

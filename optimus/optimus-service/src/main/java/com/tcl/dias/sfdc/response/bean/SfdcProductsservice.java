
package com.tcl.dias.sfdc.response.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
/**
 * This file contains the SfdcProductsservice.java class.
 * 
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
    "attributes",
    "CurrencyIsoCode",
    "Big_Machines_MRC__c",
    "Big_Machines_TCV__c",
    "IDC_Location__c",
    "Product_Line_of_Business__c",
    "L2_Feasibility_Commercial_Manager__c",
    "Do_you_need_training_for_this_Product__c",
    "Big_Machines_NRC__c",
    "IDC_Bandwidth__c",
    "Product_NRC__c",
    "Type__c",
    "Multi_VRF_Solution__c",
    "Opportunity_Name__c",
    "RecordTypeId",
    "Product_MRC__c",
    "Big_Machines_ARC__c",
    "IDC_Floor__c",
    "Order_Type__c",
    "Id",
    "Product_Type__c",
    
    //gvpn
    "of_Ports__c",
    
    //npl
    "Quantity__c","Interface__c","A_End_Country__c","B_End_Country__c","A_End_City__c","B_End_City__c",
    
    /* for gsc*/
	"Data_or_Mobile__c","POC_Attached__c","Interconnect_Type__c","Enabled_for_Unified_Access__c","Product_Category__c","Call_Type__c","Primary_Features__c",
	"LNS__c",
	"ITFS__c",
	"UIFN__c",
	"Audio_Conferencing_Access_No_Service__c",
	"Audio_Conferencing_DTF_Service__c",

        /* for NPL BM Transition*/
        "Sub_Type__c",
        "Bandwidth_Circuit_Speed__c",
        "Media_Type__c",
        /* for IZOSDWAN*/
        "MSS_Type__c",
        "Product_Flavour__c"
        
})
public class SfdcProductsservice {

    @JsonProperty("attributes")
    private SfdcAttributes attributes;
    @JsonProperty("CurrencyIsoCode")
    private String currencyIsoCode;
    @JsonProperty("Big_Machines_MRC__c")
    private Integer bigMachinesMRCC;
    @JsonProperty("Big_Machines_TCV__c")
    private Integer bigMachinesTCVC;
    @JsonProperty("IDC_Location__c")
    private String iDCLocationC;
    @JsonProperty("Product_Line_of_Business__c")
    private String productLineOfBusinessC;
    @JsonProperty("L2_Feasibility_Commercial_Manager__c")
    private String l2FeasibilityCommercialManagerC;
    @JsonProperty("Do_you_need_training_for_this_Product__c")
    private String doYouNeedTrainingForThisProductC;
    @JsonProperty("Big_Machines_NRC__c")
    private Integer bigMachinesNRCC;
    @JsonProperty("IDC_Bandwidth__c")
    private String iDCBandwidthC;
    @JsonProperty("Product_NRC__c")
    private Integer productNRCC;
    @JsonProperty("Type__c")
    private String typeC;
    @JsonProperty("Multi_VRF_Solution__c")
    private String multiVRFSolutionC;
    @JsonProperty("Opportunity_Name__c")
    private String opportunityNameC;
    @JsonProperty("RecordTypeId")
    private String recordTypeId;
    @JsonProperty("Product_MRC__c")
    private Double productMRCC;
    @JsonProperty("Big_Machines_ARC__c")
    private Integer bigMachinesARCC;
    @JsonProperty("IDC_Floor__c")
    private String iDCFloorC;
    @JsonProperty("Order_Type__c")
    private String orderTypeC;
    @JsonProperty("Id")
    private String id;
    @JsonProperty("Product_Type__c")
    private String productTypeC;
    
    @JsonProperty("of_Ports__c")
    private String ofPortsc;
    
  //for npl
  	@JsonProperty("Interface__c")
  	private String interfaceC;
  	
  	//for npl

  	@JsonProperty("Quantity__c")
  	private Integer quantityC;
  	
  	@JsonProperty("A_End_Country__c")
  	private String aEndCountryC;
  	
  	@JsonProperty("B_End_Country__c")
  	private String bEndCountryC;
  	
  	@JsonProperty("A_End_City__c")
  	private String aEndCityC;
  	
  	@JsonProperty("B_End_City__c")
  	private String bEndCityC;
  	
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
    private String bandwidthCircuitSpeedC;

    //IZO SDWAN Specific
    @JsonProperty("MSS_Type__c")
    private String MssTypeC;

    @JsonProperty("Product_Flavour__c")
    private String productFlavourC;
    
    @JsonProperty("Media_Type__c")
    private String mediaTypeC;

    @JsonProperty("attributes")
    public SfdcAttributes getAttributes() {
        return attributes;
    }

    @JsonProperty("attributes")
    public void setAttributes(SfdcAttributes attributes) {
        this.attributes = attributes;
    }

    @JsonProperty("CurrencyIsoCode")
    public String getCurrencyIsoCode() {
        return currencyIsoCode;
    }

    @JsonProperty("CurrencyIsoCode")
    public void setCurrencyIsoCode(String currencyIsoCode) {
        this.currencyIsoCode = currencyIsoCode;
    }

    @JsonProperty("Big_Machines_MRC__c")
    public Integer getBigMachinesMRCC() {
        return bigMachinesMRCC;
    }

    @JsonProperty("Big_Machines_MRC__c")
    public void setBigMachinesMRCC(Integer bigMachinesMRCC) {
        this.bigMachinesMRCC = bigMachinesMRCC;
    }

    @JsonProperty("Big_Machines_TCV__c")
    public Integer getBigMachinesTCVC() {
        return bigMachinesTCVC;
    }

    @JsonProperty("Big_Machines_TCV__c")
    public void setBigMachinesTCVC(Integer bigMachinesTCVC) {
        this.bigMachinesTCVC = bigMachinesTCVC;
    }

    @JsonProperty("IDC_Location__c")
    public String getIDCLocationC() {
        return iDCLocationC;
    }

    @JsonProperty("IDC_Location__c")
    public void setIDCLocationC(String iDCLocationC) {
        this.iDCLocationC = iDCLocationC;
    }

    @JsonProperty("Product_Line_of_Business__c")
    public String getProductLineOfBusinessC() {
        return productLineOfBusinessC;
    }

    @JsonProperty("Product_Line_of_Business__c")
    public void setProductLineOfBusinessC(String productLineOfBusinessC) {
        this.productLineOfBusinessC = productLineOfBusinessC;
    }

    @JsonProperty("L2_Feasibility_Commercial_Manager__c")
    public String getL2FeasibilityCommercialManagerC() {
        return l2FeasibilityCommercialManagerC;
    }

    @JsonProperty("L2_Feasibility_Commercial_Manager__c")
    public void setL2FeasibilityCommercialManagerC(String l2FeasibilityCommercialManagerC) {
        this.l2FeasibilityCommercialManagerC = l2FeasibilityCommercialManagerC;
    }

    @JsonProperty("Do_you_need_training_for_this_Product__c")
    public String getDoYouNeedTrainingForThisProductC() {
        return doYouNeedTrainingForThisProductC;
    }

    @JsonProperty("Do_you_need_training_for_this_Product__c")
    public void setDoYouNeedTrainingForThisProductC(String doYouNeedTrainingForThisProductC) {
        this.doYouNeedTrainingForThisProductC = doYouNeedTrainingForThisProductC;
    }

    @JsonProperty("Big_Machines_NRC__c")
    public Integer getBigMachinesNRCC() {
        return bigMachinesNRCC;
    }

    @JsonProperty("Big_Machines_NRC__c")
    public void setBigMachinesNRCC(Integer bigMachinesNRCC) {
        this.bigMachinesNRCC = bigMachinesNRCC;
    }

    @JsonProperty("IDC_Bandwidth__c")
    public String getIDCBandwidthC() {
        return iDCBandwidthC;
    }

    @JsonProperty("IDC_Bandwidth__c")
    public void setIDCBandwidthC(String iDCBandwidthC) {
        this.iDCBandwidthC = iDCBandwidthC;
    }

    @JsonProperty("Product_NRC__c")
    public Integer getProductNRCC() {
        return productNRCC;
    }

    @JsonProperty("Product_NRC__c")
    public void setProductNRCC(Integer productNRCC) {
        this.productNRCC = productNRCC;
    }

    @JsonProperty("Type__c")
    public String getTypeC() {
        return typeC;
    }

    @JsonProperty("Type__c")
    public void setTypeC(String typeC) {
        this.typeC = typeC;
    }

    @JsonProperty("Multi_VRF_Solution__c")
    public String getMultiVRFSolutionC() {
        return multiVRFSolutionC;
    }

    @JsonProperty("Multi_VRF_Solution__c")
    public void setMultiVRFSolutionC(String multiVRFSolutionC) {
        this.multiVRFSolutionC = multiVRFSolutionC;
    }

    @JsonProperty("Opportunity_Name__c")
    public String getOpportunityNameC() {
        return opportunityNameC;
    }

    @JsonProperty("Opportunity_Name__c")
    public void setOpportunityNameC(String opportunityNameC) {
        this.opportunityNameC = opportunityNameC;
    }

    @JsonProperty("RecordTypeId")
    public String getRecordTypeId() {
        return recordTypeId;
    }

    @JsonProperty("RecordTypeId")
    public void setRecordTypeId(String recordTypeId) {
        this.recordTypeId = recordTypeId;
    }

    @JsonProperty("Product_MRC__c")
    public Double getProductMRCC() {
        return productMRCC;
    }

    @JsonProperty("Product_MRC__c")
    public void setProductMRCC(Double productMRCC) {
        this.productMRCC = productMRCC;
    }

    @JsonProperty("Big_Machines_ARC__c")
    public Integer getBigMachinesARCC() {
        return bigMachinesARCC;
    }

    @JsonProperty("Big_Machines_ARC__c")
    public void setBigMachinesARCC(Integer bigMachinesARCC) {
        this.bigMachinesARCC = bigMachinesARCC;
    }

    @JsonProperty("IDC_Floor__c")
    public String getIDCFloorC() {
        return iDCFloorC;
    }

    @JsonProperty("IDC_Floor__c")
    public void setIDCFloorC(String iDCFloorC) {
        this.iDCFloorC = iDCFloorC;
    }

    @JsonProperty("Order_Type__c")
    public String getOrderTypeC() {
        return orderTypeC;
    }

    @JsonProperty("Order_Type__c")
    public void setOrderTypeC(String orderTypeC) {
        this.orderTypeC = orderTypeC;
    }

    @JsonProperty("Id")
    public String getId() {
        return id;
    }

    @JsonProperty("Id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("Product_Type__c")
    public String getProductTypeC() {
        return productTypeC;
    }

    @JsonProperty("Product_Type__c")
    public void setProductTypeC(String productTypeC) {
        this.productTypeC = productTypeC;
    }

	/**
	 * @return the ofPortsc
	 */
    @JsonProperty("of_Ports__c")
	public String getOfPortsc() {
		return ofPortsc;
	}

	/**
	 * @param ofPortsc the ofPortsc to set
	 */
    @JsonProperty("of_Ports__c")
	public void setOfPortsc(String ofPortsc) {
		this.ofPortsc = ofPortsc;
	}
    
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
	public Integer getQuantityC() {
		return quantityC;
	}

	/**
	 * @param quantityC the quantityC to set
	 */
	@JsonProperty("Quantity__c")
	public void setQuantityC(Integer quantityC) {
		this.quantityC = quantityC;
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



    @JsonProperty("Sub_Type__c")
    public String getSubTypeC() {
        return subTypeC;
    }

    @JsonProperty("Sub_Type__c")
    public void setSubTypeC(String subTypeC) {
        this.subTypeC = subTypeC;
    }

    @JsonProperty("Bandwidth_Circuit_Speed__c")
    public String getBandwidthCircuitSpeedC() {
        return bandwidthCircuitSpeedC;
    }

    @JsonProperty("Bandwidth_Circuit_Speed__c")
    public void setBandwidthCircuitSpeedC(String bandwidthCircuitSpeedC) {
        this.bandwidthCircuitSpeedC = bandwidthCircuitSpeedC;
    }

    @JsonProperty("Media_Type__c")
    public String getMediaTypeC() {
        return mediaTypeC;
    }

    @JsonProperty("Media_Type__c")
    public void setMediaTypeC(String mediaTypeC) {
        this.mediaTypeC = mediaTypeC;
    }

    @JsonProperty("MSS_Type__c")
	public String getMssTypeC() {
		return MssTypeC;
	}

    @JsonProperty("MSS_Type__c")
	public void setMssTypeC(String mssTypeC) {
		MssTypeC = mssTypeC;
	}

    @JsonProperty("Product_Flavour__c")
	public String getProductFlavourC() {
		return productFlavourC;
	}

    @JsonProperty("Product_Flavour__c")
	public void setProductFlavourC(String productFlavourC) {
		this.productFlavourC = productFlavourC;
	}
    
    
}

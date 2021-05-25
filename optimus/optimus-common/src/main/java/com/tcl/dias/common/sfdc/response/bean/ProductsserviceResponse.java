
package com.tcl.dias.common.sfdc.response.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * ProductsserviceResponse.class is used for sfdc
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductsserviceResponse {

    private AttributesResponseBean attributes;
    private String currencyIsoCode;
    private Integer bigMachinesMRC;
    private Integer bigMachinesTCV;
    private String idcLocation;
    private String productLineOfBusiness;
    private String l2FeasibilityCommercialManager;
    private String isTrainingNeeded;
    private Integer bigMachinesNRC;
    private String idcBandwidth;
    private Integer productNRC;
    private String type;
    private String multiVRFSolution;
    private String opportunityName;
    private String recordTypeId;
    private Double productMRC;
    private Integer bigMachinesARC;
    private String idcFloor;
    private String orderType;
    private String id;
    private String productType;
    
	//for npl

	private String interfaceC;
	
	//for npl

	private Integer quantityC;
	private String aEndCountryC;
	private String bEndCountryC;
	private String aEndCityC;
	private String bEndCityC;


	//for npl BM Transition specific
	private String subType;
	private String bandwidthCircuitSpeed;
	private String mediaType;

	//for gsc specific
	private String opportunityNameC;
	private String dataOrMobileC;
	private String pOCAttachedC;
	private String interconnectTypeC;
	private String enabledForUnifiedAccessC;
	private String productCategoryC;
	private String callTypeC;
	private String primaryFeaturesC;
	
	private String lnsC;
	private String itfsC;
	private String uifnC;
	private String audioConferencingAccessNoServiceC;
	private String audioConferencingDTFServiceC;
	
	//IZO SDWAN Specific
	private String mssTypec;
	private String productFlavourC;
	
	/**
	 * @return the attributes
	 */
	public AttributesResponseBean getAttributes() {
		return attributes;
	}
	/**
	 * @param attributes the attributes to set
	 */
	public void setAttributes(AttributesResponseBean attributes) {
		this.attributes = attributes;
	}
	/**
	 * @return the currencyIsoCode
	 */
	public String getCurrencyIsoCode() {
		return currencyIsoCode;
	}
	/**
	 * @param currencyIsoCode the currencyIsoCode to set
	 */
	public void setCurrencyIsoCode(String currencyIsoCode) {
		this.currencyIsoCode = currencyIsoCode;
	}
	/**
	 * @return the bigMachinesMRC
	 */
	public Integer getBigMachinesMRC() {
		return bigMachinesMRC;
	}
	/**
	 * @param bigMachinesMRC the bigMachinesMRC to set
	 */
	public void setBigMachinesMRC(Integer bigMachinesMRC) {
		this.bigMachinesMRC = bigMachinesMRC;
	}
	/**
	 * @return the bigMachinesTCV
	 */
	public Integer getBigMachinesTCV() {
		return bigMachinesTCV;
	}
	/**
	 * @param bigMachinesTCV the bigMachinesTCV to set
	 */
	public void setBigMachinesTCV(Integer bigMachinesTCV) {
		this.bigMachinesTCV = bigMachinesTCV;
	}
	/**
	 * @return the idcLocation
	 */
	public String getIdcLocation() {
		return idcLocation;
	}
	/**
	 * @param idcLocation the idcLocation to set
	 */
	public void setIdcLocation(String idcLocation) {
		this.idcLocation = idcLocation;
	}
	/**
	 * @return the productLineOfBusiness
	 */
	public String getProductLineOfBusiness() {
		return productLineOfBusiness;
	}
	/**
	 * @param productLineOfBusiness the productLineOfBusiness to set
	 */
	public void setProductLineOfBusiness(String productLineOfBusiness) {
		this.productLineOfBusiness = productLineOfBusiness;
	}
	/**
	 * @return the l2FeasibilityCommercialManager
	 */
	public String getL2FeasibilityCommercialManager() {
		return l2FeasibilityCommercialManager;
	}
	/**
	 * @param l2FeasibilityCommercialManager the l2FeasibilityCommercialManager to set
	 */
	public void setL2FeasibilityCommercialManager(String l2FeasibilityCommercialManager) {
		this.l2FeasibilityCommercialManager = l2FeasibilityCommercialManager;
	}
	/**
	 * @return the isTrainingNeeded
	 */
	public String getIsTrainingNeeded() {
		return isTrainingNeeded;
	}
	/**
	 * @param isTrainingNeeded the isTrainingNeeded to set
	 */
	public void setIsTrainingNeeded(String isTrainingNeeded) {
		this.isTrainingNeeded = isTrainingNeeded;
	}
	/**
	 * @return the bigMachinesNRC
	 */
	public Integer getBigMachinesNRC() {
		return bigMachinesNRC;
	}
	/**
	 * @param bigMachinesNRC the bigMachinesNRC to set
	 */
	public void setBigMachinesNRC(Integer bigMachinesNRC) {
		this.bigMachinesNRC = bigMachinesNRC;
	}
	/**
	 * @return the idcBandwidth
	 */
	public String getIdcBandwidth() {
		return idcBandwidth;
	}
	/**
	 * @param idcBandwidth the idcBandwidth to set
	 */
	public void setIdcBandwidth(String idcBandwidth) {
		this.idcBandwidth = idcBandwidth;
	}
	/**
	 * @return the productNRC
	 */
	public Integer getProductNRC() {
		return productNRC;
	}
	/**
	 * @param productNRC the productNRC to set
	 */
	public void setProductNRC(Integer productNRC) {
		this.productNRC = productNRC;
	}
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * @return the multiVRFSolution
	 */
	public String getMultiVRFSolution() {
		return multiVRFSolution;
	}
	/**
	 * @param multiVRFSolution the multiVRFSolution to set
	 */
	public void setMultiVRFSolution(String multiVRFSolution) {
		this.multiVRFSolution = multiVRFSolution;
	}
	/**
	 * @return the opportunityName
	 */
	public String getOpportunityName() {
		return opportunityName;
	}
	/**
	 * @param opportunityName the opportunityName to set
	 */
	public void setOpportunityName(String opportunityName) {
		this.opportunityName = opportunityName;
	}
	/**
	 * @return the recordTypeId
	 */
	public String getRecordTypeId() {
		return recordTypeId;
	}
	/**
	 * @param recordTypeId the recordTypeId to set
	 */
	public void setRecordTypeId(String recordTypeId) {
		this.recordTypeId = recordTypeId;
	}
	/**
	 * @return the productMRC
	 */
	public Double getProductMRC() {
		return productMRC;
	}
	/**
	 * @param productMRC the productMRC to set
	 */
	public void setProductMRC(Double productMRC) {
		this.productMRC = productMRC;
	}
	/**
	 * @return the bigMachinesARC
	 */
	public Integer getBigMachinesARC() {
		return bigMachinesARC;
	}
	/**
	 * @param bigMachinesARC the bigMachinesARC to set
	 */
	public void setBigMachinesARC(Integer bigMachinesARC) {
		this.bigMachinesARC = bigMachinesARC;
	}
	/**
	 * @return the idcFloor
	 */
	public String getIdcFloor() {
		return idcFloor;
	}
	/**
	 * @param idcFloor the idcFloor to set
	 */
	public void setIdcFloor(String idcFloor) {
		this.idcFloor = idcFloor;
	}
	/**
	 * @return the orderType
	 */
	public String getOrderType() {
		return orderType;
	}
	/**
	 * @param orderType the orderType to set
	 */
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return the productType
	 */
	public String getProductType() {
		return productType;
	}
	/**
	 * @param productType the productType to set
	 */
	public void setProductType(String productType) {
		this.productType = productType;
	}
	/**
	 * @return the interfaceC
	 */
	public String getInterfaceC() {
		return interfaceC;
	}
	/**
	 * @param interfaceC the interfaceC to set
	 */
	public void setInterfaceC(String interfaceC) {
		this.interfaceC = interfaceC;
	}
	/**
	 * @return the quantityC
	 */
	public Integer getQuantityC() {
		return quantityC;
	}
	/**
	 * @param quantityC the quantityC to set
	 */
	public void setQuantityC(Integer quantityC) {
		this.quantityC = quantityC;
	}

	public String getaEndCountryC() {
		return aEndCountryC;
	}
	public void setaEndCountryC(String aEndCountryC) {
		this.aEndCountryC = aEndCountryC;
	}
	public String getbEndCountryC() {
		return bEndCountryC;
	}
	public void setbEndCountryC(String bEndCountryC) {
		this.bEndCountryC = bEndCountryC;
	}
	public String getaEndCityC() {
		return aEndCityC;
	}
	public void setaEndCityC(String aEndCityC) {
		this.aEndCityC = aEndCityC;
	}
	public String getbEndCityC() {
		return bEndCityC;
	}
	public void setbEndCityC(String bEndCityC) {
		this.bEndCityC = bEndCityC;
	}

	public String getOpportunityNameC() {
		return opportunityNameC;
	}

	public void setOpportunityNameC(String opportunityNameC) {
		this.opportunityNameC = opportunityNameC;
	}

	public String getDataOrMobileC() {
		return dataOrMobileC;
	}

	public void setDataOrMobileC(String dataOrMobileC) {
		this.dataOrMobileC = dataOrMobileC;
	}

	public String getpOCAttachedC() {
		return pOCAttachedC;
	}

	public void setpOCAttachedC(String pOCAttachedC) {
		this.pOCAttachedC = pOCAttachedC;
	}

	public String getInterconnectTypeC() {
		return interconnectTypeC;
	}

	public void setInterconnectTypeC(String interconnectTypeC) {
		this.interconnectTypeC = interconnectTypeC;
	}

	public String getEnabledForUnifiedAccessC() {
		return enabledForUnifiedAccessC;
	}

	public void setEnabledForUnifiedAccessC(String enabledForUnifiedAccessC) {
		this.enabledForUnifiedAccessC = enabledForUnifiedAccessC;
	}

	public String getProductCategoryC() {
		return productCategoryC;
	}

	public void setProductCategoryC(String productCategoryC) {
		this.productCategoryC = productCategoryC;
	}

	public String getCallTypeC() {
		return callTypeC;
	}

	public void setCallTypeC(String callTypeC) {
		this.callTypeC = callTypeC;
	}

	public String getPrimaryFeaturesC() {
		return primaryFeaturesC;
	}

	public void setPrimaryFeaturesC(String primaryFeaturesC) {
		this.primaryFeaturesC = primaryFeaturesC;
	}
	
	public String getLnsC() {
		return lnsC;
	}
	public void setLnsC(String lnsC) {
		this.lnsC = lnsC;
	}
	public String getItfsC() {
		return itfsC;
	}
	public void setItfsC(String itfsC) {
		this.itfsC = itfsC;
	}
	public String getUifnC() {
		return uifnC;
	}
	public void setUifnC(String uifnC) {
		this.uifnC = uifnC;
	}
	public String getAudioConferencingAccessNoServiceC() {
		return audioConferencingAccessNoServiceC;
	}
	public void setAudioConferencingAccessNoServiceC(String audioConferencingAccessNoServiceC) {
		this.audioConferencingAccessNoServiceC = audioConferencingAccessNoServiceC;
	}
	public String getAudioConferencingDTFServiceC() {
		return audioConferencingDTFServiceC;
	}
	public void setAudioConferencingDTFServiceC(String audioConferencingDTFServiceC) {
		this.audioConferencingDTFServiceC = audioConferencingDTFServiceC;
	}

	public String getSubType() {
		return subType;
	}

	public void setSubType(String subType) {
		this.subType = subType;
	}

	public String getBandwidthCircuitSpeed() {
		return bandwidthCircuitSpeed;
	}

	public void setBandwidthCircuitSpeed(String bandwidthCircuitSpeed) {
		this.bandwidthCircuitSpeed = bandwidthCircuitSpeed;
	}

	public String getMediaType() {
		return mediaType;
	}

	public void setMediaType(String mediaType) {
		this.mediaType = mediaType;
	}
	public String getMssTypec() {
		return mssTypec;
	}
	public void setMssTypec(String mssTypec) {
		this.mssTypec = mssTypec;
	}
	public String getProductFlavourC() {
		return productFlavourC;
	}
	public void setProductFlavourC(String productFlavourC) {
		this.productFlavourC = productFlavourC;
	}

	

}

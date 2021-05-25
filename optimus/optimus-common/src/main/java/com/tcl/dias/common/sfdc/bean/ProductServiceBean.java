package com.tcl.dias.common.sfdc.bean;

import java.util.List;

/**
 * 
 * ProductServiceBean Class used for sfdc
 * 
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class ProductServiceBean {

	private String productType;
	private String productSolutionCode;
	private String productId;
	private String currencyIsoCode;
	private String productLineOfBusiness;
	private String type;
	private String isTrainingRequire;
	private String idcBandwidth;
	private String idcLocation;
	private String idcFloor;
	private Double productMRC;
	private Double productNRC;
	private String multiVRFSolution;
	private String orderType;
	private Double bigMachinesMrc;
	private Double bigMachinesArc;
	private Double bigMachinesNrc;
	private Double bigMachinesTcv;
	private Double previousMrc;
	private Double previousNrc;
	private String opportunityId;
	private String l2feasibilityCommercialManagerName;
	private String recordTypeName;
	private String productName;
	private String sourceSystem;
	private List<String> productIds;
	private Integer quoteToLeId;
	private Boolean isCancel;

	// for gvpn specific
	private String ofPortsC;

	// for gvpn specific
	private String termInMonthsC;

	// for gvpn specific
	private String CloudEnablementC;

	// for npl specific

	private String quantityC;
	private String interfaceC;
	private String aEndCountryC;
	private String bEndCountryC;
	private String aEndCityC;
	private String bEndCityC;
	private Double productARC;

	// for npl cross connect specific
	private String subType;
	private String bandwidthCircuitSpeed;
	private String mediaType;
	private String ofFiberC;
	private String typeOfFiberEntry;


	// for gsc specific
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

	// IZOPC specific
	private String cloudProvider;
	
	//MACD ILL GVPN Specific
	private String customerMailReceivedDate;
	
	//IZO SDWAN Specific
	private String mssTypeC;
	private String productFlavourC;
	
	public String getProductFlavourC() {
		return productFlavourC;
	}

	public void setProductFlavourC(String productFlavourC) {
		this.productFlavourC = productFlavourC;
	}
	//NDE
	private String hubId;
	private String productSubType;
	
	//SDWAN
	private String sdwanProductName;

	// Teamsdr
	private String parentTpsSfdcOptyId;
	private Integer parentQuoteToLeId;
	
	public String getSdwanProductName() {
		return sdwanProductName;
	}

	public void setSdwanProductName(String sdwanProductName) {
		this.sdwanProductName = sdwanProductName;
	}

	public String getHubId() {
		return hubId;
	}

	public void setHubId(String hubId) {
		this.hubId = hubId;
	}

	public String getProductSubType() {
		return productSubType;
	}

	public void setProductSubType(String productSubType) {
		this.productSubType = productSubType;
	}

	public String getCloudProvider() {
		return cloudProvider;
	}

	public void setCloudProvider(String cloudProvider) {
		this.cloudProvider = cloudProvider;
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

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getCurrencyIsoCode() {
		return currencyIsoCode;
	}

	public void setCurrencyIsoCode(String currencyIsoCode) {
		this.currencyIsoCode = currencyIsoCode;
	}

	public String getProductLineOfBusiness() {
		return productLineOfBusiness;
	}

	public void setProductLineOfBusiness(String productLineOfBusiness) {
		this.productLineOfBusiness = productLineOfBusiness;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getIsTrainingRequire() {
		return isTrainingRequire;
	}

	public void setIsTrainingRequire(String isTrainingRequire) {
		this.isTrainingRequire = isTrainingRequire;
	}

	public String getIdcBandwidth() {
		return idcBandwidth;
	}

	public void setIdcBandwidth(String idcBandwidth) {
		this.idcBandwidth = idcBandwidth;
	}

	public String getIdcLocation() {
		return idcLocation;
	}

	public void setIdcLocation(String idcLocation) {
		this.idcLocation = idcLocation;
	}

	public String getIdcFloor() {
		return idcFloor;
	}

	public void setIdcFloor(String idcFloor) {
		this.idcFloor = idcFloor;
	}

	public Double getProductMRC() {
		return productMRC;
	}

	public void setProductMRC(Double productMRC) {
		this.productMRC = productMRC;
	}

	public Double getProductNRC() {
		return productNRC;
	}

	public void setProductNRC(Double productNRC) {
		this.productNRC = productNRC;
	}

	public String getMultiVRFSolution() {
		return multiVRFSolution;
	}

	public void setMultiVRFSolution(String multiVRFSolution) {
		this.multiVRFSolution = multiVRFSolution;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public Double getBigMachinesMrc() {
		return bigMachinesMrc;
	}

	public void setBigMachinesMrc(Double bigMachinesMrc) {
		this.bigMachinesMrc = bigMachinesMrc;
	}

	public Double getBigMachinesArc() {
		return bigMachinesArc;
	}

	public void setBigMachinesArc(Double bigMachinesArc) {
		this.bigMachinesArc = bigMachinesArc;
	}

	public Double getBigMachinesNrc() {
		return bigMachinesNrc;
	}

	public void setBigMachinesNrc(Double bigMachinesNrc) {
		this.bigMachinesNrc = bigMachinesNrc;
	}

	public Double getBigMachinesTcv() {
		return bigMachinesTcv;
	}

	public void setBigMachinesTcv(Double bigMachinesTcv) {
		this.bigMachinesTcv = bigMachinesTcv;
	}

	public String getOpportunityId() {
		return opportunityId;
	}

	public void setOpportunityId(String opportunityId) {
		this.opportunityId = opportunityId;
	}

	public String getL2feasibilityCommercialManagerName() {
		return l2feasibilityCommercialManagerName;
	}

	public void setL2feasibilityCommercialManagerName(String l2feasibilityCommercialManagerName) {
		this.l2feasibilityCommercialManagerName = l2feasibilityCommercialManagerName;
	}

	public String getRecordTypeName() {
		return recordTypeName;
	}

	public void setRecordTypeName(String recordTypeName) {
		this.recordTypeName = recordTypeName;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductSolutionCode() {
		return productSolutionCode;
	}

	public void setProductSolutionCode(String productSolutionCode) {
		this.productSolutionCode = productSolutionCode;
	}

	public String getSourceSystem() {
		return sourceSystem;
	}

	public void setSourceSystem(String sourceSystem) {
		this.sourceSystem = sourceSystem;
	}

	public List<String> getProductIds() {
		return productIds;
	}

	public void setProductIds(List<String> productIds) {
		this.productIds = productIds;
	}

	/**
	 * @return the ofPortsC
	 */
	public String getOfPortsC() {
		return ofPortsC;
	}

	/**
	 * @param ofPortsC the ofPortsC to set
	 */
	public void setOfPortsC(String ofPortsC) {
		this.ofPortsC = ofPortsC;
	}

	/**
	 * @return the termInMonthsC
	 */
	public String getTermInMonthsC() {
		return termInMonthsC;
	}

	/**
	 * @param termInMonthsC the termInMonthsC to set
	 */
	public void setTermInMonthsC(String termInMonthsC) {
		this.termInMonthsC = termInMonthsC;
	}

	/**
	 * @return the cloudEnablementC
	 */
	public String getCloudEnablementC() {
		return CloudEnablementC;
	}

	/**
	 * @param cloudEnablementC the cloudEnablementC to set
	 */
	public void setCloudEnablementC(String cloudEnablementC) {
		CloudEnablementC = cloudEnablementC;
	}

	/**
	 * @return the quantityC
	 */
	public String getQuantityC() {
		return quantityC;
	}

	/**
	 * @param quantityC the quantityC to set
	 */
	public void setQuantityC(String quantityC) {
		this.quantityC = quantityC;
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

	// NPL getters and setters
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
	
	
	/**
	 * @return the customerMailReceivedDate
	 */
	public String getCustomerMailReceivedDate() {
		return customerMailReceivedDate;
	}

	/**
	 * @param customerMailReceivedDate the customerMailReceivedDate to set
	 */
	public void setCustomerMailReceivedDate(String customerMailReceivedDate) {
		this.customerMailReceivedDate = customerMailReceivedDate;
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

	public Double getProductARC() {
		return productARC;
	}

	public void setProductARC(Double productARC) {
		this.productARC = productARC;
	}
	
	

	public Integer getQuoteToLeId() {
		return quoteToLeId;
	}

	public void setQuoteToLeId(Integer quoteToLeId) {
		this.quoteToLeId = quoteToLeId;
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

	public String getOfFiberC() {
		return ofFiberC;
	}

	public void setOfFiberC(String ofFiberC) {
		this.ofFiberC = ofFiberC;
	}

	public String getTypeOfFiberEntry() {
		return typeOfFiberEntry;
	}

	public void setTypeOfFiberEntry(String typeOfFiberEntry) {
		this.typeOfFiberEntry = typeOfFiberEntry;
	}

	public Boolean getIsCancel() {
		return isCancel;
	}

	public void setIsCancel(Boolean isCancel) {
		this.isCancel = isCancel;
	}
	
	public String getMssTypeC() {
		return mssTypeC;
	}

	public void setMssTypeC(String mssTypeC) {
		this.mssTypeC = mssTypeC;
	}
	
	public String getParentTpsSfdcOptyId() {
		return parentTpsSfdcOptyId;
	}

	public void setParentTpsSfdcOptyId(String parentTpsSfdcOptyId) {
		this.parentTpsSfdcOptyId = parentTpsSfdcOptyId;
	}

	public Integer getParentQuoteToLeId() {
		return parentQuoteToLeId;
	}

	public void setParentQuoteToLeId(Integer parentQuoteToLeId) {
		this.parentQuoteToLeId = parentQuoteToLeId;

	}
	public Double getPreviousMrc() {
		return previousMrc;

	}

	public void setPreviousMrc(Double previousMrc) {
		this.previousMrc = previousMrc;
	}

	public Double getPreviousNrc() {
		return previousNrc;
	}

	public void setPreviousNrc(Double previousNrc) {
		this.previousNrc = previousNrc;
	}

	@Override
	public String toString() {
		return "ProductServiceBean [productType=" + productType + ", productSolutionCode=" + productSolutionCode
				+ ", productId=" + productId + ", currencyIsoCode=" + currencyIsoCode + ", productLineOfBusiness="
				+ productLineOfBusiness + ", type=" + type + ", isTrainingRequire=" + isTrainingRequire
				+ ", idcBandwidth=" + idcBandwidth + ", idcLocation=" + idcLocation + ", idcFloor=" + idcFloor
				+ ", productMRC=" + productMRC + ", productNRC=" + productNRC + ", multiVRFSolution=" + multiVRFSolution
				+ ", orderType=" + orderType + ", bigMachinesMrc=" + bigMachinesMrc + ", bigMachinesArc="
				+ bigMachinesArc + ", bigMachinesNrc=" + bigMachinesNrc + ", bigMachinesTcv=" + bigMachinesTcv
				+ ", opportunityId=" + opportunityId + ", l2feasibilityCommercialManagerName="
				+ l2feasibilityCommercialManagerName + ", recordTypeName=" + recordTypeName + ", productName="
				+ productName + ", sourceSystem=" + sourceSystem + ", productIds=" + productIds + ", quoteToLeId="
				+ quoteToLeId + ", isCancel=" + isCancel + ", ofPortsC=" + ofPortsC + ", termInMonthsC=" + termInMonthsC
				+ ", CloudEnablementC=" + CloudEnablementC + ", quantityC=" + quantityC + ", interfaceC=" + interfaceC
				+ ", aEndCountryC=" + aEndCountryC + ", bEndCountryC=" + bEndCountryC + ", aEndCityC=" + aEndCityC
				+ ", bEndCityC=" + bEndCityC + ", productARC=" + productARC + ", subType=" + subType
				+ ", bandwidthCircuitSpeed=" + bandwidthCircuitSpeed + ", mediaType=" + mediaType + ", ofFiberC="
				+ ofFiberC + ", typeOfFiberEntry=" + typeOfFiberEntry + ", opportunityNameC=" + opportunityNameC
				+ ", dataOrMobileC=" + dataOrMobileC + ", pOCAttachedC=" + pOCAttachedC + ", interconnectTypeC="
				+ interconnectTypeC + ", enabledForUnifiedAccessC=" + enabledForUnifiedAccessC + ", productCategoryC="
				+ productCategoryC + ", callTypeC=" + callTypeC + ", primaryFeaturesC=" + primaryFeaturesC + ", lnsC="
				+ lnsC + ", itfsC=" + itfsC + ", uifnC=" + uifnC + ", audioConferencingAccessNoServiceC="
				+ audioConferencingAccessNoServiceC + ", audioConferencingDTFServiceC=" + audioConferencingDTFServiceC
				+ ", cloudProvider=" + cloudProvider+ ", sdwanProductName=" + sdwanProductName + ", customerMailReceivedDate=" + customerMailReceivedDate
				+ ", mssTypeC=" + mssTypeC +
				", parentTpsSfdcOptyId=" + parentTpsSfdcOptyId + '\'' +
				", parentQuoteToLeId=" + parentQuoteToLeId + '}';
	}
	
	

}

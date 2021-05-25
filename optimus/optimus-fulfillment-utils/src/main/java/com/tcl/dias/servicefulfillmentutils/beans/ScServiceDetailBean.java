package com.tcl.dias.servicefulfillmentutils.beans;

import com.tcl.dias.common.serviceinventory.bean.OptimusRfDataBean;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Objects;

public class ScServiceDetailBean {
    private Integer id;
    private String accessType;
    private Double arc;
    private String billingAccountId;
    private String billingGstNumber;
    private Double billingRatioPercent;
    private String billingType;
    private String bwPortspeed;
    private String bwPortspeedAltName;
    private String bwUnit;
    private String callType;
    private String createdBy;
    private String createdDate;
    private String custOrgNo;
    private String demarcationBuildingName;
    private String demarcationFloor;
    private String demarcationRack;
    private String demarcationRoom;
    private String destinationCity;
    private String destinationCountry;
    private String destinationCountryCode;
    private String destinationCountryCodeRepc;
    private BigDecimal discountArc;
    private BigDecimal discountMrc;
    private BigDecimal discountNrc;
    private String serviceVariant;
    private String erfLocDestinationCityId;
    private Integer erfLocDestinationCountryId;
    private String erfLocPopSiteAddressId;
    private String erfLocSiteAddressId;
    private String erfLocSourceCityId;
    private Integer erfLocSrcCountryId;
    private Integer erfPrdCatalogOfferingId;
    private Integer erfOdrServiceId;
    private String erfPrdCatalogOfferingName;
    private String erfPrdCatalogParentProductName;
    private String erfPrdCatalogParentProductOfferingName;
    private Integer erfPrdCatalogProductId;
    private String erfPrdCatalogProductName;
    private String feasibilityId;
    private String gscOrderSequenceId;
    private String isActive;
    private String isIzo;
    private String lastmileBw;
    private String lastmileBwAltName;
    private String lastmileBwUnit;
    private String lastmileProvider;
    private String lastmileType;
    private String latLong;
    private String localItContactEmail;
    private String localItContactMobile;
    private String localItContactName;
    private Double mrc;
    private Double nrc;
    private Integer parentBundleServiceId;
    private Integer parentId;
    private String popSiteAddress;
    private String popSiteCode;
    private String priSecServiceLink;
    private Integer erdPriSecServiceLinkId;
    private String primarySecondary;
    private Integer productReferenceId;
    private String scOrderUuid;
    private String serviceClass;
    private String serviceClassification;
    private String serviceCommissionedDate;
    private String serviceGroupId;
    private String serviceGroupType;
    private String serviceOption;
    private String serviceStatus;
    private String serviceTerminationDate;
    private String serviceTopology;
    private String siteAddress;
    private String siteAlias;
    private String siteEndInterface;
    private String siteLinkLabel;
    private String siteTopology;
    private String siteType;
    private String slaTemplate;
    private String smEmail;
    private String smName;
    private String sourceCity;
    private String sourceCountry;
    private String sourceState;
    private String destinationState;
    private String sourceAddressLineOne;
    private String sourceAddressLineTwo;
    private String sourceLocality;
    private String sourcePincode;
    private String destinationAddressLineOne;
    private String destinationAddressLineTwo;
    private String destinationLocality;
    private String destinationPincode;
    private String sourceCountryCode;
    private String sourceCountryCodeRepc;
    private String supplOrgNo;
    private String taxExemptionFlag;
    private String tpsCopfId;
    private String tpsServiceId;
    private String tpsSourceServiceId;
    private String updatedBy;
    private String updatedDate;
    private String uuid;
    private String vpnName;
    private String estimatedDeliveryDate;
    private String committedDeliveryDate;
    private String actualDeliveryDate;
    private String targetedDeliveryDate;
    private Float priority;
    private String vpnSolutionId;
    private String burstableBw;
    private String burstableBwUnit;
    private String lastmileScenario;
    private String lastmileConnectionType;
    private String lineRate;
    private String siteCode;
    private String orderType;
    private String status;
    private String assignedPM="";
    private String clrStageForAmendment;
    private String additionalIpPoolType;
    
    private String orderSubCategory;

	private Byte isJeopardyTask;
	private Integer serviceLinkId;
    private String isOrderAmendent;
    
	private String orderCategory;

	private Double differentialMrc;
    private Double differentialNrc;

    private String tigerOrderId;


    //Multi Vrf
    private String isMultiVrf;
    private String masterVrfServiceId;
    private String multiVrfSolution;
    private String terminationFlowTriggered;
    private Timestamp terminationInitiationDate;
    private String customerRequestorDate;
    private String terminationEffectiveDate;
    private String cancellationFlowTriggered;
    private Timestamp cancellationInitiationDate;

    private String isAmended="N";
    private String requestForAmendment="N";
    private String workFlowName;
    private String noOfAdditionalIps;
    private String parentUuid;






    public String getParentUuid() {
        return parentUuid;
    }

    public void setParentUuid(String parentUuid) {
        this.parentUuid = parentUuid;
    }
    public String getNoOfAdditionalIps() {
        return noOfAdditionalIps;
    }

    public void setNoOfAdditionalIps(String noOfAdditionalIps) {
        this.noOfAdditionalIps = noOfAdditionalIps;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public void setServiceCommissionedDate(String serviceCommissionedDate) {
        this.serviceCommissionedDate = serviceCommissionedDate;
    }

    public void setServiceTerminationDate(String serviceTerminationDate) {
        this.serviceTerminationDate = serviceTerminationDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }

    public void setEstimatedDeliveryDate(String estimatedDeliveryDate) {
        this.estimatedDeliveryDate = estimatedDeliveryDate;
    }

    public void setCommittedDeliveryDate(String committedDeliveryDate) {
        this.committedDeliveryDate = committedDeliveryDate;
    }

    public void setActualDeliveryDate(String actualDeliveryDate) {
        this.actualDeliveryDate = actualDeliveryDate;
    }

    public void setTargetedDeliveryDate(String targetedDeliveryDate) {
        this.targetedDeliveryDate = targetedDeliveryDate;
    }

    public String getTerminationFlowTriggered() {
        return terminationFlowTriggered;
    }

    public void setTerminationFlowTriggered(String terminationFlowTriggered) {
        this.terminationFlowTriggered = terminationFlowTriggered;
    }

    public Timestamp getTerminationInitiationDate() {
        return terminationInitiationDate;
    }

    public void setTerminationInitiationDate(Timestamp terminationInitiationDate) {
        this.terminationInitiationDate = terminationInitiationDate;
    }

    public String getCustomerRequestorDate() {
        return customerRequestorDate;
    }

    public void setCustomerRequestorDate(String customerRequestorDate) {
        this.customerRequestorDate = customerRequestorDate;
    }

    public String getTerminationEffectiveDate() {
        return terminationEffectiveDate;
    }

    public void setTerminationEffectiveDate(String terminationEffectiveDate) {
        this.terminationEffectiveDate = terminationEffectiveDate;
    }

    public String getCancellationFlowTriggered() {
        return cancellationFlowTriggered;
    }

    public void setCancellationFlowTriggered(String cancellationFlowTriggered) {
        this.cancellationFlowTriggered = cancellationFlowTriggered;
    }

    public Timestamp getCancellationInitiationDate() {
        return cancellationInitiationDate;
    }

    public void setCancellationInitiationDate(Timestamp cancellationInitiationDate) {
        this.cancellationInitiationDate = cancellationInitiationDate;
    }

    public String getIsAmended() {
        return isAmended;
    }

    public void setIsAmended(String isAmended) {
        this.isAmended = isAmended;
    }

    public String getRequestForAmendment() {
        return requestForAmendment;
    }

    public void setRequestForAmendment(String requestForAmendment) {
        this.requestForAmendment = requestForAmendment;
    }

    public String getWorkFlowName() {
        return workFlowName;
    }

    public void setWorkFlowName(String workFlowName) {
        this.workFlowName = workFlowName;
    }




    /**
	 * @return the orderCategory
	 */
	public String getOrderCategory() {
		return orderCategory;
	}

	/**
	 * @param orderCategory the orderCategory to set
	 */
	public void setOrderCategory(String orderCategory) {
		this.orderCategory = orderCategory;
	}

	public String getOrderSubCategory() {
        return orderSubCategory;
    }

    public void setOrderSubCategory(String orderSubCategory) {
        this.orderSubCategory = orderSubCategory;
    }

    public Byte getIsJeopardyTask() {
		return isJeopardyTask;
	}

	public void setIsJeopardyTask(Byte isJeopardyTask) {
		this.isJeopardyTask = isJeopardyTask;
	}

	private OptimusRfDataBean optimusRfDataBean;

    public String getAccessType() {
        return accessType;
    }

    public void setAccessType(String accessType) {
        this.accessType = accessType;
    }

    public Double getArc() {
        return arc;
    }

    public void setArc(Double arc) {
        this.arc = arc;
    }

    public String getBillingAccountId() {
        return billingAccountId;
    }

    public void setBillingAccountId(String billingAccountId) {
        this.billingAccountId = billingAccountId;
    }

    public String getBillingGstNumber() {
        return billingGstNumber;
    }

    public void setBillingGstNumber(String billingGstNumber) {
        this.billingGstNumber = billingGstNumber;
    }

    public Double getBillingRatioPercent() {
        return billingRatioPercent;
    }

    public void setBillingRatioPercent(Double billingRatioPercent) {
        this.billingRatioPercent = billingRatioPercent;
    }

    public String getBillingType() {
        return billingType;
    }

    public void setBillingType(String billingType) {
        this.billingType = billingType;
    }

    public String getBwPortspeed() {
        return bwPortspeed;
    }

    public void setBwPortspeed(String bwPortspeed) {
        this.bwPortspeed = bwPortspeed;
    }

    public String getBwPortspeedAltName() {
        return bwPortspeedAltName;
    }

    public void setBwPortspeedAltName(String bwPortspeedAltName) {
        this.bwPortspeedAltName = bwPortspeedAltName;
    }

    public String getBwUnit() {
        return bwUnit;
    }

    public void setBwUnit(String bwUnit) {
        this.bwUnit = bwUnit;
    }

    public String getCallType() {
        return callType;
    }

    public void setCallType(String callType) {
        this.callType = callType;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        if (Objects.nonNull(createdDate))
            this.createdDate = createdDate.toString();
    }

    public String getCustOrgNo() {
        return custOrgNo;
    }

    public void setCustOrgNo(String custOrgNo) {
        this.custOrgNo = custOrgNo;
    }

    public String getDemarcationBuildingName() {
        return demarcationBuildingName;
    }

    public void setDemarcationBuildingName(String demarcationBuildingName) {
        this.demarcationBuildingName = demarcationBuildingName;
    }

    public String getDemarcationFloor() {
        return demarcationFloor;
    }

    public void setDemarcationFloor(String demarcationFloor) {
        this.demarcationFloor = demarcationFloor;
    }

    public String getDemarcationRack() {
        return demarcationRack;
    }

    public void setDemarcationRack(String demarcationRack) {
        this.demarcationRack = demarcationRack;
    }

    public String getDemarcationRoom() {
        return demarcationRoom;
    }

    public void setDemarcationRoom(String demarcationRoom) {
        this.demarcationRoom = demarcationRoom;
    }

    public String getDestinationCity() {
        return destinationCity;
    }

    public void setDestinationCity(String destinationCity) {
        this.destinationCity = destinationCity;
    }

    public String getDestinationCountry() {
        return destinationCountry;
    }

    public void setDestinationCountry(String destinationCountry) {
        this.destinationCountry = destinationCountry;
    }

    public String getDestinationCountryCode() {
        return destinationCountryCode;
    }

    public void setDestinationCountryCode(String destinationCountryCode) {
        this.destinationCountryCode = destinationCountryCode;
    }

    public String getDestinationCountryCodeRepc() {
        return destinationCountryCodeRepc;
    }

    public void setDestinationCountryCodeRepc(String destinationCountryCodeRepc) {
        this.destinationCountryCodeRepc = destinationCountryCodeRepc;
    }

    public BigDecimal getDiscountArc() {
        return discountArc;
    }

    public void setDiscountArc(BigDecimal discountArc) {
        this.discountArc = discountArc;
    }

    public BigDecimal getDiscountMrc() {
        return discountMrc;
    }

    public void setDiscountMrc(BigDecimal discountMrc) {
        this.discountMrc = discountMrc;
    }

    public BigDecimal getDiscountNrc() {
        return discountNrc;
    }

    public void setDiscountNrc(BigDecimal discountNrc) {
        this.discountNrc = discountNrc;
    }

    public String getServiceVariant() {
        return serviceVariant;
    }

    public void setServiceVariant(String serviceVariant) {
        this.serviceVariant = serviceVariant;
    }

    public String getErfLocDestinationCityId() {
        return erfLocDestinationCityId;
    }

    public void setErfLocDestinationCityId(String erfLocDestinationCityId) {
        this.erfLocDestinationCityId = erfLocDestinationCityId;
    }

    public Integer getErfLocDestinationCountryId() {
        return erfLocDestinationCountryId;
    }

    public void setErfLocDestinationCountryId(Integer erfLocDestinationCountryId) {
        this.erfLocDestinationCountryId = erfLocDestinationCountryId;
    }

    public String getErfLocPopSiteAddressId() {
        return erfLocPopSiteAddressId;
    }

    public void setErfLocPopSiteAddressId(String erfLocPopSiteAddressId) {
        this.erfLocPopSiteAddressId = erfLocPopSiteAddressId;
    }

    public String getErfLocSiteAddressId() {
        return erfLocSiteAddressId;
    }

    public void setErfLocSiteAddressId(String erfLocSiteAddressId) {
        this.erfLocSiteAddressId = erfLocSiteAddressId;
    }

    public String getErfLocSourceCityId() {
        return erfLocSourceCityId;
    }

    public void setErfLocSourceCityId(String erfLocSourceCityId) {
        this.erfLocSourceCityId = erfLocSourceCityId;
    }

    public Integer getErfLocSrcCountryId() {
        return erfLocSrcCountryId;
    }

    public void setErfLocSrcCountryId(Integer erfLocSrcCountryId) {
        this.erfLocSrcCountryId = erfLocSrcCountryId;
    }

    public Integer getErfPrdCatalogOfferingId() {
        return erfPrdCatalogOfferingId;
    }

    public void setErfPrdCatalogOfferingId(Integer erfPrdCatalogOfferingId) {
        this.erfPrdCatalogOfferingId = erfPrdCatalogOfferingId;
    }

    public Integer getErfOdrServiceId() {
        return erfOdrServiceId;
    }

    public void setErfOdrServiceId(Integer erfOdrServiceId) {
        this.erfOdrServiceId = erfOdrServiceId;
    }

    public String getErfPrdCatalogOfferingName() {
        return erfPrdCatalogOfferingName;
    }

    public void setErfPrdCatalogOfferingName(String erfPrdCatalogOfferingName) {
        this.erfPrdCatalogOfferingName = erfPrdCatalogOfferingName;
    }

    public String getErfPrdCatalogParentProductName() {
        return erfPrdCatalogParentProductName;
    }

    public void setErfPrdCatalogParentProductName(String erfPrdCatalogParentProductName) {
        this.erfPrdCatalogParentProductName = erfPrdCatalogParentProductName;
    }

    public String getErfPrdCatalogParentProductOfferingName() {
        return erfPrdCatalogParentProductOfferingName;
    }

    public void setErfPrdCatalogParentProductOfferingName(String erfPrdCatalogParentProductOfferingName) {
        this.erfPrdCatalogParentProductOfferingName = erfPrdCatalogParentProductOfferingName;
    }

    public Integer getErfPrdCatalogProductId() {
        return erfPrdCatalogProductId;
    }

    public void setErfPrdCatalogProductId(Integer erfPrdCatalogProductId) {
        this.erfPrdCatalogProductId = erfPrdCatalogProductId;
    }

    public String getErfPrdCatalogProductName() {
        return erfPrdCatalogProductName;
    }

    public void setErfPrdCatalogProductName(String erfPrdCatalogProductName) {
        this.erfPrdCatalogProductName = erfPrdCatalogProductName;
    }

    public String getFeasibilityId() {
        return feasibilityId;
    }

    public void setFeasibilityId(String feasibilityId) {
        this.feasibilityId = feasibilityId;
    }

    public String getGscOrderSequenceId() {
        return gscOrderSequenceId;
    }

    public void setGscOrderSequenceId(String gscOrderSequenceId) {
        this.gscOrderSequenceId = gscOrderSequenceId;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getIsIzo() {
        return isIzo;
    }

    public void setIsIzo(String isIzo) {
        this.isIzo = isIzo;
    }

    public String getLastmileBw() {
        return lastmileBw;
    }

    public void setLastmileBw(String lastmileBw) {
        this.lastmileBw = lastmileBw;
    }

    public String getLastmileBwAltName() {
        return lastmileBwAltName;
    }

    public void setLastmileBwAltName(String lastmileBwAltName) {
        this.lastmileBwAltName = lastmileBwAltName;
    }

    public String getLastmileBwUnit() {
        return lastmileBwUnit;
    }

    public void setLastmileBwUnit(String lastmileBwUnit) {
        this.lastmileBwUnit = lastmileBwUnit;
    }

    public String getLastmileProvider() {
        return lastmileProvider;
    }

    public void setLastmileProvider(String lastmileProvider) {
        this.lastmileProvider = lastmileProvider;
    }

    public String getLastmileType() {
        return lastmileType;
    }

    public void setLastmileType(String lastmileType) {
        this.lastmileType = lastmileType;
    }

    public String getLatLong() {
        return latLong;
    }

    public void setLatLong(String latLong) {
        this.latLong = latLong;
    }

    public String getLocalItContactEmail() {
        return localItContactEmail;
    }

    public void setLocalItContactEmail(String localItContactEmail) {
        this.localItContactEmail = localItContactEmail;
    }

    public String getLocalItContactMobile() {
        return localItContactMobile;
    }

    public void setLocalItContactMobile(String localItContactMobile) {
        this.localItContactMobile = localItContactMobile;
    }

    public String getLocalItContactName() {
        return localItContactName;
    }

    public void setLocalItContactName(String localItContactName) {
        this.localItContactName = localItContactName;
    }

    public Double getMrc() {
        return mrc;
    }

    public void setMrc(Double mrc) {
        this.mrc = mrc;
    }

    public Double getNrc() {
        return nrc;
    }

    public void setNrc(Double nrc) {
        this.nrc = nrc;
    }

    public Integer getParentBundleServiceId() {
        return parentBundleServiceId;
    }

    public void setParentBundleServiceId(Integer parentBundleServiceId) {
        this.parentBundleServiceId = parentBundleServiceId;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getPopSiteAddress() {
        return popSiteAddress;
    }

    public void setPopSiteAddress(String popSiteAddress) {
        this.popSiteAddress = popSiteAddress;
    }

    public String getPopSiteCode() {
        return popSiteCode;
    }

    public void setPopSiteCode(String popSiteCode) {
        this.popSiteCode = popSiteCode;
    }

    public String getPriSecServiceLink() {
        return priSecServiceLink;
    }

    public void setPriSecServiceLink(String priSecServiceLink) {
        this.priSecServiceLink = priSecServiceLink;
    }

    public Integer getErdPriSecServiceLinkId() {
        return erdPriSecServiceLinkId;
    }

    public void setErdPriSecServiceLinkId(Integer erdPriSecServiceLinkId) {
        this.erdPriSecServiceLinkId = erdPriSecServiceLinkId;
    }

    public String getPrimarySecondary() {
        return primarySecondary;
    }

    public void setPrimarySecondary(String primarySecondary) {
        this.primarySecondary = primarySecondary;
    }

    public Integer getProductReferenceId() {
        return productReferenceId;
    }

    public void setProductReferenceId(Integer productReferenceId) {
        this.productReferenceId = productReferenceId;
    }

    public String getScOrderUuid() {
        return scOrderUuid;
    }

    public void setScOrderUuid(String scOrderUuid) {
        this.scOrderUuid = scOrderUuid;
    }

    public String getServiceClass() {
        return serviceClass;
    }

    public void setServiceClass(String serviceClass) {
        this.serviceClass = serviceClass;
    }

    public String getServiceClassification() {
        return serviceClassification;
    }

    public void setServiceClassification(String serviceClassification) {
        this.serviceClassification = serviceClassification;
    }

    public String getServiceCommissionedDate() {
        return serviceCommissionedDate;
    }

    public void setServiceCommissionedDate(Timestamp serviceCommissionedDate) {
        if (Objects.nonNull(serviceCommissionedDate))
            this.serviceCommissionedDate = serviceCommissionedDate.toString();
    }

    public String getServiceGroupId() {
        return serviceGroupId;
    }

    public void setServiceGroupId(String serviceGroupId) {
        this.serviceGroupId = serviceGroupId;
    }

    public String getServiceGroupType() {
        return serviceGroupType;
    }

    public void setServiceGroupType(String serviceGroupType) {
        this.serviceGroupType = serviceGroupType;
    }

    public String getServiceOption() {
        return serviceOption;
    }

    public void setServiceOption(String serviceOption) {
        this.serviceOption = serviceOption;
    }

    public String getServiceStatus() {
        return serviceStatus;
    }

    public void setServiceStatus(String serviceStatus) {
        this.serviceStatus = serviceStatus;
    }

    public String getServiceTerminationDate() {
        return serviceTerminationDate;
    }

    public void setServiceTerminationDate(Timestamp serviceTerminationDate) {
        if (Objects.nonNull(serviceTerminationDate))
            this.serviceTerminationDate = serviceTerminationDate.toString();
    }

    public String getServiceTopology() {
        return serviceTopology;
    }

    public String getStatus() { return status; }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setServiceTopology(String serviceTopology) {
        this.serviceTopology = serviceTopology;
    }

    public String getSiteAddress() {
        return siteAddress;
    }

    public void setSiteAddress(String siteAddress) {
        this.siteAddress = siteAddress;
    }

    public String getSiteAlias() {
        return siteAlias;
    }

    public void setSiteAlias(String siteAlias) {
        this.siteAlias = siteAlias;
    }

    public String getSiteEndInterface() {
        return siteEndInterface;
    }

    public void setSiteEndInterface(String siteEndInterface) {
        this.siteEndInterface = siteEndInterface;
    }

    public String getSiteLinkLabel() {
        return siteLinkLabel;
    }

    public void setSiteLinkLabel(String siteLinkLabel) {
        this.siteLinkLabel = siteLinkLabel;
    }

    public String getSiteTopology() {
        return siteTopology;
    }

    public void setSiteTopology(String siteTopology) {
        this.siteTopology = siteTopology;
    }

    public String getSiteType() {
        return siteType;
    }

    public void setSiteType(String siteType) {
        this.siteType = siteType;
    }

    public String getSlaTemplate() {
        return slaTemplate;
    }

    public void setSlaTemplate(String slaTemplate) {
        this.slaTemplate = slaTemplate;
    }

    public String getSmEmail() {
        return smEmail;
    }

    public void setSmEmail(String smEmail) {
        this.smEmail = smEmail;
    }

    public String getSmName() {
        return smName;
    }

    public void setSmName(String smName) {
        this.smName = smName;
    }

    public String getSourceCity() {
        return sourceCity;
    }

    public void setSourceCity(String sourceCity) {
        this.sourceCity = sourceCity;
    }

    public String getSourceCountry() {
        return sourceCountry;
    }

    public void setSourceCountry(String sourceCountry) {
        this.sourceCountry = sourceCountry;
    }

    public String getSourceState() {
        return sourceState;
    }

    public void setSourceState(String sourceState) {
        this.sourceState = sourceState;
    }

    public String getDestinationState() {
        return destinationState;
    }

    public void setDestinationState(String destinationState) {
        this.destinationState = destinationState;
    }

    public String getSourceAddressLineOne() {
        return sourceAddressLineOne;
    }

    public void setSourceAddressLineOne(String sourceAddressLineOne) {
        this.sourceAddressLineOne = sourceAddressLineOne;
    }

    public String getSourceAddressLineTwo() {
        return sourceAddressLineTwo;
    }

    public void setSourceAddressLineTwo(String sourceAddressLineTwo) {
        this.sourceAddressLineTwo = sourceAddressLineTwo;
    }

    public String getSourceLocality() {
        return sourceLocality;
    }

    public void setSourceLocality(String sourceLocality) {
        this.sourceLocality = sourceLocality;
    }

    public String getSourcePincode() {
        return sourcePincode;
    }

    public void setSourcePincode(String sourcePincode) {
        this.sourcePincode = sourcePincode;
    }

    public String getDestinationAddressLineOne() {
        return destinationAddressLineOne;
    }

    public void setDestinationAddressLineOne(String destinationAddressLineOne) {
        this.destinationAddressLineOne = destinationAddressLineOne;
    }

    public String getDestinationAddressLineTwo() {
        return destinationAddressLineTwo;
    }

    public void setDestinationAddressLineTwo(String destinationAddressLineTwo) {
        this.destinationAddressLineTwo = destinationAddressLineTwo;
    }

    public String getDestinationLocality() {
        return destinationLocality;
    }

    public void setDestinationLocality(String destinationLocality) {
        this.destinationLocality = destinationLocality;
    }

    public String getDestinationPincode() {
        return destinationPincode;
    }

    public void setDestinationPincode(String destinationPincode) {
        this.destinationPincode = destinationPincode;
    }

    public String getSourceCountryCode() {
        return sourceCountryCode;
    }

    public void setSourceCountryCode(String sourceCountryCode) {
        this.sourceCountryCode = sourceCountryCode;
    }

    public String getSourceCountryCodeRepc() {
        return sourceCountryCodeRepc;
    }

    public void setSourceCountryCodeRepc(String sourceCountryCodeRepc) {
        this.sourceCountryCodeRepc = sourceCountryCodeRepc;
    }

    public String getOrderType() { return orderType; }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getSupplOrgNo() {
        return supplOrgNo;
    }

    public void setSupplOrgNo(String supplOrgNo) {
        this.supplOrgNo = supplOrgNo;
    }

    public String getTaxExemptionFlag() {
        return taxExemptionFlag;
    }

    public void setTaxExemptionFlag(String taxExemptionFlag) {
        this.taxExemptionFlag = taxExemptionFlag;
    }

    public String getTpsCopfId() {
        return tpsCopfId;
    }

    public void setTpsCopfId(String tpsCopfId) {
        this.tpsCopfId = tpsCopfId;
    }

    public String getTpsServiceId() {
        return tpsServiceId;
    }

    public void setTpsServiceId(String tpsServiceId) {
        this.tpsServiceId = tpsServiceId;
    }

    public String getTpsSourceServiceId() {
        return tpsSourceServiceId;
    }

    public void setTpsSourceServiceId(String tpsSourceServiceId) {
        this.tpsSourceServiceId = tpsSourceServiceId;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Timestamp updatedDate) {
        if (Objects.nonNull(updatedDate))
            this.updatedDate = updatedDate.toString();
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getVpnName() {
        return vpnName;
    }

    public void setVpnName(String vpnName) {
        this.vpnName = vpnName;
    }

    public String getEstimatedDeliveryDate() {
        return estimatedDeliveryDate;
    }

    public void setEstimatedDeliveryDate(Timestamp estimatedDeliveryDate) {
        if (Objects.nonNull(estimatedDeliveryDate))
            this.estimatedDeliveryDate = estimatedDeliveryDate.toString();
    }

    public String getCommittedDeliveryDate() {
        return committedDeliveryDate;
    }

    public void setCommittedDeliveryDate(Timestamp committedDeliveryDate) {
        if (Objects.nonNull(committedDeliveryDate))
            this.committedDeliveryDate = committedDeliveryDate.toString();
    }

    public String getActualDeliveryDate() {
        return actualDeliveryDate;
    }

    public void setActualDeliveryDate(Timestamp actualDeliveryDate) {
        if (Objects.nonNull(targetedDeliveryDate))
            this.actualDeliveryDate = actualDeliveryDate.toString();
    }

    public String getTargetedDeliveryDate() {
        return targetedDeliveryDate;
    }

    public void setTargetedDeliveryDate(Timestamp targetedDeliveryDate) {
        if (Objects.nonNull(targetedDeliveryDate))
            this.targetedDeliveryDate = targetedDeliveryDate.toString();
    }

    public Float getPriority() {
        return priority;
    }

    public void setPriority(Float priority) {
        this.priority = priority;
    }

    public String getVpnSolutionId() {
        return vpnSolutionId;
    }

    public void setVpnSolutionId(String vpnSolutionId) {
        this.vpnSolutionId = vpnSolutionId;
    }

    public String getBurstableBw() {
        return burstableBw;
    }

    public void setBurstableBw(String burstableBw) {
        this.burstableBw = burstableBw;
    }

    public String getBurstableBwUnit() {
        return burstableBwUnit;
    }

    public void setBurstableBwUnit(String burstableBwUnit) {
        this.burstableBwUnit = burstableBwUnit;
    }

    public String getLastmileScenario() {
        return lastmileScenario;
    }

    public void setLastmileScenario(String lastmileScenario) {
        this.lastmileScenario = lastmileScenario;
    }

    public String getLastmileConnectionType() {
        return lastmileConnectionType;
    }

    public void setLastmileConnectionType(String lastmileConnectionType) {
        this.lastmileConnectionType = lastmileConnectionType;
    }

    public String getLineRate() {
        return lineRate;
    }

    public void setLineRate(String lineRate) {
        this.lineRate = lineRate;
    }

    public String getSiteCode() { return siteCode; }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
    }

	public String getAssignedPM() {
		return assignedPM;
	}

	public void setAssignedPM(String assignedPM) {
		this.assignedPM = assignedPM;
	}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getClrStageForAmendment() {
        return clrStageForAmendment;
    }

    public void setClrStageForAmendment(String clrStageForAmendment) {
        this.clrStageForAmendment = clrStageForAmendment;
    }

    public String getAdditionalIpPoolType() { return additionalIpPoolType; }

    public void setAdditionalIpPoolType(String additionalIpPoolType) { this.additionalIpPoolType = additionalIpPoolType; }

    public OptimusRfDataBean getOptimusRfDataBean() { return optimusRfDataBean; }

    public void setOptimusRfDataBean(OptimusRfDataBean optimusRfDataBean) { this.optimusRfDataBean = optimusRfDataBean; }

    public Integer getServiceLinkId() {
		return serviceLinkId;
	}

	public void setServiceLinkId(Integer serviceLinkId) {
		this.serviceLinkId = serviceLinkId;
	}

    public String getIsOrderAmendent() {
        return isOrderAmendent;
    }

    public void setIsOrderAmendent(String isOrderAmendent) {
        this.isOrderAmendent = isOrderAmendent;
    }

    public Double getDifferentialMrc() {
        return differentialMrc;
    }

    public void setDifferentialMrc(Double differentialMrc) {
        this.differentialMrc = differentialMrc;
    }

    public Double getDifferentialNrc() {
        return differentialNrc;
    }

    public void setDifferentialNrc(Double differentialNrc) {
        this.differentialNrc = differentialNrc;
    }


    public String getTigerOrderId() {
        return tigerOrderId;
    }

    public void setTigerOrderId(String tigerOrderId) {
        this.tigerOrderId = tigerOrderId;
    }

    public String getIsMultiVrf() {
        return isMultiVrf;
    }

    public void setIsMultiVrf(String isMultiVrf) {
        this.isMultiVrf = isMultiVrf;
    }

    public String getMasterVrfServiceId() {
        return masterVrfServiceId;
    }

    public void setMasterVrfServiceId(String masterVrfServiceId) {
        this.masterVrfServiceId = masterVrfServiceId;
    }

    public String getMultiVrfSolution() {
        return multiVrfSolution;
    }

    public void setMultiVrfSolution(String multiVrfSolution) {
        this.multiVrfSolution = multiVrfSolution;
    }

	@Override
    public String toString() {
        return "ScServiceDetailBean{" +
                "id=" + id +
                ", accessType='" + accessType + '\'' +
                ", arc=" + arc +
                ", billingAccountId='" + billingAccountId + '\'' +
                ", billingGstNumber='" + billingGstNumber + '\'' +
                ", billingRatioPercent=" + billingRatioPercent +
                ", billingType='" + billingType + '\'' +
                ", bwPortspeed='" + bwPortspeed + '\'' +
                ", bwPortspeedAltName='" + bwPortspeedAltName + '\'' +
                ", bwUnit='" + bwUnit + '\'' +
                ", callType='" + callType + '\'' +
                ", createdBy='" + createdBy + '\'' +
                ", createdDate='" + createdDate + '\'' +
                ", custOrgNo='" + custOrgNo + '\'' +
                ", demarcationBuildingName='" + demarcationBuildingName + '\'' +
                ", demarcationFloor='" + demarcationFloor + '\'' +
                ", demarcationRack='" + demarcationRack + '\'' +
                ", demarcationRoom='" + demarcationRoom + '\'' +
                ", destinationCity='" + destinationCity + '\'' +
                ", destinationCountry='" + destinationCountry + '\'' +
                ", destinationCountryCode='" + destinationCountryCode + '\'' +
                ", destinationCountryCodeRepc='" + destinationCountryCodeRepc + '\'' +
                ", discountArc=" + discountArc +
                ", discountMrc=" + discountMrc +
                ", discountNrc=" + discountNrc +
                ", serviceVariant='" + serviceVariant + '\'' +
                ", erfLocDestinationCityId='" + erfLocDestinationCityId + '\'' +
                ", erfLocDestinationCountryId=" + erfLocDestinationCountryId +
                ", erfLocPopSiteAddressId='" + erfLocPopSiteAddressId + '\'' +
                ", erfLocSiteAddressId='" + erfLocSiteAddressId + '\'' +
                ", erfLocSourceCityId='" + erfLocSourceCityId + '\'' +
                ", erfLocSrcCountryId=" + erfLocSrcCountryId +
                ", erfPrdCatalogOfferingId=" + erfPrdCatalogOfferingId +
                ", erfOdrServiceId=" + erfOdrServiceId +
                ", erfPrdCatalogOfferingName='" + erfPrdCatalogOfferingName + '\'' +
                ", erfPrdCatalogParentProductName='" + erfPrdCatalogParentProductName + '\'' +
                ", erfPrdCatalogParentProductOfferingName='" + erfPrdCatalogParentProductOfferingName + '\'' +
                ", erfPrdCatalogProductId=" + erfPrdCatalogProductId +
                ", erfPrdCatalogProductName='" + erfPrdCatalogProductName + '\'' +
                ", feasibilityId='" + feasibilityId + '\'' +
                ", gscOrderSequenceId='" + gscOrderSequenceId + '\'' +
                ", isActive='" + isActive + '\'' +
                ", isIzo='" + isIzo + '\'' +
                ", lastmileBw='" + lastmileBw + '\'' +
                ", lastmileBwAltName='" + lastmileBwAltName + '\'' +
                ", lastmileBwUnit='" + lastmileBwUnit + '\'' +
                ", lastmileProvider='" + lastmileProvider + '\'' +
                ", lastmileType='" + lastmileType + '\'' +
                ", latLong='" + latLong + '\'' +
                ", localItContactEmail='" + localItContactEmail + '\'' +
                ", localItContactMobile='" + localItContactMobile + '\'' +
                ", localItContactName='" + localItContactName + '\'' +
                ", mrc=" + mrc +
                ", nrc=" + nrc +
                ", parentBundleServiceId=" + parentBundleServiceId +
                ", parentId=" + parentId +
                ", popSiteAddress='" + popSiteAddress + '\'' +
                ", popSiteCode='" + popSiteCode + '\'' +
                ", priSecServiceLink='" + priSecServiceLink + '\'' +
                ", erdPriSecServiceLinkId=" + erdPriSecServiceLinkId +
                ", primarySecondary='" + primarySecondary + '\'' +
                ", productReferenceId=" + productReferenceId +
                ", scOrderUuid='" + scOrderUuid + '\'' +
                ", serviceClass='" + serviceClass + '\'' +
                ", serviceClassification='" + serviceClassification + '\'' +
                ", serviceCommissionedDate='" + serviceCommissionedDate + '\'' +
                ", serviceGroupId='" + serviceGroupId + '\'' +
                ", serviceGroupType='" + serviceGroupType + '\'' +
                ", serviceOption='" + serviceOption + '\'' +
                ", serviceStatus='" + serviceStatus + '\'' +
                ", serviceTerminationDate='" + serviceTerminationDate + '\'' +
                ", serviceTopology='" + serviceTopology + '\'' +
                ", siteAddress='" + siteAddress + '\'' +
                ", siteAlias='" + siteAlias + '\'' +
                ", siteEndInterface='" + siteEndInterface + '\'' +
                ", siteLinkLabel='" + siteLinkLabel + '\'' +
                ", siteTopology='" + siteTopology + '\'' +
                ", siteType='" + siteType + '\'' +
                ", slaTemplate='" + slaTemplate + '\'' +
                ", smEmail='" + smEmail + '\'' +
                ", smName='" + smName + '\'' +
                ", sourceCity='" + sourceCity + '\'' +
                ", sourceCountry='" + sourceCountry + '\'' +
                ", sourceState='" + sourceState + '\'' +
                ", destinationState='" + destinationState + '\'' +
                ", sourceAddressLineOne='" + sourceAddressLineOne + '\'' +
                ", sourceAddressLineTwo='" + sourceAddressLineTwo + '\'' +
                ", sourceLocality='" + sourceLocality + '\'' +
                ", sourcePincode='" + sourcePincode + '\'' +
                ", destinationAddressLineOne='" + destinationAddressLineOne + '\'' +
                ", destinationAddressLineTwo='" + destinationAddressLineTwo + '\'' +
                ", destinationLocality='" + destinationLocality + '\'' +
                ", destinationPincode='" + destinationPincode + '\'' +
                ", sourceCountryCode='" + sourceCountryCode + '\'' +
                ", sourceCountryCodeRepc='" + sourceCountryCodeRepc + '\'' +
                ", supplOrgNo='" + supplOrgNo + '\'' +
                ", taxExemptionFlag='" + taxExemptionFlag + '\'' +
                ", tpsCopfId='" + tpsCopfId + '\'' +
                ", tpsServiceId='" + tpsServiceId + '\'' +
                ", tpsSourceServiceId='" + tpsSourceServiceId + '\'' +
                ", updatedBy='" + updatedBy + '\'' +
                ", updatedDate='" + updatedDate + '\'' +
                ", uuid='" + uuid + '\'' +
                ", vpnName='" + vpnName + '\'' +
                ", estimatedDeliveryDate='" + estimatedDeliveryDate + '\'' +
                ", committedDeliveryDate='" + committedDeliveryDate + '\'' +
                ", actualDeliveryDate='" + actualDeliveryDate + '\'' +
                ", targetedDeliveryDate='" + targetedDeliveryDate + '\'' +
                ", priority=" + priority +
                ", vpnSolutionId='" + vpnSolutionId + '\'' +
                ", burstableBw='" + burstableBw + '\'' +
                ", burstableBwUnit='" + burstableBwUnit + '\'' +
                ", lastmileScenario='" + lastmileScenario + '\'' +
                ", lastmileConnectionType='" + lastmileConnectionType + '\'' +
                ", lineRate='" + lineRate + '\'' +
                ", siteCode='" + siteCode + '\'' +
                ", orderType='" + orderType + '\'' +
                ", status='" + status + '\'' +
                ", assignedPM='" + assignedPM + '\'' +
                ", clrStageForAmendment='" + clrStageForAmendment + '\'' +
                ", serviceLinkId='" + serviceLinkId + '\'' +
                ", tigerOrderId='" + tigerOrderId + '\'' +
                '}';
    }
}
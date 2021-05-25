package com.tcl.dias.common.servicefulfillment.beans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 
 * This file contains the ScServiceDetailBean.java class.
 * 
 *
 * @author Dimple S
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
public class ScServiceDetailBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer id;
	private String accessType;
	private Double arc;
	private String billingAccountId;
	private String billingGstNumber;
	private Double billingRatioPercent;
	private String billingType;
	private String burstableBwPortspeed;
	private String burstableBwPortspeedAltName;
	private String burstableBwUnit;
	private String bwPortspeed;
	private String bwPortspeedAltName;
	private String bwUnit;
	private String callType;
	private String createdBy;
	private Date createdDate;
	private String custOrgNo;
	private String demarcationApartment;
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
	private String erfLocDestinationCityId;
	private Integer erfLocDestinationCountryId;
	private String erfLocPopSiteAddressId;
	private String erfLocSiteAddressId;
	private String erfLocSourceCityId;
	private Integer erfLocSrcCountryId;
	private Integer erfPrdCatalogOfferingId;
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
	private String scOrderUuid;
	private Integer parentBundleServiceId;
	private Integer parentId;
	private String popSiteAddress;
	private String popSiteCode;
	// private String priSecServiceLink;

	private Integer erfPriSecServiceLinkId;

	private String primarySecondary;
	private Integer productReferenceId;
	private String serviceClass;
	private String serviceClassification;
	private Date serviceCommissionedDate;
	private String serviceGroupId;
	private String serviceGroupType;
	private String serviceOption;
	private String serviceStatus;
	private Date serviceTerminationDate;
	private String serviceTopology;
	private String siteAddress;
	private String siteAlias;
	private String siteEndInterface;
	private String siteLatLang;
	private String siteLinkLabel;
	private String siteTopology;
	private String siteType;
	private String slaTemplate;
	private String smEmail;
	private String smName;
	private String sourceCity;
	private String sourceCountry;
	private String sourcePincode;
	private String sourceLocality;
	private String sourceAddressLineOne;
	private String sourceAddressLineTwo;
	private String destinationPincode;
	private String destinationLocality;
	private String destinationState;
	private String sourceState;
	private String destinationAddressLineOne;
	private String destinationAddressLineTwo;
	private String sourceCountryCode;
	private String sourceCountryCodeRepc;
	private String supplOrgNo;
	private String taxExemptionFlag;
	private String tpsCopfId;
	private String tpsServiceId;
	private String tpsSourceServiceId;
	private String updatedBy;
	private Date updatedDate;
	private String uuid;
	private String vpnName;
	private Integer erfScServiceId;
	private ScOrderBean scOrder;
	private ScContractInfoBean scContractInfo;
	private String orderType;
	private String orderCategory;
	private String tigerOrderId;
	
	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getOrderCategory() {
		return orderCategory;
	}

	public void setOrderCategory(String orderCategory) {
		this.orderCategory = orderCategory;
	}

	private Set<ScServiceAttributeBean> scServiceAttributes = new HashSet<>();
	private Set<ScServiceSlaBean> scServiceSlas = new HashSet<>();
	private List<ScAttachmentBean> scAttachments = new ArrayList<>();
	private List<ScProductDetailBean> scProductDetail = new ArrayList<>();

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAccessType() {
		return this.accessType;
	}

	public void setAccessType(String accessType) {
		this.accessType = accessType;
	}

	public Double getArc() {
		return this.arc;
	}

	public void setArc(Double arc) {
		this.arc = arc;
	}

	public String getBillingAccountId() {
		return this.billingAccountId;
	}

	public void setBillingAccountId(String billingAccountId) {
		this.billingAccountId = billingAccountId;
	}

	public String getBillingGstNumber() {
		return this.billingGstNumber;
	}

	public void setBillingGstNumber(String billingGstNumber) {
		this.billingGstNumber = billingGstNumber;
	}

	public Double getBillingRatioPercent() {
		return this.billingRatioPercent;
	}

	public void setBillingRatioPercent(Double billingRatioPercent) {
		this.billingRatioPercent = billingRatioPercent;
	}

	public String getBillingType() {
		return this.billingType;
	}

	public void setBillingType(String billingType) {
		this.billingType = billingType;
	}

	public String getBurstableBwPortspeed() {
		return this.burstableBwPortspeed;
	}

	public void setBurstableBwPortspeed(String burstableBwPortspeed) {
		this.burstableBwPortspeed = burstableBwPortspeed;
	}

	public String getBurstableBwPortspeedAltName() {
		return this.burstableBwPortspeedAltName;
	}

	public void setBurstableBwPortspeedAltName(String burstableBwPortspeedAltName) {
		this.burstableBwPortspeedAltName = burstableBwPortspeedAltName;
	}

	public String getBurstableBwUnit() {
		return this.burstableBwUnit;
	}

	public void setBurstableBwUnit(String burstableBwUnit) {
		this.burstableBwUnit = burstableBwUnit;
	}

	public String getBwPortspeed() {
		return this.bwPortspeed;
	}

	public void setBwPortspeed(String bwPortspeed) {
		this.bwPortspeed = bwPortspeed;
	}

	public String getBwPortspeedAltName() {
		return this.bwPortspeedAltName;
	}

	public void setBwPortspeedAltName(String bwPortspeedAltName) {
		this.bwPortspeedAltName = bwPortspeedAltName;
	}

	public String getBwUnit() {
		return this.bwUnit;
	}

	public void setBwUnit(String bwUnit) {
		this.bwUnit = bwUnit;
	}

	public String getCallType() {
		return this.callType;
	}

	public void setCallType(String callType) {
		this.callType = callType;
	}

	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getCustOrgNo() {
		return this.custOrgNo;
	}

	public void setCustOrgNo(String custOrgNo) {
		this.custOrgNo = custOrgNo;
	}

	public String getDemarcationApartment() {
		return this.demarcationApartment;
	}

	public void setDemarcationApartment(String demarcationApartment) {
		this.demarcationApartment = demarcationApartment;
	}

	public String getDemarcationFloor() {
		return this.demarcationFloor;
	}

	public void setDemarcationFloor(String demarcationFloor) {
		this.demarcationFloor = demarcationFloor;
	}

	public String getDemarcationRack() {
		return this.demarcationRack;
	}

	public void setDemarcationRack(String demarcationRack) {
		this.demarcationRack = demarcationRack;
	}

	public String getDemarcationRoom() {
		return this.demarcationRoom;
	}

	public void setDemarcationRoom(String demarcationRoom) {
		this.demarcationRoom = demarcationRoom;
	}

	public String getDestinationCity() {
		return this.destinationCity;
	}

	public void setDestinationCity(String destinationCity) {
		this.destinationCity = destinationCity;
	}

	public String getDestinationCountry() {
		return this.destinationCountry;
	}

	public void setDestinationCountry(String destinationCountry) {
		this.destinationCountry = destinationCountry;
	}

	public String getDestinationCountryCode() {
		return this.destinationCountryCode;
	}

	public void setDestinationCountryCode(String destinationCountryCode) {
		this.destinationCountryCode = destinationCountryCode;
	}

	public String getDestinationCountryCodeRepc() {
		return this.destinationCountryCodeRepc;
	}

	public void setDestinationCountryCodeRepc(String destinationCountryCodeRepc) {
		this.destinationCountryCodeRepc = destinationCountryCodeRepc;
	}

	public BigDecimal getDiscountArc() {
		return this.discountArc;
	}

	public void setDiscountArc(BigDecimal discountArc) {
		this.discountArc = discountArc;
	}

	public BigDecimal getDiscountMrc() {
		return this.discountMrc;
	}

	public void setDiscountMrc(BigDecimal discountMrc) {
		this.discountMrc = discountMrc;
	}

	public BigDecimal getDiscountNrc() {
		return this.discountNrc;
	}

	public void setDiscountNrc(BigDecimal discountNrc) {
		this.discountNrc = discountNrc;
	}

	public String getErfLocDestinationCityId() {
		return this.erfLocDestinationCityId;
	}

	public void setErfLocDestinationCityId(String erfLocDestinationCityId) {
		this.erfLocDestinationCityId = erfLocDestinationCityId;
	}

	public Integer getErfLocDestinationCountryId() {
		return this.erfLocDestinationCountryId;
	}

	public void setErfLocDestinationCountryId(Integer erfLocDestinationCountryId) {
		this.erfLocDestinationCountryId = erfLocDestinationCountryId;
	}

	public String getErfLocPopSiteAddressId() {
		return this.erfLocPopSiteAddressId;
	}

	public void setErfLocPopSiteAddressId(String erfLocPopSiteAddressId) {
		this.erfLocPopSiteAddressId = erfLocPopSiteAddressId;
	}

	public String getErfLocSiteAddressId() {
		return this.erfLocSiteAddressId;
	}

	public void setErfLocSiteAddressId(String erfLocSiteAddressId) {
		this.erfLocSiteAddressId = erfLocSiteAddressId;
	}

	public String getErfLocSourceCityId() {
		return this.erfLocSourceCityId;
	}

	public void setErfLocSourceCityId(String erfLocSourceCityId) {
		this.erfLocSourceCityId = erfLocSourceCityId;
	}

	public Integer getErfLocSrcCountryId() {
		return this.erfLocSrcCountryId;
	}

	public void setErfLocSrcCountryId(Integer erfLocSrcCountryId) {
		this.erfLocSrcCountryId = erfLocSrcCountryId;
	}

	public Integer getErfPrdCatalogOfferingId() {
		return this.erfPrdCatalogOfferingId;
	}

	public void setErfPrdCatalogOfferingId(Integer erfPrdCatalogOfferingId) {
		this.erfPrdCatalogOfferingId = erfPrdCatalogOfferingId;
	}

	public String getErfPrdCatalogOfferingName() {
		return this.erfPrdCatalogOfferingName;
	}

	public void setErfPrdCatalogOfferingName(String erfPrdCatalogOfferingName) {
		this.erfPrdCatalogOfferingName = erfPrdCatalogOfferingName;
	}

	public String getErfPrdCatalogParentProductName() {
		return this.erfPrdCatalogParentProductName;
	}

	public void setErfPrdCatalogParentProductName(String erfPrdCatalogParentProductName) {
		this.erfPrdCatalogParentProductName = erfPrdCatalogParentProductName;
	}

	public String getErfPrdCatalogParentProductOfferingName() {
		return this.erfPrdCatalogParentProductOfferingName;
	}

	public void setErfPrdCatalogParentProductOfferingName(String erfPrdCatalogParentProductOfferingName) {
		this.erfPrdCatalogParentProductOfferingName = erfPrdCatalogParentProductOfferingName;
	}

	public Integer getErfPrdCatalogProductId() {
		return this.erfPrdCatalogProductId;
	}

	public void setErfPrdCatalogProductId(Integer erfPrdCatalogProductId) {
		this.erfPrdCatalogProductId = erfPrdCatalogProductId;
	}

	public String getErfPrdCatalogProductName() {
		return this.erfPrdCatalogProductName;
	}

	public void setErfPrdCatalogProductName(String erfPrdCatalogProductName) {
		this.erfPrdCatalogProductName = erfPrdCatalogProductName;
	}

	public String getFeasibilityId() {
		return this.feasibilityId;
	}

	public void setFeasibilityId(String feasibilityId) {
		this.feasibilityId = feasibilityId;
	}

	public String getGscOrderSequenceId() {
		return this.gscOrderSequenceId;
	}

	public void setGscOrderSequenceId(String gscOrderSequenceId) {
		this.gscOrderSequenceId = gscOrderSequenceId;
	}

	public String getIsActive() {
		return this.isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public String getIsIzo() {
		return this.isIzo;
	}

	public void setIsIzo(String isIzo) {
		this.isIzo = isIzo;
	}

	public String getLastmileBw() {
		return this.lastmileBw;
	}

	public void setLastmileBw(String lastmileBw) {
		this.lastmileBw = lastmileBw;
	}

	public String getLastmileBwAltName() {
		return this.lastmileBwAltName;
	}

	public void setLastmileBwAltName(String lastmileBwAltName) {
		this.lastmileBwAltName = lastmileBwAltName;
	}

	public String getLastmileBwUnit() {
		return this.lastmileBwUnit;
	}

	public void setLastmileBwUnit(String lastmileBwUnit) {
		this.lastmileBwUnit = lastmileBwUnit;
	}

	public String getLastmileProvider() {
		return this.lastmileProvider;
	}

	public void setLastmileProvider(String lastmileProvider) {
		this.lastmileProvider = lastmileProvider;
	}

	public String getLastmileType() {
		return this.lastmileType;
	}

	public void setLastmileType(String lastmileType) {
		this.lastmileType = lastmileType;
	}

	public String getLatLong() {
		return this.latLong;
	}

	public void setLatLong(String latLong) {
		this.latLong = latLong;
	}

	public String getLocalItContactEmail() {
		return this.localItContactEmail;
	}

	public void setLocalItContactEmail(String localItContactEmail) {
		this.localItContactEmail = localItContactEmail;
	}

	public String getLocalItContactMobile() {
		return this.localItContactMobile;
	}

	public void setLocalItContactMobile(String localItContactMobile) {
		this.localItContactMobile = localItContactMobile;
	}

	public String getLocalItContactName() {
		return this.localItContactName;
	}

	public void setLocalItContactName(String localItContactName) {
		this.localItContactName = localItContactName;
	}

	public Double getMrc() {
		return this.mrc;
	}

	public void setMrc(Double mrc) {
		this.mrc = mrc;
	}

	public Double getNrc() {
		return this.nrc;
	}

	public void setNrc(Double nrc) {
		this.nrc = nrc;
	}

	public String getScOrderUuid() {
		return this.scOrderUuid;
	}

	public void setScOrderUuid(String scOrderUuid) {
		this.scOrderUuid = scOrderUuid;
	}

	public Integer getParentBundleServiceId() {
		return this.parentBundleServiceId;
	}

	public void setParentBundleServiceId(Integer parentBundleServiceId) {
		this.parentBundleServiceId = parentBundleServiceId;
	}

	public Integer getParentId() {
		return this.parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public String getPopSiteAddress() {
		return this.popSiteAddress;
	}

	public void setPopSiteAddress(String popSiteAddress) {
		this.popSiteAddress = popSiteAddress;
	}

	public String getPopSiteCode() {
		return this.popSiteCode;
	}

	public void setPopSiteCode(String popSiteCode) {
		this.popSiteCode = popSiteCode;
	}

	/*
	 * public String getPriSecServiceLink() { return this.priSecServiceLink; }
	 * 
	 * public void setPriSecServiceLink(String priSecServiceLink) {
	 * this.priSecServiceLink = priSecServiceLink; }
	 */

	public String getPrimarySecondary() {
		return this.primarySecondary;
	}

	public Integer getErfPriSecServiceLinkId() {
		return erfPriSecServiceLinkId;
	}

	public void setErfPriSecServiceLinkId(Integer erfPriSecServiceLinkId) {
		this.erfPriSecServiceLinkId = erfPriSecServiceLinkId;
	}

	public void setPrimarySecondary(String primarySecondary) {
		this.primarySecondary = primarySecondary;
	}

	public Integer getProductReferenceId() {
		return this.productReferenceId;
	}

	public void setProductReferenceId(Integer productReferenceId) {
		this.productReferenceId = productReferenceId;
	}

	public String getServiceClass() {
		return this.serviceClass;
	}

	public void setServiceClass(String serviceClass) {
		this.serviceClass = serviceClass;
	}

	public String getServiceClassification() {
		return this.serviceClassification;
	}

	public void setServiceClassification(String serviceClassification) {
		this.serviceClassification = serviceClassification;
	}

	public Date getServiceCommissionedDate() {
		return this.serviceCommissionedDate;
	}

	public void setServiceCommissionedDate(Date serviceCommissionedDate) {
		this.serviceCommissionedDate = serviceCommissionedDate;
	}

	public String getServiceGroupId() {
		return this.serviceGroupId;
	}

	public void setServiceGroupId(String serviceGroupId) {
		this.serviceGroupId = serviceGroupId;
	}

	public String getServiceGroupType() {
		return this.serviceGroupType;
	}

	public void setServiceGroupType(String serviceGroupType) {
		this.serviceGroupType = serviceGroupType;
	}

	public String getServiceOption() {
		return this.serviceOption;
	}

	public void setServiceOption(String serviceOption) {
		this.serviceOption = serviceOption;
	}

	public String getServiceStatus() {
		return this.serviceStatus;
	}

	public void setServiceStatus(String serviceStatus) {
		this.serviceStatus = serviceStatus;
	}

	public Date getServiceTerminationDate() {
		return this.serviceTerminationDate;
	}

	public void setServiceTerminationDate(Date serviceTerminationDate) {
		this.serviceTerminationDate = serviceTerminationDate;
	}

	public String getServiceTopology() {
		return this.serviceTopology;
	}

	public void setServiceTopology(String serviceTopology) {
		this.serviceTopology = serviceTopology;
	}

	public String getSiteAddress() {
		return this.siteAddress;
	}

	public void setSiteAddress(String siteAddress) {
		this.siteAddress = siteAddress;
	}

	public String getSiteAlias() {
		return this.siteAlias;
	}

	public void setSiteAlias(String siteAlias) {
		this.siteAlias = siteAlias;
	}

	public String getSiteEndInterface() {
		return this.siteEndInterface;
	}

	public void setSiteEndInterface(String siteEndInterface) {
		this.siteEndInterface = siteEndInterface;
	}

	public String getSiteLatLang() {
		return this.siteLatLang;
	}

	public void setSiteLatLang(String siteLatLang) {
		this.siteLatLang = siteLatLang;
	}

	public String getSiteLinkLabel() {
		return this.siteLinkLabel;
	}

	public void setSiteLinkLabel(String siteLinkLabel) {
		this.siteLinkLabel = siteLinkLabel;
	}

	public String getSiteTopology() {
		return this.siteTopology;
	}

	public void setSiteTopology(String siteTopology) {
		this.siteTopology = siteTopology;
	}

	public String getSiteType() {
		return this.siteType;
	}

	public void setSiteType(String siteType) {
		this.siteType = siteType;
	}

	public String getSlaTemplate() {
		return this.slaTemplate;
	}

	public void setSlaTemplate(String slaTemplate) {
		this.slaTemplate = slaTemplate;
	}

	public String getSmEmail() {
		return this.smEmail;
	}

	public void setSmEmail(String smEmail) {
		this.smEmail = smEmail;
	}

	public String getSmName() {
		return this.smName;
	}

	public void setSmName(String smName) {
		this.smName = smName;
	}

	public String getSourceCity() {
		return this.sourceCity;
	}

	public void setSourceCity(String sourceCity) {
		this.sourceCity = sourceCity;
	}

	public String getSourceCountry() {
		return this.sourceCountry;
	}

	public void setSourceCountry(String sourceCountry) {
		this.sourceCountry = sourceCountry;
	}

	public String getSourceCountryCode() {
		return this.sourceCountryCode;
	}

	public void setSourceCountryCode(String sourceCountryCode) {
		this.sourceCountryCode = sourceCountryCode;
	}

	public String getSourceCountryCodeRepc() {
		return this.sourceCountryCodeRepc;
	}

	public void setSourceCountryCodeRepc(String sourceCountryCodeRepc) {
		this.sourceCountryCodeRepc = sourceCountryCodeRepc;
	}

	public String getSupplOrgNo() {
		return this.supplOrgNo;
	}

	public void setSupplOrgNo(String supplOrgNo) {
		this.supplOrgNo = supplOrgNo;
	}

	public String getTaxExemptionFlag() {
		return this.taxExemptionFlag;
	}

	public void setTaxExemptionFlag(String taxExemptionFlag) {
		this.taxExemptionFlag = taxExemptionFlag;
	}

	public String getTpsCopfId() {
		return this.tpsCopfId;
	}

	public void setTpsCopfId(String tpsCopfId) {
		this.tpsCopfId = tpsCopfId;
	}

	public String getTpsServiceId() {
		return this.tpsServiceId;
	}

	public void setTpsServiceId(String tpsServiceId) {
		this.tpsServiceId = tpsServiceId;
	}

	public String getTpsSourceServiceId() {
		return this.tpsSourceServiceId;
	}

	public void setTpsSourceServiceId(String tpsSourceServiceId) {
		this.tpsSourceServiceId = tpsSourceServiceId;
	}

	public String getUpdatedBy() {
		return this.updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedDate() {
		return this.updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public String getUuid() {
		return this.uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getVpnName() {
		return this.vpnName;
	}

	public void setVpnName(String vpnName) {
		this.vpnName = vpnName;
	}

	public Set<ScServiceAttributeBean> getScServiceAttributes() {
		return this.scServiceAttributes;
	}

	public void setScServiceAttributes(Set<ScServiceAttributeBean> scServiceAttributes) {
		this.scServiceAttributes = scServiceAttributes;
	}

	public ScServiceAttributeBean addScServiceAttribute(ScServiceAttributeBean scServiceAttribute) {
		getScServiceAttributes().add(scServiceAttribute);
		scServiceAttribute.setScServiceDetail(this);
		return scServiceAttribute;
	}

	public ScServiceAttributeBean removeScServiceAttribute(ScServiceAttributeBean scServiceAttribute) {
		getScServiceAttributes().remove(scServiceAttribute);
		scServiceAttribute.setScServiceDetail(null);
		return scServiceAttribute;
	}

	public ScOrderBean getScOrder() {
		return this.scOrder;
	}

	public void setScOrder(ScOrderBean scOrder) {
		this.scOrder = scOrder;
	}

	public ScContractInfoBean getScContractInfo() {
		return this.scContractInfo;
	}

	public void setScContractInfo(ScContractInfoBean scContractInfo) {
		this.scContractInfo = scContractInfo;
	}

	public Set<ScServiceSlaBean> getScServiceSlas() {
		return this.scServiceSlas;
	}

	public void setScServiceSlas(Set<ScServiceSlaBean> scServiceSlas) {
		this.scServiceSlas = scServiceSlas;
	}

	public List<ScAttachmentBean> getScAttachments() {
		return scAttachments;
	}

	public void setScAttachments(List<ScAttachmentBean> scAttachments) {
		this.scAttachments = scAttachments;
	}

	public String getSourcePincode() {
		return sourcePincode;
	}

	public void setSourcePincode(String sourcePincode) {
		this.sourcePincode = sourcePincode;
	}

	public String getSourceLocality() {
		return sourceLocality;
	}

	public void setSourceLocality(String sourceLocality) {
		this.sourceLocality = sourceLocality;
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

	public String getDestinationPincode() {
		return destinationPincode;
	}

	public void setDestinationPincode(String destinationPincode) {
		this.destinationPincode = destinationPincode;
	}

	public String getDestinationLocality() {
		return destinationLocality;
	}

	public void setDestinationLocality(String destinationLocality) {
		this.destinationLocality = destinationLocality;
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

	public String getDestinationState() {
		return destinationState;
	}

	public void setDestinationState(String destinationState) {
		this.destinationState = destinationState;
	}

	public String getSourceState() {
		return sourceState;
	}

	public void setSourceState(String sourceState) {
		this.sourceState = sourceState;
	}

	public Integer getErfScServiceId() {
		return erfScServiceId;
	}

	public void setErfScServiceId(Integer erfScServiceId) {
		this.erfScServiceId = erfScServiceId;
	}

	public ScServiceSlaBean addScServiceSla(ScServiceSlaBean scServiceSla) {
		getScServiceSlas().add(scServiceSla);
		scServiceSla.setScServiceDetail(this);
		return scServiceSla;
	}

	public ScServiceSlaBean removeScServiceSla(ScServiceSlaBean scServiceSla) {
		getScServiceSlas().remove(scServiceSla);
		scServiceSla.setScServiceDetail(null);
		return scServiceSla;
	}

	public List<ScProductDetailBean> getScProductDetail() {
		return scProductDetail;
	}

	public void setOdrProductDetail(List<ScProductDetailBean> scProductDetail) {
		this.scProductDetail = scProductDetail;
	}

	public String getTigerOrderId() {
		return tigerOrderId;
	}

	public void setTigerOrderId(String tigerOrderId) {
		this.tigerOrderId = tigerOrderId;
	}
}
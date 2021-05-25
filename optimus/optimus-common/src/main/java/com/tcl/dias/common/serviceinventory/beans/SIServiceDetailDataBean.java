package com.tcl.dias.common.serviceinventory.beans;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tcl.dias.common.beans.SIComponentBean;
import com.tcl.dias.common.beans.SIContractInfoBean;

/**
 * Bean class to contain Service Inventory service detail data
 *
 * @author Ramasubramanian Sankar
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 *
 */
@JsonInclude(NON_NULL)
public class SIServiceDetailDataBean {
	private Integer id;
	private String billingAccountId;
	private String billingType;
	private String erfLocGscDestinationCityId;
	private Integer erfLocGscDestinationCountryId;
	private String erfLocSiteAddressId;
	private String erfLocGscSourceCityId;
	private Integer erfLocGscSrcCountryId;
	private Integer erfPrdCatalogOfferingId;
	private String erfPrdCatalogOfferingName;
	private String erfPrdCatalogParentProductName;
	private String erfPrdCatalogParentProductOfferingName;
	private Integer erfPrdCatalogProductId;
	private String erfPrdCatalogProductName;
	private String feasibilityId;
	private String gscDestinationCity;
	private String gscDestinationCountry;
	private String destinationCountryCode;
	private String gscSourceCity;
	private String gscSourceCountry;
	private String sourceCountryCode;
	private Integer parentBundleServiceId;
	private String orderUuid;
	private String tpsServiceId;
	private String tpsSourceServiceId;
	private String uuid;
	private String gscProductName;
	private String accessType;
	private String alias;
	private String linkId;
	private String linkType;
	private String ipAddressArrangementType;
	private String serviceOption;
	//ILL and GVPN specific
	private Double mrc;
	private Double nrc;
	private Double arc;
    private String lastmileBw;
    private String lastmileBwUnit;
    private String lmType;
    private String lastMileProvider;
    private String portBw;
    private String portBwUnit;
    private String priSecServLink;
	private Timestamp serviceCommissionedDate;
    private Double contractTerm;
    private Date contractStartDate;
	private Date contractEndDate;
    private String latLong;
    private String vpnName;
	private String accessProvider;
	private String siteAddress;
    private List<SIAttributeBean> attributes;
    private List<SIComponentBean> components;
    private List<SIAssetBean> assets;
    private List<SIAssetRelationBean> assetRelations;
    private SIContractInfoBean contractInfo;
    private String referenceOrderId;
    private String billingFrequency;
    private String billingMethod;
    private String paymentTerm;
    private String createdBy;
    private String updatedBy;
    private Timestamp updatedDate;
    private Timestamp createdDate;
    private String siteTopology;
    private String serviceTopology;
    private String siteType;
	private String demarcationApartment;
	private String demarcationFloor;
	private String demarcationRack;
	private String demarcationRoom;
    private String siteEndInterface;
    private String sourceCity;
    private String destinationCity;
    private String accountManager;
    private Integer tpsCrmCofId;
    private String orderType;
    private String totalVrfBandwith;
    private String tpsCopfId;
	private String orderCode;
	private Integer tpsSfdcCuId;
	private String orderCategory;
	private String orderSubCategory;
	private String burstableBw;
	private String burstableBwUnit;
	private Date circuitExpiryDate;
	private String billingCurrency;
	private String portMode;
    private String sCommisionDate;
	private String ipv4AddressPoolsize;
	private String crossConnectType;



	public String getCrossConnectType() {
		return crossConnectType;
	}

	public void setCrossConnectType(String crossConnectType) {
		this.crossConnectType = crossConnectType;
	}

    private String currentOpportunityType;

	public String getsCommisionDate() {
		return sCommisionDate;
	}
	public void setsCommisionDate(String sCommisionDate) {
		this.sCommisionDate = sCommisionDate;
	}
	public String getIpv4AddressPoolsize() {
		return ipv4AddressPoolsize;
	}
	public void setIpv4AddressPoolsize(String ipv4AddressPoolsize) {
		this.ipv4AddressPoolsize = ipv4AddressPoolsize;
	}
	
	public String getBillingCurrency() {
			return billingCurrency;
		}
	public void setBillingCurrency(String billingCurrency) {
			this.billingCurrency = billingCurrency;
		}
	public String getPortMode() {
			return portMode;
		}
	public void setPortMode(String portMode) {
			this.portMode = portMode;
		}


	
	public String getTotalVrfBandwith() {
		return totalVrfBandwith;
	}
	public void setTotalVrfBandwith(String totalVrfBandwith) {
		this.totalVrfBandwith = totalVrfBandwith;
	}


	public String getPriSecServLink() {
		return priSecServLink;
	}

	public void setPriSecServLink(String priSecServLink) {
		this.priSecServLink = priSecServLink;
	}


	public String getErfLocSiteAddressId() {
		return erfLocSiteAddressId;
	}

	public void setErfLocSiteAddressId(String erfLocSiteAddressId) {
		this.erfLocSiteAddressId = erfLocSiteAddressId;
	}

	public String getVpnName() {
		return vpnName;
	}

	public void setVpnName(String vpnName) {
		this.vpnName = vpnName;
	}

	public String getLatLong() {
        return latLong;
    }

    public void setLatLong(String latLong) {
        this.latLong = latLong;
    }

    public Double getContractTerm() {
        return contractTerm;
    }

    public void setContractTerm(Double contractTerm) {
        this.contractTerm = contractTerm;
    }

    public String getLastmileBw() {
		return lastmileBw;
	}

	public void setLastmileBw(String lastmileBw) {
		this.lastmileBw = lastmileBw;
	}

	public String getLastmileBwUnit() {
		return lastmileBwUnit;
	}

	public void setLastmileBwUnit(String lastmileBwUnit) {
		this.lastmileBwUnit = lastmileBwUnit;
	}

	public String getPortBw() {
		return portBw;
	}

	public void setPortBw(String portBw) {
		this.portBw = portBw;
	}

	public Timestamp getServiceCommissionedDate() {
		return serviceCommissionedDate;
	}

	public void setServiceCommissionedDate(Timestamp serviceCommissionedDate) {
		this.serviceCommissionedDate = serviceCommissionedDate;
	}


	public String getPortBwUnit() {
		return portBwUnit;
	}

	public void setPortBwUnit(String portBwUnit) {
		this.portBwUnit = portBwUnit;
	}



	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}


	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getLinkId() {
		return linkId;
	}

	public void setLinkId(String linkId) {
		this.linkId = linkId;
	}


	public String getBillingAccountId() {
		return billingAccountId;
	}

	public void setBillingAccountId(String billingAccountId) {
		this.billingAccountId = billingAccountId;
	}

	public String getBillingType() {
		return billingType;
	}

	public void setBillingType(String billingType) {
		this.billingType = billingType;
	}

	public String getErfLocGscDestinationCityId() {
		return erfLocGscDestinationCityId;
	}

	public void setErfLocGscDestinationCityId(String erfLocGscDestinationCityId) {
		this.erfLocGscDestinationCityId = erfLocGscDestinationCityId;
	}

	public Integer getErfLocGscDestinationCountryId() {
		return erfLocGscDestinationCountryId;
	}

	public void setErfLocGscDestinationCountryId(Integer erfLocGscDestinationCountryId) {
		this.erfLocGscDestinationCountryId = erfLocGscDestinationCountryId;
	}

	public String getErfLocGscSourceCityId() {
		return erfLocGscSourceCityId;
	}

	public void setErfLocGscSourceCityId(String erfLocGscSourceCityId) {
		this.erfLocGscSourceCityId = erfLocGscSourceCityId;
	}

	public Integer getErfLocGscSrcCountryId() {
		return erfLocGscSrcCountryId;
	}

	public void setErfLocGscSrcCountryId(Integer erfLocGscSrcCountryId) {
		this.erfLocGscSrcCountryId = erfLocGscSrcCountryId;
	}

	public Integer getErfPrdCatalogOfferingId() {
		return erfPrdCatalogOfferingId;
	}

	public void setErfPrdCatalogOfferingId(Integer erfPrdCatalogOfferingId) {
		this.erfPrdCatalogOfferingId = erfPrdCatalogOfferingId;
	}

	public String getErfPrdCatalogOfferingName() {
		return erfPrdCatalogOfferingName;
	}

	public void setErfPrdCatalogOfferingName(String erfPrdCatalogOfferingName) {
		this.erfPrdCatalogOfferingName = erfPrdCatalogOfferingName;
		String[] parts = erfPrdCatalogOfferingName.split(" on ");
		if(parts.length > 0) {
			gscProductName = parts[0];
			if(parts.length > 1) {
				accessType = parts[1];
			}
		}
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

	public String getGscDestinationCity() {
		return gscDestinationCity;
	}

	public void setGscDestinationCity(String gscDestinationCity) {
		this.gscDestinationCity = gscDestinationCity;
	}

	public String getGscDestinationCountry() {
		return gscDestinationCountry;
	}

	public void setGscDestinationCountry(String gscDestinationCountry) {
		this.gscDestinationCountry = gscDestinationCountry;
	}

	public String getGscSourceCity() {
		return gscSourceCity;
	}

	public void setGscSourceCity(String gscSourceCity) {
		this.gscSourceCity = gscSourceCity;
	}

	public String getGscSourceCountry() {
		return gscSourceCountry;
	}

	public void setGscSourceCountry(String gscSourceCountry) {
		this.gscSourceCountry = gscSourceCountry;
	}

	public Integer getParentBundleServiceId() {
		return parentBundleServiceId;
	}

	public void setParentBundleServiceId(Integer parentBundleServiceId) {
		this.parentBundleServiceId = parentBundleServiceId;
	}

	public String getOrderUuid() {
		return orderUuid;
	}

	public void setOrderUuid(String orderUuid) {
		this.orderUuid = orderUuid;
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

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public List<SIAttributeBean> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<SIAttributeBean> attributes) {
		this.attributes = attributes;
	}

	public List<SIAssetBean> getAssets() {
		return assets;
	}

	public void setAssets(List<SIAssetBean> assets) {
		this.assets = assets;
	}

	public String getGscProductName() {
		return gscProductName;
	}

	public void setGscProductName(String gscProductName) {
		this.gscProductName = gscProductName;
	}

	public String getAccessType() {
		return accessType;
	}

	public void setAccessType(String accessType) {
		this.accessType = accessType;
	}

	public List<SIAssetRelationBean> getAssetRelations() {
		return assetRelations;
	}

	public void setAssetRelations(List<SIAssetRelationBean> assetRelations) {
		this.assetRelations = assetRelations;
	}

	public String getDestinationCountryCode() {
		return destinationCountryCode;
	}

	public void setDestinationCountryCode(String destinationCountryCode) {
		this.destinationCountryCode = destinationCountryCode;
	}

	public String getSourceCountryCode() {
		return sourceCountryCode;
	}

	public void setSourceCountryCode(String sourceCountryCode) {
		this.sourceCountryCode = sourceCountryCode;
	}
	
	//ILL and GVPN specific
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

	public Double getArc() {
		return arc;
	}

	public void setArc(Double arc) {
		this.arc = arc;
	}


	public String getLinkType() {
		return linkType;
	}

	public void setLinkType(String linkType) {
		this.linkType = linkType;
	}

	public String getAccessProvider() {
		return accessProvider;
	}

	public void setAccessProvider(String accessProvider) {
		this.accessProvider = accessProvider;
	}



	public String getReferenceOrderId() {
		return referenceOrderId;
	}

	public void setReferenceOrderId(String referenceOrderId) {
		this.referenceOrderId = referenceOrderId;
	}


	public String getSiteAddress() {
		return siteAddress;
	}

	public void setSiteAddress(String siteAddress) {
		this.siteAddress = siteAddress;
	}

	public String getBillingFrequency() { return billingFrequency; }

	public void setBillingFrequency(String billingFrequency) { this.billingFrequency = billingFrequency; }

	public String getBillingMethod() { return billingMethod; }

	public void setBillingMethod(String billingMethod) { this.billingMethod = billingMethod; }

	public String getPaymentTerm() { return paymentTerm; }

	public void setPaymentTerm(String paymentTerm) { this.paymentTerm = paymentTerm; }
	
	public Date getContractStartDate() {
		return contractStartDate;
	}

	public void setContractStartDate(Date contractStartDate) {
		this.contractStartDate = contractStartDate;
	}

	public Date getContractEndDate() {
		return contractEndDate;
	}

	public void setContractEndDate(Date contractEndDate) {
		this.contractEndDate = contractEndDate;
	}


	public List<SIComponentBean> getComponents() {
		return components;
	}

	public void setComponents(List<SIComponentBean> components) {
		this.components = components;
	}
	
	public SIContractInfoBean getContractInfo() {
		return contractInfo;
	}

	public void setContractInfo(SIContractInfoBean contractInfo) {
		this.contractInfo = contractInfo;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Timestamp getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Timestamp updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Timestamp getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	public String getIpAddressArrangementType() {
		return ipAddressArrangementType;
	}

	public void setIpAddressArrangementType(String ipAddressArrangementType) {
		this.ipAddressArrangementType = ipAddressArrangementType;
	}

	public String getServiceOption() {
		return serviceOption;
	}

	public void setServiceOption(String serviceOption) {
		this.serviceOption = serviceOption;
	}

	public String getLmType() {
		return lmType;
	}

	public void setLmType(String lmType) {
		this.lmType = lmType;
	}

	public String getLastMileProvider() {
		return lastMileProvider;
	}

	public void setLastMileProvider(String lastMileProvider) {
		this.lastMileProvider = lastMileProvider;
	}

	public String getSiteTopology() {
		return siteTopology;
	}

	public void setSiteTopology(String siteTopology) {
		this.siteTopology = siteTopology;
	}

	public String getServiceTopology() {
		return serviceTopology;
	}

	public void setServiceTopology(String serviceTopology) {
		this.serviceTopology = serviceTopology;
	}

	public String getSiteType() {
		return siteType;
	}

	public void setSiteType(String siteType) {
		this.siteType = siteType;
	}
	
	

	public String getDemarcationApartment() {
		return demarcationApartment;
	}

	public void setDemarcationApartment(String demarcationApartment) {
		this.demarcationApartment = demarcationApartment;
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

	public String getSiteEndInterface() {
		return siteEndInterface;
	}

	public void setSiteEndInterface(String siteEndInterface) {
		this.siteEndInterface = siteEndInterface;
	}
	
	

	public String getSourceCity() {
		return sourceCity;
	}

	public void setSourceCity(String sourceCity) {
		this.sourceCity = sourceCity;
	}
	
	

	public String getDestinationCity() {
		return destinationCity;
	}
	public void setDestinationCity(String destinationCity) {
		this.destinationCity = destinationCity;
	}
	public String getAccountManager() {
		return accountManager;
	}
	public void setAccountManager(String accountManager) {
		this.accountManager = accountManager;
	}
	public Integer getTpsCrmCofId() {
		return tpsCrmCofId;
	}

	public void setTpsCrmCofId(Integer tpsCrmCofId) {
		this.tpsCrmCofId = tpsCrmCofId;
	}
	
	

	public String getTpsCopfId() {
		return tpsCopfId;
	}
	public void setTpsCopfId(String tpsCopfId) {
		this.tpsCopfId = tpsCopfId;
	}
	
	
	public String getOrderCode() {
		return orderCode;
	}
	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}
	
	
	
	public Integer getTpsSfdcCuId() {
		return tpsSfdcCuId;
	}
	public void setTpsSfdcCuId(Integer tpsSfdcCuId) {
		this.tpsSfdcCuId = tpsSfdcCuId;
	}
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
	public String getOrderSubCategory() {
		return orderSubCategory;
	}
	public void setOrderSubCategory(String orderSubCategory) {
		this.orderSubCategory = orderSubCategory;
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
	
	
	public Date getCircuitExpiryDate() {
		return circuitExpiryDate;
	}
	public void setCircuitExpiryDate(Date date) {
		this.circuitExpiryDate = date;
	}

	public String getCurrentOpportunityType() {
		return currentOpportunityType;
	}

	public void setCurrentOpportunityType(String currentOpportunityType) {
		this.currentOpportunityType = currentOpportunityType;
	}

	@Override
	public String toString() {
		return "SIServiceDetailDataBean [id=" + id + ", billingAccountId=" + billingAccountId + ", billingType="
				+ billingType + ", erfLocGscDestinationCityId=" + erfLocGscDestinationCityId
				+ ", erfLocGscDestinationCountryId=" + erfLocGscDestinationCountryId + ", erfLocSiteAddressId="
				+ erfLocSiteAddressId + ", erfLocGscSourceCityId=" + erfLocGscSourceCityId + ", erfLocGscSrcCountryId="
				+ erfLocGscSrcCountryId + ", erfPrdCatalogOfferingId=" + erfPrdCatalogOfferingId
				+ ", erfPrdCatalogOfferingName=" + erfPrdCatalogOfferingName + ", erfPrdCatalogParentProductName="
				+ erfPrdCatalogParentProductName + ", erfPrdCatalogParentProductOfferingName="
				+ erfPrdCatalogParentProductOfferingName + ", erfPrdCatalogProductId=" + erfPrdCatalogProductId
				+ ", erfPrdCatalogProductName=" + erfPrdCatalogProductName + ", feasibilityId=" + feasibilityId
				+ ", gscDestinationCity=" + gscDestinationCity + ", gscDestinationCountry=" + gscDestinationCountry
				+ ", destinationCountryCode=" + destinationCountryCode + ", gscSourceCity=" + gscSourceCity
				+ ", gscSourceCountry=" + gscSourceCountry + ", sourceCountryCode=" + sourceCountryCode
				+ ", parentBundleServiceId=" + parentBundleServiceId + ", orderUuid=" + orderUuid + ", tpsServiceId="
				+ tpsServiceId + ", tpsSourceServiceId=" + tpsSourceServiceId + ", uuid=" + uuid + ", gscProductName="
				+ gscProductName + ", accessType=" + accessType + ", alias=" + alias + ", linkId=" + linkId
				+ ", linkType=" + linkType + ", ipAddressArrangementType=" + ipAddressArrangementType
				+ ", serviceOption=" + serviceOption + ", mrc=" + mrc + ", nrc=" + nrc + ", arc=" + arc
				+ ", lastmileBw=" + lastmileBw + ", lastmileBwUnit=" + lastmileBwUnit + ", lmType=" + lmType
				+ ", lastMileProvider=" + lastMileProvider + ", portBw=" + portBw + ", portBwUnit=" + portBwUnit
				+ ", priSecServLink=" + priSecServLink + ", serviceCommissionedDate=" + serviceCommissionedDate
				+ ", contractTerm=" + contractTerm + ", contractStartDate=" + contractStartDate + ", contractEndDate="
				+ contractEndDate + ", latLong=" + latLong + ", vpnName=" + vpnName + ", accessProvider="
				+ accessProvider + ", siteAddress=" + siteAddress + ", attributes=" + attributes + ", components="
				+ components + ", assets=" + assets + ", assetRelations=" + assetRelations + ", contractInfo="
				+ contractInfo + ", referenceOrderId=" + referenceOrderId + ", billingFrequency=" + billingFrequency
				+ ", billingMethod=" + billingMethod + ", paymentTerm=" + paymentTerm + ", createdBy=" + createdBy
				+ ", updatedBy=" + updatedBy + ", updatedDate=" + updatedDate + ", createdDate=" + createdDate
				+ ", siteTopology=" + siteTopology + ", serviceTopology=" + serviceTopology + ", siteType=" + siteType
				+ ", demarcationApartment=" + demarcationApartment + ", demarcationFloor=" + demarcationFloor
				+ ", demarcationRack=" + demarcationRack + ", demarcationRoom=" + demarcationRoom
				+ ", siteEndInterface=" + siteEndInterface + ", sourceCity=" + sourceCity + ", destinationCity="
				+ destinationCity + ", accountManager=" + accountManager + ", tpsCrmCofId=" + tpsCrmCofId
				+ ", orderType=" + orderType + ", totalVrfBandwith=" + totalVrfBandwith + ", tpsCopfId=" + tpsCopfId
				+ ", orderCode=" + orderCode + ", tpsSfdcCuId=" + tpsSfdcCuId + ", orderCategory=" + orderCategory
				+ ", orderSubCategory=" + orderSubCategory + ", burstableBw=" + burstableBw + ", burstableBwUnit="
				+ burstableBwUnit + ", circuitExpiryDate=" + circuitExpiryDate + ", billingCurrency=" + billingCurrency
				+ ", portMode=" + portMode + ", sCommisionDate=" + sCommisionDate + ", ipv4AddressPoolsize="
				+ ipv4AddressPoolsize + ", currentOpportunityType=" + currentOpportunityType + "]";
	}

}

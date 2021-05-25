package com.tcl.dias.common.serviceinventory.beans;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Set;

import com.tcl.dias.common.beans.SIComponentAttributeBean;
import com.tcl.dias.common.beans.SIComponentBean;
/**
 * Bean class to hold SI service detail
 *
 * @author ANNE NISHA
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class SIServiceDetailsBean {
	
	private Integer id;
	private String billingAccountId;
	private String billingType;
	private String erfLocGscDestinationCityId;
	private Integer erfLocGscDestinationCountryId;
	private Integer erfLocSiteAddressId;
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
	//ILL and GVPN specific
	private Double mrc;
	private Double nrc;
	private Double arc;
    private String lastmileBw;
    private String lastmileBwUnit;
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
    private List<SIAssetBean> assets;
    private List<SIAssetRelationBean> assetRelations;
    private Integer referenceOrderId;
    private String billingFrequency;
    private String billingMethod;
    private String paymentTerm;
    private Integer erfCustomerLeId;
    private String erfCustomerLeName;
    private Integer erfCustomerId;
    private String erfCustomerName;
    private Integer erfSpLeId;
    private String erfSpLeName;
    private Integer customerCurrencyId;
    private Integer parentOpportunityId;
    private Set<SIServiceAttributeBean> assetAttributes;
    private String serviceManagementOption;
    private String serviceTopology;
    private String siteTopology;
    private String supportType;
    private String scopeOfManagement;
	private String partnerId;
	private String orderPartnerName;
	private String partnerCuid;
	private String erfCustPartnerLeId;
	private String opportunityType;


	private String demarcationApartment;
	private String demarcationFloor;
	private String demarcationRack;
	private String demarcationRoom;
	private String lastMileType;


	private String srvSiteType;

	private String demoFlag;
	private String demoType;

	private String totalVrfBandwith;
	
	private String tpsCopfId;
	private String orderCode;
	private Integer tpsSfdcCuid;
	private String orderCategory;
	private String orderSubCategory;
	private String burstableBw;
	private String burstableBwUnit;
	private Date circuitExpiryDate;
	private List<SIComponentBean> componentBean;
	private String siteEndInterface;
    private String sourceCity;
    private String destinationCity;
    private String accountManager;

    private String billingCurrency;
	private String portMode;
    private String sCommisionDate;
    private String ipv4AddressPoolsize;
     
    private String currentOpportunityType;

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
	public String getTotalVrfBandwith() {
		return totalVrfBandwith;
	}
	public void setTotalVrfBandwith(String totalVrfBandwith) {
		this.totalVrfBandwith = totalVrfBandwith;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
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
	public Integer getErfLocSiteAddressId() {
		return erfLocSiteAddressId;
	}
	public void setErfLocSiteAddressId(Integer erfLocSiteAddressId) {
		this.erfLocSiteAddressId = erfLocSiteAddressId;
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
	public String getDestinationCountryCode() {
		return destinationCountryCode;
	}
	public void setDestinationCountryCode(String destinationCountryCode) {
		this.destinationCountryCode = destinationCountryCode;
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
	public String getSourceCountryCode() {
		return sourceCountryCode;
	}
	public void setSourceCountryCode(String sourceCountryCode) {
		this.sourceCountryCode = sourceCountryCode;
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
	public String getLinkType() {
		return linkType;
	}
	public void setLinkType(String linkType) {
		this.linkType = linkType;
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
	public Double getArc() {
		return arc;
	}
	public void setArc(Double arc) {
		this.arc = arc;
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
	public String getPortBwUnit() {
		return portBwUnit;
	}
	public void setPortBwUnit(String portBwUnit) {
		this.portBwUnit = portBwUnit;
	}
	public String getPriSecServLink() {
		return priSecServLink;
	}
	public void setPriSecServLink(String priSecServLink) {
		this.priSecServLink = priSecServLink;
	}
	public Timestamp getServiceCommissionedDate() {
		return serviceCommissionedDate;
	}
	public void setServiceCommissionedDate(Timestamp serviceCommissionedDate) {
		this.serviceCommissionedDate = serviceCommissionedDate;
	}
	public Double getContractTerm() {
		return contractTerm;
	}
	public void setContractTerm(Double contractTerm) {
		this.contractTerm = contractTerm;
	}
	public String getLatLong() {
		return latLong;
	}
	public void setLatLong(String latLong) {
		this.latLong = latLong;
	}
	public String getVpnName() {
		return vpnName;
	}
	public void setVpnName(String vpnName) {
		this.vpnName = vpnName;
	}
	public String getAccessProvider() {
		return accessProvider;
	}
	public void setAccessProvider(String accessProvider) {
		this.accessProvider = accessProvider;
	}
	public String getSiteAddress() {
		return siteAddress;
	}
	public void setSiteAddress(String siteAddress) {
		this.siteAddress = siteAddress;
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
	public List<SIAssetRelationBean> getAssetRelations() {
		return assetRelations;
	}
	public void setAssetRelations(List<SIAssetRelationBean> assetRelations) {
		this.assetRelations = assetRelations;
	}

	public String getBillingFrequency() {
		return billingFrequency;
	}
	public void setBillingFrequency(String billingFrequency) {
		this.billingFrequency = billingFrequency;
	}
	public String getBillingMethod() {
		return billingMethod;
	}
	public void setBillingMethod(String billingMethod) {
		this.billingMethod = billingMethod;
	}
	public String getPaymentTerm() {
		return paymentTerm;
	}
	public void setPaymentTerm(String paymentTerm) {
		this.paymentTerm = paymentTerm;
	}
	public Integer getErfCustomerLeId() {
		return erfCustomerLeId;
	}
	public void setErfCustomerLeId(Integer erfCustomerLeId) {
		this.erfCustomerLeId = erfCustomerLeId;
	}
	public String getErfCustomerLeName() {
		return erfCustomerLeName;
	}
	public void setErfCustomerLeName(String erfCustomerLeName) {
		this.erfCustomerLeName = erfCustomerLeName;
	}
	public Integer getErfCustomerId() {
		return erfCustomerId;
	}
	public void setErfCustomerId(Integer erfCustomerId) {
		this.erfCustomerId = erfCustomerId;
	}
	public String getErfCustomerName() {
		return erfCustomerName;
	}
	public void setErfCustomerName(String erfCustomerName) {
		this.erfCustomerName = erfCustomerName;
	}
	public Integer getReferenceOrderId() {
		return referenceOrderId;
	}
	public void setReferenceOrderId(Integer referenceOrderId) {
		this.referenceOrderId = referenceOrderId;
	}
	public Integer getErfSpLeId() {
		return erfSpLeId;
	}
	public void setErfSpLeId(Integer erfSpLeId) {
		this.erfSpLeId = erfSpLeId;
	}
	public String getErfSpLeName() {
		return erfSpLeName;
	}
	public void setErfSpLeName(String erfSpLeName) {
		this.erfSpLeName = erfSpLeName;
	}
	public Integer getCustomerCurrencyId() {
		return customerCurrencyId;
	}
	public void setCustomerCurrencyId(Integer customerCurrencyId) {
		this.customerCurrencyId = customerCurrencyId;
	}
	public Integer getParentOpportunityId() {
		return parentOpportunityId;
	}
	public void setParentOpportunityId(Integer parentOpportunityId) {
		this.parentOpportunityId = parentOpportunityId;
	}

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

	public Set<SIServiceAttributeBean> getAssetAttributes() {
		return assetAttributes;
	}
	public void setAssetAttributes(Set<SIServiceAttributeBean> assetAttributes) {
		this.assetAttributes = assetAttributes;
	}
	
	
	public String getServiceManagementOption() {
		return serviceManagementOption;
	}
	public void setServiceManagementOption(String serviceManagementOption) {
		this.serviceManagementOption = serviceManagementOption;
	}
	
	
	public String getServiceTopology() {
		return serviceTopology;
	}
	public void setServiceTopology(String serviceTopology) {
		this.serviceTopology = serviceTopology;
	}
	public String getSiteTopology() {
		return siteTopology;
	}
	public void setSiteTopology(String siteTopology) {
		this.siteTopology = siteTopology;
	}
	
	public String getSupportType() {
		return supportType;
	}
	public void setSupportType(String supportType) {
		this.supportType = supportType;
	}
	public String getScopeOfManagement() {
		return scopeOfManagement;
	}
	public void setScopeOfManagement(String scopeOfManagement) {
		this.scopeOfManagement = scopeOfManagement;
	}

	public String getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(String partnerId) {
		this.partnerId = partnerId;
	}

	public String getOrderPartnerName() {
		return orderPartnerName;
	}

	public void setOrderPartnerName(String orderPartnerName) {
		this.orderPartnerName = orderPartnerName;
	}

	public String getPartnerCuid() {
		return partnerCuid;
	}

	public void setPartnerCuid(String partnerCuid) {
		this.partnerCuid = partnerCuid;
	}

	public String getErfCustPartnerLeId() {
		return erfCustPartnerLeId;
	}

	public void setErfCustPartnerLeId(String erfCustPartnerLeId) {
		this.erfCustPartnerLeId = erfCustPartnerLeId;
	}

	public String getOpportunityType() {
		return opportunityType;
	}

	public void setOpportunityType(String opportunityType) {
		this.opportunityType = opportunityType;
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


	public String getDemoFlag() {
		return demoFlag;
	}

	public void setDemoFlag(String demoFlag) {
		this.demoFlag = demoFlag;
	}

	public String getDemoType() {
		return demoType;
	}

	public void setDemoType(String demoType) {
		this.demoType = demoType;
	}

	public String getLastMileType() {
		return lastMileType;
	}
	public void setLastMileType(String lastMileType) {
		this.lastMileType = lastMileType;
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
	
	
	
	public Integer getTpsSfdcCuid() {
		return tpsSfdcCuid;
	}
	public void setTpsSfdcCuid(Integer tpsSfdcCuid) {
		this.tpsSfdcCuid = tpsSfdcCuid;
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
	public void setCircuitExpiryDate(Date circuitExpiryDate) {
		this.circuitExpiryDate = circuitExpiryDate;
	}
	
	
	public List<SIComponentBean> getComponentBean() {
		return componentBean;
	}
	public void setComponentBean(List<SIComponentBean> componentBean) {
		this.componentBean = componentBean;
	}
	
	
	public String getSiteEndInterface() {
		return siteEndInterface;
	}
	public void setSiteEndInterface(String siteEndInterface) {
		this.siteEndInterface = siteEndInterface;
	}

	public String getSrvSiteType() {
		return srvSiteType;
	}

	public void setSrvSiteType(String srvSiteType) {
		this.srvSiteType = srvSiteType;
	}
	
	public String getCurrentOpportunityType() {
		return currentOpportunityType;
	}
	
	public void setCurrentOpportunityType(String currentOpportunityType) {
		this.currentOpportunityType = currentOpportunityType;
	}
	
	@Override
	public String toString() {
		return "SIServiceDetailsBean [id=" + id + ", billingAccountId=" + billingAccountId + ", billingType="
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
				+ ", linkType=" + linkType + ", mrc=" + mrc + ", nrc=" + nrc + ", arc=" + arc + ", lastmileBw="
				+ lastmileBw + ", lastmileBwUnit=" + lastmileBwUnit + ", portBw=" + portBw + ", portBwUnit="
				+ portBwUnit + ", priSecServLink=" + priSecServLink + ", serviceCommissionedDate="
				+ serviceCommissionedDate + ", contractTerm=" + contractTerm + ", contractStartDate="
				+ contractStartDate + ", contractEndDate=" + contractEndDate + ", latLong=" + latLong + ", vpnName="
				+ vpnName + ", accessProvider=" + accessProvider + ", siteAddress=" + siteAddress + ", attributes="
				+ attributes + ", assets=" + assets + ", assetRelations=" + assetRelations + ", referenceOrderId="
				+ referenceOrderId + ", billingFrequency=" + billingFrequency + ", billingMethod=" + billingMethod
				+ ", paymentTerm=" + paymentTerm + ", erfCustomerLeId=" + erfCustomerLeId + ", erfCustomerLeName="
				+ erfCustomerLeName + ", erfCustomerId=" + erfCustomerId + ", erfCustomerName=" + erfCustomerName
				+ ", erfSpLeId=" + erfSpLeId + ", erfSpLeName=" + erfSpLeName + ", customerCurrencyId="
				+ customerCurrencyId + ", parentOpportunityId=" + parentOpportunityId + ", assetAttributes="
				+ assetAttributes + ", serviceManagementOption=" + serviceManagementOption + ", serviceTopology="
				+ serviceTopology + ", siteTopology=" + siteTopology + ", supportType=" + supportType
				+ ", scopeOfManagement=" + scopeOfManagement + ", partnerId=" + partnerId + ", orderPartnerName="
				+ orderPartnerName + ", partnerCuid=" + partnerCuid + ", erfCustPartnerLeId=" + erfCustPartnerLeId
				+ ", opportunityType=" + opportunityType + ", demarcationApartment=" + demarcationApartment
				+ ", demarcationFloor=" + demarcationFloor + ", demarcationRack=" + demarcationRack
				+ ", demarcationRoom=" + demarcationRoom + ", lastMileType=" + lastMileType + ", srvSiteType="
				+ srvSiteType + ", demoFlag=" + demoFlag + ", demoType=" + demoType + ", totalVrfBandwith="
				+ totalVrfBandwith + ", tpsCopfId=" + tpsCopfId + ", orderCode=" + orderCode + ", tpsSfdcCuid="
				+ tpsSfdcCuid + ", orderCategory=" + orderCategory + ", orderSubCategory=" + orderSubCategory
				+ ", burstableBw=" + burstableBw + ", burstableBwUnit=" + burstableBwUnit + ", circuitExpiryDate="
				+ circuitExpiryDate + ", componentBean=" + componentBean + ", siteEndInterface=" + siteEndInterface
				+ ", sourceCity=" + sourceCity + ", destinationCity=" + destinationCity + ", accountManager="
				+ accountManager + ", billingCurrency=" + billingCurrency + ", portMode=" + portMode
				+ ", sCommisionDate=" + sCommisionDate + ", ipv4AddressPoolsize=" + ipv4AddressPoolsize
				+ ", currentOpportunityType=" + currentOpportunityType + "]";
	}
	

}

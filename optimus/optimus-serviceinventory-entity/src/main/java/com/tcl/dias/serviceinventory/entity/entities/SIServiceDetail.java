package com.tcl.dias.serviceinventory.entity.entities;

import java.io.Serializable;
import javax.persistence.*;


import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Set;


/**
 * 
 * @author Dinahar Vivekanandan
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 * The persistent class for the si_service_detail database table.
 * 
 */
@Entity
@Table(name="si_service_detail")
public class SIServiceDetail implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	@Column(name="access_type")
	private String accessType;

	private Double arc;

	@Column(name="billing_account_id")
	private String billingAccountId;

	@Column(name="billing_gst_number")
	private String billingGstNumber;

	@Column(name="billing_ratio_percent")
	private Double billingRatioPercent;

	@Column(name="billing_type")
	private String billingType;

	@Column(name="burstable_bw_portspeed")
	private String burstableBwPortspeed;

	@Column(name="burstable_bw_portspeed_alt_name")
	private String burstableBwPortspeedAltName;

	@Column(name="burstable_bw_unit")
	private String burstableBwUnit;

	@Column(name="bw_portspeed")
	private String bwPortspeed;

	@Column(name="bw_portspeed_alt_name")
	private String bwPortspeedAltName;

	@Column(name="bw_unit")
	private String bwUnit;

	@Column(name="created_by")
	private String createdBy;

	@Column(name="created_date")
	private Timestamp createdDate;

	@Column(name="demarcation_apartment")
	private String demarcationApartment;

	@Column(name="demarcation_floor")
	private String demarcationFloor;

	@Column(name="demarcation_rack")
	private String demarcationRack;

	@Column(name="denarcation_room")
	private String demarcationRoom;

	@Column(name="destination_city")
	private String destinationCity;

	@Column(name="destination_country")
	private String destinationCountry;

	@Column(name="destination_country_code")
	private String destinationCountryCode;

	@Column(name="destination_country_code_repc")
	private String destinationCountryCodeRepc;

	@Column(name="discount_arc")
	private BigDecimal discountArc;

	@Column(name="discount_mrc")
	private BigDecimal discountMrc;

	@Column(name="discount_nrc")
	private BigDecimal discountNrc;

	@Column(name="erf_loc_destination_city_id")
	private String erfLocDestinationCityId;

	@Column(name="erf_loc_destination_country_id")
	private Integer erfLocDestinationCountryId;

	@Column(name="erf_loc_pop_site_address_id")
	private String erfLocPopSiteAddressId;

	@Column(name="erf_loc_site_address_id")
	private String erfLocSiteAddressId;

	@Column(name="erf_loc_source_city_id")
	private String erfLocSourceCityId;

	@Column(name="erf_loc_src_country_id")
	private Integer erfLocSrcCountryId;

	@Column(name="erf_prd_catalog_offering_id")
	private Integer erfPrdCatalogOfferingId;

	@Column(name="erf_prd_catalog_offering_name")
	private String erfPrdCatalogOfferingName;

	@Column(name="erf_prd_catalog_parent_product_name")
	private String erfPrdCatalogParentProductName;

	@Column(name="erf_prd_catalog_parent_product_offering_name")
	private String erfPrdCatalogParentProductOfferingName;

	@Column(name="erf_prd_catalog_product_id")
	private Integer erfPrdCatalogProductId;

	@Column(name="erf_prd_catalog_product_name")
	private String erfPrdCatalogProductName;

	@Column(name="feasibility_id")
	private String feasibilityId;

	@Column(name="gsc_order_sequence_id")
	private String gscOrderSequenceId;

	@Column(name="is_active")
	private String isActive;

	@Column(name="is_izo")
	private String isIzo;

	@Column(name="lastmile_bw")
	private String lastmileBw;

	@Column(name="lastmile_bw_alt_name")
	private String lastmileBwAltName;

	@Column(name="lastmile_bw_unit")
	private String lastmileBwUnit;

	@Column(name="lastmile_provider")
	private String lastmileProvider;

	@Column(name="lastmile_type")
	private String lastmileType;

	private Double mrc;

	private Double nrc;

	@Column(name="parent_bundle_service_id")
	private Integer parentBundleServiceId;

	@Column(name="parent_id")
	private Integer parentId;

	@Column(name="pop_site_address")
	private String popSiteAddress;

	@Column(name="pop_site_code")
	private String popSiteCode;

	@Column(name="pri_sec_service_link")
	private String priSecServiceLink;

	@Column(name="primary_secondary")
	private String primarySecondary;

	@Column(name="product_reference_id")
	private Integer productReferenceId;

	@Column(name="service_class")
	private String serviceClass;

	@Column(name="service_classification")
	private String serviceClassification;

	@Column(name="service_commissioned_date")
	private Timestamp serviceCommissionedDate;

	@Column(name="service_group_id")
	private String serviceGroupId;

	@Column(name="service_group_type")
	private String serviceGroupType;

	@Column(name="service_option")
	private String serviceOption;

	@Column(name="service_status")
	private String serviceStatus;

	@Column(name="service_termination_date")
	private Timestamp serviceTerminationDate;

	@Column(name="service_topology")
	private String serviceTopology;

	@Column(name="si_order_uuid")
	private String siOrderUuid;

	@Column(name="site_address")
	private String siteAddress;

	@Column(name="site_end_interface")
	private String siteEndInterface;

	@Column(name="site_lat_lang")
	private String siteLatLang;

	@Column(name="site_link_label")
	private String siteLinkLabel;

	@Column(name="site_topology")
	private String siteTopology;

	@Column(name="site_type")
	private String siteType;

	@Column(name="sla_template")
	private String slaTemplate;

	@Column(name="sm_email")
	private String smEmail;

	@Column(name="sm_name")
	private String smName;

	@Column(name="source_city")
	private String sourceCity;

	@Column(name="source_country")
	private String sourceCountry;

	@Column(name="source_country_code")
	private String sourceCountryCode;

	@Column(name="source_country_code_repc")
	private String sourceCountryCodeRepc;

	@Column(name="tax_exemption_flag")
	private String taxExemptionFlag;

	@Column(name="tps_copf_id")
	private String tpsCopfId;

	@Column(name="tps_service_id")
	private String tpsServiceId;

	@Column(name="tps_source_service_id")
	private String tpsSourceServiceId;

	@Column(name="updated_by")
	private String updatedBy;

	@Column(name="updated_date")
	private Timestamp updatedDate;

	private String uuid;

	@Column(name="vpn_name")
	private String vpnName;

	@Column(name="call_type")
	private String callType;

	@Column(name="cust_org_no")
	private String custOrgNo;

	@Column(name="suppl_org_no")
	private String supplOrgNo;
	
	@Column(name="lat_long")
	private String latLong;

	@Column(name="IP_address_provided_by")
	private String ipAddressProvidedBy;
	
	@Column(name="ip_address_arrangement_type")
	private String ipAddressArrangementType;
	
	@Column(name="show_cos_message")
	private Byte showCosMessage;

	@Column(name="master_vrf_service_id")
	private String mastervrfServiceId;

	private Byte ismultivrf;

	@Column(name="customer_service_id")
	private String customerServiceId;

	@Column(name="circuit_status")
	private String circuitStatus;

	@Column(name="service_varient")
	private String serviceVarient;

	@Column(name = "erf_sc_service_id")
	private Integer erfScServiceId;

	@Column(name = "destination_state")
	private String destinationState;

	@Column(name = "source_state")
	private String sourceState;
	
	@Column(name = "order_sub_category")
	private String orderSubCategory;
	
	@Column(name ="IZO_SDWAN_SRVC_ID")
	private String izoSdwanSrvcId;
	
	@Column(name="primary_tps_service_id")
	private String primaryTpsSrviceId;
	
	@Column(name = "order_type")
	private String orderType;

	@Column(name = "order_category")
	private String orderCategory;
	
	@Column(name = "tiger_order_id")
	private String tigerOrderId;

	@Column(name="associate_billable_id")
	private String associateBillableId;

	@Column(name="circuit_expiry_date")
	private Timestamp circuitExpiryDate;
	
	@Column(name="contract_start_date")
	private Timestamp contractStartDate;
	
	@Column(name="contract_end_date")
	private Timestamp contractEndDate;
	
	@Column(name="additional_ips_req")
	private String additionalIpsReq;

	public String getLatLong() {
		return latLong;
	}

	public void setLatLong(String latLong) {
		this.latLong = latLong;
	}

	public String getIpAddressProvidedBy() {
		return ipAddressProvidedBy;
	}

	public void setIpAddressProvidedBy(String ipAddressProvidedBy) {
		this.ipAddressProvidedBy = ipAddressProvidedBy;
	}


	//bi-directional many-to-one association to SiAssetToService
	@OneToMany(mappedBy="siServiceDetail", cascade = CascadeType.ALL)
	private Set<SIAssetToService> siAssetToServices;

	//bi-directional many-to-one association to SiServiceAttribute
	@OneToMany(mappedBy="siServiceDetail", cascade = CascadeType.ALL)
	private Set<SIServiceAttribute> siServiceAttributes;

	//bi-directional many-to-one association to SiContractInfo
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="si_contract_info_id")
	private SIContractInfo siContractInfo;

	//bi-directional many-to-one association to SiOrder
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="si_order_id")
	private SIOrder siOrder;

	//bi-directional many-to-one association to SiServiceSla
	@OneToMany(mappedBy="siServiceDetail", cascade = CascadeType.ALL)
	private Set<SIServiceSla> siServiceSlas;
	
	//bi-directional many-to-one association to SIAsset
	@OneToMany(mappedBy="siServiceDetail", cascade = CascadeType.ALL)
	private Set<SIAsset> siAssets;
	
	//bi-directional many-to-one association to SIServiceContact
	@OneToMany(mappedBy="siServiceDetail", cascade = CascadeType.ALL)
	private Set<SiServiceContact> siServiceContacts;
	
	//bi-directional many-to-one association to SIAttachment
	@OneToMany(mappedBy = "siServiceDetail", cascade = CascadeType.PERSIST)
	private Set<SIAttachment> siAttachments;
	
	@Column(name="site_alias")
	private String siteAlias;

	@Column(name="remarks")
	private String remarks;

	@Column(name="service_type")
	private String serviceType;

	@Column(name="assigned_pm")
	private String assignedPm;

	@Column(name="parent_uuid")
	private String parentUuid;

	@Column(name="erf_prd_catalog_flavour_name")
	private String erfPrdCatalogFlavourName;

	@Column(name="billing_completed_date")
	private Timestamp billingCompletedDate;
	
	@Column(name="MULTI_VRF_SOLUTION")
	private String multiVrfSolution;
	
	public String getMultiVrfSolution() {
		return multiVrfSolution;
	}

	public void setMultiVrfSolution(String multiVrfSolution) {
		this.multiVrfSolution = multiVrfSolution;
	}


	@Column(name="resiliency_ind")
	private String resiliencyInd;

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getSiteAlias() {
		return siteAlias;
	}

	public void setSiteAlias(String siteAlias) {
		this.siteAlias = siteAlias;
	}

	public SIServiceDetail() {
    }

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

	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Timestamp getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
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

	public String getPriSecServiceLink() {
		return this.priSecServiceLink;
	}

	public void setPriSecServiceLink(String priSecServiceLink) {
		this.priSecServiceLink = priSecServiceLink;
	}

	public String getPrimarySecondary() {
		return this.primarySecondary;
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

	public Timestamp getServiceCommissionedDate() {
		return this.serviceCommissionedDate;
	}

	public void setServiceCommissionedDate(Timestamp serviceCommissionedDate) {
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

	public Timestamp getServiceTerminationDate() {
		return this.serviceTerminationDate;
	}

	public void setServiceTerminationDate(Timestamp serviceTerminationDate) {
		this.serviceTerminationDate = serviceTerminationDate;
	}

	public String getServiceTopology() {
		return this.serviceTopology;
	}

	public void setServiceTopology(String serviceTopology) {
		this.serviceTopology = serviceTopology;
	}

	public String getSiOrderUuid() {
		return this.siOrderUuid;
	}

	public void setSiOrderUuid(String siOrderUuid) {
		this.siOrderUuid = siOrderUuid;
	}

	public String getSiteAddress() {
		return this.siteAddress;
	}

	public void setSiteAddress(String siteAddress) {
		this.siteAddress = siteAddress;
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

	public Timestamp getUpdatedDate() {
		return this.updatedDate;
	}

	public void setUpdatedDate(Timestamp updatedDate) {
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

	public String getCallType() {
		return callType;
	}

	public void setCallType(String callType) {
		this.callType = callType;
	}

	public String getCustOrgNo() {
		return custOrgNo;
	}

	public void setCustOrgNo(String custOrgNo) {
		this.custOrgNo = custOrgNo;
	}

	public String getSupplOrgNo() {
		return supplOrgNo;
	}

	public void setSupplOrgNo(String supplOrgNo) {
		this.supplOrgNo = supplOrgNo;
	}

	public Set<SIAssetToService> getSiAssetToServices() {
		return this.siAssetToServices;
	}

	public void setSiAssetToServices(Set<SIAssetToService> siAssetToServices) {
		this.siAssetToServices = siAssetToServices;
	}
	
	public Set<SIServiceAttribute> getSiServiceAttributes() {
		return this.siServiceAttributes;
	}

	public void setSiServiceAttributes(Set<SIServiceAttribute> siServiceAttributes) {
		this.siServiceAttributes = siServiceAttributes;
	}
	
	public SIContractInfo getSiContractInfo() {
		return this.siContractInfo;
	}

	public void setSiContractInfo(SIContractInfo siContractInfo) {
		this.siContractInfo = siContractInfo;
	}
	
	public SIOrder getSiOrder() {
		return this.siOrder;
	}

	public void setSiOrder(SIOrder siOrder) {
		this.siOrder = siOrder;
	}
	
	public Set<SIServiceSla> getSiServiceSlas() {
		return this.siServiceSlas;
	}

	public void setSiServiceSlas(Set<SIServiceSla> siServiceSlas) {
		this.siServiceSlas = siServiceSlas;
	}

	public String getDestinationCountryCodeRepc() {
		return destinationCountryCodeRepc;
	}

	public void setDestinationCountryCodeRepc(String destinationCountryCodeRepc) {
		this.destinationCountryCodeRepc = destinationCountryCodeRepc;
	}

	public String getSourceCountryCodeRepc() {
		return sourceCountryCodeRepc;
	}

	public void setSourceCountryCodeRepc(String sourceCountryCodeRepc) {
		this.sourceCountryCodeRepc = sourceCountryCodeRepc;
	}

	public Set<SIAsset> getSiAssets() {
		return siAssets;
	}

	public void setSiAssets(Set<SIAsset> siAssets) {
		this.siAssets = siAssets;
	}

	public Set<SiServiceContact> getSiServiceContacts() {
		return siServiceContacts;
	}

	public void setSiServiceContacts(Set<SiServiceContact> siServiceContacts) {
		this.siServiceContacts = siServiceContacts;
	}

	public Set<SIAttachment> getSiAttachments() {
		return siAttachments;
	}

	public void setSiAttachments(Set<SIAttachment> siAttachments) {
		this.siAttachments = siAttachments;
	}
	
	public Byte getShowCosMessage() {
		return showCosMessage;
	}

	public void setShowCosMessage(Byte showCosMessage) {
		this.showCosMessage = showCosMessage;
	}

	public String getMastervrfServiceId() {
		return mastervrfServiceId;
	}

	public void setMastervrfServiceId(String mastervrfServiceId) {
		this.mastervrfServiceId = mastervrfServiceId;
	}

	public Byte getIsmultivrf() {
		return ismultivrf;
	}

	public void setIsmultivrf(Byte ismultivrf) {
		this.ismultivrf = ismultivrf;
	}

	public String getCustomerServiceId() {
		return customerServiceId;
	}

	public void setCustomerServiceId(String customerServiceId) {
		this.customerServiceId = customerServiceId;
	}

	public String getCircuitStatus() {
		return circuitStatus;
	}

	public void setCircuitStatus(String circuitStatus) {
		this.circuitStatus = circuitStatus;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public String getServiceVarient() {
		return serviceVarient;
	}

	public void setServiceVarient(String serviceVarient) {
		this.serviceVarient = serviceVarient;
	}

	public Integer getErfScServiceId() {
		return erfScServiceId;
	}

	public void setErfScServiceId(Integer erfScServiceId) {
		this.erfScServiceId = erfScServiceId;
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

	public String getIpAddressArrangementType() {
		return ipAddressArrangementType;
	}

	public void setIpAddressArrangementType(String ipAddressArrangementType) {
		this.ipAddressArrangementType = ipAddressArrangementType;
	}
	
	public String getOrderSubCategory() {
		return orderSubCategory;
	}

	public void setOrderSubCategory(String orderSubCategory) {
		this.orderSubCategory = orderSubCategory;
	}

	public String getIzoSdwanSrvcId() {
		return izoSdwanSrvcId;
	}

	public void setIzoSdwanSrvcId(String izoSdwanSrvcId) {
		this.izoSdwanSrvcId = izoSdwanSrvcId;
	}

	public String getPrimaryTpsSrviceId() {
		return primaryTpsSrviceId;
	}

	public void setPrimaryTpsSrviceId(String primaryTpsSrviceId) {
		this.primaryTpsSrviceId = primaryTpsSrviceId;
	}

	public String getAssignedPm() {
		return assignedPm;
	}

	public void setAssignedPm(String assignedPm) {
		this.assignedPm = assignedPm;
	}

	public String getParentUuid() {
		return parentUuid;
	}

	public void setParentUuid(String parentUuid) {
		this.parentUuid = parentUuid;
	}

	public String getErfPrdCatalogFlavourName() {
		return erfPrdCatalogFlavourName;
	}

	public void setErfPrdCatalogFlavourName(String erfPrdCatalogFlavourName) {
		this.erfPrdCatalogFlavourName = erfPrdCatalogFlavourName;
	}

	public Timestamp getBillingCompletedDate() {
		return billingCompletedDate;
	}

	public void setBillingCompletedDate(Timestamp billingCompletedDate) {
		this.billingCompletedDate = billingCompletedDate;
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

	public String getResiliencyInd() {
		return resiliencyInd;
	}

	public void setResiliencyInd(String resiliencyInd) {
		this.resiliencyInd = resiliencyInd;
	}

	public String getTigerOrderId() {
		return tigerOrderId;
	}

	public void setTigerOrderId(String tigerOrderId) {
		this.tigerOrderId = tigerOrderId;
	}
	
	public String getAssociateBillableId() {
		return associateBillableId;
	}

	public void setAssociateBillableId(String associateBillableId) {
		this.associateBillableId = associateBillableId;
	}

	public Timestamp getCircuitExpiryDate() {
		return circuitExpiryDate;
	}

	public void setCircuitExpiryDate(Timestamp circuitExpiryDate) {
		this.circuitExpiryDate = circuitExpiryDate;
	}

	public Timestamp getContractStartDate() {
		return contractStartDate;
	}

	public void setContractStartDate(Timestamp contractStartDate) {
		this.contractStartDate = contractStartDate;
	}

	public Timestamp getContractEndDate() {
		return contractEndDate;
	}

	public void setContractEndDate(Timestamp contractEndDate) {
		this.contractEndDate = contractEndDate;
	}

	public String getAdditionalIpsReq() {
		return additionalIpsReq;
	}

	public void setAdditionalIpsReq(String additionalIpsReq) {
		this.additionalIpsReq = additionalIpsReq;
	}


}

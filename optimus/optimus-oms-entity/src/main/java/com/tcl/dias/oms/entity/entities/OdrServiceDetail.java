package com.tcl.dias.oms.entity.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * 
 * This class contains entity of odr_service_detail table
 * 
 *
 * @author ANANDHI VIJAY
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name = "odr_service_detail")
@NamedQuery(name = "OdrServiceDetail.findAll", query = "SELECT o FROM OdrServiceDetail o")
public class OdrServiceDetail implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "access_type")
	private String accessType;

	@Column(name = "service_ref_id")
	private String serviceRefId;

	private Double arc;

	@Column(name = "billing_account_id")
	private String billingAccountId;

	@Column(name = "billing_gst_number")
	private String billingGstNumber;

	@Column(name = "billing_ratio_percent")
	private Double billingRatioPercent;

	@Column(name = "billing_type")
	private String billingType;

	@Column(name = "burstable_bw_portspeed")
	private String burstableBwPortspeed;

	@Column(name = "burstable_bw_portspeed_alt_name")
	private String burstableBwPortspeedAltName;

	@Column(name = "burstable_bw_unit")
	private String burstableBwUnit;

	@Column(name = "bw_portspeed")
	private String bwPortspeed;

	@Column(name = "bw_portspeed_alt_name")
	private String bwPortspeedAltName;

	@Column(name = "bw_unit")
	private String bwUnit;

	@Column(name = "call_type")
	private String callType;

	@Column(name = "created_by")
	private String createdBy;

	@Column(name = "created_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;

	@Column(name = "cust_org_no")
	private String custOrgNo;

	@Column(name = "demarcation_apartment")
	private String demarcationApartment;

	@Column(name = "demarcation_floor")
	private String demarcationFloor;

	@Column(name = "demarcation_rack")
	private String demarcationRack;

	@Column(name = "demarcation_room")
	private String demarcationRoom;

	@Column(name = "destination_city")
	private String destinationCity;

	@Column(name = "destination_country")
	private String destinationCountry;

	@Column(name = "destination_country_code")
	private String destinationCountryCode;

	@Column(name = "destination_country_code_repc")
	private String destinationCountryCodeRepc;

	@Column(name = "destination_state")
	private String destinationState;

	@Column(name = "source_state")
	private String sourceState;

	@Column(name = "discount_arc")
	private BigDecimal discountArc;

	@Column(name = "discount_mrc")
	private BigDecimal discountMrc;

	@Column(name = "discount_nrc")
	private BigDecimal discountNrc;

	@Column(name = "erf_loc_destination_city_id")
	private String erfLocDestinationCityId;

	@Column(name = "erf_loc_destination_country_id")
	private Integer erfLocDestinationCountryId;

	@Column(name = "erf_loc_pop_site_address_id")
	private String erfLocPopSiteAddressId;

	@Column(name = "erf_loc_site_address_id")
	private String erfLocSiteAddressId;

	@Column(name = "erf_loc_source_city_id")
	private String erfLocSourceCityId;

	@Column(name = "erf_loc_src_country_id")
	private Integer erfLocSrcCountryId;

	@Column(name = "erf_prd_catalog_offering_id")
	private Integer erfPrdCatalogOfferingId;

	@Column(name = "erf_prd_catalog_offering_name")
	private String erfPrdCatalogOfferingName;
	
	@Column(name = "erf_prd_catalog_flavour_name")
	private String erfPrdCatalogFlavourName;

	@Column(name = "erf_prd_catalog_parent_product_name")
	private String erfPrdCatalogParentProductName;

	@Column(name = "erf_prd_catalog_parent_product_offering_name")
	private String erfPrdCatalogParentProductOfferingName;

	@Column(name = "erf_prd_catalog_product_id")
	private Integer erfPrdCatalogProductId;

	@Column(name = "erf_prd_catalog_product_name")
	private String erfPrdCatalogProductName;

	@Column(name = "feasibility_id")
	private String feasibilityId;

	@Column(name = "gsc_order_sequence_id")
	private String gscOrderSequenceId;

	@Column(name = "is_active")
	private String isActive;

	@Column(name = "is_izo")
	private String isIzo;

	@Column(name = "lastmile_bw")
	private String lastmileBw;

	@Column(name = "lastmile_bw_alt_name")
	private String lastmileBwAltName;

	@Column(name = "lastmile_bw_unit")
	private String lastmileBwUnit;

	@Column(name = "lastmile_provider")
	private String lastmileProvider;

	@Column(name = "lastmile_type")
	private String lastmileType;

	@Column(name = "lat_long")
	private String latLong;

	@Column(name = "local_it_contact_email")
	private String localItContactEmail;

	@Column(name = "local_it_contact_mobile")
	private String localItContactMobile;

	@Column(name = "local_it_contact_name")
	private String localItContactName;

	private Double mrc;

	private Double nrc;

	@Column(name = "odr_order_uuid")
	private String odrOrderUuid;

	@Column(name = "parent_bundle_service_id")
	private Integer parentBundleServiceId;

	@Column(name = "parent_id")
	private Integer parentId;

	@Column(name = "parent_uuid")
	private String parentUuid;

	@Column(name = "pop_site_address")
	private String popSiteAddress;

	@Column(name = "pop_site_code")
	private String popSiteCode;

	@Column(name = "order_sub_category")
	private String orderSubCategory;

	// bi-directional many-to-one association to OdrGstAddress
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "odr_gst_address_id")
	private OdrGstAddress odrGstAddress;

	@Column(name = "erf_pri_sec_service_link_id")
	private Integer erfPriSecServiceLinkId;

	@Column(name = "primary_secondary")
	private String primarySecondary;

	@Column(name = "product_reference_id")
	private Integer productReferenceId;

	@Column(name = "service_class")
	private String serviceClass;

	@Column(name = "service_classification")
	private String serviceClassification;

	@Column(name = "service_commissioned_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date serviceCommissionedDate;

	@Column(name = "service_group_id")
	private String serviceGroupId;

	@Column(name = "service_group_type")
	private String serviceGroupType;

	@Column(name = "service_option")
	private String serviceOption;

	@Column(name = "service_status")
	private String serviceStatus;

	@Column(name = "service_termination_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date serviceTerminationDate;

	@Column(name = "service_topology")
	private String serviceTopology;

	@Column(name = "site_address")
	private String siteAddress;

	@Column(name = "site_alias")
	private String siteAlias;

	@Column(name = "site_end_interface")
	private String siteEndInterface;

	@Column(name = "site_lat_lang")
	private String siteLatLang;

	@Column(name = "site_link_label")
	private String siteLinkLabel;

	@Column(name = "site_topology")
	private String siteTopology;

	@Column(name = "site_type")
	private String siteType;

	@Column(name = "sla_template")
	private String slaTemplate;

	@Column(name = "sm_email")
	private String smEmail;

	@Column(name = "sm_name")
	private String smName;

	@Column(name = "source_city")
	private String sourceCity;

	@Column(name = "source_country")
	private String sourceCountry;

	@Column(name = "source_address_line_one")
	private String sourceAddressLineOne;

	@Column(name = "source_address_line_two")
	private String sourceAddressLineTwo;

	@Column(name = "source_locality")
	private String sourceLocality;

	@Column(name = "source_pincode")
	private String sourcePincode;

	@Column(name = "destination_address_line_one")
	private String destinationAddressLineOne;

	@Column(name = "destination_address_line_two")
	private String destinationAddressLineTwo;

	@Column(name = "destination_locality")
	private String destinationLocality;

	@Column(name = "destination_pincode")
	private String destinationPincode;

	@Column(name = "source_country_code")
	private String sourceCountryCode;

	@Column(name = "source_country_code_repc")
	private String sourceCountryCodeRepc;

	@Column(name = "suppl_org_no")
	private String supplOrgNo;

	@Column(name = "tax_exemption_flag")
	private String taxExemptionFlag;

	@Column(name = "tps_copf_id")
	private String tpsCopfId;

	@Column(name = "tps_service_id")
	private String tpsServiceId;

	@Column(name = "tps_source_service_id")
	private String tpsSourceServiceId;

	@Column(name = "flow_status")
	private String flowStatus;

	@Column(name = "updated_by")
	private String updatedBy;

	@Column(name = "updated_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updatedDate;

	private String uuid;

	@Column(name = "vpn_name")
	private String vpnName;

	@Column(name = "rrfs_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date rrfsDate;

	@Column(name = "is_amended")
	private String isAmended="N";

	@Column(name = "amended_service_id")
	private String amendedServiceId;

	// bi-directional many-to-one association to OdrServiceAttribute
	@OneToMany(mappedBy = "odrServiceDetail", cascade = CascadeType.ALL)
	private Set<OdrServiceAttribute> odrServiceAttributes = new HashSet<>();

	// bi-directional many-to-one association to OdrOrder
	@ManyToOne
	@JoinColumn(name = "odr_order_id")
	private OdrOrder odrOrder;

	// bi-directional many-to-one association to OdrContractInfo
	@ManyToOne
	@JoinColumn(name = "odr_contract_info_id")
	private OdrContractInfo odrContractInfo;
	
	@Column(name="differential_mrc")
	private Double differentialMrc;
	
	@Column(name="differential_nrc")
	private Double differentialNrc;
	
	@Column(name="cancellation_charges")
	private Double cancellationCharges;
	
	@Column(name="absorbed_or_passed_on")
	private String absorbedOrPassedOn;

	// bi-directional many-to-one association to OdrServiceSla
	@OneToMany(mappedBy = "odrServiceDetail", cascade = CascadeType.ALL)
	private Set<OdrServiceSla> odrServiceSlas;

	// bi-directional many-to-one association to OdrServiceSla
	@OneToMany(mappedBy = "odrServiceDetail", cascade = CascadeType.ALL)
	private Set<OdrAttachment> odrAttachments = new HashSet<>();

	// bi-directional many-to-one association to OdrServiceAttribute
	@OneToMany(mappedBy = "odrServiceDetail", cascade = CascadeType.ALL)
	private Set<OdrProductDetail> odrProductDetail = new HashSet<>();

	// bi-directional many-to-one association to OdrGstAddress
	@OneToMany(mappedBy = "odrServiceDetail", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<OdrComponent> odrComponents;
	
	@Column(name = "master_service_id")
	private Integer masterServiceId;
	
	@Column(name = "is_multi_vrf")
	private String isMultiVrf;
	
	@OneToMany(mappedBy = "odrServiceDetail", fetch = FetchType.LAZY)
	private Set<OdrAsset> odrAssets;

	// bi-directional many-to-one association to OdrSolutionComponent
	@OneToMany(mappedBy = "odrServiceDetail1", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Set<OdrSolutionComponent> odrSolutionComponents1;

	// bi-directional many-to-one association to OdrSolutionComponent
	@OneToMany(mappedBy = "odrServiceDetail2", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Set<OdrSolutionComponent> odrSolutionComponents2;

	// bi-directional many-to-one association to OdrSolutionComponent
	@OneToMany(mappedBy = "odrServiceDetail3", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Set<OdrSolutionComponent> odrSolutionComponents3;
	
	@Column(name = "order_category")
	private String orderCategory;
	
	@Column(name = "order_type")
	private String orderType;
	
	@Column(name = "intnl_destination_ctry_dial_cd")
	private String intlDestinationDialCode;
	
	@Column(name = "multi_vrf_solution")
	private String multiVrfSolution;
	
	@Column(name="termination_reason")
	private String terminationReason;
	
	@Column(name="negotiation_required")
	private String negotiationRequired;
	
	@Column(name="termination_effective_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date terminationEffectiveDate;
	
	@Column(name="etc_value")
	private Double etcValue;
	
	@Column(name="etc_waiver")
	private String etcWaiver;
	
	@Column(name="customer_requestor_date")
	private Date customerRequestorDate;
	
	public OdrServiceDetail() {
		// DO NOTHING
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
	
	public String getErfPrdCatalogFlavourName() {
		return this.erfPrdCatalogFlavourName;
	}

	public void setErfPrdCatalogFlavourName(String erfPrdCatalogFlavourName) {
		this.erfPrdCatalogFlavourName = erfPrdCatalogFlavourName;
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

	public String getOdrOrderUuid() {
		return this.odrOrderUuid;
	}

	public void setOdrOrderUuid(String odrOrderUuid) {
		this.odrOrderUuid = odrOrderUuid;
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

	public Set<OdrServiceAttribute> getOdrServiceAttributes() {
		return this.odrServiceAttributes;
	}

	public void setOdrServiceAttributes(Set<OdrServiceAttribute> odrServiceAttributes) {
		this.odrServiceAttributes = odrServiceAttributes;
	}

	public OdrServiceAttribute removeOdrServiceAttribute(OdrServiceAttribute odrServiceAttribute) {
		getOdrServiceAttributes().remove(odrServiceAttribute);
		odrServiceAttribute.setOdrServiceDetail(null);

		return odrServiceAttribute;
	}

	public OdrOrder getOdrOrder() {
		return this.odrOrder;
	}

	public void setOdrOrder(OdrOrder odrOrder) {
		this.odrOrder = odrOrder;
	}

	public OdrContractInfo getOdrContractInfo() {
		return this.odrContractInfo;
	}

	public void setOdrContractInfo(OdrContractInfo odrContractInfo) {
		this.odrContractInfo = odrContractInfo;
	}

	public Set<OdrServiceSla> getOdrServiceSlas() {
		return this.odrServiceSlas;
	}

	public void setOdrServiceSlas(Set<OdrServiceSla> odrServiceSlas) {
		this.odrServiceSlas = odrServiceSlas;
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

	public String getServiceRefId() {
		return serviceRefId;
	}

	public void setServiceRefId(String serviceRefId) {
		this.serviceRefId = serviceRefId;
	}

	public Set<OdrProductDetail> getOdrProductDetail() {
		return odrProductDetail;
	}

	public void setOdrProductDetail(Set<OdrProductDetail> odrProductDetail) {
		this.odrProductDetail = odrProductDetail;
	}

	public String getFlowStatus() {
		return flowStatus;
	}

	public void setFlowStatus(String flowStatus) {
		this.flowStatus = flowStatus;
	}

	public Set<OdrAttachment> getOdrAttachments() {
		return odrAttachments;
	}

	public void setOdrAttachments(Set<OdrAttachment> odrAttachments) {
		this.odrAttachments = odrAttachments;
	}

	public OdrServiceSla addOdrServiceSla(OdrServiceSla odrServiceSla) {
		getOdrServiceSlas().add(odrServiceSla);
		odrServiceSla.setOdrServiceDetail(this);

		return odrServiceSla;
	}

	public OdrServiceSla removeOdrServiceSla(OdrServiceSla odrServiceSla) {
		getOdrServiceSlas().remove(odrServiceSla);
		odrServiceSla.setOdrServiceDetail(null);

		return odrServiceSla;
	}

	public OdrProductDetail addOdrProductDetail(OdrProductDetail odrProductDetail) {
		getOdrProductDetail().add(odrProductDetail);
		odrProductDetail.setOdrServiceDetail(this);
		return odrProductDetail;
	}

	public OdrProductDetail removeOdrProductDetail(OdrProductDetail odrProductDetail) {
		getOdrProductDetail().remove(odrProductDetail);
		odrProductDetail.setOdrServiceDetail(null);
		return odrProductDetail;
	}

	public OdrGstAddress getOdrGstAddress() {
		return this.odrGstAddress;
	}

	public void setOdrGstAddress(OdrGstAddress odrGstAddress) {
		this.odrGstAddress = odrGstAddress;
	}

	public Set<OdrComponent> getOdrComponents() {
		if (this.odrComponents == null) {
			odrComponents = new HashSet<>();
		}
		return odrComponents;
	}

	public void setOdrComponents(Set<OdrComponent> odrComponents) {
		this.odrComponents = odrComponents;
	}

	public Set<OdrAsset> getOdrAssets() {
		if (this.odrAssets == null)
			this.odrAssets = new HashSet<>();
		return odrAssets;
	}

	public void setOdrAssets(Set<OdrAsset> odrAssets) {
		this.odrAssets = odrAssets;
	}

	public String getParentUuid() {
		return parentUuid;
	}

	public void setParentUuid(String parentUuid) {
		this.parentUuid = parentUuid;
	}

	public String getOrderSubCategory() {
		return orderSubCategory;
	}

	public void setOrderSubCategory(String orderSubCategory) {
		this.orderSubCategory = orderSubCategory;
	}

	public String getOrderCategory() {
		return orderCategory;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderCategory(String orderCategory) {
		this.orderCategory = orderCategory;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getIsAmended() {
		return isAmended;
	}

	public void setIsAmended(String isAmended) {
		this.isAmended = isAmended;
	}

	public String getAmendedServiceId() {
		return amendedServiceId;
	}

	public void setAmendedServiceId(String amendedServiceId) {
		this.amendedServiceId = amendedServiceId;
	}

	public Date getRrfsDate() {
		return rrfsDate;
	}

	public void setRrfsDate(Date rrfsDate) {
		this.rrfsDate = rrfsDate;
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

	public Double getCancellationCharges() {
		return cancellationCharges;
	}

	public void setCancellationCharges(Double cancellationCharges) {
		this.cancellationCharges = cancellationCharges;
	}

	public String getAbsorbedOrPassedOn() {
		return absorbedOrPassedOn;
	}

	public void setAbsorbedOrPassedOn(String absorbedOrPassedOn) {
		this.absorbedOrPassedOn = absorbedOrPassedOn;
	}

	public Integer getMasterServiceId() {
		return masterServiceId;
	}

	public void setMasterServiceId(Integer masterServiceId) {
		this.masterServiceId = masterServiceId;
	}

	@Override
	public String toString() {
		return "OdrServiceDetail [id=" + id + ", accessType=" + accessType + ", serviceRefId=" + serviceRefId + ", arc="
				+ arc + ", billingAccountId=" + billingAccountId + ", billingGstNumber=" + billingGstNumber
				+ ", billingRatioPercent=" + billingRatioPercent + ", billingType=" + billingType
				+ ", burstableBwPortspeed=" + burstableBwPortspeed + ", burstableBwPortspeedAltName="
				+ burstableBwPortspeedAltName + ", burstableBwUnit=" + burstableBwUnit + ", bwPortspeed=" + bwPortspeed
				+ ", bwPortspeedAltName=" + bwPortspeedAltName + ", bwUnit=" + bwUnit + ", callType=" + callType
				+ ", createdBy=" + createdBy + ", createdDate=" + createdDate + ", custOrgNo=" + custOrgNo
				+ ", demarcationApartment=" + demarcationApartment + ", demarcationFloor=" + demarcationFloor
				+ ", demarcationRack=" + demarcationRack + ", demarcationRoom=" + demarcationRoom + ", destinationCity="
				+ destinationCity + ", destinationCountry=" + destinationCountry + ", destinationCountryCode="
				+ destinationCountryCode + ", destinationCountryCodeRepc=" + destinationCountryCodeRepc
				+ ", destinationState=" + destinationState + ", sourceState=" + sourceState + ", discountArc="
				+ discountArc + ", discountMrc=" + discountMrc + ", discountNrc=" + discountNrc
				+ ", erfLocDestinationCityId=" + erfLocDestinationCityId + ", erfLocDestinationCountryId="
				+ erfLocDestinationCountryId + ", erfLocPopSiteAddressId=" + erfLocPopSiteAddressId
				+ ", erfLocSiteAddressId=" + erfLocSiteAddressId + ", erfLocSourceCityId=" + erfLocSourceCityId
				+ ", erfLocSrcCountryId=" + erfLocSrcCountryId + ", erfPrdCatalogOfferingId=" + erfPrdCatalogOfferingId
				+ ", erfPrdCatalogOfferingName=" + erfPrdCatalogOfferingName + ", erfPrdCatalogParentProductName="
				+ erfPrdCatalogParentProductName + ", erfPrdCatalogParentProductOfferingName="
				+ erfPrdCatalogParentProductOfferingName + ", erfPrdCatalogProductId=" + erfPrdCatalogProductId
				+ ", erfPrdCatalogProductName=" + erfPrdCatalogProductName + ", feasibilityId=" + feasibilityId
				+ ", gscOrderSequenceId=" + gscOrderSequenceId + ", isActive=" + isActive + ", isIzo=" + isIzo
				+ ", lastmileBw=" + lastmileBw + ", lastmileBwAltName=" + lastmileBwAltName + ", lastmileBwUnit="
				+ lastmileBwUnit + ", lastmileProvider=" + lastmileProvider + ", lastmileType=" + lastmileType
				+ ", latLong=" + latLong + ", localItContactEmail=" + localItContactEmail + ", localItContactMobile="
				+ localItContactMobile + ", localItContactName=" + localItContactName + ", mrc=" + mrc + ", nrc=" + nrc
				+ ", odrOrderUuid=" + odrOrderUuid + ", parentBundleServiceId=" + parentBundleServiceId + ", parentId="
				+ parentId + ", parentUuid=" + parentUuid + ", popSiteAddress=" + popSiteAddress + ", popSiteCode="
				+ popSiteCode + ", odrGstAddress=" + odrGstAddress + ", erfPriSecServiceLinkId="
				+ erfPriSecServiceLinkId + ", primarySecondary=" + primarySecondary + ", productReferenceId="
				+ productReferenceId + ", serviceClass=" + serviceClass + ", serviceClassification="
				+ serviceClassification + ", serviceCommissionedDate=" + serviceCommissionedDate + ", serviceGroupId="
				+ serviceGroupId + ", serviceGroupType=" + serviceGroupType + ", serviceOption=" + serviceOption
				+ ", serviceStatus=" + serviceStatus + ", serviceTerminationDate=" + serviceTerminationDate
				+ ", serviceTopology=" + serviceTopology + ", siteAddress=" + siteAddress + ", siteAlias=" + siteAlias
				+ ", siteEndInterface=" + siteEndInterface + ", siteLatLang=" + siteLatLang + ", siteLinkLabel="
				+ siteLinkLabel + ", siteTopology=" + siteTopology + ", siteType=" + siteType + ", slaTemplate="
				+ slaTemplate + ", smEmail=" + smEmail + ", smName=" + smName + ", sourceCity=" + sourceCity
				+ ", sourceCountry=" + sourceCountry + ", sourceAddressLineOne=" + sourceAddressLineOne
				+ ", sourceAddressLineTwo=" + sourceAddressLineTwo + ", sourceLocality=" + sourceLocality
				+ ", sourcePincode=" + sourcePincode + ", destinationAddressLineOne=" + destinationAddressLineOne
				+ ", destinationAddressLineTwo=" + destinationAddressLineTwo + ", destinationLocality="
				+ destinationLocality + ", destinationPincode=" + destinationPincode + ", sourceCountryCode="
				+ sourceCountryCode + ", sourceCountryCodeRepc=" + sourceCountryCodeRepc + ", supplOrgNo=" + supplOrgNo
				+ ", taxExemptionFlag=" + taxExemptionFlag + ", tpsCopfId=" + tpsCopfId + ", tpsServiceId="
				+ tpsServiceId + ", tpsSourceServiceId=" + tpsSourceServiceId + ", flowStatus=" + flowStatus
				+ ", updatedBy=" + updatedBy + ", updatedDate=" + updatedDate + ", uuid=" + uuid + ", vpnName="
				+ vpnName + ", odrServiceAttributes=" + odrServiceAttributes + ", odrOrder=" + odrOrder
				+ ", odrContractInfo=" + odrContractInfo + ", odrServiceSlas=" + odrServiceSlas + ", odrAttachments="
				+ odrAttachments + ", odrProductDetail=" + odrProductDetail + ", odrComponents=" + odrComponents
				+ ", orderSubCategory=" + orderSubCategory + ", cancellationCharges=" + cancellationCharges 
				+ ", absorbedOrPassedOn=" + absorbedOrPassedOn +  ", differentialMrc=" + differentialMrc  + ", masterServiceId=" + masterServiceId 
				+ ", differentialNrc=" + differentialNrc + "]";
	}

	public Set<OdrSolutionComponent> getOdrSolutionComponents1() {
		return odrSolutionComponents1;
	}

	public void setOdrSolutionComponents1(Set<OdrSolutionComponent> odrSolutionComponents1) {
		this.odrSolutionComponents1 = odrSolutionComponents1;
	}

	public Set<OdrSolutionComponent> getOdrSolutionComponents2() {
		return odrSolutionComponents2;
	}

	public void setOdrSolutionComponents2(Set<OdrSolutionComponent> odrSolutionComponents2) {
		this.odrSolutionComponents2 = odrSolutionComponents2;
	}

	public Set<OdrSolutionComponent> getOdrSolutionComponents3() {
		return odrSolutionComponents3;
	}

	public void setOdrSolutionComponents3(Set<OdrSolutionComponent> odrSolutionComponents3) {
		this.odrSolutionComponents3 = odrSolutionComponents3;
	}

	public String getIntlDestinationDialCode() {
		return intlDestinationDialCode;
	}

	public void setIntlDestinationDialCode(String intlDestinationDialCode) {
		this.intlDestinationDialCode = intlDestinationDialCode;
	}

	public String getIsMultiVrf() {
		return isMultiVrf;
	}

	public void setIsMultiVrf(String isMultiVrf) {
		this.isMultiVrf = isMultiVrf;
	}

	public String getMultiVrfSolution() {
		return multiVrfSolution;
	}

	public void setMultiVrfSolution(String multiVrfSolution) {
		this.multiVrfSolution = multiVrfSolution;
	}

	public String getTerminationReason() {
		return terminationReason;
	}

	public void setTerminationReason(String terminationReason) {
		this.terminationReason = terminationReason;
	}

	public String getNegotiationRequired() {
		return negotiationRequired;
	}

	public void setNegotiationRequired(String negotiationRequired) {
		this.negotiationRequired = negotiationRequired;
	}

	public Date getTerminationEffectiveDate() {
		return terminationEffectiveDate;
	}

	public void setTerminationEffectiveDate(Date terminationEffectiveDate) {
		this.terminationEffectiveDate = terminationEffectiveDate;
	}

	public Double getEtcValue() {
		return etcValue;
	}

	public void setEtcValue(Double etcValue) {
		this.etcValue = etcValue;
	}

	public Date getCustomerRequestorDate() {
		return customerRequestorDate;
	}

	public void setCustomerRequestorDate(Date customerRequestorDate) {
		this.customerRequestorDate = customerRequestorDate;
	}

	public String getEtcWaiver() {
		return etcWaiver;
	}

	public void setEtcWaiver(String etcWaiver) {
		this.etcWaiver = etcWaiver;
	}
	
	
	
}
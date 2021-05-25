package com.tcl.dias.servicefulfillment.entity.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
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
import javax.persistence.Transient;

/**
 * 
 * This file contains the ScServiceDetail.java class.
 * 
 *
 * @author Mayank S
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Entity
@Table(name = "sc_service_detail")
@NamedQuery(name = "ScServiceDetail.findAll", query = "SELECT s FROM ScServiceDetail s")
public class ScServiceDetail implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "access_type")
	private String accessType;

	private Double arc;

	@Column(name = "billing_account_id")
	private String billingAccountId;

	@Column(name = "billing_gst_number")
	private String billingGstNumber;

	@Column(name = "billing_ratio_percent")
	private Double billingRatioPercent;

	@Column(name = "billing_type")
	private String billingType;

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
	private Timestamp createdDate;

	@Column(name = "cust_org_no")
	private String custOrgNo;

	@Column(name = "demarcation_building_name")
	private String demarcationBuildingName;

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

	@Column(name = "discount_arc")
	private BigDecimal discountArc;

	@Column(name = "discount_mrc")
	private BigDecimal discountMrc;

	@Column(name = "discount_nrc")
	private BigDecimal discountNrc;

	@Column(name = "service_variant")
	private String serviceVariant;

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

	@Column(name = "erf_odr_service_id")
	private Integer erfOdrServiceId;

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
	
	@Column(name = "is_migrated_order")
	private String isMigratedOrder="N";

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
	
	@Column(name = "is_bundle")
	private String isBundle;

	@OneToMany(mappedBy = "scServiceDetail")
	private Set<AceIPMapping> aceIPMapping;

	private Double mrc;

	private Double nrc;

	@Column(name = "parent_bundle_service_id")
	private Integer parentBundleServiceId;

	@Column(name = "parent_id")
	private Integer parentId;
	
	@Column(name = "parent_uuid")
	private String parentUuid;
	
	@Column(name = "service_link_id")
	private Integer serviceLinkId;

	@Column(name = "pop_site_address")
	private String popSiteAddress;

	@Column(name = "pop_site_code")
	private String popSiteCode;

	@Column(name = "pri_sec_service_link")
	private String priSecServiceLink;

	@Column(name = "erd_prisec_service_linkid")
	private Integer erdPriSecServiceLinkId;

	@Column(name = "primary_secondary")
	private String primarySecondary;

	@Column(name = "product_reference_id")
	private Integer productReferenceId;

	@Column(name = "sc_order_uuid")
	private String scOrderUuid;

	@Column(name = "service_class")
	private String serviceClass;

	@Column(name = "service_classification")
	private String serviceClassification;

	@Column(name = "service_commissioned_date")
	private Timestamp serviceCommissionedDate;

	@Column(name = "service_group_id")
	private String serviceGroupId;

	@Column(name = "service_group_type")
	private String serviceGroupType;

	@Column(name = "service_option")
	private String serviceOption;

	@Column(name = "service_status")
	private String serviceStatus;

	@Column(name = "service_termination_date")
	private Timestamp serviceTerminationDate;

	@Column(name = "service_topology")
	private String serviceTopology;

	@Column(name = "is_oe_completed")
	private String isOeCompleted;

	@Column(name = "site_address")
	private String siteAddress;

	@Column(name = "site_alias")
	private String siteAlias;

	@Column(name = "site_end_interface")
	private String siteEndInterface;

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

	@Column(name = "source_state")
	private String sourceState;

	@Column(name = "destination_state")
	private String destinationState;

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
	
	@Column(name = "tiger_order_id")
	private String tigerOrderId;

	@Column(name = "tps_source_service_id")
	private String tpsSourceServiceId;

	@Column(name = "updated_by")
	private String updatedBy;

	@Column(name = "updated_date")
	private Timestamp updatedDate;

	private String uuid;

	@Column(name = "vpn_name")
	private String vpnName;

	@Column(name = "estimated_delivery_date")
	private Timestamp estimatedDeliveryDate;

	@Column(name = "committed_delivery_date")
	private Timestamp committedDeliveryDate;

	@Column(name = "actual_delivery_date")
	private Timestamp actualDeliveryDate;

	@Column(name = "targeted_delivery_date")
	private Timestamp targetedDeliveryDate;

	@Column(name = "priority")
	private Float priority;

	@Column(name = "vpn_solution_id")
	private String vpnSolutionId;

	@Column(name = "burstable_bw")
	private String burstableBw;

	@Column(name = "burstable_bw_unit")
	private String burstableBwUnit;

	@Column(name = "lastmile_scenario")
	private String lastmileScenario;

	@Column(name = "lastmile_connection_type")
	private String lastmileConnectionType;

	@Column(name = "no_of_additional_ips")
	private String noOfAdditionalIps;

	@Column(name = "additional_ip_pool_type")
	private String additionalIpPoolType;
	
	@Column(name = "assigned_pm")
	private String assignedPM="";
	
	@Column(name = "order_sub_category")
	private String orderSubCategory;

	@Column(name="termination_flow_triggered")
	private String terminationFlowTriggered;

	@Column(name="termination_initiation_date")
	private Timestamp terminationInitiationDate;

	@Column(name="customer_requestor_date")
	private String customerRequestorDate;

	@Column(name="termination_effective_date")
	private String terminationEffectiveDate;
	
	
	@Column(name="cancellation_flow_triggered")
	private String cancellationFlowTriggered;

	@Column(name="cancellation_initiation_date")
	private Timestamp cancellationInitiationDate;

	@Column(name = "is_amended")
	private String isAmended="N";
	
	@Column(name = "request_for_amendment")
	private String requestForAmendment="N";

	@Column(name = "differential_mrc")
	private  Double differentialMrc;

	@Column(name = "differential_nrc")
	private  Double differentialNrc;
	
	
	@Column(name = "work_flow_name")
	private String workFlowName;
	
	

	/**
	 * @return the workFlowName
	 */
	public String getWorkFlowName() {
		return workFlowName;
	}

	/**
	 * @param workFlowName the workFlowName to set
	 */
	public void setWorkFlowName(String workFlowName) {
		this.workFlowName = workFlowName;
	}

	/**
	 * @return the requestForAmendment
	 */
	public String getRequestForAmendment() {
		return requestForAmendment;
	}

	/**
	 * @param requestForAmendment the requestForAmendment to set
	 */
	public void setRequestForAmendment(String requestForAmendment) {
		this.requestForAmendment = requestForAmendment;
	}

	public Timestamp getRrfsDate() {
		return rrfsDate;
	}

	public void setRrfsDate(Timestamp rrfsDate) {
		this.rrfsDate = rrfsDate;
	}

	public Integer getErfOdrServiceId() {
		return erfOdrServiceId;
	}

	public void setErfOdrServiceId(Integer erfOdrServiceId) {
		this.erfOdrServiceId = erfOdrServiceId;
	}

	@Column(name = "line_rate")
	private String lineRate;

	// bi-directional many-to-one association to OdrGstAddress
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "sc_gst_address_id")
	private ScGstAddress scGstAddress;

	// bi-directional many-to-one association to ScServiceAttribute
	@OneToMany(mappedBy = "scServiceDetail", cascade = CascadeType.ALL)
	private Set<ScServiceAttribute> scServiceAttributes;

	@OneToMany(mappedBy = "scServiceDetail", cascade = CascadeType.PERSIST)
	private Set<ScAttachment> scAttachments;

	@OneToMany(mappedBy = "scServiceDetail")
	private Set<Task> tasks;

	// bi-directional many-to-one association to ScContractInfo
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sc_contract_info_id")
	private ScContractInfo scContractInfo;

	// bi-directional many-to-one association to ScOrder
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sc_order_id")
	private ScOrder scOrder;

	// bi-directional many-to-one association to ScServiceSla
	@OneToMany(mappedBy = "scServiceDetail", cascade = CascadeType.PERSIST)
	private Set<ScServiceSla> scServiceSlas;

	// bi-directional many-to-one association to MstStatus
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "status")
	private MstStatus mstStatus;

	@Column(name = "mst_template_key")
	private String mstTemplateKey;

	// bi-directional many-to-one association to ScServiceAttribute
	@OneToMany(mappedBy = "scServiceDetail", cascade = CascadeType.ALL)
	private Set<ScProductDetail> scProductDetail = new HashSet<>();
	
	
	@Column(name="is_jeopardy_task")
	private Byte isJeopardyTask;
	
	@Column(name = "service_config_status")
	private String serviceConfigStatus;

	@Column(name = "service_config_date")
	private Timestamp serviceConfigDate;

	@Column(name = "activation_config_status")
	private String activationConfigStatus;

	@Column(name = "activation_config_date")
	private Timestamp activationConfigDate;

	@Column(name = "billing_status")
	private String billingStatus;

	@Column(name = "billing_completed_date")
	private Timestamp billingCompletedDate;

	@Column(name = "commissioned_date")
	private Timestamp commissionedDate;

	@Column(name = "bill_start_date")
	private Timestamp billStartDate;
	
	
	@Column(name = "assurance_completion_date")
	private Timestamp assuranceCompletionDate;

	@Column(name = "assurance_completion_status")
	private String assuranceCompletionStatus;

	@Column(name = "delivered_date")
	private Timestamp deliveredDate;

	@Column(name = "is_delivered")
	private String isDelivered;

	@Column(name = "service_aceptance_date")
	private Timestamp serviceAceptanceDate;

	@Column(name = "service_aceptance_status")
	private String serviceAceptanceStatus;
	
	// bi-directional many-to-one association to ScSolutionComponent
	@OneToMany(mappedBy = "scServiceDetail1")
	private Set<ScSolutionComponent> scSolutionComponents1;

	// bi-directional many-to-one association to ScSolutionComponent
	@OneToMany(mappedBy = "scServiceDetail2")
	private Set<ScSolutionComponent> scSolutionComponents2;

	// bi-directional many-to-one association to ScSolutionComponent
	@OneToMany(mappedBy = "scServiceDetail3")
	private Set<ScSolutionComponent> scSolutionComponents3;

	@Column(name = "on_hold_category")
	private String onHoldCategory;
	
	@Column(name = "tenatative_hold_resume_date")
	private Timestamp tenatativeHoldResumeDate;
	
	@Column(name = "hold_reason")
	private String holdReason;
	
	
	@Column(name = "is_deferred_delivery")
	private String isDeferredDelivery;
	@Column(name = "order_type")
	private String orderType;
	
	
	@Column(name = "order_category")
	private String orderCategory;
	
	
	// bi-directional many-to-one association to CpeDeviceNameDetail
	@OneToMany(mappedBy = "scServiceDetail",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<CpeDeviceNameDetail> cpeDeviceNameDetails;
	
	@Column(name = "rrfs_date")
	private Timestamp rrfsDate;
	
	@Column(name = "intnl_destination_ctry_dial_cd")
	private String intlDestinationDialCode;
	
	@Column(name = "master_vrf_service_id")
	private Integer masterVrfServiceId;
	
	@Column(name = "is_multi_vrf")
	private String isMultiVrf;

	@Column(name = "multi_vrf_solution")
	private String multiVrfSolution;
	
	@Column(name = "master_vrf_service_code")
	private String masterVrfServiceCode;

	@Column(name="contract_start_date")
	private Timestamp contractStartDate;
	
	@Column(name="contract_end_date")
	private Timestamp contractEndDate;
	/**
	 * @return the isDeferredDelivery
	 */
	public String getIsDeferredDelivery() {
		return isDeferredDelivery;
	}

	/**
	 * @param isDeferredDelivery the isDeferredDelivery to set
	 */
	public void setIsDeferredDelivery(String isDeferredDelivery) {
		this.isDeferredDelivery = isDeferredDelivery;
	}
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

	@Transient
	private List<ScAsset> scAssets;

	public ScServiceDetail() {
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

	public Timestamp getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	public String getCustOrgNo() {
		return this.custOrgNo;
	}

	public void setCustOrgNo(String custOrgNo) {
		this.custOrgNo = custOrgNo;
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
		if(arc==null)return 0.0;
		else return this.arc/12;		
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

	public Integer getErdPriSecServiceLinkId() {
		return erdPriSecServiceLinkId;
	}

	public void setErdPriSecServiceLinkId(Integer erdPriSecServiceLinkId) {
		this.erdPriSecServiceLinkId = erdPriSecServiceLinkId;
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

	public String getScOrderUuid() {
		return this.scOrderUuid;
	}

	public void setScOrderUuid(String scOrderUuid) {
		this.scOrderUuid = scOrderUuid;
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

	public Set<ScServiceAttribute> getScServiceAttributes() {
		return this.scServiceAttributes;
	}

	public void setScServiceAttributes(Set<ScServiceAttribute> scServiceAttributes) {
		this.scServiceAttributes = scServiceAttributes;
	}

	public ScServiceAttribute addScServiceAttribute(ScServiceAttribute scServiceAttribute) {
		getScServiceAttributes().add(scServiceAttribute);
		scServiceAttribute.setScServiceDetail(this);

		return scServiceAttribute;
	}

	public ScServiceAttribute removeScServiceAttribute(ScServiceAttribute scServiceAttribute) {
		getScServiceAttributes().remove(scServiceAttribute);
		scServiceAttribute.setScServiceDetail(null);

		return scServiceAttribute;
	}

	public ScContractInfo getScContractInfo() {
		return this.scContractInfo;
	}

	public void setScContractInfo(ScContractInfo scContractInfo) {
		this.scContractInfo = scContractInfo;
	}

	public ScOrder getScOrder() {
		return this.scOrder;
	}

	public void setScOrder(ScOrder scOrder) {
		this.scOrder = scOrder;
	}

	public Set<ScServiceSla> getScServiceSlas() {
		return this.scServiceSlas;
	}

	public void setScServiceSlas(Set<ScServiceSla> scServiceSlas) {
		this.scServiceSlas = scServiceSlas;
	}

	public ScServiceSla addScServiceSla(ScServiceSla scServiceSla) {
		getScServiceSlas().add(scServiceSla);
		scServiceSla.setScServiceDetail(this);

		return scServiceSla;
	}

	public ScServiceSla removeScServiceSla(ScServiceSla scServiceSla) {
		getScServiceSlas().remove(scServiceSla);
		scServiceSla.setScServiceDetail(null);

		return scServiceSla;
	}

	public Float getPriority() {
		return priority;
	}

	public void setPriority(Float priority) {
		this.priority = priority;
	}

	public Timestamp getEstimatedDeliveryDate() {
		return estimatedDeliveryDate;
	}

	public void setEstimatedDeliveryDate(Timestamp estimatedDeliveryDate) {
		this.estimatedDeliveryDate = estimatedDeliveryDate;
	}

	public Timestamp getCommittedDeliveryDate() {
		return committedDeliveryDate;
	}

	public void setCommittedDeliveryDate(Timestamp committedDeliveryDate) {
		this.committedDeliveryDate = committedDeliveryDate;
	}

	public Timestamp getActualDeliveryDate() {
		return actualDeliveryDate;
	}

	public void setActualDeliveryDate(Timestamp actualDeliveryDate) {
		this.actualDeliveryDate = actualDeliveryDate;
	}

	public Timestamp getTargetedDeliveryDate() {
		return targetedDeliveryDate;
	}

	public void setTargetedDeliveryDate(Timestamp targetedDeliveryDate) {
		this.targetedDeliveryDate = targetedDeliveryDate;
	}

	public Set<ScAttachment> getScAttachments() {
		return scAttachments;
	}

	public void setScAttachments(Set<ScAttachment> scAttachments) {
		this.scAttachments = scAttachments;
	}

	public MstStatus getMstStatus() {
		return mstStatus;
	}

	public void setMstStatus(MstStatus mstStatus) {
		this.mstStatus = mstStatus;
	}

	public String getMstTemplateKey() {
		return mstTemplateKey;
	}

	public void setMstTemplateKey(String mstTemplateKey) {
		this.mstTemplateKey = mstTemplateKey;
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

	public String getServiceVariant() {
		return serviceVariant;
	}

	public void setServiceVariant(String serviceVariant) {
		this.serviceVariant = serviceVariant;
	}

	public String getVpnSolutionId() {
		return vpnSolutionId;
	}

	public void setVpnSolutionId(String vpnSolutionId) {
		this.vpnSolutionId = vpnSolutionId;
	}

	public Set<ScProductDetail> getScProductDetail() {
		return scProductDetail;
	}

	public void setScProductDetail(Set<ScProductDetail> scProductDetail) {
		this.scProductDetail = scProductDetail;
	}

	public Set<AceIPMapping> getAceIPMapping() {
		return aceIPMapping;
	}

	public void setAceIPMapping(Set<AceIPMapping> aceIPMapping) {
		this.aceIPMapping = aceIPMapping;
	}

	public String getLineRate() {
		return lineRate;
	}

	public void setLineRate(String lineRate) {
		this.lineRate = lineRate;
	}

	public String getDemarcationBuildingName() {
		return demarcationBuildingName;
	}

	public void setDemarcationBuildingName(String demarcationBuildingName) {
		this.demarcationBuildingName = demarcationBuildingName;
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

	public String getNoOfAdditionalIps() {
		return noOfAdditionalIps;
	}

	public void setNoOfAdditionalIps(String noOfAdditionalIps) {
		this.noOfAdditionalIps = noOfAdditionalIps;
	}

	public String getAdditionalIpPoolType() {
		return additionalIpPoolType;
	}

	public void setAdditionalIpPoolType(String additionalIpPoolType) {
		this.additionalIpPoolType = additionalIpPoolType;
	}

	public ScGstAddress getScGstAddress() {
		return scGstAddress;
	}

	public void setScGstAddress(ScGstAddress scGstAddress) {
		this.scGstAddress = scGstAddress;
	}

	public String getIsOeCompleted() {
		return isOeCompleted;
	}

	public void setIsOeCompleted(String isOeCompleted) {
		this.isOeCompleted = isOeCompleted;
	}

	public String getIsMigratedOrder() {
		return isMigratedOrder;
	}

	public void setIsMigratedOrder(String isMigratedOrder) {
		this.isMigratedOrder = isMigratedOrder;
	}

	public String getParentUuid() {
		return parentUuid;
	}

	public void setParentUuid(String parentUuid) {
		this.parentUuid = parentUuid;
	}

	public String getAssignedPM() {
		return assignedPM;
	}

	public void setAssignedPM(String assignedPM) {
		if(assignedPM==null)assignedPM="";
		this.assignedPM = assignedPM;
	}

	public Byte getIsJeopardyTask() {
		return isJeopardyTask;
	}

	public void setIsJeopardyTask(Byte isJeopardyTask) {
		this.isJeopardyTask = isJeopardyTask;
	}

	public String getServiceConfigStatus() {
		return serviceConfigStatus;
	}

	public void setServiceConfigStatus(String serviceConfigStatus) {
		this.serviceConfigStatus = serviceConfigStatus;
	}

	public Timestamp getServiceConfigDate() {
		return serviceConfigDate;
	}

	public void setServiceConfigDate(Timestamp serviceConfigDate) {
		this.serviceConfigDate = serviceConfigDate;
	}

	public String getActivationConfigStatus() {
		return activationConfigStatus;
	}

	public void setActivationConfigStatus(String activationConfigStatus) {
		this.activationConfigStatus = activationConfigStatus;
	}

	public Timestamp getActivationConfigDate() {
		return activationConfigDate;
	}

	public void setActivationConfigDate(Timestamp activationConfigDate) {
		this.activationConfigDate = activationConfigDate;
	}

	public String getBillingStatus() {
		return billingStatus;
	}

	public void setBillingStatus(String billingStatus) {
		this.billingStatus = billingStatus;
	}

	public Timestamp getBillingCompletedDate() {
		return billingCompletedDate;
	}

	public void setBillingCompletedDate(Timestamp billingCompletedDate) {
		this.billingCompletedDate = billingCompletedDate;
	}

	public Timestamp getCommissionedDate() {
		return commissionedDate;
	}

	public void setCommissionedDate(Timestamp commissionedDate) {
		this.commissionedDate = commissionedDate;
	}

	public Timestamp getBillStartDate() {
		return billStartDate;
	}

	public void setBillStartDate(Timestamp billStartDate) {
		this.billStartDate = billStartDate;
	}

	public Timestamp getAssuranceCompletionDate() {
		return assuranceCompletionDate;
	}

	public void setAssuranceCompletionDate(Timestamp assuranceCompletionDate) {
		this.assuranceCompletionDate = assuranceCompletionDate;
	}

	public String getAssuranceCompletionStatus() {
		return assuranceCompletionStatus;
	}

	public void setAssuranceCompletionStatus(String assuranceCompletionStatus) {
		this.assuranceCompletionStatus = assuranceCompletionStatus;
	}

	public Timestamp getDeliveredDate() {
		return deliveredDate;
	}

	public void setDeliveredDate(Timestamp deliveredDate) {
		this.deliveredDate = deliveredDate;
	}

	public String getIsDelivered() {
		return isDelivered;
	}

	public void setIsDelivered(String isDelivered) {
		this.isDelivered = isDelivered;
	}

	public Timestamp getServiceAceptanceDate() {
		return serviceAceptanceDate;
	}

	public void setServiceAceptanceDate(Timestamp serviceAceptanceDate) {
		this.serviceAceptanceDate = serviceAceptanceDate;
	}

	public String getServiceAceptanceStatus() {
		return serviceAceptanceStatus;
	}

	public void setServiceAceptanceStatus(String serviceAceptanceStatus) {
		this.serviceAceptanceStatus = serviceAceptanceStatus;
	}
	
	public String getOrderSubCategory() {
		return orderSubCategory;
	}

	public void setOrderSubCategory(String orderSubCategory) {
		this.orderSubCategory = orderSubCategory;
	}

	public Integer getServiceLinkId() {
		return serviceLinkId;
	}

	public void setServiceLinkId(Integer serviceLinkId) {
		this.serviceLinkId = serviceLinkId;
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

	public Set<ScSolutionComponent> getScSolutionComponents1() {
		return scSolutionComponents1;
	}

	public void setScSolutionComponents1(Set<ScSolutionComponent> scSolutionComponents1) {
		this.scSolutionComponents1 = scSolutionComponents1;
	}

	public Set<ScSolutionComponent> getScSolutionComponents2() {
		return scSolutionComponents2;
	}

	public void setScSolutionComponents2(Set<ScSolutionComponent> scSolutionComponents2) {
		this.scSolutionComponents2 = scSolutionComponents2;
	}

	public Set<ScSolutionComponent> getScSolutionComponents3() {
		return scSolutionComponents3;
	}

	public void setScSolutionComponents3(Set<ScSolutionComponent> scSolutionComponents3) {
		this.scSolutionComponents3 = scSolutionComponents3;
	}

	public List<ScAsset> getScAssets() {
		return scAssets;
	}

	public void setScAssets(List<ScAsset> scAssets) {
		this.scAssets = scAssets;
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

	public String getIsBundle() {
		return isBundle;
	}

	public void setIsBundle(String isBundle) {
		this.isBundle = isBundle;
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

	public Set<CpeDeviceNameDetail> getCpeDeviceNameDetails() {
		return cpeDeviceNameDetails;
	}

	public void setCpeDeviceNameDetails(Set<CpeDeviceNameDetail> cpeDeviceNameDetails) {
		this.cpeDeviceNameDetails = cpeDeviceNameDetails;
	}

	public String getIsAmended() {
		return isAmended;
	}

	public void setIsAmended(String isAmended) {
		this.isAmended = isAmended;
	}

	public String getOnHoldCategory() {
		return onHoldCategory;
	}

	public void setOnHoldCategory(String onHoldCategory) {
		this.onHoldCategory = onHoldCategory;
	}

	public Timestamp getTenatativeHoldResumeDate() {
		return tenatativeHoldResumeDate;
	}

	public void setTenatativeHoldResumeDate(Timestamp tenatativeHoldResumeDate) {
		this.tenatativeHoldResumeDate = tenatativeHoldResumeDate;
	}

	public String getHoldReason() {
		return holdReason;
	}

	public void setHoldReason(String holdReason) {
		this.holdReason = holdReason;
	}

	public String getIntlDestinationDialCode() {
		return intlDestinationDialCode;
	}

	public void setIntlDestinationDialCode(String intlDestinationDialCode) {
		this.intlDestinationDialCode = intlDestinationDialCode;
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
	
	public Integer getMasterVrfServiceId() {
		return masterVrfServiceId;
	}

	public void setMasterVrfServiceId(Integer masterVrfServiceId) {
		this.masterVrfServiceId = masterVrfServiceId;
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

	public String getMasterVrfServiceCode() {
		return masterVrfServiceCode;
	}

	public void setMasterVrfServiceCode(String masterVrfServiceCode) {
		this.masterVrfServiceCode = masterVrfServiceCode;
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
	
	
	

}
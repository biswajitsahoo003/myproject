package com.tcl.dias.serviceinventory.entity.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * This file contains the SIServiceInfo.java class.
 * 
 *
 * @author DSIVALIN
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name = "vw_si_service_info")
public class SIServiceInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="sys_id")
	private Integer id;

	@Column(name = "srv_service_id")
	private String serviceId;

	@Column(name = "srv_site_alias")
	private String siteAlias;

	@Column(name = "srv_product_family_id")
	private Integer productFamilyId;

	@Column(name = "srv_product_offering_id")
	private Integer productOfferingId;

	@Column(name = "srv_product_family_name")
	private String productFamilyName;

	@Column(name = "srv_product_offering_name")
	private String productOfferingName;

	@Column(name = "srv_primary_service_id")
	private String primaryServiceId;

	@Column(name = "srv_service_record_sequence")
	private String serviceRecordSequence;

	@Column(name = "srv_pri_sec")
	private String primaryOrSecondary;

	@Column(name = "srv_pri_sec_link")
	private String primarySecondaryLink;

	@Column(name = "srv_gsc_order_sequence_id")
	private String gscOrderSequenceId;

	@Column(name = "srv_lat_long")
	private String latLong;

	@Column(name = "srv_service_classification")
	private String serviceClassification;

	@Column(name = "srv_service_management_option")
	private String serviceManagementOption;

	@Column(name = "srv_gvpn_site_topology")
	private String gvpnSiteTopology;

	@Column(name = "srv_vpn_topology")
	private String vpnTopology;

	@Column(name = "srv_vpn_name")
	private String vpnName;

	@Column(name = "srv_gvpn_cos")
	private String gvpnCos;

	@Column(name = "srv_customer_site_address")
	private String customerSiteAddress;

	@Column(name = "srv_access_type")
	private String accessType;

	@Column(name = "srv_bandwidth")
	private String bandwidth;

	@Column(name = "srv_bandwidth_unit")
	private String bandwidthUnit;

	@Column(name = "srv_bandwidth_disp_name")
	private String bandwidthDisplayName;
	
	@Column(name = "srv_lastmile_bandwidth")
	private String lastMileBandwidth;
	
	@Column(name = "srv_lastmile_bandwidth_unit")
	private String lastMileBandwidthUnit;

	@Column(name = "srv_lastmile_bandwidth_disp_name")
	private String lastMileBandwidthDisplayName;

	@Column(name = "srv_pop_address")
	private String popAddress;
	
	@Column(name = "srv_pop_code")
	private String popCode;
	
	@Column(name = "srv_source_country")
	private String sourceCountry;
	
	@Column(name = "srv_service_status")
	private String serviceStatus;

	@Column(name = "srv_commissioned_date")
	private String commissionedDate;
	
	@Column(name = "srv_termination_date")
	private String terminationDate;
	
	@Column(name = "srv_lastmile_provider")
	private String lastMileProvider;
	
	@Column(name = "srv_last_mile_type")
	private String lastMileType;
	
	@Column(name = "srv_burstable_bw")
	private String burstableBandwidth;
	
	@Column(name = "srv_vurstable_bw_unit")
	private String burstableBandwidthUnit;
	
	@Column(name = "srv_burstable_bw_disp_name")
	private String burstableBandwidthDisplayName;
	
	@Column(name = "srv_destination_country")
	private String destinationCountry;
	
	@Column(name = "srv_destination_country_code")
	private String destinationCountryCode;
	
	@Column(name = "srv_destination_country_code_repc")
	private String destinationCountryCodeRepc;
	
	@Column(name = "srv_destination_city")
	private String destinationCity;
	
	@Column(name = "srv_source_country_code")
	private String sourceCountryCode;
	
	@Column(name = "srv_source_country_code_repc")
	private String sourceCountryCodeRepc;
	
	@Column(name = "srv_source_city")
	private String sourceCity;
	
	@Column(name = "srv_site_type")
	private String siteType;
	
	@Column(name = "demarcation_floor")
	private String demarcationFloor;
	
	@Column(name = "denarcation_room")
	private String demarcationRoom;
	
	@Column(name = "demarcation_apartment")
	private String demarcationApartment;

	@Column(name = "demarcation_rack")
	private String demarcationRack;
	
	@Column(name = "order_sys_id")
	private Integer orderSysId;
	
	@Column(name = "order_code")
	private String orderCode;
	
	@Column(name = "opportunity_type")
	private String opportunityType;
	
	@Column(name = "order_demo_flag")
	private String orderDemoFlag;
	
	@Column(name = "order_customer_id")
	private Integer orderCustomerId;
	
	@Column(name = "order_customer")
	private String orderCustomer;
	
	@Column(name = "order_cust_le_id")
	private Integer orderCustLeId;
	
	@Column(name = "order_cust_le_name")
	private String orderCustLeName;
	
	@Column(name = "order_sp_le_id")
	private Integer orderSpLeId;
	
	@Column(name = "order_sp_le_name")
	private String orderSpLeName;
	
	@Column(name = "order_partner")
	private String orderPartner;
	
	@Column(name = "order_partner_name")
	private String orderPartnerName;
	
	@Column(name = "order_initiator")
	private String orderInitiator;
	
	@Column(name = "order_created_by")
	private String orderCreatedBy;
	
	@Column(name = "order_created_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date orderCreatedDate;

	@Column(name = "order_is_active")
	private String orderIsActive;
	
	@Column(name = "order_multiple_le_flag")
	private String orderMultipleLeFlag;
	
	@Column(name = "order_bundle_order_flag")
	private String orderBundleOrderFlag;
	
	@Column(name = "sfdc_cuid")
	private Integer sfdcCuId;

	@Column(name = "account_manager")
	private String accountManager;
	
	@Column(name = "account_manager_email")
	private String accountManagerEmail;
	
	@Column(name = "customer_contact")
	private String customerContact;
	
	@Column(name = "customer_contact_email")
	private String customerContactEmail;
	
	@Column(name = "order_term_in_months")
	private Double orderTermInMonths;
	
	@Column(name = "billing_frequency")
	private String billingFrequency;
	
	@Column(name = "contract_start_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date contractStartDate;
	
	@Column(name = "contract_end_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date contractEndDate;
	
	@Column(name = "last_macd_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastMacdDate;

	private Double mrc;

	private Double nrc;

	private Double arc;

	@Column(name = "discount_mrc")
	private Double discountMrc;
	
	@Column(name = "discount_nrc")
	private Double discountNrc;
	
	@Column(name = "discount_arc")
	private Double discountArc;
	
	@Column(name = "currency_id")
	private Integer currencyId;
	
	@Column(name = "billing_address")
	private String billingAddress;
	

    @Column(name = "srv_site_end_interface")
    private String siteEndInterface;


	@Column(name = "billing_method")
	private String billingMethod;
	
	@Column(name = "erf_loc_site_address_id")
	private Integer locationId;

	@Column(name = "service_type")
	private String serviceType;

	@Column(name = "additional_ips_req")
	private String additionalIpsReq;

	@Column(name = "ip_address_arrangement_type")
	private String ipAddressArrangement;

	@Column(name = "ipv4_address_pool_size")
	private String ipv4AddressPoolSize;

	@Column(name = "ipv6_address_pool_size")
	private String ipv6AddressPoolSize;
	
	@Column(name = "opportunity_id")
	private Integer opportunityId;
	
	@Column(name = "committed_sla")
	private String committedSla;

	@Column(name = "site_classification")
	private String siteClassification;
	
	@Column(name = "tps_crm_cof_id")
	private Integer tpsCrmCofId;

	@Column(name = "partner_cuid")
	private String partnerCuid;

	@Column(name = "erf_cust_partner_le_id")
	private String erfCustPartnerLeId;

	@Column(name = "billing_type")
	private String billingType;
	
	@Column(name="Multi_vrf_solution")
	private String multiVrfSolution;
	
	@Column(name="tps_copf_id")
	private String tpsCopfId;
	
	@Column(name="current_opportunity_type")
	private String currentOpportunityType;
	
	@Column(name="circuit_expiry_date")
	private Date circuitExpiryDate;
	
	@Column(name="billing_currency")
	private String billingCurrency;
	
	@Column(name="srv_port_mode")
	private String srvPortMode;

	@Column(name="uuid")
	private String uuid;

	public String getSrvPortMode() {
		return srvPortMode;
	}

	public void setSrvPortMode(String srvPortMode) {
		this.srvPortMode = srvPortMode;
	}

	public String getBillingCurrency() {
		return billingCurrency;
	}

	public void setBillingCurrency(String billingCurrency) {
		this.billingCurrency = billingCurrency;
	}

	public String getMultiVrfSolution() {
		return multiVrfSolution;
	}

	public void setMultiVrfSolution(String multiVrfSolution) {
		this.multiVrfSolution = multiVrfSolution;
	}

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return the serviceId
	 */
	public String getServiceId() {
		return serviceId;
	}

	/**
	 * @param serviceId the serviceId to set
	 */
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	/**
	 * @return the siteAlias
	 */
	public String getSiteAlias() {
		return siteAlias;
	}

	/**
	 * @param siteAlias the siteAlias to set
	 */
	public void setSiteAlias(String siteAlias) {
		this.siteAlias = siteAlias;
	}

	/**
	 * @return the productFamilyId
	 */
	public Integer getProductFamilyId() {
		return productFamilyId;
	}

	/**
	 * @param productFamilyId the productFamilyId to set
	 */
	public void setProductFamilyId(Integer productFamilyId) {
		this.productFamilyId = productFamilyId;
	}

	/**
	 * @return the productOfferingId
	 */
	public Integer getProductOfferingId() {
		return productOfferingId;
	}

	/**
	 * @param productOfferingId the productOfferingId to set
	 */
	public void setProductOfferingId(Integer productOfferingId) {
		this.productOfferingId = productOfferingId;
	}

	/**
	 * @return the productFamilyName
	 */
	public String getProductFamilyName() {
		return productFamilyName;
	}

	/**
	 * @param productFamilyName the productFamilyName to set
	 */
	public void setProductFamilyName(String productFamilyName) {
		this.productFamilyName = productFamilyName;
	}

	/**
	 * @return the productOfferingName
	 */
	public String getProductOfferingName() {
		return productOfferingName;
	}

	/**
	 * @param productOfferingName the productOfferingName to set
	 */
	public void setProductOfferingName(String productOfferingName) {
		this.productOfferingName = productOfferingName;
	}

	/**
	 * @return the primaryServiceId
	 */
	public String getPrimaryServiceId() {
		return primaryServiceId;
	}

	/**
	 * @param primaryServiceId the primaryServiceId to set
	 */
	public void setPrimaryServiceId(String primaryServiceId) {
		this.primaryServiceId = primaryServiceId;
	}

	/**
	 * @return the serviceRecordSequence
	 */
	public String getServiceRecordSequence() {
		return serviceRecordSequence;
	}

	/**
	 * @param serviceRecordSequence the serviceRecordSequence to set
	 */
	public void setServiceRecordSequence(String serviceRecordSequence) {
		this.serviceRecordSequence = serviceRecordSequence;
	}

	/**
	 * @return the primaryOrSecondary
	 */
	public String getPrimaryOrSecondary() {
		return primaryOrSecondary;
	}

	/**
	 * @param primaryOrSecondary the primaryOrSecondary to set
	 */
	public void setPrimaryOrSecondary(String primaryOrSecondary) {
		this.primaryOrSecondary = primaryOrSecondary;
	}

	/**
	 * @return the primarySecondaryLink
	 */
	public String getPrimarySecondaryLink() {
		return primarySecondaryLink;
	}

	/**
	 * @param primarySecondaryLink the primarySecondaryLink to set
	 */
	public void setPrimarySecondaryLink(String primarySecondaryLink) {
		this.primarySecondaryLink = primarySecondaryLink;
	}

	/**
	 * @return the gscOrderSequenceId
	 */
	public String getGscOrderSequenceId() {
		return gscOrderSequenceId;
	}

	/**
	 * @param gscOrderSequenceId the gscOrderSequenceId to set
	 */
	public void setGscOrderSequenceId(String gscOrderSequenceId) {
		this.gscOrderSequenceId = gscOrderSequenceId;
	}

	/**
	 * @return the latLong
	 */
	public String getLatLong() {
		return latLong;
	}

	/**
	 * @param latLong the latLong to set
	 */
	public void setLatLong(String latLong) {
		this.latLong = latLong;
	}

	/**
	 * @return the serviceClassification
	 */
	public String getServiceClassification() {
		return serviceClassification;
	}

	/**
	 * @param serviceClassification the serviceClassification to set
	 */
	public void setServiceClassification(String serviceClassification) {
		this.serviceClassification = serviceClassification;
	}

	/**
	 * @return the serviceManagementOption
	 */
	public String getServiceManagementOption() {
		return serviceManagementOption;
	}

	/**
	 * @param serviceManagementOption the serviceManagementOption to set
	 */
	public void setServiceManagementOption(String serviceManagementOption) {
		this.serviceManagementOption = serviceManagementOption;
	}

	/**
	 * @return the gvpnSiteTopology
	 */
	public String getGvpnSiteTopology() {
		return gvpnSiteTopology;
	}

	/**
	 * @param gvpnSiteTopology the gvpnSiteTopology to set
	 */
	public void setGvpnSiteTopology(String gvpnSiteTopology) {
		this.gvpnSiteTopology = gvpnSiteTopology;
	}

	/**
	 * @return the vpnTopology
	 */
	public String getVpnTopology() {
		return vpnTopology;
	}

	/**
	 * @param vpnTopology the vpnTopology to set
	 */
	public void setVpnTopology(String vpnTopology) {
		this.vpnTopology = vpnTopology;
	}

	/**
	 * @return the vpnName
	 */
	public String getVpnName() {
		return vpnName;
	}

	/**
	 * @param vpnName the vpnName to set
	 */
	public void setVpnName(String vpnName) {
		this.vpnName = vpnName;
	}

	/**
	 * @return the gvpnCos
	 */
	public String getGvpnCos() {
		return gvpnCos;
	}

	/**
	 * @param gvpnCos the gvpnCos to set
	 */
	public void setGvpnCos(String gvpnCos) {
		this.gvpnCos = gvpnCos;
	}

	/**
	 * @return the customerSiteAddress
	 */
	public String getCustomerSiteAddress() {
		return customerSiteAddress;
	}

	/**
	 * @param customerSiteAddress the customerSiteAddress to set
	 */
	public void setCustomerSiteAddress(String customerSiteAddress) {
		this.customerSiteAddress = customerSiteAddress;
	}

	/**
	 * @return the accessType
	 */
	public String getAccessType() {
		return accessType;
	}

	/**
	 * @param accessType the accessType to set
	 */
	public void setAccessType(String accessType) {
		this.accessType = accessType;
	}

	/**
	 * @return the bandwidth
	 */
	public String getBandwidth() {
		return bandwidth;
	}

	/**
	 * @param bandwidth the bandwidth to set
	 */
	public void setBandwidth(String bandwidth) {
		this.bandwidth = bandwidth;
	}

	/**
	 * @return the bandwidthUnit
	 */
	public String getBandwidthUnit() {
		return bandwidthUnit;
	}

	/**
	 * @param bandwidthUnit the bandwidthUnit to set
	 */
	public void setBandwidthUnit(String bandwidthUnit) {
		this.bandwidthUnit = bandwidthUnit;
	}

	/**
	 * @return the bandwidthDisplayName
	 */
	public String getBandwidthDisplayName() {
		return bandwidthDisplayName;
	}

	/**
	 * @param bandwidthDisplayName the bandwidthDisplayName to set
	 */
	public void setBandwidthDisplayName(String bandwidthDisplayName) {
		this.bandwidthDisplayName = bandwidthDisplayName;
	}

	/**
	 * @return the lastMileBandwidth
	 */
	public String getLastMileBandwidth() {
		return lastMileBandwidth;
	}

	/**
	 * @param lastMileBandwidth the lastMileBandwidth to set
	 */
	public void setLastMileBandwidth(String lastMileBandwidth) {
		this.lastMileBandwidth = lastMileBandwidth;
	}

	/**
	 * @return the lastMileBandwidthUnit
	 */
	public String getLastMileBandwidthUnit() {
		return lastMileBandwidthUnit;
	}

	/**
	 * @param lastMileBandwidthUnit the lastMileBandwidthUnit to set
	 */
	public void setLastMileBandwidthUnit(String lastMileBandwidthUnit) {
		this.lastMileBandwidthUnit = lastMileBandwidthUnit;
	}

	/**
	 * @return the lastMileBandwidthDisplayName
	 */
	public String getLastMileBandwidthDisplayName() {
		return lastMileBandwidthDisplayName;
	}

	/**
	 * @param lastMileBandwidthDisplayName the lastMileBandwidthDisplayName to set
	 */
	public void setLastMileBandwidthDisplayName(String lastMileBandwidthDisplayName) {
		this.lastMileBandwidthDisplayName = lastMileBandwidthDisplayName;
	}

	/**
	 * @return the popAddress
	 */
	public String getPopAddress() {
		return popAddress;
	}

	/**
	 * @param popAddress the popAddress to set
	 */
	public void setPopAddress(String popAddress) {
		this.popAddress = popAddress;
	}

	/**
	 * @return the popCode
	 */
	public String getPopCode() {
		return popCode;
	}

	/**
	 * @param popCode the popCode to set
	 */
	public void setPopCode(String popCode) {
		this.popCode = popCode;
	}

	/**
	 * @return the sourceCountry
	 */
	public String getSourceCountry() {
		return sourceCountry;
	}

	/**
	 * @param sourceCountry the sourceCountry to set
	 */
	public void setSourceCountry(String sourceCountry) {
		this.sourceCountry = sourceCountry;
	}

	/**
	 * @return the serviceStatus
	 */
	public String getServiceStatus() {
		return serviceStatus;
	}

	/**
	 * @param serviceStatus the serviceStatus to set
	 */
	public void setServiceStatus(String serviceStatus) {
		this.serviceStatus = serviceStatus;
	}

	/**
	 * @return the commissionedDate
	 */
	public String getCommissionedDate() {
		return commissionedDate;
	}

	/**
	 * @param commissionedDate the commissionedDate to set
	 */
	public void setCommissionedDate(String commissionedDate) {
		this.commissionedDate = commissionedDate;
	}

	/**
	 * @return the terminationDate
	 */
	public String getTerminationDate() {
		return terminationDate;
	}

	/**
	 * @param terminationDate the terminationDate to set
	 */
	public void setTerminationDate(String terminationDate) {
		this.terminationDate = terminationDate;
	}

	/**
	 * @return the lastMileProvider
	 */
	public String getLastMileProvider() {
		return lastMileProvider;
	}

	/**
	 * @param lastMileProvider the lastMileProvider to set
	 */
	public void setLastMileProvider(String lastMileProvider) {
		this.lastMileProvider = lastMileProvider;
	}

	/**
	 * @return the lastMileType
	 */
	public String getLastMileType() {
		return lastMileType;
	}

	/**
	 * @param lastMileType the lastMileType to set
	 */
	public void setLastMileType(String lastMileType) {
		this.lastMileType = lastMileType;
	}

	/**
	 * @return the burstableBandwidth
	 */
	public String getBurstableBandwidth() {
		return burstableBandwidth;
	}

	/**
	 * @param burstableBandwidth the burstableBandwidth to set
	 */
	public void setBurstableBandwidth(String burstableBandwidth) {
		this.burstableBandwidth = burstableBandwidth;
	}

	/**
	 * @return the burstableBandwidthUnit
	 */
	public String getBurstableBandwidthUnit() {
		return burstableBandwidthUnit;
	}

	/**
	 * @param burstableBandwidthUnit the burstableBandwidthUnit to set
	 */
	public void setBurstableBandwidthUnit(String burstableBandwidthUnit) {
		this.burstableBandwidthUnit = burstableBandwidthUnit;
	}

	/**
	 * @return the burstableBandwidthDisplayName
	 */
	public String getBurstableBandwidthDisplayName() {
		return burstableBandwidthDisplayName;
	}

	/**
	 * @param burstableBandwidthDisplayName the burstableBandwidthDisplayName to set
	 */
	public void setBurstableBandwidthDisplayName(String burstableBandwidthDisplayName) {
		this.burstableBandwidthDisplayName = burstableBandwidthDisplayName;
	}

	/**
	 * @return the destinationCountry
	 */
	public String getDestinationCountry() {
		return destinationCountry;
	}

	/**
	 * @param destinationCountry the destinationCountry to set
	 */
	public void setDestinationCountry(String destinationCountry) {
		this.destinationCountry = destinationCountry;
	}

	/**
	 * @return the destinationCountryCode
	 */
	public String getDestinationCountryCode() {
		return destinationCountryCode;
	}

	/**
	 * @param destinationCountryCode the destinationCountryCode to set
	 */
	public void setDestinationCountryCode(String destinationCountryCode) {
		this.destinationCountryCode = destinationCountryCode;
	}

	/**
	 * @return the destinationCountryCodeRepc
	 */
	public String getDestinationCountryCodeRepc() {
		return destinationCountryCodeRepc;
	}

	/**
	 * @param destinationCountryCodeRepc the destinationCountryCodeRepc to set
	 */
	public void setDestinationCountryCodeRepc(String destinationCountryCodeRepc) {
		this.destinationCountryCodeRepc = destinationCountryCodeRepc;
	}

	/**
	 * @return the destinationCity
	 */
	public String getDestinationCity() {
		return destinationCity;
	}

	/**
	 * @param destinationCity the destinationCity to set
	 */
	public void setDestinationCity(String destinationCity) {
		this.destinationCity = destinationCity;
	}

	/**
	 * @return the sourceCountryCode
	 */
	public String getSourceCountryCode() {
		return sourceCountryCode;
	}

	/**
	 * @param sourceCountryCode the sourceCountryCode to set
	 */
	public void setSourceCountryCode(String sourceCountryCode) {
		this.sourceCountryCode = sourceCountryCode;
	}

	/**
	 * @return the sourceCountryCodeRepc
	 */
	public String getSourceCountryCodeRepc() {
		return sourceCountryCodeRepc;
	}

	/**
	 * @param sourceCountryCodeRepc the sourceCountryCodeRepc to set
	 */
	public void setSourceCountryCodeRepc(String sourceCountryCodeRepc) {
		this.sourceCountryCodeRepc = sourceCountryCodeRepc;
	}

	/**
	 * @return the sourceCity
	 */
	public String getSourceCity() {
		return sourceCity;
	}

	/**
	 * @param sourceCity the sourceCity to set
	 */
	public void setSourceCity(String sourceCity) {
		this.sourceCity = sourceCity;
	}

	/**
	 * @return the siteType
	 */
	public String getSiteType() {
		return siteType;
	}

	/**
	 * @param siteType the siteType to set
	 */
	public void setSiteType(String siteType) {
		this.siteType = siteType;
	}

	/**
	 * @return the demarcationFloor
	 */
	public String getDemarcationFloor() {
		return demarcationFloor;
	}

	/**
	 * @param demarcationFloor the demarcationFloor to set
	 */
	public void setDemarcationFloor(String demarcationFloor) {
		this.demarcationFloor = demarcationFloor;
	}

	/**
	 * @return the demarcationRoom
	 */
	public String getDemarcationRoom() {
		return demarcationRoom;
	}

	/**
	 * @param demarcationRoom the demarcationRoom to set
	 */
	public void setDemarcationRoom(String demarcationRoom) {
		this.demarcationRoom = demarcationRoom;
	}

	/**
	 * @return the demarcation_apartment
	 */
	public String getDemarcationApartment() {
		return demarcationApartment;
	}

	/**
	 * @param demarcationApartment the demarcation_apartment to set
	 */
	public void setDemarcationApartment(String demarcationApartment) {
		this.demarcationApartment = demarcationApartment;
	}

	/**
	 * @return the orderSysId
	 */
	public Integer getOrderSysId() {
		return orderSysId;
	}

	/**
	 * @param orderSysId the orderSysId to set
	 */
	public void setOrderSysId(Integer orderSysId) {
		this.orderSysId = orderSysId;
	}

	/**
	 * @return the orderCode
	 */
	public String getOrderCode() {
		return orderCode;
	}

	/**
	 * @param orderCode the orderCode to set
	 */
	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	/**
	 * @return the opportunityType
	 */
	public String getOpportunityType() {
		return opportunityType;
	}

	/**
	 * @param opportunityType the opportunityType to set
	 */
	public void setOpportunityType(String opportunityType) {
		this.opportunityType = opportunityType;
	}

	/**
	 * @return the orderDemoFlag
	 */
	public String getOrderDemoFlag() {
		return orderDemoFlag;
	}

	/**
	 * @param orderDemoFlag the orderDemoFlag to set
	 */
	public void setOrderDemoFlag(String orderDemoFlag) {
		this.orderDemoFlag = orderDemoFlag;
	}

	/**
	 * @return the orderCustomerId
	 */
	public Integer getOrderCustomerId() {
		return orderCustomerId;
	}

	/**
	 * @param orderCustomerId the orderCustomerId to set
	 */
	public void setOrderCustomerId(Integer orderCustomerId) {
		this.orderCustomerId = orderCustomerId;
	}

	/**
	 * @return the orderCustomer
	 */
	public String getOrderCustomer() {
		return orderCustomer;
	}

	/**
	 * @param orderCustomer the orderCustomer to set
	 */
	public void setOrderCustomer(String orderCustomer) {
		this.orderCustomer = orderCustomer;
	}

	/**
	 * @return the orderCustLeId
	 */
	public Integer getOrderCustLeId() {
		return orderCustLeId;
	}

	/**
	 * @param orderCustLeId the orderCustLeId to set
	 */
	public void setOrderCustLeId(Integer orderCustLeId) {
		this.orderCustLeId = orderCustLeId;
	}

	/**
	 * @return the orderCustLeName
	 */
	public String getOrderCustLeName() {
		return orderCustLeName;
	}

	/**
	 * @param orderCustLeName the orderCustLeName to set
	 */
	public void setOrderCustLeName(String orderCustLeName) {
		this.orderCustLeName = orderCustLeName;
	}

	/**
	 * @return the orderSpLeId
	 */
	public Integer getOrderSpLeId() {
		return orderSpLeId;
	}

	/**
	 * @param orderSpLeId the orderSpLeId to set
	 */
	public void setOrderSpLeId(Integer orderSpLeId) {
		this.orderSpLeId = orderSpLeId;
	}

	/**
	 * @return the orderSpLeName
	 */
	public String getOrderSpLeName() {
		return orderSpLeName;
	}

	/**
	 * @param orderSpLeName the orderSpLeName to set
	 */
	public void setOrderSpLeName(String orderSpLeName) {
		this.orderSpLeName = orderSpLeName;
	}

	/**
	 * @return the orderPartner
	 */
	public String getOrderPartner() {
		return orderPartner;
	}

	/**
	 * @param orderPartner the orderPartner to set
	 */
	public void setOrderPartner(String orderPartner) {
		this.orderPartner = orderPartner;
	}

	/**
	 * @return the orderPartnerName
	 */
	public String getOrderPartnerName() {
		return orderPartnerName;
	}

	/**
	 * @param orderPartnerName the orderPartnerName to set
	 */
	public void setOrderPartnerName(String orderPartnerName) {
		this.orderPartnerName = orderPartnerName;
	}

	/**
	 * @return the orderInitiator
	 */
	public String getOrderInitiator() {
		return orderInitiator;
	}

	/**
	 * @param orderInitiator the orderInitiator to set
	 */
	public void setOrderInitiator(String orderInitiator) {
		this.orderInitiator = orderInitiator;
	}

	/**
	 * @return the orderCreatedBy
	 */
	public String getOrderCreatedBy() {
		return orderCreatedBy;
	}

	/**
	 * @param orderCreatedBy the orderCreatedBy to set
	 */
	public void setOrderCreatedBy(String orderCreatedBy) {
		this.orderCreatedBy = orderCreatedBy;
	}

	/**
	 * @return the orderCreatedDate
	 */
	public Date getOrderCreatedDate() {
		return orderCreatedDate;
	}

	/**
	 * @param orderCreatedDate the orderCreatedDate to set
	 */
	public void setOrderCreatedDate(Date orderCreatedDate) {
		this.orderCreatedDate = orderCreatedDate;
	}

	/**
	 * @return the orderIsActive
	 */
	public String getOrderIsActive() {
		return orderIsActive;
	}

	/**
	 * @param orderIsActive the orderIsActive to set
	 */
	public void setOrderIsActive(String orderIsActive) {
		this.orderIsActive = orderIsActive;
	}

	/**
	 * @return the orderMultipleLeFlag
	 */
	public String getOrderMultipleLeFlag() {
		return orderMultipleLeFlag;
	}

	/**
	 * @param orderMultipleLeFlag the orderMultipleLeFlag to set
	 */
	public void setOrderMultipleLeFlag(String orderMultipleLeFlag) {
		this.orderMultipleLeFlag = orderMultipleLeFlag;
	}

	/**
	 * @return the orderBundleOrderFlag
	 */
	public String getOrderBundleOrderFlag() {
		return orderBundleOrderFlag;
	}

	/**
	 * @param orderBundleOrderFlag the orderBundleOrderFlag to set
	 */
	public void setOrderBundleOrderFlag(String orderBundleOrderFlag) {
		this.orderBundleOrderFlag = orderBundleOrderFlag;
	}

	/**
	 * @return the sfdcCuId
	 */
	public Integer getSfdcCuId() {
		return sfdcCuId;
	}

	/**
	 * @param sfdcCuId the sfdcCuId to set
	 */
	public void setSfdcCuId(Integer sfdcCuId) {
		this.sfdcCuId = sfdcCuId;
	}

	/**
	 * @return the accountManager
	 */
	public String getAccountManager() {
		return accountManager;
	}

	/**
	 * @param accountManager the accountManager to set
	 */
	public void setAccountManager(String accountManager) {
		this.accountManager = accountManager;
	}

	/**
	 * @return the accountManagerEmail
	 */
	public String getAccountManagerEmail() {
		return accountManagerEmail;
	}

	/**
	 * @param accountManagerEmail the accountManagerEmail to set
	 */
	public void setAccountManagerEmail(String accountManagerEmail) {
		this.accountManagerEmail = accountManagerEmail;
	}

	/**
	 * @return the customerContact
	 */
	public String getCustomerContact() {
		return customerContact;
	}

	/**
	 * @param customerContact the customerContact to set
	 */
	public void setCustomerContact(String customerContact) {
		this.customerContact = customerContact;
	}

	/**
	 * @return the customerContactEmail
	 */
	public String getCustomerContactEmail() {
		return customerContactEmail;
	}

	/**
	 * @param customerContactEmail the customerContactEmail to set
	 */
	public void setCustomerContactEmail(String customerContactEmail) {
		this.customerContactEmail = customerContactEmail;
	}

	/**
	 * @return the orderTermInMonths
	 */
	public Double getOrderTermInMonths() {
		return orderTermInMonths;
	}

	/**
	 * @param orderTermInMonths the orderTermInMonths to set
	 */
	public void setOrderTermInMonths(Double orderTermInMonths) {
		this.orderTermInMonths = orderTermInMonths;
	}

	/**
	 * @return the billingFrequency
	 */
	public String getBillingFrequency() {
		return billingFrequency;
	}

	/**
	 * @param billingFrequency the billingFrequency to set
	 */
	public void setBillingFrequency(String billingFrequency) {
		this.billingFrequency = billingFrequency;
	}

	/**
	 * @return the contractStartDate
	 */
	public Date getContractStartDate() {
		return contractStartDate;
	}

	/**
	 * @param contractStartDate the contractStartDate to set
	 */
	public void setContractStartDate(Date contractStartDate) {
		this.contractStartDate = contractStartDate;
	}

	/**
	 * @return the contractEndDate
	 */
	public Date getContractEndDate() {
		return contractEndDate;
	}

	/**
	 * @param contractEndDate the contractEndDate to set
	 */
	public void setContractEndDate(Date contractEndDate) {
		this.contractEndDate = contractEndDate;
	}

	/**
	 * @return the lastMacdDate
	 */
	public Date getLastMacdDate() {
		return lastMacdDate;
	}

	/**
	 * @param lastMacdDate the lastMacdDate to set
	 */
	public void setLastMacdDate(Date lastMacdDate) {
		this.lastMacdDate = lastMacdDate;
	}

	/**
	 * @return the mrc
	 */
	public Double getMrc() {
		return mrc;
	}

	/**
	 * @param mrc the mrc to set
	 */
	public void setMrc(Double mrc) {
		this.mrc = mrc;
	}

	/**
	 * @return the nrc
	 */
	public Double getNrc() {
		return nrc;
	}

	/**
	 * @param nrc the nrc to set
	 */
	public void setNrc(Double nrc) {
		this.nrc = nrc;
	}

	/**
	 * @return the arc
	 */
	public Double getArc() {
		return arc;
	}

	/**
	 * @param arc the arc to set
	 */
	public void setArc(Double arc) {
		this.arc = arc;
	}

	/**
	 * @return the discountMrc
	 */
	public Double getDiscountMrc() {
		return discountMrc;
	}

	/**
	 * @param discountMrc the discountMrc to set
	 */
	public void setDiscountMrc(Double discountMrc) {
		this.discountMrc = discountMrc;
	}

	/**
	 * @return the discountNrc
	 */
	public Double getDiscountNrc() {
		return discountNrc;
	}

	/**
	 * @param discountNrc the discountNrc to set
	 */
	public void setDiscountNrc(Double discountNrc) {
		this.discountNrc = discountNrc;
	}

	/**
	 * @return the discountArc
	 */
	public Double getDiscountArc() {
		return discountArc;
	}

	/**
	 * @param discountArc the discountArc to set
	 */
	public void setDiscountArc(Double discountArc) {
		this.discountArc = discountArc;
	}

	/**
	 * @return the currencyId
	 */
	public Integer getCurrencyId() {
		return currencyId;
	}

	/**
	 * @param currencyId the currencyId to set
	 */
	public void setCurrencyId(Integer currencyId) {
		this.currencyId = currencyId;
	}

	/**
	 * @return the billingAddress
	 */
	public String getBillingAddress() {
		return billingAddress;
	}

	/**
	 * @param billingAddress the billingAddress to set
	 */
	public void setBillingAddress(String billingAddress) {
		this.billingAddress = billingAddress;
	}

	/**
	 * @return the billingMethod
	 */
	public String getBillingMethod() {
		return billingMethod;
	}

	/**
	 * @param billingMethod the billingMethod to set
	 */
	public void setBillingMethod(String billingMethod) {
		this.billingMethod = billingMethod;
	}

	public String getSiteEndInterface() {
		return siteEndInterface;
	}

	public void setSiteEndInterface(String siteEndInterface) {
		this.siteEndInterface = siteEndInterface;
	}

	/**
	 * @return the locationId
	 */
	public Integer getLocationId() {
		return locationId;
	}

	/**
	 * @param locationId the locationId to set
	 */
	public void setLocationId(Integer locationId) {
		this.locationId = locationId;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public String getAdditionalIpsReq() {
		return additionalIpsReq;
	}

	public void setAdditionalIpsReq(String additionalIpsReq) {
		this.additionalIpsReq = additionalIpsReq;
	}

	public String getIpAddressArrangement() {
		return ipAddressArrangement;
	}

	public void setIpAddressArrangement(String ipAddressArrangement) {
		this.ipAddressArrangement = ipAddressArrangement;
	}

	public String getIpv4AddressPoolSize() {
		return ipv4AddressPoolSize;
	}

	public void setIpv4AddressPoolSize(String ipv4AddressPoolSize) {
		this.ipv4AddressPoolSize = ipv4AddressPoolSize;
	}

	public String getIpv6AddressPoolSize() {
		return ipv6AddressPoolSize;
	}

	public void setIpv6AddressPoolSize(String ipv6AddressPoolSize) {
		this.ipv6AddressPoolSize = ipv6AddressPoolSize;
	}

	public Integer getOpportunityId() {
		return opportunityId;
	}

	public void setOpportunityId(Integer opportunityId) {
		this.opportunityId = opportunityId;
	}

	public String getCommittedSla() {
		return committedSla;
	}

	public void setCommittedSla(String committedSla) {
		this.committedSla = committedSla;
	}

	public String getSiteClassification() {
		return siteClassification;
	}

	public void setSiteClassification(String siteClassification) {
		this.siteClassification = siteClassification;
	}

	public Integer getTpsCrmCofId() {
		return tpsCrmCofId;
	}

	public void setTpsCrmCofId(Integer tpsCrmCofId) {
		this.tpsCrmCofId = tpsCrmCofId;
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

	public String getDemarcationRack() {
		return demarcationRack;
	}

	public void setDemarcationRack(String demarcationRack) {
		this.demarcationRack = demarcationRack;
	}

	public String getBillingType() {
		return billingType;
	}

	public void setBillingType(String billingType) {
		this.billingType = billingType;
	}

	public String getTpsCopfId() {
		return tpsCopfId;
	}

	public void setTpsCopfId(String tpsCopfId) {
		this.tpsCopfId = tpsCopfId;
	}

	public String getCurrentOpportunityType() {
		return currentOpportunityType;
	}

	public void setCurrentOpportunityType(String currentOpportunityType) {
		this.currentOpportunityType = currentOpportunityType;
	}

	public Date getCircuitExpiryDate() {
		return circuitExpiryDate;
	}

	public void setCircuitExpiryDate(Date circuitExpiryDate) {
		this.circuitExpiryDate = circuitExpiryDate;
	}


	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
}

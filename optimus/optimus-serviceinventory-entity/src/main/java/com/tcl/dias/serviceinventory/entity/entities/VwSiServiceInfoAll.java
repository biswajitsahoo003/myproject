package com.tcl.dias.serviceinventory.entity.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * 
 * This is the entity class for vw_si_service_info_all table
 * 
 *
 * @author AnandhiV
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name="vw_si_service_info_all")
@NamedQuery(name="VwSiServiceInfoAll.findAll", query="SELECT v FROM VwSiServiceInfoAll v")
public class VwSiServiceInfoAll implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name="account_manager")
	private String accountManager;

	@Column(name="account_manager_email")
	private String accountManagerEmail;

	@Column(name="additional_ips_req")
	private String additionalIpsReq;

	private Double arc;

	@Column(name="backup_config_mode")
	private String backupConfigMode;

	@Column(name="billing_address")
	private String billingAddress;

	@Column(name="billing_frequency")
	private String billingFrequency;

	@Column(name="billing_method")
	private String billingMethod;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="contract_end_date")
	private Date contractEndDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="contract_start_date")
	private Date contractStartDate;

	@Column(name="currency_id")
	private Integer currencyId;

	@Column(name="customer_contact")
	private String customerContact;

	@Column(name="customer_contact_email")
	private String customerContactEmail;

	@Column(name="customer_service_id")
	private String customerServiceId;

	@Column(name="demarcation_apartment")
	private String demarcationApartment;

	@Column(name="demarcation_floor")
	private String demarcationFloor;

	@Column(name="denarcation_room")
	private String denarcationRoom;

	@Column(name="discount_arc")
	private Double discountArc;

	@Column(name="discount_mrc")
	private Double discountMrc;

	@Column(name="discount_nrc")
	private Double discountNrc;

	@Column(name="erf_loc_site_address_id")
	private String erfLocSiteAddressId;

	@Column(name="ip_address_arrangement_type")
	private String ipAddressArrangementType;

	private String IP_address_provided_by;

	@Column(name="ipv4_address_pool_size")
	private String ipv4AddressPoolSize;

	@Column(name="ipv6_address_pool_size")
	private String ipv6AddressPoolSize;

	@Column(name="is_active")
	private String isActive;

	@Column(name="last_macd_date")
	private String lastMacdDate;

	private Double mrc;

	private Double nrc;

	@Column(name="opportunity_type")
	private String opportunityType;

	@Column(name="order_bundle_order_flag")
	private String orderBundleOrderFlag;

	@Column(name="order_code")
	private String orderCode;

	@Column(name="order_created_by")
	private String orderCreatedBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="order_created_date")
	private Date orderCreatedDate;

	@Column(name="order_cust_le_id")
	private Integer orderCustLeId;

	@Column(name="order_cust_le_name")
	private String orderCustLeName;

	@Column(name="order_customer")
	private String orderCustomer;

	@Column(name="order_customer_id")
	private String orderCustomerId;

	@Column(name="order_demo_flag")
	private String orderDemoFlag;

	@Column(name="order_initiator")
	private Integer orderInitiator;

	@Column(name="order_is_active")
	private String orderIsActive;

	@Column(name="order_multiple_le_flag")
	private String orderMultipleLeFlag;

	@Column(name="order_partner")
	private Integer orderPartner;

	@Column(name="order_partner_name")
	private String orderPartnerName;

	@Column(name="order_sp_le_id")
	private Integer orderSpLeId;

	@Column(name="order_sp_le_name")
	private String orderSpLeName;

	@Column(name="order_sys_id")
	private Integer orderSysId;

	@Column(name="order_term_in_months")
	private Double orderTermInMonths;

	@Column(name="parent_bundle_service_id")
	private Integer parentBundleServiceId;

	@Column(name="parent_bundle_service_name")
	private String parentBundleServiceName;

	private String remarks;

	@Column(name="resiliency_ind")
	private String resiliencyInd;

	@Column(name="routing_protocol")
	private String routingProtocol;

	@Column(name="service_type")
	private String serviceType;

	@Column(name="service_varient")
	private String serviceVarient;

	@Column(name="sfdc_cuid")
	private String sfdcCuid;

	@Column(name="srv_access_topology")
	private String srvAccessTopology;

	@Column(name="srv_access_type")
	private String srvAccessType;

	@Column(name="srv_bandwidth")
	private String srvBandwidth;

	@Column(name="srv_bandwidth_disp_name")
	private String srvBandwidthDispName;

	@Column(name="srv_bandwidth_unit")
	private String srvBandwidthUnit;

	@Column(name="srv_burstable_bw")
	private String srvBurstableBw;

	@Column(name="srv_burstable_bw_disp_name")
	private String srvBurstableBwDispName;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="srv_commissioned_date")
	private Date srvCommissionedDate;

	@Column(name="srv_customer_site_address")
	private String srvCustomerSiteAddress;

	@Column(name="srv_destination_city")
	private String srvDestinationCity;

	@Column(name="srv_destination_country")
	private String srvDestinationCountry;

	@Column(name="srv_destination_country_code")
	private String srvDestinationCountryCode;

	@Column(name="srv_destination_country_code_repc")
	private String srvDestinationCountryCodeRepc;

	@Column(name="srv_gsc_order_sequence_id")
	private String srvGscOrderSequenceId;

	@Column(name="srv_gvpn_cos")
	private String srvGvpnCos;

	@Column(name="srv_gvpn_site_topology")
	private String srvGvpnSiteTopology;

	@Column(name="srv_last_mile_type")
	private String srvLastMileType;

	@Column(name="srv_lastmile_bandwidth")
	private String srvLastmileBandwidth;

	@Column(name="srv_lastmile_bandwidth_disp_name")
	private String srvLastmileBandwidthDispName;

	@Column(name="srv_lastmile_bandwidth_unit")
	private String srvLastmileBandwidthUnit;

	@Column(name="srv_lastmile_provider")
	private String srvLastmileProvider;

	@Column(name="srv_lat_long")
	private String srvLatLong;

	@Column(name="srv_pop_address")
	private String srvPopAddress;

	@Column(name="srv_pop_code")
	private String srvPopCode;

	@Column(name="srv_port_mode")
	private String srvPortMode;

	@Column(name="srv_pri_sec")
	private String srvPriSec;

	@Column(name="srv_pri_sec_link")
	private String srvPriSecLink;

	@Column(name="srv_primary_service_id")
	private String srvPrimaryServiceId;

	@Column(name="srv_product_family_id")
	private Integer srvProductFamilyId;

	@Column(name="srv_product_family_name")
	private String srvProductFamilyName;

	@Column(name="srv_product_offering_id")
	private Integer srvProductOfferingId;

	@Column(name="srv_product_offering_name")
	private String srvProductOfferingName;

	@Column(name="srv_service_classification")
	private String srvServiceClassification;

	@Column(name="srv_service_id")
	private String srvServiceId;

	@Column(name="srv_service_management_option")
	private String srvServiceManagementOption;

	@Column(name="srv_service_record_sequence")
	private Integer srvServiceRecordSequence;

	@Column(name="srv_service_status")
	private String srvServiceStatus;

	@Column(name="srv_site_alias")
	private String srvSiteAlias;

	@Column(name="srv_site_end_interface")
	private String srvSiteEndInterface;

	@Column(name="srv_site_type")
	private String srvSiteType;

	@Column(name="srv_source_city")
	private String srvSourceCity;

	@Column(name="srv_source_country")
	private String srvSourceCountry;

	@Column(name="srv_source_country_code")
	private String srvSourceCountryCode;

	@Column(name="srv_source_country_code_repc")
	private String srvSourceCountryCodeRepc;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="srv_termination_date")
	private Date srvTerminationDate;

	@Column(name="srv_vpn_name")
	private String srvVpnName;

	@Column(name="srv_vpn_topology")
	private String srvVpnTopology;

	@Column(name="srv_vurstable_bw_unit")
	private String srvVurstableBwUnit;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="sys_id")
	private Integer sysId;

	@Column(name="usage_model")
	private String usageModel;

	public VwSiServiceInfoAll() {
	}

	public String getAccountManager() {
		return this.accountManager;
	}

	public void setAccountManager(String accountManager) {
		this.accountManager = accountManager;
	}

	public String getAccountManagerEmail() {
		return this.accountManagerEmail;
	}

	public void setAccountManagerEmail(String accountManagerEmail) {
		this.accountManagerEmail = accountManagerEmail;
	}

	public String getAdditionalIpsReq() {
		return this.additionalIpsReq;
	}

	public void setAdditionalIpsReq(String additionalIpsReq) {
		this.additionalIpsReq = additionalIpsReq;
	}

	public Double getArc() {
		return this.arc;
	}

	public void setArc(Double arc) {
		this.arc = arc;
	}

	public String getBackupConfigMode() {
		return this.backupConfigMode;
	}

	public void setBackupConfigMode(String backupConfigMode) {
		this.backupConfigMode = backupConfigMode;
	}

	public String getBillingAddress() {
		return this.billingAddress;
	}

	public void setBillingAddress(String billingAddress) {
		this.billingAddress = billingAddress;
	}

	public String getBillingFrequency() {
		return this.billingFrequency;
	}

	public void setBillingFrequency(String billingFrequency) {
		this.billingFrequency = billingFrequency;
	}

	public String getBillingMethod() {
		return this.billingMethod;
	}

	public void setBillingMethod(String billingMethod) {
		this.billingMethod = billingMethod;
	}

	public Date getContractEndDate() {
		return this.contractEndDate;
	}

	public void setContractEndDate(Date contractEndDate) {
		this.contractEndDate = contractEndDate;
	}

	public Date getContractStartDate() {
		return this.contractStartDate;
	}

	public void setContractStartDate(Date contractStartDate) {
		this.contractStartDate = contractStartDate;
	}

	public Integer getCurrencyId() {
		return this.currencyId;
	}

	public void setCurrencyId(Integer currencyId) {
		this.currencyId = currencyId;
	}

	public String getCustomerContact() {
		return this.customerContact;
	}

	public void setCustomerContact(String customerContact) {
		this.customerContact = customerContact;
	}

	public String getCustomerContactEmail() {
		return this.customerContactEmail;
	}

	public void setCustomerContactEmail(String customerContactEmail) {
		this.customerContactEmail = customerContactEmail;
	}

	public String getCustomerServiceId() {
		return this.customerServiceId;
	}

	public void setCustomerServiceId(String customerServiceId) {
		this.customerServiceId = customerServiceId;
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

	public String getDenarcationRoom() {
		return this.denarcationRoom;
	}

	public void setDenarcationRoom(String denarcationRoom) {
		this.denarcationRoom = denarcationRoom;
	}

	public Double getDiscountArc() {
		return this.discountArc;
	}

	public void setDiscountArc(Double discountArc) {
		this.discountArc = discountArc;
	}

	public Double getDiscountMrc() {
		return this.discountMrc;
	}

	public void setDiscountMrc(Double discountMrc) {
		this.discountMrc = discountMrc;
	}

	public Double getDiscountNrc() {
		return this.discountNrc;
	}

	public void setDiscountNrc(Double discountNrc) {
		this.discountNrc = discountNrc;
	}

	public String getErfLocSiteAddressId() {
		return this.erfLocSiteAddressId;
	}

	public void setErfLocSiteAddressId(String erfLocSiteAddressId) {
		this.erfLocSiteAddressId = erfLocSiteAddressId;
	}

	public String getIpAddressArrangementType() {
		return this.ipAddressArrangementType;
	}

	public void setIpAddressArrangementType(String ipAddressArrangementType) {
		this.ipAddressArrangementType = ipAddressArrangementType;
	}

	public String getIP_address_provided_by() {
		return this.IP_address_provided_by;
	}

	public void setIP_address_provided_by(String IP_address_provided_by) {
		this.IP_address_provided_by = IP_address_provided_by;
	}

	public String getIpv4AddressPoolSize() {
		return this.ipv4AddressPoolSize;
	}

	public void setIpv4AddressPoolSize(String ipv4AddressPoolSize) {
		this.ipv4AddressPoolSize = ipv4AddressPoolSize;
	}

	public String getIpv6AddressPoolSize() {
		return this.ipv6AddressPoolSize;
	}

	public void setIpv6AddressPoolSize(String ipv6AddressPoolSize) {
		this.ipv6AddressPoolSize = ipv6AddressPoolSize;
	}

	public String getIsActive() {
		return this.isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public String getLastMacdDate() {
		return this.lastMacdDate;
	}

	public void setLastMacdDate(String lastMacdDate) {
		this.lastMacdDate = lastMacdDate;
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

	public String getOpportunityType() {
		return this.opportunityType;
	}

	public void setOpportunityType(String opportunityType) {
		this.opportunityType = opportunityType;
	}

	public String getOrderBundleOrderFlag() {
		return this.orderBundleOrderFlag;
	}

	public void setOrderBundleOrderFlag(String orderBundleOrderFlag) {
		this.orderBundleOrderFlag = orderBundleOrderFlag;
	}

	public String getOrderCode() {
		return this.orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public String getOrderCreatedBy() {
		return this.orderCreatedBy;
	}

	public void setOrderCreatedBy(String orderCreatedBy) {
		this.orderCreatedBy = orderCreatedBy;
	}

	public Date getOrderCreatedDate() {
		return this.orderCreatedDate;
	}

	public void setOrderCreatedDate(Date orderCreatedDate) {
		this.orderCreatedDate = orderCreatedDate;
	}

	public Integer getOrderCustLeId() {
		return this.orderCustLeId;
	}

	public void setOrderCustLeId(Integer orderCustLeId) {
		this.orderCustLeId = orderCustLeId;
	}

	public String getOrderCustLeName() {
		return this.orderCustLeName;
	}

	public void setOrderCustLeName(String orderCustLeName) {
		this.orderCustLeName = orderCustLeName;
	}

	public String getOrderCustomer() {
		return this.orderCustomer;
	}

	public void setOrderCustomer(String orderCustomer) {
		this.orderCustomer = orderCustomer;
	}

	public String getOrderCustomerId() {
		return this.orderCustomerId;
	}

	public void setOrderCustomerId(String orderCustomerId) {
		this.orderCustomerId = orderCustomerId;
	}

	public String getOrderDemoFlag() {
		return this.orderDemoFlag;
	}

	public void setOrderDemoFlag(String orderDemoFlag) {
		this.orderDemoFlag = orderDemoFlag;
	}

	public Integer getOrderInitiator() {
		return this.orderInitiator;
	}

	public void setOrderInitiator(Integer orderInitiator) {
		this.orderInitiator = orderInitiator;
	}

	public String getOrderIsActive() {
		return this.orderIsActive;
	}

	public void setOrderIsActive(String orderIsActive) {
		this.orderIsActive = orderIsActive;
	}

	public String getOrderMultipleLeFlag() {
		return this.orderMultipleLeFlag;
	}

	public void setOrderMultipleLeFlag(String orderMultipleLeFlag) {
		this.orderMultipleLeFlag = orderMultipleLeFlag;
	}

	public Integer getOrderPartner() {
		return this.orderPartner;
	}

	public void setOrderPartner(Integer orderPartner) {
		this.orderPartner = orderPartner;
	}

	public String getOrderPartnerName() {
		return this.orderPartnerName;
	}

	public void setOrderPartnerName(String orderPartnerName) {
		this.orderPartnerName = orderPartnerName;
	}

	public Integer getOrderSpLeId() {
		return this.orderSpLeId;
	}

	public void setOrderSpLeId(Integer orderSpLeId) {
		this.orderSpLeId = orderSpLeId;
	}

	public String getOrderSpLeName() {
		return this.orderSpLeName;
	}

	public void setOrderSpLeName(String orderSpLeName) {
		this.orderSpLeName = orderSpLeName;
	}

	public Integer getOrderSysId() {
		return this.orderSysId;
	}

	public void setOrderSysId(Integer orderSysId) {
		this.orderSysId = orderSysId;
	}

	public Double getOrderTermInMonths() {
		return this.orderTermInMonths;
	}

	public void setOrderTermInMonths(Double orderTermInMonths) {
		this.orderTermInMonths = orderTermInMonths;
	}

	public Integer getParentBundleServiceId() {
		return this.parentBundleServiceId;
	}

	public void setParentBundleServiceId(Integer parentBundleServiceId) {
		this.parentBundleServiceId = parentBundleServiceId;
	}

	public String getParentBundleServiceName() {
		return this.parentBundleServiceName;
	}

	public void setParentBundleServiceName(String parentBundleServiceName) {
		this.parentBundleServiceName = parentBundleServiceName;
	}

	public String getRemarks() {
		return this.remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getResiliencyInd() {
		return this.resiliencyInd;
	}

	public void setResiliencyInd(String resiliencyInd) {
		this.resiliencyInd = resiliencyInd;
	}

	public String getRoutingProtocol() {
		return this.routingProtocol;
	}

	public void setRoutingProtocol(String routingProtocol) {
		this.routingProtocol = routingProtocol;
	}

	public String getServiceType() {
		return this.serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public String getServiceVarient() {
		return this.serviceVarient;
	}

	public void setServiceVarient(String serviceVarient) {
		this.serviceVarient = serviceVarient;
	}

	public String getSfdcCuid() {
		return this.sfdcCuid;
	}

	public void setSfdcCuid(String sfdcCuid) {
		this.sfdcCuid = sfdcCuid;
	}

	public String getSrvAccessTopology() {
		return this.srvAccessTopology;
	}

	public void setSrvAccessTopology(String srvAccessTopology) {
		this.srvAccessTopology = srvAccessTopology;
	}

	public String getSrvAccessType() {
		return this.srvAccessType;
	}

	public void setSrvAccessType(String srvAccessType) {
		this.srvAccessType = srvAccessType;
	}

	public String getSrvBandwidth() {
		return this.srvBandwidth;
	}

	public void setSrvBandwidth(String srvBandwidth) {
		this.srvBandwidth = srvBandwidth;
	}

	public String getSrvBandwidthDispName() {
		return this.srvBandwidthDispName;
	}

	public void setSrvBandwidthDispName(String srvBandwidthDispName) {
		this.srvBandwidthDispName = srvBandwidthDispName;
	}

	public String getSrvBandwidthUnit() {
		return this.srvBandwidthUnit;
	}

	public void setSrvBandwidthUnit(String srvBandwidthUnit) {
		this.srvBandwidthUnit = srvBandwidthUnit;
	}

	public String getSrvBurstableBw() {
		return this.srvBurstableBw;
	}

	public void setSrvBurstableBw(String srvBurstableBw) {
		this.srvBurstableBw = srvBurstableBw;
	}

	public String getSrvBurstableBwDispName() {
		return this.srvBurstableBwDispName;
	}

	public void setSrvBurstableBwDispName(String srvBurstableBwDispName) {
		this.srvBurstableBwDispName = srvBurstableBwDispName;
	}

	public Date getSrvCommissionedDate() {
		return this.srvCommissionedDate;
	}

	public void setSrvCommissionedDate(Date srvCommissionedDate) {
		this.srvCommissionedDate = srvCommissionedDate;
	}

	public String getSrvCustomerSiteAddress() {
		return this.srvCustomerSiteAddress;
	}

	public void setSrvCustomerSiteAddress(String srvCustomerSiteAddress) {
		this.srvCustomerSiteAddress = srvCustomerSiteAddress;
	}

	public String getSrvDestinationCity() {
		return this.srvDestinationCity;
	}

	public void setSrvDestinationCity(String srvDestinationCity) {
		this.srvDestinationCity = srvDestinationCity;
	}

	public String getSrvDestinationCountry() {
		return this.srvDestinationCountry;
	}

	public void setSrvDestinationCountry(String srvDestinationCountry) {
		this.srvDestinationCountry = srvDestinationCountry;
	}

	public String getSrvDestinationCountryCode() {
		return this.srvDestinationCountryCode;
	}

	public void setSrvDestinationCountryCode(String srvDestinationCountryCode) {
		this.srvDestinationCountryCode = srvDestinationCountryCode;
	}

	public String getSrvDestinationCountryCodeRepc() {
		return this.srvDestinationCountryCodeRepc;
	}

	public void setSrvDestinationCountryCodeRepc(String srvDestinationCountryCodeRepc) {
		this.srvDestinationCountryCodeRepc = srvDestinationCountryCodeRepc;
	}

	public String getSrvGscOrderSequenceId() {
		return this.srvGscOrderSequenceId;
	}

	public void setSrvGscOrderSequenceId(String srvGscOrderSequenceId) {
		this.srvGscOrderSequenceId = srvGscOrderSequenceId;
	}

	public String getSrvGvpnCos() {
		return this.srvGvpnCos;
	}

	public void setSrvGvpnCos(String srvGvpnCos) {
		this.srvGvpnCos = srvGvpnCos;
	}

	public String getSrvGvpnSiteTopology() {
		return this.srvGvpnSiteTopology;
	}

	public void setSrvGvpnSiteTopology(String srvGvpnSiteTopology) {
		this.srvGvpnSiteTopology = srvGvpnSiteTopology;
	}

	public String getSrvLastMileType() {
		return this.srvLastMileType;
	}

	public void setSrvLastMileType(String srvLastMileType) {
		this.srvLastMileType = srvLastMileType;
	}

	public String getSrvLastmileBandwidth() {
		return this.srvLastmileBandwidth;
	}

	public void setSrvLastmileBandwidth(String srvLastmileBandwidth) {
		this.srvLastmileBandwidth = srvLastmileBandwidth;
	}

	public String getSrvLastmileBandwidthDispName() {
		return this.srvLastmileBandwidthDispName;
	}

	public void setSrvLastmileBandwidthDispName(String srvLastmileBandwidthDispName) {
		this.srvLastmileBandwidthDispName = srvLastmileBandwidthDispName;
	}

	public String getSrvLastmileBandwidthUnit() {
		return this.srvLastmileBandwidthUnit;
	}

	public void setSrvLastmileBandwidthUnit(String srvLastmileBandwidthUnit) {
		this.srvLastmileBandwidthUnit = srvLastmileBandwidthUnit;
	}

	public String getSrvLastmileProvider() {
		return this.srvLastmileProvider;
	}

	public void setSrvLastmileProvider(String srvLastmileProvider) {
		this.srvLastmileProvider = srvLastmileProvider;
	}

	public String getSrvLatLong() {
		return this.srvLatLong;
	}

	public void setSrvLatLong(String srvLatLong) {
		this.srvLatLong = srvLatLong;
	}

	public String getSrvPopAddress() {
		return this.srvPopAddress;
	}

	public void setSrvPopAddress(String srvPopAddress) {
		this.srvPopAddress = srvPopAddress;
	}

	public String getSrvPopCode() {
		return this.srvPopCode;
	}

	public void setSrvPopCode(String srvPopCode) {
		this.srvPopCode = srvPopCode;
	}

	public String getSrvPortMode() {
		return this.srvPortMode;
	}

	public void setSrvPortMode(String srvPortMode) {
		this.srvPortMode = srvPortMode;
	}

	public String getSrvPriSec() {
		return this.srvPriSec;
	}

	public void setSrvPriSec(String srvPriSec) {
		this.srvPriSec = srvPriSec;
	}

	public String getSrvPriSecLink() {
		return this.srvPriSecLink;
	}

	public void setSrvPriSecLink(String srvPriSecLink) {
		this.srvPriSecLink = srvPriSecLink;
	}

	public String getSrvPrimaryServiceId() {
		return this.srvPrimaryServiceId;
	}

	public void setSrvPrimaryServiceId(String srvPrimaryServiceId) {
		this.srvPrimaryServiceId = srvPrimaryServiceId;
	}

	public Integer getSrvProductFamilyId() {
		return this.srvProductFamilyId;
	}

	public void setSrvProductFamilyId(Integer srvProductFamilyId) {
		this.srvProductFamilyId = srvProductFamilyId;
	}

	public String getSrvProductFamilyName() {
		return this.srvProductFamilyName;
	}

	public void setSrvProductFamilyName(String srvProductFamilyName) {
		this.srvProductFamilyName = srvProductFamilyName;
	}

	public Integer getSrvProductOfferingId() {
		return this.srvProductOfferingId;
	}

	public void setSrvProductOfferingId(Integer srvProductOfferingId) {
		this.srvProductOfferingId = srvProductOfferingId;
	}

	public String getSrvProductOfferingName() {
		return this.srvProductOfferingName;
	}

	public void setSrvProductOfferingName(String srvProductOfferingName) {
		this.srvProductOfferingName = srvProductOfferingName;
	}

	public String getSrvServiceClassification() {
		return this.srvServiceClassification;
	}

	public void setSrvServiceClassification(String srvServiceClassification) {
		this.srvServiceClassification = srvServiceClassification;
	}

	public String getSrvServiceId() {
		return this.srvServiceId;
	}

	public void setSrvServiceId(String srvServiceId) {
		this.srvServiceId = srvServiceId;
	}

	public String getSrvServiceManagementOption() {
		return this.srvServiceManagementOption;
	}

	public void setSrvServiceManagementOption(String srvServiceManagementOption) {
		this.srvServiceManagementOption = srvServiceManagementOption;
	}

	public Integer getSrvServiceRecordSequence() {
		return this.srvServiceRecordSequence;
	}

	public void setSrvServiceRecordSequence(Integer srvServiceRecordSequence) {
		this.srvServiceRecordSequence = srvServiceRecordSequence;
	}

	public String getSrvServiceStatus() {
		return this.srvServiceStatus;
	}

	public void setSrvServiceStatus(String srvServiceStatus) {
		this.srvServiceStatus = srvServiceStatus;
	}	

	public String getSrvSiteAlias() {
		return this.srvSiteAlias;
	}

	public void setSrvSiteAlias(String srvSiteAlias) {
		this.srvSiteAlias = srvSiteAlias;
	}

	public String getSrvSiteEndInterface() {
		return this.srvSiteEndInterface;
	}

	public void setSrvSiteEndInterface(String srvSiteEndInterface) {
		this.srvSiteEndInterface = srvSiteEndInterface;
	}

	public String getSrvSiteType() {
		return this.srvSiteType;
	}

	public void setSrvSiteType(String srvSiteType) {
		this.srvSiteType = srvSiteType;
	}

	public String getSrvSourceCity() {
		return this.srvSourceCity;
	}

	public void setSrvSourceCity(String srvSourceCity) {
		this.srvSourceCity = srvSourceCity;
	}

	public String getSrvSourceCountry() {
		return this.srvSourceCountry;
	}

	public void setSrvSourceCountry(String srvSourceCountry) {
		this.srvSourceCountry = srvSourceCountry;
	}

	public String getSrvSourceCountryCode() {
		return this.srvSourceCountryCode;
	}

	public void setSrvSourceCountryCode(String srvSourceCountryCode) {
		this.srvSourceCountryCode = srvSourceCountryCode;
	}

	public String getSrvSourceCountryCodeRepc() {
		return this.srvSourceCountryCodeRepc;
	}

	public void setSrvSourceCountryCodeRepc(String srvSourceCountryCodeRepc) {
		this.srvSourceCountryCodeRepc = srvSourceCountryCodeRepc;
	}

	public Date getSrvTerminationDate() {
		return this.srvTerminationDate;
	}

	public void setSrvTerminationDate(Date srvTerminationDate) {
		this.srvTerminationDate = srvTerminationDate;
	}

	public String getSrvVpnName() {
		return this.srvVpnName;
	}

	public void setSrvVpnName(String srvVpnName) {
		this.srvVpnName = srvVpnName;
	}

	public String getSrvVpnTopology() {
		return this.srvVpnTopology;
	}

	public void setSrvVpnTopology(String srvVpnTopology) {
		this.srvVpnTopology = srvVpnTopology;
	}

	public String getSrvVurstableBwUnit() {
		return this.srvVurstableBwUnit;
	}

	public void setSrvVurstableBwUnit(String srvVurstableBwUnit) {
		this.srvVurstableBwUnit = srvVurstableBwUnit;
	}

	public Integer getSysId() {
		return this.sysId;
	}

	public void setSysId(Integer sysId) {
		this.sysId = sysId;
	}

	public String getUsageModel() {
		return this.usageModel;
	}

	public void setUsageModel(String usageModel) {
		this.usageModel = usageModel;
	}

}
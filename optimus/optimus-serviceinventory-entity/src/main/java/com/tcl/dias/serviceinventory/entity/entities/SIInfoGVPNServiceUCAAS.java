package com.tcl.dias.serviceinventory.entity.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the vw_gvpn_services_qlfy_ucaas database table.
 *
 * @author Srinivasa Raghavan
 * @Link www.tatacommunications.com
 */
@Entity
@Table(name="vw_gvpn_services_qlfy_ucaas")
@NamedQuery(name="SIInfoGVPNServiceUCAAS.findAll", query="SELECT v FROM SIInfoGVPNServiceUCAAS v")
public class SIInfoGVPNServiceUCAAS implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name="sys_id")
    private Integer sysId;

    @Column(name="account_manager")
    private String accountManager;

    @Column(name="account_manager_email")
    private String accountManagerEmail;

    @Column(name="additional_ips_req")
    private String additionalIpsReq;

    private BigDecimal arc;

    @Column(name="backup_config_mode")
    private String backupConfigMode;

    @Column(name="billing_address")
    private String billingAddress;

    @Column(name="billing_frequency")
    private String billingFrequency;

    @Column(name="billing_method")
    private String billingMethod;

    @Column(name="contract_end_date")
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date contractEndDate;

    @Column(name="contract_start_date")
    @Temporal(value = TemporalType.TIMESTAMP)
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
    private BigDecimal discountArc;

    @Column(name="discount_mrc")
    private BigDecimal discountMrc;

    @Column(name="discount_nrc")
    private BigDecimal discountNrc;

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

    private BigDecimal mrc;

    private BigDecimal nrc;

    @Column(name="opportunity_id")
    private String opportunityId;

    @Column(name="opportunity_type")
    private String opportunityType;

    @Column(name="order_bundle_order_flag")
    private String orderBundleOrderFlag;

    @Column(name="order_code")
    private String orderCode;

    @Column(name="order_created_by")
    private String orderCreatedBy;

    @Column(name="order_created_date")
    @Temporal(value = TemporalType.TIMESTAMP)
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
    private Integer orderTermInMonths;

    @Column(name="parent_bundle_service_id")
    private Integer parentBundleServiceId;

    @Column(name="parent_bundle_service_name")
    private String parentBundleServiceName;

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
    private String accessTopology;

    @Column(name="srv_access_type")
    private String accessType;

    @Column(name="srv_bandwidth")
    private String bandwidth;

    @Column(name="srv_bandwidth_disp_name")
    private String bandwidthName;

    @Column(name="srv_bandwidth_unit")
    private String bandwidthUnit;

    @Column(name="srv_burstable_bw")
    private String burstableBandwidth;

    @Column(name="srv_burstable_bw_disp_name")
    private String burstableBandwidthName;

    @Column(name="srv_commissioned_date")
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date commissionedDate;

    @Column(name="srv_customer_site_address")
    private String customerSiteAddress;

    @Column(name="srv_destination_city")
    private String destinationCity;

    @Column(name="srv_destination_country")
    private String destinationCountry;

    @Column(name="srv_destination_country_code")
    private String destinationCountryCode;

    @Column(name="srv_destination_country_code_repc")
    private String destinationCountryCodeRepc;

    @Column(name="srv_gsc_order_sequence_id")
    private String gscOrderSequenceId;

    @Column(name="srv_gvpn_cos")
    private String gvpnCos;

    @Column(name="srv_gvpn_site_topology")
    private String gvpnSiteTopology;

    @Column(name="srv_last_mile_type")
    private String lastMileType;

    @Column(name="srv_lastmile_bandwidth")
    private String lastmileBandwidth;

    @Column(name="srv_lastmile_bandwidth_disp_name")
    private String lastmileBandwidthDispName;

    @Column(name="srv_lastmile_bandwidth_unit")
    private String lastmileBandwidthUnit;

    @Column(name="srv_lastmile_provider")
    private String lastmileProvider;

    @Column(name="srv_lat_long")
    private String latLong;

    @Column(name="srv_pop_address")
    private String popAddress;

    @Column(name="srv_pop_code")
    private String popCode;

    @Column(name="srv_pri_sec")
    private String priSec;

    @Column(name="srv_pri_sec_link")
    private String priSecLink;

    @Column(name="srv_primary_service_id")
    private String primaryServiceId;

    @Column(name="srv_product_family_id")
    private Integer productFamilyId;

    @Column(name="srv_product_family_name")
    private String productFamilyName;

    @Column(name="srv_product_offering_id")
    private Integer productOfferingId;

    @Column(name="srv_product_offering_name")
    private String productOfferingName;

    @Column(name="srv_service_classification")
    private String serviceClassification;

    @Column(name="srv_service_id")
    private String serviceId;

    @Column(name="srv_service_management_option")
    private String serviceManagementOption;

    @Column(name="srv_service_record_sequence")
    private Short serviceRecordSequence;

    @Column(name="srv_service_status")
    private String serviceStatus;

    @Column(name="srv_site_alias")
    private String siteAlias;

    @Column(name="srv_site_end_interface")
    private String siteEndInterface;

    @Column(name="srv_site_type")
    private String siteType;

    @Column(name="srv_source_city")
    private String sourceCity;

    @Column(name="srv_source_country")
    private String sourceCountry;

    @Column(name="srv_source_country_code")
    private String sourceCountryCode;

    @Column(name="srv_source_country_code_repc")
    private String sourceCountryCodeRepc;

    @Column(name="srv_termination_date")
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date terminationDate;

    @Column(name="srv_vpn_name")
    private String vpnName;

    @Column(name="srv_vpn_topology")
    private String vpnTopology;

    @Column(name="srv_vurstable_bw_unit")
    private String burstableBandwidthUnit;

    @Column(name="usage_model")
    private String usageModel;

	@Column(name = "remarks")
	private String remarks;

	@Column(name = "srv_port_mode")
	private String srvPortMode;

    public SIInfoGVPNServiceUCAAS() {
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

    public BigDecimal getArc() {
        return this.arc;
    }

    public void setArc(BigDecimal arc) {
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

    public BigDecimal getMrc() {
        return this.mrc;
    }

    public void setMrc(BigDecimal mrc) {
        this.mrc = mrc;
    }

    public BigDecimal getNrc() {
        return this.nrc;
    }

    public void setNrc(BigDecimal nrc) {
        this.nrc = nrc;
    }

    public String getOpportunityId() {
        return this.opportunityId;
    }

    public void setOpportunityId(String opportunityId) {
        this.opportunityId = opportunityId;
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

    public Integer getOrderTermInMonths() {
        return this.orderTermInMonths;
    }

    public void setOrderTermInMonths(Integer orderTermInMonths) {
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

    public String getAccessTopology() {
        return this.accessTopology;
    }

    public void setAccessTopology(String accessTopology) {
        this.accessTopology = accessTopology;
    }

    public String getAccessType() {
        return this.accessType;
    }

    public void setAccessType(String accessType) {
        this.accessType = accessType;
    }

    public String getBandwidth() {
        return this.bandwidth;
    }

    public void setBandwidth(String bandwidth) {
        this.bandwidth = bandwidth;
    }

    public String getBandwidthName() {
        return this.bandwidthName;
    }

    public void setBandwidthName(String bandwidthName) {
        this.bandwidthName = bandwidthName;
    }

    public String getBandwidthUnit() {
        return this.bandwidthUnit;
    }

    public void setBandwidthUnit(String bandwidthUnit) {
        this.bandwidthUnit = bandwidthUnit;
    }

    public String getBurstableBandwidth() {
        return this.burstableBandwidth;
    }

    public void setBurstableBandwidth(String burstableBandwidth) {
        this.burstableBandwidth = burstableBandwidth;
    }

    public String getBurstableBandwidthName() {
        return this.burstableBandwidthName;
    }

    public void setBurstableBandwidthName(String burstableBandwidthName) {
        this.burstableBandwidthName = burstableBandwidthName;
    }

    public Date getCommissionedDate() {
        return this.commissionedDate;
    }

    public void setCommissionedDate(Date commissionedDate) {
        this.commissionedDate = commissionedDate;
    }

    public String getCustomerSiteAddress() {
        return this.customerSiteAddress;
    }

    public void setCustomerSiteAddress(String customerSiteAddress) {
        this.customerSiteAddress = customerSiteAddress;
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

    public String getGscOrderSequenceId() {
        return this.gscOrderSequenceId;
    }

    public void setGscOrderSequenceId(String gscOrderSequenceId) {
        this.gscOrderSequenceId = gscOrderSequenceId;
    }

    public String getGvpnCos() {
        return this.gvpnCos;
    }

    public void setGvpnCos(String gvpnCos) {
        this.gvpnCos = gvpnCos;
    }

    public String getGvpnSiteTopology() {
        return this.gvpnSiteTopology;
    }

    public void setGvpnSiteTopology(String gvpnSiteTopology) {
        this.gvpnSiteTopology = gvpnSiteTopology;
    }

    public String getLastMileType() {
        return this.lastMileType;
    }

    public void setLastMileType(String lastMileType) {
        this.lastMileType = lastMileType;
    }

    public String getLastmileBandwidth() {
        return this.lastmileBandwidth;
    }

    public void setLastmileBandwidth(String lastmileBandwidth) {
        this.lastmileBandwidth = lastmileBandwidth;
    }

    public String getLastmileBandwidthDispName() {
        return this.lastmileBandwidthDispName;
    }

    public void setLastmileBandwidthDispName(String lastmileBandwidthDispName) {
        this.lastmileBandwidthDispName = lastmileBandwidthDispName;
    }

    public String getLastmileBandwidthUnit() {
        return this.lastmileBandwidthUnit;
    }

    public void setLastmileBandwidthUnit(String lastmileBandwidthUnit) {
        this.lastmileBandwidthUnit = lastmileBandwidthUnit;
    }

    public String getLastmileProvider() {
        return this.lastmileProvider;
    }

    public void setLastmileProvider(String lastmileProvider) {
        this.lastmileProvider = lastmileProvider;
    }

    public String getLatLong() {
        return this.latLong;
    }

    public void setLatLong(String latLong) {
        this.latLong = latLong;
    }

    public String getPopAddress() {
        return this.popAddress;
    }

    public void setPopAddress(String popAddress) {
        this.popAddress = popAddress;
    }

    public String getPopCode() {
        return this.popCode;
    }

    public void setPopCode(String popCode) {
        this.popCode = popCode;
    }

    public String getPriSec() {
        return this.priSec;
    }

    public void setPriSec(String priSec) {
        this.priSec = priSec;
    }

    public String getPriSecLink() {
        return this.priSecLink;
    }

    public void setPriSecLink(String priSecLink) {
        this.priSecLink = priSecLink;
    }

    public String getPrimaryServiceId() {
        return this.primaryServiceId;
    }

    public void setPrimaryServiceId(String primaryServiceId) {
        this.primaryServiceId = primaryServiceId;
    }

    public Integer getProductFamilyId() {
        return this.productFamilyId;
    }

    public void setProductFamilyId(Integer productFamilyId) {
        this.productFamilyId = productFamilyId;
    }

    public String getProductFamilyName() {
        return this.productFamilyName;
    }

    public void setProductFamilyName(String productFamilyName) {
        this.productFamilyName = productFamilyName;
    }

    public Integer getProductOfferingId() {
        return this.productOfferingId;
    }

    public void setProductOfferingId(Integer productOfferingId) {
        this.productOfferingId = productOfferingId;
    }

    public String getProductOfferingName() {
        return this.productOfferingName;
    }

    public void setProductOfferingName(String productOfferingName) {
        this.productOfferingName = productOfferingName;
    }

    public String getServiceClassification() {
        return this.serviceClassification;
    }

    public void setServiceClassification(String serviceClassification) {
        this.serviceClassification = serviceClassification;
    }

    public String getServiceId() {
        return this.serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceManagementOption() {
        return this.serviceManagementOption;
    }

    public void setServiceManagementOption(String serviceManagementOption) {
        this.serviceManagementOption = serviceManagementOption;
    }

    public Short getServiceRecordSequence() {
        return this.serviceRecordSequence;
    }

    public void setServiceRecordSequence(Short serviceRecordSequence) {
        this.serviceRecordSequence = serviceRecordSequence;
    }

    public String getServiceStatus() {
        return this.serviceStatus;
    }

    public void setServiceStatus(String serviceStatus) {
        this.serviceStatus = serviceStatus;
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

    public String getSiteType() {
        return this.siteType;
    }

    public void setSiteType(String siteType) {
        this.siteType = siteType;
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

    public Date getTerminationDate() {
        return this.terminationDate;
    }

    public void setTerminationDate(Date terminationDate) {
        this.terminationDate = terminationDate;
    }

    public String getVpnName() {
        return this.vpnName;
    }

    public void setVpnName(String vpnName) {
        this.vpnName = vpnName;
    }

    public String getVpnTopology() {
        return this.vpnTopology;
    }

    public void setVpnTopology(String vpnTopology) {
        this.vpnTopology = vpnTopology;
    }

    public String getBurstableBandwidthUnit() {
        return this.burstableBandwidthUnit;
    }

    public void setBurstableBandwidthUnit(String burstableBandwidthUnit) {
        this.burstableBandwidthUnit = burstableBandwidthUnit;
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

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getSrvPortMode() {
		return srvPortMode;
	}

	public void setSrvPortMode(String srvPortMode) {
		this.srvPortMode = srvPortMode;
	}
}

package com.tcl.dias.serviceinventory.entity.entities;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.NamedQuery;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;
import javax.persistence.Table;

import jdk.nashorn.internal.ir.annotations.Immutable;


/**
 * The persistent class for the vw_service_inv_SatSoc database table.
 *
 */
@Entity
@Immutable
@IdClass(SatSocId.class)
@Table(name = "vw_service_inv_SatSoc")
@NamedStoredProcedureQuery(name="invStoredProcedure",
        procedureName = "proc_service_inv_SatSoc",
        parameters = {
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "service_id", type = String.class),@StoredProcedureParameter(mode = ParameterMode.OUT, name = "out", type = String.class)},
                resultClasses=Object.class
        )
@NamedQuery(name="VwServiceInvSatSoc.findAll", query="SELECT v FROM VwServiceInvSatSoc v")
public class VwServiceInvSatSoc implements Serializable {
        private static final long serialVersionUID = 1L;

        @Column(name="A_END_BS_IP")
        private String aEndBsIp;

        @Column(name="A_END_BS_NAME")
        private String aEndBsName;

        @Column(name="A_END_BSO_ID")
        private String aEndBsoId;

        @Column(name="A_END_INTERFACE")
        private String aEndInterface;

        @Column(name="A_END_LL_BANDWIDTH")
        private String aEndLlBandwidth;

        @Column(name="A_END_LL_PROVIDER")
        private String aEndLlProvider;

        @Column(name="A_END_SECTOR_ID")
        private String aEndSectorId;

        @Column(name="A_END_SITE_ADDRESS")
        private String aEndSiteAddress;

        @Column(name="A_END_SITE_CITY")
        private String aEndSiteCity;

        @Column(name="A_END_SITE_COUNTRY")
        private String aEndSiteCountry;

        @Column(name="A_STATE")
        private String aState;

        @Column(name="ACC_MANAGER_NAME")
        private String accManagerName;

        @Column(name="ACCOUNT_NAME")
        private String accountName;

        @Column(name="ACCOUNT_RTM")
        private String accountRtm;

        @Column(name="ADDITIONAL_IPV4_IP")
        private String additionalIpv4Ip;

        @Column(name="ADT_IP_ADD_TO_BE_PROVD_BY_VSNL")
        private String adtIpAddToBeProvdByVsnl;

        @Column(name="ADT_NO_IP_ADD_PROVD_BY_VSNL")
        private String adtNoIpAddProvdByVsnl;

        private String alias;

        @Column(name="AMC_END_DATE")
        private String amcEndDate;

        @Column(name="AMC_START_DATE")
        private String amcStartDate;

        private double arc;

        @Column(name="ASD_OPPORTUNITY")
        private String asdOpportunity;

        @Column(name="ASSOCIATE_ID")
        private String associateId;

        private String athenticationPassword;

        private String authenticationMode;

        @Column(name="B_END_BSO_ID")
        private String bEndBsoId;

        @Column(name="B_END_INTERFACE")
        private String bEndInterface;

        @Column(name="B_END_LL_BANDWIDTH")
        private String bEndLlBandwidth;

        @Column(name="B_END_LL_PROVIDER")
        private String bEndLlProvider;

        @Column(name="B_END_POP_ADDRESS")
        private String bEndPopAddress;

        @Column(name="B_END_SITE_ADDRESS")
        private String bEndSiteAddress;

        @Column(name="B_END_SITE_CITY")
        private String bEndSiteCity;

        @Column(name="B_END_SITE_COUNTRY")
        private String bEndSiteCountry;

        @Column(name="B_STATE")
        private String bState;

        private String bandwidth;

        @Column(name="BASE_BANDWIDTH")
        private String baseBandwidth;

        @Column(name="BASE_BANDWIDTH_UNIT")
        private String baseBandwidthUnit;

        private String bfdMultiplier;

        private String bfdReceiveInterval;

        private String bfdTransmitInterval;

        @Column(name="`BGP AS Number`")
        private String BGP_AS_Number;

        private String bgpPeeringOn;

        private String billFreePeriod;

        @Column(name="`Billing Model`")
        private String billing_Model;

        @Column(name="BILLING_ACCOUNT_NUMBER")
        private String billingAccountNumber;

        @Column(name="BILLING_ENTITY")
        private String billingEntity;

        @Column(name="BILLING_ENTITY_CD")
        private String billingEntityCd;

        @Column(name="BILLING_FREQUENCY")
        private String billingFrequency;

        @Column(name="BILLING_TYPE")
        private String billingType;

        private String billStartDate;

        @Column(name="BSN_SWITCH_HANDOFFPORT")
        private String bsnSwitchHandoffport;

        @Column(name="BSN_SWITCH_UPLINKPORT")
        private String bsnSwitchUplinkport;

        @Column(name="BSO_NAME")
        private String bsoName;

        @Column(name="BURST_BANDWIDTH")
        private String burstBandwidth;

        @Column(name="BURST_BANDWIDTH_UNIT")
        private String burstBandwidthUnit;

        @Column(name="BUSINESS_SWITCH_HOSTNAME")
        private String businessSwitchHostname;

        @Column(name="BUSINESS_SWITCH_IP")
        private String businessSwitchIp;

        @Column(name="CIRCUIT_STATUS")
        private String circuitStatus;

        @Column(name="COMMISSIONING_DATE")
        private String commissioningDate;

        private String connectorType;

        @Column(name="CONTRACT_TERM")
        private int contractTerm;

        @Column(name="COPF_ID")
        private String copfId;

        @Column(name="COPF_ID_MRC")
        private double copfIdMrc;

        @Column(name="COS_MODEL")
        private String cosModel;

        @Column(name="COS_PROFILE")
        private String cosProfile;

        private String cos1;

        private String cos2;

        private String cos3;

        private String cos4;

        private String cos5;

        private String cos6;

        @Column(name="`CPE Basic Chassis`")
        private String CPE_Basic_Chassis;

        @Column(name="`CPE Support Type`")
        private String CPE_Support_Type;

        @Column(name="CPE_COMMISSIONING_DATE")
        private String cpeCommissioningDate;

        @Column(name="CPE_DATE_OF_INSTALLATION")
        private String cpeDateOfInstallation;

        @Column(name="CPE_MANAGED")
        private String cpeManaged;

        @Column(name="CPE_PROVIDER")
        private String cpeProvider;

        @Column(name="CPE_SERIAL_NO")
        private String cpeSerialNo;

        @Column(name="CPE_TYPE")
        private String cpeType;

        private String cpeInstallationPoNumber;

        private String cpeInstallationVendorName;

        private String cpeSupplyHardwarePoNumber;

        private String cpeSupportPoNumber;

        private String cpeSupportVendorName;

        @Column(name="CRN_ID")
        private String crnId;

        private String cuid;

        private String currency;

        private String customerLeId;

        @Column(name="`Customer prefixes`")
        private String customer_prefixes;

        @Column(name="CUSTOMER_CATEGORY")
        private String customerCategory;

        @Column(name="CUSTOMER_CONTRACTING_ENTITY")
        private String customerContractingEntity;

        @Column(name="CUSTOMER_NAME")
        private String customerName;

        @Column(name="CUSTOMER_PROJECT_NAME")
        private String customerProjectName;

        @Column(name="CUSTOMER_SEGMENT")
        private String customerSegment;

        @Column(name="CUSTOMER_SERVICE_ID")
        private String customerServiceId;

        @Column(name="CUSTOMER_TYPE")
        private String customerType;

        private String customerAcceptanceDate;

        @Column(name="DATA_SOURCE__C")
        private String dataSourceC;

        private String deemedAcceptance;

        private String demarcationBuildingName;

        private String demarcationFloor;

        private String demarcationRoom;

        private String demarcationWing;

        @Column(name="DEMO_FLAG")
        private String demoFlag;

        private String destinationAddressLineOne;

        private String destinationAddressLineTwo;

        private String destinationPinCode;

        @Column(name="END_CUST_NAME")
        private String endCustName;

        @Column(name="END_CUSTOMER_NAME")
        private String endCustomerName;

        private String endMuxNodeIp;

        private String endMuxNodeName;

        @Column(name="EQUIPMENT_MODEL")
        private String equipmentModel;

        private String equipmentmake;

        private String extendedLanRequired;

        @Column(name="FEASIBILITY_ID")
        private String feasibilityId;

        @Column(name="FINAL_STATUS")
        private String finalStatus;

        @Column(name="GROUP_NAME")
        private String groupName;

        @Column(name="HOST_NAME")
        private String hostName;

        @Column(name="ILL_FLAVOUR")
        private String illFlavour;

        private String interfaceType;

        private String iPAddressProvidedBy;

        @Column(name="IPV4_CUST_END_LOOPBACK")
        private String ipv4CustEndLoopback;

        @Column(name="IPV4_CUST_WAN_IP")
        private String ipv4CustWanIp;

        @Column(name="IS_IZO")
        private String isIzo;

        private String ishub;

        @Column(name="`Jitter Servicer Level Target (ms)`")
        private String jitter_Servicer_Level_Target__ms_;

        @Column(name="`LANV4 Address`")
        private String LANV4_Address;

        @Column(name="LAST_MACD_DATE")
        private Timestamp lastMacdDate;

        private String latlong;

        @Column(name="LM_COMMISSIONING_DATE")
        private String lmCommissioningDate;

        @Column(name="LOGICAL_PE_INTERFACE")
        private String logicalPeInterface;

        @Column(name="MASTER_VRF_SERVICE_ID")
        private String masterVrfServiceId;

        @Column(name="`Mean Time To Restore (MTTR) in Hrs`")
        private String mean_Time_To_Restore__MTTR__in_Hrs;

        @Column(name="MINIMUM_CONTRACT_TERM_MONTHS")
        private int minimumContractTermMonths;

        private double mrc;

        @Column(name="`Network Uptime`")
        private String network_Uptime;

        private double nrc;

        @Column(name="OPPORTUNITY_BID_CATEGORY")
        private String opportunityBidCategory;

        @Column(name="OPPORTUNITY_CLASSIFICATION")
        private String opportunityClassification;

        @Column(name="OPPORTUNITY_ID")
        private String opportunityId;

        @Column(name="ORDER_CATEGORY")
        private String orderCategory;

        @Id
        @Column(name="ORDER_ID")
        private String orderId;

        private String orderLeId;

        @Column(name="ORDER_TYPE")
        private String orderType;

        private String outerVLAN;

        @Column(name="`Packet Drop`")
        private String packet_Drop;

        @Column(name="PARENT_ID")
        private String parentId;

        @Column(name="PARENT_SERVICE")
        private String parentService;

        @Column(name="PARTNER_CUID")
        private int partnerCuid;

        @Column(name="PARTNER_NAME")
        private String partnerName;

        @Column(name="PE_MANAGEMENT_IP")
        private String peManagementIp;

        @Column(name="PE_NAME")
        private String peName;

        @Column(name="PHYSICAL_PEPORT")
        private String physicalPeport;

        @Column(name="PO_NUMBER")
        private String poNumber;

        @Column(name="PO_NUMBER_REF_ID")
        private String poNumberRefId;

        @Column(name="PORT_SPEED")
        private String portSpeed;

        private String prisec;

        @Column(name="PRODUCT_FLAVOUR")
        private String productFlavour;

        @Column(name="PROTECTION_REQUIRED")
        private String protectionRequired;

        private String region;

        @Column(name="RESILIENT_TYPE")
        private String resilientType;

        @Column(name="`Round Trip Delay (RTD)`")
        private String round_Trip_Delay__RTD_;

        @Column(name="ROUTING_PROTOCOL")
        private String routingProtocol;

        @Column(name="SCOPE_OF_MANAGEMENT")
        private String scopeOfManagement;

        @Column(name="SECS_ID")
        private String secsId;

        @Column(name="SERVICE_CLASS")
        private String serviceClass;

        @Id
        @Column(name="SERVICE_ID")
        private String serviceId;

        @Column(name="SERVICE_LINK")
        private String serviceLink;

        @Column(name="SERVICE_NAME")
        private String serviceName;

        @Column(name="SERVICE_OPTION_TYPE")
        private String serviceOptionType;

        @Column(name="SERVICE_SEGMENT")
        private String serviceSegment;

        @Column(name="SERVICE_STATUS")
        private String serviceStatus;

        @Column(name="SERVICE_TOPOLOGY")
        private String serviceTopology;

        @Column(name="SERVICE_TYPE")
        private String serviceType;

        @Column(name="SERVICEID_CREATED_DATE")
        private Timestamp serviceidCreatedDate;

        @Column(name="SERVICEID_MODIFIED_DATE")
        private Timestamp serviceidModifiedDate;

        private String serviceoption;

        @Column(name="SITE_ADDRESS")
        private String siteAddress;

        @Column(name="SLT_VARIANT")
        private String sltVariant;

        @Column(name="SM_NAME")
        private String smName;

        @Column(name="SUBORDER_TYPE")
        private String suborderType;

        @Column(name="SUPPORT_MODEL")
        private String supportModel;

        private String tataCommWANIPv4;

        @Column(name="TERMINATION_DATE")
        private Timestamp terminationDate;

        private String type;

        @Column(name="TYPE_FIXED_BURSTABLE")
        private String typeFixedBurstable;

        @Column(name="TYPE_OF_CONNECTIVITY")
        private String typeOfConnectivity;

        @Column(name="`Usage Model`")
        private String usage_Model;

        private String vlan;

        @Column(name="`WANV4 Address`")
        private String WANV4_Address;

        public VwServiceInvSatSoc() {
        }

        public String getAEndBsIp() {
                return this.aEndBsIp;
        }

        public void setAEndBsIp(String aEndBsIp) {
                this.aEndBsIp = aEndBsIp;
        }

        public String getAEndBsName() {
                return this.aEndBsName;
        }

        public void setAEndBsName(String aEndBsName) {
                this.aEndBsName = aEndBsName;
        }

        public String getAEndBsoId() {
                return this.aEndBsoId;
        }

        public void setAEndBsoId(String aEndBsoId) {
                this.aEndBsoId = aEndBsoId;
        }

        public String getAEndInterface() {
                return this.aEndInterface;
        }

        public void setAEndInterface(String aEndInterface) {
                this.aEndInterface = aEndInterface;
        }

        public String getAEndLlBandwidth() {
                return this.aEndLlBandwidth;
        }

        public void setAEndLlBandwidth(String aEndLlBandwidth) {
                this.aEndLlBandwidth = aEndLlBandwidth;
        }

        public String getAEndLlProvider() {
                return this.aEndLlProvider;
        }

        public void setAEndLlProvider(String aEndLlProvider) {
                this.aEndLlProvider = aEndLlProvider;
        }

        public String getAEndSectorId() {
                return this.aEndSectorId;
        }

        public void setAEndSectorId(String aEndSectorId) {
                this.aEndSectorId = aEndSectorId;
        }

        public String getAEndSiteAddress() {
                return this.aEndSiteAddress;
        }

        public void setAEndSiteAddress(String aEndSiteAddress) {
                this.aEndSiteAddress = aEndSiteAddress;
        }

        public String getAEndSiteCity() {
                return this.aEndSiteCity;
        }

        public void setAEndSiteCity(String aEndSiteCity) {
                this.aEndSiteCity = aEndSiteCity;
        }

        public String getAEndSiteCountry() {
                return this.aEndSiteCountry;
        }

        public void setAEndSiteCountry(String aEndSiteCountry) {
                this.aEndSiteCountry = aEndSiteCountry;
        }

        public String getAState() {
                return this.aState;
        }

        public void setAState(String aState) {
                this.aState = aState;
        }

        public String getAccManagerName() {
                return this.accManagerName;
        }

        public void setAccManagerName(String accManagerName) {
                this.accManagerName = accManagerName;
        }

        public String getAccountName() {
                return this.accountName;
        }

        public void setAccountName(String accountName) {
                this.accountName = accountName;
        }

        public String getAccountRtm() {
                return this.accountRtm;
        }

        public void setAccountRtm(String accountRtm) {
                this.accountRtm = accountRtm;
        }

        public String getAdditionalIpv4Ip() {
                return this.additionalIpv4Ip;
        }

        public void setAdditionalIpv4Ip(String additionalIpv4Ip) {
                this.additionalIpv4Ip = additionalIpv4Ip;
        }

        public String getAdtIpAddToBeProvdByVsnl() {
                return this.adtIpAddToBeProvdByVsnl;
        }

        public void setAdtIpAddToBeProvdByVsnl(String adtIpAddToBeProvdByVsnl) {
                this.adtIpAddToBeProvdByVsnl = adtIpAddToBeProvdByVsnl;
        }

        public String getAdtNoIpAddProvdByVsnl() {
                return this.adtNoIpAddProvdByVsnl;
        }

        public void setAdtNoIpAddProvdByVsnl(String adtNoIpAddProvdByVsnl) {
                this.adtNoIpAddProvdByVsnl = adtNoIpAddProvdByVsnl;
        }

        public String getAlias() {
                return this.alias;
        }

        public void setAlias(String alias) {
                this.alias = alias;
        }

        public String getAmcEndDate() {
                return this.amcEndDate;
        }

        public void setAmcEndDate(String amcEndDate) {
                this.amcEndDate = amcEndDate;
        }

        public String getAmcStartDate() {
                return this.amcStartDate;
        }

        public void setAmcStartDate(String amcStartDate) {
                this.amcStartDate = amcStartDate;
        }

        public double getArc() {
                return this.arc;
        }

        public void setArc(double arc) {
                this.arc = arc;
        }

        public String getAsdOpportunity() {
                return this.asdOpportunity;
        }

        public void setAsdOpportunity(String asdOpportunity) {
                this.asdOpportunity = asdOpportunity;
        }

        public String getAssociateId() {
                return this.associateId;
        }

        public void setAssociateId(String associateId) {
                this.associateId = associateId;
        }

        public String getAthenticationPassword() {
                return this.athenticationPassword;
        }

        public void setAthenticationPassword(String athenticationPassword) {
                this.athenticationPassword = athenticationPassword;
        }

        public String getAuthenticationMode() {
                return this.authenticationMode;
        }

        public void setAuthenticationMode(String authenticationMode) {
                this.authenticationMode = authenticationMode;
        }

        public String getBEndBsoId() {
                return this.bEndBsoId;
        }

        public void setBEndBsoId(String bEndBsoId) {
                this.bEndBsoId = bEndBsoId;
        }

        public String getBEndInterface() {
                return this.bEndInterface;
        }

        public void setBEndInterface(String bEndInterface) {
                this.bEndInterface = bEndInterface;
        }

        public String getBEndLlBandwidth() {
                return this.bEndLlBandwidth;
        }

        public void setBEndLlBandwidth(String bEndLlBandwidth) {
                this.bEndLlBandwidth = bEndLlBandwidth;
        }

        public String getBEndLlProvider() {
                return this.bEndLlProvider;
        }

        public void setBEndLlProvider(String bEndLlProvider) {
                this.bEndLlProvider = bEndLlProvider;
        }

        public String getBEndPopAddress() {
                return this.bEndPopAddress;
        }

        public void setBEndPopAddress(String bEndPopAddress) {
                this.bEndPopAddress = bEndPopAddress;
        }

        public String getBEndSiteAddress() {
                return this.bEndSiteAddress;
        }

        public void setBEndSiteAddress(String bEndSiteAddress) {
                this.bEndSiteAddress = bEndSiteAddress;
        }

        public String getBEndSiteCity() {
                return this.bEndSiteCity;
        }

        public void setBEndSiteCity(String bEndSiteCity) {
                this.bEndSiteCity = bEndSiteCity;
        }

        public String getBEndSiteCountry() {
                return this.bEndSiteCountry;
        }

        public void setBEndSiteCountry(String bEndSiteCountry) {
                this.bEndSiteCountry = bEndSiteCountry;
        }

        public String getBState() {
                return this.bState;
        }

        public void setBState(String bState) {
                this.bState = bState;
        }

        public String getBandwidth() {
                return this.bandwidth;
        }

        public void setBandwidth(String bandwidth) {
                this.bandwidth = bandwidth;
        }

        public String getBaseBandwidth() {
                return this.baseBandwidth;
        }

        public void setBaseBandwidth(String baseBandwidth) {
                this.baseBandwidth = baseBandwidth;
        }

        public String getBaseBandwidthUnit() {
                return this.baseBandwidthUnit;
        }

        public void setBaseBandwidthUnit(String baseBandwidthUnit) {
                this.baseBandwidthUnit = baseBandwidthUnit;
        }

        public String getBfdMultiplier() {
                return this.bfdMultiplier;
        }

        public void setBfdMultiplier(String bfdMultiplier) {
                this.bfdMultiplier = bfdMultiplier;
        }

        public String getBfdReceiveInterval() {
                return this.bfdReceiveInterval;
        }

        public void setBfdReceiveInterval(String bfdReceiveInterval) {
                this.bfdReceiveInterval = bfdReceiveInterval;
        }

        public String getBfdTransmitInterval() {
                return this.bfdTransmitInterval;
        }

        public void setBfdTransmitInterval(String bfdTransmitInterval) {
                this.bfdTransmitInterval = bfdTransmitInterval;
        }

        public String getBGP_AS_Number() {
                return this.BGP_AS_Number;
        }

        public void setBGP_AS_Number(String BGP_AS_Number) {
                this.BGP_AS_Number = BGP_AS_Number;
        }

        public String getBgpPeeringOn() {
                return this.bgpPeeringOn;
        }

        public void setBgpPeeringOn(String bgpPeeringOn) {
                this.bgpPeeringOn = bgpPeeringOn;
        }

        public String getBillFreePeriod() {
                return this.billFreePeriod;
        }

        public void setBillFreePeriod(String billFreePeriod) {
                this.billFreePeriod = billFreePeriod;
        }

        public String getBilling_Model() {
                return this.billing_Model;
        }

        public void setBilling_Model(String billing_Model) {
                this.billing_Model = billing_Model;
        }

        public String getBillingAccountNumber() {
                return this.billingAccountNumber;
        }

        public void setBillingAccountNumber(String billingAccountNumber) {
                this.billingAccountNumber = billingAccountNumber;
        }

        public String getBillingEntity() {
                return this.billingEntity;
        }

        public void setBillingEntity(String billingEntity) {
                this.billingEntity = billingEntity;
        }

        public String getBillingEntityCd() {
                return this.billingEntityCd;
        }

        public void setBillingEntityCd(String billingEntityCd) {
                this.billingEntityCd = billingEntityCd;
        }

        public String getBillingFrequency() {
                return this.billingFrequency;
        }

        public void setBillingFrequency(String billingFrequency) {
                this.billingFrequency = billingFrequency;
        }

        public String getBillingType() {
                return this.billingType;
        }

        public void setBillingType(String billingType) {
                this.billingType = billingType;
        }

        public String getBillStartDate() {
                return this.billStartDate;
        }

        public void setBillStartDate(String billStartDate) {
                this.billStartDate = billStartDate;
        }

        public String getBsnSwitchHandoffport() {
                return this.bsnSwitchHandoffport;
        }

        public void setBsnSwitchHandoffport(String bsnSwitchHandoffport) {
                this.bsnSwitchHandoffport = bsnSwitchHandoffport;
        }

        public String getBsnSwitchUplinkport() {
                return this.bsnSwitchUplinkport;
        }

        public void setBsnSwitchUplinkport(String bsnSwitchUplinkport) {
                this.bsnSwitchUplinkport = bsnSwitchUplinkport;
        }

        public String getBsoName() {
                return this.bsoName;
        }

        public void setBsoName(String bsoName) {
                this.bsoName = bsoName;
        }

        public String getBurstBandwidth() {
                return this.burstBandwidth;
        }

        public void setBurstBandwidth(String burstBandwidth) {
                this.burstBandwidth = burstBandwidth;
        }

        public String getBurstBandwidthUnit() {
                return this.burstBandwidthUnit;
        }

        public void setBurstBandwidthUnit(String burstBandwidthUnit) {
                this.burstBandwidthUnit = burstBandwidthUnit;
        }

        public String getBusinessSwitchHostname() {
                return this.businessSwitchHostname;
        }

        public void setBusinessSwitchHostname(String businessSwitchHostname) {
                this.businessSwitchHostname = businessSwitchHostname;
        }

        public String getBusinessSwitchIp() {
                return this.businessSwitchIp;
        }

        public void setBusinessSwitchIp(String businessSwitchIp) {
                this.businessSwitchIp = businessSwitchIp;
        }

        public String getCircuitStatus() {
                return this.circuitStatus;
        }

        public void setCircuitStatus(String circuitStatus) {
                this.circuitStatus = circuitStatus;
        }

        public String getCommissioningDate() {
                return this.commissioningDate;
        }

        public void setCommissioningDate(String commissioningDate) {
                this.commissioningDate = commissioningDate;
        }

        public String getConnectorType() {
                return this.connectorType;
        }

        public void setConnectorType(String connectorType) {
                this.connectorType = connectorType;
        }

        public int getContractTerm() {
                return this.contractTerm;
        }

        public void setContractTerm(int contractTerm) {
                this.contractTerm = contractTerm;
        }

        public String getCopfId() {
                return this.copfId;
        }

        public void setCopfId(String copfId) {
                this.copfId = copfId;
        }

        public double getCopfIdMrc() {
                return this.copfIdMrc;
        }

        public void setCopfIdMrc(double copfIdMrc) {
                this.copfIdMrc = copfIdMrc;
        }

        public String getCosModel() {
                return this.cosModel;
        }

        public void setCosModel(String cosModel) {
                this.cosModel = cosModel;
        }

        public String getCosProfile() {
                return this.cosProfile;
        }

        public void setCosProfile(String cosProfile) {
                this.cosProfile = cosProfile;
        }

        public String getCos1() {
                return this.cos1;
        }

        public void setCos1(String cos1) {
                this.cos1 = cos1;
        }

        public String getCos2() {
                return this.cos2;
        }

        public void setCos2(String cos2) {
                this.cos2 = cos2;
        }

        public String getCos3() {
                return this.cos3;
        }

        public void setCos3(String cos3) {
                this.cos3 = cos3;
        }

        public String getCos4() {
                return this.cos4;
        }

        public void setCos4(String cos4) {
                this.cos4 = cos4;
        }

        public String getCos5() {
                return this.cos5;
        }

        public void setCos5(String cos5) {
                this.cos5 = cos5;
        }

        public String getCos6() {
                return this.cos6;
        }

        public void setCos6(String cos6) {
                this.cos6 = cos6;
        }

        public String getCPE_Basic_Chassis() {
                return this.CPE_Basic_Chassis;
        }

        public void setCPE_Basic_Chassis(String CPE_Basic_Chassis) {
                this.CPE_Basic_Chassis = CPE_Basic_Chassis;
        }

        public String getCPE_Support_Type() {
                return this.CPE_Support_Type;
        }

        public void setCPE_Support_Type(String CPE_Support_Type) {
                this.CPE_Support_Type = CPE_Support_Type;
        }

        public String getCpeCommissioningDate() {
                return this.cpeCommissioningDate;
        }

        public void setCpeCommissioningDate(String cpeCommissioningDate) {
                this.cpeCommissioningDate = cpeCommissioningDate;
        }

        public String getCpeDateOfInstallation() {
                return this.cpeDateOfInstallation;
        }

        public void setCpeDateOfInstallation(String cpeDateOfInstallation) {
                this.cpeDateOfInstallation = cpeDateOfInstallation;
        }

        public String getCpeManaged() {
                return this.cpeManaged;
        }

        public void setCpeManaged(String cpeManaged) {
                this.cpeManaged = cpeManaged;
        }

        public String getCpeProvider() {
                return this.cpeProvider;
        }

        public void setCpeProvider(String cpeProvider) {
                this.cpeProvider = cpeProvider;
        }

        public String getCpeSerialNo() {
                return this.cpeSerialNo;
        }

        public void setCpeSerialNo(String cpeSerialNo) {
                this.cpeSerialNo = cpeSerialNo;
        }

        public String getCpeType() {
                return this.cpeType;
        }

        public void setCpeType(String cpeType) {
                this.cpeType = cpeType;
        }

        public String getCpeInstallationPoNumber() {
                return this.cpeInstallationPoNumber;
        }

        public void setCpeInstallationPoNumber(String cpeInstallationPoNumber) {
                this.cpeInstallationPoNumber = cpeInstallationPoNumber;
        }

        public String getCpeInstallationVendorName() {
                return this.cpeInstallationVendorName;
        }

        public void setCpeInstallationVendorName(String cpeInstallationVendorName) {
                this.cpeInstallationVendorName = cpeInstallationVendorName;
        }

        public String getCpeSupplyHardwarePoNumber() {
                return this.cpeSupplyHardwarePoNumber;
        }

        public void setCpeSupplyHardwarePoNumber(String cpeSupplyHardwarePoNumber) {
                this.cpeSupplyHardwarePoNumber = cpeSupplyHardwarePoNumber;
        }

        public String getCpeSupportPoNumber() {
                return this.cpeSupportPoNumber;
        }

        public void setCpeSupportPoNumber(String cpeSupportPoNumber) {
                this.cpeSupportPoNumber = cpeSupportPoNumber;
        }

        public String getCpeSupportVendorName() {
                return this.cpeSupportVendorName;
        }

        public void setCpeSupportVendorName(String cpeSupportVendorName) {
                this.cpeSupportVendorName = cpeSupportVendorName;
        }

        public String getCrnId() {
                return this.crnId;
        }

        public void setCrnId(String crnId) {
                this.crnId = crnId;
        }

        public String getCuid() {
                return this.cuid;
        }

        public void setCuid(String cuid) {
                this.cuid = cuid;
        }

        public String getCurrency() {
                return this.currency;
        }

        public void setCurrency(String currency) {
                this.currency = currency;
        }

        public String getCustomer_prefixes() {
                return this.customer_prefixes;
        }

        public void setCustomer_prefixes(String customer_prefixes) {
                this.customer_prefixes = customer_prefixes;
        }

        public String getCustomerCategory() {
                return this.customerCategory;
        }

        public void setCustomerCategory(String customerCategory) {
                this.customerCategory = customerCategory;
        }

        public String getCustomerContractingEntity() {
                return this.customerContractingEntity;
        }

        public void setCustomerContractingEntity(String customerContractingEntity) {
                this.customerContractingEntity = customerContractingEntity;
        }

        public String getCustomerName() {
                return this.customerName;
        }

        public void setCustomerName(String customerName) {
                this.customerName = customerName;
        }

        public String getCustomerProjectName() {
                return this.customerProjectName;
        }

        public void setCustomerProjectName(String customerProjectName) {
                this.customerProjectName = customerProjectName;
        }

        public String getCustomerSegment() {
                return this.customerSegment;
        }

        public void setCustomerSegment(String customerSegment) {
                this.customerSegment = customerSegment;
        }

        public String getCustomerServiceId() {
                return this.customerServiceId;
        }

        public void setCustomerServiceId(String customerServiceId) {
                this.customerServiceId = customerServiceId;
        }

        public String getCustomerType() {
                return this.customerType;
        }

        public void setCustomerType(String customerType) {
                this.customerType = customerType;
        }

        public String getCustomerAcceptanceDate() {
                return this.customerAcceptanceDate;
        }

        public void setCustomerAcceptanceDate(String customerAcceptanceDate) {
                this.customerAcceptanceDate = customerAcceptanceDate;
        }

        public String getDataSourceC() {
                return this.dataSourceC;
        }

        public void setDataSourceC(String dataSourceC) {
                this.dataSourceC = dataSourceC;
        }

        public String getDeemedAcceptance() {
                return this.deemedAcceptance;
        }

        public void setDeemedAcceptance(String deemedAcceptance) {
                this.deemedAcceptance = deemedAcceptance;
        }

        public String getDemarcationBuildingName() {
                return this.demarcationBuildingName;
        }

        public void setDemarcationBuildingName(String demarcationBuildingName) {
                this.demarcationBuildingName = demarcationBuildingName;
        }

        public String getDemarcationFloor() {
                return this.demarcationFloor;
        }

        public void setDemarcationFloor(String demarcationFloor) {
                this.demarcationFloor = demarcationFloor;
        }

        public String getDemarcationRoom() {
                return this.demarcationRoom;
        }

        public void setDemarcationRoom(String demarcationRoom) {
                this.demarcationRoom = demarcationRoom;
        }

        public String getDemarcationWing() {
                return this.demarcationWing;
        }

        public void setDemarcationWing(String demarcationWing) {
                this.demarcationWing = demarcationWing;
        }

        public String getDemoFlag() {
                return this.demoFlag;
        }

        public void setDemoFlag(String demoFlag) {
                this.demoFlag = demoFlag;
        }

        public String getDestinationAddressLineOne() {
                return this.destinationAddressLineOne;
        }

        public void setDestinationAddressLineOne(String destinationAddressLineOne) {
                this.destinationAddressLineOne = destinationAddressLineOne;
        }

        public String getDestinationAddressLineTwo() {
                return this.destinationAddressLineTwo;
        }

        public void setDestinationAddressLineTwo(String destinationAddressLineTwo) {
                this.destinationAddressLineTwo = destinationAddressLineTwo;
        }

        public String getDestinationPinCode() {
                return this.destinationPinCode;
        }

        public void setDestinationPinCode(String destinationPinCode) {
                this.destinationPinCode = destinationPinCode;
        }

        public String getEndCustName() {
                return this.endCustName;
        }

        public void setEndCustName(String endCustName) {
                this.endCustName = endCustName;
        }

        public String getEndCustomerName() {
                return this.endCustomerName;
        }

        public void setEndCustomerName(String endCustomerName) {
                this.endCustomerName = endCustomerName;
        }

        public String getEndMuxNodeIp() {
                return this.endMuxNodeIp;
        }

        public void setEndMuxNodeIp(String endMuxNodeIp) {
                this.endMuxNodeIp = endMuxNodeIp;
        }

        public String getEndMuxNodeName() {
                return this.endMuxNodeName;
        }

        public void setEndMuxNodeName(String endMuxNodeName) {
                this.endMuxNodeName = endMuxNodeName;
        }

        public String getEquipmentModel() {
                return this.equipmentModel;
        }

        public void setEquipmentModel(String equipmentModel) {
                this.equipmentModel = equipmentModel;
        }

        public String getEquipmentmake() {
                return this.equipmentmake;
        }

        public void setEquipmentmake(String equipmentmake) {
                this.equipmentmake = equipmentmake;
        }

        public String getExtendedLanRequired() {
                return this.extendedLanRequired;
        }

        public void setExtendedLanRequired(String extendedLanRequired) {
                this.extendedLanRequired = extendedLanRequired;
        }

        public String getFeasibilityId() {
                return this.feasibilityId;
        }

        public void setFeasibilityId(String feasibilityId) {
                this.feasibilityId = feasibilityId;
        }

        public String getFinalStatus() {
                return this.finalStatus;
        }

        public void setFinalStatus(String finalStatus) {
                this.finalStatus = finalStatus;
        }

        public String getGroupName() {
                return this.groupName;
        }

        public void setGroupName(String groupName) {
                this.groupName = groupName;
        }

        public String getHostName() {
                return this.hostName;
        }

        public void setHostName(String hostName) {
                this.hostName = hostName;
        }

        public String getIllFlavour() {
                return this.illFlavour;
        }

        public void setIllFlavour(String illFlavour) {
                this.illFlavour = illFlavour;
        }

        public String getInterfaceType() {
                return this.interfaceType;
        }

        public void setInterfaceType(String interfaceType) {
                this.interfaceType = interfaceType;
        }

        public String getIPAddressProvidedBy() {
                return this.iPAddressProvidedBy;
        }

        public void setIPAddressProvidedBy(String iPAddressProvidedBy) {
                this.iPAddressProvidedBy = iPAddressProvidedBy;
        }

        public String getIpv4CustEndLoopback() {
                return this.ipv4CustEndLoopback;
        }

        public void setIpv4CustEndLoopback(String ipv4CustEndLoopback) {
                this.ipv4CustEndLoopback = ipv4CustEndLoopback;
        }

        public String getIpv4CustWanIp() {
                return this.ipv4CustWanIp;
        }

        public void setIpv4CustWanIp(String ipv4CustWanIp) {
                this.ipv4CustWanIp = ipv4CustWanIp;
        }

        public String getIsIzo() {
                return this.isIzo;
        }

        public void setIsIzo(String isIzo) {
                this.isIzo = isIzo;
        }

        public String getIshub() {
                return this.ishub;
        }

        public void setIshub(String ishub) {
                this.ishub = ishub;
        }

        public String getJitter_Servicer_Level_Target__ms_() {
                return this.jitter_Servicer_Level_Target__ms_;
        }

        public void setJitter_Servicer_Level_Target__ms_(String jitter_Servicer_Level_Target__ms_) {
                this.jitter_Servicer_Level_Target__ms_ = jitter_Servicer_Level_Target__ms_;
        }

        public String getLANV4_Address() {
                return this.LANV4_Address;
        }

        public void setLANV4_Address(String LANV4_Address) {
                this.LANV4_Address = LANV4_Address;
        }

        public Timestamp getLastMacdDate() {
                return this.lastMacdDate;
        }

        public void setLastMacdDate(Timestamp lastMacdDate) {
                this.lastMacdDate = lastMacdDate;
        }

        public String getLatlong() {
                return this.latlong;
        }

        public void setLatlong(String latlong) {
                this.latlong = latlong;
        }

        public String getLmCommissioningDate() {
                return this.lmCommissioningDate;
        }

        public void setLmCommissioningDate(String lmCommissioningDate) {
                this.lmCommissioningDate = lmCommissioningDate;
        }

        public String getLogicalPeInterface() {
                return this.logicalPeInterface;
        }

        public void setLogicalPeInterface(String logicalPeInterface) {
                this.logicalPeInterface = logicalPeInterface;
        }

        public String getMasterVrfServiceId() {
                return this.masterVrfServiceId;
        }

        public void setMasterVrfServiceId(String masterVrfServiceId) {
                this.masterVrfServiceId = masterVrfServiceId;
        }

        public String getMean_Time_To_Restore__MTTR__in_Hrs() {
                return this.mean_Time_To_Restore__MTTR__in_Hrs;
        }

        public void setMean_Time_To_Restore__MTTR__in_Hrs(String mean_Time_To_Restore__MTTR__in_Hrs) {
                this.mean_Time_To_Restore__MTTR__in_Hrs = mean_Time_To_Restore__MTTR__in_Hrs;
        }

        public int getMinimumContractTermMonths() {
                return this.minimumContractTermMonths;
        }

        public void setMinimumContractTermMonths(int minimumContractTermMonths) {
                this.minimumContractTermMonths = minimumContractTermMonths;
        }

        public double getMrc() {
                return this.mrc;
        }

        public void setMrc(double mrc) {
                this.mrc = mrc;
        }

        public String getNetwork_Uptime() {
                return this.network_Uptime;
        }

        public void setNetwork_Uptime(String network_Uptime) {
                this.network_Uptime = network_Uptime;
        }

        public double getNrc() {
                return this.nrc;
        }

        public void setNrc(double nrc) {
                this.nrc = nrc;
        }

        public String getOpportunityBidCategory() {
                return this.opportunityBidCategory;
        }

        public void setOpportunityBidCategory(String opportunityBidCategory) {
                this.opportunityBidCategory = opportunityBidCategory;
        }

        public String getOpportunityClassification() {
                return this.opportunityClassification;
        }

        public void setOpportunityClassification(String opportunityClassification) {
                this.opportunityClassification = opportunityClassification;
        }

        public String getOpportunityId() {
                return this.opportunityId;
        }

        public void setOpportunityId(String opportunityId) {
                this.opportunityId = opportunityId;
        }

        public String getOrderCategory() {
                return this.orderCategory;
        }

        public void setOrderCategory(String orderCategory) {
                this.orderCategory = orderCategory;
        }

        public String getOrderId() {
                return this.orderId;
        }

        public void setOrderId(String orderId) {
                this.orderId = orderId;
        }

        public String getOrderType() {
                return this.orderType;
        }

        public void setOrderType(String orderType) {
                this.orderType = orderType;
        }

        public String getOuterVLAN() {
                return this.outerVLAN;
        }

        public void setOuterVLAN(String outerVLAN) {
                this.outerVLAN = outerVLAN;
        }

        public String getPacket_Drop() {
                return this.packet_Drop;
        }

        public void setPacket_Drop(String packet_Drop) {
                this.packet_Drop = packet_Drop;
        }

        public String getParentId() {
                return this.parentId;
        }

        public void setParentId(String parentId) {
                this.parentId = parentId;
        }

        public String getParentService() {
                return this.parentService;
        }

        public void setParentService(String parentService) {
                this.parentService = parentService;
        }

        public int getPartnerCuid() {
                return this.partnerCuid;
        }

        public void setPartnerCuid(int partnerCuid) {
                this.partnerCuid = partnerCuid;
        }

        public String getPartnerName() {
                return this.partnerName;
        }

        public void setPartnerName(String partnerName) {
                this.partnerName = partnerName;
        }

        public String getPeManagementIp() {
                return this.peManagementIp;
        }

        public void setPeManagementIp(String peManagementIp) {
                this.peManagementIp = peManagementIp;
        }

        public String getPeName() {
                return this.peName;
        }

        public void setPeName(String peName) {
                this.peName = peName;
        }

        public String getPhysicalPeport() {
                return this.physicalPeport;
        }

        public void setPhysicalPeport(String physicalPeport) {
                this.physicalPeport = physicalPeport;
        }

        public String getPoNumber() {
                return this.poNumber;
        }

        public void setPoNumber(String poNumber) {
                this.poNumber = poNumber;
        }

        public String getPoNumberRefId() {
                return this.poNumberRefId;
        }

        public void setPoNumberRefId(String poNumberRefId) {
                this.poNumberRefId = poNumberRefId;
        }

        public String getPortSpeed() {
                return this.portSpeed;
        }

        public void setPortSpeed(String portSpeed) {
                this.portSpeed = portSpeed;
        }

        public String getPrisec() {
                return this.prisec;
        }

        public void setPrisec(String prisec) {
                this.prisec = prisec;
        }

        public String getProductFlavour() {
                return this.productFlavour;
        }

        public void setProductFlavour(String productFlavour) {
                this.productFlavour = productFlavour;
        }

        public String getProtectionRequired() {
                return this.protectionRequired;
        }

        public void setProtectionRequired(String protectionRequired) {
                this.protectionRequired = protectionRequired;
        }

        public String getRegion() {
                return this.region;
        }

        public void setRegion(String region) {
                this.region = region;
        }

        public String getResilientType() {
                return this.resilientType;
        }

        public void setResilientType(String resilientType) {
                this.resilientType = resilientType;
        }

        public String getRound_Trip_Delay__RTD_() {
                return this.round_Trip_Delay__RTD_;
        }

        public void setRound_Trip_Delay__RTD_(String round_Trip_Delay__RTD_) {
                this.round_Trip_Delay__RTD_ = round_Trip_Delay__RTD_;
        }

        public String getRoutingProtocol() {
                return this.routingProtocol;
        }

        public void setRoutingProtocol(String routingProtocol) {
                this.routingProtocol = routingProtocol;
        }

        public String getScopeOfManagement() {
                return this.scopeOfManagement;
        }

        public void setScopeOfManagement(String scopeOfManagement) {
                this.scopeOfManagement = scopeOfManagement;
        }

        public String getSecsId() {
                return this.secsId;
        }

        public void setSecsId(String secsId) {
                this.secsId = secsId;
        }

        public String getServiceClass() {
                return this.serviceClass;
        }

        public void setServiceClass(String serviceClass) {
                this.serviceClass = serviceClass;
        }

        public String getServiceId() {
                return this.serviceId;
        }

        public void setServiceId(String serviceId) {
                this.serviceId = serviceId;
        }

        public String getServiceLink() {
                return this.serviceLink;
        }

        public void setServiceLink(String serviceLink) {
                this.serviceLink = serviceLink;
        }

        public String getServiceName() {
                return this.serviceName;
        }

        public void setServiceName(String serviceName) {
                this.serviceName = serviceName;
        }

        public String getServiceOptionType() {
                return this.serviceOptionType;
        }

        public void setServiceOptionType(String serviceOptionType) {
                this.serviceOptionType = serviceOptionType;
        }

        public String getServiceSegment() {
                return this.serviceSegment;
        }

        public void setServiceSegment(String serviceSegment) {
                this.serviceSegment = serviceSegment;
        }

        public String getServiceStatus() {
                return this.serviceStatus;
        }

        public void setServiceStatus(String serviceStatus) {
                this.serviceStatus = serviceStatus;
        }

        public String getServiceTopology() {
                return this.serviceTopology;
        }

        public void setServiceTopology(String serviceTopology) {
                this.serviceTopology = serviceTopology;
        }

        public String getServiceType() {
                return this.serviceType;
        }

        public void setServiceType(String serviceType) {
                this.serviceType = serviceType;
        }

        public Timestamp getServiceidCreatedDate() {
                return this.serviceidCreatedDate;
        }

        public void setServiceidCreatedDate(Timestamp serviceidCreatedDate) {
                this.serviceidCreatedDate = serviceidCreatedDate;
        }

        public Timestamp getServiceidModifiedDate() {
                return this.serviceidModifiedDate;
        }

        public void setServiceidModifiedDate(Timestamp serviceidModifiedDate) {
                this.serviceidModifiedDate = serviceidModifiedDate;
        }

        public String getServiceoption() {
                return this.serviceoption;
        }

        public void setServiceoption(String serviceoption) {
                this.serviceoption = serviceoption;
        }

        public String getSiteAddress() {
                return this.siteAddress;
        }

        public void setSiteAddress(String siteAddress) {
                this.siteAddress = siteAddress;
        }

        public String getSltVariant() {
                return this.sltVariant;
        }

        public void setSltVariant(String sltVariant) {
                this.sltVariant = sltVariant;
        }

        public String getSmName() {
                return this.smName;
        }

        public void setSmName(String smName) {
                this.smName = smName;
        }

        public String getSuborderType() {
                return this.suborderType;
        }

        public void setSuborderType(String suborderType) {
                this.suborderType = suborderType;
        }

        public String getSupportModel() {
                return this.supportModel;
        }

        public void setSupportModel(String supportModel) {
                this.supportModel = supportModel;
        }

        public String getTataCommWANIPv4() {
                return this.tataCommWANIPv4;
        }

        public void setTataCommWANIPv4(String tataCommWANIPv4) {
                this.tataCommWANIPv4 = tataCommWANIPv4;
        }

        public Timestamp getTerminationDate() {
                return this.terminationDate;
        }

        public void setTerminationDate(Timestamp terminationDate) {
                this.terminationDate = terminationDate;
        }

        public String getType() {
                return this.type;
        }

        public void setType(String type) {
                this.type = type;
        }

        public String getTypeFixedBurstable() {
                return this.typeFixedBurstable;
        }

        public void setTypeFixedBurstable(String typeFixedBurstable) {
                this.typeFixedBurstable = typeFixedBurstable;
        }

        public String getTypeOfConnectivity() {
                return this.typeOfConnectivity;
        }

        public void setTypeOfConnectivity(String typeOfConnectivity) {
                this.typeOfConnectivity = typeOfConnectivity;
        }

        public String getUsage_Model() {
                return this.usage_Model;
        }

        public void setUsage_Model(String usage_Model) {
                this.usage_Model = usage_Model;
        }

        public String getVlan() {
                return this.vlan;
        }

        public void setVlan(String vlan) {
                this.vlan = vlan;
        }

        public String getWANV4_Address() {
                return this.WANV4_Address;
        }

        public void setWANV4_Address(String WANV4_Address) {
                this.WANV4_Address = WANV4_Address;
        }

        public String getaEndBsIp() {
                return aEndBsIp;
        }

        public void setaEndBsIp(String aEndBsIp) {
                this.aEndBsIp = aEndBsIp;
        }

        public String getaEndBsName() {
                return aEndBsName;
        }

        public void setaEndBsName(String aEndBsName) {
                this.aEndBsName = aEndBsName;
        }

        public String getaEndBsoId() {
                return aEndBsoId;
        }

        public void setaEndBsoId(String aEndBsoId) {
                this.aEndBsoId = aEndBsoId;
        }

        public String getaEndInterface() {
                return aEndInterface;
        }

        public void setaEndInterface(String aEndInterface) {
                this.aEndInterface = aEndInterface;
        }

        public String getaEndLlBandwidth() {
                return aEndLlBandwidth;
        }

        public void setaEndLlBandwidth(String aEndLlBandwidth) {
                this.aEndLlBandwidth = aEndLlBandwidth;
        }

        public String getaEndLlProvider() {
                return aEndLlProvider;
        }

        public void setaEndLlProvider(String aEndLlProvider) {
                this.aEndLlProvider = aEndLlProvider;
        }

        public String getaEndSectorId() {
                return aEndSectorId;
        }

        public void setaEndSectorId(String aEndSectorId) {
                this.aEndSectorId = aEndSectorId;
        }

        public String getaEndSiteAddress() {
                return aEndSiteAddress;
        }

        public void setaEndSiteAddress(String aEndSiteAddress) {
                this.aEndSiteAddress = aEndSiteAddress;
        }

        public String getaEndSiteCity() {
                return aEndSiteCity;
        }

        public void setaEndSiteCity(String aEndSiteCity) {
                this.aEndSiteCity = aEndSiteCity;
        }

        public String getaEndSiteCountry() {
                return aEndSiteCountry;
        }

        public void setaEndSiteCountry(String aEndSiteCountry) {
                this.aEndSiteCountry = aEndSiteCountry;
        }

        public String getaState() {
                return aState;
        }

        public void setaState(String aState) {
                this.aState = aState;
        }

        public String getbEndBsoId() {
                return bEndBsoId;
        }

        public void setbEndBsoId(String bEndBsoId) {
                this.bEndBsoId = bEndBsoId;
        }

        public String getbEndInterface() {
                return bEndInterface;
        }

        public void setbEndInterface(String bEndInterface) {
                this.bEndInterface = bEndInterface;
        }

        public String getbEndLlBandwidth() {
                return bEndLlBandwidth;
        }

        public void setbEndLlBandwidth(String bEndLlBandwidth) {
                this.bEndLlBandwidth = bEndLlBandwidth;
        }

        public String getbEndLlProvider() {
                return bEndLlProvider;
        }

        public void setbEndLlProvider(String bEndLlProvider) {
                this.bEndLlProvider = bEndLlProvider;
        }

        public String getbEndPopAddress() {
                return bEndPopAddress;
        }

        public void setbEndPopAddress(String bEndPopAddress) {
                this.bEndPopAddress = bEndPopAddress;
        }

        public String getbEndSiteAddress() {
                return bEndSiteAddress;
        }

        public void setbEndSiteAddress(String bEndSiteAddress) {
                this.bEndSiteAddress = bEndSiteAddress;
        }

        public String getbEndSiteCity() {
                return bEndSiteCity;
        }

        public void setbEndSiteCity(String bEndSiteCity) {
                this.bEndSiteCity = bEndSiteCity;
        }

        public String getbEndSiteCountry() {
                return bEndSiteCountry;
        }

        public void setbEndSiteCountry(String bEndSiteCountry) {
                this.bEndSiteCountry = bEndSiteCountry;
        }

        public String getbState() {
                return bState;
        }

        public void setbState(String bState) {
                this.bState = bState;
        }

        public String getCustomerLeId() {
                return customerLeId;
        }

        public void setCustomerLeId(String customerLeId) {
                this.customerLeId = customerLeId;
        }

        public String getOrderLeId() {
                return orderLeId;
        }

        public void setOrderLeId(String orderLeId) {
                this.orderLeId = orderLeId;
        }

        public String getiPAddressProvidedBy() {
                return iPAddressProvidedBy;
        }

        public void setiPAddressProvidedBy(String iPAddressProvidedBy) {
                this.iPAddressProvidedBy = iPAddressProvidedBy;
        }
}
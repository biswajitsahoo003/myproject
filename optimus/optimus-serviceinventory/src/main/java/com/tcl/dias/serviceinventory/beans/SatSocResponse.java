package com.tcl.dias.serviceinventory.beans;

import java.sql.Timestamp;

import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 
 * @author MRajakum
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SatSocResponse {

	@JsonProperty("A_END_BS_IP")
	private String aEndBsIp;

	@JsonProperty("A_END_BS_NAME")
	private String aEndBsName;

	@JsonProperty("A_END_BSO_ID")
	private String aEndBsoId;

	@JsonProperty("A_END_INTERFACE")
	private String aEndInterface;

	@JsonProperty("A_END_LL_BANDWIDTH")
	private String aEndLlBandwidth;

	@JsonProperty("A_END_LL_PROVIDER")
	private String aEndLlProvider;

	@JsonProperty("A_END_SECTOR_ID")
	private String aEndSectorId;

	@JsonProperty("A_END_SITE_ADDRESS")
	private String aEndSiteAddress;

	@JsonProperty("A_END_SITE_CITY")
	private String aEndSiteCity;

	@JsonProperty("A_END_SITE_COUNTRY")
	private String aEndSiteCountry;

	@JsonProperty("A_STATE")
	private String aState;

	@JsonProperty("ACC_MANAGER_NAME")
	private String accManagerName;

	@JsonProperty("ACCOUNT_NAME")
	private String accountName;

	@JsonProperty("ACCOUNT_RTM")
	private String accountRtm;

	@JsonProperty("ADDITIONAL_IPV4_IP")
	private String additionalIpv4Ip;

	@JsonProperty("ADT_IP_ADD_TO_BE_PROVD_BY_VSNL")
	private String adtIpAddToBeProvdByVsnl;

	@JsonProperty("ADT_NO_IP_ADD_PROVD_BY_VSNL")
	private String adtNoIpAddProvdByVsnl;

	private String alias;

	@JsonProperty("AMC_END_DATE")
	private String amcEndDate;

	@JsonProperty("AMC_START_DATE")
	private String amcStartDate;

	private Double arc;

	@JsonProperty("ASD_OPPORTUNITY")
	private String asdOpportunity;

	@JsonProperty("ASSOCIATE_ID")
	private String associateId;

	private String athenticationPassword;

	private String authenticationMode;

	@JsonProperty("B_END_BSO_ID")
	private String bEndBsoId;

	@JsonProperty("B_END_INTERFACE")
	private String bEndInterface;

	@JsonProperty("B_END_LL_BANDWIDTH")
	private String bEndLlBandwidth;

	@JsonProperty("B_END_LL_PROVIDER")
	private String bEndLlProvider;

	@JsonProperty("B_END_POP_ADDRESS")
	private String bEndPopAddress;

	@JsonProperty("B_END_SITE_ADDRESS")
	private String bEndSiteAddress;

	@JsonProperty("B_END_SITE_CITY")
	private String bEndSiteCity;

	@JsonProperty("B_END_SITE_COUNTRY")
	private String bEndSiteCountry;

	@JsonProperty("B_STATE")
	private String bState;

	private String bandwidth;

	@JsonProperty("BASE_BANDWIDTH")
	private String baseBandwidth;

	@JsonProperty("BASE_BANDWIDTH_UNIT")
	private String baseBandwidthUnit;

	private String bfdMultiplier;

	private String bfdReceiveInterval;

	private String bfdTransmitInterval;

	@JsonProperty("BGP AS Number")
	private String BGP_AS_Number;

	private String bgpPeeringOn;

	private String billFreePeriod;

	@JsonProperty("Billing Model")
	private String billing_Model;

	@JsonProperty("BILLING_ACCOUNT_NUMBER")
	private String billingAccountNumber;

	@JsonProperty("BILLING_ENTITY")
	private String billingEntity;

	@JsonProperty("BILLING_ENTITY_CD")
	private String billingEntityCd;

	@JsonProperty("BILLING_FREQUENCY")
	private String billingFrequency;

	@JsonProperty("BILLING_TYPE")
	private String billingType;

	private String billStartDate;

	@JsonProperty("BSN_SWITCH_HANDOFFPORT")
	private String bsnSwitchHandoffport;

	@JsonProperty("BSN_SWITCH_UPLINKPORT")
	private String bsnSwitchUplinkport;

	@JsonProperty("BSO_NAME")
	private String bsoName;

	@JsonProperty("BURST_BANDWIDTH")
	private String burstBandwidth;

	@JsonProperty("BURST_BANDWIDTH_UNIT")
	private String burstBandwidthUnit;

	@JsonProperty("BUSINESS_SWITCH_HOSTNAME")
	private String businessSwitchHostname;

	@JsonProperty("BUSINESS_SWITCH_IP")
	private String businessSwitchIp;

	@JsonProperty("CIRCUIT_STATUS")
	private String circuitStatus;

	@JsonProperty("COMMISSIONING_DATE")
	private String commissioningDate;

	private String connectorType;

	@JsonProperty("CONTRACT_TERM")
	private int contractTerm;

	@JsonProperty("COPF_ID")
	private String copfId;

	@JsonProperty("COPF_ID_MRC")
	private double copfIdMrc;

	@JsonProperty("COS_MODEL")
	private String cosModel;

	@JsonProperty("COS_PROFILE")
	private String cosProfile;

	private String cos1;

	private String cos2;

	private String cos3;

	private String cos4;

	private String cos5;

	private String cos6;

	@JsonProperty("CPE Basic Chassis")
	private String CPE_Basic_Chassis;

	@JsonProperty("CPE Support Type")
	private String CPE_Support_Type;

	@JsonProperty("CPE_COMMISSIONING_DATE")
	private String cpeCommissioningDate;

	@JsonProperty("CPE_DATE_OF_INSTALLATION")
	private String cpeDateOfInstallation;

	@JsonProperty("CPE_MANAGED")
	private String cpeManaged;

	@JsonProperty("CPE_PROVIDER")
	private String cpeProvider;

	@JsonProperty("CPE_SERIAL_NO")
	private String cpeSerialNo;

	@JsonProperty("CPE_TYPE")
	private String cpeType;

	private String cpeInstallationPoNumber;

	private String cpeInstallationVendorName;

	private String cpeSupplyHardwarePoNumber;

	private String cpeSupportPoNumber;

	private String cpeSupportVendorName;

	@JsonProperty("CRN_ID")
	private String crnId;

	private String cuid;

	private String currency;

	private String customerLeId;

	@JsonProperty("Customer prefixes")
	private String customer_prefixes;

	@JsonProperty("CUSTOMER_CATEGORY")
	private String customerCategory;

	@JsonProperty("CUSTOMER_CONTRACTING_ENTITY")
	private String customerContractingEntity;

	@JsonProperty("CUSTOMER_NAME")
	private String customerName;

	@JsonProperty("CUSTOMER_PROJECT_NAME")
	private String customerProjectName;

	@JsonProperty("CUSTOMER_SEGMENT")
	private String customerSegment;

	@JsonProperty("CUSTOMER_SERVICE_ID")
	private String customerServiceId;

	@JsonProperty("CUSTOMER_TYPE")
	private String customerType;

	private String customerAcceptanceDate;

	@JsonProperty("DATA_SOURCE__C")
	private String dataSourceC;

	private String deemedAcceptance;

	private String demarcationBuildingName;

	private String demarcationFloor;

	private String demarcationRoom;

	private String demarcationWing;

	@JsonProperty("DEMO_FLAG")
	private String demoFlag;

	private String destinationAddressLineOne;

	private String destinationAddressLineTwo;

	private String destinationPinCode;

	@JsonProperty("END_CUST_NAME")
	private String endCustName;

	@JsonProperty("END_CUSTOMER_NAME")
	private String endCustomerName;

	private String endMuxNodeIp;

	private String endMuxNodeName;

	@JsonProperty("EQUIPMENT_MODEL")
	private String equipmentModel;

	private String equipmentmake;

	private String extendedLanRequired;

	@JsonProperty("FEASIBILITY_ID")
	private String feasibilityId;

	@JsonProperty("FINAL_STATUS")
	private String finalStatus;

	@JsonProperty("GROUP_NAME")
	private String groupName;

	@JsonProperty("HOST_NAME")
	private String hostName;

	@JsonProperty("ILL_FLAVOUR")
	private String illFlavour;

	private String interfaceType;

	private String iPAddressProvidedBy;

	@JsonProperty("IPV4_CUST_END_LOOPBACK")
	private String ipv4CustEndLoopback;

	@JsonProperty("IPV4_CUST_WAN_IP")
	private String ipv4CustWanIp;

	@JsonProperty("IS_IZO")
	private String isIzo;

	private String ishub;

	@JsonProperty("Jitter Servicer Level Target (ms)")
	private String jitter_Servicer_Level_Target__ms_;

	@JsonProperty("LANV4 Address")
	private String LANV4_Address;

	@JsonProperty("LAST_MACD_DATE")
	private Timestamp lastMacdDate;

	private String latlong;

	@JsonProperty("LM_COMMISSIONING_DATE")
	private String lmCommissioningDate;

	@JsonProperty("LOGICAL_PE_INTERFACE")
	private String logicalPeInterface;

	@JsonProperty("MASTER_VRF_SERVICE_ID")
	private String masterVrfServiceId;

	@JsonProperty("Mean Time To Restore (MTTR) in Hrs")
	private String mean_Time_To_Restore__MTTR__in_Hrs;

	@JsonProperty("MINIMUM_CONTRACT_TERM_MONTHS")
	private int minimumContractTermMonths;

	private double mrc;

	@JsonProperty("Network Uptime")
	private String network_Uptime;

	private double nrc;

	@JsonProperty("OPPORTUNITY_BID_CATEGORY")
	private String opportunityBidCategory;

	@JsonProperty("OPPORTUNITY_CLASSIFICATION")
	private String opportunityClassification;

	@JsonProperty("OPPORTUNITY_ID")
	private String opportunityId;

	@JsonProperty("ORDER_CATEGORY")
	private String orderCategory;

	@Id
	@JsonProperty("ORDER_ID")
	private String orderId;

	private String orderLeId;

	@JsonProperty("ORDER_TYPE")
	private String orderType;

	private String outerVLAN;

	@JsonProperty("Packet Drop")
	private String packet_Drop;

	@JsonProperty("PARENT_ID")
	private String parentId;

	@JsonProperty("PARENT_SERVICE")
	private String parentService;

	@JsonProperty("PARTNER_CUID")
	private int partnerCuid;

	@JsonProperty("PARTNER_NAME")
	private String partnerName;

	@JsonProperty("PE_MANAGEMENT_IP")
	private String peManagementIp;

	@JsonProperty("PE_NAME")
	private String peName;

	@JsonProperty("PHYSICAL_PEPORT")
	private String physicalPeport;

	@JsonProperty("PO_NUMBER")
	private String poNumber;

	@JsonProperty("PO_NUMBER_REF_ID")
	private String poNumberRefId;

	@JsonProperty("PORT_SPEED")
	private String portSpeed;

	private String prisec;

	@JsonProperty("PRODUCT_FLAVOUR")
	private String productFlavour;

	@JsonProperty("PROTECTION_REQUIRED")
	private String protectionRequired;

	private String region;

	@JsonProperty("RESILIENT_TYPE")
	private String resilientType;

	@JsonProperty("Round Trip Delay (RTD)")
	private String round_Trip_Delay__RTD_;

	@JsonProperty("ROUTING_PROTOCOL")
	private String routingProtocol;

	@JsonProperty("SCOPE_OF_MANAGEMENT")
	private String scopeOfManagement;

	@JsonProperty("SECS_ID")
	private String secsId;

	@JsonProperty("SERVICE_CLASS")
	private String serviceClass;

	@Id
	@JsonProperty("SERVICE_ID")
	private String serviceId;

	@JsonProperty("SERVICE_LINK")
	private String serviceLink;

	@JsonProperty("SERVICE_NAME")
	private String serviceName;

	@JsonProperty("SERVICE_OPTION_TYPE")
	private String serviceOptionType;

	@JsonProperty("SERVICE_SEGMENT")
	private String serviceSegment;

	@JsonProperty("SERVICE_STATUS")
	private String serviceStatus;

	@JsonProperty("SERVICE_TOPOLOGY")
	private String serviceTopology;

	@JsonProperty("SERVICE_TYPE")
	private String serviceType;

	@JsonProperty("SERVICEID_CREATED_DATE")
	private Timestamp serviceidCreatedDate;

	@JsonProperty("SERVICEID_MODIFIED_DATE")
	private Timestamp serviceidModifiedDate;

	private String serviceoption;

	@JsonProperty("SITE_ADDRESS")
	private String siteAddress;

	@JsonProperty("SLT_VARIANT")
	private String sltVariant;

	@JsonProperty("SM_NAME")
	private String smName;

	@JsonProperty("SUBORDER_TYPE")
	private String suborderType;

	@JsonProperty("SUPPORT_MODEL")
	private String supportModel;

	private String tataCommWANIPv4;

	@JsonProperty("TERMINATION_DATE")
	private Timestamp terminationDate;

	private String type;

	@JsonProperty("TYPE_FIXED_BURSTABLE")
	private String typeFixedBurstable;

	@JsonProperty("TYPE_OF_CONNECTIVITY")
	private String typeOfConnectivity;

	@JsonProperty("Usage Model")
	private String usage_Model;

	private String vlan;

	@JsonProperty("WANV4 Address")
	private String WANV4_Address;
	
	@JsonProperty("ERF_ORDER_ID")
	private String erfOrderId;

	public SatSocResponse() {
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

	public String getAccManagerName() {
		return accManagerName;
	}

	public void setAccManagerName(String accManagerName) {
		this.accManagerName = accManagerName;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getAccountRtm() {
		return accountRtm;
	}

	public void setAccountRtm(String accountRtm) {
		this.accountRtm = accountRtm;
	}

	public String getAdditionalIpv4Ip() {
		return additionalIpv4Ip;
	}

	public void setAdditionalIpv4Ip(String additionalIpv4Ip) {
		this.additionalIpv4Ip = additionalIpv4Ip;
	}

	public String getAdtIpAddToBeProvdByVsnl() {
		return adtIpAddToBeProvdByVsnl;
	}

	public void setAdtIpAddToBeProvdByVsnl(String adtIpAddToBeProvdByVsnl) {
		this.adtIpAddToBeProvdByVsnl = adtIpAddToBeProvdByVsnl;
	}

	public String getAdtNoIpAddProvdByVsnl() {
		return adtNoIpAddProvdByVsnl;
	}

	public void setAdtNoIpAddProvdByVsnl(String adtNoIpAddProvdByVsnl) {
		this.adtNoIpAddProvdByVsnl = adtNoIpAddProvdByVsnl;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getAmcEndDate() {
		return amcEndDate;
	}

	public void setAmcEndDate(String amcEndDate) {
		this.amcEndDate = amcEndDate;
	}

	public String getAmcStartDate() {
		return amcStartDate;
	}

	public void setAmcStartDate(String amcStartDate) {
		this.amcStartDate = amcStartDate;
	}

	public Double getArc() {
		return arc;
	}

	public void setArc(Double arc) {
		this.arc = arc;
	}

	public String getAsdOpportunity() {
		return asdOpportunity;
	}

	public void setAsdOpportunity(String asdOpportunity) {
		this.asdOpportunity = asdOpportunity;
	}

	public String getAssociateId() {
		return associateId;
	}

	public void setAssociateId(String associateId) {
		this.associateId = associateId;
	}

	public String getAthenticationPassword() {
		return athenticationPassword;
	}

	public void setAthenticationPassword(String athenticationPassword) {
		this.athenticationPassword = athenticationPassword;
	}

	public String getAuthenticationMode() {
		return authenticationMode;
	}

	public void setAuthenticationMode(String authenticationMode) {
		this.authenticationMode = authenticationMode;
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

	public String getBandwidth() {
		return bandwidth;
	}

	public void setBandwidth(String bandwidth) {
		this.bandwidth = bandwidth;
	}

	public String getBaseBandwidth() {
		return baseBandwidth;
	}

	public void setBaseBandwidth(String baseBandwidth) {
		this.baseBandwidth = baseBandwidth;
	}

	public String getBaseBandwidthUnit() {
		return baseBandwidthUnit;
	}

	public void setBaseBandwidthUnit(String baseBandwidthUnit) {
		this.baseBandwidthUnit = baseBandwidthUnit;
	}

	public String getBfdMultiplier() {
		return bfdMultiplier;
	}

	public void setBfdMultiplier(String bfdMultiplier) {
		this.bfdMultiplier = bfdMultiplier;
	}

	public String getBfdReceiveInterval() {
		return bfdReceiveInterval;
	}

	public void setBfdReceiveInterval(String bfdReceiveInterval) {
		this.bfdReceiveInterval = bfdReceiveInterval;
	}

	public String getBfdTransmitInterval() {
		return bfdTransmitInterval;
	}

	public void setBfdTransmitInterval(String bfdTransmitInterval) {
		this.bfdTransmitInterval = bfdTransmitInterval;
	}

	public String getBGP_AS_Number() {
		return BGP_AS_Number;
	}

	public void setBGP_AS_Number(String bGP_AS_Number) {
		BGP_AS_Number = bGP_AS_Number;
	}

	public String getBgpPeeringOn() {
		return bgpPeeringOn;
	}

	public void setBgpPeeringOn(String bgpPeeringOn) {
		this.bgpPeeringOn = bgpPeeringOn;
	}

	public String getBillFreePeriod() {
		return billFreePeriod;
	}

	public void setBillFreePeriod(String billFreePeriod) {
		this.billFreePeriod = billFreePeriod;
	}

	public String getBilling_Model() {
		return billing_Model;
	}

	public void setBilling_Model(String billing_Model) {
		this.billing_Model = billing_Model;
	}

	public String getBillingAccountNumber() {
		return billingAccountNumber;
	}

	public void setBillingAccountNumber(String billingAccountNumber) {
		this.billingAccountNumber = billingAccountNumber;
	}

	public String getBillingEntity() {
		return billingEntity;
	}

	public void setBillingEntity(String billingEntity) {
		this.billingEntity = billingEntity;
	}

	public String getBillingEntityCd() {
		return billingEntityCd;
	}

	public void setBillingEntityCd(String billingEntityCd) {
		this.billingEntityCd = billingEntityCd;
	}

	public String getBillingFrequency() {
		return billingFrequency;
	}

	public void setBillingFrequency(String billingFrequency) {
		this.billingFrequency = billingFrequency;
	}

	public String getBillingType() {
		return billingType;
	}

	public void setBillingType(String billingType) {
		this.billingType = billingType;
	}

	public String getBillStartDate() {
		return billStartDate;
	}

	public void setBillStartDate(String billStartDate) {
		this.billStartDate = billStartDate;
	}

	public String getBsnSwitchHandoffport() {
		return bsnSwitchHandoffport;
	}

	public void setBsnSwitchHandoffport(String bsnSwitchHandoffport) {
		this.bsnSwitchHandoffport = bsnSwitchHandoffport;
	}

	public String getBsnSwitchUplinkport() {
		return bsnSwitchUplinkport;
	}

	public void setBsnSwitchUplinkport(String bsnSwitchUplinkport) {
		this.bsnSwitchUplinkport = bsnSwitchUplinkport;
	}

	public String getBsoName() {
		return bsoName;
	}

	public void setBsoName(String bsoName) {
		this.bsoName = bsoName;
	}

	public String getBurstBandwidth() {
		return burstBandwidth;
	}

	public void setBurstBandwidth(String burstBandwidth) {
		this.burstBandwidth = burstBandwidth;
	}

	public String getBurstBandwidthUnit() {
		return burstBandwidthUnit;
	}

	public void setBurstBandwidthUnit(String burstBandwidthUnit) {
		this.burstBandwidthUnit = burstBandwidthUnit;
	}

	public String getBusinessSwitchHostname() {
		return businessSwitchHostname;
	}

	public void setBusinessSwitchHostname(String businessSwitchHostname) {
		this.businessSwitchHostname = businessSwitchHostname;
	}

	public String getBusinessSwitchIp() {
		return businessSwitchIp;
	}

	public void setBusinessSwitchIp(String businessSwitchIp) {
		this.businessSwitchIp = businessSwitchIp;
	}

	public String getCircuitStatus() {
		return circuitStatus;
	}

	public void setCircuitStatus(String circuitStatus) {
		this.circuitStatus = circuitStatus;
	}

	public String getCommissioningDate() {
		return commissioningDate;
	}

	public void setCommissioningDate(String commissioningDate) {
		this.commissioningDate = commissioningDate;
	}

	public String getConnectorType() {
		return connectorType;
	}

	public void setConnectorType(String connectorType) {
		this.connectorType = connectorType;
	}

	public int getContractTerm() {
		return contractTerm;
	}

	public void setContractTerm(int contractTerm) {
		this.contractTerm = contractTerm;
	}

	public String getCopfId() {
		return copfId;
	}

	public void setCopfId(String copfId) {
		this.copfId = copfId;
	}

	public double getCopfIdMrc() {
		return copfIdMrc;
	}

	public void setCopfIdMrc(double copfIdMrc) {
		this.copfIdMrc = copfIdMrc;
	}

	public String getCosModel() {
		return cosModel;
	}

	public void setCosModel(String cosModel) {
		this.cosModel = cosModel;
	}

	public String getCosProfile() {
		return cosProfile;
	}

	public void setCosProfile(String cosProfile) {
		this.cosProfile = cosProfile;
	}

	public String getCos1() {
		return cos1;
	}

	public void setCos1(String cos1) {
		this.cos1 = cos1;
	}

	public String getCos2() {
		return cos2;
	}

	public void setCos2(String cos2) {
		this.cos2 = cos2;
	}

	public String getCos3() {
		return cos3;
	}

	public void setCos3(String cos3) {
		this.cos3 = cos3;
	}

	public String getCos4() {
		return cos4;
	}

	public void setCos4(String cos4) {
		this.cos4 = cos4;
	}

	public String getCos5() {
		return cos5;
	}

	public void setCos5(String cos5) {
		this.cos5 = cos5;
	}

	public String getCos6() {
		return cos6;
	}

	public void setCos6(String cos6) {
		this.cos6 = cos6;
	}

	public String getCPE_Basic_Chassis() {
		return CPE_Basic_Chassis;
	}

	public void setCPE_Basic_Chassis(String cPE_Basic_Chassis) {
		CPE_Basic_Chassis = cPE_Basic_Chassis;
	}

	public String getCPE_Support_Type() {
		return CPE_Support_Type;
	}

	public void setCPE_Support_Type(String cPE_Support_Type) {
		CPE_Support_Type = cPE_Support_Type;
	}

	public String getCpeCommissioningDate() {
		return cpeCommissioningDate;
	}

	public void setCpeCommissioningDate(String cpeCommissioningDate) {
		this.cpeCommissioningDate = cpeCommissioningDate;
	}

	public String getCpeDateOfInstallation() {
		return cpeDateOfInstallation;
	}

	public void setCpeDateOfInstallation(String cpeDateOfInstallation) {
		this.cpeDateOfInstallation = cpeDateOfInstallation;
	}

	public String getCpeManaged() {
		return cpeManaged;
	}

	public void setCpeManaged(String cpeManaged) {
		this.cpeManaged = cpeManaged;
	}

	public String getCpeProvider() {
		return cpeProvider;
	}

	public void setCpeProvider(String cpeProvider) {
		this.cpeProvider = cpeProvider;
	}

	public String getCpeSerialNo() {
		return cpeSerialNo;
	}

	public void setCpeSerialNo(String cpeSerialNo) {
		this.cpeSerialNo = cpeSerialNo;
	}

	public String getCpeType() {
		return cpeType;
	}

	public void setCpeType(String cpeType) {
		this.cpeType = cpeType;
	}

	public String getCpeInstallationPoNumber() {
		return cpeInstallationPoNumber;
	}

	public void setCpeInstallationPoNumber(String cpeInstallationPoNumber) {
		this.cpeInstallationPoNumber = cpeInstallationPoNumber;
	}

	public String getCpeInstallationVendorName() {
		return cpeInstallationVendorName;
	}

	public void setCpeInstallationVendorName(String cpeInstallationVendorName) {
		this.cpeInstallationVendorName = cpeInstallationVendorName;
	}

	public String getCpeSupplyHardwarePoNumber() {
		return cpeSupplyHardwarePoNumber;
	}

	public void setCpeSupplyHardwarePoNumber(String cpeSupplyHardwarePoNumber) {
		this.cpeSupplyHardwarePoNumber = cpeSupplyHardwarePoNumber;
	}

	public String getCpeSupportPoNumber() {
		return cpeSupportPoNumber;
	}

	public void setCpeSupportPoNumber(String cpeSupportPoNumber) {
		this.cpeSupportPoNumber = cpeSupportPoNumber;
	}

	public String getCpeSupportVendorName() {
		return cpeSupportVendorName;
	}

	public void setCpeSupportVendorName(String cpeSupportVendorName) {
		this.cpeSupportVendorName = cpeSupportVendorName;
	}

	public String getCrnId() {
		return crnId;
	}

	public void setCrnId(String crnId) {
		this.crnId = crnId;
	}

	public String getCuid() {
		return cuid;
	}

	public void setCuid(String cuid) {
		this.cuid = cuid;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getCustomerLeId() {
		return customerLeId;
	}

	public void setCustomerLeId(String customerLeId) {
		this.customerLeId = customerLeId;
	}

	public String getCustomer_prefixes() {
		return customer_prefixes;
	}

	public void setCustomer_prefixes(String customer_prefixes) {
		this.customer_prefixes = customer_prefixes;
	}

	public String getCustomerCategory() {
		return customerCategory;
	}

	public void setCustomerCategory(String customerCategory) {
		this.customerCategory = customerCategory;
	}

	public String getCustomerContractingEntity() {
		return customerContractingEntity;
	}

	public void setCustomerContractingEntity(String customerContractingEntity) {
		this.customerContractingEntity = customerContractingEntity;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCustomerProjectName() {
		return customerProjectName;
	}

	public void setCustomerProjectName(String customerProjectName) {
		this.customerProjectName = customerProjectName;
	}

	public String getCustomerSegment() {
		return customerSegment;
	}

	public void setCustomerSegment(String customerSegment) {
		this.customerSegment = customerSegment;
	}

	public String getCustomerServiceId() {
		return customerServiceId;
	}

	public void setCustomerServiceId(String customerServiceId) {
		this.customerServiceId = customerServiceId;
	}

	public String getCustomerType() {
		return customerType;
	}

	public void setCustomerType(String customerType) {
		this.customerType = customerType;
	}

	public String getCustomerAcceptanceDate() {
		return customerAcceptanceDate;
	}

	public void setCustomerAcceptanceDate(String customerAcceptanceDate) {
		this.customerAcceptanceDate = customerAcceptanceDate;
	}

	public String getDataSourceC() {
		return dataSourceC;
	}

	public void setDataSourceC(String dataSourceC) {
		this.dataSourceC = dataSourceC;
	}

	public String getDeemedAcceptance() {
		return deemedAcceptance;
	}

	public void setDeemedAcceptance(String deemedAcceptance) {
		this.deemedAcceptance = deemedAcceptance;
	}

	public String getDemarcationBuildingName() {
		return demarcationBuildingName;
	}

	public void setDemarcationBuildingName(String demarcationBuildingName) {
		this.demarcationBuildingName = demarcationBuildingName;
	}

	public String getDemarcationFloor() {
		return demarcationFloor;
	}

	public void setDemarcationFloor(String demarcationFloor) {
		this.demarcationFloor = demarcationFloor;
	}

	public String getDemarcationRoom() {
		return demarcationRoom;
	}

	public void setDemarcationRoom(String demarcationRoom) {
		this.demarcationRoom = demarcationRoom;
	}

	public String getDemarcationWing() {
		return demarcationWing;
	}

	public void setDemarcationWing(String demarcationWing) {
		this.demarcationWing = demarcationWing;
	}

	public String getDemoFlag() {
		return demoFlag;
	}

	public void setDemoFlag(String demoFlag) {
		this.demoFlag = demoFlag;
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

	public String getDestinationPinCode() {
		return destinationPinCode;
	}

	public void setDestinationPinCode(String destinationPinCode) {
		this.destinationPinCode = destinationPinCode;
	}

	public String getEndCustName() {
		return endCustName;
	}

	public void setEndCustName(String endCustName) {
		this.endCustName = endCustName;
	}

	public String getEndCustomerName() {
		return endCustomerName;
	}

	public void setEndCustomerName(String endCustomerName) {
		this.endCustomerName = endCustomerName;
	}

	public String getEndMuxNodeIp() {
		return endMuxNodeIp;
	}

	public void setEndMuxNodeIp(String endMuxNodeIp) {
		this.endMuxNodeIp = endMuxNodeIp;
	}

	public String getEndMuxNodeName() {
		return endMuxNodeName;
	}

	public void setEndMuxNodeName(String endMuxNodeName) {
		this.endMuxNodeName = endMuxNodeName;
	}

	public String getEquipmentModel() {
		return equipmentModel;
	}

	public void setEquipmentModel(String equipmentModel) {
		this.equipmentModel = equipmentModel;
	}

	public String getEquipmentmake() {
		return equipmentmake;
	}

	public void setEquipmentmake(String equipmentmake) {
		this.equipmentmake = equipmentmake;
	}

	public String getExtendedLanRequired() {
		return extendedLanRequired;
	}

	public void setExtendedLanRequired(String extendedLanRequired) {
		this.extendedLanRequired = extendedLanRequired;
	}

	public String getFeasibilityId() {
		return feasibilityId;
	}

	public void setFeasibilityId(String feasibilityId) {
		this.feasibilityId = feasibilityId;
	}

	public String getFinalStatus() {
		return finalStatus;
	}

	public void setFinalStatus(String finalStatus) {
		this.finalStatus = finalStatus;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public String getIllFlavour() {
		return illFlavour;
	}

	public void setIllFlavour(String illFlavour) {
		this.illFlavour = illFlavour;
	}

	public String getInterfaceType() {
		return interfaceType;
	}

	public void setInterfaceType(String interfaceType) {
		this.interfaceType = interfaceType;
	}

	public String getiPAddressProvidedBy() {
		return iPAddressProvidedBy;
	}

	public void setiPAddressProvidedBy(String iPAddressProvidedBy) {
		this.iPAddressProvidedBy = iPAddressProvidedBy;
	}

	public String getIpv4CustEndLoopback() {
		return ipv4CustEndLoopback;
	}

	public void setIpv4CustEndLoopback(String ipv4CustEndLoopback) {
		this.ipv4CustEndLoopback = ipv4CustEndLoopback;
	}

	public String getIpv4CustWanIp() {
		return ipv4CustWanIp;
	}

	public void setIpv4CustWanIp(String ipv4CustWanIp) {
		this.ipv4CustWanIp = ipv4CustWanIp;
	}

	public String getIsIzo() {
		return isIzo;
	}

	public void setIsIzo(String isIzo) {
		this.isIzo = isIzo;
	}

	public String getIshub() {
		return ishub;
	}

	public void setIshub(String ishub) {
		this.ishub = ishub;
	}

	public String getJitter_Servicer_Level_Target__ms_() {
		return jitter_Servicer_Level_Target__ms_;
	}

	public void setJitter_Servicer_Level_Target__ms_(String jitter_Servicer_Level_Target__ms_) {
		this.jitter_Servicer_Level_Target__ms_ = jitter_Servicer_Level_Target__ms_;
	}

	public String getLANV4_Address() {
		return LANV4_Address;
	}

	public void setLANV4_Address(String lANV4_Address) {
		LANV4_Address = lANV4_Address;
	}

	public Timestamp getLastMacdDate() {
		return lastMacdDate;
	}

	public void setLastMacdDate(Timestamp lastMacdDate) {
		this.lastMacdDate = lastMacdDate;
	}

	public String getLatlong() {
		return latlong;
	}

	public void setLatlong(String latlong) {
		this.latlong = latlong;
	}

	public String getLmCommissioningDate() {
		return lmCommissioningDate;
	}

	public void setLmCommissioningDate(String lmCommissioningDate) {
		this.lmCommissioningDate = lmCommissioningDate;
	}

	public String getLogicalPeInterface() {
		return logicalPeInterface;
	}

	public void setLogicalPeInterface(String logicalPeInterface) {
		this.logicalPeInterface = logicalPeInterface;
	}

	public String getMasterVrfServiceId() {
		return masterVrfServiceId;
	}

	public void setMasterVrfServiceId(String masterVrfServiceId) {
		this.masterVrfServiceId = masterVrfServiceId;
	}

	public String getMean_Time_To_Restore__MTTR__in_Hrs() {
		return mean_Time_To_Restore__MTTR__in_Hrs;
	}

	public void setMean_Time_To_Restore__MTTR__in_Hrs(String mean_Time_To_Restore__MTTR__in_Hrs) {
		this.mean_Time_To_Restore__MTTR__in_Hrs = mean_Time_To_Restore__MTTR__in_Hrs;
	}

	public int getMinimumContractTermMonths() {
		return minimumContractTermMonths;
	}

	public void setMinimumContractTermMonths(int minimumContractTermMonths) {
		this.minimumContractTermMonths = minimumContractTermMonths;
	}

	public double getMrc() {
		return mrc;
	}

	public void setMrc(double mrc) {
		this.mrc = mrc;
	}

	public String getNetwork_Uptime() {
		return network_Uptime;
	}

	public void setNetwork_Uptime(String network_Uptime) {
		this.network_Uptime = network_Uptime;
	}

	public double getNrc() {
		return nrc;
	}

	public void setNrc(double nrc) {
		this.nrc = nrc;
	}

	public String getOpportunityBidCategory() {
		return opportunityBidCategory;
	}

	public void setOpportunityBidCategory(String opportunityBidCategory) {
		this.opportunityBidCategory = opportunityBidCategory;
	}

	public String getOpportunityClassification() {
		return opportunityClassification;
	}

	public void setOpportunityClassification(String opportunityClassification) {
		this.opportunityClassification = opportunityClassification;
	}

	public String getOpportunityId() {
		return opportunityId;
	}

	public void setOpportunityId(String opportunityId) {
		this.opportunityId = opportunityId;
	}

	public String getOrderCategory() {
		return orderCategory;
	}

	public void setOrderCategory(String orderCategory) {
		this.orderCategory = orderCategory;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getOrderLeId() {
		return orderLeId;
	}

	public void setOrderLeId(String orderLeId) {
		this.orderLeId = orderLeId;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getOuterVLAN() {
		return outerVLAN;
	}

	public void setOuterVLAN(String outerVLAN) {
		this.outerVLAN = outerVLAN;
	}

	public String getPacket_Drop() {
		return packet_Drop;
	}

	public void setPacket_Drop(String packet_Drop) {
		this.packet_Drop = packet_Drop;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getParentService() {
		return parentService;
	}

	public void setParentService(String parentService) {
		this.parentService = parentService;
	}

	public int getPartnerCuid() {
		return partnerCuid;
	}

	public void setPartnerCuid(int partnerCuid) {
		this.partnerCuid = partnerCuid;
	}

	public String getPartnerName() {
		return partnerName;
	}

	public void setPartnerName(String partnerName) {
		this.partnerName = partnerName;
	}

	public String getPeManagementIp() {
		return peManagementIp;
	}

	public void setPeManagementIp(String peManagementIp) {
		this.peManagementIp = peManagementIp;
	}

	public String getPeName() {
		return peName;
	}

	public void setPeName(String peName) {
		this.peName = peName;
	}

	public String getPhysicalPeport() {
		return physicalPeport;
	}

	public void setPhysicalPeport(String physicalPeport) {
		this.physicalPeport = physicalPeport;
	}

	public String getPoNumber() {
		return poNumber;
	}

	public void setPoNumber(String poNumber) {
		this.poNumber = poNumber;
	}

	public String getPoNumberRefId() {
		return poNumberRefId;
	}

	public void setPoNumberRefId(String poNumberRefId) {
		this.poNumberRefId = poNumberRefId;
	}

	public String getPortSpeed() {
		return portSpeed;
	}

	public void setPortSpeed(String portSpeed) {
		this.portSpeed = portSpeed;
	}

	public String getPrisec() {
		return prisec;
	}

	public void setPrisec(String prisec) {
		this.prisec = prisec;
	}

	public String getProductFlavour() {
		return productFlavour;
	}

	public void setProductFlavour(String productFlavour) {
		this.productFlavour = productFlavour;
	}

	public String getProtectionRequired() {
		return protectionRequired;
	}

	public void setProtectionRequired(String protectionRequired) {
		this.protectionRequired = protectionRequired;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getResilientType() {
		return resilientType;
	}

	public void setResilientType(String resilientType) {
		this.resilientType = resilientType;
	}

	public String getRound_Trip_Delay__RTD_() {
		return round_Trip_Delay__RTD_;
	}

	public void setRound_Trip_Delay__RTD_(String round_Trip_Delay__RTD_) {
		this.round_Trip_Delay__RTD_ = round_Trip_Delay__RTD_;
	}

	public String getRoutingProtocol() {
		return routingProtocol;
	}

	public void setRoutingProtocol(String routingProtocol) {
		this.routingProtocol = routingProtocol;
	}

	public String getScopeOfManagement() {
		return scopeOfManagement;
	}

	public void setScopeOfManagement(String scopeOfManagement) {
		this.scopeOfManagement = scopeOfManagement;
	}

	public String getSecsId() {
		return secsId;
	}

	public void setSecsId(String secsId) {
		this.secsId = secsId;
	}

	public String getServiceClass() {
		return serviceClass;
	}

	public void setServiceClass(String serviceClass) {
		this.serviceClass = serviceClass;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getServiceLink() {
		return serviceLink;
	}

	public void setServiceLink(String serviceLink) {
		this.serviceLink = serviceLink;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getServiceOptionType() {
		return serviceOptionType;
	}

	public void setServiceOptionType(String serviceOptionType) {
		this.serviceOptionType = serviceOptionType;
	}

	public String getServiceSegment() {
		return serviceSegment;
	}

	public void setServiceSegment(String serviceSegment) {
		this.serviceSegment = serviceSegment;
	}

	public String getServiceStatus() {
		return serviceStatus;
	}

	public void setServiceStatus(String serviceStatus) {
		this.serviceStatus = serviceStatus;
	}

	public String getServiceTopology() {
		return serviceTopology;
	}

	public void setServiceTopology(String serviceTopology) {
		this.serviceTopology = serviceTopology;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public Timestamp getServiceidCreatedDate() {
		return serviceidCreatedDate;
	}

	public void setServiceidCreatedDate(Timestamp serviceidCreatedDate) {
		this.serviceidCreatedDate = serviceidCreatedDate;
	}

	public Timestamp getServiceidModifiedDate() {
		return serviceidModifiedDate;
	}

	public void setServiceidModifiedDate(Timestamp serviceidModifiedDate) {
		this.serviceidModifiedDate = serviceidModifiedDate;
	}

	public String getServiceoption() {
		return serviceoption;
	}

	public void setServiceoption(String serviceoption) {
		this.serviceoption = serviceoption;
	}

	public String getSiteAddress() {
		return siteAddress;
	}

	public void setSiteAddress(String siteAddress) {
		this.siteAddress = siteAddress;
	}

	public String getSltVariant() {
		return sltVariant;
	}

	public void setSltVariant(String sltVariant) {
		this.sltVariant = sltVariant;
	}

	public String getSmName() {
		return smName;
	}

	public void setSmName(String smName) {
		this.smName = smName;
	}

	public String getSuborderType() {
		return suborderType;
	}

	public void setSuborderType(String suborderType) {
		this.suborderType = suborderType;
	}

	public String getSupportModel() {
		return supportModel;
	}

	public void setSupportModel(String supportModel) {
		this.supportModel = supportModel;
	}

	public String getTataCommWANIPv4() {
		return tataCommWANIPv4;
	}

	public void setTataCommWANIPv4(String tataCommWANIPv4) {
		this.tataCommWANIPv4 = tataCommWANIPv4;
	}

	public Timestamp getTerminationDate() {
		return terminationDate;
	}

	public void setTerminationDate(Timestamp terminationDate) {
		this.terminationDate = terminationDate;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTypeFixedBurstable() {
		return typeFixedBurstable;
	}

	public void setTypeFixedBurstable(String typeFixedBurstable) {
		this.typeFixedBurstable = typeFixedBurstable;
	}

	public String getTypeOfConnectivity() {
		return typeOfConnectivity;
	}

	public void setTypeOfConnectivity(String typeOfConnectivity) {
		this.typeOfConnectivity = typeOfConnectivity;
	}

	public String getUsage_Model() {
		return usage_Model;
	}

	public void setUsage_Model(String usage_Model) {
		this.usage_Model = usage_Model;
	}

	public String getVlan() {
		return vlan;
	}

	public void setVlan(String vlan) {
		this.vlan = vlan;
	}

	public String getWANV4_Address() {
		return WANV4_Address;
	}

	public void setWANV4_Address(String wANV4_Address) {
		WANV4_Address = wANV4_Address;
	}

	public String getErfOrderId() {
		return erfOrderId;
	}

	public void setErfOrderId(String erfOrderId) {
		this.erfOrderId = erfOrderId;
	}
	

}
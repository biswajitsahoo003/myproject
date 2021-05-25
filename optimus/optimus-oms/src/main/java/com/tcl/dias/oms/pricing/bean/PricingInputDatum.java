
package com.tcl.dias.oms.pricing.bean;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "site_id", "latitude_final", "longitude_final", "prospect_name", "bw_mbps", "burstable_bw",
	"resp_city", "customer_segment", "sales_org", "product_name", "feasibility_response_created_date",
	"local_loop_interface", "last_mile_contract_term", "account_id_with_18_digit", "opportunity_term",
	"quotetype_quote", "connection_type", "sum_no_of_sites_uni_len", "cpe_variant", "cpe_management_type",
	"cpe_supply_type", "topology", "sum_onnet_flag", "sum_offnet_flag", "lm_arc_bw_onwl", "lm_nrc_bw_onwl",
	"lm_nrc_mux_onwl", "lm_nrc_inbldg_onwl", "lm_nrc_ospcapex_onwl", "lm_nrc_nerental_onwl", "lm_arc_bw_prov_ofrf",
	"lm_nrc_bw_prov_ofrf", "lm_nrc_mast_ofrf", "lm_arc_bw_onrf", "lm_nrc_bw_onrf", "lm_nrc_mast_onrf",
	"Orch_Connection", "Orch_LM_Type", "additional_ip_flag", "ip_address_arrangement", "ipv4_address_pool_size",
	"ipv6_address_pool_size","cu_le_id","product_solution","macd_service",
	"ll_change",
	"macd_option",
	"trigger_feasibility",
	"old_contract_term",
	"service_commissioned_date",
	"lat_long",
	"service_id",
	"backup_port_requested",
	"old_Port_Bw","old_Ll_Bw","parallel_run_days","prd_category","cpe_chassis_changed",
	"local_loop_bw","country","siteFlag","Compressed_Internet_Ratio",
	"partner_account_id_with_18_digit",
	"partner_profile",
	"quotetype_partner",
	"solution_type",
	"Mast_3KM_avg_mast_ht",
	"avg_mast_ht",
	"user_name",
	"user_type",
	"deal_reg_flag",
	"lm_otc_modem_charges_offwl",
	"lm_otc_nrc_installation_offwl",
	"lm_arc_modem_charges_offwl",
	"lm_arc_bw_offwl",
	//commercial manual feasability subcomponent
	"lm_nrc_prow_onwl",
	"lm_arc_prow_onwl",
	"lm_arc_converter_charges_onrf",
	"lm_arc_bw_backhaul_onrf",
	"lm_arc_colocation_charges_onrf",
	"provider",
	"BHConnectivity",
	"non_standard","Orch_Category","vpn_Name","POP_DIST_KM_SERVICE_MOD","concurrent_sessions","pbx_type","cube_licenses","pvdm_quantities","product_code","type_of_connectivity"
	,
	//CST-10 customer portal
	"is_customer"
})
public class PricingInputDatum {

	@JsonProperty("cpe_chassis_changed")
	private String cpeChassisChanged;
	@JsonProperty("old_Port_Bw")
	private String oldPortBw;
	@JsonProperty("old_Ll_Bw")
	private String oldLlBw;
	@JsonProperty("site_id")
	private String siteId;
	@JsonProperty("latitude_final")
	private String latitudeFinal;
	@JsonProperty("longitude_final")
	private String longitudeFinal;
	@JsonProperty("prospect_name")
	private String prospectName;
	@JsonProperty("bw_mbps")
	private String bwMbps;
	@JsonProperty("burstable_bw")
	private String burstableBw;
	@JsonProperty("resp_city")
	private String respCity;
	@JsonProperty("customer_segment")
	private String customerSegment;
	@JsonProperty("sales_org")
	private String salesOrg;
	@JsonProperty("product_name")
	private String productName;
	@JsonProperty("feasibility_response_created_date")
	private String feasibilityResponseCreatedDate;
	@JsonProperty("local_loop_interface")
	private String localLoopInterface;
	@JsonProperty("last_mile_contract_term")
	private String lastMileContractTerm;
	@JsonProperty("account_id_with_18_digit")
	private String accountIdWith18Digit;
	@JsonProperty("opportunity_term")
	private String opportunityTerm;
	@JsonProperty("quotetype_quote")
	private String quotetypeQuote;
	@JsonProperty("connection_type")
	private String connectionType;
	@JsonProperty("sum_no_of_sites_uni_len")
	private String sumNoOfSitesUniLen;
	@JsonProperty("cpe_variant")
	private String cpeVariant;
	@JsonProperty("cpe_management_type")
	private String cpeManagementType;
	@JsonProperty("cpe_supply_type")
	private String cpeSupplyType;
	@JsonProperty("topology")
	private String topology;
	@JsonProperty("sum_onnet_flag")
	private String sumOnnetFlag;
	@JsonProperty("sum_offnet_flag")
	private String sumOffnetFlag;
	@JsonProperty("lm_arc_bw_onwl")
	private String lmArcBwOnwl;
	@JsonProperty("lm_nrc_bw_onwl")
	private String lmNrcBwOnwl;
	@JsonProperty("lm_nrc_mux_onwl")
	private String lmNrcMuxOnwl;
	@JsonProperty("lm_nrc_inbldg_onwl")
	private String lmNrcInbldgOnwl;
	@JsonProperty("lm_nrc_ospcapex_onwl")
	private String lmNrcOspcapexOnwl;
	@JsonProperty("lm_nrc_nerental_onwl")
	private String lmNrcNerentalOnwl;
	@JsonProperty("lm_arc_bw_prov_ofrf")
	private String lmArcBwProvOfrf;
	@JsonProperty("lm_nrc_bw_prov_ofrf")
	private String lmNrcBwProvOfrf;
	@JsonProperty("lm_nrc_mast_ofrf")
	private String lmNrcMastOfrf;
	@JsonProperty("lm_arc_bw_onrf")
	private String lmArcBwOnrf;
	@JsonProperty("lm_nrc_bw_onrf")
	private String lmNrcBwOnrf;
	@JsonProperty("lm_nrc_mast_onrf")
	private String lmNrcMastOnrf;
	@JsonProperty("Orch_Connection")
	private String orchConnection;
	@JsonProperty("Orch_LM_Type")
	private String orchLMType;
	@JsonProperty("additional_ip_flag")
	private String additionalIpFlag;
	@JsonProperty("ip_address_arrangement")
	private String ipAddressArrangement;
	@JsonProperty("ipv4_address_pool_size")
	private String ipv4AddressPoolSize;
	@JsonProperty("ipv6_address_pool_size")
	private String ipv6AddressPoolSize;
	@JsonProperty("cu_le_id")
	private String cuLeId;
	@JsonProperty("lat_long")
	private String latLong;
	@JsonProperty("old_contract_term")
	private String oldContractTerm;
	@JsonProperty("service_commissioned_date")
	private String serviceCommissionedDate;
	@JsonProperty("macd_option")
	private String macdOption;
	@JsonProperty("trigger_feasibility")
	private String triggerFeasibility;
	@JsonProperty("ll_change")
	private String llChange;
	@JsonProperty("macd_service")
	private String macdService;
	@JsonProperty("service_id")
	private String serviceId;
	@JsonProperty("backup_port_requested")
	private String backupPortRequested;
	@JsonProperty("Orch_Category")
	private String orchCategory;
	@JsonProperty("parallel_run_days")
	private String parallelRunDays;
	@JsonProperty("local_loop_bw")
	private String localLoopBw;
	@JsonProperty("Compressed_Internet_Ratio")
	private String compressedInternetRatio;
	@JsonProperty("partner_account_id_with_18_digit")
	private String partnerAccountIdWith18Digit;
	@JsonProperty("partner_profile")
	private String partnerProfile;
	@JsonProperty("quotetype_partner")
	private String quoteTypePartner;
	@JsonProperty("solution_type")
	private String solutionType;
	@JsonProperty("Mast_3KM_avg_mast_ht")
	private String mast3KMAvgMastHt; 
	@JsonProperty("avg_mast_ht")
	private String avgMastHt;
	@JsonProperty("min_hh_fatg")
	private String minHhFatg; 
	@JsonProperty("POP_DIST_KM_SERVICE_MOD")
	private String pOPDISTKMSERVICEMOD;
	@JsonProperty("user_name")
	private String userName;
	@JsonProperty("user_type")
	private String userType;
	@JsonProperty("deal_reg_flag")
	private String dealRegFlag;
	
	// for Offnet wireline
	@JsonProperty("lm_otc_modem_charges_offwl")
	private String lmOtcModemChargesOffwl;
	
	@JsonProperty("lm_otc_nrc_installation_offwl")
	private String lmOtcNrcInstallationOffwl;
	
	@JsonProperty("lm_arc_modem_charges_offwl")
	private String lmArcModemChargesOffwl;
	
	@JsonProperty("lm_arc_bw_offwl")
	private String lmArcBWOffwl;
	
	//mf subcomponent
	@JsonProperty("lm_nrc_prow_onwl")
	private String lmNrcProwOnwl;
	@JsonProperty("lm_arc_prow_onwl")
	private String lmArcProwOnwl;
	@JsonProperty("lm_arc_converter_charges_onrf")
	private String lmArcConverterChargesOnrf;
	@JsonProperty("lm_arc_bw_backhaul_onrf")
	private String lmArcBwBackhaulOnrf ;
	@JsonProperty("lm_arc_colocation_charges_onrf")
	private String lmArcColocationChargesOnrf;
	
	@JsonProperty("provider")
	private String provider;
	@JsonProperty("BHConnectivity")
	private String BHConnectivity;
    @JsonProperty("non_standard")
    private String nonStandard;
    @JsonProperty("product_solution")
    private String productSolution;
    @JsonProperty("vpn_Name")
    private String vpnName;
    @JsonProperty("siteFlag")
    private String siteFlag;
    @JsonProperty("country")
    private String country;
    @JsonProperty("prd_category")
    private String ProductCategory;
    @JsonProperty("concurrent_sessions")
    private String concurrentSessions;
    @JsonProperty("pbx_type")
    private String pbxType;
    @JsonProperty("cube_licenses")
    private String cubeLicenses;
    @JsonProperty("pvdm_quantities")
    private String pvdmQuantities;
    @JsonProperty("product_code")
    private String productCode;
    @JsonProperty("type_of_connectivity")
    private String typeOfConnectivity;
    @JsonProperty("no_of_additional_ip")
    private String noOfAdditionalIp;
    
	public String getProductCategory() {
		return ProductCategory;
	}

	public void setProductCategory(String productCategory) {
		ProductCategory = productCategory;
	}

	public String getProductSolution() {
		return productSolution;
	}

	public void setProductSolution(String productSolution) {
		this.productSolution = productSolution;
	}

	public String getVpnName() {
		return vpnName;
	}

	public void setVpnName(String vpnName) {
		this.vpnName = vpnName;
	}

	public String getSiteFlag() {
		return siteFlag;
	}

	public void setSiteFlag(String siteFlag) {
		this.siteFlag = siteFlag;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

    
    @JsonProperty("is_customer")
	private String isCustomer;
	@JsonProperty("is_colocated")
	private String isColocated;

	@JsonProperty("is_demo")
	private String isDemo;
	@JsonProperty("demo_type")
	private String demoType;
		
	// Attribute to indicate the quote is non standard or not
	public String getNonStandard() {
	return nonStandard;
	}

	public void setNonStandard(String nonStandard) {
	  this.nonStandard = nonStandard;
	}
	
	
	@JsonProperty("provider")
	public String getProvider() {
		return provider;
	}

	@JsonProperty("BHConnectivity")
	public void setProvider(String provider) {
		this.provider = provider;
	}

	public String getBHConnectivity() {
		return BHConnectivity;
	}

	public void setBHConnectivity(String bHConnectivity) {
		BHConnectivity = bHConnectivity;
	}

	@JsonProperty("lm_nrc_prow_onwl")
	public String getLmNrcProwOnwl() {
		return lmNrcProwOnwl;
	}

	@JsonProperty("lm_nrc_prow_onwl")
	public void setLmNrcProwOnwl(String lmNrcProwOnwl) {
		this.lmNrcProwOnwl = lmNrcProwOnwl;
	}

	@JsonProperty("lm_arc_prow_onwl")
	public String getLmArcProwOnwl() {
		return lmArcProwOnwl;
	}

	@JsonProperty("lm_arc_prow_onwl")
	public void setLmArcProwOnwl(String lmArcProwOnwl) {
		this.lmArcProwOnwl = lmArcProwOnwl;
	}

	@JsonProperty("lm_arc_converter_charges_onrf")
	public String getLmArcConverterChargesOnrf() {
		return lmArcConverterChargesOnrf;
	}

	@JsonProperty("lm_arc_converter_charges_onrf")
	public void setLmArcConverterChargesOnrf(String lmArcConverterChargesOnrf) {
		this.lmArcConverterChargesOnrf = lmArcConverterChargesOnrf;
	}

	@JsonProperty("lm_arc_bw_backhaul_onrf")
	public String getLmArcBwBackhaulOnrf() {
		return lmArcBwBackhaulOnrf;
	}

	@JsonProperty("lm_arc_bw_backhaul_onrf")
	public void setLmArcBwBackhaulOnrf(String lmArcBwBackhaulOnrf) {
		this.lmArcBwBackhaulOnrf = lmArcBwBackhaulOnrf;
	}

	

	

	@JsonProperty("lm_arc_colocation_charges_onrf")
	public String getLmArcColocationChargesOnrf() {
		return lmArcColocationChargesOnrf;
	}

	@JsonProperty("lm_arc_colocation_charges_onrf")
	public void setLmArcColocationChargesOnrf(String lmArcColocationChargesOnrf) {
		this.lmArcColocationChargesOnrf = lmArcColocationChargesOnrf;
	}

	public String getLmOtcModemChargesOffwl() {
		return lmOtcModemChargesOffwl;
	}

	public void setLmOtcModemChargesOffwl(String lmOtcModemChargesOffwl) {
		this.lmOtcModemChargesOffwl = lmOtcModemChargesOffwl;
	}

	public String getLmOtcNrcInstallationOffwl() {
		return lmOtcNrcInstallationOffwl;
	}

	public void setLmOtcNrcInstallationOffwl(String lmOtcNrcInstallationOffwl) {
		this.lmOtcNrcInstallationOffwl = lmOtcNrcInstallationOffwl;
	}

	public String getLmArcModemChargesOffwl() {
		return lmArcModemChargesOffwl;
	}

	public void setLmArcModemChargesOffwl(String lmArcModemChargesOffwl) {
		this.lmArcModemChargesOffwl = lmArcModemChargesOffwl;
	}

	public String getLmArcBWOffwl() {
		return lmArcBWOffwl;
	}

	public void setLmArcBWOffwl(String lmArcBWOffwl) {
		this.lmArcBWOffwl = lmArcBWOffwl;
	}

	public String getServiceCommissionedDate() {
		return serviceCommissionedDate;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}



	@JsonProperty("Compressed_Internet_Ratio")
	public String getCompressedInternetRatio() {
		return compressedInternetRatio;
	}
	
	@JsonProperty("Compressed_Internet_Ratio")
	public void setCompressedInternetRatio(String compressedInternetRatio) {
		this.compressedInternetRatio = compressedInternetRatio;
	}
	
	

	@JsonProperty("local_loop_bw")
	public String getLocalLoopBw() {
		return localLoopBw;
	}

	@JsonProperty("local_loop_bw")
	public void setLocalLoopBw(String localLoopBw) {
		this.localLoopBw = localLoopBw;
	}

	@JsonProperty("cpe_chassis_changed")
	public String getCpeChassisChanged() {
		return cpeChassisChanged;
	}

	@JsonProperty("cpe_chassis_changed")
	public void setCpeChassisChanged(String cpeChasisChanged) {
		this.cpeChassisChanged = cpeChasisChanged;
	}

	@JsonProperty("parallel_run_days")
	public String getParallelRunDays() {
		return parallelRunDays;
	}

	@JsonProperty("parallel_run_days")
	public void setParallelRunDays(String parallelRunDays) {
		this.parallelRunDays = parallelRunDays;
	}

	@JsonProperty("site_id")
	public String getSiteId() {
		return siteId;
	}

	@JsonProperty("site_id")
	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}

	@JsonProperty("latitude_final")
	public String getLatitudeFinal() {
		return latitudeFinal;
	}

	@JsonProperty("latitude_final")
	public void setLatitudeFinal(String latitudeFinal) {
		this.latitudeFinal = latitudeFinal;
	}

	@JsonProperty("longitude_final")
	public String getLongitudeFinal() {
		return longitudeFinal;
	}

	@JsonProperty("longitude_final")
	public void setLongitudeFinal(String longitudeFinal) {
		this.longitudeFinal = longitudeFinal;
	}

	@JsonProperty("prospect_name")
	public String getProspectName() {
		return prospectName;
	}

	@JsonProperty("prospect_name")
	public void setProspectName(String prospectName) {
		this.prospectName = prospectName;
	}

	@JsonProperty("bw_mbps")
	public String getBwMbps() {
		return bwMbps;
	}

	@JsonProperty("bw_mbps")
	public void setBwMbps(String bwMbps) {
		this.bwMbps = bwMbps;
	}

	@JsonProperty("burstable_bw")
	public String getBurstableBw() {
		return burstableBw;
	}

	@JsonProperty("burstable_bw")
	public void setBurstableBw(String burstableBw) {
		this.burstableBw = burstableBw;
	}

	@JsonProperty("resp_city")
	public String getRespCity() {
		return respCity;
	}

	@JsonProperty("resp_city")
	public void setRespCity(String respCity) {
		this.respCity = respCity;
	}

	@JsonProperty("customer_segment")
	public String getCustomerSegment() {
		return customerSegment;
	}

	@JsonProperty("customer_segment")
	public void setCustomerSegment(String customerSegment) {
		this.customerSegment = customerSegment;
	}

	@JsonProperty("sales_org")
	public String getSalesOrg() {
		return salesOrg;
	}

	@JsonProperty("sales_org")
	public void setSalesOrg(String salesOrg) {
		this.salesOrg = salesOrg;
	}

	@JsonProperty("product_name")
	public String getProductName() {
		return productName;
	}

	@JsonProperty("product_name")
	public void setProductName(String productName) {
		this.productName = productName;
	}

	@JsonProperty("feasibility_response_created_date")
	public String getFeasibilityResponseCreatedDate() {
		return feasibilityResponseCreatedDate;
	}

	@JsonProperty("feasibility_response_created_date")
	public void setFeasibilityResponseCreatedDate(String feasibilityResponseCreatedDate) {
		this.feasibilityResponseCreatedDate = feasibilityResponseCreatedDate;
	}

	@JsonProperty("local_loop_interface")
	public String getLocalLoopInterface() {
		return localLoopInterface;
	}

	@JsonProperty("local_loop_interface")
	public void setLocalLoopInterface(String localLoopInterface) {
		this.localLoopInterface = localLoopInterface;
	}

	@JsonProperty("last_mile_contract_term")
	public String getLastMileContractTerm() {
		return lastMileContractTerm;
	}

	@JsonProperty("last_mile_contract_term")
	public void setLastMileContractTerm(String lastMileContractTerm) {
		this.lastMileContractTerm = lastMileContractTerm;
	}

	@JsonProperty("account_id_with_18_digit")
	public String getAccountIdWith18Digit() {
		return accountIdWith18Digit;
	}

	@JsonProperty("account_id_with_18_digit")
	public void setAccountIdWith18Digit(String accountIdWith18Digit) {
		this.accountIdWith18Digit = accountIdWith18Digit;
	}

	@JsonProperty("opportunity_term")
	public String getOpportunityTerm() {
		return opportunityTerm;
	}

	@JsonProperty("opportunity_term")
	public void setOpportunityTerm(String opportunityTerm) {
		this.opportunityTerm = opportunityTerm;
	}

	@JsonProperty("quotetype_quote")
	public String getQuotetypeQuote() {
		return quotetypeQuote;
	}

	@JsonProperty("quotetype_quote")
	public void setQuotetypeQuote(String quotetypeQuote) {
		this.quotetypeQuote = quotetypeQuote;
	}

	@JsonProperty("connection_type")
	public String getConnectionType() {
		return connectionType;
	}

	@JsonProperty("connection_type")
	public void setConnectionType(String connectionType) {
		this.connectionType = connectionType;
	}

	@JsonProperty("sum_no_of_sites_uni_len")
	public String getSumNoOfSitesUniLen() {
		return sumNoOfSitesUniLen;
	}

	@JsonProperty("sum_no_of_sites_uni_len")
	public void setSumNoOfSitesUniLen(String sumNoOfSitesUniLen) {
		this.sumNoOfSitesUniLen = sumNoOfSitesUniLen;
	}

	@JsonProperty("cpe_variant")
	public String getCpeVariant() {
		return cpeVariant;
	}

	@JsonProperty("cpe_variant")
	public void setCpeVariant(String cpeVariant) {
		this.cpeVariant = cpeVariant;
	}

	@JsonProperty("cpe_management_type")
	public String getCpeManagementType() {
		return cpeManagementType;
	}

	@JsonProperty("cpe_management_type")
	public void setCpeManagementType(String cpeManagementType) {
		this.cpeManagementType = cpeManagementType;
	}

	@JsonProperty("cpe_supply_type")
	public String getCpeSupplyType() {
		return cpeSupplyType;
	}

	@JsonProperty("cpe_supply_type")
	public void setCpeSupplyType(String cpeSupplyType) {
		this.cpeSupplyType = cpeSupplyType;
	}

	@JsonProperty("topology")
	public String getTopology() {
		return topology;
	}

	@JsonProperty("topology")
	public void setTopology(String topology) {
		this.topology = topology;
	}

	@JsonProperty("sum_onnet_flag")
	public String getSumOnnetFlag() {
		return sumOnnetFlag;
	}

	@JsonProperty("sum_onnet_flag")
	public void setSumOnnetFlag(String sumOnnetFlag) {
		this.sumOnnetFlag = sumOnnetFlag;
	}

	@JsonProperty("sum_offnet_flag")
	public String getSumOffnetFlag() {
		return sumOffnetFlag;
	}

	@JsonProperty("sum_offnet_flag")
	public void setSumOffnetFlag(String sumOffnetFlag) {
		this.sumOffnetFlag = sumOffnetFlag;
	}

	@JsonProperty("lm_arc_bw_onwl")
	public String getLmArcBwOnwl() {
		return lmArcBwOnwl;
	}

	@JsonProperty("lm_arc_bw_onwl")
	public void setLmArcBwOnwl(String lmArcBwOnwl) {
		this.lmArcBwOnwl = lmArcBwOnwl;
	}

	@JsonProperty("lm_nrc_bw_onwl")
	public String getLmNrcBwOnwl() {
		return lmNrcBwOnwl;
	}

	@JsonProperty("lm_nrc_bw_onwl")
	public void setLmNrcBwOnwl(String lmNrcBwOnwl) {
		this.lmNrcBwOnwl = lmNrcBwOnwl;
	}

	@JsonProperty("lm_nrc_mux_onwl")
	public String getLmNrcMuxOnwl() {
		return lmNrcMuxOnwl;
	}

	@JsonProperty("lm_nrc_mux_onwl")
	public void setLmNrcMuxOnwl(String lmNrcMuxOnwl) {
		this.lmNrcMuxOnwl = lmNrcMuxOnwl;
	}

	@JsonProperty("lm_nrc_inbldg_onwl")
	public String getLmNrcInbldgOnwl() {
		return lmNrcInbldgOnwl;
	}

	@JsonProperty("lm_nrc_inbldg_onwl")
	public void setLmNrcInbldgOnwl(String lmNrcInbldgOnwl) {
		this.lmNrcInbldgOnwl = lmNrcInbldgOnwl;
	}

	@JsonProperty("lm_nrc_ospcapex_onwl")
	public String getLmNrcOspcapexOnwl() {
		return lmNrcOspcapexOnwl;
	}

	@JsonProperty("lm_nrc_ospcapex_onwl")
	public void setLmNrcOspcapexOnwl(String lmNrcOspcapexOnwl) {
		this.lmNrcOspcapexOnwl = lmNrcOspcapexOnwl;
	}

	@JsonProperty("lm_nrc_nerental_onwl")
	public String getLmNrcNerentalOnwl() {
		return lmNrcNerentalOnwl;
	}

	@JsonProperty("lm_nrc_nerental_onwl")
	public void setLmNrcNerentalOnwl(String lmNrcNerentalOnwl) {
		this.lmNrcNerentalOnwl = lmNrcNerentalOnwl;
	}

	@JsonProperty("lm_arc_bw_prov_ofrf")
	public String getLmArcBwProvOfrf() {
		return lmArcBwProvOfrf;
	}

	@JsonProperty("lm_arc_bw_prov_ofrf")
	public void setLmArcBwProvOfrf(String lmArcBwProvOfrf) {
		this.lmArcBwProvOfrf = lmArcBwProvOfrf;
	}

	@JsonProperty("lm_nrc_bw_prov_ofrf")
	public String getLmNrcBwProvOfrf() {
		return lmNrcBwProvOfrf;
	}

	@JsonProperty("lm_nrc_bw_prov_ofrf")
	public void setLmNrcBwProvOfrf(String lmNrcBwProvOfrf) {
		this.lmNrcBwProvOfrf = lmNrcBwProvOfrf;
	}

	@JsonProperty("lm_nrc_mast_ofrf")
	public String getLmNrcMastOfrf() {
		return lmNrcMastOfrf;
	}

	@JsonProperty("lm_nrc_mast_ofrf")
	public void setLmNrcMastOfrf(String lmNrcMastOfrf) {
		this.lmNrcMastOfrf = lmNrcMastOfrf;
	}

	@JsonProperty("lm_arc_bw_onrf")
	public String getLmArcBwOnrf() {
		return lmArcBwOnrf;
	}

	@JsonProperty("lm_arc_bw_onrf")
	public void setLmArcBwOnrf(String lmArcBwOnrf) {
		this.lmArcBwOnrf = lmArcBwOnrf;
	}

	@JsonProperty("lm_nrc_bw_onrf")
	public String getLmNrcBwOnrf() {
		return lmNrcBwOnrf;
	}

	@JsonProperty("lm_nrc_bw_onrf")
	public void setLmNrcBwOnrf(String lmNrcBwOnrf) {
		this.lmNrcBwOnrf = lmNrcBwOnrf;
	}

	@JsonProperty("lm_nrc_mast_onrf")
	public String getLmNrcMastOnrf() {
		return lmNrcMastOnrf;
	}

	@JsonProperty("lm_nrc_mast_onrf")
	public void setLmNrcMastOnrf(String lmNrcMastOnrf) {
		this.lmNrcMastOnrf = lmNrcMastOnrf;
	}

	@JsonProperty("Orch_Connection")
	public String getOrchConnection() {
		return orchConnection;
	}

	@JsonProperty("Orch_Connection")
	public void setOrchConnection(String orchConnection) {
		this.orchConnection = orchConnection;
	}

	@JsonProperty("Orch_LM_Type")
	public String getOrchLMType() {
		return orchLMType;
	}

	@JsonProperty("Orch_LM_Type")
	public void setOrchLMType(String orchLMType) {
		this.orchLMType = orchLMType;
	}

	@JsonProperty("additional_ip_flag")
	public String getAdditionalIpFlag() {
		return additionalIpFlag;
	}

	@JsonProperty("additional_ip_flag")
	public void setAdditionalIpFlag(String additionalIpFlag) {
		this.additionalIpFlag = additionalIpFlag;
	}

	@JsonProperty("ip_address_arrangement")
	public String getIpAddressArrangement() {
		return ipAddressArrangement;
	}

	@JsonProperty("ip_address_arrangement")
	public void setIpAddressArrangement(String ipAddressArrangement) {
		this.ipAddressArrangement = ipAddressArrangement;
	}

	@JsonProperty("ipv4_address_pool_size")
	public String getIpv4AddressPoolSize() {
		return ipv4AddressPoolSize;
	}

	@JsonProperty("ipv4_address_pool_size")
	public void setIpv4AddressPoolSize(String ipv4AddressPoolSize) {
		this.ipv4AddressPoolSize = ipv4AddressPoolSize;
	}

	@JsonProperty("ipv6_address_pool_size")
	public String getIpv6AddressPoolSize() {
		return ipv6AddressPoolSize;
	}

	@JsonProperty("ipv6_address_pool_size")
	public void setIpv6AddressPoolSize(String ipv6AddressPoolSize) {
		this.ipv6AddressPoolSize = ipv6AddressPoolSize;
	}
	@JsonProperty("cu_le_id")
	public String getCuLeId() {
		return cuLeId;
	}
	@JsonProperty("cu_le_id")
	public void setCuLeId(String cuLeId) {
		this.cuLeId = cuLeId;
	}

	@JsonProperty("old_contract_term")
	public String getOldContractTerm() {
		return oldContractTerm;
	}

	@JsonProperty("old_contract_term")
	public void setOldContractTerm(String oldContractTerm) {
		this.oldContractTerm = oldContractTerm;
	}


	@JsonProperty("service_commissioned_date")
	public String getServiceCommisionedDate() {
		return serviceCommissionedDate;
	}

	@JsonProperty("service_commissioned_date")
	public void setServiceCommissionedDate(String serviceCommissionedDate) {
		this.serviceCommissionedDate = serviceCommissionedDate;
	}



	@JsonProperty("macd_option")
	public String getMacdOption() {
		return macdOption;
	}

	@JsonProperty("macd_option")
	public void setMacdOption(String macdOption) {
		this.macdOption = macdOption;
	}

	@JsonProperty("trigger_feasibility")
	public String getTriggerFeasibility() {
		return triggerFeasibility;
	}

	@JsonProperty("trigger_feasibility")
	public void setTriggerFeasibility(String triggerFeasibility) {
		this.triggerFeasibility = triggerFeasibility;
	}

	@JsonProperty("ll_change")
	public String getLlChange() {
		return llChange;
	}

	@JsonProperty("ll_change")
	public void setLlChange(String llChange) {
		this.llChange = llChange;
	}

	@JsonProperty("macd_service")
	public String getMacdService() {
		return macdService;
	}

	@JsonProperty("macd_service")
	public void setMacdService(String macdService) {
		this.macdService = macdService;
	}


	public String getLatLong() {
		return latLong;
	}

	public void setLatLong(String latLong) {
		this.latLong = latLong;
	}

	@JsonProperty("service_id")
	public String getServiceId() {
		return serviceId;
	}

	@JsonProperty("service_id")
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	@JsonProperty("backup_port_requested")
	public String getBackupPortRequested() {
		return backupPortRequested;
	}

	@JsonProperty("backup_port_requested")
	public void setBackupPortRequested(String backupPortRequested) {
		this.backupPortRequested = backupPortRequested;
	}

	@JsonProperty("Orch_Category")
	public String getOrchCategory() {
		return orchCategory;
	}

	@JsonProperty("Orch_Category")
	public void setOrchCategory(String orchCategory) {
		this.orchCategory = orchCategory;
	}

	@JsonProperty("old_Ll_Bw")
	public String getOldLlBw() {
		return oldLlBw;
	}
	@JsonProperty("old_Ll_Bw")
	public void setOldLlBw(String oldLlBw) {
		this.oldLlBw = oldLlBw;
	}

	@JsonProperty("old_Port_Bw")
	public String getOldPortBw() {
		return oldPortBw;
	}

	@JsonProperty("old_Port_Bw")
	public void setOldPortBw(String oldPortBw) {
		this.oldPortBw = oldPortBw;
	}

	@JsonProperty("partner_account_id_with_18_digit")
	public String getPartnerAccountIdWith18Digit() {
		return partnerAccountIdWith18Digit;
	}

	@JsonProperty("partner_account_id_with_18_digit")
	public void setPartnerAccountIdWith18Digit(String partnerAccountIdWith18Digit) {
		this.partnerAccountIdWith18Digit = partnerAccountIdWith18Digit;
	}

	@JsonProperty("partner_profile")
	public String getPartnerProfile() {
		return partnerProfile;
	}

	@JsonProperty("partner_profile")
	public void setPartnerProfile(String partnerProfile) {
		this.partnerProfile = partnerProfile;
	}

	@JsonProperty("quotetype_partner")
	public String getQuoteTypePartner() {
		return quoteTypePartner;
	}

	@JsonProperty("quotetype_partner")
	public void setQuoteTypePartner(String quoteTypePartner) {
		this.quoteTypePartner = quoteTypePartner;
	}

	@JsonProperty("solution_type")
	public String getSolutionType() {
		return solutionType;
	}

	@JsonProperty("solution_type")
	public void setSolutionType(String solutionType) {
		this.solutionType = solutionType;
	}

	
	public String getMast3KMAvgMastHt() {
		return mast3KMAvgMastHt;
	}

	public void setMast3KMAvgMastHt(String mast3kmAvgMastHt) {
		mast3KMAvgMastHt = mast3kmAvgMastHt;
	}

	public String getAvgMastHt() {
		return avgMastHt;
	}

	public void setAvgMastHt(String avgMastHt) {
		this.avgMastHt = avgMastHt;
	}	

	public String getMinHhFatg() {
		return minHhFatg;
	}

	public void setMinHhFatg(String minHhFatg) {
		this.minHhFatg = minHhFatg;
	}

	public String getpOPDISTKMSERVICEMOD() {
		return pOPDISTKMSERVICEMOD;
	}

	public void setpOPDISTKMSERVICEMOD(String pOPDISTKMSERVICEMOD) {
		this.pOPDISTKMSERVICEMOD = pOPDISTKMSERVICEMOD;
	}

	@JsonProperty("deal_reg_flag")
	public String getDealRegFlag() {
		return dealRegFlag;
	}

	@JsonProperty("deal_reg_flag")
	public void setDealRegFlag(String dealRegFlag) {
		this.dealRegFlag = dealRegFlag;
	}
	
	@JsonProperty("is_customer")
	public String getIsCustomer() {
		return isCustomer;
	}

	@JsonProperty("is_customer")
	public void setIsCustomer(String isCustomer) {
		this.isCustomer = isCustomer;
	}

	public String getIsColocated() {
		return isColocated;
	}

	public void setIsColocated(String isColocated) {
		this.isColocated = isColocated;
	}

	@JsonProperty("is_demo")
	public String getIsDemo() {
		return isDemo;
	}

	@JsonProperty("is_demo")
	public void setIsDemo(String isDemo) {
		this.isDemo = isDemo;
	}

	@JsonProperty("demo_type")
	public String getDemoType() {
		return demoType;
	}

	@JsonProperty("demo_type")
	public void setDemoType(String demoType) {
		this.demoType = demoType;
	}

	@Override
	public String toString() {
		return "PricingInputDatum{" +
				"cpeChassisChanged='" + cpeChassisChanged + '\'' +
				", oldPortBw='" + oldPortBw + '\'' +
				", oldLlBw='" + oldLlBw + '\'' +
				", siteId='" + siteId + '\'' +
				", latitudeFinal='" + latitudeFinal + '\'' +
				", longitudeFinal='" + longitudeFinal + '\'' +
				", prospectName='" + prospectName + '\'' +
				", bwMbps='" + bwMbps + '\'' +
				", burstableBw='" + burstableBw + '\'' +
				", respCity='" + respCity + '\'' +
				", customerSegment='" + customerSegment + '\'' +
				", salesOrg='" + salesOrg + '\'' +
				", productName='" + productName + '\'' +
				", feasibilityResponseCreatedDate='" + feasibilityResponseCreatedDate + '\'' +
				", localLoopInterface='" + localLoopInterface + '\'' +
				", lastMileContractTerm='" + lastMileContractTerm + '\'' +
				", accountIdWith18Digit='" + accountIdWith18Digit + '\'' +
				", opportunityTerm='" + opportunityTerm + '\'' +
				", quotetypeQuote='" + quotetypeQuote + '\'' +
				", connectionType='" + connectionType + '\'' +
				", sumNoOfSitesUniLen='" + sumNoOfSitesUniLen + '\'' +
				", cpeVariant='" + cpeVariant + '\'' +
				", cpeManagementType='" + cpeManagementType + '\'' +
				", cpeSupplyType='" + cpeSupplyType + '\'' +
				", topology='" + topology + '\'' +
				", sumOnnetFlag='" + sumOnnetFlag + '\'' +
				", sumOffnetFlag='" + sumOffnetFlag + '\'' +
				", lmArcBwOnwl='" + lmArcBwOnwl + '\'' +
				", lmNrcBwOnwl='" + lmNrcBwOnwl + '\'' +
				", lmNrcMuxOnwl='" + lmNrcMuxOnwl + '\'' +
				", lmNrcInbldgOnwl='" + lmNrcInbldgOnwl + '\'' +
				", lmNrcOspcapexOnwl='" + lmNrcOspcapexOnwl + '\'' +
				", lmNrcNerentalOnwl='" + lmNrcNerentalOnwl + '\'' +
				", lmArcBwProvOfrf='" + lmArcBwProvOfrf + '\'' +
				", lmNrcBwProvOfrf='" + lmNrcBwProvOfrf + '\'' +
				", lmNrcMastOfrf='" + lmNrcMastOfrf + '\'' +
				", lmArcBwOnrf='" + lmArcBwOnrf + '\'' +
				", lmNrcBwOnrf='" + lmNrcBwOnrf + '\'' +
				", lmNrcMastOnrf='" + lmNrcMastOnrf + '\'' +
				", orchConnection='" + orchConnection + '\'' +
				", orchLMType='" + orchLMType + '\'' +
				", additionalIpFlag='" + additionalIpFlag + '\'' +
				", ipAddressArrangement='" + ipAddressArrangement + '\'' +
				", ipv4AddressPoolSize='" + ipv4AddressPoolSize + '\'' +
				", ipv6AddressPoolSize='" + ipv6AddressPoolSize + '\'' +
				", cuLeId='" + cuLeId + '\'' +
				", latLong='" + latLong + '\'' +
				", oldContractTerm='" + oldContractTerm + '\'' +
				", serviceCommissionedDate='" + serviceCommissionedDate + '\'' +
				", macdOption='" + macdOption + '\'' +
				", triggerFeasibility='" + triggerFeasibility + '\'' +
				", llChange='" + llChange + '\'' +
				", macdService='" + macdService + '\'' +
				", serviceId='" + serviceId + '\'' +
				", backupPortRequested='" + backupPortRequested + '\'' +
				", orchCategory='" + orchCategory + '\'' +
				", parallelRunDays='" + parallelRunDays + '\'' +
				", localLoopBw='" + localLoopBw + '\'' +
				", compressedInternetRatio='" + compressedInternetRatio + '\'' +
				", partnerAccountIdWith18Digit='" + partnerAccountIdWith18Digit + '\'' +
				", partnerProfile='" + partnerProfile + '\'' +
				", quoteTypePartner='" + quoteTypePartner + '\'' +
				", solutionType='" + solutionType + '\'' +
				", mast3KMAvgMastHt='" + mast3KMAvgMastHt + '\'' +
				", avgMastHt='" + avgMastHt + '\'' +
				", minHhFatg='" + minHhFatg + '\'' +
				", pOPDISTKMSERVICEMOD='" + pOPDISTKMSERVICEMOD + '\'' +
				", userName='" + userName + '\'' +
				", userType='" + userType + '\'' +
				", dealRegFlag='" + dealRegFlag + '\'' +
				", lmOtcModemChargesOffwl='" + lmOtcModemChargesOffwl + '\'' +
				", lmOtcNrcInstallationOffwl='" + lmOtcNrcInstallationOffwl + '\'' +
				", lmArcModemChargesOffwl='" + lmArcModemChargesOffwl + '\'' +
				", lmArcBWOffwl='" + lmArcBWOffwl + '\'' +
				", lmNrcProwOnwl='" + lmNrcProwOnwl + '\'' +
				", lmArcProwOnwl='" + lmArcProwOnwl + '\'' +
				", lmArcConverterChargesOnrf='" + lmArcConverterChargesOnrf + '\'' +
				", lmArcBwBackhaulOnrf='" + lmArcBwBackhaulOnrf + '\'' +
				", lmArcColocationChargesOnrf='" + lmArcColocationChargesOnrf + '\'' +
				", provider='" + provider + '\'' +
				", BHConnectivity='" + BHConnectivity + '\'' +
				", nonStandard='" + nonStandard + '\'' +
				", isCustomer='" + isCustomer + '\'' +
				", isColocated='" + isColocated + '\'' +
				", isDemo='" + isDemo + '\'' +
				", demoType='" + demoType + '\'' +
				'}';
		
	}

	public String getConcurrentSessions() {
		return concurrentSessions;
	}

	public void setConcurrentSessions(String concurrentSessions) {
		this.concurrentSessions = concurrentSessions;
	}

	public String getPbxType() {
		return pbxType;
	}

	public void setPbxType(String pbxType) {
		this.pbxType = pbxType;
	}

	public String getCubeLicenses() {
		return cubeLicenses;
	}

	public void setCubeLicenses(String cubeLicenses) {
		this.cubeLicenses = cubeLicenses;
	}

	public String getPvdmQuantities() {
		return pvdmQuantities;
	}

	public void setPvdmQuantities(String pvdmQuantities) {
		this.pvdmQuantities = pvdmQuantities;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getTypeOfConnectivity() {
		return typeOfConnectivity;
	}

	public void setTypeOfConnectivity(String typeOfConnectivity) {
		this.typeOfConnectivity = typeOfConnectivity;
	}

	public String getNoOfAdditionalIp() {
		return noOfAdditionalIp;
	}

	public void setNoOfAdditionalIp(String noOfAdditionalIp) {
		this.noOfAdditionalIp = noOfAdditionalIp;
	}
	
}

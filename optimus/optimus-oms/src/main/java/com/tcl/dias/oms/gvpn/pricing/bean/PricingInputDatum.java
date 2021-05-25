
package com.tcl.dias.oms.gvpn.pricing.bean;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * This file is used for pricing request
 * @author PAULRAJ SUNDAR
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "site_id", "latitude_final", "longitude_final", "prospect_name", "bw_mbps", "burstable_bw",
	"resp_city", "customer_segment", "sales_org", "product_name", "feasibility_response_created_date",
	"local_loop_interface", "last_mile_contract_term", "account_id_with_18_digit", "opportunity_term",
	"quotetype_quote", "connection_type", "sum_no_of_sites_uni_len", "cpe_variant", "cpe_management_type",
	"cpe_supply_type", "topology", "sum_onnet_flag", "sum_offnet_flag", "lm_arc_bw_onwl", "lm_nrc_bw_onwl",
	"lm_nrc_mux_onwl", "lm_nrc_inbldg_onwl", "lm_nrc_ospcapex_onwl", "lm_nrc_nerental_onwl", "lm_arc_bw_prov_ofrf",
	"lm_nrc_bw_prov_ofrf", "lm_nrc_mast_ofrf", "lm_arc_bw_onrf", "lm_nrc_bw_onrf", "lm_nrc_mast_onrf",
	"Orch_Connection", "Orch_LM_Type", "additional_ip_flag", "ip_address_arrangement", "ipv4_address_pool_size",
	"ipv6_address_pool_size",
	"backup_port_requested",
	"cu_le_id",
	"product_solution",
	"macd_service",
	"ll_change",
	"macd_option",
	"trigger_feasibility",
	"old_contract_term",
	"service_commissioned_date",
	"lat_long",
	"service_id",
	"backup_port_requested",
	"old_Port_Bw","old_Ll_Bw","parallel_run_days","prd_category","local_loop_bw",
	//Introduced for NPL 
	"Last_Modified_Date","BW_mbps_upd","DistanceBetweenPOPs","sum_model_name","model_name_transform","product_flavor_transform","country",
	"siteFlag",
	"partner_account_id_with_18_digit",
	"partner_profile",
	"quotetype_partner",
	"solution_type",
	"x_connect_commercialNote",
	"x_connect_isInterfaceChanged",
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
		"lm_arc_orderable_bw_onwl",
	    "lm_otc_nrc_orderable_bw_onwl",
	    "lm_nrc_network_capex_onwl",
	    "lm_arc_radwin_bw_onrf",
	    "non_standard","Orch_Category","vpn_Name","POP_DIST_KM_SERVICE_MOD","concurrent_sessions","pbx_type","cube_licenses","pvdm_quantities","product_code","type_of_connectivity"
	    ,
	    //CST-21 customer portal
	    "is_customer",
	  //added multivrf
		"multi_vrf_flag",
		"no_of_vrfs",
		"billing_type"
})
public class PricingInputDatum {
	
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
	@JsonProperty("country")
	private String country;
	@JsonProperty("siteFlag")
	private String siteFlag;
	@JsonProperty("old_contract_term")
	private String oldContractTerm;
	@JsonProperty("service_commissioned_date")
	private String serviceCommissionedDate;
	@JsonProperty("macd_option")
	private String macdOption;
	@JsonProperty("lat_long")
	private String latLong;
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
	@JsonProperty("old_Port_Bw")
	private String oldPortBw;
	@JsonProperty("old_Ll_Bw")
	private String oldLlBw;
	@JsonProperty("vpn_Name")
	private String vpnName;
	@JsonProperty("cu_le_id")
	private String cuLeId;
	@JsonProperty("parallel_run_days")
	private String parallelRunDays;
	@JsonProperty("prd_category")
	private String prd_category; 
	@JsonProperty("cpe_chassis_changed")
	private String cpeChassisChanged;
	@JsonProperty("local_loop_bw")
	private String localLoopBw;
	@JsonProperty("partner_account_id_with_18_digit")
	private String partnerAccountIdWith18Digit;
	@JsonProperty("partner_profile")
	private String partnerProfile;
	@JsonProperty("quotetype_partner")
	private String quoteTypePartner;
	@JsonProperty("solution_type")
	private String solutionType;
	@JsonProperty("x_connect_isInterfaceChanged")
	private String xconnectIsInterfaceChanged;
	@JsonProperty("x_connect_commercialNote")
    private String xConnectCommercialNote;
	@JsonProperty("deal_reg_flag")
	private String dealRegFlag;
	@JsonProperty("POP_DIST_KM_SERVICE_MOD")
	private String pOPDISTKMSERVICEMOD;
	
	public String getProductCategory() {
		return ProductCategory;
	}

	public void setProductCategory(String productCategory) {
		ProductCategory = productCategory;
	}

	@JsonProperty("prd_category")
    private String ProductCategory;

	public String getServiceCommissionedDate() {
		return serviceCommissionedDate;
	}

	@JsonProperty("lm_arc_orderable_bw_onwl")
	private String lmArcOrderableBwOnwl;
	@JsonProperty("lm_otc_nrc_orderable_bw_onwl")
    private String lmOtcNrcOrderableBwOnwl;
	@JsonProperty("lm_nrc_network_capex_onwl")
	private String lmNrcNetworkCapexOnwl;
	@JsonProperty("lm_arc_radwin_bw_onrf")
	private String lmArcRadwinBwOnrf;

	/*GSC-GVPN catalogue related attributes - starts */
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
	private String typeOfGscCpeConnectivity;
	/*GSC-GVPN catalogue related attributes - ends */
	
	@JsonProperty("non_standard")
	private String nonStandard;
	
	@JsonProperty("is_customer")
	private String isCustomer;

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
	
	@JsonProperty("lm_arc_orderable_bw_onwl")
	public String getLmArcOrderableBwOnwl() {
		return lmArcOrderableBwOnwl;
	}
	@JsonProperty("lm_arc_orderable_bw_onwl")
	public void setLmArcOrderableBwOnwl(String lmArcOrderableBwOnwl) {
		this.lmArcOrderableBwOnwl = lmArcOrderableBwOnwl;
	}
	@JsonProperty("lm_otc_nrc_orderable_bw_onwl")
	public String getLmOtcNrcOrderableBwOnwl() {
		return lmOtcNrcOrderableBwOnwl;
	}
	@JsonProperty("lm_otc_nrc_orderable_bw_onwl")
	public void setLmOtcNrcOrderableBwOnwl(String lmOtcNrcOrderableBwOnwl) {
		this.lmOtcNrcOrderableBwOnwl = lmOtcNrcOrderableBwOnwl;
	}
	@JsonProperty("lm_nrc_network_capex_onwl")
	public String getLmNrcNetworkCapexOnwl() {
		return lmNrcNetworkCapexOnwl;
	}
	@JsonProperty("lm_nrc_network_capex_onwl")
	public void setLmNrcNetworkCapexOnwl(String lmNrcNetworkCapexOnwl) {
		this.lmNrcNetworkCapexOnwl = lmNrcNetworkCapexOnwl;
	}
	@JsonProperty("lm_arc_radwin_bw_onrf")
	public String getLmArcRadwinBwOnrf() {
		return lmArcRadwinBwOnrf;
	}
	@JsonProperty("lm_arc_radwin_bw_onrf")
	public void setLmArcRadwinBwOnrf(String lmArcRadwinBwOnrf) {
		this.lmArcRadwinBwOnrf = lmArcRadwinBwOnrf;
	}

	@JsonProperty("x_connect_isInterfaceChanged")
    public String getXconnectIsInterfaceChanged() {
		return xconnectIsInterfaceChanged;
	}

	@JsonProperty("x_connect_isInterfaceChanged")
	public void setXconnectIsInterfaceChanged(String xconnectIsInterfaceChanged) {
		this.xconnectIsInterfaceChanged = xconnectIsInterfaceChanged;
	}

	
	@JsonProperty("x_connect_commercialNote")
    public String getxConnectCommercialNote() {
		return xConnectCommercialNote;
	}

    @JsonProperty("x_connect_commercialNote")
	public void setxConnectCommercialNote(String xConnectCommercialNote) {
		this.xConnectCommercialNote = xConnectCommercialNote;
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

	public String getPrd_category() {
		return prd_category;
	}

	@JsonProperty("prd_category")
	public void setPrd_category(String prd_category) {
		this.prd_category = prd_category;
	}



	@JsonProperty("parallel_run_days")
	public String getParallelRunDays() {
		return parallelRunDays;
	}

	@JsonProperty("parallel_run_days")
	public void setParallelRunDays(String parallelRunDays) {
		this.parallelRunDays = parallelRunDays;
	}

	// NPL related attributes - start
	@JsonProperty("Last_Modified_Date")
	private String lastModifiedDate;

	@JsonProperty("BW_mbps_upd")
	private String BWMbpsUpd;

	@JsonProperty("DistanceBetweenPOPs")
	private String distanceBetweenPOPs;

	@JsonProperty("sum_model_name")
	private String sumModelName;

	@JsonProperty("model_name_transform")
	private String modelNameTransform;

	@JsonProperty("product_flavor_transform")
	private String productFlavorTransform;

	@JsonProperty("product_solution")
	private String productSolution;
	
	@JsonProperty("multi_vrf_flag")
	private String multiVrfFlag;
	@JsonProperty("no_of_vrfs")
	private String noOfVrfs;
	@JsonProperty("billing_type")
	private String billingType;
	
	@JsonProperty("multi_vrf_flag")
	public String getMultiVrfFlag() {
	return multiVrfFlag;
	}

	@JsonProperty("multi_vrf_flag")
	public void setMultiVrfFlag(String multiVrfFlag) {
	this.multiVrfFlag = multiVrfFlag;
	}

	@JsonProperty("no_of_vrfs")
	public String getNoOfVrfs() {
	return noOfVrfs;
	}

	@JsonProperty("no_of_vrfs")
	public void setNoOfVrfs(String noOfVrfs) {
	this.noOfVrfs = noOfVrfs;
	}

	@JsonProperty("billing_type")
	public String getBillingType() {
	return billingType;
	}

	@JsonProperty("billing_type")
	public void setBillingType(String billingType) {
	this.billingType = billingType;
	}

	@JsonProperty("DistanceBetweenPOPs")
	public String getDistanceBetweenPOPs() {
		return distanceBetweenPOPs;
	}

	@JsonProperty("DistanceBetweenPOPs")
	public void setDistanceBetweenPOPs(String distanceBetweenPOPs) {
		this.distanceBetweenPOPs = distanceBetweenPOPs;
	}

	@JsonProperty("sum_model_name")
	public String getSumModelName() {
		return sumModelName;
	}

	@JsonProperty("sum_model_name")
	public void setSumModelName(String sumModelName) {
		this.sumModelName = sumModelName;
	}

	@JsonProperty("model_name_transform")
	public String getModelNameTransform() {
		return modelNameTransform;
	}

	@JsonProperty("model_name_transform")
	public void setModelNameTransform(String modelNameTransform) {
		this.modelNameTransform = modelNameTransform;
	}

	@JsonProperty("product_flavor_transform")
	public String getProductFlavorTransform() {
		return productFlavorTransform;
	}

	@JsonProperty("product_flavor_transform")
	public void setProductFlavorTransform(String productFlavorTransform) {
		this.productFlavorTransform = productFlavorTransform;
	}

	@JsonProperty("Last_Modified_Date")
	public String getLastModifiedDate() {
		return lastModifiedDate;
	}

	@JsonProperty("Last_Modified_Date")
	public void setLastModifiedDate(String lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	@JsonProperty("BW_mbps_upd")
	public String getBWMbpsUpd() {
		return BWMbpsUpd;
	}

	@JsonProperty("BW_mbps_upd")
	public void setBWMbpsUpd(String bWMbpsUpd) {
		BWMbpsUpd = bWMbpsUpd;
	}

	// NPL related attributes - end

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

	@JsonProperty("country")
	public String getCountry() {
		return country;
	}
	@JsonProperty("country")
	public void setCountry(String country) {
		this.country = country;
	}
	@JsonProperty("siteFlag")
	public String getSiteFlag() {
		return siteFlag;
	}
	@JsonProperty("siteFlag")
	public void setSiteFlag(String siteFlag) {
		this.siteFlag = siteFlag;
	}
	@JsonProperty("cu_le_id")
	public String getCuLeId() {
		return cuLeId;
	}
	@JsonProperty("cu_le_id")
	public void setCuLeId(String cuLeId) {
		this.cuLeId = cuLeId;
	}
	public String getProductSolution() {
		return productSolution;
	}

	public void setProductSolution(String productSolution) {
		this.productSolution = productSolution;
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
	
	@JsonProperty("vpn_Name")
	public String getVpnName() {
		return vpnName;
	}

	@JsonProperty("vpn_Name")
	public void setVpnName(String vpnName) {
		this.vpnName = vpnName;
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

	@JsonProperty("deal_reg_flag")
	public String getDealRegFlag() {
		return dealRegFlag;
	}

	@JsonProperty("deal_reg_flag")
	public void setDealRegFlag(String dealRegFlag) {
		this.dealRegFlag = dealRegFlag;
	}
	
	@JsonProperty("POP_DIST_KM_SERVICE_MOD")
	public String getpOPDISTKMSERVICEMOD() {
		return pOPDISTKMSERVICEMOD;
	}
	@JsonProperty("POP_DIST_KM_SERVICE_MOD")
	public void setpOPDISTKMSERVICEMOD(String pOPDISTKMSERVICEMOD) {
		this.pOPDISTKMSERVICEMOD = pOPDISTKMSERVICEMOD;
	}

		// for MF starts
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
		
		@JsonProperty("provider")
		public String getProvider() {
			return provider;
		}

		@JsonProperty("provider")
		public void setProvider(String provider) {
			this.provider = provider;
		}

		@JsonProperty("BHConnectivity")
		public String getBHConnectivity() {
			return BHConnectivity;
		}

		@JsonProperty("BHConnectivity")
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

		@JsonProperty("lm_otc_modem_charges_offwl")
		public String getLmOtcModemChargesOffwl() {
			return lmOtcModemChargesOffwl;
		}

		@JsonProperty("lm_otc_modem_charges_offwl")
		public void setLmOtcModemChargesOffwl(String lmOtcModemChargesOffwl) {
			this.lmOtcModemChargesOffwl = lmOtcModemChargesOffwl;
		}

		@JsonProperty("lm_otc_nrc_installation_offwl")
		public String getLmOtcNrcInstallationOffwl() {
			return lmOtcNrcInstallationOffwl;
		}

		@JsonProperty("lm_otc_nrc_installation_offwl")
		public void setLmOtcNrcInstallationOffwl(String lmOtcNrcInstallationOffwl) {
			this.lmOtcNrcInstallationOffwl = lmOtcNrcInstallationOffwl;
		}

		@JsonProperty("lm_arc_modem_charges_offwl")
		public String getLmArcModemChargesOffwl() {
			return lmArcModemChargesOffwl;
		}

		@JsonProperty("lm_arc_modem_charges_offwl")
		public void setLmArcModemChargesOffwl(String lmArcModemChargesOffwl) {
			this.lmArcModemChargesOffwl = lmArcModemChargesOffwl;
		}

		@JsonProperty("lm_arc_bw_offwl")
		public String getLmArcBWOffwl() {
			return lmArcBWOffwl;
		}

		@JsonProperty("lm_arc_bw_offwl")
		public void setLmArcBWOffwl(String lmArcBWOffwl) {
			this.lmArcBWOffwl = lmArcBWOffwl;
		}
		// for mf ends

		@JsonProperty("concurrent_sessions")
		public String getConcurrentSessions() {
			return concurrentSessions;
		}

		@JsonProperty("concurrent_sessions")
		public void setConcurrentSessions(String concurrentSessions) {
			this.concurrentSessions = concurrentSessions;
		}

		@JsonProperty("pbx_type")
		public String getPbxType() {
			return pbxType;
		}

		@JsonProperty("pbx_type")
		public void setPbxType(String pbxType) {
			this.pbxType = pbxType;
		}

		@JsonProperty("cube_licenses")
		public String getCubeLicenses() {
			return cubeLicenses;
		}

		@JsonProperty("cube_licenses")
		public void setCubeLicenses(String cubeLicenses) {
			this.cubeLicenses = cubeLicenses;
		}

		@JsonProperty("pvdm_quantities")
		public String getPvdmQuantities() {
			return pvdmQuantities;
		}

		@JsonProperty("pvdm_quantities")
		public void setPvdmQuantities(String pvdmQuantities) {
			this.pvdmQuantities = pvdmQuantities;
		}

		@JsonProperty("product_code")
		public String getProductCode() {
			return productCode;
		}

		@JsonProperty("product_code")
		public void setProductCode(String productCode) {
			this.productCode = productCode;
		}

		@JsonProperty("type_of_connectivity")
		public String getTypeOfGscCpeConnectivity() {
			return typeOfGscCpeConnectivity;
		}

		@JsonProperty("type_of_connectivity")
		public void setTypeOfGscCpeConnectivity(String typeOfGscCpeConnectivity) {
			this.typeOfGscCpeConnectivity = typeOfGscCpeConnectivity;
		}
		
		@JsonProperty("is_customer")
		public String getIsCustomer() {
			return isCustomer;
		}

		@JsonProperty("is_customer")
		public void setIsCustomer(String isCustomer) {
			this.isCustomer = isCustomer;
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
				"siteId='" + siteId + '\'' +
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
				", country='" + country + '\'' +
				", siteFlag='" + siteFlag + '\'' +
				", oldContractTerm='" + oldContractTerm + '\'' +
				", serviceCommissionedDate='" + serviceCommissionedDate + '\'' +
				", macdOption='" + macdOption + '\'' +
				", latLong='" + latLong + '\'' +
				", triggerFeasibility='" + triggerFeasibility + '\'' +
				", llChange='" + llChange + '\'' +
				", macdService='" + macdService + '\'' +
				", serviceId='" + serviceId + '\'' +
				", backupPortRequested='" + backupPortRequested + '\'' +
				", orchCategory='" + orchCategory + '\'' +
				", oldPortBw='" + oldPortBw + '\'' +
				", oldLlBw='" + oldLlBw + '\'' +
				", vpnName='" + vpnName + '\'' +
				", cuLeId='" + cuLeId + '\'' +
				", parallelRunDays='" + parallelRunDays + '\'' +
				", prd_category='" + prd_category + '\'' +
				", cpeChassisChanged='" + cpeChassisChanged + '\'' +
				", localLoopBw='" + localLoopBw + '\'' +
				", partnerAccountIdWith18Digit='" + partnerAccountIdWith18Digit + '\'' +
				", partnerProfile='" + partnerProfile + '\'' +
				", quoteTypePartner='" + quoteTypePartner + '\'' +
				", solutionType='" + solutionType + '\'' +
				", xconnectIsInterfaceChanged='" + xconnectIsInterfaceChanged + '\'' +
				", xConnectCommercialNote='" + xConnectCommercialNote + '\'' +
				", dealRegFlag='" + dealRegFlag + '\'' +
				", pOPDISTKMSERVICEMOD=" + pOPDISTKMSERVICEMOD +
				", lmArcOrderableBwOnwl='" + lmArcOrderableBwOnwl + '\'' +
				", lmOtcNrcOrderableBwOnwl='" + lmOtcNrcOrderableBwOnwl + '\'' +
				", lmNrcNetworkCapexOnwl='" + lmNrcNetworkCapexOnwl + '\'' +
				", lmArcRadwinBwOnrf='" + lmArcRadwinBwOnrf + '\'' +
				", concurrentSessions='" + concurrentSessions + '\'' +
				", pbxType='" + pbxType + '\'' +
				", cubeLicenses='" + cubeLicenses + '\'' +
				", pvdmQuantities=" + pvdmQuantities +
				", productCode='" + productCode + '\'' +
				", typeOfGscCpeConnectivity='" + typeOfGscCpeConnectivity + '\'' +
				", lastModifiedDate='" + lastModifiedDate + '\'' +
				", BWMbpsUpd='" + BWMbpsUpd + '\'' +
				", distanceBetweenPOPs='" + distanceBetweenPOPs + '\'' +
				", sumModelName='" + sumModelName + '\'' +
				", modelNameTransform='" + modelNameTransform + '\'' +
				", productFlavorTransform='" + productFlavorTransform + '\'' +
				", productSolution='" + productSolution + '\'' +
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
				", isCustomer='" + isCustomer + '\'' +
				'}';
	}
}

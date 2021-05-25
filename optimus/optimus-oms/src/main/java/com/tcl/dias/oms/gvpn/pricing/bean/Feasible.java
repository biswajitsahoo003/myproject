
package com.tcl.dias.oms.gvpn.pricing.bean;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
/**
 * This file is used to get the feasibility response
 * @author PAULRAJ SUNDAR
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
	"lm_nrc_ospcapex_onwl","parallel_run_days",
	"Service_ID",
	"hh_name",
	"total_cost",
	"X0.5km_avg_dist",
	"POP_DIST_KM",
	"X0.5km_prospect_avg_bw",
	"OnnetCity_tag",
	"lm_arc_bw_prov_ofrf",
	"Network_F_NF_CC",
	"lm_nrc_nerental_onwl",
	"lm_nrc_inbldg_onwl",
	"Network_Feasibility_Check",
	"core_check_CC",
	"X5km_min_bw",
	"access_check_CC",
	"rank",
	"POP_DIST_KM_SERVICE_MOD",
	"X5km_max_bw",
	"X5km_prospect_perc_feasible",
	"lm_arc_bw_onrf",
	"POP_Category",
	"lm_nrc_bw_prov_ofrf",
	"FATG_Building_Type",
	"HH_0_5_access_rings_F",
	"hh_flag",
	"local_loop_interface",
	"X5km_prospect_avg_dist",
	"latitude_final",
	"FATG_Network_Location_Type",
	"topology",
	"city_tier",
	"lm_nrc_bw_onwl",
	"X2km_prospect_min_dist",
	"X2km_prospect_perc_feasible",
	"X0.5km_prospect_num_feasible",
	"cpe_supply_type",
	"additional_ip_flag",
	"Orch_Connection",
	"site_id",
	"cpe_management_type",
	"sales_org",
	"opportunity_term",
	"error_flag",
	"resp_city",
	"connected_cust_tag",
	"net_pre_feasible_flag",
	"Network_F_NF_CC_Flag",
	"pop_long",
	"Orch_LM_Type",
	"ipv4_address_pool_size",
	"X5km_prospect_avg_bw",
	"FATG_Category",
	"lm_nrc_mast_ofrf",
	"X2km_prospect_min_bw",
	"X5km_prospect_num_feasible",
	"Selected",
	"pop_name",
	"lm_nrc_mast_onrf",
	"X5km_prospect_count",
	"error_msg",
	"connection_type",
	"ip_address_arrangement",
	"X0.5km_avg_bw",
	"X2km_cust_count",
	"customer_segment",
	"FATG_PROW",
	"time_taken",
	"connected_building_tag",
	"FATG_DIST_KM",
	"X0.5km_prospect_max_bw",
	"X5km_prospect_min_dist",
	"Type",
	"FATG_Ring_type",
	"longitude_final",
	"Orch_BW",
	"scenario_1",
	"scenario_2",
	"access_check_hh",
	"X2km_prospect_count",
	"ipv6_address_pool_size",
	"min_hh_fatg",
	"POP_Construction_Status",
	"X5km_avg_dist",
	"X2km_avg_dist",
	"X2km_prospect_avg_bw",
	"X0.5km_prospect_min_dist",
	"cost_permeter",
	"prospect_name",
	"Predicted_Access_Feasibility",
	"X2km_max_bw",
	"X2km_prospect_max_bw",
	"last_mile_contract_term",
	"X5km_prospect_min_bw",
	"X2km_min_bw",
	"burstable_bw",
	"pop_network_loc_id",
	"bw_mbps",
	"X0.5km_cust_count",
	"X0.5km_min_bw",
	"product_name",
	"Network_F_NF_HH",
	"HH_0_5_access_rings_NF",
	"X0.5km_min_dist",
	"POP_Building_Type",
	"core_check_hh",
	"HH_DIST_KM",
	"X0.5km_prospect_min_bw",
	"error_code",
	"POP_DIST_KM_SERVICE",
	"lm_arc_bw_onwl",
	"num_connected_building",
	"account_id_with_18_digit",
	"Orch_Category",
	"X0.5km_prospect_avg_dist",
	"POP_Network_Location_Type",
	"lm_nrc_mux_onwl",
	"num_connected_cust",
	"HH_0_5km",
	"pop_lat",
	"X5km_min_dist",
	"feasibility_response_created_date",
	"X2km_prospect_num_feasible",
	"X2km_avg_bw",
	"X0.5km_max_bw",
	"X5km_avg_bw",
	"X5km_prospect_max_bw",
	"X2km_prospect_avg_dist",
	"cpe_variant",
	"X5km_cust_count",
	"sum_no_of_sites_uni_len",
	"X0.5km_prospect_perc_feasible",
	"POP_TCL_Access",
	"X0.5km_prospect_count",
	"Probabililty_Access_Feasibility",
	"lm_nrc_bw_onrf",
	"quotetype_quote",
	"pop_address",
	"X2km_min_dist",
	"FATG_TCL_Access",
	"Network_F_NF_HH_Flag",
	"closest_provider_site_addr",
	"otc_tikona",
	"closest_provider_site",
	"offnet_5km_Min_accuracy_num",
	"interim_BW",
	"offnet_5km_Min_DistanceKilometers",
	"offnet_0_5km_Max_BW_Mbps",
	"prospect_0_5km_Min_BW_Mbps",
	"bw_flag_32",
	"arc_tikona",
	"min_mast_ht",
	"offnet_2km_Min_DistanceKilometers",
	"prospect_2km_feasibility_pct",
	"bw_flag_3",
	"offnet_2km_cust_Count",
	"prospect_2km_Avg_DistanceKilometers",
	"offnet_5km_Max_BW_Mbps",
	"prospect_2km_Sum_Feasibility_flag",
	"arc_sify",
	"prospect_2km_Avg_BW_Mbps",
	"max_mast_ht",
	"provider_tot_towers",
	"offnet_2km_Min_BW_Mbps",
	"offnet_0_5km_Min_BW_Mbps",
	"offnet_5km_Avg_BW_Mbps",
	"prospect_0_5km_Sum_Feasibility_flag",
	"prospect_2km_cust_Count",
	"prospect_2km_Max_BW_Mbps",
	"otc_sify",
	"provider_min_dist",
	"offnet_5km_Min_BW_Mbps",
	"avg_mast_ht",
	"prospect_0_5km_feasibility_pct",
	"prospect_0_5km_Max_BW_Mbps",
	"offnet_0_5km_Min_accuracy_num",
	"closest_provider",
	"nearest_mast_ht_cost",
	"offnet_2km_Avg_BW_Mbps",
	"offnet_2km_Min_accuracy_num",
	"cust_count",
	"offnet_0_5km_Avg_BW_Mbps",
	"closest_provider_bso_name",
	"prospect_0_5km_Min_DistanceKilometers",
	"nearest_mast_ht",
	"prospect_2km_Min_BW_Mbps",
	"offnet_0_5km_Min_DistanceKilometers",
	"offnet_0_5km_cust_Count",
	"prospect_0_5km_Avg_BW_Mbps",
	"country",
	"siteFlag",
	"backup_port_requested","cu_le_id",
	"Mast_3KM_avg_mast_ht",
	"solution_type",
	"P2P_bts_3km_radius",
	"PMP_bts_3km_radius",
	"bts_min_dist_km",
	"bts_num_BTS","product_solution","macd_service",
	"ll_change",
	"macd_option",
	"trigger_feasibility",
	"old_contract_term",
	"service_commissioned_date",
	"lat_long",
	"service_id",
	"backup_port_requested",
	"old_Port_Bw","old_Ll_Bw",
	"vpn_Name",
	"cpe_chassis_changed",
	"local_loop_bw",
	"partner_account_id_with_18_digit",
	"partner_profile",
	"quotetype_partner",
	
	"mux_ip",
	"mux_port",
	"selected_access_ring",
	"bts_lat",
	"bts_long",
	"bts_site_name",
	"BH_Capacity",
	"BH_Type",
	"BHConnectivity",
	"bts_device_type",
	"bts_azimuth",
	"sector_id",
	"SECTOR_NAME",
	"version",
	"error_msg_display",
	"selected_access_rings",
	"bw_selected_access_rings",
	"bw_selected_access_ring",
	"x_connect_commercialNote",
	"x_connect_isInterfaceChanged",

	"GVPN_Port_MRC_Adjusted",
	"GVPN_Port_NRC_Adjusted",
	"CPE_Installation_charges",
	"support_charges",
	"recovery",
	"management",
	"SFP_lp",
	"customs_local_taxes",
	"Logistics_cost",
	"provider_MRCCost",
	"provider_NRCCost",
	"x_connect_Xconnect_MRC",
	"x_connect_Xconnect_NRC",
	"izo_sdwan",



 // for MF
 	
	"otc_modem_charges",
	"lm_nrc_bw_prov_ofwl",
	"arc_modem_charges",
	"arc_bw",
	"lm_nrc_prow_onwl",
	"lm_arc_prow_onwl",
	"lm_arc_converter_charges_onrf",
	"lm_arc_bw_backhaul_onrf",
	"lm_arc_colocation_onrf",
	"lm_otc_modem_charges_offwl",
	"lm_otc_nrc_installation_offwl",
	"lm_arc_modem_charges_offwl",
	"lm_arc_bw_offwl",
	"provider_name",
	
	"lm_arc_orderable_bw_onwl",
    "lm_otc_nrc_orderable_bw_onwl",
    "lm_nrc_network_capex_onwl",
    "lm_arc_radwin_bw_onrf",
	// Additional Attributes for System Feasibility response - Onnet Wireline.
	"prow_cost_type",
	"prow_message",
	"prow_flag",
	"lm_arc_prow_gpon_ownl",
	"lm_nrc_prow_gpon_onwl",
	"lm_arc_prow_prow_onwl",
	"lm_nrc_prow_prow_onwl",
	"lm_arc_prow_onwl",
	"lm_nrc_prow_onwl",
	//Mandatory Manual PO Attributes
	
	"vendor_name",
	"vendor_id",
	"provider_reference_number",
	"tcl_pop_address",

		//CST-21 customer portal
		"is_customer",


		//PIPF-145
		"grc_version_name","bts_sector_id","bts_sector_name","bts_tagged_comment",

		// PIPF152
		"feasibility_response_id",
		"task_id",
		
		//added multivrf
		"multi_vrf_flag",
		"no_of_vrfs",
		"billing_type",
				
		//Planned Cost Table for Auto Budget WBS allocation
		"lm_arc_colocation_charges_onrf",
		"cpe_hw_ctc",
		"cpe_installation_ctc",
		"cpe_support_ctc"

})
public class Feasible {

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
	@JsonProperty("old_Port_Bw")
	private String oldPortBw;
	@JsonProperty("old_Ll_Bw")
	private String oldLlBw;
	@JsonProperty("vpn_Name")
	private String vpnName;
	@JsonProperty("lm_nrc_ospcapex_onwl")
	private String lmNrcOspcapexOnwl;
	@JsonProperty("Service_ID")
	private Integer serviceID;
	@JsonProperty("hh_name")
	private String hhName;
	@JsonProperty("total_cost")
	private String totalCost;
	@JsonProperty("X0.5km_avg_dist")
	private Integer x05kmAvgDist;
	@JsonProperty("POP_DIST_KM")
	private Float pOPDISTKM;
	@JsonProperty("X0.5km_prospect_avg_bw")
	private Float x05kmProspectAvgBw;
	@JsonProperty("OnnetCity_tag")
	private Integer onnetCityTag;
	@JsonProperty("lm_arc_bw_prov_ofrf")
	private Integer lmArcBwProvOfrf;
	@JsonProperty("Network_F_NF_CC")
	private String networkFNFCC;
	@JsonProperty("lm_nrc_nerental_onwl")
	private Integer lmNrcNerentalOnwl;
	@JsonProperty("lm_nrc_inbldg_onwl")
	private Integer lmNrcInbldgOnwl;
	@JsonProperty("Network_Feasibility_Check")
	private String networkFeasibilityCheck;
	@JsonProperty("core_check_CC")
	private String coreCheckCC;
	@JsonProperty("X5km_min_bw")
	private Integer x5kmMinBw;
	@JsonProperty("access_check_CC")
	private String accessCheckCC;
	@JsonProperty("rank")
	private Integer rank;
	@JsonProperty("POP_DIST_KM_SERVICE_MOD")
	private Integer pOPDISTKMSERVICEMOD;
	@JsonProperty("X5km_max_bw")
	private Integer x5kmMaxBw;
	@JsonProperty("X5km_prospect_perc_feasible")
	private Float x5kmProspectPercFeasible;
	@JsonProperty("lm_arc_bw_onrf")
	private Integer lmArcBwOnrf;
	@JsonProperty("POP_Category")
	private String pOPCategory;
	@JsonProperty("lm_nrc_bw_prov_ofrf")
	private Integer lmNrcBwProvOfrf;
	@JsonProperty("FATG_Building_Type")
	private String fATGBuildingType;
	@JsonProperty("HH_0_5_access_rings_F")
	private String hH05AccessRingsF;
	@JsonProperty("hh_flag")
	private String hhFlag;
	@JsonProperty("local_loop_interface")
	private String localLoopInterface;
	@JsonProperty("X5km_prospect_avg_dist")
	private Float x5kmProspectAvgDist;
	@JsonProperty("latitude_final")
	private Float latitudeFinal;
	@JsonProperty("FATG_Network_Location_Type")
	private String fATGNetworkLocationType;
	@JsonProperty("topology")
	private String topology;
	@JsonProperty("city_tier")
	private String cityTier;
	@JsonProperty("lm_nrc_bw_onwl")
	private Integer lmNrcBwOnwl;
	@JsonProperty("X2km_prospect_min_dist")
	private Float x2kmProspectMinDist;
	@JsonProperty("X2km_prospect_perc_feasible")
	private Float x2kmProspectPercFeasible;
	@JsonProperty("X0.5km_prospect_num_feasible")
	private Integer x05kmProspectNumFeasible;
	@JsonProperty("cpe_supply_type")
	private String cpeSupplyType;
	@JsonProperty("additional_ip_flag")
	private String additionalIpFlag;
	@JsonProperty("Orch_Connection")
	private String orchConnection;
	@JsonProperty("site_id")
	private String siteId;
	@JsonProperty("cpe_management_type")
	private String cpeManagementType;
	@JsonProperty("sales_org")
	private String salesOrg;
	@JsonProperty("opportunity_term")
	private Integer opportunityTerm;
	@JsonProperty("error_flag")
	private Integer errorFlag;
	@JsonProperty("resp_city")
	private String respCity;
	@JsonProperty("connected_cust_tag")
	private Integer connectedCustTag;
	@JsonProperty("net_pre_feasible_flag")
	private Integer netPreFeasibleFlag;
	@JsonProperty("Network_F_NF_CC_Flag")
	private String networkFNFCCFlag;
	@JsonProperty("pop_long")
	private Float popLong;
	@JsonProperty("Orch_LM_Type")
	private String orchLMType;
	@JsonProperty("ipv4_address_pool_size")
	private String ipv4AddressPoolSize;
	@JsonProperty("X5km_prospect_avg_bw")
	private Float x5kmProspectAvgBw;
	@JsonProperty("FATG_Category")
	private String fATGCategory;
	@JsonProperty("lm_nrc_mast_ofrf")
	private Float lmNrcMastOfrf;
	@JsonProperty("X2km_prospect_min_bw")
	private Integer x2kmProspectMinBw;
	@JsonProperty("X5km_prospect_num_feasible")
	private Integer x5kmProspectNumFeasible;
	@JsonProperty("Selected")
	private Boolean selected;
	@JsonProperty("pop_name")
	private String popName;
	@JsonProperty("lm_nrc_mast_onrf")
	private Integer lmNrcMastOnrf;
	@JsonProperty("X5km_prospect_count")
	private Integer x5kmProspectCount;
	@JsonProperty("error_msg")
	private String errorMsg;
	@JsonProperty("connection_type")
	private String connectionType;
	@JsonProperty("ip_address_arrangement")
	private String ipAddressArrangement;
	@JsonProperty("X0.5km_avg_bw")
	private Integer x05kmAvgBw;
	@JsonProperty("X2km_cust_count")
	private Integer x2kmCustCount;
	@JsonProperty("customer_segment")
	private String customerSegment;
	@JsonProperty("FATG_PROW")
	private String fATGPROW;
	@JsonProperty("time_taken")
	private Float timeTaken;
	@JsonProperty("connected_building_tag")
	private Integer connectedBuildingTag;
	@JsonProperty("FATG_DIST_KM")
	private Float fATGDISTKM;
	@JsonProperty("X0.5km_prospect_max_bw")
	private Integer x05kmProspectMaxBw;
	@JsonProperty("X5km_prospect_min_dist")
	private Float x5kmProspectMinDist;
	@JsonProperty("Type")
	private String type;
	@JsonProperty("FATG_Ring_type")
	private String fATGRingType;
	@JsonProperty("longitude_final")
	private Float longitudeFinal;
	@JsonProperty("Orch_BW")
	private Integer orchBW;
	@JsonProperty("scenario_1")
	private Integer scenario1;
	@JsonProperty("scenario_2")
	private Integer scenario2;
	@JsonProperty("access_check_hh")
	private String accessCheckHh;
	@JsonProperty("X2km_prospect_count")
	private Integer x2kmProspectCount;
	@JsonProperty("ipv6_address_pool_size")
	private String ipv6AddressPoolSize;
	@JsonProperty("min_hh_fatg")
	private Float minHhFatg;
	@JsonProperty("POP_Construction_Status")
	private String pOPConstructionStatus;
	@JsonProperty("X5km_avg_dist")
	private Float x5kmAvgDist;
	@JsonProperty("X2km_avg_dist")
	private Float x2kmAvgDist;
	@JsonProperty("X2km_prospect_avg_bw")
	private Float x2kmProspectAvgBw;
	@JsonProperty("X0.5km_prospect_min_dist")
	private Float x05kmProspectMinDist;
	@JsonProperty("cost_permeter")
	private Integer costPermeter;
	@JsonProperty("prospect_name")
	private String prospectName;
	@JsonProperty("Predicted_Access_Feasibility")
	private String predictedAccessFeasibility;
	@JsonProperty("X2km_max_bw")
	private Integer x2kmMaxBw;
	@JsonProperty("X2km_prospect_max_bw")
	private Integer x2kmProspectMaxBw;
	@JsonProperty("last_mile_contract_term")
	private String lastMileContractTerm;
	@JsonProperty("X5km_prospect_min_bw")
	private Float x5kmProspectMinBw;
	@JsonProperty("X2km_min_bw")
	private Integer x2kmMinBw;
	@JsonProperty("burstable_bw")
	private Double burstableBw;
	@JsonProperty("pop_network_loc_id")
	private String popNetworkLocId;
	@JsonProperty("bw_mbps")
	private Double bwMbps;
	@JsonProperty("X0.5km_cust_count")
	private Integer x05kmCustCount;
	@JsonProperty("X0.5km_min_bw")
	private Integer x05kmMinBw;
	@JsonProperty("product_name")
	private String productName;
	@JsonProperty("Network_F_NF_HH")
	private String networkFNFHH;
	@JsonProperty("HH_0_5_access_rings_NF")
	private String hH05AccessRingsNF;
	@JsonProperty("X0.5km_min_dist")
	private Integer x05kmMinDist;
	@JsonProperty("POP_Building_Type")
	private String pOPBuildingType;
	@JsonProperty("core_check_hh")
	private String coreCheckHh;
	@JsonProperty("HH_DIST_KM")
	private Float hHDISTKM;
	@JsonProperty("X0.5km_prospect_min_bw")
	private Integer x05kmProspectMinBw;
	@JsonProperty("error_code")
	private String errorCode;
	@JsonProperty("POP_DIST_KM_SERVICE")
	private Float pOPDISTKMSERVICE;
	@JsonProperty("lm_arc_bw_onwl")
	private Integer lmArcBwOnwl;
	@JsonProperty("num_connected_building")
	private Integer numConnectedBuilding;
	@JsonProperty("account_id_with_18_digit")
	private String accountIdWith18Digit;
	@JsonProperty("Orch_Category")
	private String orchCategory;
	@JsonProperty("X0.5km_prospect_avg_dist")
	private Float x05kmProspectAvgDist;
	@JsonProperty("POP_Network_Location_Type")
	private String pOPNetworkLocationType;
	@JsonProperty("lm_nrc_mux_onwl")
	private Integer lmNrcMuxOnwl;
	@JsonProperty("num_connected_cust")
	private Integer numConnectedCust;
	@JsonProperty("HH_0_5km")
	private String hH05km;
	@JsonProperty("pop_lat")
	private Float popLat;
	@JsonProperty("X5km_min_dist")
	private Float x5kmMinDist;
	@JsonProperty("feasibility_response_created_date")
	private String feasibilityResponseCreatedDate;
	@JsonProperty("X2km_prospect_num_feasible")
	private Integer x2kmProspectNumFeasible;
	@JsonProperty("X2km_avg_bw")
	private Integer x2kmAvgBw;
	@JsonProperty("X0.5km_max_bw")
	private Integer x05kmMaxBw;
	@JsonProperty("X5km_avg_bw")
	private Integer x5kmAvgBw;
	@JsonProperty("X5km_prospect_max_bw")
	private Integer x5kmProspectMaxBw;
	@JsonProperty("X2km_prospect_avg_dist")
	private Float x2kmProspectAvgDist;
	@JsonProperty("cpe_variant")
	private String cpeVariant;
	@JsonProperty("X5km_cust_count")
	private Integer x5kmCustCount;
	@JsonProperty("sum_no_of_sites_uni_len")
	private Integer sumNoOfSitesUniLen;
	@JsonProperty("X0.5km_prospect_perc_feasible")
	private Integer x05kmProspectPercFeasible;
	@JsonProperty("POP_TCL_Access")
	private String pOPTCLAccess;
	@JsonProperty("X0.5km_prospect_count")
	private Integer x05kmProspectCount;
	@JsonProperty("Probabililty_Access_Feasibility")
	private Float probabililtyAccessFeasibility;
	@JsonProperty("lm_nrc_bw_onrf")
	private Integer lmNrcBwOnrf;
	@JsonProperty("quotetype_quote")
	private String quotetypeQuote;
	@JsonProperty("pop_address")
	private String popAddress;
	@JsonProperty("X2km_min_dist")
	private Float x2kmMinDist;
	@JsonProperty("FATG_TCL_Access")
	private String fATGTCLAccess;
	@JsonProperty("Network_F_NF_HH_Flag")
	private String networkFNFHHFlag;
	@JsonProperty("closest_provider_site_addr")
	private String closestProviderSiteAddr;
	@JsonProperty("otc_tikona")
	private Integer otcTikona;
	@JsonProperty("closest_provider_site")
	private String closestProviderSite;
	@JsonProperty("offnet_5km_Min_accuracy_num")
	private Integer offnet5kmMinAccuracyNum;
	@JsonProperty("interim_BW")
	private Integer interimBW;
	@JsonProperty("offnet_5km_Min_DistanceKilometers")
	private Float offnet5kmMinDistanceKilometers;
	@JsonProperty("offnet_0_5km_Max_BW_Mbps")
	private Integer offnet05kmMaxBWMbps;
	@JsonProperty("prospect_0_5km_Min_BW_Mbps")
	private Integer prospect05kmMinBWMbps;
	@JsonProperty("bw_flag_32")
	private Integer bwFlag32;
	@JsonProperty("arc_tikona")
	private Integer arcTikona;
	@JsonProperty("min_mast_ht")
	private Integer minMastHt;
	@JsonProperty("offnet_2km_Min_DistanceKilometers")
	private Float offnet2kmMinDistanceKilometers;
	@JsonProperty("prospect_2km_feasibility_pct")
	private Float prospect2kmFeasibilityPct;
	@JsonProperty("bw_flag_3")
	private Integer bwFlag3;
	@JsonProperty("offnet_2km_cust_Count")
	private Integer offnet2kmCustCount;
	@JsonProperty("prospect_2km_Avg_DistanceKilometers")
	private Float prospect2kmAvgDistanceKilometers;
	@JsonProperty("offnet_5km_Max_BW_Mbps")
	private Integer offnet5kmMaxBWMbps;
	@JsonProperty("prospect_2km_Sum_Feasibility_flag")
	private Integer prospect2kmSumFeasibilityFlag;
	@JsonProperty("arc_sify")
	private Integer arcSify;
	@JsonProperty("prospect_2km_Avg_BW_Mbps")
	private Float prospect2kmAvgBWMbps;
	@JsonProperty("max_mast_ht")
	private Integer maxMastHt;
	@JsonProperty("provider_tot_towers")
	private Integer providerTotTowers;
	@JsonProperty("offnet_2km_Min_BW_Mbps")
	private Float offnet2kmMinBWMbps;
	@JsonProperty("offnet_0_5km_Min_BW_Mbps")
	private Integer offnet05kmMinBWMbps;
	@JsonProperty("offnet_5km_Avg_BW_Mbps")
	private Float offnet5kmAvgBWMbps;
	@JsonProperty("prospect_0_5km_Sum_Feasibility_flag")
	private Integer prospect05kmSumFeasibilityFlag;
	@JsonProperty("prospect_2km_cust_Count")
	private Integer prospect2kmCustCount;
	@JsonProperty("prospect_2km_Max_BW_Mbps")
	private Integer prospect2kmMaxBWMbps;
	@JsonProperty("otc_sify")
	private Integer otcSify;
	@JsonProperty("provider_min_dist")
	private Float providerMinDist;
	@JsonProperty("offnet_5km_Min_BW_Mbps")
	private Float offnet5kmMinBWMbps;
	@JsonProperty("avg_mast_ht")
	private Float avgMastHt;
	@JsonProperty("prospect_0_5km_feasibility_pct")
	private Integer prospect05kmFeasibilityPct;
	@JsonProperty("prospect_0_5km_Max_BW_Mbps")
	private Integer prospect05kmMaxBWMbps;
	@JsonProperty("offnet_0_5km_Min_accuracy_num")
	private Integer offnet05kmMinAccuracyNum;
	@JsonProperty("closest_provider")
	private String closestProvider;
	@JsonProperty("nearest_mast_ht_cost")
	private Integer nearestMastHtCost;
	@JsonProperty("offnet_2km_Avg_BW_Mbps")
	private Float offnet2kmAvgBWMbps;
	@JsonProperty("offnet_2km_Min_accuracy_num")
	private Integer offnet2kmMinAccuracyNum;
	@JsonProperty("cust_count")
	private Integer custCount;
	@JsonProperty("offnet_0_5km_Avg_BW_Mbps")
	private Integer offnet05kmAvgBWMbps;
	@JsonProperty("closest_provider_bso_name")
	private String closestProviderBsoName;
	@JsonProperty("prospect_0_5km_Min_DistanceKilometers")
	private Integer prospect05kmMinDistanceKilometers;
	@JsonProperty("nearest_mast_ht")
	private Integer nearestMastHt;
	@JsonProperty("prospect_2km_Min_BW_Mbps")
	private Float prospect2kmMinBWMbps;
	@JsonProperty("offnet_0_5km_Min_DistanceKilometers")
	private Integer offnet05kmMinDistanceKilometers;
	@JsonProperty("offnet_0_5km_cust_Count")
	private Integer offnet05kmCustCount;
	@JsonProperty("prospect_0_5km_Avg_BW_Mbps")
	private Integer prospect05kmAvgBWMbps;
	@JsonProperty("country")
	private String country;
	@JsonProperty("siteFlag")
	private String siteFlag;
	@JsonProperty("backup_port_requested")
	private String backupPortRequested;
	@JsonProperty("cu_le_id")
	private String cuLeId;
	@JsonProperty("Mast_3KM_avg_mast_ht")
	private String mast3KMAvgMastHt;
	@JsonProperty("solution_type")
	private String solutionType;
	@JsonProperty("P2P_bts_3km_radius")
	private String p2PBts3kmRadius;
	@JsonProperty("PMP_bts_3km_radius")
	private String pMPBts3kmRadius;
	@JsonProperty("bts_min_dist_km")
	private String btsMinDistKm;
	@JsonProperty("bts_num_BTS")
	private String btsNumBTS;
	@JsonProperty("Sector_network_check")
	private String sectorNetworkCheck;
	@JsonProperty("Selected_solution_BW")
	private String selectedSolutionBw;
	@JsonProperty("Product.Name")
	private String productDotNme;
	@JsonProperty("onnet_0_5km_Avg_BW_Mbps")
	private String onnet0_5kmAvgBwMbps;
	@JsonProperty("onnet_0_5km_cust_Count")
	private String onnet0_5kmCustCount;
	@JsonProperty("onnet_0_5km_Min_DistanceKilometers")
	private String onnet0_5kmMinDistanceKilometers;
	@JsonProperty("onnet_2km_Avg_BW_Mbps")
	private String onnet2kmAvgBwMbps;
	@JsonProperty("onnet_2km_Avg_DistanceKilometers")
	private String onnet2kmAvgDistancekilometers;
	@JsonProperty("onnet_2km_cust_Count")
	private String onnet2kmCustCount;
	@JsonProperty("onnet_2km_Max_BW_Mbps")
	private String onnet2kmMaxBwMbps;
	@JsonProperty("onnet_2km_Min_BW_Mbps")
	private String onnet2kmMinBwMbps;
	@JsonProperty("onnet_5km_Avg_BW_Mbps")
	private String onnet5kmAvgBwMbps;
	@JsonProperty("onnet_5km_Avg_DistanceKilometers")
	private String onnet5kmAvgDistanceKilometers;
	@JsonProperty("onnet_5km_cust_Count")
	private String onnet5kmCustCount;
	@JsonProperty("onnet_5km_Max_BW_Mbps")
	private String onnet5kmMaxBwMbps;
	@JsonProperty("onnet_5km_Min_BW_Mbps")
	private String onnet5kmMinBwMbps;
	@JsonProperty("mux")
	private String mux;
	@JsonProperty("Mast_3KM_cust_count")
	private String mast3kmCustCount;
	@JsonProperty("Mast_3KM_max_mast_ht")
	private String mast3kmMaxMastHt;
	@JsonProperty("Mast_3KM_min_mast_ht")
	private String mast3kmMinMastHt;
	@JsonProperty("Mast_3KM_nearest_mast_ht")
	private String mast3kmNearestMastHt;
	@JsonProperty("LM_OTC_INR")
	private String lmOtcInr;
	@JsonProperty("LM_ARC_INR")
	private String lmArcInr;
	@JsonProperty("LM_Cost")
	private String lmCost;
	@JsonProperty("hub_node")
	private String hubNode;
	@JsonProperty("Interface")
	private String intrface;
	@JsonProperty("core_ring")
	private String coreRing;
	@JsonProperty("Bandwidth")
	private String bandwidth;
	@JsonProperty("bts_Closest_infra_provider")
	private String btsClosestInfraProvider;
	@JsonProperty("bts_Closest_Site_type")
	private String btsClosestSiteType;
	@JsonProperty("bts_flag")
	private String btsFlag;
	@JsonProperty("bts_IP_PMP")
	private String btsIpPmp;
	@JsonProperty("Backhaul_Network_check")
	private String backhaulNetworkCheck;
	@JsonProperty("Backhaul_Network_check_reason")
	private String backhaulNetworkCheckReason;
	@JsonProperty("Avg_3KM_Mast_ht_cost")
	private String avg3kmMastHtCost;
	@JsonProperty("access_rings_hh")
	private String accessRingsHh;
	@JsonProperty("product_solution")
	private String productSolution;
	@JsonProperty("cpe_chassis_changed")
	private String cpeChassisChanged;
	@JsonProperty("local_loop_bw")
	private Double localLoopBw;
	@JsonProperty("partner_account_id_with_18_digit")
	private String partnerAccountIdWith18Digit;
	@JsonProperty("partner_profile")
	private String partnerProfile;
	@JsonProperty("quotetype_partner")
	private String quoteTypePartner;
	
	@JsonProperty("mux_ip")
	private String muxIp;
	@JsonProperty("mux_port")
	private String muxPort;
	@JsonProperty("selected_access_ring")
	private String selectedAccessRing;
	@JsonProperty("bts_lat")
	private String btsLat;
	@JsonProperty("bts_long")
	private String btsLong;
	@JsonProperty("bts_site_name")
	private String btsSiteName;
	@JsonProperty("BH_Capacity")
	private String BHCapacity;
	@JsonProperty("BH_Type")
	private String BH_Type;
	@JsonProperty("BHConnectivity")
	private String BHConnectivity;
	@JsonProperty("bts_device_type")
	private String btsDeviceType;
	@JsonProperty("bts_azimuth")
	private String btsAzimuth;
	
	@JsonProperty("sector_id")
	private String sectorId;
	@JsonProperty("SECTOR_NAME")
	private String SECTORNAME;
	
	@JsonProperty("version")
	private String version;
	

	@JsonProperty("error_msg_display")
	private String errorMsgDisplay;

	@JsonProperty("GVPN_Port_MRC_Adjusted")
	private String gvpnPortMrcAdjusted;

	@JsonProperty("GVPN_Port_NRC_Adjusted")
	private String gvpnPortNrcAdjusted;

	@JsonProperty("CPE_Installation_charges")
	private String cpeInstallationCharges;

	@JsonProperty("support_charges")
	private String supportCharges;

	@JsonProperty("recovery")
	private String recovery;

	@JsonProperty("management")
	private String management;

	@JsonProperty("SFP_lp")
	private String sfpIp;

	@JsonProperty("customs_local_taxes")
	private String customsLocalTaxes;

	@JsonProperty("Logistics_cost")
	private String logisticsCost;

	@JsonProperty("provider_MRCCost")
	private String providerMrcCost;

	@JsonProperty("provider_NRCCost")
	private String providerNrcCost;

	@JsonProperty("x_connect_Xconnect_MRC")
	private String xConnectXconnectMrc;

	@JsonProperty("x_connect_Xconnect_NRC")
	private String xConnectXconnectNrc;
	
	@JsonProperty("izo_sdwan")
	private String izoSdwan;
	
	public String getIzoSdwan() {
		return izoSdwan;
	}
	@JsonProperty("lm_arc_orderable_bw_onwl")
	private String lmArcOrderableBwOnwl;
	@JsonProperty("lm_otc_nrc_orderable_bw_onwl")
    private String lmOtcNrcOrderableBwOnwl;
	@JsonProperty("lm_nrc_network_capex_onwl")
	private String lmNrcNetworkCapexOnwl;
	@JsonProperty("lm_arc_radwin_bw_onrf")
	private String lmArcRadwinBwOnrf;
	
	
	//Additional Attributes for system response start
	
		@JsonProperty("prow_cost_type")
		private String prowCostType;
		
		@JsonProperty("prow_message")
		private String ProwMessage;
		
		
		@JsonProperty("prow_flag")
		private String prowFlag;
		
		@JsonProperty("lm_arc_prow_gpon_onwl")
		private String lmArcProwGponOnwl;
		
		@JsonProperty("lm_nrc_prow_gpon_onwl")
		private String lmNrcProwGponOnwl;
		
		@JsonProperty("lm_arc_prow_prow_onwl")
		private String lmArcProwProwOnwl;
		
		@JsonProperty("lm_nrc_prow_prow_onwl")
		private String lmNrcProwProwOnwl;
		//Additional Attributes for system response end
		
		
		@JsonProperty("primary_bts_ip_address")
		private String primaryBtsIpAddress;
		@JsonProperty("primary_bts_name")
		private String primaryBtsName;
		@JsonProperty("primary_first_sector_id")
		private String primaryFirstSectorId;
		@JsonProperty("delivery_timeLine")
		private String deliveryTimeLine;
		@JsonProperty("feasibility_remarks")
		private String feasibilityRemarks;
		@JsonProperty("response_type")
		private String responseType;
		@JsonProperty("validity_period")
		private String validityPeriod;
		@JsonProperty("feasibility_response_id")
		private String feasibilityResponseId;
		@JsonProperty("provider_response_date")
		private String providerResponseDate;
		@JsonProperty("resp_state")
		private String respState;	
		
		@JsonProperty("bts_equipment_type")
		private String btsEquipmentType;
		@JsonProperty("bts_ht")
		private String btsHt;
		@JsonProperty("bts_site_address")
		private String btsSiteAddress;
		
		// added for PIPF -152
		@JsonProperty("task_id")
		private String taskId;
		
		//Planned Cost Table for Auto Budget WBS allocation
		@JsonProperty("lm_arc_colocation_charges_onrf")
		private String lmArcColocationChargesOnrf;
		
		@JsonProperty("cpe_hw_ctc")
		private String cpeHwCtc;
		
		@JsonProperty("cpe_installation_ctc")
		private String cpeInstallationCtc;
		
		@JsonProperty("cpe_support_ctc")
		private String cpeSupportCtc;
		
		public String getTaskId() {
			return taskId;
		}
		public void setTaskId(String taskId) {
			this.taskId = taskId;
		}
		public String getRespState() {
			return respState;
		}
		public void setRespState(String respState) {
			this.respState = respState;
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
	public void setLmArcRadwinBwOnrf(String  lmArcRadwinBwOnrf) {
		this.lmArcRadwinBwOnrf = lmArcRadwinBwOnrf;
	}

	public void setIzoSdwan(String izoSdwan) {
		this.izoSdwan = izoSdwan;
	}

	@JsonProperty("error_msg_display")
	public String getErrorMsgDisplay() {
		return errorMsgDisplay;
	}

	
	// added for O2c
	@JsonProperty("selected_access_rings")
	private List<String> selectedAccessRings;
	@JsonProperty("bw_selected_access_rings")
	private List<String> bwSelectedAccessRings;
	
	@JsonProperty("bw_selected_access_ring")
	private String bwSelectedAccessRing;
	
	@JsonProperty("x_connect_commercialNote")
    private String xConnectCommercialNote;
	
	@JsonProperty("x_connect_isInterfaceChanged")
    private String xconnectIsInterfaceChanged;
	
	@JsonProperty("vendor_name")
	private String vendorName;
	
	@JsonProperty("vendor_id")
	private String vendorId;
	
	@JsonProperty("tcl_pop_address")
	private String tclPopAddress;
	
	//CST-21 customer portal
	@JsonProperty("is_customer")
	private String isCustomer;
	
	@JsonProperty("Access_feasibility_updated")
	private String accessFeasibilityUpdated;

	@JsonProperty("is_demo")
	private String isDemo;
	@JsonProperty("demo_type")
	private String demoType;

	//PIPF-145
	@JsonProperty("grc_version_name")
	private String grcVersionName;
	@JsonProperty("bts_tagged_comment")
	private String btsTaggedComment;
	@JsonProperty("bts_sector_id")
	private String btsSectorId;
	@JsonProperty("bts_sector_name")
	private String btsSectorName;
	
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


	@JsonProperty("vendor_name")
	public String getVendorName() {
		return vendorName;
	}
	@JsonProperty("vendor_name")
	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}
	
	@JsonProperty("vendor_id")
	public String getVendorId() {
		return vendorId;
	}
	@JsonProperty("vendor_id")
	public void setVendorId(String vendorId) {
		this.vendorId = vendorId;
	}
	
	@JsonProperty("tcl_pop_address")
	public String getTclPopAddress() {
		return tclPopAddress;
	}
	@JsonProperty("tcl_pop_address")
	public void setTclPopAddress(String tclPopAddress) {
		this.tclPopAddress = tclPopAddress;
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

	public String getBwSelectedAccessRing() {
		return bwSelectedAccessRing;
	}

	public void setBwSelectedAccessRing(String bwSelectedAccessRing) {
		this.bwSelectedAccessRing = bwSelectedAccessRing;
	}

	public List<String> getSelectedAccessRings() {
		return selectedAccessRings;
	}

	public void setSelectedAccessRings(List<String> selectedAccessRings) {
		this.selectedAccessRings = selectedAccessRings;
	}

	public List<String> getBwSelectedAccessRings() {
		return bwSelectedAccessRings;
	}

	public void setBwSelectedAccessRings(List<String> bwSelectedAccessRings) {
		this.bwSelectedAccessRings = bwSelectedAccessRings;
	}
	


	@JsonProperty("error_msg_display")
	public void setErrorMsgDisplay(String errorMsgDisplay) {
		this.errorMsgDisplay = errorMsgDisplay;
	}
	
	@JsonProperty("version")
	public String getVersion() {
		return version;
	}

	@JsonProperty("version")
	public void setVersion(String version) {
		this.version = version;
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

	@JsonProperty("local_loop_bw")
	public Double getLocalLoopBw() {
		return localLoopBw;
	}

	@JsonProperty("local_loop_bw")
	public void setLocalLoopBw(Double localLoopBw) {
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
	private String parallelRunDays;
	
	 @JsonProperty("prd_category")
	  private String prd_category; 
	
	 @JsonProperty("prd_category")
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

	@JsonProperty("lm_nrc_ospcapex_onwl")
	public String getLmNrcOspcapexOnwl() {
		return lmNrcOspcapexOnwl;
	}

	@JsonProperty("lm_nrc_ospcapex_onwl")
	public void setLmNrcOspcapexOnwl(String lmNrcOspcapexOnwl) {
		this.lmNrcOspcapexOnwl = lmNrcOspcapexOnwl;
	}

	@JsonProperty("Service_ID")
	public Integer getServiceID() {
		return serviceID;
	}

	@JsonProperty("Service_ID")
	public void setServiceID(Integer serviceID) {
		this.serviceID = serviceID;
	}

	@JsonProperty("hh_name")
	public String getHhName() {
		return hhName;
	}

	@JsonProperty("hh_name")
	public void setHhName(String hhName) {
		this.hhName = hhName;
	}

	@JsonProperty("total_cost")
	public String getTotalCost() {
		return totalCost;
	}

	@JsonProperty("total_cost")
	public void setTotalCost(String totalCost) {
		this.totalCost = totalCost;
	}

	@JsonProperty("X0.5km_avg_dist")
	public Integer getX05kmAvgDist() {
		return x05kmAvgDist;
	}

	@JsonProperty("X0.5km_avg_dist")
	public void setX05kmAvgDist(Integer x05kmAvgDist) {
		this.x05kmAvgDist = x05kmAvgDist;
	}

	@JsonProperty("POP_DIST_KM")
	public Float getPOPDISTKM() {
		return pOPDISTKM;
	}

	@JsonProperty("POP_DIST_KM")
	public void setPOPDISTKM(Float pOPDISTKM) {
		this.pOPDISTKM = pOPDISTKM;
	}

	@JsonProperty("X0.5km_prospect_avg_bw")
	public Float getX05kmProspectAvgBw() {
		return x05kmProspectAvgBw;
	}

	@JsonProperty("X0.5km_prospect_avg_bw")
	public void setX05kmProspectAvgBw(Float x05kmProspectAvgBw) {
		this.x05kmProspectAvgBw = x05kmProspectAvgBw;
	}

	@JsonProperty("OnnetCity_tag")
	public Integer getOnnetCityTag() {
		return onnetCityTag;
	}

	@JsonProperty("OnnetCity_tag")
	public void setOnnetCityTag(Integer onnetCityTag) {
		this.onnetCityTag = onnetCityTag;
	}

	@JsonProperty("lm_arc_bw_prov_ofrf")
	public Integer getLmArcBwProvOfrf() {
		return lmArcBwProvOfrf;
	}

	@JsonProperty("lm_arc_bw_prov_ofrf")
	public void setLmArcBwProvOfrf(Integer lmArcBwProvOfrf) {
		this.lmArcBwProvOfrf = lmArcBwProvOfrf;
	}

	@JsonProperty("Network_F_NF_CC")
	public String getNetworkFNFCC() {
		return networkFNFCC;
	}

	@JsonProperty("Network_F_NF_CC")
	public void setNetworkFNFCC(String networkFNFCC) {
		this.networkFNFCC = networkFNFCC;
	}

	@JsonProperty("lm_nrc_nerental_onwl")
	public Integer getLmNrcNerentalOnwl() {
		return lmNrcNerentalOnwl;
	}

	@JsonProperty("lm_nrc_nerental_onwl")
	public void setLmNrcNerentalOnwl(Integer lmNrcNerentalOnwl) {
		this.lmNrcNerentalOnwl = lmNrcNerentalOnwl;
	}

	@JsonProperty("lm_nrc_inbldg_onwl")
	public Integer getLmNrcInbldgOnwl() {
		return lmNrcInbldgOnwl;
	}

	@JsonProperty("lm_nrc_inbldg_onwl")
	public void setLmNrcInbldgOnwl(Integer lmNrcInbldgOnwl) {
		this.lmNrcInbldgOnwl = lmNrcInbldgOnwl;
	}

	@JsonProperty("Network_Feasibility_Check")
	public String getNetworkFeasibilityCheck() {
		return networkFeasibilityCheck;
	}

	@JsonProperty("Network_Feasibility_Check")
	public void setNetworkFeasibilityCheck(String networkFeasibilityCheck) {
		this.networkFeasibilityCheck = networkFeasibilityCheck;
	}

	@JsonProperty("core_check_CC")
	public String getCoreCheckCC() {
		return coreCheckCC;
	}

	@JsonProperty("core_check_CC")
	public void setCoreCheckCC(String coreCheckCC) {
		this.coreCheckCC = coreCheckCC;
	}

	@JsonProperty("X5km_min_bw")
	public Integer getX5kmMinBw() {
		return x5kmMinBw;
	}

	@JsonProperty("X5km_min_bw")
	public void setX5kmMinBw(Integer x5kmMinBw) {
		this.x5kmMinBw = x5kmMinBw;
	}

	@JsonProperty("access_check_CC")
	public String getAccessCheckCC() {
		return accessCheckCC;
	}

	@JsonProperty("access_check_CC")
	public void setAccessCheckCC(String accessCheckCC) {
		this.accessCheckCC = accessCheckCC;
	}

	@JsonProperty("rank")
	public Integer getRank() {
		return rank;
	}

	@JsonProperty("rank")
	public void setRank(Integer rank) {
		this.rank = rank;
	}

	@JsonProperty("POP_DIST_KM_SERVICE_MOD")
	public Integer getPOPDISTKMSERVICEMOD() {
		return pOPDISTKMSERVICEMOD;
	}

	@JsonProperty("POP_DIST_KM_SERVICE_MOD")
	public void setPOPDISTKMSERVICEMOD(Integer pOPDISTKMSERVICEMOD) {
		this.pOPDISTKMSERVICEMOD = pOPDISTKMSERVICEMOD;
	}

	@JsonProperty("X5km_max_bw")
	public Integer getX5kmMaxBw() {
		return x5kmMaxBw;
	}

	@JsonProperty("X5km_max_bw")
	public void setX5kmMaxBw(Integer x5kmMaxBw) {
		this.x5kmMaxBw = x5kmMaxBw;
	}

	@JsonProperty("X5km_prospect_perc_feasible")
	public Float getX5kmProspectPercFeasible() {
		return x5kmProspectPercFeasible;
	}

	@JsonProperty("X5km_prospect_perc_feasible")
	public void setX5kmProspectPercFeasible(Float x5kmProspectPercFeasible) {
		this.x5kmProspectPercFeasible = x5kmProspectPercFeasible;
	}

	@JsonProperty("lm_arc_bw_onrf")
	public Integer getLmArcBwOnrf() {
		return lmArcBwOnrf;
	}

	@JsonProperty("lm_arc_bw_onrf")
	public void setLmArcBwOnrf(Integer lmArcBwOnrf) {
		this.lmArcBwOnrf = lmArcBwOnrf;
	}

	@JsonProperty("POP_Category")
	public String getPOPCategory() {
		return pOPCategory;
	}

	@JsonProperty("POP_Category")
	public void setPOPCategory(String pOPCategory) {
		this.pOPCategory = pOPCategory;
	}

	@JsonProperty("lm_nrc_bw_prov_ofrf")
	public Integer getLmNrcBwProvOfrf() {
		return lmNrcBwProvOfrf;
	}

	@JsonProperty("lm_nrc_bw_prov_ofrf")
	public void setLmNrcBwProvOfrf(Integer lmNrcBwProvOfrf) {
		this.lmNrcBwProvOfrf = lmNrcBwProvOfrf;
	}

	@JsonProperty("FATG_Building_Type")
	public String getFATGBuildingType() {
		return fATGBuildingType;
	}

	@JsonProperty("FATG_Building_Type")
	public void setFATGBuildingType(String fATGBuildingType) {
		this.fATGBuildingType = fATGBuildingType;
	}

	@JsonProperty("HH_0_5_access_rings_F")
	public String getHH05AccessRingsF() {
		return hH05AccessRingsF;
	}

	@JsonProperty("HH_0_5_access_rings_F")
	public void setHH05AccessRingsF(String hH05AccessRingsF) {
		this.hH05AccessRingsF = hH05AccessRingsF;
	}

	@JsonProperty("hh_flag")
	public String getHhFlag() {
		return hhFlag;
	}

	@JsonProperty("hh_flag")
	public void setHhFlag(String hhFlag) {
		this.hhFlag = hhFlag;
	}

	@JsonProperty("local_loop_interface")
	public String getLocalLoopInterface() {
		return localLoopInterface;
	}

	@JsonProperty("local_loop_interface")
	public void setLocalLoopInterface(String localLoopInterface) {
		this.localLoopInterface = localLoopInterface;
	}

	@JsonProperty("X5km_prospect_avg_dist")
	public Float getX5kmProspectAvgDist() {
		return x5kmProspectAvgDist;
	}

	@JsonProperty("X5km_prospect_avg_dist")
	public void setX5kmProspectAvgDist(Float x5kmProspectAvgDist) {
		this.x5kmProspectAvgDist = x5kmProspectAvgDist;
	}

	@JsonProperty("latitude_final")
	public Float getLatitudeFinal() {
		return latitudeFinal;
	}

	@JsonProperty("latitude_final")
	public void setLatitudeFinal(Float latitudeFinal) {
		this.latitudeFinal = latitudeFinal;
	}

	@JsonProperty("FATG_Network_Location_Type")
	public String getFATGNetworkLocationType() {
		return fATGNetworkLocationType;
	}

	@JsonProperty("FATG_Network_Location_Type")
	public void setFATGNetworkLocationType(String fATGNetworkLocationType) {
		this.fATGNetworkLocationType = fATGNetworkLocationType;
	}

	@JsonProperty("topology")
	public String getTopology() {
		return topology;
	}

	@JsonProperty("topology")
	public void setTopology(String topology) {
		this.topology = topology;
	}

	@JsonProperty("city_tier")
	public String getCityTier() {
		return cityTier;
	}

	@JsonProperty("city_tier")
	public void setCityTier(String cityTier) {
		this.cityTier = cityTier;
	}

	@JsonProperty("lm_nrc_bw_onwl")
	public Integer getLmNrcBwOnwl() {
		return lmNrcBwOnwl;
	}

	@JsonProperty("lm_nrc_bw_onwl")
	public void setLmNrcBwOnwl(Integer lmNrcBwOnwl) {
		this.lmNrcBwOnwl = lmNrcBwOnwl;
	}

	@JsonProperty("X2km_prospect_min_dist")
	public Float getX2kmProspectMinDist() {
		return x2kmProspectMinDist;
	}

	@JsonProperty("X2km_prospect_min_dist")
	public void setX2kmProspectMinDist(Float x2kmProspectMinDist) {
		this.x2kmProspectMinDist = x2kmProspectMinDist;
	}

	@JsonProperty("X2km_prospect_perc_feasible")
	public Float getX2kmProspectPercFeasible() {
		return x2kmProspectPercFeasible;
	}

	@JsonProperty("X2km_prospect_perc_feasible")
	public void setX2kmProspectPercFeasible(Float x2kmProspectPercFeasible) {
		this.x2kmProspectPercFeasible = x2kmProspectPercFeasible;
	}

	@JsonProperty("X0.5km_prospect_num_feasible")
	public Integer getX05kmProspectNumFeasible() {
		return x05kmProspectNumFeasible;
	}

	@JsonProperty("X0.5km_prospect_num_feasible")
	public void setX05kmProspectNumFeasible(Integer x05kmProspectNumFeasible) {
		this.x05kmProspectNumFeasible = x05kmProspectNumFeasible;
	}

	@JsonProperty("cpe_supply_type")
	public String getCpeSupplyType() {
		return cpeSupplyType;
	}

	@JsonProperty("cpe_supply_type")
	public void setCpeSupplyType(String cpeSupplyType) {
		this.cpeSupplyType = cpeSupplyType;
	}

	@JsonProperty("additional_ip_flag")
	public String getAdditionalIpFlag() {
		return additionalIpFlag;
	}

	@JsonProperty("additional_ip_flag")
	public void setAdditionalIpFlag(String additionalIpFlag) {
		this.additionalIpFlag = additionalIpFlag;
	}

	@JsonProperty("Orch_Connection")
	public String getOrchConnection() {
		return orchConnection;
	}

	@JsonProperty("Orch_Connection")
	public void setOrchConnection(String orchConnection) {
		this.orchConnection = orchConnection;
	}

	@JsonProperty("site_id")
	public String getSiteId() {
		return siteId;
	}

	@JsonProperty("site_id")
	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}

	@JsonProperty("cpe_management_type")
	public String getCpeManagementType() {
		return cpeManagementType;
	}

	@JsonProperty("cpe_management_type")
	public void setCpeManagementType(String cpeManagementType) {
		this.cpeManagementType = cpeManagementType;
	}

	@JsonProperty("sales_org")
	public String getSalesOrg() {
		return salesOrg;
	}

	@JsonProperty("sales_org")
	public void setSalesOrg(String salesOrg) {
		this.salesOrg = salesOrg;
	}

	@JsonProperty("opportunity_term")
	public Integer getOpportunityTerm() {
		return opportunityTerm;
	}

	@JsonProperty("opportunity_term")
	public void setOpportunityTerm(Integer opportunityTerm) {
		this.opportunityTerm = opportunityTerm;
	}

	@JsonProperty("error_flag")
	public Integer getErrorFlag() {
		return errorFlag;
	}

	@JsonProperty("error_flag")
	public void setErrorFlag(Integer errorFlag) {
		this.errorFlag = errorFlag;
	}

	@JsonProperty("resp_city")
	public String getRespCity() {
		return respCity;
	}

	@JsonProperty("resp_city")
	public void setRespCity(String respCity) {
		this.respCity = respCity;
	}

	@JsonProperty("connected_cust_tag")
	public Integer getConnectedCustTag() {
		return connectedCustTag;
	}

	@JsonProperty("connected_cust_tag")
	public void setConnectedCustTag(Integer connectedCustTag) {
		this.connectedCustTag = connectedCustTag;
	}

	@JsonProperty("net_pre_feasible_flag")
	public Integer getNetPreFeasibleFlag() {
		return netPreFeasibleFlag;
	}

	@JsonProperty("net_pre_feasible_flag")
	public void setNetPreFeasibleFlag(Integer netPreFeasibleFlag) {
		this.netPreFeasibleFlag = netPreFeasibleFlag;
	}

	@JsonProperty("Network_F_NF_CC_Flag")
	public String getNetworkFNFCCFlag() {
		return networkFNFCCFlag;
	}

	@JsonProperty("Network_F_NF_CC_Flag")
	public void setNetworkFNFCCFlag(String networkFNFCCFlag) {
		this.networkFNFCCFlag = networkFNFCCFlag;
	}

	@JsonProperty("pop_long")
	public Float getPopLong() {
		return popLong;
	}

	@JsonProperty("pop_long")
	public void setPopLong(Float popLong) {
		this.popLong = popLong;
	}

	@JsonProperty("Orch_LM_Type")
	public String getOrchLMType() {
		return orchLMType;
	}

	@JsonProperty("Orch_LM_Type")
	public void setOrchLMType(String orchLMType) {
		this.orchLMType = orchLMType;
	}

	@JsonProperty("ipv4_address_pool_size")
	public String getIpv4AddressPoolSize() {
		return ipv4AddressPoolSize;
	}

	@JsonProperty("ipv4_address_pool_size")
	public void setIpv4AddressPoolSize(String ipv4AddressPoolSize) {
		this.ipv4AddressPoolSize = ipv4AddressPoolSize;
	}

	@JsonProperty("X5km_prospect_avg_bw")
	public Float getX5kmProspectAvgBw() {
		return x5kmProspectAvgBw;
	}

	@JsonProperty("X5km_prospect_avg_bw")
	public void setX5kmProspectAvgBw(Float x5kmProspectAvgBw) {
		this.x5kmProspectAvgBw = x5kmProspectAvgBw;
	}

	@JsonProperty("FATG_Category")
	public String getFATGCategory() {
		return fATGCategory;
	}

	@JsonProperty("FATG_Category")
	public void setFATGCategory(String fATGCategory) {
		this.fATGCategory = fATGCategory;
	}

	@JsonProperty("lm_nrc_mast_ofrf")
	public Float getLmNrcMastOfrf() {
		return lmNrcMastOfrf;
	}

	@JsonProperty("lm_nrc_mast_ofrf")
	public void setLmNrcMastOfrf(Float lmNrcMastOfrf) {
		this.lmNrcMastOfrf = lmNrcMastOfrf;
	}

	@JsonProperty("X2km_prospect_min_bw")
	public Integer getX2kmProspectMinBw() {
		return x2kmProspectMinBw;
	}

	@JsonProperty("X2km_prospect_min_bw")
	public void setX2kmProspectMinBw(Integer x2kmProspectMinBw) {
		this.x2kmProspectMinBw = x2kmProspectMinBw;
	}

	@JsonProperty("X5km_prospect_num_feasible")
	public Integer getX5kmProspectNumFeasible() {
		return x5kmProspectNumFeasible;
	}

	@JsonProperty("X5km_prospect_num_feasible")
	public void setX5kmProspectNumFeasible(Integer x5kmProspectNumFeasible) {
		this.x5kmProspectNumFeasible = x5kmProspectNumFeasible;
	}

	@JsonProperty("Selected")
	public Boolean getSelected() {
		return selected;
	}

	@JsonProperty("Selected")
	public void setSelected(Boolean selected) {
		this.selected = selected;
	}

	@JsonProperty("pop_name")
	public String getPopName() {
		return popName;
	}

	@JsonProperty("pop_name")
	public void setPopName(String popName) {
		this.popName = popName;
	}

	@JsonProperty("lm_nrc_mast_onrf")
	public Integer getLmNrcMastOnrf() {
		return lmNrcMastOnrf;
	}

	@JsonProperty("lm_nrc_mast_onrf")
	public void setLmNrcMastOnrf(Integer lmNrcMastOnrf) {
		this.lmNrcMastOnrf = lmNrcMastOnrf;
	}

	@JsonProperty("X5km_prospect_count")
	public Integer getX5kmProspectCount() {
		return x5kmProspectCount;
	}

	@JsonProperty("X5km_prospect_count")
	public void setX5kmProspectCount(Integer x5kmProspectCount) {
		this.x5kmProspectCount = x5kmProspectCount;
	}

	@JsonProperty("error_msg")
	public String getErrorMsg() {
		return errorMsg;
	}

	@JsonProperty("error_msg")
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	@JsonProperty("connection_type")
	public String getConnectionType() {
		return connectionType;
	}

	@JsonProperty("connection_type")
	public void setConnectionType(String connectionType) {
		this.connectionType = connectionType;
	}

	@JsonProperty("ip_address_arrangement")
	public String getIpAddressArrangement() {
		return ipAddressArrangement;
	}

	@JsonProperty("ip_address_arrangement")
	public void setIpAddressArrangement(String ipAddressArrangement) {
		this.ipAddressArrangement = ipAddressArrangement;
	}

	@JsonProperty("X0.5km_avg_bw")
	public Integer getX05kmAvgBw() {
		return x05kmAvgBw;
	}

	@JsonProperty("X0.5km_avg_bw")
	public void setX05kmAvgBw(Integer x05kmAvgBw) {
		this.x05kmAvgBw = x05kmAvgBw;
	}

	@JsonProperty("X2km_cust_count")
	public Integer getX2kmCustCount() {
		return x2kmCustCount;
	}

	@JsonProperty("X2km_cust_count")
	public void setX2kmCustCount(Integer x2kmCustCount) {
		this.x2kmCustCount = x2kmCustCount;
	}

	@JsonProperty("customer_segment")
	public String getCustomerSegment() {
		return customerSegment;
	}

	@JsonProperty("customer_segment")
	public void setCustomerSegment(String customerSegment) {
		this.customerSegment = customerSegment;
	}

	@JsonProperty("FATG_PROW")
	public String getFATGPROW() {
		return fATGPROW;
	}

	@JsonProperty("FATG_PROW")
	public void setFATGPROW(String fATGPROW) {
		this.fATGPROW = fATGPROW;
	}

	@JsonProperty("time_taken")
	public Float getTimeTaken() {
		return timeTaken;
	}

	@JsonProperty("time_taken")
	public void setTimeTaken(Float timeTaken) {
		this.timeTaken = timeTaken;
	}

	@JsonProperty("connected_building_tag")
	public Integer getConnectedBuildingTag() {
		return connectedBuildingTag;
	}

	@JsonProperty("connected_building_tag")
	public void setConnectedBuildingTag(Integer connectedBuildingTag) {
		this.connectedBuildingTag = connectedBuildingTag;
	}

	@JsonProperty("FATG_DIST_KM")
	public Float getFATGDISTKM() {
		return fATGDISTKM;
	}

	@JsonProperty("FATG_DIST_KM")
	public void setFATGDISTKM(Float fATGDISTKM) {
		this.fATGDISTKM = fATGDISTKM;
	}

	@JsonProperty("X0.5km_prospect_max_bw")
	public Integer getX05kmProspectMaxBw() {
		return x05kmProspectMaxBw;
	}

	@JsonProperty("X0.5km_prospect_max_bw")
	public void setX05kmProspectMaxBw(Integer x05kmProspectMaxBw) {
		this.x05kmProspectMaxBw = x05kmProspectMaxBw;
	}

	@JsonProperty("X5km_prospect_min_dist")
	public Float getX5kmProspectMinDist() {
		return x5kmProspectMinDist;
	}

	@JsonProperty("X5km_prospect_min_dist")
	public void setX5kmProspectMinDist(Float x5kmProspectMinDist) {
		this.x5kmProspectMinDist = x5kmProspectMinDist;
	}

	@JsonProperty("Type")
	public String getType() {
		return type;
	}

	@JsonProperty("Type")
	public void setType(String type) {
		this.type = type;
	}

	@JsonProperty("FATG_Ring_type")
	public String getFATGRingType() {
		return fATGRingType;
	}

	@JsonProperty("FATG_Ring_type")
	public void setFATGRingType(String fATGRingType) {
		this.fATGRingType = fATGRingType;
	}

	@JsonProperty("longitude_final")
	public Float getLongitudeFinal() {
		return longitudeFinal;
	}

	@JsonProperty("longitude_final")
	public void setLongitudeFinal(Float longitudeFinal) {
		this.longitudeFinal = longitudeFinal;
	}

	@JsonProperty("Orch_BW")
	public Integer getOrchBW() {
		return orchBW;
	}

	@JsonProperty("Orch_BW")
	public void setOrchBW(Integer orchBW) {
		this.orchBW = orchBW;
	}

	@JsonProperty("scenario_1")
	public Integer getScenario1() {
		return scenario1;
	}

	@JsonProperty("scenario_1")
	public void setScenario1(Integer scenario1) {
		this.scenario1 = scenario1;
	}

	@JsonProperty("scenario_2")
	public Integer getScenario2() {
		return scenario2;
	}

	@JsonProperty("scenario_2")
	public void setScenario2(Integer scenario2) {
		this.scenario2 = scenario2;
	}

	@JsonProperty("access_check_hh")
	public String getAccessCheckHh() {
		return accessCheckHh;
	}

	@JsonProperty("access_check_hh")
	public void setAccessCheckHh(String accessCheckHh) {
		this.accessCheckHh = accessCheckHh;
	}

	@JsonProperty("X2km_prospect_count")
	public Integer getX2kmProspectCount() {
		return x2kmProspectCount;
	}

	@JsonProperty("X2km_prospect_count")
	public void setX2kmProspectCount(Integer x2kmProspectCount) {
		this.x2kmProspectCount = x2kmProspectCount;
	}

	@JsonProperty("ipv6_address_pool_size")
	public String getIpv6AddressPoolSize() {
		return ipv6AddressPoolSize;
	}

	@JsonProperty("ipv6_address_pool_size")
	public void setIpv6AddressPoolSize(String ipv6AddressPoolSize) {
		this.ipv6AddressPoolSize = ipv6AddressPoolSize;
	}

	@JsonProperty("min_hh_fatg")
	public Float getMinHhFatg() {
		return minHhFatg;
	}

	@JsonProperty("min_hh_fatg")
	public void setMinHhFatg(Float minHhFatg) {
		this.minHhFatg = minHhFatg;
	}

	@JsonProperty("POP_Construction_Status")
	public String getPOPConstructionStatus() {
		return pOPConstructionStatus;
	}

	@JsonProperty("POP_Construction_Status")
	public void setPOPConstructionStatus(String pOPConstructionStatus) {
		this.pOPConstructionStatus = pOPConstructionStatus;
	}

	@JsonProperty("X5km_avg_dist")
	public Float getX5kmAvgDist() {
		return x5kmAvgDist;
	}

	@JsonProperty("X5km_avg_dist")
	public void setX5kmAvgDist(Float x5kmAvgDist) {
		this.x5kmAvgDist = x5kmAvgDist;
	}

	@JsonProperty("X2km_avg_dist")
	public Float getX2kmAvgDist() {
		return x2kmAvgDist;
	}

	@JsonProperty("X2km_avg_dist")
	public void setX2kmAvgDist(Float x2kmAvgDist) {
		this.x2kmAvgDist = x2kmAvgDist;
	}

	@JsonProperty("X2km_prospect_avg_bw")
	public Float getX2kmProspectAvgBw() {
		return x2kmProspectAvgBw;
	}

	@JsonProperty("X2km_prospect_avg_bw")
	public void setX2kmProspectAvgBw(Float x2kmProspectAvgBw) {
		this.x2kmProspectAvgBw = x2kmProspectAvgBw;
	}

	@JsonProperty("X0.5km_prospect_min_dist")
	public Float getX05kmProspectMinDist() {
		return x05kmProspectMinDist;
	}

	@JsonProperty("X0.5km_prospect_min_dist")
	public void setX05kmProspectMinDist(Float x05kmProspectMinDist) {
		this.x05kmProspectMinDist = x05kmProspectMinDist;
	}

	@JsonProperty("cost_permeter")
	public Integer getCostPermeter() {
		return costPermeter;
	}

	@JsonProperty("cost_permeter")
	public void setCostPermeter(Integer costPermeter) {
		this.costPermeter = costPermeter;
	}

	@JsonProperty("prospect_name")
	public String getProspectName() {
		return prospectName;
	}

	@JsonProperty("prospect_name")
	public void setProspectName(String prospectName) {
		this.prospectName = prospectName;
	}

	@JsonProperty("Predicted_Access_Feasibility")
	public String getPredictedAccessFeasibility() {
		return predictedAccessFeasibility;
	}

	@JsonProperty("Predicted_Access_Feasibility")
	public void setPredictedAccessFeasibility(String predictedAccessFeasibility) {
		this.predictedAccessFeasibility = predictedAccessFeasibility;
	}

	@JsonProperty("X2km_max_bw")
	public Integer getX2kmMaxBw() {
		return x2kmMaxBw;
	}

	@JsonProperty("X2km_max_bw")
	public void setX2kmMaxBw(Integer x2kmMaxBw) {
		this.x2kmMaxBw = x2kmMaxBw;
	}

	@JsonProperty("X2km_prospect_max_bw")
	public Integer getX2kmProspectMaxBw() {
		return x2kmProspectMaxBw;
	}

	@JsonProperty("X2km_prospect_max_bw")
	public void setX2kmProspectMaxBw(Integer x2kmProspectMaxBw) {
		this.x2kmProspectMaxBw = x2kmProspectMaxBw;
	}

	@JsonProperty("last_mile_contract_term")
	public String getLastMileContractTerm() {
		return lastMileContractTerm;
	}

	@JsonProperty("last_mile_contract_term")
	public void setLastMileContractTerm(String lastMileContractTerm) {
		this.lastMileContractTerm = lastMileContractTerm;
	}

	@JsonProperty("X5km_prospect_min_bw")
	public Float getX5kmProspectMinBw() {
		return x5kmProspectMinBw;
	}

	@JsonProperty("X5km_prospect_min_bw")
	public void setX5kmProspectMinBw(Float x5kmProspectMinBw) {
		this.x5kmProspectMinBw = x5kmProspectMinBw;
	}

	@JsonProperty("X2km_min_bw")
	public Integer getX2kmMinBw() {
		return x2kmMinBw;
	}

	@JsonProperty("X2km_min_bw")
	public void setX2kmMinBw(Integer x2kmMinBw) {
		this.x2kmMinBw = x2kmMinBw;
	}

	@JsonProperty("burstable_bw")
	public Double getBurstableBw() {
		return burstableBw;
	}

	@JsonProperty("burstable_bw")
	public void setBurstableBw(Double burstableBw) {
		this.burstableBw = burstableBw;
	}

	@JsonProperty("pop_network_loc_id")
	public String getPopNetworkLocId() {
		return popNetworkLocId;
	}

	@JsonProperty("pop_network_loc_id")
	public void setPopNetworkLocId(String popNetworkLocId) {
		this.popNetworkLocId = popNetworkLocId;
	}

	@JsonProperty("bw_mbps")
	public Double getBwMbps() {
		return bwMbps;
	}

	@JsonProperty("bw_mbps")
	public void setBwMbps(Double bwMbps) {
		this.bwMbps = bwMbps;
	}

	@JsonProperty("X0.5km_cust_count")
	public Integer getX05kmCustCount() {
		return x05kmCustCount;
	}

	@JsonProperty("X0.5km_cust_count")
	public void setX05kmCustCount(Integer x05kmCustCount) {
		this.x05kmCustCount = x05kmCustCount;
	}

	@JsonProperty("X0.5km_min_bw")
	public Integer getX05kmMinBw() {
		return x05kmMinBw;
	}

	@JsonProperty("X0.5km_min_bw")
	public void setX05kmMinBw(Integer x05kmMinBw) {
		this.x05kmMinBw = x05kmMinBw;
	}

	@JsonProperty("product_name")
	public String getProductName() {
		return productName;
	}

	@JsonProperty("product_name")
	public void setProductName(String productName) {
		this.productName = productName;
	}

	@JsonProperty("Network_F_NF_HH")
	public String getNetworkFNFHH() {
		return networkFNFHH;
	}

	@JsonProperty("Network_F_NF_HH")
	public void setNetworkFNFHH(String networkFNFHH) {
		this.networkFNFHH = networkFNFHH;
	}

	@JsonProperty("HH_0_5_access_rings_NF")
	public String getHH05AccessRingsNF() {
		return hH05AccessRingsNF;
	}

	@JsonProperty("HH_0_5_access_rings_NF")
	public void setHH05AccessRingsNF(String hH05AccessRingsNF) {
		this.hH05AccessRingsNF = hH05AccessRingsNF;
	}

	@JsonProperty("X0.5km_min_dist")
	public Integer getX05kmMinDist() {
		return x05kmMinDist;
	}

	@JsonProperty("X0.5km_min_dist")
	public void setX05kmMinDist(Integer x05kmMinDist) {
		this.x05kmMinDist = x05kmMinDist;
	}

	@JsonProperty("POP_Building_Type")
	public String getPOPBuildingType() {
		return pOPBuildingType;
	}

	@JsonProperty("POP_Building_Type")
	public void setPOPBuildingType(String pOPBuildingType) {
		this.pOPBuildingType = pOPBuildingType;
	}

	@JsonProperty("core_check_hh")
	public String getCoreCheckHh() {
		return coreCheckHh;
	}

	@JsonProperty("core_check_hh")
	public void setCoreCheckHh(String coreCheckHh) {
		this.coreCheckHh = coreCheckHh;
	}

	@JsonProperty("HH_DIST_KM")
	public Float getHHDISTKM() {
		return hHDISTKM;
	}

	@JsonProperty("HH_DIST_KM")
	public void setHHDISTKM(Float hHDISTKM) {
		this.hHDISTKM = hHDISTKM;
	}

	@JsonProperty("X0.5km_prospect_min_bw")
	public Integer getX05kmProspectMinBw() {
		return x05kmProspectMinBw;
	}

	@JsonProperty("X0.5km_prospect_min_bw")
	public void setX05kmProspectMinBw(Integer x05kmProspectMinBw) {
		this.x05kmProspectMinBw = x05kmProspectMinBw;
	}

	@JsonProperty("error_code")
	public String getErrorCode() {
		return errorCode;
	}

	@JsonProperty("error_code")
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	@JsonProperty("POP_DIST_KM_SERVICE")
	public Float getPOPDISTKMSERVICE() {
		return pOPDISTKMSERVICE;
	}

	@JsonProperty("POP_DIST_KM_SERVICE")
	public void setPOPDISTKMSERVICE(Float pOPDISTKMSERVICE) {
		this.pOPDISTKMSERVICE = pOPDISTKMSERVICE;
	}

	@JsonProperty("lm_arc_bw_onwl")
	public Integer getLmArcBwOnwl() {
		return lmArcBwOnwl;
	}

	@JsonProperty("lm_arc_bw_onwl")
	public void setLmArcBwOnwl(Integer lmArcBwOnwl) {
		this.lmArcBwOnwl = lmArcBwOnwl;
	}

	@JsonProperty("num_connected_building")
	public Integer getNumConnectedBuilding() {
		return numConnectedBuilding;
	}

	@JsonProperty("num_connected_building")
	public void setNumConnectedBuilding(Integer numConnectedBuilding) {
		this.numConnectedBuilding = numConnectedBuilding;
	}

	@JsonProperty("account_id_with_18_digit")
	public String getAccountIdWith18Digit() {
		return accountIdWith18Digit;
	}

	@JsonProperty("account_id_with_18_digit")
	public void setAccountIdWith18Digit(String accountIdWith18Digit) {
		this.accountIdWith18Digit = accountIdWith18Digit;
	}

	@JsonProperty("Orch_Category")
	public String getOrchCategory() {
		return orchCategory;
	}

	@JsonProperty("Orch_Category")
	public void setOrchCategory(String orchCategory) {
		this.orchCategory = orchCategory;
	}

	@JsonProperty("X0.5km_prospect_avg_dist")
	public Float getX05kmProspectAvgDist() {
		return x05kmProspectAvgDist;
	}

	@JsonProperty("X0.5km_prospect_avg_dist")
	public void setX05kmProspectAvgDist(Float x05kmProspectAvgDist) {
		this.x05kmProspectAvgDist = x05kmProspectAvgDist;
	}

	@JsonProperty("POP_Network_Location_Type")
	public String getPOPNetworkLocationType() {
		return pOPNetworkLocationType;
	}

	@JsonProperty("POP_Network_Location_Type")
	public void setPOPNetworkLocationType(String pOPNetworkLocationType) {
		this.pOPNetworkLocationType = pOPNetworkLocationType;
	}

	@JsonProperty("lm_nrc_mux_onwl")
	public Integer getLmNrcMuxOnwl() {
		return lmNrcMuxOnwl;
	}

	@JsonProperty("lm_nrc_mux_onwl")
	public void setLmNrcMuxOnwl(Integer lmNrcMuxOnwl) {
		this.lmNrcMuxOnwl = lmNrcMuxOnwl;
	}

	@JsonProperty("num_connected_cust")
	public Integer getNumConnectedCust() {
		return numConnectedCust;
	}

	@JsonProperty("num_connected_cust")
	public void setNumConnectedCust(Integer numConnectedCust) {
		this.numConnectedCust = numConnectedCust;
	}

	@JsonProperty("HH_0_5km")
	public String getHH05km() {
		return hH05km;
	}

	@JsonProperty("HH_0_5km")
	public void setHH05km(String hH05km) {
		this.hH05km = hH05km;
	}

	@JsonProperty("pop_lat")
	public Float getPopLat() {
		return popLat;
	}

	@JsonProperty("pop_lat")
	public void setPopLat(Float popLat) {
		this.popLat = popLat;
	}

	@JsonProperty("X5km_min_dist")
	public Float getX5kmMinDist() {
		return x5kmMinDist;
	}

	@JsonProperty("X5km_min_dist")
	public void setX5kmMinDist(Float x5kmMinDist) {
		this.x5kmMinDist = x5kmMinDist;
	}

	@JsonProperty("feasibility_response_created_date")
	public String getFeasibilityResponseCreatedDate() {
		return feasibilityResponseCreatedDate;
	}

	@JsonProperty("feasibility_response_created_date")
	public void setFeasibilityResponseCreatedDate(String feasibilityResponseCreatedDate) {
		this.feasibilityResponseCreatedDate = feasibilityResponseCreatedDate;
	}

	@JsonProperty("X2km_prospect_num_feasible")
	public Integer getX2kmProspectNumFeasible() {
		return x2kmProspectNumFeasible;
	}

	@JsonProperty("X2km_prospect_num_feasible")
	public void setX2kmProspectNumFeasible(Integer x2kmProspectNumFeasible) {
		this.x2kmProspectNumFeasible = x2kmProspectNumFeasible;
	}

	@JsonProperty("X2km_avg_bw")
	public Integer getX2kmAvgBw() {
		return x2kmAvgBw;
	}

	@JsonProperty("X2km_avg_bw")
	public void setX2kmAvgBw(Integer x2kmAvgBw) {
		this.x2kmAvgBw = x2kmAvgBw;
	}

	@JsonProperty("X0.5km_max_bw")
	public Integer getX05kmMaxBw() {
		return x05kmMaxBw;
	}

	@JsonProperty("X0.5km_max_bw")
	public void setX05kmMaxBw(Integer x05kmMaxBw) {
		this.x05kmMaxBw = x05kmMaxBw;
	}

	@JsonProperty("X5km_avg_bw")
	public Integer getX5kmAvgBw() {
		return x5kmAvgBw;
	}

	@JsonProperty("X5km_avg_bw")
	public void setX5kmAvgBw(Integer x5kmAvgBw) {
		this.x5kmAvgBw = x5kmAvgBw;
	}

	@JsonProperty("X5km_prospect_max_bw")
	public Integer getX5kmProspectMaxBw() {
		return x5kmProspectMaxBw;
	}

	@JsonProperty("X5km_prospect_max_bw")
	public void setX5kmProspectMaxBw(Integer x5kmProspectMaxBw) {
		this.x5kmProspectMaxBw = x5kmProspectMaxBw;
	}

	@JsonProperty("X2km_prospect_avg_dist")
	public Float getX2kmProspectAvgDist() {
		return x2kmProspectAvgDist;
	}

	@JsonProperty("X2km_prospect_avg_dist")
	public void setX2kmProspectAvgDist(Float x2kmProspectAvgDist) {
		this.x2kmProspectAvgDist = x2kmProspectAvgDist;
	}

	@JsonProperty("cpe_variant")
	public String getCpeVariant() {
		return cpeVariant;
	}

	@JsonProperty("cpe_variant")
	public void setCpeVariant(String cpeVariant) {
		this.cpeVariant = cpeVariant;
	}

	@JsonProperty("X5km_cust_count")
	public Integer getX5kmCustCount() {
		return x5kmCustCount;
	}

	@JsonProperty("X5km_cust_count")
	public void setX5kmCustCount(Integer x5kmCustCount) {
		this.x5kmCustCount = x5kmCustCount;
	}

	@JsonProperty("sum_no_of_sites_uni_len")
	public Integer getSumNoOfSitesUniLen() {
		return sumNoOfSitesUniLen;
	}

	@JsonProperty("sum_no_of_sites_uni_len")
	public void setSumNoOfSitesUniLen(Integer sumNoOfSitesUniLen) {
		this.sumNoOfSitesUniLen = sumNoOfSitesUniLen;
	}

	@JsonProperty("X0.5km_prospect_perc_feasible")
	public Integer getX05kmProspectPercFeasible() {
		return x05kmProspectPercFeasible;
	}

	@JsonProperty("X0.5km_prospect_perc_feasible")
	public void setX05kmProspectPercFeasible(Integer x05kmProspectPercFeasible) {
		this.x05kmProspectPercFeasible = x05kmProspectPercFeasible;
	}

	@JsonProperty("POP_TCL_Access")
	public String getPOPTCLAccess() {
		return pOPTCLAccess;
	}

	@JsonProperty("POP_TCL_Access")
	public void setPOPTCLAccess(String pOPTCLAccess) {
		this.pOPTCLAccess = pOPTCLAccess;
	}

	@JsonProperty("X0.5km_prospect_count")
	public Integer getX05kmProspectCount() {
		return x05kmProspectCount;
	}

	@JsonProperty("X0.5km_prospect_count")
	public void setX05kmProspectCount(Integer x05kmProspectCount) {
		this.x05kmProspectCount = x05kmProspectCount;
	}

	@JsonProperty("Probabililty_Access_Feasibility")
	public Float getProbabililtyAccessFeasibility() {
		return probabililtyAccessFeasibility;
	}

	@JsonProperty("Probabililty_Access_Feasibility")
	public void setProbabililtyAccessFeasibility(Float probabililtyAccessFeasibility) {
		this.probabililtyAccessFeasibility = probabililtyAccessFeasibility;
	}

	@JsonProperty("lm_nrc_bw_onrf")
	public Integer getLmNrcBwOnrf() {
		return lmNrcBwOnrf;
	}

	@JsonProperty("lm_nrc_bw_onrf")
	public void setLmNrcBwOnrf(Integer lmNrcBwOnrf) {
		this.lmNrcBwOnrf = lmNrcBwOnrf;
	}

	@JsonProperty("quotetype_quote")
	public String getQuotetypeQuote() {
		return quotetypeQuote;
	}

	@JsonProperty("quotetype_quote")
	public void setQuotetypeQuote(String quotetypeQuote) {
		this.quotetypeQuote = quotetypeQuote;
	}

	@JsonProperty("pop_address")
	public String getPopAddress() {
		return popAddress;
	}

	@JsonProperty("pop_address")
	public void setPopAddress(String popAddress) {
		this.popAddress = popAddress;
	}

	@JsonProperty("X2km_min_dist")
	public Float getX2kmMinDist() {
		return x2kmMinDist;
	}

	@JsonProperty("X2km_min_dist")
	public void setX2kmMinDist(Float x2kmMinDist) {
		this.x2kmMinDist = x2kmMinDist;
	}

	@JsonProperty("FATG_TCL_Access")
	public String getFATGTCLAccess() {
		return fATGTCLAccess;
	}

	@JsonProperty("FATG_TCL_Access")
	public void setFATGTCLAccess(String fATGTCLAccess) {
		this.fATGTCLAccess = fATGTCLAccess;
	}

	@JsonProperty("Network_F_NF_HH_Flag")
	public String getNetworkFNFHHFlag() {
		return networkFNFHHFlag;
	}

	@JsonProperty("Network_F_NF_HH_Flag")
	public void setNetworkFNFHHFlag(String networkFNFHHFlag) {
		this.networkFNFHHFlag = networkFNFHHFlag;
	}

	@JsonProperty("closest_provider_site_addr")
	public String getClosestProviderSiteAddr() {
		return closestProviderSiteAddr;
	}

	@JsonProperty("closest_provider_site_addr")
	public void setClosestProviderSiteAddr(String closestProviderSiteAddr) {
		this.closestProviderSiteAddr = closestProviderSiteAddr;
	}

	@JsonProperty("otc_tikona")
	public Integer getOtcTikona() {
		return otcTikona;
	}

	@JsonProperty("otc_tikona")
	public void setOtcTikona(Integer otcTikona) {
		this.otcTikona = otcTikona;
	}

	@JsonProperty("closest_provider_site")
	public String getClosestProviderSite() {
		return closestProviderSite;
	}

	@JsonProperty("closest_provider_site")
	public void setClosestProviderSite(String closestProviderSite) {
		this.closestProviderSite = closestProviderSite;
	}

	@JsonProperty("offnet_5km_Min_accuracy_num")
	public Integer getOffnet5kmMinAccuracyNum() {
		return offnet5kmMinAccuracyNum;
	}

	@JsonProperty("offnet_5km_Min_accuracy_num")
	public void setOffnet5kmMinAccuracyNum(Integer offnet5kmMinAccuracyNum) {
		this.offnet5kmMinAccuracyNum = offnet5kmMinAccuracyNum;
	}

	@JsonProperty("interim_BW")
	public Integer getInterimBW() {
		return interimBW;
	}

	@JsonProperty("interim_BW")
	public void setInterimBW(Integer interimBW) {
		this.interimBW = interimBW;
	}

	@JsonProperty("offnet_5km_Min_DistanceKilometers")
	public Float getOffnet5kmMinDistanceKilometers() {
		return offnet5kmMinDistanceKilometers;
	}

	@JsonProperty("offnet_5km_Min_DistanceKilometers")
	public void setOffnet5kmMinDistanceKilometers(Float offnet5kmMinDistanceKilometers) {
		this.offnet5kmMinDistanceKilometers = offnet5kmMinDistanceKilometers;
	}

	@JsonProperty("offnet_0_5km_Max_BW_Mbps")
	public Integer getOffnet05kmMaxBWMbps() {
		return offnet05kmMaxBWMbps;
	}

	@JsonProperty("offnet_0_5km_Max_BW_Mbps")
	public void setOffnet05kmMaxBWMbps(Integer offnet05kmMaxBWMbps) {
		this.offnet05kmMaxBWMbps = offnet05kmMaxBWMbps;
	}

	@JsonProperty("prospect_0_5km_Min_BW_Mbps")
	public Integer getProspect05kmMinBWMbps() {
		return prospect05kmMinBWMbps;
	}

	@JsonProperty("prospect_0_5km_Min_BW_Mbps")
	public void setProspect05kmMinBWMbps(Integer prospect05kmMinBWMbps) {
		this.prospect05kmMinBWMbps = prospect05kmMinBWMbps;
	}

	@JsonProperty("bw_flag_32")
	public Integer getBwFlag32() {
		return bwFlag32;
	}

	@JsonProperty("bw_flag_32")
	public void setBwFlag32(Integer bwFlag32) {
		this.bwFlag32 = bwFlag32;
	}

	@JsonProperty("arc_tikona")
	public Integer getArcTikona() {
		return arcTikona;
	}

	@JsonProperty("arc_tikona")
	public void setArcTikona(Integer arcTikona) {
		this.arcTikona = arcTikona;
	}

	@JsonProperty("min_mast_ht")
	public Integer getMinMastHt() {
		return minMastHt;
	}

	@JsonProperty("min_mast_ht")
	public void setMinMastHt(Integer minMastHt) {
		this.minMastHt = minMastHt;
	}

	@JsonProperty("offnet_2km_Min_DistanceKilometers")
	public Float getOffnet2kmMinDistanceKilometers() {
		return offnet2kmMinDistanceKilometers;
	}

	@JsonProperty("offnet_2km_Min_DistanceKilometers")
	public void setOffnet2kmMinDistanceKilometers(Float offnet2kmMinDistanceKilometers) {
		this.offnet2kmMinDistanceKilometers = offnet2kmMinDistanceKilometers;
	}

	@JsonProperty("prospect_2km_feasibility_pct")
	public Float getProspect2kmFeasibilityPct() {
		return prospect2kmFeasibilityPct;
	}

	@JsonProperty("prospect_2km_feasibility_pct")
	public void setProspect2kmFeasibilityPct(Float prospect2kmFeasibilityPct) {
		this.prospect2kmFeasibilityPct = prospect2kmFeasibilityPct;
	}

	@JsonProperty("bw_flag_3")
	public Integer getBwFlag3() {
		return bwFlag3;
	}

	@JsonProperty("bw_flag_3")
	public void setBwFlag3(Integer bwFlag3) {
		this.bwFlag3 = bwFlag3;
	}

	@JsonProperty("offnet_2km_cust_Count")
	public Integer getOffnet2kmCustCount() {
		return offnet2kmCustCount;
	}

	@JsonProperty("offnet_2km_cust_Count")
	public void setOffnet2kmCustCount(Integer offnet2kmCustCount) {
		this.offnet2kmCustCount = offnet2kmCustCount;
	}

	@JsonProperty("prospect_2km_Avg_DistanceKilometers")
	public Float getProspect2kmAvgDistanceKilometers() {
		return prospect2kmAvgDistanceKilometers;
	}

	@JsonProperty("prospect_2km_Avg_DistanceKilometers")
	public void setProspect2kmAvgDistanceKilometers(Float prospect2kmAvgDistanceKilometers) {
		this.prospect2kmAvgDistanceKilometers = prospect2kmAvgDistanceKilometers;
	}

	@JsonProperty("offnet_5km_Max_BW_Mbps")
	public Integer getOffnet5kmMaxBWMbps() {
		return offnet5kmMaxBWMbps;
	}

	@JsonProperty("offnet_5km_Max_BW_Mbps")
	public void setOffnet5kmMaxBWMbps(Integer offnet5kmMaxBWMbps) {
		this.offnet5kmMaxBWMbps = offnet5kmMaxBWMbps;
	}

	@JsonProperty("prospect_2km_Sum_Feasibility_flag")
	public Integer getProspect2kmSumFeasibilityFlag() {
		return prospect2kmSumFeasibilityFlag;
	}

	@JsonProperty("prospect_2km_Sum_Feasibility_flag")
	public void setProspect2kmSumFeasibilityFlag(Integer prospect2kmSumFeasibilityFlag) {
		this.prospect2kmSumFeasibilityFlag = prospect2kmSumFeasibilityFlag;
	}

	@JsonProperty("arc_sify")
	public Integer getArcSify() {
		return arcSify;
	}

	@JsonProperty("arc_sify")
	public void setArcSify(Integer arcSify) {
		this.arcSify = arcSify;
	}

	@JsonProperty("prospect_2km_Avg_BW_Mbps")
	public Float getProspect2kmAvgBWMbps() {
		return prospect2kmAvgBWMbps;
	}

	@JsonProperty("prospect_2km_Avg_BW_Mbps")
	public void setProspect2kmAvgBWMbps(Float prospect2kmAvgBWMbps) {
		this.prospect2kmAvgBWMbps = prospect2kmAvgBWMbps;
	}

	@JsonProperty("max_mast_ht")
	public Integer getMaxMastHt() {
		return maxMastHt;
	}

	@JsonProperty("max_mast_ht")
	public void setMaxMastHt(Integer maxMastHt) {
		this.maxMastHt = maxMastHt;
	}

	@JsonProperty("provider_tot_towers")
	public Integer getProviderTotTowers() {
		return providerTotTowers;
	}

	@JsonProperty("provider_tot_towers")
	public void setProviderTotTowers(Integer providerTotTowers) {
		this.providerTotTowers = providerTotTowers;
	}

	@JsonProperty("offnet_2km_Min_BW_Mbps")
	public Float getOffnet2kmMinBWMbps() {
		return offnet2kmMinBWMbps;
	}

	@JsonProperty("offnet_2km_Min_BW_Mbps")
	public void setOffnet2kmMinBWMbps(Float offnet2kmMinBWMbps) {
		this.offnet2kmMinBWMbps = offnet2kmMinBWMbps;
	}

	@JsonProperty("offnet_0_5km_Min_BW_Mbps")
	public Integer getOffnet05kmMinBWMbps() {
		return offnet05kmMinBWMbps;
	}

	@JsonProperty("offnet_0_5km_Min_BW_Mbps")
	public void setOffnet05kmMinBWMbps(Integer offnet05kmMinBWMbps) {
		this.offnet05kmMinBWMbps = offnet05kmMinBWMbps;
	}

	@JsonProperty("offnet_5km_Avg_BW_Mbps")
	public Float getOffnet5kmAvgBWMbps() {
		return offnet5kmAvgBWMbps;
	}

	@JsonProperty("offnet_5km_Avg_BW_Mbps")
	public void setOffnet5kmAvgBWMbps(Float offnet5kmAvgBWMbps) {
		this.offnet5kmAvgBWMbps = offnet5kmAvgBWMbps;
	}

	@JsonProperty("prospect_0_5km_Sum_Feasibility_flag")
	public Integer getProspect05kmSumFeasibilityFlag() {
		return prospect05kmSumFeasibilityFlag;
	}

	@JsonProperty("prospect_0_5km_Sum_Feasibility_flag")
	public void setProspect05kmSumFeasibilityFlag(Integer prospect05kmSumFeasibilityFlag) {
		this.prospect05kmSumFeasibilityFlag = prospect05kmSumFeasibilityFlag;
	}

	@JsonProperty("prospect_2km_cust_Count")
	public Integer getProspect2kmCustCount() {
		return prospect2kmCustCount;
	}

	@JsonProperty("prospect_2km_cust_Count")
	public void setProspect2kmCustCount(Integer prospect2kmCustCount) {
		this.prospect2kmCustCount = prospect2kmCustCount;
	}

	@JsonProperty("prospect_2km_Max_BW_Mbps")
	public Integer getProspect2kmMaxBWMbps() {
		return prospect2kmMaxBWMbps;
	}

	@JsonProperty("prospect_2km_Max_BW_Mbps")
	public void setProspect2kmMaxBWMbps(Integer prospect2kmMaxBWMbps) {
		this.prospect2kmMaxBWMbps = prospect2kmMaxBWMbps;
	}

	@JsonProperty("otc_sify")
	public Integer getOtcSify() {
		return otcSify;
	}

	@JsonProperty("otc_sify")
	public void setOtcSify(Integer otcSify) {
		this.otcSify = otcSify;
	}

	@JsonProperty("provider_min_dist")
	public Float getProviderMinDist() {
		return providerMinDist;
	}

	@JsonProperty("provider_min_dist")
	public void setProviderMinDist(Float providerMinDist) {
		this.providerMinDist = providerMinDist;
	}

	@JsonProperty("offnet_5km_Min_BW_Mbps")
	public Float getOffnet5kmMinBWMbps() {
		return offnet5kmMinBWMbps;
	}

	@JsonProperty("offnet_5km_Min_BW_Mbps")
	public void setOffnet5kmMinBWMbps(Float offnet5kmMinBWMbps) {
		this.offnet5kmMinBWMbps = offnet5kmMinBWMbps;
	}

	@JsonProperty("avg_mast_ht")
	public Float getAvgMastHt() {
		return avgMastHt;
	}

	@JsonProperty("avg_mast_ht")
	public void setAvgMastHt(Float avgMastHt) {
		this.avgMastHt = avgMastHt;
	}

	@JsonProperty("prospect_0_5km_feasibility_pct")
	public Integer getProspect05kmFeasibilityPct() {
		return prospect05kmFeasibilityPct;
	}

	@JsonProperty("prospect_0_5km_feasibility_pct")
	public void setProspect05kmFeasibilityPct(Integer prospect05kmFeasibilityPct) {
		this.prospect05kmFeasibilityPct = prospect05kmFeasibilityPct;
	}

	@JsonProperty("prospect_0_5km_Max_BW_Mbps")
	public Integer getProspect05kmMaxBWMbps() {
		return prospect05kmMaxBWMbps;
	}

	@JsonProperty("prospect_0_5km_Max_BW_Mbps")
	public void setProspect05kmMaxBWMbps(Integer prospect05kmMaxBWMbps) {
		this.prospect05kmMaxBWMbps = prospect05kmMaxBWMbps;
	}

	@JsonProperty("offnet_0_5km_Min_accuracy_num")
	public Integer getOffnet05kmMinAccuracyNum() {
		return offnet05kmMinAccuracyNum;
	}

	@JsonProperty("offnet_0_5km_Min_accuracy_num")
	public void setOffnet05kmMinAccuracyNum(Integer offnet05kmMinAccuracyNum) {
		this.offnet05kmMinAccuracyNum = offnet05kmMinAccuracyNum;
	}

	@JsonProperty("closest_provider")
	public String getClosestProvider() {
		return closestProvider;
	}

	@JsonProperty("closest_provider")
	public void setClosestProvider(String closestProvider) {
		this.closestProvider = closestProvider;
	}

	@JsonProperty("nearest_mast_ht_cost")
	public Integer getNearestMastHtCost() {
		return nearestMastHtCost;
	}

	@JsonProperty("nearest_mast_ht_cost")
	public void setNearestMastHtCost(Integer nearestMastHtCost) {
		this.nearestMastHtCost = nearestMastHtCost;
	}

	@JsonProperty("offnet_2km_Avg_BW_Mbps")
	public Float getOffnet2kmAvgBWMbps() {
		return offnet2kmAvgBWMbps;
	}

	@JsonProperty("offnet_2km_Avg_BW_Mbps")
	public void setOffnet2kmAvgBWMbps(Float offnet2kmAvgBWMbps) {
		this.offnet2kmAvgBWMbps = offnet2kmAvgBWMbps;
	}

	@JsonProperty("offnet_2km_Min_accuracy_num")
	public Integer getOffnet2kmMinAccuracyNum() {
		return offnet2kmMinAccuracyNum;
	}

	@JsonProperty("offnet_2km_Min_accuracy_num")
	public void setOffnet2kmMinAccuracyNum(Integer offnet2kmMinAccuracyNum) {
		this.offnet2kmMinAccuracyNum = offnet2kmMinAccuracyNum;
	}

	@JsonProperty("cust_count")
	public Integer getCustCount() {
		return custCount;
	}

	@JsonProperty("cust_count")
	public void setCustCount(Integer custCount) {
		this.custCount = custCount;
	}

	@JsonProperty("offnet_0_5km_Avg_BW_Mbps")
	public Integer getOffnet05kmAvgBWMbps() {
		return offnet05kmAvgBWMbps;
	}

	@JsonProperty("offnet_0_5km_Avg_BW_Mbps")
	public void setOffnet05kmAvgBWMbps(Integer offnet05kmAvgBWMbps) {
		this.offnet05kmAvgBWMbps = offnet05kmAvgBWMbps;
	}

	@JsonProperty("closest_provider_bso_name")
	public String getClosestProviderBsoName() {
		return closestProviderBsoName;
	}

	@JsonProperty("closest_provider_bso_name")
	public void setClosestProviderBsoName(String closestProviderBsoName) {
		this.closestProviderBsoName = closestProviderBsoName;
	}

	@JsonProperty("prospect_0_5km_Min_DistanceKilometers")
	public Integer getProspect05kmMinDistanceKilometers() {
		return prospect05kmMinDistanceKilometers;
	}

	@JsonProperty("prospect_0_5km_Min_DistanceKilometers")
	public void setProspect05kmMinDistanceKilometers(Integer prospect05kmMinDistanceKilometers) {
		this.prospect05kmMinDistanceKilometers = prospect05kmMinDistanceKilometers;
	}

	@JsonProperty("nearest_mast_ht")
	public Integer getNearestMastHt() {
		return nearestMastHt;
	}

	@JsonProperty("nearest_mast_ht")
	public void setNearestMastHt(Integer nearestMastHt) {
		this.nearestMastHt = nearestMastHt;
	}

	@JsonProperty("prospect_2km_Min_BW_Mbps")
	public Float getProspect2kmMinBWMbps() {
		return prospect2kmMinBWMbps;
	}

	@JsonProperty("prospect_2km_Min_BW_Mbps")
	public void setProspect2kmMinBWMbps(Float prospect2kmMinBWMbps) {
		this.prospect2kmMinBWMbps = prospect2kmMinBWMbps;
	}

	@JsonProperty("offnet_0_5km_Min_DistanceKilometers")
	public Integer getOffnet05kmMinDistanceKilometers() {
		return offnet05kmMinDistanceKilometers;
	}

	@JsonProperty("offnet_0_5km_Min_DistanceKilometers")
	public void setOffnet05kmMinDistanceKilometers(Integer offnet05kmMinDistanceKilometers) {
		this.offnet05kmMinDistanceKilometers = offnet05kmMinDistanceKilometers;
	}

	@JsonProperty("offnet_0_5km_cust_Count")
	public Integer getOffnet05kmCustCount() {
		return offnet05kmCustCount;
	}

	@JsonProperty("offnet_0_5km_cust_Count")
	public void setOffnet05kmCustCount(Integer offnet05kmCustCount) {
		this.offnet05kmCustCount = offnet05kmCustCount;
	}

	@JsonProperty("prospect_0_5km_Avg_BW_Mbps")
	public Integer getProspect05kmAvgBWMbps() {
		return prospect05kmAvgBWMbps;
	}

	@JsonProperty("prospect_0_5km_Avg_BW_Mbps")
	public void setProspect05kmAvgBWMbps(Integer prospect05kmAvgBWMbps) {
		this.prospect05kmAvgBWMbps = prospect05kmAvgBWMbps;
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
	@JsonProperty("backup_port_requested")
	public String getBackupPortRequested() {
		return backupPortRequested;
	}
	@JsonProperty("backup_port_requested")
	public void setBackupPortRequested(String backupPortRequested) {
		this.backupPortRequested = backupPortRequested;
	}
	@JsonProperty("cu_le_id")
	public String getCuLeId() {
		return cuLeId;
	}
	@JsonProperty("cu_le_id")
	public void setCuLeId(String cuLeId) {
		this.cuLeId = cuLeId;
	}
	@JsonProperty("Mast_3KM_avg_mast_ht")
	public String getMast3KMAvgMastHt() {
		return mast3KMAvgMastHt;
	}

	@JsonProperty("Mast_3KM_avg_mast_ht")
	public void setMast3KMAvgMastHt(String mast3KMAvgMastHt) {
		this.mast3KMAvgMastHt = mast3KMAvgMastHt;
	}

	@JsonProperty("P2P_bts_3km_radius")
	public String getP2PBts3kmRadius() {
		return p2PBts3kmRadius;
	}

	@JsonProperty("P2P_bts_3km_radius")
	public void setP2PBts3kmRadius(String p2PBts3kmRadius) {
		this.p2PBts3kmRadius = p2PBts3kmRadius;
	}

	@JsonProperty("PMP_bts_3km_radius")
	public String getPMPBts3kmRadius() {
		return pMPBts3kmRadius;
	}

	@JsonProperty("PMP_bts_3km_radius")
	public void setPMPBts3kmRadius(String pMPBts3kmRadius) {
		this.pMPBts3kmRadius = pMPBts3kmRadius;
	}

	@JsonProperty("bts_num_BTS")
	public String getBtsNumBTS() {
		return btsNumBTS;
	}

	@JsonProperty("bts_num_BTS")
	public void setBtsNumBTS(String btsNumBTS) {
		this.btsNumBTS = btsNumBTS;
	}

	@JsonProperty("bts_min_dist_km")
	public String getBtsMinDistKm() {
		return btsMinDistKm;
	}

	@JsonProperty("bts_min_dist_km")
	public void setBtsMinDistKm(String btsMinDistKm) {
		this.btsMinDistKm = btsMinDistKm;
	}

	@JsonProperty("solution_type")
	public String getSolutionType() {
		return solutionType;
	}

	@JsonProperty("solution_type")
	public void setSolutionType(String solutionType) {
		this.solutionType = solutionType;
	}

	/**
	 * @return the pOPDISTKM
	 */
	public Float getpOPDISTKM() {
		return pOPDISTKM;
	}

	/**
	 * @param pOPDISTKM the pOPDISTKM to set
	 */
	public void setpOPDISTKM(Float pOPDISTKM) {
		this.pOPDISTKM = pOPDISTKM;
	}

	/**
	 * @return the pOPDISTKMSERVICEMOD
	 */
	public Integer getpOPDISTKMSERVICEMOD() {
		return pOPDISTKMSERVICEMOD;
	}

	/**
	 * @param pOPDISTKMSERVICEMOD the pOPDISTKMSERVICEMOD to set
	 */
	public void setpOPDISTKMSERVICEMOD(Integer pOPDISTKMSERVICEMOD) {
		this.pOPDISTKMSERVICEMOD = pOPDISTKMSERVICEMOD;
	}

	/**
	 * @return the pOPCategory
	 */
	public String getpOPCategory() {
		return pOPCategory;
	}

	/**
	 * @param pOPCategory the pOPCategory to set
	 */
	public void setpOPCategory(String pOPCategory) {
		this.pOPCategory = pOPCategory;
	}

	/**
	 * @return the fATGBuildingType
	 */
	public String getfATGBuildingType() {
		return fATGBuildingType;
	}

	/**
	 * @param fATGBuildingType the fATGBuildingType to set
	 */
	public void setfATGBuildingType(String fATGBuildingType) {
		this.fATGBuildingType = fATGBuildingType;
	}

	/**
	 * @return the hH05AccessRingsF
	 */
	public String gethH05AccessRingsF() {
		return hH05AccessRingsF;
	}

	/**
	 * @param hH05AccessRingsF the hH05AccessRingsF to set
	 */
	public void sethH05AccessRingsF(String hH05AccessRingsF) {
		this.hH05AccessRingsF = hH05AccessRingsF;
	}

	/**
	 * @return the fATGNetworkLocationType
	 */
	public String getfATGNetworkLocationType() {
		return fATGNetworkLocationType;
	}

	/**
	 * @param fATGNetworkLocationType the fATGNetworkLocationType to set
	 */
	public void setfATGNetworkLocationType(String fATGNetworkLocationType) {
		this.fATGNetworkLocationType = fATGNetworkLocationType;
	}

	/**
	 * @return the fATGCategory
	 */
	public String getfATGCategory() {
		return fATGCategory;
	}

	/**
	 * @param fATGCategory the fATGCategory to set
	 */
	public void setfATGCategory(String fATGCategory) {
		this.fATGCategory = fATGCategory;
	}

	/**
	 * @return the fATGPROW
	 */
	public String getfATGPROW() {
		return fATGPROW;
	}

	/**
	 * @param fATGPROW the fATGPROW to set
	 */
	public void setfATGPROW(String fATGPROW) {
		this.fATGPROW = fATGPROW;
	}

	/**
	 * @return the fATGDISTKM
	 */
	public Float getfATGDISTKM() {
		return fATGDISTKM;
	}

	/**
	 * @param fATGDISTKM the fATGDISTKM to set
	 */
	public void setfATGDISTKM(Float fATGDISTKM) {
		this.fATGDISTKM = fATGDISTKM;
	}

	/**
	 * @return the fATGRingType
	 */
	public String getfATGRingType() {
		return fATGRingType;
	}

	/**
	 * @param fATGRingType the fATGRingType to set
	 */
	public void setfATGRingType(String fATGRingType) {
		this.fATGRingType = fATGRingType;
	}

	/**
	 * @return the pOPConstructionStatus
	 */
	public String getpOPConstructionStatus() {
		return pOPConstructionStatus;
	}

	/**
	 * @param pOPConstructionStatus the pOPConstructionStatus to set
	 */
	public void setpOPConstructionStatus(String pOPConstructionStatus) {
		this.pOPConstructionStatus = pOPConstructionStatus;
	}

	/**
	 * @return the hH05AccessRingsNF
	 */
	public String gethH05AccessRingsNF() {
		return hH05AccessRingsNF;
	}

	/**
	 * @param hH05AccessRingsNF the hH05AccessRingsNF to set
	 */
	public void sethH05AccessRingsNF(String hH05AccessRingsNF) {
		this.hH05AccessRingsNF = hH05AccessRingsNF;
	}

	/**
	 * @return the pOPBuildingType
	 */
	public String getpOPBuildingType() {
		return pOPBuildingType;
	}

	/**
	 * @param pOPBuildingType the pOPBuildingType to set
	 */
	public void setpOPBuildingType(String pOPBuildingType) {
		this.pOPBuildingType = pOPBuildingType;
	}

	/**
	 * @return the hHDISTKM
	 */
	public Float gethHDISTKM() {
		return hHDISTKM;
	}

	/**
	 * @param hHDISTKM the hHDISTKM to set
	 */
	public void sethHDISTKM(Float hHDISTKM) {
		this.hHDISTKM = hHDISTKM;
	}

	/**
	 * @return the pOPDISTKMSERVICE
	 */
	public Float getpOPDISTKMSERVICE() {
		return pOPDISTKMSERVICE;
	}

	/**
	 * @param pOPDISTKMSERVICE the pOPDISTKMSERVICE to set
	 */
	public void setpOPDISTKMSERVICE(Float pOPDISTKMSERVICE) {
		this.pOPDISTKMSERVICE = pOPDISTKMSERVICE;
	}

	/**
	 * @return the pOPNetworkLocationType
	 */
	public String getpOPNetworkLocationType() {
		return pOPNetworkLocationType;
	}

	/**
	 * @param pOPNetworkLocationType the pOPNetworkLocationType to set
	 */
	public void setpOPNetworkLocationType(String pOPNetworkLocationType) {
		this.pOPNetworkLocationType = pOPNetworkLocationType;
	}

	/**
	 * @return the hH05km
	 */
	public String gethH05km() {
		return hH05km;
	}

	/**
	 * @param hH05km the hH05km to set
	 */
	public void sethH05km(String hH05km) {
		this.hH05km = hH05km;
	}

	/**
	 * @return the pOPTCLAccess
	 */
	public String getpOPTCLAccess() {
		return pOPTCLAccess;
	}

	/**
	 * @param pOPTCLAccess the pOPTCLAccess to set
	 */
	public void setpOPTCLAccess(String pOPTCLAccess) {
		this.pOPTCLAccess = pOPTCLAccess;
	}

	/**
	 * @return the fATGTCLAccess
	 */
	public String getfATGTCLAccess() {
		return fATGTCLAccess;
	}

	/**
	 * @param fATGTCLAccess the fATGTCLAccess to set
	 */
	public void setfATGTCLAccess(String fATGTCLAccess) {
		this.fATGTCLAccess = fATGTCLAccess;
	}

	/**
	 * @return the pMPBts3kmRadius
	 */
	public String getpMPBts3kmRadius() {
		return pMPBts3kmRadius;
	}

	/**
	 * @param pMPBts3kmRadius the pMPBts3kmRadius to set
	 */
	public void setpMPBts3kmRadius(String pMPBts3kmRadius) {
		this.pMPBts3kmRadius = pMPBts3kmRadius;
	}

	/**
	 * @return the sectorNetworkCheck
	 */
	public String getSectorNetworkCheck() {
		return sectorNetworkCheck;
	}

	/**
	 * @param sectorNetworkCheck the sectorNetworkCheck to set
	 */
	public void setSectorNetworkCheck(String sectorNetworkCheck) {
		this.sectorNetworkCheck = sectorNetworkCheck;
	}

	/**
	 * @return the selectedSolutionBw
	 */
	public String getSelectedSolutionBw() {
		return selectedSolutionBw;
	}

	/**
	 * @param selectedSolutionBw the selectedSolutionBw to set
	 */
	public void setSelectedSolutionBw(String selectedSolutionBw) {
		this.selectedSolutionBw = selectedSolutionBw;
	}

	/**
	 * @return the productDotNme
	 */
	public String getProductDotNme() {
		return productDotNme;
	}

	/**
	 * @param productDotNme the productDotNme to set
	 */
	public void setProductDotNme(String productDotNme) {
		this.productDotNme = productDotNme;
	}

	/**
	 * @return the onnet0_5kmAvgBwMbps
	 */
	public String getOnnet0_5kmAvgBwMbps() {
		return onnet0_5kmAvgBwMbps;
	}

	/**
	 * @param onnet0_5kmAvgBwMbps the onnet0_5kmAvgBwMbps to set
	 */
	public void setOnnet0_5kmAvgBwMbps(String onnet0_5kmAvgBwMbps) {
		this.onnet0_5kmAvgBwMbps = onnet0_5kmAvgBwMbps;
	}

	/**
	 * @return the onnet0_5kmCustCount
	 */
	public String getOnnet0_5kmCustCount() {
		return onnet0_5kmCustCount;
	}

	/**
	 * @param onnet0_5kmCustCount the onnet0_5kmCustCount to set
	 */
	public void setOnnet0_5kmCustCount(String onnet0_5kmCustCount) {
		this.onnet0_5kmCustCount = onnet0_5kmCustCount;
	}

	/**
	 * @return the onnet0_5kmMinDistanceKilometers
	 */
	public String getOnnet0_5kmMinDistanceKilometers() {
		return onnet0_5kmMinDistanceKilometers;
	}

	/**
	 * @param onnet0_5kmMinDistanceKilometers the onnet0_5kmMinDistanceKilometers to set
	 */
	public void setOnnet0_5kmMinDistanceKilometers(String onnet0_5kmMinDistanceKilometers) {
		this.onnet0_5kmMinDistanceKilometers = onnet0_5kmMinDistanceKilometers;
	}

	/**
	 * @return the onnet2kmAvgBwMbps
	 */
	public String getOnnet2kmAvgBwMbps() {
		return onnet2kmAvgBwMbps;
	}

	/**
	 * @param onnet2kmAvgBwMbps the onnet2kmAvgBwMbps to set
	 */
	public void setOnnet2kmAvgBwMbps(String onnet2kmAvgBwMbps) {
		this.onnet2kmAvgBwMbps = onnet2kmAvgBwMbps;
	}

	/**
	 * @return the onnet2kmAvgDistancekilometers
	 */
	public String getOnnet2kmAvgDistancekilometers() {
		return onnet2kmAvgDistancekilometers;
	}

	/**
	 * @param onnet2kmAvgDistancekilometers the onnet2kmAvgDistancekilometers to set
	 */
	public void setOnnet2kmAvgDistancekilometers(String onnet2kmAvgDistancekilometers) {
		this.onnet2kmAvgDistancekilometers = onnet2kmAvgDistancekilometers;
	}

	/**
	 * @return the onnet2kmCustCount
	 */
	public String getOnnet2kmCustCount() {
		return onnet2kmCustCount;
	}

	/**
	 * @param onnet2kmCustCount the onnet2kmCustCount to set
	 */
	public void setOnnet2kmCustCount(String onnet2kmCustCount) {
		this.onnet2kmCustCount = onnet2kmCustCount;
	}

	/**
	 * @return the onnet2kmMaxBwMbps
	 */
	public String getOnnet2kmMaxBwMbps() {
		return onnet2kmMaxBwMbps;
	}

	/**
	 * @param onnet2kmMaxBwMbps the onnet2kmMaxBwMbps to set
	 */
	public void setOnnet2kmMaxBwMbps(String onnet2kmMaxBwMbps) {
		this.onnet2kmMaxBwMbps = onnet2kmMaxBwMbps;
	}

	/**
	 * @return the onnet2kmMinBwMbps
	 */
	public String getOnnet2kmMinBwMbps() {
		return onnet2kmMinBwMbps;
	}

	/**
	 * @param onnet2kmMinBwMbps the onnet2kmMinBwMbps to set
	 */
	public void setOnnet2kmMinBwMbps(String onnet2kmMinBwMbps) {
		this.onnet2kmMinBwMbps = onnet2kmMinBwMbps;
	}

	/**
	 * @return the onnet5kmAvgBwMbps
	 */
	public String getOnnet5kmAvgBwMbps() {
		return onnet5kmAvgBwMbps;
	}

	/**
	 * @param onnet5kmAvgBwMbps the onnet5kmAvgBwMbps to set
	 */
	public void setOnnet5kmAvgBwMbps(String onnet5kmAvgBwMbps) {
		this.onnet5kmAvgBwMbps = onnet5kmAvgBwMbps;
	}

	/**
	 * @return the onnet5kmAvgDistanceKilometers
	 */
	public String getOnnet5kmAvgDistanceKilometers() {
		return onnet5kmAvgDistanceKilometers;
	}

	/**
	 * @param onnet5kmAvgDistanceKilometers the onnet5kmAvgDistanceKilometers to set
	 */
	public void setOnnet5kmAvgDistanceKilometers(String onnet5kmAvgDistanceKilometers) {
		this.onnet5kmAvgDistanceKilometers = onnet5kmAvgDistanceKilometers;
	}

	/**
	 * @return the onnet5kmCustCount
	 */
	public String getOnnet5kmCustCount() {
		return onnet5kmCustCount;
	}

	/**
	 * @param onnet5kmCustCount the onnet5kmCustCount to set
	 */
	public void setOnnet5kmCustCount(String onnet5kmCustCount) {
		this.onnet5kmCustCount = onnet5kmCustCount;
	}

	/**
	 * @return the onnet5kmMaxBwMbps
	 */
	public String getOnnet5kmMaxBwMbps() {
		return onnet5kmMaxBwMbps;
	}

	/**
	 * @param onnet5kmMaxBwMbps the onnet5kmMaxBwMbps to set
	 */
	public void setOnnet5kmMaxBwMbps(String onnet5kmMaxBwMbps) {
		this.onnet5kmMaxBwMbps = onnet5kmMaxBwMbps;
	}

	/**
	 * @return the onnet5kmMinBwMbps
	 */
	public String getOnnet5kmMinBwMbps() {
		return onnet5kmMinBwMbps;
	}

	/**
	 * @param onnet5kmMinBwMbps the onnet5kmMinBwMbps to set
	 */
	public void setOnnet5kmMinBwMbps(String onnet5kmMinBwMbps) {
		this.onnet5kmMinBwMbps = onnet5kmMinBwMbps;
	}

	/**
	 * @return the mux
	 */
	public String getMux() {
		return mux;
	}

	/**
	 * @param mux the mux to set
	 */
	public void setMux(String mux) {
		this.mux = mux;
	}

	/**
	 * @return the mast3kmCustCount
	 */
	public String getMast3kmCustCount() {
		return mast3kmCustCount;
	}

	/**
	 * @param mast3kmCustCount the mast3kmCustCount to set
	 */
	public void setMast3kmCustCount(String mast3kmCustCount) {
		this.mast3kmCustCount = mast3kmCustCount;
	}

	/**
	 * @return the mast3kmMaxMastHt
	 */
	public String getMast3kmMaxMastHt() {
		return mast3kmMaxMastHt;
	}

	/**
	 * @param mast3kmMaxMastHt the mast3kmMaxMastHt to set
	 */
	public void setMast3kmMaxMastHt(String mast3kmMaxMastHt) {
		this.mast3kmMaxMastHt = mast3kmMaxMastHt;
	}

	/**
	 * @return the mast3kmMinMastHt
	 */
	public String getMast3kmMinMastHt() {
		return mast3kmMinMastHt;
	}

	/**
	 * @param mast3kmMinMastHt the mast3kmMinMastHt to set
	 */
	public void setMast3kmMinMastHt(String mast3kmMinMastHt) {
		this.mast3kmMinMastHt = mast3kmMinMastHt;
	}

	/**
	 * @return the mast3kmNearestMastHt
	 */
	public String getMast3kmNearestMastHt() {
		return mast3kmNearestMastHt;
	}

	/**
	 * @param mast3kmNearestMastHt the mast3kmNearestMastHt to set
	 */
	public void setMast3kmNearestMastHt(String mast3kmNearestMastHt) {
		this.mast3kmNearestMastHt = mast3kmNearestMastHt;
	}

	/**
	 * @return the lmOtcInr
	 */
	public String getLmOtcInr() {
		return lmOtcInr;
	}

	/**
	 * @param lmOtcInr the lmOtcInr to set
	 */
	public void setLmOtcInr(String lmOtcInr) {
		this.lmOtcInr = lmOtcInr;
	}

	/**
	 * @return the lmArcInr
	 */
	public String getLmArcInr() {
		return lmArcInr;
	}

	/**
	 * @param lmArcInr the lmArcInr to set
	 */
	public void setLmArcInr(String lmArcInr) {
		this.lmArcInr = lmArcInr;
	}

	/**
	 * @return the lmCost
	 */
	public String getLmCost() {
		return lmCost;
	}

	/**
	 * @param lmCost the lmCost to set
	 */
	public void setLmCost(String lmCost) {
		this.lmCost = lmCost;
	}

	/**
	 * @return the hubNode
	 */
	public String getHubNode() {
		return hubNode;
	}

	/**
	 * @param hubNode the hubNode to set
	 */
	public void setHubNode(String hubNode) {
		this.hubNode = hubNode;
	}

	/**
	 * @return the intrface
	 */
	public String getIntrface() {
		return intrface;
	}

	/**
	 * @param intrface the intrface to set
	 */
	public void setIntrface(String intrface) {
		this.intrface = intrface;
	}

	/**
	 * @return the coreRing
	 */
	public String getCoreRing() {
		return coreRing;
	}

	/**
	 * @param coreRing the coreRing to set
	 */
	public void setCoreRing(String coreRing) {
		this.coreRing = coreRing;
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
	 * @return the btsClosestInfraProvider
	 */
	public String getBtsClosestInfraProvider() {
		return btsClosestInfraProvider;
	}

	/**
	 * @param btsClosestInfraProvider the btsClosestInfraProvider to set
	 */
	public void setBtsClosestInfraProvider(String btsClosestInfraProvider) {
		this.btsClosestInfraProvider = btsClosestInfraProvider;
	}

	/**
	 * @return the btsClosestSiteType
	 */
	public String getBtsClosestSiteType() {
		return btsClosestSiteType;
	}

	/**
	 * @param btsClosestSiteType the btsClosestSiteType to set
	 */
	public void setBtsClosestSiteType(String btsClosestSiteType) {
		this.btsClosestSiteType = btsClosestSiteType;
	}

	/**
	 * @return the btsFlag
	 */
	public String getBtsFlag() {
		return btsFlag;
	}

	/**
	 * @param btsFlag the btsFlag to set
	 */
	public void setBtsFlag(String btsFlag) {
		this.btsFlag = btsFlag;
	}

	/**
	 * @return the btsIpPmp
	 */
	public String getBtsIpPmp() {
		return btsIpPmp;
	}

	/**
	 * @param btsIpPmp the btsIpPmp to set
	 */
	public void setBtsIpPmp(String btsIpPmp) {
		this.btsIpPmp = btsIpPmp;
	}

	/**
	 * @return the backhaulNetworkCheck
	 */
	public String getBackhaulNetworkCheck() {
		return backhaulNetworkCheck;
	}

	/**
	 * @param backhaulNetworkCheck the backhaulNetworkCheck to set
	 */
	public void setBackhaulNetworkCheck(String backhaulNetworkCheck) {
		this.backhaulNetworkCheck = backhaulNetworkCheck;
	}

	/**
	 * @return the backhaulNetworkCheckReason
	 */
	public String getBackhaulNetworkCheckReason() {
		return backhaulNetworkCheckReason;
	}

	/**
	 * @param backhaulNetworkCheckReason the backhaulNetworkCheckReason to set
	 */
	public void setBackhaulNetworkCheckReason(String backhaulNetworkCheckReason) {
		this.backhaulNetworkCheckReason = backhaulNetworkCheckReason;
	}

	/**
	 * @return the avg3kmMastHtCost
	 */
	public String getAvg3kmMastHtCost() {
		return avg3kmMastHtCost;
	}

	/**
	 * @param avg3kmMastHtCost the avg3kmMastHtCost to set
	 */
	public void setAvg3kmMastHtCost(String avg3kmMastHtCost) {
		this.avg3kmMastHtCost = avg3kmMastHtCost;
	}

	/**
	 * @return the accessRingsHh
	 */
	public String getAccessRingsHh() {
		return accessRingsHh;
	}

	/**
	 * @param accessRingsHh the accessRingsHh to set
	 */
	public void setAccessRingsHh(String accessRingsHh) {
		this.accessRingsHh = accessRingsHh;
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
	public String getServiceCommissionedDate() {
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

	public String getMuxIp() {
		return muxIp;
	}

	public void setMuxIp(String muxIp) {
		this.muxIp = muxIp;
	}

	public String getMuxPort() {
		return muxPort;
	}

	public void setMuxPort(String muxPort) {
		this.muxPort = muxPort;
	}

	public String getSelectedAccessRing() {
		return selectedAccessRing;
	}

	public void setSelectedAccessRing(String selectedAccessRing) {
		this.selectedAccessRing = selectedAccessRing;
	}

	public String getBtsLat() {
		return btsLat;
	}

	public void setBtsLat(String btsLat) {
		this.btsLat = btsLat;
	}

	public String getBtsLong() {
		return btsLong;
	}

	public void setBtsLong(String btsLong) {
		this.btsLong = btsLong;
	}

	public String getBtsSiteName() {
		return btsSiteName;
	}

	public void setBtsSiteName(String btsSiteName) {
		this.btsSiteName = btsSiteName;
	}

	public String getBHCapacity() {
		return BHCapacity;
	}

	public void setBHCapacity(String bHCapacity) {
		BHCapacity = bHCapacity;
	}

	public String getBH_Type() {
		return BH_Type;
	}

	public void setBH_Type(String bH_Type) {
		BH_Type = bH_Type;
	}

	public String getBHConnectivity() {
		return BHConnectivity;
	}

	public void setBHConnectivity(String bHConnectivity) {
		BHConnectivity = bHConnectivity;
	}

	public String getBtsDeviceType() {
		return btsDeviceType;
	}

	public void setBtsDeviceType(String btsDeviceType) {
		this.btsDeviceType = btsDeviceType;
	}

	public String getBtsAzimuth() {
		return btsAzimuth;
	}

	public void setBtsAzimuth(String btsAzimuth) {
		this.btsAzimuth = btsAzimuth;
	}

	public String getSectorId() {
		return sectorId;
	}

	public void setSectorId(String sectorId) {
		this.sectorId = sectorId;
	}

	public String getSECTORNAME() {
		return SECTORNAME;
	}

	public void setSECTORNAME(String sECTORNAME) {
		SECTORNAME = sECTORNAME;
	}

	@JsonProperty("GVPN_Port_MRC_Adjusted")
	public String getGvpnPortMrcAdjusted() { return gvpnPortMrcAdjusted; }

	@JsonProperty("GVPN_Port_MRC_Adjusted")
	public void setGvpnPortMrcAdjusted(String gvpnPortMrcAdjusted) { this.gvpnPortMrcAdjusted = gvpnPortMrcAdjusted; }

	@JsonProperty("GVPN_Port_NRC_Adjusted")
	public String getGvpnPortNrcAdjusted() { return gvpnPortNrcAdjusted; }

	@JsonProperty("GVPN_Port_NRC_Adjusted")
	public void setGvpnPortNrcAdjusted(String gvpnPortNrcAdjusted) { this.gvpnPortNrcAdjusted = gvpnPortNrcAdjusted; }

	@JsonProperty("CPE_Installation_charges")
	public String getCpeInstallationCharges() { return cpeInstallationCharges; }

	@JsonProperty("CPE_Installation_charges")
	public void setCpeInstallationCharges(String cpeInstallationCharges) { this.cpeInstallationCharges = cpeInstallationCharges; }

	@JsonProperty("support_charges")
	public String getSupportCharges() { return supportCharges; }

	@JsonProperty("support_charges")
	public void setSupportCharges(String supportCharges) { this.supportCharges = supportCharges; }

	@JsonProperty("recovery")
	public String getRecovery() { return recovery; }

	@JsonProperty("recovery")
	public void setRecovery(String recovery) {
		this.recovery = recovery;
	}
	@JsonProperty("management")
	public String getManagement() { return management; }

	@JsonProperty("management")
	public void setManagement(String management) { this.management = management; }

	@JsonProperty("SFP_lp")
	public String getSfpIp() { return sfpIp; }

	@JsonProperty("SFP_lp")
	public void setSfpIp(String sfpIp) { this.sfpIp = sfpIp; }

	@JsonProperty("customs_local_taxes")
	public String getCustomsLocalTaxes() { return customsLocalTaxes; }

	@JsonProperty("customs_local_taxes")
	public void setCustomsLocalTaxes(String customsLocalTaxes) { this.customsLocalTaxes = customsLocalTaxes; }

	@JsonProperty("Logistics_cost")
	public String getLogisticsCost() { return logisticsCost; }

	@JsonProperty("Logistics_cost")
	public void setLogisticsCost(String logisticsCost) { this.logisticsCost = logisticsCost; }

	@JsonProperty("provider_MRCCost")
	public String getProviderMrcCost() { return providerMrcCost; }

	@JsonProperty("provider_MRCCost")
	public void setProviderMrcCost(String providerMrcCost) { this.providerMrcCost = providerMrcCost; }

	@JsonProperty("provider_NRCCost")
	public String getProviderNrcCost() { return providerNrcCost; }

	@JsonProperty("provider_NRCCost")
	public void setProviderNrcCost(String providerNrcCost) { this.providerNrcCost = providerNrcCost; }

	@JsonProperty("x_connect_Xconnect_MRC")
	public String getxConnectXconnectMrc() { return xConnectXconnectMrc; }

	@JsonProperty("x_connect_Xconnect_MRC")
	public void setxConnectXconnectMrc(String xConnectXconnectMrc) { this.xConnectXconnectMrc = xConnectXconnectMrc; }

	@JsonProperty("x_connect_Xconnect_NRC")
	public String getxConnectXconnectNrc() { return xConnectXconnectNrc; }

	@JsonProperty("x_connect_Xconnect_NRC")
	public void setxConnectXconnectNrc(String xConnectXconnectNrc) { this.xConnectXconnectNrc = xConnectXconnectNrc; }

	//manual feasibility subcomponent commercial start
	
	@JsonProperty("otc_modem_charges")
	private String otcModemCharges;
	
	@JsonProperty("lm_nrc_bw_prov_ofwl")
	private String lmNrcBWProvOfwl;
	
	@JsonProperty("arc_modem_charges")
	private String arcModemCharges;
	
	@JsonProperty("arc_bw")
	private String arcBw;
	
	@JsonProperty("lm_nrc_prow_onwl")
	private String lmNrcProwOnwl;
	
	@JsonProperty("lm_arc_prow_onwl")
	private String lmArcProwOnwl;
	
	@JsonProperty("lm_arc_converter_charges_onrf")
	private String lmArcConverterChargesOnrf;
	
	@JsonProperty("lm_arc_bw_backhaul_onrf")
	private String lmArcBwBackhaulOnrf ;
	
	@JsonProperty("lm_arc_colocation_onrf")
	private String lmArcColocationOnrf;
	
	@JsonProperty("lm_otc_modem_charges_offwl")
	private String lmOtcModemChargesOffwl;
	
	@JsonProperty("lm_otc_nrc_installation_offwl")
	private String lmOtcNrcInstallationOffwl;
	
	@JsonProperty("lm_arc_modem_charges_offwl")
	private String lmArcModemChargesOffwl;
	
	@JsonProperty("lm_arc_bw_offwl")
	private String lmArcBwOffwl;
	
	@JsonProperty("provider_name")
	private String providerName;
	

	@JsonProperty("mast_type")
	private String mastType;
	
	@JsonProperty("record_type")
	private String recordType;
	
	@JsonProperty("provider_reference_number")
	private String providerReferenceNumber;
	
	
	
	
	public String getMastType() {
		return mastType;
	}
	public void setMastType(String mastType) {
		this.mastType = mastType;
	}
	public String getRecordType() {
		return recordType;
	}
	public void setRecordType(String recordType) {
		this.recordType = recordType;
	}
	
	@JsonProperty("provider_reference_number")
	public String getProviderReferenceNumber() {
		return providerReferenceNumber;
	}
	@JsonProperty("provider_reference_number")
	public void setProviderReferenceNumber(String providerReferenceNumber) {
		this.providerReferenceNumber = providerReferenceNumber;
	}


	@JsonProperty("provider_name")
	public String getProviderName() {
		return providerName;
	}
	@JsonProperty("provider_name")
	public void setProviderName(String providerName) {
		this.providerName = providerName;
	}

	@JsonProperty("otc_modem_charges")
    public String getOtcModemCharges() {
		return otcModemCharges;
	}

	@JsonProperty("otc_modem_charges")
	public void setOtcModemCharges(String otcModemCharges) {
		this.otcModemCharges = otcModemCharges;
	}

	@JsonProperty("lm_nrc_bw_prov_ofwl")
	public String getLmNrcBWProvOfwl() {
		return lmNrcBWProvOfwl;
	}

	@JsonProperty("lm_nrc_bw_prov_ofwl")
	public void setLmNrcBWProvOfwl(String lmNrcBWProvOfwl) {
		this.lmNrcBWProvOfwl = lmNrcBWProvOfwl;
	}

	@JsonProperty("arc_modem_charges")
	public String getArcModemCharges() {
		return arcModemCharges;
	}

	@JsonProperty("arc_modem_charges")
	public void setArcModemCharges(String arcModemCharges) {
		this.arcModemCharges = arcModemCharges;
	}

	@JsonProperty("arc_bw")
	public String getArcBw() {
		return arcBw;
	}

	@JsonProperty("arc_bw")
	public void setArcBw(String arcBw) {
		this.arcBw = arcBw;
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

	@JsonProperty("lm_arc_colocation_onrf")
	public String getLmArcColocationOnrf() {
		return lmArcColocationOnrf;
	}

	@JsonProperty("lm_arc_colocation_onrf")
	public void setLmArcColocationOnrf(String lmArcColocationOnrf) {
		this.lmArcColocationOnrf = lmArcColocationOnrf;
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
	public String getLmArcBwOffwl() {
		return lmArcBwOffwl;
	}
	@JsonProperty("lm_arc_bw_offwl")
	public void setLmArcBwOffwl(String lmArcBwOffwl) {
		this.lmArcBwOffwl = lmArcBwOffwl;
	}
	//manual feasibility subcomponent commercial end
	public String getProwCostType() {
		return prowCostType;
	}
	public void setProwCostType(String prowCostType) {
		this.prowCostType = prowCostType;
	}
	public String getProwMessage() {
		return ProwMessage;
	}
	public void setProwMessage(String prowMessage) {
		ProwMessage = prowMessage;
	}
	public String getProwFlag() {
		return prowFlag;
	}
	public void setProwFlag(String prowFlag) {
		this.prowFlag = prowFlag;
	}
	public String getLmArcProwGponOnwl() {
		return lmArcProwGponOnwl;
	}
	public void setLmArcProwGponOnwl(String lmArcProwGponOnwl) {
		this.lmArcProwGponOnwl = lmArcProwGponOnwl;
	}
	public String getLmNrcProwGponOnwl() {
		return lmNrcProwGponOnwl;
	}
	public void setLmNrcProwGponOnwl(String lmNrcProwGponOnwl) {
		this.lmNrcProwGponOnwl = lmNrcProwGponOnwl;
	}
	public String getLmArcProwProwOnwl() {
		return lmArcProwProwOnwl;
	}
	public void setLmArcProwProwOnwl(String lmArcProwProwOnwl) {
		this.lmArcProwProwOnwl = lmArcProwProwOnwl;
	}
	public String getLmNrcProwProwOnwl() {
		return lmNrcProwProwOnwl;
	}
	public void setLmNrcProwProwOnwl(String lmNrcProwProwOnwl) {
		this.lmNrcProwProwOnwl = lmNrcProwProwOnwl;
	}
	public String getPrimaryBtsIpAddress() {
		return primaryBtsIpAddress;
	}
	public void setPrimaryBtsIpAddress(String primaryBtsIpAddress) {
		this.primaryBtsIpAddress = primaryBtsIpAddress;
	}
	public String getPrimaryBtsName() {
		return primaryBtsName;
	}
	public void setPrimaryBtsName(String primaryBtsName) {
		this.primaryBtsName = primaryBtsName;
	}
	public String getPrimaryFirstSectorId() {
		return primaryFirstSectorId;
	}
	public void setPrimaryFirstSectorId(String primaryFirstSectorId) {
		this.primaryFirstSectorId = primaryFirstSectorId;
	}
	public String getDeliveryTimeLine() {
		return deliveryTimeLine;
	}
	public void setDeliveryTimeLine(String deliveryTimeLine) {
		this.deliveryTimeLine = deliveryTimeLine;
	}
	public String getFeasibilityRemarks() {
		return feasibilityRemarks;
	}
	public void setFeasibilityRemarks(String feasibilityRemarks) {
		this.feasibilityRemarks = feasibilityRemarks;
	}
	public String getResponseType() {
		return responseType;
	}
	public void setResponseType(String responseType) {
		this.responseType = responseType;
	}
	public String getValidityPeriod() {
		return validityPeriod;
	}
	public void setValidityPeriod(String validityPeriod) {
		this.validityPeriod = validityPeriod;
	}
	public String getFeasibilityResponseId() {
		return feasibilityResponseId;
	}
	public void setFeasibilityResponseId(String feasibilityResponseId) {
		this.feasibilityResponseId = feasibilityResponseId;
	}
	public String getProviderResponseDate() {
		return providerResponseDate;
	}
	public void setProviderResponseDate(String providerResponseDate) {
		this.providerResponseDate = providerResponseDate;
	}
	public String getBtsEquipmentType() {
		return btsEquipmentType;
	}
	public void setBtsEquipmentType(String btsEquipmentType) {
		this.btsEquipmentType = btsEquipmentType;
	}
	public String getBtsHt() {
		return btsHt;
	}
	public void setBtsHt(String btsHt) {
		this.btsHt = btsHt;
	}
	public String getBtsSiteAddress() {
		return btsSiteAddress;
	}
	public void setBtsSiteAddress(String btsSiteAddress) {
		this.btsSiteAddress = btsSiteAddress;
	}
	public String getIsCustomer() {
		return isCustomer;
	}
	public void setIsCustomer(String isCustomer) {
		this.isCustomer = isCustomer;
	}
	
	public String getAccessFeasibilityUpdated() {
		return accessFeasibilityUpdated;
	}
	public void setAccessFeasibilityUpdated(String accessFeasibilityUpdated) {
		this.accessFeasibilityUpdated = accessFeasibilityUpdated;
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

	//PIPF-145

	@JsonProperty("grc_version_name")
	public String getGrcVersionName() {
		return grcVersionName;
	}

	@JsonProperty("grc_version_name")
	public void setGrcVersionName(String grcVersionName) {
		this.grcVersionName = grcVersionName;
	}

	@JsonProperty("bts_tagged_comment")
	public String getBtsTaggedComment() {
		return btsTaggedComment;
	}

	@JsonProperty("bts_tagged_comment")
	public void setBtsTaggedComment(String btsTaggedComment) {
		this.btsTaggedComment = btsTaggedComment;
	}

	@JsonProperty("bts_sector_id")
	public String getBtsSectorId() {
		return btsSectorId;
	}

	@JsonProperty("bts_sector_id")
	public void setBtsSectorId(String btsSectorId) {
		this.btsSectorId = btsSectorId;
	}

	@JsonProperty("bts_sector_name")
	public String getBtsSectorName() {
		return btsSectorName;
	}

	@JsonProperty("bts_sector_name")
	public void setBtsSectorName(String btsSectorName) {
		this.btsSectorName = btsSectorName;
	}
	public String getLmArcColocationChargesOnrf() {
		return lmArcColocationChargesOnrf;
	}
	public void setLmArcColocationChargesOnrf(String lmArcColocationChargesOnrf) {
		this.lmArcColocationChargesOnrf = lmArcColocationChargesOnrf;
	}
	public String getCpeHwCtc() {
		return cpeHwCtc;
	}
	public void setCpeHwCtc(String cpeHwCtc) {
		this.cpeHwCtc = cpeHwCtc;
	}
	public String getCpeInstallationCtc() {
		return cpeInstallationCtc;
	}
	public void setCpeInstallationCtc(String cpeInstallationCtc) {
		this.cpeInstallationCtc = cpeInstallationCtc;
	}
	public String getCpeSupportCtc() {
		return cpeSupportCtc;
	}
	public void setCpeSupportCtc(String cpeSupportCtc) {
		this.cpeSupportCtc = cpeSupportCtc;
	}
}


package com.tcl.dias.oms.pricing.bean;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.tcl.dias.common.beans.AccessRingInfo;
import com.tcl.dias.common.beans.MuxDetailsItem;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({ "lm_nrc_ospcapex_onwl", "Mast_3KM_avg_mast_ht", "solution_type", "onnet_5km_Avg_BW_Mbps",
	"lm_arc_bw_prov_ofrf", "lm_nrc_nerental_onwl", "lm_nrc_inbldg_onwl", "Mast_3KM_cust_count",
	"Network_Feasibility_Check", "interim_BW", "onnet_0_5km_Avg_BW_Mbps", "lm_arc_bw_onrf",
	"prospect_0_5km_Min_BW_Mbps", "onnet_5km_cust_Count", "lm_nrc_bw_prov_ofrf", "onnet_0_5km_cust_Count",
	"local_loop_interface", "latitude_final", "topology", "Mast_3KM_nearest_mast_ht", "lm_nrc_bw_onwl",
	"cpe_supply_type", "prospect_2km_feasibility_pct", "additional_ip_flag", "Orch_Connection",
	"Backhaul_Network_check", "site_id", "cpe_management_type", "sales_org", "onnet_2km_Avg_BW_Mbps",
	"opportunity_term", "error_flag", "resp_city", "prospect_2km_Avg_DistanceKilometers",
	"prospect_2km_Sum_Feasibility_flag", "prospect_2km_Avg_BW_Mbps", "Orch_LM_Type", "ipv4_address_pool_size",
	"onnet_2km_cust_Count", "lm_nrc_mast_ofrf", "onnet_5km_Max_BW_Mbps", "lm_nrc_mast_onrf", "error_msg",
	"connection_type", "ip_address_arrangement", "onnet_0_5km_Min_DistanceKilometers", "Mast_3KM_min_mast_ht",
	"customer_segment", "Nearest_Mast_ht_cost", "time_taken", "prospect_0_5km_Sum_Feasibility_flag", "Type",
	"bts_Closest_Site_type", "longitude_final", "Orch_BW", "LM_OTC_INR", "Interface", "prospect_2km_Max_BW_Mbps",
	"ipv6_address_pool_size", "onnet_2km_Min_BW_Mbps", "Selected_solution_BW", "prospect_name",
	"PMP_bts_3km_radius", "Predicted_Access_Feasibility", "last_mile_contract_term",
	"prospect_0_5km_feasibility_pct", "Backhaul_Network_check_reason", "onnet_2km_Avg_DistanceKilometers",
	"Avg_3KM_Mast_ht_cost", "burstable_bw", "bw_mbps", "bts_flag", "bts_IP_PMP", "prospect_0_5km_Max_BW_Mbps",
	"product_name", "LM_ARC_INR", "bts_min_dist_km", "error_code", "lm_arc_bw_onwl", "account_id_with_18_digit",
	"Orch_Category", "lm_nrc_mux_onwl", "bts_num_BTS", "feasibility_response_created_date",
	"prospect_0_5km_Min_DistanceKilometers", "Sector_network_check", "Bandwidth", "P2P_bts_3km_radius",
	"cpe_variant", "prospect_2km_Min_BW_Mbps", "onnet_5km_Avg_DistanceKilometers", "sum_no_of_sites_uni_len",
	"onnet_2km_Max_BW_Mbps", "onnet_5km_Min_BW_Mbps", "LM_Cost", "Mast_3KM_max_mast_ht",
	"prospect_0_5km_Avg_BW_Mbps", "bts_Closest_infra_provider", "Probabililty_Access_Feasibility", "lm_nrc_bw_onrf",
	"quotetype_quote", "closest_provider_bso_name", "a_customer_segment", "b_customer_segment","cu_le_id", "X5km_prospect_perc_feasible", "X5km_prospect_num_feasible",
	"X5km_prospect_min_dist","macd_service",
	"ll_change",
	"macd_option","parallel_run_days",
	"trigger_feasibility",
	"old_contract_term",
	"service_commissioned_date",
	"lat_long","old_Port_Bw","old_Ll_Bw","service_id","backup_port_requested","cpe_chassis_changed","local_loop_bw","Compressed_Internet_Ratio",
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
	"user_name",
	"user_type",
	"error_msg_display",
	"mux_details",
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
	"izo_sdwan",
	
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
	"provider_name",
	"vendor_name",
	"vendor_id",
	"provider_reference_number",
	"tcl_pop_address",

		//CST-10 Customer portal
		"is_customer",

		//PIPF-145
		"grc_version_name","bts_sector_id","bts_sector_name","bts_tagged_comment",

		// PIPF152
		"feasibility_response_id",
		"task_id",
		
		//Planned Cost Table for Auto Budget WBS allocation
		"lm_arc_colocation_charges_onrf",
		"lm_arc_orderable_bw_onwl",
		"lm_arc_radwin_bw_onrf",
		"lm_nrc_network_capex_onwl",
		"lm_otc_nrc_orderable_bw_onwl",
		"cpe_hw_ctc",
		"cpe_installation_ctc",
		"cpe_support_ctc"

})
public class NotFeasible {

	@JsonProperty("local_loop_bw")
	private Double localLoopBw;
	@JsonProperty("cpe_chassis_changed")
	private String cpeChassisChanged;
	@JsonProperty("old_Port_Bw")
	private String oldPortBw;
	@JsonProperty("service_id")
	private String serviceId;
	@JsonProperty("old_Ll_Bw")
	private String oldLlBw;
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
	@JsonProperty("lm_nrc_ospcapex_onwl")
	private String lmNrcOspcapexOnwl;
	@JsonProperty("Mast_3KM_avg_mast_ht")
	private String mast3KMAvgMastHt;
	@JsonProperty("solution_type")
	private String solutionType;
	@JsonProperty("onnet_5km_Avg_BW_Mbps")
	private String onnet5kmAvgBWMbps;
	@JsonProperty("lm_arc_bw_prov_ofrf")
	private String lmArcBwProvOfrf;
	@JsonProperty("lm_nrc_nerental_onwl")
	private String lmNrcNerentalOnwl;
	@JsonProperty("lm_nrc_inbldg_onwl")
	private String lmNrcInbldgOnwl;
	@JsonProperty("Mast_3KM_cust_count")
	private String mast3KMCustCount;
	@JsonProperty("Network_Feasibility_Check")
	private String networkFeasibilityCheck;
	@JsonProperty("interim_BW")
	private String interimBW;
	@JsonProperty("onnet_0_5km_Avg_BW_Mbps")
	private String onnet05kmAvgBWMbps;
	@JsonProperty("lm_arc_bw_onrf")
	private String lmArcBwOnrf;
	@JsonProperty("prospect_0_5km_Min_BW_Mbps")
	private String prospect05kmMinBWMbps;
	@JsonProperty("onnet_5km_cust_Count")
	private String onnet5kmCustCount;
	@JsonProperty("lm_nrc_bw_prov_ofrf")
	private String lmNrcBwProvOfrf;
	@JsonProperty("closest_provider_bso_name")
	private String closestProviderBsoName;
	@JsonProperty("onnet_0_5km_cust_Count")
	private String onnet05kmCustCount;
	@JsonProperty("local_loop_interface")
	private String localLoopInterface;
	@JsonProperty("latitude_final")
	private Float latitudeFinal;
	@JsonProperty("topology")
	private String topology;
	@JsonProperty("Mast_3KM_nearest_mast_ht")
	private String mast3KMNearestMastHt;
	@JsonProperty("lm_nrc_bw_onwl")
	private String lmNrcBwOnwl;
	@JsonProperty("cpe_supply_type")
	private String cpeSupplyType;
	@JsonProperty("prospect_2km_feasibility_pct")
	private String prospect2kmFeasibilityPct;
	@JsonProperty("additional_ip_flag")
	private String additionalIpFlag;
	@JsonProperty("Orch_Connection")
	private String orchConnection;
	@JsonProperty("Backhaul_Network_check")
	private String backhaulNetworkCheck;
	@JsonProperty("site_id")
	private String siteId;
	@JsonProperty("avg_mast_ht")
	private Float avgMastHt;
	@JsonProperty("cpe_management_type")
	private String cpeManagementType;
	@JsonProperty("sales_org")
	private String salesOrg;
	@JsonProperty("onnet_2km_Avg_BW_Mbps")
	private String onnet2kmAvgBWMbps;
	@JsonProperty("opportunity_term")
	private Integer opportunityTerm;
	@JsonProperty("error_flag")
	private Integer errorFlag;
	@JsonProperty("resp_city")
	private String respCity;
	@JsonProperty("prospect_2km_Avg_DistanceKilometers")
	private String prospect2kmAvgDistanceKilometers;
	@JsonProperty("prospect_2km_Sum_Feasibility_flag")
	private String prospect2kmSumFeasibilityFlag;
	@JsonProperty("prospect_2km_Avg_BW_Mbps")
	private String prospect2kmAvgBWMbps;
	@JsonProperty("Orch_LM_Type")
	private String orchLMType;
	@JsonProperty("ipv4_address_pool_size")
	private String ipv4AddressPoolSize;
	@JsonProperty("onnet_2km_cust_Count")
	private String onnet2kmCustCount;
	@JsonProperty("lm_nrc_mast_ofrf")
	private String lmNrcMastOfrf;
	@JsonProperty("onnet_5km_Max_BW_Mbps")
	private String onnet5kmMaxBWMbps;
	@JsonProperty("lm_nrc_mast_onrf")
	private String lmNrcMastOnrf;
	@JsonProperty("error_msg")
	private String errorMsg;
	@JsonProperty("connection_type")
	private String connectionType;
	@JsonProperty("ip_address_arrangement")
	private String ipAddressArrangement;
	@JsonProperty("onnet_0_5km_Min_DistanceKilometers")
	private String onnet05kmMinDistanceKilometers;
	@JsonProperty("Mast_3KM_min_mast_ht")
	private String mast3KMMinMastHt;
	@JsonProperty("customer_segment")
	private String customerSegment;
	@JsonProperty("Nearest_Mast_ht_cost")
	private String nearestMastHtCost;
	@JsonProperty("time_taken")
	private String timeTaken;
	@JsonProperty("prospect_0_5km_Sum_Feasibility_flag")
	private String prospect05kmSumFeasibilityFlag;
	@JsonProperty("Type")
	private String type;
	@JsonProperty("bts_Closest_Site_type")
	private String btsClosestSiteType;
	@JsonProperty("longitude_final")
	private Float longitudeFinal;
	@JsonProperty("Orch_BW")
	private String orchBW;
	@JsonProperty("LM_OTC_INR")
	private String lMOTCINR;
	@JsonProperty("Interface")
	private String _interface;
	@JsonProperty("prospect_2km_Max_BW_Mbps")
	private String prospect2kmMaxBWMbps;
	@JsonProperty("ipv6_address_pool_size")
	private String ipv6AddressPoolSize;
	@JsonProperty("onnet_2km_Min_BW_Mbps")
	private String onnet2kmMinBWMbps;
	@JsonProperty("Selected_solution_BW")
	private String selectedSolutionBW;
	@JsonProperty("prospect_name")
	private String prospectName;
	@JsonProperty("PMP_bts_3km_radius")
	private String pMPBts3kmRadius;
	@JsonProperty("Predicted_Access_Feasibility")
	private String predictedAccessFeasibility;
	@JsonProperty("last_mile_contract_term")
	private String lastMileContractTerm;
	@JsonProperty("prospect_0_5km_feasibility_pct")
	private String prospect05kmFeasibilityPct;
	@JsonProperty("Backhaul_Network_check_reason")
	private String backhaulNetworkCheckReason;
	@JsonProperty("onnet_2km_Avg_DistanceKilometers")
	private String onnet2kmAvgDistanceKilometers;
	@JsonProperty("Avg_3KM_Mast_ht_cost")
	private String avg3KMMastHtCost;
	@JsonProperty("burstable_bw")
	private Double burstableBw;
	@JsonProperty("bw_mbps")
	private Double bwMbps;
	@JsonProperty("bts_flag")
	private String btsFlag;
	@JsonProperty("bts_IP_PMP")
	private String btsIPPMP;
	@JsonProperty("prospect_0_5km_Max_BW_Mbps")
	private String prospect05kmMaxBWMbps;
	@JsonProperty("product_name")
	private String productName;
	@JsonProperty("LM_ARC_INR")
	private String lMARCINR;
	@JsonProperty("bts_min_dist_km")
	private String btsMinDistKm;
	@JsonProperty("error_code")
	private String errorCode;
	@JsonProperty("lm_arc_bw_onwl")
	private String lmArcBwOnwl;
	@JsonProperty("account_id_with_18_digit")
	private String accountIdWith18Digit;
	@JsonProperty("Orch_Category")
	private String orchCategory;
	@JsonProperty("lm_nrc_mux_onwl")
	private String lmNrcMuxOnwl;
	@JsonProperty("bts_num_BTS")
	private String btsNumBTS;
	@JsonProperty("feasibility_response_created_date")
	private String feasibilityResponseCreatedDate;
	@JsonProperty("prospect_0_5km_Min_DistanceKilometers")
	private String prospect05kmMinDistanceKilometers;
	@JsonProperty("Sector_network_check")
	private String sectorNetworkCheck;
	@JsonProperty("Bandwidth")
	private String bandwidth;
	@JsonProperty("P2P_bts_3km_radius")
	private String p2PBts3kmRadius;
	@JsonProperty("cpe_variant")
	private String cpeVariant;
	@JsonProperty("prospect_2km_Min_BW_Mbps")
	private String prospect2kmMinBWMbps;
	@JsonProperty("onnet_5km_Avg_DistanceKilometers")
	private String onnet5kmAvgDistanceKilometers;
	@JsonProperty("sum_no_of_sites_uni_len")
	private Integer sumNoOfSitesUniLen;
	@JsonProperty("onnet_2km_Max_BW_Mbps")
	private String onnet2kmMaxBWMbps;
	@JsonProperty("onnet_5km_Min_BW_Mbps")
	private String onnet5kmMinBWMbps;
	@JsonProperty("LM_Cost")
	private String lMCost;
	@JsonProperty("Mast_3KM_max_mast_ht")
	private String mast3KMMaxMastHt;
	@JsonProperty("prospect_0_5km_Avg_BW_Mbps")
	private String prospect05kmAvgBWMbps;
	@JsonProperty("bts_Closest_infra_provider")
	private String btsClosestInfraProvider;
	@JsonProperty("Probabililty_Access_Feasibility")
	private String probabililtyAccessFeasibility;
	@JsonProperty("lm_nrc_bw_onrf")
	private String lmNrcBwOnrf;
	@JsonProperty("quotetype_quote")
	private String quotetypeQuote;
	@JsonProperty("cu_le_id")
	private String cuLeId;
	@JsonProperty("X5km_prospect_perc_feasible")
	private String X5kmProspectPercFeasible;
	@JsonProperty("X5km_prospect_num_feasible")
	private String x5kmProspectNumFeasible;
	@JsonProperty("X5km_prospect_min_dist")
	private String x5kmProspectMinDist;
	@JsonProperty("X5km_prospect_min_bw")
	private String x5kmProspectMinBw;
	@JsonProperty("X5km_prospect_max_bw")
	private String x5kmProspectMaxBw;
	@JsonProperty("X5km_prospect_count")
	private String x5kmProspectCount;
	@JsonProperty("X5km_prospect_avg_dist")
	private String x5kmProspectAvgDist;
	@JsonProperty("X2km_prospect_max_bw")
	private String x2kmProspectMaxBw;
	@JsonProperty("X2km_prospect_min_bw")
	private String x2kmProspectMinBw;
	@JsonProperty("X2km_prospect_min_dist")
	private String x2kmProspectMinDist;
	@JsonProperty("X2km_prospect_num_feasible")
	private String x2kmProspectNumFeasible;
	@JsonProperty("X2km_prospect_perc_feasible")
	private String x2kmProspectPercFeasible;
	@JsonProperty("X5km_avg_bw")
	private String x5kmAvgBw;
	@JsonProperty("X5km_avg_dist")
	private String x5kmAvgDist;
	@JsonProperty("X5km_cust_count")
	private String x5kmCustCount;
	@JsonProperty("X5km_max_bw")
	private String x5kmMaxBw;
	@JsonProperty("X5km_min_bw")
	private String x5kmMinBw;
	@JsonProperty("X5km_min_dist")
	private String x5kmMinDist;
	@JsonProperty("X5km_prospect_avg_bw")
	private String x5kmProspectAvgBw;
	@JsonProperty("total_cost")
	private String totalCost;
	@JsonProperty("X0.5km_avg_bw")
	private String x0_5kmAvgBw;
	@JsonProperty("X0.5km_avg_dist")
	private String x0_5kmAvgDist;
	@JsonProperty("X0.5km_cust_count")
	private String x0_5kmCustCount;
	@JsonProperty("X0.5km_max_bw")
	private String x0_5kmMaxBw;
	@JsonProperty("X0.5km_min_bw")
	private String x0_5kmMinBw;
	@JsonProperty("X0.5km_min_dist")
	private String x0_5kmMinDist;
	@JsonProperty("X0.5km_prospect_avg_bw")
	private String x0_5kmProspectAvgBw;
	@JsonProperty("X0.5km_prospect_avg_dist")
	private String x0_5kmProspectAvgDist;
	@JsonProperty("X0.5km_prospect_count")
	private String x0_5ProspectCount;
	@JsonProperty("X0.5km_prospect_max_bw")
	private String x0_5kmProspectMaxBw;
	@JsonProperty("X0.5km_prospect_min_bw")
	private String x0_5kmProspectMinBw;
	@JsonProperty("X0.5km_prospect_min_dist")
	private String x0_5kmProspectMinDist;
	@JsonProperty("X0.5km_prospect_num_feasible")
	private String x0_5kmProspectNumFeasible;
	@JsonProperty("X0.5km_prospect_perc_feasible")
	private String x0_5kmProspectPercFeasible;
	@JsonProperty("X2km_avg_bw")
	private String x2kmAvgBw;
	@JsonProperty("X2km_avg_dist")
	private String x2kmAvgDist;
	@JsonProperty("X2km_cust_count")
	private String x2kmCustCount;
	@JsonProperty("X2km_max_bw")
	private String x2kmMaxBw;
	@JsonProperty("X2km_min_bw")
	private String x2kmMinBw;
	@JsonProperty("X2km_min_dist")
	private String x2kmMinDist;
	@JsonProperty("X2km_prospect_avg_bw")
	private String x2kmProspectAvgBw;
	@JsonProperty("X2km_prospect_avg_dist")
	private String x2kmProspectAvgDist;
	@JsonProperty("X2km_prospect_count")
	private String x2kmProspectCount;
	@JsonProperty("siteFlag")
	private String siteFlag;
	@JsonProperty("Service_ID")
	private String serviceID;
	@JsonProperty("scenario_1")
	private String scenario1;
	@JsonProperty("scenario_2")
	private String scenario2;
	@JsonProperty("provider_min_dist")
	private String providerMinDist;
	@JsonProperty("provider_tot_towers")
	private String providerTotTowers;
	@JsonProperty("prospect_2km_cust_Count")
	private String prospect2kmCustCount;
	@JsonProperty("Product.Name")
	private String productDotNme;
	@JsonProperty("POP_TCL_Access")
	private String popTclAccess;
	@JsonProperty("pop_lat")
	private String popLat;
	@JsonProperty("pop_long")
	private String popLong;
	@JsonProperty("pop_name")
	private String popName;
	@JsonProperty("pop_network_loc_id")
	private String popNetworkLocId;
	@JsonProperty("POP_Network_Location_Type")
	private String popNetworkLocaationType;
	@JsonProperty("POP_DIST_KM_SERVICE_MOD")
	private String popDistKmServiceMod;
	@JsonProperty("pop_address")
	private String popAddress;
	@JsonProperty("POP_Building_Type")
	private String popBuildingType;
	@JsonProperty("POP_Category")
	private String popCategory;
	@JsonProperty("POP_Construction_Status")
	private String popConstructionStatus;
	@JsonProperty("POP_DIST_KM")
	private String popDistKm;
	@JsonProperty("POP_DIST_KM_SERVICE")
	private String popDistKmService;
	@JsonProperty("otc_sify")
	private String otcSify;
	@JsonProperty("otc_tikona")
	private String otcTikona;
	@JsonProperty("OnnetCity_tag")
	private String onnetCityTag;
	@JsonProperty("num_connected_building")
	private String numConnectedBuilding;
	@JsonProperty("num_connected_cust")
	private String numConnectedCust;
	@JsonProperty("offnet_0_5km_Avg_BW_Mbps")
	private String offnet0_5kmAvgBwMbps;
	@JsonProperty("offnet_0_5km_cust_Count")
	private String offnet0_5kmCustCount;
	@JsonProperty("offnet_0_5km_Max_BW_Mbps")
	private String offnet0_5kmMaxBwMbps;
	@JsonProperty("offnet_0_5km_Min_accuracy_num")
	private String offnet0_5kmMinAccuracyNum;
	@JsonProperty("offnet_0_5km_Min_BW_Mbps")
	private String offnet0_5kmMinBwMbps;
	@JsonProperty("offnet_0_5km_Min_DistanceKilometers")
	private String offnet0_5kmMinDistanceKilometers;
	@JsonProperty("offnet_2km_Avg_BW_Mbps")
	private String offnet2kmAvgBwMbps;
	@JsonProperty("offnet_2km_cust_Count")
	private String offnet2kmCustCount;
	@JsonProperty("offnet_2km_Min_accuracy_num")
	private String offnet2kmMinAccuracyNum;
	@JsonProperty("offnet_2km_Min_BW_Mbps")
	private String offnet2kmMinBwMbps;
	@JsonProperty("offnet_2km_Min_DistanceKilometers")
	private String offnet2kmMinDistanceKilometers;
	@JsonProperty("offnet_5km_Avg_BW_Mbps")
	private String offnet5kmAvgBwMbps;
	@JsonProperty("offnet_5km_Max_BW_Mbps")
	private String offnet5kmMaxBwMbps;
	@JsonProperty("offnet_5km_Min_accuracy_num")
	private String offnet5kmMinAccuracyNum;
	@JsonProperty("offnet_5km_Min_BW_Mbps")
	private String offnet5kmMinBwMbps;
	@JsonProperty("offnet_5km_Min_DistanceKilometers")
	private String offnet5kmMinDistanceKilometers;
	@JsonProperty("max_mast_ht")
	private String maxMastHt;
	@JsonProperty("min_hh_fatg")
	private String minHhFatg;
	@JsonProperty("min_mast_ht")
	private String minMastHt;
	@JsonProperty("mux")
	private String mux;
	@JsonProperty("nearest_mast_ht")
	private String nearestMastHt;
	@JsonProperty("net_pre_feasible_flag")
	private String netPreFeasibleFlag;
	@JsonProperty("Network_F_NF_CC")
	private String networkFNFCc;
	@JsonProperty("Network_F_NF_CC_Flag")
	private String networkFNFCCFlag;
	@JsonProperty("Network_F_NF_HH")
	private String networkFNFHh;
	@JsonProperty("Network_F_NF_HH_Flag")
	private String networkFNFHhFlag;
	@JsonProperty("hub_node")
	private String hubNode;
	@JsonProperty("HH_0_5km")
	private String hh0_5km;
	@JsonProperty("HH_DIST_KM")
	private String hhDistKm;
	@JsonProperty("hh_flag")
	private String hhFlag;
	@JsonProperty("hh_name")
	private String hhName;
	@JsonProperty("FATG_Building_Type")
	private String fatgBuildingType;
	@JsonProperty("FATG_Category")
	private String fatgCategory;
	@JsonProperty("FATG_DIST_KM")
	private String fatgDistKm;
	@JsonProperty("FATG_Network_Location_Type")
	private String fatgNetworkLocationType;
	@JsonProperty("FATG_PROW")
	private String fatgProw;
	@JsonProperty("FATG_Ring_type")
	private String fatgRingType;
	@JsonProperty("FATG_TCL_Access")
	private String fatgTclAccess;
	@JsonProperty("cust_count")
	private String custCount;
	@JsonProperty("country")
	private String country;
	@JsonProperty("cost_permeter")
	private String costPerMeter;
	@JsonProperty("core_ring")
	private String coreRing;
	@JsonProperty("core_check_CC")
	private String coreCheckCc;
	@JsonProperty("core_check_hh")
	private String coreCheckHh;
	@JsonProperty("closest_provider_site")
	private String closestProviderSite;
	@JsonProperty("closest_provider_site_addr")
	private String cloasestProviderSiteAddr;
	@JsonProperty("connected_building_tag")
	private String connectedBuildingTag;
	@JsonProperty("connected_cust_tag")
	private String connectedCustTag;
	@JsonProperty("city_tier")
	private String cityTier;
	@JsonProperty("closest_provider")
	private String closestProvider;
	@JsonProperty("bw_flag_3")
	private String bwFlag3;
	@JsonProperty("bw_flag_32")
	private String bwFlag32;
	@JsonProperty("backup_port_requested")
	private String backupPortRequested;
	@JsonProperty("arc_sify")
	private String arcSify;
	@JsonProperty("arc_tikona")
	private String arcTikona;
	@JsonProperty("access_rings_hh")
	private String accessRingsHh;
	@JsonProperty("access_rings")
	private List<AccessRingInfo> accessRings;
	@JsonProperty("access_check_CC")
	private String accessCheckCc;
	@JsonProperty("access_check_hh")
	private String accessCheckHh;

	// NPL related attributes - start
	@JsonProperty("a_customer_segment")
	private String customerSegmentA;
	@JsonProperty("b_customer_segment")
	private String customerSegmentB;
	@JsonProperty("parallel_run_days")
	private String parallelRunDays;
	
	@JsonProperty("Compressed_Internet_Ratio")
	private String compressedInternetRatio;
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
	
	@JsonProperty("user_name")
	private String userName;
	@JsonProperty("user_type")
	private String userType;
	
	@JsonProperty("error_msg_display")
	private String errorMsgDisplay;

	@JsonProperty("mux_details")
	private List<MuxDetailsItem> muxDetails;
	
	// added for offnet wireline
	
	@JsonProperty("otc_modem_charges")
	private String otcModemCharges;
		
	@JsonProperty("lm_nrc_bw_prov_ofwl")
	private String lmNrcBWProvOfwl;
		
	@JsonProperty("arc_modem_charges")
	private String arcModemCharges;
		
	@JsonProperty("arc_bw")
	private String arcBw;

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


	//manual feasability subcomponent commercial
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
		
		@JsonProperty("vendor_name")
		private String vendorName;
		
		@JsonProperty("vendor_id")
		private String vendorId;
		
		@JsonProperty("tcl_pop_address")
		private String tclPopAddress;
		
		@JsonProperty("provider_reference_number")
		private String providerReferenceNumber;
		
		@JsonProperty("bts_equipment_type")
		private String btsEquipmentType;
		@JsonProperty("bts_ht")
		private String btsHt;
		@JsonProperty("bts_site_address")
		private String btsSiteAddress;
		
		@JsonProperty("resp_state")
		private String respState;	
		

		// added for PIPF -152
		@JsonProperty("task_id")
		private String taskId;

		@JsonProperty("feasibility_response_id")
		private String feasibilityResponseId;
		
		
		//Planned Cost Table for Auto Budget WBS allocation
		@JsonProperty("lm_arc_colocation_charges_onrf")
		private String lmArcColocationChargesOnrf;
		
		@JsonProperty("lm_arc_orderable_bw_onwl")
		private String lmArcOrderableBwOnwl;
		
		@JsonProperty("lm_arc_radwin_bw_onrf")
		private String lmArcRadwinBwOnrf;
		
		@JsonProperty("lm_nrc_network_capex_onwl")
		private String lmNrcNetworkCapexOnwl;
		
		@JsonProperty("lm_otc_nrc_orderable_bw_onwl")
		private String lmOtcNrcOrderableBwOnwl;
		
		@JsonProperty("cpe_hw_ctc")
		private String cpeHwCtc;
		
		@JsonProperty("cpe_installation_ctc")
		private String cpeInstallationCtc;
		
		@JsonProperty("cpe_support_ctc")
		private String cpeSupportCtc;
		
		public String getFeasibilityResponseId() {
			return feasibilityResponseId;
		}
		public void setFeasibilityResponseId(String feasibilityResponseId) {
			this.feasibilityResponseId = feasibilityResponseId;
		}
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

		@JsonProperty("is_customer")
		private String isCustomer;
		
		@JsonProperty("Access_feasibility_updated")
		private String accessFeasibilityUpdated;
     	@JsonProperty("is_colo")
		private String isColo;



	@JsonProperty("provider_reference_number")
		public String getProviderReferenceNumber() {
			return providerReferenceNumber;
		}
		@JsonProperty("provider_reference_number")
		public void setProviderReferenceNumber(String providerReferenceNumber) {
			this.providerReferenceNumber = providerReferenceNumber;
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
		
		
		//Additional Attributes for system response end

		@JsonProperty("provider_name")
		public String getProviderName() {
			return providerName;
		}
		@JsonProperty("provider_name")
		public void setProviderName(String providerName) {
			this.providerName = providerName;
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
// Additional attr for system response - end
	
		// added for offnet wireline --- start

	public String getOtcModemCharges() {
		return otcModemCharges;
	}

	public void setOtcModemCharges(String otcModemCharges) {
		this.otcModemCharges = otcModemCharges;
	}

	public String getLmNrcBWProvOfwl() {
		return lmNrcBWProvOfwl;
	}

	public void setLmNrcBWProvOfwl(String lmNrcBWProvOfwl) {
		this.lmNrcBWProvOfwl = lmNrcBWProvOfwl;
	}

	public String getArcModemCharges() {
		return arcModemCharges;
	}

	public void setArcModemCharges(String arcModemCharges) {
		this.arcModemCharges = arcModemCharges;
	}

	public String getArcBw() {
		return arcBw;
	}

	public void setArcBw(String arcBw) {
		this.arcBw = arcBw;
	}

	// added for offnet wireline --- End

	@JsonProperty("mux_details")
	public List<MuxDetailsItem> getMuxDetails() {
		return muxDetails;
	}

	@JsonProperty("mux_details")
	public void setMuxDetails(List<MuxDetailsItem> muxDetails) {
		this.muxDetails = muxDetails;
	}
	

	@JsonProperty("error_msg_display")
	public String getErrorMsgDisplay() {
		return errorMsgDisplay;
	}

	@JsonProperty("error_msg_display")
	public void setErrorMsgDisplay(String errorMsgDisplay) {
		this.errorMsgDisplay = errorMsgDisplay;
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

	@JsonProperty("Compressed_Internet_Ratio")
	public String getCompressedInternetRatio() {
		return compressedInternetRatio;
	}
	
	@JsonProperty("Compressed_Internet_Ratio")
	public void setCompressedInternetRatio(String compressedInternetRatio) {
		this.compressedInternetRatio = compressedInternetRatio;
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
	public String getParallelRunDays() {
		return parallelRunDays;
	}

	@JsonProperty("parallel_run_days")
	public void setParallelRunDays(String parallelRunDays) {
		this.parallelRunDays = parallelRunDays;
	}


	@JsonProperty("a_customer_segment")
	public String getCustomerSegmentA() {
		return customerSegmentA;
	}

	@JsonProperty("a_customer_segment")
	public void setCustomerSegmentA(String customerSegmentA) {
		this.customerSegmentA = customerSegmentA;
	}

	@JsonProperty("b_customer_segment")
	public String getCustomerSegmentB() {
		return customerSegmentB;
	}

	@JsonProperty("b_customer_segment")
	public void setCustomerSegmentB(String customerSegmentB) {
		this.customerSegmentB = customerSegmentB;
	}
	// NPL related attributes - end

	@JsonProperty("lm_nrc_ospcapex_onwl")
	public String getLmNrcOspcapexOnwl() {
		return lmNrcOspcapexOnwl;
	}

	@JsonProperty("lm_nrc_ospcapex_onwl")
	public void setLmNrcOspcapexOnwl(String lmNrcOspcapexOnwl) {
		this.lmNrcOspcapexOnwl = lmNrcOspcapexOnwl;
	}

	@JsonProperty("Mast_3KM_avg_mast_ht")
	public String getMast3KMAvgMastHt() {
		return mast3KMAvgMastHt;
	}

	@JsonProperty("Mast_3KM_avg_mast_ht")
	public void setMast3KMAvgMastHt(String mast3KMAvgMastHt) {
		this.mast3KMAvgMastHt = mast3KMAvgMastHt;
	}

	@JsonProperty("solution_type")
	public String getSolutionType() {
		return solutionType;
	}

	@JsonProperty("solution_type")
	public void setSolutionType(String solutionType) {
		this.solutionType = solutionType;
	}

	@JsonProperty("onnet_5km_Avg_BW_Mbps")
	public String getOnnet5kmAvgBWMbps() {
		return onnet5kmAvgBWMbps;
	}

	@JsonProperty("onnet_5km_Avg_BW_Mbps")
	public void setOnnet5kmAvgBWMbps(String onnet5kmAvgBWMbps) {
		this.onnet5kmAvgBWMbps = onnet5kmAvgBWMbps;
	}

	@JsonProperty("lm_arc_bw_prov_ofrf")
	public String getLmArcBwProvOfrf() {
		return lmArcBwProvOfrf;
	}

	@JsonProperty("lm_arc_bw_prov_ofrf")
	public void setLmArcBwProvOfrf(String lmArcBwProvOfrf) {
		this.lmArcBwProvOfrf = lmArcBwProvOfrf;
	}

	@JsonProperty("lm_nrc_nerental_onwl")
	public String getLmNrcNerentalOnwl() {
		return lmNrcNerentalOnwl;
	}

	@JsonProperty("lm_nrc_nerental_onwl")
	public void setLmNrcNerentalOnwl(String lmNrcNerentalOnwl) {
		this.lmNrcNerentalOnwl = lmNrcNerentalOnwl;
	}

	@JsonProperty("lm_nrc_inbldg_onwl")
	public String getLmNrcInbldgOnwl() {
		return lmNrcInbldgOnwl;
	}

	@JsonProperty("lm_nrc_inbldg_onwl")
	public void setLmNrcInbldgOnwl(String lmNrcInbldgOnwl) {
		this.lmNrcInbldgOnwl = lmNrcInbldgOnwl;
	}

	@JsonProperty("Mast_3KM_cust_count")
	public String getMast3KMCustCount() {
		return mast3KMCustCount;
	}

	@JsonProperty("Mast_3KM_cust_count")
	public void setMast3KMCustCount(String mast3KMCustCount) {
		this.mast3KMCustCount = mast3KMCustCount;
	}

	@JsonProperty("Network_Feasibility_Check")
	public String getNetworkFeasibilityCheck() {
		return networkFeasibilityCheck;
	}

	@JsonProperty("Network_Feasibility_Check")
	public void setNetworkFeasibilityCheck(String networkFeasibilityCheck) {
		this.networkFeasibilityCheck = networkFeasibilityCheck;
	}

	@JsonProperty("interim_BW")
	public String getInterimBW() {
		return interimBW;
	}

	@JsonProperty("interim_BW")
	public void setInterimBW(String interimBW) {
		this.interimBW = interimBW;
	}

	@JsonProperty("onnet_0_5km_Avg_BW_Mbps")
	public String getOnnet05kmAvgBWMbps() {
		return onnet05kmAvgBWMbps;
	}

	@JsonProperty("onnet_0_5km_Avg_BW_Mbps")
	public void setOnnet05kmAvgBWMbps(String onnet05kmAvgBWMbps) {
		this.onnet05kmAvgBWMbps = onnet05kmAvgBWMbps;
	}

	@JsonProperty("lm_arc_bw_onrf")
	public String getLmArcBwOnrf() {
		return lmArcBwOnrf;
	}

	@JsonProperty("lm_arc_bw_onrf")
	public void setLmArcBwOnrf(String lmArcBwOnrf) {
		this.lmArcBwOnrf = lmArcBwOnrf;
	}

	@JsonProperty("prospect_0_5km_Min_BW_Mbps")
	public String getProspect05kmMinBWMbps() {
		return prospect05kmMinBWMbps;
	}

	@JsonProperty("prospect_0_5km_Min_BW_Mbps")
	public void setProspect05kmMinBWMbps(String prospect05kmMinBWMbps) {
		this.prospect05kmMinBWMbps = prospect05kmMinBWMbps;
	}

	@JsonProperty("onnet_5km_cust_Count")
	public String getOnnet5kmCustCount() {
		return onnet5kmCustCount;
	}

	@JsonProperty("onnet_5km_cust_Count")
	public void setOnnet5kmCustCount(String onnet5kmCustCount) {
		this.onnet5kmCustCount = onnet5kmCustCount;
	}

	@JsonProperty("lm_nrc_bw_prov_ofrf")
	public String getLmNrcBwProvOfrf() {
		return lmNrcBwProvOfrf;
	}

	@JsonProperty("lm_nrc_bw_prov_ofrf")
	public void setLmNrcBwProvOfrf(String lmNrcBwProvOfrf) {
		this.lmNrcBwProvOfrf = lmNrcBwProvOfrf;
	}

	@JsonProperty("onnet_0_5km_cust_Count")
	public String getOnnet05kmCustCount() {
		return onnet05kmCustCount;
	}

	@JsonProperty("onnet_0_5km_cust_Count")
	public void setOnnet05kmCustCount(String onnet05kmCustCount) {
		this.onnet05kmCustCount = onnet05kmCustCount;
	}

	@JsonProperty("local_loop_interface")
	public String getLocalLoopInterface() {
		return localLoopInterface;
	}

	@JsonProperty("local_loop_interface")
	public void setLocalLoopInterface(String localLoopInterface) {
		this.localLoopInterface = localLoopInterface;
	}

	@JsonProperty("latitude_final")
	public Float getLatitudeFinal() {
		return latitudeFinal;
	}

	@JsonProperty("latitude_final")
	public void setLatitudeFinal(Float latitudeFinal) {
		this.latitudeFinal = latitudeFinal;
	}

	@JsonProperty("topology")
	public String getTopology() {
		return topology;
	}

	@JsonProperty("topology")
	public void setTopology(String topology) {
		this.topology = topology;
	}

	@JsonProperty("Mast_3KM_nearest_mast_ht")
	public String getMast3KMNearestMastHt() {
		return mast3KMNearestMastHt;
	}

	@JsonProperty("Mast_3KM_nearest_mast_ht")
	public void setMast3KMNearestMastHt(String mast3KMNearestMastHt) {
		this.mast3KMNearestMastHt = mast3KMNearestMastHt;
	}

	@JsonProperty("lm_nrc_bw_onwl")
	public String getLmNrcBwOnwl() {
		return lmNrcBwOnwl;
	}

	@JsonProperty("lm_nrc_bw_onwl")
	public void setLmNrcBwOnwl(String lmNrcBwOnwl) {
		this.lmNrcBwOnwl = lmNrcBwOnwl;
	}

	@JsonProperty("cpe_supply_type")
	public String getCpeSupplyType() {
		return cpeSupplyType;
	}

	@JsonProperty("cpe_supply_type")
	public void setCpeSupplyType(String cpeSupplyType) {
		this.cpeSupplyType = cpeSupplyType;
	}

	@JsonProperty("prospect_2km_feasibility_pct")
	public String getProspect2kmFeasibilityPct() {
		return prospect2kmFeasibilityPct;
	}

	@JsonProperty("prospect_2km_feasibility_pct")
	public void setProspect2kmFeasibilityPct(String prospect2kmFeasibilityPct) {
		this.prospect2kmFeasibilityPct = prospect2kmFeasibilityPct;
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

	@JsonProperty("Backhaul_Network_check")
	public String getBackhaulNetworkCheck() {
		return backhaulNetworkCheck;
	}

	@JsonProperty("Backhaul_Network_check")
	public void setBackhaulNetworkCheck(String backhaulNetworkCheck) {
		this.backhaulNetworkCheck = backhaulNetworkCheck;
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

	@JsonProperty("onnet_2km_Avg_BW_Mbps")
	public String getOnnet2kmAvgBWMbps() {
		return onnet2kmAvgBWMbps;
	}

	@JsonProperty("onnet_2km_Avg_BW_Mbps")
	public void setOnnet2kmAvgBWMbps(String onnet2kmAvgBWMbps) {
		this.onnet2kmAvgBWMbps = onnet2kmAvgBWMbps;
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

	@JsonProperty("prospect_2km_Avg_DistanceKilometers")
	public String getProspect2kmAvgDistanceKilometers() {
		return prospect2kmAvgDistanceKilometers;
	}

	@JsonProperty("prospect_2km_Avg_DistanceKilometers")
	public void setProspect2kmAvgDistanceKilometers(String prospect2kmAvgDistanceKilometers) {
		this.prospect2kmAvgDistanceKilometers = prospect2kmAvgDistanceKilometers;
	}

	@JsonProperty("prospect_2km_Sum_Feasibility_flag")
	public String getProspect2kmSumFeasibilityFlag() {
		return prospect2kmSumFeasibilityFlag;
	}

	@JsonProperty("prospect_2km_Sum_Feasibility_flag")
	public void setProspect2kmSumFeasibilityFlag(String prospect2kmSumFeasibilityFlag) {
		this.prospect2kmSumFeasibilityFlag = prospect2kmSumFeasibilityFlag;
	}

	@JsonProperty("prospect_2km_Avg_BW_Mbps")
	public String getProspect2kmAvgBWMbps() {
		return prospect2kmAvgBWMbps;
	}

	@JsonProperty("prospect_2km_Avg_BW_Mbps")
	public void setProspect2kmAvgBWMbps(String prospect2kmAvgBWMbps) {
		this.prospect2kmAvgBWMbps = prospect2kmAvgBWMbps;
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

	@JsonProperty("onnet_2km_cust_Count")
	public String getOnnet2kmCustCount() {
		return onnet2kmCustCount;
	}

	@JsonProperty("onnet_2km_cust_Count")
	public void setOnnet2kmCustCount(String onnet2kmCustCount) {
		this.onnet2kmCustCount = onnet2kmCustCount;
	}

	@JsonProperty("lm_nrc_mast_ofrf")
	public String getLmNrcMastOfrf() {
		return lmNrcMastOfrf;
	}

	@JsonProperty("lm_nrc_mast_ofrf")
	public void setLmNrcMastOfrf(String lmNrcMastOfrf) {
		this.lmNrcMastOfrf = lmNrcMastOfrf;
	}

	@JsonProperty("onnet_5km_Max_BW_Mbps")
	public String getOnnet5kmMaxBWMbps() {
		return onnet5kmMaxBWMbps;
	}

	@JsonProperty("onnet_5km_Max_BW_Mbps")
	public void setOnnet5kmMaxBWMbps(String onnet5kmMaxBWMbps) {
		this.onnet5kmMaxBWMbps = onnet5kmMaxBWMbps;
	}

	@JsonProperty("lm_nrc_mast_onrf")
	public String getLmNrcMastOnrf() {
		return lmNrcMastOnrf;
	}

	@JsonProperty("lm_nrc_mast_onrf")
	public void setLmNrcMastOnrf(String lmNrcMastOnrf) {
		this.lmNrcMastOnrf = lmNrcMastOnrf;
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

	@JsonProperty("onnet_0_5km_Min_DistanceKilometers")
	public String getOnnet05kmMinDistanceKilometers() {
		return onnet05kmMinDistanceKilometers;
	}

	@JsonProperty("onnet_0_5km_Min_DistanceKilometers")
	public void setOnnet05kmMinDistanceKilometers(String onnet05kmMinDistanceKilometers) {
		this.onnet05kmMinDistanceKilometers = onnet05kmMinDistanceKilometers;
	}

	@JsonProperty("Mast_3KM_min_mast_ht")
	public String getMast3KMMinMastHt() {
		return mast3KMMinMastHt;
	}

	@JsonProperty("Mast_3KM_min_mast_ht")
	public void setMast3KMMinMastHt(String mast3KMMinMastHt) {
		this.mast3KMMinMastHt = mast3KMMinMastHt;
	}

	@JsonProperty("customer_segment")
	public String getCustomerSegment() {
		return customerSegment;
	}

	@JsonProperty("customer_segment")
	public void setCustomerSegment(String customerSegment) {
		this.customerSegment = customerSegment;
	}

	@JsonProperty("Nearest_Mast_ht_cost")
	public String getNearestMastHtCost() {
		return nearestMastHtCost;
	}

	@JsonProperty("Nearest_Mast_ht_cost")
	public void setNearestMastHtCost(String nearestMastHtCost) {
		this.nearestMastHtCost = nearestMastHtCost;
	}

	@JsonProperty("time_taken")
	public String getTimeTaken() {
		return timeTaken;
	}

	@JsonProperty("time_taken")
	public void setTimeTaken(String timeTaken) {
		this.timeTaken = timeTaken;
	}

	@JsonProperty("prospect_0_5km_Sum_Feasibility_flag")
	public String getProspect05kmSumFeasibilityFlag() {
		return prospect05kmSumFeasibilityFlag;
	}

	@JsonProperty("prospect_0_5km_Sum_Feasibility_flag")
	public void setProspect05kmSumFeasibilityFlag(String prospect05kmSumFeasibilityFlag) {
		this.prospect05kmSumFeasibilityFlag = prospect05kmSumFeasibilityFlag;
	}

	@JsonProperty("Type")
	public String getType() {
		return type;
	}

	@JsonProperty("Type")
	public void setType(String type) {
		this.type = type;
	}

	@JsonProperty("bts_Closest_Site_type")
	public String getBtsClosestSiteType() {
		return btsClosestSiteType;
	}

	@JsonProperty("bts_Closest_Site_type")
	public void setBtsClosestSiteType(String btsClosestSiteType) {
		this.btsClosestSiteType = btsClosestSiteType;
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
	public String getOrchBW() {
		return orchBW;
	}

	@JsonProperty("Orch_BW")
	public void setOrchBW(String orchBW) {
		this.orchBW = orchBW;
	}

	@JsonProperty("LM_OTC_INR")
	public String getLMOTCINR() {
		return lMOTCINR;
	}

	@JsonProperty("LM_OTC_INR")
	public void setLMOTCINR(String lMOTCINR) {
		this.lMOTCINR = lMOTCINR;
	}

	@JsonProperty("Interface")
	public String getInterface() {
		return _interface;
	}

	@JsonProperty("Interface")
	public void setInterface(String _interface) {
		this._interface = _interface;
	}

	@JsonProperty("prospect_2km_Max_BW_Mbps")
	public String getProspect2kmMaxBWMbps() {
		return prospect2kmMaxBWMbps;
	}

	@JsonProperty("prospect_2km_Max_BW_Mbps")
	public void setProspect2kmMaxBWMbps(String prospect2kmMaxBWMbps) {
		this.prospect2kmMaxBWMbps = prospect2kmMaxBWMbps;
	}

	@JsonProperty("ipv6_address_pool_size")
	public String getIpv6AddressPoolSize() {
		return ipv6AddressPoolSize;
	}

	@JsonProperty("ipv6_address_pool_size")
	public void setIpv6AddressPoolSize(String ipv6AddressPoolSize) {
		this.ipv6AddressPoolSize = ipv6AddressPoolSize;
	}

	@JsonProperty("onnet_2km_Min_BW_Mbps")
	public String getOnnet2kmMinBWMbps() {
		return onnet2kmMinBWMbps;
	}

	@JsonProperty("onnet_2km_Min_BW_Mbps")
	public void setOnnet2kmMinBWMbps(String onnet2kmMinBWMbps) {
		this.onnet2kmMinBWMbps = onnet2kmMinBWMbps;
	}

	@JsonProperty("Selected_solution_BW")
	public String getSelectedSolutionBW() {
		return selectedSolutionBW;
	}

	@JsonProperty("Selected_solution_BW")
	public void setSelectedSolutionBW(String selectedSolutionBW) {
		this.selectedSolutionBW = selectedSolutionBW;
	}

	@JsonProperty("prospect_name")
	public String getProspectName() {
		return prospectName;
	}

	@JsonProperty("prospect_name")
	public void setProspectName(String prospectName) {
		this.prospectName = prospectName;
	}

	@JsonProperty("PMP_bts_3km_radius")
	public String getPMPBts3kmRadius() {
		return pMPBts3kmRadius;
	}

	@JsonProperty("PMP_bts_3km_radius")
	public void setPMPBts3kmRadius(String pMPBts3kmRadius) {
		this.pMPBts3kmRadius = pMPBts3kmRadius;
	}

	@JsonProperty("Predicted_Access_Feasibility")
	public String getPredictedAccessFeasibility() {
		return predictedAccessFeasibility;
	}

	@JsonProperty("Predicted_Access_Feasibility")
	public void setPredictedAccessFeasibility(String predictedAccessFeasibility) {
		this.predictedAccessFeasibility = predictedAccessFeasibility;
	}

	@JsonProperty("last_mile_contract_term")
	public String getLastMileContractTerm() {
		return lastMileContractTerm;
	}

	@JsonProperty("last_mile_contract_term")
	public void setLastMileContractTerm(String lastMileContractTerm) {
		this.lastMileContractTerm = lastMileContractTerm;
	}

	@JsonProperty("prospect_0_5km_feasibility_pct")
	public String getProspect05kmFeasibilityPct() {
		return prospect05kmFeasibilityPct;
	}

	@JsonProperty("prospect_0_5km_feasibility_pct")
	public void setProspect05kmFeasibilityPct(String prospect05kmFeasibilityPct) {
		this.prospect05kmFeasibilityPct = prospect05kmFeasibilityPct;
	}

	@JsonProperty("Backhaul_Network_check_reason")
	public String getBackhaulNetworkCheckReason() {
		return backhaulNetworkCheckReason;
	}

	@JsonProperty("Backhaul_Network_check_reason")
	public void setBackhaulNetworkCheckReason(String backhaulNetworkCheckReason) {
		this.backhaulNetworkCheckReason = backhaulNetworkCheckReason;
	}

	@JsonProperty("onnet_2km_Avg_DistanceKilometers")
	public String getOnnet2kmAvgDistanceKilometers() {
		return onnet2kmAvgDistanceKilometers;
	}

	@JsonProperty("onnet_2km_Avg_DistanceKilometers")
	public void setOnnet2kmAvgDistanceKilometers(String onnet2kmAvgDistanceKilometers) {
		this.onnet2kmAvgDistanceKilometers = onnet2kmAvgDistanceKilometers;
	}

	@JsonProperty("Avg_3KM_Mast_ht_cost")
	public String getAvg3KMMastHtCost() {
		return avg3KMMastHtCost;
	}

	@JsonProperty("Avg_3KM_Mast_ht_cost")
	public void setAvg3KMMastHtCost(String avg3KMMastHtCost) {
		this.avg3KMMastHtCost = avg3KMMastHtCost;
	}

	@JsonProperty("burstable_bw")
	public Double getBurstableBw() {
		return burstableBw;
	}

	@JsonProperty("burstable_bw")
	public void setBurstableBw(Double burstableBw) {
		this.burstableBw = burstableBw;
	}

	@JsonProperty("bw_mbps")
	public Double getBwMbps() {
		return bwMbps;
	}

	@JsonProperty("bw_mbps")
	public void setBwMbps(Double bwMbps) {
		this.bwMbps = bwMbps;
	}

	@JsonProperty("bts_flag")
	public String getBtsFlag() {
		return btsFlag;
	}

	@JsonProperty("bts_flag")
	public void setBtsFlag(String btsFlag) {
		this.btsFlag = btsFlag;
	}

	@JsonProperty("bts_IP_PMP")
	public String getBtsIPPMP() {
		return btsIPPMP;
	}

	@JsonProperty("bts_IP_PMP")
	public void setBtsIPPMP(String btsIPPMP) {
		this.btsIPPMP = btsIPPMP;
	}

	@JsonProperty("prospect_0_5km_Max_BW_Mbps")
	public String getProspect05kmMaxBWMbps() {
		return prospect05kmMaxBWMbps;
	}

	@JsonProperty("prospect_0_5km_Max_BW_Mbps")
	public void setProspect05kmMaxBWMbps(String prospect05kmMaxBWMbps) {
		this.prospect05kmMaxBWMbps = prospect05kmMaxBWMbps;
	}

	@JsonProperty("product_name")
	public String getProductName() {
		return productName;
	}

	@JsonProperty("product_name")
	public void setProductName(String productName) {
		this.productName = productName;
	}

	@JsonProperty("LM_ARC_INR")
	public String getLMARCINR() {
		return lMARCINR;
	}

	@JsonProperty("LM_ARC_INR")
	public void setLMARCINR(String lMARCINR) {
		this.lMARCINR = lMARCINR;
	}

	@JsonProperty("bts_min_dist_km")
	public String getBtsMinDistKm() {
		return btsMinDistKm;
	}

	@JsonProperty("bts_min_dist_km")
	public void setBtsMinDistKm(String btsMinDistKm) {
		this.btsMinDistKm = btsMinDistKm;
	}

	@JsonProperty("error_code")
	public String getErrorCode() {
		return errorCode;
	}

	@JsonProperty("error_code")
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	@JsonProperty("lm_arc_bw_onwl")
	public String getLmArcBwOnwl() {
		return lmArcBwOnwl;
	}

	@JsonProperty("lm_arc_bw_onwl")
	public void setLmArcBwOnwl(String lmArcBwOnwl) {
		this.lmArcBwOnwl = lmArcBwOnwl;
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

	@JsonProperty("lm_nrc_mux_onwl")
	public String getLmNrcMuxOnwl() {
		return lmNrcMuxOnwl;
	}

	@JsonProperty("lm_nrc_mux_onwl")
	public void setLmNrcMuxOnwl(String lmNrcMuxOnwl) {
		this.lmNrcMuxOnwl = lmNrcMuxOnwl;
	}

	@JsonProperty("bts_num_BTS")
	public String getBtsNumBTS() {
		return btsNumBTS;
	}

	@JsonProperty("bts_num_BTS")
	public void setBtsNumBTS(String btsNumBTS) {
		this.btsNumBTS = btsNumBTS;
	}

	@JsonProperty("feasibility_response_created_date")
	public String getFeasibilityResponseCreatedDate() {
		return feasibilityResponseCreatedDate;
	}

	@JsonProperty("feasibility_response_created_date")
	public void setFeasibilityResponseCreatedDate(String feasibilityResponseCreatedDate) {
		this.feasibilityResponseCreatedDate = feasibilityResponseCreatedDate;
	}

	@JsonProperty("prospect_0_5km_Min_DistanceKilometers")
	public String getProspect05kmMinDistanceKilometers() {
		return prospect05kmMinDistanceKilometers;
	}

	@JsonProperty("prospect_0_5km_Min_DistanceKilometers")
	public void setProspect05kmMinDistanceKilometers(String prospect05kmMinDistanceKilometers) {
		this.prospect05kmMinDistanceKilometers = prospect05kmMinDistanceKilometers;
	}

	@JsonProperty("Sector_network_check")
	public String getSectorNetworkCheck() {
		return sectorNetworkCheck;
	}

	@JsonProperty("Sector_network_check")
	public void setSectorNetworkCheck(String sectorNetworkCheck) {
		this.sectorNetworkCheck = sectorNetworkCheck;
	}

	@JsonProperty("Bandwidth")
	public String getBandwidth() {
		return bandwidth;
	}

	@JsonProperty("Bandwidth")
	public void setBandwidth(String bandwidth) {
		this.bandwidth = bandwidth;
	}

	@JsonProperty("P2P_bts_3km_radius")
	public String getP2PBts3kmRadius() {
		return p2PBts3kmRadius;
	}

	@JsonProperty("P2P_bts_3km_radius")
	public void setP2PBts3kmRadius(String p2PBts3kmRadius) {
		this.p2PBts3kmRadius = p2PBts3kmRadius;
	}

	@JsonProperty("cpe_variant")
	public String getCpeVariant() {
		return cpeVariant;
	}

	@JsonProperty("cpe_variant")
	public void setCpeVariant(String cpeVariant) {
		this.cpeVariant = cpeVariant;
	}

	@JsonProperty("prospect_2km_Min_BW_Mbps")
	public String getProspect2kmMinBWMbps() {
		return prospect2kmMinBWMbps;
	}

	@JsonProperty("prospect_2km_Min_BW_Mbps")
	public void setProspect2kmMinBWMbps(String prospect2kmMinBWMbps) {
		this.prospect2kmMinBWMbps = prospect2kmMinBWMbps;
	}

	@JsonProperty("onnet_5km_Avg_DistanceKilometers")
	public String getOnnet5kmAvgDistanceKilometers() {
		return onnet5kmAvgDistanceKilometers;
	}

	@JsonProperty("onnet_5km_Avg_DistanceKilometers")
	public void setOnnet5kmAvgDistanceKilometers(String onnet5kmAvgDistanceKilometers) {
		this.onnet5kmAvgDistanceKilometers = onnet5kmAvgDistanceKilometers;
	}

	@JsonProperty("sum_no_of_sites_uni_len")
	public Integer getSumNoOfSitesUniLen() {
		return sumNoOfSitesUniLen;
	}

	@JsonProperty("sum_no_of_sites_uni_len")
	public void setSumNoOfSitesUniLen(Integer sumNoOfSitesUniLen) {
		this.sumNoOfSitesUniLen = sumNoOfSitesUniLen;
	}

	@JsonProperty("onnet_2km_Max_BW_Mbps")
	public String getOnnet2kmMaxBWMbps() {
		return onnet2kmMaxBWMbps;
	}

	@JsonProperty("onnet_2km_Max_BW_Mbps")
	public void setOnnet2kmMaxBWMbps(String onnet2kmMaxBWMbps) {
		this.onnet2kmMaxBWMbps = onnet2kmMaxBWMbps;
	}

	@JsonProperty("onnet_5km_Min_BW_Mbps")
	public String getOnnet5kmMinBWMbps() {
		return onnet5kmMinBWMbps;
	}

	@JsonProperty("onnet_5km_Min_BW_Mbps")
	public void setOnnet5kmMinBWMbps(String onnet5kmMinBWMbps) {
		this.onnet5kmMinBWMbps = onnet5kmMinBWMbps;
	}

	@JsonProperty("LM_Cost")
	public String getLMCost() {
		return lMCost;
	}

	@JsonProperty("LM_Cost")
	public void setLMCost(String lMCost) {
		this.lMCost = lMCost;
	}

	@JsonProperty("Mast_3KM_max_mast_ht")
	public String getMast3KMMaxMastHt() {
		return mast3KMMaxMastHt;
	}

	@JsonProperty("Mast_3KM_max_mast_ht")
	public void setMast3KMMaxMastHt(String mast3KMMaxMastHt) {
		this.mast3KMMaxMastHt = mast3KMMaxMastHt;
	}

	@JsonProperty("prospect_0_5km_Avg_BW_Mbps")
	public String getProspect05kmAvgBWMbps() {
		return prospect05kmAvgBWMbps;
	}

	@JsonProperty("prospect_0_5km_Avg_BW_Mbps")
	public void setProspect05kmAvgBWMbps(String prospect05kmAvgBWMbps) {
		this.prospect05kmAvgBWMbps = prospect05kmAvgBWMbps;
	}

	@JsonProperty("bts_Closest_infra_provider")
	public String getBtsClosestInfraProvider() {
		return btsClosestInfraProvider;
	}

	@JsonProperty("bts_Closest_infra_provider")
	public void setBtsClosestInfraProvider(String btsClosestInfraProvider) {
		this.btsClosestInfraProvider = btsClosestInfraProvider;
	}

	@JsonProperty("Probabililty_Access_Feasibility")
	public String getProbabililtyAccessFeasibility() {
		return probabililtyAccessFeasibility;
	}

	@JsonProperty("Probabililty_Access_Feasibility")
	public void setProbabililtyAccessFeasibility(String probabililtyAccessFeasibility) {
		this.probabililtyAccessFeasibility = probabililtyAccessFeasibility;
	}

	@JsonProperty("lm_nrc_bw_onrf")
	public String getLmNrcBwOnrf() {
		return lmNrcBwOnrf;
	}

	@JsonProperty("lm_nrc_bw_onrf")
	public void setLmNrcBwOnrf(String lmNrcBwOnrf) {
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

	public String getClosestProviderBsoName() {
		return closestProviderBsoName;
	}

	public void setClosestProviderBsoName(String closestProviderBsoName) {
		this.closestProviderBsoName = closestProviderBsoName;
	}

	public String getlMOTCINR() {
		return lMOTCINR;
	}

	public void setlMOTCINR(String lMOTCINR) {
		this.lMOTCINR = lMOTCINR;
	}

	public String get_interface() {
		return _interface;
	}

	public void set_interface(String _interface) {
		this._interface = _interface;
	}

	public String getpMPBts3kmRadius() {
		return pMPBts3kmRadius;
	}

	public void setpMPBts3kmRadius(String pMPBts3kmRadius) {
		this.pMPBts3kmRadius = pMPBts3kmRadius;
	}

	public String getlMARCINR() {
		return lMARCINR;
	}

	public void setlMARCINR(String lMARCINR) {
		this.lMARCINR = lMARCINR;
	}

	public String getlMCost() {
		return lMCost;
	}

	public void setlMCost(String lMCost) {
		this.lMCost = lMCost;
	}

	public Float getAvgMastHt() {
		return avgMastHt;
	}

	public void setAvgMastHt(Float avgMastHt) {
		this.avgMastHt = avgMastHt;
	}
	@JsonProperty("cu_le_id")
	public String getCuLeId() {
		return cuLeId;
	}
	@JsonProperty("cu_le_id")
	public void setCuLeId(String cuLeId) {
		this.cuLeId = cuLeId;
	}

	/**
	 * @return the x5kmProspectPercFeasible
	 */
	public String getX5kmProspectPercFeasible() {
		return X5kmProspectPercFeasible;
	}

	/**
	 * @param x5kmProspectPercFeasible the x5kmProspectPercFeasible to set
	 */
	public void setX5kmProspectPercFeasible(String x5kmProspectPercFeasible) {
		X5kmProspectPercFeasible = x5kmProspectPercFeasible;
	}

	/**
	 * @return the x5kmProspectNumFeasible
	 */
	public String getX5kmProspectNumFeasible() {
		return x5kmProspectNumFeasible;
	}

	/**
	 * @param x5kmProspectNumFeasible the x5kmProspectNumFeasible to set
	 */
	public void setX5kmProspectNumFeasible(String x5kmProspectNumFeasible) {
		this.x5kmProspectNumFeasible = x5kmProspectNumFeasible;
	}

	/**
	 * @return the x5kmProspectMinDist
	 */
	public String getX5kmProspectMinDist() {
		return x5kmProspectMinDist;
	}

	/**
	 * @param x5kmProspectMinDist the x5kmProspectMinDist to set
	 */
	public void setX5kmProspectMinDist(String x5kmProspectMinDist) {
		this.x5kmProspectMinDist = x5kmProspectMinDist;
	}

	/**
	 * @return the x5kmProspectMinBw
	 */
	public String getX5kmProspectMinBw() {
		return x5kmProspectMinBw;
	}

	/**
	 * @param x5kmProspectMinBw the x5kmProspectMinBw to set
	 */
	public void setX5kmProspectMinBw(String x5kmProspectMinBw) {
		this.x5kmProspectMinBw = x5kmProspectMinBw;
	}

	/**
	 * @return the x5kmProspectMaxBw
	 */
	public String getX5kmProspectMaxBw() {
		return x5kmProspectMaxBw;
	}

	/**
	 * @param x5kmProspectMaxBw the x5kmProspectMaxBw to set
	 */
	public void setX5kmProspectMaxBw(String x5kmProspectMaxBw) {
		this.x5kmProspectMaxBw = x5kmProspectMaxBw;
	}

	/**
	 * @return the x5kmProspectCount
	 */
	public String getX5kmProspectCount() {
		return x5kmProspectCount;
	}

	/**
	 * @param x5kmProspectCount the x5kmProspectCount to set
	 */
	public void setX5kmProspectCount(String x5kmProspectCount) {
		this.x5kmProspectCount = x5kmProspectCount;
	}

	/**
	 * @return the x5kmProspectAvgDist
	 */
	public String getX5kmProspectAvgDist() {
		return x5kmProspectAvgDist;
	}

	/**
	 * @param x5kmProspectAvgDist the x5kmProspectAvgDist to set
	 */
	public void setX5kmProspectAvgDist(String x5kmProspectAvgDist) {
		this.x5kmProspectAvgDist = x5kmProspectAvgDist;
	}

	/**
	 * @return the x2kmProspectMaxBw
	 */
	public String getX2kmProspectMaxBw() {
		return x2kmProspectMaxBw;
	}

	/**
	 * @param x2kmProspectMaxBw the x2kmProspectMaxBw to set
	 */
	public void setX2kmProspectMaxBw(String x2kmProspectMaxBw) {
		this.x2kmProspectMaxBw = x2kmProspectMaxBw;
	}

	/**
	 * @return the x2kmProspectMinBw
	 */
	public String getX2kmProspectMinBw() {
		return x2kmProspectMinBw;
	}

	/**
	 * @param x2kmProspectMinBw the x2kmProspectMinBw to set
	 */
	public void setX2kmProspectMinBw(String x2kmProspectMinBw) {
		this.x2kmProspectMinBw = x2kmProspectMinBw;
	}

	/**
	 * @return the x2kmProspectMinDist
	 */
	public String getX2kmProspectMinDist() {
		return x2kmProspectMinDist;
	}

	/**
	 * @param x2kmProspectMinDist the x2kmProspectMinDist to set
	 */
	public void setX2kmProspectMinDist(String x2kmProspectMinDist) {
		this.x2kmProspectMinDist = x2kmProspectMinDist;
	}

	/**
	 * @return the x2kmProspectNumFeasible
	 */
	public String getX2kmProspectNumFeasible() {
		return x2kmProspectNumFeasible;
	}

	/**
	 * @param x2kmProspectNumFeasible the x2kmProspectNumFeasible to set
	 */
	public void setX2kmProspectNumFeasible(String x2kmProspectNumFeasible) {
		this.x2kmProspectNumFeasible = x2kmProspectNumFeasible;
	}

	/**
	 * @return the x2kmProspectPercFeasible
	 */
	public String getX2kmProspectPercFeasible() {
		return x2kmProspectPercFeasible;
	}

	/**
	 * @param x2kmProspectPercFeasible the x2kmProspectPercFeasible to set
	 */
	public void setX2kmProspectPercFeasible(String x2kmProspectPercFeasible) {
		this.x2kmProspectPercFeasible = x2kmProspectPercFeasible;
	}

	/**
	 * @return the x5kmAvgBw
	 */
	public String getX5kmAvgBw() {
		return x5kmAvgBw;
	}

	/**
	 * @param x5kmAvgBw the x5kmAvgBw to set
	 */
	public void setX5kmAvgBw(String x5kmAvgBw) {
		this.x5kmAvgBw = x5kmAvgBw;
	}

	/**
	 * @return the x5kmAvgDist
	 */
	public String getX5kmAvgDist() {
		return x5kmAvgDist;
	}

	/**
	 * @param x5kmAvgDist the x5kmAvgDist to set
	 */
	public void setX5kmAvgDist(String x5kmAvgDist) {
		this.x5kmAvgDist = x5kmAvgDist;
	}

	/**
	 * @return the x5kmCustCount
	 */
	public String getX5kmCustCount() {
		return x5kmCustCount;
	}

	/**
	 * @param x5kmCustCount the x5kmCustCount to set
	 */
	public void setX5kmCustCount(String x5kmCustCount) {
		this.x5kmCustCount = x5kmCustCount;
	}

	/**
	 * @return the x5kmMaxBw
	 */
	public String getX5kmMaxBw() {
		return x5kmMaxBw;
	}

	/**
	 * @param x5kmMaxBw the x5kmMaxBw to set
	 */
	public void setX5kmMaxBw(String x5kmMaxBw) {
		this.x5kmMaxBw = x5kmMaxBw;
	}

	/**
	 * @return the x5kmMinBw
	 */
	public String getX5kmMinBw() {
		return x5kmMinBw;
	}

	/**
	 * @param x5kmMinBw the x5kmMinBw to set
	 */
	public void setX5kmMinBw(String x5kmMinBw) {
		this.x5kmMinBw = x5kmMinBw;
	}

	/**
	 * @return the x5kmMinDist
	 */
	public String getX5kmMinDist() {
		return x5kmMinDist;
	}

	/**
	 * @param x5kmMinDist the x5kmMinDist to set
	 */
	public void setX5kmMinDist(String x5kmMinDist) {
		this.x5kmMinDist = x5kmMinDist;
	}

	/**
	 * @return the x5kmProspectAvgBw
	 */
	public String getX5kmProspectAvgBw() {
		return x5kmProspectAvgBw;
	}

	/**
	 * @param x5kmProspectAvgBw the x5kmProspectAvgBw to set
	 */
	public void setX5kmProspectAvgBw(String x5kmProspectAvgBw) {
		this.x5kmProspectAvgBw = x5kmProspectAvgBw;
	}

	/**
	 * @return the totalCost
	 */
	public String getTotalCost() {
		return totalCost;
	}

	/**
	 * @param totalCost the totalCost to set
	 */
	public void setTotalCost(String totalCost) {
		this.totalCost = totalCost;
	}

	/**
	 * @return the x0_5kmAvgBw
	 */
	public String getX0_5kmAvgBw() {
		return x0_5kmAvgBw;
	}

	/**
	 * @param x0_5kmAvgBw the x0_5kmAvgBw to set
	 */
	public void setX0_5kmAvgBw(String x0_5kmAvgBw) {
		this.x0_5kmAvgBw = x0_5kmAvgBw;
	}

	/**
	 * @return the x0_5kmAvgDist
	 */
	public String getX0_5kmAvgDist() {
		return x0_5kmAvgDist;
	}

	/**
	 * @param x0_5kmAvgDist the x0_5kmAvgDist to set
	 */
	public void setX0_5kmAvgDist(String x0_5kmAvgDist) {
		this.x0_5kmAvgDist = x0_5kmAvgDist;
	}

	/**
	 * @return the x0_5kmCustCount
	 */
	public String getX0_5kmCustCount() {
		return x0_5kmCustCount;
	}

	/**
	 * @param x0_5kmCustCount the x0_5kmCustCount to set
	 */
	public void setX0_5kmCustCount(String x0_5kmCustCount) {
		this.x0_5kmCustCount = x0_5kmCustCount;
	}

	/**
	 * @return the x0_5kmMaxBw
	 */
	public String getX0_5kmMaxBw() {
		return x0_5kmMaxBw;
	}

	/**
	 * @param x0_5kmMaxBw the x0_5kmMaxBw to set
	 */
	public void setX0_5kmMaxBw(String x0_5kmMaxBw) {
		this.x0_5kmMaxBw = x0_5kmMaxBw;
	}

	/**
	 * @return the x0_5kmMinBw
	 */
	public String getX0_5kmMinBw() {
		return x0_5kmMinBw;
	}

	/**
	 * @param x0_5kmMinBw the x0_5kmMinBw to set
	 */
	public void setX0_5kmMinBw(String x0_5kmMinBw) {
		this.x0_5kmMinBw = x0_5kmMinBw;
	}

	/**
	 * @return the x0_5kmMinDist
	 */
	public String getX0_5kmMinDist() {
		return x0_5kmMinDist;
	}

	/**
	 * @param x0_5kmMinDist the x0_5kmMinDist to set
	 */
	public void setX0_5kmMinDist(String x0_5kmMinDist) {
		this.x0_5kmMinDist = x0_5kmMinDist;
	}

	/**
	 * @return the x0_5kmProspectAvgBw
	 */
	public String getX0_5kmProspectAvgBw() {
		return x0_5kmProspectAvgBw;
	}

	/**
	 * @param x0_5kmProspectAvgBw the x0_5kmProspectAvgBw to set
	 */
	public void setX0_5kmProspectAvgBw(String x0_5kmProspectAvgBw) {
		this.x0_5kmProspectAvgBw = x0_5kmProspectAvgBw;
	}

	/**
	 * @return the x0_5kmProspectAvgDist
	 */
	public String getX0_5kmProspectAvgDist() {
		return x0_5kmProspectAvgDist;
	}

	/**
	 * @param x0_5kmProspectAvgDist the x0_5kmProspectAvgDist to set
	 */
	public void setX0_5kmProspectAvgDist(String x0_5kmProspectAvgDist) {
		this.x0_5kmProspectAvgDist = x0_5kmProspectAvgDist;
	}

	/**
	 * @return the x0_5ProspectCount
	 */
	public String getX0_5ProspectCount() {
		return x0_5ProspectCount;
	}

	/**
	 * @param x0_5ProspectCount the x0_5ProspectCount to set
	 */
	public void setX0_5ProspectCount(String x0_5ProspectCount) {
		this.x0_5ProspectCount = x0_5ProspectCount;
	}

	/**
	 * @return the x0_5kmProspectMaxBw
	 */
	public String getX0_5kmProspectMaxBw() {
		return x0_5kmProspectMaxBw;
	}

	/**
	 * @param x0_5kmProspectMaxBw the x0_5kmProspectMaxBw to set
	 */
	public void setX0_5kmProspectMaxBw(String x0_5kmProspectMaxBw) {
		this.x0_5kmProspectMaxBw = x0_5kmProspectMaxBw;
	}

	/**
	 * @return the x0_5kmProspectMinBw
	 */
	public String getX0_5kmProspectMinBw() {
		return x0_5kmProspectMinBw;
	}

	/**
	 * @param x0_5kmProspectMinBw the x0_5kmProspectMinBw to set
	 */
	public void setX0_5kmProspectMinBw(String x0_5kmProspectMinBw) {
		this.x0_5kmProspectMinBw = x0_5kmProspectMinBw;
	}

	/**
	 * @return the x0_5kmProspectMinDist
	 */
	public String getX0_5kmProspectMinDist() {
		return x0_5kmProspectMinDist;
	}

	/**
	 * @param x0_5kmProspectMinDist the x0_5kmProspectMinDist to set
	 */
	public void setX0_5kmProspectMinDist(String x0_5kmProspectMinDist) {
		this.x0_5kmProspectMinDist = x0_5kmProspectMinDist;
	}

	/**
	 * @return the x0_5kmProspectNumFeasible
	 */
	public String getX0_5kmProspectNumFeasible() {
		return x0_5kmProspectNumFeasible;
	}

	/**
	 * @param x0_5kmProspectNumFeasible the x0_5kmProspectNumFeasible to set
	 */
	public void setX0_5kmProspectNumFeasible(String x0_5kmProspectNumFeasible) {
		this.x0_5kmProspectNumFeasible = x0_5kmProspectNumFeasible;
	}

	/**
	 * @return the x0_5kmProspectPercFeasible
	 */
	public String getX0_5kmProspectPercFeasible() {
		return x0_5kmProspectPercFeasible;
	}

	/**
	 * @param x0_5kmProspectPercFeasible the x0_5kmProspectPercFeasible to set
	 */
	public void setX0_5kmProspectPercFeasible(String x0_5kmProspectPercFeasible) {
		this.x0_5kmProspectPercFeasible = x0_5kmProspectPercFeasible;
	}

	/**
	 * @return the x2kmAvgBw
	 */
	public String getX2kmAvgBw() {
		return x2kmAvgBw;
	}

	/**
	 * @param x2kmAvgBw the x2kmAvgBw to set
	 */
	public void setX2kmAvgBw(String x2kmAvgBw) {
		this.x2kmAvgBw = x2kmAvgBw;
	}

	/**
	 * @return the x2kmAvgDist
	 */
	public String getX2kmAvgDist() {
		return x2kmAvgDist;
	}

	/**
	 * @param x2kmAvgDist the x2kmAvgDist to set
	 */
	public void setX2kmAvgDist(String x2kmAvgDist) {
		this.x2kmAvgDist = x2kmAvgDist;
	}

	/**
	 * @return the x2kmCustCount
	 */
	public String getX2kmCustCount() {
		return x2kmCustCount;
	}

	/**
	 * @param x2kmCustCount the x2kmCustCount to set
	 */
	public void setX2kmCustCount(String x2kmCustCount) {
		this.x2kmCustCount = x2kmCustCount;
	}

	/**
	 * @return the x2kmMaxBw
	 */
	public String getX2kmMaxBw() {
		return x2kmMaxBw;
	}

	/**
	 * @param x2kmMaxBw the x2kmMaxBw to set
	 */
	public void setX2kmMaxBw(String x2kmMaxBw) {
		this.x2kmMaxBw = x2kmMaxBw;
	}

	/**
	 * @return the x2kmMinBw
	 */
	public String getX2kmMinBw() {
		return x2kmMinBw;
	}

	/**
	 * @param x2kmMinBw the x2kmMinBw to set
	 */
	public void setX2kmMinBw(String x2kmMinBw) {
		this.x2kmMinBw = x2kmMinBw;
	}

	/**
	 * @return the x2kmMinDist
	 */
	public String getX2kmMinDist() {
		return x2kmMinDist;
	}

	/**
	 * @param x2kmMinDist the x2kmMinDist to set
	 */
	public void setX2kmMinDist(String x2kmMinDist) {
		this.x2kmMinDist = x2kmMinDist;
	}

	/**
	 * @return the x2kmProspectAvgBw
	 */
	public String getX2kmProspectAvgBw() {
		return x2kmProspectAvgBw;
	}

	/**
	 * @param x2kmProspectAvgBw the x2kmProspectAvgBw to set
	 */
	public void setX2kmProspectAvgBw(String x2kmProspectAvgBw) {
		this.x2kmProspectAvgBw = x2kmProspectAvgBw;
	}

	/**
	 * @return the x2kmProspectAvgDist
	 */
	public String getX2kmProspectAvgDist() {
		return x2kmProspectAvgDist;
	}

	/**
	 * @param x2kmProspectAvgDist the x2kmProspectAvgDist to set
	 */
	public void setX2kmProspectAvgDist(String x2kmProspectAvgDist) {
		this.x2kmProspectAvgDist = x2kmProspectAvgDist;
	}

	/**
	 * @return the x2kmProspectCount
	 */
	public String getX2kmProspectCount() {
		return x2kmProspectCount;
	}

	/**
	 * @param x2kmProspectCount the x2kmProspectCount to set
	 */
	public void setX2kmProspectCount(String x2kmProspectCount) {
		this.x2kmProspectCount = x2kmProspectCount;
	}

	/**
	 * @return the siteFlag
	 */
	public String getSiteFlag() {
		return siteFlag;
	}

	/**
	 * @param siteFlag the siteFlag to set
	 */
	public void setSiteFlag(String siteFlag) {
		this.siteFlag = siteFlag;
	}

	/**
	 * @return the serviceId
	 */
	public String getServiceID() {
		return serviceID;
	}

	/**
	 * @param serviceId the serviceId to set
	 */
	public void setServiceID(String serviceID) {
		this.serviceID = serviceID;
	}

	/**
	 * @return the scenario1
	 */
	public String getScenario1() {
		return scenario1;
	}

	/**
	 * @param scenario1 the scenario1 to set
	 */
	public void setScenario1(String scenario1) {
		this.scenario1 = scenario1;
	}

	/**
	 * @return the scenario2
	 */
	public String getScenario2() {
		return scenario2;
	}

	/**
	 * @param scenario2 the scenario2 to set
	 */
	public void setScenario2(String scenario2) {
		this.scenario2 = scenario2;
	}

	/**
	 * @return the providerMinDist
	 */
	public String getProviderMinDist() {
		return providerMinDist;
	}

	/**
	 * @param providerMinDist the providerMinDist to set
	 */
	public void setProviderMinDist(String providerMinDist) {
		this.providerMinDist = providerMinDist;
	}

	/**
	 * @return the providerTotTowers
	 */
	public String getProviderTotTowers() {
		return providerTotTowers;
	}

	/**
	 * @param providerTotTowers the providerTotTowers to set
	 */
	public void setProviderTotTowers(String providerTotTowers) {
		this.providerTotTowers = providerTotTowers;
	}

	/**
	 * @return the prospect2kmCustCount
	 */
	public String getProspect2kmCustCount() {
		return prospect2kmCustCount;
	}

	/**
	 * @param prospect2kmCustCount the prospect2kmCustCount to set
	 */
	public void setProspect2kmCustCount(String prospect2kmCustCount) {
		this.prospect2kmCustCount = prospect2kmCustCount;
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
	 * @return the popTclAccess
	 */
	public String getPopTclAccess() {
		return popTclAccess;
	}

	/**
	 * @param popTclAccess the popTclAccess to set
	 */
	public void setPopTclAccess(String popTclAccess) {
		this.popTclAccess = popTclAccess;
	}

	/**
	 * @return the popLat
	 */
	public String getPopLat() {
		return popLat;
	}

	/**
	 * @param popLat the popLat to set
	 */
	public void setPopLat(String popLat) {
		this.popLat = popLat;
	}

	/**
	 * @return the popLong
	 */
	public String getPopLong() {
		return popLong;
	}

	/**
	 * @param popLong the popLong to set
	 */
	public void setPopLong(String popLong) {
		this.popLong = popLong;
	}

	/**
	 * @return the popName
	 */
	public String getPopName() {
		return popName;
	}

	/**
	 * @param popName the popName to set
	 */
	public void setPopName(String popName) {
		this.popName = popName;
	}

	/**
	 * @return the popNetworkLocId
	 */
	public String getPopNetworkLocId() {
		return popNetworkLocId;
	}

	/**
	 * @param popNetworkLocId the popNetworkLocId to set
	 */
	public void setPopNetworkLocId(String popNetworkLocId) {
		this.popNetworkLocId = popNetworkLocId;
	}

	/**
	 * @return the popNetworkLocaationType
	 */
	public String getPopNetworkLocaationType() {
		return popNetworkLocaationType;
	}

	/**
	 * @param popNetworkLocaationType the popNetworkLocaationType to set
	 */
	public void setPopNetworkLocaationType(String popNetworkLocaationType) {
		this.popNetworkLocaationType = popNetworkLocaationType;
	}

	/**
	 * @return the popDistKmServiceMod
	 */
	public String getPopDistKmServiceMod() {
		return popDistKmServiceMod;
	}

	/**
	 * @param popDistKmServiceMod the popDistKmServiceMod to set
	 */
	public void setPopDistKmServiceMod(String popDistKmServiceMod) {
		this.popDistKmServiceMod = popDistKmServiceMod;
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
	 * @return the popBuildingType
	 */
	public String getPopBuildingType() {
		return popBuildingType;
	}

	/**
	 * @param popBuildingType the popBuildingType to set
	 */
	public void setPopBuildingType(String popBuildingType) {
		this.popBuildingType = popBuildingType;
	}

	/**
	 * @return the popCategory
	 */
	public String getPopCategory() {
		return popCategory;
	}

	/**
	 * @param popCategory the popCategory to set
	 */
	public void setPopCategory(String popCategory) {
		this.popCategory = popCategory;
	}

	/**
	 * @return the popConstructionStatus
	 */
	public String getPopConstructionStatus() {
		return popConstructionStatus;
	}

	/**
	 * @param popConstructionStatus the popConstructionStatus to set
	 */
	public void setPopConstructionStatus(String popConstructionStatus) {
		this.popConstructionStatus = popConstructionStatus;
	}

	/**
	 * @return the popDistKm
	 */
	public String getPopDistKm() {
		return popDistKm;
	}

	/**
	 * @param popDistKm the popDistKm to set
	 */
	public void setPopDistKm(String popDistKm) {
		this.popDistKm = popDistKm;
	}

	/**
	 * @return the popDistKmService
	 */
	public String getPopDistKmService() {
		return popDistKmService;
	}

	/**
	 * @param popDistKmService the popDistKmService to set
	 */
	public void setPopDistKmService(String popDistKmService) {
		this.popDistKmService = popDistKmService;
	}

	/**
	 * @return the otcSify
	 */
	public String getOtcSify() {
		return otcSify;
	}

	/**
	 * @param otcSify the otcSify to set
	 */
	public void setOtcSify(String otcSify) {
		this.otcSify = otcSify;
	}

	/**
	 * @return the otcTikona
	 */
	public String getOtcTikona() {
		return otcTikona;
	}

	/**
	 * @param otcTikona the otcTikona to set
	 */
	public void setOtcTikona(String otcTikona) {
		this.otcTikona = otcTikona;
	}

	/**
	 * @return the onnetCityTag
	 */
	public String getOnnetCityTag() {
		return onnetCityTag;
	}

	/**
	 * @param onnetCityTag the onnetCityTag to set
	 */
	public void setOnnetCityTag(String onnetCityTag) {
		this.onnetCityTag = onnetCityTag;
	}

	/**
	 * @return the numConnectedBuilding
	 */
	public String getNumConnectedBuilding() {
		return numConnectedBuilding;
	}

	/**
	 * @param numConnectedBuilding the numConnectedBuilding to set
	 */
	public void setNumConnectedBuilding(String numConnectedBuilding) {
		this.numConnectedBuilding = numConnectedBuilding;
	}

	/**
	 * @return the numConnectedCust
	 */
	public String getNumConnectedCust() {
		return numConnectedCust;
	}

	/**
	 * @param numConnectedCust the numConnectedCust to set
	 */
	public void setNumConnectedCust(String numConnectedCust) {
		this.numConnectedCust = numConnectedCust;
	}

	/**
	 * @return the offnet0_5kmAvgBwMbps
	 */
	public String getOffnet0_5kmAvgBwMbps() {
		return offnet0_5kmAvgBwMbps;
	}

	/**
	 * @param offnet0_5kmAvgBwMbps the offnet0_5kmAvgBwMbps to set
	 */
	public void setOffnet0_5kmAvgBwMbps(String offnet0_5kmAvgBwMbps) {
		this.offnet0_5kmAvgBwMbps = offnet0_5kmAvgBwMbps;
	}

	/**
	 * @return the offnet0_5kmCustCount
	 */
	public String getOffnet0_5kmCustCount() {
		return offnet0_5kmCustCount;
	}

	/**
	 * @param offnet0_5kmCustCount the offnet0_5kmCustCount to set
	 */
	public void setOffnet0_5kmCustCount(String offnet0_5kmCustCount) {
		this.offnet0_5kmCustCount = offnet0_5kmCustCount;
	}

	/**
	 * @return the offnet0_5kmMaxBwMbps
	 */
	public String getOffnet0_5kmMaxBwMbps() {
		return offnet0_5kmMaxBwMbps;
	}

	/**
	 * @param offnet0_5kmMaxBwMbps the offnet0_5kmMaxBwMbps to set
	 */
	public void setOffnet0_5kmMaxBwMbps(String offnet0_5kmMaxBwMbps) {
		this.offnet0_5kmMaxBwMbps = offnet0_5kmMaxBwMbps;
	}

	/**
	 * @return the offnet0_5kmMinAccuracyNum
	 */
	public String getOffnet0_5kmMinAccuracyNum() {
		return offnet0_5kmMinAccuracyNum;
	}

	/**
	 * @param offnet0_5kmMinAccuracyNum the offnet0_5kmMinAccuracyNum to set
	 */
	public void setOffnet0_5kmMinAccuracyNum(String offnet0_5kmMinAccuracyNum) {
		this.offnet0_5kmMinAccuracyNum = offnet0_5kmMinAccuracyNum;
	}

	/**
	 * @return the offnet0_5kmMinBwMbps
	 */
	public String getOffnet0_5kmMinBwMbps() {
		return offnet0_5kmMinBwMbps;
	}

	/**
	 * @param offnet0_5kmMinBwMbps the offnet0_5kmMinBwMbps to set
	 */
	public void setOffnet0_5kmMinBwMbps(String offnet0_5kmMinBwMbps) {
		this.offnet0_5kmMinBwMbps = offnet0_5kmMinBwMbps;
	}

	/**
	 * @return the offnet0_5kmMinDistanceKilometers
	 */
	public String getOffnet0_5kmMinDistanceKilometers() {
		return offnet0_5kmMinDistanceKilometers;
	}

	/**
	 * @param offnet0_5kmMinDistanceKilometers the offnet0_5kmMinDistanceKilometers to set
	 */
	public void setOffnet0_5kmMinDistanceKilometers(String offnet0_5kmMinDistanceKilometers) {
		this.offnet0_5kmMinDistanceKilometers = offnet0_5kmMinDistanceKilometers;
	}

	/**
	 * @return the offnet2kmAvgBwMbps
	 */
	public String getOffnet2kmAvgBwMbps() {
		return offnet2kmAvgBwMbps;
	}

	/**
	 * @param offnet2kmAvgBwMbps the offnet2kmAvgBwMbps to set
	 */
	public void setOffnet2kmAvgBwMbps(String offnet2kmAvgBwMbps) {
		this.offnet2kmAvgBwMbps = offnet2kmAvgBwMbps;
	}

	/**
	 * @return the offnet2kmCustCount
	 */
	public String getOffnet2kmCustCount() {
		return offnet2kmCustCount;
	}

	/**
	 * @param offnet2kmCustCount the offnet2kmCustCount to set
	 */
	public void setOffnet2kmCustCount(String offnet2kmCustCount) {
		this.offnet2kmCustCount = offnet2kmCustCount;
	}

	/**
	 * @return the offnet2kmMinAccuracyNum
	 */
	public String getOffnet2kmMinAccuracyNum() {
		return offnet2kmMinAccuracyNum;
	}

	/**
	 * @param offnet2kmMinAccuracyNum the offnet2kmMinAccuracyNum to set
	 */
	public void setOffnet2kmMinAccuracyNum(String offnet2kmMinAccuracyNum) {
		this.offnet2kmMinAccuracyNum = offnet2kmMinAccuracyNum;
	}

	/**
	 * @return the offnet2kmMinBwMbps
	 */
	public String getOffnet2kmMinBwMbps() {
		return offnet2kmMinBwMbps;
	}

	/**
	 * @param offnet2kmMinBwMbps the offnet2kmMinBwMbps to set
	 */
	public void setOffnet2kmMinBwMbps(String offnet2kmMinBwMbps) {
		this.offnet2kmMinBwMbps = offnet2kmMinBwMbps;
	}

	/**
	 * @return the offnet2kmMinDistanceKilometers
	 */
	public String getOffnet2kmMinDistanceKilometers() {
		return offnet2kmMinDistanceKilometers;
	}

	/**
	 * @param offnet2kmMinDistanceKilometers the offnet2kmMinDistanceKilometers to set
	 */
	public void setOffnet2kmMinDistanceKilometers(String offnet2kmMinDistanceKilometers) {
		this.offnet2kmMinDistanceKilometers = offnet2kmMinDistanceKilometers;
	}

	/**
	 * @return the offnet5kmAvgBwMbps
	 */
	public String getOffnet5kmAvgBwMbps() {
		return offnet5kmAvgBwMbps;
	}

	/**
	 * @param offnet5kmAvgBwMbps the offnet5kmAvgBwMbps to set
	 */
	public void setOffnet5kmAvgBwMbps(String offnet5kmAvgBwMbps) {
		this.offnet5kmAvgBwMbps = offnet5kmAvgBwMbps;
	}

	/**
	 * @return the offnet5kmMaxBwMbps
	 */
	public String getOffnet5kmMaxBwMbps() {
		return offnet5kmMaxBwMbps;
	}

	/**
	 * @param offnet5kmMaxBwMbps the offnet5kmMaxBwMbps to set
	 */
	public void setOffnet5kmMaxBwMbps(String offnet5kmMaxBwMbps) {
		this.offnet5kmMaxBwMbps = offnet5kmMaxBwMbps;
	}

	/**
	 * @return the offnet5kmMinAccuracyNum
	 */
	public String getOffnet5kmMinAccuracyNum() {
		return offnet5kmMinAccuracyNum;
	}

	/**
	 * @param offnet5kmMinAccuracyNum the offnet5kmMinAccuracyNum to set
	 */
	public void setOffnet5kmMinAccuracyNum(String offnet5kmMinAccuracyNum) {
		this.offnet5kmMinAccuracyNum = offnet5kmMinAccuracyNum;
	}

	/**
	 * @return the offnet5kmMinBwMbps
	 */
	public String getOffnet5kmMinBwMbps() {
		return offnet5kmMinBwMbps;
	}

	/**
	 * @param offnet5kmMinBwMbps the offnet5kmMinBwMbps to set
	 */
	public void setOffnet5kmMinBwMbps(String offnet5kmMinBwMbps) {
		this.offnet5kmMinBwMbps = offnet5kmMinBwMbps;
	}

	/**
	 * @return the offnet5kmMinDistanceKilometers
	 */
	public String getOffnet5kmMinDistanceKilometers() {
		return offnet5kmMinDistanceKilometers;
	}

	/**
	 * @param offnet5kmMinDistanceKilometers the offnet5kmMinDistanceKilometers to set
	 */
	public void setOffnet5kmMinDistanceKilometers(String offnet5kmMinDistanceKilometers) {
		this.offnet5kmMinDistanceKilometers = offnet5kmMinDistanceKilometers;
	}

	/**
	 * @return the maxMastHt
	 */
	public String getMaxMastHt() {
		return maxMastHt;
	}

	/**
	 * @param maxMastHt the maxMastHt to set
	 */
	public void setMaxMastHt(String maxMastHt) {
		this.maxMastHt = maxMastHt;
	}

	/**
	 * @return the minHhFatg
	 */
	public String getMinHhFatg() {
		return minHhFatg;
	}

	/**
	 * @param minHhFatg the minHhFatg to set
	 */
	public void setMinHhFatg(String minHhFatg) {
		this.minHhFatg = minHhFatg;
	}

	/**
	 * @return the minMastHt
	 */
	public String getMinMastHt() {
		return minMastHt;
	}

	/**
	 * @param minMastHt the minMastHt to set
	 */
	public void setMinMastHt(String minMastHt) {
		this.minMastHt = minMastHt;
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
	 * @return the nearestMastHt
	 */
	public String getNearestMastHt() {
		return nearestMastHt;
	}

	/**
	 * @param nearestMastHt the nearestMastHt to set
	 */
	public void setNearestMastHt(String nearestMastHt) {
		this.nearestMastHt = nearestMastHt;
	}

	/**
	 * @return the netPreFeasibleFlag
	 */
	public String getNetPreFeasibleFlag() {
		return netPreFeasibleFlag;
	}

	/**
	 * @param netPreFeasibleFlag the netPreFeasibleFlag to set
	 */
	public void setNetPreFeasibleFlag(String netPreFeasibleFlag) {
		this.netPreFeasibleFlag = netPreFeasibleFlag;
	}

	/**
	 * @return the networkFNFCc
	 */
	public String getNetworkFNFCc() {
		return networkFNFCc;
	}

	/**
	 * @param networkFNFCc the networkFNFCc to set
	 */
	public void setNetworkFNFCc(String networkFNFCc) {
		this.networkFNFCc = networkFNFCc;
	}

	/**
	 * @return the networkFNFCCFlag
	 */
	public String getNetworkFNFCCFlag() {
		return networkFNFCCFlag;
	}

	/**
	 * @param networkFNFCCFlag the networkFNFCCFlag to set
	 */
	public void setNetworkFNFCCFlag(String networkFNFCCFlag) {
		this.networkFNFCCFlag = networkFNFCCFlag;
	}

	/**
	 * @return the networkFNFHh
	 */
	public String getNetworkFNFHh() {
		return networkFNFHh;
	}

	/**
	 * @param networkFNFHh the networkFNFHh to set
	 */
	public void setNetworkFNFHh(String networkFNFHh) {
		this.networkFNFHh = networkFNFHh;
	}

	/**
	 * @return the networkFNFHhFlag
	 */
	public String getNetworkFNFHhFlag() {
		return networkFNFHhFlag;
	}

	/**
	 * @param networkFNFHhFlag the networkFNFHhFlag to set
	 */
	public void setNetworkFNFHhFlag(String networkFNFHhFlag) {
		this.networkFNFHhFlag = networkFNFHhFlag;
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
	 * @return the hh0_5km
	 */
	public String getHh0_5km() {
		return hh0_5km;
	}

	/**
	 * @param hh0_5km the hh0_5km to set
	 */
	public void setHh0_5km(String hh0_5km) {
		this.hh0_5km = hh0_5km;
	}

	/**
	 * @return the hhDistKm
	 */
	public String getHhDistKm() {
		return hhDistKm;
	}

	/**
	 * @param hhDistKm the hhDistKm to set
	 */
	public void setHhDistKm(String hhDistKm) {
		this.hhDistKm = hhDistKm;
	}

	/**
	 * @return the hhFlag
	 */
	public String getHhFlag() {
		return hhFlag;
	}

	/**
	 * @param hhFlag the hhFlag to set
	 */
	public void setHhFlag(String hhFlag) {
		this.hhFlag = hhFlag;
	}

	/**
	 * @return the hhName
	 */
	public String getHhName() {
		return hhName;
	}

	/**
	 * @param hhName the hhName to set
	 */
	public void setHhName(String hhName) {
		this.hhName = hhName;
	}

	/**
	 * @return the fatgBuildingType
	 */
	public String getFatgBuildingType() {
		return fatgBuildingType;
	}

	/**
	 * @param fatgBuildingType the fatgBuildingType to set
	 */
	public void setFatgBuildingType(String fatgBuildingType) {
		this.fatgBuildingType = fatgBuildingType;
	}

	/**
	 * @return the fatgCategory
	 */
	public String getFatgCategory() {
		return fatgCategory;
	}

	/**
	 * @param fatgCategory the fatgCategory to set
	 */
	public void setFatgCategory(String fatgCategory) {
		this.fatgCategory = fatgCategory;
	}

	/**
	 * @return the fatgDistKm
	 */
	public String getFatgDistKm() {
		return fatgDistKm;
	}

	/**
	 * @param fatgDistKm the fatgDistKm to set
	 */
	public void setFatgDistKm(String fatgDistKm) {
		this.fatgDistKm = fatgDistKm;
	}

	/**
	 * @return the fatgNetworkLocationType
	 */
	public String getFatgNetworkLocationType() {
		return fatgNetworkLocationType;
	}

	/**
	 * @param fatgNetworkLocationType the fatgNetworkLocationType to set
	 */
	public void setFatgNetworkLocationType(String fatgNetworkLocationType) {
		this.fatgNetworkLocationType = fatgNetworkLocationType;
	}

	/**
	 * @return the fatgProw
	 */
	public String getFatgProw() {
		return fatgProw;
	}

	/**
	 * @param fatgProw the fatgProw to set
	 */
	public void setFatgProw(String fatgProw) {
		this.fatgProw = fatgProw;
	}

	/**
	 * @return the fatgRingType
	 */
	public String getFatgRingType() {
		return fatgRingType;
	}

	/**
	 * @param fatgRingType the fatgRingType to set
	 */
	public void setFatgRingType(String fatgRingType) {
		this.fatgRingType = fatgRingType;
	}

	/**
	 * @return the fatgTclAccess
	 */
	public String getFatgTclAccess() {
		return fatgTclAccess;
	}

	/**
	 * @param fatgTclAccess the fatgTclAccess to set
	 */
	public void setFatgTclAccess(String fatgTclAccess) {
		this.fatgTclAccess = fatgTclAccess;
	}

	/**
	 * @return the custCount
	 */
	public String getCustCount() {
		return custCount;
	}

	/**
	 * @param custCount the custCount to set
	 */
	public void setCustCount(String custCount) {
		this.custCount = custCount;
	}

	/**
	 * @return the country
	 */
	public String getCountry() {
		return country;
	}

	/**
	 * @param country the country to set
	 */
	public void setCountry(String country) {
		this.country = country;
	}

	/**
	 * @return the costPerMeter
	 */
	public String getCostPerMeter() {
		return costPerMeter;
	}

	/**
	 * @param costPerMeter the costPerMeter to set
	 */
	public void setCostPerMeter(String costPerMeter) {
		this.costPerMeter = costPerMeter;
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
	 * @return the coreCheckCc
	 */
	public String getCoreCheckCc() {
		return coreCheckCc;
	}

	/**
	 * @param coreCheckCc the coreCheckCc to set
	 */
	public void setCoreCheckCc(String coreCheckCc) {
		this.coreCheckCc = coreCheckCc;
	}

	/**
	 * @return the coreCheckHh
	 */
	public String getCoreCheckHh() {
		return coreCheckHh;
	}

	/**
	 * @param coreCheckHh the coreCheckHh to set
	 */
	public void setCoreCheckHh(String coreCheckHh) {
		this.coreCheckHh = coreCheckHh;
	}

	/**
	 * @return the closestProviderSite
	 */
	public String getClosestProviderSite() {
		return closestProviderSite;
	}

	/**
	 * @param closestProviderSite the closestProviderSite to set
	 */
	public void setClosestProviderSite(String closestProviderSite) {
		this.closestProviderSite = closestProviderSite;
	}

	/**
	 * @return the cloasestProviderSiteAddr
	 */
	public String getCloasestProviderSiteAddr() {
		return cloasestProviderSiteAddr;
	}

	/**
	 * @param cloasestProviderSiteAddr the cloasestProviderSiteAddr to set
	 */
	public void setCloasestProviderSiteAddr(String cloasestProviderSiteAddr) {
		this.cloasestProviderSiteAddr = cloasestProviderSiteAddr;
	}

	/**
	 * @return the connectedBuildingTag
	 */
	public String getConnectedBuildingTag() {
		return connectedBuildingTag;
	}

	/**
	 * @param connectedBuildingTag the connectedBuildingTag to set
	 */
	public void setConnectedBuildingTag(String connectedBuildingTag) {
		this.connectedBuildingTag = connectedBuildingTag;
	}

	/**
	 * @return the connectedCustTag
	 */
	public String getConnectedCustTag() {
		return connectedCustTag;
	}

	/**
	 * @param connectedCustTag the connectedCustTag to set
	 */
	public void setConnectedCustTag(String connectedCustTag) {
		this.connectedCustTag = connectedCustTag;
	}

	/**
	 * @return the cityTier
	 */
	public String getCityTier() {
		return cityTier;
	}

	/**
	 * @param cityTier the cityTier to set
	 */
	public void setCityTier(String cityTier) {
		this.cityTier = cityTier;
	}

	/**
	 * @return the closestProvider
	 */
	public String getClosestProvider() {
		return closestProvider;
	}

	/**
	 * @param closestProvider the closestProvider to set
	 */
	public void setClosestProvider(String closestProvider) {
		this.closestProvider = closestProvider;
	}

	/**
	 * @return the bwFlag3
	 */
	public String getBwFlag3() {
		return bwFlag3;
	}

	/**
	 * @param bwFlag3 the bwFlag3 to set
	 */
	public void setBwFlag3(String bwFlag3) {
		this.bwFlag3 = bwFlag3;
	}

	/**
	 * @return the bwFlag32
	 */
	public String getBwFlag32() {
		return bwFlag32;
	}

	/**
	 * @param bwFlag32 the bwFlag32 to set
	 */
	public void setBwFlag32(String bwFlag32) {
		this.bwFlag32 = bwFlag32;
	}



	/**
	 * @return the arcSify
	 */
	public String getArcSify() {
		return arcSify;
	}

	/**
	 * @param arcSify the arcSify to set
	 */
	public void setArcSify(String arcSify) {
		this.arcSify = arcSify;
	}

	/**
	 * @return the arcTikona
	 */
	public String getArcTikona() {
		return arcTikona;
	}

	/**
	 * @param arcTikona the arcTikona to set
	 */
	public void setArcTikona(String arcTikona) {
		this.arcTikona = arcTikona;
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

	/**
	 * @return the accessCheckCc
	 */
	public String getAccessCheckCc() {
		return accessCheckCc;
	}

	/**
	 * @param accessCheckCc the accessCheckCc to set
	 */
	public void setAccessCheckCc(String accessCheckCc) {
		this.accessCheckCc = accessCheckCc;
	}

	/**
	 * @return the accessCheckHh
	 */
	public String getAccessCheckHh() {
		return accessCheckHh;
	}

	/**
	 * @param accessCheckHh the accessCheckHh to set
	 */
	public void setAccessCheckHh(String accessCheckHh) {
		this.accessCheckHh = accessCheckHh;
	}

	@JsonProperty("old_contract_term")
	public String getOldContractTerm() {
		return oldContractTerm;
	}

	@JsonProperty("old_contract_term")
	public void setOldContractTerm(String oldContractTerm) {
		this.oldContractTerm = oldContractTerm;
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

	@JsonProperty("lat_long")
	public String getLatLong() {
		return latLong;
	}

	@JsonProperty("lat_long")
	public void setLatLong(String latLong) {
		this.latLong = latLong;
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

	public String getServiceCommissionedDate() {
		return serviceCommissionedDate;
	}

	public void setServiceCommissionedDate(String serviceCommissionedDate) {

		this.serviceCommissionedDate = serviceCommissionedDate;

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
	
	public List<AccessRingInfo> getAccessRings() {
		return accessRings;
	}
	public void setAccessRings(List<AccessRingInfo> accessRings) {
		this.accessRings = accessRings;
	}



	//For IZOSDWAN
	@JsonProperty("izo_sdwan")
	private String izoSdwan;
	
	//For IZO_INTERNET_WAN
	@JsonProperty("izo_internet_wan")
	private String izoInternetWan;

	public String getIzoSdwan() {
		return izoSdwan;
	}
	public void setIzoSdwan(String izoSdwan) {
		this.izoSdwan = izoSdwan;
	}
	
	
	// Additional Attr for sytem response.
	
	public String getIzoInternetWan() {
		return izoInternetWan;
	}
	public void setIzoInternetWan(String izoInternetWan) {
		this.izoInternetWan = izoInternetWan;
	}
		// Additional attributes for system response.
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
		
		// Addtional Attributes for System response - end
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
		// Addtional Attributes for System response - end


	public String getIsColo() {
		return isColo;
	}

	public void setIsColo(String isColo) {
		this.isColo = isColo;
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
	public String getLmArcOrderableBwOnwl() {
		return lmArcOrderableBwOnwl;
	}
	public void setLmArcOrderableBwOnwl(String lmArcOrderableBwOnwl) {
		this.lmArcOrderableBwOnwl = lmArcOrderableBwOnwl;
	}
	public String getLmArcRadwinBwOnrf() {
		return lmArcRadwinBwOnrf;
	}
	public void setLmArcRadwinBwOnrf(String lmArcRadwinBwOnrf) {
		this.lmArcRadwinBwOnrf = lmArcRadwinBwOnrf;
	}
	public String getLmNrcNetworkCapexOnwl() {
		return lmNrcNetworkCapexOnwl;
	}
	public void setLmNrcNetworkCapexOnwl(String lmNrcNetworkCapexOnwl) {
		this.lmNrcNetworkCapexOnwl = lmNrcNetworkCapexOnwl;
	}
	public String getLmOtcNrcOrderableBwOnwl() {
		return lmOtcNrcOrderableBwOnwl;
	}
	public void setLmOtcNrcOrderableBwOnwl(String lmOtcNrcOrderableBwOnwl) {
		this.lmOtcNrcOrderableBwOnwl = lmOtcNrcOrderableBwOnwl;
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


package com.tcl.dias.oms.npl.pricing.bean;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.tcl.dias.common.beans.AccessRingInfo;
import com.tcl.dias.common.beans.MuxDetailsItem;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
    "a_X2km_prospect_min_bw",
    "b_POP_DIST_KM_SERVICE_MOD",
    "intra_inter_flag",
    "a_X2km_min_bw",
    "b_lm_arc_bw_prov_ofrf",
    "b_num_connected_cust",
    "a_HH_0_5_access_rings_NF",
    "b_X2km_avg_dist",
    "b_lm_nrc_bw_onwl",
    "b_X2km_max_bw",
    "a_X2km_max_bw",
    "a_X2km_prospect_max_bw",
    "b_X2km_min_bw",
    "a_longitude_final",
    "b_access_check_CC",
    "a_num_connected_building",
    "b_Product.Name",
    "a_city_tier",
    "b_X5km_prospect_count",
    "a_sales_org",
    "b_scenario_1",
    "a_FATG_Ring_type",
    "a_pop_lat",
    "a_resp_city",
    "b_X2km_prospect_min_bw",
    "a_HH_0_5_access_rings_F",
    "site_id",
    "b_FATG_Ring_type",
    "a_X5km_prospect_min_bw",
    "a_POP_Construction_Status",
    "b_city_tier",
    "b_time_taken",
    "b_lm_nrc_mux_onwl",
    "b_sales_org",
    "b_FATG_Category",
    "b_scenario_2",
    "b_Network_F_NF_HH",
    "Selected",
    "a_hh_name",
    "b_lm_nrc_bw_prov_ofrf",
    "b_latitude_final",
    "b_connected_cust_tag",
    "a_POP_Building_Type",
    "a_X0.5km_prospect_avg_bw",
    "a_lm_arc_bw_onwl",
    "b_X2km_prospect_max_bw",
    "b_X5km_prospect_min_bw",
    "b_X0.5km_prospect_count",
    "b_OnnetCity_tag",
    "a_pop_network_loc_id",
    "a_X0.5km_avg_bw",
    "a_X2km_prospect_avg_dist",
    "pop2pop_network_check",
    "a_POP_Category",
    "b_POP_DIST_KM_SERVICE",
    "a_X5km_prospect_avg_dist",
    "b_pop_long",
    "a_Predicted_Access_Feasibility",
    "a_POP_Network_Location_Type",
    "a_scenario_1",
    "a_scenario_2",
    "b_POP_Building_Type",
    "b_X2km_prospect_count",
    "a_FATG_DIST_KM",
    "b_net_pre_feasible_flag",
    "a_X5km_min_bw",
    "a_X0.5km_prospect_max_bw",
    "a_X2km_prospect_perc_feasible",
    "b_X5km_prospect_max_bw",
    "b_pop_name",
    "b_X0.5km_avg_bw",
    "b_X5km_max_bw",
    "a_Network_F_NF_CC_Flag",
    "b_Orch_BW",
    "a_X0.5km_prospect_perc_feasible",
    "a_site_id",
    "b_Predicted_Access_Feasibility",
    "burstable_bw",
    "a_Product.Name",
    "b_X5km_prospect_num_feasible",
    "bw_mbps",
    "b_X5km_prospect_avg_bw",
    "a_X2km_prospect_count",
    "b_X2km_prospect_min_dist",
    "b_FATG_TCL_Access",
    "b_last_mile_contract_term",
    "b_customer_segment",
    "a_num_connected_cust",
    "b_longitude_final",
    "a_POP_DIST_KM",
    "b_pop_address",
    "b_hh_name",
    "a_lm_nrc_nerental_onwl",
    "b_lm_nrc_inbldg_onwl",
    "account_id_with_18_digit",
    "a_lm_nrc_bw_onwl",
    "a_X2km_cust_count",
    "b_Network_Feasibility_Check",
    "b_FATG_DIST_KM",
    "a_X5km_avg_bw",
    "b_lm_nrc_ospcapex_onwl",
    "b_core_check_hh",
    "a_Network_F_NF_HH",
    "a_error_code",
    "b_X2km_min_dist",
    "b_a_or_b_end",
    "a_Network_Feasibility_Check",
    "b_X2km_prospect_num_feasible",
    "bw_mbps_upd",
    "a_access_check_hh",
    "b_cost_permeter",
    "b_X0.5km_min_bw",
    "a_X0.5km_prospect_count",
    "a_X0.5km_avg_dist",
    "a_FATG_PROW",
    "b_X2km_prospect_perc_feasible",
    "a_X2km_min_dist",
    "a_lm_nrc_mast_ofrf",
    "a_X2km_prospect_num_feasible",
    "a_SERVICE_ID",
    "b_connected_building_tag",
    "b_pop_lat",
    "b_X5km_avg_dist",
    "a_lm_nrc_mast_onrf",
    "a_HH_DIST_KM",
    "a_pop_selected",
    "a_min_hh_fatg",
    "a_X5km_avg_dist",
    "b_X5km_prospect_min_dist",
    "b_X2km_prospect_avg_bw",
    "a_latitude_final",
    "b_Network_F_NF_CC",
    "b_POP_Category",
    "a_cost_permeter",
    "b_X0.5km_min_dist",
    "b_lm_arc_bw_onwl",
    "b_Probabililty_Access_Feasibility",
    "b_X0.5km_avg_dist",
    "a_lm_arc_bw_onrf",
    "link_id",
    "a_lm_nrc_bw_prov_ofrf",
    "pop2pop_network_flag",
    "a_POP_TCL_Access",
    "b_POP_Network_Location_Type",
    "a_FATG_TCL_Access",
    "b_error_flag",
    "a_lm_nrc_mux_onwl",
    "sla_varient",
    "a_lm_nrc_bw_onrf",
    "a_pop_ui_id",
    "b_HH_0_5_access_rings_F",
    "b_total_cost",
    "b_X0.5km_prospect_avg_dist",
    "a_core_check_hh",
    "b_Network_F_NF_HH_Flag",
    "a_local_loop_interface",
    "a_Probabililty_Access_Feasibility",
    "b_HH_0_5_access_rings_NF",
    "manual_flag",
    "a_lm_arc_bw_prov_ofrf",
    "b_X0.5km_prospect_min_bw",
    "b_X5km_cust_count",
    "b_X0.5km_prospect_num_feasible",
    "b_FATG_PROW",
    "opportunity_term",
    "b_num_connected_building",
    "a_X0.5km_min_bw",
    "a_X5km_prospect_count",
    "a_lm_nrc_inbldg_onwl",
    "a_X2km_avg_bw",
    "b_lm_arc_bw_onrf",
    "b_X2km_avg_bw",
    "b_hh_flag",
    "b_pop_ui_id",
    "a_Network_F_NF_HH_Flag",
    "a_error_flag",
    "a_X0.5km_min_dist",
    "a_Orch_Connection",
    "b_X5km_min_dist",
    "b_X2km_prospect_avg_dist",
    "b_error_msg",
    "a_last_mile_contract_term",
    "a_OnnetCity_tag",
    "a_POP_DIST_KM_SERVICE",
    "dist_betw_pops",
    "a_connected_cust_tag",
    "b_Orch_LM_Type",
    "a_X5km_prospect_num_feasible",
    "b_pop_network_loc_id",
    "Type",
    "b_pop_selected",
    "a_access_check_CC",
    "a_X0.5km_prospect_min_dist",
    "a_FATG_Category",
    "a_X0.5km_cust_count",
    "b_X0.5km_prospect_min_dist",
    "b_HH_0_5km",
    "a_X5km_cust_count",
    "a_X5km_prospect_avg_bw",
    "a_hh_flag",
    "a_X2km_prospect_min_dist",
    "b_X5km_min_bw",
    "b_X0.5km_prospect_avg_bw",
    "b_HH_DIST_KM",
    "b_X5km_prospect_perc_feasible",
    "b_POP_Construction_Status",
    "a_net_pre_feasible_flag",
    "prospect_name",
    "Predicted_Access_Feasibility",
    "a_time_taken",
    "a_total_cost",
    "b_error_code",
    "a_X5km_min_dist",
    "b_X5km_prospect_avg_dist",
    "a_pop_address",
    "a_X0.5km_prospect_avg_dist",
    "b_POP_TCL_Access",
    "b_core_check_CC",
    "a_X5km_prospect_max_bw",
    "b_POP_DIST_KM",
    "product_name",
    "a_Network_F_NF_CC",
    "a_X5km_max_bw",
    "b_lm_nrc_bw_onrf",
    "a_lm_nrc_ospcapex_onwl",
    "a_X2km_avg_dist",
    "b_Network_F_NF_CC_Flag",
    "b_lm_nrc_mast_ofrf",
    "b_access_check_hh",
    "b_FATG_Network_Location_Type",
    "b_X2km_cust_count",
    "b_lm_nrc_mast_onrf",
    "a_X2km_prospect_avg_bw",
    "a_X5km_prospect_perc_feasible",
    "b_X5km_avg_bw",
    "a_connected_building_tag",
    "a_pop_name",
    "a_core_check_CC",
    "feasibility_response_created_date",
    "b_resp_city",
    "a_FATG_Network_Location_Type",
    "a_pop_long",
    "a_X0.5km_prospect_min_bw",
    "a_POP_DIST_KM_SERVICE_MOD",
    "a_X5km_prospect_min_dist",
    "a_Orch_BW",
    "b_Orch_Category",
    "b_X0.5km_max_bw",
    "b_site_id",
    "a_HH_0_5km",
    "a_X0.5km_max_bw",
    "a_X0.5km_prospect_num_feasible",
    "a_a_or_b_end",
    "b_X0.5km_cust_count",
    "a_Orch_LM_Type",
    "b_lm_nrc_nerental_onwl",
    "b_local_loop_interface",
    "a_FATG_Building_Type",
    "a_Orch_Category",
    "chargeable_distance",
    "b_X0.5km_prospect_perc_feasible",
    "b_FATG_Building_Type",
    "b_Orch_Connection",
    "b_min_hh_fatg",
    "b_X0.5km_prospect_max_bw",
    "quotetype_quote",
    "a_error_msg",
    "b_SERVICE_ID",
    "a_customer_segment",
    "version",
    "error_msg_display",
    //macd attributes
    "macd_service",
	"macd_option",
	"trigger_feasibility",
	"service_id",
	"access_provider",
	"lat_long",
	"zip_code",
	"cu_le_id",
	"service_commissioned_date",
	"old_Ll_Bw",
	"old_contract_term",
	"prd_category",
	"address",
	"bandwidth",
	"ll_change",
	"a_macd_service",
	"a_macd_option",
	"a_trigger_feasibility",
	"a_service_id",
	"a_access_provider",
	"a_lat_long",
	"a_zip_code",
	"a_cu_le_id",
	"a_service_commissioned_date",
	"a_old_Ll_Bw",
	"a_old_contract_term",
	"a_prd_category",
	"a_address",
	"a_bandwidth",
	"a_ll_change",
	"b_macd_service",
	"b_macd_option",
	"b_trigger_feasibility",
	"b_service_id",
	"b_access_provider",
	"b_lat_long",
	"b_zip_code",
	"b_cu_le_id",
	"b_service_commissioned_date",
	"b_old_Ll_Bw",
	"b_old_contract_term",
	"b_prd_category",
	"b_address",
	"b_bandwidth",
	"b_ll_change",

	"isHub",

//	CST-35 customer portal change
	"is_customer",
	
	// for MF
		"a_otc_modem_charges",
		"a_lm_nrc_bw_prov_ofwl",
		"a_arc_modem_charges",
		"a_arc_bw",
		"a_lm_nrc_prow_onwl",
		"a_lm_arc_prow_onwl",
		"a_lm_arc_converter_charges_onrf",
		"a_lm_arc_bw_backhaul_onrf",
		"a_lm_arc_colocation_onrf",
		"a_lm_otc_modem_charges_offwl",
		"a_lm_otc_nrc_installation_offwl",
		"a_lm_arc_modem_charges_offwl",
		"a_lm_arc_bw_offwl",
		"a_provider_name",
		"a_BHConnectivity",
		"a_lm_arc_orderable_bw_onwl",
	    "a_lm_otc_nrc_orderable_bw_onwl",
	    "a_lm_nrc_network_capex_onwl",
	    "a_lm_arc_radwin_bw_onrf",
	    
	    
	    "b_otc_modem_charges",
		"b_lm_nrc_bw_prov_ofwl",
		"b_arc_modem_charges",
		"b_arc_bw",
		"b_lm_nrc_prow_onwl",
		"b_lm_arc_prow_onwl",
		"b_lm_arc_converter_charges_onrf",
		"b_lm_arc_bw_backhaul_onrf",
		"b_lm_arc_colocation_onrf",
		"b_lm_otc_modem_charges_offwl",
		"b_lm_otc_nrc_installation_offwl",
		"b_lm_arc_modem_charges_offwl",
		"b_lm_arc_bw_offwl",
		"b_provider_name",
		"b_BHConnectivity",
		"b_lm_arc_orderable_bw_onwl",
	    "b_lm_otc_nrc_orderable_bw_onwl",
	    "b_lm_nrc_network_capex_onwl",
	    "b_lm_arc_radwin_bw_onrf",
	    
	    "a_parallel_run_days",
		"b_parallel_run_days",

		
		"a_local_loop_bw",
		"b_local_loop_bw",
		
		"a_error_msg_display",
		"b_error_msg_display",


		 // PIPF152
	 	"feasibility_response_id",
	 	"task_id",
        "partner_account_id_with_18_digit",
        "partner_profile",
        "quotetype_partner",
        "solution_type",
        
        // PIPF-143
        "a_prow_cost_type",
        "a_prow_message",
        "a_prow_flag",
        "a_lm_arc_prow_gpon_onwl",
        "a_lm_nrc_prow_gpon_onwl",
        "a_lm_arc_prow_prow_onwl",
        "a_lm_nrc_prow_prow_onwl",
        
        "b_prow_cost_type",
        "b_prow_message",
        "b_prow_flag",
        "b_lm_arc_prow_gpon_onwl",
        "b_lm_nrc_prow_gpon_onwl",
        "b_lm_arc_prow_prow_onwl",
        "b_lm_nrc_prow_prow_onwl",
        "feasibility_remarks"

})
public class NotFeasible {

    @JsonProperty("a_X2km_prospect_min_bw")
    private String aX2kmProspectMinBw;
    @JsonProperty("b_POP_DIST_KM_SERVICE_MOD")
    private String bPOPDISTKMSERVICEMOD;
    @JsonProperty("intra_inter_flag")
    private String intraInterFlag;
    @JsonProperty("a_X2km_min_bw")
    private String aX2kmMinBw;
    @JsonProperty("b_lm_arc_bw_prov_ofrf")
    private String bLmArcBwProvOfrf;
    @JsonProperty("b_num_connected_cust")
    private String bNumConnectedCust;
    @JsonProperty("a_HH_0_5_access_rings_NF")
    private String aHH05AccessRingsNF;
    @JsonProperty("b_X2km_avg_dist")
    private String bX2kmAvgDist;
    @JsonProperty("b_lm_nrc_bw_onwl")
    private String bLmNrcBwOnwl;
    @JsonProperty("b_X2km_max_bw")
    private String bX2kmMaxBw;
    @JsonProperty("a_X2km_max_bw")
    private String aX2kmMaxBw;
    @JsonProperty("a_X2km_prospect_max_bw")
    private String aX2kmProspectMaxBw;
    @JsonProperty("b_X2km_min_bw")
    private String bX2kmMinBw;
    @JsonProperty("a_longitude_final")
    private String aLongitudeFinal;
    @JsonProperty("b_access_check_CC")
    private String bAccessCheckCC;
    @JsonProperty("a_num_connected_building")
    private String aNumConnectedBuilding;
    @JsonProperty("b_Product.Name")
    private String bProductName;
    @JsonProperty("a_city_tier")
    private String aCityTier;
    @JsonProperty("b_X5km_prospect_count")
    private String bX5kmProspectCount;
    @JsonProperty("a_sales_org")
    private String aSalesOrg;
    @JsonProperty("b_scenario_1")
    private String bScenario1;
    @JsonProperty("a_FATG_Ring_type")
    private String aFATGRingType;
    @JsonProperty("a_pop_lat")
    private String aPopLat;
    @JsonProperty("a_resp_city")
    private String aRespCity;
    @JsonProperty("b_X2km_prospect_min_bw")
    private String bX2kmProspectMinBw;
    @JsonProperty("a_HH_0_5_access_rings_F")
    private String aHH05AccessRingsF;
    @JsonProperty("site_id")
    private String siteId;
    @JsonProperty("b_FATG_Ring_type")
    private String bFATGRingType;
    @JsonProperty("a_X5km_prospect_min_bw")
    private String aX5kmProspectMinBw;
    @JsonProperty("a_POP_Construction_Status")
    private String aPOPConstructionStatus;
    @JsonProperty("b_city_tier")
    private String bCityTier;
    @JsonProperty("b_time_taken")
    private String bTimeTaken;
    @JsonProperty("b_lm_nrc_mux_onwl")
    private String bLmNrcMuxOnwl;
    @JsonProperty("b_sales_org")
    private String bSalesOrg;
    @JsonProperty("b_FATG_Category")
    private String bFATGCategory;
    @JsonProperty("b_scenario_2")
    private String bScenario2;
    @JsonProperty("b_Network_F_NF_HH")
    private String bNetworkFNFHH;
    @JsonProperty("Selected")
    private Boolean selected;
    @JsonProperty("a_hh_name")
    private String aHhName;
    @JsonProperty("b_lm_nrc_bw_prov_ofrf")
    private String bLmNrcBwProvOfrf;
    @JsonProperty("b_latitude_final")
    private String bLatitudeFinal;
    @JsonProperty("b_connected_cust_tag")
    private String bConnectedCustTag;
    @JsonProperty("a_POP_Building_Type")
    private String aPOPBuildingType;
    @JsonProperty("a_X0.5km_prospect_avg_bw")
    private String aX05kmProspectAvgBw;
    @JsonProperty("a_lm_arc_bw_onwl")
    private String aLmArcBwOnwl;
    @JsonProperty("b_X2km_prospect_max_bw")
    private String bX2kmProspectMaxBw;
    @JsonProperty("b_X5km_prospect_min_bw")
    private String bX5kmProspectMinBw;
    @JsonProperty("b_X0.5km_prospect_count")
    private String bX05kmProspectCount;
    @JsonProperty("b_OnnetCity_tag")
    private String bOnnetCityTag;
    @JsonProperty("a_pop_network_loc_id")
    private String aPopNetworkLocId;
    @JsonProperty("a_X0.5km_avg_bw")
    private String aX05kmAvgBw;
    @JsonProperty("a_X2km_prospect_avg_dist")
    private String aX2kmProspectAvgDist;
    @JsonProperty("pop2pop_network_check")
    private String pop2popNetworkCheck;
    @JsonProperty("a_POP_Category")
    private String aPOPCategory;
    @JsonProperty("b_POP_DIST_KM_SERVICE")
    private String bPOPDISTKMSERVICE;
    @JsonProperty("a_X5km_prospect_avg_dist")
    private String aX5kmProspectAvgDist;
    @JsonProperty("b_pop_long")
    private String bPopLong;
    @JsonProperty("a_Predicted_Access_Feasibility")
    private String aPredictedAccessFeasibility;
    @JsonProperty("a_POP_Network_Location_Type")
    private String aPOPNetworkLocationType;
    @JsonProperty("a_scenario_1")
    private String aScenario1;
    @JsonProperty("a_scenario_2")
    private String aScenario2;
    @JsonProperty("b_POP_Building_Type")
    private String bPOPBuildingType;
    @JsonProperty("b_X2km_prospect_count")
    private String bX2kmProspectCount;
    @JsonProperty("a_FATG_DIST_KM")
    private String aFATGDISTKM;
    @JsonProperty("b_net_pre_feasible_flag")
    private String bNetPreFeasibleFlag;
    @JsonProperty("a_X5km_min_bw")
    private String aX5kmMinBw;
    @JsonProperty("a_X0.5km_prospect_max_bw")
    private String aX05kmProspectMaxBw;
    @JsonProperty("a_X2km_prospect_perc_feasible")
    private String aX2kmProspectPercFeasible;
    @JsonProperty("b_X5km_prospect_max_bw")
    private String bX5kmProspectMaxBw;
    @JsonProperty("b_pop_name")
    private String bPopName;
    @JsonProperty("b_X0.5km_avg_bw")
    private String bX05kmAvgBw;
    @JsonProperty("b_X5km_max_bw")
    private String bX5kmMaxBw;
    @JsonProperty("a_Network_F_NF_CC_Flag")
    private String aNetworkFNFCCFlag;
    @JsonProperty("b_Orch_BW")
    private String bOrchBW;
    @JsonProperty("a_X0.5km_prospect_perc_feasible")
    private String aX05kmProspectPercFeasible;
    @JsonProperty("a_site_id")
    private String aSiteId;
    @JsonProperty("b_Predicted_Access_Feasibility")
    private String bPredictedAccessFeasibility;
    @JsonProperty("burstable_bw")
    private String burstableBw;
    @JsonProperty("a_Product.Name")
    private String aProductName;
    @JsonProperty("b_X5km_prospect_num_feasible")
    private String bX5kmProspectNumFeasible;
    @JsonProperty("bw_mbps")
    private String bwMbps;
    @JsonProperty("b_X5km_prospect_avg_bw")
    private String bX5kmProspectAvgBw;
    @JsonProperty("a_X2km_prospect_count")
    private String aX2kmProspectCount;
    @JsonProperty("b_X2km_prospect_min_dist")
    private String bX2kmProspectMinDist;
    @JsonProperty("b_FATG_TCL_Access")
    private String bFATGTCLAccess;
    @JsonProperty("b_last_mile_contract_term")
    private String bLastMileContractTerm;
    @JsonProperty("b_customer_segment")
    private String bCustomerSegment;
    @JsonProperty("a_num_connected_cust")
    private String aNumConnectedCust;
    @JsonProperty("b_longitude_final")
    private String bLongitudeFinal;
    @JsonProperty("a_POP_DIST_KM")
    private String aPOPDISTKM;
    @JsonProperty("b_pop_address")
    private String bPopAddress;
    @JsonProperty("b_hh_name")
    private String bHhName;
    @JsonProperty("a_lm_nrc_nerental_onwl")
    private String aLmNrcNerentalOnwl;
    @JsonProperty("b_lm_nrc_inbldg_onwl")
    private String bLmNrcInbldgOnwl;
    @JsonProperty("account_id_with_18_digit")
    private String accountIdWith18Digit;
    @JsonProperty("a_lm_nrc_bw_onwl")
    private String aLmNrcBwOnwl;
    @JsonProperty("a_X2km_cust_count")
    private String aX2kmCustCount;
    @JsonProperty("b_Network_Feasibility_Check")
    private String bNetworkFeasibilityCheck;
    @JsonProperty("b_FATG_DIST_KM")
    private String bFATGDISTKM;
    @JsonProperty("a_X5km_avg_bw")
    private String aX5kmAvgBw;
    @JsonProperty("b_lm_nrc_ospcapex_onwl")
    private String bLmNrcOspcapexOnwl;
    @JsonProperty("b_core_check_hh")
    private String bCoreCheckHh;
    @JsonProperty("a_Network_F_NF_HH")
    private String aNetworkFNFHH;
    @JsonProperty("a_error_code")
    private String aErrorCode;
    @JsonProperty("b_X2km_min_dist")
    private String bX2kmMinDist;
    @JsonProperty("b_a_or_b_end")
    private String bAOrBEnd;
    @JsonProperty("a_Network_Feasibility_Check")
    private String aNetworkFeasibilityCheck;
    @JsonProperty("b_X2km_prospect_num_feasible")
    private String bX2kmProspectNumFeasible;
    @JsonProperty("bw_mbps_upd")
    private String bwMbpsUpd;
    @JsonProperty("a_access_check_hh")
    private String aAccessCheckHh;
    @JsonProperty("b_cost_permeter")
    private String bCostPermeter;
    @JsonProperty("b_X0.5km_min_bw")
    private String bX05kmMinBw;
    @JsonProperty("a_X0.5km_prospect_count")
    private String aX05kmProspectCount;
    @JsonProperty("a_X0.5km_avg_dist")
    private String aX05kmAvgDist;
    @JsonProperty("a_FATG_PROW")
    private String aFATGPROW;
    @JsonProperty("b_X2km_prospect_perc_feasible")
    private String bX2kmProspectPercFeasible;
    @JsonProperty("a_X2km_min_dist")
    private String aX2kmMinDist;
    @JsonProperty("a_lm_nrc_mast_ofrf")
    private String aLmNrcMastOfrf;
    @JsonProperty("a_X2km_prospect_num_feasible")
    private String aX2kmProspectNumFeasible;
    @JsonProperty("a_SERVICE_ID")
    private String aSERVICEID;
    @JsonProperty("b_connected_building_tag")
    private String bConnectedBuildingTag;
    @JsonProperty("b_pop_lat")
    private String bPopLat;
    @JsonProperty("b_X5km_avg_dist")
    private String bX5kmAvgDist;
    @JsonProperty("a_lm_nrc_mast_onrf")
    private String aLmNrcMastOnrf;
    @JsonProperty("a_HH_DIST_KM")
    private String aHHDISTKM;
    @JsonProperty("a_pop_selected")
    private String aPopSelected;
    @JsonProperty("a_min_hh_fatg")
    private String aMinHhFatg;
    @JsonProperty("a_X5km_avg_dist")
    private String aX5kmAvgDist;
    @JsonProperty("b_X5km_prospect_min_dist")
    private String bX5kmProspectMinDist;
    @JsonProperty("b_X2km_prospect_avg_bw")
    private String bX2kmProspectAvgBw;
    @JsonProperty("a_latitude_final")
    private String aLatitudeFinal;
    @JsonProperty("b_Network_F_NF_CC")
    private String bNetworkFNFCC;
    @JsonProperty("b_POP_Category")
    private String bPOPCategory;
    @JsonProperty("a_cost_permeter")
    private String aCostPermeter;
    @JsonProperty("b_X0.5km_min_dist")
    private String bX05kmMinDist;
    @JsonProperty("b_lm_arc_bw_onwl")
    private String bLmArcBwOnwl;
    @JsonProperty("b_Probabililty_Access_Feasibility")
    private String bProbabililtyAccessFeasibility;
    @JsonProperty("b_X0.5km_avg_dist")
    private String bX05kmAvgDist;
    @JsonProperty("a_lm_arc_bw_onrf")
    private String aLmArcBwOnrf;
    @JsonProperty("link_id")
    private String linkId;
    @JsonProperty("a_lm_nrc_bw_prov_ofrf")
    private String aLmNrcBwProvOfrf;
    @JsonProperty("pop2pop_network_flag")
    private String pop2popNetworkFlag;
    @JsonProperty("a_POP_TCL_Access")
    private String aPOPTCLAccess;
    @JsonProperty("b_POP_Network_Location_Type")
    private String bPOPNetworkLocationType;
    @JsonProperty("a_FATG_TCL_Access")
    private String aFATGTCLAccess;
    @JsonProperty("b_error_flag")
    private String bErrorFlag;
    @JsonProperty("a_lm_nrc_mux_onwl")
    private String aLmNrcMuxOnwl;
    @JsonProperty("sla_varient")
    private String slaVarient;
    @JsonProperty("a_lm_nrc_bw_onrf")
    private String aLmNrcBwOnrf;
    @JsonProperty("a_pop_ui_id")
    private String aPopUiId;
    @JsonProperty("b_HH_0_5_access_rings_F")
    private String bHH05AccessRingsF;
    @JsonProperty("b_total_cost")
    private String bTotalCost;
    @JsonProperty("b_X0.5km_prospect_avg_dist")
    private String bX05kmProspectAvgDist;
    @JsonProperty("a_core_check_hh")
    private String aCoreCheckHh;
    @JsonProperty("b_Network_F_NF_HH_Flag")
    private String bNetworkFNFHHFlag;
    @JsonProperty("a_local_loop_interface")
    private String aLocalLoopInterface;
    @JsonProperty("a_Probabililty_Access_Feasibility")
    private String aProbabililtyAccessFeasibility;
    @JsonProperty("b_HH_0_5_access_rings_NF")
    private String bHH05AccessRingsNF;
    @JsonProperty("manual_flag")
    private String manualFlag;
    @JsonProperty("a_lm_arc_bw_prov_ofrf")
    private String aLmArcBwProvOfrf;
    @JsonProperty("b_X0.5km_prospect_min_bw")
    private String bX05kmProspectMinBw;
    @JsonProperty("b_X5km_cust_count")
    private String bX5kmCustCount;
    @JsonProperty("b_X0.5km_prospect_num_feasible")
    private String bX05kmProspectNumFeasible;
    @JsonProperty("b_FATG_PROW")
    private String bFATGPROW;
    @JsonProperty("opportunity_term")
    private String opportunityTerm;
    @JsonProperty("b_num_connected_building")
    private String bNumConnectedBuilding;
    @JsonProperty("a_X0.5km_min_bw")
    private String aX05kmMinBw;
    @JsonProperty("a_X5km_prospect_count")
    private String aX5kmProspectCount;
    @JsonProperty("a_lm_nrc_inbldg_onwl")
    private String aLmNrcInbldgOnwl;
    @JsonProperty("a_X2km_avg_bw")
    private String aX2kmAvgBw;
    @JsonProperty("b_lm_arc_bw_onrf")
    private String bLmArcBwOnrf;
    @JsonProperty("b_X2km_avg_bw")
    private String bX2kmAvgBw;
    @JsonProperty("b_hh_flag")
    private String bHhFlag;
    @JsonProperty("b_pop_ui_id")
    private String bPopUiId;
    @JsonProperty("a_Network_F_NF_HH_Flag")
    private String aNetworkFNFHHFlag;
    @JsonProperty("a_error_flag")
    private String aErrorFlag;
    @JsonProperty("a_X0.5km_min_dist")
    private String aX05kmMinDist;
    @JsonProperty("a_Orch_Connection")
    private String aOrchConnection;
    @JsonProperty("b_X5km_min_dist")
    private String bX5kmMinDist;
    @JsonProperty("b_X2km_prospect_avg_dist")
    private String bX2kmProspectAvgDist;
    @JsonProperty("b_error_msg")
    private String bErrorMsg;
    @JsonProperty("a_last_mile_contract_term")
    private String aLastMileContractTerm;
    @JsonProperty("a_OnnetCity_tag")
    private String aOnnetCityTag;
    @JsonProperty("a_POP_DIST_KM_SERVICE")
    private String aPOPDISTKMSERVICE;
    @JsonProperty("dist_betw_pops")
    private String distBetwPops;
    @JsonProperty("a_connected_cust_tag")
    private String aConnectedCustTag;
    @JsonProperty("b_Orch_LM_Type")
    private String bOrchLMType;
    @JsonProperty("a_X5km_prospect_num_feasible")
    private String aX5kmProspectNumFeasible;
    @JsonProperty("b_pop_network_loc_id")
    private String bPopNetworkLocId;
    @JsonProperty("Type")
    private String type;
    @JsonProperty("b_pop_selected")
    private String bPopSelected;
    @JsonProperty("a_access_check_CC")
    private String aAccessCheckCC;
    @JsonProperty("a_X0.5km_prospect_min_dist")
    private String aX05kmProspectMinDist;
    @JsonProperty("a_FATG_Category")
    private String aFATGCategory;
    @JsonProperty("a_X0.5km_cust_count")
    private String aX05kmCustCount;
    @JsonProperty("b_X0.5km_prospect_min_dist")
    private String bX05kmProspectMinDist;
    @JsonProperty("b_HH_0_5km")
    private String bHH05km;
    @JsonProperty("a_X5km_cust_count")
    private String aX5kmCustCount;
    @JsonProperty("a_X5km_prospect_avg_bw")
    private String aX5kmProspectAvgBw;
    @JsonProperty("a_hh_flag")
    private String aHhFlag;
    @JsonProperty("a_X2km_prospect_min_dist")
    private String aX2kmProspectMinDist;
    @JsonProperty("b_X5km_min_bw")
    private String bX5kmMinBw;
    @JsonProperty("b_X0.5km_prospect_avg_bw")
    private String bX05kmProspectAvgBw;
    @JsonProperty("b_HH_DIST_KM")
    private String bHHDISTKM;
    @JsonProperty("b_X5km_prospect_perc_feasible")
    private String bX5kmProspectPercFeasible;
    @JsonProperty("b_POP_Construction_Status")
    private String bPOPConstructionStatus;
    @JsonProperty("a_net_pre_feasible_flag")
    private String aNetPreFeasibleFlag;
    @JsonProperty("prospect_name")
    private String prospectName;
    @JsonProperty("Predicted_Access_Feasibility")
    private String predictedAccessFeasibility;
    @JsonProperty("a_time_taken")
    private String aTimeTaken;
    @JsonProperty("a_total_cost")
    private String aTotalCost;
    @JsonProperty("b_error_code")
    private String bErrorCode;
    @JsonProperty("a_X5km_min_dist")
    private String aX5kmMinDist;
    @JsonProperty("b_X5km_prospect_avg_dist")
    private String bX5kmProspectAvgDist;
    @JsonProperty("a_pop_address")
    private String aPopAddress;
    @JsonProperty("a_X0.5km_prospect_avg_dist")
    private String aX05kmProspectAvgDist;
    @JsonProperty("b_POP_TCL_Access")
    private String bPOPTCLAccess;
    @JsonProperty("b_core_check_CC")
    private String bCoreCheckCC;
    @JsonProperty("a_X5km_prospect_max_bw")
    private String aX5kmProspectMaxBw;
    @JsonProperty("b_POP_DIST_KM")
    private String bPOPDISTKM;
    @JsonProperty("product_name")
    private String productName;
    @JsonProperty("a_Network_F_NF_CC")
    private String aNetworkFNFCC;
    @JsonProperty("a_X5km_max_bw")
    private String aX5kmMaxBw;
    @JsonProperty("b_lm_nrc_bw_onrf")
    private String bLmNrcBwOnrf;
    @JsonProperty("a_lm_nrc_ospcapex_onwl")
    private String aLmNrcOspcapexOnwl;
    @JsonProperty("a_X2km_avg_dist")
    private String aX2kmAvgDist;
    @JsonProperty("b_Network_F_NF_CC_Flag")
    private String bNetworkFNFCCFlag;
    @JsonProperty("b_lm_nrc_mast_ofrf")
    private String bLmNrcMastOfrf;
    @JsonProperty("b_access_check_hh")
    private String bAccessCheckHh;
    @JsonProperty("b_FATG_Network_Location_Type")
    private String bFATGNetworkLocationType;
    @JsonProperty("b_X2km_cust_count")
    private String bX2kmCustCount;
    @JsonProperty("b_lm_nrc_mast_onrf")
    private String bLmNrcMastOnrf;
    @JsonProperty("a_X2km_prospect_avg_bw")
    private String aX2kmProspectAvgBw;
    @JsonProperty("a_X5km_prospect_perc_feasible")
    private String aX5kmProspectPercFeasible;
    @JsonProperty("b_X5km_avg_bw")
    private String bX5kmAvgBw;
    @JsonProperty("a_connected_building_tag")
    private String aConnectedBuildingTag;
    @JsonProperty("a_pop_name")
    private String aPopName;
    @JsonProperty("a_core_check_CC")
    private String aCoreCheckCC;
    @JsonProperty("feasibility_response_created_date")
    private String feasibilityResponseCreatedDate;
    @JsonProperty("b_resp_city")
    private String bRespCity;
    @JsonProperty("a_FATG_Network_Location_Type")
    private String aFATGNetworkLocationType;
    @JsonProperty("a_pop_long")
    private String aPopLong;
    @JsonProperty("a_X0.5km_prospect_min_bw")
    private String aX05kmProspectMinBw;
    @JsonProperty("a_POP_DIST_KM_SERVICE_MOD")
    private String aPOPDISTKMSERVICEMOD;
    @JsonProperty("a_X5km_prospect_min_dist")
    private String aX5kmProspectMinDist;
    @JsonProperty("a_Orch_BW")
    private String aOrchBW;
    @JsonProperty("b_Orch_Category")
    private String bOrchCategory;
    @JsonProperty("b_X0.5km_max_bw")
    private String bX05kmMaxBw;
    @JsonProperty("b_site_id")
    private String bSiteId;
    @JsonProperty("a_HH_0_5km")
    private String aHH05km;
    @JsonProperty("a_X0.5km_max_bw")
    private String aX05kmMaxBw;
    @JsonProperty("a_X0.5km_prospect_num_feasible")
    private String aX05kmProspectNumFeasible;
    @JsonProperty("a_a_or_b_end")
    private String aAOrBEnd;
    @JsonProperty("b_X0.5km_cust_count")
    private String bX05kmCustCount;
    @JsonProperty("a_Orch_LM_Type")
    private String aOrchLMType;
    @JsonProperty("b_lm_nrc_nerental_onwl")
    private String bLmNrcNerentalOnwl;
    @JsonProperty("b_local_loop_interface")
    private String bLocalLoopInterface;
    @JsonProperty("a_FATG_Building_Type")
    private String aFATGBuildingType;
    @JsonProperty("a_Orch_Category")
    private String aOrchCategory;
    @JsonProperty("chargeable_distance")
    private String chargeableDistance;
    @JsonProperty("b_X0.5km_prospect_perc_feasible")
    private String bX05kmProspectPercFeasible;
    @JsonProperty("b_FATG_Building_Type")
    private String bFATGBuildingType;
    @JsonProperty("b_Orch_Connection")
    private String bOrchConnection;
    @JsonProperty("b_min_hh_fatg")
    private String bMinHhFatg;
    @JsonProperty("b_X0.5km_prospect_max_bw")
    private String bX05kmProspectMaxBw;
    @JsonProperty("quotetype_quote")
    private String quotetypeQuote;
    @JsonProperty("a_error_msg")
    private String aErrorMsg;
    @JsonProperty("b_SERVICE_ID")
    private String bSERVICEID;
    @JsonProperty("a_customer_segment")
    private String aCustomerSegment;
    @JsonProperty("rank")
    private Integer rank;
    
    @JsonProperty("a_access_rings")
	private List<AccessRingInfo> aAccessRings;
    @JsonProperty("b_access_rings")
	private List<AccessRingInfo> bAccessRings;
    
    @JsonProperty("a_mux_details")
	private List<MuxDetailsItem> aMuxDetails;
    @JsonProperty("b_mux_details")
	private List<MuxDetailsItem> bMuxDetails;
    
    
    @JsonProperty("version")
	private String version;
    
    @JsonProperty("error_msg_display")
	private String errorMsgDisplay;
    
    //macd attributes
    @JsonProperty("macd_option")
	private String macdOption;
	@JsonProperty("trigger_feasibility")
	private String triggerFeasibility;
	@JsonProperty("macd_service")
	private String macdService;
	@JsonProperty("service_id")
	private String serviceId;
	@JsonProperty("access_provider")
	private String accessProvider;
	@JsonProperty("lat_long")
	private String latLong;
	@JsonProperty("zip_code")
	private String zipCode;
	@JsonProperty("cu_le_id")
	private String cuLeId;
	@JsonProperty("service_commissioned_date")
	private String serviceCommissionedDate;
	@JsonProperty("old_Ll_Bw")
	private String oldLlBw;
	@JsonProperty("old_contract_term")
	private String oldContractTerm;
	@JsonProperty("prd_category")
	private String prdCategory;
	@JsonProperty("address")
	private String address;
	@JsonProperty("bandwidth")
	private String bandwidth;
	@JsonProperty("ll_change")
	private String llChange;
	
	@JsonProperty("a_macd_option")
	private String amacdOption;
	@JsonProperty("a_trigger_feasibility")
	private String atriggerFeasibility;
	@JsonProperty("a_macd_service")
	private String amacdService;
	@JsonProperty("a_service_id")
	private String aserviceId;
	@JsonProperty("a_access_provider")
	private String aaccessProvider;
	@JsonProperty("a_lat_long")
	private String alatLong;
	@JsonProperty("a_zip_code")
	private String azipCode;
	@JsonProperty("a_cu_le_id")
	private String acuLeId;
	@JsonProperty("a_service_commissioned_date")
	private String aserviceCommissionedDate;
	@JsonProperty("a_old_Ll_Bw")
	private String aoldLlBw;
	@JsonProperty("a_old_contract_term")
	private String aoldContractTerm;
	@JsonProperty("a_prd_category")
	private String aprdCategory;
	@JsonProperty("a_address")
	private String aaddress;
	@JsonProperty("a_bandwidth")
	private String abandwidth;
	@JsonProperty("a_ll_change")
	private String allChange;
	
	@JsonProperty("b_macd_option")
	private String bmacdOption;
	@JsonProperty("b_trigger_feasibility")
	private String btriggerFeasibility;
	@JsonProperty("b_macd_service")
	private String bmacdService;
	@JsonProperty("b_service_id")
	private String bserviceId;
	@JsonProperty("b_access_provider")
	private String baccessProvider;
	@JsonProperty("b_lat_long")
	private String blatLong;
	@JsonProperty("b_zip_code")
	private String bzipCode;
	@JsonProperty("b_cu_le_id")
	private String bcuLeId;
	@JsonProperty("b_service_commissioned_date")
	private String bserviceCommissionedDate;
	@JsonProperty("b_old_Ll_Bw")
	private String boldLlBw;
	@JsonProperty("b_old_contract_term")
	private String boldContractTerm;
	@JsonProperty("b_prd_category")
	private String bprdCategory;
	@JsonProperty("b_address")
	private String baddress;
	@JsonProperty("b_bandwidth")
	private String bbandwidth;
	@JsonProperty("b_ll_change")
	private String bllChange;
	@JsonProperty("resp_state")
	private String respState;

    @JsonProperty("partner_account_id_with_18_digit")
    private String partnerAccountIdWith18Digit;
    @JsonProperty("partner_profile")
    private String partnerProfile;
    @JsonProperty("quotetype_partner")
    private String quoteTypePartner;
    @JsonProperty("solution_type")
    private String solutionType;

	
	@JsonProperty("old_Ll_Bw_ref")
	private String oldLlBwRef;
	
	public String getRespState() {
		return respState;
	}
	public void setRespState(String respState) {
		this.respState = respState;
	}

//	CST-35 customer portal
	@JsonProperty("is_customer")
	private String isCustomer;
	
	
	@JsonProperty("isHub")
	private String isHub;
	
	@JsonProperty("feasibility_remarks")
    private String feasibilityRemarks;
	
	
	//MF ATTRIBUTES
	
	
		@JsonProperty("a_otc_modem_charges")
		private String aOtcModemCharges;
		
		@JsonProperty("a_lm_nrc_bw_prov_ofwl")
		private String aLmNrcBwProvOfwl;
		
		@JsonProperty("a_arc_modem_charges")
		private String aArcModemCharges;
		
		@JsonProperty("a_arc_bw")
		private String aArcBw;
		
		@JsonProperty("a_lm_nrc_prow_onwl")
		private String aLmNrcProwOnwl;
		
		@JsonProperty("a_lm_arc_prow_onwl")
		private String aLmArcProwOnwl;
		
		@JsonProperty("a_lm_arc_converter_charges_onrf")
		private String aLmArcConverterChargesOnrf;
			
		@JsonProperty("a_lm_arc_bw_backhaul_onrf")
		private String aLmArcBwBackhaulOnrf;
		
		@JsonProperty("a_lm_arc_colocation_onrf")
		private String aLmArcColocationOnrf;
		
		@JsonProperty("a_lm_otc_modem_charges_offwl")
		private String aLmOtcModemChargesOffwl;
		
		@JsonProperty("a_lm_otc_nrc_installation_offwl")
		private String aLmOtcNrcInstallationOffwl;
		
		@JsonProperty("a_lm_arc_modem_charges_offwl")
		private String aLmArcModemChargesOffwl;
		
		@JsonProperty("a_lm_arc_bw_offwl")
		private String aLmArcBwOffwl;
		
		@JsonProperty("a_provider_name")
		private String aProviderName;
		
		@JsonProperty("a_BHConnectivity")
		private String aBHConnectivity;
		
		@JsonProperty("a_lm_arc_orderable_bw_onwl")
		private String aLmArcOrderableBwOnwl;
		
		@JsonProperty("a_lm_otc_nrc_orderable_bw_onwl")
		private String aLmOtcNrcOrderableBwOnwl;
		
		@JsonProperty("a_lm_nrc_network_capex_onwl")
		private String aLmNrcNetworkCapexOnwl;
		
		@JsonProperty("a_lm_arc_radwin_bw_onrf")
		private String aLmArcRadwinBwOnrf;	
		
		
		@JsonProperty("b_otc_modem_charges")
		private String bOtcModemCharges;
		
		@JsonProperty("b_lm_nrc_bw_prov_ofwl")
		private String bLmNrcBwProvOfwl;
		
		@JsonProperty("b_arc_modem_charges")
		private String bArcModemCharges;
		
		@JsonProperty("b_arc_bw")
		private String bArcBw;
		
		@JsonProperty("b_lm_nrc_prow_onwl")
		private String bLmNrcProwOnwl;
		
		@JsonProperty("b_lm_arc_prow_onwl")
		private String bLmArcProwOnwl;
		
		@JsonProperty("b_lm_arc_converter_charges_onrf")
		private String bLmArcConverterChargesOnrf;
			
		@JsonProperty("b_lm_arc_bw_backhaul_onrf")
		private String bLmArcBwBackhaulOnrf;
		
		@JsonProperty("b_lm_arc_colocation_onrf")
		private String bLmArcColocationOnrf;
		
		@JsonProperty("b_lm_otc_modem_charges_offwl")
		private String bLmOtcModemChargesOffwl;
		
		@JsonProperty("b_lm_otc_nrc_installation_offwl")
		private String bLmOtcNrcInstallationOffwl;
		
		@JsonProperty("b_lm_arc_modem_charges_offwl")
		private String bLmArcModemChargesOffwl;
		
		@JsonProperty("b_lm_arc_bw_offwl")
		private String bLmArcBwOffwl;
		
		@JsonProperty("b_provider_name")
		private String bProviderName;
		
		@JsonProperty("b_BHConnectivity")
		private String bBHConnectivity;
		
		@JsonProperty("b_lm_arc_orderable_bw_onwl")
		private String bLmArcOrderableBwOnwl;
		
		@JsonProperty("b_lm_otc_nrc_orderable_bw_onwl")
		private String bLmOtcNrcOrderableBwOnwl;
		
		@JsonProperty("b_lm_nrc_network_capex_onwl")
		private String bLmNrcNetworkCapexOnwl;
		
		@JsonProperty("b_lm_arc_radwin_bw_onrf")
		private String bLmArcRadwinBwOnrf;
		
		
		@JsonProperty("a_parallel_run_days")
		private String aParallelRunDays;
		
		@JsonProperty("b_parallel_run_days")
		private String bParallelRunDays;
		
		@JsonProperty("a_local_loop_bw")
		private String aLocalLoopBw;
		
		@JsonProperty("b_local_loop_bw")
		private String bLocalLoopBw;
		
		
		@JsonProperty("a_error_msg_display")
		private String aErrorMsgDisplay;
		
		@JsonProperty("b_error_msg_display")
		private String bErrorMsgDisplay;
		
		
		@JsonProperty("a_error_msg_display")
		public String getAErrorMsgDisplay() {
		return aErrorMsgDisplay;
		}

		@JsonProperty("a_error_msg_display")
		public void setAErrorMsgDisplay(String aErrorMsgDisplay) {
		this.aErrorMsgDisplay = aErrorMsgDisplay;
		}

		@JsonProperty("b_error_msg_display")
		public String getbErrorMsgDisplay() {
			return bErrorMsgDisplay;
		}
		
		@JsonProperty("b_error_msg_display")
		public void setbErrorMsgDisplay(String bErrorMsgDisplay) {
			this.bErrorMsgDisplay = bErrorMsgDisplay;
		}
		
		
		
		
		@JsonProperty("a_local_loop_bw")
		public String getaLocalLoopBw() {
			return aLocalLoopBw;
		}
		
		@JsonProperty("a_local_loop_bw")
		public void setaLocalLoopBw(String aLocalLoopBw) {
			this.aLocalLoopBw = aLocalLoopBw;
		}
		
		@JsonProperty("b_local_loop_bw")
		public String getbLocalLoopBw() {
			return bLocalLoopBw;
		}
		
		@JsonProperty("b_local_loop_bw")
		public void setbLocalLoopBw(String bLocalLoopBw) {
			this.bLocalLoopBw = bLocalLoopBw;
		}
		
		
		@JsonProperty("a_parallel_run_days")
		public String getaParallelRunDays() {
			return aParallelRunDays;
		}
		
		@JsonProperty("a_parallel_run_days")
		public void setaParallelRunDays(String aParallelRunDays) {
			this.aParallelRunDays = aParallelRunDays;
		}
		
		@JsonProperty("b_parallel_run_days")
		public String getbParallelRunDays() {
			return bParallelRunDays;
		}
		
		@JsonProperty("b_parallel_run_days")
		public void setbParallelRunDays(String bParallelRunDays) {
			this.bParallelRunDays = bParallelRunDays;
		}
	
	
		// added for PIPF -152
		@JsonProperty("task_id")
		private String taskId;

		@JsonProperty("feasibility_response_id")
		private String feasibilityResponseId;
		
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
    
	@JsonProperty("isHub")
    public String getIsHub() {
		return isHub;
	}
	@JsonProperty("isHub")
	public void setIsHub(String isHub) {
		this.isHub = isHub;
	}
	
	
	@JsonProperty("b_macd_option")
	public String getBmacdOption() {
		return bmacdOption;
	}
	@JsonProperty("b_macd_option")
	public void setBmacdOption(String bmacdOption) {
		this.bmacdOption = bmacdOption;
	}
	
	@JsonProperty("b_trigger_feasibility")
	public String getBtriggerFeasibility() {
		return btriggerFeasibility;
	}
	@JsonProperty("b_trigger_feasibility")
	public void setBtriggerFeasibility(String btriggerFeasibility) {
		this.btriggerFeasibility = btriggerFeasibility;
	}
	
	@JsonProperty("b_macd_service")
	public String getBmacdService() {
		return bmacdService;
	}
	@JsonProperty("b_macd_service")
	public void setBmacdService(String bmacdService) {
		this.bmacdService = bmacdService;
	}
	
	@JsonProperty("b_service_id")
	public String getBserviceId() {
		return bserviceId;
	}
	@JsonProperty("b_service_id")
	public void setBserviceId(String bserviceId) {
		this.bserviceId = bserviceId;
	}
	
	@JsonProperty("b_access_provider")
	public String getBaccessProvider() {
		return baccessProvider;
	}
	@JsonProperty("b_access_provider")
	public void setBaccessProvider(String baccessProvider) {
		this.baccessProvider = baccessProvider;
	}
	
	@JsonProperty("b_lat_long")
	public String getBlatLong() {
		return blatLong;
	}
	@JsonProperty("b_lat_long")
	public void setBlatLong(String blatLong) {
		this.blatLong = blatLong;
	}
	
	@JsonProperty("b_zip_code")
	public String getBzipCode() {
		return bzipCode;
	}
	@JsonProperty("b_zip_code")
	public void setBzipCode(String bzipCode) {
		this.bzipCode = bzipCode;
	}
	
	@JsonProperty("b_cu_le_id")
	public String getBcuLeId() {
		return bcuLeId;
	}
	@JsonProperty("b_cu_le_id")
	public void setBcuLeId(String bcuLeId) {
		this.bcuLeId = bcuLeId;
	}
	
	@JsonProperty("b_service_commissioned_date")
	public String getBserviceCommissionedDate() {
		return bserviceCommissionedDate;
	}
	@JsonProperty("b_service_commissioned_date")
	public void setBserviceCommissionedDate(String bserviceCommissionedDate) {
		this.bserviceCommissionedDate = bserviceCommissionedDate;
	}
	
	@JsonProperty("b_old_Ll_Bw")
	public String getBoldLlBw() {
		return boldLlBw;
	}
	@JsonProperty("b_old_Ll_Bw")
	public void setBoldLlBw(String boldLlBw) {
		this.boldLlBw = boldLlBw;
	}
	
	@JsonProperty("b_old_contract_term")
	public String getBoldContractTerm() {
		return boldContractTerm;
	}
	@JsonProperty("b_old_contract_term")
	public void setBoldContractTerm(String boldContractTerm) {
		this.boldContractTerm = boldContractTerm;
	}
	
	@JsonProperty("b_prd_category")
	public String getBprdCategory() {
		return bprdCategory;
	}
	@JsonProperty("b_prd_category")
	public void setBprdCategory(String bprdCategory) {
		this.bprdCategory = bprdCategory;
	}
	
	@JsonProperty("b_address")
	public String getBaddress() {
		return baddress;
	}
	@JsonProperty("b_address")
	public void setBaddress(String baddress) {
		this.baddress = baddress;
	}
	
	@JsonProperty("b_bandwidth")
	public String getBbandwidth() {
		return bbandwidth;
	}
	@JsonProperty("b_bandwidth")
	public void setBbandwidth(String bbandwidth) {
		this.bbandwidth = bbandwidth;
	}
	
	@JsonProperty("b_ll_change")
	public String getBllChange() {
		return bllChange;
	}
	@JsonProperty("b_ll_change")
	public void setBllChange(String bllChange) {
		this.bllChange = bllChange;
	}
	
	@JsonProperty("a_macd_option")
	public String getAmacdOption() {
		return amacdOption;
	}
	 @JsonProperty("a_macd_option")
	public void setAmacdOption(String amacdOption) {
		this.amacdOption = amacdOption;
	}
	
	@JsonProperty("a_trigger_feasibility")
	public String getAtriggerFeasibility() {
		return atriggerFeasibility;
	}
	@JsonProperty("a_trigger_feasibility")
	public void setAtriggerFeasibility(String atriggerFeasibility) {
		this.atriggerFeasibility = atriggerFeasibility;
	}
	
	@JsonProperty("a_macd_service")
	public String getAmacdService() {
		return amacdService;
	}
	@JsonProperty("a_macd_service")
	public void setAmacdService(String amacdService) {
		this.amacdService = amacdService;
	}
	
	@JsonProperty("a_service_id")
	public String getAserviceId() {
		return aserviceId;
	}
	@JsonProperty("a_service_id")
	public void setAserviceId(String aserviceId) {
		this.aserviceId = aserviceId;
	}

	@JsonProperty("a_access_provider")
	public String getAaccessProvider() {
		return aaccessProvider;
	}
	@JsonProperty("a_access_provider")
	public void setAaccessProvider(String aaccessProvider) {
		this.aaccessProvider = aaccessProvider;
	}

	@JsonProperty("a_lat_long")
	public String getAlatLong() {
		return alatLong;
	}
	@JsonProperty("a_lat_long")
	public void setAlatLong(String alatLong) {
		this.alatLong = alatLong;
	}

	@JsonProperty("a_zip_code")
	public String getAzipCode() {
		return azipCode;
	}
	@JsonProperty("a_zip_code")
	public void setAzipCode(String azipCode) {
		this.azipCode = azipCode;
	}

	@JsonProperty("a_cu_le_id")
	public String getAcuLeId() {
		return acuLeId;
	}
	@JsonProperty("a_cu_le_id")
	public void setAcuLeId(String acuLeId) {
		this.acuLeId = acuLeId;
	}

	@JsonProperty("a_service_commissioned_date")
	public String getAserviceCommissionedDate() {
		return aserviceCommissionedDate;
	}
	@JsonProperty("a_service_commissioned_date")
	public void setAserviceCommissionedDate(String aserviceCommissionedDate) {
		this.aserviceCommissionedDate = aserviceCommissionedDate;
	}

	@JsonProperty("a_old_Ll_Bw")
	public String getAoldLlBw() {
		return aoldLlBw;
	}
	@JsonProperty("a_old_Ll_Bw")
	public void setAoldLlBw(String aoldLlBw) {
		this.aoldLlBw = aoldLlBw;
	}

	@JsonProperty("a_old_contract_term")
	public String getAoldContractTerm() {
		return aoldContractTerm;
	}
	
	@JsonProperty("a_old_contract_term")
	public void setAoldContractTerm(String aoldContractTerm) {
		this.aoldContractTerm = aoldContractTerm;
	}
	@JsonProperty("a_prd_category")
	public String getAprdCategory() {
		return aprdCategory;
	}
	@JsonProperty("a_prd_category")
	public void setAprdCategory(String aprdCategory) {
		this.aprdCategory = aprdCategory;
	}
	
	@JsonProperty("a_address")
	public String getAaddress() {
		return aaddress;
	}
	@JsonProperty("a_address")
	public void setAaddress(String aaddress) {
		this.aaddress = aaddress;
	}

	@JsonProperty("a_bandwidth")
	public String getAbandwidth() {
		return abandwidth;
	}
	@JsonProperty("a_bandwidth")
	public void setAbandwidth(String abandwidth) {
		this.abandwidth = abandwidth;
	}

	@JsonProperty("a_ll_change")
	public String getAllChange() {
		return allChange;
	}
	@JsonProperty("a_ll_change")
	public void setAllChange(String allChange) {
		this.allChange = allChange;
	}
	
	@JsonProperty("error_msg_display")
	public String getErrorMsgDisplay() {
		return errorMsgDisplay;
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


    
    public Integer getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}

	@JsonProperty("a_X2km_prospect_min_bw")
    public String getAX2kmProspectMinBw() {
        return aX2kmProspectMinBw;
    }

    @JsonProperty("a_X2km_prospect_min_bw")
    public void setAX2kmProspectMinBw(String aX2kmProspectMinBw) {
        this.aX2kmProspectMinBw = aX2kmProspectMinBw;
    }

    @JsonProperty("b_POP_DIST_KM_SERVICE_MOD")
    public String getBPOPDISTKMSERVICEMOD() {
        return bPOPDISTKMSERVICEMOD;
    }

    @JsonProperty("b_POP_DIST_KM_SERVICE_MOD")
    public void setBPOPDISTKMSERVICEMOD(String bPOPDISTKMSERVICEMOD) {
        this.bPOPDISTKMSERVICEMOD = bPOPDISTKMSERVICEMOD;
    }

    @JsonProperty("intra_inter_flag")
    public String getIntraInterFlag() {
        return intraInterFlag;
    }

    @JsonProperty("intra_inter_flag")
    public void setIntraInterFlag(String intraInterFlag) {
        this.intraInterFlag = intraInterFlag;
    }

    @JsonProperty("a_X2km_min_bw")
    public String getAX2kmMinBw() {
        return aX2kmMinBw;
    }

    @JsonProperty("a_X2km_min_bw")
    public void setAX2kmMinBw(String aX2kmMinBw) {
        this.aX2kmMinBw = aX2kmMinBw;
    }

    @JsonProperty("b_lm_arc_bw_prov_ofrf")
    public String getBLmArcBwProvOfrf() {
        return bLmArcBwProvOfrf;
    }

    @JsonProperty("b_lm_arc_bw_prov_ofrf")
    public void setBLmArcBwProvOfrf(String bLmArcBwProvOfrf) {
        this.bLmArcBwProvOfrf = bLmArcBwProvOfrf;
    }

    @JsonProperty("b_num_connected_cust")
    public String getBNumConnectedCust() {
        return bNumConnectedCust;
    }

    @JsonProperty("b_num_connected_cust")
    public void setBNumConnectedCust(String bNumConnectedCust) {
        this.bNumConnectedCust = bNumConnectedCust;
    }

    @JsonProperty("a_HH_0_5_access_rings_NF")
    public String getAHH05AccessRingsNF() {
        return aHH05AccessRingsNF;
    }

    @JsonProperty("a_HH_0_5_access_rings_NF")
    public void setAHH05AccessRingsNF(String aHH05AccessRingsNF) {
        this.aHH05AccessRingsNF = aHH05AccessRingsNF;
    }

    @JsonProperty("b_X2km_avg_dist")
    public String getBX2kmAvgDist() {
        return bX2kmAvgDist;
    }

    @JsonProperty("b_X2km_avg_dist")
    public void setBX2kmAvgDist(String bX2kmAvgDist) {
        this.bX2kmAvgDist = bX2kmAvgDist;
    }

    @JsonProperty("b_lm_nrc_bw_onwl")
    public String getBLmNrcBwOnwl() {
        return bLmNrcBwOnwl;
    }

    @JsonProperty("b_lm_nrc_bw_onwl")
    public void setBLmNrcBwOnwl(String bLmNrcBwOnwl) {
        this.bLmNrcBwOnwl = bLmNrcBwOnwl;
    }

    @JsonProperty("b_X2km_max_bw")
    public String getBX2kmMaxBw() {
        return bX2kmMaxBw;
    }

    @JsonProperty("b_X2km_max_bw")
    public void setBX2kmMaxBw(String bX2kmMaxBw) {
        this.bX2kmMaxBw = bX2kmMaxBw;
    }

    @JsonProperty("a_X2km_max_bw")
    public String getAX2kmMaxBw() {
        return aX2kmMaxBw;
    }

    @JsonProperty("a_X2km_max_bw")
    public void setAX2kmMaxBw(String aX2kmMaxBw) {
        this.aX2kmMaxBw = aX2kmMaxBw;
    }

    @JsonProperty("a_X2km_prospect_max_bw")
    public String getAX2kmProspectMaxBw() {
        return aX2kmProspectMaxBw;
    }

    @JsonProperty("a_X2km_prospect_max_bw")
    public void setAX2kmProspectMaxBw(String aX2kmProspectMaxBw) {
        this.aX2kmProspectMaxBw = aX2kmProspectMaxBw;
    }

    @JsonProperty("b_X2km_min_bw")
    public String getBX2kmMinBw() {
        return bX2kmMinBw;
    }

    @JsonProperty("b_X2km_min_bw")
    public void setBX2kmMinBw(String bX2kmMinBw) {
        this.bX2kmMinBw = bX2kmMinBw;
    }

    @JsonProperty("a_longitude_final")
    public String getALongitudeFinal() {
        return aLongitudeFinal;
    }

    @JsonProperty("a_longitude_final")
    public void setALongitudeFinal(String aLongitudeFinal) {
        this.aLongitudeFinal = aLongitudeFinal;
    }

    @JsonProperty("b_access_check_CC")
    public String getBAccessCheckCC() {
        return bAccessCheckCC;
    }

    @JsonProperty("b_access_check_CC")
    public void setBAccessCheckCC(String bAccessCheckCC) {
        this.bAccessCheckCC = bAccessCheckCC;
    }

    @JsonProperty("a_num_connected_building")
    public String getANumConnectedBuilding() {
        return aNumConnectedBuilding;
    }

    @JsonProperty("a_num_connected_building")
    public void setANumConnectedBuilding(String aNumConnectedBuilding) {
        this.aNumConnectedBuilding = aNumConnectedBuilding;
    }

    @JsonProperty("b_Product.Name")
    public String getBProductName() {
        return bProductName;
    }

    @JsonProperty("b_Product.Name")
    public void setBProductName(String bProductName) {
        this.bProductName = bProductName;
    }

    @JsonProperty("a_city_tier")
    public String getACityTier() {
        return aCityTier;
    }

    @JsonProperty("a_city_tier")
    public void setACityTier(String aCityTier) {
        this.aCityTier = aCityTier;
    }

    @JsonProperty("b_X5km_prospect_count")
    public String getBX5kmProspectCount() {
        return bX5kmProspectCount;
    }

    @JsonProperty("b_X5km_prospect_count")
    public void setBX5kmProspectCount(String bX5kmProspectCount) {
        this.bX5kmProspectCount = bX5kmProspectCount;
    }

    @JsonProperty("a_sales_org")
    public String getASalesOrg() {
        return aSalesOrg;
    }

    @JsonProperty("a_sales_org")
    public void setASalesOrg(String aSalesOrg) {
        this.aSalesOrg = aSalesOrg;
    }

    @JsonProperty("b_scenario_1")
    public String getBScenario1() {
        return bScenario1;
    }

    @JsonProperty("b_scenario_1")
    public void setBScenario1(String bScenario1) {
        this.bScenario1 = bScenario1;
    }

    @JsonProperty("a_FATG_Ring_type")
    public String getAFATGRingType() {
        return aFATGRingType;
    }

    @JsonProperty("a_FATG_Ring_type")
    public void setAFATGRingType(String aFATGRingType) {
        this.aFATGRingType = aFATGRingType;
    }

    @JsonProperty("a_pop_lat")
    public String getAPopLat() {
        return aPopLat;
    }

    @JsonProperty("a_pop_lat")
    public void setAPopLat(String aPopLat) {
        this.aPopLat = aPopLat;
    }

    @JsonProperty("a_resp_city")
    public String getARespCity() {
        return aRespCity;
    }

    @JsonProperty("a_resp_city")
    public void setARespCity(String aRespCity) {
        this.aRespCity = aRespCity;
    }

    @JsonProperty("b_X2km_prospect_min_bw")
    public String getBX2kmProspectMinBw() {
        return bX2kmProspectMinBw;
    }

    @JsonProperty("b_X2km_prospect_min_bw")
    public void setBX2kmProspectMinBw(String bX2kmProspectMinBw) {
        this.bX2kmProspectMinBw = bX2kmProspectMinBw;
    }

    @JsonProperty("a_HH_0_5_access_rings_F")
    public String getAHH05AccessRingsF() {
        return aHH05AccessRingsF;
    }

    @JsonProperty("a_HH_0_5_access_rings_F")
    public void setAHH05AccessRingsF(String aHH05AccessRingsF) {
        this.aHH05AccessRingsF = aHH05AccessRingsF;
    }

    @JsonProperty("site_id")
    public String getSiteId() {
        return siteId;
    }

    @JsonProperty("site_id")
    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    @JsonProperty("b_FATG_Ring_type")
    public String getBFATGRingType() {
        return bFATGRingType;
    }

    @JsonProperty("b_FATG_Ring_type")
    public void setBFATGRingType(String bFATGRingType) {
        this.bFATGRingType = bFATGRingType;
    }

    @JsonProperty("a_X5km_prospect_min_bw")
    public String getAX5kmProspectMinBw() {
        return aX5kmProspectMinBw;
    }

    @JsonProperty("a_X5km_prospect_min_bw")
    public void setAX5kmProspectMinBw(String aX5kmProspectMinBw) {
        this.aX5kmProspectMinBw = aX5kmProspectMinBw;
    }

    @JsonProperty("a_POP_Construction_Status")
    public String getAPOPConstructionStatus() {
        return aPOPConstructionStatus;
    }

    @JsonProperty("a_POP_Construction_Status")
    public void setAPOPConstructionStatus(String aPOPConstructionStatus) {
        this.aPOPConstructionStatus = aPOPConstructionStatus;
    }

    @JsonProperty("b_city_tier")
    public String getBCityTier() {
        return bCityTier;
    }

    @JsonProperty("b_city_tier")
    public void setBCityTier(String bCityTier) {
        this.bCityTier = bCityTier;
    }

    @JsonProperty("b_time_taken")
    public String getBTimeTaken() {
        return bTimeTaken;
    }

    @JsonProperty("b_time_taken")
    public void setBTimeTaken(String bTimeTaken) {
        this.bTimeTaken = bTimeTaken;
    }

    @JsonProperty("b_lm_nrc_mux_onwl")
    public String getBLmNrcMuxOnwl() {
        return bLmNrcMuxOnwl;
    }

    @JsonProperty("b_lm_nrc_mux_onwl")
    public void setBLmNrcMuxOnwl(String bLmNrcMuxOnwl) {
        this.bLmNrcMuxOnwl = bLmNrcMuxOnwl;
    }

    @JsonProperty("b_sales_org")
    public String getBSalesOrg() {
        return bSalesOrg;
    }

    @JsonProperty("b_sales_org")
    public void setBSalesOrg(String bSalesOrg) {
        this.bSalesOrg = bSalesOrg;
    }

    @JsonProperty("b_FATG_Category")
    public String getBFATGCategory() {
        return bFATGCategory;
    }

    @JsonProperty("b_FATG_Category")
    public void setBFATGCategory(String bFATGCategory) {
        this.bFATGCategory = bFATGCategory;
    }

    @JsonProperty("b_scenario_2")
    public String getBScenario2() {
        return bScenario2;
    }

    @JsonProperty("b_scenario_2")
    public void setBScenario2(String bScenario2) {
        this.bScenario2 = bScenario2;
    }

    @JsonProperty("b_Network_F_NF_HH")
    public String getBNetworkFNFHH() {
        return bNetworkFNFHH;
    }

    @JsonProperty("b_Network_F_NF_HH")
    public void setBNetworkFNFHH(String bNetworkFNFHH) {
        this.bNetworkFNFHH = bNetworkFNFHH;
    }

    @JsonProperty("Selected")
    public Boolean getSelected() {
        return selected;
    }

    @JsonProperty("Selected")
    public void setSelected(Boolean selected) {
        this.selected = selected;
    }

    @JsonProperty("a_hh_name")
    public String getAHhName() {
        return aHhName;
    }

    @JsonProperty("a_hh_name")
    public void setAHhName(String aHhName) {
        this.aHhName = aHhName;
    }

    @JsonProperty("b_lm_nrc_bw_prov_ofrf")
    public String getBLmNrcBwProvOfrf() {
        return bLmNrcBwProvOfrf;
    }

    @JsonProperty("b_lm_nrc_bw_prov_ofrf")
    public void setBLmNrcBwProvOfrf(String bLmNrcBwProvOfrf) {
        this.bLmNrcBwProvOfrf = bLmNrcBwProvOfrf;
    }

    @JsonProperty("b_latitude_final")
    public String getBLatitudeFinal() {
        return bLatitudeFinal;
    }

    @JsonProperty("b_latitude_final")
    public void setBLatitudeFinal(String bLatitudeFinal) {
        this.bLatitudeFinal = bLatitudeFinal;
    }

    @JsonProperty("b_connected_cust_tag")
    public String getBConnectedCustTag() {
        return bConnectedCustTag;
    }

    @JsonProperty("b_connected_cust_tag")
    public void setBConnectedCustTag(String bConnectedCustTag) {
        this.bConnectedCustTag = bConnectedCustTag;
    }

    @JsonProperty("a_POP_Building_Type")
    public String getAPOPBuildingType() {
        return aPOPBuildingType;
    }

    @JsonProperty("a_POP_Building_Type")
    public void setAPOPBuildingType(String aPOPBuildingType) {
        this.aPOPBuildingType = aPOPBuildingType;
    }

    @JsonProperty("a_X0.5km_prospect_avg_bw")
    public String getAX05kmProspectAvgBw() {
        return aX05kmProspectAvgBw;
    }

    @JsonProperty("a_X0.5km_prospect_avg_bw")
    public void setAX05kmProspectAvgBw(String aX05kmProspectAvgBw) {
        this.aX05kmProspectAvgBw = aX05kmProspectAvgBw;
    }

    @JsonProperty("a_lm_arc_bw_onwl")
    public String getALmArcBwOnwl() {
        return aLmArcBwOnwl;
    }

    @JsonProperty("a_lm_arc_bw_onwl")
    public void setALmArcBwOnwl(String aLmArcBwOnwl) {
        this.aLmArcBwOnwl = aLmArcBwOnwl;
    }

    @JsonProperty("b_X2km_prospect_max_bw")
    public String getBX2kmProspectMaxBw() {
        return bX2kmProspectMaxBw;
    }

    @JsonProperty("b_X2km_prospect_max_bw")
    public void setBX2kmProspectMaxBw(String bX2kmProspectMaxBw) {
        this.bX2kmProspectMaxBw = bX2kmProspectMaxBw;
    }

    @JsonProperty("b_X5km_prospect_min_bw")
    public String getBX5kmProspectMinBw() {
        return bX5kmProspectMinBw;
    }

    @JsonProperty("b_X5km_prospect_min_bw")
    public void setBX5kmProspectMinBw(String bX5kmProspectMinBw) {
        this.bX5kmProspectMinBw = bX5kmProspectMinBw;
    }

    @JsonProperty("b_X0.5km_prospect_count")
    public String getBX05kmProspectCount() {
        return bX05kmProspectCount;
    }

    @JsonProperty("b_X0.5km_prospect_count")
    public void setBX05kmProspectCount(String bX05kmProspectCount) {
        this.bX05kmProspectCount = bX05kmProspectCount;
    }

    @JsonProperty("b_OnnetCity_tag")
    public String getBOnnetCityTag() {
        return bOnnetCityTag;
    }

    @JsonProperty("b_OnnetCity_tag")
    public void setBOnnetCityTag(String bOnnetCityTag) {
        this.bOnnetCityTag = bOnnetCityTag;
    }

    @JsonProperty("a_pop_network_loc_id")
    public String getAPopNetworkLocId() {
        return aPopNetworkLocId;
    }

    @JsonProperty("a_pop_network_loc_id")
    public void setAPopNetworkLocId(String aPopNetworkLocId) {
        this.aPopNetworkLocId = aPopNetworkLocId;
    }

    @JsonProperty("a_X0.5km_avg_bw")
    public String getAX05kmAvgBw() {
        return aX05kmAvgBw;
    }

    @JsonProperty("a_X0.5km_avg_bw")
    public void setAX05kmAvgBw(String aX05kmAvgBw) {
        this.aX05kmAvgBw = aX05kmAvgBw;
    }

    @JsonProperty("a_X2km_prospect_avg_dist")
    public String getAX2kmProspectAvgDist() {
        return aX2kmProspectAvgDist;
    }

    @JsonProperty("a_X2km_prospect_avg_dist")
    public void setAX2kmProspectAvgDist(String aX2kmProspectAvgDist) {
        this.aX2kmProspectAvgDist = aX2kmProspectAvgDist;
    }

    @JsonProperty("pop2pop_network_check")
    public String getPop2popNetworkCheck() {
        return pop2popNetworkCheck;
    }

    @JsonProperty("pop2pop_network_check")
    public void setPop2popNetworkCheck(String pop2popNetworkCheck) {
        this.pop2popNetworkCheck = pop2popNetworkCheck;
    }

    @JsonProperty("a_POP_Category")
    public String getAPOPCategory() {
        return aPOPCategory;
    }

    @JsonProperty("a_POP_Category")
    public void setAPOPCategory(String aPOPCategory) {
        this.aPOPCategory = aPOPCategory;
    }

    @JsonProperty("b_POP_DIST_KM_SERVICE")
    public String getBPOPDISTKMSERVICE() {
        return bPOPDISTKMSERVICE;
    }

    @JsonProperty("b_POP_DIST_KM_SERVICE")
    public void setBPOPDISTKMSERVICE(String bPOPDISTKMSERVICE) {
        this.bPOPDISTKMSERVICE = bPOPDISTKMSERVICE;
    }

    @JsonProperty("a_X5km_prospect_avg_dist")
    public String getAX5kmProspectAvgDist() {
        return aX5kmProspectAvgDist;
    }

    @JsonProperty("a_X5km_prospect_avg_dist")
    public void setAX5kmProspectAvgDist(String aX5kmProspectAvgDist) {
        this.aX5kmProspectAvgDist = aX5kmProspectAvgDist;
    }

    @JsonProperty("b_pop_long")
    public String getBPopLong() {
        return bPopLong;
    }

    @JsonProperty("b_pop_long")
    public void setBPopLong(String bPopLong) {
        this.bPopLong = bPopLong;
    }

    @JsonProperty("a_Predicted_Access_Feasibility")
    public String getAPredictedAccessFeasibility() {
        return aPredictedAccessFeasibility;
    }

    @JsonProperty("a_Predicted_Access_Feasibility")
    public void setAPredictedAccessFeasibility(String aPredictedAccessFeasibility) {
        this.aPredictedAccessFeasibility = aPredictedAccessFeasibility;
    }

    @JsonProperty("a_POP_Network_Location_Type")
    public String getAPOPNetworkLocationType() {
        return aPOPNetworkLocationType;
    }

    @JsonProperty("a_POP_Network_Location_Type")
    public void setAPOPNetworkLocationType(String aPOPNetworkLocationType) {
        this.aPOPNetworkLocationType = aPOPNetworkLocationType;
    }

    @JsonProperty("a_scenario_1")
    public String getAScenario1() {
        return aScenario1;
    }

    @JsonProperty("a_scenario_1")
    public void setAScenario1(String aScenario1) {
        this.aScenario1 = aScenario1;
    }

    @JsonProperty("a_scenario_2")
    public String getAScenario2() {
        return aScenario2;
    }

    @JsonProperty("a_scenario_2")
    public void setAScenario2(String aScenario2) {
        this.aScenario2 = aScenario2;
    }

    @JsonProperty("b_POP_Building_Type")
    public String getBPOPBuildingType() {
        return bPOPBuildingType;
    }

    @JsonProperty("b_POP_Building_Type")
    public void setBPOPBuildingType(String bPOPBuildingType) {
        this.bPOPBuildingType = bPOPBuildingType;
    }

    @JsonProperty("b_X2km_prospect_count")
    public String getBX2kmProspectCount() {
        return bX2kmProspectCount;
    }

    @JsonProperty("b_X2km_prospect_count")
    public void setBX2kmProspectCount(String bX2kmProspectCount) {
        this.bX2kmProspectCount = bX2kmProspectCount;
    }

    @JsonProperty("a_FATG_DIST_KM")
    public String getAFATGDISTKM() {
        return aFATGDISTKM;
    }

    @JsonProperty("a_FATG_DIST_KM")
    public void setAFATGDISTKM(String aFATGDISTKM) {
        this.aFATGDISTKM = aFATGDISTKM;
    }

    @JsonProperty("b_net_pre_feasible_flag")
    public String getBNetPreFeasibleFlag() {
        return bNetPreFeasibleFlag;
    }

    @JsonProperty("b_net_pre_feasible_flag")
    public void setBNetPreFeasibleFlag(String bNetPreFeasibleFlag) {
        this.bNetPreFeasibleFlag = bNetPreFeasibleFlag;
    }

    @JsonProperty("a_X5km_min_bw")
    public String getAX5kmMinBw() {
        return aX5kmMinBw;
    }

    @JsonProperty("a_X5km_min_bw")
    public void setAX5kmMinBw(String aX5kmMinBw) {
        this.aX5kmMinBw = aX5kmMinBw;
    }

    @JsonProperty("a_X0.5km_prospect_max_bw")
    public String getAX05kmProspectMaxBw() {
        return aX05kmProspectMaxBw;
    }

    @JsonProperty("a_X0.5km_prospect_max_bw")
    public void setAX05kmProspectMaxBw(String aX05kmProspectMaxBw) {
        this.aX05kmProspectMaxBw = aX05kmProspectMaxBw;
    }

    @JsonProperty("a_X2km_prospect_perc_feasible")
    public String getAX2kmProspectPercFeasible() {
        return aX2kmProspectPercFeasible;
    }

    @JsonProperty("a_X2km_prospect_perc_feasible")
    public void setAX2kmProspectPercFeasible(String aX2kmProspectPercFeasible) {
        this.aX2kmProspectPercFeasible = aX2kmProspectPercFeasible;
    }

    @JsonProperty("b_X5km_prospect_max_bw")
    public String getBX5kmProspectMaxBw() {
        return bX5kmProspectMaxBw;
    }

    @JsonProperty("b_X5km_prospect_max_bw")
    public void setBX5kmProspectMaxBw(String bX5kmProspectMaxBw) {
        this.bX5kmProspectMaxBw = bX5kmProspectMaxBw;
    }

    @JsonProperty("b_pop_name")
    public String getBPopName() {
        return bPopName;
    }

    @JsonProperty("b_pop_name")
    public void setBPopName(String bPopName) {
        this.bPopName = bPopName;
    }

    @JsonProperty("b_X0.5km_avg_bw")
    public String getBX05kmAvgBw() {
        return bX05kmAvgBw;
    }

    @JsonProperty("b_X0.5km_avg_bw")
    public void setBX05kmAvgBw(String bX05kmAvgBw) {
        this.bX05kmAvgBw = bX05kmAvgBw;
    }

    @JsonProperty("b_X5km_max_bw")
    public String getBX5kmMaxBw() {
        return bX5kmMaxBw;
    }

    @JsonProperty("b_X5km_max_bw")
    public void setBX5kmMaxBw(String bX5kmMaxBw) {
        this.bX5kmMaxBw = bX5kmMaxBw;
    }

    @JsonProperty("a_Network_F_NF_CC_Flag")
    public String getANetworkFNFCCFlag() {
        return aNetworkFNFCCFlag;
    }

    @JsonProperty("a_Network_F_NF_CC_Flag")
    public void setANetworkFNFCCFlag(String aNetworkFNFCCFlag) {
        this.aNetworkFNFCCFlag = aNetworkFNFCCFlag;
    }

    @JsonProperty("b_Orch_BW")
    public String getBOrchBW() {
        return bOrchBW;
    }

    @JsonProperty("b_Orch_BW")
    public void setBOrchBW(String bOrchBW) {
        this.bOrchBW = bOrchBW;
    }

    @JsonProperty("a_X0.5km_prospect_perc_feasible")
    public String getAX05kmProspectPercFeasible() {
        return aX05kmProspectPercFeasible;
    }

    @JsonProperty("a_X0.5km_prospect_perc_feasible")
    public void setAX05kmProspectPercFeasible(String aX05kmProspectPercFeasible) {
        this.aX05kmProspectPercFeasible = aX05kmProspectPercFeasible;
    }

    @JsonProperty("a_site_id")
    public String getASiteId() {
        return aSiteId;
    }

    @JsonProperty("a_site_id")
    public void setASiteId(String aSiteId) {
        this.aSiteId = aSiteId;
    }

    @JsonProperty("b_Predicted_Access_Feasibility")
    public String getBPredictedAccessFeasibility() {
        return bPredictedAccessFeasibility;
    }

    @JsonProperty("b_Predicted_Access_Feasibility")
    public void setBPredictedAccessFeasibility(String bPredictedAccessFeasibility) {
        this.bPredictedAccessFeasibility = bPredictedAccessFeasibility;
    }

    @JsonProperty("burstable_bw")
    public String getBurstableBw() {
        return burstableBw;
    }

    @JsonProperty("burstable_bw")
    public void setBurstableBw(String burstableBw) {
        this.burstableBw = burstableBw;
    }

    @JsonProperty("a_Product.Name")
    public String getAProductName() {
        return aProductName;
    }

    @JsonProperty("a_Product.Name")
    public void setAProductName(String aProductName) {
        this.aProductName = aProductName;
    }

    @JsonProperty("b_X5km_prospect_num_feasible")
    public String getBX5kmProspectNumFeasible() {
        return bX5kmProspectNumFeasible;
    }

    @JsonProperty("b_X5km_prospect_num_feasible")
    public void setBX5kmProspectNumFeasible(String bX5kmProspectNumFeasible) {
        this.bX5kmProspectNumFeasible = bX5kmProspectNumFeasible;
    }

    @JsonProperty("bw_mbps")
    public String getBwMbps() {
        return bwMbps;
    }

    @JsonProperty("bw_mbps")
    public void setBwMbps(String bwMbps) {
        this.bwMbps = bwMbps;
    }

    @JsonProperty("b_X5km_prospect_avg_bw")
    public String getBX5kmProspectAvgBw() {
        return bX5kmProspectAvgBw;
    }

    @JsonProperty("b_X5km_prospect_avg_bw")
    public void setBX5kmProspectAvgBw(String bX5kmProspectAvgBw) {
        this.bX5kmProspectAvgBw = bX5kmProspectAvgBw;
    }

    @JsonProperty("a_X2km_prospect_count")
    public String getAX2kmProspectCount() {
        return aX2kmProspectCount;
    }

    @JsonProperty("a_X2km_prospect_count")
    public void setAX2kmProspectCount(String aX2kmProspectCount) {
        this.aX2kmProspectCount = aX2kmProspectCount;
    }

    @JsonProperty("b_X2km_prospect_min_dist")
    public String getBX2kmProspectMinDist() {
        return bX2kmProspectMinDist;
    }

    @JsonProperty("b_X2km_prospect_min_dist")
    public void setBX2kmProspectMinDist(String bX2kmProspectMinDist) {
        this.bX2kmProspectMinDist = bX2kmProspectMinDist;
    }

    @JsonProperty("b_FATG_TCL_Access")
    public String getBFATGTCLAccess() {
        return bFATGTCLAccess;
    }

    @JsonProperty("b_FATG_TCL_Access")
    public void setBFATGTCLAccess(String bFATGTCLAccess) {
        this.bFATGTCLAccess = bFATGTCLAccess;
    }

    @JsonProperty("b_last_mile_contract_term")
    public String getBLastMileContractTerm() {
        return bLastMileContractTerm;
    }

    @JsonProperty("b_last_mile_contract_term")
    public void setBLastMileContractTerm(String bLastMileContractTerm) {
        this.bLastMileContractTerm = bLastMileContractTerm;
    }

    @JsonProperty("b_customer_segment")
    public String getBCustomerSegment() {
        return bCustomerSegment;
    }

    @JsonProperty("b_customer_segment")
    public void setBCustomerSegment(String bCustomerSegment) {
        this.bCustomerSegment = bCustomerSegment;
    }

    @JsonProperty("a_num_connected_cust")
    public String getANumConnectedCust() {
        return aNumConnectedCust;
    }

    @JsonProperty("a_num_connected_cust")
    public void setANumConnectedCust(String aNumConnectedCust) {
        this.aNumConnectedCust = aNumConnectedCust;
    }

    @JsonProperty("b_longitude_final")
    public String getBLongitudeFinal() {
        return bLongitudeFinal;
    }

    @JsonProperty("b_longitude_final")
    public void setBLongitudeFinal(String bLongitudeFinal) {
        this.bLongitudeFinal = bLongitudeFinal;
    }

    @JsonProperty("a_POP_DIST_KM")
    public String getAPOPDISTKM() {
        return aPOPDISTKM;
    }

    @JsonProperty("a_POP_DIST_KM")
    public void setAPOPDISTKM(String aPOPDISTKM) {
        this.aPOPDISTKM = aPOPDISTKM;
    }

    @JsonProperty("b_pop_address")
    public String getBPopAddress() {
        return bPopAddress;
    }

    @JsonProperty("b_pop_address")
    public void setBPopAddress(String bPopAddress) {
        this.bPopAddress = bPopAddress;
    }

    @JsonProperty("b_hh_name")
    public String getBHhName() {
        return bHhName;
    }

    @JsonProperty("b_hh_name")
    public void setBHhName(String bHhName) {
        this.bHhName = bHhName;
    }

    @JsonProperty("a_lm_nrc_nerental_onwl")
    public String getALmNrcNerentalOnwl() {
        return aLmNrcNerentalOnwl;
    }

    @JsonProperty("a_lm_nrc_nerental_onwl")
    public void setALmNrcNerentalOnwl(String aLmNrcNerentalOnwl) {
        this.aLmNrcNerentalOnwl = aLmNrcNerentalOnwl;
    }

    @JsonProperty("b_lm_nrc_inbldg_onwl")
    public String getBLmNrcInbldgOnwl() {
        return bLmNrcInbldgOnwl;
    }

    @JsonProperty("b_lm_nrc_inbldg_onwl")
    public void setBLmNrcInbldgOnwl(String bLmNrcInbldgOnwl) {
        this.bLmNrcInbldgOnwl = bLmNrcInbldgOnwl;
    }

    @JsonProperty("account_id_with_18_digit")
    public String getAccountIdWith18Digit() {
        return accountIdWith18Digit;
    }

    @JsonProperty("account_id_with_18_digit")
    public void setAccountIdWith18Digit(String accountIdWith18Digit) {
        this.accountIdWith18Digit = accountIdWith18Digit;
    }

    @JsonProperty("a_lm_nrc_bw_onwl")
    public String getALmNrcBwOnwl() {
        return aLmNrcBwOnwl;
    }

    @JsonProperty("a_lm_nrc_bw_onwl")
    public void setALmNrcBwOnwl(String aLmNrcBwOnwl) {
        this.aLmNrcBwOnwl = aLmNrcBwOnwl;
    }

    @JsonProperty("a_X2km_cust_count")
    public String getAX2kmCustCount() {
        return aX2kmCustCount;
    }

    @JsonProperty("a_X2km_cust_count")
    public void setAX2kmCustCount(String aX2kmCustCount) {
        this.aX2kmCustCount = aX2kmCustCount;
    }

    @JsonProperty("b_Network_Feasibility_Check")
    public String getBNetworkFeasibilityCheck() {
        return bNetworkFeasibilityCheck;
    }

    @JsonProperty("b_Network_Feasibility_Check")
    public void setBNetworkFeasibilityCheck(String bNetworkFeasibilityCheck) {
        this.bNetworkFeasibilityCheck = bNetworkFeasibilityCheck;
    }

    @JsonProperty("b_FATG_DIST_KM")
    public String getBFATGDISTKM() {
        return bFATGDISTKM;
    }

    @JsonProperty("b_FATG_DIST_KM")
    public void setBFATGDISTKM(String bFATGDISTKM) {
        this.bFATGDISTKM = bFATGDISTKM;
    }

    @JsonProperty("a_X5km_avg_bw")
    public String getAX5kmAvgBw() {
        return aX5kmAvgBw;
    }

    @JsonProperty("a_X5km_avg_bw")
    public void setAX5kmAvgBw(String aX5kmAvgBw) {
        this.aX5kmAvgBw = aX5kmAvgBw;
    }

    @JsonProperty("b_lm_nrc_ospcapex_onwl")
    public String getBLmNrcOspcapexOnwl() {
        return bLmNrcOspcapexOnwl;
    }

    @JsonProperty("b_lm_nrc_ospcapex_onwl")
    public void setBLmNrcOspcapexOnwl(String bLmNrcOspcapexOnwl) {
        this.bLmNrcOspcapexOnwl = bLmNrcOspcapexOnwl;
    }

    @JsonProperty("b_core_check_hh")
    public String getBCoreCheckHh() {
        return bCoreCheckHh;
    }

    @JsonProperty("b_core_check_hh")
    public void setBCoreCheckHh(String bCoreCheckHh) {
        this.bCoreCheckHh = bCoreCheckHh;
    }

    @JsonProperty("a_Network_F_NF_HH")
    public String getANetworkFNFHH() {
        return aNetworkFNFHH;
    }

    @JsonProperty("a_Network_F_NF_HH")
    public void setANetworkFNFHH(String aNetworkFNFHH) {
        this.aNetworkFNFHH = aNetworkFNFHH;
    }

    @JsonProperty("a_error_code")
    public String getAErrorCode() {
        return aErrorCode;
    }

    @JsonProperty("a_error_code")
    public void setAErrorCode(String aErrorCode) {
        this.aErrorCode = aErrorCode;
    }

    @JsonProperty("b_X2km_min_dist")
    public String getBX2kmMinDist() {
        return bX2kmMinDist;
    }

    @JsonProperty("b_X2km_min_dist")
    public void setBX2kmMinDist(String bX2kmMinDist) {
        this.bX2kmMinDist = bX2kmMinDist;
    }

    @JsonProperty("b_a_or_b_end")
    public String getBAOrBEnd() {
        return bAOrBEnd;
    }

    @JsonProperty("b_a_or_b_end")
    public void setBAOrBEnd(String bAOrBEnd) {
        this.bAOrBEnd = bAOrBEnd;
    }

    @JsonProperty("a_Network_Feasibility_Check")
    public String getANetworkFeasibilityCheck() {
        return aNetworkFeasibilityCheck;
    }

    @JsonProperty("a_Network_Feasibility_Check")
    public void setANetworkFeasibilityCheck(String aNetworkFeasibilityCheck) {
        this.aNetworkFeasibilityCheck = aNetworkFeasibilityCheck;
    }

    @JsonProperty("b_X2km_prospect_num_feasible")
    public String getBX2kmProspectNumFeasible() {
        return bX2kmProspectNumFeasible;
    }

    @JsonProperty("b_X2km_prospect_num_feasible")
    public void setBX2kmProspectNumFeasible(String bX2kmProspectNumFeasible) {
        this.bX2kmProspectNumFeasible = bX2kmProspectNumFeasible;
    }

    @JsonProperty("bw_mbps_upd")
    public String getBwMbpsUpd() {
        return bwMbpsUpd;
    }

    @JsonProperty("bw_mbps_upd")
    public void setBwMbpsUpd(String bwMbpsUpd) {
        this.bwMbpsUpd = bwMbpsUpd;
    }

    @JsonProperty("a_access_check_hh")
    public String getAAccessCheckHh() {
        return aAccessCheckHh;
    }

    @JsonProperty("a_access_check_hh")
    public void setAAccessCheckHh(String aAccessCheckHh) {
        this.aAccessCheckHh = aAccessCheckHh;
    }

    @JsonProperty("b_cost_permeter")
    public String getBCostPermeter() {
        return bCostPermeter;
    }

    @JsonProperty("b_cost_permeter")
    public void setBCostPermeter(String bCostPermeter) {
        this.bCostPermeter = bCostPermeter;
    }

    @JsonProperty("b_X0.5km_min_bw")
    public String getBX05kmMinBw() {
        return bX05kmMinBw;
    }

    @JsonProperty("b_X0.5km_min_bw")
    public void setBX05kmMinBw(String bX05kmMinBw) {
        this.bX05kmMinBw = bX05kmMinBw;
    }

    @JsonProperty("a_X0.5km_prospect_count")
    public String getAX05kmProspectCount() {
        return aX05kmProspectCount;
    }

    @JsonProperty("a_X0.5km_prospect_count")
    public void setAX05kmProspectCount(String aX05kmProspectCount) {
        this.aX05kmProspectCount = aX05kmProspectCount;
    }

    @JsonProperty("a_X0.5km_avg_dist")
    public String getAX05kmAvgDist() {
        return aX05kmAvgDist;
    }

    @JsonProperty("a_X0.5km_avg_dist")
    public void setAX05kmAvgDist(String aX05kmAvgDist) {
        this.aX05kmAvgDist = aX05kmAvgDist;
    }

    @JsonProperty("a_FATG_PROW")
    public String getAFATGPROW() {
        return aFATGPROW;
    }

    @JsonProperty("a_FATG_PROW")
    public void setAFATGPROW(String aFATGPROW) {
        this.aFATGPROW = aFATGPROW;
    }

    @JsonProperty("b_X2km_prospect_perc_feasible")
    public String getBX2kmProspectPercFeasible() {
        return bX2kmProspectPercFeasible;
    }

    @JsonProperty("b_X2km_prospect_perc_feasible")
    public void setBX2kmProspectPercFeasible(String bX2kmProspectPercFeasible) {
        this.bX2kmProspectPercFeasible = bX2kmProspectPercFeasible;
    }

    @JsonProperty("a_X2km_min_dist")
    public String getAX2kmMinDist() {
        return aX2kmMinDist;
    }

    @JsonProperty("a_X2km_min_dist")
    public void setAX2kmMinDist(String aX2kmMinDist) {
        this.aX2kmMinDist = aX2kmMinDist;
    }

    @JsonProperty("a_lm_nrc_mast_ofrf")
    public String getALmNrcMastOfrf() {
        return aLmNrcMastOfrf;
    }

    @JsonProperty("a_lm_nrc_mast_ofrf")
    public void setALmNrcMastOfrf(String aLmNrcMastOfrf) {
        this.aLmNrcMastOfrf = aLmNrcMastOfrf;
    }

    @JsonProperty("a_X2km_prospect_num_feasible")
    public String getAX2kmProspectNumFeasible() {
        return aX2kmProspectNumFeasible;
    }

    @JsonProperty("a_X2km_prospect_num_feasible")
    public void setAX2kmProspectNumFeasible(String aX2kmProspectNumFeasible) {
        this.aX2kmProspectNumFeasible = aX2kmProspectNumFeasible;
    }

    @JsonProperty("a_SERVICE_ID")
    public String getASERVICEID() {
        return aSERVICEID;
    }

    @JsonProperty("a_SERVICE_ID")
    public void setASERVICEID(String aSERVICEID) {
        this.aSERVICEID = aSERVICEID;
    }

    @JsonProperty("b_connected_building_tag")
    public String getBConnectedBuildingTag() {
        return bConnectedBuildingTag;
    }

    @JsonProperty("b_connected_building_tag")
    public void setBConnectedBuildingTag(String bConnectedBuildingTag) {
        this.bConnectedBuildingTag = bConnectedBuildingTag;
    }

    @JsonProperty("b_pop_lat")
    public String getBPopLat() {
        return bPopLat;
    }

    @JsonProperty("b_pop_lat")
    public void setBPopLat(String bPopLat) {
        this.bPopLat = bPopLat;
    }

    @JsonProperty("b_X5km_avg_dist")
    public String getBX5kmAvgDist() {
        return bX5kmAvgDist;
    }

    @JsonProperty("b_X5km_avg_dist")
    public void setBX5kmAvgDist(String bX5kmAvgDist) {
        this.bX5kmAvgDist = bX5kmAvgDist;
    }

    @JsonProperty("a_lm_nrc_mast_onrf")
    public String getALmNrcMastOnrf() {
        return aLmNrcMastOnrf;
    }

    @JsonProperty("a_lm_nrc_mast_onrf")
    public void setALmNrcMastOnrf(String aLmNrcMastOnrf) {
        this.aLmNrcMastOnrf = aLmNrcMastOnrf;
    }

    @JsonProperty("a_HH_DIST_KM")
    public String getAHHDISTKM() {
        return aHHDISTKM;
    }

    @JsonProperty("a_HH_DIST_KM")
    public void setAHHDISTKM(String aHHDISTKM) {
        this.aHHDISTKM = aHHDISTKM;
    }

    @JsonProperty("a_pop_selected")
    public String getAPopSelected() {
        return aPopSelected;
    }

    @JsonProperty("a_pop_selected")
    public void setAPopSelected(String aPopSelected) {
        this.aPopSelected = aPopSelected;
    }

    @JsonProperty("a_min_hh_fatg")
    public String getAMinHhFatg() {
        return aMinHhFatg;
    }

    @JsonProperty("a_min_hh_fatg")
    public void setAMinHhFatg(String aMinHhFatg) {
        this.aMinHhFatg = aMinHhFatg;
    }

    @JsonProperty("a_X5km_avg_dist")
    public String getAX5kmAvgDist() {
        return aX5kmAvgDist;
    }

    @JsonProperty("a_X5km_avg_dist")
    public void setAX5kmAvgDist(String aX5kmAvgDist) {
        this.aX5kmAvgDist = aX5kmAvgDist;
    }

    @JsonProperty("b_X5km_prospect_min_dist")
    public String getBX5kmProspectMinDist() {
        return bX5kmProspectMinDist;
    }

    @JsonProperty("b_X5km_prospect_min_dist")
    public void setBX5kmProspectMinDist(String bX5kmProspectMinDist) {
        this.bX5kmProspectMinDist = bX5kmProspectMinDist;
    }

    @JsonProperty("b_X2km_prospect_avg_bw")
    public String getBX2kmProspectAvgBw() {
        return bX2kmProspectAvgBw;
    }

    @JsonProperty("b_X2km_prospect_avg_bw")
    public void setBX2kmProspectAvgBw(String bX2kmProspectAvgBw) {
        this.bX2kmProspectAvgBw = bX2kmProspectAvgBw;
    }

    @JsonProperty("a_latitude_final")
    public String getALatitudeFinal() {
        return aLatitudeFinal;
    }

    @JsonProperty("a_latitude_final")
    public void setALatitudeFinal(String aLatitudeFinal) {
        this.aLatitudeFinal = aLatitudeFinal;
    }

    @JsonProperty("b_Network_F_NF_CC")
    public String getBNetworkFNFCC() {
        return bNetworkFNFCC;
    }

    @JsonProperty("b_Network_F_NF_CC")
    public void setBNetworkFNFCC(String bNetworkFNFCC) {
        this.bNetworkFNFCC = bNetworkFNFCC;
    }

    @JsonProperty("b_POP_Category")
    public String getBPOPCategory() {
        return bPOPCategory;
    }

    @JsonProperty("b_POP_Category")
    public void setBPOPCategory(String bPOPCategory) {
        this.bPOPCategory = bPOPCategory;
    }

    @JsonProperty("a_cost_permeter")
    public String getACostPermeter() {
        return aCostPermeter;
    }

    @JsonProperty("a_cost_permeter")
    public void setACostPermeter(String aCostPermeter) {
        this.aCostPermeter = aCostPermeter;
    }

    @JsonProperty("b_X0.5km_min_dist")
    public String getBX05kmMinDist() {
        return bX05kmMinDist;
    }

    @JsonProperty("b_X0.5km_min_dist")
    public void setBX05kmMinDist(String bX05kmMinDist) {
        this.bX05kmMinDist = bX05kmMinDist;
    }

    @JsonProperty("b_lm_arc_bw_onwl")
    public String getBLmArcBwOnwl() {
        return bLmArcBwOnwl;
    }

    @JsonProperty("b_lm_arc_bw_onwl")
    public void setBLmArcBwOnwl(String bLmArcBwOnwl) {
        this.bLmArcBwOnwl = bLmArcBwOnwl;
    }

    @JsonProperty("b_Probabililty_Access_Feasibility")
    public String getBProbabililtyAccessFeasibility() {
        return bProbabililtyAccessFeasibility;
    }

    @JsonProperty("b_Probabililty_Access_Feasibility")
    public void setBProbabililtyAccessFeasibility(String bProbabililtyAccessFeasibility) {
        this.bProbabililtyAccessFeasibility = bProbabililtyAccessFeasibility;
    }

    @JsonProperty("b_X0.5km_avg_dist")
    public String getBX05kmAvgDist() {
        return bX05kmAvgDist;
    }

    @JsonProperty("b_X0.5km_avg_dist")
    public void setBX05kmAvgDist(String bX05kmAvgDist) {
        this.bX05kmAvgDist = bX05kmAvgDist;
    }

    @JsonProperty("a_lm_arc_bw_onrf")
    public String getALmArcBwOnrf() {
        return aLmArcBwOnrf;
    }

    @JsonProperty("a_lm_arc_bw_onrf")
    public void setALmArcBwOnrf(String aLmArcBwOnrf) {
        this.aLmArcBwOnrf = aLmArcBwOnrf;
    }

    @JsonProperty("link_id")
    public String getLinkId() {
        return linkId;
    }

    @JsonProperty("link_id")
    public void setLinkId(String linkId) {
        this.linkId = linkId;
    }

    @JsonProperty("a_lm_nrc_bw_prov_ofrf")
    public String getALmNrcBwProvOfrf() {
        return aLmNrcBwProvOfrf;
    }

    @JsonProperty("a_lm_nrc_bw_prov_ofrf")
    public void setALmNrcBwProvOfrf(String aLmNrcBwProvOfrf) {
        this.aLmNrcBwProvOfrf = aLmNrcBwProvOfrf;
    }

    @JsonProperty("pop2pop_network_flag")
    public String getPop2popNetworkFlag() {
        return pop2popNetworkFlag;
    }

    @JsonProperty("pop2pop_network_flag")
    public void setPop2popNetworkFlag(String pop2popNetworkFlag) {
        this.pop2popNetworkFlag = pop2popNetworkFlag;
    }

    @JsonProperty("a_POP_TCL_Access")
    public String getAPOPTCLAccess() {
        return aPOPTCLAccess;
    }

    @JsonProperty("a_POP_TCL_Access")
    public void setAPOPTCLAccess(String aPOPTCLAccess) {
        this.aPOPTCLAccess = aPOPTCLAccess;
    }

    @JsonProperty("b_POP_Network_Location_Type")
    public String getBPOPNetworkLocationType() {
        return bPOPNetworkLocationType;
    }

    @JsonProperty("b_POP_Network_Location_Type")
    public void setBPOPNetworkLocationType(String bPOPNetworkLocationType) {
        this.bPOPNetworkLocationType = bPOPNetworkLocationType;
    }

    @JsonProperty("a_FATG_TCL_Access")
    public String getAFATGTCLAccess() {
        return aFATGTCLAccess;
    }

    @JsonProperty("a_FATG_TCL_Access")
    public void setAFATGTCLAccess(String aFATGTCLAccess) {
        this.aFATGTCLAccess = aFATGTCLAccess;
    }

    @JsonProperty("b_error_flag")
    public String getBErrorFlag() {
        return bErrorFlag;
    }

    @JsonProperty("b_error_flag")
    public void setBErrorFlag(String bErrorFlag) {
        this.bErrorFlag = bErrorFlag;
    }

    @JsonProperty("a_lm_nrc_mux_onwl")
    public String getALmNrcMuxOnwl() {
        return aLmNrcMuxOnwl;
    }

    @JsonProperty("a_lm_nrc_mux_onwl")
    public void setALmNrcMuxOnwl(String aLmNrcMuxOnwl) {
        this.aLmNrcMuxOnwl = aLmNrcMuxOnwl;
    }

    @JsonProperty("sla_varient")
    public String getSlaVarient() {
        return slaVarient;
    }

    @JsonProperty("sla_varient")
    public void setSlaVarient(String slaVarient) {
        this.slaVarient = slaVarient;
    }

    @JsonProperty("a_lm_nrc_bw_onrf")
    public String getALmNrcBwOnrf() {
        return aLmNrcBwOnrf;
    }

    @JsonProperty("a_lm_nrc_bw_onrf")
    public void setALmNrcBwOnrf(String aLmNrcBwOnrf) {
        this.aLmNrcBwOnrf = aLmNrcBwOnrf;
    }

    @JsonProperty("a_pop_ui_id")
    public String getAPopUiId() {
        return aPopUiId;
    }

    @JsonProperty("a_pop_ui_id")
    public void setAPopUiId(String aPopUiId) {
        this.aPopUiId = aPopUiId;
    }

    @JsonProperty("b_HH_0_5_access_rings_F")
    public String getBHH05AccessRingsF() {
        return bHH05AccessRingsF;
    }

    @JsonProperty("b_HH_0_5_access_rings_F")
    public void setBHH05AccessRingsF(String bHH05AccessRingsF) {
        this.bHH05AccessRingsF = bHH05AccessRingsF;
    }

    @JsonProperty("b_total_cost")
    public String getBTotalCost() {
        return bTotalCost;
    }

    @JsonProperty("b_total_cost")
    public void setBTotalCost(String bTotalCost) {
        this.bTotalCost = bTotalCost;
    }

    @JsonProperty("b_X0.5km_prospect_avg_dist")
    public String getBX05kmProspectAvgDist() {
        return bX05kmProspectAvgDist;
    }

    @JsonProperty("b_X0.5km_prospect_avg_dist")
    public void setBX05kmProspectAvgDist(String bX05kmProspectAvgDist) {
        this.bX05kmProspectAvgDist = bX05kmProspectAvgDist;
    }

    @JsonProperty("a_core_check_hh")
    public String getACoreCheckHh() {
        return aCoreCheckHh;
    }

    @JsonProperty("a_core_check_hh")
    public void setACoreCheckHh(String aCoreCheckHh) {
        this.aCoreCheckHh = aCoreCheckHh;
    }

    @JsonProperty("b_Network_F_NF_HH_Flag")
    public String getBNetworkFNFHHFlag() {
        return bNetworkFNFHHFlag;
    }

    @JsonProperty("b_Network_F_NF_HH_Flag")
    public void setBNetworkFNFHHFlag(String bNetworkFNFHHFlag) {
        this.bNetworkFNFHHFlag = bNetworkFNFHHFlag;
    }

    @JsonProperty("a_local_loop_interface")
    public String getALocalLoopInterface() {
        return aLocalLoopInterface;
    }

    @JsonProperty("a_local_loop_interface")
    public void setALocalLoopInterface(String aLocalLoopInterface) {
        this.aLocalLoopInterface = aLocalLoopInterface;
    }

    @JsonProperty("a_Probabililty_Access_Feasibility")
    public String getAProbabililtyAccessFeasibility() {
        return aProbabililtyAccessFeasibility;
    }

    @JsonProperty("a_Probabililty_Access_Feasibility")
    public void setAProbabililtyAccessFeasibility(String aProbabililtyAccessFeasibility) {
        this.aProbabililtyAccessFeasibility = aProbabililtyAccessFeasibility;
    }

    @JsonProperty("b_HH_0_5_access_rings_NF")
    public String getBHH05AccessRingsNF() {
        return bHH05AccessRingsNF;
    }

    @JsonProperty("b_HH_0_5_access_rings_NF")
    public void setBHH05AccessRingsNF(String bHH05AccessRingsNF) {
        this.bHH05AccessRingsNF = bHH05AccessRingsNF;
    }

    @JsonProperty("manual_flag")
    public String getManualFlag() {
        return manualFlag;
    }

    @JsonProperty("manual_flag")
    public void setManualFlag(String manualFlag) {
        this.manualFlag = manualFlag;
    }

    @JsonProperty("a_lm_arc_bw_prov_ofrf")
    public String getALmArcBwProvOfrf() {
        return aLmArcBwProvOfrf;
    }

    @JsonProperty("a_lm_arc_bw_prov_ofrf")
    public void setALmArcBwProvOfrf(String aLmArcBwProvOfrf) {
        this.aLmArcBwProvOfrf = aLmArcBwProvOfrf;
    }

    @JsonProperty("b_X0.5km_prospect_min_bw")
    public String getBX05kmProspectMinBw() {
        return bX05kmProspectMinBw;
    }

    @JsonProperty("b_X0.5km_prospect_min_bw")
    public void setBX05kmProspectMinBw(String bX05kmProspectMinBw) {
        this.bX05kmProspectMinBw = bX05kmProspectMinBw;
    }

    @JsonProperty("b_X5km_cust_count")
    public String getBX5kmCustCount() {
        return bX5kmCustCount;
    }

    @JsonProperty("b_X5km_cust_count")
    public void setBX5kmCustCount(String bX5kmCustCount) {
        this.bX5kmCustCount = bX5kmCustCount;
    }

    @JsonProperty("b_X0.5km_prospect_num_feasible")
    public String getBX05kmProspectNumFeasible() {
        return bX05kmProspectNumFeasible;
    }

    @JsonProperty("b_X0.5km_prospect_num_feasible")
    public void setBX05kmProspectNumFeasible(String bX05kmProspectNumFeasible) {
        this.bX05kmProspectNumFeasible = bX05kmProspectNumFeasible;
    }

    @JsonProperty("b_FATG_PROW")
    public String getBFATGPROW() {
        return bFATGPROW;
    }

    @JsonProperty("b_FATG_PROW")
    public void setBFATGPROW(String bFATGPROW) {
        this.bFATGPROW = bFATGPROW;
    }

    @JsonProperty("opportunity_term")
    public String getOpportunityTerm() {
        return opportunityTerm;
    }

    @JsonProperty("opportunity_term")
    public void setOpportunityTerm(String opportunityTerm) {
        this.opportunityTerm = opportunityTerm;
    }

    @JsonProperty("b_num_connected_building")
    public String getBNumConnectedBuilding() {
        return bNumConnectedBuilding;
    }

    @JsonProperty("b_num_connected_building")
    public void setBNumConnectedBuilding(String bNumConnectedBuilding) {
        this.bNumConnectedBuilding = bNumConnectedBuilding;
    }

    @JsonProperty("a_X0.5km_min_bw")
    public String getAX05kmMinBw() {
        return aX05kmMinBw;
    }

    @JsonProperty("a_X0.5km_min_bw")
    public void setAX05kmMinBw(String aX05kmMinBw) {
        this.aX05kmMinBw = aX05kmMinBw;
    }

    @JsonProperty("a_X5km_prospect_count")
    public String getAX5kmProspectCount() {
        return aX5kmProspectCount;
    }

    @JsonProperty("a_X5km_prospect_count")
    public void setAX5kmProspectCount(String aX5kmProspectCount) {
        this.aX5kmProspectCount = aX5kmProspectCount;
    }

    @JsonProperty("a_lm_nrc_inbldg_onwl")
    public String getALmNrcInbldgOnwl() {
        return aLmNrcInbldgOnwl;
    }

    @JsonProperty("a_lm_nrc_inbldg_onwl")
    public void setALmNrcInbldgOnwl(String aLmNrcInbldgOnwl) {
        this.aLmNrcInbldgOnwl = aLmNrcInbldgOnwl;
    }

    @JsonProperty("a_X2km_avg_bw")
    public String getAX2kmAvgBw() {
        return aX2kmAvgBw;
    }

    @JsonProperty("a_X2km_avg_bw")
    public void setAX2kmAvgBw(String aX2kmAvgBw) {
        this.aX2kmAvgBw = aX2kmAvgBw;
    }

    @JsonProperty("b_lm_arc_bw_onrf")
    public String getBLmArcBwOnrf() {
        return bLmArcBwOnrf;
    }

    @JsonProperty("b_lm_arc_bw_onrf")
    public void setBLmArcBwOnrf(String bLmArcBwOnrf) {
        this.bLmArcBwOnrf = bLmArcBwOnrf;
    }

    @JsonProperty("b_X2km_avg_bw")
    public String getBX2kmAvgBw() {
        return bX2kmAvgBw;
    }

    @JsonProperty("b_X2km_avg_bw")
    public void setBX2kmAvgBw(String bX2kmAvgBw) {
        this.bX2kmAvgBw = bX2kmAvgBw;
    }

    @JsonProperty("b_hh_flag")
    public String getBHhFlag() {
        return bHhFlag;
    }

    @JsonProperty("b_hh_flag")
    public void setBHhFlag(String bHhFlag) {
        this.bHhFlag = bHhFlag;
    }

    @JsonProperty("b_pop_ui_id")
    public String getBPopUiId() {
        return bPopUiId;
    }

    @JsonProperty("b_pop_ui_id")
    public void setBPopUiId(String bPopUiId) {
        this.bPopUiId = bPopUiId;
    }

    @JsonProperty("a_Network_F_NF_HH_Flag")
    public String getANetworkFNFHHFlag() {
        return aNetworkFNFHHFlag;
    }

    @JsonProperty("a_Network_F_NF_HH_Flag")
    public void setANetworkFNFHHFlag(String aNetworkFNFHHFlag) {
        this.aNetworkFNFHHFlag = aNetworkFNFHHFlag;
    }

    @JsonProperty("a_error_flag")
    public String getAErrorFlag() {
        return aErrorFlag;
    }

    @JsonProperty("a_error_flag")
    public void setAErrorFlag(String aErrorFlag) {
        this.aErrorFlag = aErrorFlag;
    }

    @JsonProperty("a_X0.5km_min_dist")
    public String getAX05kmMinDist() {
        return aX05kmMinDist;
    }

    @JsonProperty("a_X0.5km_min_dist")
    public void setAX05kmMinDist(String aX05kmMinDist) {
        this.aX05kmMinDist = aX05kmMinDist;
    }

    @JsonProperty("a_Orch_Connection")
    public String getAOrchConnection() {
        return aOrchConnection;
    }

    @JsonProperty("a_Orch_Connection")
    public void setAOrchConnection(String aOrchConnection) {
        this.aOrchConnection = aOrchConnection;
    }

    @JsonProperty("b_X5km_min_dist")
    public String getBX5kmMinDist() {
        return bX5kmMinDist;
    }

    @JsonProperty("b_X5km_min_dist")
    public void setBX5kmMinDist(String bX5kmMinDist) {
        this.bX5kmMinDist = bX5kmMinDist;
    }

    @JsonProperty("b_X2km_prospect_avg_dist")
    public String getBX2kmProspectAvgDist() {
        return bX2kmProspectAvgDist;
    }

    @JsonProperty("b_X2km_prospect_avg_dist")
    public void setBX2kmProspectAvgDist(String bX2kmProspectAvgDist) {
        this.bX2kmProspectAvgDist = bX2kmProspectAvgDist;
    }

    @JsonProperty("b_error_msg")
    public String getBErrorMsg() {
        return bErrorMsg;
    }

    @JsonProperty("b_error_msg")
    public void setBErrorMsg(String bErrorMsg) {
        this.bErrorMsg = bErrorMsg;
    }

    @JsonProperty("a_last_mile_contract_term")
    public String getALastMileContractTerm() {
        return aLastMileContractTerm;
    }

    @JsonProperty("a_last_mile_contract_term")
    public void setALastMileContractTerm(String aLastMileContractTerm) {
        this.aLastMileContractTerm = aLastMileContractTerm;
    }

    @JsonProperty("a_OnnetCity_tag")
    public String getAOnnetCityTag() {
        return aOnnetCityTag;
    }

    @JsonProperty("a_OnnetCity_tag")
    public void setAOnnetCityTag(String aOnnetCityTag) {
        this.aOnnetCityTag = aOnnetCityTag;
    }

    @JsonProperty("a_POP_DIST_KM_SERVICE")
    public String getAPOPDISTKMSERVICE() {
        return aPOPDISTKMSERVICE;
    }

    @JsonProperty("a_POP_DIST_KM_SERVICE")
    public void setAPOPDISTKMSERVICE(String aPOPDISTKMSERVICE) {
        this.aPOPDISTKMSERVICE = aPOPDISTKMSERVICE;
    }

    @JsonProperty("dist_betw_pops")
    public String getDistBetwPops() {
        return distBetwPops;
    }

    @JsonProperty("dist_betw_pops")
    public void setDistBetwPops(String distBetwPops) {
        this.distBetwPops = distBetwPops;
    }

    @JsonProperty("a_connected_cust_tag")
    public String getAConnectedCustTag() {
        return aConnectedCustTag;
    }

    @JsonProperty("a_connected_cust_tag")
    public void setAConnectedCustTag(String aConnectedCustTag) {
        this.aConnectedCustTag = aConnectedCustTag;
    }

    @JsonProperty("b_Orch_LM_Type")
    public String getBOrchLMType() {
        return bOrchLMType;
    }

    @JsonProperty("b_Orch_LM_Type")
    public void setBOrchLMType(String bOrchLMType) {
        this.bOrchLMType = bOrchLMType;
    }

    @JsonProperty("a_X5km_prospect_num_feasible")
    public String getAX5kmProspectNumFeasible() {
        return aX5kmProspectNumFeasible;
    }

    @JsonProperty("a_X5km_prospect_num_feasible")
    public void setAX5kmProspectNumFeasible(String aX5kmProspectNumFeasible) {
        this.aX5kmProspectNumFeasible = aX5kmProspectNumFeasible;
    }

    @JsonProperty("b_pop_network_loc_id")
    public String getBPopNetworkLocId() {
        return bPopNetworkLocId;
    }

    @JsonProperty("b_pop_network_loc_id")
    public void setBPopNetworkLocId(String bPopNetworkLocId) {
        this.bPopNetworkLocId = bPopNetworkLocId;
    }

    @JsonProperty("Type")
    public String getType() {
        return type;
    }

    @JsonProperty("Type")
    public void setType(String type) {
        this.type = type;
    }

    @JsonProperty("b_pop_selected")
    public String getBPopSelected() {
        return bPopSelected;
    }

    @JsonProperty("b_pop_selected")
    public void setBPopSelected(String bPopSelected) {
        this.bPopSelected = bPopSelected;
    }

    @JsonProperty("a_access_check_CC")
    public String getAAccessCheckCC() {
        return aAccessCheckCC;
    }

    @JsonProperty("a_access_check_CC")
    public void setAAccessCheckCC(String aAccessCheckCC) {
        this.aAccessCheckCC = aAccessCheckCC;
    }

    @JsonProperty("a_X0.5km_prospect_min_dist")
    public String getAX05kmProspectMinDist() {
        return aX05kmProspectMinDist;
    }

    @JsonProperty("a_X0.5km_prospect_min_dist")
    public void setAX05kmProspectMinDist(String aX05kmProspectMinDist) {
        this.aX05kmProspectMinDist = aX05kmProspectMinDist;
    }

    @JsonProperty("a_FATG_Category")
    public String getAFATGCategory() {
        return aFATGCategory;
    }

    @JsonProperty("a_FATG_Category")
    public void setAFATGCategory(String aFATGCategory) {
        this.aFATGCategory = aFATGCategory;
    }

    @JsonProperty("a_X0.5km_cust_count")
    public String getAX05kmCustCount() {
        return aX05kmCustCount;
    }

    @JsonProperty("a_X0.5km_cust_count")
    public void setAX05kmCustCount(String aX05kmCustCount) {
        this.aX05kmCustCount = aX05kmCustCount;
    }

    @JsonProperty("b_X0.5km_prospect_min_dist")
    public String getBX05kmProspectMinDist() {
        return bX05kmProspectMinDist;
    }

    @JsonProperty("b_X0.5km_prospect_min_dist")
    public void setBX05kmProspectMinDist(String bX05kmProspectMinDist) {
        this.bX05kmProspectMinDist = bX05kmProspectMinDist;
    }

    @JsonProperty("b_HH_0_5km")
    public String getBHH05km() {
        return bHH05km;
    }

    @JsonProperty("b_HH_0_5km")
    public void setBHH05km(String bHH05km) {
        this.bHH05km = bHH05km;
    }

    @JsonProperty("a_X5km_cust_count")
    public String getAX5kmCustCount() {
        return aX5kmCustCount;
    }

    @JsonProperty("a_X5km_cust_count")
    public void setAX5kmCustCount(String aX5kmCustCount) {
        this.aX5kmCustCount = aX5kmCustCount;
    }

    @JsonProperty("a_X5km_prospect_avg_bw")
    public String getAX5kmProspectAvgBw() {
        return aX5kmProspectAvgBw;
    }

    @JsonProperty("a_X5km_prospect_avg_bw")
    public void setAX5kmProspectAvgBw(String aX5kmProspectAvgBw) {
        this.aX5kmProspectAvgBw = aX5kmProspectAvgBw;
    }

    @JsonProperty("a_hh_flag")
    public String getAHhFlag() {
        return aHhFlag;
    }

    @JsonProperty("a_hh_flag")
    public void setAHhFlag(String aHhFlag) {
        this.aHhFlag = aHhFlag;
    }

    @JsonProperty("a_X2km_prospect_min_dist")
    public String getAX2kmProspectMinDist() {
        return aX2kmProspectMinDist;
    }

    @JsonProperty("a_X2km_prospect_min_dist")
    public void setAX2kmProspectMinDist(String aX2kmProspectMinDist) {
        this.aX2kmProspectMinDist = aX2kmProspectMinDist;
    }

    @JsonProperty("b_X5km_min_bw")
    public String getBX5kmMinBw() {
        return bX5kmMinBw;
    }

    @JsonProperty("b_X5km_min_bw")
    public void setBX5kmMinBw(String bX5kmMinBw) {
        this.bX5kmMinBw = bX5kmMinBw;
    }

    @JsonProperty("b_X0.5km_prospect_avg_bw")
    public String getBX05kmProspectAvgBw() {
        return bX05kmProspectAvgBw;
    }

    @JsonProperty("b_X0.5km_prospect_avg_bw")
    public void setBX05kmProspectAvgBw(String bX05kmProspectAvgBw) {
        this.bX05kmProspectAvgBw = bX05kmProspectAvgBw;
    }

    @JsonProperty("b_HH_DIST_KM")
    public String getBHHDISTKM() {
        return bHHDISTKM;
    }

    @JsonProperty("b_HH_DIST_KM")
    public void setBHHDISTKM(String bHHDISTKM) {
        this.bHHDISTKM = bHHDISTKM;
    }

    @JsonProperty("b_X5km_prospect_perc_feasible")
    public String getBX5kmProspectPercFeasible() {
        return bX5kmProspectPercFeasible;
    }

    @JsonProperty("b_X5km_prospect_perc_feasible")
    public void setBX5kmProspectPercFeasible(String bX5kmProspectPercFeasible) {
        this.bX5kmProspectPercFeasible = bX5kmProspectPercFeasible;
    }

    @JsonProperty("b_POP_Construction_Status")
    public String getBPOPConstructionStatus() {
        return bPOPConstructionStatus;
    }

    @JsonProperty("b_POP_Construction_Status")
    public void setBPOPConstructionStatus(String bPOPConstructionStatus) {
        this.bPOPConstructionStatus = bPOPConstructionStatus;
    }

    @JsonProperty("a_net_pre_feasible_flag")
    public String getANetPreFeasibleFlag() {
        return aNetPreFeasibleFlag;
    }

    @JsonProperty("a_net_pre_feasible_flag")
    public void setANetPreFeasibleFlag(String aNetPreFeasibleFlag) {
        this.aNetPreFeasibleFlag = aNetPreFeasibleFlag;
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

    @JsonProperty("a_time_taken")
    public String getATimeTaken() {
        return aTimeTaken;
    }

    @JsonProperty("a_time_taken")
    public void setATimeTaken(String aTimeTaken) {
        this.aTimeTaken = aTimeTaken;
    }

    @JsonProperty("a_total_cost")
    public String getATotalCost() {
        return aTotalCost;
    }

    @JsonProperty("a_total_cost")
    public void setATotalCost(String aTotalCost) {
        this.aTotalCost = aTotalCost;
    }

    @JsonProperty("b_error_code")
    public String getBErrorCode() {
        return bErrorCode;
    }

    @JsonProperty("b_error_code")
    public void setBErrorCode(String bErrorCode) {
        this.bErrorCode = bErrorCode;
    }

    @JsonProperty("a_X5km_min_dist")
    public String getAX5kmMinDist() {
        return aX5kmMinDist;
    }

    @JsonProperty("a_X5km_min_dist")
    public void setAX5kmMinDist(String aX5kmMinDist) {
        this.aX5kmMinDist = aX5kmMinDist;
    }

    @JsonProperty("b_X5km_prospect_avg_dist")
    public String getBX5kmProspectAvgDist() {
        return bX5kmProspectAvgDist;
    }

    @JsonProperty("b_X5km_prospect_avg_dist")
    public void setBX5kmProspectAvgDist(String bX5kmProspectAvgDist) {
        this.bX5kmProspectAvgDist = bX5kmProspectAvgDist;
    }

    @JsonProperty("a_pop_address")
    public String getAPopAddress() {
        return aPopAddress;
    }

    @JsonProperty("a_pop_address")
    public void setAPopAddress(String aPopAddress) {
        this.aPopAddress = aPopAddress;
    }

    @JsonProperty("a_X0.5km_prospect_avg_dist")
    public String getAX05kmProspectAvgDist() {
        return aX05kmProspectAvgDist;
    }

    @JsonProperty("a_X0.5km_prospect_avg_dist")
    public void setAX05kmProspectAvgDist(String aX05kmProspectAvgDist) {
        this.aX05kmProspectAvgDist = aX05kmProspectAvgDist;
    }

    @JsonProperty("b_POP_TCL_Access")
    public String getBPOPTCLAccess() {
        return bPOPTCLAccess;
    }

    @JsonProperty("b_POP_TCL_Access")
    public void setBPOPTCLAccess(String bPOPTCLAccess) {
        this.bPOPTCLAccess = bPOPTCLAccess;
    }

    @JsonProperty("b_core_check_CC")
    public String getBCoreCheckCC() {
        return bCoreCheckCC;
    }

    @JsonProperty("b_core_check_CC")
    public void setBCoreCheckCC(String bCoreCheckCC) {
        this.bCoreCheckCC = bCoreCheckCC;
    }

    @JsonProperty("a_X5km_prospect_max_bw")
    public String getAX5kmProspectMaxBw() {
        return aX5kmProspectMaxBw;
    }

    @JsonProperty("a_X5km_prospect_max_bw")
    public void setAX5kmProspectMaxBw(String aX5kmProspectMaxBw) {
        this.aX5kmProspectMaxBw = aX5kmProspectMaxBw;
    }

    @JsonProperty("b_POP_DIST_KM")
    public String getBPOPDISTKM() {
        return bPOPDISTKM;
    }

    @JsonProperty("b_POP_DIST_KM")
    public void setBPOPDISTKM(String bPOPDISTKM) {
        this.bPOPDISTKM = bPOPDISTKM;
    }

    @JsonProperty("product_name")
    public String getProductName() {
        return productName;
    }

    @JsonProperty("product_name")
    public void setProductName(String productName) {
        this.productName = productName;
    }

    @JsonProperty("a_Network_F_NF_CC")
    public String getANetworkFNFCC() {
        return aNetworkFNFCC;
    }

    @JsonProperty("a_Network_F_NF_CC")
    public void setANetworkFNFCC(String aNetworkFNFCC) {
        this.aNetworkFNFCC = aNetworkFNFCC;
    }

    @JsonProperty("a_X5km_max_bw")
    public String getAX5kmMaxBw() {
        return aX5kmMaxBw;
    }

    @JsonProperty("a_X5km_max_bw")
    public void setAX5kmMaxBw(String aX5kmMaxBw) {
        this.aX5kmMaxBw = aX5kmMaxBw;
    }

    @JsonProperty("b_lm_nrc_bw_onrf")
    public String getBLmNrcBwOnrf() {
        return bLmNrcBwOnrf;
    }

    @JsonProperty("b_lm_nrc_bw_onrf")
    public void setBLmNrcBwOnrf(String bLmNrcBwOnrf) {
        this.bLmNrcBwOnrf = bLmNrcBwOnrf;
    }

    @JsonProperty("a_lm_nrc_ospcapex_onwl")
    public String getALmNrcOspcapexOnwl() {
        return aLmNrcOspcapexOnwl;
    }

    @JsonProperty("a_lm_nrc_ospcapex_onwl")
    public void setALmNrcOspcapexOnwl(String aLmNrcOspcapexOnwl) {
        this.aLmNrcOspcapexOnwl = aLmNrcOspcapexOnwl;
    }

    @JsonProperty("a_X2km_avg_dist")
    public String getAX2kmAvgDist() {
        return aX2kmAvgDist;
    }

    @JsonProperty("a_X2km_avg_dist")
    public void setAX2kmAvgDist(String aX2kmAvgDist) {
        this.aX2kmAvgDist = aX2kmAvgDist;
    }

    @JsonProperty("b_Network_F_NF_CC_Flag")
    public String getBNetworkFNFCCFlag() {
        return bNetworkFNFCCFlag;
    }

    @JsonProperty("b_Network_F_NF_CC_Flag")
    public void setBNetworkFNFCCFlag(String bNetworkFNFCCFlag) {
        this.bNetworkFNFCCFlag = bNetworkFNFCCFlag;
    }

    @JsonProperty("b_lm_nrc_mast_ofrf")
    public String getBLmNrcMastOfrf() {
        return bLmNrcMastOfrf;
    }

    @JsonProperty("b_lm_nrc_mast_ofrf")
    public void setBLmNrcMastOfrf(String bLmNrcMastOfrf) {
        this.bLmNrcMastOfrf = bLmNrcMastOfrf;
    }

    @JsonProperty("b_access_check_hh")
    public String getBAccessCheckHh() {
        return bAccessCheckHh;
    }

    @JsonProperty("b_access_check_hh")
    public void setBAccessCheckHh(String bAccessCheckHh) {
        this.bAccessCheckHh = bAccessCheckHh;
    }

    @JsonProperty("b_FATG_Network_Location_Type")
    public String getBFATGNetworkLocationType() {
        return bFATGNetworkLocationType;
    }

    @JsonProperty("b_FATG_Network_Location_Type")
    public void setBFATGNetworkLocationType(String bFATGNetworkLocationType) {
        this.bFATGNetworkLocationType = bFATGNetworkLocationType;
    }

    @JsonProperty("b_X2km_cust_count")
    public String getBX2kmCustCount() {
        return bX2kmCustCount;
    }

    @JsonProperty("b_X2km_cust_count")
    public void setBX2kmCustCount(String bX2kmCustCount) {
        this.bX2kmCustCount = bX2kmCustCount;
    }

    @JsonProperty("b_lm_nrc_mast_onrf")
    public String getBLmNrcMastOnrf() {
        return bLmNrcMastOnrf;
    }

    @JsonProperty("b_lm_nrc_mast_onrf")
    public void setBLmNrcMastOnrf(String bLmNrcMastOnrf) {
        this.bLmNrcMastOnrf = bLmNrcMastOnrf;
    }

    @JsonProperty("a_X2km_prospect_avg_bw")
    public String getAX2kmProspectAvgBw() {
        return aX2kmProspectAvgBw;
    }

    @JsonProperty("a_X2km_prospect_avg_bw")
    public void setAX2kmProspectAvgBw(String aX2kmProspectAvgBw) {
        this.aX2kmProspectAvgBw = aX2kmProspectAvgBw;
    }

    @JsonProperty("a_X5km_prospect_perc_feasible")
    public String getAX5kmProspectPercFeasible() {
        return aX5kmProspectPercFeasible;
    }

    @JsonProperty("a_X5km_prospect_perc_feasible")
    public void setAX5kmProspectPercFeasible(String aX5kmProspectPercFeasible) {
        this.aX5kmProspectPercFeasible = aX5kmProspectPercFeasible;
    }

    @JsonProperty("b_X5km_avg_bw")
    public String getBX5kmAvgBw() {
        return bX5kmAvgBw;
    }

    @JsonProperty("b_X5km_avg_bw")
    public void setBX5kmAvgBw(String bX5kmAvgBw) {
        this.bX5kmAvgBw = bX5kmAvgBw;
    }

    @JsonProperty("a_connected_building_tag")
    public String getAConnectedBuildingTag() {
        return aConnectedBuildingTag;
    }

    @JsonProperty("a_connected_building_tag")
    public void setAConnectedBuildingTag(String aConnectedBuildingTag) {
        this.aConnectedBuildingTag = aConnectedBuildingTag;
    }

    @JsonProperty("a_pop_name")
    public String getAPopName() {
        return aPopName;
    }

    @JsonProperty("a_pop_name")
    public void setAPopName(String aPopName) {
        this.aPopName = aPopName;
    }

    @JsonProperty("a_core_check_CC")
    public String getACoreCheckCC() {
        return aCoreCheckCC;
    }

    @JsonProperty("a_core_check_CC")
    public void setACoreCheckCC(String aCoreCheckCC) {
        this.aCoreCheckCC = aCoreCheckCC;
    }

    @JsonProperty("feasibility_response_created_date")
    public String getFeasibilityResponseCreatedDate() {
        return feasibilityResponseCreatedDate;
    }

    @JsonProperty("feasibility_response_created_date")
    public void setFeasibilityResponseCreatedDate(String feasibilityResponseCreatedDate) {
        this.feasibilityResponseCreatedDate = feasibilityResponseCreatedDate;
    }

    @JsonProperty("b_resp_city")
    public String getBRespCity() {
        return bRespCity;
    }

    @JsonProperty("b_resp_city")
    public void setBRespCity(String bRespCity) {
        this.bRespCity = bRespCity;
    }

    @JsonProperty("a_FATG_Network_Location_Type")
    public String getAFATGNetworkLocationType() {
        return aFATGNetworkLocationType;
    }

    @JsonProperty("a_FATG_Network_Location_Type")
    public void setAFATGNetworkLocationType(String aFATGNetworkLocationType) {
        this.aFATGNetworkLocationType = aFATGNetworkLocationType;
    }

    @JsonProperty("a_pop_long")
    public String getAPopLong() {
        return aPopLong;
    }

    @JsonProperty("a_pop_long")
    public void setAPopLong(String aPopLong) {
        this.aPopLong = aPopLong;
    }

    @JsonProperty("a_X0.5km_prospect_min_bw")
    public String getAX05kmProspectMinBw() {
        return aX05kmProspectMinBw;
    }

    @JsonProperty("a_X0.5km_prospect_min_bw")
    public void setAX05kmProspectMinBw(String aX05kmProspectMinBw) {
        this.aX05kmProspectMinBw = aX05kmProspectMinBw;
    }

    @JsonProperty("a_POP_DIST_KM_SERVICE_MOD")
    public String getAPOPDISTKMSERVICEMOD() {
        return aPOPDISTKMSERVICEMOD;
    }

    @JsonProperty("a_POP_DIST_KM_SERVICE_MOD")
    public void setAPOPDISTKMSERVICEMOD(String aPOPDISTKMSERVICEMOD) {
        this.aPOPDISTKMSERVICEMOD = aPOPDISTKMSERVICEMOD;
    }

    @JsonProperty("a_X5km_prospect_min_dist")
    public String getAX5kmProspectMinDist() {
        return aX5kmProspectMinDist;
    }

    @JsonProperty("a_X5km_prospect_min_dist")
    public void setAX5kmProspectMinDist(String aX5kmProspectMinDist) {
        this.aX5kmProspectMinDist = aX5kmProspectMinDist;
    }

    @JsonProperty("a_Orch_BW")
    public String getAOrchBW() {
        return aOrchBW;
    }

    @JsonProperty("a_Orch_BW")
    public void setAOrchBW(String aOrchBW) {
        this.aOrchBW = aOrchBW;
    }

    @JsonProperty("b_Orch_Category")
    public String getBOrchCategory() {
        return bOrchCategory;
    }

    @JsonProperty("b_Orch_Category")
    public void setBOrchCategory(String bOrchCategory) {
        this.bOrchCategory = bOrchCategory;
    }

    @JsonProperty("b_X0.5km_max_bw")
    public String getBX05kmMaxBw() {
        return bX05kmMaxBw;
    }

    @JsonProperty("b_X0.5km_max_bw")
    public void setBX05kmMaxBw(String bX05kmMaxBw) {
        this.bX05kmMaxBw = bX05kmMaxBw;
    }

    @JsonProperty("b_site_id")
    public String getBSiteId() {
        return bSiteId;
    }

    @JsonProperty("b_site_id")
    public void setBSiteId(String bSiteId) {
        this.bSiteId = bSiteId;
    }

    @JsonProperty("a_HH_0_5km")
    public String getAHH05km() {
        return aHH05km;
    }

    @JsonProperty("a_HH_0_5km")
    public void setAHH05km(String aHH05km) {
        this.aHH05km = aHH05km;
    }

    @JsonProperty("a_X0.5km_max_bw")
    public String getAX05kmMaxBw() {
        return aX05kmMaxBw;
    }

    @JsonProperty("a_X0.5km_max_bw")
    public void setAX05kmMaxBw(String aX05kmMaxBw) {
        this.aX05kmMaxBw = aX05kmMaxBw;
    }

    @JsonProperty("a_X0.5km_prospect_num_feasible")
    public String getAX05kmProspectNumFeasible() {
        return aX05kmProspectNumFeasible;
    }

    @JsonProperty("a_X0.5km_prospect_num_feasible")
    public void setAX05kmProspectNumFeasible(String aX05kmProspectNumFeasible) {
        this.aX05kmProspectNumFeasible = aX05kmProspectNumFeasible;
    }

    @JsonProperty("a_a_or_b_end")
    public String getAAOrBEnd() {
        return aAOrBEnd;
    }

    @JsonProperty("a_a_or_b_end")
    public void setAAOrBEnd(String aAOrBEnd) {
        this.aAOrBEnd = aAOrBEnd;
    }

    @JsonProperty("b_X0.5km_cust_count")
    public String getBX05kmCustCount() {
        return bX05kmCustCount;
    }

    @JsonProperty("b_X0.5km_cust_count")
    public void setBX05kmCustCount(String bX05kmCustCount) {
        this.bX05kmCustCount = bX05kmCustCount;
    }

    @JsonProperty("a_Orch_LM_Type")
    public String getAOrchLMType() {
        return aOrchLMType;
    }

    @JsonProperty("a_Orch_LM_Type")
    public void setAOrchLMType(String aOrchLMType) {
        this.aOrchLMType = aOrchLMType;
    }

    @JsonProperty("b_lm_nrc_nerental_onwl")
    public String getBLmNrcNerentalOnwl() {
        return bLmNrcNerentalOnwl;
    }

    @JsonProperty("b_lm_nrc_nerental_onwl")
    public void setBLmNrcNerentalOnwl(String bLmNrcNerentalOnwl) {
        this.bLmNrcNerentalOnwl = bLmNrcNerentalOnwl;
    }

    @JsonProperty("b_local_loop_interface")
    public String getBLocalLoopInterface() {
        return bLocalLoopInterface;
    }

    @JsonProperty("b_local_loop_interface")
    public void setBLocalLoopInterface(String bLocalLoopInterface) {
        this.bLocalLoopInterface = bLocalLoopInterface;
    }

    @JsonProperty("a_FATG_Building_Type")
    public String getAFATGBuildingType() {
        return aFATGBuildingType;
    }

    @JsonProperty("a_FATG_Building_Type")
    public void setAFATGBuildingType(String aFATGBuildingType) {
        this.aFATGBuildingType = aFATGBuildingType;
    }

    @JsonProperty("a_Orch_Category")
    public String getAOrchCategory() {
        return aOrchCategory;
    }

    @JsonProperty("a_Orch_Category")
    public void setAOrchCategory(String aOrchCategory) {
        this.aOrchCategory = aOrchCategory;
    }

    @JsonProperty("chargeable_distance")
    public String getChargeableDistance() {
        return chargeableDistance;
    }

    @JsonProperty("chargeable_distance")
    public void setChargeableDistance(String chargeableDistance) {
        this.chargeableDistance = chargeableDistance;
    }

    @JsonProperty("b_X0.5km_prospect_perc_feasible")
    public String getBX05kmProspectPercFeasible() {
        return bX05kmProspectPercFeasible;
    }

    @JsonProperty("b_X0.5km_prospect_perc_feasible")
    public void setBX05kmProspectPercFeasible(String bX05kmProspectPercFeasible) {
        this.bX05kmProspectPercFeasible = bX05kmProspectPercFeasible;
    }

    @JsonProperty("b_FATG_Building_Type")
    public String getBFATGBuildingType() {
        return bFATGBuildingType;
    }

    @JsonProperty("b_FATG_Building_Type")
    public void setBFATGBuildingType(String bFATGBuildingType) {
        this.bFATGBuildingType = bFATGBuildingType;
    }

    @JsonProperty("b_Orch_Connection")
    public String getBOrchConnection() {
        return bOrchConnection;
    }

    @JsonProperty("b_Orch_Connection")
    public void setBOrchConnection(String bOrchConnection) {
        this.bOrchConnection = bOrchConnection;
    }

    @JsonProperty("b_min_hh_fatg")
    public String getBMinHhFatg() {
        return bMinHhFatg;
    }

    @JsonProperty("b_min_hh_fatg")
    public void setBMinHhFatg(String bMinHhFatg) {
        this.bMinHhFatg = bMinHhFatg;
    }

    @JsonProperty("b_X0.5km_prospect_max_bw")
    public String getBX05kmProspectMaxBw() {
        return bX05kmProspectMaxBw;
    }

    @JsonProperty("b_X0.5km_prospect_max_bw")
    public void setBX05kmProspectMaxBw(String bX05kmProspectMaxBw) {
        this.bX05kmProspectMaxBw = bX05kmProspectMaxBw;
    }

    @JsonProperty("quotetype_quote")
    public String getQuotetypeQuote() {
        return quotetypeQuote;
    }

    @JsonProperty("quotetype_quote")
    public void setQuotetypeQuote(String quotetypeQuote) {
        this.quotetypeQuote = quotetypeQuote;
    }

    @JsonProperty("a_error_msg")
    public String getAErrorMsg() {
        return aErrorMsg;
    }

    @JsonProperty("a_error_msg")
    public void setAErrorMsg(String aErrorMsg) {
        this.aErrorMsg = aErrorMsg;
    }

    @JsonProperty("b_SERVICE_ID")
    public String getBSERVICEID() {
        return bSERVICEID;
    }

    @JsonProperty("b_SERVICE_ID")
    public void setBSERVICEID(String bSERVICEID) {
        this.bSERVICEID = bSERVICEID;
    }

    @JsonProperty("a_customer_segment")
    public String getACustomerSegment() {
        return aCustomerSegment;
    }

    @JsonProperty("a_customer_segment")
    public void setACustomerSegment(String aCustomerSegment) {
        this.aCustomerSegment = aCustomerSegment;
    }
    public List<AccessRingInfo> getaAccessRings() {
		return aAccessRings;
	}

	public void setaAccessRings(List<AccessRingInfo> aAccessRings) {
		this.aAccessRings = aAccessRings;
	}

	public List<AccessRingInfo> getbAccessRings() {
		return bAccessRings;
	}

	public void setbAccessRings(List<AccessRingInfo> bAccessRings) {
		this.bAccessRings = bAccessRings;
	}

	public List<MuxDetailsItem> getaMuxDetails() {
		return aMuxDetails;
	}

	public void setaMuxDetails(List<MuxDetailsItem> aMuxDetails) {
		this.aMuxDetails = aMuxDetails;
	}

	public List<MuxDetailsItem> getbMuxDetails() {
		return bMuxDetails;
	}

	public void setbMuxDetails(List<MuxDetailsItem> bMuxDetails) {
		this.bMuxDetails = bMuxDetails;
	}
    
    //macd attributes
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

	

	@JsonProperty("macd_service")
	public String getMacdService() {
		return macdService;
	}

	@JsonProperty("macd_service")
	public void setMacdService(String macdService) {
		this.macdService = macdService;
	}


	@JsonProperty("service_id")
	public String getServiceId() {
		return serviceId;
	}

	@JsonProperty("service_id")
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	@JsonProperty("access_provider")
	public String getAccessProvider() {
		return accessProvider;
	}
	@JsonProperty("access_provider")
	public void setAccessProvider(String accessProvider) {
		this.accessProvider = accessProvider;
	}
	@JsonProperty("lat_long")
	public String getLatLong() {
		return latLong;
	}
	@JsonProperty("lat_long")
	public void setLatLong(String latLong) {
		this.latLong = latLong;
	}
	@JsonProperty("zip_code")
	public String getZipCode() {
		return zipCode;
	}
	@JsonProperty("zip_code")
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	@JsonProperty("cu_le_id")
	public String getCuLeId() {
		return cuLeId;
	}
	@JsonProperty("cu_le_id")
	public void setCuLeId(String cuLeId) {
		this.cuLeId = cuLeId;
	}
	@JsonProperty("service_commissioned_date")
	public String getServiceCommissionedDate() {
		return serviceCommissionedDate;
	}
	@JsonProperty("service_commissioned_date")
	public void setServiceCommissionedDate(String serviceCommissionedDate) {
		this.serviceCommissionedDate = serviceCommissionedDate;
	}
	@JsonProperty("old_Ll_Bw")
	public String getOldLlBw() {
		return oldLlBw;
	}
	@JsonProperty("old_Ll_Bw")
	public void setOldLlBw(String oldLlBw) {
		this.oldLlBw = oldLlBw;
	}
	@JsonProperty("old_contract_term")
	public String getOldContractTerm() {
		return oldContractTerm;
	}
	@JsonProperty("old_contract_term")
	public void setOldContractTerm(String oldContractTerm) {
		this.oldContractTerm = oldContractTerm;
	}
	@JsonProperty("prd_category")
	public String getPrdCategory() {
		return prdCategory;
	}
	@JsonProperty("prd_category")
	public void setPrdCategory(String prdCategory) {
		this.prdCategory = prdCategory;
	}
	@JsonProperty("address")
	public String getAddress() {
		return address;
	}
	@JsonProperty("address")
	public void setAddress(String address) {
		this.address = address;
	}
	@JsonProperty("bandwidth")
	public String getBandwidth() {
		return bandwidth;
	}
	@JsonProperty("bandwidth")
	public void setBandwidth(String bandwidth) {
		this.bandwidth = bandwidth;
	}
	@JsonProperty("ll_change")
	public String getLlChange() {
		return llChange;
	}
	@JsonProperty("ll_change")
	public void setLlChange(String llChange) {
		this.llChange = llChange;
	}
	public String getIsCustomer() {
		return isCustomer;
	}
	public void setIsCustomer(String isCustomer) {
		this.isCustomer = isCustomer;
	}
	
	//mf 
		@JsonProperty("a_otc_modem_charges")
		public String getaOtcModemCharges() {
			return aOtcModemCharges;
		}
		@JsonProperty("a_otc_modem_charges")
		public void setaOtcModemCharges(String aOtcModemCharges) {
			this.aOtcModemCharges = aOtcModemCharges;
		}
		@JsonProperty("a_lm_nrc_bw_prov_ofwl")
		public String getaLmNrcBwProvOfwl() {
			return aLmNrcBwProvOfwl;
		}
		@JsonProperty("a_lm_nrc_bw_prov_ofwl")
		public void setaLmNrcBwProvOfwl(String aLmNrcBwProvOfwl) {
			this.aLmNrcBwProvOfwl = aLmNrcBwProvOfwl;
		}
		@JsonProperty("a_arc_modem_charges")
		public String getaArcModemCharges() {
			return aArcModemCharges;
		}
		@JsonProperty("a_arc_modem_charges")
		public void setaArcModemCharges(String aArcModemCharges) {
			this.aArcModemCharges = aArcModemCharges;
		}
		@JsonProperty("a_arc_bw")
		public String getaArcBw() {
			return aArcBw;
		}
		@JsonProperty("a_arc_bw")
		public void setaArcBw(String aArcBw) {
			this.aArcBw = aArcBw;
		}
		@JsonProperty("a_lm_nrc_prow_onwl")
		public String getaLmNrcProwOnwl() {
			return aLmNrcProwOnwl;
		}
		@JsonProperty("a_lm_nrc_prow_onwl")
		public void setaLmNrcProwOnwl(String aLmNrcProwOnwl) {
			this.aLmNrcProwOnwl = aLmNrcProwOnwl;
		}
		@JsonProperty("a_lm_arc_prow_onwl")
		public String getaLmArcProwOnwl() {
			return aLmArcProwOnwl;
		}
		@JsonProperty("a_lm_arc_prow_onwl")
		public void setaLmArcProwOnwl(String aLmArcProwOnwl) {
			this.aLmArcProwOnwl = aLmArcProwOnwl;
		}
		@JsonProperty("a_lm_arc_converter_charges_onrf")
		public String getaLmArcConverterChargesOnrf() {
			return aLmArcConverterChargesOnrf;
		}
		@JsonProperty("a_lm_arc_converter_charges_onrf")
		public void setaLmArcConverterChargesOnrf(String aLmArcConverterChargesOnrf) {
			this.aLmArcConverterChargesOnrf = aLmArcConverterChargesOnrf;
		}
		@JsonProperty("a_lm_arc_bw_backhaul_onrf")
		public String getaLmArcBwBackhaulOnrf() {
			return aLmArcBwBackhaulOnrf;
		}
		@JsonProperty("a_lm_arc_bw_backhaul_onrf")
		public void setaLmArcBwBackhaulOnrf(String aLmArcBwBackhaulOnrf) {
			this.aLmArcBwBackhaulOnrf = aLmArcBwBackhaulOnrf;
		}
		@JsonProperty("a_lm_arc_colocation_onrf")
		public String getaLmArcColocationOnrf() {
			return aLmArcColocationOnrf;
		}
		@JsonProperty("a_lm_arc_colocation_onrf")
		public void setaLmArcColocationOnrf(String aLmArcColocationOnrf) {
			this.aLmArcColocationOnrf = aLmArcColocationOnrf;
		}
		@JsonProperty("a_lm_otc_modem_charges_offwl")
		public String getaLmOtcModemChargesOffwl() {
			return aLmOtcModemChargesOffwl;
		}
		@JsonProperty("a_lm_otc_modem_charges_offwl")
		public void setaLmOtcModemChargesOffwl(String aLmOtcModemChargesOffwl) {
			this.aLmOtcModemChargesOffwl = aLmOtcModemChargesOffwl;
		}	
		@JsonProperty("a_lm_otc_nrc_installation_offwl")
	    public String getaLmOtcNrcInstallationOffwl() {
			return aLmOtcNrcInstallationOffwl;
		}
		@JsonProperty("a_lm_otc_nrc_installation_offwl")
		public void setaLmOtcNrcInstallationOffwl(String aLmOtcNrcInstallationOffwl) {
			this.aLmOtcNrcInstallationOffwl = aLmOtcNrcInstallationOffwl;
		}
		@JsonProperty("a_lm_arc_modem_charges_offwl")
		public String getaLmArcModemChargesOffwl() {
			return aLmArcModemChargesOffwl;
		}
		@JsonProperty("a_lm_arc_modem_charges_offwl")
		public void setaLmArcModemChargesOffwl(String aLmArcModemChargesOffwl) {
			this.aLmArcModemChargesOffwl = aLmArcModemChargesOffwl;
		}
		@JsonProperty("a_lm_arc_bw_offwl")
		public String getaLmArcBwOffwl() {
			return aLmArcBwOffwl;
		}
		@JsonProperty("a_lm_arc_bw_offwl")
		public void setaLmArcBwOffwl(String aLmArcBwOffwl) {
			this.aLmArcBwOffwl = aLmArcBwOffwl;
		}
		@JsonProperty("a_provider_name")
		public String getaProviderName() {
			return aProviderName;
		}
		@JsonProperty("a_provider_name")
		public void setaProviderName(String aProviderName) {
			this.aProviderName = aProviderName;
		}
		@JsonProperty("a_BHConnectivity")
		public String getaBHConnectivity() {
			return aBHConnectivity;
		}
		@JsonProperty("a_BHConnectivity")
		public void setaBHConnectivity(String aBHConnectivity) {
			this.aBHConnectivity = aBHConnectivity;
		}
		@JsonProperty("a_lm_arc_orderable_bw_onwl")
		public String getaLmArcOrderableBwOnwl() {
			return aLmArcOrderableBwOnwl;
		}
		@JsonProperty("a_lm_arc_orderable_bw_onwl")
		public void setaLmArcOrderableBwOnwl(String aLmArcOrderableBwOnwl) {
			this.aLmArcOrderableBwOnwl = aLmArcOrderableBwOnwl;
		}
		@JsonProperty("a_lm_otc_nrc_orderable_bw_onwl")
		public String getaLmOtcNrcOrderableBwOnwl() {
			return aLmOtcNrcOrderableBwOnwl;
		}
		@JsonProperty("a_lm_otc_nrc_orderable_bw_onwl")
		public void setaLmOtcNrcOrderableBwOnwl(String aLmOtcNrcOrderableBwOnwl) {
			this.aLmOtcNrcOrderableBwOnwl = aLmOtcNrcOrderableBwOnwl;
		}
		@JsonProperty("a_lm_nrc_network_capex_onwl")
		public String getaLmNrcNetworkCapexOnwl() {
			return aLmNrcNetworkCapexOnwl;
		}
		@JsonProperty("a_lm_nrc_network_capex_onwl")
		public void setaLmNrcNetworkCapexOnwl(String aLmNrcNetworkCapexOnwl) {
			this.aLmNrcNetworkCapexOnwl = aLmNrcNetworkCapexOnwl;
		}
		@JsonProperty("a_lm_arc_radwin_bw_onrf")
		public String getaLmArcRadwinBwOnrf() {
			return aLmArcRadwinBwOnrf;
		}
		@JsonProperty("a_lm_arc_radwin_bw_onrf")
		public void setaLmArcRadwinBwOnrf(String aLmArcRadwinBwOnrf) {
			this.aLmArcRadwinBwOnrf = aLmArcRadwinBwOnrf;
		}
		

		
		@JsonProperty("b_otc_modem_charges")
		public String getbOtcModemCharges() {
			return bOtcModemCharges;
		}
		@JsonProperty("b_otc_modem_charges")
		public void setbOtcModemCharges(String bOtcModemCharges) {
			this.bOtcModemCharges = bOtcModemCharges;
		}
		@JsonProperty("b_lm_nrc_bw_prov_ofwl")
		public String getbLmNrcBwProvOfwl() {
			return bLmNrcBwProvOfwl;
		}
		@JsonProperty("b_lm_nrc_bw_prov_ofwl")
		public void setbLmNrcBwProvOfwl(String bLmNrcBwProvOfwl) {
			this.bLmNrcBwProvOfwl = bLmNrcBwProvOfwl;
		}
		@JsonProperty("b_arc_modem_charges")
		public String getbArcModemCharges() {
			return bArcModemCharges;
		}
		@JsonProperty("b_arc_modem_charges")
		public void setbArcModemCharges(String bArcModemCharges) {
			this.bArcModemCharges = bArcModemCharges;
		}
		@JsonProperty("b_arc_bw")
		public String getbArcBw() {
			return bArcBw;
		}
		@JsonProperty("b_arc_bw")
		public void setbArcBw(String bArcBw) {
			this.bArcBw = bArcBw;
		}
		@JsonProperty("b_lm_nrc_prow_onwl")
		public String getbLmNrcProwOnwl() {
			return bLmNrcProwOnwl;
		}
		@JsonProperty("b_lm_nrc_prow_onwl")
		public void setbLmNrcProwOnwl(String bLmNrcProwOnwl) {
			this.bLmNrcProwOnwl = bLmNrcProwOnwl;
		}
		@JsonProperty("b_lm_arc_prow_onwl")
		public String getbLmArcProwOnwl() {
			return bLmArcProwOnwl;
		}
		@JsonProperty("b_lm_arc_prow_onwl")
		public void setbLmArcProwOnwl(String bLmArcProwOnwl) {
			this.bLmArcProwOnwl = bLmArcProwOnwl;
		}
		@JsonProperty("b_lm_arc_converter_charges_onrf")
		public String getbLmArcConverterChargesOnrf() {
			return bLmArcConverterChargesOnrf;
		}
		@JsonProperty("b_lm_arc_converter_charges_onrf")
		public void setbLmArcConverterChargesOnrf(String bLmArcConverterChargesOnrf) {
			this.bLmArcConverterChargesOnrf = bLmArcConverterChargesOnrf;
		}
		@JsonProperty("b_lm_arc_bw_backhaul_onrf")
		public String getbLmArcBwBackhaulOnrf() {
			return bLmArcBwBackhaulOnrf;
		}
		@JsonProperty("b_lm_arc_bw_backhaul_onrf")
		public void setbLmArcBwBackhaulOnrf(String bLmArcBwBackhaulOnrf) {
			this.bLmArcBwBackhaulOnrf = bLmArcBwBackhaulOnrf;
		}
		@JsonProperty("b_lm_arc_colocation_onrf")
		public String getbLmArcColocationOnrf() {
			return bLmArcColocationOnrf;
		}
		@JsonProperty("b_lm_arc_colocation_onrf")
		public void setbLmArcColocationOnrf(String bLmArcColocationOnrf) {
			this.bLmArcColocationOnrf = bLmArcColocationOnrf;
		}
		@JsonProperty("b_lm_otc_modem_charges_offwl")
		public String getbLmOtcModemChargesOffwl() {
			return bLmOtcModemChargesOffwl;
		}
		@JsonProperty("b_lm_otc_modem_charges_offwl")
		public void setbLmOtcModemChargesOffwl(String bLmOtcModemChargesOffwl) {
			this.bLmOtcModemChargesOffwl = bLmOtcModemChargesOffwl;
		}
		@JsonProperty("b_lm_otc_nrc_installation_offwl")
		public String getbLmOtcNrcInstallationOffwl() {
			return bLmOtcNrcInstallationOffwl;
		}
		@JsonProperty("b_lm_otc_nrc_installation_offwl")
		public void setbLmOtcNrcInstallationOffwl(String bLmOtcNrcInstallationOffwl) {
			this.bLmOtcNrcInstallationOffwl = bLmOtcNrcInstallationOffwl;
		}
		@JsonProperty("b_lm_arc_bw_offwl")
		public String getbLmArcBwOffwl() {
			return bLmArcBwOffwl;
		}
		@JsonProperty("b_lm_arc_bw_offwl")
		public void setbLmArcBwOffwl(String bLmArcBwOffwl) {
			this.bLmArcBwOffwl = bLmArcBwOffwl;
		}
		@JsonProperty("b_provider_name")
		public String getbProviderName() {
			return bProviderName;
		}
		@JsonProperty("b_provider_name")
		public void setbProviderName(String bProviderName) {
			this.bProviderName = bProviderName;
		}
		@JsonProperty("b_BHConnectivity")
		public String getbBHConnectivity() {
			return bBHConnectivity;
		}
		@JsonProperty("b_BHConnectivity")
		public void setbBHConnectivity(String bBHConnectivity) {
			this.bBHConnectivity = bBHConnectivity;
		}
		@JsonProperty("b_lm_arc_orderable_bw_onwl")
		public String getbLmArcOrderableBwOnwl() {
			return bLmArcOrderableBwOnwl;
		}
		@JsonProperty("b_lm_arc_orderable_bw_onwl")
		public void setbLmArcOrderableBwOnwl(String bLmArcOrderableBwOnwl) {
			this.bLmArcOrderableBwOnwl = bLmArcOrderableBwOnwl;
		}
		@JsonProperty("b_lm_otc_nrc_orderable_bw_onwl")
		public String getbLmOtcNrcOrderableBwOnwl() {
			return bLmOtcNrcOrderableBwOnwl;
		}
		@JsonProperty("b_lm_otc_nrc_orderable_bw_onwl")
		public void setbLmOtcNrcOrderableBwOnwl(String bLmOtcNrcOrderableBwOnwl) {
			this.bLmOtcNrcOrderableBwOnwl = bLmOtcNrcOrderableBwOnwl;
		}
		@JsonProperty("b_lm_nrc_network_capex_onwl")
		public String getbLmNrcNetworkCapexOnwl() {
			return bLmNrcNetworkCapexOnwl;
		}
		@JsonProperty("b_lm_nrc_network_capex_onwl")
		public void setbLmNrcNetworkCapexOnwl(String bLmNrcNetworkCapexOnwl) {
			this.bLmNrcNetworkCapexOnwl = bLmNrcNetworkCapexOnwl;
		}
		@JsonProperty("b_lm_arc_radwin_bw_onrf")
		public String getbLmArcRadwinBwOnrf() {
			return bLmArcRadwinBwOnrf;
		}
		@JsonProperty("b_lm_arc_radwin_bw_onrf")
		public void setbLmArcRadwinBwOnrf(String bLmArcRadwinBwOnrf) {
			this.bLmArcRadwinBwOnrf = bLmArcRadwinBwOnrf;
		}
		@JsonProperty("b_lm_arc_modem_charges_offwl")
		public String getbLmArcModemChargesOffwl() {
			return bLmArcModemChargesOffwl;
		}
		@JsonProperty("b_lm_arc_modem_charges_offwl")
		public void setbLmArcModemChargesOffwl(String bLmArcModemChargesOffwl) {
			this.bLmArcModemChargesOffwl = bLmArcModemChargesOffwl;
		}
		public String getOldLlBwRef() {
			return oldLlBwRef;
		}
		public void setOldLlBwRef(String oldLlBwRef) {
			this.oldLlBwRef = oldLlBwRef;
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
    
 // PIPF -143
 	@JsonProperty("a_prow_cost_type")
 	private String aProwCostType;

 	@JsonProperty("a_prow_message")
 	private String aProwMessage;
 	
 	@JsonProperty("a_prow_flag")
 	private String aProwFlag;
 	
 	@JsonProperty("a_lm_arc_prow_gpon_onwl")
 	private String aLmArcProwGponOnwl;
 	
 	@JsonProperty("a_lm_nrc_prow_gpon_onwl")
 	private String aLmNrcProwGponOnwl;
 	
 	@JsonProperty("a_lm_arc_prow_prow_onwl")
 	private String aLmArcProwProwOnwl;
 	
 	@JsonProperty("a_lm_nrc_prow_prow_onwl")
 	private String aLmNrcProwProwOnwl;
 	
 	@JsonProperty("b_prow_cost_type")
 	private String bProwCostType;
 	
 	@JsonProperty("b_prow_message")
 	private String bProwMessage;
 	
 	@JsonProperty("b_prow_flag")
 	private String bProwFlag;
 	
 	@JsonProperty("b_lm_arc_prow_gpon_onwl")
 	private String bLmArcProwGponOnwl;
 	
 	@JsonProperty("b_lm_nrc_prow_gpon_onwl")
 	private String bLmNrcProwGponOnwl;
 	
 	@JsonProperty("b_lm_arc_prow_prow_onwl")
 	private String bLmArcProwProwOnwl;
 	
 	@JsonProperty("b_lm_nrc_prow_prow_onwl")
 	private String bLmNrcProwProwOnwl;
 	
 	public String getaProwCostType() {
 		return aProwCostType;
 	}
 	public void setaProwCostType(String aProwCostType) {
 		this.aProwCostType = aProwCostType;
 	}
 	public String getaProwMessage() {
 		return aProwMessage;
 	}
 	public void setaProwMessage(String aProwMessage) {
 		this.aProwMessage = aProwMessage;
 	}
 	public String getaProwFlag() {
 		return aProwFlag;
 	}
 	public void setaProwFlag(String aProwFlag) {
 		this.aProwFlag = aProwFlag;
 	}
 	public String getaLmArcProwGponOnwl() {
 		return aLmArcProwGponOnwl;
 	}
 	public void setaLmArcProwGponOnwl(String aLmArcProwGponOnwl) {
 		this.aLmArcProwGponOnwl = aLmArcProwGponOnwl;
 	}
 	public String getaLmNrcProwGponOnwl() {
 		return aLmNrcProwGponOnwl;
 	}
 	public void setaLmNrcProwGponOnwl(String aLmNrcProwGponOnwl) {
 		this.aLmNrcProwGponOnwl = aLmNrcProwGponOnwl;
 	}
 	public String getaLmArcProwProwOnwl() {
 		return aLmArcProwProwOnwl;
 	}
 	public void setaLmArcProwProwOnwl(String aLmArcProwProwOnwl) {
 		this.aLmArcProwProwOnwl = aLmArcProwProwOnwl;
 	}
 	public String getaLmNrcProwProwOnwl() {
 		return aLmNrcProwProwOnwl;
 	}
 	public void setaLmNrcProwProwOnwl(String aLmNrcProwProwOnwl) {
 		this.aLmNrcProwProwOnwl = aLmNrcProwProwOnwl;
 	}
 	public String getbProwCostType() {
 		return bProwCostType;
 	}
 	public void setbProwCostType(String bProwCostType) {
 		this.bProwCostType = bProwCostType;
 	}
 	public String getbProwMessage() {
 		return bProwMessage;
 	}
 	public void setbProwMessage(String bProwMessage) {
 		this.bProwMessage = bProwMessage;
 	}
 	public String getbProwFlag() {
 		return bProwFlag;
 	}
 	public void setbProwFlag(String bProwFlag) {
 		this.bProwFlag = bProwFlag;
 	}
 	
 	public String getbLmArcProwGponOnwl() {
 		return bLmArcProwGponOnwl;
 	}
 	public void setbLmArcProwGponOnwl(String bLmArcProwGponOnwl) {
 		this.bLmArcProwGponOnwl = bLmArcProwGponOnwl;
 	}
 	
 	public String getbLmNrcProwGponOnwl() {
 		return bLmNrcProwGponOnwl;
 	}
 	public void setbLmNrcProwGponOnwl(String bLmNrcProwGponOnwl) {
 		this.bLmNrcProwGponOnwl = bLmNrcProwGponOnwl;
 	}

 	public String getbLmArcProwProwOnwl() {
 		return bLmArcProwProwOnwl;
 	}
 	public void setbLmArcProwProwOnwl(String bLmArcProwProwOnwl) {
 		this.bLmArcProwProwOnwl = bLmArcProwProwOnwl;
 	}

 	public String getbLmNrcProwProwOnwl() {
 		return bLmNrcProwProwOnwl;
 	}
 	public void setbLmNrcProwProwOnwl(String bLmNrcProwProwOnwl) {
 		this.bLmNrcProwProwOnwl = bLmNrcProwProwOnwl;
 	}
 	// PIPF 143 end
	public String getFeasibilityRemarks() {
		return feasibilityRemarks;
	}
	public void setFeasibilityRemarks(String feasibilityRemarks) {
		this.feasibilityRemarks = feasibilityRemarks;
	}
 	
}

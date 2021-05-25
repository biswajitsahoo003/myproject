package com.tcl.dias.oms.npl.pricing.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "Account.RTM_Cust",
    "BW_mbps_upd",
    "DistanceBetweenPOPs",
    "Final_list_price",
    "GVPN_ARC_per_BW",
    "ILL_ARC_per_BW",
    "Identifier",
    "Industry_Cust",
    "Inv_GVPN_bw",
    "Inv_ILL_bw",
    "Inv_NPL_bw",
    "Inv_Other_bw",
    "Inv_Tot_BW",
    "Last_Mile_Cost_ARC",
    "Last_Mile_Cost_NRC",
    "Last_Modified_Date",
    "NPL_ARC_per_BW",
    "NPL_Port_ARC_Adjusted",
    "NPL_Port_MRC_Adjusted",
    "NPL_Port_NRC_Adjusted",
    "OpportunityID_Prod_Identifier",
    "Other_ARC_per_BW",
    "Segment_Cust",
    "Sum_CAT_1_2_MACD_FLAG",
    "Sum_CAT_1_2_New_Opportunity_FLAG",
    "Sum_CAT_3_MACD_FLAG",
    "Sum_CAT_3_New_Opportunity_FLAG",
    "Sum_CAT_4_MACD_FLAG",
    "Sum_CAT_4_New_Opportunity_FLAG",
    "Sum_Cat_1_2_opp",
    "Sum_GVPN_Flag",
    "Sum_IAS_FLAG",
    "Sum_MACD_Opportunity",
    "Sum_NPL_Flag",
    "Sum_New_ARC_Converted",
    "Sum_New_ARC_Converted_GVPN",
    "Sum_New_ARC_Converted_ILL",
    "Sum_New_ARC_Converted_NPL",
    "Sum_New_ARC_Converted_Other",
    "Sum_New_Opportunity",
    "Sum_Other_Flag",
    "Sum_tot_oppy_historic_opp",
    "Sum_tot_oppy_historic_prod",
    "TOT_ARC_per_BW",
    "a_latitude_final",
    "a_local_loop_interface",
    "a_longitude_final",
    "a_resp_city",
    "account_id_with_18_digit",
    "b_latitude_final",
    "b_local_loop_interface",
    "b_longitude_final",
    "b_resp_city",
    "bw_mbps",
    "chargeable_distance",
    "createdDate_quote",
    "datediff",
    "dist_betw_pops",
    "error_code",
    "error_flag",
    "error_msg",
    "f_a_lm_arc_bw_onwl",
    "f_a_lm_nrc_bw_onwl",
    "f_a_lm_nrc_inbldg_onwl",
    "f_a_lm_nrc_mux_onwl",
    "f_a_lm_nrc_nerental_onwl",
    "f_a_lm_nrc_ospcapex_onwl",
    "f_b_lm_arc_bw_onwl",
    "f_b_lm_nrc_bw_onwl",
    "f_b_lm_nrc_inbldg_onwl",
    "f_b_lm_nrc_mux_onwl",
    "f_b_lm_nrc_nerental_onwl",
    "f_b_lm_nrc_ospcapex_onwl",
    "feasibility_response_created_date",
    "hist_flag",
    "intra_inter_flag",
    "link_id",
    "list_price_mb",
    "model_name_transform",
    "opportunity_day",
    "opportunity_month",
    "opportunity_term",
    "p_a_lm_arc_bw_onwl",
    "p_a_lm_nrc_bw_onwl",
    "p_a_lm_nrc_inbldg_onwl",
    "p_a_lm_nrc_mux_onwl",
    "p_a_lm_nrc_nerental_onwl",
    "p_a_lm_nrc_ospcapex_onwl",
    "p_b_lm_arc_bw_onwl",
    "p_b_lm_nrc_bw_onwl",
    "p_b_lm_nrc_inbldg_onwl",
    "p_b_lm_nrc_mux_onwl",
    "p_b_lm_nrc_nerental_onwl",
    "p_b_lm_nrc_ospcapex_onwl",
    "port_lm_arc",
    "port_pred_discount",
    "predicted_NPL_Port_ARC",
    "predicted_net_price",
    "product_flavor_transform",
    "product_name",
    "prospect_name",
    "quoteType_quote",
    "quotetype_quote",
    "site_id",
    "sla_varient",
    "sum_cat1_2_Opportunity",
    "sum_cat_3_Opportunity",
    "sum_cat_4_Opportunity",
    "sum_model_name",
    "time_taken",
    "total_contract_value",
    "total_osp_capex",
    "rate_card_flag",
    "api_version_no",
    "version",
    "shift_charge",
    //added for workbench
    "sp_a_lm_arc_bw_backhaul_onrf",
    "sp_a_lm_arc_bw_offwl",
    "sp_a_lm_arc_bw_onrf",
    "sp_a_lm_arc_bw_onwl",
    "sp_a_lm_arc_colocation_charges_onrf",
    "sp_a_lm_arc_colocation_onrf",
    "sp_a_lm_arc_converter_charges_onrf",
    "sp_a_lm_arc_modem_charges_offwl", 
    "sp_a_lm_arc_offwl",
    "sp_a_lm_arc_orderable_bw_onwl",
    "sp_a_lm_arc_prow_onwl",
    "sp_a_lm_arc_radwin_bw_onrf", 
    "sp_a_lm_nrc_bw_onrf",
    "sp_a_lm_nrc_bw_onwl", 
    "sp_a_lm_nrc_bw_prov_ofrf",
    "sp_a_lm_nrc_inbldg_onwl", 
    "sp_a_lm_nrc_mast_ofrf",
    "sp_a_lm_nrc_mast_onrf", 
    "sp_a_lm_nrc_mux_onwl", 
    "sp_a_lm_nrc_nerental_onwl", 
    "sp_a_lm_nrc_network_capex_onwl",
    "sp_a_lm_nrc_ospcapex_onwl",
    "sp_a_lm_nrc_prow_onwl",
    "sp_a_lm_otc_modem_charges_offwl",
    "sp_a_lm_otc_nrc_installation_offwl", 
    "sp_a_lm_otc_nrc_orderable_bw_onwl",
    
    "sp_additional_IP_ARC",
    
    
    "sp_b_lm_arc_bw_backhaul_onrf",
    "sp_b_lm_arc_bw_offwl", 
    "sp_b_lm_arc_bw_onrf",
    "sp_b_lm_arc_bw_onwl",
    "sp_b_lm_arc_colocation_charges_onrf",
    "sp_b_lm_arc_colocation_onrf", 
    "sp_b_lm_arc_converter_charges_onrf",
    "sp_b_lm_arc_modem_charges_offwl",
    "sp_b_lm_arc_offwl",
    "sp_b_lm_arc_orderable_bw_onwl", 
    "sp_b_lm_arc_prow_onwl",
    "sp_b_lm_arc_radwin_bw_onrf",
    "sp_b_lm_nrc_bw_onrf",
    "sp_b_lm_nrc_bw_onwl",
    "sp_b_lm_nrc_bw_prov_ofrf",
    "sp_b_lm_nrc_inbldg_onwl", 
    "sp_b_lm_nrc_mast_ofrf", 
    "sp_b_lm_nrc_mast_onrf", 
    "sp_b_lm_nrc_mux_onwl", 
    "sp_b_lm_nrc_nerental_onwl",
    "sp_b_lm_nrc_network_capex_onwl",
    "sp_b_lm_nrc_ospcapex_onwl",
    "sp_b_lm_nrc_prow_onwl",
    "sp_b_lm_otc_modem_charges_offwl",
    "sp_b_lm_otc_nrc_installation_offwl",
    "sp_b_lm_otc_nrc_orderable_bw_onwl",
    "sp_burst_per_MB_price_ARC",
    "sp_lm_arc_prow_onwl",
    "sp_lm_nrc_ospcapex_onwl",
    "sp_lm_nrc_prow_onwl",
    
    "sp_port_arc",
    "sp_port_nrc",
    "sp_a_lm_arc_bw_prov_ofrf",
    "sp_b_lm_arc_bw_prov_ofrf",
    "a_Orch_Connection",
    "b_Orch_Connection",
    "a_Orch_LM_Type",
    "b_Orch_LM_Type",
    "a_Orch_Category",
    "b_Orch_Category",
    "a_local_loop_bw",
	"b_local_loop_bw",
	
	// added for nde dis
	
	"lm_arc_bw_onwl"  ,
	"lm_nrc_bw_onwl"  ,
	"lm_arc_prow_onwl"  ,
	"lm_nrc_inbldg_onwl"  ,
	"lm_nrc_mux_onwl"  ,
	"lm_nrc_nerental_onwl"  ,
	"lm_nrc_ospcapex_onwl"  ,
	"lm_nrc_prow_onwl"  ,

	"lm_arc_bw_backhaul_onrf" ,
	"lm_arc_bw_onrf" ,
	"lm_arc_colocation_charges_onrf" ,
	"lm_arc_converter_charges_onrf",
	"lm_nrc_bw_onrf" ,
	"lm_nrc_mast_onrf",

	"lm_arc_bw_offwl",
	"lm_arc_modem_charges_offwl",
	"lm_otc_modem_charges_offwl",
	"lm_otc_nrc_installation_offwl",

	"lm_nrc_bw_prov_ofrf",
	"lm_arc_bw_prov_ofrf",
	"lm_nrc_mast_ofrf",
	"a_bandwidth",
	"b_bandwidth",
	"Port_ARC_LP",
	"Port_NRC_LP",
	 "rcFlag",
	 "Port_LP",
     "CPE_Rental_MRC"
    
    
})
public class Result {

    @JsonProperty("Account.RTM_Cust")
    private String accountRTMCust;
    @JsonProperty("BW_mbps_upd")
    private String bWMbpsUpd;
    @JsonProperty("DistanceBetweenPOPs")
    private String distanceBetweenPOPs;
    @JsonProperty("Final_list_price")
    private String finalListPrice;
    @JsonProperty("GVPN_ARC_per_BW")
    private String gVPNARCPerBW;
    @JsonProperty("ILL_ARC_per_BW")
    private String iLLARCPerBW;
    @JsonProperty("Identifier")
    private String identifier;
    @JsonProperty("Industry_Cust")
    private String industryCust;
    @JsonProperty("Inv_GVPN_bw")
    private String invGVPNBw;
    @JsonProperty("Inv_ILL_bw")
    private String invILLBw;
    @JsonProperty("Inv_NPL_bw")
    private String invNPLBw;
    @JsonProperty("Inv_Other_bw")
    private String invOtherBw;
    @JsonProperty("Inv_Tot_BW")
    private String invTotBW;
    @JsonProperty("Last_Mile_Cost_ARC")
    private String lastMileCostARC;
    @JsonProperty("Last_Mile_Cost_NRC")
    private String lastMileCostNRC;
    @JsonProperty("Last_Modified_Date")
    private String lastModifiedDate;
    @JsonProperty("NPL_ARC_per_BW")
    private String nPLARCPerBW;
    @JsonProperty("NPL_Port_ARC_Adjusted")
    private String nPLPortARCAdjusted;
    @JsonProperty("NPL_Port_MRC_Adjusted")
    private String nPLPortMRCAdjusted;
    @JsonProperty("NPL_Port_NRC_Adjusted")
    private String nPLPortNRCAdjusted;
    @JsonProperty("OpportunityID_Prod_Identifier")
    private String opportunityIDProdIdentifier;
    @JsonProperty("Other_ARC_per_BW")
    private String otherARCPerBW;
    @JsonProperty("Segment_Cust")
    private String segmentCust;
    @JsonProperty("Sum_CAT_1_2_MACD_FLAG")
    private String sumCAT12MACDFLAG;
    @JsonProperty("Sum_CAT_1_2_New_Opportunity_FLAG")
    private String sumCAT12NewOpportunityFLAG;
    @JsonProperty("Sum_CAT_3_MACD_FLAG")
    private String sumCAT3MACDFLAG;
    @JsonProperty("Sum_CAT_3_New_Opportunity_FLAG")
    private String sumCAT3NewOpportunityFLAG;
    @JsonProperty("Sum_CAT_4_MACD_FLAG")
    private String sumCAT4MACDFLAG;
    @JsonProperty("Sum_CAT_4_New_Opportunity_FLAG")
    private String sumCAT4NewOpportunityFLAG;
    @JsonProperty("Sum_Cat_1_2_opp")
    private String sumCat12Opp;
    @JsonProperty("Sum_GVPN_Flag")
    private String sumGVPNFlag;
    @JsonProperty("Sum_IAS_FLAG")
    private String sumIASFLAG;
    @JsonProperty("Sum_MACD_Opportunity")
    private String sumMACDOpportunity;
    @JsonProperty("Sum_NPL_Flag")
    private String sumNPLFlag;
    @JsonProperty("Sum_New_ARC_Converted")
    private String sumNewARCConverted;
    @JsonProperty("Sum_New_ARC_Converted_GVPN")
    private String sumNewARCConvertedGVPN;
    @JsonProperty("Sum_New_ARC_Converted_ILL")
    private String sumNewARCConvertedILL;
    @JsonProperty("Sum_New_ARC_Converted_NPL")
    private String sumNewARCConvertedNPL;
    @JsonProperty("Sum_New_ARC_Converted_Other")
    private String sumNewARCConvertedOther;
    @JsonProperty("Sum_New_Opportunity")
    private String sumNewOpportunity;
    @JsonProperty("Sum_Other_Flag")
    private String sumOtherFlag;
    @JsonProperty("Sum_tot_oppy_historic_opp")
    private String sumTotOppyHistoricOpp;
    @JsonProperty("Sum_tot_oppy_historic_prod")
    private String sumTotOppyHistoricProd;
    @JsonProperty("TOT_ARC_per_BW")
    private String tOTARCPerBW;
    @JsonProperty("a_latitude_final")
    private String aLatitudeFinal;
    @JsonProperty("a_local_loop_interface")
    private String aLocalLoopInterface;
    @JsonProperty("a_longitude_final")
    private String aLongitudeFinal;
    @JsonProperty("a_resp_city")
    private String aRespCity;
    @JsonProperty("account_id_with_18_digit")
    private String accountIdWith18Digit;
    @JsonProperty("b_latitude_final")
    private String bLatitudeFinal;
    @JsonProperty("b_local_loop_interface")
    private String bLocalLoopInterface;
    @JsonProperty("b_longitude_final")
    private String bLongitudeFinal;
    @JsonProperty("b_resp_city")
    private String bRespCity;
    @JsonProperty("bw_mbps")
    private String bwMbps;
    @JsonProperty("chargeable_distance")
    private String chargeableDistance;
    @JsonProperty("createdDate_quote")
    private String createdDateQuote;
    @JsonProperty("datediff")
    private String datediff;
    @JsonProperty("dist_betw_pops")
    private String distBetwPops;
    @JsonProperty("error_code")
    private String errorCode;
    @JsonProperty("error_flag")
    private String errorFlag;
    @JsonProperty("error_msg")
    private String errorMsg;
    @JsonProperty("f_a_lm_arc_bw_onwl")
    private String fALmArcBwOnwl;
    @JsonProperty("f_a_lm_nrc_bw_onwl")
    private String fALmNrcBwOnwl;
    @JsonProperty("f_a_lm_nrc_inbldg_onwl")
    private String fALmNrcInbldgOnwl;
    @JsonProperty("f_a_lm_nrc_mux_onwl")
    private String fALmNrcMuxOnwl;
    @JsonProperty("f_a_lm_nrc_nerental_onwl")
    private String fALmNrcNerentalOnwl;
    @JsonProperty("f_a_lm_nrc_ospcapex_onwl")
    private String fALmNrcOspcapexOnwl;
    @JsonProperty("f_b_lm_arc_bw_onwl")
    private String fBLmArcBwOnwl;
    @JsonProperty("f_b_lm_nrc_bw_onwl")
    private String fBLmNrcBwOnwl;
    @JsonProperty("f_b_lm_nrc_inbldg_onwl")
    private String fBLmNrcInbldgOnwl;
    @JsonProperty("f_b_lm_nrc_mux_onwl")
    private String fBLmNrcMuxOnwl;
    @JsonProperty("f_b_lm_nrc_nerental_onwl")
    private String fBLmNrcNerentalOnwl;
    @JsonProperty("f_b_lm_nrc_ospcapex_onwl")
    private String fBLmNrcOspcapexOnwl;
    @JsonProperty("feasibility_response_created_date")
    private String feasibilityResponseCreatedDate;
    @JsonProperty("hist_flag")
    private String histFlag;
    @JsonProperty("intra_inter_flag")
    private String intraInterFlag;
    @JsonProperty("link_id")
    private String linkId;
    @JsonProperty("list_price_mb")
    private String listPriceMb;
    @JsonProperty("model_name_transform")
    private String modelNameTransform;
    @JsonProperty("opportunity_day")
    private String opportunityDay;
    @JsonProperty("opportunity_month")
    private String opportunityMonth;
    @JsonProperty("opportunity_term")
    private String opportunityTerm;
    @JsonProperty("p_a_lm_arc_bw_onwl")
    private String pALmArcBwOnwl;
    @JsonProperty("p_a_lm_nrc_bw_onwl")
    private String pALmNrcBwOnwl;
    @JsonProperty("p_a_lm_nrc_inbldg_onwl")
    private String pALmNrcInbldgOnwl;
    @JsonProperty("p_a_lm_nrc_mux_onwl")
    private String pALmNrcMuxOnwl;
    @JsonProperty("p_a_lm_nrc_nerental_onwl")
    private String pALmNrcNerentalOnwl;
    @JsonProperty("p_a_lm_nrc_ospcapex_onwl")
    private String pALmNrcOspcapexOnwl;
    @JsonProperty("p_b_lm_arc_bw_onwl")
    private String pBLmArcBwOnwl;
    @JsonProperty("p_b_lm_nrc_bw_onwl")
    private String pBLmNrcBwOnwl;
    @JsonProperty("p_b_lm_nrc_inbldg_onwl")
    private String pBLmNrcInbldgOnwl;
    @JsonProperty("p_b_lm_nrc_mux_onwl")
    private String pBLmNrcMuxOnwl;
    @JsonProperty("p_b_lm_nrc_nerental_onwl")
    private String pBLmNrcNerentalOnwl;
    @JsonProperty("p_b_lm_nrc_ospcapex_onwl")
    private String pBLmNrcOspcapexOnwl;
    @JsonProperty("port_lm_arc")
    private String portLmArc;
    @JsonProperty("port_pred_discount")
    private String portPredDiscount;
    @JsonProperty("predicted_NPL_Port_ARC")
    private String predictedNPLPortARC;
    @JsonProperty("predicted_net_price")
    private String predictedNetPrice;
    @JsonProperty("product_flavor_transform")
    private String productFlavorTransform;
    @JsonProperty("product_name")
    private String productName;
    @JsonProperty("prospect_name")
    private String prospectName;
    @JsonProperty("quoteType_quote")
    private String quoteTypeQuote;
    @JsonProperty("quotetype_quote")
    private String quotetypeQuote;
    @JsonProperty("site_id")
    private String siteId;
    @JsonProperty("sla_varient")
    private String slaVarient;
    @JsonProperty("sum_cat1_2_Opportunity")
    private String sumCat12Opportunity;
    @JsonProperty("sum_cat_3_Opportunity")
    private String sumCat3Opportunity;
    @JsonProperty("sum_cat_4_Opportunity")
    private String sumCat4Opportunity;
    @JsonProperty("sum_model_name")
    private String sumModelName;
    @JsonProperty("time_taken")
    private String timeTaken;
    @JsonProperty("total_contract_value")
    private String totalContractValue;
    @JsonProperty("total_osp_capex")
    private String totalOspCapex;
    @JsonProperty("NPL_ARC_Mgmnt_charges")
    private String nplArcMgmntCharges;
    @JsonProperty("NPL_MRC_Mgmnt_charges")
    private String nplMrcMgmntCharges;
    
    @JsonProperty("rate_card_flag")
    private String rateCardFlag;
    @JsonProperty("api_version_no")
    private String apiVersionNo;
    @JsonProperty("version")
	private String version;
    @JsonProperty("Bucket_Adjustment_Type")
    private String bucketAdjustmentType;
    
    @JsonProperty("shift_charge")
    private String shiftCharge;
    
    
    //added wb
    
    @JsonProperty("sp_a_lm_arc_bw_backhaul_onrf")
    private String spALmArcBwBackhaulOnrf;
    
    @JsonProperty("sp_a_lm_arc_bw_offwl")
    private String spALmArcBwOffwl;
    
    @JsonProperty("sp_a_lm_arc_bw_onrf")
    private String spALmArcBwOnrf;
    
    @JsonProperty("sp_a_lm_arc_bw_onwl")
    private String spALmArcBwOnwl;
    
    @JsonProperty("sp_a_lm_arc_colocation_charges_onrf")
    private String spALmArcColocationChargesOnrf;
    
    @JsonProperty("sp_a_lm_arc_colocation_onrf")
    private String spALmArcColocationOnrf;
    
    @JsonProperty("sp_a_lm_arc_converter_charges_onrf")
    private String spALmArcConverterChargesOnrf;
    
    @JsonProperty("sp_a_lm_arc_modem_charges_offwl")
    private String spALmArcModemChargesOffwl;
    
    @JsonProperty("sp_a_lm_arc_offwl")
    private String spALmArcOffwl;
    
    @JsonProperty("sp_a_lm_arc_orderable_bw_onwl")
    private String spaALmArcOrderableBwOnwl;
    
    
    
    @JsonProperty("sp_a_lm_arc_prow_onwl")
    private String spALmArcProwOnwl;
    
    @JsonProperty("sp_a_lm_arc_radwin_bw_onrf")
    private String spALmArcRadwinBwOnrf;
    
    @JsonProperty("sp_a_lm_nrc_bw_onrf")
    private String spALmNrcBwOnrf;
    
    @JsonProperty("sp_a_lm_nrc_bw_onwl")
    private String spALmNrcBwOnwl;
    
    @JsonProperty("sp_a_lm_nrc_bw_prov_ofrf")
    private String spALmNrcBwProvOfrf;
    
    @JsonProperty("sp_a_lm_nrc_inbldg_onwl")
    private String spALmNrcInbldgOnwl;
    
    @JsonProperty("sp_a_lm_nrc_mast_ofrf")
    private String spALmNrcMastOfrf;
    
    @JsonProperty("sp_a_lm_nrc_mast_onrf")
    private String spALmNrcMastOnrf;
    
    @JsonProperty("sp_a_lm_nrc_mux_onwl")
    private String spALmNrcMuxOnwl;
      
    @JsonProperty("sp_a_lm_nrc_nerental_onwl")
    private String spALmNrcNerentalOnwl;
    
    @JsonProperty("sp_a_lm_nrc_network_capex_onwl")
    private String spALmNrcNetworkCapexOnwl;
       
    @JsonProperty("sp_a_lm_nrc_ospcapex_onwl")
    private String spALmNrcOspcapexOnwl;
       
    @JsonProperty("sp_a_lm_nrc_prow_onwl")
    private String spALmNrcProwOnwl;
       
    @JsonProperty("sp_a_lm_otc_modem_charges_offwl")
    private String spALmOtcModemChargesOffwl; 
    
    @JsonProperty("sp_a_lm_otc_nrc_installation_offwl")
    private String spALmOtcNrcInstallationOffwl;
    
    @JsonProperty("sp_a_lm_otc_nrc_orderable_bw_onwl")
    private String spALmOtcNrcOrderableBwOnwl;
     
	/*
	 * @JsonProperty("sp_additional_IP_ARC") private String sp_additional_IP_ARC;
	 */
    //B END
    @JsonProperty("sp_b_lm_arc_bw_backhaul_onrf")
    private String spBLmArcBwBackhaulOnrf;
    
    @JsonProperty("sp_b_lm_arc_bw_offwl")
    private String spBLmArcBwOffwl;
    
    @JsonProperty("sp_b_lm_arc_bw_onrf")
    private String spBLmArcBwOnrf;
       
    @JsonProperty("sp_b_lm_arc_bw_onwl")
    private String spBLmArcBwOnwl;
    
    @JsonProperty("sp_b_lm_arc_colocation_charges_onrf")
    private String spBLmArcColocationChargesOnrf;
    
    @JsonProperty("sp_b_lm_arc_colocation_onrf")
    private String spBLmArcColocationOnrf;
    
    @JsonProperty("sp_b_lm_arc_converter_charges_onrf")
    private String spBLmArcConverterChargesOnrf;
    
    @JsonProperty("sp_b_lm_arc_modem_charges_offwl")
    private String spBLmArcModemChargesOffwl;
    
    @JsonProperty("sp_b_lm_arc_offwl")
    private String spBLmArcOffwl;
    
    @JsonProperty("sp_b_lm_arc_orderable_bw_onwl")
    private String spBLmArcOrderableBwOnwl;
    
    
    
    @JsonProperty("sp_b_lm_arc_prow_onwl")
    private String spBLmArcProwOnwl;
    
    @JsonProperty("sp_b_lm_arc_radwin_bw_onrf")
    private String spBLmArcRadwinBwOnrf;
    
    @JsonProperty("sp_b_lm_nrc_bw_onrf")
    private String spBLmNrcBwOnrf;
    
    @JsonProperty("sp_b_lm_nrc_bw_onwl")
    private String spBLmNrcBwOnwl;
    
    @JsonProperty("sp_b_lm_nrc_bw_prov_ofrf")
    private String spBLmNrcBwProvOfrf;
    
    @JsonProperty("sp_b_lm_nrc_inbldg_onwl")
    private String spBLmNrcInbldgOnwl;
    
    @JsonProperty("sp_b_lm_nrc_mast_ofrf")
    private String spBLmNrcMastOfrf;
    
    @JsonProperty("sp_b_lm_nrc_mast_onrf")
    private String spBLmNrcMastOnrf;
    
    @JsonProperty("sp_b_lm_nrc_mux_onwl")
    private String spBLmNrcMuxOnwl;
    
    @JsonProperty("sp_b_lm_nrc_nerental_onwl")
    private String spBLmNrcNerentalOnwl;
    
    @JsonProperty("sp_b_lm_nrc_network_capex_onwl")
    private String spBLmNrcNetworkCapexOnwl;
    
    @JsonProperty("sp_b_lm_nrc_ospcapex_onwl")
    private String spBLmNrcOspcapexOnwl;
    
    @JsonProperty("sp_b_lm_nrc_prow_onwl")
    private String spBLmNrcProwOnwl;
    
    @JsonProperty("sp_b_lm_otc_modem_charges_offwl")
    private String spBLmOtcModemChargesOffwl;
    
    @JsonProperty("sp_b_lm_otc_nrc_installation_offwl")
    private String spBLmOtcNrcInstallationOffwl;
    
    @JsonProperty("sp_b_lm_otc_nrc_orderable_bw_onwl")
    private String spBLmOtcNrcOrderableBwOnwl;
    
	/*
	 * @JsonProperty("sp_burst_per_MB_price_ARC") private String
	 * sp_burst_per_MB_price_ARC;
	 */
    
	/*
	 * @JsonProperty("sp_lm_arc_prow_onwl") private String spLmArcProwOnwl;
	 * 
	 * @JsonProperty("sp_lm_nrc_ospcapex_onwl") private String spLmNrcOspcapexOnwl;
	 * 
	 * @JsonProperty("sp_lm_nrc_prow_onwl") private String spLmNrcProwOnwl;
	 */
    
    @JsonProperty("sp_port_arc")
    private String spPortArc;
    
    @JsonProperty("sp_port_nrc")
    private String spPortNrc;
    
    
    
    @JsonProperty("sp_a_lm_arc_bw_prov_ofrf")
    private String spALmArcBwProvOfrf;
    
    @JsonProperty("sp_b_lm_arc_bw_prov_ofrf")
    private String spBLmArcBwProvOfrf;
    
    
    @JsonProperty("a_Orch_Connection")
	private String aOrchConnection;

	@JsonProperty("b_Orch_Connection")
	private String bOrchConnection;
	
	
	@JsonProperty("a_Orch_LM_Type")
	private String aOrchLMType;
	
	@JsonProperty("b_Orch_LM_Type")
	private String bOrchLMType;
	
	@JsonProperty("a_Orch_Category")
	private String aOrchCategory;
	
	
	@JsonProperty("b_Orch_Category")
	private String bOrchCategory;
	
	
	@JsonProperty("a_local_loop_bw")
	private Double aLocalLoopBw;
	
	@JsonProperty("b_local_loop_bw")
	private Double bLocalLoopBw;
	
	//added for nde dis
	 @JsonProperty("lm_arc_bw_onwl")
	    private String lmArcBwOnwl;
	    @JsonProperty("lm_nrc_bw_onwl")
	    private String lmNrcBwOnwl;
	    @JsonProperty("lm_arc_prow_onwl")
	    private String lmArcProwOnwl;
	    @JsonProperty("lm_nrc_inbldg_onwl")
	    private String lmNrcInbldgOnwl;
	    @JsonProperty("lm_nrc_mux_onwl")
	    private String lmNrcMuxOnwl;
	    @JsonProperty("lm_nrc_nerental_onwl")
	    private String lmNrcNerentalOnwl;
	    @JsonProperty("lm_nrc_ospcapex_onwl")
	    private String lmNrcOspcapexOnwl;
	    @JsonProperty("lm_nrc_prow_onwl")
	    private String lmNrcProwOnwl;
	    @JsonProperty("lm_arc_bw_backhaul_onrf")
	    private String lmArcBwBackhaulOnrf;
	    @JsonProperty("lm_arc_bw_onrf")
	    private String lmArcBwOnrf;
	    @JsonProperty("lm_arc_colocation_charges_onrf")
	    private String lmArcColocationChargesOnrf;
	    @JsonProperty("lm_arc_converter_charges_onrf")
	    private String lmArcConverterChargesOnrf;
	    @JsonProperty("lm_nrc_bw_onrf")
	    private String lmNrcBwOnrf;
	    @JsonProperty("lm_nrc_mast_onrf")
	    private String lmNrcMastOnrf;
	    @JsonProperty("lm_arc_bw_offwl")
	    private String lmArcBwOffwl;
	    @JsonProperty("lm_arc_modem_charges_offwl")
	    private String lmArcModemChargesOffwl;
	    @JsonProperty("lm_otc_modem_charges_offwl")
	    private String lmOtcModemChargesOffwl;
	    @JsonProperty("lm_otc_nrc_installation_offwl")
	    private String lmOtcNrcInstallationOffwl;
	    @JsonProperty("lm_nrc_bw_prov_ofrf")
	    private String lmNrcBwProvOfrf;
	    @JsonProperty("lm_arc_bw_prov_ofrf")
	    private String lmArcBwProvOfrf;
	    @JsonProperty("lm_nrc_mast_ofrf")
	    private String lmNrcMastOfrf;
	    
	    @JsonProperty("a_bandwidth")
		private String abandwidth;
	    
	    @JsonProperty("b_bandwidth")
		private String bbandwidth;
	    
	    @JsonProperty("Port_ARC_LP")
		private String PortARCLP;
	    
	    
	    @JsonProperty("Port_NRC_LP")
		private String PortNRCLP;
	    
	    @JsonProperty("rc_flag")
		private String rcFlag;
	    
	    @JsonProperty("Port_LP")
		private String PortLP;
		
		@JsonProperty("CPE_Rental_MRC")
		private String CPERentalMRC;
		
		@JsonProperty("a_lm_nrc_bw_onwl")
		private String aLmNrcBwOnwl;
		
		@JsonProperty("a_lm_nrc_bw_onrf")
		private String aLmNrcBwOnrf;
		
		@JsonProperty("a_lm_nrc_bw_prov_ofrf")
		private String aLmNrcBwProvOfrf;
		
		@JsonProperty("b_lm_nrc_bw_onwl")
		private String bLmNrcBwOnwl;
		
		@JsonProperty("b_lm_nrc_bw_onrf")
		private String bLmNrcBwOnrf;
		
		@JsonProperty("b_lm_nrc_bw_prov_ofrf")
		private String bLmNrcBwProvOfrf;
		
		@JsonProperty("a_lm_arc_bw_onwl")
		private String aLmArcBwOnwl;
		
		@JsonProperty("a_lm_arc_bw_onrf")
		private String aLmArcBwOnrf;
		
		@JsonProperty("a_lm_arc_bw_prov_ofrf")
		private String aLmArcBwProvOfrf;
		
		@JsonProperty("b_lm_arc_bw_onwl")
		private String bLmArcBwOnwl;
		
		@JsonProperty("b_lm_arc_bw_onrf")
		private String bLmArcBwOnrf;
		
		@JsonProperty("b_lm_arc_bw_prov_ofrf")
		private String bLmArcBwProvOfrf;

		@JsonProperty("a_lm_nrc_mast_onrf")
		private String aLmNrcMastOnrf;
		 
		@JsonProperty("a_lm_nrc_mast_ofrf")
		private String aLmNrcMastOfrf;
		
		@JsonProperty("p_a_lm_nrc_mast_onrf")
		private String pALmNrcMastOnrf;
			 
		@JsonProperty("p_a_lm_nrc_mast_ofrf")
		private String pALmNrcMastOfrf;
		
		@JsonProperty("f_a_lm_nrc_bw_ofrf")
		private String fALmNrcBwProvOfrf;

		@JsonProperty("f_a_lm_nrc_bw_onrf")
		private String fALmNrcBwOnrf;
		
		@JsonProperty("f_b_lm_nrc_bw_prov_ofrf")
		private String fBLmNrcBwProvOfrf;
				
		@JsonProperty("f_a_lm_arc_bw_onrf")
		private String fALmArcBwOnrf;
		
		@JsonProperty("f_a_lm_arc_bw_prov_ofrf")
		private String fALmArcBwProvOfrf;

		@JsonProperty("f_b_lm_arc_bw_onrf")
		private String fBLmArcBwOnrf;
		
		@JsonProperty("f_b_lm_arc_bw_prov_ofrf")
		private String fBLmArcBwProvOfrf;
		
		@JsonProperty("f_a_lm_nrc_mast_onrf")
		private String fALmNrcMastOnrf;
		
		@JsonProperty("f_a_lm_nrc_mast_ofrf")
		private String fALmNrcMastOfrf;
		
		@JsonProperty("p_b_lm_arc_bw_onrf")
		private String pBLmArcBwOnrf;
		
		@JsonProperty("p_a_lm_arc_bw_onrf")
		private String pALmArcBwOnrf;

		@JsonProperty("p_a_lm_arc_bw_Prov_ofrf")
		private String pALmArcBwProvOfrf;
		
		@JsonProperty("p_b_lm_arc_bw_Prov_ofrf")
		private String pBLmArcBwProvOfrf;
		
		@JsonProperty("p_b_lm_nrc_bw_onrf")
		private String pBLmNrcBwOnrf;
		
		@JsonProperty("p_a_lm_nrc_bw_onrf")
		private String pALmNrcBwOnrf;

		@JsonProperty("p_a_lm_nrc_bw_prov_ofrf")
		private String pALmNrcBwProvOfrf;
		
		@JsonProperty("p_b_lm_nrc_bw_prov_ofrf")
		private String pBLmNrcBwProvOfrf;
		
		
		
		public String getpBLmNrcBwOnrf() {
			return pBLmNrcBwOnrf;
		}

		public void setpBLmNrcBwOnrf(String pBLmNrcBwOnrf) {
			this.pBLmNrcBwOnrf = pBLmNrcBwOnrf;
		}

		public String getpALmNrcBwOnrf() {
			return pALmNrcBwOnrf;
		}

		public void setpALmNrcBwOnrf(String pALmNrcBwOnrf) {
			this.pALmNrcBwOnrf = pALmNrcBwOnrf;
		}

		public String getpALmNrcBwProvOfrf() {
			return pALmNrcBwProvOfrf;
		}

		public void setpALmNrcBwProvOfrf(String pALmNrcBwProvOfrf) {
			this.pALmNrcBwProvOfrf = pALmNrcBwProvOfrf;
		}

		public String getpBLmNrcBwProvOfrf() {
			return pBLmNrcBwProvOfrf;
		}

		public void setpBLmNrcBwProvOfrf(String pBLmNrcBwProvOfrf) {
			this.pBLmNrcBwProvOfrf = pBLmNrcBwProvOfrf;
		}

		public String getpBLmArcBwOnrf() {
			return pBLmArcBwOnrf;
		}

		public void setpBLmArcBwOnrf(String pBLmArcBwOnrf) {
			this.pBLmArcBwOnrf = pBLmArcBwOnrf;
		}

		public String getpALmArcBwOnrf() {
			return pALmArcBwOnrf;
		}

		public void setpALmArcBwOnrf(String pALmArcBwOnrf) {
			this.pALmArcBwOnrf = pALmArcBwOnrf;
		}

		public String getpALmArcBwProvOfrf() {
			return pALmArcBwProvOfrf;
		}

		public void setpALmArcBwProvOfrf(String pALmArcBwProvOfrf) {
			this.pALmArcBwProvOfrf = pALmArcBwProvOfrf;
		}

		public String getpBLmArcBwProvOfrf() {
			return pBLmArcBwProvOfrf;
		}

		public void setpBLmArcBwProvOfrf(String pBLmArcBwProvOfrf) {
			this.pBLmArcBwProvOfrf = pBLmArcBwProvOfrf;
		}

		public String getfALmNrcMastOnrf() {
			return fALmNrcMastOnrf;
		}

		public void setfALmNrcMastOnrf(String fALmNrcMastOnrf) {
			this.fALmNrcMastOnrf = fALmNrcMastOnrf;
		}

		public String getfALmNrcMastOfrf() {
			return fALmNrcMastOfrf;
		}

		public void setfALmNrcMastOfrf(String fALmNrcMastOfrf) {
			this.fALmNrcMastOfrf = fALmNrcMastOfrf;
		}

		public String getfALmArcBwOnrf() {
			return fALmArcBwOnrf;
		}

		public void setfALmArcBwOnrf(String fALmArcBwOnrf) {
			this.fALmArcBwOnrf = fALmArcBwOnrf;
		}

		public String getfALmArcBwProvOfrf() {
			return fALmArcBwProvOfrf;
		}

		public void setfALmArcBwProvOfrf(String fALmArcBwProvOfrf) {
			this.fALmArcBwProvOfrf = fALmArcBwProvOfrf;
		}

		public String getfBLmArcBwOnrf() {
			return fBLmArcBwOnrf;
		}

		public void setfBLmArcBwOnrf(String fBLmArcBwOnrf) {
			this.fBLmArcBwOnrf = fBLmArcBwOnrf;
		}

		public String getfBLmArcBwProvOfrf() {
			return fBLmArcBwProvOfrf;
		}

		public void setfBLmArcBwProvOfrf(String fBLmArcBwProvOfrf) {
			this.fBLmArcBwProvOfrf = fBLmArcBwProvOfrf;
		}

		public String getfALmNrcBwProvOfrf() {
			return fALmNrcBwProvOfrf;
		}

		public void setfALmNrcBwProvOfrf(String fALmNrcBwProvOfrf) {
			this.fALmNrcBwProvOfrf = fALmNrcBwProvOfrf;
		}

		public String getfBLmNrcBwProvOfrf() {
			return fBLmNrcBwProvOfrf;
		}

		public void setfBLmNrcBwProvOfrf(String fBLmNrcBwProvOfrf) {
			this.fBLmNrcBwProvOfrf = fBLmNrcBwProvOfrf;
		}

		public String getfALmNrcBwOnrf() {
			return fALmNrcBwOnrf;
		}

		public void setfALmNrcBwOnrf(String fALmNrcBwOnrf) {
			this.fALmNrcBwOnrf = fALmNrcBwOnrf;
		}

		public String getfBLmNrcBwOnrf() {
			return fBLmNrcBwOnrf;
		}

		public void setfBLmNrcBwOnrf(String fBLmNrcBwOnrf) {
			this.fBLmNrcBwOnrf = fBLmNrcBwOnrf;
		}

		@JsonProperty("f_b_lm_nrc_bw_onrf")
		private String fBLmNrcBwOnrf;
		
		
		
		public String getfALmArcBwOnwl() {
			return fALmArcBwOnwl;
		}

		public void setfALmArcBwOnwl(String fALmArcBwOnwl) {
			this.fALmArcBwOnwl = fALmArcBwOnwl;
		}


		public String getpALmNrcMastOnrf() {
			return pALmNrcMastOnrf;
		}

		public void setpALmNrcMastOnrf(String pALmNrcMastOnrf) {
			this.pALmNrcMastOnrf = pALmNrcMastOnrf;
		}

		public String getpALmNrcMastOfrf() {
			return pALmNrcMastOfrf;
		}

		public void setpALmNrcMastOfrf(String pALmNrcMastOfrf) {
			this.pALmNrcMastOfrf = pALmNrcMastOfrf;
		}

		public String getpALmArcBwOnwl() {
			return pALmArcBwOnwl;
		}

		public void setpALmArcBwOnwl(String pALmArcBwOnwl) {
			this.pALmArcBwOnwl = pALmArcBwOnwl;
		}

		public String getpALmNrcBwOnwl() {
			return pALmNrcBwOnwl;
		}

		public void setpALmNrcBwOnwl(String pALmNrcBwOnwl) {
			this.pALmNrcBwOnwl = pALmNrcBwOnwl;
		}

		public String getaLmNrcMastOnrf() {
			return aLmNrcMastOnrf;
		}

		public void setaLmNrcMastOnrf(String aLmNrcMastOnrf) {
			this.aLmNrcMastOnrf = aLmNrcMastOnrf;
		}

		public String getaLmNrcMastOfrf() {
			return aLmNrcMastOfrf;
		}

		public void setaLmNrcMastOfrf(String aLmNrcMastOfrf) {
			this.aLmNrcMastOfrf = aLmNrcMastOfrf;
		}

		public String getaLmArcBwOnwl() {
			return aLmArcBwOnwl;
		}

		public void setaLmArcBwOnwl(String aLmArcBwOnwl) {
			this.aLmArcBwOnwl = aLmArcBwOnwl;
		}

		public String getaLmArcBwOnrf() {
			return aLmArcBwOnrf;
		}

		public void setaLmArcBwOnrf(String aLmArcBwOnrf) {
			this.aLmArcBwOnrf = aLmArcBwOnrf;
		}

		public String getaLmArcBwProvOfrf() {
			return aLmArcBwProvOfrf;
		}

		public void setaLmArcBwProvOfrf(String aLmArcBwProvOfrf) {
			this.aLmArcBwProvOfrf = aLmArcBwProvOfrf;
		}

		public String getbLmArcBwOnwl() {
			return bLmArcBwOnwl;
		}

		public void setbLmArcBwOnwl(String bLmArcBwOnwl) {
			this.bLmArcBwOnwl = bLmArcBwOnwl;
		}

		public String getbLmArcBwOnrf() {
			return bLmArcBwOnrf;
		}

		public void setbLmArcBwOnrf(String bLmArcBwOnrf) {
			this.bLmArcBwOnrf = bLmArcBwOnrf;
		}

		public String getbLmArcBwProvOfrf() {
			return bLmArcBwProvOfrf;
		}

		public void setbLmArcBwProvOfrf(String bLmArcBwProvOfrf) {
			this.bLmArcBwProvOfrf = bLmArcBwProvOfrf;
		}

		public String getaLmNrcBwOnwl() {
			return aLmNrcBwOnwl;
		}

		public void setaLmNrcBwOnwl(String aLmNrcBwOnwl) {
			this.aLmNrcBwOnwl = aLmNrcBwOnwl;
		}

		public String getaLmNrcBwOnrf() {
			return aLmNrcBwOnrf;
		}

		public void setaLmNrcBwOnrf(String aLmNrcBwOnrf) {
			this.aLmNrcBwOnrf = aLmNrcBwOnrf;
		}

		public String getaLmNrcBwProvOfrf() {
			return aLmNrcBwProvOfrf;
		}

		public void setaLmNrcBwProvOfrf(String aLmNrcBwProvOfrf) {
			this.aLmNrcBwProvOfrf = aLmNrcBwProvOfrf;
		}

		public String getbLmNrcBwOnwl() {
			return bLmNrcBwOnwl;
		}

		public void setbLmNrcBwOnwl(String bLmNrcBwOnwl) {
			this.bLmNrcBwOnwl = bLmNrcBwOnwl;
		}

		public String getbLmNrcBwOnrf() {
			return bLmNrcBwOnrf;
		}

		public void setbLmNrcBwOnrf(String bLmNrcBwOnrf) {
			this.bLmNrcBwOnrf = bLmNrcBwOnrf;
		}

		public String getbLmNrcBwProvOfrf() {
			return bLmNrcBwProvOfrf;
		}

		public void setbLmNrcBwProvOfrf(String bLmNrcBwProvOfrf) {
			this.bLmNrcBwProvOfrf = bLmNrcBwProvOfrf;
		}
		
		
		@JsonProperty("Port_LP")
		public String getPortLP() {
			return PortLP;
		}

		@JsonProperty("Port_LP")
		public void setPortLP(String portLP) {
			PortLP = portLP;
		}

		@JsonProperty("CPE_Rental_MRC")
		public String getCPERentalMRC() {
			return CPERentalMRC;
		}

		@JsonProperty("CPE_Rental_MRC")
		public void setCPERentalMRC(String cPERentalMRC) {
			CPERentalMRC = cPERentalMRC;
		}


	    
	  

	    @JsonProperty("Port_ARC_LP")
		public String getPortARCLP() {
			return PortARCLP;
		}

	    @JsonProperty("Port_ARC_LP")
		public void setPortARCLP(String portARCLP) {
			PortARCLP = portARCLP;
		}

	    @JsonProperty("Port_NRC_LP")
		public String getPortNRCLP() {
			return PortNRCLP;
		}

	    @JsonProperty("Port_NRC_LP")
		public void setPortNRCLP(String portNRCLP) {
			PortNRCLP = portNRCLP;
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

	    @JsonProperty("lm_arc_prow_onwl")
	    public String getLmArcProwOnwl() {
	    return lmArcProwOnwl;
	    }

	    @JsonProperty("lm_arc_prow_onwl")
	    public void setLmArcProwOnwl(String lmArcProwOnwl) {
	    this.lmArcProwOnwl = lmArcProwOnwl;
	    }

	    @JsonProperty("lm_nrc_inbldg_onwl")
	    public String getLmNrcInbldgOnwl() {
	    return lmNrcInbldgOnwl;
	    }

	    @JsonProperty("lm_nrc_inbldg_onwl")
	    public void setLmNrcInbldgOnwl(String lmNrcInbldgOnwl) {
	    this.lmNrcInbldgOnwl = lmNrcInbldgOnwl;
	    }

	    @JsonProperty("lm_nrc_mux_onwl")
	    public String getLmNrcMuxOnwl() {
	    return lmNrcMuxOnwl;
	    }

	    @JsonProperty("lm_nrc_mux_onwl")
	    public void setLmNrcMuxOnwl(String lmNrcMuxOnwl) {
	    this.lmNrcMuxOnwl = lmNrcMuxOnwl;
	    }

	    @JsonProperty("lm_nrc_nerental_onwl")
	    public String getLmNrcNerentalOnwl() {
	    return lmNrcNerentalOnwl;
	    }

	    @JsonProperty("lm_nrc_nerental_onwl")
	    public void setLmNrcNerentalOnwl(String lmNrcNerentalOnwl) {
	    this.lmNrcNerentalOnwl = lmNrcNerentalOnwl;
	    }

	    @JsonProperty("lm_nrc_ospcapex_onwl")
	    public String getLmNrcOspcapexOnwl() {
	    return lmNrcOspcapexOnwl;
	    }

	    @JsonProperty("lm_nrc_ospcapex_onwl")
	    public void setLmNrcOspcapexOnwl(String lmNrcOspcapexOnwl) {
	    this.lmNrcOspcapexOnwl = lmNrcOspcapexOnwl;
	    }

	    @JsonProperty("lm_nrc_prow_onwl")
	    public String getLmNrcProwOnwl() {
	    return lmNrcProwOnwl;
	    }

	    @JsonProperty("lm_nrc_prow_onwl")
	    public void setLmNrcProwOnwl(String lmNrcProwOnwl) {
	    this.lmNrcProwOnwl = lmNrcProwOnwl;
	    }

	    @JsonProperty("lm_arc_bw_backhaul_onrf")
	    public String getLmArcBwBackhaulOnrf() {
	    return lmArcBwBackhaulOnrf;
	    }

	    @JsonProperty("lm_arc_bw_backhaul_onrf")
	    public void setLmArcBwBackhaulOnrf(String lmArcBwBackhaulOnrf) {
	    this.lmArcBwBackhaulOnrf = lmArcBwBackhaulOnrf;
	    }

	    @JsonProperty("lm_arc_bw_onrf")
	    public String getLmArcBwOnrf() {
	    return lmArcBwOnrf;
	    }

	    @JsonProperty("lm_arc_bw_onrf")
	    public void setLmArcBwOnrf(String lmArcBwOnrf) {
	    this.lmArcBwOnrf = lmArcBwOnrf;
	    }

	    @JsonProperty("lm_arc_colocation_charges_onrf")
	    public String getLmArcColocationChargesOnrf() {
	    return lmArcColocationChargesOnrf;
	    }

	    @JsonProperty("lm_arc_colocation_charges_onrf")
	    public void setLmArcColocationChargesOnrf(String lmArcColocationChargesOnrf) {
	    this.lmArcColocationChargesOnrf = lmArcColocationChargesOnrf;
	    }

	    @JsonProperty("lm_arc_converter_charges_onrf")
	    public String getLmArcConverterChargesOnrf() {
	    return lmArcConverterChargesOnrf;
	    }

	    @JsonProperty("lm_arc_converter_charges_onrf")
	    public void setLmArcConverterChargesOnrf(String lmArcConverterChargesOnrf) {
	    this.lmArcConverterChargesOnrf = lmArcConverterChargesOnrf;
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

	    @JsonProperty("lm_arc_bw_offwl")
	    public String getLmArcBwOffwl() {
	    return lmArcBwOffwl;
	    }

	    @JsonProperty("lm_arc_bw_offwl")
	    public void setLmArcBwOffwl(String lmArcBwOffwl) {
	    this.lmArcBwOffwl = lmArcBwOffwl;
	    }

	    @JsonProperty("lm_arc_modem_charges_offwl")
	    public String getLmArcModemChargesOffwl() {
	    return lmArcModemChargesOffwl;
	    }

	    @JsonProperty("lm_arc_modem_charges_offwl")
	    public void setLmArcModemChargesOffwl(String lmArcModemChargesOffwl) {
	    this.lmArcModemChargesOffwl = lmArcModemChargesOffwl;
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

	    @JsonProperty("lm_nrc_bw_prov_ofrf")
	    public String getLmNrcBwProvOfrf() {
	    return lmNrcBwProvOfrf;
	    }

	    @JsonProperty("lm_nrc_bw_prov_ofrf")
	    public void setLmNrcBwProvOfrf(String lmNrcBwProvOfrf) {
	    this.lmNrcBwProvOfrf = lmNrcBwProvOfrf;
	    }

	    @JsonProperty("lm_arc_bw_prov_ofrf")
	    public String getLmArcBwProvOfrf() {
	    return lmArcBwProvOfrf;
	    }

	    @JsonProperty("lm_arc_bw_prov_ofrf")
	    public void setLmArcBwProvOfrf(String lmArcBwProvOfrf) {
	    this.lmArcBwProvOfrf = lmArcBwProvOfrf;
	    }

	    @JsonProperty("lm_nrc_mast_ofrf")
	    public String getLmNrcMastOfrf() {
	    return lmNrcMastOfrf;
	    }

	    @JsonProperty("lm_nrc_mast_ofrf")
	    public void setLmNrcMastOfrf(String lmNrcMastOfrf) {
	    this.lmNrcMastOfrf = lmNrcMastOfrf;
	    }

// end nde dis
	
	
	@JsonProperty("a_local_loop_bw")
	public Double getaLocalLoopBw() {
		return aLocalLoopBw;
	}
	
	@JsonProperty("a_local_loop_bw")
	public void setaLocalLoopBw(Double aLocalLoopBw) {
		this.aLocalLoopBw = aLocalLoopBw;
	}
	
	@JsonProperty("b_local_loop_bw")
	public Double getbLocalLoopBw() {
		return bLocalLoopBw;
	}
	
	@JsonProperty("b_local_loop_bw")
	public void setbLocalLoopBw(Double bLocalLoopBw) {
		this.bLocalLoopBw = bLocalLoopBw;
	}
	
	
	@JsonProperty("a_Orch_LM_Type")
	public String getaOrchLMType() {
		return aOrchLMType;
	}
	@JsonProperty("a_Orch_LM_Type")
	public void setaOrchLMType(String aOrchLMType) {
		this.aOrchLMType = aOrchLMType;
	}
	
	@JsonProperty("b_Orch_LM_Type")
	public String getbOrchLMType() {
		return bOrchLMType;
	}
	@JsonProperty("b_Orch_LM_Type")
	public void setbOrchLMType(String bOrchLMType) {
		this.bOrchLMType = bOrchLMType;
	}

    
    
    
    
    @JsonProperty("sp_a_lm_arc_bw_prov_ofrf")
    public String getSpALmArcBwProvOfrf() {
		return spALmArcBwProvOfrf;
	}

    @JsonProperty("sp_a_lm_arc_bw_prov_ofrf")
	public void setSpALmArcBwProvOfrf(String spALmArcBwProvOfrf) {
		this.spALmArcBwProvOfrf = spALmArcBwProvOfrf;
	}

    @JsonProperty("sp_b_lm_arc_bw_prov_ofrf")
	public String getSpBLmArcBwProvOfrf() {
		return spBLmArcBwProvOfrf;
	}

    @JsonProperty("sp_b_lm_arc_bw_prov_ofrf")
	public void setSpBLmArcBwProvOfrf(String spBLmArcBwProvOfrf) {
		this.spBLmArcBwProvOfrf = spBLmArcBwProvOfrf;
	}

	@JsonProperty("sp_b_lm_arc_bw_backhaul_onrf")
    public String getSpBLmArcBwBackhaulOnrf() {
		return spBLmArcBwBackhaulOnrf;
	}

    @JsonProperty("sp_b_lm_arc_bw_backhaul_onrf")
	public void setSpBLmArcBwBackhaulOnrf(String spBLmArcBwBackhaulOnrf) {
		this.spBLmArcBwBackhaulOnrf = spBLmArcBwBackhaulOnrf;
	}

    @JsonProperty("sp_b_lm_arc_bw_offwl")
	public String getSpBLmArcBwOffwl() {
		return spBLmArcBwOffwl;
	}

    @JsonProperty("sp_b_lm_arc_bw_offwl")
	public void setSpBLmArcBwOffwl(String spBLmArcBwOffwl) {
		this.spBLmArcBwOffwl = spBLmArcBwOffwl;
	}

    @JsonProperty("sp_b_lm_arc_bw_onrf")
	public String getSpBLmArcBwOnrf() {
		return spBLmArcBwOnrf;
	}

    @JsonProperty("sp_b_lm_arc_bw_onrf")
	public void setSpBLmArcBwOnrf(String spBLmArcBwOnrf) {
		this.spBLmArcBwOnrf = spBLmArcBwOnrf;
	}

    @JsonProperty("sp_b_lm_arc_bw_onwl")
	public String getSpBLmArcBwOnwl() {
		return spBLmArcBwOnwl;
	}

    @JsonProperty("sp_b_lm_arc_bw_onwl")
	public void setSpBLmArcBwOnwl(String spBLmArcBwOnwl) {
		this.spBLmArcBwOnwl = spBLmArcBwOnwl;
	}

    @JsonProperty("sp_b_lm_arc_colocation_charges_onrf")
	public String getSpBLmArcColocationChargesOnrf() {
		return spBLmArcColocationChargesOnrf;
	}

    @JsonProperty("sp_b_lm_arc_colocation_charges_onrf")
	public void setSpBLmArcColocationChargesOnrf(String spBLmArcColocationChargesOnrf) {
		this.spBLmArcColocationChargesOnrf = spBLmArcColocationChargesOnrf;
	}

    @JsonProperty("sp_b_lm_arc_colocation_onrf")
	public String getSpBLmArcColocationOnrf() {
		return spBLmArcColocationOnrf;
	}

    @JsonProperty("sp_b_lm_arc_colocation_onrf")
	public void setSpBLmArcColocationOnrf(String spBLmArcColocationOnrf) {
		this.spBLmArcColocationOnrf = spBLmArcColocationOnrf;
	}

    @JsonProperty("sp_b_lm_arc_converter_charges_onrf")
	public String getSpBLmArcConverterChargesOnrf() {
		return spBLmArcConverterChargesOnrf;
	}

    @JsonProperty("sp_b_lm_arc_converter_charges_onrf")
	public void setSpBLmArcConverterChargesOnrf(String spBLmArcConverterChargesOnrf) {
		this.spBLmArcConverterChargesOnrf = spBLmArcConverterChargesOnrf;
	}

    @JsonProperty("sp_b_lm_arc_modem_charges_offwl")
	public String getSpBLmArcModemChargesOffwl() {
		return spBLmArcModemChargesOffwl;
	}

    @JsonProperty("sp_b_lm_arc_modem_charges_offwl")
	public void setSpBLmArcModemChargesOffwl(String spBLmArcModemChargesOffwl) {
		this.spBLmArcModemChargesOffwl = spBLmArcModemChargesOffwl;
	}

    @JsonProperty("sp_b_lm_arc_offwl")
	public String getSpBLmArcOffwl() {
		return spBLmArcOffwl;
	}

    @JsonProperty("sp_b_lm_arc_offwl")
	public void setSpBLmArcOffwl(String spBLmArcOffwl) {
		this.spBLmArcOffwl = spBLmArcOffwl;
	}

    @JsonProperty("sp_b_lm_arc_orderable_bw_onwl")
	public String getSpBLmArcOrderableBwOnwl() {
		return spBLmArcOrderableBwOnwl;
	}

    @JsonProperty("sp_b_lm_arc_orderable_bw_onwl")
	public void setSpBLmArcOrderableBwOnwl(String spBLmArcOrderableBwOnwl) {
		this.spBLmArcOrderableBwOnwl = spBLmArcOrderableBwOnwl;
	}

    @JsonProperty("sp_b_lm_arc_prow_onwl")
	public String getSpBLmArcProwOnwl() {
		return spBLmArcProwOnwl;
	}

    @JsonProperty("sp_b_lm_arc_prow_onwl")
	public void setSpBLmArcProwOnwl(String spBLmArcProwOnwl) {
		this.spBLmArcProwOnwl = spBLmArcProwOnwl;
	}

    @JsonProperty("sp_b_lm_arc_radwin_bw_onrf")
	public String getSpBLmArcRadwinBwOnrf() {
		return spBLmArcRadwinBwOnrf;
	}

    @JsonProperty("sp_b_lm_arc_radwin_bw_onrf")
	public void setSpBLmArcRadwinBwOnrf(String spBLmArcRadwinBwOnrf) {
		this.spBLmArcRadwinBwOnrf = spBLmArcRadwinBwOnrf;
	}

    @JsonProperty("sp_b_lm_nrc_bw_onrf")
	public String getSpBLmNrcBwOnrf() {
		return spBLmNrcBwOnrf;
	}

    @JsonProperty("sp_b_lm_nrc_bw_onrf")
	public void setSpBLmNrcBwOnrf(String spBLmNrcBwOnrf) {
		this.spBLmNrcBwOnrf = spBLmNrcBwOnrf;
	}

    @JsonProperty("sp_b_lm_nrc_bw_onwl")
	public String getSpBLmNrcBwOnwl() {
		return spBLmNrcBwOnwl;
	}
    
    @JsonProperty("sp_b_lm_nrc_bw_onwl")
	public void setSpBLmNrcBwOnwl(String spBLmNrcBwOnwl) {
		this.spBLmNrcBwOnwl = spBLmNrcBwOnwl;
	}

    @JsonProperty("sp_b_lm_nrc_bw_prov_ofrf")
	public String getSpBLmNrcBwProvOfrf() {
		return spBLmNrcBwProvOfrf;
	}

    @JsonProperty("sp_b_lm_nrc_bw_prov_ofrf")
	public void setSpBLmNrcBwProvOfrf(String spBLmNrcBwProvOfrf) {
		this.spBLmNrcBwProvOfrf = spBLmNrcBwProvOfrf;
	}

    @JsonProperty("sp_b_lm_nrc_inbldg_onwl")
	public String getSpBLmNrcInbldgOnwl() {
		return spBLmNrcInbldgOnwl;
	}

    @JsonProperty("sp_b_lm_nrc_inbldg_onwl")
	public void setSpBLmNrcInbldgOnwl(String spBLmNrcInbldgOnwl) {
		this.spBLmNrcInbldgOnwl = spBLmNrcInbldgOnwl;
	}

    @JsonProperty("sp_b_lm_nrc_mast_ofrf")
	public String getSpBLmNrcMastOfrf() {
		return spBLmNrcMastOfrf;
	}

    @JsonProperty("sp_b_lm_nrc_mast_ofrf")
	public void setSpBLmNrcMastOfrf(String spBLmNrcMastOfrf) {
		this.spBLmNrcMastOfrf = spBLmNrcMastOfrf;
	}

    @JsonProperty("sp_b_lm_nrc_mast_onrf")
	public String getSpBLmNrcMastOnrf() {
		return spBLmNrcMastOnrf;
	}

    @JsonProperty("sp_b_lm_nrc_mast_onrf")
	public void setSpBLmNrcMastOnrf(String spBLmNrcMastOnrf) {
		this.spBLmNrcMastOnrf = spBLmNrcMastOnrf;
	}

    @JsonProperty("sp_b_lm_nrc_mux_onwl")
	public String getSpBLmNrcMuxOnwl() {
		return spBLmNrcMuxOnwl;
	}

    @JsonProperty("sp_b_lm_nrc_mux_onwl")
	public void setSpBLmNrcMuxOnwl(String spBLmNrcMuxOnwl) {
		this.spBLmNrcMuxOnwl = spBLmNrcMuxOnwl;
	}

    @JsonProperty("sp_b_lm_nrc_nerental_onwl")
	public String getSpBLmNrcNerentalOnwl() {
		return spBLmNrcNerentalOnwl;
	}

    @JsonProperty("sp_b_lm_nrc_nerental_onwl")
	public void setSpBLmNrcNerentalOnwl(String spBLmNrcNerentalOnwl) {
		this.spBLmNrcNerentalOnwl = spBLmNrcNerentalOnwl;
	}

    @JsonProperty("sp_b_lm_nrc_network_capex_onwl")
	public String getSpBLmNrcNetworkCapexOnwl() {
		return spBLmNrcNetworkCapexOnwl;
	}

    @JsonProperty("sp_b_lm_nrc_network_capex_onwl")
	public void setSpBLmNrcNetworkCapexOnwl(String spBLmNrcNetworkCapexOnwl) {
		this.spBLmNrcNetworkCapexOnwl = spBLmNrcNetworkCapexOnwl;
	}

    @JsonProperty("sp_b_lm_nrc_ospcapex_onwl")
	public String getSpBLmNrcOspcapexOnwl() {
		return spBLmNrcOspcapexOnwl;
	}
    
    @JsonProperty("sp_b_lm_nrc_ospcapex_onwl")
	public void setSpBLmNrcOspcapexOnwl(String spBLmNrcOspcapexOnwl) {
		this.spBLmNrcOspcapexOnwl = spBLmNrcOspcapexOnwl;
	}

    @JsonProperty("sp_b_lm_nrc_prow_onwl")
	public String getSpBLmNrcProwOnwl() {
		return spBLmNrcProwOnwl;
	}

    @JsonProperty("sp_b_lm_nrc_prow_onwl")
	public void setSpBLmNrcProwOnwl(String spBLmNrcProwOnwl) {
		this.spBLmNrcProwOnwl = spBLmNrcProwOnwl;
	}

    @JsonProperty("sp_b_lm_otc_modem_charges_offwl")
	public String getSpBLmOtcModemChargesOffwl() {
		return spBLmOtcModemChargesOffwl;
	}

    @JsonProperty("sp_b_lm_otc_modem_charges_offwl")
	public void setSpBLmOtcModemChargesOffwl(String spBLmOtcModemChargesOffwl) {
		this.spBLmOtcModemChargesOffwl = spBLmOtcModemChargesOffwl;
	}

    @JsonProperty("sp_b_lm_otc_nrc_installation_offwl")
	public String getSpBLmOtcNrcInstallationOffwl() {
		return spBLmOtcNrcInstallationOffwl;
	}

    @JsonProperty("sp_b_lm_otc_nrc_installation_offwl")
	public void setSpBLmOtcNrcInstallationOffwl(String spBLmOtcNrcInstallationOffwl) {
		this.spBLmOtcNrcInstallationOffwl = spBLmOtcNrcInstallationOffwl;
	}

    @JsonProperty("sp_b_lm_otc_nrc_orderable_bw_onwl")
	public String getSpBLmOtcNrcOrderableBwOnwl() {
		return spBLmOtcNrcOrderableBwOnwl;
	}

    @JsonProperty("sp_b_lm_otc_nrc_orderable_bw_onwl")
	public void setSpBLmOtcNrcOrderableBwOnwl(String spBLmOtcNrcOrderableBwOnwl) {
		this.spBLmOtcNrcOrderableBwOnwl = spBLmOtcNrcOrderableBwOnwl;
	}

    @JsonProperty("sp_port_arc")
	public String getSpPortArc() {
		return spPortArc;
	}

    @JsonProperty("sp_port_arc")
	public void setSpPortArc(String spPortArc) {
		this.spPortArc = spPortArc;
	}

    @JsonProperty("sp_port_nrc")
	public String getSpPortNrc() {
		return spPortNrc;
	}

    @JsonProperty("sp_port_nrc")
	public void setSpPortNrc(String spPortNrc) {
		this.spPortNrc = spPortNrc;
	}

	@JsonProperty("sp_a_lm_arc_prow_onwl")
    public String getSpALmArcProwOnwl() {
		return spALmArcProwOnwl;
	}

    @JsonProperty("sp_a_lm_arc_prow_onwl")
	public void setSpALmArcProwOnwl(String spALmArcProwOnwl) {
		this.spALmArcProwOnwl = spALmArcProwOnwl;
	}

    @JsonProperty("sp_a_lm_arc_radwin_bw_onrf")
	public String getSpALmArcRadwinBwOnrf() {
		return spALmArcRadwinBwOnrf;
	}

    @JsonProperty("sp_a_lm_arc_radwin_bw_onrf")
	public void setSpALmArcRadwinBwOnrf(String spALmArcRadwinBwOnrf) {
		this.spALmArcRadwinBwOnrf = spALmArcRadwinBwOnrf;
	}

    @JsonProperty("sp_a_lm_nrc_bw_onrf")
	public String getSpALmNrcBwOnrf() {
		return spALmNrcBwOnrf;
	}

    @JsonProperty("sp_a_lm_nrc_bw_onrf")
	public void setSpALmNrcBwOnrf(String spALmNrcBwOnrf) {
		this.spALmNrcBwOnrf = spALmNrcBwOnrf;
	}

    @JsonProperty("sp_a_lm_nrc_bw_onwl")
	public String getSpALmNrcBwOnwl() {
		return spALmNrcBwOnwl;
	}

    @JsonProperty("sp_a_lm_nrc_bw_onwl")
	public void setSpALmNrcBwOnwl(String spALmNrcBwOnwl) {
		this.spALmNrcBwOnwl = spALmNrcBwOnwl;
	}

    @JsonProperty("sp_a_lm_nrc_bw_prov_ofrf")
	public String getSpALmNrcBwProvOfrf() {
		return spALmNrcBwProvOfrf;
	}

    @JsonProperty("sp_a_lm_nrc_bw_prov_ofrf")
	public void setSpALmNrcBwProvOfrf(String spALmNrcBwProvOfrf) {
		this.spALmNrcBwProvOfrf = spALmNrcBwProvOfrf;
	}

    @JsonProperty("sp_a_lm_nrc_inbldg_onwl")
	public String getSpALmNrcInbldgOnwl() {
		return spALmNrcInbldgOnwl;
	}

    @JsonProperty("sp_a_lm_nrc_inbldg_onwl")
	public void setSpALmNrcInbldgOnwl(String spALmNrcInbldgOnwl) {
		this.spALmNrcInbldgOnwl = spALmNrcInbldgOnwl;
	}

    @JsonProperty("sp_a_lm_nrc_mast_ofrf")
	public String getSpALmNrcMastOfrf() {
		return spALmNrcMastOfrf;
	}

    @JsonProperty("sp_a_lm_nrc_mast_ofrf")
	public void setSpALmNrcMastOfrf(String spALmNrcMastOfrf) {
		this.spALmNrcMastOfrf = spALmNrcMastOfrf;
	}

    @JsonProperty("sp_a_lm_nrc_mast_onrf")
	public String getSpALmNrcMastOnrf() {
		return spALmNrcMastOnrf;
	}

    @JsonProperty("sp_a_lm_nrc_mast_onrf")
	public void setSpALmNrcMastOnrf(String spALmNrcMastOnrf) {
		this.spALmNrcMastOnrf = spALmNrcMastOnrf;
	}

    @JsonProperty("sp_a_lm_nrc_mux_onwl")
	public String getSpALmNrcMuxOnwl() {
		return spALmNrcMuxOnwl;
	}

    @JsonProperty("sp_a_lm_nrc_mux_onwl")
	public void setSpALmNrcMuxOnwl(String spALmNrcMuxOnwl) {
		this.spALmNrcMuxOnwl = spALmNrcMuxOnwl;
	}

    @JsonProperty("sp_a_lm_nrc_nerental_onwl")
	public String getSpALmNrcNerentalOnwl() {
		return spALmNrcNerentalOnwl;
	}

    @JsonProperty("sp_a_lm_nrc_nerental_onwl")
	public void setSpALmNrcNerentalOnwl(String spALmNrcNerentalOnwl) {
		this.spALmNrcNerentalOnwl = spALmNrcNerentalOnwl;
	}

    @JsonProperty("sp_a_lm_nrc_network_capex_onwl")
	public String getSpALmNrcNetworkCapexOnwl() {
		return spALmNrcNetworkCapexOnwl;
	}

    @JsonProperty("sp_a_lm_nrc_network_capex_onwl")
	public void setSpALmNrcNetworkCapexOnwl(String spALmNrcNetworkCapexOnwl) {
		this.spALmNrcNetworkCapexOnwl = spALmNrcNetworkCapexOnwl;
	}

    @JsonProperty("sp_a_lm_nrc_ospcapex_onwl")
	public String getSpALmNrcOspcapexOnwl() {
		return spALmNrcOspcapexOnwl;
	}

    @JsonProperty("sp_a_lm_nrc_ospcapex_onwl")
	public void setSpALmNrcOspcapexOnwl(String spALmNrcOspcapexOnwl) {
		this.spALmNrcOspcapexOnwl = spALmNrcOspcapexOnwl;
	}

    @JsonProperty("sp_a_lm_nrc_prow_onwl")
	public String getSpALmNrcProwOnwl() {
		return spALmNrcProwOnwl;
	}

    @JsonProperty("sp_a_lm_nrc_prow_onwl")
	public void setSpALmNrcProwOnwl(String spALmNrcProwOnwl) {
		this.spALmNrcProwOnwl = spALmNrcProwOnwl;
	}

    @JsonProperty("sp_a_lm_otc_modem_charges_offwl")
	public String getSpALmOtcModemChargesOffwl() {
		return spALmOtcModemChargesOffwl;
	}

    @JsonProperty("sp_a_lm_otc_modem_charges_offwl")
	public void setSpALmOtcModemChargesOffwl(String spALmOtcModemChargesOffwl) {
		this.spALmOtcModemChargesOffwl = spALmOtcModemChargesOffwl;
	}

    @JsonProperty("sp_a_lm_otc_nrc_installation_offwl")
	public String getSpALmOtcNrcInstallationOffwl() {
		return spALmOtcNrcInstallationOffwl;
	}

    @JsonProperty("sp_a_lm_otc_nrc_installation_offwl")
	public void setSpALmOtcNrcInstallationOffwl(String spALmOtcNrcInstallationOffwl) {
		this.spALmOtcNrcInstallationOffwl = spALmOtcNrcInstallationOffwl;
	}

    @JsonProperty("sp_a_lm_otc_nrc_orderable_bw_onwl")
	public String getSpALmOtcNrcOrderableBwOnwl() {
		return spALmOtcNrcOrderableBwOnwl;
	}

    @JsonProperty("sp_a_lm_otc_nrc_orderable_bw_onwl")
	public void setSpALmOtcNrcOrderableBwOnwl(String spALmOtcNrcOrderableBwOnwl) {
		this.spALmOtcNrcOrderableBwOnwl = spALmOtcNrcOrderableBwOnwl;
	}
	
	
	@JsonProperty("sp_a_lm_arc_bw_backhaul_onrf")
	public String getSpALmArcBwBackhaulOnrf() {
			return spALmArcBwBackhaulOnrf;
	}

	@JsonProperty("sp_a_lm_arc_bw_backhaul_onrf")
	public void setSpALmArcBwBackhaulOnrf(String spALmArcBwBackhaulOnrf) {
		this.spALmArcBwBackhaulOnrf = spALmArcBwBackhaulOnrf;
	}

    @JsonProperty("sp_a_lm_arc_bw_offwl")
	public String getSpALmArcBwOffwl() {
		return spALmArcBwOffwl;
	}

    @JsonProperty("sp_a_lm_arc_bw_offwl")
	public void setSpALmArcBwOffwl(String spALmArcBwOffwl) {
		this.spALmArcBwOffwl = spALmArcBwOffwl;
	}

    @JsonProperty("sp_a_lm_arc_bw_onrf")
	public String getSpALmArcBwOnrf() {
		return spALmArcBwOnrf;
	}

    @JsonProperty("sp_a_lm_arc_bw_onrf")
	public void setSpALmArcBwOnrf(String spALmArcBwOnrf) {
		this.spALmArcBwOnrf = spALmArcBwOnrf;
	}

    @JsonProperty("sp_a_lm_arc_bw_onwl")
	public String getSpALmArcBwOnwl() {
		return spALmArcBwOnwl;
	}

    @JsonProperty("sp_a_lm_arc_bw_onwl")
	public void setSpALmArcBwOnwl(String spALmArcBwOnwl) {
		this.spALmArcBwOnwl = spALmArcBwOnwl;
	}

    @JsonProperty("sp_a_lm_arc_colocation_charges_onrf")
	public String getSpALmArcColocationChargesOnrf() {
		return spALmArcColocationChargesOnrf;
	}

    @JsonProperty("sp_a_lm_arc_colocation_charges_onrf")
	public void setSpALmArcColocationChargesOnrf(String spALmArcColocationChargesOnrf) {
		this.spALmArcColocationChargesOnrf = spALmArcColocationChargesOnrf;
	}

    @JsonProperty("sp_a_lm_arc_colocation_onrf")
	public String getSpALmArcColocationOnrf() {
		return spALmArcColocationOnrf;
	}

    @JsonProperty("sp_a_lm_arc_colocation_onrf")
	public void setSpALmArcColocationOnrf(String spALmArcColocationOnrf) {
		this.spALmArcColocationOnrf = spALmArcColocationOnrf;
	}

    @JsonProperty("sp_a_lm_arc_converter_charges_onrf")
	public String getSpALmArcConverterChargesOnrf() {
		return spALmArcConverterChargesOnrf;
	}

    @JsonProperty("sp_a_lm_arc_converter_charges_onrf")
	public void setSpALmArcConverterChargesOnrf(String spALmArcConverterChargesOnrf) {
		this.spALmArcConverterChargesOnrf = spALmArcConverterChargesOnrf;
	}

    @JsonProperty("sp_a_lm_arc_modem_charges_offwl")
	public String getSpALmArcModemChargesOffwl() {
		return spALmArcModemChargesOffwl;
	}

    @JsonProperty("sp_a_lm_arc_modem_charges_offwl")
	public void setSpALmArcModemChargesOffwl(String spALmArcModemChargesOffwl) {
		this.spALmArcModemChargesOffwl = spALmArcModemChargesOffwl;
	}

    @JsonProperty("sp_a_lm_arc_offwl")
	public String getSpALmArcOffwl() {
		return spALmArcOffwl;
	}

    @JsonProperty("sp_a_lm_arc_offwl")
	public void setSpALmArcOffwl(String spALmArcOffwl) {
		this.spALmArcOffwl = spALmArcOffwl;
	}

    @JsonProperty("sp_a_lm_arc_orderable_bw_onwl")
	public String getSpaALmArcOrderableBwOnwl() {
		return spaALmArcOrderableBwOnwl;
	}

    @JsonProperty("sp_a_lm_arc_orderable_bw_onwl")
	public void setSpaALmArcOrderableBwOnwl(String spaALmArcOrderableBwOnwl) {
		this.spaALmArcOrderableBwOnwl = spaALmArcOrderableBwOnwl;
	}

	
	
	
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

   

    @JsonProperty("Account.RTM_Cust")
    public String getAccountRTMCust() {
        return accountRTMCust;
    }

    @JsonProperty("Account.RTM_Cust")
    public void setAccountRTMCust(String accountRTMCust) {
        this.accountRTMCust = accountRTMCust;
    }

    @JsonProperty("BW_mbps_upd")
    public String getBWMbpsUpd() {
        return bWMbpsUpd;
    }

    @JsonProperty("BW_mbps_upd")
    public void setBWMbpsUpd(String bWMbpsUpd) {
        this.bWMbpsUpd = bWMbpsUpd;
    }

    @JsonProperty("DistanceBetweenPOPs")
    public String getDistanceBetweenPOPs() {
        return distanceBetweenPOPs;
    }

    @JsonProperty("DistanceBetweenPOPs")
    public void setDistanceBetweenPOPs(String distanceBetweenPOPs) {
        this.distanceBetweenPOPs = distanceBetweenPOPs;
    }

    @JsonProperty("Final_list_price")
    public String getFinalListPrice() {
        return finalListPrice;
    }

    @JsonProperty("Final_list_price")
    public void setFinalListPrice(String finalListPrice) {
        this.finalListPrice = finalListPrice;
    }

    @JsonProperty("GVPN_ARC_per_BW")
    public String getGVPNARCPerBW() {
        return gVPNARCPerBW;
    }

    @JsonProperty("GVPN_ARC_per_BW")
    public void setGVPNARCPerBW(String gVPNARCPerBW) {
        this.gVPNARCPerBW = gVPNARCPerBW;
    }

    @JsonProperty("ILL_ARC_per_BW")
    public String getILLARCPerBW() {
        return iLLARCPerBW;
    }

    @JsonProperty("ILL_ARC_per_BW")
    public void setILLARCPerBW(String iLLARCPerBW) {
        this.iLLARCPerBW = iLLARCPerBW;
    }

    @JsonProperty("Identifier")
    public String getIdentifier() {
        return identifier;
    }

    @JsonProperty("Identifier")
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    @JsonProperty("Industry_Cust")
    public String getIndustryCust() {
        return industryCust;
    }

    @JsonProperty("Industry_Cust")
    public void setIndustryCust(String industryCust) {
        this.industryCust = industryCust;
    }

    @JsonProperty("Inv_GVPN_bw")
    public String getInvGVPNBw() {
        return invGVPNBw;
    }

    @JsonProperty("Inv_GVPN_bw")
    public void setInvGVPNBw(String invGVPNBw) {
        this.invGVPNBw = invGVPNBw;
    }

    @JsonProperty("Inv_ILL_bw")
    public String getInvILLBw() {
        return invILLBw;
    }

    @JsonProperty("Inv_ILL_bw")
    public void setInvILLBw(String invILLBw) {
        this.invILLBw = invILLBw;
    }

    @JsonProperty("Inv_NPL_bw")
    public String getInvNPLBw() {
        return invNPLBw;
    }

    @JsonProperty("Inv_NPL_bw")
    public void setInvNPLBw(String invNPLBw) {
        this.invNPLBw = invNPLBw;
    }

    @JsonProperty("Inv_Other_bw")
    public String getInvOtherBw() {
        return invOtherBw;
    }

    @JsonProperty("Inv_Other_bw")
    public void setInvOtherBw(String invOtherBw) {
        this.invOtherBw = invOtherBw;
    }

    @JsonProperty("Inv_Tot_BW")
    public String getInvTotBW() {
        return invTotBW;
    }

    @JsonProperty("Inv_Tot_BW")
    public void setInvTotBW(String invTotBW) {
        this.invTotBW = invTotBW;
    }

    @JsonProperty("Last_Mile_Cost_ARC")
    public String getLastMileCostARC() {
        return lastMileCostARC;
    }

    @JsonProperty("Last_Mile_Cost_ARC")
    public void setLastMileCostARC(String lastMileCostARC) {
        this.lastMileCostARC = lastMileCostARC;
    }

    @JsonProperty("Last_Mile_Cost_NRC")
    public String getLastMileCostNRC() {
        return lastMileCostNRC;
    }

    @JsonProperty("Last_Mile_Cost_NRC")
    public void setLastMileCostNRC(String lastMileCostNRC) {
        this.lastMileCostNRC = lastMileCostNRC;
    }

    @JsonProperty("Last_Modified_Date")
    public String getLastModifiedDate() {
        return lastModifiedDate;
    }

    @JsonProperty("Last_Modified_Date")
    public void setLastModifiedDate(String lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    @JsonProperty("NPL_ARC_per_BW")
    public String getNPLARCPerBW() {
        return nPLARCPerBW;
    }

    @JsonProperty("NPL_ARC_per_BW")
    public void setNPLARCPerBW(String nPLARCPerBW) {
        this.nPLARCPerBW = nPLARCPerBW;
    }

    @JsonProperty("NPL_Port_ARC_Adjusted")
    public String getNPLPortARCAdjusted() {
        return nPLPortARCAdjusted;
    }

    @JsonProperty("NPL_Port_ARC_Adjusted")
    public void setNPLPortARCAdjusted(String nPLPortARCAdjusted) {
        this.nPLPortARCAdjusted = nPLPortARCAdjusted;
    }

    @JsonProperty("NPL_Port_MRC_Adjusted")
    public String getNPLPortMRCAdjusted() {
        return nPLPortMRCAdjusted;
    }

    @JsonProperty("NPL_Port_MRC_Adjusted")
    public void setNPLPortMRCAdjusted(String nPLPortMRCAdjusted) {
        this.nPLPortMRCAdjusted = nPLPortMRCAdjusted;
    }

    @JsonProperty("NPL_Port_NRC_Adjusted")
    public String getNPLPortNRCAdjusted() {
        return nPLPortNRCAdjusted;
    }

    @JsonProperty("NPL_Port_NRC_Adjusted")
    public void setNPLPortNRCAdjusted(String nPLPortNRCAdjusted) {
        this.nPLPortNRCAdjusted = nPLPortNRCAdjusted;
    }

    @JsonProperty("OpportunityID_Prod_Identifier")
    public String getOpportunityIDProdIdentifier() {
        return opportunityIDProdIdentifier;
    }

    @JsonProperty("OpportunityID_Prod_Identifier")
    public void setOpportunityIDProdIdentifier(String opportunityIDProdIdentifier) {
        this.opportunityIDProdIdentifier = opportunityIDProdIdentifier;
    }

    @JsonProperty("Other_ARC_per_BW")
    public String getOtherARCPerBW() {
        return otherARCPerBW;
    }

    @JsonProperty("Other_ARC_per_BW")
    public void setOtherARCPerBW(String otherARCPerBW) {
        this.otherARCPerBW = otherARCPerBW;
    }

    @JsonProperty("Segment_Cust")
    public String getSegmentCust() {
        return segmentCust;
    }

    @JsonProperty("Segment_Cust")
    public void setSegmentCust(String segmentCust) {
        this.segmentCust = segmentCust;
    }

    @JsonProperty("Sum_CAT_1_2_MACD_FLAG")
    public String getSumCAT12MACDFLAG() {
        return sumCAT12MACDFLAG;
    }

    @JsonProperty("Sum_CAT_1_2_MACD_FLAG")
    public void setSumCAT12MACDFLAG(String sumCAT12MACDFLAG) {
        this.sumCAT12MACDFLAG = sumCAT12MACDFLAG;
    }

    @JsonProperty("Sum_CAT_1_2_New_Opportunity_FLAG")
    public String getSumCAT12NewOpportunityFLAG() {
        return sumCAT12NewOpportunityFLAG;
    }

    @JsonProperty("Sum_CAT_1_2_New_Opportunity_FLAG")
    public void setSumCAT12NewOpportunityFLAG(String sumCAT12NewOpportunityFLAG) {
        this.sumCAT12NewOpportunityFLAG = sumCAT12NewOpportunityFLAG;
    }

    @JsonProperty("Sum_CAT_3_MACD_FLAG")
    public String getSumCAT3MACDFLAG() {
        return sumCAT3MACDFLAG;
    }

    @JsonProperty("Sum_CAT_3_MACD_FLAG")
    public void setSumCAT3MACDFLAG(String sumCAT3MACDFLAG) {
        this.sumCAT3MACDFLAG = sumCAT3MACDFLAG;
    }

    @JsonProperty("Sum_CAT_3_New_Opportunity_FLAG")
    public String getSumCAT3NewOpportunityFLAG() {
        return sumCAT3NewOpportunityFLAG;
    }

    @JsonProperty("Sum_CAT_3_New_Opportunity_FLAG")
    public void setSumCAT3NewOpportunityFLAG(String sumCAT3NewOpportunityFLAG) {
        this.sumCAT3NewOpportunityFLAG = sumCAT3NewOpportunityFLAG;
    }

    @JsonProperty("Sum_CAT_4_MACD_FLAG")
    public String getSumCAT4MACDFLAG() {
        return sumCAT4MACDFLAG;
    }

    @JsonProperty("Sum_CAT_4_MACD_FLAG")
    public void setSumCAT4MACDFLAG(String sumCAT4MACDFLAG) {
        this.sumCAT4MACDFLAG = sumCAT4MACDFLAG;
    }

    @JsonProperty("Sum_CAT_4_New_Opportunity_FLAG")
    public String getSumCAT4NewOpportunityFLAG() {
        return sumCAT4NewOpportunityFLAG;
    }

    @JsonProperty("Sum_CAT_4_New_Opportunity_FLAG")
    public void setSumCAT4NewOpportunityFLAG(String sumCAT4NewOpportunityFLAG) {
        this.sumCAT4NewOpportunityFLAG = sumCAT4NewOpportunityFLAG;
    }

    @JsonProperty("Sum_Cat_1_2_opp")
    public String getSumCat12Opp() {
        return sumCat12Opp;
    }

    @JsonProperty("Sum_Cat_1_2_opp")
    public void setSumCat12Opp(String sumCat12Opp) {
        this.sumCat12Opp = sumCat12Opp;
    }

    @JsonProperty("Sum_GVPN_Flag")
    public String getSumGVPNFlag() {
        return sumGVPNFlag;
    }

    @JsonProperty("Sum_GVPN_Flag")
    public void setSumGVPNFlag(String sumGVPNFlag) {
        this.sumGVPNFlag = sumGVPNFlag;
    }

    @JsonProperty("Sum_IAS_FLAG")
    public String getSumIASFLAG() {
        return sumIASFLAG;
    }

    @JsonProperty("Sum_IAS_FLAG")
    public void setSumIASFLAG(String sumIASFLAG) {
        this.sumIASFLAG = sumIASFLAG;
    }

    @JsonProperty("Sum_MACD_Opportunity")
    public String getSumMACDOpportunity() {
        return sumMACDOpportunity;
    }

    @JsonProperty("Sum_MACD_Opportunity")
    public void setSumMACDOpportunity(String sumMACDOpportunity) {
        this.sumMACDOpportunity = sumMACDOpportunity;
    }

    @JsonProperty("Sum_NPL_Flag")
    public String getSumNPLFlag() {
        return sumNPLFlag;
    }

    @JsonProperty("Sum_NPL_Flag")
    public void setSumNPLFlag(String sumNPLFlag) {
        this.sumNPLFlag = sumNPLFlag;
    }

    @JsonProperty("Sum_New_ARC_Converted")
    public String getSumNewARCConverted() {
        return sumNewARCConverted;
    }

    @JsonProperty("Sum_New_ARC_Converted")
    public void setSumNewARCConverted(String sumNewARCConverted) {
        this.sumNewARCConverted = sumNewARCConverted;
    }

    @JsonProperty("Sum_New_ARC_Converted_GVPN")
    public String getSumNewARCConvertedGVPN() {
        return sumNewARCConvertedGVPN;
    }

    @JsonProperty("Sum_New_ARC_Converted_GVPN")
    public void setSumNewARCConvertedGVPN(String sumNewARCConvertedGVPN) {
        this.sumNewARCConvertedGVPN = sumNewARCConvertedGVPN;
    }

    @JsonProperty("Sum_New_ARC_Converted_ILL")
    public String getSumNewARCConvertedILL() {
        return sumNewARCConvertedILL;
    }

    @JsonProperty("Sum_New_ARC_Converted_ILL")
    public void setSumNewARCConvertedILL(String sumNewARCConvertedILL) {
        this.sumNewARCConvertedILL = sumNewARCConvertedILL;
    }

    @JsonProperty("Sum_New_ARC_Converted_NPL")
    public String getSumNewARCConvertedNPL() {
        return sumNewARCConvertedNPL;
    }

    @JsonProperty("Sum_New_ARC_Converted_NPL")
    public void setSumNewARCConvertedNPL(String sumNewARCConvertedNPL) {
        this.sumNewARCConvertedNPL = sumNewARCConvertedNPL;
    }

    @JsonProperty("Sum_New_ARC_Converted_Other")
    public String getSumNewARCConvertedOther() {
        return sumNewARCConvertedOther;
    }

    @JsonProperty("Sum_New_ARC_Converted_Other")
    public void setSumNewARCConvertedOther(String sumNewARCConvertedOther) {
        this.sumNewARCConvertedOther = sumNewARCConvertedOther;
    }

    @JsonProperty("Sum_New_Opportunity")
    public String getSumNewOpportunity() {
        return sumNewOpportunity;
    }

    @JsonProperty("Sum_New_Opportunity")
    public void setSumNewOpportunity(String sumNewOpportunity) {
        this.sumNewOpportunity = sumNewOpportunity;
    }

    @JsonProperty("Sum_Other_Flag")
    public String getSumOtherFlag() {
        return sumOtherFlag;
    }

    @JsonProperty("Sum_Other_Flag")
    public void setSumOtherFlag(String sumOtherFlag) {
        this.sumOtherFlag = sumOtherFlag;
    }

    @JsonProperty("Sum_tot_oppy_historic_opp")
    public String getSumTotOppyHistoricOpp() {
        return sumTotOppyHistoricOpp;
    }

    @JsonProperty("Sum_tot_oppy_historic_opp")
    public void setSumTotOppyHistoricOpp(String sumTotOppyHistoricOpp) {
        this.sumTotOppyHistoricOpp = sumTotOppyHistoricOpp;
    }

    @JsonProperty("Sum_tot_oppy_historic_prod")
    public String getSumTotOppyHistoricProd() {
        return sumTotOppyHistoricProd;
    }

    @JsonProperty("Sum_tot_oppy_historic_prod")
    public void setSumTotOppyHistoricProd(String sumTotOppyHistoricProd) {
        this.sumTotOppyHistoricProd = sumTotOppyHistoricProd;
    }

    @JsonProperty("TOT_ARC_per_BW")
    public String getTOTARCPerBW() {
        return tOTARCPerBW;
    }

    @JsonProperty("TOT_ARC_per_BW")
    public void setTOTARCPerBW(String tOTARCPerBW) {
        this.tOTARCPerBW = tOTARCPerBW;
    }

    @JsonProperty("a_latitude_final")
    public String getALatitudeFinal() {
        return aLatitudeFinal;
    }

    @JsonProperty("a_latitude_final")
    public void setALatitudeFinal(String aLatitudeFinal) {
        this.aLatitudeFinal = aLatitudeFinal;
    }

    @JsonProperty("a_local_loop_interface")
    public String getALocalLoopInterface() {
        return aLocalLoopInterface;
    }

    @JsonProperty("a_local_loop_interface")
    public void setALocalLoopInterface(String aLocalLoopInterface) {
        this.aLocalLoopInterface = aLocalLoopInterface;
    }

    @JsonProperty("a_longitude_final")
    public String getALongitudeFinal() {
        return aLongitudeFinal;
    }

    @JsonProperty("a_longitude_final")
    public void setALongitudeFinal(String aLongitudeFinal) {
        this.aLongitudeFinal = aLongitudeFinal;
    }

    @JsonProperty("a_resp_city")
    public String getARespCity() {
        return aRespCity;
    }

    @JsonProperty("a_resp_city")
    public void setARespCity(String aRespCity) {
        this.aRespCity = aRespCity;
    }

    @JsonProperty("account_id_with_18_digit")
    public String getAccountIdWith18Digit() {
        return accountIdWith18Digit;
    }

    @JsonProperty("account_id_with_18_digit")
    public void setAccountIdWith18Digit(String accountIdWith18Digit) {
        this.accountIdWith18Digit = accountIdWith18Digit;
    }

    @JsonProperty("b_latitude_final")
    public String getBLatitudeFinal() {
        return bLatitudeFinal;
    }

    @JsonProperty("b_latitude_final")
    public void setBLatitudeFinal(String bLatitudeFinal) {
        this.bLatitudeFinal = bLatitudeFinal;
    }

    @JsonProperty("b_local_loop_interface")
    public String getBLocalLoopInterface() {
        return bLocalLoopInterface;
    }

    @JsonProperty("b_local_loop_interface")
    public void setBLocalLoopInterface(String bLocalLoopInterface) {
        this.bLocalLoopInterface = bLocalLoopInterface;
    }

    @JsonProperty("b_longitude_final")
    public String getBLongitudeFinal() {
        return bLongitudeFinal;
    }

    @JsonProperty("b_longitude_final")
    public void setBLongitudeFinal(String bLongitudeFinal) {
        this.bLongitudeFinal = bLongitudeFinal;
    }

    @JsonProperty("b_resp_city")
    public String getBRespCity() {
        return bRespCity;
    }

    @JsonProperty("b_resp_city")
    public void setBRespCity(String bRespCity) {
        this.bRespCity = bRespCity;
    }

    @JsonProperty("bw_mbps")
    public String getBwMbps() {
        return bwMbps;
    }

    @JsonProperty("bw_mbps")
    public void setBwMbps(String bwMbps) {
        this.bwMbps = bwMbps;
    }

    @JsonProperty("chargeable_distance")
    public String getChargeableDistance() {
        return chargeableDistance;
    }

    @JsonProperty("chargeable_distance")
    public void setChargeableDistance(String chargeableDistance) {
        this.chargeableDistance = chargeableDistance;
    }

    @JsonProperty("createdDate_quote")
    public String getCreatedDateQuote() {
        return createdDateQuote;
    }

    @JsonProperty("createdDate_quote")
    public void setCreatedDateQuote(String createdDateQuote) {
        this.createdDateQuote = createdDateQuote;
    }

    @JsonProperty("datediff")
    public String getDatediff() {
        return datediff;
    }

    @JsonProperty("datediff")
    public void setDatediff(String datediff) {
        this.datediff = datediff;
    }

    @JsonProperty("dist_betw_pops")
    public String getDistBetwPops() {
        return distBetwPops;
    }

    @JsonProperty("dist_betw_pops")
    public void setDistBetwPops(String distBetwPops) {
        this.distBetwPops = distBetwPops;
    }

    @JsonProperty("error_code")
    public String getErrorCode() {
        return errorCode;
    }

    @JsonProperty("error_code")
    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    @JsonProperty("error_flag")
    public String getErrorFlag() {
        return errorFlag;
    }

    @JsonProperty("error_flag")
    public void setErrorFlag(String errorFlag) {
        this.errorFlag = errorFlag;
    }

    @JsonProperty("error_msg")
    public String getErrorMsg() {
        return errorMsg;
    }

    @JsonProperty("error_msg")
    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    @JsonProperty("f_a_lm_arc_bw_onwl")
    public String getFALmArcBwOnwl() {
        return fALmArcBwOnwl;
    }

    @JsonProperty("f_a_lm_arc_bw_onwl")
    public void setFALmArcBwOnwl(String fALmArcBwOnwl) {
        this.fALmArcBwOnwl = fALmArcBwOnwl;
    }

    @JsonProperty("f_a_lm_nrc_bw_onwl")
    public String getFALmNrcBwOnwl() {
        return fALmNrcBwOnwl;
    }

    @JsonProperty("f_a_lm_nrc_bw_onwl")
    public void setFALmNrcBwOnwl(String fALmNrcBwOnwl) {
        this.fALmNrcBwOnwl = fALmNrcBwOnwl;
    }

    @JsonProperty("f_a_lm_nrc_inbldg_onwl")
    public String getFALmNrcInbldgOnwl() {
        return fALmNrcInbldgOnwl;
    }

    @JsonProperty("f_a_lm_nrc_inbldg_onwl")
    public void setFALmNrcInbldgOnwl(String fALmNrcInbldgOnwl) {
        this.fALmNrcInbldgOnwl = fALmNrcInbldgOnwl;
    }

    @JsonProperty("f_a_lm_nrc_mux_onwl")
    public String getFALmNrcMuxOnwl() {
        return fALmNrcMuxOnwl;
    }

    @JsonProperty("f_a_lm_nrc_mux_onwl")
    public void setFALmNrcMuxOnwl(String fALmNrcMuxOnwl) {
        this.fALmNrcMuxOnwl = fALmNrcMuxOnwl;
    }

    @JsonProperty("f_a_lm_nrc_nerental_onwl")
    public String getFALmNrcNerentalOnwl() {
        return fALmNrcNerentalOnwl;
    }

    @JsonProperty("f_a_lm_nrc_nerental_onwl")
    public void setFALmNrcNerentalOnwl(String fALmNrcNerentalOnwl) {
        this.fALmNrcNerentalOnwl = fALmNrcNerentalOnwl;
    }

    @JsonProperty("f_a_lm_nrc_ospcapex_onwl")
    public String getFALmNrcOspcapexOnwl() {
        return fALmNrcOspcapexOnwl;
    }

    @JsonProperty("f_a_lm_nrc_ospcapex_onwl")
    public void setFALmNrcOspcapexOnwl(String fALmNrcOspcapexOnwl) {
        this.fALmNrcOspcapexOnwl = fALmNrcOspcapexOnwl;
    }

    @JsonProperty("f_b_lm_arc_bw_onwl")
    public String getFBLmArcBwOnwl() {
        return fBLmArcBwOnwl;
    }

    @JsonProperty("f_b_lm_arc_bw_onwl")
    public void setFBLmArcBwOnwl(String fBLmArcBwOnwl) {
        this.fBLmArcBwOnwl = fBLmArcBwOnwl;
    }

    @JsonProperty("f_b_lm_nrc_bw_onwl")
    public String getFBLmNrcBwOnwl() {
        return fBLmNrcBwOnwl;
    }

    @JsonProperty("f_b_lm_nrc_bw_onwl")
    public void setFBLmNrcBwOnwl(String fBLmNrcBwOnwl) {
        this.fBLmNrcBwOnwl = fBLmNrcBwOnwl;
    }

    @JsonProperty("f_b_lm_nrc_inbldg_onwl")
    public String getFBLmNrcInbldgOnwl() {
        return fBLmNrcInbldgOnwl;
    }

    @JsonProperty("f_b_lm_nrc_inbldg_onwl")
    public void setFBLmNrcInbldgOnwl(String fBLmNrcInbldgOnwl) {
        this.fBLmNrcInbldgOnwl = fBLmNrcInbldgOnwl;
    }

    @JsonProperty("f_b_lm_nrc_mux_onwl")
    public String getFBLmNrcMuxOnwl() {
        return fBLmNrcMuxOnwl;
    }

    @JsonProperty("f_b_lm_nrc_mux_onwl")
    public void setFBLmNrcMuxOnwl(String fBLmNrcMuxOnwl) {
        this.fBLmNrcMuxOnwl = fBLmNrcMuxOnwl;
    }

    @JsonProperty("f_b_lm_nrc_nerental_onwl")
    public String getFBLmNrcNerentalOnwl() {
        return fBLmNrcNerentalOnwl;
    }

    @JsonProperty("f_b_lm_nrc_nerental_onwl")
    public void setFBLmNrcNerentalOnwl(String fBLmNrcNerentalOnwl) {
        this.fBLmNrcNerentalOnwl = fBLmNrcNerentalOnwl;
    }

    @JsonProperty("f_b_lm_nrc_ospcapex_onwl")
    public String getFBLmNrcOspcapexOnwl() {
        return fBLmNrcOspcapexOnwl;
    }

    @JsonProperty("f_b_lm_nrc_ospcapex_onwl")
    public void setFBLmNrcOspcapexOnwl(String fBLmNrcOspcapexOnwl) {
        this.fBLmNrcOspcapexOnwl = fBLmNrcOspcapexOnwl;
    }

    @JsonProperty("feasibility_response_created_date")
    public String getFeasibilityResponseCreatedDate() {
        return feasibilityResponseCreatedDate;
    }

    @JsonProperty("feasibility_response_created_date")
    public void setFeasibilityResponseCreatedDate(String feasibilityResponseCreatedDate) {
        this.feasibilityResponseCreatedDate = feasibilityResponseCreatedDate;
    }

    @JsonProperty("hist_flag")
    public String getHistFlag() {
        return histFlag;
    }

    @JsonProperty("hist_flag")
    public void setHistFlag(String histFlag) {
        this.histFlag = histFlag;
    }

    @JsonProperty("intra_inter_flag")
    public String getIntraInterFlag() {
        return intraInterFlag;
    }

    @JsonProperty("intra_inter_flag")
    public void setIntraInterFlag(String intraInterFlag) {
        this.intraInterFlag = intraInterFlag;
    }

    @JsonProperty("link_id")
    public String getLinkId() {
        return linkId;
    }

    @JsonProperty("link_id")
    public void setLinkId(String linkId) {
        this.linkId = linkId;
    }

    @JsonProperty("list_price_mb")
    public String getListPriceMb() {
        return listPriceMb;
    }

    @JsonProperty("list_price_mb")
    public void setListPriceMb(String listPriceMb) {
        this.listPriceMb = listPriceMb;
    }

    @JsonProperty("model_name_transform")
    public String getModelNameTransform() {
        return modelNameTransform;
    }

    @JsonProperty("model_name_transform")
    public void setModelNameTransform(String modelNameTransform) {
        this.modelNameTransform = modelNameTransform;
    }

    @JsonProperty("opportunity_day")
    public String getOpportunityDay() {
        return opportunityDay;
    }

    @JsonProperty("opportunity_day")
    public void setOpportunityDay(String opportunityDay) {
        this.opportunityDay = opportunityDay;
    }

    @JsonProperty("opportunity_month")
    public String getOpportunityMonth() {
        return opportunityMonth;
    }

    @JsonProperty("opportunity_month")
    public void setOpportunityMonth(String opportunityMonth) {
        this.opportunityMonth = opportunityMonth;
    }

    @JsonProperty("opportunity_term")
    public String getOpportunityTerm() {
        return opportunityTerm;
    }

    @JsonProperty("opportunity_term")
    public void setOpportunityTerm(String opportunityTerm) {
        this.opportunityTerm = opportunityTerm;
    }

    @JsonProperty("p_a_lm_arc_bw_onwl")
    public String getPALmArcBwOnwl() {
        return pALmArcBwOnwl;
    }

    @JsonProperty("p_a_lm_arc_bw_onwl")
    public void setPALmArcBwOnwl(String pALmArcBwOnwl) {
        this.pALmArcBwOnwl = pALmArcBwOnwl;
    }

    @JsonProperty("p_a_lm_nrc_bw_onwl")
    public String getPALmNrcBwOnwl() {
        return pALmNrcBwOnwl;
    }

    @JsonProperty("p_a_lm_nrc_bw_onwl")
    public void setPALmNrcBwOnwl(String pALmNrcBwOnwl) {
        this.pALmNrcBwOnwl = pALmNrcBwOnwl;
    }

    @JsonProperty("p_a_lm_nrc_inbldg_onwl")
    public String getPALmNrcInbldgOnwl() {
        return pALmNrcInbldgOnwl;
    }

    @JsonProperty("p_a_lm_nrc_inbldg_onwl")
    public void setPALmNrcInbldgOnwl(String pALmNrcInbldgOnwl) {
        this.pALmNrcInbldgOnwl = pALmNrcInbldgOnwl;
    }

    @JsonProperty("p_a_lm_nrc_mux_onwl")
    public String getPALmNrcMuxOnwl() {
        return pALmNrcMuxOnwl;
    }

    @JsonProperty("p_a_lm_nrc_mux_onwl")
    public void setPALmNrcMuxOnwl(String pALmNrcMuxOnwl) {
        this.pALmNrcMuxOnwl = pALmNrcMuxOnwl;
    }

    @JsonProperty("p_a_lm_nrc_nerental_onwl")
    public String getPALmNrcNerentalOnwl() {
        return pALmNrcNerentalOnwl;
    }

    @JsonProperty("p_a_lm_nrc_nerental_onwl")
    public void setPALmNrcNerentalOnwl(String pALmNrcNerentalOnwl) {
        this.pALmNrcNerentalOnwl = pALmNrcNerentalOnwl;
    }

    @JsonProperty("p_a_lm_nrc_ospcapex_onwl")
    public String getPALmNrcOspcapexOnwl() {
        return pALmNrcOspcapexOnwl;
    }

    @JsonProperty("p_a_lm_nrc_ospcapex_onwl")
    public void setPALmNrcOspcapexOnwl(String pALmNrcOspcapexOnwl) {
        this.pALmNrcOspcapexOnwl = pALmNrcOspcapexOnwl;
    }

    @JsonProperty("p_b_lm_arc_bw_onwl")
    public String getPBLmArcBwOnwl() {
        return pBLmArcBwOnwl;
    }

    @JsonProperty("p_b_lm_arc_bw_onwl")
    public void setPBLmArcBwOnwl(String pBLmArcBwOnwl) {
        this.pBLmArcBwOnwl = pBLmArcBwOnwl;
    }

    @JsonProperty("p_b_lm_nrc_bw_onwl")
    public String getPBLmNrcBwOnwl() {
        return pBLmNrcBwOnwl;
    }

    @JsonProperty("p_b_lm_nrc_bw_onwl")
    public void setPBLmNrcBwOnwl(String pBLmNrcBwOnwl) {
        this.pBLmNrcBwOnwl = pBLmNrcBwOnwl;
    }

    @JsonProperty("p_b_lm_nrc_inbldg_onwl")
    public String getPBLmNrcInbldgOnwl() {
        return pBLmNrcInbldgOnwl;
    }

    @JsonProperty("p_b_lm_nrc_inbldg_onwl")
    public void setPBLmNrcInbldgOnwl(String pBLmNrcInbldgOnwl) {
        this.pBLmNrcInbldgOnwl = pBLmNrcInbldgOnwl;
    }

    @JsonProperty("p_b_lm_nrc_mux_onwl")
    public String getPBLmNrcMuxOnwl() {
        return pBLmNrcMuxOnwl;
    }

    @JsonProperty("p_b_lm_nrc_mux_onwl")
    public void setPBLmNrcMuxOnwl(String pBLmNrcMuxOnwl) {
        this.pBLmNrcMuxOnwl = pBLmNrcMuxOnwl;
    }

    @JsonProperty("p_b_lm_nrc_nerental_onwl")
    public String getPBLmNrcNerentalOnwl() {
        return pBLmNrcNerentalOnwl;
    }

    @JsonProperty("p_b_lm_nrc_nerental_onwl")
    public void setPBLmNrcNerentalOnwl(String pBLmNrcNerentalOnwl) {
        this.pBLmNrcNerentalOnwl = pBLmNrcNerentalOnwl;
    }

    @JsonProperty("p_b_lm_nrc_ospcapex_onwl")
    public String getPBLmNrcOspcapexOnwl() {
        return pBLmNrcOspcapexOnwl;
    }

    @JsonProperty("p_b_lm_nrc_ospcapex_onwl")
    public void setPBLmNrcOspcapexOnwl(String pBLmNrcOspcapexOnwl) {
        this.pBLmNrcOspcapexOnwl = pBLmNrcOspcapexOnwl;
    }

    @JsonProperty("port_lm_arc")
    public String getPortLmArc() {
        return portLmArc;
    }

    @JsonProperty("port_lm_arc")
    public void setPortLmArc(String portLmArc) {
        this.portLmArc = portLmArc;
    }

    @JsonProperty("port_pred_discount")
    public String getPortPredDiscount() {
        return portPredDiscount;
    }

    @JsonProperty("port_pred_discount")
    public void setPortPredDiscount(String portPredDiscount) {
        this.portPredDiscount = portPredDiscount;
    }

    @JsonProperty("predicted_NPL_Port_ARC")
    public String getPredictedNPLPortARC() {
        return predictedNPLPortARC;
    }

    @JsonProperty("predicted_NPL_Port_ARC")
    public void setPredictedNPLPortARC(String predictedNPLPortARC) {
        this.predictedNPLPortARC = predictedNPLPortARC;
    }

    @JsonProperty("predicted_net_price")
    public String getPredictedNetPrice() {
        return predictedNetPrice;
    }

    @JsonProperty("predicted_net_price")
    public void setPredictedNetPrice(String predictedNetPrice) {
        this.predictedNetPrice = predictedNetPrice;
    }

    @JsonProperty("product_flavor_transform")
    public String getProductFlavorTransform() {
        return productFlavorTransform;
    }

    @JsonProperty("product_flavor_transform")
    public void setProductFlavorTransform(String productFlavorTransform) {
        this.productFlavorTransform = productFlavorTransform;
    }

    @JsonProperty("product_name")
    public String getProductName() {
        return productName;
    }

    @JsonProperty("product_name")
    public void setProductName(String productName) {
        this.productName = productName;
    }

    @JsonProperty("prospect_name")
    public String getProspectName() {
        return prospectName;
    }

    @JsonProperty("prospect_name")
    public void setProspectName(String prospectName) {
        this.prospectName = prospectName;
    }

    @JsonProperty("quoteType_quote")
    public String getQuoteTypeQuote() {
        return quoteTypeQuote;
    }

    @JsonProperty("quoteType_quote")
    public void setQuoteTypeQuote(String quoteTypeQuote) {
        this.quoteTypeQuote = quoteTypeQuote;
    }

    @JsonProperty("quotetype_quote")
    public String getQuotetypeQuote() {
        return quotetypeQuote;
    }

    @JsonProperty("quotetype_quote")
    public void setQuotetypeQuote(String quotetypeQuote) {
        this.quotetypeQuote = quotetypeQuote;
    }

    @JsonProperty("site_id")
    public String getSiteId() {
        return siteId;
    }

    @JsonProperty("site_id")
    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    @JsonProperty("sla_varient")
    public String getSlaVarient() {
        return slaVarient;
    }

    @JsonProperty("sla_varient")
    public void setSlaVarient(String slaVarient) {
        this.slaVarient = slaVarient;
    }

    @JsonProperty("sum_cat1_2_Opportunity")
    public String getSumCat12Opportunity() {
        return sumCat12Opportunity;
    }

    @JsonProperty("sum_cat1_2_Opportunity")
    public void setSumCat12Opportunity(String sumCat12Opportunity) {
        this.sumCat12Opportunity = sumCat12Opportunity;
    }

    @JsonProperty("sum_cat_3_Opportunity")
    public String getSumCat3Opportunity() {
        return sumCat3Opportunity;
    }

    @JsonProperty("sum_cat_3_Opportunity")
    public void setSumCat3Opportunity(String sumCat3Opportunity) {
        this.sumCat3Opportunity = sumCat3Opportunity;
    }

    @JsonProperty("sum_cat_4_Opportunity")
    public String getSumCat4Opportunity() {
        return sumCat4Opportunity;
    }

    @JsonProperty("sum_cat_4_Opportunity")
    public void setSumCat4Opportunity(String sumCat4Opportunity) {
        this.sumCat4Opportunity = sumCat4Opportunity;
    }

    @JsonProperty("sum_model_name")
    public String getSumModelName() {
        return sumModelName;
    }

    @JsonProperty("sum_model_name")
    public void setSumModelName(String sumModelName) {
        this.sumModelName = sumModelName;
    }

    @JsonProperty("time_taken")
    public String getTimeTaken() {
        return timeTaken;
    }

    @JsonProperty("time_taken")
    public void setTimeTaken(String timeTaken) {
        this.timeTaken = timeTaken;
    }

    @JsonProperty("total_contract_value")
    public String getTotalContractValue() {
        return totalContractValue;
    }

    @JsonProperty("total_contract_value")
    public void setTotalContractValue(String totalContractValue) {
        this.totalContractValue = totalContractValue;
    }

    @JsonProperty("total_osp_capex")
    public String getTotalOspCapex() {
        return totalOspCapex;
    }

    @JsonProperty("total_osp_capex")
    public void setTotalOspCapex(String totalOspCapex) {
        this.totalOspCapex = totalOspCapex;
    }

	public String getNplArcMgmntCharges() {
		return nplArcMgmntCharges;
	}

	public void setNplArcMgmntCharges(String nplArcMgmntCharges) {
		this.nplArcMgmntCharges = nplArcMgmntCharges;
	}

	public String getNplMrcMgmntCharges() {
		return nplMrcMgmntCharges;
	}

	public void setNplMrcMgmntCharges(String nplMrcMgmntCharges) {
		this.nplMrcMgmntCharges = nplMrcMgmntCharges;
	}

	public String getRateCardFlag() {
		return rateCardFlag;
	}

	public void setRateCardFlag(String rateCardFlag) {
		this.rateCardFlag = rateCardFlag;
	}

	public String getApiVersionNo() {
		return apiVersionNo;
	}

	public void setApiVersionNo(String apiVersionNo) {
		this.apiVersionNo = apiVersionNo;
	}

	@JsonProperty("Bucket_Adjustment_Type")
    public String getBucketAdjustmentType() {
        return bucketAdjustmentType;
    }

    @JsonProperty("Bucket_Adjustment_Type")
    public void setBucketAdjustmentType(String bucketAdjustmentType) {
        this.bucketAdjustmentType = bucketAdjustmentType;
    }
    
    @JsonProperty("shift_charge")
    public String getShiftCharge() {
    return shiftCharge;
    }

    @JsonProperty("shift_charge")
    public void setShiftCharge(String shiftCharge) {
    this.shiftCharge = shiftCharge;
    }
    
    public String getaOrchConnection() { return aOrchConnection; }

	public void setaOrchConnection(String aOrchConnection) { this.aOrchConnection = aOrchConnection; }

	public String getbOrchConnection() { return bOrchConnection; }

	public void setbOrchConnection(String bOrchConnection) { this.bOrchConnection = bOrchConnection; }
	
	 public String getaOrchCategory() {
	        return aOrchCategory;
	    }

	    public void setaOrchCategory(String aOrchCategory) {
	        this.aOrchCategory = aOrchCategory;
	    }

	    public String getbOrchCategory() {
	        return bOrchCategory;
	    }

	    public void setbOrchCategory(String bOrchCategory) {
	        this.bOrchCategory = bOrchCategory;
	    }
	    @JsonProperty("a_bandwidth")
		public String getAbandwidth() {
			return abandwidth;
		}
		@JsonProperty("a_bandwidth")
		public void setAbandwidth(String abandwidth) {
			this.abandwidth = abandwidth;
		}
		
		@JsonProperty("b_bandwidth")
		public String getBbandwidth() {
			return bbandwidth;
		}
		@JsonProperty("b_bandwidth")
		public void setBbandwidth(String bbandwidth) {
			this.bbandwidth = bbandwidth;
		}
		
		
		public String getRcFlag() {
			return rcFlag;
		}

		public void setRcFlag(String rcFlag) {
			this.rcFlag = rcFlag;
		}

		@Override
		public String toString() {
			return "Result [accountRTMCust=" + accountRTMCust + ", bWMbpsUpd=" + bWMbpsUpd + ", distanceBetweenPOPs="
					+ distanceBetweenPOPs + ", finalListPrice=" + finalListPrice + ", gVPNARCPerBW=" + gVPNARCPerBW
					+ ", iLLARCPerBW=" + iLLARCPerBW + ", identifier=" + identifier + ", industryCust=" + industryCust
					+ ", invGVPNBw=" + invGVPNBw + ", invILLBw=" + invILLBw + ", invNPLBw=" + invNPLBw + ", invOtherBw="
					+ invOtherBw + ", invTotBW=" + invTotBW + ", lastMileCostARC=" + lastMileCostARC
					+ ", lastMileCostNRC=" + lastMileCostNRC + ", lastModifiedDate=" + lastModifiedDate
					+ ", nPLARCPerBW=" + nPLARCPerBW + ", nPLPortARCAdjusted=" + nPLPortARCAdjusted
					+ ", nPLPortMRCAdjusted=" + nPLPortMRCAdjusted + ", nPLPortNRCAdjusted=" + nPLPortNRCAdjusted
					+ ", opportunityIDProdIdentifier=" + opportunityIDProdIdentifier + ", otherARCPerBW="
					+ otherARCPerBW + ", segmentCust=" + segmentCust + ", sumCAT12MACDFLAG=" + sumCAT12MACDFLAG
					+ ", sumCAT12NewOpportunityFLAG=" + sumCAT12NewOpportunityFLAG + ", sumCAT3MACDFLAG="
					+ sumCAT3MACDFLAG + ", sumCAT3NewOpportunityFLAG=" + sumCAT3NewOpportunityFLAG
					+ ", sumCAT4MACDFLAG=" + sumCAT4MACDFLAG + ", sumCAT4NewOpportunityFLAG="
					+ sumCAT4NewOpportunityFLAG + ", sumCat12Opp=" + sumCat12Opp + ", sumGVPNFlag=" + sumGVPNFlag
					+ ", sumIASFLAG=" + sumIASFLAG + ", sumMACDOpportunity=" + sumMACDOpportunity + ", sumNPLFlag="
					+ sumNPLFlag + ", sumNewARCConverted=" + sumNewARCConverted + ", sumNewARCConvertedGVPN="
					+ sumNewARCConvertedGVPN + ", sumNewARCConvertedILL=" + sumNewARCConvertedILL
					+ ", sumNewARCConvertedNPL=" + sumNewARCConvertedNPL + ", sumNewARCConvertedOther="
					+ sumNewARCConvertedOther + ", sumNewOpportunity=" + sumNewOpportunity + ", sumOtherFlag="
					+ sumOtherFlag + ", sumTotOppyHistoricOpp=" + sumTotOppyHistoricOpp + ", sumTotOppyHistoricProd="
					+ sumTotOppyHistoricProd + ", tOTARCPerBW=" + tOTARCPerBW + ", aLatitudeFinal=" + aLatitudeFinal
					+ ", aLocalLoopInterface=" + aLocalLoopInterface + ", aLongitudeFinal=" + aLongitudeFinal
					+ ", aRespCity=" + aRespCity + ", accountIdWith18Digit=" + accountIdWith18Digit
					+ ", bLatitudeFinal=" + bLatitudeFinal + ", bLocalLoopInterface=" + bLocalLoopInterface
					+ ", bLongitudeFinal=" + bLongitudeFinal + ", bRespCity=" + bRespCity + ", bwMbps=" + bwMbps
					+ ", chargeableDistance=" + chargeableDistance + ", createdDateQuote=" + createdDateQuote
					+ ", datediff=" + datediff + ", distBetwPops=" + distBetwPops + ", errorCode=" + errorCode
					+ ", errorFlag=" + errorFlag + ", errorMsg=" + errorMsg + ", fALmArcBwOnwl=" + fALmArcBwOnwl
					+ ", fALmNrcBwOnwl=" + fALmNrcBwOnwl + ", fALmNrcInbldgOnwl=" + fALmNrcInbldgOnwl
					+ ", fALmNrcMuxOnwl=" + fALmNrcMuxOnwl + ", fALmNrcNerentalOnwl=" + fALmNrcNerentalOnwl
					+ ", fALmNrcOspcapexOnwl=" + fALmNrcOspcapexOnwl + ", fBLmArcBwOnwl=" + fBLmArcBwOnwl
					+ ", fBLmNrcBwOnwl=" + fBLmNrcBwOnwl + ", fBLmNrcInbldgOnwl=" + fBLmNrcInbldgOnwl
					+ ", fBLmNrcMuxOnwl=" + fBLmNrcMuxOnwl + ", fBLmNrcNerentalOnwl=" + fBLmNrcNerentalOnwl
					+ ", fBLmNrcOspcapexOnwl=" + fBLmNrcOspcapexOnwl + ", feasibilityResponseCreatedDate="
					+ feasibilityResponseCreatedDate + ", histFlag=" + histFlag + ", intraInterFlag=" + intraInterFlag
					+ ", linkId=" + linkId + ", listPriceMb=" + listPriceMb + ", modelNameTransform="
					+ modelNameTransform + ", opportunityDay=" + opportunityDay + ", opportunityMonth="
					+ opportunityMonth + ", opportunityTerm=" + opportunityTerm + ", pALmArcBwOnwl=" + pALmArcBwOnwl
					+ ", pALmNrcBwOnwl=" + pALmNrcBwOnwl + ", pALmNrcInbldgOnwl=" + pALmNrcInbldgOnwl
					+ ", pALmNrcMuxOnwl=" + pALmNrcMuxOnwl + ", pALmNrcNerentalOnwl=" + pALmNrcNerentalOnwl
					+ ", pALmNrcOspcapexOnwl=" + pALmNrcOspcapexOnwl + ", pBLmArcBwOnwl=" + pBLmArcBwOnwl
					+ ", pBLmNrcBwOnwl=" + pBLmNrcBwOnwl + ", pBLmNrcInbldgOnwl=" + pBLmNrcInbldgOnwl
					+ ", pBLmNrcMuxOnwl=" + pBLmNrcMuxOnwl + ", pBLmNrcNerentalOnwl=" + pBLmNrcNerentalOnwl
					+ ", pBLmNrcOspcapexOnwl=" + pBLmNrcOspcapexOnwl + ", portLmArc=" + portLmArc
					+ ", portPredDiscount=" + portPredDiscount + ", predictedNPLPortARC=" + predictedNPLPortARC
					+ ", predictedNetPrice=" + predictedNetPrice + ", productFlavorTransform=" + productFlavorTransform
					+ ", productName=" + productName + ", prospectName=" + prospectName + ", quoteTypeQuote="
					+ quoteTypeQuote + ", quotetypeQuote=" + quotetypeQuote + ", siteId=" + siteId + ", slaVarient="
					+ slaVarient + ", sumCat12Opportunity=" + sumCat12Opportunity + ", sumCat3Opportunity="
					+ sumCat3Opportunity + ", sumCat4Opportunity=" + sumCat4Opportunity + ", sumModelName="
					+ sumModelName + ", timeTaken=" + timeTaken + ", totalContractValue=" + totalContractValue
					+ ", totalOspCapex=" + totalOspCapex + ", nplArcMgmntCharges=" + nplArcMgmntCharges
					+ ", nplMrcMgmntCharges=" + nplMrcMgmntCharges + ", rateCardFlag=" + rateCardFlag
					+ ", apiVersionNo=" + apiVersionNo + ", version=" + version + ", bucketAdjustmentType="
					+ bucketAdjustmentType + ", shiftCharge=" + shiftCharge + ", spALmArcBwBackhaulOnrf="
					+ spALmArcBwBackhaulOnrf + ", spALmArcBwOffwl=" + spALmArcBwOffwl + ", spALmArcBwOnrf="
					+ spALmArcBwOnrf + ", spALmArcBwOnwl=" + spALmArcBwOnwl + ", spALmArcColocationChargesOnrf="
					+ spALmArcColocationChargesOnrf + ", spALmArcColocationOnrf=" + spALmArcColocationOnrf
					+ ", spALmArcConverterChargesOnrf=" + spALmArcConverterChargesOnrf + ", spALmArcModemChargesOffwl="
					+ spALmArcModemChargesOffwl + ", spALmArcOffwl=" + spALmArcOffwl + ", spaALmArcOrderableBwOnwl="
					+ spaALmArcOrderableBwOnwl + ", spALmArcProwOnwl=" + spALmArcProwOnwl + ", spALmArcRadwinBwOnrf="
					+ spALmArcRadwinBwOnrf + ", spALmNrcBwOnrf=" + spALmNrcBwOnrf + ", spALmNrcBwOnwl=" + spALmNrcBwOnwl
					+ ", spALmNrcBwProvOfrf=" + spALmNrcBwProvOfrf + ", spALmNrcInbldgOnwl=" + spALmNrcInbldgOnwl
					+ ", spALmNrcMastOfrf=" + spALmNrcMastOfrf + ", spALmNrcMastOnrf=" + spALmNrcMastOnrf
					+ ", spALmNrcMuxOnwl=" + spALmNrcMuxOnwl + ", spALmNrcNerentalOnwl=" + spALmNrcNerentalOnwl
					+ ", spALmNrcNetworkCapexOnwl=" + spALmNrcNetworkCapexOnwl + ", spALmNrcOspcapexOnwl="
					+ spALmNrcOspcapexOnwl + ", spALmNrcProwOnwl=" + spALmNrcProwOnwl + ", spALmOtcModemChargesOffwl="
					+ spALmOtcModemChargesOffwl + ", spALmOtcNrcInstallationOffwl=" + spALmOtcNrcInstallationOffwl
					+ ", spALmOtcNrcOrderableBwOnwl=" + spALmOtcNrcOrderableBwOnwl + ", spBLmArcBwBackhaulOnrf="
					+ spBLmArcBwBackhaulOnrf + ", spBLmArcBwOffwl=" + spBLmArcBwOffwl + ", spBLmArcBwOnrf="
					+ spBLmArcBwOnrf + ", spBLmArcBwOnwl=" + spBLmArcBwOnwl + ", spBLmArcColocationChargesOnrf="
					+ spBLmArcColocationChargesOnrf + ", spBLmArcColocationOnrf=" + spBLmArcColocationOnrf
					+ ", spBLmArcConverterChargesOnrf=" + spBLmArcConverterChargesOnrf + ", spBLmArcModemChargesOffwl="
					+ spBLmArcModemChargesOffwl + ", spBLmArcOffwl=" + spBLmArcOffwl + ", spBLmArcOrderableBwOnwl="
					+ spBLmArcOrderableBwOnwl + ", spBLmArcProwOnwl=" + spBLmArcProwOnwl + ", spBLmArcRadwinBwOnrf="
					+ spBLmArcRadwinBwOnrf + ", spBLmNrcBwOnrf=" + spBLmNrcBwOnrf + ", spBLmNrcBwOnwl=" + spBLmNrcBwOnwl
					+ ", spBLmNrcBwProvOfrf=" + spBLmNrcBwProvOfrf + ", spBLmNrcInbldgOnwl=" + spBLmNrcInbldgOnwl
					+ ", spBLmNrcMastOfrf=" + spBLmNrcMastOfrf + ", spBLmNrcMastOnrf=" + spBLmNrcMastOnrf
					+ ", spBLmNrcMuxOnwl=" + spBLmNrcMuxOnwl + ", spBLmNrcNerentalOnwl=" + spBLmNrcNerentalOnwl
					+ ", spBLmNrcNetworkCapexOnwl=" + spBLmNrcNetworkCapexOnwl + ", spBLmNrcOspcapexOnwl="
					+ spBLmNrcOspcapexOnwl + ", spBLmNrcProwOnwl=" + spBLmNrcProwOnwl + ", spBLmOtcModemChargesOffwl="
					+ spBLmOtcModemChargesOffwl + ", spBLmOtcNrcInstallationOffwl=" + spBLmOtcNrcInstallationOffwl
					+ ", spBLmOtcNrcOrderableBwOnwl=" + spBLmOtcNrcOrderableBwOnwl + ", spPortArc=" + spPortArc
					+ ", spPortNrc=" + spPortNrc + ", spALmArcBwProvOfrf=" + spALmArcBwProvOfrf
					+ ", spBLmArcBwProvOfrf=" + spBLmArcBwProvOfrf + ", aOrchConnection=" + aOrchConnection
					+ ", bOrchConnection=" + bOrchConnection + ", aOrchLMType=" + aOrchLMType + ", bOrchLMType="
					+ bOrchLMType + ", aOrchCategory=" + aOrchCategory + ", bOrchCategory=" + bOrchCategory
					+ ", aLocalLoopBw=" + aLocalLoopBw + ", bLocalLoopBw=" + bLocalLoopBw + ", lmArcBwOnwl="
					+ lmArcBwOnwl + ", lmNrcBwOnwl=" + lmNrcBwOnwl + ", lmArcProwOnwl=" + lmArcProwOnwl
					+ ", lmNrcInbldgOnwl=" + lmNrcInbldgOnwl + ", lmNrcMuxOnwl=" + lmNrcMuxOnwl + ", lmNrcNerentalOnwl="
					+ lmNrcNerentalOnwl + ", lmNrcOspcapexOnwl=" + lmNrcOspcapexOnwl + ", lmNrcProwOnwl="
					+ lmNrcProwOnwl + ", lmArcBwBackhaulOnrf=" + lmArcBwBackhaulOnrf + ", lmArcBwOnrf=" + lmArcBwOnrf
					+ ", lmArcColocationChargesOnrf=" + lmArcColocationChargesOnrf + ", lmArcConverterChargesOnrf="
					+ lmArcConverterChargesOnrf + ", lmNrcBwOnrf=" + lmNrcBwOnrf + ", lmNrcMastOnrf=" + lmNrcMastOnrf
					+ ", lmArcBwOffwl=" + lmArcBwOffwl + ", lmArcModemChargesOffwl=" + lmArcModemChargesOffwl
					+ ", lmOtcModemChargesOffwl=" + lmOtcModemChargesOffwl + ", lmOtcNrcInstallationOffwl="
					+ lmOtcNrcInstallationOffwl + ", lmNrcBwProvOfrf=" + lmNrcBwProvOfrf + ", lmArcBwProvOfrf="
					+ lmArcBwProvOfrf + ", lmNrcMastOfrf=" + lmNrcMastOfrf + ", abandwidth=" + abandwidth
					+ ", bbandwidth=" + bbandwidth + ", PortARCLP=" + PortARCLP + ", PortNRCLP=" + PortNRCLP
					+ ", rcFlag=" + rcFlag + ", PortLP=" + PortLP + ", CPERentalMRC=" + CPERentalMRC + ", aLmNrcBwOnwl="
					+ aLmNrcBwOnwl + ", aLmNrcBwOnrf=" + aLmNrcBwOnrf + ", aLmNrcBwProvOfrf=" + aLmNrcBwProvOfrf
					+ ", bLmNrcBwOnwl=" + bLmNrcBwOnwl + ", bLmNrcBwOnrf=" + bLmNrcBwOnrf + ", bLmNrcBwProvOfrf="
					+ bLmNrcBwProvOfrf + ", aLmArcBwOnwl=" + aLmArcBwOnwl + ", aLmArcBwOnrf=" + aLmArcBwOnrf
					+ ", aLmArcBwProvOfrf=" + aLmArcBwProvOfrf + ", bLmArcBwOnwl=" + bLmArcBwOnwl + ", bLmArcBwOnrf="
					+ bLmArcBwOnrf + ", bLmArcBwProvOfrf=" + bLmArcBwProvOfrf + ", aLmNrcMastOnrf=" + aLmNrcMastOnrf
					+ ", aLmNrcMastOfrf=" + aLmNrcMastOfrf + ", pALmNrcMastOnrf=" + pALmNrcMastOnrf
					+ ", pALmNrcMastOfrf=" + pALmNrcMastOfrf + ", fALmNrcBwProvOfrf=" + fALmNrcBwProvOfrf
					+ ", fALmNrcBwOnrf=" + fALmNrcBwOnrf + ", fBLmNrcBwProvOfrf=" + fBLmNrcBwProvOfrf
					+ ", fALmArcBwOnrf=" + fALmArcBwOnrf + ", fALmArcBwProvOfrf=" + fALmArcBwProvOfrf
					+ ", fBLmArcBwOnrf=" + fBLmArcBwOnrf + ", fBLmArcBwProvOfrf=" + fBLmArcBwProvOfrf
					+ ", fALmNrcMastOnrf=" + fALmNrcMastOnrf + ", fALmNrcMastOfrf=" + fALmNrcMastOfrf
					+ ", fBLmNrcBwOnrf=" + fBLmNrcBwOnrf + "]";
		}
		
		
		
}

package com.tcl.dias.oms.gvpn.pricing.bean;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
/**
 * This file contains the AddressDetails.java class.
 * 
 *
 * @author Paulraj 
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
    "Bucket_Adjustment_Type",
    "RP_Actual_Price_Per_Mb",
    "RP_Actual_Records_Count",
    "RP_Asked_Bldng_Coverage",
    "RP_Bandwidth",
    "RP_Bandwidth_Mb",
    "RP_Bw_Band",
    "RP_Cluster",
    "RP_Contract_Term_Months",
    "RP_Country_Id",
    "RP_Coverage_Type",
    "RP_Curr_Year_Pq_Records_Count",
    "RP_Currency",
    "RP_Dist_Prc_Closest_Building",
    "RP_Exception_Code",
    "RP_Final_Price_Per_Mb",
    "RP_Frequency",
    "RP_Inter_Connection_Type",
    "RP_Intercept_Pq_Bw",
    "RP_Intercept_Prc_Valid",
    "RP_Interface",
    "RP_Is_Actual_Avlbl",
    "RP_Is_Bw_Trend_Avlbl",
    "RP_Is_Curr_Yr_Pq_Avlbl",
    "RP_Is_Exact_Match_To_Act_Avlbl",
    "RP_Is_Pq_To_Act_Avlbl",
    "RP_Is_Prc_To_Act_Avlbl",
    "RP_Is_Prc_To_Pq_Avlbl",
    "RP_Is_Rate_Card_Avlbl",
    "RP_Is_Valid_Prc_Avlbl",
    "RP_Ll_Mrc",
    "RP_Ll_Nrc",
    "RP_Mrc_Bw_Usd_Mean_Pq",
    "RP_New_Intercept_Bw",
    "RP_New_Slope_Log_Bw",
    "RP_Obs_Count_Pq_Bw",
    "RP_Pop_Address",
    "RP_Pop_Code",
    "RP_Pq_Regression_Line_Year",
    "RP_Pq_To_Act_Adj",
    "RP_Prc_Closest_Building",
    "RP_Prc_Cluster",
    "RP_Prc_Price_Per_Mb",
    "RP_Prc_To_Act_Adj",
    "RP_Prc_To_Pq_Adj",
    "RP_Predicted_Price",
    "RP_Predicted_Price_Per_Mb_Pq",
    "RP_Provider_Name",
    "RP_Provider_Product_Name",
    "RP_Quotation_ID",
    "RP_Quote_Category",
    "RP_Quote_Created_Date",
    "RP_Quote_No",
    "RP_Shifted_Price_Per_Mb_Pq",
    "RP_Slope_Log_Bw_Prc_Valid",
    "RP_Slope_Log_Pq_Bw",
    "RP_Termdiscountmrc24Months",
    "RP_Termdiscountmrc36Months",
    "RP_Total_Mrc",
    "RP_Total_Nrc",
    "RP_Valid_Prc_End_Date",
    "RP_Valid_Prc_Records_Count",
    "RP_Valid_Prc_Start_Date",
    "RP_Vendor_Name",
    "RP_Xc_Mrc",
    "RP_Xc_Nrc",
    "RP_Xconnect_Provider_Name",
    "RQ_Bandwidth",
    "RQ_Contract_Term_Months",
    "RQ_Country",
    "RQ_Lat",
    "RQ_Long",
    "RQ_Product_Type",
    "RQ_Tcl_Pop_Short_Code",
    "RQ_User_Name",
    "Type",
    "account_id_with_18_digit",
    "additional_ip_flag",
    "backup_port_requested",
    "burstable_bw",
    "bw_mbps",
    "connection_type",
    "country",
    "cpe_management_type",
    "cpe_supply_type",
    "cpe_variant",
    "cu_le_id",
    "customer_segment",
    "feasibility_response_created_date",
    "ip_address_arrangement",
    "ipv4_address_pool_size",
    "ipv6_address_pool_size",
    "last_mile_contract_term",
    "latitude_final",
    "local_loop_interface",
    "longitude_final",
    "opportunity_term",
    "product_name",
    "prospect_name",
    "quotetype_quote",
    "rank",
    "resp_city",
    "sales_org",
    "siteFlag",
    "site_id",
    "sum_no_of_sites_uni_len",
    "topology",
    "topology_secondary",
    "Selected",
    "bw",
    "cluster_id",
    "contract_term_with_vendor_months",
    "db_code",
    "distance_to_pop",
    "mrc_bw",
    "mrc_bw_hat",
    "priority",
    "provider_name",
    "provider_product_name",
    "related_quotes",
    "source",
    "xconnect_cur",
    "xconnect_mrc",
    "xconnect_nrc",
    "bw_band",
    "otc_nrc_installation_hat",
    "quote_id",
    "rqi_bw",
    "rqi_lat",
    "rqi_long",
    "rqi_tcl_prod",
    "tcl_pop_address",
    "tcl_pop_short_code",
    "xconnect_cur_hat",
    "xconnect_mrc_hat",
    "xconnect_nrc_hat",
    "ll_interface",
    
    "mrc_bw_bkp",
    "selected_quote",
    "error_code",
    "error_msg",
    "error_flag",
    "row_id",
    "priority_rank",
    "Predicted_Access_Feasibility",
    "solution_type",
    "partner_account_id_with_18_digit",
    "partner_profile",
    "quotetype_partner",
    "version",
    "provider_Provider_Product_Name_Subset",
    "lm_arc_bw_onwl", "lm_nrc_bw_onwl",
	"lm_nrc_mux_onwl", "lm_nrc_inbldg_onwl", "lm_nrc_ospcapex_onwl", "lm_nrc_nerental_onwl", "lm_arc_bw_prov_ofrf",
	"lm_nrc_bw_prov_ofrf", "lm_nrc_mast_ofrf", "lm_arc_bw_onrf", "lm_nrc_bw_onrf", "lm_nrc_mast_onrf", "GVPN_Port_MRC_Adjusted",
    "GVPN_Port_NRC_Adjusted", "CPE_Installation_charges", "support_charges", "recovery", "management", "SFP_lp",
    "customs_local_taxes", "Logistics_cost", "provider_MRCCost", "provider_NRCCost", "x_connect_Xconnect_MRC",
    "x_connect_Xconnect_NRC","izo_sdwan",
    "macd_service",
    //CST-21 
    "is_customer"

})
public class IntlNotFeasible {

    @JsonProperty("Bucket_Adjustment_Type")
    private String bucketAdjustmentType;
    @JsonProperty("RP_Actual_Price_Per_Mb")
    private String rPActualPricePerMb;
    @JsonProperty("RP_Actual_Records_Count")
    private String rPActualRecordsCount;
    @JsonProperty("RP_Asked_Bldng_Coverage")
    private String rPAskedBldngCoverage;
    @JsonProperty("RP_Bandwidth")
    private String rPBandwidth;
    @JsonProperty("RP_Bandwidth_Mb")
    private String rPBandwidthMb;
    @JsonProperty("RP_Bw_Band")
    private String rPBwBand;
    @JsonProperty("RP_Cluster")
    private String rPCluster;
    @JsonProperty("RP_Contract_Term_Months")
    private String rPContractTermMonths;
    @JsonProperty("RP_Country_Id")
    private String rPCountryId;
    @JsonProperty("RP_Coverage_Type")
    private String rPCoverageType;
    @JsonProperty("RP_Curr_Year_Pq_Records_Count")
    private String rPCurrYearPqRecordsCount;
    @JsonProperty("RP_Currency")
    private String rPCurrency;
    @JsonProperty("RP_Dist_Prc_Closest_Building")
    private String rPDistPrcClosestBuilding;
    @JsonProperty("RP_Exception_Code")
    private String rPExceptionCode;
    @JsonProperty("RP_Final_Price_Per_Mb")
    private String rPFinalPricePerMb;
    @JsonProperty("RP_Frequency")
    private String rPFrequency;
    @JsonProperty("RP_Inter_Connection_Type")
    private String rPInterConnectionType;
    @JsonProperty("RP_Intercept_Pq_Bw")
    private String rPInterceptPqBw;
    @JsonProperty("RP_Intercept_Prc_Valid")
    private String rPInterceptPrcValid;
    @JsonProperty("RP_Interface")
    private String rPInterface;
    @JsonProperty("RP_Is_Actual_Avlbl")
    private String rPIsActualAvlbl;
    @JsonProperty("RP_Is_Bw_Trend_Avlbl")
    private String rPIsBwTrendAvlbl;
    @JsonProperty("RP_Is_Curr_Yr_Pq_Avlbl")
    private String rPIsCurrYrPqAvlbl;
    @JsonProperty("RP_Is_Exact_Match_To_Act_Avlbl")
    private String rPIsExactMatchToActAvlbl;
    @JsonProperty("RP_Is_Pq_To_Act_Avlbl")
    private String rPIsPqToActAvlbl;
    @JsonProperty("RP_Is_Prc_To_Act_Avlbl")
    private String rPIsPrcToActAvlbl;
    @JsonProperty("RP_Is_Prc_To_Pq_Avlbl")
    private String rPIsPrcToPqAvlbl;
    @JsonProperty("RP_Is_Rate_Card_Avlbl")
    private String rPIsRateCardAvlbl;
    @JsonProperty("RP_Is_Valid_Prc_Avlbl")
    private String rPIsValidPrcAvlbl;
    @JsonProperty("RP_Ll_Mrc")
    private String rPLlMrc;
    @JsonProperty("RP_Ll_Nrc")
    private String rPLlNrc;
    @JsonProperty("RP_Mrc_Bw_Usd_Mean_Pq")
    private String rPMrcBwUsdMeanPq;
    @JsonProperty("RP_New_Intercept_Bw")
    private String rPNewInterceptBw;
    @JsonProperty("RP_New_Slope_Log_Bw")
    private String rPNewSlopeLogBw;
    @JsonProperty("RP_Obs_Count_Pq_Bw")
    private String rPObsCountPqBw;
    @JsonProperty("RP_Pop_Address")
    private String rPPopAddress;
    @JsonProperty("RP_Pop_Code")
    private String rPPopCode;
    @JsonProperty("RP_Pq_Regression_Line_Year")
    private String rPPqRegressionLineYear;
    @JsonProperty("RP_Pq_To_Act_Adj")
    private String rPPqToActAdj;
    @JsonProperty("RP_Prc_Closest_Building")
    private String rPPrcClosestBuilding;
    @JsonProperty("RP_Prc_Cluster")
    private String rPPrcCluster;
    @JsonProperty("RP_Prc_Price_Per_Mb")
    private String rPPrcPricePerMb;
    @JsonProperty("RP_Prc_To_Act_Adj")
    private String rPPrcToActAdj;
    @JsonProperty("RP_Prc_To_Pq_Adj")
    private String rPPrcToPqAdj;
    @JsonProperty("RP_Predicted_Price")
    private String rPPredictedPrice;
    @JsonProperty("RP_Predicted_Price_Per_Mb_Pq")
    private String rPPredictedPricePerMbPq;
    @JsonProperty("RP_Provider_Name")
    private String rPProviderName;
    @JsonProperty("RP_Provider_Product_Name")
    private String rPProviderProductName;
    @JsonProperty("RP_Quotation_ID")
    private String rPQuotationID;
    @JsonProperty("RP_Quote_Category")
    private String rPQuoteCategory;
    @JsonProperty("RP_Quote_Created_Date")
    private String rPQuoteCreatedDate;
    @JsonProperty("RP_Quote_No")
    private String rPQuoteNo;
    @JsonProperty("RP_Shifted_Price_Per_Mb_Pq")
    private String rPShiftedPricePerMbPq;
    @JsonProperty("RP_Slope_Log_Bw_Prc_Valid")
    private String rPSlopeLogBwPrcValid;
    @JsonProperty("RP_Slope_Log_Pq_Bw")
    private String rPSlopeLogPqBw;
    @JsonProperty("RP_Termdiscountmrc24Months")
    private String rPTermdiscountmrc24Months;
    @JsonProperty("RP_Termdiscountmrc36Months")
    private String rPTermdiscountmrc36Months;
    @JsonProperty("RP_Total_Mrc")
    private String rPTotalMrc;
    @JsonProperty("RP_Total_Nrc")
    private String rPTotalNrc;
    @JsonProperty("RP_Valid_Prc_End_Date")
    private String rPValidPrcEndDate;
    @JsonProperty("RP_Valid_Prc_Records_Count")
    private String rPValidPrcRecordsCount;
    @JsonProperty("RP_Valid_Prc_Start_Date")
    private String rPValidPrcStartDate;
    @JsonProperty("RP_Vendor_Name")
    private String rPVendorName;
    @JsonProperty("RP_Xc_Mrc")
    private String rPXcMrc;
    @JsonProperty("RP_Xc_Nrc")
    private String rPXcNrc;
    @JsonProperty("RP_Xconnect_Provider_Name")
    private String rPXconnectProviderName;
    @JsonProperty("RQ_Bandwidth")
    private String rQBandwidth;
    @JsonProperty("RQ_Contract_Term_Months")
    private String rQContractTermMonths;
    @JsonProperty("RQ_Country")
    private String rQCountry;
    @JsonProperty("RQ_Lat")
    private String rQLat;
    @JsonProperty("RQ_Long")
    private String rQLong;
    @JsonProperty("RQ_Product_Type")
    private String rQProductType;
    @JsonProperty("RQ_Tcl_Pop_Short_Code")
    private String rQTclPopShortCode;
    @JsonProperty("RQ_User_Name")
    private String rQUserName;
    @JsonProperty("Type")
    private String type;
    @JsonProperty("account_id_with_18_digit")
    private String accountIdWith18Digit;
    @JsonProperty("additional_ip_flag")
    private String additionalIpFlag;
    @JsonProperty("backup_port_requested")
    private String backupPortRequested;
    @JsonProperty("burstable_bw")
    private Double burstableBw;
    @JsonProperty("bw_mbps")
    private Double bwMbps;
    @JsonProperty("connection_type")
    private String connectionType;
    @JsonProperty("country")
    private String country;
    @JsonProperty("cpe_management_type")
    private String cpeManagementType;
    @JsonProperty("cpe_supply_type")
    private String cpeSupplyType;
    @JsonProperty("cpe_variant")
    private String cpeVariant;
    @JsonProperty("cu_le_id")
    private String cuLeId;
    @JsonProperty("customer_segment")
    private String customerSegment;
    @JsonProperty("feasibility_response_created_date")
    private String feasibilityResponseCreatedDate;
    @JsonProperty("ip_address_arrangement")
    private String ipAddressArrangement;
    @JsonProperty("ipv4_address_pool_size")
    private String ipv4AddressPoolSize;
    @JsonProperty("ipv6_address_pool_size")
    private String ipv6AddressPoolSize;
    @JsonProperty("last_mile_contract_term")
    private String lastMileContractTerm;
    @JsonProperty("latitude_final")
    private Float latitudeFinal;
    @JsonProperty("local_loop_interface")
    private String localLoopInterface;
    @JsonProperty("longitude_final")
    private Float longitudeFinal;
    @JsonProperty("opportunity_term")
    private Integer opportunityTerm;
    @JsonProperty("product_name")
    private String productName;
    @JsonProperty("prospect_name")
    private String prospectName;
    @JsonProperty("quotetype_quote")
    private String quotetypeQuote;
    @JsonProperty("rank")
    private String rank;
    @JsonProperty("resp_city")
    private String respCity;
    @JsonProperty("sales_org")
    private String salesOrg;
    @JsonProperty("siteFlag")
    private String siteFlag;
    @JsonProperty("site_id")
    private String siteId;
    @JsonProperty("sum_no_of_sites_uni_len")
    private Integer sumNoOfSitesUniLen;
    @JsonProperty("topology")
    private String topology;
    @JsonProperty("topology_secondary")
    private String topologySecondary;
    @JsonProperty("Selected")
    private Boolean selected;
    @JsonProperty("bw")
    private String bw;
    @JsonProperty("cluster_id")
    private String clusterId;
    @JsonProperty("contract_term_with_vendor_months")
    private String contractTermWithVendorMonths;
    @JsonProperty("distance_to_pop")
    private String distanceToPop;
    @JsonProperty("mrc_bw")
    private String mrcBw;
    @JsonProperty("mrc_bw_hat")
    private String mrcBwHat;
    @JsonProperty("priority")
    private String priority;
    @JsonProperty("provider_name")
    private String providerName;
    @JsonProperty("provider_product_name")
    private String providerProductName;
    @JsonProperty("Predicted_Access_Feasibility")
    private String predictedAccessFeasibility;
    @JsonProperty("db_code")
    private String dbCode;
    @JsonProperty("related_quotes")
    private List<RelatedQuote> relatedQuotes = null;
    @JsonProperty("source")
    private String source;
    @JsonProperty("xconnect_cur")
    private String xconnectCur;
    @JsonProperty("xconnect_mrc")
    private String xconnectMrc;
    @JsonProperty("xconnect_nrc")
    private String xconnectNrc;

    @JsonProperty("bw_band")
    private String bwBand;
    @JsonProperty("otc_nrc_installation_hat")
    private Double otcNrcInstallationHat;
    @JsonProperty("quote_id")
    private String quoteId;
    @JsonProperty("rqi_bw")
    private String rqiBw;
    @JsonProperty("rqi_lat")
    private Double rqiLat;
    @JsonProperty("rqi_long")
    private Double rqiLong;
    @JsonProperty("rqi_tcl_prod")
    private String rqiTclProd;
    @JsonProperty("tcl_pop_address")
    private String tclPopAddress;
    @JsonProperty("tcl_pop_short_code")
    private List<String> tclPopShortCode=null;
    @JsonProperty("xconnect_cur_hat")
    private String xconnectCurHat;
    @JsonProperty("xconnect_mrc_hat")
    private Integer xconnectMrcHat;
    @JsonProperty("xconnect_nrc_hat")
    private Integer xconnectNrcHat;
    @JsonProperty("ll_interface")
    private String llInterface;
    
    @JsonProperty("mrc_bw_bkp")
    private String mrcBwBkp;
    @JsonProperty("selected_quote")
    private String selectedQuote;
    @JsonProperty("error_code")
    private String errorCode;
    @JsonProperty("error_msg")
    private String errorMsg;
    @JsonProperty("error_flag")
    private String errorFlag;
    @JsonProperty("row_id")
    private String rowId;
    @JsonProperty("priority_rank")
    private String priorityRank;
    @JsonProperty("solution_type")
    private String solutionType;
    @JsonProperty("partner_account_id_with_18_digit")
    private String partnerAccountIdWith18Digit;
    @JsonProperty("partner_profile")
    private String partnerProfile;
    @JsonProperty("quotetype_partner")
    private String quoteTypePartner;
    
    @JsonProperty("version")
   	private String version;
    @JsonProperty("provider_Provider_Name")
    private String providerProviderName;
    @JsonProperty("provider_Provider_Product_Name")
    private String providerProviderProductName;
    @JsonProperty("provider_Provider_Product_Name_Subset")
    private String providerProviderProductNameSubset;
    @JsonProperty("provider_Inter_connection_Type")
    private String providerInterConnectionType;
    @JsonProperty("provider_coverage")
    private String providerCoverage;
    @JsonProperty("provider_country_ID")
    private float providerCountryID;
    @JsonProperty("provider_quote_category_short")
    private String providerQuoteCategoryShort;
    @JsonProperty("provider_error")
    private String providerError;
    @JsonProperty("provider_isManualQuote")
    private float providerIsManualQuote;
    @JsonProperty("provider_MRC_BW_Currency")
    private String provider_MRC_BW_Currency;
    @JsonProperty("provider_MRC_BW_Currency_access")
    private String provider_MRC_BW_Currency_Access;
    @JsonProperty("provider_Local_Loop_Interface")
    private String providerLocalLoopInterface;
    @JsonProperty("provider_Xconnect_MRC")
    private float providerXconnectMRC;
    @JsonProperty("provider_Xconnect_NRC")
    private float providerXconnectNRC;
    @JsonProperty("provider_info")
    private String providerInfo;
    @JsonProperty("provider_Provider_Product_Name_SubSet")
    private String providerProviderProductNameSubSet;
    @JsonProperty("provider_Local_Loop_Capacity")
    private String providerLocalLoopCapacity;
    @JsonProperty("provider_MRC_BW")
    private float providerMRCBW;
    @JsonProperty("provider_OTC_NRC_Installation")
    private float providerOTCNRCInstallation;
    @JsonProperty("provider_TermDiscountMRC24months")
    private float providerTermDiscountMRC24months;
    @JsonProperty("provider_TermDiscountNRC24months")
    private float providerTermDiscountNRC24months;
    @JsonProperty("provider_TermDiscountMRC36months")
    private float providerTermDiscountMRC36months;
    @JsonProperty("provider_TermDiscountNRC36months")
    private float providerTermDiscountNRC36months;
    @JsonProperty("provider_ct_12")
    private float providerCt12;
    @JsonProperty("provider_ct_24")
    private float providerCt24;
    @JsonProperty("provider_ct_36")
    private float providerCt36;
    @JsonProperty("total_MRC")
    private float totalMRC;
    @JsonProperty("parallel_run_days")
    private String parallelRunDays;
    @JsonProperty("lat_long")
    private String latLong;
    @JsonProperty("intf_type")
    private String intfType;
    @JsonProperty("cpe_chassis_changed")
    private String cpeChassisChanged;
    @JsonProperty("zip_code")
    private String zipCode;
    @JsonProperty("vpn_Name")
    private String vpnName;
    @JsonProperty("lat")
    private String lat;
    @JsonProperty("longitude")
    private String longitude;
    @JsonProperty("info")
    private String info;
    @JsonProperty("local_loop_bw")
    private float localLoopBw;
    @JsonProperty("macd_option")
    private String macdOption;
    @JsonProperty("bwunit")
    private String bwunit;
    @JsonProperty("zipcode")
    private String zipcode;
    @JsonProperty("trigger_feasibility")
    private String triggerFeasibility;
    @JsonProperty("quotetype_partner")
    private String quotetypePartner;
    @JsonProperty("service_commissioned_date")
    private String serviceCommissionedDate;
    @JsonProperty("old_Ll_Bw")
    private String oldLlBw;
    @JsonProperty("latitude")
    private String latitude;
    @JsonProperty("old_contract_term")
    private String oldContractTerm;
    @JsonProperty("error")
    private String error;
    @JsonProperty("feasibility_response_created_date")
    private String feasibilityResponseCreateDate;
    @JsonProperty("longg")
    private String longg;
    @JsonProperty("product_solution")
    private String productSolution;
    @JsonProperty("service_id")
    private String serviceId;
    @JsonProperty("term")
    private String term;
    @JsonProperty("product")
    private String product;
    @JsonProperty("address")
    private String address;
    @JsonIgnore
    @JsonProperty("bandwidth")
    private float bandwidth;
    @JsonProperty("ll_change")
    private String llChange;
    @JsonProperty("old_Port_Bw")
    private String oldPortBw;
    @JsonProperty("feasiblity_id")
    private String feasiblityId;
    @JsonProperty("x_connect_isInterfaceChanged")
    private String xconnectIsInterfaceChanged;
    @JsonProperty("x_connect_Xconnect_MRC_Currency")
    private String xConnectXconnectMRCCurrency;
    @JsonProperty("x_connect_Xconnect_MRC_Currency_access")
    private String xConnectXconnectMRCCurrency_Access;
    @JsonProperty("x_connect_Xconnect_MRC")
    private String xConnectXconnectMRC;
    @JsonProperty("x_connect_Xconnect_NRC")
    private String xConnectXconnectNRC;
    @JsonProperty("cloud_provider")
    private String cloudProvider;
    @JsonProperty("provider_Xconnect_Interface")
    private String providerXconnectInterface;
    @JsonProperty("LM_type")
    private String LMType;
    @JsonProperty("record_type")
    private String recordType;
    @JsonProperty("x_connect_distance")
    private String xConnectDistance;
    @JsonProperty("x_connect_DB_Code")
    private String xConnectDBCode;
    @JsonProperty("x_connect_Country_ID")
    private String xConnectCountryID;
    @JsonProperty("x_connect_Country")
    private String xConnectCountry;
    @JsonProperty("x_connect_City")
    private String xConnectCity;
    @JsonProperty("x_connect_TCL_POP_Address")
    private String xConnectTCLPOPAddress;
    @JsonProperty("x_connect_TCL_POP_Latitude")
    private String xConnectTCLPOPLatitude;
    @JsonProperty("x_connect_TCL_POP_Longitude")
    private String xConnectTCLPOPLongitude;
    @JsonProperty("x_connect_EAS")
    private String xConnectEAS;
    @JsonProperty("x_connect_All_Sites_Long")
    private String xConnectAllSitesLong;
    @JsonProperty("x_connect_is_selected")
    private String xConnectIsSelected;
    @JsonProperty("x_connect_Xconnect_Interface")
    private String xConnectXconnectInterface;
    @JsonProperty("x_connect_error")
    private String xConnectError;
    @JsonProperty("x_connect_isManualQuote")
    private String xConnectIsManualQuote;
    @JsonProperty("x_connect_XC_Media")
    private String xConnectXCMedia;
    @JsonProperty("x_connect_Provider_Product_Name")
    private String xConnectProviderProductName;
    @JsonProperty("x_connect_cross_connect_charges")
    private String xConnectCrossConnectCharges;
    
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
    private String providerMRCCost;

    @JsonProperty("provider_NRCCost")
    private String providerNRCCost;
    @JsonProperty("provider_pop_all_site_lat_long")
   	private String providerPopAllSiteLatLong;
    @JsonProperty("provider_pop_addresses")
   	private String providerPopAddresses;
    @JsonProperty("x_connect_Xconnect_Provider_Name")
    private String xConnectXConnectProviderName;
    @JsonProperty("cpe_intl_chassis_flag")
    private String cpeIntlChassisFlag;
    
    @JsonProperty("macd_service")
	private String macdService;
    
    @JsonProperty("is_customer")
    private String isCustomer;
    
    @JsonProperty("macd_service")
	public String getMacdService() {
		return macdService;
	}

	@JsonProperty("macd_service")
	public void setMacdService(String macdService) {
		this.macdService = macdService;
	}
    
    @JsonProperty("izo_sdwan")
	private String izoSdwan;
	
	public String getIzoSdwan() {
		return izoSdwan;
	}

	public void setIzoSdwan(String izoSdwan) {
		this.izoSdwan = izoSdwan;
	}
    
    public String getVersion() {
   		return version;
   	}

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
    
    
    @JsonProperty("Bucket_Adjustment_Type")
    public String getBucketAdjustmentType() {
        return bucketAdjustmentType;
    }

    @JsonProperty("Bucket_Adjustment_Type")
    public void setBucketAdjustmentType(String bucketAdjustmentType) {
        this.bucketAdjustmentType = bucketAdjustmentType;
    }

    @JsonProperty("RP_Actual_Price_Per_Mb")
    public String getRPActualPricePerMb() {
        return rPActualPricePerMb;
    }

    @JsonProperty("RP_Actual_Price_Per_Mb")
    public void setRPActualPricePerMb(String rPActualPricePerMb) {
        this.rPActualPricePerMb = rPActualPricePerMb;
    }

    @JsonProperty("RP_Actual_Records_Count")
    public String getRPActualRecordsCount() {
        return rPActualRecordsCount;
    }

    @JsonProperty("RP_Actual_Records_Count")
    public void setRPActualRecordsCount(String rPActualRecordsCount) {
        this.rPActualRecordsCount = rPActualRecordsCount;
    }

    @JsonProperty("RP_Asked_Bldng_Coverage")
    public String getRPAskedBldngCoverage() {
        return rPAskedBldngCoverage;
    }

    @JsonProperty("RP_Asked_Bldng_Coverage")
    public void setRPAskedBldngCoverage(String rPAskedBldngCoverage) {
        this.rPAskedBldngCoverage = rPAskedBldngCoverage;
    }

    @JsonProperty("RP_Bandwidth")
    public String getRPBandwidth() {
        return rPBandwidth;
    }

    @JsonProperty("RP_Bandwidth")
    public void setRPBandwidth(String rPBandwidth) {
        this.rPBandwidth = rPBandwidth;
    }

    @JsonProperty("RP_Bandwidth_Mb")
    public String getRPBandwidthMb() {
        return rPBandwidthMb;
    }

    @JsonProperty("RP_Bandwidth_Mb")
    public void setRPBandwidthMb(String rPBandwidthMb) {
        this.rPBandwidthMb = rPBandwidthMb;
    }

    @JsonProperty("RP_Bw_Band")
    public String getRPBwBand() {
        return rPBwBand;
    }

    @JsonProperty("RP_Bw_Band")
    public void setRPBwBand(String rPBwBand) {
        this.rPBwBand = rPBwBand;
    }

    @JsonProperty("RP_Cluster")
    public String getRPCluster() {
        return rPCluster;
    }

    @JsonProperty("RP_Cluster")
    public void setRPCluster(String rPCluster) {
        this.rPCluster = rPCluster;
    }

    @JsonProperty("RP_Contract_Term_Months")
    public String getRPContractTermMonths() {
        return rPContractTermMonths;
    }

    @JsonProperty("RP_Contract_Term_Months")
    public void setRPContractTermMonths(String rPContractTermMonths) {
        this.rPContractTermMonths = rPContractTermMonths;
    }

    @JsonProperty("RP_Country_Id")
    public String getRPCountryId() {
        return rPCountryId;
    }

    @JsonProperty("RP_Country_Id")
    public void setRPCountryId(String rPCountryId) {
        this.rPCountryId = rPCountryId;
    }

    @JsonProperty("RP_Coverage_Type")
    public String getRPCoverageType() {
        return rPCoverageType;
    }

    @JsonProperty("RP_Coverage_Type")
    public void setRPCoverageType(String rPCoverageType) {
        this.rPCoverageType = rPCoverageType;
    }

    @JsonProperty("RP_Curr_Year_Pq_Records_Count")
    public String getRPCurrYearPqRecordsCount() {
        return rPCurrYearPqRecordsCount;
    }

    @JsonProperty("RP_Curr_Year_Pq_Records_Count")
    public void setRPCurrYearPqRecordsCount(String rPCurrYearPqRecordsCount) {
        this.rPCurrYearPqRecordsCount = rPCurrYearPqRecordsCount;
    }

    @JsonProperty("RP_Currency")
    public String getRPCurrency() {
        return rPCurrency;
    }

    @JsonProperty("RP_Currency")
    public void setRPCurrency(String rPCurrency) {
        this.rPCurrency = rPCurrency;
    }

    @JsonProperty("RP_Dist_Prc_Closest_Building")
    public String getRPDistPrcClosestBuilding() {
        return rPDistPrcClosestBuilding;
    }

    @JsonProperty("RP_Dist_Prc_Closest_Building")
    public void setRPDistPrcClosestBuilding(String rPDistPrcClosestBuilding) {
        this.rPDistPrcClosestBuilding = rPDistPrcClosestBuilding;
    }

    @JsonProperty("RP_Exception_Code")
    public String getRPExceptionCode() {
        return rPExceptionCode;
    }

    @JsonProperty("RP_Exception_Code")
    public void setRPExceptionCode(String rPExceptionCode) {
        this.rPExceptionCode = rPExceptionCode;
    }

    @JsonProperty("RP_Final_Price_Per_Mb")
    public String getRPFinalPricePerMb() {
        return rPFinalPricePerMb;
    }

    @JsonProperty("RP_Final_Price_Per_Mb")
    public void setRPFinalPricePerMb(String rPFinalPricePerMb) {
        this.rPFinalPricePerMb = rPFinalPricePerMb;
    }

    @JsonProperty("RP_Frequency")
    public String getRPFrequency() {
        return rPFrequency;
    }

    @JsonProperty("RP_Frequency")
    public void setRPFrequency(String rPFrequency) {
        this.rPFrequency = rPFrequency;
    }

    @JsonProperty("RP_Inter_Connection_Type")
    public String getRPInterConnectionType() {
        return rPInterConnectionType;
    }

    @JsonProperty("RP_Inter_Connection_Type")
    public void setRPInterConnectionType(String rPInterConnectionType) {
        this.rPInterConnectionType = rPInterConnectionType;
    }

    @JsonProperty("RP_Intercept_Pq_Bw")
    public String getRPInterceptPqBw() {
        return rPInterceptPqBw;
    }

    @JsonProperty("RP_Intercept_Pq_Bw")
    public void setRPInterceptPqBw(String rPInterceptPqBw) {
        this.rPInterceptPqBw = rPInterceptPqBw;
    }

    @JsonProperty("RP_Intercept_Prc_Valid")
    public String getRPInterceptPrcValid() {
        return rPInterceptPrcValid;
    }

    @JsonProperty("RP_Intercept_Prc_Valid")
    public void setRPInterceptPrcValid(String rPInterceptPrcValid) {
        this.rPInterceptPrcValid = rPInterceptPrcValid;
    }

    @JsonProperty("RP_Interface")
    public String getRPInterface() {
        return rPInterface;
    }

    @JsonProperty("RP_Interface")
    public void setRPInterface(String rPInterface) {
        this.rPInterface = rPInterface;
    }

    @JsonProperty("RP_Is_Actual_Avlbl")
    public String getRPIsActualAvlbl() {
        return rPIsActualAvlbl;
    }

    @JsonProperty("RP_Is_Actual_Avlbl")
    public void setRPIsActualAvlbl(String rPIsActualAvlbl) {
        this.rPIsActualAvlbl = rPIsActualAvlbl;
    }

    @JsonProperty("RP_Is_Bw_Trend_Avlbl")
    public String getRPIsBwTrendAvlbl() {
        return rPIsBwTrendAvlbl;
    }

    @JsonProperty("RP_Is_Bw_Trend_Avlbl")
    public void setRPIsBwTrendAvlbl(String rPIsBwTrendAvlbl) {
        this.rPIsBwTrendAvlbl = rPIsBwTrendAvlbl;
    }

    @JsonProperty("RP_Is_Curr_Yr_Pq_Avlbl")
    public String getRPIsCurrYrPqAvlbl() {
        return rPIsCurrYrPqAvlbl;
    }

    @JsonProperty("RP_Is_Curr_Yr_Pq_Avlbl")
    public void setRPIsCurrYrPqAvlbl(String rPIsCurrYrPqAvlbl) {
        this.rPIsCurrYrPqAvlbl = rPIsCurrYrPqAvlbl;
    }

    @JsonProperty("RP_Is_Exact_Match_To_Act_Avlbl")
    public String getRPIsExactMatchToActAvlbl() {
        return rPIsExactMatchToActAvlbl;
    }

    @JsonProperty("RP_Is_Exact_Match_To_Act_Avlbl")
    public void setRPIsExactMatchToActAvlbl(String rPIsExactMatchToActAvlbl) {
        this.rPIsExactMatchToActAvlbl = rPIsExactMatchToActAvlbl;
    }

    @JsonProperty("RP_Is_Pq_To_Act_Avlbl")
    public String getRPIsPqToActAvlbl() {
        return rPIsPqToActAvlbl;
    }

    @JsonProperty("RP_Is_Pq_To_Act_Avlbl")
    public void setRPIsPqToActAvlbl(String rPIsPqToActAvlbl) {
        this.rPIsPqToActAvlbl = rPIsPqToActAvlbl;
    }

    @JsonProperty("RP_Is_Prc_To_Act_Avlbl")
    public String getRPIsPrcToActAvlbl() {
        return rPIsPrcToActAvlbl;
    }

    @JsonProperty("RP_Is_Prc_To_Act_Avlbl")
    public void setRPIsPrcToActAvlbl(String rPIsPrcToActAvlbl) {
        this.rPIsPrcToActAvlbl = rPIsPrcToActAvlbl;
    }

    @JsonProperty("RP_Is_Prc_To_Pq_Avlbl")
    public String getRPIsPrcToPqAvlbl() {
        return rPIsPrcToPqAvlbl;
    }

    @JsonProperty("RP_Is_Prc_To_Pq_Avlbl")
    public void setRPIsPrcToPqAvlbl(String rPIsPrcToPqAvlbl) {
        this.rPIsPrcToPqAvlbl = rPIsPrcToPqAvlbl;
    }

    @JsonProperty("RP_Is_Rate_Card_Avlbl")
    public String getRPIsRateCardAvlbl() {
        return rPIsRateCardAvlbl;
    }

    @JsonProperty("RP_Is_Rate_Card_Avlbl")
    public void setRPIsRateCardAvlbl(String rPIsRateCardAvlbl) {
        this.rPIsRateCardAvlbl = rPIsRateCardAvlbl;
    }

    @JsonProperty("RP_Is_Valid_Prc_Avlbl")
    public String getRPIsValidPrcAvlbl() {
        return rPIsValidPrcAvlbl;
    }

    @JsonProperty("RP_Is_Valid_Prc_Avlbl")
    public void setRPIsValidPrcAvlbl(String rPIsValidPrcAvlbl) {
        this.rPIsValidPrcAvlbl = rPIsValidPrcAvlbl;
    }

    @JsonProperty("RP_Ll_Mrc")
    public String getRPLlMrc() {
        return rPLlMrc;
    }

    @JsonProperty("RP_Ll_Mrc")
    public void setRPLlMrc(String rPLlMrc) {
        this.rPLlMrc = rPLlMrc;
    }

    @JsonProperty("RP_Ll_Nrc")
    public String getRPLlNrc() {
        return rPLlNrc;
    }

    @JsonProperty("RP_Ll_Nrc")
    public void setRPLlNrc(String rPLlNrc) {
        this.rPLlNrc = rPLlNrc;
    }

    @JsonProperty("RP_Mrc_Bw_Usd_Mean_Pq")
    public String getRPMrcBwUsdMeanPq() {
        return rPMrcBwUsdMeanPq;
    }

    @JsonProperty("RP_Mrc_Bw_Usd_Mean_Pq")
    public void setRPMrcBwUsdMeanPq(String rPMrcBwUsdMeanPq) {
        this.rPMrcBwUsdMeanPq = rPMrcBwUsdMeanPq;
    }

    @JsonProperty("RP_New_Intercept_Bw")
    public String getRPNewInterceptBw() {
        return rPNewInterceptBw;
    }

    @JsonProperty("RP_New_Intercept_Bw")
    public void setRPNewInterceptBw(String rPNewInterceptBw) {
        this.rPNewInterceptBw = rPNewInterceptBw;
    }

    @JsonProperty("RP_New_Slope_Log_Bw")
    public String getRPNewSlopeLogBw() {
        return rPNewSlopeLogBw;
    }

    @JsonProperty("RP_New_Slope_Log_Bw")
    public void setRPNewSlopeLogBw(String rPNewSlopeLogBw) {
        this.rPNewSlopeLogBw = rPNewSlopeLogBw;
    }

    @JsonProperty("RP_Obs_Count_Pq_Bw")
    public String getRPObsCountPqBw() {
        return rPObsCountPqBw;
    }

    @JsonProperty("RP_Obs_Count_Pq_Bw")
    public void setRPObsCountPqBw(String rPObsCountPqBw) {
        this.rPObsCountPqBw = rPObsCountPqBw;
    }

    @JsonProperty("RP_Pop_Address")
    public String getRPPopAddress() {
        return rPPopAddress;
    }

    @JsonProperty("RP_Pop_Address")
    public void setRPPopAddress(String rPPopAddress) {
        this.rPPopAddress = rPPopAddress;
    }

    @JsonProperty("RP_Pop_Code")
    public String getRPPopCode() {
        return rPPopCode;
    }

    @JsonProperty("RP_Pop_Code")
    public void setRPPopCode(String rPPopCode) {
        this.rPPopCode = rPPopCode;
    }

    @JsonProperty("RP_Pq_Regression_Line_Year")
    public String getRPPqRegressionLineYear() {
        return rPPqRegressionLineYear;
    }

    @JsonProperty("RP_Pq_Regression_Line_Year")
    public void setRPPqRegressionLineYear(String rPPqRegressionLineYear) {
        this.rPPqRegressionLineYear = rPPqRegressionLineYear;
    }

    @JsonProperty("RP_Pq_To_Act_Adj")
    public String getRPPqToActAdj() {
        return rPPqToActAdj;
    }

    @JsonProperty("RP_Pq_To_Act_Adj")
    public void setRPPqToActAdj(String rPPqToActAdj) {
        this.rPPqToActAdj = rPPqToActAdj;
    }

    @JsonProperty("RP_Prc_Closest_Building")
    public String getRPPrcClosestBuilding() {
        return rPPrcClosestBuilding;
    }

    @JsonProperty("RP_Prc_Closest_Building")
    public void setRPPrcClosestBuilding(String rPPrcClosestBuilding) {
        this.rPPrcClosestBuilding = rPPrcClosestBuilding;
    }

    @JsonProperty("RP_Prc_Cluster")
    public String getRPPrcCluster() {
        return rPPrcCluster;
    }

    @JsonProperty("RP_Prc_Cluster")
    public void setRPPrcCluster(String rPPrcCluster) {
        this.rPPrcCluster = rPPrcCluster;
    }

    @JsonProperty("RP_Prc_Price_Per_Mb")
    public String getRPPrcPricePerMb() {
        return rPPrcPricePerMb;
    }

    @JsonProperty("RP_Prc_Price_Per_Mb")
    public void setRPPrcPricePerMb(String rPPrcPricePerMb) {
        this.rPPrcPricePerMb = rPPrcPricePerMb;
    }

    @JsonProperty("RP_Prc_To_Act_Adj")
    public String getRPPrcToActAdj() {
        return rPPrcToActAdj;
    }

    @JsonProperty("RP_Prc_To_Act_Adj")
    public void setRPPrcToActAdj(String rPPrcToActAdj) {
        this.rPPrcToActAdj = rPPrcToActAdj;
    }

    @JsonProperty("RP_Prc_To_Pq_Adj")
    public String getRPPrcToPqAdj() {
        return rPPrcToPqAdj;
    }

    @JsonProperty("RP_Prc_To_Pq_Adj")
    public void setRPPrcToPqAdj(String rPPrcToPqAdj) {
        this.rPPrcToPqAdj = rPPrcToPqAdj;
    }

    @JsonProperty("RP_Predicted_Price")
    public String getRPPredictedPrice() {
        return rPPredictedPrice;
    }

    @JsonProperty("RP_Predicted_Price")
    public void setRPPredictedPrice(String rPPredictedPrice) {
        this.rPPredictedPrice = rPPredictedPrice;
    }

    @JsonProperty("RP_Predicted_Price_Per_Mb_Pq")
    public String getRPPredictedPricePerMbPq() {
        return rPPredictedPricePerMbPq;
    }

    @JsonProperty("RP_Predicted_Price_Per_Mb_Pq")
    public void setRPPredictedPricePerMbPq(String rPPredictedPricePerMbPq) {
        this.rPPredictedPricePerMbPq = rPPredictedPricePerMbPq;
    }

    @JsonProperty("RP_Provider_Name")
    public String getRPProviderName() {
        return rPProviderName;
    }

    @JsonProperty("RP_Provider_Name")
    public void setRPProviderName(String rPProviderName) {
        this.rPProviderName = rPProviderName;
    }

    @JsonProperty("RP_Provider_Product_Name")
    public String getRPProviderProductName() {
        return rPProviderProductName;
    }

    @JsonProperty("RP_Provider_Product_Name")
    public void setRPProviderProductName(String rPProviderProductName) {
        this.rPProviderProductName = rPProviderProductName;
    }

    @JsonProperty("RP_Quotation_ID")
    public String getRPQuotationID() {
        return rPQuotationID;
    }

    @JsonProperty("RP_Quotation_ID")
    public void setRPQuotationID(String rPQuotationID) {
        this.rPQuotationID = rPQuotationID;
    }

    @JsonProperty("RP_Quote_Category")
    public String getRPQuoteCategory() {
        return rPQuoteCategory;
    }

    @JsonProperty("RP_Quote_Category")
    public void setRPQuoteCategory(String rPQuoteCategory) {
        this.rPQuoteCategory = rPQuoteCategory;
    }

    @JsonProperty("RP_Quote_Created_Date")
    public String getRPQuoteCreatedDate() {
        return rPQuoteCreatedDate;
    }

    @JsonProperty("RP_Quote_Created_Date")
    public void setRPQuoteCreatedDate(String rPQuoteCreatedDate) {
        this.rPQuoteCreatedDate = rPQuoteCreatedDate;
    }

    @JsonProperty("RP_Quote_No")
    public String getRPQuoteNo() {
        return rPQuoteNo;
    }

    @JsonProperty("RP_Quote_No")
    public void setRPQuoteNo(String rPQuoteNo) {
        this.rPQuoteNo = rPQuoteNo;
    }

    @JsonProperty("RP_Shifted_Price_Per_Mb_Pq")
    public String getRPShiftedPricePerMbPq() {
        return rPShiftedPricePerMbPq;
    }

    @JsonProperty("RP_Shifted_Price_Per_Mb_Pq")
    public void setRPShiftedPricePerMbPq(String rPShiftedPricePerMbPq) {
        this.rPShiftedPricePerMbPq = rPShiftedPricePerMbPq;
    }

    @JsonProperty("RP_Slope_Log_Bw_Prc_Valid")
    public String getRPSlopeLogBwPrcValid() {
        return rPSlopeLogBwPrcValid;
    }

    @JsonProperty("RP_Slope_Log_Bw_Prc_Valid")
    public void setRPSlopeLogBwPrcValid(String rPSlopeLogBwPrcValid) {
        this.rPSlopeLogBwPrcValid = rPSlopeLogBwPrcValid;
    }

    @JsonProperty("RP_Slope_Log_Pq_Bw")
    public String getRPSlopeLogPqBw() {
        return rPSlopeLogPqBw;
    }

    @JsonProperty("RP_Slope_Log_Pq_Bw")
    public void setRPSlopeLogPqBw(String rPSlopeLogPqBw) {
        this.rPSlopeLogPqBw = rPSlopeLogPqBw;
    }

    @JsonProperty("RP_Termdiscountmrc24Months")
    public String getRPTermdiscountmrc24Months() {
        return rPTermdiscountmrc24Months;
    }

    @JsonProperty("RP_Termdiscountmrc24Months")
    public void setRPTermdiscountmrc24Months(String rPTermdiscountmrc24Months) {
        this.rPTermdiscountmrc24Months = rPTermdiscountmrc24Months;
    }

    @JsonProperty("RP_Termdiscountmrc36Months")
    public String getRPTermdiscountmrc36Months() {
        return rPTermdiscountmrc36Months;
    }

    @JsonProperty("RP_Termdiscountmrc36Months")
    public void setRPTermdiscountmrc36Months(String rPTermdiscountmrc36Months) {
        this.rPTermdiscountmrc36Months = rPTermdiscountmrc36Months;
    }

    @JsonProperty("RP_Total_Mrc")
    public String getRPTotalMrc() {
        return rPTotalMrc;
    }

    @JsonProperty("RP_Total_Mrc")
    public void setRPTotalMrc(String rPTotalMrc) {
        this.rPTotalMrc = rPTotalMrc;
    }

    @JsonProperty("RP_Total_Nrc")
    public String getRPTotalNrc() {
        return rPTotalNrc;
    }

    @JsonProperty("RP_Total_Nrc")
    public void setRPTotalNrc(String rPTotalNrc) {
        this.rPTotalNrc = rPTotalNrc;
    }

    @JsonProperty("RP_Valid_Prc_End_Date")
    public String getRPValidPrcEndDate() {
        return rPValidPrcEndDate;
    }

    @JsonProperty("RP_Valid_Prc_End_Date")
    public void setRPValidPrcEndDate(String rPValidPrcEndDate) {
        this.rPValidPrcEndDate = rPValidPrcEndDate;
    }

    @JsonProperty("RP_Valid_Prc_Records_Count")
    public String getRPValidPrcRecordsCount() {
        return rPValidPrcRecordsCount;
    }

    @JsonProperty("RP_Valid_Prc_Records_Count")
    public void setRPValidPrcRecordsCount(String rPValidPrcRecordsCount) {
        this.rPValidPrcRecordsCount = rPValidPrcRecordsCount;
    }

    @JsonProperty("RP_Valid_Prc_Start_Date")
    public String getRPValidPrcStartDate() {
        return rPValidPrcStartDate;
    }

    @JsonProperty("RP_Valid_Prc_Start_Date")
    public void setRPValidPrcStartDate(String rPValidPrcStartDate) {
        this.rPValidPrcStartDate = rPValidPrcStartDate;
    }

    @JsonProperty("RP_Vendor_Name")
    public String getRPVendorName() {
        return rPVendorName;
    }

    @JsonProperty("RP_Vendor_Name")
    public void setRPVendorName(String rPVendorName) {
        this.rPVendorName = rPVendorName;
    }

    @JsonProperty("RP_Xc_Mrc")
    public String getRPXcMrc() {
        return rPXcMrc;
    }

    @JsonProperty("RP_Xc_Mrc")
    public void setRPXcMrc(String rPXcMrc) {
        this.rPXcMrc = rPXcMrc;
    }

    @JsonProperty("RP_Xc_Nrc")
    public String getRPXcNrc() {
        return rPXcNrc;
    }

    @JsonProperty("RP_Xc_Nrc")
    public void setRPXcNrc(String rPXcNrc) {
        this.rPXcNrc = rPXcNrc;
    }

    @JsonProperty("RP_Xconnect_Provider_Name")
    public String getRPXconnectProviderName() {
        return rPXconnectProviderName;
    }

    @JsonProperty("RP_Xconnect_Provider_Name")
    public void setRPXconnectProviderName(String rPXconnectProviderName) {
        this.rPXconnectProviderName = rPXconnectProviderName;
    }

    @JsonProperty("RQ_Bandwidth")
    public String getRQBandwidth() {
        return rQBandwidth;
    }

    @JsonProperty("RQ_Bandwidth")
    public void setRQBandwidth(String rQBandwidth) {
        this.rQBandwidth = rQBandwidth;
    }

    @JsonProperty("RQ_Contract_Term_Months")
    public String getRQContractTermMonths() {
        return rQContractTermMonths;
    }

    @JsonProperty("RQ_Contract_Term_Months")
    public void setRQContractTermMonths(String rQContractTermMonths) {
        this.rQContractTermMonths = rQContractTermMonths;
    }

    @JsonProperty("RQ_Country")
    public String getRQCountry() {
        return rQCountry;
    }

    @JsonProperty("RQ_Country")
    public void setRQCountry(String rQCountry) {
        this.rQCountry = rQCountry;
    }

    @JsonProperty("RQ_Lat")
    public String getRQLat() {
        return rQLat;
    }

    @JsonProperty("RQ_Lat")
    public void setRQLat(String rQLat) {
        this.rQLat = rQLat;
    }

    @JsonProperty("RQ_Long")
    public String getRQLong() {
        return rQLong;
    }

    @JsonProperty("RQ_Long")
    public void setRQLong(String rQLong) {
        this.rQLong = rQLong;
    }

    @JsonProperty("RQ_Product_Type")
    public String getRQProductType() {
        return rQProductType;
    }

    @JsonProperty("RQ_Product_Type")
    public void setRQProductType(String rQProductType) {
        this.rQProductType = rQProductType;
    }

    @JsonProperty("RQ_Tcl_Pop_Short_Code")
    public String getRQTclPopShortCode() {
        return rQTclPopShortCode;
    }

    @JsonProperty("RQ_Tcl_Pop_Short_Code")
    public void setRQTclPopShortCode(String rQTclPopShortCode) {
        this.rQTclPopShortCode = rQTclPopShortCode;
    }

    @JsonProperty("RQ_User_Name")
    public String getRQUserName() {
        return rQUserName;
    }

    @JsonProperty("RQ_User_Name")
    public void setRQUserName(String rQUserName) {
        this.rQUserName = rQUserName;
    }

    @JsonProperty("Type")
    public String getType() {
        return type;
    }

    @JsonProperty("Type")
    public void setType(String type) {
        this.type = type;
    }

    @JsonProperty("account_id_with_18_digit")
    public String getAccountIdWith18Digit() {
        return accountIdWith18Digit;
    }

    @JsonProperty("account_id_with_18_digit")
    public void setAccountIdWith18Digit(String accountIdWith18Digit) {
        this.accountIdWith18Digit = accountIdWith18Digit;
    }

    @JsonProperty("additional_ip_flag")
    public String getAdditionalIpFlag() {
        return additionalIpFlag;
    }

    @JsonProperty("additional_ip_flag")
    public void setAdditionalIpFlag(String additionalIpFlag) {
        this.additionalIpFlag = additionalIpFlag;
    }

    @JsonProperty("backup_port_requested")
    public String getBackupPortRequested() {
        return backupPortRequested;
    }

    @JsonProperty("backup_port_requested")
    public void setBackupPortRequested(String backupPortRequested) {
        this.backupPortRequested = backupPortRequested;
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

    @JsonProperty("connection_type")
    public String getConnectionType() {
        return connectionType;
    }

    @JsonProperty("connection_type")
    public void setConnectionType(String connectionType) {
        this.connectionType = connectionType;
    }

    @JsonProperty("country")
    public String getCountry() {
        return country;
    }

    @JsonProperty("country")
    public void setCountry(String country) {
        this.country = country;
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

    @JsonProperty("cpe_variant")
    public String getCpeVariant() {
        return cpeVariant;
    }

    @JsonProperty("cpe_variant")
    public void setCpeVariant(String cpeVariant) {
        this.cpeVariant = cpeVariant;
    }

    @JsonProperty("cu_le_id")
    public String getCuLeId() {
        return cuLeId;
    }

    @JsonProperty("cu_le_id")
    public void setCuLeId(String cuLeId) {
        this.cuLeId = cuLeId;
    }

    @JsonProperty("customer_segment")
    public String getCustomerSegment() {
        return customerSegment;
    }

    @JsonProperty("customer_segment")
    public void setCustomerSegment(String customerSegment) {
        this.customerSegment = customerSegment;
    }

    @JsonProperty("feasibility_response_created_date")
    public String getFeasibilityResponseCreatedDate() {
        return feasibilityResponseCreatedDate;
    }

    @JsonProperty("feasibility_response_created_date")
    public void setFeasibilityResponseCreatedDate(String feasibilityResponseCreatedDate) {
        this.feasibilityResponseCreatedDate = feasibilityResponseCreatedDate;
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

    @JsonProperty("last_mile_contract_term")
    public String getLastMileContractTerm() {
        return lastMileContractTerm;
    }

    @JsonProperty("last_mile_contract_term")
    public void setLastMileContractTerm(String lastMileContractTerm) {
        this.lastMileContractTerm = lastMileContractTerm;
    }

    @JsonProperty("latitude_final")
    public Float getLatitudeFinal() {
        return latitudeFinal;
    }

    @JsonProperty("latitude_final")
    public void setLatitudeFinal(Float latitudeFinal) {
        this.latitudeFinal = latitudeFinal;
    }

    @JsonProperty("local_loop_interface")
    public String getLocalLoopInterface() {
        return localLoopInterface;
    }

    @JsonProperty("local_loop_interface")
    public void setLocalLoopInterface(String localLoopInterface) {
        this.localLoopInterface = localLoopInterface;
    }

    @JsonProperty("longitude_final")
    public Float getLongitudeFinal() {
        return longitudeFinal;
    }

    @JsonProperty("longitude_final")
    public void setLongitudeFinal(Float longitudeFinal) {
        this.longitudeFinal = longitudeFinal;
    }

    @JsonProperty("opportunity_term")
    public Integer getOpportunityTerm() {
        return opportunityTerm;
    }

    @JsonProperty("opportunity_term")
    public void setOpportunityTerm(Integer opportunityTerm) {
        this.opportunityTerm = opportunityTerm;
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

    @JsonProperty("quotetype_quote")
    public String getQuotetypeQuote() {
        return quotetypeQuote;
    }

    @JsonProperty("quotetype_quote")
    public void setQuotetypeQuote(String quotetypeQuote) {
        this.quotetypeQuote = quotetypeQuote;
    }

    @JsonProperty("rank")
    public String getRank() {
        return rank;
    }

    @JsonProperty("rank")
    public void setRank(String rank) {
        this.rank = rank;
    }

    @JsonProperty("resp_city")
    public String getRespCity() {
        return respCity;
    }

    @JsonProperty("resp_city")
    public void setRespCity(String respCity) {
        this.respCity = respCity;
    }

    @JsonProperty("sales_org")
    public String getSalesOrg() {
        return salesOrg;
    }

    @JsonProperty("sales_org")
    public void setSalesOrg(String salesOrg) {
        this.salesOrg = salesOrg;
    }

    @JsonProperty("siteFlag")
    public String getSiteFlag() {
        return siteFlag;
    }

    @JsonProperty("siteFlag")
    public void setSiteFlag(String siteFlag) {
        this.siteFlag = siteFlag;
    }

    @JsonProperty("site_id")
    public String getSiteId() {
        return siteId;
    }

    @JsonProperty("site_id")
    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    @JsonProperty("sum_no_of_sites_uni_len")
    public Integer getSumNoOfSitesUniLen() {
        return sumNoOfSitesUniLen;
    }

    @JsonProperty("sum_no_of_sites_uni_len")
    public void setSumNoOfSitesUniLen(Integer sumNoOfSitesUniLen) {
        this.sumNoOfSitesUniLen = sumNoOfSitesUniLen;
    }

    @JsonProperty("topology")
    public String getTopology() {
        return topology;
    }

    @JsonProperty("topology")
    public void setTopology(String topology) {
        this.topology = topology;
    }

    @JsonProperty("topology_secondary")
    public String getTopologySecondary() {
        return topologySecondary;
    }

    @JsonProperty("topology_secondary")
    public void setTopologySecondary(String topologySecondary) {
        this.topologySecondary = topologySecondary;
    }
    @JsonProperty("Selected")
    public Boolean getSelected() {
        return selected;
    }

    @JsonProperty("Selected")
    public void setSelected(Boolean selected) {
        this.selected = selected;
    }

    @JsonProperty("provider_allsiteslong")
   	private String providerAllSiteslong;
    @JsonProperty("x_connect_Xconnect_MRC_access")
    private Double xConnectXConnectMRCAccess;
    @JsonProperty("x_connect_Xconnect_NRC_access")
    private Double xConnectXConnectNRCAccess;
    @JsonProperty("x_connect_Xconnect_pop_allsiteslong")
    private String xConnectXConnectPopAllSitesLong;
    
	public String getBw() {
		return bw;
	}

	public void setBw(String bw) {
		this.bw = bw;
	}

	public String getClusterId() {
		return clusterId;
	}

	public void setClusterId(String clusterId) {
		this.clusterId = clusterId;
	}

	public String getContractTermWithVendorMonths() {
		return contractTermWithVendorMonths;
	}

	public void setContractTermWithVendorMonths(String contractTermWithVendorMonths) {
		this.contractTermWithVendorMonths = contractTermWithVendorMonths;
	}

	public String getDistanceToPop() {
		return distanceToPop;
	}

	public void setDistanceToPop(String distanceToPop) {
		this.distanceToPop = distanceToPop;
	}

	public String getMrcBw() {
		return mrcBw;
	}

	public void setMrcBw(String mrcBw) {
		this.mrcBw = mrcBw;
	}

	public String getMrcBwHat() {
		return mrcBwHat;
	}

	public void setMrcBwHat(String mrcBwHat) {
		this.mrcBwHat = mrcBwHat;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getProviderName() {
		return providerName;
	}

	public void setProviderName(String providerName) {
		this.providerName = providerName;
	}

	public String getProviderProductName() {
		return providerProductName;
	}

	public void setProviderProductName(String providerProductName) {
		this.providerProductName = providerProductName;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getPredictedAccessFeasibility() {
		return predictedAccessFeasibility;
	}

	public void setPredictedAccessFeasibility(String predictedAccessFeasibility) {
		this.predictedAccessFeasibility = predictedAccessFeasibility;
	}

	/**
	 * @return the rPActualPricePerMb
	 */
	public String getrPActualPricePerMb() {
		return rPActualPricePerMb;
	}

	/**
	 * @param rPActualPricePerMb the rPActualPricePerMb to set
	 */
	public void setrPActualPricePerMb(String rPActualPricePerMb) {
		this.rPActualPricePerMb = rPActualPricePerMb;
	}

	/**
	 * @return the rPActualRecordsCount
	 */
	public String getrPActualRecordsCount() {
		return rPActualRecordsCount;
	}

	/**
	 * @param rPActualRecordsCount the rPActualRecordsCount to set
	 */
	public void setrPActualRecordsCount(String rPActualRecordsCount) {
		this.rPActualRecordsCount = rPActualRecordsCount;
	}

	/**
	 * @return the rPAskedBldngCoverage
	 */
	public String getrPAskedBldngCoverage() {
		return rPAskedBldngCoverage;
	}

	/**
	 * @param rPAskedBldngCoverage the rPAskedBldngCoverage to set
	 */
	public void setrPAskedBldngCoverage(String rPAskedBldngCoverage) {
		this.rPAskedBldngCoverage = rPAskedBldngCoverage;
	}

	/**
	 * @return the rPBandwidth
	 */
	public String getrPBandwidth() {
		return rPBandwidth;
	}

	/**
	 * @param rPBandwidth the rPBandwidth to set
	 */
	public void setrPBandwidth(String rPBandwidth) {
		this.rPBandwidth = rPBandwidth;
	}

	/**
	 * @return the rPBandwidthMb
	 */
	public String getrPBandwidthMb() {
		return rPBandwidthMb;
	}

	/**
	 * @param rPBandwidthMb the rPBandwidthMb to set
	 */
	public void setrPBandwidthMb(String rPBandwidthMb) {
		this.rPBandwidthMb = rPBandwidthMb;
	}

	/**
	 * @return the rPBwBand
	 */
	public String getrPBwBand() {
		return rPBwBand;
	}

	/**
	 * @param rPBwBand the rPBwBand to set
	 */
	public void setrPBwBand(String rPBwBand) {
		this.rPBwBand = rPBwBand;
	}

	/**
	 * @return the rPCluster
	 */
	public String getrPCluster() {
		return rPCluster;
	}

	/**
	 * @param rPCluster the rPCluster to set
	 */
	public void setrPCluster(String rPCluster) {
		this.rPCluster = rPCluster;
	}

	/**
	 * @return the rPContractTermMonths
	 */
	public String getrPContractTermMonths() {
		return rPContractTermMonths;
	}

	/**
	 * @param rPContractTermMonths the rPContractTermMonths to set
	 */
	public void setrPContractTermMonths(String rPContractTermMonths) {
		this.rPContractTermMonths = rPContractTermMonths;
	}

	/**
	 * @return the rPCountryId
	 */
	public String getrPCountryId() {
		return rPCountryId;
	}

	/**
	 * @param rPCountryId the rPCountryId to set
	 */
	public void setrPCountryId(String rPCountryId) {
		this.rPCountryId = rPCountryId;
	}

	/**
	 * @return the rPCoverageType
	 */
	public String getrPCoverageType() {
		return rPCoverageType;
	}

	/**
	 * @param rPCoverageType the rPCoverageType to set
	 */
	public void setrPCoverageType(String rPCoverageType) {
		this.rPCoverageType = rPCoverageType;
	}

	/**
	 * @return the rPCurrYearPqRecordsCount
	 */
	public String getrPCurrYearPqRecordsCount() {
		return rPCurrYearPqRecordsCount;
	}

	/**
	 * @param rPCurrYearPqRecordsCount the rPCurrYearPqRecordsCount to set
	 */
	public void setrPCurrYearPqRecordsCount(String rPCurrYearPqRecordsCount) {
		this.rPCurrYearPqRecordsCount = rPCurrYearPqRecordsCount;
	}

	/**
	 * @return the rPCurrency
	 */
	public String getrPCurrency() {
		return rPCurrency;
	}

	/**
	 * @param rPCurrency the rPCurrency to set
	 */
	public void setrPCurrency(String rPCurrency) {
		this.rPCurrency = rPCurrency;
	}

	/**
	 * @return the rPDistPrcClosestBuilding
	 */
	public String getrPDistPrcClosestBuilding() {
		return rPDistPrcClosestBuilding;
	}

	/**
	 * @param rPDistPrcClosestBuilding the rPDistPrcClosestBuilding to set
	 */
	public void setrPDistPrcClosestBuilding(String rPDistPrcClosestBuilding) {
		this.rPDistPrcClosestBuilding = rPDistPrcClosestBuilding;
	}

	/**
	 * @return the rPExceptionCode
	 */
	public String getrPExceptionCode() {
		return rPExceptionCode;
	}

	/**
	 * @param rPExceptionCode the rPExceptionCode to set
	 */
	public void setrPExceptionCode(String rPExceptionCode) {
		this.rPExceptionCode = rPExceptionCode;
	}

	/**
	 * @return the rPFinalPricePerMb
	 */
	public String getrPFinalPricePerMb() {
		return rPFinalPricePerMb;
	}

	/**
	 * @param rPFinalPricePerMb the rPFinalPricePerMb to set
	 */
	public void setrPFinalPricePerMb(String rPFinalPricePerMb) {
		this.rPFinalPricePerMb = rPFinalPricePerMb;
	}

	/**
	 * @return the rPFrequency
	 */
	public String getrPFrequency() {
		return rPFrequency;
	}

	/**
	 * @param rPFrequency the rPFrequency to set
	 */
	public void setrPFrequency(String rPFrequency) {
		this.rPFrequency = rPFrequency;
	}

	/**
	 * @return the rPInterConnectionType
	 */
	public String getrPInterConnectionType() {
		return rPInterConnectionType;
	}

	/**
	 * @param rPInterConnectionType the rPInterConnectionType to set
	 */
	public void setrPInterConnectionType(String rPInterConnectionType) {
		this.rPInterConnectionType = rPInterConnectionType;
	}

	/**
	 * @return the rPInterceptPqBw
	 */
	public String getrPInterceptPqBw() {
		return rPInterceptPqBw;
	}

	/**
	 * @param rPInterceptPqBw the rPInterceptPqBw to set
	 */
	public void setrPInterceptPqBw(String rPInterceptPqBw) {
		this.rPInterceptPqBw = rPInterceptPqBw;
	}

	/**
	 * @return the rPInterceptPrcValid
	 */
	public String getrPInterceptPrcValid() {
		return rPInterceptPrcValid;
	}

	/**
	 * @param rPInterceptPrcValid the rPInterceptPrcValid to set
	 */
	public void setrPInterceptPrcValid(String rPInterceptPrcValid) {
		this.rPInterceptPrcValid = rPInterceptPrcValid;
	}

	/**
	 * @return the rPInterface
	 */
	public String getrPInterface() {
		return rPInterface;
	}

	/**
	 * @param rPInterface the rPInterface to set
	 */
	public void setrPInterface(String rPInterface) {
		this.rPInterface = rPInterface;
	}

	/**
	 * @return the rPIsActualAvlbl
	 */
	public String getrPIsActualAvlbl() {
		return rPIsActualAvlbl;
	}

	/**
	 * @param rPIsActualAvlbl the rPIsActualAvlbl to set
	 */
	public void setrPIsActualAvlbl(String rPIsActualAvlbl) {
		this.rPIsActualAvlbl = rPIsActualAvlbl;
	}

	/**
	 * @return the rPIsBwTrendAvlbl
	 */
	public String getrPIsBwTrendAvlbl() {
		return rPIsBwTrendAvlbl;
	}

	/**
	 * @param rPIsBwTrendAvlbl the rPIsBwTrendAvlbl to set
	 */
	public void setrPIsBwTrendAvlbl(String rPIsBwTrendAvlbl) {
		this.rPIsBwTrendAvlbl = rPIsBwTrendAvlbl;
	}

	/**
	 * @return the rPIsCurrYrPqAvlbl
	 */
	public String getrPIsCurrYrPqAvlbl() {
		return rPIsCurrYrPqAvlbl;
	}

	/**
	 * @param rPIsCurrYrPqAvlbl the rPIsCurrYrPqAvlbl to set
	 */
	public void setrPIsCurrYrPqAvlbl(String rPIsCurrYrPqAvlbl) {
		this.rPIsCurrYrPqAvlbl = rPIsCurrYrPqAvlbl;
	}

	/**
	 * @return the rPIsExactMatchToActAvlbl
	 */
	public String getrPIsExactMatchToActAvlbl() {
		return rPIsExactMatchToActAvlbl;
	}

	/**
	 * @param rPIsExactMatchToActAvlbl the rPIsExactMatchToActAvlbl to set
	 */
	public void setrPIsExactMatchToActAvlbl(String rPIsExactMatchToActAvlbl) {
		this.rPIsExactMatchToActAvlbl = rPIsExactMatchToActAvlbl;
	}

	/**
	 * @return the rPIsPqToActAvlbl
	 */
	public String getrPIsPqToActAvlbl() {
		return rPIsPqToActAvlbl;
	}

	/**
	 * @param rPIsPqToActAvlbl the rPIsPqToActAvlbl to set
	 */
	public void setrPIsPqToActAvlbl(String rPIsPqToActAvlbl) {
		this.rPIsPqToActAvlbl = rPIsPqToActAvlbl;
	}

	/**
	 * @return the rPIsPrcToActAvlbl
	 */
	public String getrPIsPrcToActAvlbl() {
		return rPIsPrcToActAvlbl;
	}

	/**
	 * @param rPIsPrcToActAvlbl the rPIsPrcToActAvlbl to set
	 */
	public void setrPIsPrcToActAvlbl(String rPIsPrcToActAvlbl) {
		this.rPIsPrcToActAvlbl = rPIsPrcToActAvlbl;
	}

	/**
	 * @return the rPIsPrcToPqAvlbl
	 */
	public String getrPIsPrcToPqAvlbl() {
		return rPIsPrcToPqAvlbl;
	}

	/**
	 * @param rPIsPrcToPqAvlbl the rPIsPrcToPqAvlbl to set
	 */
	public void setrPIsPrcToPqAvlbl(String rPIsPrcToPqAvlbl) {
		this.rPIsPrcToPqAvlbl = rPIsPrcToPqAvlbl;
	}

	/**
	 * @return the rPIsRateCardAvlbl
	 */
	public String getrPIsRateCardAvlbl() {
		return rPIsRateCardAvlbl;
	}

	/**
	 * @param rPIsRateCardAvlbl the rPIsRateCardAvlbl to set
	 */
	public void setrPIsRateCardAvlbl(String rPIsRateCardAvlbl) {
		this.rPIsRateCardAvlbl = rPIsRateCardAvlbl;
	}

	/**
	 * @return the rPIsValidPrcAvlbl
	 */
	public String getrPIsValidPrcAvlbl() {
		return rPIsValidPrcAvlbl;
	}

	/**
	 * @param rPIsValidPrcAvlbl the rPIsValidPrcAvlbl to set
	 */
	public void setrPIsValidPrcAvlbl(String rPIsValidPrcAvlbl) {
		this.rPIsValidPrcAvlbl = rPIsValidPrcAvlbl;
	}

	/**
	 * @return the rPLlMrc
	 */
	public String getrPLlMrc() {
		return rPLlMrc;
	}

	/**
	 * @param rPLlMrc the rPLlMrc to set
	 */
	public void setrPLlMrc(String rPLlMrc) {
		this.rPLlMrc = rPLlMrc;
	}

	/**
	 * @return the rPLlNrc
	 */
	public String getrPLlNrc() {
		return rPLlNrc;
	}

	/**
	 * @param rPLlNrc the rPLlNrc to set
	 */
	public void setrPLlNrc(String rPLlNrc) {
		this.rPLlNrc = rPLlNrc;
	}

	/**
	 * @return the rPMrcBwUsdMeanPq
	 */
	public String getrPMrcBwUsdMeanPq() {
		return rPMrcBwUsdMeanPq;
	}

	/**
	 * @param rPMrcBwUsdMeanPq the rPMrcBwUsdMeanPq to set
	 */
	public void setrPMrcBwUsdMeanPq(String rPMrcBwUsdMeanPq) {
		this.rPMrcBwUsdMeanPq = rPMrcBwUsdMeanPq;
	}

	/**
	 * @return the rPNewInterceptBw
	 */
	public String getrPNewInterceptBw() {
		return rPNewInterceptBw;
	}

	/**
	 * @param rPNewInterceptBw the rPNewInterceptBw to set
	 */
	public void setrPNewInterceptBw(String rPNewInterceptBw) {
		this.rPNewInterceptBw = rPNewInterceptBw;
	}

	/**
	 * @return the rPNewSlopeLogBw
	 */
	public String getrPNewSlopeLogBw() {
		return rPNewSlopeLogBw;
	}

	/**
	 * @param rPNewSlopeLogBw the rPNewSlopeLogBw to set
	 */
	public void setrPNewSlopeLogBw(String rPNewSlopeLogBw) {
		this.rPNewSlopeLogBw = rPNewSlopeLogBw;
	}

	/**
	 * @return the rPObsCountPqBw
	 */
	public String getrPObsCountPqBw() {
		return rPObsCountPqBw;
	}

	/**
	 * @param rPObsCountPqBw the rPObsCountPqBw to set
	 */
	public void setrPObsCountPqBw(String rPObsCountPqBw) {
		this.rPObsCountPqBw = rPObsCountPqBw;
	}

	/**
	 * @return the rPPopAddress
	 */
	public String getrPPopAddress() {
		return rPPopAddress;
	}

	/**
	 * @param rPPopAddress the rPPopAddress to set
	 */
	public void setrPPopAddress(String rPPopAddress) {
		this.rPPopAddress = rPPopAddress;
	}

	/**
	 * @return the rPPopCode
	 */
	public String getrPPopCode() {
		return rPPopCode;
	}

	/**
	 * @param rPPopCode the rPPopCode to set
	 */
	public void setrPPopCode(String rPPopCode) {
		this.rPPopCode = rPPopCode;
	}

	/**
	 * @return the rPPqRegressionLineYear
	 */
	public String getrPPqRegressionLineYear() {
		return rPPqRegressionLineYear;
	}

	/**
	 * @param rPPqRegressionLineYear the rPPqRegressionLineYear to set
	 */
	public void setrPPqRegressionLineYear(String rPPqRegressionLineYear) {
		this.rPPqRegressionLineYear = rPPqRegressionLineYear;
	}

	/**
	 * @return the rPPqToActAdj
	 */
	public String getrPPqToActAdj() {
		return rPPqToActAdj;
	}

	/**
	 * @param rPPqToActAdj the rPPqToActAdj to set
	 */
	public void setrPPqToActAdj(String rPPqToActAdj) {
		this.rPPqToActAdj = rPPqToActAdj;
	}

	/**
	 * @return the rPPrcClosestBuilding
	 */
	public String getrPPrcClosestBuilding() {
		return rPPrcClosestBuilding;
	}

	/**
	 * @param rPPrcClosestBuilding the rPPrcClosestBuilding to set
	 */
	public void setrPPrcClosestBuilding(String rPPrcClosestBuilding) {
		this.rPPrcClosestBuilding = rPPrcClosestBuilding;
	}

	/**
	 * @return the rPPrcCluster
	 */
	public String getrPPrcCluster() {
		return rPPrcCluster;
	}

	/**
	 * @param rPPrcCluster the rPPrcCluster to set
	 */
	public void setrPPrcCluster(String rPPrcCluster) {
		this.rPPrcCluster = rPPrcCluster;
	}

	/**
	 * @return the rPPrcPricePerMb
	 */
	public String getrPPrcPricePerMb() {
		return rPPrcPricePerMb;
	}

	/**
	 * @param rPPrcPricePerMb the rPPrcPricePerMb to set
	 */
	public void setrPPrcPricePerMb(String rPPrcPricePerMb) {
		this.rPPrcPricePerMb = rPPrcPricePerMb;
	}

	/**
	 * @return the rPPrcToActAdj
	 */
	public String getrPPrcToActAdj() {
		return rPPrcToActAdj;
	}

	/**
	 * @param rPPrcToActAdj the rPPrcToActAdj to set
	 */
	public void setrPPrcToActAdj(String rPPrcToActAdj) {
		this.rPPrcToActAdj = rPPrcToActAdj;
	}

	/**
	 * @return the rPPrcToPqAdj
	 */
	public String getrPPrcToPqAdj() {
		return rPPrcToPqAdj;
	}

	/**
	 * @param rPPrcToPqAdj the rPPrcToPqAdj to set
	 */
	public void setrPPrcToPqAdj(String rPPrcToPqAdj) {
		this.rPPrcToPqAdj = rPPrcToPqAdj;
	}

	/**
	 * @return the rPPredictedPrice
	 */
	public String getrPPredictedPrice() {
		return rPPredictedPrice;
	}

	/**
	 * @param rPPredictedPrice the rPPredictedPrice to set
	 */
	public void setrPPredictedPrice(String rPPredictedPrice) {
		this.rPPredictedPrice = rPPredictedPrice;
	}

	/**
	 * @return the rPPredictedPricePerMbPq
	 */
	public String getrPPredictedPricePerMbPq() {
		return rPPredictedPricePerMbPq;
	}

	/**
	 * @param rPPredictedPricePerMbPq the rPPredictedPricePerMbPq to set
	 */
	public void setrPPredictedPricePerMbPq(String rPPredictedPricePerMbPq) {
		this.rPPredictedPricePerMbPq = rPPredictedPricePerMbPq;
	}

	/**
	 * @return the rPProviderName
	 */
	public String getrPProviderName() {
		return rPProviderName;
	}

	/**
	 * @param rPProviderName the rPProviderName to set
	 */
	public void setrPProviderName(String rPProviderName) {
		this.rPProviderName = rPProviderName;
	}

	/**
	 * @return the rPProviderProductName
	 */
	public String getrPProviderProductName() {
		return rPProviderProductName;
	}

	/**
	 * @param rPProviderProductName the rPProviderProductName to set
	 */
	public void setrPProviderProductName(String rPProviderProductName) {
		this.rPProviderProductName = rPProviderProductName;
	}

	/**
	 * @return the rPQuotationID
	 */
	public String getrPQuotationID() {
		return rPQuotationID;
	}

	/**
	 * @param rPQuotationID the rPQuotationID to set
	 */
	public void setrPQuotationID(String rPQuotationID) {
		this.rPQuotationID = rPQuotationID;
	}

	/**
	 * @return the rPQuoteCategory
	 */
	public String getrPQuoteCategory() {
		return rPQuoteCategory;
	}

	/**
	 * @param rPQuoteCategory the rPQuoteCategory to set
	 */
	public void setrPQuoteCategory(String rPQuoteCategory) {
		this.rPQuoteCategory = rPQuoteCategory;
	}

	/**
	 * @return the rPQuoteCreatedDate
	 */
	public String getrPQuoteCreatedDate() {
		return rPQuoteCreatedDate;
	}

	/**
	 * @param rPQuoteCreatedDate the rPQuoteCreatedDate to set
	 */
	public void setrPQuoteCreatedDate(String rPQuoteCreatedDate) {
		this.rPQuoteCreatedDate = rPQuoteCreatedDate;
	}

	/**
	 * @return the rPQuoteNo
	 */
	public String getrPQuoteNo() {
		return rPQuoteNo;
	}

	/**
	 * @param rPQuoteNo the rPQuoteNo to set
	 */
	public void setrPQuoteNo(String rPQuoteNo) {
		this.rPQuoteNo = rPQuoteNo;
	}

	/**
	 * @return the rPShiftedPricePerMbPq
	 */
	public String getrPShiftedPricePerMbPq() {
		return rPShiftedPricePerMbPq;
	}

	/**
	 * @param rPShiftedPricePerMbPq the rPShiftedPricePerMbPq to set
	 */
	public void setrPShiftedPricePerMbPq(String rPShiftedPricePerMbPq) {
		this.rPShiftedPricePerMbPq = rPShiftedPricePerMbPq;
	}

	/**
	 * @return the rPSlopeLogBwPrcValid
	 */
	public String getrPSlopeLogBwPrcValid() {
		return rPSlopeLogBwPrcValid;
	}

	/**
	 * @param rPSlopeLogBwPrcValid the rPSlopeLogBwPrcValid to set
	 */
	public void setrPSlopeLogBwPrcValid(String rPSlopeLogBwPrcValid) {
		this.rPSlopeLogBwPrcValid = rPSlopeLogBwPrcValid;
	}

	/**
	 * @return the rPSlopeLogPqBw
	 */
	public String getrPSlopeLogPqBw() {
		return rPSlopeLogPqBw;
	}

	/**
	 * @param rPSlopeLogPqBw the rPSlopeLogPqBw to set
	 */
	public void setrPSlopeLogPqBw(String rPSlopeLogPqBw) {
		this.rPSlopeLogPqBw = rPSlopeLogPqBw;
	}

	/**
	 * @return the rPTermdiscountmrc24Months
	 */
	public String getrPTermdiscountmrc24Months() {
		return rPTermdiscountmrc24Months;
	}

	/**
	 * @param rPTermdiscountmrc24Months the rPTermdiscountmrc24Months to set
	 */
	public void setrPTermdiscountmrc24Months(String rPTermdiscountmrc24Months) {
		this.rPTermdiscountmrc24Months = rPTermdiscountmrc24Months;
	}

	/**
	 * @return the rPTermdiscountmrc36Months
	 */
	public String getrPTermdiscountmrc36Months() {
		return rPTermdiscountmrc36Months;
	}

	/**
	 * @param rPTermdiscountmrc36Months the rPTermdiscountmrc36Months to set
	 */
	public void setrPTermdiscountmrc36Months(String rPTermdiscountmrc36Months) {
		this.rPTermdiscountmrc36Months = rPTermdiscountmrc36Months;
	}

	/**
	 * @return the rPTotalMrc
	 */
	public String getrPTotalMrc() {
		return rPTotalMrc;
	}

	/**
	 * @param rPTotalMrc the rPTotalMrc to set
	 */
	public void setrPTotalMrc(String rPTotalMrc) {
		this.rPTotalMrc = rPTotalMrc;
	}

	/**
	 * @return the rPTotalNrc
	 */
	public String getrPTotalNrc() {
		return rPTotalNrc;
	}

	/**
	 * @param rPTotalNrc the rPTotalNrc to set
	 */
	public void setrPTotalNrc(String rPTotalNrc) {
		this.rPTotalNrc = rPTotalNrc;
	}

	/**
	 * @return the rPValidPrcEndDate
	 */
	public String getrPValidPrcEndDate() {
		return rPValidPrcEndDate;
	}

	/**
	 * @param rPValidPrcEndDate the rPValidPrcEndDate to set
	 */
	public void setrPValidPrcEndDate(String rPValidPrcEndDate) {
		this.rPValidPrcEndDate = rPValidPrcEndDate;
	}

	/**
	 * @return the rPValidPrcRecordsCount
	 */
	public String getrPValidPrcRecordsCount() {
		return rPValidPrcRecordsCount;
	}

	/**
	 * @param rPValidPrcRecordsCount the rPValidPrcRecordsCount to set
	 */
	public void setrPValidPrcRecordsCount(String rPValidPrcRecordsCount) {
		this.rPValidPrcRecordsCount = rPValidPrcRecordsCount;
	}

	/**
	 * @return the rPValidPrcStartDate
	 */
	public String getrPValidPrcStartDate() {
		return rPValidPrcStartDate;
	}

	/**
	 * @param rPValidPrcStartDate the rPValidPrcStartDate to set
	 */
	public void setrPValidPrcStartDate(String rPValidPrcStartDate) {
		this.rPValidPrcStartDate = rPValidPrcStartDate;
	}

	/**
	 * @return the rPVendorName
	 */
	public String getrPVendorName() {
		return rPVendorName;
	}

	/**
	 * @param rPVendorName the rPVendorName to set
	 */
	public void setrPVendorName(String rPVendorName) {
		this.rPVendorName = rPVendorName;
	}

	/**
	 * @return the rPXcMrc
	 */
	public String getrPXcMrc() {
		return rPXcMrc;
	}

	/**
	 * @param rPXcMrc the rPXcMrc to set
	 */
	public void setrPXcMrc(String rPXcMrc) {
		this.rPXcMrc = rPXcMrc;
	}

	/**
	 * @return the rPXcNrc
	 */
	public String getrPXcNrc() {
		return rPXcNrc;
	}

	/**
	 * @param rPXcNrc the rPXcNrc to set
	 */
	public void setrPXcNrc(String rPXcNrc) {
		this.rPXcNrc = rPXcNrc;
	}

	/**
	 * @return the rPXconnectProviderName
	 */
	public String getrPXconnectProviderName() {
		return rPXconnectProviderName;
	}

	/**
	 * @param rPXconnectProviderName the rPXconnectProviderName to set
	 */
	public void setrPXconnectProviderName(String rPXconnectProviderName) {
		this.rPXconnectProviderName = rPXconnectProviderName;
	}

	/**
	 * @return the rQBandwidth
	 */
	public String getrQBandwidth() {
		return rQBandwidth;
	}

	/**
	 * @param rQBandwidth the rQBandwidth to set
	 */
	public void setrQBandwidth(String rQBandwidth) {
		this.rQBandwidth = rQBandwidth;
	}

	/**
	 * @return the rQContractTermMonths
	 */
	public String getrQContractTermMonths() {
		return rQContractTermMonths;
	}

	/**
	 * @param rQContractTermMonths the rQContractTermMonths to set
	 */
	public void setrQContractTermMonths(String rQContractTermMonths) {
		this.rQContractTermMonths = rQContractTermMonths;
	}

	/**
	 * @return the rQCountry
	 */
	public String getrQCountry() {
		return rQCountry;
	}

	/**
	 * @param rQCountry the rQCountry to set
	 */
	public void setrQCountry(String rQCountry) {
		this.rQCountry = rQCountry;
	}

	/**
	 * @return the rQLat
	 */
	public String getrQLat() {
		return rQLat;
	}

	/**
	 * @param rQLat the rQLat to set
	 */
	public void setrQLat(String rQLat) {
		this.rQLat = rQLat;
	}

	/**
	 * @return the rQLong
	 */
	public String getrQLong() {
		return rQLong;
	}

	/**
	 * @param rQLong the rQLong to set
	 */
	public void setrQLong(String rQLong) {
		this.rQLong = rQLong;
	}

	/**
	 * @return the rQProductType
	 */
	public String getrQProductType() {
		return rQProductType;
	}

	/**
	 * @param rQProductType the rQProductType to set
	 */
	public void setrQProductType(String rQProductType) {
		this.rQProductType = rQProductType;
	}

	/**
	 * @return the rQTclPopShortCode
	 */
	public String getrQTclPopShortCode() {
		return rQTclPopShortCode;
	}

	/**
	 * @param rQTclPopShortCode the rQTclPopShortCode to set
	 */
	public void setrQTclPopShortCode(String rQTclPopShortCode) {
		this.rQTclPopShortCode = rQTclPopShortCode;
	}

	/**
	 * @return the rQUserName
	 */
	public String getrQUserName() {
		return rQUserName;
	}

	/**
	 * @param rQUserName the rQUserName to set
	 */
	public void setrQUserName(String rQUserName) {
		this.rQUserName = rQUserName;
	}

	/**
	 * @return the dbCode
	 */
	public String getDbCode() {
		return dbCode;
	}

	/**
	 * @param dbCode the dbCode to set
	 */
	public void setDbCode(String dbCode) {
		this.dbCode = dbCode;
	}

	/**
	 * @return the relatedQuotes
	 */
	public List<RelatedQuote> getRelatedQuotes() {
		return relatedQuotes;
	}

	/**
	 * @param relatedQuotes the relatedQuotes to set
	 */
	public void setRelatedQuotes(List<RelatedQuote> relatedQuotes) {
		this.relatedQuotes = relatedQuotes;
	}

	/**
	 * @return the xconnectCur
	 */
	public String getXconnectCur() {
		return xconnectCur;
	}

	/**
	 * @param xconnectCur the xconnectCur to set
	 */
	public void setXconnectCur(String xconnectCur) {
		this.xconnectCur = xconnectCur;
	}

	/**
	 * @return the xconnectMrc
	 */
	public String getXconnectMrc() {
		return xconnectMrc;
	}

	/**
	 * @param xconnectMrc the xconnectMrc to set
	 */
	public void setXconnectMrc(String xconnectMrc) {
		this.xconnectMrc = xconnectMrc;
	}

	/**
	 * @return the xconnectNrc
	 */
	public String getXconnectNrc() {
		return xconnectNrc;
	}

	/**
	 * @param xconnectNrc the xconnectNrc to set
	 */
	public void setXconnectNrc(String xconnectNrc) {
		this.xconnectNrc = xconnectNrc;
	}

	public String getBwBand() {
		return bwBand;
	}

	public void setBwBand(String bwBand) {
		this.bwBand = bwBand;
	}

	

	public Double getOtcNrcInstallationHat() {
		return otcNrcInstallationHat;
	}

	public void setOtcNrcInstallationHat(Double otcNrcInstallationHat) {
		this.otcNrcInstallationHat = otcNrcInstallationHat;
	}

	public String getQuoteId() {
		return quoteId;
	}

	public void setQuoteId(String quoteId) {
		this.quoteId = quoteId;
	}

	public String getRqiBw() {
		return rqiBw;
	}

	public void setRqiBw(String rqiBw) {
		this.rqiBw = rqiBw;
	}

	public Double getRqiLat() {
		return rqiLat;
	}

	public void setRqiLat(Double rqiLat) {
		this.rqiLat = rqiLat;
	}

	public Double getRqiLong() {
		return rqiLong;
	}

	public void setRqiLong(Double rqiLong) {
		this.rqiLong = rqiLong;
	}

	public String getRqiTclProd() {
		return rqiTclProd;
	}

	public void setRqiTclProd(String rqiTclProd) {
		this.rqiTclProd = rqiTclProd;
	}

	public String getTclPopAddress() {
		return tclPopAddress;
	}

	public void setTclPopAddress(String tclPopAddress) {
		this.tclPopAddress = tclPopAddress;
	}

	public List<String> getTclPopShortCode() {
		return tclPopShortCode;
	}

	public void setTclPopShortCode(List<String> tclPopShortCode) {
		this.tclPopShortCode = tclPopShortCode;
	}

	public String getXconnectCurHat() {
		return xconnectCurHat;
	}

	public void setXconnectCurHat(String xconnectCurHat) {
		this.xconnectCurHat = xconnectCurHat;
	}

	public Integer getXconnectMrcHat() {
		return xconnectMrcHat;
	}

	public void setXconnectMrcHat(Integer xconnectMrcHat) {
		this.xconnectMrcHat = xconnectMrcHat;
	}

	public Integer getXconnectNrcHat() {
		return xconnectNrcHat;
	}

	public void setXconnectNrcHat(Integer xconnectNrcHat) {
		this.xconnectNrcHat = xconnectNrcHat;
	}

	/**
	 * @return the llInterface
	 */
	public String getLlInterface() {
		return llInterface;
	}

	/**
	 * @param llInterface the llInterface to set
	 */
	public void setLlInterface(String llInterface) {
		this.llInterface = llInterface;
	}

	public String getMrcBwBkp() {
		return mrcBwBkp;
	}

	public void setMrcBwBkp(String mrcBwBkp) {
		this.mrcBwBkp = mrcBwBkp;
	}

	public String getSelectedQuote() {
		return selectedQuote;
	}

	public void setSelectedQuote(String selectedQuote) {
		this.selectedQuote = selectedQuote;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public String getErrorFlag() {
		return errorFlag;
	}

	public void setErrorFlag(String errorFlag) {
		this.errorFlag = errorFlag;
	}

	public String getRowId() {
		return rowId;
	}

	public void setRowId(String rowId) {
		this.rowId = rowId;
	}

	public String getPriorityRank() {
		return priorityRank;
	}

	public void setPriorityRank(String priorityRank) {
		this.priorityRank = priorityRank;
	}

    public String getSolutionType() {
        return solutionType;
    }

    public void setSolutionType(String solutionType) {
        this.solutionType = solutionType;
    }
    public String getProviderProviderName() {
        return providerProviderName;
    }

    public void setProviderProviderName(String providerProviderName) {
        this.providerProviderName = providerProviderName;
    }

    public String getProviderProviderProductName() {
        return providerProviderProductName;
    }

    public void setProviderProviderProductName(String providerProviderProductName) {
        this.providerProviderProductName = providerProviderProductName;
    }
    @JsonProperty("provider_Provider_Product_Name_Subset")
    public String getProviderProviderProductNameSubset() {
        return providerProviderProductNameSubset;
    }

    @JsonProperty("provider_Provider_Product_Name_Subset")
    public void setProviderProviderProductNameSubset(String providerProviderProductNameSubset) {
        this.providerProviderProductNameSubset = providerProviderProductNameSubset;
    }

    public String getProviderInterConnectionType() {
        return providerInterConnectionType;
    }

    public void setProviderInterConnectionType(String providerInterConnectionType) {
        this.providerInterConnectionType = providerInterConnectionType;
    }

    public String getProviderCoverage() {
        return providerCoverage;
    }

    public void setProviderCoverage(String providerCoverage) {
        this.providerCoverage = providerCoverage;
    }

    public float getProviderCountryID() {
        return providerCountryID;
    }

    public void setProviderCountryID(float providerCountryID) {
        this.providerCountryID = providerCountryID;
    }

    public String getProviderQuoteCategoryShort() {
        return providerQuoteCategoryShort;
    }

    public void setProviderQuoteCategoryShort(String providerQuoteCategoryShort) {
        this.providerQuoteCategoryShort = providerQuoteCategoryShort;
    }

    public String getProviderError() {
        return providerError;
    }

    public void setProviderError(String providerError) {
        this.providerError = providerError;
    }

    public float getProviderIsManualQuote() {
        return providerIsManualQuote;
    }

    public void setProviderIsManualQuote(float providerIsManualQuote) {
        this.providerIsManualQuote = providerIsManualQuote;
    }

    public String getProvider_MRC_BW_Currency() {
        return provider_MRC_BW_Currency;
    }

    public void setProvider_MRC_BW_Currency(String provider_MRC_BW_Currency) {
        this.provider_MRC_BW_Currency = provider_MRC_BW_Currency;
    }

    public String getProviderLocalLoopInterface() {
        return providerLocalLoopInterface;
    }

    public void setProviderLocalLoopInterface(String providerLocalLoopInterface) {
        this.providerLocalLoopInterface = providerLocalLoopInterface;
    }

    public float getProviderXconnectMRC() {
        return providerXconnectMRC;
    }

    public void setProviderXconnectMRC(float providerXconnectMRC) {
        this.providerXconnectMRC = providerXconnectMRC;
    }

    public float getProviderXconnectNRC() {
        return providerXconnectNRC;
    }

    public void setProviderXconnectNRC(float providerXconnectNRC) {
        this.providerXconnectNRC = providerXconnectNRC;
    }

    public String getProviderInfo() {
        return providerInfo;
    }

    public void setProviderInfo(String providerInfo) {
        this.providerInfo = providerInfo;
    }

    public String getProviderProviderProductNameSubSet() {
        return providerProviderProductNameSubSet;
    }

    public void setProviderProviderProductNameSubSet(String providerProviderProductNameSubSet) {
        this.providerProviderProductNameSubSet = providerProviderProductNameSubSet;
    }

    public String getProviderLocalLoopCapacity() {
        return providerLocalLoopCapacity;
    }

    public void setProviderLocalLoopCapacity(String providerLocalLoopCapacity) {
        this.providerLocalLoopCapacity = providerLocalLoopCapacity;
    }

    public float getProviderMRCBW() {
        return providerMRCBW;
    }

    public void setProviderMRCBW(float providerMRCBW) {
        this.providerMRCBW = providerMRCBW;
    }

    public float getProviderOTCNRCInstallation() {
        return providerOTCNRCInstallation;
    }

    public void setProviderOTCNRCInstallation(float providerOTCNRCInstallation) {
        this.providerOTCNRCInstallation = providerOTCNRCInstallation;
    }

    public float getProviderTermDiscountMRC24months() {
        return providerTermDiscountMRC24months;
    }

    public void setProviderTermDiscountMRC24months(float providerTermDiscountMRC24months) {
        this.providerTermDiscountMRC24months = providerTermDiscountMRC24months;
    }

    public float getProviderTermDiscountNRC24months() {
        return providerTermDiscountNRC24months;
    }

    public void setProviderTermDiscountNRC24months(float providerTermDiscountNRC24months) {
        this.providerTermDiscountNRC24months = providerTermDiscountNRC24months;
    }

    public float getProviderTermDiscountMRC36months() {
        return providerTermDiscountMRC36months;
    }

    public void setProviderTermDiscountMRC36months(float providerTermDiscountMRC36months) {
        this.providerTermDiscountMRC36months = providerTermDiscountMRC36months;
    }

    public float getProviderTermDiscountNRC36months() {
        return providerTermDiscountNRC36months;
    }

    public void setProviderTermDiscountNRC36months(float providerTermDiscountNRC36months) {
        this.providerTermDiscountNRC36months = providerTermDiscountNRC36months;
    }

    public float getProviderCt12() {
        return providerCt12;
    }

    public void setProviderCt12(float providerCt12) {
        this.providerCt12 = providerCt12;
    }

    public float getProviderCt24() {
        return providerCt24;
    }

    public void setProviderCt24(float providerCt24) {
        this.providerCt24 = providerCt24;
    }

    public float getProviderCt36() {
        return providerCt36;
    }

    public void setProviderCt36(float providerCt36) {
        this.providerCt36 = providerCt36;
    }

    public float getTotalMRC() {
        return totalMRC;
    }

    public void setTotalMRC(float totalMRC) {
        this.totalMRC = totalMRC;
    }

    public String getParallelRunDays() {
        return parallelRunDays;
    }

    public void setParallelRunDays(String parallelRunDays) {
        this.parallelRunDays = parallelRunDays;
    }

    public String getLatLong() {
        return latLong;
    }

    public void setLatLong(String latLong) {
        this.latLong = latLong;
    }

    public String getIntfType() {
        return intfType;
    }

    public void setIntfType(String intfType) {
        this.intfType = intfType;
    }

    public String getCpeChassisChanged() {
        return cpeChassisChanged;
    }

    public void setCpeChassisChanged(String cpeChassisChanged) {
        this.cpeChassisChanged = cpeChassisChanged;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getVpnName() {
        return vpnName;
    }

    public void setVpnName(String vpnName) {
        this.vpnName = vpnName;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public float getLocalLoopBw() {
        return localLoopBw;
    }

    public void setLocalLoopBw(float localLoopBw) {
        this.localLoopBw = localLoopBw;
    }

    public String getMacdOption() {
        return macdOption;
    }

    public void setMacdOption(String macdOption) {
        this.macdOption = macdOption;
    }

    public String getBwunit() {
        return bwunit;
    }

    public void setBwunit(String bwunit) {
        this.bwunit = bwunit;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getTriggerFeasibility() {
        return triggerFeasibility;
    }

    public void setTriggerFeasibility(String triggerFeasibility) {
        this.triggerFeasibility = triggerFeasibility;
    }

    public String getQuotetypePartner() {
        return quotetypePartner;
    }

    public void setQuotetypePartner(String quotetypePartner) {
        this.quotetypePartner = quotetypePartner;
    }

    public String getServiceCommissionedDate() {
        return serviceCommissionedDate;
    }

    public void setServiceCommissionedDate(String serviceCommissionedDate) {
        this.serviceCommissionedDate = serviceCommissionedDate;
    }

    public String getOldLlBw() {
        return oldLlBw;
    }

    public void setOldLlBw(String oldLlBw) {
        this.oldLlBw = oldLlBw;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getOldContractTerm() {
        return oldContractTerm;
    }

    public void setOldContractTerm(String oldContractTerm) {
        this.oldContractTerm = oldContractTerm;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getFeasibilityResponseCreateDate() {
        return feasibilityResponseCreateDate;
    }

    public void setFeasibilityResponseCreateDate(String feasibilityResponseCreateDate) {
        this.feasibilityResponseCreateDate = feasibilityResponseCreateDate;
    }

    public String getLongg() {
        return longg;
    }

    public void setLongg(String longg) {
        this.longg = longg;
    }

    public String getProductSolution() {
        return productSolution;
    }

    public void setProductSolution(String productSolution) {
        this.productSolution = productSolution;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public float getBandwidth() {
        return bandwidth;
    }

    public void setBandwidth(float bandwidth) {
        this.bandwidth = bandwidth;
    }

    public String getLlChange() {
        return llChange;
    }

    public void setLlChange(String llChange) {
        this.llChange = llChange;
    }

    public String getOldPortBw() {
        return oldPortBw;
    }

    public void setOldPortBw(String oldPortBw) {
        this.oldPortBw = oldPortBw;
    }
	public String getFeasiblityId() {
		return feasiblityId;
	}

	public void setFeasiblityId(String feasiblityId) {
		this.feasiblityId = feasiblityId;
	}

    public String getXconnectIsInterfaceChanged() {
		return xconnectIsInterfaceChanged;
	}

	public void setXconnectIsInterfaceChanged(String xconnectIsInterfaceChanged) {
		this.xconnectIsInterfaceChanged = xconnectIsInterfaceChanged;
	}
	public String getxConnectXconnectMRCCurrency() {
		return xConnectXconnectMRCCurrency;
	}

	public void setxConnectXconnectMRCCurrency(String xConnectXconnectMRCCurrency) {
		this.xConnectXconnectMRCCurrency = xConnectXconnectMRCCurrency;
	}

    @JsonProperty("x_connect_Xconnect_MRC")
	public String getxConnectXconnectMRC() {
		return xConnectXconnectMRC;
	}

    @JsonProperty("x_connect_Xconnect_MRC")
	public void setxConnectXconnectMRC(String xConnectXconnectMRC) {
		this.xConnectXconnectMRC = xConnectXconnectMRC;
	}

    @JsonProperty("x_connect_Xconnect_NRC")
	public String getxConnectXconnectNRC() {
		return xConnectXconnectNRC;
	}

    @JsonProperty("x_connect_Xconnect_NRC")
	public void setxConnectXconnectNRC(String xConnectXconnectNRC) {
		this.xConnectXconnectNRC = xConnectXconnectNRC;
	}

	public String getCloudProvider() {
		return cloudProvider;
	}

	public void setCloudProvider(String cloudProvider) {
		this.cloudProvider = cloudProvider;
	}
	 public String getProviderXconnectInterface() {
	        return providerXconnectInterface;
	    }

	    public void setProviderXconnectInterface(String providerXconnectInterface) {
	        this.providerXconnectInterface = providerXconnectInterface;
	    }

	    public String getLMType() {
	        return LMType;
	   }

	    public void setLMType(String LMType) {
	        this.LMType = LMType;
	    }

	    public String getRecordType() {
	        return recordType;
	    }

	    public void setRecordType(String recordType) {
	        this.recordType = recordType;
	    }

	    public String getxConnectDistance() {
	        return xConnectDistance;
	    }

	    public void setxConnectDistance(String xConnectDistance) {
	        this.xConnectDistance = xConnectDistance;
	    }

	    public String getxConnectDBCode() {
	        return xConnectDBCode;
	    }

	    public void setxConnectDBCode(String xConnectDBCode) {
	        this.xConnectDBCode = xConnectDBCode;
	    }

	    public String getxConnectCountryID() {
	        return xConnectCountryID;
	    }

	    public void setxConnectCountryID(String xConnectCountryID) {
	        this.xConnectCountryID = xConnectCountryID;
	    }

	    public String getxConnectCountry() {
	        return xConnectCountry;
	    }

	    public void setxConnectCountry(String xConnectCountry) {
	        this.xConnectCountry = xConnectCountry;
	    }

	    public String getxConnectCity() {
	        return xConnectCity;
	    }

	    public void setxConnectCity(String xConnectCity) {
	        this.xConnectCity = xConnectCity;
	    }

	    public String getxConnectTCLPOPAddress() {
	        return xConnectTCLPOPAddress;
	    }

	    public void setxConnectTCLPOPAddress(String xConnectTCLPOPAddress) {
	        this.xConnectTCLPOPAddress = xConnectTCLPOPAddress;
	    }

	    public String getxConnectTCLPOPLatitude() {
	        return xConnectTCLPOPLatitude;
	    }

	    public void setxConnectTCLPOPLatitude(String xConnectTCLPOPLatitude) {
	        this.xConnectTCLPOPLatitude = xConnectTCLPOPLatitude;
	    }

	    public String getxConnectTCLPOPLongitude() {
	        return xConnectTCLPOPLongitude;
	    }

	    public void setxConnectTCLPOPLongitude(String xConnectTCLPOPLongitude) {
	        this.xConnectTCLPOPLongitude = xConnectTCLPOPLongitude;
	    }

	    public String getxConnectEAS() {
	        return xConnectEAS;
	    }

	    public void setxConnectEAS(String xConnectEAS) {
	        this.xConnectEAS = xConnectEAS;
	    }

	    public String getxConnectAllSitesLong() {
	        return xConnectAllSitesLong;
	    }

	    public void setxConnectAllSitesLong(String xConnectAllSitesLong) {
	        this.xConnectAllSitesLong = xConnectAllSitesLong;
	    }

	    public String getxConnectIsSelected() {
	        return xConnectIsSelected;
	    }

	    public void setxConnectIsSelected(String xConnectIsSelected) {
	        this.xConnectIsSelected = xConnectIsSelected;
	    }

	    public String getxConnectXconnectInterface() {
	        return xConnectXconnectInterface;
	    }

	    public void setxConnectXconnectInterface(String xConnectXconnectInterface) {
	        this.xConnectXconnectInterface = xConnectXconnectInterface;
	    }

	    public String getxConnectError() {
	        return xConnectError;
	    }

	    public void setxConnectError(String xConnectError) {
	        this.xConnectError = xConnectError;
	    }

	    public String getxConnectIsManualQuote() {
	        return xConnectIsManualQuote;
	    }

	    public void setxConnectIsManualQuote(String xConnectIsManualQuote) {
	        this.xConnectIsManualQuote = xConnectIsManualQuote;
	    }

	    public String getxConnectXCMedia() {
	        return xConnectXCMedia;
	    }

	    public void setxConnectXCMedia(String xConnectXCMedia) {
	        this.xConnectXCMedia = xConnectXCMedia;
	    }

	    public String getxConnectProviderProductName() {
	        return xConnectProviderProductName;
	    }

	    public void setxConnectProviderProductName(String xConnectProviderProductName) {
	        this.xConnectProviderProductName = xConnectProviderProductName;
	    }

	    public String getxConnectCrossConnectCharges() {
	        return xConnectCrossConnectCharges;
	    }

	    public void setxConnectCrossConnectCharges(String xConnectCrossConnectCharges) {
	        this.xConnectCrossConnectCharges = xConnectCrossConnectCharges;
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
    public String getProviderMRCCost() {
        return providerMRCCost;
    }

    @JsonProperty("provider_MRCCost")
    public void setProviderMRCCost(String providerMRCCost) {
        this.providerMRCCost = providerMRCCost;
    }

    @JsonProperty("provider_NRCCost")
    public String getProviderNRCCost() {
        return providerNRCCost;
    }

    @JsonProperty("provider_NRCCost")
    public void setProviderNRCCost(String providerNRCCost) {
        this.providerNRCCost = providerNRCCost;
    }
	public String getProviderAllSiteslong() {
		return providerAllSiteslong;
	}

	public void setProviderAllSiteslong(String providerAllSiteslong) {
		this.providerAllSiteslong = providerAllSiteslong;
	}
	public String getProvider_MRC_BW_Currency_Access() {
		return provider_MRC_BW_Currency_Access;
	}

	public void setProvider_MRC_BW_Currency_Access(String provider_MRC_BW_Currency_Access) {
		this.provider_MRC_BW_Currency_Access = provider_MRC_BW_Currency_Access;
	}

	public String getxConnectXconnectMRCCurrency_Access() {
		return xConnectXconnectMRCCurrency_Access;
	}

	public void setxConnectXconnectMRCCurrency_Access(String xConnectXconnectMRCCurrency_Access) {
		this.xConnectXconnectMRCCurrency_Access = xConnectXconnectMRCCurrency_Access;
	}
	public String getProviderPopAllSiteLatLong() {
		return providerPopAllSiteLatLong;
	}

	public void setProviderPopAllSiteLatLong(String providerPopAllSiteLatLong) {
		this.providerPopAllSiteLatLong = providerPopAllSiteLatLong;
	}

	public String getProviderPopAddresses() {
		return providerPopAddresses;
	}

	public void setProviderPopAddresses(String providerPopAddresses) {
		this.providerPopAddresses = providerPopAddresses;
	}

    public Double getxConnectXConnectMRCAccess() {
        return xConnectXConnectMRCAccess;
    }

    public void setxConnectXConnectMRCAccess(Double xConnectXConnectMRCAccess) {
        this.xConnectXConnectMRCAccess = xConnectXConnectMRCAccess;
    }

    public Double getxConnectXConnectNRCAccess() {
        return xConnectXConnectNRCAccess;
    }

    public void setxConnectXConnectNRCAccess(Double xConnectXConnectNRCAccess) {
        this.xConnectXConnectNRCAccess = xConnectXConnectNRCAccess;
    }

    public String getxConnectXConnectPopAllSitesLong() {
        return xConnectXConnectPopAllSitesLong;
    }

    public void setxConnectXConnectPopAllSitesLong(String xConnectXConnectPopAllSitesLong) {
        this.xConnectXConnectPopAllSitesLong = xConnectXConnectPopAllSitesLong;
    }

    public String getxConnectXConnectProviderName() {
        return xConnectXConnectProviderName;
    }

    public void setxConnectXConnectProviderName(String xConnectXConnectProviderName) {
        this.xConnectXConnectProviderName = xConnectXConnectProviderName;
    }
    @JsonProperty("cpe_intl_chassis_flag")
    public String getCpeIntlChassisFlag() {
        return cpeIntlChassisFlag;
    }
    @JsonProperty("cpe_intl_chassis_flag")
    public void setCpeIntlChassisFlag(String cpeIntlChassisFlag) {
        this.cpeIntlChassisFlag = cpeIntlChassisFlag;
    }
	public String getIsCustomer() {
		return isCustomer;
	}
	public void setIsCustomer(String isCustomer) {
		this.isCustomer = isCustomer;
	}
    
    
}
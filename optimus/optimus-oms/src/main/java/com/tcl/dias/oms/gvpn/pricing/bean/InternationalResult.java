package com.tcl.dias.oms.gvpn.pricing.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
    "Bucket_Adjustment_Type",
    "Burstable_BW",
    "CPE_HW_MP",
    "CPE_HW_charges",
    "CPE_HW_tot_charges",
    "CPE_Installation_MP",
    "CPE_Installation_charges",
    "CPE_Variant",
    "CPE_management_type",
    "CPE_supply_type",
    "Discounted_CPE_ARC",
    "Discounted_CPE_MRC",
    "Discounted_CPE_NRC",
    "GVPN_Port_ARC_Adjusted",
    "GVPN_Port_MRC_Adjusted",
    "GVPN_Port_NRC_Adjusted",
    "Last_Mile_Cost_ARC",
    "Last_Mile_Cost_MRC",
    "Last_Mile_Cost_NRC",
    "Last_Modified_Date",
    "Logistics_cost",
    "MRC",
    "NRC",
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
    "Support_MP",
    "Total_CPE_Price",
    "Type",
    "a_country",
    "account_id_with_18_digit",
    "additional_components",
    "additional_ip_flag",
    "additional_IP_ARC",
    "additional_IP_MRC",
    "backup_port_requested",
    "burst_per_MB_price_ARC",
    "burst_per_MB_price_MRC",
    "burstable_bw",
    "bw_mbps",
    "chassis_discount_pct",
    "city_class",
    "connection_type",
    "country",
    "cpe_management_type",
    "cpe_supply_type",
    "cpe_variant",
    "cu_le_id",
    "currency",
    "customer_segment",
    "customs_local_tax_pct",
    "feasibility_response_created_date",
    "install_charges",
    "ip_address_arrangement",
    "ipv4_address_pool_size",
    "ipv6_address_pool_size",
    "last_mile_contract_term",
    "latitude_final",
    "list_price",
    "local_loop_interface",
    "logistics_15_kg",
    "logistics_25_kg",
    "logistics_5_kg",
    "longitude_final",
    "management",
    "opportunity_term",
    "product_name",
    "prospect_name",
    "quoteType_quote",
    "quotetype_quote",
    "rank",
    "recovery",
    "region",
    "resp_city",
    "row_names",
    "sales_org",
    "siteFlag",
    "site_id",
    "sla_support",
    "smartnet_discount_pct",
    "sum_no_of_sites_uni_len",
    "support_charge_bill_frequency",
    "support_charges",
    "topology",
    "topology_secondary",
    "total_contract_value",
    "version_GVPN_Int",
    "weight_category",
    "error_code",
    "error_flag",
    "sp_CPE_Install_NRC",
    "sp_CPE_Outright_NRC",
    "sp_CPE_Rental_NRC",
    "sp_CPE_Management_ARC",
    "sp_lm_arc_bw_onwl",
    "sp_lm_nrc_bw_onwl",
    "sp_lm_nrc_mux_onwl",
    "sp_lm_nrc_inbldg_onwl",
    "sp_lm_nrc_ospcapex_onwl",
    "sp_lm_nrc_nerental_onwl",
    "sp_lm_arc_bw_prov_ofrf",
    "sp_lm_nrc_bw_prov_ofrf",
    "sp_lm_nrc_mast_ofrf",
    "sp_lm_arc_bw_onrf",
    "sp_lm_nrc_bw_onrf",
    "sp_lm_nrc_mast_onrf",
    "sp_CPE_Rental_ARC",
    "local_loop_bw",
    "Mast_3KM_avg_mast_ht",
    "avg_mast_ht",
    "min_hh_fatg",
    "POP_DIST_KM_SERVICE_MOD",
    "solution_type",
    "Orch_Connection",
    "Orch_LM_Type",
    "Orch_Category",
    "error_msg",
    "SFP_lp",
    "customs_local_taxes",
    "provider_MRCCost",
    "provider_NRCCost",
    "x_connect_Xconnect_MRC",
    "x_connect_Xconnect_NRC",
    "recovery",
    "management",
    "support_charges",
    "Logistics_cost",
    "shift_charge"
})
public class InternationalResult {

    @JsonProperty("Bucket_Adjustment_Type")
    private String bucketAdjustmentType;
    @JsonProperty("Burstable_BW")
    private String burstableBW;
    @JsonProperty("CPE_HW_MP")
    private String cPEHWMP;
    @JsonProperty("CPE_HW_charges")
    private String cPEHWCharges;
    @JsonProperty("CPE_HW_tot_charges")
    private String cPEHWTotCharges;
    @JsonProperty("CPE_Installation_MP")
    private String cPEInstallationMP;
    @JsonProperty("CPE_Installation_charges")
    private String cPEInstallationCharges;
    @JsonProperty("CPE_Variant")
    private String cPEVariant;
    @JsonProperty("CPE_management_type")
    private String cPEManagementType;
    @JsonProperty("CPE_supply_type")
    private String cPESupplyType;
    @JsonProperty("Discounted_CPE_ARC")
    private String discountedCPEARC;
    @JsonProperty("Discounted_CPE_MRC")
    private String discountedCPEMRC;
    @JsonProperty("Discounted_CPE_NRC")
    private String discountedCPENRC;
    @JsonProperty("GVPN_Port_ARC_Adjusted")
    private String gVPNPortARCAdjusted;
    @JsonProperty("GVPN_Port_MRC_Adjusted")
    private String gVPNPortMRCAdjusted;
    @JsonProperty("GVPN_Port_NRC_Adjusted")
    private String gVPNPortNRCAdjusted;
    @JsonProperty("Last_Mile_Cost_ARC")
    private String lastMileCostARC;
    @JsonProperty("Last_Mile_Cost_MRC")
    private String lastMileCostMRC;
    @JsonProperty("Last_Mile_Cost_NRC")
    private String lastMileCostNRC;
    @JsonProperty("Last_Modified_Date")
    private String lastModifiedDate;
    @JsonProperty("Logistics_cost")
    private String logisticsCost;
    @JsonProperty("MRC")
    private String mRC;
    @JsonProperty("NRC")
    private String nRC;
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
    @JsonProperty("Support_MP")
    private String supportMP;
    @JsonProperty("Total_CPE_Price")
    private String totalCPEPrice;
    @JsonProperty("Type")
    private String type;
    @JsonProperty("a_country")
    private String aCountry;
    @JsonProperty("account_id_with_18_digit")
    private String accountIdWith18Digit;
    @JsonProperty("additional_components")
    private String additionalComponents;
    @JsonProperty("additional_ip_flag")
    private String additionalIpFlag;
    @JsonProperty("backup_port_requested")
    private String backupPortRequested;
    @JsonProperty("burst_per_MB_price_ARC")
    private String burstPerMBPriceARC;
    @JsonProperty("burst_per_MB_price_MRC")
    private String burstPerMBPriceMRC;
    @JsonProperty("burstable_bw")
    private String burstableBw;
    @JsonProperty("bw_mbps")
    private String bwMbps;
    @JsonProperty("chassis_discount_pct")
    private String chassisDiscountPct;
    @JsonProperty("city_class")
    private String cityClass;
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
    @JsonProperty("currency")
    private String currency;
    @JsonProperty("customer_segment")
    private String customerSegment;
    @JsonProperty("customs_local_tax_pct")
    private String customsLocalTaxPct;
    @JsonProperty("feasibility_response_created_date")
    private String feasibilityResponseCreatedDate;
    @JsonProperty("install_charges")
    private String installCharges;
    @JsonProperty("ip_address_arrangement")
    private String ipAddressArrangement;
    @JsonProperty("ipv4_address_pool_size")
    private String ipv4AddressPoolSize;
    @JsonProperty("ipv6_address_pool_size")
    private String ipv6AddressPoolSize;
    @JsonProperty("last_mile_contract_term")
    private String lastMileContractTerm;
    @JsonProperty("latitude_final")
    private String latitudeFinal;
    @JsonProperty("list_price")
    private String listPrice;
    @JsonProperty("local_loop_interface")
    private String localLoopInterface;
    @JsonProperty("logistics_15_kg")
    private String logistics15Kg;
    @JsonProperty("logistics_25_kg")
    private String logistics25Kg;
    @JsonProperty("logistics_5_kg")
    private String logistics5Kg;
    @JsonProperty("longitude_final")
    private String longitudeFinal;
    @JsonProperty("management")
    private String management;
    @JsonProperty("opportunity_term")
    private String opportunityTerm;
    @JsonProperty("product_name")
    private String productName;
    @JsonProperty("prospect_name")
    private String prospectName;
    @JsonProperty("quoteType_quote")
    private String quoteTypeQuote;
    @JsonProperty("quotetype_quote")
    private String quotetypeQuote;
    @JsonProperty("rank")
    private String rank;
    @JsonProperty("recovery")
    private String recovery;
    @JsonProperty("region")
    private String region;
    @JsonProperty("resp_city")
    private String respCity;
    @JsonProperty("row_names")
    private String rowNames;
    @JsonProperty("sales_org")
    private String salesOrg;
    @JsonProperty("siteFlag")
    private String siteFlag;
    @JsonProperty("site_id")
    private String siteId;
    @JsonProperty("sla_support")
    private String slaSupport;
    @JsonProperty("smartnet_discount_pct")
    private String smartnetDiscountPct;
    @JsonProperty("sum_no_of_sites_uni_len")
    private String sumNoOfSitesUniLen;
    @JsonProperty("support_charge_bill_frequency")
    private String supportChargeBillFrequency;
    @JsonProperty("support_charges")
    private String supportCharges;
    @JsonProperty("topology")
    private String topology;
    @JsonProperty("topology_secondary")
    private String topologySecondary;
    @JsonProperty("total_contract_value")
    private String totalContractValue;
    @JsonProperty("version_GVPN_Int")
    private String versionGVPNInt;
    @JsonProperty("weight_category")
    private String weightCategory;
    @JsonProperty("error_code")
    private String errorCode;
    @JsonProperty("error_flag")
    private String errorFlag;
    @JsonProperty("error_msg")
    private String errorMsg;
    @JsonProperty("feasiblity_id")
    private String feasiblityId;
    @JsonProperty("x_connect_isInterfaceChanged")
    private String xConnectIsInterfaceChanged;
    @JsonProperty("provider_Local_Loop_Interface")
    private String providerLocalLoopInterface;
    @JsonProperty("provider_Local_Loop_Capacity")
    private String providerLocalLoopCapacity;
    @JsonProperty("cloud_provider")
    private String cloudProvider;
    @JsonProperty("provider_MRC_BW_Currency")
    private String providerMRCBWCurrency;
    @JsonProperty("provider_MRCCost")
    private String providerMRCCost;
    @JsonProperty("provider_NRCCost")
    private String providerNRCCost;
    @JsonProperty("product_solution")
    private String productSolution;
    @JsonProperty("provider_name")
    private String providerName;
    @JsonProperty("provider_product_name")
    private String providerProductName;
    @JsonProperty("x_connect_Xconnect_MRC_Currency")
    private String xConnectXconnectMRCCurrency;
    @JsonProperty("x_connect_Xconnect_MRC")
    private String xConnectXconnectMRC;
    @JsonProperty("x_connect_Xconnect_NRC")
    private String xConnectXconnectNRC;
    @JsonProperty("x_connect_commercialNote")
    private String xConnectCommercialNote;
    //commercial attribute
    @JsonProperty("sp_CPE_Install_NRC")
    private String spCPEInstallNRC;
    @JsonProperty("sp_CPE_Outright_NRC")
    private String spCPEOutrightNRC;
    @JsonProperty("sp_CPE_Rental_NRC")
    private String spCPERentalNRC;
    @JsonProperty("sp_CPE_Rental_ARC")
    private String spCPERentalARC;
    @JsonProperty("sp_CPE_Management_ARC")
    private String spCPEManagementARC;
    @JsonProperty("sp_lm_arc_bw_onwl")
    private String splmArcBwOnwl;
    @JsonProperty("sp_lm_nrc_bw_onwl")
    private String spLmNrcBwOnwl;
    @JsonProperty("sp_lm_nrc_mux_onwl")
    private String spLmNrcMuxOnwl;
    @JsonProperty("sp_lm_nrc_inbldg_onwl")
    private String spLmNrcInbldgOnwl;
    @JsonProperty("sp_lm_nrc_ospcapex_onwl")
    private String spLmNrcOspcapexOnwl;
    @JsonProperty("sp_lm_nrc_nerental_onwl")
    private String spLmNrcNerentalOnwl;
    @JsonProperty("sp_lm_arc_bw_prov_ofrf")
    private String spLmArcBwProvOfrf;
    @JsonProperty("sp_lm_nrc_bw_prov_ofrf")
    private String spLmNrcBwProvOfrf;
    @JsonProperty("sp_lm_nrc_mast_ofrf")
    private String spLmNrcMastOfrf;
    @JsonProperty("sp_lm_arc_bw_onrf")
    private String spLmArcBwOnrf;
    @JsonProperty("sp_lm_nrc_bw_onrf")
    private String spLmNrcBwOnrf;
    @JsonProperty("sp_lm_nrc_mast_onrf")
    private String spLmNrcMastOnrf;
    @JsonProperty("additional_IP_ARC")
    private String additionalIPARC;
    @JsonProperty("additional_IP_MRC")
    private String additionalIPMRC;
    @JsonProperty("local_loop_bw")
	private String localLoopBw;
    @JsonProperty("Mast_3KM_avg_mast_ht")
	private String mast3KMAvgMastHt; 
	@JsonProperty("avg_mast_ht")
	private String avgMastHt;
	@JsonProperty("min_hh_fatg")
	private String minHhFatg; 
	@JsonProperty("POP_DIST_KM_SERVICE_MOD")
	private String pOPDISTKMSERVICEMOD;
	@JsonProperty("solution_type")
	private String solutionType;
	@JsonProperty("Orch_Connection")
    private String orchConnection;
    @JsonProperty("Orch_LM_Type")
    private String orchLMType;
    @JsonProperty("Orch_Category")
    private String orchCategory;
    
    @JsonProperty("SFP_lp")
    private String SfpIp;
    @JsonProperty("customs_local_taxes")
    private String CustomsLocalTaxes;
	
    @JsonProperty("recovery")
    private String Recovery;
    @JsonProperty("management")
    private String Management;
    @JsonProperty("support_charges")
    private String SupportCharges;
    @JsonProperty("Logistics_cost")
    private String LogisticsCost;
    @JsonProperty("shift_charge")
    private String shiftCharge;
    @JsonProperty("cpe_disposable_charge")
    private String cpeDisposableCharge;

    @JsonProperty("local_loop_bw")
    public String getLocalLoopBw() {
		return localLoopBw;
	}

	@JsonProperty("local_loop_bw")
	public void setLocalLoopBw(String localLoopBw) {
		this.localLoopBw = localLoopBw;
	}

	@JsonProperty("Mast_3KM_avg_mast_ht")
	public String getMast3KMAvgMastHt() {
		return mast3KMAvgMastHt;
	}
	
	@JsonProperty("Mast_3KM_avg_mast_ht")
	public void setMast3KMAvgMastHt(String mast3kmAvgMastHt) {
		mast3KMAvgMastHt = mast3kmAvgMastHt;
	}

	@JsonProperty("avg_mast_ht")
	public String getAvgMastHt() {
		return avgMastHt;
	}

	@JsonProperty("avg_mast_ht")
	public void setAvgMastHt(String avgMastHt) {
		this.avgMastHt = avgMastHt;
	}

	@JsonProperty("min_hh_fatg")
	public String getMinHhFatg() {
		return minHhFatg;
	}

	@JsonProperty("min_hh_fatg")
	public void setMinHhFatg(String minHhFatg) {
		this.minHhFatg = minHhFatg;
	}

	@JsonProperty("POP_DIST_KM_SERVICE_MOD")
	public String getpOPDISTKMSERVICEMOD() {
		return pOPDISTKMSERVICEMOD;
	}

	@JsonProperty("POP_DIST_KM_SERVICE_MOD")
	public void setpOPDISTKMSERVICEMOD(String pOPDISTKMSERVICEMOD) {
		this.pOPDISTKMSERVICEMOD = pOPDISTKMSERVICEMOD;
	}

	@JsonProperty("solution_type")
	public String getSolutionType() {
		return solutionType;
	}

	@JsonProperty("solution_type")
	public void setSolutionType(String solutionType) {
		this.solutionType = solutionType;
	}
    
    
    @JsonProperty("additional_IP_ARC")
    public String getAdditionalIPARC() {
        return additionalIPARC;
    }

    @JsonProperty("additional_IP_ARC")
    public void setAdditionalIPARC(String additionalIPARC) {
        this.additionalIPARC = additionalIPARC;
    }

    @JsonProperty("additional_IP_MRC")
    public String getAdditionalIPMRC() {
        return additionalIPMRC;
    }

    @JsonProperty("additional_IP_MRC")
    public void setAdditionalIPMRC(String additionalIPMRC) {
        this.additionalIPMRC = additionalIPMRC;
    }
	
	
	@JsonProperty("x_connect_commercialNote")
    public String getxConnectCommercialNote() {
		return xConnectCommercialNote;
	}

    @JsonProperty("x_connect_commercialNote")
	public void setxConnectCommercialNote(String xConnectCommercialNote) {
		this.xConnectCommercialNote = xConnectCommercialNote;
	}
    

    @JsonProperty("Bucket_Adjustment_Type")
    public String getBucketAdjustmentType() {
        return bucketAdjustmentType;
    }

    @JsonProperty("Bucket_Adjustment_Type")
    public void setBucketAdjustmentType(String bucketAdjustmentType) {
        this.bucketAdjustmentType = bucketAdjustmentType;
    }

    @JsonProperty("Burstable_BW")
    public String getBurstableBW() {
        return burstableBW;
    }

    @JsonProperty("Burstable_BW")
    public void setBurstableBW(String burstableBW) {
        this.burstableBW = burstableBW;
    }

    @JsonProperty("CPE_HW_MP")
    public String getCPEHWMP() {
        return cPEHWMP;
    }

    @JsonProperty("CPE_HW_MP")
    public void setCPEHWMP(String cPEHWMP) {
        this.cPEHWMP = cPEHWMP;
    }

    @JsonProperty("CPE_HW_charges")
    public String getCPEHWCharges() {
        return cPEHWCharges;
    }

    @JsonProperty("CPE_HW_charges")
    public void setCPEHWCharges(String cPEHWCharges) {
        this.cPEHWCharges = cPEHWCharges;
    }

    @JsonProperty("CPE_HW_tot_charges")
    public String getCPEHWTotCharges() {
        return cPEHWTotCharges;
    }

    @JsonProperty("CPE_HW_tot_charges")
    public void setCPEHWTotCharges(String cPEHWTotCharges) {
        this.cPEHWTotCharges = cPEHWTotCharges;
    }

    @JsonProperty("CPE_Installation_MP")
    public String getCPEInstallationMP() {
        return cPEInstallationMP;
    }

    @JsonProperty("CPE_Installation_MP")
    public void setCPEInstallationMP(String cPEInstallationMP) {
        this.cPEInstallationMP = cPEInstallationMP;
    }

    @JsonProperty("CPE_Installation_charges")
    public String getCPEInstallationCharges() {
        return cPEInstallationCharges;
    }

    @JsonProperty("CPE_Installation_charges")
    public void setCPEInstallationCharges(String cPEInstallationCharges) {
        this.cPEInstallationCharges = cPEInstallationCharges;
    }

    @JsonProperty("CPE_Variant")
    public String getCPEVariant() {
        return cPEVariant;
    }

    @JsonProperty("CPE_Variant")
    public void setCPEVariant(String cPEVariant) {
        this.cPEVariant = cPEVariant;
    }

    @JsonProperty("CPE_management_type")
    public String getCPEManagementType() {
        return cPEManagementType;
    }

    @JsonProperty("CPE_management_type")
    public void setCPEManagementType(String cPEManagementType) {
        this.cPEManagementType = cPEManagementType;
    }

    @JsonProperty("CPE_supply_type")
    public String getCPESupplyType() {
        return cPESupplyType;
    }

    @JsonProperty("CPE_supply_type")
    public void setCPESupplyType(String cPESupplyType) {
        this.cPESupplyType = cPESupplyType;
    }

    @JsonProperty("Discounted_CPE_ARC")
    public String getDiscountedCPEARC() {
        return discountedCPEARC;
    }

    @JsonProperty("Discounted_CPE_ARC")
    public void setDiscountedCPEARC(String discountedCPEARC) {
        this.discountedCPEARC = discountedCPEARC;
    }

    @JsonProperty("Discounted_CPE_MRC")
    public String getDiscountedCPEMRC() {
        return discountedCPEMRC;
    }

    @JsonProperty("Discounted_CPE_MRC")
    public void setDiscountedCPEMRC(String discountedCPEMRC) {
        this.discountedCPEMRC = discountedCPEMRC;
    }

    @JsonProperty("Discounted_CPE_NRC")
    public String getDiscountedCPENRC() {
        return discountedCPENRC;
    }

    @JsonProperty("Discounted_CPE_NRC")
    public void setDiscountedCPENRC(String discountedCPENRC) {
        this.discountedCPENRC = discountedCPENRC;
    }

    @JsonProperty("GVPN_Port_ARC_Adjusted")
    public String getGVPNPortARCAdjusted() {
        return gVPNPortARCAdjusted;
    }

    @JsonProperty("GVPN_Port_ARC_Adjusted")
    public void setGVPNPortARCAdjusted(String gVPNPortARCAdjusted) {
        this.gVPNPortARCAdjusted = gVPNPortARCAdjusted;
    }

    @JsonProperty("GVPN_Port_MRC_Adjusted")
    public String getGVPNPortMRCAdjusted() {
        return gVPNPortMRCAdjusted;
    }

    @JsonProperty("GVPN_Port_MRC_Adjusted")
    public void setGVPNPortMRCAdjusted(String gVPNPortMRCAdjusted) {
        this.gVPNPortMRCAdjusted = gVPNPortMRCAdjusted;
    }

    @JsonProperty("GVPN_Port_NRC_Adjusted")
    public String getGVPNPortNRCAdjusted() {
        return gVPNPortNRCAdjusted;
    }

    @JsonProperty("GVPN_Port_NRC_Adjusted")
    public void setGVPNPortNRCAdjusted(String gVPNPortNRCAdjusted) {
        this.gVPNPortNRCAdjusted = gVPNPortNRCAdjusted;
    }

    @JsonProperty("Last_Mile_Cost_ARC")
    public String getLastMileCostARC() {
        return lastMileCostARC;
    }

    @JsonProperty("Last_Mile_Cost_ARC")
    public void setLastMileCostARC(String lastMileCostARC) {
        this.lastMileCostARC = lastMileCostARC;
    }

    @JsonProperty("Last_Mile_Cost_MRC")
    public String getLastMileCostMRC() {
        return lastMileCostMRC;
    }

    @JsonProperty("Last_Mile_Cost_MRC")
    public void setLastMileCostMRC(String lastMileCostMRC) {
        this.lastMileCostMRC = lastMileCostMRC;
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

    @JsonProperty("Logistics_cost")
    public String getLogisticsCost() {
        return logisticsCost;
    }

    @JsonProperty("Logistics_cost")
    public void setLogisticsCost(String logisticsCost) {
        this.logisticsCost = logisticsCost;
    }

    @JsonProperty("MRC")
    public String getMRC() {
        return mRC;
    }

    @JsonProperty("MRC")
    public void setMRC(String mRC) {
        this.mRC = mRC;
    }

    @JsonProperty("NRC")
    public String getNRC() {
        return nRC;
    }

    @JsonProperty("NRC")
    public void setNRC(String nRC) {
        this.nRC = nRC;
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

    @JsonProperty("Support_MP")
    public String getSupportMP() {
        return supportMP;
    }

    @JsonProperty("Support_MP")
    public void setSupportMP(String supportMP) {
        this.supportMP = supportMP;
    }

    @JsonProperty("Total_CPE_Price")
    public String getTotalCPEPrice() {
        return totalCPEPrice;
    }

    @JsonProperty("Total_CPE_Price")
    public void setTotalCPEPrice(String totalCPEPrice) {
        this.totalCPEPrice = totalCPEPrice;
    }

    @JsonProperty("Type")
    public String getType() {
        return type;
    }

    @JsonProperty("Type")
    public void setType(String type) {
        this.type = type;
    }

    @JsonProperty("a_country")
    public String getACountry() {
        return aCountry;
    }

    @JsonProperty("a_country")
    public void setACountry(String aCountry) {
        this.aCountry = aCountry;
    }

    @JsonProperty("account_id_with_18_digit")
    public String getAccountIdWith18Digit() {
        return accountIdWith18Digit;
    }

    @JsonProperty("account_id_with_18_digit")
    public void setAccountIdWith18Digit(String accountIdWith18Digit) {
        this.accountIdWith18Digit = accountIdWith18Digit;
    }

    @JsonProperty("additional_components")
    public String getAdditionalComponents() {
        return additionalComponents;
    }

    @JsonProperty("additional_components")
    public void setAdditionalComponents(String additionalComponents) {
        this.additionalComponents = additionalComponents;
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

    @JsonProperty("burst_per_MB_price_ARC")
    public String getBurstPerMBPriceARC() {
        return burstPerMBPriceARC;
    }

    @JsonProperty("burst_per_MB_price_ARC")
    public void setBurstPerMBPriceARC(String burstPerMBPriceARC) {
        this.burstPerMBPriceARC = burstPerMBPriceARC;
    }

    @JsonProperty("burst_per_MB_price_MRC")
    public String getBurstPerMBPriceMRC() {
        return burstPerMBPriceMRC;
    }

    @JsonProperty("burst_per_MB_price_MRC")
    public void setBurstPerMBPriceMRC(String burstPerMBPriceMRC) {
        this.burstPerMBPriceMRC = burstPerMBPriceMRC;
    }

    @JsonProperty("burstable_bw")
    public String getBurstableBw() {
        return burstableBw;
    }

    @JsonProperty("burstable_bw")
    public void setBurstableBw(String burstableBw) {
        this.burstableBw = burstableBw;
    }

    @JsonProperty("bw_mbps")
    public String getBwMbps() {
        return bwMbps;
    }

    @JsonProperty("bw_mbps")
    public void setBwMbps(String bwMbps) {
        this.bwMbps = bwMbps;
    }

    @JsonProperty("chassis_discount_pct")
    public String getChassisDiscountPct() {
        return chassisDiscountPct;
    }

    @JsonProperty("chassis_discount_pct")
    public void setChassisDiscountPct(String chassisDiscountPct) {
        this.chassisDiscountPct = chassisDiscountPct;
    }

    @JsonProperty("city_class")
    public String getCityClass() {
        return cityClass;
    }

    @JsonProperty("city_class")
    public void setCityClass(String cityClass) {
        this.cityClass = cityClass;
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

    @JsonProperty("currency")
    public String getCurrency() {
        return currency;
    }

    @JsonProperty("currency")
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @JsonProperty("customer_segment")
    public String getCustomerSegment() {
        return customerSegment;
    }

    @JsonProperty("customer_segment")
    public void setCustomerSegment(String customerSegment) {
        this.customerSegment = customerSegment;
    }

    @JsonProperty("customs_local_tax_pct")
    public String getCustomsLocalTaxPct() {
        return customsLocalTaxPct;
    }

    @JsonProperty("customs_local_tax_pct")
    public void setCustomsLocalTaxPct(String customsLocalTaxPct) {
        this.customsLocalTaxPct = customsLocalTaxPct;
    }

    @JsonProperty("feasibility_response_created_date")
    public String getFeasibilityResponseCreatedDate() {
        return feasibilityResponseCreatedDate;
    }

    @JsonProperty("feasibility_response_created_date")
    public void setFeasibilityResponseCreatedDate(String feasibilityResponseCreatedDate) {
        this.feasibilityResponseCreatedDate = feasibilityResponseCreatedDate;
    }

    @JsonProperty("install_charges")
    public String getInstallCharges() {
        return installCharges;
    }

    @JsonProperty("install_charges")
    public void setInstallCharges(String installCharges) {
        this.installCharges = installCharges;
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
    public String getLatitudeFinal() {
        return latitudeFinal;
    }

    @JsonProperty("latitude_final")
    public void setLatitudeFinal(String latitudeFinal) {
        this.latitudeFinal = latitudeFinal;
    }

    @JsonProperty("list_price")
    public String getListPrice() {
        return listPrice;
    }

    @JsonProperty("list_price")
    public void setListPrice(String listPrice) {
        this.listPrice = listPrice;
    }

    @JsonProperty("local_loop_interface")
    public String getLocalLoopInterface() {
        return localLoopInterface;
    }

    @JsonProperty("local_loop_interface")
    public void setLocalLoopInterface(String localLoopInterface) {
        this.localLoopInterface = localLoopInterface;
    }

    @JsonProperty("logistics_15_kg")
    public String getLogistics15Kg() {
        return logistics15Kg;
    }

    @JsonProperty("logistics_15_kg")
    public void setLogistics15Kg(String logistics15Kg) {
        this.logistics15Kg = logistics15Kg;
    }

    @JsonProperty("logistics_25_kg")
    public String getLogistics25Kg() {
        return logistics25Kg;
    }

    @JsonProperty("logistics_25_kg")
    public void setLogistics25Kg(String logistics25Kg) {
        this.logistics25Kg = logistics25Kg;
    }

    @JsonProperty("logistics_5_kg")
    public String getLogistics5Kg() {
        return logistics5Kg;
    }

    @JsonProperty("logistics_5_kg")
    public void setLogistics5Kg(String logistics5Kg) {
        this.logistics5Kg = logistics5Kg;
    }

    @JsonProperty("longitude_final")
    public String getLongitudeFinal() {
        return longitudeFinal;
    }

    @JsonProperty("longitude_final")
    public void setLongitudeFinal(String longitudeFinal) {
        this.longitudeFinal = longitudeFinal;
    }

    @JsonProperty("management")
    public String getManagement() {
        return management;
    }

    @JsonProperty("management")
    public void setManagement(String management) {
        this.management = management;
    }

    @JsonProperty("opportunity_term")
    public String getOpportunityTerm() {
        return opportunityTerm;
    }

    @JsonProperty("opportunity_term")
    public void setOpportunityTerm(String opportunityTerm) {
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

    @JsonProperty("rank")
    public String getRank() {
        return rank;
    }

    @JsonProperty("rank")
    public void setRank(String rank) {
        this.rank = rank;
    }

    @JsonProperty("recovery")
    public String getRecovery() {
        return recovery;
    }

    @JsonProperty("recovery")
    public void setRecovery(String recovery) {
        this.recovery = recovery;
    }

    @JsonProperty("region")
    public String getRegion() {
        return region;
    }

    @JsonProperty("region")
    public void setRegion(String region) {
        this.region = region;
    }

    @JsonProperty("resp_city")
    public String getRespCity() {
        return respCity;
    }

    @JsonProperty("resp_city")
    public void setRespCity(String respCity) {
        this.respCity = respCity;
    }

    @JsonProperty("row_names")
    public String getRowNames() {
        return rowNames;
    }

    @JsonProperty("row_names")
    public void setRowNames(String rowNames) {
        this.rowNames = rowNames;
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

    @JsonProperty("sla_support")
    public String getSlaSupport() {
        return slaSupport;
    }

    @JsonProperty("sla_support")
    public void setSlaSupport(String slaSupport) {
        this.slaSupport = slaSupport;
    }

    @JsonProperty("smartnet_discount_pct")
    public String getSmartnetDiscountPct() {
        return smartnetDiscountPct;
    }

    @JsonProperty("smartnet_discount_pct")
    public void setSmartnetDiscountPct(String smartnetDiscountPct) {
        this.smartnetDiscountPct = smartnetDiscountPct;
    }

    @JsonProperty("sum_no_of_sites_uni_len")
    public String getSumNoOfSitesUniLen() {
        return sumNoOfSitesUniLen;
    }

    @JsonProperty("sum_no_of_sites_uni_len")
    public void setSumNoOfSitesUniLen(String sumNoOfSitesUniLen) {
        this.sumNoOfSitesUniLen = sumNoOfSitesUniLen;
    }

    @JsonProperty("support_charge_bill_frequency")
    public String getSupportChargeBillFrequency() {
        return supportChargeBillFrequency;
    }

    @JsonProperty("support_charge_bill_frequency")
    public void setSupportChargeBillFrequency(String supportChargeBillFrequency) {
        this.supportChargeBillFrequency = supportChargeBillFrequency;
    }

    @JsonProperty("support_charges")
    public String getSupportCharges() {
        return supportCharges;
    }

    @JsonProperty("support_charges")
    public void setSupportCharges(String supportCharges) {
        this.supportCharges = supportCharges;
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

    @JsonProperty("total_contract_value")
    public String getTotalContractValue() {
        return totalContractValue;
    }

    @JsonProperty("total_contract_value")
    public void setTotalContractValue(String totalContractValue) {
        this.totalContractValue = totalContractValue;
    }

    @JsonProperty("version_GVPN_Int")
    public String getVersionGVPNInt() {
        return versionGVPNInt;
    }

    @JsonProperty("version_GVPN_Int")
    public void setVersionGVPNInt(String versionGVPNInt) {
        this.versionGVPNInt = versionGVPNInt;
    }

    @JsonProperty("weight_category")
    public String getWeightCategory() {
        return weightCategory;
    }

    @JsonProperty("weight_category")
    public void setWeightCategory(String weightCategory) {
        this.weightCategory = weightCategory;
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
    
    @JsonProperty("sp_CPE_Install_NRC")
	public String getSpCPEInstallNRC() {
		return spCPEInstallNRC;
	}

    @JsonProperty("sp_CPE_Install_NRC")
	public void setSpCPEInstallNRC(String spCPEInstallNRC) {
		this.spCPEInstallNRC = spCPEInstallNRC;
	}
    
    @JsonProperty("sp_CPE_Outright_NRC")
	public String getSpCPEOutrightNRC() {
		return spCPEOutrightNRC;
	}

    @JsonProperty("sp_CPE_Outright_NRC")
	public void setSpCPEOutrightNRC(String spCPEOutrightNRC) {
		this.spCPEOutrightNRC = spCPEOutrightNRC;
	}

    @JsonProperty("sp_CPE_Rental_NRC")
	public String getSpCPERentalNRC() {
		return spCPERentalNRC;
	}

    @JsonProperty("sp_CPE_Rental_NRC")
	public void setSpCPERentalNRC(String spCPERentalNRC) {
		this.spCPERentalNRC = spCPERentalNRC;
	}

    @JsonProperty("sp_CPE_Rental_ARC")
	public String getSpCPERentalARC() {
		return spCPERentalARC;
	}

    @JsonProperty("sp_CPE_Rental_ARC")
	public void setSpCPERentalARC(String spCPERentalARC) {
		this.spCPERentalARC = spCPERentalARC;
	}

    @JsonProperty("sp_CPE_Management_ARC")
	public String getSpCPEManagementARC() {
		return spCPEManagementARC;
	}

    @JsonProperty("sp_CPE_Management_ARC")
	public void setSpCPEManagementARC(String spCPEManagementARC) {
		this.spCPEManagementARC = spCPEManagementARC;
	}

    @JsonProperty("sp_lm_arc_bw_onwl")
	public String getSplmArcBwOnwl() {
		return splmArcBwOnwl;
	}

    @JsonProperty("sp_lm_arc_bw_onwl")
	public void setSplmArcBwOnwl(String splmArcBwOnwl) {
		this.splmArcBwOnwl = splmArcBwOnwl;
	}

    @JsonProperty("sp_lm_nrc_bw_onwl")
	public String getSpLmNrcBwOnwl() {
		return spLmNrcBwOnwl;
	}

    @JsonProperty("sp_lm_nrc_bw_onwl")
	public void setSpLmNrcBwOnwl(String spLmNrcBwOnwl) {
		this.spLmNrcBwOnwl = spLmNrcBwOnwl;
	}

    @JsonProperty("sp_lm_nrc_mux_onwl")
	public String getSpLmNrcMuxOnwl() {
		return spLmNrcMuxOnwl;
	}

    @JsonProperty("sp_lm_nrc_mux_onwl")
	public void setSpLmNrcMuxOnwl(String spLmNrcMuxOnwl) {
		this.spLmNrcMuxOnwl = spLmNrcMuxOnwl;
	}

    
    @JsonProperty("sp_lm_nrc_inbldg_onwl")
	public String getSpLmNrcInbldgOnwl() {
		return spLmNrcInbldgOnwl;
	}

    @JsonProperty("sp_lm_nrc_inbldg_onwl")
	public void setSpLmNrcInbldgOnwl(String spLmNrcInbldgOnwl) {
		this.spLmNrcInbldgOnwl = spLmNrcInbldgOnwl;
	}

    @JsonProperty("sp_lm_nrc_ospcapex_onwl")
	public String getSpLmNrcOspcapexOnwl() {
		return spLmNrcOspcapexOnwl;
	}

    @JsonProperty("sp_lm_nrc_ospcapex_onwl")
	public void setSpLmNrcOspcapexOnwl(String spLmNrcOspcapexOnwl) {
		this.spLmNrcOspcapexOnwl = spLmNrcOspcapexOnwl;
	}

    @JsonProperty("sp_lm_nrc_nerental_onwl")
	public String getSpLmNrcNerentalOnwl() {
		return spLmNrcNerentalOnwl;
	}

    @JsonProperty("sp_lm_nrc_nerental_onwl")
	public void setSpLmNrcNerentalOnwl(String spLmNrcNerentalOnwl) {
		this.spLmNrcNerentalOnwl = spLmNrcNerentalOnwl;
	}

    
    @JsonProperty("sp_lm_arc_bw_prov_ofrf")
	public String getSpLmArcBwProvOfrf() {
		return spLmArcBwProvOfrf;
	}

    @JsonProperty("sp_lm_arc_bw_prov_ofrf")
	public void setSpLmArcBwProvOfrf(String spLmArcBwProvOfrf) {
		this.spLmArcBwProvOfrf = spLmArcBwProvOfrf;
	}

    
    @JsonProperty("sp_lm_nrc_bw_prov_ofrf")
	public String getSpLmNrcBwProvOfrf() {
		return spLmNrcBwProvOfrf;
	}

    @JsonProperty("sp_lm_nrc_bw_prov_ofrf")
	public void setSpLmNrcBwProvOfrf(String spLmNrcBwProvOfrf) {
		this.spLmNrcBwProvOfrf = spLmNrcBwProvOfrf;
	}

    
    @JsonProperty("sp_lm_nrc_mast_ofrf")
	public String getSpLmNrcMastOfrf() {
		return spLmNrcMastOfrf;
	}

    @JsonProperty("sp_lm_nrc_mast_ofrf")
	public void setSpLmNrcMastOfrf(String spLmNrcMastOfrf) {
		this.spLmNrcMastOfrf = spLmNrcMastOfrf;
	}

    @JsonProperty("sp_lm_arc_bw_onrf")
	public String getSpLmArcBwOnrf() {
		return spLmArcBwOnrf;
	}

    @JsonProperty("sp_lm_arc_bw_onrf")
	public void setSpLmArcBwOnrf(String spLmArcBwOnrf) {
		this.spLmArcBwOnrf = spLmArcBwOnrf;
	}

    @JsonProperty("sp_lm_nrc_bw_onrf")
	public String getSpLmNrcBwOnrf() {
		return spLmNrcBwOnrf;
	}

    @JsonProperty("sp_lm_nrc_bw_onrf")
	public void setSpLmNrcBwOnrf(String spLmNrcBwOnrf) {
		this.spLmNrcBwOnrf = spLmNrcBwOnrf;
	}
    
    @JsonProperty("sp_lm_nrc_mast_onrf")
	public String getSpLmNrcMastOnrf() {
		return spLmNrcMastOnrf;
	}

    @JsonProperty("sp_lm_nrc_mast_onrf")
	public void setSpLmNrcMastOnrf(String spLmNrcMastOnrf) {
		this.spLmNrcMastOnrf = spLmNrcMastOnrf;
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
    @JsonProperty("Orch_Category")
    public String getOrchCategory() {
    return orchCategory;
    }

    @JsonProperty("Orch_Category")
    public void setOrchCategory(String orchCategory) {
    this.orchCategory = orchCategory;
    }

    


    @JsonProperty("SFP_lp")
    public String getSfpIp() {
        return SfpIp;
    }

    @JsonProperty("SFP_lp")
    public void setSfpIp(String SfpIp) {
        this.SfpIp = SfpIp;
    }

    @JsonProperty("customs_local_taxes")
    public String getCustomsLocalTaxes() {
        return CustomsLocalTaxes;
    }

    @JsonProperty("customs_local_taxes")
    public void setCustomsLocalTaxes(String CustomsLocalTaxes) {
        this.CustomsLocalTaxes = CustomsLocalTaxes;
    }

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
    @JsonProperty("shift_charge")
    public String getShiftCharge() {
    return shiftCharge;
    }

    @JsonProperty("shift_charge")
    public void setShiftCharge(String shiftCharge) {
    this.shiftCharge = shiftCharge;
    }

	public String getCpeDisposableCharge() {
		return cpeDisposableCharge;
	}

	public void setCpeDisposableCharge(String cpeDisposableCharge) {
		this.cpeDisposableCharge = cpeDisposableCharge;
	}

	@JsonProperty("provider_Local_Loop_Interface")
	public String getProviderLocalLoopInterface() {
		return providerLocalLoopInterface;
	}

	@JsonProperty("provider_Local_Loop_Interface")
	public void setProviderLocalLoopInterface(String providerLocalLoopInterface) {
		this.providerLocalLoopInterface = providerLocalLoopInterface;
	}
	

}

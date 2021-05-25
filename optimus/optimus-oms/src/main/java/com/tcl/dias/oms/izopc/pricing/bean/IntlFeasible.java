package com.tcl.dias.oms.izopc.pricing.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
/**
 * This class is used to process intl Feasible sites
 * @author PAULRAJ SUNDAR
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
    "version",
    "trigger_feasibility",
    "macd_option",
    "service_commissioned_date",
    "old_contract_term",
    "lat_long",
    "ll_change",
    "macd_service",
    "old_Ll_Bw",
    "old_Port_Bw",
    "vpn_Name",
    "parallel_run_days",
    "cpe_chassis_changed",
    "local_loop_bw",
    "cpe_intl_chassis_flag",
    "provider_Provider_Name"
})
public class IntlFeasible {

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
    private String dataCentreLoc;
    @JsonProperty("cloud_provider")
   	private String cloudProvider;
    @JsonProperty("service_id")
   	private String serviceId;
    @JsonProperty("manual_feasibility_req")
   	private String manualFeasibilityReq;
    @JsonProperty("manual_feasibility_reason")
   	private String manualFeasibilityReason;
    
    @JsonProperty("version")
   	private String version;
    //MACD Attributes
    @JsonProperty("parallel_run_days")
    private String parallelRunDays;
    @JsonProperty("lat_long")
    private String latLong;
    @JsonProperty("cpe_chassis_changed")
    private String cpeChassisChanged;
    @JsonProperty("vpn_Name")
    private String vpnName;
    @JsonProperty("local_loop_bw")
    private float localLoopBw;
    @JsonProperty("macd_option")
    private String macdOption;
    @JsonProperty("service_commissioned_date")
    private String serviceCommissionedDate;
    @JsonProperty("old_Ll_Bw")
    private String oldLlBw;
    @JsonProperty("trigger_feasibility")
    private String triggerFeasibility;
    @JsonProperty("old_contract_term")
    private String oldContractTerm;
    @JsonProperty("ll_change")
    private String llChange;
    @JsonProperty("old_Port_Bw")
    private String oldPortBw;
    @JsonProperty("cpe_intl_chassis_flag")
    private String cpeIntlChassisFlag;
    @JsonProperty("macd_service")
    private String macdService;
    @JsonProperty("provider_Provider_Name")
    private String providerProviderName;
       
   	public String getVersion() {
   		return version;
   	}

   	public void setVersion(String version) {
   		this.version = version;
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
    public String getDataCentreLoc() {
		return dataCentreLoc;
	}
	public void setDataCentreLoc(String dataCentreLoc) {
		this.dataCentreLoc = dataCentreLoc;
	}

	public String getCloudProvider() {
		return cloudProvider;
	}

	public void setCloudProvider(String cloudProvider) {
		this.cloudProvider = cloudProvider;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	public String getManualFeasibilityReq() {
		return manualFeasibilityReq;
	}

	public void setManualFeasibilityReq(String manualFeasibilityReq) {
		this.manualFeasibilityReq = manualFeasibilityReq;
	}

	public String getManualFeasibilityReason() {
		return manualFeasibilityReason;
	}

	public void setManualFeasibilityReason(String manualFeasibilityReason) {
		this.manualFeasibilityReason = manualFeasibilityReason;
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

    public String getCpeChassisChanged() {
        return cpeChassisChanged;
    }

    public void setCpeChassisChanged(String cpeChassisChanged) {
        this.cpeChassisChanged = cpeChassisChanged;
    }

    public String getVpnName() {
        return vpnName;
    }

    public void setVpnName(String vpnName) {
        this.vpnName = vpnName;
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

    public String getTriggerFeasibility() {
        return triggerFeasibility;
    }

    public void setTriggerFeasibility(String triggerFeasibility) {
        this.triggerFeasibility = triggerFeasibility;
    }

    public String getOldContractTerm() {
        return oldContractTerm;
    }

    public void setOldContractTerm(String oldContractTerm) {
        this.oldContractTerm = oldContractTerm;
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

    public String getCpeIntlChassisFlag() {
        return cpeIntlChassisFlag;
    }

    public void setCpeIntlChassisFlag(String cpeIntlChassisFlag) {
        this.cpeIntlChassisFlag = cpeIntlChassisFlag;
    }

    public String getMacdService() {
        return macdService;
    }

    public void setMacdService(String macdService) {
        this.macdService = macdService;
    }

    public String getProviderProviderName() {
        return providerProviderName;
    }

    public void setProviderProviderName(String providerProviderName) {
        this.providerProviderName = providerProviderName;
    }
}
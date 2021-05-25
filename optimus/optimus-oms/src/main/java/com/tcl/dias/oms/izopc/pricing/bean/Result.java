package com.tcl.dias.oms.izopc.pricing.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
/**
 * All pricing response information holding for each sites
 * 
 * @author PAULRAJ SUNDAR
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
    "Account_RTM_Cust",
    "Account_id_with_18_Digit",
    "Adjusted_CPE_Discount",
    "Adjustment_Factor",
    "BW_mbps_upd",
    "Bucket_Adjustment_Type",
    "Burstable_BW",
    "CPE_HW_MP",
    "CPE_Hardware_LP_USD",
    "CPE_Installation_INR",
    "CPE_Installation_MP",
    "CPE_Management_INR",
    "CPE_Pricing_Req",
    "CPE_Support_INR",
    "CPE_Support_MP",
    "CPE_Variant",
    "CPE_management_type",
    "CPE_supply_type",
    "Discounted_CPE_ARC",
    "Discounted_CPE_MRC",
    "Discounted_CPE_NRC",
    "GVPN_ARC",
    "GVPN_ARC_per_BW",
    "GVPN_CPE_ARC",
    "GVPN_CPE_MRC",
    "GVPN_CPE_NRC",
    "GVPN_CPE_Predicted_Discount",
    "GVPN_Port_ARC_Adjusted",
    "GVPN_Port_MRC_Adjusted",
    "GVPN_Port_NRC_Adjusted",
    "ILL_ARC_per_BW",
    "Identifier",
    "Industry_Cust",
    "Inv_GVPN_bw",
    "Inv_ILL_bw",
    "Inv_NPL_bw",
    "Inv_Other_bw",
    "Inv_Tot_BW",
    "Last_Mile_Cost_ARC",
    "Last_Mile_Cost_MRC",
    "Last_Mile_Cost_NRC",
    "Last_Modified_Date",
    "NPL_ARC_per_BW",
    "NRC",
    "OEM_Discount",
    "OpportunityID_Prod_Identifier",
    "Orch_Connection",
    "Orch_LM_Type",
    "Other_ARC_per_BW",
    "Overall_calc_arc_list_inr",
    "Recovery_INR",
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
    "Sum_Offnet_Flag",
    "Sum_Onnet_Flag",
    "Sum_Other_Flag",
    "Sum_tot_oppy_historic_opp",
    "Sum_tot_oppy_historic_prod",
    "TOT_ARC_per_BW",
    "Total_CPE_Cost",
    "Total_CPE_Price",
    "additional_IP_ARC",
    "additional_IP_MRC",
    "additional_ip_flag",
    "backup_port_requested",
    "burst_per_MB_price_ARC",
    "burst_per_MB_price_MRC",
    "burstable_bw",
    "bw_mbps",
    "connection_type",
    "country",
    "cpe_management_type",
    "cpe_supply_type",
    "cpe_variant",
    "createdDate_quote",
    "customer_segment",
    "datediff",
    "error_code",
    "error_flag",
    "error_msg",
    "f_lm_arc_bw_onrf",
    "f_lm_arc_bw_onwl",
    "f_lm_arc_bw_prov_ofrf",
    "f_lm_nrc_bw_onrf",
    "f_lm_nrc_bw_onwl",
    "f_lm_nrc_bw_prov_ofrf",
    "f_lm_nrc_inbldg_onwl",
    "f_lm_nrc_mast_ofrf",
    "f_lm_nrc_mast_onrf",
    "f_lm_nrc_mux_onwl",
    "f_lm_nrc_nerental_onwl",
    "f_lm_nrc_ospcapex_onwl",
    "feasibility_response_created_date",
    "hist_flag",
    "ip_address_arrangement",
    "ipv4_address_pool_size",
    "ipv6_address_pool_size",
    "last_mile_contract_term",
    "latitude_final",
    "list_price_mb",
    "local_loop_interface",
    "longitude_final",
    "opportunity_day",
    "opportunity_month",
    "opportunity_term",
    "overall_node",
    "overall_node_CPE",
    "p_lm_arc_bw_onrf",
    "p_lm_arc_bw_onwl",
    "p_lm_arc_bw_prov_ofrf",
    "p_lm_nrc_bw_onrf",
    "p_lm_nrc_bw_onwl",
    "p_lm_nrc_bw_prov_ofrf",
    "p_lm_nrc_inbldg_onwl",
    "p_lm_nrc_mast_ofrf",
    "p_lm_nrc_mast_onrf",
    "p_lm_nrc_mux_onwl",
    "p_lm_nrc_nerental_onwl",
    "p_lm_nrc_ospcapex_onwl",
    "port_lm_arc",
    "port_pred_discount",
    "predicted_GVPN_Port_ARC",
    "predicted_GVPN_Port_NRC",
    "product_name",
    "prospect_name",
    "quoteType_quote",
    "quotetype_quote",
    "resp_city",
    "row_names",
    "sales_org",
    "siteFlag",
    "site_id",
    "sum_cat1_2_Opportunity",
    "sum_cat_3_Opportunity",
    "sum_cat_4_Opportunity",
    "sum_no_of_sites_uni_len",
    "time_taken",
    "topology",
    "total_contract_value",
    "IZO_Port_ARC_Adjusted",
    "IZO_Port_MRC_Adjusted",
    "IZO_Port_NRC_Adjusted",
    "version",
    "rcFlag"
})
public class Result{

    @JsonProperty("Account_RTM_Cust")
    private String accountRTMCust;
    @JsonProperty("Account_id_with_18_Digit")
    private String accountIdWith18Digit;
    @JsonProperty("Adjusted_CPE_Discount")
    private String adjustedCPEDiscount;
    @JsonProperty("Adjustment_Factor")
    private String adjustmentFactor;
    @JsonProperty("BW_mbps_upd")
    private String bWMbpsUpd;
    @JsonProperty("Bucket_Adjustment_Type")
    private String bucketAdjustmentType;
    @JsonProperty("Burstable_BW")
    private String burstableBW;
    @JsonProperty("CPE_HW_MP")
    private String cPEHWMP;
    @JsonProperty("CPE_Hardware_LP_USD")
    private String cPEHardwareLPUSD;
    @JsonProperty("CPE_Installation_INR")
    private String cPEInstallationINR;
    @JsonProperty("CPE_Installation_MP")
    private String cPEInstallationMP;
    @JsonProperty("CPE_Management_INR")
    private String cPEManagementINR;
    @JsonProperty("CPE_Pricing_Req")
    private String cPEPricingReq;
    @JsonProperty("CPE_Support_INR")
    private String cPESupportINR;
    @JsonProperty("CPE_Support_MP")
    private String cPESupportMP;
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
    @JsonProperty("GVPN_ARC")
    private String gVPNARC;
    @JsonProperty("GVPN_ARC_per_BW")
    private String gVPNARCPerBW;
    @JsonProperty("GVPN_CPE_ARC")
    private String gVPNCPEARC;
    @JsonProperty("GVPN_CPE_MRC")
    private String gVPNCPEMRC;
    @JsonProperty("GVPN_CPE_NRC")
    private String gVPNCPENRC;
    @JsonProperty("GVPN_CPE_Predicted_Discount")
    private String gVPNCPEPredictedDiscount;
    @JsonProperty("GVPN_Port_ARC_Adjusted")
    private String gVPNPortARCAdjusted;
    @JsonProperty("GVPN_Port_MRC_Adjusted")
    private String gVPNPortMRCAdjusted;
    @JsonProperty("GVPN_Port_NRC_Adjusted")
    private String gVPNPortNRCAdjusted;
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
    @JsonProperty("Last_Mile_Cost_MRC")
    private String lastMileCostMRC;
    @JsonProperty("Last_Mile_Cost_NRC")
    private String lastMileCostNRC;
    @JsonProperty("Last_Modified_Date")
    private String lastModifiedDate;
    @JsonProperty("NPL_ARC_per_BW")
    private String nPLARCPerBW;
    @JsonProperty("NRC")
    private String nRC;
    @JsonProperty("OEM_Discount")
    private String oEMDiscount;
    @JsonProperty("OpportunityID_Prod_Identifier")
    private String opportunityIDProdIdentifier;
    @JsonProperty("Orch_Connection")
    private String orchConnection;
    @JsonProperty("Orch_LM_Type")
    private String orchLMType;
    @JsonProperty("Other_ARC_per_BW")
    private String otherARCPerBW;
    @JsonProperty("Overall_calc_arc_list_inr")
    private String overallCalcArcListInr;
    @JsonProperty("Recovery_INR")
    private String recoveryINR;
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
    @JsonProperty("Sum_Offnet_Flag")
    private String sumOffnetFlag;
    @JsonProperty("Sum_Onnet_Flag")
    private String sumOnnetFlag;
    @JsonProperty("Sum_Other_Flag")
    private String sumOtherFlag;
    @JsonProperty("Sum_tot_oppy_historic_opp")
    private String sumTotOppyHistoricOpp;
    @JsonProperty("Sum_tot_oppy_historic_prod")
    private String sumTotOppyHistoricProd;
    @JsonProperty("TOT_ARC_per_BW")
    private String tOTARCPerBW;
    @JsonProperty("Total_CPE_Cost")
    private String totalCPECost;
    @JsonProperty("Total_CPE_Price")
    private String totalCPEPrice;
    @JsonProperty("additional_IP_ARC")
    private String additionalIPARC;
    @JsonProperty("additional_IP_MRC")
    private String additionalIPMRC;
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
    @JsonProperty("createdDate_quote")
    private String createdDateQuote;
    @JsonProperty("customer_segment")
    private String customerSegment;
    @JsonProperty("datediff")
    private String datediff;
    @JsonProperty("error_code")
    private String errorCode;
    @JsonProperty("error_flag")
    private String errorFlag;
    @JsonProperty("error_msg")
    private String errorMsg;
    @JsonProperty("f_lm_arc_bw_onrf")
    private String fLmArcBwOnrf;
    @JsonProperty("f_lm_arc_bw_onwl")
    private String fLmArcBwOnwl;
    @JsonProperty("f_lm_arc_bw_prov_ofrf")
    private String fLmArcBwProvOfrf;
    @JsonProperty("f_lm_nrc_bw_onrf")
    private String fLmNrcBwOnrf;
    @JsonProperty("f_lm_nrc_bw_onwl")
    private String fLmNrcBwOnwl;
    @JsonProperty("f_lm_nrc_bw_prov_ofrf")
    private String fLmNrcBwProvOfrf;
    @JsonProperty("f_lm_nrc_inbldg_onwl")
    private String fLmNrcInbldgOnwl;
    @JsonProperty("f_lm_nrc_mast_ofrf")
    private String fLmNrcMastOfrf;
    @JsonProperty("f_lm_nrc_mast_onrf")
    private String fLmNrcMastOnrf;
    @JsonProperty("f_lm_nrc_mux_onwl")
    private String fLmNrcMuxOnwl;
    @JsonProperty("f_lm_nrc_nerental_onwl")
    private String fLmNrcNerentalOnwl;
    @JsonProperty("f_lm_nrc_ospcapex_onwl")
    private String fLmNrcOspcapexOnwl;
    @JsonProperty("feasibility_response_created_date")
    private String feasibilityResponseCreatedDate;
    @JsonProperty("hist_flag")
    private String histFlag;
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
    @JsonProperty("list_price_mb")
    private String listPriceMb;
    @JsonProperty("local_loop_interface")
    private String localLoopInterface;
    @JsonProperty("longitude_final")
    private String longitudeFinal;
    @JsonProperty("opportunity_day")
    private String opportunityDay;
    @JsonProperty("opportunity_month")
    private String opportunityMonth;
    @JsonProperty("opportunity_term")
    private String opportunityTerm;
    @JsonProperty("overall_node")
    private String overallNode;
    @JsonProperty("overall_node_CPE")
    private String overallNodeCPE;
    @JsonProperty("p_lm_arc_bw_onrf")
    private String pLmArcBwOnrf;
    @JsonProperty("p_lm_arc_bw_onwl")
    private String pLmArcBwOnwl;
    @JsonProperty("p_lm_arc_bw_prov_ofrf")
    private String pLmArcBwProvOfrf;
    @JsonProperty("p_lm_nrc_bw_onrf")
    private String pLmNrcBwOnrf;
    @JsonProperty("p_lm_nrc_bw_onwl")
    private String pLmNrcBwOnwl;
    @JsonProperty("p_lm_nrc_bw_prov_ofrf")
    private String pLmNrcBwProvOfrf;
    @JsonProperty("p_lm_nrc_inbldg_onwl")
    private String pLmNrcInbldgOnwl;
    @JsonProperty("p_lm_nrc_mast_ofrf")
    private String pLmNrcMastOfrf;
    @JsonProperty("p_lm_nrc_mast_onrf")
    private String pLmNrcMastOnrf;
    @JsonProperty("p_lm_nrc_mux_onwl")
    private String pLmNrcMuxOnwl;
    @JsonProperty("p_lm_nrc_nerental_onwl")
    private String pLmNrcNerentalOnwl;
    @JsonProperty("p_lm_nrc_ospcapex_onwl")
    private String pLmNrcOspcapexOnwl;
    @JsonProperty("port_lm_arc")
    private String portLmArc;
    @JsonProperty("port_pred_discount")
    private String portPredDiscount;
    @JsonProperty("predicted_GVPN_Port_ARC")
    private String predictedGVPNPortARC;
    @JsonProperty("predicted_GVPN_Port_NRC")
    private String predictedGVPNPortNRC;
    @JsonProperty("product_name")
    private String productName;
    @JsonProperty("prospect_name")
    private String prospectName;
    @JsonProperty("quoteType_quote")
    private String quoteTypeQuote;
    @JsonProperty("quotetype_quote")
    private String quotetypeQuote;
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
    @JsonProperty("sum_cat1_2_Opportunity")
    private String sumCat12Opportunity;
    @JsonProperty("sum_cat_3_Opportunity")
    private String sumCat3Opportunity;
    @JsonProperty("sum_cat_4_Opportunity")
    private String sumCat4Opportunity;
    @JsonProperty("sum_no_of_sites_uni_len")
    private String sumNoOfSitesUniLen;
    @JsonProperty("time_taken")
    private String timeTaken;
    @JsonProperty("topology")
    private String topology;
    @JsonProperty("total_contract_value")
    private String totalContractValue;
    @JsonProperty("IZO_Port_ARC_Adjusted")
    private String izoPortARCAdjusted;
    @JsonProperty("IZO_Port_MRC_Adjusted")
    private String izoPortMRCAdjusted;
    @JsonProperty("IZO_Port_NRC_Adjusted")
    private String izoPortNRCAdjusted;
    @JsonProperty("version")
	private String version;
    @JsonProperty("rc_flag")
	private String rcFlag;

	public String getRcFlag() {
		return rcFlag;
	}

	public void setRcFlag(String rcFlag) {
		this.rcFlag = rcFlag;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

    
    
    @JsonProperty("Account_RTM_Cust")
    public String getAccountRTMCust() {
        return accountRTMCust;
    }

    @JsonProperty("Account_RTM_Cust")
    public void setAccountRTMCust(String accountRTMCust) {
        this.accountRTMCust = accountRTMCust;
    }

    @JsonProperty("Account_id_with_18_Digit")
    public String getAccountIdWith18Digit() {
        return accountIdWith18Digit;
    }

    @JsonProperty("Account_id_with_18_Digit")
    public void setAccountIdWith18Digit(String accountIdWith18Digit) {
        this.accountIdWith18Digit = accountIdWith18Digit;
    }

    @JsonProperty("Adjusted_CPE_Discount")
    public String getAdjustedCPEDiscount() {
        return adjustedCPEDiscount;
    }

    @JsonProperty("Adjusted_CPE_Discount")
    public void setAdjustedCPEDiscount(String adjustedCPEDiscount) {
        this.adjustedCPEDiscount = adjustedCPEDiscount;
    }

    @JsonProperty("Adjustment_Factor")
    public String getAdjustmentFactor() {
        return adjustmentFactor;
    }

    @JsonProperty("Adjustment_Factor")
    public void setAdjustmentFactor(String adjustmentFactor) {
        this.adjustmentFactor = adjustmentFactor;
    }

    @JsonProperty("BW_mbps_upd")
    public String getBWMbpsUpd() {
        return bWMbpsUpd;
    }

    @JsonProperty("BW_mbps_upd")
    public void setBWMbpsUpd(String bWMbpsUpd) {
        this.bWMbpsUpd = bWMbpsUpd;
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

    @JsonProperty("CPE_Hardware_LP_USD")
    public String getCPEHardwareLPUSD() {
        return cPEHardwareLPUSD;
    }

    @JsonProperty("CPE_Hardware_LP_USD")
    public void setCPEHardwareLPUSD(String cPEHardwareLPUSD) {
        this.cPEHardwareLPUSD = cPEHardwareLPUSD;
    }

    @JsonProperty("CPE_Installation_INR")
    public String getCPEInstallationINR() {
        return cPEInstallationINR;
    }

    @JsonProperty("CPE_Installation_INR")
    public void setCPEInstallationINR(String cPEInstallationINR) {
        this.cPEInstallationINR = cPEInstallationINR;
    }

    @JsonProperty("CPE_Installation_MP")
    public String getCPEInstallationMP() {
        return cPEInstallationMP;
    }

    @JsonProperty("CPE_Installation_MP")
    public void setCPEInstallationMP(String cPEInstallationMP) {
        this.cPEInstallationMP = cPEInstallationMP;
    }

    @JsonProperty("CPE_Management_INR")
    public String getCPEManagementINR() {
        return cPEManagementINR;
    }

    @JsonProperty("CPE_Management_INR")
    public void setCPEManagementINR(String cPEManagementINR) {
        this.cPEManagementINR = cPEManagementINR;
    }

    @JsonProperty("CPE_Pricing_Req")
    public String getCPEPricingReq() {
        return cPEPricingReq;
    }

    @JsonProperty("CPE_Pricing_Req")
    public void setCPEPricingReq(String cPEPricingReq) {
        this.cPEPricingReq = cPEPricingReq;
    }

    @JsonProperty("CPE_Support_INR")
    public String getCPESupportINR() {
        return cPESupportINR;
    }

    @JsonProperty("CPE_Support_INR")
    public void setCPESupportINR(String cPESupportINR) {
        this.cPESupportINR = cPESupportINR;
    }

    @JsonProperty("CPE_Support_MP")
    public String getCPESupportMP() {
        return cPESupportMP;
    }

    @JsonProperty("CPE_Support_MP")
    public void setCPESupportMP(String cPESupportMP) {
        this.cPESupportMP = cPESupportMP;
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

    @JsonProperty("GVPN_ARC")
    public String getGVPNARC() {
        return gVPNARC;
    }

    @JsonProperty("GVPN_ARC")
    public void setGVPNARC(String gVPNARC) {
        this.gVPNARC = gVPNARC;
    }

    @JsonProperty("GVPN_ARC_per_BW")
    public String getGVPNARCPerBW() {
        return gVPNARCPerBW;
    }

    @JsonProperty("GVPN_ARC_per_BW")
    public void setGVPNARCPerBW(String gVPNARCPerBW) {
        this.gVPNARCPerBW = gVPNARCPerBW;
    }

    @JsonProperty("GVPN_CPE_ARC")
    public String getGVPNCPEARC() {
        return gVPNCPEARC;
    }

    @JsonProperty("GVPN_CPE_ARC")
    public void setGVPNCPEARC(String gVPNCPEARC) {
        this.gVPNCPEARC = gVPNCPEARC;
    }

    @JsonProperty("GVPN_CPE_MRC")
    public String getGVPNCPEMRC() {
        return gVPNCPEMRC;
    }

    @JsonProperty("GVPN_CPE_MRC")
    public void setGVPNCPEMRC(String gVPNCPEMRC) {
        this.gVPNCPEMRC = gVPNCPEMRC;
    }

    @JsonProperty("GVPN_CPE_NRC")
    public String getGVPNCPENRC() {
        return gVPNCPENRC;
    }

    @JsonProperty("GVPN_CPE_NRC")
    public void setGVPNCPENRC(String gVPNCPENRC) {
        this.gVPNCPENRC = gVPNCPENRC;
    }

    @JsonProperty("GVPN_CPE_Predicted_Discount")
    public String getGVPNCPEPredictedDiscount() {
        return gVPNCPEPredictedDiscount;
    }

    @JsonProperty("GVPN_CPE_Predicted_Discount")
    public void setGVPNCPEPredictedDiscount(String gVPNCPEPredictedDiscount) {
        this.gVPNCPEPredictedDiscount = gVPNCPEPredictedDiscount;
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

    @JsonProperty("NPL_ARC_per_BW")
    public String getNPLARCPerBW() {
        return nPLARCPerBW;
    }

    @JsonProperty("NPL_ARC_per_BW")
    public void setNPLARCPerBW(String nPLARCPerBW) {
        this.nPLARCPerBW = nPLARCPerBW;
    }

    @JsonProperty("NRC")
    public String getNRC() {
        return nRC;
    }

    @JsonProperty("NRC")
    public void setNRC(String nRC) {
        this.nRC = nRC;
    }

    @JsonProperty("OEM_Discount")
    public String getOEMDiscount() {
        return oEMDiscount;
    }

    @JsonProperty("OEM_Discount")
    public void setOEMDiscount(String oEMDiscount) {
        this.oEMDiscount = oEMDiscount;
    }

    @JsonProperty("OpportunityID_Prod_Identifier")
    public String getOpportunityIDProdIdentifier() {
        return opportunityIDProdIdentifier;
    }

    @JsonProperty("OpportunityID_Prod_Identifier")
    public void setOpportunityIDProdIdentifier(String opportunityIDProdIdentifier) {
        this.opportunityIDProdIdentifier = opportunityIDProdIdentifier;
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

    @JsonProperty("Other_ARC_per_BW")
    public String getOtherARCPerBW() {
        return otherARCPerBW;
    }

    @JsonProperty("Other_ARC_per_BW")
    public void setOtherARCPerBW(String otherARCPerBW) {
        this.otherARCPerBW = otherARCPerBW;
    }

    @JsonProperty("Overall_calc_arc_list_inr")
    public String getOverallCalcArcListInr() {
        return overallCalcArcListInr;
    }

    @JsonProperty("Overall_calc_arc_list_inr")
    public void setOverallCalcArcListInr(String overallCalcArcListInr) {
        this.overallCalcArcListInr = overallCalcArcListInr;
    }

    @JsonProperty("Recovery_INR")
    public String getRecoveryINR() {
        return recoveryINR;
    }

    @JsonProperty("Recovery_INR")
    public void setRecoveryINR(String recoveryINR) {
        this.recoveryINR = recoveryINR;
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

    @JsonProperty("Sum_Offnet_Flag")
    public String getSumOffnetFlag() {
        return sumOffnetFlag;
    }

    @JsonProperty("Sum_Offnet_Flag")
    public void setSumOffnetFlag(String sumOffnetFlag) {
        this.sumOffnetFlag = sumOffnetFlag;
    }

    @JsonProperty("Sum_Onnet_Flag")
    public String getSumOnnetFlag() {
        return sumOnnetFlag;
    }

    @JsonProperty("Sum_Onnet_Flag")
    public void setSumOnnetFlag(String sumOnnetFlag) {
        this.sumOnnetFlag = sumOnnetFlag;
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

    @JsonProperty("Total_CPE_Cost")
    public String getTotalCPECost() {
        return totalCPECost;
    }

    @JsonProperty("Total_CPE_Cost")
    public void setTotalCPECost(String totalCPECost) {
        this.totalCPECost = totalCPECost;
    }

    @JsonProperty("Total_CPE_Price")
    public String getTotalCPEPrice() {
        return totalCPEPrice;
    }

    @JsonProperty("Total_CPE_Price")
    public void setTotalCPEPrice(String totalCPEPrice) {
        this.totalCPEPrice = totalCPEPrice;
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

    @JsonProperty("createdDate_quote")
    public String getCreatedDateQuote() {
        return createdDateQuote;
    }

    @JsonProperty("createdDate_quote")
    public void setCreatedDateQuote(String createdDateQuote) {
        this.createdDateQuote = createdDateQuote;
    }

    @JsonProperty("customer_segment")
    public String getCustomerSegment() {
        return customerSegment;
    }

    @JsonProperty("customer_segment")
    public void setCustomerSegment(String customerSegment) {
        this.customerSegment = customerSegment;
    }

    @JsonProperty("datediff")
    public String getDatediff() {
        return datediff;
    }

    @JsonProperty("datediff")
    public void setDatediff(String datediff) {
        this.datediff = datediff;
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

    @JsonProperty("f_lm_arc_bw_onrf")
    public String getFLmArcBwOnrf() {
        return fLmArcBwOnrf;
    }

    @JsonProperty("f_lm_arc_bw_onrf")
    public void setFLmArcBwOnrf(String fLmArcBwOnrf) {
        this.fLmArcBwOnrf = fLmArcBwOnrf;
    }

    @JsonProperty("f_lm_arc_bw_onwl")
    public String getFLmArcBwOnwl() {
        return fLmArcBwOnwl;
    }

    @JsonProperty("f_lm_arc_bw_onwl")
    public void setFLmArcBwOnwl(String fLmArcBwOnwl) {
        this.fLmArcBwOnwl = fLmArcBwOnwl;
    }

    @JsonProperty("f_lm_arc_bw_prov_ofrf")
    public String getFLmArcBwProvOfrf() {
        return fLmArcBwProvOfrf;
    }

    @JsonProperty("f_lm_arc_bw_prov_ofrf")
    public void setFLmArcBwProvOfrf(String fLmArcBwProvOfrf) {
        this.fLmArcBwProvOfrf = fLmArcBwProvOfrf;
    }

    @JsonProperty("f_lm_nrc_bw_onrf")
    public String getFLmNrcBwOnrf() {
        return fLmNrcBwOnrf;
    }

    @JsonProperty("f_lm_nrc_bw_onrf")
    public void setFLmNrcBwOnrf(String fLmNrcBwOnrf) {
        this.fLmNrcBwOnrf = fLmNrcBwOnrf;
    }

    @JsonProperty("f_lm_nrc_bw_onwl")
    public String getFLmNrcBwOnwl() {
        return fLmNrcBwOnwl;
    }

    @JsonProperty("f_lm_nrc_bw_onwl")
    public void setFLmNrcBwOnwl(String fLmNrcBwOnwl) {
        this.fLmNrcBwOnwl = fLmNrcBwOnwl;
    }

    @JsonProperty("f_lm_nrc_bw_prov_ofrf")
    public String getFLmNrcBwProvOfrf() {
        return fLmNrcBwProvOfrf;
    }

    @JsonProperty("f_lm_nrc_bw_prov_ofrf")
    public void setFLmNrcBwProvOfrf(String fLmNrcBwProvOfrf) {
        this.fLmNrcBwProvOfrf = fLmNrcBwProvOfrf;
    }

    @JsonProperty("f_lm_nrc_inbldg_onwl")
    public String getFLmNrcInbldgOnwl() {
        return fLmNrcInbldgOnwl;
    }

    @JsonProperty("f_lm_nrc_inbldg_onwl")
    public void setFLmNrcInbldgOnwl(String fLmNrcInbldgOnwl) {
        this.fLmNrcInbldgOnwl = fLmNrcInbldgOnwl;
    }

    @JsonProperty("f_lm_nrc_mast_ofrf")
    public String getFLmNrcMastOfrf() {
        return fLmNrcMastOfrf;
    }

    @JsonProperty("f_lm_nrc_mast_ofrf")
    public void setFLmNrcMastOfrf(String fLmNrcMastOfrf) {
        this.fLmNrcMastOfrf = fLmNrcMastOfrf;
    }

    @JsonProperty("f_lm_nrc_mast_onrf")
    public String getFLmNrcMastOnrf() {
        return fLmNrcMastOnrf;
    }

    @JsonProperty("f_lm_nrc_mast_onrf")
    public void setFLmNrcMastOnrf(String fLmNrcMastOnrf) {
        this.fLmNrcMastOnrf = fLmNrcMastOnrf;
    }

    @JsonProperty("f_lm_nrc_mux_onwl")
    public String getFLmNrcMuxOnwl() {
        return fLmNrcMuxOnwl;
    }

    @JsonProperty("f_lm_nrc_mux_onwl")
    public void setFLmNrcMuxOnwl(String fLmNrcMuxOnwl) {
        this.fLmNrcMuxOnwl = fLmNrcMuxOnwl;
    }

    @JsonProperty("f_lm_nrc_nerental_onwl")
    public String getFLmNrcNerentalOnwl() {
        return fLmNrcNerentalOnwl;
    }

    @JsonProperty("f_lm_nrc_nerental_onwl")
    public void setFLmNrcNerentalOnwl(String fLmNrcNerentalOnwl) {
        this.fLmNrcNerentalOnwl = fLmNrcNerentalOnwl;
    }

    @JsonProperty("f_lm_nrc_ospcapex_onwl")
    public String getFLmNrcOspcapexOnwl() {
        return fLmNrcOspcapexOnwl;
    }

    @JsonProperty("f_lm_nrc_ospcapex_onwl")
    public void setFLmNrcOspcapexOnwl(String fLmNrcOspcapexOnwl) {
        this.fLmNrcOspcapexOnwl = fLmNrcOspcapexOnwl;
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

    @JsonProperty("list_price_mb")
    public String getListPriceMb() {
        return listPriceMb;
    }

    @JsonProperty("list_price_mb")
    public void setListPriceMb(String listPriceMb) {
        this.listPriceMb = listPriceMb;
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
    public String getLongitudeFinal() {
        return longitudeFinal;
    }

    @JsonProperty("longitude_final")
    public void setLongitudeFinal(String longitudeFinal) {
        this.longitudeFinal = longitudeFinal;
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

    @JsonProperty("overall_node")
    public String getOverallNode() {
        return overallNode;
    }

    @JsonProperty("overall_node")
    public void setOverallNode(String overallNode) {
        this.overallNode = overallNode;
    }

    @JsonProperty("overall_node_CPE")
    public String getOverallNodeCPE() {
        return overallNodeCPE;
    }

    @JsonProperty("overall_node_CPE")
    public void setOverallNodeCPE(String overallNodeCPE) {
        this.overallNodeCPE = overallNodeCPE;
    }

    @JsonProperty("p_lm_arc_bw_onrf")
    public String getPLmArcBwOnrf() {
        return pLmArcBwOnrf;
    }

    @JsonProperty("p_lm_arc_bw_onrf")
    public void setPLmArcBwOnrf(String pLmArcBwOnrf) {
        this.pLmArcBwOnrf = pLmArcBwOnrf;
    }

    @JsonProperty("p_lm_arc_bw_onwl")
    public String getPLmArcBwOnwl() {
        return pLmArcBwOnwl;
    }

    @JsonProperty("p_lm_arc_bw_onwl")
    public void setPLmArcBwOnwl(String pLmArcBwOnwl) {
        this.pLmArcBwOnwl = pLmArcBwOnwl;
    }

    @JsonProperty("p_lm_arc_bw_prov_ofrf")
    public String getPLmArcBwProvOfrf() {
        return pLmArcBwProvOfrf;
    }

    @JsonProperty("p_lm_arc_bw_prov_ofrf")
    public void setPLmArcBwProvOfrf(String pLmArcBwProvOfrf) {
        this.pLmArcBwProvOfrf = pLmArcBwProvOfrf;
    }

    @JsonProperty("p_lm_nrc_bw_onrf")
    public String getPLmNrcBwOnrf() {
        return pLmNrcBwOnrf;
    }

    @JsonProperty("p_lm_nrc_bw_onrf")
    public void setPLmNrcBwOnrf(String pLmNrcBwOnrf) {
        this.pLmNrcBwOnrf = pLmNrcBwOnrf;
    }

    @JsonProperty("p_lm_nrc_bw_onwl")
    public String getPLmNrcBwOnwl() {
        return pLmNrcBwOnwl;
    }

    @JsonProperty("p_lm_nrc_bw_onwl")
    public void setPLmNrcBwOnwl(String pLmNrcBwOnwl) {
        this.pLmNrcBwOnwl = pLmNrcBwOnwl;
    }

    @JsonProperty("p_lm_nrc_bw_prov_ofrf")
    public String getPLmNrcBwProvOfrf() {
        return pLmNrcBwProvOfrf;
    }

    @JsonProperty("p_lm_nrc_bw_prov_ofrf")
    public void setPLmNrcBwProvOfrf(String pLmNrcBwProvOfrf) {
        this.pLmNrcBwProvOfrf = pLmNrcBwProvOfrf;
    }

    @JsonProperty("p_lm_nrc_inbldg_onwl")
    public String getPLmNrcInbldgOnwl() {
        return pLmNrcInbldgOnwl;
    }

    @JsonProperty("p_lm_nrc_inbldg_onwl")
    public void setPLmNrcInbldgOnwl(String pLmNrcInbldgOnwl) {
        this.pLmNrcInbldgOnwl = pLmNrcInbldgOnwl;
    }

    @JsonProperty("p_lm_nrc_mast_ofrf")
    public String getPLmNrcMastOfrf() {
        return pLmNrcMastOfrf;
    }

    @JsonProperty("p_lm_nrc_mast_ofrf")
    public void setPLmNrcMastOfrf(String pLmNrcMastOfrf) {
        this.pLmNrcMastOfrf = pLmNrcMastOfrf;
    }

    @JsonProperty("p_lm_nrc_mast_onrf")
    public String getPLmNrcMastOnrf() {
        return pLmNrcMastOnrf;
    }

    @JsonProperty("p_lm_nrc_mast_onrf")
    public void setPLmNrcMastOnrf(String pLmNrcMastOnrf) {
        this.pLmNrcMastOnrf = pLmNrcMastOnrf;
    }

    @JsonProperty("p_lm_nrc_mux_onwl")
    public String getPLmNrcMuxOnwl() {
        return pLmNrcMuxOnwl;
    }

    @JsonProperty("p_lm_nrc_mux_onwl")
    public void setPLmNrcMuxOnwl(String pLmNrcMuxOnwl) {
        this.pLmNrcMuxOnwl = pLmNrcMuxOnwl;
    }

    @JsonProperty("p_lm_nrc_nerental_onwl")
    public String getPLmNrcNerentalOnwl() {
        return pLmNrcNerentalOnwl;
    }

    @JsonProperty("p_lm_nrc_nerental_onwl")
    public void setPLmNrcNerentalOnwl(String pLmNrcNerentalOnwl) {
        this.pLmNrcNerentalOnwl = pLmNrcNerentalOnwl;
    }

    @JsonProperty("p_lm_nrc_ospcapex_onwl")
    public String getPLmNrcOspcapexOnwl() {
        return pLmNrcOspcapexOnwl;
    }

    @JsonProperty("p_lm_nrc_ospcapex_onwl")
    public void setPLmNrcOspcapexOnwl(String pLmNrcOspcapexOnwl) {
        this.pLmNrcOspcapexOnwl = pLmNrcOspcapexOnwl;
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

    @JsonProperty("predicted_GVPN_Port_ARC")
    public String getPredictedGVPNPortARC() {
        return predictedGVPNPortARC;
    }

    @JsonProperty("predicted_GVPN_Port_ARC")
    public void setPredictedGVPNPortARC(String predictedGVPNPortARC) {
        this.predictedGVPNPortARC = predictedGVPNPortARC;
    }

    @JsonProperty("predicted_GVPN_Port_NRC")
    public String getPredictedGVPNPortNRC() {
        return predictedGVPNPortNRC;
    }

    @JsonProperty("predicted_GVPN_Port_NRC")
    public void setPredictedGVPNPortNRC(String predictedGVPNPortNRC) {
        this.predictedGVPNPortNRC = predictedGVPNPortNRC;
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

    @JsonProperty("sum_no_of_sites_uni_len")
    public String getSumNoOfSitesUniLen() {
        return sumNoOfSitesUniLen;
    }

    @JsonProperty("sum_no_of_sites_uni_len")
    public void setSumNoOfSitesUniLen(String sumNoOfSitesUniLen) {
        this.sumNoOfSitesUniLen = sumNoOfSitesUniLen;
    }

    @JsonProperty("time_taken")
    public String getTimeTaken() {
        return timeTaken;
    }

    @JsonProperty("time_taken")
    public void setTimeTaken(String timeTaken) {
        this.timeTaken = timeTaken;
    }

    @JsonProperty("topology")
    public String getTopology() {
        return topology;
    }

    @JsonProperty("topology")
    public void setTopology(String topology) {
        this.topology = topology;
    }

    @JsonProperty("total_contract_value")
    public String getTotalContractValue() {
        return totalContractValue;
    }

    @JsonProperty("total_contract_value")
    public void setTotalContractValue(String totalContractValue) {
        this.totalContractValue = totalContractValue;
    }

	public String getIzoPortARCAdjusted() {
		return izoPortARCAdjusted;
	}

	public void setIzoPortARCAdjusted(String izoPortARCAdjusted) {
		this.izoPortARCAdjusted = izoPortARCAdjusted;
	}

	public String getIzoPortMRCAdjusted() {
		return izoPortMRCAdjusted;
	}

	public void setIzoPortMRCAdjusted(String izoPortMRCAdjusted) {
		this.izoPortMRCAdjusted = izoPortMRCAdjusted;
	}

	public String getIzoPortNRCAdjusted() {
		return izoPortNRCAdjusted;
	}

	public void setIzoPortNRCAdjusted(String izoPortNRCAdjusted) {
		this.izoPortNRCAdjusted = izoPortNRCAdjusted;
	}

}

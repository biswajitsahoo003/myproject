package com.tcl.dias.oms.beans;

/**
 * This file contains the FeasibleEngineInput.java class.
 * 
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "BW_mbps_2", "Service_ID", "bw_arc_cost", "total_cost", "X0.5km_avg_dist", "POP_DIST_KM",
		"X0.5km_prospect_avg_bw", "Latitude_final", "OnnetCity_tag", "Network_F_NF_CC", "Network_Feasibility_Check",
		"core_check_CC", "X5km_min_bw", "access_check_CC", "rank", "POP_DIST_KM_SERVICE_MOD", "X5km_max_bw",
		"X5km_prospect_perc_feasible", "POP_Category", "FATG_Building_Type", "HH_0_5_access_rings_F", "hh_flag",
		"local_loop_interface", "X5km_prospect_avg_dist", "FATG_Network_Location_Type", "BW_mbps_act", "city_tier",
		"X2km_prospect_min_dist", "X2km_prospect_perc_feasible", "X0.5km_prospect_num_feasible", "Orch_Connection",
		"BW_mbps", "resp_city", "Burstable_BW", "connected_cust_tag", "net_pre_feasible_flag", "Network_F_NF_CC_Flag",
		"Orch_LM_Type", "X5km_prospect_avg_bw", "FATG_Category", "Feasibility.Response..Created.Date",
		"X2km_prospect_min_bw", "X5km_prospect_num_feasible", "Selected", "resp_state", "X5km_prospect_count",
		"X0.5km_avg_bw", "X2km_cust_count", "Customer_Segment", "FATG_PROW", "connected_building_tag", "FATG_DIST_KM",
		"X0.5km_prospect_max_bw", "X5km_prospect_min_dist", "FATG_Ring_type", "Orch_BW", "scenario_1", "scenario_2",
		"sno", "access_check_hh", "X2km_prospect_count", "min_hh_fatg", "POP_Construction_Status", "X5km_avg_dist",
		"X2km_avg_dist", "X2km_prospect_avg_bw", "X0.5km_prospect_min_dist", "cost_permeter", "mux_cost", "Site_id",
		"prospect_name", "Predicted_Access_Feasibility", "bw_otc_cost", "X2km_max_bw", "X2km_prospect_max_bw",
		"last_mile_contract_term", "X5km_prospect_min_bw", "ne_rental_cost", "X2km_min_bw", "seq_no", "osp_capex_cost",
		"Sales.Org", "X0.5km_cust_count", "X0.5km_min_bw", "Network_F_NF_HH", "HH_0_5_access_rings_NF",
		"X0.5km_min_dist", "POP_Building_Type", "core_check_hh", "HH_DIST_KM", "X0.5km_prospect_min_bw",
		"POP_DIST_KM_SERVICE", "num_connected_building", "Orch_Category", "X0.5km_prospect_avg_dist",
		"POP_Network_Location_Type", "num_connected_cust", "HH_0_5km", "X5km_min_dist", "X2km_prospect_num_feasible",
		"X2km_avg_bw", "X0.5km_max_bw", "X5km_avg_bw", "X5km_prospect_max_bw", "X2km_prospect_avg_dist",
		"Longitude_final", "X5km_cust_count", "Product.Name", "X0.5km_prospect_perc_feasible", "POP_TCL_Access",
		"X0.5km_prospect_count", "in_building_capex_cost", "Probabililty_Access_Feasibility", "X2km_min_dist",
		"FATG_TCL_Access", "Network_F_NF_HH_Flag" })
public class FeasibleEngineOutputBean {

	@JsonProperty("BW_mbps_2")
	private Double bWMbps2;
	@JsonProperty("Service_ID")
	private Double serviceID;
	@JsonProperty("bw_arc_cost")
	private Double bwArcCost;
	@JsonProperty("total_cost")
	private Double totalCost;
	@JsonProperty("X0.5km_avg_dist")
	private Double x05kmAvgDist;
	@JsonProperty("POP_DIST_KM")
	private Double pOPDISTKM;
	@JsonProperty("X0.5km_prospect_avg_bw")
	private Double x05kmProspectAvgBw;
	@JsonProperty("Latitude_final")
	private Double latitudeFinal;
	@JsonProperty("OnnetCity_tag")
	private Double onnetCityTag;
	@JsonProperty("Network_F_NF_CC")
	private String networkFNFCC;
	@JsonProperty("Network_Feasibility_Check")
	private String networkFeasibilityCheck;
	@JsonProperty("core_check_CC")
	private String coreCheckCC;
	@JsonProperty("X5km_min_bw")
	private Double x5kmMinBw;
	@JsonProperty("access_check_CC")
	private String accessCheckCC;
	@JsonProperty("rank")
	private Integer rank;
	@JsonProperty("POP_DIST_KM_SERVICE_MOD")
	private Double pOPDISTKMSERVICEMOD;
	@JsonProperty("X5km_max_bw")
	private Double x5kmMaxBw;
	@JsonProperty("X5km_prospect_perc_feasible")
	private Double x5kmProspectPercFeasible;
	@JsonProperty("POP_Category")
	private String pOPCategory;
	@JsonProperty("FATG_Building_Type")
	private String fATGBuildingType;
	@JsonProperty("HH_0_5_access_rings_F")
	private String hH05AccessRingsF;
	@JsonProperty("hh_flag")
	private String hhFlag;
	@JsonProperty("local_loop_interface")
	private String localLoopInterface;
	@JsonProperty("X5km_prospect_avg_dist")
	private Double x5kmProspectAvgDist;
	@JsonProperty("FATG_Network_Location_Type")
	private String fATGNetworkLocationType;
	@JsonProperty("BW_mbps_act")
	private Double bWMbpsAct;
	@JsonProperty("city_tier")
	private String cityTier;
	@JsonProperty("X2km_prospect_min_dist")
	private Double x2kmProspectMinDist;
	@JsonProperty("X2km_prospect_perc_feasible")
	private Double x2kmProspectPercFeasible;
	@JsonProperty("X0.5km_prospect_num_feasible")
	private Double x05kmProspectNumFeasible;
	@JsonProperty("Orch_Connection")
	private String orchConnection;
	@JsonProperty("BW_mbps")
	private Double bWMbps;
	@JsonProperty("resp_city")
	private String respCity;
	@JsonProperty("Burstable_BW")
	private Double burstableBW;
	@JsonProperty("connected_cust_tag")
	private Double connectedCustTag;
	@JsonProperty("net_pre_feasible_flag")
	private Double netPreFeasibleFlag;
	@JsonProperty("Network_F_NF_CC_Flag")
	private String networkFNFCCFlag;
	@JsonProperty("Orch_LM_Type")
	private String orchLMType;
	@JsonProperty("X5km_prospect_avg_bw")
	private Double x5kmProspectAvgBw;
	@JsonProperty("FATG_Category")
	private String fATGCategory;
	@JsonProperty("Feasibility.Response..Created.Date")
	private Double feasibilityResponseCreatedDate;
	@JsonProperty("X2km_prospect_min_bw")
	private Double x2kmProspectMinBw;
	@JsonProperty("X5km_prospect_num_feasible")
	private Double x5kmProspectNumFeasible;
	@JsonProperty("Selected")
	private Boolean selected;
	@JsonProperty("resp_state")
	private String respState;
	@JsonProperty("X5km_prospect_count")
	private Double x5kmProspectCount;
	@JsonProperty("X0.5km_avg_bw")
	private Double x05kmAvgBw;
	@JsonProperty("X2km_cust_count")
	private Double x2kmCustCount;
	@JsonProperty("Customer_Segment")
	private String customerSegment;
	@JsonProperty("FATG_PROW")
	private String fATGPROW;
	@JsonProperty("connected_building_tag")
	private Double connectedBuildingTag;
	@JsonProperty("FATG_DIST_KM")
	private Double fATGDISTKM;
	@JsonProperty("X0.5km_prospect_max_bw")
	private Double x05kmProspectMaxBw;
	@JsonProperty("X5km_prospect_min_dist")
	private Double x5kmProspectMinDist;
	@JsonProperty("FATG_Ring_type")
	private String fATGRingType;
	@JsonProperty("Orch_BW")
	private String orchBW;
	@JsonProperty("scenario_1")
	private Double scenario1;
	@JsonProperty("scenario_2")
	private Double scenario2;
	@JsonProperty("sno")
	private Integer sno;
	@JsonProperty("access_check_hh")
	private String accessCheckHh;
	@JsonProperty("X2km_prospect_count")
	private Double x2kmProspectCount;
	@JsonProperty("min_hh_fatg")
	private Double minHhFatg;
	@JsonProperty("POP_Construction_Status")
	private String pOPConstructionStatus;
	@JsonProperty("X5km_avg_dist")
	private Double x5kmAvgDist;
	@JsonProperty("X2km_avg_dist")
	private Double x2kmAvgDist;
	@JsonProperty("X2km_prospect_avg_bw")
	private Double x2kmProspectAvgBw;
	@JsonProperty("X0.5km_prospect_min_dist")
	private Double x05kmProspectMinDist;
	@JsonProperty("cost_permeter")
	private Double costPermeter;
	@JsonProperty("mux_cost")
	private Double muxCost;
	@JsonProperty("Site_id")
	private String siteId;
	@JsonProperty("prospect_name")
	private String prospectName;
	@JsonProperty("Predicted_Access_Feasibility")
	private String predictedAccessFeasibility;
	@JsonProperty("bw_otc_cost")
	private Double bwOtcCost;
	@JsonProperty("X2km_max_bw")
	private Double x2kmMaxBw;
	@JsonProperty("X2km_prospect_max_bw")
	private Double x2kmProspectMaxBw;
	@JsonProperty("last_mile_contract_term")
	private String lastMileContractTerm;
	@JsonProperty("X5km_prospect_min_bw")
	private Double x5kmProspectMinBw;
	@JsonProperty("ne_rental_cost")
	private Double neRentalCost;
	@JsonProperty("X2km_min_bw")
	private Double x2kmMinBw;
	@JsonProperty("seq_no")
	private Integer seqNo;
	@JsonProperty("osp_capex_cost")
	private Double ospCapexCost;
	@JsonProperty("Sales.Org")
	private String salesOrg;
	@JsonProperty("X0.5km_cust_count")
	private Double x05kmCustCount;
	@JsonProperty("X0.5km_min_bw")
	private Double x05kmMinBw;
	@JsonProperty("Network_F_NF_HH")
	private String networkFNFHH;
	@JsonProperty("HH_0_5_access_rings_NF")
	private String hH05AccessRingsNF;
	@JsonProperty("X0.5km_min_dist")
	private Double x05kmMinDist;
	@JsonProperty("POP_Building_Type")
	private String pOPBuildingType;
	@JsonProperty("core_check_hh")
	private String coreCheckHh;
	@JsonProperty("HH_DIST_KM")
	private Double hHDISTKM;
	@JsonProperty("X0.5km_prospect_min_bw")
	private Double x05kmProspectMinBw;
	@JsonProperty("POP_DIST_KM_SERVICE")
	private Double pOPDISTKMSERVICE;
	@JsonProperty("num_connected_building")
	private Double numConnectedBuilding;
	@JsonProperty("Orch_Category")
	private String orchCategory;
	@JsonProperty("X0.5km_prospect_avg_dist")
	private Double x05kmProspectAvgDist;
	@JsonProperty("POP_Network_Location_Type")
	private String pOPNetworkLocationType;
	@JsonProperty("num_connected_cust")
	private Double numConnectedCust;
	@JsonProperty("HH_0_5km")
	private String hH05km;
	@JsonProperty("X5km_min_dist")
	private Double x5kmMinDist;
	@JsonProperty("X2km_prospect_num_feasible")
	private Double x2kmProspectNumFeasible;
	@JsonProperty("X2km_avg_bw")
	private Double x2kmAvgBw;
	@JsonProperty("X0.5km_max_bw")
	private Double x05kmMaxBw;
	@JsonProperty("X5km_avg_bw")
	private Double x5kmAvgBw;
	@JsonProperty("X5km_prospect_max_bw")
	private Double x5kmProspectMaxBw;
	@JsonProperty("X2km_prospect_avg_dist")
	private Double x2kmProspectAvgDist;
	@JsonProperty("Longitude_final")
	private Double longitudeFinal;
	@JsonProperty("X5km_cust_count")
	private Double x5kmCustCount;
	@JsonProperty("Product.Name")
	private String productName;
	@JsonProperty("X0.5km_prospect_perc_feasible")
	private Double x05kmProspectPercFeasible;
	@JsonProperty("POP_TCL_Access")
	private String pOPTCLAccess;
	@JsonProperty("X0.5km_prospect_count")
	private Double x05kmProspectCount;
	@JsonProperty("in_building_capex_cost")
	private Double inBuildingCapexCost;
	@JsonProperty("Probabililty_Access_Feasibility")
	private Double probabililtyAccessFeasibility;
	@JsonProperty("X2km_min_dist")
	private Double x2kmMinDist;
	@JsonProperty("FATG_TCL_Access")
	private String fATGTCLAccess;
	@JsonProperty("Network_F_NF_HH_Flag")
	private String networkFNFHHFlag;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("BW_mbps_2")
	public Double getBWMbps2() {
		return bWMbps2;
	}

	@JsonProperty("BW_mbps_2")
	public void setBWMbps2(Double bWMbps2) {
		this.bWMbps2 = bWMbps2;
	}

	@JsonProperty("Service_ID")
	public Double getServiceID() {
		return serviceID;
	}

	@JsonProperty("Service_ID")
	public void setServiceID(Double serviceID) {
		this.serviceID = serviceID;
	}

	@JsonProperty("bw_arc_cost")
	public Double getBwArcCost() {
		return bwArcCost;
	}

	@JsonProperty("bw_arc_cost")
	public void setBwArcCost(Double bwArcCost) {
		this.bwArcCost = bwArcCost;
	}

	@JsonProperty("total_cost")
	public Double getTotalCost() {
		return totalCost;
	}

	@JsonProperty("total_cost")
	public void setTotalCost(Double totalCost) {
		this.totalCost = totalCost;
	}

	@JsonProperty("X0.5km_avg_dist")
	public Double getX05kmAvgDist() {
		return x05kmAvgDist;
	}

	@JsonProperty("X0.5km_avg_dist")
	public void setX05kmAvgDist(Double x05kmAvgDist) {
		this.x05kmAvgDist = x05kmAvgDist;
	}

	@JsonProperty("POP_DIST_KM")
	public Double getPOPDISTKM() {
		return pOPDISTKM;
	}

	@JsonProperty("POP_DIST_KM")
	public void setPOPDISTKM(Double pOPDISTKM) {
		this.pOPDISTKM = pOPDISTKM;
	}

	@JsonProperty("X0.5km_prospect_avg_bw")
	public Double getX05kmProspectAvgBw() {
		return x05kmProspectAvgBw;
	}

	@JsonProperty("X0.5km_prospect_avg_bw")
	public void setX05kmProspectAvgBw(Double x05kmProspectAvgBw) {
		this.x05kmProspectAvgBw = x05kmProspectAvgBw;
	}

	@JsonProperty("Latitude_final")
	public Double getLatitudeFinal() {
		return latitudeFinal;
	}

	@JsonProperty("Latitude_final")
	public void setLatitudeFinal(Double latitudeFinal) {
		this.latitudeFinal = latitudeFinal;
	}

	@JsonProperty("OnnetCity_tag")
	public Double getOnnetCityTag() {
		return onnetCityTag;
	}

	@JsonProperty("OnnetCity_tag")
	public void setOnnetCityTag(Double onnetCityTag) {
		this.onnetCityTag = onnetCityTag;
	}

	@JsonProperty("Network_F_NF_CC")
	public String getNetworkFNFCC() {
		return networkFNFCC;
	}

	@JsonProperty("Network_F_NF_CC")
	public void setNetworkFNFCC(String networkFNFCC) {
		this.networkFNFCC = networkFNFCC;
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
	public Double getX5kmMinBw() {
		return x5kmMinBw;
	}

	@JsonProperty("X5km_min_bw")
	public void setX5kmMinBw(Double x5kmMinBw) {
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
	public Double getPOPDISTKMSERVICEMOD() {
		return pOPDISTKMSERVICEMOD;
	}

	@JsonProperty("POP_DIST_KM_SERVICE_MOD")
	public void setPOPDISTKMSERVICEMOD(Double pOPDISTKMSERVICEMOD) {
		this.pOPDISTKMSERVICEMOD = pOPDISTKMSERVICEMOD;
	}

	@JsonProperty("X5km_max_bw")
	public Double getX5kmMaxBw() {
		return x5kmMaxBw;
	}

	@JsonProperty("X5km_max_bw")
	public void setX5kmMaxBw(Double x5kmMaxBw) {
		this.x5kmMaxBw = x5kmMaxBw;
	}

	@JsonProperty("X5km_prospect_perc_feasible")
	public Double getX5kmProspectPercFeasible() {
		return x5kmProspectPercFeasible;
	}

	@JsonProperty("X5km_prospect_perc_feasible")
	public void setX5kmProspectPercFeasible(Double x5kmProspectPercFeasible) {
		this.x5kmProspectPercFeasible = x5kmProspectPercFeasible;
	}

	@JsonProperty("POP_Category")
	public String getPOPCategory() {
		return pOPCategory;
	}

	@JsonProperty("POP_Category")
	public void setPOPCategory(String pOPCategory) {
		this.pOPCategory = pOPCategory;
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
	public Double getX5kmProspectAvgDist() {
		return x5kmProspectAvgDist;
	}

	@JsonProperty("X5km_prospect_avg_dist")
	public void setX5kmProspectAvgDist(Double x5kmProspectAvgDist) {
		this.x5kmProspectAvgDist = x5kmProspectAvgDist;
	}

	@JsonProperty("FATG_Network_Location_Type")
	public String getFATGNetworkLocationType() {
		return fATGNetworkLocationType;
	}

	@JsonProperty("FATG_Network_Location_Type")
	public void setFATGNetworkLocationType(String fATGNetworkLocationType) {
		this.fATGNetworkLocationType = fATGNetworkLocationType;
	}

	@JsonProperty("BW_mbps_act")
	public Double getBWMbpsAct() {
		return bWMbpsAct;
	}

	@JsonProperty("BW_mbps_act")
	public void setBWMbpsAct(Double bWMbpsAct) {
		this.bWMbpsAct = bWMbpsAct;
	}

	@JsonProperty("city_tier")
	public String getCityTier() {
		return cityTier;
	}

	@JsonProperty("city_tier")
	public void setCityTier(String cityTier) {
		this.cityTier = cityTier;
	}

	@JsonProperty("X2km_prospect_min_dist")
	public Double getX2kmProspectMinDist() {
		return x2kmProspectMinDist;
	}

	@JsonProperty("X2km_prospect_min_dist")
	public void setX2kmProspectMinDist(Double x2kmProspectMinDist) {
		this.x2kmProspectMinDist = x2kmProspectMinDist;
	}

	@JsonProperty("X2km_prospect_perc_feasible")
	public Double getX2kmProspectPercFeasible() {
		return x2kmProspectPercFeasible;
	}

	@JsonProperty("X2km_prospect_perc_feasible")
	public void setX2kmProspectPercFeasible(Double x2kmProspectPercFeasible) {
		this.x2kmProspectPercFeasible = x2kmProspectPercFeasible;
	}

	@JsonProperty("X0.5km_prospect_num_feasible")
	public Double getX05kmProspectNumFeasible() {
		return x05kmProspectNumFeasible;
	}

	@JsonProperty("X0.5km_prospect_num_feasible")
	public void setX05kmProspectNumFeasible(Double x05kmProspectNumFeasible) {
		this.x05kmProspectNumFeasible = x05kmProspectNumFeasible;
	}

	@JsonProperty("Orch_Connection")
	public String getOrchConnection() {
		return orchConnection;
	}

	@JsonProperty("Orch_Connection")
	public void setOrchConnection(String orchConnection) {
		this.orchConnection = orchConnection;
	}

	@JsonProperty("BW_mbps")
	public Double getBWMbps() {
		return bWMbps;
	}

	@JsonProperty("BW_mbps")
	public void setBWMbps(Double bWMbps) {
		this.bWMbps = bWMbps;
	}

	@JsonProperty("resp_city")
	public String getRespCity() {
		return respCity;
	}

	@JsonProperty("resp_city")
	public void setRespCity(String respCity) {
		this.respCity = respCity;
	}

	@JsonProperty("Burstable_BW")
	public Double getBurstableBW() {
		return burstableBW;
	}

	@JsonProperty("Burstable_BW")
	public void setBurstableBW(Double burstableBW) {
		this.burstableBW = burstableBW;
	}

	@JsonProperty("connected_cust_tag")
	public Double getConnectedCustTag() {
		return connectedCustTag;
	}

	@JsonProperty("connected_cust_tag")
	public void setConnectedCustTag(Double connectedCustTag) {
		this.connectedCustTag = connectedCustTag;
	}

	@JsonProperty("net_pre_feasible_flag")
	public Double getNetPreFeasibleFlag() {
		return netPreFeasibleFlag;
	}

	@JsonProperty("net_pre_feasible_flag")
	public void setNetPreFeasibleFlag(Double netPreFeasibleFlag) {
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

	@JsonProperty("Orch_LM_Type")
	public String getOrchLMType() {
		return orchLMType;
	}

	@JsonProperty("Orch_LM_Type")
	public void setOrchLMType(String orchLMType) {
		this.orchLMType = orchLMType;
	}

	@JsonProperty("X5km_prospect_avg_bw")
	public Double getX5kmProspectAvgBw() {
		return x5kmProspectAvgBw;
	}

	@JsonProperty("X5km_prospect_avg_bw")
	public void setX5kmProspectAvgBw(Double x5kmProspectAvgBw) {
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

	@JsonProperty("Feasibility.Response..Created.Date")
	public Double getFeasibilityResponseCreatedDate() {
		return feasibilityResponseCreatedDate;
	}

	@JsonProperty("Feasibility.Response..Created.Date")
	public void setFeasibilityResponseCreatedDate(Double feasibilityResponseCreatedDate) {
		this.feasibilityResponseCreatedDate = feasibilityResponseCreatedDate;
	}

	@JsonProperty("X2km_prospect_min_bw")
	public Double getX2kmProspectMinBw() {
		return x2kmProspectMinBw;
	}

	@JsonProperty("X2km_prospect_min_bw")
	public void setX2kmProspectMinBw(Double x2kmProspectMinBw) {
		this.x2kmProspectMinBw = x2kmProspectMinBw;
	}

	@JsonProperty("X5km_prospect_num_feasible")
	public Double getX5kmProspectNumFeasible() {
		return x5kmProspectNumFeasible;
	}

	@JsonProperty("X5km_prospect_num_feasible")
	public void setX5kmProspectNumFeasible(Double x5kmProspectNumFeasible) {
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

	@JsonProperty("resp_state")
	public String getRespState() {
		return respState;
	}

	@JsonProperty("resp_state")
	public void setRespState(String respState) {
		this.respState = respState;
	}

	@JsonProperty("X5km_prospect_count")
	public Double getX5kmProspectCount() {
		return x5kmProspectCount;
	}

	@JsonProperty("X5km_prospect_count")
	public void setX5kmProspectCount(Double x5kmProspectCount) {
		this.x5kmProspectCount = x5kmProspectCount;
	}

	@JsonProperty("X0.5km_avg_bw")
	public Double getX05kmAvgBw() {
		return x05kmAvgBw;
	}

	@JsonProperty("X0.5km_avg_bw")
	public void setX05kmAvgBw(Double x05kmAvgBw) {
		this.x05kmAvgBw = x05kmAvgBw;
	}

	@JsonProperty("X2km_cust_count")
	public Double getX2kmCustCount() {
		return x2kmCustCount;
	}

	@JsonProperty("X2km_cust_count")
	public void setX2kmCustCount(Double x2kmCustCount) {
		this.x2kmCustCount = x2kmCustCount;
	}

	@JsonProperty("Customer_Segment")
	public String getCustomerSegment() {
		return customerSegment;
	}

	@JsonProperty("Customer_Segment")
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

	@JsonProperty("connected_building_tag")
	public Double getConnectedBuildingTag() {
		return connectedBuildingTag;
	}

	@JsonProperty("connected_building_tag")
	public void setConnectedBuildingTag(Double connectedBuildingTag) {
		this.connectedBuildingTag = connectedBuildingTag;
	}

	@JsonProperty("FATG_DIST_KM")
	public Double getFATGDISTKM() {
		return fATGDISTKM;
	}

	@JsonProperty("FATG_DIST_KM")
	public void setFATGDISTKM(Double fATGDISTKM) {
		this.fATGDISTKM = fATGDISTKM;
	}

	@JsonProperty("X0.5km_prospect_max_bw")
	public Double getX05kmProspectMaxBw() {
		return x05kmProspectMaxBw;
	}

	@JsonProperty("X0.5km_prospect_max_bw")
	public void setX05kmProspectMaxBw(Double x05kmProspectMaxBw) {
		this.x05kmProspectMaxBw = x05kmProspectMaxBw;
	}

	@JsonProperty("X5km_prospect_min_dist")
	public Double getX5kmProspectMinDist() {
		return x5kmProspectMinDist;
	}

	@JsonProperty("X5km_prospect_min_dist")
	public void setX5kmProspectMinDist(Double x5kmProspectMinDist) {
		this.x5kmProspectMinDist = x5kmProspectMinDist;
	}

	@JsonProperty("FATG_Ring_type")
	public String getFATGRingType() {
		return fATGRingType;
	}

	@JsonProperty("FATG_Ring_type")
	public void setFATGRingType(String fATGRingType) {
		this.fATGRingType = fATGRingType;
	}

	@JsonProperty("Orch_BW")
	public String getOrchBW() {
		return orchBW;
	}

	@JsonProperty("Orch_BW")
	public void setOrchBW(String orchBW) {
		this.orchBW = orchBW;
	}

	@JsonProperty("scenario_1")
	public Double getScenario1() {
		return scenario1;
	}

	@JsonProperty("scenario_1")
	public void setScenario1(Double scenario1) {
		this.scenario1 = scenario1;
	}

	@JsonProperty("scenario_2")
	public Double getScenario2() {
		return scenario2;
	}

	@JsonProperty("scenario_2")
	public void setScenario2(Double scenario2) {
		this.scenario2 = scenario2;
	}

	@JsonProperty("sno")
	public Integer getSno() {
		return sno;
	}

	@JsonProperty("sno")
	public void setSno(Integer sno) {
		this.sno = sno;
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
	public Double getX2kmProspectCount() {
		return x2kmProspectCount;
	}

	@JsonProperty("X2km_prospect_count")
	public void setX2kmProspectCount(Double x2kmProspectCount) {
		this.x2kmProspectCount = x2kmProspectCount;
	}

	@JsonProperty("min_hh_fatg")
	public Double getMinHhFatg() {
		return minHhFatg;
	}

	@JsonProperty("min_hh_fatg")
	public void setMinHhFatg(Double minHhFatg) {
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
	public Double getX5kmAvgDist() {
		return x5kmAvgDist;
	}

	@JsonProperty("X5km_avg_dist")
	public void setX5kmAvgDist(Double x5kmAvgDist) {
		this.x5kmAvgDist = x5kmAvgDist;
	}

	@JsonProperty("X2km_avg_dist")
	public Double getX2kmAvgDist() {
		return x2kmAvgDist;
	}

	@JsonProperty("X2km_avg_dist")
	public void setX2kmAvgDist(Double x2kmAvgDist) {
		this.x2kmAvgDist = x2kmAvgDist;
	}

	@JsonProperty("X2km_prospect_avg_bw")
	public Double getX2kmProspectAvgBw() {
		return x2kmProspectAvgBw;
	}

	@JsonProperty("X2km_prospect_avg_bw")
	public void setX2kmProspectAvgBw(Double x2kmProspectAvgBw) {
		this.x2kmProspectAvgBw = x2kmProspectAvgBw;
	}

	@JsonProperty("X0.5km_prospect_min_dist")
	public Double getX05kmProspectMinDist() {
		return x05kmProspectMinDist;
	}

	@JsonProperty("X0.5km_prospect_min_dist")
	public void setX05kmProspectMinDist(Double x05kmProspectMinDist) {
		this.x05kmProspectMinDist = x05kmProspectMinDist;
	}

	@JsonProperty("cost_permeter")
	public Double getCostPermeter() {
		return costPermeter;
	}

	@JsonProperty("cost_permeter")
	public void setCostPermeter(Double costPermeter) {
		this.costPermeter = costPermeter;
	}

	@JsonProperty("mux_cost")
	public Double getMuxCost() {
		return muxCost;
	}

	@JsonProperty("mux_cost")
	public void setMuxCost(Double muxCost) {
		this.muxCost = muxCost;
	}

	@JsonProperty("Site_id")
	public String getSiteId() {
		return siteId;
	}

	@JsonProperty("Site_id")
	public void setSiteId(String siteId) {
		this.siteId = siteId;
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

	@JsonProperty("bw_otc_cost")
	public Double getBwOtcCost() {
		return bwOtcCost;
	}

	@JsonProperty("bw_otc_cost")
	public void setBwOtcCost(Double bwOtcCost) {
		this.bwOtcCost = bwOtcCost;
	}

	@JsonProperty("X2km_max_bw")
	public Double getX2kmMaxBw() {
		return x2kmMaxBw;
	}

	@JsonProperty("X2km_max_bw")
	public void setX2kmMaxBw(Double x2kmMaxBw) {
		this.x2kmMaxBw = x2kmMaxBw;
	}

	@JsonProperty("X2km_prospect_max_bw")
	public Double getX2kmProspectMaxBw() {
		return x2kmProspectMaxBw;
	}

	@JsonProperty("X2km_prospect_max_bw")
	public void setX2kmProspectMaxBw(Double x2kmProspectMaxBw) {
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
	public Double getX5kmProspectMinBw() {
		return x5kmProspectMinBw;
	}

	@JsonProperty("X5km_prospect_min_bw")
	public void setX5kmProspectMinBw(Double x5kmProspectMinBw) {
		this.x5kmProspectMinBw = x5kmProspectMinBw;
	}

	@JsonProperty("ne_rental_cost")
	public Double getNeRentalCost() {
		return neRentalCost;
	}

	@JsonProperty("ne_rental_cost")
	public void setNeRentalCost(Double neRentalCost) {
		this.neRentalCost = neRentalCost;
	}

	@JsonProperty("X2km_min_bw")
	public Double getX2kmMinBw() {
		return x2kmMinBw;
	}

	@JsonProperty("X2km_min_bw")
	public void setX2kmMinBw(Double x2kmMinBw) {
		this.x2kmMinBw = x2kmMinBw;
	}

	@JsonProperty("seq_no")
	public Integer getSeqNo() {
		return seqNo;
	}

	@JsonProperty("seq_no")
	public void setSeqNo(Integer seqNo) {
		this.seqNo = seqNo;
	}

	@JsonProperty("osp_capex_cost")
	public Double getOspCapexCost() {
		return ospCapexCost;
	}

	@JsonProperty("osp_capex_cost")
	public void setOspCapexCost(Double ospCapexCost) {
		this.ospCapexCost = ospCapexCost;
	}

	@JsonProperty("Sales.Org")
	public String getSalesOrg() {
		return salesOrg;
	}

	@JsonProperty("Sales.Org")
	public void setSalesOrg(String salesOrg) {
		this.salesOrg = salesOrg;
	}

	@JsonProperty("X0.5km_cust_count")
	public Double getX05kmCustCount() {
		return x05kmCustCount;
	}

	@JsonProperty("X0.5km_cust_count")
	public void setX05kmCustCount(Double x05kmCustCount) {
		this.x05kmCustCount = x05kmCustCount;
	}

	@JsonProperty("X0.5km_min_bw")
	public Double getX05kmMinBw() {
		return x05kmMinBw;
	}

	@JsonProperty("X0.5km_min_bw")
	public void setX05kmMinBw(Double x05kmMinBw) {
		this.x05kmMinBw = x05kmMinBw;
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
	public Double getX05kmMinDist() {
		return x05kmMinDist;
	}

	@JsonProperty("X0.5km_min_dist")
	public void setX05kmMinDist(Double x05kmMinDist) {
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
	public Double getHHDISTKM() {
		return hHDISTKM;
	}

	@JsonProperty("HH_DIST_KM")
	public void setHHDISTKM(Double hHDISTKM) {
		this.hHDISTKM = hHDISTKM;
	}

	@JsonProperty("X0.5km_prospect_min_bw")
	public Double getX05kmProspectMinBw() {
		return x05kmProspectMinBw;
	}

	@JsonProperty("X0.5km_prospect_min_bw")
	public void setX05kmProspectMinBw(Double x05kmProspectMinBw) {
		this.x05kmProspectMinBw = x05kmProspectMinBw;
	}

	@JsonProperty("POP_DIST_KM_SERVICE")
	public Double getPOPDISTKMSERVICE() {
		return pOPDISTKMSERVICE;
	}

	@JsonProperty("POP_DIST_KM_SERVICE")
	public void setPOPDISTKMSERVICE(Double pOPDISTKMSERVICE) {
		this.pOPDISTKMSERVICE = pOPDISTKMSERVICE;
	}

	@JsonProperty("num_connected_building")
	public Double getNumConnectedBuilding() {
		return numConnectedBuilding;
	}

	@JsonProperty("num_connected_building")
	public void setNumConnectedBuilding(Double numConnectedBuilding) {
		this.numConnectedBuilding = numConnectedBuilding;
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
	public Double getX05kmProspectAvgDist() {
		return x05kmProspectAvgDist;
	}

	@JsonProperty("X0.5km_prospect_avg_dist")
	public void setX05kmProspectAvgDist(Double x05kmProspectAvgDist) {
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

	@JsonProperty("num_connected_cust")
	public Double getNumConnectedCust() {
		return numConnectedCust;
	}

	@JsonProperty("num_connected_cust")
	public void setNumConnectedCust(Double numConnectedCust) {
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

	@JsonProperty("X5km_min_dist")
	public Double getX5kmMinDist() {
		return x5kmMinDist;
	}

	@JsonProperty("X5km_min_dist")
	public void setX5kmMinDist(Double x5kmMinDist) {
		this.x5kmMinDist = x5kmMinDist;
	}

	@JsonProperty("X2km_prospect_num_feasible")
	public Double getX2kmProspectNumFeasible() {
		return x2kmProspectNumFeasible;
	}

	@JsonProperty("X2km_prospect_num_feasible")
	public void setX2kmProspectNumFeasible(Double x2kmProspectNumFeasible) {
		this.x2kmProspectNumFeasible = x2kmProspectNumFeasible;
	}

	@JsonProperty("X2km_avg_bw")
	public Double getX2kmAvgBw() {
		return x2kmAvgBw;
	}

	@JsonProperty("X2km_avg_bw")
	public void setX2kmAvgBw(Double x2kmAvgBw) {
		this.x2kmAvgBw = x2kmAvgBw;
	}

	@JsonProperty("X0.5km_max_bw")
	public Double getX05kmMaxBw() {
		return x05kmMaxBw;
	}

	@JsonProperty("X0.5km_max_bw")
	public void setX05kmMaxBw(Double x05kmMaxBw) {
		this.x05kmMaxBw = x05kmMaxBw;
	}

	@JsonProperty("X5km_avg_bw")
	public Double getX5kmAvgBw() {
		return x5kmAvgBw;
	}

	@JsonProperty("X5km_avg_bw")
	public void setX5kmAvgBw(Double x5kmAvgBw) {
		this.x5kmAvgBw = x5kmAvgBw;
	}

	@JsonProperty("X5km_prospect_max_bw")
	public Double getX5kmProspectMaxBw() {
		return x5kmProspectMaxBw;
	}

	@JsonProperty("X5km_prospect_max_bw")
	public void setX5kmProspectMaxBw(Double x5kmProspectMaxBw) {
		this.x5kmProspectMaxBw = x5kmProspectMaxBw;
	}

	@JsonProperty("X2km_prospect_avg_dist")
	public Double getX2kmProspectAvgDist() {
		return x2kmProspectAvgDist;
	}

	@JsonProperty("X2km_prospect_avg_dist")
	public void setX2kmProspectAvgDist(Double x2kmProspectAvgDist) {
		this.x2kmProspectAvgDist = x2kmProspectAvgDist;
	}

	@JsonProperty("Longitude_final")
	public Double getLongitudeFinal() {
		return longitudeFinal;
	}

	@JsonProperty("Longitude_final")
	public void setLongitudeFinal(Double longitudeFinal) {
		this.longitudeFinal = longitudeFinal;
	}

	@JsonProperty("X5km_cust_count")
	public Double getX5kmCustCount() {
		return x5kmCustCount;
	}

	@JsonProperty("X5km_cust_count")
	public void setX5kmCustCount(Double x5kmCustCount) {
		this.x5kmCustCount = x5kmCustCount;
	}

	@JsonProperty("Product.Name")
	public String getProductName() {
		return productName;
	}

	@JsonProperty("Product.Name")
	public void setProductName(String productName) {
		this.productName = productName;
	}

	@JsonProperty("X0.5km_prospect_perc_feasible")
	public Double getX05kmProspectPercFeasible() {
		return x05kmProspectPercFeasible;
	}

	@JsonProperty("X0.5km_prospect_perc_feasible")
	public void setX05kmProspectPercFeasible(Double x05kmProspectPercFeasible) {
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
	public Double getX05kmProspectCount() {
		return x05kmProspectCount;
	}

	@JsonProperty("X0.5km_prospect_count")
	public void setX05kmProspectCount(Double x05kmProspectCount) {
		this.x05kmProspectCount = x05kmProspectCount;
	}

	@JsonProperty("in_building_capex_cost")
	public Double getInBuildingCapexCost() {
		return inBuildingCapexCost;
	}

	@JsonProperty("in_building_capex_cost")
	public void setInBuildingCapexCost(Double inBuildingCapexCost) {
		this.inBuildingCapexCost = inBuildingCapexCost;
	}

	@JsonProperty("Probabililty_Access_Feasibility")
	public Double getProbabililtyAccessFeasibility() {
		return probabililtyAccessFeasibility;
	}

	@JsonProperty("Probabililty_Access_Feasibility")
	public void setProbabililtyAccessFeasibility(Double probabililtyAccessFeasibility) {
		this.probabililtyAccessFeasibility = probabililtyAccessFeasibility;
	}

	@JsonProperty("X2km_min_dist")
	public Double getX2kmMinDist() {
		return x2kmMinDist;
	}

	@JsonProperty("X2km_min_dist")
	public void setX2kmMinDist(Double x2kmMinDist) {
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

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

}

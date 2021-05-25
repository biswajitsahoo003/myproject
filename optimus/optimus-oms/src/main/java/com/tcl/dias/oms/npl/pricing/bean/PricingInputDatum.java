
package com.tcl.dias.oms.npl.pricing.bean;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "link_id",
    "site_id",
    "prospect_name",
    "bw_mbps",
    "product_name",
    "feasibility_response_created_date",
    "account_id_with_18_digit",
    "opportunity_term",
    "quotetype_quote",
    "sla_varient",
    "chargeable_distance",
    "dist_betw_pops",
    "intra_inter_flag",
    "a_latitude_final",
    "a_longitude_final",
    "a_resp_city",
    "a_local_loop_interface",
    "a_lm_arc_bw_onwl",
    "a_lm_nrc_bw_onwl",
    "a_lm_nrc_mux_onwl",
    "a_lm_nrc_inbldg_onwl",
    "a_lm_nrc_ospcapex_onwl",
    "a_lm_nrc_nerental_onwl",
    "b_latitude_final",
    "b_longitude_final",
    "b_resp_city",
    "b_local_loop_interface",
    "b_lm_arc_bw_onwl",
    "b_lm_nrc_bw_onwl",
    "b_lm_nrc_mux_onwl",
    "b_lm_nrc_inbldg_onwl",
    "b_lm_nrc_ospcapex_onwl",
    "b_lm_nrc_nerental_onwl",
    "a_POP_DIST_KM_SERVICE_MOD",
    "b_POP_DIST_KM_SERVICE_MOD", 
    "a_Orch_Category",
    "b_Orch_Category",
    
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
	
	"a_lm_arc_bw_offwl",
    "a_lm_arc_bw_onrf",
    "a_lm_arc_colocation_charges_onrf",
    "a_lm_arc_converter_charges_onrf",
    "a_lm_arc_modem_charges_offwl",
    "a_lm_arc_offwl",
    "a_lm_arc_orderable_bw_onwl",
    "a_lm_arc_prow_onwl",
    "a_lm_arc_radwin_bw_onrf",
    "a_lm_nrc_bw_onrf",
    "a_lm_nrc_bw_prov_ofrf",
    "a_lm_nrc_mast_ofrf",
    "a_lm_nrc_mast_onrf",
    "a_lm_nrc_network_capex_onwl",
    "a_lm_nrc_prow_onwl",
    "a_lm_otc_modem_charges_offwl",
    "a_lm_otc_nrc_installation_offwl",
    "a_lm_otc_nrc_orderable_bw_onwl",
    "a_lm_arc_colocation_onrf",
    "a_lm_arc_bw_prov_ofrf",
    "a_lm_arc_bw_backhaul_onrf",
    
    "b_lm_arc_bw_offwl",
    "b_lm_arc_bw_onrf",
    "b_lm_arc_colocation_charges_onrf",
    "b_lm_arc_converter_charges_onrf",
    "b_lm_arc_modem_charges_offwl",
    "b_lm_arc_offwl",
    "b_lm_arc_orderable_bw_onwl",
    "b_lm_arc_prow_onwl",
    "b_lm_arc_radwin_bw_onrf",
    "b_lm_nrc_bw_onrf",
    "b_lm_nrc_bw_prov_ofrf",
    "b_lm_nrc_mast_ofrf",
    "b_lm_nrc_mast_onrf",
    "b_lm_nrc_network_capex_onwl",
    "b_lm_nrc_prow_onwl",
    "b_lm_otc_modem_charges_offwl",
    "b_lm_otc_nrc_installation_offwl",
    "b_lm_otc_nrc_orderable_bw_onwl",
    "b_lm_arc_colocation_onrf",
    "b_lm_arc_bw_prov_ofrf",
    "b_lm_arc_bw_backhaul_onrf",
    
    "old_contract_term",
    "service_id",
    "cu_le_id",
    "old_Port_Bw",
    "backup_port_requested",
    "a_Orch_LM_Type",
    "b_Orch_LM_Type",
    
    "a_provider",
    "b_provider",
    "a_BHConnectivity",
    "b_BHConnectivity",
    "a_parallel_run_days",
	"b_parallel_run_days",
    "isHub",
    "a_local_loop_bw",
	"b_local_loop_bw",

	"quotetype_partner",
	"partner_profile",
	"deal_reg_flag",
	"b_local_loop_bw",

    "isHub",
	"partner_account_id_with_18_digit",
	"partner_profile",
	"quotetype_partner",
	"solution_type",
	"deal_reg_flag"
})
public class PricingInputDatum {

    @JsonProperty("link_id")
    private String linkId;
    @JsonProperty("site_id")
    private String siteId;
    @JsonProperty("prospect_name")
    private String prospectName;
    @JsonProperty("bw_mbps")
    private String bwMbps;
    @JsonProperty("product_name")
    private String productName;
    @JsonProperty("feasibility_response_created_date")
    private String feasibilityResponseCreatedDate;
    @JsonProperty("account_id_with_18_digit")
    private String accountIdWith18Digit;
    @JsonProperty("opportunity_term")
    private String opportunityTerm;
    @JsonProperty("quotetype_quote")
    private String quotetypeQuote;
    @JsonProperty("sla_varient")
    private String slaVarient;
    @JsonProperty("chargeable_distance")
    private String chargeableDistance;
    @JsonProperty("dist_betw_pops")
    private String distBetwPops;
    @JsonProperty("intra_inter_flag")
    private String intraInterFlag;
    @JsonProperty("a_latitude_final")
    private String aLatitudeFinal;
    @JsonProperty("a_longitude_final")
    private String aLongitudeFinal;
    @JsonProperty("a_resp_city")
    private String aRespCity;
    @JsonProperty("a_local_loop_interface")
    private String aLocalLoopInterface;
    @JsonProperty("a_lm_arc_bw_onwl")
    private String aLmArcBwOnwl;
    @JsonProperty("a_lm_nrc_bw_onwl")
    private String aLmNrcBwOnwl;
    @JsonProperty("a_lm_nrc_mux_onwl")
    private String aLmNrcMuxOnwl;
    @JsonProperty("a_lm_nrc_inbldg_onwl")
    private String aLmNrcInbldgOnwl;
    @JsonProperty("a_lm_nrc_ospcapex_onwl")
    private String aLmNrcOspcapexOnwl;
    @JsonProperty("a_lm_nrc_nerental_onwl")
    private String aLmNrcNerentalOnwl;
    @JsonProperty("b_latitude_final")
    private String bLatitudeFinal;
    @JsonProperty("b_longitude_final")
    private String bLongitudeFinal;
    @JsonProperty("b_resp_city")
    private String bRespCity;
    @JsonProperty("b_local_loop_interface")
    private String bLocalLoopInterface;
    @JsonProperty("b_lm_arc_bw_onwl")
    private String bLmArcBwOnwl;
    @JsonProperty("b_lm_nrc_bw_onwl")
    private String bLmNrcBwOnwl;
    @JsonProperty("b_lm_nrc_mux_onwl")
    private String bLmNrcMuxOnwl;
    @JsonProperty("b_lm_nrc_inbldg_onwl")
    private String bLmNrcInbldgOnwl;
    @JsonProperty("b_lm_nrc_ospcapex_onwl")
    private String bLmNrcOspcapexOnwl;
    @JsonProperty("b_lm_nrc_nerental_onwl")
    private String bLmNrcNerentalOnwl;
    @JsonProperty("a_POP_DIST_KM_SERVICE_MOD")
    private String aPopDistKmServiceMod;
    @JsonProperty("b_POP_DIST_KM_SERVICE_MOD")
    private String bPopDistKmServiceMod;
    @JsonProperty("a_Orch_Category")
    private String aOrchCategory;
    @JsonProperty("b_Orch_Category")
    private String bOrchCategory;
    
    //MACD aatributes
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
	
	@JsonProperty("a_lm_arc_bw_offwl")
	private String aLmArcBwOffwl;
	@JsonProperty("a_lm_arc_bw_onrf")
	private String aLmArcBwOnrf;
	@JsonProperty("a_lm_arc_colocation_charges_onrf")
	private String aLmArcColocationChargesOnrf;
	@JsonProperty("a_lm_arc_converter_charges_onrf")
	private String aLmArcConverterChargesOnrf;
	@JsonProperty("a_lm_arc_modem_charges_offwl")
	private String aLmArcModemChargesOffwl;
	@JsonProperty("a_lm_arc_offwl")
	private String aLmArcOffwl;
	@JsonProperty("a_lm_arc_orderable_bw_onwl")
	private String aLmArcOrderableBwOnwl;
	@JsonProperty("a_lm_arc_prow_onwl")
	private String aLmArcProwOnwl;
	@JsonProperty("a_lm_arc_radwin_bw_onrf")
	private String aLmArcRadwinBwOnrf;
	@JsonProperty("a_lm_nrc_bw_onrf")
	private String aLmNrcBwOnrf;
	@JsonProperty("a_lm_nrc_bw_prov_ofrf")
	private String aLmNrcBwProvOfrf;
	@JsonProperty("a_lm_nrc_mast_ofrf")
	private String aLmNrcMast_ofrf;
	@JsonProperty("a_lm_nrc_mast_onrf")
	private String aLmNrcMastOnrf;
	@JsonProperty("a_lm_nrc_network_capex_onwl")
	private String aLmNrcNetworkCapexOnwl;
	@JsonProperty("a_lm_nrc_prow_onwl")
	private String aLmNrcProwOnwl;
	@JsonProperty("a_lm_otc_modem_charges_offwl")
	private String aLmOtcModemChargesOffwl;
	@JsonProperty("a_lm_otc_nrc_installation_offwl")
	private String aLmotcNrcInstallationOffwl;
	@JsonProperty("a_lm_otc_nrc_orderable_bw_onwl")
	private String aLmOtcNrcOrderablBwOnwl;
	
	
	@JsonProperty("b_lm_arc_bw_offwl")
	private String bLmArcBwOffwl;
	@JsonProperty("b_lm_arc_bw_onrf")
	private String bLmArcBwOnrf;
	@JsonProperty("b_lm_arc_colocation_charges_onrf")
	private String bLmArcColocationChargesOnrf;
	@JsonProperty("b_lm_arc_converter_charges_onrf")
	private String bLmArcConverterChargesOnrf;
	@JsonProperty("b_lm_arc_modem_charges_offwl")
	private String bLmArcModemChargesOffwl;
	@JsonProperty("b_lm_arc_offwl")
	private String bLmArcOffwl;
	@JsonProperty("b_lm_arc_orderable_bw_onwl")
	private String bLmArcOrderableBwOnwl;
	@JsonProperty("b_lm_arc_prow_onwl")
	private String bLmArcProwOnwl;
	@JsonProperty("b_lm_arc_radwin_bw_onrf")
	private String bLmArcRadwinBwOnrf;
	@JsonProperty("b_lm_nrc_bw_onrf")
	private String bLmNrcBwOnrf;
	@JsonProperty("b_lm_nrc_bw_prov_ofrf")
	private String bLmNrcBwProvOfrf;
	@JsonProperty("b_lm_nrc_mast_ofrf")
	private String bLmNrcMast_ofrf;
	@JsonProperty("b_lm_nrc_mast_onrf")
	private String bLmNrcMastOnrf;
	@JsonProperty("b_lm_nrc_network_capex_onwl")
	private String bLmNrcNetworkCapexOnwl;
	@JsonProperty("b_lm_nrc_prow_onwl")
	private String bLmNrcProwOnwl;
	@JsonProperty("b_lm_otc_modem_charges_offwl")
	private String bLmOtcModemChargesOffwl;
	@JsonProperty("b_lm_otc_nrc_installation_offwl")
	private String bLmotcNrcInstallationOffwl;
	@JsonProperty("b_lm_otc_nrc_orderable_bw_onwl")
	private String bLmOtcNrcOrderablBwOnwl;

	@JsonProperty("a_Orch_Connection")
	private String aOrchConnection;

	@JsonProperty("b_Orch_Connection")
	private String bOrchConnection;

	@JsonProperty("old_contract_term")
	private String oldContractTerm;
	
	@JsonProperty("service_id")
	private String serviceId;
	
	@JsonProperty("cu_le_id")
	private String cuLeId;
	
	@JsonProperty("old_Port_Bw")
	private String oldPortBw;
	
	@JsonProperty("backup_port_requested")
	private String backupPortRequested;
	
	
	@JsonProperty("a_Orch_LM_Type")
	private String aOrchLMType;
	
	@JsonProperty("b_Orch_LM_Type")
	private String bOrchLMType;
	
	
	@JsonProperty("a_lm_arc_colocation_onrf")
	private String aLmarcColocationOnrf;
	
	@JsonProperty("b_lm_arc_colocation_onrf")
	private String bLmarcColocationOnrf;
	
	
	@JsonProperty("a_provider")
	private String aProvider;
	
	@JsonProperty("b_provider")
	private String bProvider;
	
	
	@JsonProperty("a_BHConnectivity")
	private String aBHConnectivity;
	
	@JsonProperty("b_BHConnectivity")
	private String bBHConnectivity;
	
	
	@JsonProperty("a_lm_arc_bw_prov_ofrf")
	private String aLmArcBwProvOfrf;
	
	@JsonProperty("a_lm_arc_bw_backhaul_onrf")
	private String aLmArcBwBackhaulOnrf;
	
	@JsonProperty("b_lm_arc_bw_prov_ofrf")
	private String bLmArcBwProvOfrf;
	
	@JsonProperty("b_lm_arc_bw_backhaul_onrf")
	private String bLmArcBwBackhaulOnrf;
	
	
	
	
	@JsonProperty("isHub")
	private Integer isHub;
	
	@JsonProperty("a_parallel_run_days")
	private String aParallelRunDays;
	
	@JsonProperty("b_parallel_run_days")
	private String bParallelRunDays;

	@JsonProperty("partner_account_id_with_18_digit")
	private String partnerAccountIdWith18Digit;

	@JsonProperty("partner_profile")
	private String partnerProfile;

	@JsonProperty("quotetype_partner")
	private String quoteTypePartner;

	@JsonProperty("solution_type")
	private String solutionType;

	@JsonProperty("deal_reg_flag")
	private String dealRegFlag;

	@JsonProperty("a_local_loop_bw")
	private String aLocalLoopBw;
	
	@JsonProperty("b_local_loop_bw")
	private String bLocalLoopBw;
	
	@JsonProperty("a_local_loop_bw")
	public String getaLocalLoopBw() {
		return aLocalLoopBw;
	}
	
	@JsonProperty("a_local_loop_bw")
	public void setaLocalLoopBw(String newBW) {
		this.aLocalLoopBw = newBW;
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
	
	@JsonProperty("isHub")
    public Integer getIsHub() {
		return isHub;
	}
	@JsonProperty("isHub")
	public void setIsHub(Integer isHub) {
		this.isHub = isHub;
	} 
	
	
	
	
	
	@JsonProperty("a_lm_arc_bw_backhaul_onrf")
	public String getaLmArcBwBackhaulOnrf() {
		return aLmArcBwBackhaulOnrf;
	}
	
	@JsonProperty("a_lm_arc_bw_backhaul_onrf")
	public void setaLmArcBwBackhaulOnrf(String aLmArcBwBackhaulOnrf) {
		this.aLmArcBwBackhaulOnrf = aLmArcBwBackhaulOnrf;
	}

	
	@JsonProperty("b_lm_arc_bw_prov_ofrf")
	public String getbLmArcBwProvOfrf() {
		return bLmArcBwProvOfrf;
	}
	
	@JsonProperty("b_lm_arc_bw_prov_ofrf")
	public void setbLmArcBwProvOfrf(String bLmArcBwProvOfrf) {
		this.bLmArcBwProvOfrf = bLmArcBwProvOfrf;
	}

	@JsonProperty("b_lm_arc_bw_backhaul_onrf")
	public String getbLmArcBwBackhaulOnrf() {
		return bLmArcBwBackhaulOnrf;
	}

	@JsonProperty("b_lm_arc_bw_backhaul_onrf")
	public void setbLmArcBwBackhaulOnrf(String bLmArcBwBackhaulOnrf) {
		this.bLmArcBwBackhaulOnrf = bLmArcBwBackhaulOnrf;
	}

	@JsonProperty("a_lm_arc_bw_prov_ofrf")
	public String getaLmArcBwProvOfrf() {
		return aLmArcBwProvOfrf;
	}

	@JsonProperty("a_lm_arc_bw_prov_ofrf")
	public void setaLmArcBwProvOfrf(String aLmArcBwProvOfrf) {
		this.aLmArcBwProvOfrf = aLmArcBwProvOfrf;
	}

	@JsonProperty("a_BHConnectivity")
	public String getaBHConnectivity() {
		return aBHConnectivity;
	}
	
	@JsonProperty("a_BHConnectivity")
	public void setaBHConnectivity(String aBHConnectivity) {
		this.aBHConnectivity = aBHConnectivity;
	}
	
	@JsonProperty("b_BHConnectivity")
	public String getbBHConnectivity() {
		return bBHConnectivity;
	}
	
	@JsonProperty("b_BHConnectivity")
	public void setbBHConnectivity(String bBHConnectivity) {
		this.bBHConnectivity = bBHConnectivity;
	}
	
	@JsonProperty("a_provider")
	public String getaProvider() {
		return aProvider;
	}
	
	@JsonProperty("a_provider")
	public void setaProvider(String aProvider) {
		this.aProvider = aProvider;
	}
	
	@JsonProperty("b_provider")
	public String getbProvider() {
		return bProvider;
	}
	
	@JsonProperty("b_provider")
	public void setbProvider(String bProvider) {
		this.bProvider = bProvider;
	}
	
	
	@JsonProperty("a_lm_arc_colocation_onrf")
	public String getaLmarcColocationOnrf() {
		return aLmarcColocationOnrf;
	}

	@JsonProperty("a_lm_arc_colocation_onrf")
	public void setaLmarcColocationOnrf(String aLmarcColocationOnrf) {
		this.aLmarcColocationOnrf = aLmarcColocationOnrf;
	}

	@JsonProperty("b_lm_arc_colocation_onrf")
	public String getbLmarcColocationOnrf() {
		return bLmarcColocationOnrf;
	}

	@JsonProperty("b_lm_arc_colocation_onrf")
	public void setbLmarcColocationOnrf(String bLmarcColocationOnrf) {
		this.bLmarcColocationOnrf = bLmarcColocationOnrf;
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
	@JsonProperty("service_id")
	public String getServiceId() {
		return serviceId;
	}
	@JsonProperty("service_id")
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	
	@JsonProperty("cu_le_id")
	public String getCuLeId() {
		return cuLeId;
	}
	@JsonProperty("cu_le_id")
	public void setCuLeId(String cuLeId) {
		this.cuLeId = cuLeId;
	}
	
	@JsonProperty("old_Port_Bw")
	public String getOldPortBw() {
		return oldPortBw;
	}
	@JsonProperty("old_Port_Bw")
	public void setOldPortBw(String oldPortBw) {
		this.oldPortBw = oldPortBw;
	}
	
	@JsonProperty("backup_port_requested")
	public String getBackupPortRequested() {
		return backupPortRequested;
	}
	@JsonProperty("backup_port_requested")
	public void setBackupPortRequested(String backupPortRequested) {
		this.backupPortRequested = backupPortRequested;
	}
	
	@JsonProperty("b_lm_arc_bw_offwl")
	public String getbLmArcBwOffwl() {
		return bLmArcBwOffwl;
	}
	@JsonProperty("b_lm_arc_bw_offwl")
	public void setbLmArcBwOffwl(String bLmArcBwOffwl) {
		this.bLmArcBwOffwl = bLmArcBwOffwl;
	}
	@JsonProperty("b_lm_arc_bw_onrf")
	public String getbLmArcBwOnrf() {
		return bLmArcBwOnrf;
	}
	@JsonProperty("b_lm_arc_bw_onrf")
	public void setbLmArcBwOnrf(String bLmArcBwOnrf) {
		this.bLmArcBwOnrf = bLmArcBwOnrf;
	}
	@JsonProperty("b_lm_arc_colocation_charges_onrf")
	public String getbLmArcColocationChargesOnrf() {
		return bLmArcColocationChargesOnrf;
	}
	@JsonProperty("b_lm_arc_colocation_charges_onrf")
	public void setbLmArcColocationChargesOnrf(String bLmArcColocationChargesOnrf) {
		this.bLmArcColocationChargesOnrf = bLmArcColocationChargesOnrf;
	}
	@JsonProperty("b_lm_arc_converter_charges_onrf")
	public String getbLmArcConverterChargesOnrf() {
		return bLmArcConverterChargesOnrf;
	}
	@JsonProperty("b_lm_arc_converter_charges_onrf")
	public void setbLmArcConverterChargesOnrf(String bLmArcConverterChargesOnrf) {
		this.bLmArcConverterChargesOnrf = bLmArcConverterChargesOnrf;
	}
	@JsonProperty("b_lm_arc_modem_charges_offwl")
	public String getbLmArcModemChargesOffwl() {
		return bLmArcModemChargesOffwl;
	}
	@JsonProperty("b_lm_arc_modem_charges_offwl")
	public void setbLmArcModemChargesOffwl(String bLmArcModemChargesOffwl) {
		this.bLmArcModemChargesOffwl = bLmArcModemChargesOffwl;
	}
	@JsonProperty("b_lm_arc_offwl")
	public String getbLmArcOffwl() {
		return bLmArcOffwl;
	}
	@JsonProperty("b_lm_arc_offwl")
	public void setbLmArcOffwl(String bLmArcOffwl) {
		this.bLmArcOffwl = bLmArcOffwl;
	}
	@JsonProperty("b_lm_arc_orderable_bw_onwl")
	public String getbLmArcOrderableBwOnwl() {
		return bLmArcOrderableBwOnwl;
	}
	@JsonProperty("b_lm_arc_orderable_bw_onwl")
	public void setbLmArcOrderableBwOnwl(String bLmArcOrderableBwOnwl) {
		this.bLmArcOrderableBwOnwl = bLmArcOrderableBwOnwl;
	}
	@JsonProperty("b_lm_arc_prow_onwl")
	public String getbLmArcProwOnwl() {
		return bLmArcProwOnwl;
	}
	@JsonProperty("b_lm_arc_prow_onwl")
	public void setbLmArcProwOnwl(String bLmArcProwOnwl) {
		this.bLmArcProwOnwl = bLmArcProwOnwl;
	}
	@JsonProperty("b_lm_arc_radwin_bw_onrf")
	public String getbLmArcRadwinBwOnrf() {
		return bLmArcRadwinBwOnrf;
	}
	@JsonProperty("b_lm_arc_radwin_bw_onrf")
	public void setbLmArcRadwinBwOnrf(String bLmArcRadwinBwOnrf) {
		this.bLmArcRadwinBwOnrf = bLmArcRadwinBwOnrf;
	}
	@JsonProperty("b_lm_nrc_bw_onrf")
	public String getbLmNrcBwOnrf() {
		return bLmNrcBwOnrf;
	}
	@JsonProperty("b_lm_nrc_bw_onrf")
	public void setbLmNrcBwOnrf(String bLmNrcBwOnrf) {
		this.bLmNrcBwOnrf = bLmNrcBwOnrf;
	}
	@JsonProperty("b_lm_nrc_bw_prov_ofrf")
	public String getbLmNrcBwProvOfrf() {
		return bLmNrcBwProvOfrf;
	}
	@JsonProperty("b_lm_nrc_bw_prov_ofrf")
	public void setbLmNrcBwProvOfrf(String bLmNrcBwProvOfrf) {
		this.bLmNrcBwProvOfrf = bLmNrcBwProvOfrf;
	}
	@JsonProperty("b_lm_nrc_mast_ofrf")
	public String getbLmNrcMast_ofrf() {
		return bLmNrcMast_ofrf;
	}
	@JsonProperty("b_lm_nrc_mast_ofrf")
	public void setbLmNrcMast_ofrf(String bLmNrcMast_ofrf) {
		this.bLmNrcMast_ofrf = bLmNrcMast_ofrf;
	}
	@JsonProperty("b_lm_nrc_mast_onrf")
	public String getbLmNrcMastOnrf() {
		return bLmNrcMastOnrf;
	}
	@JsonProperty("b_lm_nrc_mast_onrf")
	public void setbLmNrcMastOnrf(String bLmNrcMastOnrf) {
		this.bLmNrcMastOnrf = bLmNrcMastOnrf;
	}
	@JsonProperty("b_lm_nrc_network_capex_onwl")
	public String getbLmNrcNetworkCapexOnwl() {
		return bLmNrcNetworkCapexOnwl;
	}
	@JsonProperty("b_lm_nrc_network_capex_onwl")
	public void setbLmNrcNetworkCapexOnwl(String bLmNrcNetworkCapexOnwl) {
		this.bLmNrcNetworkCapexOnwl = bLmNrcNetworkCapexOnwl;
	}
	@JsonProperty("b_lm_nrc_prow_onwl")
	public String getbLmNrcProwOnwl() {
		return bLmNrcProwOnwl;
	}
	@JsonProperty("b_lm_nrc_prow_onwl")
	public void setbLmNrcProwOnwl(String bLmNrcProwOnwl) {
		this.bLmNrcProwOnwl = bLmNrcProwOnwl;
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
	public String getbLmotcNrcInstallationOffwl() {
		return bLmotcNrcInstallationOffwl;
	}
	@JsonProperty("b_lm_otc_nrc_installation_offwl")
	public void setbLmotcNrcInstallationOffwl(String bLmotcNrcInstallationOffwl) {
		this.bLmotcNrcInstallationOffwl = bLmotcNrcInstallationOffwl;
	}
	@JsonProperty("b_lm_otc_nrc_orderable_bw_onwl")
	public String getbLmOtcNrcOrderablBwOnwl() {
		return bLmOtcNrcOrderablBwOnwl;
	}
	@JsonProperty("b_lm_otc_nrc_orderable_bw_onwl")
	public void setbLmOtcNrcOrderablBwOnwl(String bLmOtcNrcOrderablBwOnwl) {
		this.bLmOtcNrcOrderablBwOnwl = bLmOtcNrcOrderablBwOnwl;
	}
	@JsonProperty("a_lm_arc_bw_offwl")
	public String getaLmArcBwOffwl() {
		return aLmArcBwOffwl;
	}
	@JsonProperty("a_lm_arc_bw_offwl")
	public void setaLmArcBwOffwl(String aLmArcBwOffwl) {
		this.aLmArcBwOffwl = aLmArcBwOffwl;
	}
	@JsonProperty("a_lm_arc_bw_onrf")
	public String getaLmArcBwOnrf() {
		return aLmArcBwOnrf;
	}
	@JsonProperty("a_lm_arc_bw_onrf")
	public void setaLmArcBwOnrf(String aLmArcBwOnrf) {
		this.aLmArcBwOnrf = aLmArcBwOnrf;
	}
	@JsonProperty("a_lm_arc_colocation_charges_onrf")
	public String getaLmArcColocationChargesOnrf() {
		return aLmArcColocationChargesOnrf;
	}
	@JsonProperty("a_lm_arc_colocation_charges_onrf")
	public void setaLmArcColocationChargesOnrf(String aLmArcColocationChargesOnrf) {
		this.aLmArcColocationChargesOnrf = aLmArcColocationChargesOnrf;
	}
	@JsonProperty("a_lm_arc_converter_charges_onrf")
	public String getaLmArcConverterChargesOnrf() {
		return aLmArcConverterChargesOnrf;
	}
	@JsonProperty("a_lm_arc_converter_charges_onrf")
	public void setaLmArcConverterChargesOnrf(String aLmArcConverterChargesOnrf) {
		this.aLmArcConverterChargesOnrf = aLmArcConverterChargesOnrf;
	}
	@JsonProperty("a_lm_arc_modem_charges_offwl")
	public String getaLmArcModemChargesOffwl() {
		return aLmArcModemChargesOffwl;
	}
	@JsonProperty("a_lm_arc_modem_charges_offwl")
	public void setaLmArcModemChargesOffwl(String aLmArcModemChargesOffwl) {
		this.aLmArcModemChargesOffwl = aLmArcModemChargesOffwl;
	}
	@JsonProperty("a_lm_arc_offwl")
	public String getaLmArcOffwl() {
		return aLmArcOffwl;
	}
	@JsonProperty("a_lm_arc_offwl")
	public void setaLmArcOffwl(String aLmArcOffwl) {
		this.aLmArcOffwl = aLmArcOffwl;
	}
	@JsonProperty("a_lm_arc_orderable_bw_onwl")
	public String getaLmArcOrderableBwOnwl() {
		return aLmArcOrderableBwOnwl;
	}
	@JsonProperty("a_lm_arc_orderable_bw_onwl")
	public void setaLmArcOrderableBwOnwl(String aLmArcOrderableBwOnwl) {
		this.aLmArcOrderableBwOnwl = aLmArcOrderableBwOnwl;
	}
	@JsonProperty("a_lm_arc_prow_onwl")
	public String getaLmArcProwOnwl() {
		return aLmArcProwOnwl;
	}
	@JsonProperty("a_lm_arc_prow_onwl")
	public void setaLmArcProwOnwl(String aLmArcProwOnwl) {
		this.aLmArcProwOnwl = aLmArcProwOnwl;
	}
	@JsonProperty("a_lm_arc_radwin_bw_onrf")
	public String getaLmArcRadwinBwOnrf() {
		return aLmArcRadwinBwOnrf;
	}
	@JsonProperty("a_lm_arc_radwin_bw_onrf")
	public void setaLmArcRadwinBwOnrf(String aLmArcRadwinBwOnrf) {
		this.aLmArcRadwinBwOnrf = aLmArcRadwinBwOnrf;
	}
	@JsonProperty("a_lm_nrc_bw_onrf")
	public String getaLmNrcBwOnrf() {
		return aLmNrcBwOnrf;
	}
	@JsonProperty("a_lm_nrc_bw_onrf")
	public void setaLmNrcBwOnrf(String aLmNrcBwOnrf) {
		this.aLmNrcBwOnrf = aLmNrcBwOnrf;
	}
	@JsonProperty("a_lm_nrc_bw_prov_ofrf")
	public String getaLmNrcBwProvOfrf() {
		return aLmNrcBwProvOfrf;
	}
	@JsonProperty("a_lm_nrc_bw_prov_ofrf")
	public void setaLmNrcBwProvOfrf(String aLmNrcBwProvOfrf) {
		this.aLmNrcBwProvOfrf = aLmNrcBwProvOfrf;
	}
	@JsonProperty("a_lm_nrc_mast_ofrf")
	public String getaLmNrcMast_ofrf() {
		return aLmNrcMast_ofrf;
	}
	@JsonProperty("a_lm_nrc_mast_ofrf")
	public void setaLmNrcMast_ofrf(String aLmNrcMast_ofrf) {
		this.aLmNrcMast_ofrf = aLmNrcMast_ofrf;
	}
	@JsonProperty("a_lm_nrc_mast_onrf")
	public String getaLmNrcMastOnrf() {
		return aLmNrcMastOnrf;
	}
	@JsonProperty("a_lm_nrc_mast_onrf")
	public void setaLmNrcMastOnrf(String aLmNrcMastOnrf) {
		this.aLmNrcMastOnrf = aLmNrcMastOnrf;
	}
	@JsonProperty("a_lm_nrc_network_capex_onwl")
	public String getaLmNrcNetworkCapexOnwl() {
		return aLmNrcNetworkCapexOnwl;
	}
	@JsonProperty("a_lm_nrc_network_capex_onwl")
	public void setaLmNrcNetworkCapexOnwl(String aLmNrcNetworkCapexOnwl) {
		this.aLmNrcNetworkCapexOnwl = aLmNrcNetworkCapexOnwl;
	}
	@JsonProperty("a_lm_nrc_prow_onwl")
	public String getaLmNrcProwOnwl() {
		return aLmNrcProwOnwl;
	}
	@JsonProperty("a_lm_nrc_prow_onwl")
	public void setaLmNrcProwOnwl(String aLmNrcProwOnwl) {
		this.aLmNrcProwOnwl = aLmNrcProwOnwl;
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
	public String getaLmotcNrcInstallationOffwl() {
		return aLmotcNrcInstallationOffwl;
	}
	@JsonProperty("a_lm_otc_nrc_installation_offwl")
	public void setaLmotcNrcInstallationOffwl(String aLmotcNrcInstallationOffwl) {
		this.aLmotcNrcInstallationOffwl = aLmotcNrcInstallationOffwl;
	}
	@JsonProperty("a_lm_otc_nrc_orderable_bw_onwl")
	public String getaLmOtcNrcOrderablBwOnwl() {
		return aLmOtcNrcOrderablBwOnwl;
	}
	@JsonProperty("a_lm_otc_nrc_orderable_bw_onwl")
	public void setaLmOtcNrcOrderablBwOnwl(String aLmOtcNrcOrderablBwOnwl) {
		this.aLmOtcNrcOrderablBwOnwl = aLmOtcNrcOrderablBwOnwl;
	}

	
	@JsonProperty("old_contract_term")
	public String getOldContractTerm() {
		return oldContractTerm;
	}
	@JsonProperty("old_contract_term")
	public void setOldContractTerm(String oldContractTerm) {
		this.oldContractTerm = oldContractTerm;
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


    @JsonProperty("link_id")
    public String getLinkId() {
        return linkId;
    }

    @JsonProperty("link_id")
    public void setLinkId(String linkId) {
        this.linkId = linkId;
    }

    @JsonProperty("site_id")
    public String getSiteId() {
        return siteId;
    }

    @JsonProperty("site_id")
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

    @JsonProperty("bw_mbps")
    public String getBwMbps() {
        return bwMbps;
    }

    @JsonProperty("bw_mbps")
    public void setBwMbps(String bwMbps) {
        this.bwMbps = bwMbps;
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

    @JsonProperty("sla_varient")
    public String getSlaVarient() {
        return slaVarient;
    }

    @JsonProperty("sla_varient")
    public void setSlaVarient(String slaVarient) {
        this.slaVarient = slaVarient;
    }

    @JsonProperty("chargeable_distance")
    public String getChargeableDistance() {
        return chargeableDistance;
    }

    @JsonProperty("chargeable_distance")
    public void setChargeableDistance(String chargeableDistance) {
        this.chargeableDistance = chargeableDistance;
    }

    @JsonProperty("dist_betw_pops")
    public String getDistBetwPops() {
        return distBetwPops;
    }

    @JsonProperty("dist_betw_pops")
    public void setDistBetwPops(String distBetwPops) {
        this.distBetwPops = distBetwPops;
    }

    @JsonProperty("intra_inter_flag")
    public String getIntraInterFlag() {
        return intraInterFlag;
    }

    @JsonProperty("intra_inter_flag")
    public void setIntraInterFlag(String intraInterFlag) {
        this.intraInterFlag = intraInterFlag;
    }

    @JsonProperty("a_latitude_final")
    public String getALatitudeFinal() {
        return aLatitudeFinal;
    }

    @JsonProperty("a_latitude_final")
    public void setALatitudeFinal(String aLatitudeFinal) {
        this.aLatitudeFinal = aLatitudeFinal;
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

    @JsonProperty("a_local_loop_interface")
    public String getALocalLoopInterface() {
        return aLocalLoopInterface;
    }

    @JsonProperty("a_local_loop_interface")
    public void setALocalLoopInterface(String aLocalLoopInterface) {
        this.aLocalLoopInterface = aLocalLoopInterface;
    }

    @JsonProperty("a_lm_arc_bw_onwl")
    public String getALmArcBwOnwl() {
        return aLmArcBwOnwl;
    }

    @JsonProperty("a_lm_arc_bw_onwl")
    public void setALmArcBwOnwl(String aLmArcBwOnwl) {
        this.aLmArcBwOnwl = aLmArcBwOnwl;
    }

    @JsonProperty("a_lm_nrc_bw_onwl")
    public String getALmNrcBwOnwl() {
        return aLmNrcBwOnwl;
    }

    @JsonProperty("a_lm_nrc_bw_onwl")
    public void setALmNrcBwOnwl(String aLmNrcBwOnwl) {
        this.aLmNrcBwOnwl = aLmNrcBwOnwl;
    }

    @JsonProperty("a_lm_nrc_mux_onwl")
    public String getALmNrcMuxOnwl() {
        return aLmNrcMuxOnwl;
    }

    @JsonProperty("a_lm_nrc_mux_onwl")
    public void setALmNrcMuxOnwl(String aLmNrcMuxOnwl) {
        this.aLmNrcMuxOnwl = aLmNrcMuxOnwl;
    }

    @JsonProperty("a_lm_nrc_inbldg_onwl")
    public String getALmNrcInbldgOnwl() {
        return aLmNrcInbldgOnwl;
    }

    @JsonProperty("a_lm_nrc_inbldg_onwl")
    public void setALmNrcInbldgOnwl(String aLmNrcInbldgOnwl) {
        this.aLmNrcInbldgOnwl = aLmNrcInbldgOnwl;
    }

    @JsonProperty("a_lm_nrc_ospcapex_onwl")
    public String getALmNrcOspcapexOnwl() {
        return aLmNrcOspcapexOnwl;
    }

    @JsonProperty("a_lm_nrc_ospcapex_onwl")
    public void setALmNrcOspcapexOnwl(String aLmNrcOspcapexOnwl) {
        this.aLmNrcOspcapexOnwl = aLmNrcOspcapexOnwl;
    }

    @JsonProperty("a_lm_nrc_nerental_onwl")
    public String getALmNrcNerentalOnwl() {
        return aLmNrcNerentalOnwl;
    }

    @JsonProperty("a_lm_nrc_nerental_onwl")
    public void setALmNrcNerentalOnwl(String aLmNrcNerentalOnwl) {
        this.aLmNrcNerentalOnwl = aLmNrcNerentalOnwl;
    }

    @JsonProperty("b_latitude_final")
    public String getBLatitudeFinal() {
        return bLatitudeFinal;
    }

    @JsonProperty("b_latitude_final")
    public void setBLatitudeFinal(String bLatitudeFinal) {
        this.bLatitudeFinal = bLatitudeFinal;
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

    @JsonProperty("b_local_loop_interface")
    public String getBLocalLoopInterface() {
        return bLocalLoopInterface;
    }

    @JsonProperty("b_local_loop_interface")
    public void setBLocalLoopInterface(String bLocalLoopInterface) {
        this.bLocalLoopInterface = bLocalLoopInterface;
    }

    @JsonProperty("b_lm_arc_bw_onwl")
    public String getBLmArcBwOnwl() {
        return bLmArcBwOnwl;
    }

    @JsonProperty("b_lm_arc_bw_onwl")
    public void setBLmArcBwOnwl(String bLmArcBwOnwl) {
        this.bLmArcBwOnwl = bLmArcBwOnwl;
    }

    @JsonProperty("b_lm_nrc_bw_onwl")
    public String getBLmNrcBwOnwl() {
        return bLmNrcBwOnwl;
    }

    @JsonProperty("b_lm_nrc_bw_onwl")
    public void setBLmNrcBwOnwl(String bLmNrcBwOnwl) {
        this.bLmNrcBwOnwl = bLmNrcBwOnwl;
    }

    @JsonProperty("b_lm_nrc_mux_onwl")
    public String getBLmNrcMuxOnwl() {
        return bLmNrcMuxOnwl;
    }

    @JsonProperty("b_lm_nrc_mux_onwl")
    public void setBLmNrcMuxOnwl(String bLmNrcMuxOnwl) {
        this.bLmNrcMuxOnwl = bLmNrcMuxOnwl;
    }

    @JsonProperty("b_lm_nrc_inbldg_onwl")
    public String getBLmNrcInbldgOnwl() {
        return bLmNrcInbldgOnwl;
    }

    @JsonProperty("b_lm_nrc_inbldg_onwl")
    public void setBLmNrcInbldgOnwl(String bLmNrcInbldgOnwl) {
        this.bLmNrcInbldgOnwl = bLmNrcInbldgOnwl;
    }

    @JsonProperty("b_lm_nrc_ospcapex_onwl")
    public String getBLmNrcOspcapexOnwl() {
        return bLmNrcOspcapexOnwl;
    }

    @JsonProperty("b_lm_nrc_ospcapex_onwl")
    public void setBLmNrcOspcapexOnwl(String bLmNrcOspcapexOnwl) {
        this.bLmNrcOspcapexOnwl = bLmNrcOspcapexOnwl;
    }

    @JsonProperty("b_lm_nrc_nerental_onwl")
    public String getBLmNrcNerentalOnwl() {
        return bLmNrcNerentalOnwl;
    }

    @JsonProperty("b_lm_nrc_nerental_onwl")
    public void setBLmNrcNerentalOnwl(String bLmNrcNerentalOnwl) {
        this.bLmNrcNerentalOnwl = bLmNrcNerentalOnwl;
    }

    @JsonProperty("a_POP_DIST_KM_SERVICE_MOD")
	public String getaPopDistKmServiceMod() {
		return aPopDistKmServiceMod;
	}

    @JsonProperty("a_POP_DIST_KM_SERVICE_MOD")
	public void setaPopDistKmServiceMod(String aPopDistKmServiceMod) {
		this.aPopDistKmServiceMod = aPopDistKmServiceMod;
	}

    @JsonProperty("b_POP_DIST_KM_SERVICE_MOD")
	public String getbPopDistKmServiceMod() {
		return bPopDistKmServiceMod;
	}

    @JsonProperty("b_POP_DIST_KM_SERVICE_MOD")
	public void setbPopDistKmServiceMod(String bPopDistKmServiceMod) {
		this.bPopDistKmServiceMod = bPopDistKmServiceMod;
	}

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

	public String getaOrchConnection() { return aOrchConnection; }

	public void setaOrchConnection(String aOrchConnection) { this.aOrchConnection = aOrchConnection; }

	public String getbOrchConnection() { return bOrchConnection; }

	public void setbOrchConnection(String bOrchConnection) { this.bOrchConnection = bOrchConnection; }

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
}


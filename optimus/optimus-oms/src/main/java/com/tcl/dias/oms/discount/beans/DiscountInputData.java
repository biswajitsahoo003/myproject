package com.tcl.dias.oms.discount.beans;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "site_id",
    "bw_mbps",
    "burstable_bw",
    "product_name",
    "local_loop_interface",
    "connection_type",
    "cpe_variant",
    "cpe_management_type",
    "cpe_supply_type",
    "topology",
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
    "Orch_Connection",
    "Orch_LM_Type",
    "ip_address_arrangement",
    "ipv4_address_pool_size",
    "ipv6_address_pool_size",
    "sp_CPE_Outright_NRC",
    "sp_CPE_Rental_ARC",
    "sp_CPE_Install_NRC",
    "sp_CPE_Management_ARC",
    "sp_port_arc",
    "sp_port_nrc",
    "sp_port_mrc",
    "sp_burst_per_MB_price_ARC",
    "sp_additional_IP_ARC",
    "opportunityTerm",
    "Mast_3KM_avg_mast_ht",
    "avg_mast_ht",
    "chargeable_distance_km",
    "solution_type",
    "resp_city",
    "osp_dist_meters",
    "Orch_Category",
    "local_loop_bw",
    "sp_lm_mrc",
    "sp_lm_nrc",
    "sp_Xconnect_MRC",
    "sp_Xconnect_NRC",
    "sp_CPE_support_charges",
    "sp_CPE_recovery",
    "sp_CPE_SFP_charges",
    "sp_CPE_customs_local_taxes",
    "sp_CPE_logistics_cost",
    "sp_CPE_management",
    "Type",
    "country",
    "sp_CPE_Management_MRC",
    "provider_Local_Loop_Interface",
    //added for nde dis
    "sp_lm_arc_bw_offwl",
	"sp_lm_arc_modem_charges_offwl",
	"sp_lm_otc_modem_charges_offwl",
	"sp_lm_otc_nrc_installation_offwl",
	"sp_lm_arc_prow_onwl",
    "sp_lm_nrc_prow_onwl",
    "sp_lm_arc_bw_backhaul_onrf",
    "sp_lm_arc_colocation_charges_onrf",
    "sp_lm_arc_converter_charges_onrf",
    
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
	
	"country",
	"is_colocated",
	"Type",
	"lp_port_arc",
	"lp_port_nrc"
    
   
})
public class DiscountInputData  {

    @JsonProperty("site_id")
    private String siteId;
    @JsonProperty("bw_mbps")
    private String bwMbps;
    @JsonProperty("burstable_bw")
    private String burstableBw;
    @JsonProperty("product_name")
    private String productName;
    @JsonProperty("local_loop_interface")
    private String localLoopInterface;
    @JsonProperty("connection_type")
    private String connectionType;
    @JsonProperty("cpe_variant")
    private String cpeVariant;
    @JsonProperty("cpe_management_type")
    private String cpeManagementType;
    @JsonProperty("cpe_supply_type")
    private String cpeSupplyType;
    @JsonProperty("topology")
    private String topology;
    @JsonProperty("sp_lm_arc_bw_onwl")
    private String spLmArcBwOnwl;
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
    @JsonProperty("Orch_Connection")
    private String orchConnection;
    @JsonProperty("Orch_LM_Type")
    private String orchLMType;
    @JsonProperty("ip_address_arrangement")
    private String ipAddressArrangement;
    @JsonProperty("ipv4_address_pool_size")
    private String ipv4AddressPoolSize;
    @JsonProperty("ipv6_address_pool_size")
    private String ipv6AddressPoolSize;
    @JsonProperty("sp_CPE_Outright_NRC")
    private String spCPEOutrightNRC;
    @JsonProperty("sp_CPE_Rental_ARC")
    private String spCPERentalARC;
    @JsonProperty("sp_CPE_Install_NRC")
    private String spCPEInstallNRC;
    @JsonProperty("sp_CPE_Management_ARC")
    private String spCPEManagementARC;
    @JsonProperty("sp_port_arc")
    private String spPortArc;
    @JsonProperty("sp_port_nrc")
    private String spPortNrc;
    @JsonProperty("sp_port_mrc")
    private String spPortMrc;
    @JsonProperty("sp_burst_per_MB_price_ARC")
    private String spBurstPerMBPriceARC;
    @JsonProperty("sp_additional_IP_ARC")
    private String spAdditionalIPARC;
    @JsonProperty("opportunityTerm")
    private String opportunityTerm;
    @JsonProperty("Mast_3KM_avg_mast_ht")
    private String mast3KMAvgMastHt;
    @JsonProperty("avg_mast_ht")
    private String avgMastHt;
    @JsonProperty("chargeable_distance_km")
    private String chargeableDistanceKm;
    @JsonProperty("solution_type")
    private String solutionType;
    @JsonProperty("resp_city")
    private String respCity;
    @JsonProperty("osp_dist_meters")
    private String ospDistMeters;
    @JsonProperty("Orch_Category")
    private String orchCategory;
    @JsonProperty("local_loop_bw")
    private String localLoopBw;
    
  //gvpn intl subcomponent
    @JsonProperty("sp_lm_mrc")
    private String spLmMrc;
    @JsonProperty("sp_lm_nrc")
    private String spLmNrc;
    @JsonProperty("sp_Xconnect_MRC")
    private String spXconnectMRC;
    @JsonProperty("sp_Xconnect_NRC")
    private String spXconnectNRC;
    @JsonProperty("sp_CPE_support_charges")
    private String spCPESupportCharges;
    @JsonProperty("sp_CPE_recovery")
    private String spCPERecovery;
    @JsonProperty("sp_CPE_SFP_charges")
    private String spCPESFPCharges;
    @JsonProperty("sp_CPE_customs_local_taxes")
    private String spCPECustomsLocalTaxes;
    @JsonProperty("sp_CPE_logistics_cost")
    private String spCPELogisticsCost;
    @JsonProperty("sp_CPE_management")
    private String spCPEManagement;
    @JsonProperty("Type")
    private String Type;
    @JsonProperty("country")
    private String country;
    @JsonProperty("sp_CPE_Management_MRC")
    private String spCPEManagementMRC;
    @JsonProperty("provider_Local_Loop_Interface")
    private String providerLocalLoopInterface;
    
    
    //added for nde dis
    @JsonProperty("sp_lm_arc_bw_offwl")
    private String spLmArcBwOffwl;
    @JsonProperty("sp_lm_arc_modem_charges_offwl")
    private String spLmArcModemChargesOffwl;
    @JsonProperty("sp_lm_otc_modem_charges_offwl")
    private String spLmOtcModemChargesOffwl;
    @JsonProperty("sp_lm_otc_nrc_installation_offwl")
    private String spLmOtcNrcInstallationOffwl;
    @JsonProperty("sp_lm_arc_prow_onwl")
    private String spLmArcProwOnwl;
    @JsonProperty("sp_lm_nrc_prow_onwl")
    private String spLmNrcProwOnwl;
    @JsonProperty("sp_lm_arc_bw_backhaul_onrf")
    private String spLmArcBwBackhaulOnrf;
    @JsonProperty("sp_lm_arc_colocation_charges_onrf")
    private String spLmArcColocationChargesOnrf;
    @JsonProperty("sp_lm_arc_converter_charges_onrf")
    private String spLmArcConverterChargesOnrf;
    
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
    
    
   
    @JsonProperty("is_colocated")
    private String isColocated;
    @JsonProperty("Type")
    private String type;
    @JsonProperty("lp_port_arc")
    private String lpPortArc;
    @JsonProperty("lp_port_nrc")
    private String lpPortNrc;
   

    @JsonProperty("is_colocated")
    public String getIsColocated() {
    return isColocated;
    }

    @JsonProperty("is_colocated")
    public void setIsColocated(String isColocated) {
    this.isColocated = isColocated;
    }

   

    @JsonProperty("lp_port_arc")
    public String getLpPortArc() {
    return lpPortArc;
    }

    @JsonProperty("lp_port_arc")
    public void setLpPortArc(String lpPortArc) {
    this.lpPortArc = lpPortArc;
    }

    @JsonProperty("lp_port_nrc")
    public String getLpPortNrc() {
    return lpPortNrc;
    }

    @JsonProperty("lp_port_nrc")
    public void setLpPortNrc(String lpPortNrc) {
    this.lpPortNrc = lpPortNrc;
    }

   

   

    @JsonProperty("sp_lm_arc_bw_offwl")
    public String getSpLmArcBwOffwl() {
    return spLmArcBwOffwl;
    }

    @JsonProperty("sp_lm_arc_bw_offwl")
    public void setSpLmArcBwOffwl(String spLmArcBwOffwl) {
    this.spLmArcBwOffwl = spLmArcBwOffwl;
    }

    @JsonProperty("sp_lm_arc_modem_charges_offwl")
    public String getSpLmArcModemChargesOffwl() {
    return spLmArcModemChargesOffwl;
    }

    @JsonProperty("sp_lm_arc_modem_charges_offwl")
    public void setSpLmArcModemChargesOffwl(String spLmArcModemChargesOffwl) {
    this.spLmArcModemChargesOffwl = spLmArcModemChargesOffwl;
    }

    @JsonProperty("sp_lm_otc_modem_charges_offwl")
    public String getSpLmOtcModemChargesOffwl() {
    return spLmOtcModemChargesOffwl;
    }

    @JsonProperty("sp_lm_otc_modem_charges_offwl")
    public void setSpLmOtcModemChargesOffwl(String spLmOtcModemChargesOffwl) {
    this.spLmOtcModemChargesOffwl = spLmOtcModemChargesOffwl;
    }

    @JsonProperty("sp_lm_otc_nrc_installation_offwl")
    public String getSpLmOtcNrcInstallationOffwl() {
    return spLmOtcNrcInstallationOffwl;
    }

    @JsonProperty("sp_lm_otc_nrc_installation_offwl")
    public void setSpLmOtcNrcInstallationOffwl(String spLmOtcNrcInstallationOffwl) {
    this.spLmOtcNrcInstallationOffwl = spLmOtcNrcInstallationOffwl;
    }

    @JsonProperty("sp_lm_arc_prow_onwl")
    public String getSpLmArcProwOnwl() {
    return spLmArcProwOnwl;
    }

    @JsonProperty("sp_lm_arc_prow_onwl")
    public void setSpLmArcProwOnwl(String spLmArcProwOnwl) {
    this.spLmArcProwOnwl = spLmArcProwOnwl;
    }

    @JsonProperty("sp_lm_nrc_prow_onwl")
    public String getSpLmNrcProwOnwl() {
    return spLmNrcProwOnwl;
    }

    @JsonProperty("sp_lm_nrc_prow_onwl")
    public void setSpLmNrcProwOnwl(String spLmNrcProwOnwl) {
    this.spLmNrcProwOnwl = spLmNrcProwOnwl;
    }

    @JsonProperty("sp_lm_arc_bw_backhaul_onrf")
    public String getSpLmArcBwBackhaulOnrf() {
    return spLmArcBwBackhaulOnrf;
    }

    @JsonProperty("sp_lm_arc_bw_backhaul_onrf")
    public void setSpLmArcBwBackhaulOnrf(String spLmArcBwBackhaulOnrf) {
    this.spLmArcBwBackhaulOnrf = spLmArcBwBackhaulOnrf;
    }

    @JsonProperty("sp_lm_arc_colocation_charges_onrf")
    public String getSpLmArcColocationChargesOnrf() {
    return spLmArcColocationChargesOnrf;
    }

    @JsonProperty("sp_lm_arc_colocation_charges_onrf")
    public void setSpLmArcColocationChargesOnrf(String spLmArcColocationChargesOnrf) {
    this.spLmArcColocationChargesOnrf = spLmArcColocationChargesOnrf;
    }

    @JsonProperty("sp_lm_arc_converter_charges_onrf")
    public String getSpLmArcConverterChargesOnrf() {
    return spLmArcConverterChargesOnrf;
    }

    @JsonProperty("sp_lm_arc_converter_charges_onrf")
    public void setSpLmArcConverterChargesOnrf(String spLmArcConverterChargesOnrf) {
    this.spLmArcConverterChargesOnrf = spLmArcConverterChargesOnrf;
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
    
    
    
    
    @JsonProperty("sp_CPE_Management_MRC")
    public String getSpCPEManagementMRC() {
		return spCPEManagementMRC;
	}
    @JsonProperty("sp_CPE_Management_MRC")
	public void setSpCPEManagementMRC(String spCPEManagementMRC) {
		this.spCPEManagementMRC = spCPEManagementMRC;
	}
    @JsonProperty("provider_Local_Loop_Interface")
	public String getProviderLocalLoopInterface() {
		return providerLocalLoopInterface;
	}
    @JsonProperty("provider_Local_Loop_Interface")
	public void setProviderLocalLoopInterface(String providerLocalLoopInterface) {
		this.providerLocalLoopInterface = providerLocalLoopInterface;
	}
	@JsonProperty("country")
    public String getCountry() {
		return country;
	}
    @JsonProperty("country")
	public void setCountry(String country) {
		this.country = country;
	}
	@JsonProperty("Type")
    public String getType() {
		return Type;
	}
    @JsonProperty("Type")
	public void setType(String type) {
		Type = type;
	}
	@JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("site_id")
    public String getSiteId() {
        return siteId;
    }

    @JsonProperty("site_id")
    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    @JsonProperty("bw_mbps")
    public String getBwMbps() {
        return bwMbps;
    }

    @JsonProperty("bw_mbps")
    public void setBwMbps(String bwMbps) {
        this.bwMbps = bwMbps;
    }

    @JsonProperty("burstable_bw")
    public String getBurstableBw() {
        return burstableBw;
    }

    @JsonProperty("burstable_bw")
    public void setBurstableBw(String burstableBw) {
        this.burstableBw = burstableBw;
    }

    @JsonProperty("product_name")
    public String getProductName() {
        return productName;
    }

    @JsonProperty("product_name")
    public void setProductName(String productName) {
        this.productName = productName;
    }

    @JsonProperty("local_loop_interface")
    public String getLocalLoopInterface() {
        return localLoopInterface;
    }

    @JsonProperty("local_loop_interface")
    public void setLocalLoopInterface(String localLoopInterface) {
        this.localLoopInterface = localLoopInterface;
    }

    @JsonProperty("connection_type")
    public String getConnectionType() {
        return connectionType;
    }

    @JsonProperty("connection_type")
    public void setConnectionType(String connectionType) {
        this.connectionType = connectionType;
    }

    @JsonProperty("cpe_variant")
    public String getCpeVariant() {
        return cpeVariant;
    }

    @JsonProperty("cpe_variant")
    public void setCpeVariant(String cpeVariant) {
        this.cpeVariant = cpeVariant;
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

    @JsonProperty("topology")
    public String getTopology() {
        return topology;
    }

    @JsonProperty("topology")
    public void setTopology(String topology) {
        this.topology = topology;
    }

    @JsonProperty("sp_lm_arc_bw_onwl")
    public String getSpLmArcBwOnwl() {
        return spLmArcBwOnwl;
    }

    @JsonProperty("sp_lm_arc_bw_onwl")
    public void setSpLmArcBwOnwl(String spLmArcBwOnwl) {
        this.spLmArcBwOnwl = spLmArcBwOnwl;
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

    @JsonProperty("sp_CPE_Outright_NRC")
    public String getSpCPEOutrightNRC() {
        return spCPEOutrightNRC;
    }

    @JsonProperty("sp_CPE_Outright_NRC")
    public void setSpCPEOutrightNRC(String spCPEOutrightNRC) {
        this.spCPEOutrightNRC = spCPEOutrightNRC;
    }

    @JsonProperty("sp_CPE_Rental_ARC")
    public String getSpCPERentalARC() {
        return spCPERentalARC;
    }

    @JsonProperty("sp_CPE_Rental_ARC")
    public void setSpCPERentalARC(String spCPERentalARC) {
        this.spCPERentalARC = spCPERentalARC;
    }

    @JsonProperty("sp_CPE_Install_NRC")
    public String getSpCPEInstallNRC() {
        return spCPEInstallNRC;
    }

    @JsonProperty("sp_CPE_Install_NRC")
    public void setSpCPEInstallNRC(String spCPEInstallNRC) {
        this.spCPEInstallNRC = spCPEInstallNRC;
    }

    @JsonProperty("sp_CPE_Management_ARC")
    public String getSpCPEManagementARC() {
        return spCPEManagementARC;
    }

    @JsonProperty("sp_CPE_Management_ARC")
    public void setSpCPEManagementARC(String spCPEManagementARC) {
        this.spCPEManagementARC = spCPEManagementARC;
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

    @JsonProperty("sp_port_mrc")
    public void setSpPortMrc(String spPortMrc) {
        this.spPortMrc = spPortMrc;
    }
    
    @JsonProperty("sp_port_mrc")
    public String getSpPortMrc() {
        return spPortMrc;
    }

    @JsonProperty("sp_port_nrc")
    public void setSpPortNrc(String spPortNrc) {
        this.spPortNrc = spPortNrc;
    }

    @JsonProperty("sp_burst_per_MB_price_ARC")
    public String getSpBurstPerMBPriceARC() {
        return spBurstPerMBPriceARC;
    }

    @JsonProperty("sp_burst_per_MB_price_ARC")
    public void setSpBurstPerMBPriceARC(String spBurstPerMBPriceARC) {
        this.spBurstPerMBPriceARC = spBurstPerMBPriceARC;
    }

    @JsonProperty("sp_additional_IP_ARC")
    public String getSpAdditionalIPARC() {
        return spAdditionalIPARC;
    }

    @JsonProperty("sp_additional_IP_ARC")
    public void setSpAdditionalIPARC(String spAdditionalIPARC) {
        this.spAdditionalIPARC = spAdditionalIPARC;
    }

    @JsonProperty("opportunityTerm")
    public String getOpportunityTerm() {
        return opportunityTerm;
    }

    @JsonProperty("opportunityTerm")
    public void setOpportunityTerm(String opportunityTerm) {
        this.opportunityTerm = opportunityTerm;
    }

    @JsonProperty("Mast_3KM_avg_mast_ht")
    public String getMast3KMAvgMastHt() {
        return mast3KMAvgMastHt;
    }

    @JsonProperty("Mast_3KM_avg_mast_ht")
    public void setMast3KMAvgMastHt(String mast3KMAvgMastHt) {
        this.mast3KMAvgMastHt = mast3KMAvgMastHt;
    }

    @JsonProperty("avg_mast_ht")
    public String getAvgMastHt() {
        return avgMastHt;
    }

    @JsonProperty("avg_mast_ht")
    public void setAvgMastHt(String avgMastHt) {
        this.avgMastHt = avgMastHt;
    }

    @JsonProperty("chargeable_distance_km")
    public String getChargeableDistanceKm() {
        return chargeableDistanceKm;
    }

    @JsonProperty("chargeable_distance_km")
    public void setChargeableDistanceKm(String chargeableDistanceKm) {
        this.chargeableDistanceKm = chargeableDistanceKm;
    }

    @JsonProperty("solution_type")
    public String getSolutionType() {
        return solutionType;
    }

    @JsonProperty("solution_type")
    public void setSolutionType(String solutionType) {
        this.solutionType = solutionType;
    }

    @JsonProperty("resp_city")
    public String getRespCity() {
        return respCity;
    }

    @JsonProperty("resp_city")
    public void setRespCity(String respCity) {
        this.respCity = respCity;
    }

    @JsonProperty("osp_dist_meters")
    public String getOspDistMeters() {
        return ospDistMeters;
    }

    @JsonProperty("osp_dist_meters")
    public void setOspDistMeters(String ospDistMeters) {
        this.ospDistMeters = ospDistMeters;
    }

    @JsonProperty("Orch_Category")
    public String getOrchCategory() {
        return orchCategory;
    }

    @JsonProperty("Orch_Category")
    public void setOrchCategory(String orchCategory) {
        this.orchCategory = orchCategory;
    }

    public String getLocalLoopBw() {
		return localLoopBw;
	}

    @JsonProperty("local_loop_bw")
	public void setLocalLoopBw(String localLoopBw) {
		this.localLoopBw = localLoopBw;
	}

	@JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @JsonProperty("sp_lm_mrc")
	public String getSpLmMrc() {
		return spLmMrc;
	}
	@JsonProperty("sp_lm_mrc")
	public void setSpLmMrc(String spLmMrc) {
		this.spLmMrc = spLmMrc;
	}
	@JsonProperty("sp_lm_nrc")
	public String getSpLmNrc() {
		return spLmNrc;
	}
	@JsonProperty("sp_lm_nrc")
	public void setSpLmNrc(String spLmNrc) {
		this.spLmNrc = spLmNrc;
	}
	@JsonProperty("sp_Xconnect_MRC")
	public String getSpXconnectMRC() {
		return spXconnectMRC;
	}
	@JsonProperty("sp_Xconnect_MRC")
	public void setSpXconnectMRC(String spXconnectMRC) {
		this.spXconnectMRC = spXconnectMRC;
	}
	@JsonProperty("sp_Xconnect_NRC")
	public String getSpXconnectNRC() {
		return spXconnectNRC;
	}
	@JsonProperty("sp_Xconnect_NRC")
	public void setSpXconnectNRC(String spXconnectNRC) {
		this.spXconnectNRC = spXconnectNRC;
	}
	@JsonProperty("sp_CPE_support_charges")
	public String getSpCPESupportCharges() {
		return spCPESupportCharges;
	}
	@JsonProperty("sp_CPE_support_charges")
	public void setSpCPESupportCharges(String spCPESupportCharges) {
		this.spCPESupportCharges = spCPESupportCharges;
	}
	@JsonProperty("sp_CPE_recovery")
	public String getSpCPERecovery() {
		return spCPERecovery;
	}
	@JsonProperty("sp_CPE_recovery")
	public void setSpCPERecovery(String spCPERecovery) {
		this.spCPERecovery = spCPERecovery;
	}
	@JsonProperty("sp_CPE_SFP_charges")
	public String getSpCPESFPCharges() {
		return spCPESFPCharges;
	}
	@JsonProperty("sp_CPE_SFP_charges")
	public void setSpCPESFPCharges(String spCPESFPCharges) {
		this.spCPESFPCharges = spCPESFPCharges;
	}
	@JsonProperty("sp_CPE_customs_local_taxes")
	public String getSpCPECustomsLocalTaxes() {
		return spCPECustomsLocalTaxes;
	}
	@JsonProperty("sp_CPE_customs_local_taxes")
	public void setSpCPECustomsLocalTaxes(String spCPECustomsLocalTaxes) {
		this.spCPECustomsLocalTaxes = spCPECustomsLocalTaxes;
	}
	@JsonProperty("sp_CPE_logistics_cost")
	public String getSpCPELogisticsCost() {
		return spCPELogisticsCost;
	}
	@JsonProperty("sp_CPE_logistics_cost")
	public void setSpCPELogisticsCost(String spCPELogisticsCost) {
		this.spCPELogisticsCost = spCPELogisticsCost;
	}
	
	@JsonProperty("sp_CPE_management")
	public String getSpCPEManagement() {
		return spCPEManagement;
	}
	
	@JsonProperty("sp_CPE_management")
	public void setSpCPEManagement(String spCPEManagement) {
		this.spCPEManagement = spCPEManagement;
	}
	
	
}


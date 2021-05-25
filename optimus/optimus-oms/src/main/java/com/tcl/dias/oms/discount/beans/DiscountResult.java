
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
    "Mast_3KM_avg_mast_ht",
    "Orch_Category",
    "Orch_Connection",
    "Orch_LM_Type",
    "avg_mast_ht",
    "burstable_bw",
    "bw_mbps",
    "chargeable_distance_km",
    "connection_type",
    "cpe_management_type",
    "cpe_supply_type",
    "cpe_variant",
    "dis_CPE_Install_NRC",
    "dis_CPE_Management_ARC",
    "dis_CPE_Outright_NRC",
    "dis_CPE_Rental_NRC",
    "dis_additional_IP_ARC",
    "dis_burst_per_MB_price_ARC",
    "dis_lm_arc_bw_onrf",
    "dis_lm_arc_bw_onwl",
    "dis_lm_arc_bw_prov_ofrf",
    "dis_lm_nrc_bw_onrf",
    "dis_lm_nrc_bw_onwl",
    "dis_lm_nrc_bw_prov_ofrf",
    "dis_lm_nrc_inbldg_onwl",
    "dis_lm_nrc_mast_ofrf",
    "dis_lm_nrc_mast_onrf",
    "dis_lm_nrc_nerental_onwl",
    "dis_lm_nrc_ospcapex_onwl ",
    "dis_lm_nrc_mux_onwl",
    "dis_port_arc",
    "dis_port_nrc",
    "dis_port_mrc",
    "error_code",
    "error_flag",
    "error_msg",
    "ip_address_arrangement",
    "ipv4_address_pool_size",
    "ipv6_address_pool_size",
    "lm_nrc_inbldg_onwl",
    "local_loop_interface",
    "opportunityTerm",
    "osp_dist_meters",
    "product_name",
    "resp_city",
    "site_id",
    "solution_type",
    "sp_lm_arc_bw_onrf",
    "sp_lm_arc_bw_onwl",
    "sp_lm_arc_bw_prov_ofrf",
    "sp_lm_nrc_bw_onrf",
    "sp_lm_nrc_bw_onwl",
    "sp_lm_nrc_bw_prov_ofrf",
    "sp_lm_nrc_inbldg_onwl",
    "sp_lm_nrc_mast_ofrf",
    "sp_lm_nrc_nerental_onwl",
    "sp_lm_nrc_ospcapex_onwl",
    "topology",
    "version",
    "dis_CPE_Rental_ARC",
    "dis_burst_mb_arc",
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
    "sp_CPE_Install_NRC",
    "dis_lm_mrc",
    "dis_lm_nrc",
    "dis_xconnect_mrc",
    "dis_xconnect_nrc",
    "dis_CPE_support_charges",
    "dis_CPE_recovery",
    "dis_CPE_SFP_charges",
    "dis_CPE_customs_local_taxes",
    "dis_CPE_logistics_cost",
    "dis_CPE_management"
    

})
public class DiscountResult {

    @JsonProperty("Mast_3KM_avg_mast_ht")
    private String mast3KMAvgMastHt;
    @JsonProperty("Orch_Category")
    private String orchCategory;
    @JsonProperty("Orch_Connection")
    private String orchConnection;
    @JsonProperty("Orch_LM_Type")
    private String orchLMType;
    @JsonProperty("avg_mast_ht")
    private String avgMastHt;
    @JsonProperty("burstable_bw")
    private String burstableBw;
    @JsonProperty("bw_mbps")
    private String bwMbps;
    @JsonProperty("chargeable_distance_km")
    private String chargeableDistanceKm;
    @JsonProperty("connection_type")
    private String connectionType;
    @JsonProperty("cpe_management_type")
    private String cpeManagementType;
    @JsonProperty("cpe_supply_type")
    private String cpeSupplyType;
    @JsonProperty("cpe_variant")
    private String cpeVariant;
    @JsonProperty("dis_CPE_Install_NRC")
    private String disCPEInstallNRC;
    @JsonProperty("dis_CPE_Management_ARC")
    private String disCPEManagementARC;
    @JsonProperty("dis_CPE_Outright_NRC")
    private String disCPEOutrightNRC;
    @JsonProperty("dis_CPE_Rental_NRC")
    private String disCPERentalNRC;
    @JsonProperty("dis_CPE_Rental_ARC")
    private String disCPERentalARC;
    @JsonProperty("dis_additional_IP_ARC")
    private String disAdditionalIPARC;
    @JsonProperty("dis_burst_per_MB_price_ARC")
    private String disBurstPerMBPriceARC;
    @JsonProperty("dis_burst_mb_arc")
    private String disBurstMbArc;
    @JsonProperty("dis_lm_arc_bw_onrf")
    private String disLmArcBwOnrf;
    @JsonProperty("dis_lm_arc_bw_onwl")
    private String disLmArcBwOnwl;
    @JsonProperty("dis_lm_arc_bw_offwl")
    private String disLmArcBwOffwl;
    @JsonProperty("dis_lm_otc_nrc_installation_offwl")
  	private String disLmOtcNrcInstallationOffwl;
  	@JsonProperty("dis_lm_otc_modem_charges_offwl")
  	private String disLmOtcModemChargesOffwl;
  	@JsonProperty("dis_lm_arc_modem_charges_offwl")
  	private String disLmArcModemChargesOffwl;
    @JsonProperty("dis_lm_arc_bw_prov_ofrf")
    private String disLmArcBwProvOfrf;
    @JsonProperty("dis_lm_nrc_bw_onrf")
    private String disLmNrcBwOnrf;
    @JsonProperty("dis_lm_nrc_bw_onwl")
    private String disLmNrcBwOnwl;
    @JsonProperty("dis_lm_nrc_bw_prov_ofrf")
    private String disLmNrcBwProvOfrf;
    @JsonProperty("dis_lm_nrc_inbldg_onwl")
    private String disLmNrcInbldgOnwl;
    @JsonProperty("dis_lm_nrc_mast_ofrf")
    private String disLmNrcMastOfrf;
    @JsonProperty("dis_lm_nrc_mast_onrf")
    private String disLmNrcMastOnrf;
    @JsonProperty("dis_lm_nrc_nerental_onwl")
    private String disLmNrcNerentalOnwl;
    @JsonProperty("dis_lm_nrc_ospcapex_onwl ")
    private String disLmNrcOspcapexOnwl;
    @JsonProperty("dis_lm_nrc_mux_onwl")
    private String disLmNrcMuxOnwl;
    @JsonProperty("dis_port_arc")
    private String disPortArc;
    @JsonProperty("dis_port_nrc")
    private String disPortNrc;
    @JsonProperty("dis_port_mrc")
    private String disPortMrc;
    @JsonProperty("error_code")
    private String errorCode;
    @JsonProperty("error_flag")
    private Integer errorFlag;
    @JsonProperty("error_msg")
    private String errorMsg;
    @JsonProperty("ip_address_arrangement")
    private String ipAddressArrangement;
    @JsonProperty("ipv4_address_pool_size")
    private String ipv4AddressPoolSize;
    @JsonProperty("ipv6_address_pool_size")
    private String ipv6AddressPoolSize;
    @JsonProperty("lm_nrc_inbldg_onwl")
    private String lmNrcInbldgOnwl;
    @JsonProperty("local_loop_interface")
    private String localLoopInterface;
    @JsonProperty("opportunityTerm")
    private String opportunityTerm;
    @JsonProperty("osp_dist_meters")
    private String ospDistMeters;
    @JsonProperty("product_name")
    private String productName;
    @JsonProperty("resp_city")
    private String respCity;
    @JsonProperty("site_id")
    private String siteId;
    @JsonProperty("solution_type")
    private String solutionType;
    @JsonProperty("sp_lm_arc_bw_onrf")
    private String spLmArcBwOnrf;
    @JsonProperty("sp_lm_arc_bw_onwl")
    private String spLmArcBwOnwl;
    @JsonProperty("sp_lm_arc_bw_prov_ofrf")
    private String spLmArcBwProvOfrf;
    @JsonProperty("sp_lm_nrc_bw_onrf")
    private String spLmNrcBwOnrf;
    @JsonProperty("sp_lm_nrc_bw_onwl")
    private String spLmNrcBwOnwl;
    @JsonProperty("sp_lm_nrc_bw_prov_ofrf")
    private String spLmNrcBwProvOfrf;
    @JsonProperty("sp_lm_nrc_inbldg_onwl")
    private String spLmNrcInbldgOnwl;
    @JsonProperty("sp_lm_nrc_mast_ofrf")
    private String spLmNrcMastOfrf;
    @JsonProperty("sp_lm_nrc_nerental_onwl")
    private String spLmNrcNerentalOnwl;
    @JsonProperty("sp_lm_nrc_ospcapex_onwl")
    private String spLmNrcOspcapexOnwl;
    @JsonProperty("topology")
    private String topology;
    @JsonProperty("version")
    private Integer version;
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
    @JsonProperty("sp_CPE_Install_NRC")
    private String spCPEInstallNRC;
    @JsonProperty("dis_lm_mrc")
    private String disLmMrc;
    @JsonProperty("dis_lm_nrc")
    private String disLmNrc;
    @JsonProperty("dis_xconnect_mrc")
    private String disXconnectMrc;
    @JsonProperty("dis_xconnect_nrc")
    private String disXconnectNrc;
    @JsonProperty("dis_CPE_support_charges")
    private String disCPESupportCharges;
    @JsonProperty("dis_CPE_recovery")
    private String disCPERecovery;
    @JsonProperty("dis_CPE_SFP_charges")
    private String disCPESFPCharges;
    @JsonProperty("dis_CPE_customs_local_taxes")
    private String disCPECustomsLocalTaxes;
    @JsonProperty("dis_CPE_logistics_cost")
    private String disCPELogisticsCost;
    @JsonProperty("dis_CPE_management")
    private String disCPEManagement;
   


    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    
    
    public String getDisCPERentalARC() {
		return disCPERentalARC;
	}

	public void setDisCPERentalARC(String disCPERentalARC) {
		this.disCPERentalARC = disCPERentalARC;
	}

	@JsonProperty("Mast_3KM_avg_mast_ht")
    public String getMast3KMAvgMastHt() {
        return mast3KMAvgMastHt;
    }

    @JsonProperty("Mast_3KM_avg_mast_ht")
    public void setMast3KMAvgMastHt(String mast3KMAvgMastHt) {
        this.mast3KMAvgMastHt = mast3KMAvgMastHt;
    }

    @JsonProperty("Orch_Category")
    public String getOrchCategory() {
        return orchCategory;
    }

    @JsonProperty("Orch_Category")
    public void setOrchCategory(String orchCategory) {
        this.orchCategory = orchCategory;
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

    @JsonProperty("avg_mast_ht")
    public String getAvgMastHt() {
        return avgMastHt;
    }

    @JsonProperty("avg_mast_ht")
    public void setAvgMastHt(String avgMastHt) {
        this.avgMastHt = avgMastHt;
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

    @JsonProperty("chargeable_distance_km")
    public String getChargeableDistanceKm() {
        return chargeableDistanceKm;
    }

    @JsonProperty("chargeable_distance_km")
    public void setChargeableDistanceKm(String chargeableDistanceKm) {
        this.chargeableDistanceKm = chargeableDistanceKm;
    }

    @JsonProperty("connection_type")
    public String getConnectionType() {
        return connectionType;
    }

    @JsonProperty("connection_type")
    public void setConnectionType(String connectionType) {
        this.connectionType = connectionType;
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

    @JsonProperty("dis_CPE_Install_NRC")
    public String getDisCPEInstallNRC() {
        return disCPEInstallNRC;
    }

    @JsonProperty("dis_CPE_Install_NRC")
    public void setDisCPEInstallNRC(String disCPEInstallNRC) {
        this.disCPEInstallNRC = disCPEInstallNRC;
    }

    @JsonProperty("dis_CPE_Management_ARC")
    public String getDisCPEManagementARC() {
        return disCPEManagementARC;
    }

    @JsonProperty("dis_CPE_Management_ARC")
    public void setDisCPEManagementARC(String disCPEManagementARC) {
        this.disCPEManagementARC = disCPEManagementARC;
    }

    @JsonProperty("dis_CPE_Outright_NRC")
    public String getDisCPEOutrightNRC() {
        return disCPEOutrightNRC;
    }

    @JsonProperty("dis_CPE_Outright_NRC")
    public void setDisCPEOutrightNRC(String disCPEOutrightNRC) {
        this.disCPEOutrightNRC = disCPEOutrightNRC;
    }

    @JsonProperty("dis_CPE_Rental_NRC")
    public String getDisCPERentalNRC() {
        return disCPERentalNRC;
    }

    @JsonProperty("dis_CPE_Rental_NRC")
    public void setDisCPERentalNRC(String disCPERentalNRC) {
        this.disCPERentalNRC = disCPERentalNRC;
    }

    @JsonProperty("dis_additional_IP_ARC")
    public String getDisAdditionalIPARC() {
        return disAdditionalIPARC;
    }

    @JsonProperty("dis_additional_IP_ARC")
    public void setDisAdditionalIPARC(String disAdditionalIPARC) {
        this.disAdditionalIPARC = disAdditionalIPARC;
    }

    @JsonProperty("dis_burst_per_MB_price_ARC")
    public String getDisBurstPerMBPriceARC() {
        return disBurstPerMBPriceARC;
    }

    @JsonProperty("dis_burst_per_MB_price_ARC")
    public void setDisBurstPerMBPriceARC(String disBurstPerMBPriceARC) {
        this.disBurstPerMBPriceARC = disBurstPerMBPriceARC;
    }

    @JsonProperty("dis_lm_arc_bw_onrf")
    public String getDisLmArcBwOnrf() {
        return disLmArcBwOnrf;
    }

    @JsonProperty("dis_lm_arc_bw_onrf")
    public void setDisLmArcBwOnrf(String disLmArcBwOnrf) {
        this.disLmArcBwOnrf = disLmArcBwOnrf;
    }

    @JsonProperty("dis_lm_arc_bw_onwl")
    public String getDisLmArcBwOnwl() {
        return disLmArcBwOnwl;
    }

    @JsonProperty("dis_lm_arc_bw_onwl")
    public void setDisLmArcBwOnwl(String disLmArcBwOnwl) {
        this.disLmArcBwOnwl = disLmArcBwOnwl;
    }

    @JsonProperty("dis_lm_arc_bw_prov_ofrf")
    public String getDisLmArcBwProvOfrf() {
        return disLmArcBwProvOfrf;
    }

    @JsonProperty("dis_lm_arc_bw_prov_ofrf")
    public void setDisLmArcBwProvOfrf(String disLmArcBwProvOfrf) {
        this.disLmArcBwProvOfrf = disLmArcBwProvOfrf;
    }

    @JsonProperty("dis_lm_nrc_bw_onrf")
    public String getDisLmNrcBwOnrf() {
        return disLmNrcBwOnrf;
    }

    @JsonProperty("dis_lm_nrc_bw_onrf")
    public void setDisLmNrcBwOnrf(String disLmNrcBwOnrf) {
        this.disLmNrcBwOnrf = disLmNrcBwOnrf;
    }

    @JsonProperty("dis_lm_nrc_bw_onwl")
    public String getDisLmNrcBwOnwl() {
        return disLmNrcBwOnwl;
    }

    @JsonProperty("dis_lm_nrc_bw_onwl")
    public void setDisLmNrcBwOnwl(String disLmNrcBwOnwl) {
        this.disLmNrcBwOnwl = disLmNrcBwOnwl;
    }

    @JsonProperty("dis_lm_nrc_bw_prov_ofrf")
    public String getDisLmNrcBwProvOfrf() {
        return disLmNrcBwProvOfrf;
    }

    @JsonProperty("dis_lm_nrc_bw_prov_ofrf")
    public void setDisLmNrcBwProvOfrf(String disLmNrcBwProvOfrf) {
        this.disLmNrcBwProvOfrf = disLmNrcBwProvOfrf;
    }

    @JsonProperty("dis_lm_nrc_inbldg_onwl")
    public String getDisLmNrcInbldgOnwl() {
        return disLmNrcInbldgOnwl;
    }

    @JsonProperty("dis_lm_nrc_inbldg_onwl")
    public void setDisLmNrcInbldgOnwl(String disLmNrcInbldgOnwl) {
        this.disLmNrcInbldgOnwl = disLmNrcInbldgOnwl;
    }

    @JsonProperty("dis_lm_nrc_mast_ofrf")
    public String getDisLmNrcMastOfrf() {
        return disLmNrcMastOfrf;
    }

    @JsonProperty("dis_lm_nrc_mast_ofrf")
    public void setDisLmNrcMastOfrf(String disLmNrcMastOfrf) {
        this.disLmNrcMastOfrf = disLmNrcMastOfrf;
    }

    @JsonProperty("dis_lm_nrc_mast_onrf")
    public String getDisLmNrcMastOnrf() {
        return disLmNrcMastOnrf;
    }

    @JsonProperty("dis_lm_nrc_mast_onrf")
    public void setDisLmNrcMastOnrf(String disLmNrcMastOnrf) {
        this.disLmNrcMastOnrf = disLmNrcMastOnrf;
    }

    @JsonProperty("dis_lm_nrc_nerental_onwl")
    public String getDisLmNrcNerentalOnwl() {
        return disLmNrcNerentalOnwl;
    }

    @JsonProperty("dis_lm_nrc_nerental_onwl")
    public void setDisLmNrcNerentalOnwl(String disLmNrcNerentalOnwl) {
        this.disLmNrcNerentalOnwl = disLmNrcNerentalOnwl;
    }

    

    public String getDisLmNrcOspcapexOnwl() {
		return disLmNrcOspcapexOnwl;
	}

	public void setDisLmNrcOspcapexOnwl(String disLmNrcOspcapexOnwl) {
		this.disLmNrcOspcapexOnwl = disLmNrcOspcapexOnwl;
	}

	@JsonProperty("dis_port_arc")
    public String getDisPortArc() {
        return disPortArc;
    }

    @JsonProperty("dis_port_arc")
    public void setDisPortArc(String disPortArc) {
        this.disPortArc = disPortArc;
    }

    @JsonProperty("dis_port_nrc")
    public String getDisPortNrc() {
        return disPortNrc;
    }

    @JsonProperty("dis_port_mrc")
    public void setDisPortMrc(String disPortMrc) {
        this.disPortMrc = disPortMrc;
    }
    
    @JsonProperty("dis_port_mrc")
    public String getDisPortMrc() {
        return disPortMrc;
    }

    @JsonProperty("dis_port_nrc")
    public void setDisPortNrc(String disPortNrc) {
        this.disPortNrc = disPortNrc;
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
    public Integer getErrorFlag() {
        return errorFlag;
    }

    @JsonProperty("error_flag")
    public void setErrorFlag(Integer errorFlag) {
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

    @JsonProperty("lm_nrc_inbldg_onwl")
    public String getLmNrcInbldgOnwl() {
        return lmNrcInbldgOnwl;
    }

    @JsonProperty("lm_nrc_inbldg_onwl")
    public void setLmNrcInbldgOnwl(String lmNrcInbldgOnwl) {
        this.lmNrcInbldgOnwl = lmNrcInbldgOnwl;
    }

    @JsonProperty("local_loop_interface")
    public String getLocalLoopInterface() {
        return localLoopInterface;
    }

    @JsonProperty("local_loop_interface")
    public void setLocalLoopInterface(String localLoopInterface) {
        this.localLoopInterface = localLoopInterface;
    }

    @JsonProperty("opportunityTerm")
    public String getOpportunityTerm() {
        return opportunityTerm;
    }

    @JsonProperty("opportunityTerm")
    public void setOpportunityTerm(String opportunityTerm) {
        this.opportunityTerm = opportunityTerm;
    }

    @JsonProperty("osp_dist_meters")
    public String getOspDistMeters() {
        return ospDistMeters;
    }

    @JsonProperty("osp_dist_meters")
    public void setOspDistMeters(String ospDistMeters) {
        this.ospDistMeters = ospDistMeters;
    }

    @JsonProperty("product_name")
    public String getProductName() {
        return productName;
    }

    @JsonProperty("product_name")
    public void setProductName(String productName) {
        this.productName = productName;
    }

    @JsonProperty("resp_city")
    public String getRespCity() {
        return respCity;
    }

    @JsonProperty("resp_city")
    public void setRespCity(String respCity) {
        this.respCity = respCity;
    }

    @JsonProperty("site_id")
    public String getSiteId() {
        return siteId;
    }

    @JsonProperty("site_id")
    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    @JsonProperty("solution_type")
    public String getSolutionType() {
        return solutionType;
    }

    @JsonProperty("solution_type")
    public void setSolutionType(String solutionType) {
        this.solutionType = solutionType;
    }

    @JsonProperty("sp_lm_arc_bw_onrf")
    public String getSpLmArcBwOnrf() {
        return spLmArcBwOnrf;
    }

    @JsonProperty("sp_lm_arc_bw_onrf")
    public void setSpLmArcBwOnrf(String spLmArcBwOnrf) {
        this.spLmArcBwOnrf = spLmArcBwOnrf;
    }

    @JsonProperty("sp_lm_arc_bw_onwl")
    public String getSpLmArcBwOnwl() {
        return spLmArcBwOnwl;
    }

    @JsonProperty("sp_lm_arc_bw_onwl")
    public void setSpLmArcBwOnwl(String spLmArcBwOnwl) {
        this.spLmArcBwOnwl = spLmArcBwOnwl;
    }

    @JsonProperty("sp_lm_arc_bw_prov_ofrf")
    public String getSpLmArcBwProvOfrf() {
        return spLmArcBwProvOfrf;
    }

    @JsonProperty("sp_lm_arc_bw_prov_ofrf")
    public void setSpLmArcBwProvOfrf(String spLmArcBwProvOfrf) {
        this.spLmArcBwProvOfrf = spLmArcBwProvOfrf;
    }

    @JsonProperty("sp_lm_nrc_bw_onrf")
    public String getSpLmNrcBwOnrf() {
        return spLmNrcBwOnrf;
    }

    @JsonProperty("sp_lm_nrc_bw_onrf")
    public void setSpLmNrcBwOnrf(String spLmNrcBwOnrf) {
        this.spLmNrcBwOnrf = spLmNrcBwOnrf;
    }

    @JsonProperty("sp_lm_nrc_bw_onwl")
    public String getSpLmNrcBwOnwl() {
        return spLmNrcBwOnwl;
    }

    @JsonProperty("sp_lm_nrc_bw_onwl")
    public void setSpLmNrcBwOnwl(String spLmNrcBwOnwl) {
        this.spLmNrcBwOnwl = spLmNrcBwOnwl;
    }

    @JsonProperty("sp_lm_nrc_bw_prov_ofrf")
    public String getSpLmNrcBwProvOfrf() {
        return spLmNrcBwProvOfrf;
    }

    @JsonProperty("sp_lm_nrc_bw_prov_ofrf")
    public void setSpLmNrcBwProvOfrf(String spLmNrcBwProvOfrf) {
        this.spLmNrcBwProvOfrf = spLmNrcBwProvOfrf;
    }

    @JsonProperty("sp_lm_nrc_inbldg_onwl")
    public String getSpLmNrcInbldgOnwl() {
        return spLmNrcInbldgOnwl;
    }

    @JsonProperty("sp_lm_nrc_inbldg_onwl")
    public void setSpLmNrcInbldgOnwl(String spLmNrcInbldgOnwl) {
        this.spLmNrcInbldgOnwl = spLmNrcInbldgOnwl;
    }

    @JsonProperty("sp_lm_nrc_mast_ofrf")
    public String getSpLmNrcMastOfrf() {
        return spLmNrcMastOfrf;
    }

    @JsonProperty("sp_lm_nrc_mast_ofrf")
    public void setSpLmNrcMastOfrf(String spLmNrcMastOfrf) {
        this.spLmNrcMastOfrf = spLmNrcMastOfrf;
    }

    @JsonProperty("sp_lm_nrc_nerental_onwl")
    public String getSpLmNrcNerentalOnwl() {
        return spLmNrcNerentalOnwl;
    }

    @JsonProperty("sp_lm_nrc_nerental_onwl")
    public void setSpLmNrcNerentalOnwl(String spLmNrcNerentalOnwl) {
        this.spLmNrcNerentalOnwl = spLmNrcNerentalOnwl;
    }

    @JsonProperty("sp_lm_nrc_ospcapex_onwl")
    public String getSpLmNrcOspcapexOnwl() {
        return spLmNrcOspcapexOnwl;
    }

    @JsonProperty("sp_lm_nrc_ospcapex_onwl")
    public void setSpLmNrcOspcapexOnwl(String spLmNrcOspcapexOnwl) {
        this.spLmNrcOspcapexOnwl = spLmNrcOspcapexOnwl;
    }

    @JsonProperty("topology")
    public String getTopology() {
        return topology;
    }

    @JsonProperty("topology")
    public void setTopology(String topology) {
        this.topology = topology;
    }

    @JsonProperty("version")
    public Integer getVersion() {
        return version;
    }

    @JsonProperty("version")
    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getDisLmNrcMuxOnwl() {
		return disLmNrcMuxOnwl;
	}

	public void setDisLmNrcMuxOnwl(String disLmNrcMuxOnwl) {
		this.disLmNrcMuxOnwl = disLmNrcMuxOnwl;
	}

	@JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

	public String getDisBurstMbArc() {
		return disBurstMbArc;
	}

	public void setDisBurstMbArc(String disBurstMbArc) {
		this.disBurstMbArc = disBurstMbArc;
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

	@JsonProperty("sp_CPE_Install_NRC")
	public String getSpCPEInstallNRC() {
		return spCPEInstallNRC;
	}
	@JsonProperty("sp_CPE_Install_NRC")
	public void setSpCPEInstallNRC(String spCPEInstallNRC) {
		this.spCPEInstallNRC = spCPEInstallNRC;
	}
	@JsonProperty("dis_lm_mrc")
	public String getDisLmMrc() {
		return disLmMrc;
	}
	@JsonProperty("dis_lm_mrc")
	public void setDisLmMrc(String disLmMrc) {
		this.disLmMrc = disLmMrc;
	}
	@JsonProperty("dis_lm_nrc")
	public String getDisLmNrc() {
		return disLmNrc;
	}
	@JsonProperty("dis_lm_nrc")
	public void setDisLmNrc(String disLmNrc) {
		this.disLmNrc = disLmNrc;
	}
	
	@JsonProperty("dis_xconnect_mrc")
	public String getDisXconnectMrc() {
		return disXconnectMrc;
	}

	@JsonProperty("dis_xconnect_mrc")
	public void setDisXconnectMrc(String disXconnectMrc) {
		this.disXconnectMrc = disXconnectMrc;
	}

	@JsonProperty("dis_xconnect_nrc")
	public String getDisXconnectNrc() {
		return disXconnectNrc;
	}

	@JsonProperty("dis_xconnect_nrc")
	public void setDisXconnectNrc(String disXconnectNrc) {
		this.disXconnectNrc = disXconnectNrc;
	}

	@JsonProperty("dis_CPE_support_charges")
	public String getDisCPESupportCharges() {
		return disCPESupportCharges;
	}
	@JsonProperty("dis_CPE_support_charges")
	public void setDisCPESupportCharges(String disCPESupportCharges) {
		this.disCPESupportCharges = disCPESupportCharges;
	}
	@JsonProperty("dis_CPE_recovery")
	public String getDisCPERecovery() {
		return disCPERecovery;
	}
	@JsonProperty("dis_CPE_recovery")
	public void setDisCPERecovery(String disCPERecovery) {
		this.disCPERecovery = disCPERecovery;
	}
	@JsonProperty("dis_CPE_SFP_charges")
	public String getDisCPESFPCharges() {
		return disCPESFPCharges;
	}
	@JsonProperty("dis_CPE_SFP_charges")
	public void setDisCPESFPCharges(String disCPESFPCharges) {
		this.disCPESFPCharges = disCPESFPCharges;
	}
	@JsonProperty("dis_CPE_customs_local_taxes")
	public String getDisCPECustomsLocalTaxes() {
		return disCPECustomsLocalTaxes;
	}
	@JsonProperty("dis_CPE_customs_local_taxes")
	public void setDisCPECustomsLocalTaxes(String disCPECustomsLocalTaxes) {
		this.disCPECustomsLocalTaxes = disCPECustomsLocalTaxes;
	}
	@JsonProperty("dis_CPE_logistics_cost")
	public String getDisCPELogisticsCost() {
		return disCPELogisticsCost;
	}
	@JsonProperty("dis_CPE_logistics_cost")
	public void setDisCPELogisticsCost(String disCPELogisticsCost) {
		this.disCPELogisticsCost = disCPELogisticsCost;
	}

	@JsonProperty("dis_CPE_management")
	public String getDisCPEManagement() {
		return disCPEManagement;
	}

	@JsonProperty("dis_CPE_management")
	public void setDisCPEManagement(String disCPEManagement) {
		this.disCPEManagement = disCPEManagement;
	}

	@JsonProperty("dis_lm_arc_bw_offwl")
	public String getDisLmArcBwOffwl() {
		return disLmArcBwOffwl;
	}
	@JsonProperty("dis_lm_arc_bw_offwl")
	public void setDisLmArcBwOffwl(String disLmArcBwOffwl) {
		this.disLmArcBwOffwl = disLmArcBwOffwl;
	}
	@JsonProperty("dis_lm_otc_nrc_installation_offwl")
	public String getDisLmOtcNrcInstallationOffwl() {
		return disLmOtcNrcInstallationOffwl;
	}
	@JsonProperty("dis_lm_otc_nrc_installation_offwl")
	public void setDisLmOtcNrcInstallationOffwl(String disLmOtcNrcInstallationOffwl) {
		this.disLmOtcNrcInstallationOffwl = disLmOtcNrcInstallationOffwl;
	}
	@JsonProperty("dis_lm_otc_modem_charges_offwl")
	public String getDisLmOtcModemChargesOffwl() {
		return disLmOtcModemChargesOffwl;
	}
	@JsonProperty("dis_lm_otc_modem_charges_offwl")
	public void setDisLmOtcModemChargesOffwl(String disLmOtcModemChargesOffwl) {
		this.disLmOtcModemChargesOffwl = disLmOtcModemChargesOffwl;
	}
	@JsonProperty("dis_lm_arc_modem_charges_offwl")
	public String getDisLmArcModemChargesOffwl() {
		return disLmArcModemChargesOffwl;
	}
	@JsonProperty("dis_lm_arc_modem_charges_offwl")
	public void setDisLmArcModemChargesOffwl(String disLmArcModemChargesOffwl) {
		this.disLmArcModemChargesOffwl = disLmArcModemChargesOffwl;
	}
	
	

    
   
}

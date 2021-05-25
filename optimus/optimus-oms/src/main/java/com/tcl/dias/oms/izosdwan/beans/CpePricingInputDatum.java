package com.tcl.dias.oms.izosdwan.beans;

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
    "account_id_with_18_digit",
    "bw_mbps",
    "country",
    "cpe_supply_type",
    "cpe_variant",
    "opportunity_term",
    "resp_city",
    "siteFlag",
    "product_solution",
    "local_loop_bw",
    "SFP",
    "NMC",
    "SFP_Plus",
    "Rack_Mount",
    "Power_Cord",
    "License_Name",
    "License_Quantity",
    "CPE_Quantity",
    "SFP_Quantity",
    "NMC_Quantity",
    "SFP_Plus_Quantity",
    "HA_License",
    "Add_On_Licenses",
    "Underlay",
    "License_Supply_Type",
    "Markup_pct",
    "CPE_HW_charges_cost",
    "CPE_customs_tax_cost",
    "CPE_local_tax_cost",
    "CPE_delivery_cost",
    "CPE_maintenance_cost",
    "CPE_installation_charges_cost",
    "Total_CPE_Cost",
    "license_mrc_cost",
    "license_arc_cost",
    "license_nrc_cost",
    "CPE_UnitCost_USD",
    "CPE_Cost",
    "SFP_UnitCost_USD",
    "SFP_Cost",
    "SFP_Plus_UnitCost_USD",
    "SFP_Plus_Cost",
    "NMC_UnitCost_USD",
    "NMC_Cost",
    "Rack_Mount_UnitCost_USD",
    "Power_Cord_UnitCost_USD",
    "CPE_Customs_Fee_pct",
    "CPE_Local_Fee_pct","CPE_ARC","CPE_NRC"})

public class CpePricingInputDatum {

    @JsonProperty("site_id")
    private String siteId;
    @JsonProperty("account_id_with_18_digit")
    private String accountIdWith18Digit;
    @JsonProperty("bw_mbps")
    private Double bwMbps;
    @JsonProperty("country")
    private String country;
    @JsonProperty("cpe_supply_type")
    private String cpeSupplyType;
    @JsonProperty("cpe_variant")
    private String cpeVariant;
    @JsonProperty("opportunity_term")
    private Integer opportunityTerm;
    @JsonProperty("resp_city")
    private String respCity;
    @JsonProperty("siteFlag")
    private String siteFlag;
    @JsonProperty("product_solution")
    private String productSolution;
    @JsonProperty("local_loop_bw")
    private Double localLoopBw;
    @JsonProperty("SFP")
    private String sFP;
    @JsonProperty("NMC")
    private String nMC;
    @JsonProperty("SFP_Plus")
    private String sFPPlus;
    @JsonProperty("Rack_Mount")
    private String rackMount;
    @JsonProperty("Power_Cord")
    private String powerCord;
    @JsonProperty("License_Name")
    private String licenseName;
    @JsonProperty("License_Quantity")
    private Double licenseQuantity;
    @JsonProperty("CPE_Quantity")
    private Double cPEQuantity;
    @JsonProperty("SFP_Quantity")
    private Double sFPQuantity;
    @JsonProperty("NMC_Quantity")
    private Double nMCQuantity;
    @JsonProperty("SFP_Plus_Quantity")
    private Double sFPPlusQuantity;
    @JsonProperty("HA_License")
    private String hALicense;
    @JsonProperty("Add_On_Licenses")
    private String addOnLicenses;
    @JsonProperty("Underlay")
    private String underlay;
    @JsonProperty("License_Supply_Type")
    private String licenseSupplyType;
    @JsonProperty("Markup_pct")
    private Double markupPct;
    @JsonProperty("CPE_HW_charges_cost")
    private Double cPEHWChargesCost;
    @JsonProperty("CPE_customs_tax_cost")
    private Double cPECustomsTaxCost;
    @JsonProperty("CPE_local_tax_cost")
    private Double cPELocalTaxCost;
    @JsonProperty("CPE_delivery_cost")
    private Double cPEDeliveryCost;
    @JsonProperty("CPE_maintenance_cost")
    private Double cPEMaintenanceCost;
    @JsonProperty("CPE_installation_charges_cost")
    private Double cPEInstallationChargesCost;
    @JsonProperty("Total_CPE_Cost")
    private Double totalCPECost;
    @JsonProperty("license_mrc_cost")
    private Double licenseMrcCost;
    @JsonProperty("license_arc_cost")
    private Double licenseArcCost;
    @JsonProperty("license_nrc_cost")
    private Double licenseNrcCost;
    @JsonProperty("CPE_UnitCost_USD")
    private Double cPEUnitCostUSD;
    @JsonProperty("CPE_Cost")
    private Double cPECost;
    @JsonProperty("SFP_UnitCost_USD")
    private Double sFPUnitCostUSD;
    @JsonProperty("SFP_Cost")
    private Double sFPCost;
    @JsonProperty("SFP_Plus_UnitCost_USD")
    private Double sFPPlusUnitCostUSD;
    @JsonProperty("SFP_Plus_Cost")
    private Double sFPPlusCost;
    @JsonProperty("NMC_UnitCost_USD")
    private Double nMCUnitCostUSD;
    @JsonProperty("NMC_Cost")
    private Double nMCCost;
    @JsonProperty("Rack_Mount_UnitCost_USD")
    private Double rackMountUnitCostUSD;
    @JsonProperty("Power_Cord_UnitCost_USD")
    private Double powerCordUnitCostUSD;
    @JsonProperty("CPE_Customs_Fee_pct")
    private Double cPECustomsFeePct;
    @JsonProperty("CPE_Local_Fee_pct")
    private Double cPELocalFeePct;
    @JsonProperty("CPE_ARC")
    private Double cpeArc;
    @JsonProperty("CPE_NRC")
    private Double cpeNrc;
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

    @JsonProperty("account_id_with_18_digit")
    public String getAccountIdWith18Digit() {
        return accountIdWith18Digit;
    }

    @JsonProperty("account_id_with_18_digit")
    public void setAccountIdWith18Digit(String accountIdWith18Digit) {
        this.accountIdWith18Digit = accountIdWith18Digit;
    }

    @JsonProperty("bw_mbps")
    public Double getBwMbps() {
        return bwMbps;
    }

    @JsonProperty("bw_mbps")
    public void setBwMbps(Double bwMbps) {
        this.bwMbps = bwMbps;
    }

    @JsonProperty("country")
    public String getCountry() {
        return country;
    }

    @JsonProperty("country")
    public void setCountry(String country) {
        this.country = country;
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

    @JsonProperty("opportunity_term")
    public Integer getOpportunityTerm() {
        return opportunityTerm;
    }

    @JsonProperty("opportunity_term")
    public void setOpportunityTerm(Integer opportunityTerm) {
        this.opportunityTerm = opportunityTerm;
    }

    @JsonProperty("resp_city")
    public String getRespCity() {
        return respCity;
    }

    @JsonProperty("resp_city")
    public void setRespCity(String respCity) {
        this.respCity = respCity;
    }

    @JsonProperty("siteFlag")
    public String getSiteFlag() {
        return siteFlag;
    }

    @JsonProperty("siteFlag")
    public void setSiteFlag(String siteFlag) {
        this.siteFlag = siteFlag;
    }

    @JsonProperty("product_solution")
    public String getProductSolution() {
        return productSolution;
    }

    @JsonProperty("product_solution")
    public void setProductSolution(String productSolution) {
        this.productSolution = productSolution;
    }

    @JsonProperty("local_loop_bw")
    public Double getLocalLoopBw() {
        return localLoopBw;
    }

    @JsonProperty("local_loop_bw")
    public void setLocalLoopBw(Double localLoopBw) {
        this.localLoopBw = localLoopBw;
    }

    @JsonProperty("SFP")
    public String getSFP() {
        return sFP;
    }

    @JsonProperty("SFP")
    public void setSFP(String sFP) {
        this.sFP = sFP;
    }

    @JsonProperty("NMC")
    public String getNMC() {
        return nMC;
    }

    @JsonProperty("NMC")
    public void setNMC(String nMC) {
        this.nMC = nMC;
    }

    @JsonProperty("SFP_Plus")
    public String getSFPPlus() {
        return sFPPlus;
    }

    @JsonProperty("SFP_Plus")
    public void setSFPPlus(String sFPPlus) {
        this.sFPPlus = sFPPlus;
    }

    @JsonProperty("Rack_Mount")
    public String getRackMount() {
        return rackMount;
    }

    @JsonProperty("Rack_Mount")
    public void setRackMount(String rackMount) {
        this.rackMount = rackMount;
    }

    @JsonProperty("Power_Cord")
    public String getPowerCord() {
        return powerCord;
    }

    @JsonProperty("Power_Cord")
    public void setPowerCord(String powerCord) {
        this.powerCord = powerCord;
    }

    @JsonProperty("License_Name")
    public String getLicenseName() {
        return licenseName;
    }

    @JsonProperty("License_Name")
    public void setLicenseName(String licenseName) {
        this.licenseName = licenseName;
    }

    @JsonProperty("License_Quantity")
    public Double getLicenseQuantity() {
        return licenseQuantity;
    }

    @JsonProperty("License_Quantity")
    public void setLicenseQuantity(Double licenseQuantity) {
        this.licenseQuantity = licenseQuantity;
    }

    @JsonProperty("CPE_Quantity")
    public Double getCPEQuantity() {
        return cPEQuantity;
    }

    @JsonProperty("CPE_Quantity")
    public void setCPEQuantity(Double cPEQuantity) {
        this.cPEQuantity = cPEQuantity;
    }

    @JsonProperty("SFP_Quantity")
    public Double getSFPQuantity() {
        return sFPQuantity;
    }

    @JsonProperty("SFP_Quantity")
    public void setSFPQuantity(Double sFPQuantity) {
        this.sFPQuantity = sFPQuantity;
    }

    @JsonProperty("NMC_Quantity")
    public Double getNMCQuantity() {
        return nMCQuantity;
    }

    @JsonProperty("NMC_Quantity")
    public void setNMCQuantity(Double nMCQuantity) {
        this.nMCQuantity = nMCQuantity;
    }

    @JsonProperty("SFP_Plus_Quantity")
    public Double getSFPPlusQuantity() {
        return sFPPlusQuantity;
    }

    @JsonProperty("SFP_Plus_Quantity")
    public void setSFPPlusQuantity(Double sFPPlusQuantity) {
        this.sFPPlusQuantity = sFPPlusQuantity;
    }

    @JsonProperty("HA_License")
    public String getHALicense() {
        return hALicense;
    }

    @JsonProperty("HA_License")
    public void setHALicense(String hALicense) {
        this.hALicense = hALicense;
    }

    @JsonProperty("Add_On_Licenses")
    public String getAddOnLicenses() {
        return addOnLicenses;
    }

    @JsonProperty("Add_On_Licenses")
    public void setAddOnLicenses(String addOnLicenses) {
        this.addOnLicenses = addOnLicenses;
    }

    @JsonProperty("Underlay")
    public String getUnderlay() {
        return underlay;
    }

    @JsonProperty("Underlay")
    public void setUnderlay(String underlay) {
        this.underlay = underlay;
    }

    @JsonProperty("License_Supply_Type")
    public String getLicenseSupplyType() {
        return licenseSupplyType;
    }

    @JsonProperty("License_Supply_Type")
    public void setLicenseSupplyType(String licenseSupplyType) {
        this.licenseSupplyType = licenseSupplyType;
    }

    @JsonProperty("Markup_pct")
    public Double getMarkupPct() {
        return markupPct;
    }

    @JsonProperty("Markup_pct")
    public void setMarkupPct(Double markupPct) {
        this.markupPct = markupPct;
    }

    @JsonProperty("CPE_HW_charges_cost")
    public Double getCPEHWChargesCost() {
        return cPEHWChargesCost;
    }

    @JsonProperty("CPE_HW_charges_cost")
    public void setCPEHWChargesCost(Double cPEHWChargesCost) {
        this.cPEHWChargesCost = cPEHWChargesCost;
    }

    @JsonProperty("CPE_customs_tax_cost")
    public Double getCPECustomsTaxCost() {
        return cPECustomsTaxCost;
    }

    @JsonProperty("CPE_customs_tax_cost")
    public void setCPECustomsTaxCost(Double cPECustomsTaxCost) {
        this.cPECustomsTaxCost = cPECustomsTaxCost;
    }

    @JsonProperty("CPE_local_tax_cost")
    public Double getCPELocalTaxCost() {
        return cPELocalTaxCost;
    }

    @JsonProperty("CPE_local_tax_cost")
    public void setCPELocalTaxCost(Double cPELocalTaxCost) {
        this.cPELocalTaxCost = cPELocalTaxCost;
    }

    @JsonProperty("CPE_delivery_cost")
    public Double getCPEDeliveryCost() {
        return cPEDeliveryCost;
    }

    @JsonProperty("CPE_delivery_cost")
    public void setCPEDeliveryCost(Double cPEDeliveryCost) {
        this.cPEDeliveryCost = cPEDeliveryCost;
    }

    @JsonProperty("CPE_maintenance_cost")
    public Double getCPEMaintenanceCost() {
        return cPEMaintenanceCost;
    }

    @JsonProperty("CPE_maintenance_cost")
    public void setCPEMaintenanceCost(Double cPEMaintenanceCost) {
        this.cPEMaintenanceCost = cPEMaintenanceCost;
    }

    @JsonProperty("CPE_installation_charges_cost")
    public Double getCPEInstallationChargesCost() {
        return cPEInstallationChargesCost;
    }

    @JsonProperty("CPE_installation_charges_cost")
    public void setCPEInstallationChargesCost(Double cPEInstallationChargesCost) {
        this.cPEInstallationChargesCost = cPEInstallationChargesCost;
    }

    @JsonProperty("Total_CPE_Cost")
    public Double getTotalCPECost() {
        return totalCPECost;
    }

    @JsonProperty("Total_CPE_Cost")
    public void setTotalCPECost(Double totalCPECost) {
        this.totalCPECost = totalCPECost;
    }

    @JsonProperty("license_mrc_cost")
    public Double getLicenseMrcCost() {
        return licenseMrcCost;
    }

    @JsonProperty("license_mrc_cost")
    public void setLicenseMrcCost(Double licenseMrcCost) {
        this.licenseMrcCost = licenseMrcCost;
    }

    @JsonProperty("license_arc_cost")
    public Double getLicenseArcCost() {
        return licenseArcCost;
    }

    @JsonProperty("license_arc_cost")
    public void setLicenseArcCost(Double licenseArcCost) {
        this.licenseArcCost = licenseArcCost;
    }

    @JsonProperty("license_nrc_cost")
    public Double getLicenseNrcCost() {
        return licenseNrcCost;
    }

    @JsonProperty("license_nrc_cost")
    public void setLicenseNrcCost(Double licenseNrcCost) {
        this.licenseNrcCost = licenseNrcCost;
    }

    @JsonProperty("CPE_UnitCost_USD")
    public Double getCPEUnitCostUSD() {
        return cPEUnitCostUSD;
    }

    @JsonProperty("CPE_UnitCost_USD")
    public void setCPEUnitCostUSD(Double cPEUnitCostUSD) {
        this.cPEUnitCostUSD = cPEUnitCostUSD;
    }

    @JsonProperty("CPE_Cost")
    public Double getCPECost() {
        return cPECost;
    }

    @JsonProperty("CPE_Cost")
    public void setCPECost(Double cPECost) {
        this.cPECost = cPECost;
    }

    @JsonProperty("SFP_UnitCost_USD")
    public Double getSFPUnitCostUSD() {
        return sFPUnitCostUSD;
    }

    @JsonProperty("SFP_UnitCost_USD")
    public void setSFPUnitCostUSD(Double sFPUnitCostUSD) {
        this.sFPUnitCostUSD = sFPUnitCostUSD;
    }

    @JsonProperty("SFP_Cost")
    public Double getSFPCost() {
        return sFPCost;
    }

    @JsonProperty("SFP_Cost")
    public void setSFPCost(Double sFPCost) {
        this.sFPCost = sFPCost;
    }

    @JsonProperty("SFP_Plus_UnitCost_USD")
    public Double getSFPPlusUnitCostUSD() {
        return sFPPlusUnitCostUSD;
    }

    @JsonProperty("SFP_Plus_UnitCost_USD")
    public void setSFPPlusUnitCostUSD(Double sFPPlusUnitCostUSD) {
        this.sFPPlusUnitCostUSD = sFPPlusUnitCostUSD;
    }

    @JsonProperty("SFP_Plus_Cost")
    public Double getSFPPlusCost() {
        return sFPPlusCost;
    }

    @JsonProperty("SFP_Plus_Cost")
    public void setSFPPlusCost(Double sFPPlusCost) {
        this.sFPPlusCost = sFPPlusCost;
    }

    @JsonProperty("NMC_UnitCost_USD")
    public Double getNMCUnitCostUSD() {
        return nMCUnitCostUSD;
    }

    @JsonProperty("NMC_UnitCost_USD")
    public void setNMCUnitCostUSD(Double nMCUnitCostUSD) {
        this.nMCUnitCostUSD = nMCUnitCostUSD;
    }

    @JsonProperty("NMC_Cost")
    public Double getNMCCost() {
        return nMCCost;
    }

    @JsonProperty("NMC_Cost")
    public void setNMCCost(Double nMCCost) {
        this.nMCCost = nMCCost;
    }

    @JsonProperty("Rack_Mount_UnitCost_USD")
    public Double getRackMountUnitCostUSD() {
        return rackMountUnitCostUSD;
    }

    @JsonProperty("Rack_Mount_UnitCost_USD")
    public void setRackMountUnitCostUSD(Double rackMountUnitCostUSD) {
        this.rackMountUnitCostUSD = rackMountUnitCostUSD;
    }

    @JsonProperty("Power_Cord_UnitCost_USD")
    public Double getPowerCordUnitCostUSD() {
        return powerCordUnitCostUSD;
    }

    @JsonProperty("Power_Cord_UnitCost_USD")
    public void setPowerCordUnitCostUSD(Double powerCordUnitCostUSD) {
        this.powerCordUnitCostUSD = powerCordUnitCostUSD;
    }

    @JsonProperty("CPE_Customs_Fee_pct")
    public Double getCPECustomsFeePct() {
        return cPECustomsFeePct;
    }

    @JsonProperty("CPE_Customs_Fee_pct")
    public void setCPECustomsFeePct(Double cPECustomsFeePct) {
        this.cPECustomsFeePct = cPECustomsFeePct;
    }

    @JsonProperty("CPE_Local_Fee_pct")
    public Double getCPELocalFeePct() {
        return cPELocalFeePct;
    }

    @JsonProperty("CPE_Local_Fee_pct")
    public void setCPELocalFeePct(Double cPELocalFeePct) {
        this.cPELocalFeePct = cPELocalFeePct;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

	public Double getCpeArc() {
		return cpeArc;
	}

	public void setCpeArc(Double cpeArc) {
		this.cpeArc = cpeArc;
	}

	public Double getCpeNrc() {
		return cpeNrc;
	}

	public void setCpeNrc(Double cpeNrc) {
		this.cpeNrc = cpeNrc;
	}
    
    }

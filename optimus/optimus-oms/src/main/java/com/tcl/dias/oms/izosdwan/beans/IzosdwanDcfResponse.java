
package com.tcl.dias.oms.izosdwan.beans;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
/**
 * 
 * This file contains the IzosdwanDcfResponse.java class.
 * 
 *
 * @author AnandhiV
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "markup_pct",
    "opportunity_term",
    "cpe_supply_type",
    "cost_license_mrc",
    "cost_cpe_nrc",
    "port_arc",
    "port_nrc",
    "lm_arc",
    "lm_nrc",
    "margin_landed_cost",
    "margin_gross_revenue",
    "margin_total_revenue",
    "margin_license_fee",
    "margin_management_charges",
    "margin_overhead_charges",
    "margin_total_cost",
    "margin_net_revenue",
    "margin_net_margin_pct",
    "year_1",
    "year_2",
    "year_3",
    "year_4",
    "year_5",
    "payback_months",
    "Bucket_Adjustment_Type",
    "total"
})
public class IzosdwanDcfResponse {

    @JsonProperty("markup_pct")
    private Double markupPct;
    @JsonProperty("opportunity_term")
    private Integer opportunityTerm;
    @JsonProperty("cpe_supply_type")
    private String cpeSupplyType;
    @JsonProperty("cost_license_mrc")
    private Integer costLicenseMrc;
    @JsonProperty("cost_cpe_nrc")
    private Integer costCpeNrc;
    @JsonProperty("port_arc")
    private Integer portArc;
    @JsonProperty("port_nrc")
    private Integer portNrc;
    @JsonProperty("lm_arc")
    private Integer lmArc;
    @JsonProperty("lm_nrc")
    private Integer lmNrc;
    @JsonProperty("margin_landed_cost")
    private Integer marginLandedCost;
    @JsonProperty("margin_gross_revenue")
    private Double marginGrossRevenue;
    @JsonProperty("margin_total_revenue")
    private Double marginTotalRevenue;
    @JsonProperty("margin_license_fee")
    private Double marginLicenseFee;
    @JsonProperty("margin_management_charges")
    private Double marginManagementCharges;
    @JsonProperty("margin_overhead_charges")
    private Double marginOverheadCharges;
    @JsonProperty("margin_total_cost")
    private Double marginTotalCost;
    @JsonProperty("margin_net_revenue")
    private Double marginNetRevenue;
    @JsonProperty("margin_net_margin_pct")
    private Double marginNetMarginPct;
    @JsonProperty("year_1")
    private Year1 year1;
    @JsonProperty("year_2")
    private Year2 year2;
    @JsonProperty("year_3")
    private Year3 year3;
    @JsonProperty("year_4")
    private Year4 year4;
    @JsonProperty("year_5")
    private Year5 year5;
    @JsonProperty("payback_months")
    private Integer paybackMonths;
    @JsonProperty("Bucket_Adjustment_Type")
    private String bucketAdjustmentType;
    @JsonProperty("total")
    private Total total;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("markup_pct")
    public Double getMarkupPct() {
        return markupPct;
    }

    @JsonProperty("markup_pct")
    public void setMarkupPct(Double markupPct) {
        this.markupPct = markupPct;
    }

    @JsonProperty("opportunity_term")
    public Integer getOpportunityTerm() {
        return opportunityTerm;
    }

    @JsonProperty("opportunity_term")
    public void setOpportunityTerm(Integer opportunityTerm) {
        this.opportunityTerm = opportunityTerm;
    }

    @JsonProperty("cpe_supply_type")
    public String getCpeSupplyType() {
        return cpeSupplyType;
    }

    @JsonProperty("cpe_supply_type")
    public void setCpeSupplyType(String cpeSupplyType) {
        this.cpeSupplyType = cpeSupplyType;
    }

    @JsonProperty("cost_license_mrc")
    public Integer getCostLicenseMrc() {
        return costLicenseMrc;
    }

    @JsonProperty("cost_license_mrc")
    public void setCostLicenseMrc(Integer costLicenseMrc) {
        this.costLicenseMrc = costLicenseMrc;
    }

    @JsonProperty("cost_cpe_nrc")
    public Integer getCostCpeNrc() {
        return costCpeNrc;
    }

    @JsonProperty("cost_cpe_nrc")
    public void setCostCpeNrc(Integer costCpeNrc) {
        this.costCpeNrc = costCpeNrc;
    }

    @JsonProperty("port_arc")
    public Integer getPortArc() {
        return portArc;
    }

    @JsonProperty("port_arc")
    public void setPortArc(Integer portArc) {
        this.portArc = portArc;
    }

    @JsonProperty("port_nrc")
    public Integer getPortNrc() {
        return portNrc;
    }

    @JsonProperty("port_nrc")
    public void setPortNrc(Integer portNrc) {
        this.portNrc = portNrc;
    }

    @JsonProperty("lm_arc")
    public Integer getLmArc() {
        return lmArc;
    }

    @JsonProperty("lm_arc")
    public void setLmArc(Integer lmArc) {
        this.lmArc = lmArc;
    }

    @JsonProperty("lm_nrc")
    public Integer getLmNrc() {
        return lmNrc;
    }

    @JsonProperty("lm_nrc")
    public void setLmNrc(Integer lmNrc) {
        this.lmNrc = lmNrc;
    }

    @JsonProperty("margin_landed_cost")
    public Integer getMarginLandedCost() {
        return marginLandedCost;
    }

    @JsonProperty("margin_landed_cost")
    public void setMarginLandedCost(Integer marginLandedCost) {
        this.marginLandedCost = marginLandedCost;
    }

    @JsonProperty("margin_gross_revenue")
    public Double getMarginGrossRevenue() {
        return marginGrossRevenue;
    }

    @JsonProperty("margin_gross_revenue")
    public void setMarginGrossRevenue(Double marginGrossRevenue) {
        this.marginGrossRevenue = marginGrossRevenue;
    }

    @JsonProperty("margin_total_revenue")
    public Double getMarginTotalRevenue() {
        return marginTotalRevenue;
    }

    @JsonProperty("margin_total_revenue")
    public void setMarginTotalRevenue(Double marginTotalRevenue) {
        this.marginTotalRevenue = marginTotalRevenue;
    }

    @JsonProperty("margin_license_fee")
    public Double getMarginLicenseFee() {
        return marginLicenseFee;
    }

    @JsonProperty("margin_license_fee")
    public void setMarginLicenseFee(Double marginLicenseFee) {
        this.marginLicenseFee = marginLicenseFee;
    }

    @JsonProperty("margin_management_charges")
    public Double getMarginManagementCharges() {
        return marginManagementCharges;
    }

    @JsonProperty("margin_management_charges")
    public void setMarginManagementCharges(Double marginManagementCharges) {
        this.marginManagementCharges = marginManagementCharges;
    }

    @JsonProperty("margin_overhead_charges")
    public Double getMarginOverheadCharges() {
        return marginOverheadCharges;
    }

    @JsonProperty("margin_overhead_charges")
    public void setMarginOverheadCharges(Double marginOverheadCharges) {
        this.marginOverheadCharges = marginOverheadCharges;
    }

    @JsonProperty("margin_total_cost")
    public Double getMarginTotalCost() {
        return marginTotalCost;
    }

    @JsonProperty("margin_total_cost")
    public void setMarginTotalCost(Double marginTotalCost) {
        this.marginTotalCost = marginTotalCost;
    }

    @JsonProperty("margin_net_revenue")
    public Double getMarginNetRevenue() {
        return marginNetRevenue;
    }

    @JsonProperty("margin_net_revenue")
    public void setMarginNetRevenue(Double marginNetRevenue) {
        this.marginNetRevenue = marginNetRevenue;
    }

    @JsonProperty("margin_net_margin_pct")
    public Double getMarginNetMarginPct() {
        return marginNetMarginPct;
    }

    @JsonProperty("margin_net_margin_pct")
    public void setMarginNetMarginPct(Double marginNetMarginPct) {
        this.marginNetMarginPct = marginNetMarginPct;
    }

    @JsonProperty("year_1")
    public Year1 getYear1() {
        return year1;
    }

    @JsonProperty("year_1")
    public void setYear1(Year1 year1) {
        this.year1 = year1;
    }

    @JsonProperty("year_2")
    public Year2 getYear2() {
        return year2;
    }

    @JsonProperty("year_2")
    public void setYear2(Year2 year2) {
        this.year2 = year2;
    }

    @JsonProperty("year_3")
    public Year3 getYear3() {
        return year3;
    }

    @JsonProperty("year_3")
    public void setYear3(Year3 year3) {
        this.year3 = year3;
    }

    @JsonProperty("year_4")
    public Year4 getYear4() {
        return year4;
    }

    @JsonProperty("year_4")
    public void setYear4(Year4 year4) {
        this.year4 = year4;
    }

    @JsonProperty("year_5")
    public Year5 getYear5() {
        return year5;
    }

    @JsonProperty("year_5")
    public void setYear5(Year5 year5) {
        this.year5 = year5;
    }

    @JsonProperty("payback_months")
    public Integer getPaybackMonths() {
        return paybackMonths;
    }

    @JsonProperty("payback_months")
    public void setPaybackMonths(Integer paybackMonths) {
        this.paybackMonths = paybackMonths;
    }

    @JsonProperty("Bucket_Adjustment_Type")
    public String getBucketAdjustmentType() {
        return bucketAdjustmentType;
    }

    @JsonProperty("Bucket_Adjustment_Type")
    public void setBucketAdjustmentType(String bucketAdjustmentType) {
        this.bucketAdjustmentType = bucketAdjustmentType;
    }

    @JsonProperty("total")
    public Total getTotal() {
        return total;
    }

    @JsonProperty("total")
    public void setTotal(Total total) {
        this.total = total;
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

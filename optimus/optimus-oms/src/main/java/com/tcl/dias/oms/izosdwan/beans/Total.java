
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
 * This file contains the Total.java class.
 * 
 *
 * @author AnandhiV
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "revenue",
    "cost_opex",
    "gross_margin",
    "gross_margin_pct",
    "management_charges",
    "license_fees",
    "overheads",
    "net_margin",
    "net_margin_pct",
    "cash_inflow",
    "external_direct_costs",
    "internal_direct_costs",
    "direct_costs",
    "indirect_costs",
    "cash_outflow",
    "net_cash_flow",
    "disc_net_cash_flow",
    "cum_disc_cash_flow",
    "payback_months"
})
public class Total {

    @JsonProperty("revenue")
    private Double revenue;
    @JsonProperty("cost_opex")
    private Double costOpex;
    @JsonProperty("gross_margin")
    private Double grossMargin;
    @JsonProperty("gross_margin_pct")
    private Double grossMarginPct;
    @JsonProperty("management_charges")
    private Double managementCharges;
    @JsonProperty("license_fees")
    private Double licenseFees;
    @JsonProperty("overheads")
    private Double overheads;
    @JsonProperty("net_margin")
    private Double netMargin;
    @JsonProperty("net_margin_pct")
    private Double netMarginPct;
    @JsonProperty("cash_inflow")
    private Double cashInflow;
    @JsonProperty("external_direct_costs")
    private Double externalDirectCosts;
    @JsonProperty("internal_direct_costs")
    private Double internalDirectCosts;
    @JsonProperty("direct_costs")
    private Double directCosts;
    @JsonProperty("indirect_costs")
    private Double indirectCosts;
    @JsonProperty("cash_outflow")
    private Double cashOutflow;
    @JsonProperty("net_cash_flow")
    private Double netCashFlow;
    @JsonProperty("disc_net_cash_flow")
    private Double discNetCashFlow;
    @JsonProperty("cum_disc_cash_flow")
    private Double cumDiscCashFlow;
    @JsonProperty("payback_months")
    private Double paybackMonths;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("revenue")
    public Double getRevenue() {
        return revenue;
    }

    @JsonProperty("revenue")
    public void setRevenue(Double revenue) {
        this.revenue = revenue;
    }

    @JsonProperty("cost_opex")
    public Double getCostOpex() {
        return costOpex;
    }

    @JsonProperty("cost_opex")
    public void setCostOpex(Double costOpex) {
        this.costOpex = costOpex;
    }

    @JsonProperty("gross_margin")
    public Double getGrossMargin() {
        return grossMargin;
    }

    @JsonProperty("gross_margin")
    public void setGrossMargin(Double grossMargin) {
        this.grossMargin = grossMargin;
    }

    @JsonProperty("gross_margin_pct")
    public Double getGrossMarginPct() {
        return grossMarginPct;
    }

    @JsonProperty("gross_margin_pct")
    public void setGrossMarginPct(Double grossMarginPct) {
        this.grossMarginPct = grossMarginPct;
    }

    @JsonProperty("management_charges")
    public Double getManagementCharges() {
        return managementCharges;
    }

    @JsonProperty("management_charges")
    public void setManagementCharges(Double managementCharges) {
        this.managementCharges = managementCharges;
    }

    @JsonProperty("license_fees")
    public Double getLicenseFees() {
        return licenseFees;
    }

    @JsonProperty("license_fees")
    public void setLicenseFees(Double licenseFees) {
        this.licenseFees = licenseFees;
    }

    @JsonProperty("overheads")
    public Double getOverheads() {
        return overheads;
    }

    @JsonProperty("overheads")
    public void setOverheads(Double overheads) {
        this.overheads = overheads;
    }

    @JsonProperty("net_margin")
    public Double getNetMargin() {
        return netMargin;
    }

    @JsonProperty("net_margin")
    public void setNetMargin(Double netMargin) {
        this.netMargin = netMargin;
    }

    @JsonProperty("net_margin_pct")
    public Double getNetMarginPct() {
        return netMarginPct;
    }

    @JsonProperty("net_margin_pct")
    public void setNetMarginPct(Double netMarginPct) {
        this.netMarginPct = netMarginPct;
    }

    @JsonProperty("cash_inflow")
    public Double getCashInflow() {
        return cashInflow;
    }

    @JsonProperty("cash_inflow")
    public void setCashInflow(Double cashInflow) {
        this.cashInflow = cashInflow;
    }

    @JsonProperty("external_direct_costs")
    public Double getExternalDirectCosts() {
        return externalDirectCosts;
    }

    @JsonProperty("external_direct_costs")
    public void setExternalDirectCosts(Double externalDirectCosts) {
        this.externalDirectCosts = externalDirectCosts;
    }

    @JsonProperty("internal_direct_costs")
    public Double getInternalDirectCosts() {
        return internalDirectCosts;
    }

    @JsonProperty("internal_direct_costs")
    public void setInternalDirectCosts(Double internalDirectCosts) {
        this.internalDirectCosts = internalDirectCosts;
    }

    @JsonProperty("direct_costs")
    public Double getDirectCosts() {
        return directCosts;
    }

    @JsonProperty("direct_costs")
    public void setDirectCosts(Double directCosts) {
        this.directCosts = directCosts;
    }

    @JsonProperty("indirect_costs")
    public Double getIndirectCosts() {
        return indirectCosts;
    }

    @JsonProperty("indirect_costs")
    public void setIndirectCosts(Double indirectCosts) {
        this.indirectCosts = indirectCosts;
    }

    @JsonProperty("cash_outflow")
    public Double getCashOutflow() {
        return cashOutflow;
    }

    @JsonProperty("cash_outflow")
    public void setCashOutflow(Double cashOutflow) {
        this.cashOutflow = cashOutflow;
    }

    @JsonProperty("net_cash_flow")
    public Double getNetCashFlow() {
        return netCashFlow;
    }

    @JsonProperty("net_cash_flow")
    public void setNetCashFlow(Double netCashFlow) {
        this.netCashFlow = netCashFlow;
    }

    @JsonProperty("disc_net_cash_flow")
    public Double getDiscNetCashFlow() {
        return discNetCashFlow;
    }

    @JsonProperty("disc_net_cash_flow")
    public void setDiscNetCashFlow(Double discNetCashFlow) {
        this.discNetCashFlow = discNetCashFlow;
    }

    @JsonProperty("cum_disc_cash_flow")
    public Double getCumDiscCashFlow() {
        return cumDiscCashFlow;
    }

    @JsonProperty("cum_disc_cash_flow")
    public void setCumDiscCashFlow(Double cumDiscCashFlow) {
        this.cumDiscCashFlow = cumDiscCashFlow;
    }

    @JsonProperty("payback_months")
    public Double getPaybackMonths() {
        return paybackMonths;
    }

    @JsonProperty("payback_months")
    public void setPaybackMonths(Double paybackMonths) {
        this.paybackMonths = paybackMonths;
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
